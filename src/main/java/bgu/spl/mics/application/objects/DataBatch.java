package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

public class DataBatch {

    int Size;
    int Ticks; //ticks to process

    public DataBatch(int size,int Ticks){
        Size=size;
        this.Ticks=Ticks;
    }
}
