package bgu.spl.mics;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private final ConcurrentHashMap<MicroService, Deque<Message>> registeredServices;
	private final ConcurrentHashMap<Class<? extends Message>,Deque<MicroService>> SubscribedMircoServiceEvent;
	private final ConcurrentHashMap<Class<? extends Message>,Deque<MicroService>> SubscribedMircoServiceBroadCasts;
	private final ConcurrentHashMap<Event,Future> ActiveFutures;

	private  static  final class InstanceHolder {
		static final MessageBusImpl instance = new MessageBusImpl();
	}
	private MessageBusImpl(){
		registeredServices=new ConcurrentHashMap<>();
		SubscribedMircoServiceBroadCasts=new ConcurrentHashMap<>();
		SubscribedMircoServiceEvent=new ConcurrentHashMap<>();
		ActiveFutures=new ConcurrentHashMap<>();

	}
	public static MessageBusImpl getInstence(){
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
		Queue<MicroService> subbed=SubscribedMircoServiceBroadCasts.get(b.getClass());
		if(subbed!=null)
			for(MicroService service : subbed) {
				synchronized (service) {
					registeredServices.get(service).add(b);
					service.notifyAll();
				}
			}
	}



	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
			Queue<MicroService> subbed = SubscribedMircoServiceEvent.get(e.getClass());
			if (subbed == null || subbed.isEmpty())
				return null;
				Future<T> future = new Future<T>();
				ActiveFutures.put(e, future);
				MicroService m = subbed.poll();
				subbed.add(m);
			synchronized (m) {
				registeredServices.get(m).add(e);
				m.notifyAll();
			}
			return future;
		}



	@Override
	public void register(MicroService m) {
		registeredServices.put(m,new ArrayDeque<>());

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
		if(registeredServices.get(m).isEmpty()){
			synchronized (m) {
				m.wait();
			}
		}
		return registeredServices.get(m).poll();


	}
	public ConcurrentHashMap<Class<? extends Message>,Deque<MicroService>> getSubscribedMircoServiceEvent(){
		return SubscribedMircoServiceEvent;
	}
	public ConcurrentHashMap<Class<? extends Message>,Deque<MicroService>> getSubscribedMircoServiceBroadCasts(){
		return SubscribedMircoServiceBroadCasts;
	}



}
