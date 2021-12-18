package bgu.spl.mics;

import java.util.*;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private final HashMap<MicroService, Queue<Message>> registeredServices;
	private final HashMap<Class<? extends Event>,Queue<MicroService>> SubscribedMircoServiceEvent;
	private final HashMap<Class<? extends Broadcast>,Queue<MicroService>> SubscribedMircoServiceBroadCasts;
	private final HashMap<Class<? extends Event>,Future> ActiveFutures;

	private static final class InstanceHolder {
		static final MessageBusImpl instance = new MessageBusImpl();
	}
	private MessageBusImpl(){
		registeredServices=new HashMap<>();
		SubscribedMircoServiceBroadCasts=new HashMap<>();
		SubscribedMircoServiceEvent=new HashMap<>();
		ActiveFutures=new HashMap<>();
	}
	public MessageBusImpl getInstence(){
		return InstanceHolder.instance;
	}
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		if(SubscribedMircoServiceEvent.containsKey(type)){
			SubscribedMircoServiceEvent.get(type).add(m);
		}
		else{
			synchronized (SubscribedMircoServiceEvent){
			if(!SubscribedMircoServiceEvent.containsKey(type))
				SubscribedMircoServiceEvent.put(type, new ArrayDeque<MicroService>());
			}
				SubscribedMircoServiceEvent.get(type).add(m);

		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if(SubscribedMircoServiceBroadCasts.containsKey(type)){
			SubscribedMircoServiceBroadCasts.get(type).add(m);
		}
		else{
			synchronized (SubscribedMircoServiceBroadCasts) {
				if (!SubscribedMircoServiceBroadCasts.containsKey(type))
					SubscribedMircoServiceBroadCasts.put(type, new ArrayDeque<MicroService>());
			}
			SubscribedMircoServiceBroadCasts.get(type).add(m);
		}
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		Future future=ActiveFutures.get(e);
		future.resolve(result);

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		Queue<MicroService> subbed=SubscribedMircoServiceBroadCasts.get(b);
		for(MicroService service : subbed){
			registeredServices.get(service).add(b);
			service.notify();
		}
	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Queue<MicroService> subbed=SubscribedMircoServiceEvent.get(e);
		if(subbed.isEmpty())
			return null;
		Future<T> future=new Future<T>();
		ActiveFutures.put(e.getClass(),future);
		MicroService m=subbed.poll();
		subbed.add(m);
		registeredServices.get(m).add(e);
		m.notify();
		return future;
	}

	@Override
	public void register(MicroService m) {
		registeredServices.put(m,new PriorityQueue<>());

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
			m.wait();
		}
		return registeredServices.get(m).poll();


	}



}
