package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Messages.PublishConferenceBroadcast;
import bgu.spl.mics.application.Messages.PublishResultsEvent;
import bgu.spl.mics.application.Messages.TickBroadcast;
import bgu.spl.mics.application.Messages.TimeoutBroadCast;
import bgu.spl.mics.application.objects.ConfrenceInformation;
import bgu.spl.mics.application.objects.Model;

import java.util.ArrayList;

/**
 * Conference service is in charge of
 * after publishing results the conference will unregister from the system.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ConferenceService extends MicroService {
    private final ConfrenceInformation confrenceInformation;
    private final ArrayList<Model> results;
    private int ticks;
    public ConferenceService(String name, ConfrenceInformation confrenceInformation) {
        super(name);
        this.confrenceInformation=confrenceInformation;
        results=new ArrayList<>();

    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class,(e)->{
            ticks++;
            if(ticks==confrenceInformation.getDate()) {
                sendBroadcast(new PublishConferenceBroadcast(results));
                System.out.println("Conference Published");
            }
        });
        subscribeEvent(PublishResultsEvent.class,(res)->{
            results.add(res.getResult());
        });
        subscribeBroadcast(TimeoutBroadCast.class, time->{
            terminate();
        });

    }
}
