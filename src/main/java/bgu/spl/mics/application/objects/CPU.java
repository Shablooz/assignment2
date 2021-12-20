package bgu.spl.mics.application.objects;

/**
 * Passive object representing a single CPU.
 *  * Add all the fields described in the assignment as private fields.
 *  * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU implements Comparable {
    private boolean processing;
    private final int cores;
    public CPU(int cores){
        this.cores=cores;
    }

    public void ProcessBatch(){
        DataBatch batch=Cluster.getInstance().getNextBatch(this);
        batch.process();
        int prog=batch.toProcess(cores);
        if(prog==batch.getProcessTicks()){
            batch.finish();
       //     Cluster.getInstance().getNextBatch(this, batch);
        }

    }

    public boolean processing() {
        return processing;
    }

    @Override
    public int compareTo(Object o) {
        return Integer.compare(cores,((CPU) o).getCores());
    }

    public int getCores() {
        return cores;
    }
}
