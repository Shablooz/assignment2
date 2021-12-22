package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.Messages.TrainModelEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU implements Comparable {




    /**
     * Enum representing the type of the GPU.
     */
    public enum Type {RTX3090, RTX2080, GTX1080}
    private Model model=null;
    private final Type type;
    private int inProcessing;
    private final int VRAM;
    private final int trainingTicks;
    private final ArrayList<DataBatch> training;
    private boolean active;
    private boolean noUnprocessedLeft;
    private int ticks;

    public GPU(Type type){
        if(type==Type.RTX3090) {
            VRAM = 32;
            trainingTicks=1;
        }
        else if(type==Type.RTX2080) {
            VRAM = 16;
            trainingTicks=2;
        }
        else {
            VRAM = 8;
            trainingTicks=4;
        }
        this.type=type;
        inProcessing=0;
        training=new ArrayList<>(VRAM);
        ticks=0;
    }
   // public void sendSample(TrainModelEvent modelEvent) {
   //     DataBatch batch=modelEvent.getModel().getNextBatch();
   //     Cluster.getInstance().ProcessBatch(this, batch);
   //     inProcessing++;
   // }
    public int getVRAM(){
        return VRAM;
    }
    public int getTicks(){
        return ticks;
    }
    public boolean getActive(){
        return active;
    }
    public void activate() {
        Cluster.getInstance().SetActiveGPU(this);
        active=true;
    }
    public void deactivate() {
        Cluster.getInstance().SetInactiveGPU(this);
        active=false;
    }

    public boolean getNoUnprocessedLeft() {
        return noUnprocessedLeft;
    }

    public boolean isActive() {
        return active;
    }
    public void setModel(Model model){
        this.model=model;
        noUnprocessedLeft=false;
    }
    public void OnTick(){
        ticks++;
        DataBatch batch;
        Iterator<DataBatch> iterator=training.iterator();
        while(iterator.hasNext()) {   //process batches and get rid of them if done
            batch = iterator.next();
            batch.train();
            if (batch.getTrainingTicks() == trainingTicks) {
                model.addProcessedBatch(batch);
                iterator.remove();
                inProcessing--;
            }
        }
        int spaces=VRAM-inProcessing;
        for(int i=0;i<spaces;i++){ //get new batches to process, if there are any done in cluster
            DataBatch dataBatch=Cluster.getInstance().getProcessedBatch(this);
            if(dataBatch==null)
                break;
            training.add(dataBatch);
            inProcessing++;
        }
    }
    @Override
    public int compareTo(Object o) {
        GPU gpu=(GPU)o;
        if(inProcessing==0)
            return -1;
        else if(gpu.getInProcessing()==0)
            return +1;
        else return Integer.compare(gpu.getVRAM()/gpu.getInProcessing(),VRAM/inProcessing);
    }
    public boolean isFull(){
        return inProcessing<VRAM;
    }
    public int getInProcessing() {
        return inProcessing;
    }
    public DataBatch getBatchToProcess(){
        DataBatch batch= model.getNextBatch();
        if(model.noUnprocessedLeft()) {
            noUnprocessedLeft=true;
        }
        return batch;
    }
    public Model getModel(){
        return model;
    }
}
