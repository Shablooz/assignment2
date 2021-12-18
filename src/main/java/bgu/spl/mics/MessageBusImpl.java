package bgu.spl.mics;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private HashMap<MicroService, Queue<Event>> registeredServices;

	private static final class InstanceHolder {
		static final MessageBusImpl instance = new MessageBusImpl();
	}

	public MessageBusImpl getInstence(){
		return InstanceHolder.instance;
	}
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		m.subscribeEvent(type,(c) -> {}); //TODO

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub

	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void register(MicroService m) {
		registeredServices.put(m,new PriorityQueue<Event>());

	}

	@Override
	public void unregister(MicroService m) {
		registeredServices.remove(m);

	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		if(!registeredServices.containsKey(m)){
			throw new IllegalStateException("MicroService Not Registered");
		}
		while(registeredServices.get(m).isEmpty()){
			m.wait(100);
		}
		return registeredServices.get(m).remove();


	}



}
