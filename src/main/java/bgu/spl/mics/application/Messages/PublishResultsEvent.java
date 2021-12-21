package bgu.spl.mics.application.Messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.Model;

public class PublishResultsEvent implements Event {
    private final Model result;
    public PublishResultsEvent(Model model){
        result=model;
    }

    public Model getResult() {
        return result;
    }
}
