package bgu.spl.mics.application.services;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Messages.*;
import bgu.spl.mics.application.objects.*;

import java.util.ArrayDeque;

/**
 * GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {

    private GPU gpu;
    private Object waitGPU;
    private ArrayDeque<TrainModelEvent> toProcess;
    public GPUService(String name,GPU gpu,Object waitObj) {
        super(name);
        this.waitGPU=waitObj;
        this.gpu=gpu;
        toProcess=new ArrayDeque<>();
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class,(tick)->{
            if(!toProcess.isEmpty()) {
                if(!gpu.isActive() && !gpu.getNoUnprocessedLeft()) {
                    gpu.activate(toProcess.peekFirst().getModel());
                }
                TrainModelEvent e=toProcess.peekFirst();
                gpu.OnTick();
                if(e.done()){
                    complete(e,e.getModel());
                    toProcess.removeFirst();
                    gpu.deactivate();
                }
            }



        });
        subscribeBroadcast(TimeoutBroadCast.class,time->{
            for(TrainModelEvent modelEvent: toProcess) {
                complete(modelEvent, modelEvent.getModel());
            }
            terminate();
        });
        subscribeEvent(TrainModelEvent.class,model -> {
            toProcess.addLast(model);
        });
        subscribeEvent(TestModelEvent.class,test -> {
            complete(test,test.getModel().Test());
        });
        synchronized (waitGPU) {
            waitGPU.notifyAll();
        }

    }

}
