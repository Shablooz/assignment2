package bgu.spl.mics.application.Messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.Model;

public class TrainModelEvent implements Event {

    private final Model model;
    public TrainModelEvent(Model model){
        this.model=model;
    }
    public int getSize(){
        return model.getData().getSize();
    }
    public Data.Type getType(){
        return model.getData().getType();
    }

    public Model getModel() {
        return model;
    }
}
