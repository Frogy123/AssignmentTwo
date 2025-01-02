package bgu.spl.mics;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only one public method (in addition to getters which can be public solely for unit testing) may be added to this class
 * All other methods and members you add the class must be private.
 */
public class MessageBusImpl implements MessageBus {
	//note: ConcurrentHashMap are thread safe and none blocking
	private final ConcurrentHashMap<MicroService,RegisteredMicroService> regTable; // keys - MicroService, value - their Message Queues
	private final ConcurrentHashMap<Class<? extends Event<?>>, Queue<RegisteredMicroService> > eventSubsTable; // keys - event types, value - Queue of queus of the ms subscribed to this kind of events
	private final ConcurrentHashMap<Class<? extends Broadcast>, Queue<RegisteredMicroService> > broadcastSubsTable; // keys - event types, value - List of queus of the ms subscribed to this kind of events
	private final ConcurrentHashMap<Event<?>,Future<?>> eventToFuture;


	private class RegisteredMicroService{

		private Queue< Class<? extends Event<?>> > eventSubs;
		private Queue< Class<? extends Broadcast> > broadcastSubs;
		private BlockingQueue<Message> myMessageQueue;

		private RegisteredMicroService() {
			eventSubs = new ConcurrentLinkedQueue<>();
			broadcastSubs = new ConcurrentLinkedQueue<>();
			myMessageQueue = new LinkedBlockingQueue<>();
		}

	}



	// SINGLETON:

	private static class MessageBusHolder{
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

	// SINGLETON;


	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		RegisteredMicroService rm = regTable.get(m);

		// add rm to the group of registerd micro services subscribed to events of type

		eventSubsTable.putIfAbsent(type, new LinkedList<>());
		Queue<RegisteredMicroService> q = eventSubsTable.get(type);
		q.add(rm);



		// add type as a event subscribition of mine
		rm.eventSubs.add(type);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {

		RegisteredMicroService rm = regTable.get(m);

		// add rm to the group of registerd micro services subscribed to broadcast of type
		Queue<RegisteredMicroService> lst = broadcastSubsTable.putIfAbsent(type, new ConcurrentLinkedQueue<>());
		lst.add(rm);

		// add type as a broadcast subscribition of mine
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
		for(RegisteredMicroService rm: broadcastSubsTable.get(b.getClass())) { // concurent queue iterator -> snapshot approach
			rm.myMessageQueue.add(b);
		}
	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Queue<RegisteredMicroService> subs = eventSubsTable.get(e.getClass());
		RegisteredMicroService chosenRm = subs.remove(); // remove the first rm in the Queue
		chosenRm.myMessageQueue.add(e);
		subs.add(chosenRm); // insert it as last

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
		for(Class<? extends Event<?>> type: toUnreg.eventSubs)  // concurent queue iterator -> snapshot approach
			eventSubsTable.get(type).remove(toUnreg);

		for(Class<? extends Broadcast> type: toUnreg.broadcastSubs)  // concurent queue iterator -> snapshot approach
			broadcastSubsTable.get(type).remove(toUnreg);

		//question: what to do with the message that are in m messageQueue?

	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		RegisteredMicroService rm = regTable.get(m);
		return rm.myMessageQueue.take(); // will wait if empty
	}

}
