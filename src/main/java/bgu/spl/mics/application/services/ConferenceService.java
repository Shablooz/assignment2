package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Messages.PublishConferenceBroadcast;
import bgu.spl.mics.application.Messages.PublishResultsEvent;
import bgu.spl.mics.application.Messages.TickBroadcast;
import bgu.spl.mics.application.objects.ConfrenceInformation;
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
    private int ticks;
    public ConferenceService(String name, ConfrenceInformation confrenceInformation) {
        super(name);
        this.confrenceInformation=confrenceInformation;

    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class,(e)->{
            ticks++;
            if(ticks==confrenceInformation.getDate())
                sendBroadcast(new PublishConferenceBroadcast());
        });

    }
}
