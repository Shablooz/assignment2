package bgu.spl.mics.application.objects;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {
    /**
     * Enum representing the type of the GPU.
     */
    enum Type {RTX3090, RTX2080, GTX1080}

    private final Type type;

    private final int VRAM;
    private Cluster cluster;

    public GPU(Type type, Cluster cluster){
        this.cluster = cluster;
        if(type==Type.RTX3090)
            VRAM=32;
        else if(type==Type.RTX2080)
            VRAM=16;
        else VRAM=8;
        this.type=type;
    }
}
