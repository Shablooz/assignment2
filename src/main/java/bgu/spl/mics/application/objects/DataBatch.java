package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

public class DataBatch {

    private int size;
    private int ticks; //ticks to process
    private int processTicks;
    private boolean processed;

    public DataBatch(int size,int Ticks){
        size=size;
        this.ticks=Ticks;
        processed=false;
        processTicks=0;
    }

    public boolean isProcessed() {
        return processed;
    }
    public int toProcess(int cores){
        return (32/cores)*ticks;
    }
    public void process(){
        processTicks++;
    }
    public int getProcessTicks() {
        return processTicks;
    }
    public void finish(){
        processed=true;
    }

}
