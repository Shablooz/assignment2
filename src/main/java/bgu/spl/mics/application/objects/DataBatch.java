package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

public class DataBatch {

    private int size;
    private int ticks; //ticks to process
    private boolean processed;

    public DataBatch(int size,int Ticks){
        size=size;
        this.ticks=Ticks;
        processed=false;
    }
    public void Process(int cores){
        try {
            wait((32 / cores) * ticks);
        }
        catch (InterruptedException e){

        }
        processed=true;
    }

    public boolean isProcessed() {
        return processed;
    }
    public int toProcess(int cores){
        return (32/cores)*ticks;
    }
}
