package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Messages.TickBroadcast;
import bgu.spl.mics.application.Messages.TimeoutBroadCast;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService{

	final int duration;
	final int speed;
	final Object stopEvent;
	public TimeService(int speed,int duration,Object stopEvent) {
		super("Time_Service");
		this.duration=duration;
		this.speed=speed;
		this.stopEvent=stopEvent;
	}

	@Override
	protected synchronized void initialize() {
		for(int i=0;i<duration;i++) {
			sendBroadcast(new TickBroadcast());
			try {
					wait(speed);
			}
			catch (InterruptedException ignore){}
		}
		sendBroadcast(new TimeoutBroadCast());
		synchronized (stopEvent) {
			stopEvent.notifyAll();
			terminate();
		}
	}
}
