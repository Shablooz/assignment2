package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Messages.*;
import bgu.spl.mics.application.objects.*;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 * in addition to sending the {@link DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {

    GPU gpu;
    private ArrayDeque<TrainModelEvent> toProcess;
    public GPUService(String name,GPU gpu) {
        super(name);
        this.gpu=gpu;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class,(tick)->{
            gpu.sendSample(toProcess.getFirst());
        });
        subscribeEvent(TrainModelEvent.class,model -> {
            toProcess.addLast(model);
        });
    }

}
