package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.Messages.TrainModelEvent;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {

    /**
     * Enum representing the type of the GPU.
     */
    public enum Type {RTX3090, RTX2080, GTX1080}

    private final Type type;

    private final int VRAM;
    private static Cluster cluster;

    public GPU(Type type){
        if(type==Type.RTX3090)
            VRAM=32;
        else if(type==Type.RTX2080)
            VRAM=16;
        else VRAM=8;
        this.type=type;
    }
    public void sendSample(TrainModelEvent modelEvent) {
        DataBatch batch=modelEvent.getModel().getNextBatch();
        cluster.getInstance().ProcessBatch(this, batch);
        modelEvent.getModel().addProcessedBatch(batch);
    }
    public int getVRAM(){
        return VRAM;
    }

}
