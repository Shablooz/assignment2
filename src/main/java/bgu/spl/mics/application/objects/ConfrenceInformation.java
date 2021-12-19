package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Passive object representing information on a conference.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class ConfrenceInformation {

    private String name;
    private int date;
    private List<String> modelsPublished;

    public ConfrenceInformation(String name, int date){
        this.name = name;
        this.date = date;
        modelsPublished = new ArrayList<String>();
    }
    public String getName(){
        return name;
    }
    public List<String> getModelsPublished(){
        return  modelsPublished;
    }
    public void publish(Model model){
        modelsPublished.add(model.getName());
    }
}
