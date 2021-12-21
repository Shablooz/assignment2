package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

public class DataBatch {

    private final int size;
    private final int ticks; //ticks to process
    private int processTicks;
    private boolean processed;
    private int trainingTicks;

    public DataBatch(int size,int Ticks){
        this.size=size;
        this.ticks=Ticks;
        processed=false;
        processTicks=0;
        trainingTicks=0;
    }

    public int getSize() {
        return size;
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
    //public int
    public void finish(){
        processed=true;
    }
    public int getTrainingTicks(){
        return trainingTicks;
    }
    public void train(){
        trainingTicks++;
    }

}
