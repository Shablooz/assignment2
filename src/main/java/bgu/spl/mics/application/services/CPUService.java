package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Messages.TickBroadcast;
import bgu.spl.mics.application.Messages.TimeoutBroadCast;
import bgu.spl.mics.application.objects.CPU;

/**
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class CPUService extends MicroService {
    private boolean processing;
    CPU cpu;

    public CPUService(String name, CPU cpu) {
        super(name);
        this.cpu = cpu;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, (e) -> {
            if (cpu.busy()) {
                cpu.ProcessBatch();
                cpu.tick();
            } else if (cpu.ProcessNextBatch()) {
                cpu.tick();
                cpu.addBatchesProcessed();
            }

        });
        subscribeBroadcast(TimeoutBroadCast.class, time->{
            terminate();
        });
    }
}
