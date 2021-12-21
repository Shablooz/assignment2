package bgu.spl.mics.application.objects;

import com.sun.jmx.remote.internal.ArrayQueue;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Data {
    /**
     * Enum representing the Data type.
     */
    public enum Type {
        Images, Text, Tabular
    }

    private Type type;
    private int processed;
    private int size;
    private int ticks; //to process batch (independent of cpu, final amount of ticks depends on cores)
    private ArrayDeque<DataBatch> UnprocessedBatches;
    private ArrayList<DataBatch> ProcessedBatches;

    public Data(Type type,int size){
        UnprocessedBatches=new ArrayDeque<>();
        ProcessedBatches=new ArrayList();
        switch (type){
            case Text:ticks=2; break;
            case Images:ticks=4; break;
            case Tabular:ticks=1; break;

        }
        this.type=type;
        this.size=size;
        processed=0;
        for(int i=0;i<size/1000;i++)
            UnprocessedBatches.addFirst(new DataBatch(1000,ticks));
        if(ticks%1000!=0)
            UnprocessedBatches.addFirst(new DataBatch(size%1000,ticks)); //left over batch of size <=1000

    }
    public boolean UnprocessedIsEmpty(){
        return UnprocessedBatches.isEmpty();
    }
    public int getSize() {
        return size;
    }

    public Type getType() {
        return type;
    }

    public int GetUnprocessed(){
        return size-processed;
    }

    public int getProcessed() {
        return processed;
    }
    public int toProcess(int cores){
        return (32/cores)*ticks;
    }

    public DataBatch getDataBatchToProcess() {
        return UnprocessedBatches.removeFirst();
    }
    public void addProcessed(DataBatch batch){
        ProcessedBatches.add(batch);
        processed+=batch.getSize();
    }
    public boolean processed(){
        return processed==size;
    }

}
