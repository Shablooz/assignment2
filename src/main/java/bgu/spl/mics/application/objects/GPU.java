package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.Messages.TrainModelEvent;

import java.util.ArrayList;
import java.util.Comparator;

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
    private static ArrayList<DataBatch> training;
    private boolean active;

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
    }
   // public void sendSample(TrainModelEvent modelEvent) {
   //     DataBatch batch=modelEvent.getModel().getNextBatch();
   //     Cluster.getInstance().ProcessBatch(this, batch);
   //     inProcessing++;
   // }
    public int getVRAM(){
        return VRAM;
    }
    public void activate() {
        Cluster.getInstance().SetActiveGPU(this);
        active=true;
    }
    public void deactivate() {
        Cluster.getInstance().SetInactiveGPU(this);
        active=false;
    }

    public boolean isActive() {
        return active;
    }
    public void setModel(Model model){
        this.model=model;
    }
    public void OnTick(){
        for(DataBatch dataBatch: training) { //process batches and get rid of them if done
            dataBatch.train();
            if (dataBatch.getTrainingTicks() == trainingTicks) {
                model.addProcessedBatch(dataBatch);
                training.remove(dataBatch);
                inProcessing--;
            }
        }
        int spaces=VRAM-inProcessing;
        for(int i=0;i<spaces;i++){ //get new batches to process, if there are any done in cluster
            DataBatch batch=Cluster.getInstance().getProcessedBatch(this);
            if(batch==null)
                break;
            training.add(batch);
            inProcessing++;
        }
    }
    @Override
    public int compareTo(Object o) {
        GPU gpu=(GPU)o;
        return Integer.compare(VRAM,gpu.getVRAM());
    }
    public boolean isFull(){
        return inProcessing<VRAM;
    }
    public int getInProcessing() {
        return inProcessing;
    }
    public DataBatch getBatchToProcess(){
        return model.getNextBatch();
    }
}
