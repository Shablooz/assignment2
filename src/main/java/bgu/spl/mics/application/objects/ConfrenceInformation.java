package bgu.spl.mics.application.objects;

import com.sun.org.apache.xpath.internal.operations.Mod;

import java.util.ArrayList;
import java.util.List;

/**
 * Passive object representing information on a conference.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class ConfrenceInformation {

    private String name;
    private int date;
    private List<Model> modelsPublished;

    public ConfrenceInformation(String name, int date){
        this.name = name;
        this.date = date;
        modelsPublished = new ArrayList<Model>();
    }
    public String getName(){
        return name;
    }
    public int getDate(){
        return date;
    }
    public List<Model> getModelsPublished(){
        return  modelsPublished;
    }
    public void publish(Model model){
        modelsPublished.add(model);
    }

    public int getDate() {
        return date;
    }
}
