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
		private final Queue<Class<? extends Event<?>>> eventSubs;
		private final Queue<Class<? extends Broadcast>> broadcastSubs;
		private final BlockingQueue<Message> myMessageQueue;

		private RegisteredMicroService() {
			eventSubs = new ConcurrentLinkedQueue<>();
			broadcastSubs = new ConcurrentLinkedQueue<>();
			myMessageQueue = new LinkedBlockingQueue<>();
		}
	}

	// SINGLETON:

	private static class MessageBusHolder {
		private static final MessageBus messageBus = new MessageBusImpl();
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

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		RegisteredMicroService rm = regTable.get(m);
		// add rm to the group of registered microservices subscribed to broadcast of type
		broadcastSubsTable.putIfAbsent(type, new ConcurrentLinkedQueue<>());
		Queue<RegisteredMicroService> lst = broadcastSubsTable.get(type);
		synchronized (type) {
			lst.add(rm);
		}
		// add type as a broadcast subscription of mine
		rm.broadcastSubs.add(type);
			}

	@Override
	public <T> void complete(Event<T> e, T result) {
		Future<T> fut = (Future<T>) eventToFuture.get(e);
		fut.resolve(result);
		fut.isDone();
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		for (RegisteredMicroService rm : broadcastSubsTable.get(b.getClass())) {
			rm.myMessageQueue.add(b);
		}

			}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Queue<RegisteredMicroService> subs = eventSubsTable.get(e.getClass());
		synchronized(e.getClass()) {
			RegisteredMicroService chosenRm = subs.remove(); // remove the first rm in the Queue
			synchronized (chosenRm) {
				chosenRm.myMessageQueue.add(e);
			}

			subs.add(chosenRm); // insert it as last
		}


		Future<T> fut = new Future<>();
		eventToFuture.put(e, fut);

		return fut;
	}

	@Override
	public void register(MicroService m) {
		regTable.put(m, new RegisteredMicroService());
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

			}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		RegisteredMicroService rm = regTable.get(m);
		return rm.myMessageQueue.take(); // will wait if empty
	}

	// Queries for testing:
	public Boolean isRegistered(MicroService m) {
		return regTable.containsKey(m);
	}

	public Boolean isSubscribedToBroadcast(Class<? extends Broadcast> type, MicroService m) {
		RegisteredMicroService rm = regTable.get(m);
				return broadcastSubsTable.get(type).contains(rm) && rm.broadcastSubs.contains(type);
	}

	public <T> Boolean isSubscribedToEvent(Class<? extends Event<T>> type, MicroService m) {
		RegisteredMicroService rm = regTable.get(m);
				return eventSubsTable.get(type).contains(rm) && rm.eventSubs.contains(type);
	}
}
