package bgu.spl.mics.application.Messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.Model;

import java.util.ArrayList;

public class PublishConferenceBroadcast implements Broadcast {
    private final ArrayList<Model> results;
    public PublishConferenceBroadcast(ArrayList<Model> models){
        results=models;
    }

    public ArrayList<Model> getResults() {
        return results;
    }
}
