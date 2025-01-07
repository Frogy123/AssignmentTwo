package bgu.spl.mics;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class MessageBusImpl implements MessageBus {
	//note: ConcurrentHashMap are thread safe and none blocking
	private final ConcurrentHashMap<MicroService, RegisteredMicroService> regTable;
	private final ConcurrentHashMap<Class<? extends Event<?>>, Queue<RegisteredMicroService>> eventSubsTable;
	private final ConcurrentHashMap<Class<? extends Broadcast>, Queue<RegisteredMicroService>> broadcastSubsTable;
	private final ConcurrentHashMap<Event<?>, Future<?>> eventToFuture;


	private class RegisteredMicroService {
		private Queue<Class<? extends Event<?>>> eventSubs;
		private Queue<Class<? extends Broadcast>> broadcastSubs;
		private BlockingQueue<Message> myMessageQueue;

		private RegisteredMicroService() {
			eventSubs = new ConcurrentLinkedQueue<>();
			broadcastSubs = new ConcurrentLinkedQueue<>();
			myMessageQueue = new LinkedBlockingQueue<>();
		}
	}

	// SINGLETON:

	private static class MessageBusHolder {
		private static MessageBus messageBus = new MessageBusImpl();
	}

	private MessageBusImpl() {
		regTable = new ConcurrentHashMap<>();
		eventSubsTable = new ConcurrentHashMap<>();
		broadcastSubsTable = new ConcurrentHashMap<>();
		eventToFuture = new ConcurrentHashMap<>();
	}

	public static MessageBus getInstance() {
		return MessageBusHolder.messageBus;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		RegisteredMicroService rm = regTable.get(m);

		// add rm to the group of registered microservices subscribed to events of type
		eventSubsTable.putIfAbsent(type, new ConcurrentLinkedQueue<>());
		Queue<RegisteredMicroService> q = eventSubsTable.get(type);
		q.add(rm);

		// add type as an event subscription of mine
		synchronized (rm) {
			rm.eventSubs.add(type);
		}

		System.out.println("DEBUG: " + m.getName() + " subscribed to Event " + type.getSimpleName());
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		System.out.println("DEBUG: " + m.getName() + " subscribing to Broadcast " + type.getSimpleName());
		RegisteredMicroService rm = regTable.get(m);
		// add rm to the group of registered microservices subscribed to broadcast of type
		broadcastSubsTable.putIfAbsent(type, new ConcurrentLinkedQueue<>());
		Queue<RegisteredMicroService> lst = broadcastSubsTable.get(type);
		lst.add(rm);
		// add type as a broadcast subscription of mine
		rm.broadcastSubs.add(type);
		System.out.println("DEBUG: " + m.getName() + " subscribed to Broadcast " + type.getSimpleName());
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		Future<T> fut = (Future<T>) eventToFuture.get(e);
		fut.resolve(result);
		fut.isDone();

		System.out.println("DEBUG: Event " + e.getClass().getSimpleName() + " completed with result: " + result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		for (RegisteredMicroService rm : broadcastSubsTable.get(b.getClass())) {
			rm.myMessageQueue.add(b);
		}

		System.out.println("DEBUG: Broadcast " + b.getClass().getSimpleName() + " sent to subscribers.");
	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Queue<RegisteredMicroService> subs = eventSubsTable.get(e.getClass());
		RegisteredMicroService chosenRm = subs.remove();

		subs.add(chosenRm);

		Future<T> fut = new Future<>();
		eventToFuture.put(e, fut);

		System.out.println("DEBUG: Event " + e.getClass().getSimpleName() + " sent to " + chosenRm);
		return fut;
	}

	@Override
	public void register(MicroService m) {
		regTable.put(m, new RegisteredMicroService());
		System.out.println("DEBUG: MicroService " + m.getName() + " registered.");
	}

	@Override
	public void unregister(MicroService m) {
		RegisteredMicroService toUnreg = regTable.remove(m);
		for (Class<? extends Event<?>> type : toUnreg.eventSubs) {
			eventSubsTable.get(type).remove(toUnreg);
		}

		for (Class<? extends Broadcast> type : toUnreg.broadcastSubs) {
			broadcastSubsTable.get(type).remove(toUnreg);
		}

		System.out.println("DEBUG: MicroService " + m.getName() + " unregistered.");
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		RegisteredMicroService rm = regTable.get(m);
		System.out.println("DEBUG: MicroService " + m.getName() + " awaiting message.");
		return rm.myMessageQueue.take(); // will wait if empty
	}

	// Queries for testing:
	public Boolean isRegistered(MicroService m) {
		return regTable.containsKey(m);
	}

	public Boolean isSubscribedToBroadcast(Class<? extends Broadcast> type, MicroService m) {
		RegisteredMicroService rm = regTable.get(m);
		System.out.println("DEBUG: Checking if MicroService " + m.getName() + " is subscribed to Broadcast " + type.getSimpleName());
		return broadcastSubsTable.get(type).contains(rm) && rm.broadcastSubs.contains(type);
	}

	public <T> Boolean isSubscribedToEvent(Class<? extends Event<T>> type, MicroService m) {
		RegisteredMicroService rm = regTable.get(m);
		System.out.println("DEBUG: Checking if MicroService " + m.getName() + " is subscribed to Event " + type.getSimpleName());
		return eventSubsTable.get(type).contains(rm) && rm.eventSubs.contains(type);
	}
}
