package bgu.spl.mics.application.objects;

/**
 * Passive object representing a single CPU.
 *  * Add all the fields described in the assignment as private fields.
 *  * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU implements Comparable {
    private boolean busy;
    private final int cores;
    public CPU(int cores){
        this.cores=cores;
    }

    public void ProcessBatch(){
        DataBatch batch=Cluster.getInstance().ProcessBatch(this);
        batch.process();
        int prog=batch.toProcess(cores);
        if(prog==batch.getProcessTicks()){
            batch.finish();
            busy=false;
            Cluster.getInstance().finishBatch(this);
        }

    }
    public boolean ProcessNextBatch(){
        DataBatch batch=Cluster.getInstance().getNextBatch(this);
        if(batch==null)
            return false;
        batch.process();
        busy=true;
        int prog=batch.toProcess(cores); //batch may take a single tick to process
        if(prog==batch.getProcessTicks()){
            batch.finish();
            busy=false;
            Cluster.getInstance().finishBatch(this);
        }
        return true;
    }

    public boolean busy() {
        return busy;
    }

    @Override
    public int compareTo(Object o) {
        return Integer.compare(cores,((CPU) o).getCores());
    }

    public int getCores() {
        return cores;
    }
}
