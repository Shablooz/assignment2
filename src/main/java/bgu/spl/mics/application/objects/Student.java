package bgu.spl.mics.application.objects;

//import com.sun.jmx.remote.internal.ArrayQueue;

import java.util.ArrayDeque;

/**
 * Passive object representing single student.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Student {
    /**
     * Enum representing the Degree the student is studying for.
     */
    public enum Degree {
        MSc, PhD
    }
    public ArrayDeque<Model> models;
    private String name;
    private String department;
    private Degree status;
    private int publications;
    private int papersRead;

    public Student(String name, String department, Degree status) {
        this.name = name;
        this.department = department;
        this.status = status;
        publications=0;
        papersRead=0;
        models=new ArrayDeque<>();
    }
    public Degree getDegree() {
        return status;
    }

    public String getName() {
        return name;
    }

    public int getPapersRead() {
        return papersRead;
    }

    public int getPublications() {
        return publications;
    }

    public String getDepartment() {
        return department;
    }
    public void PublishPaper(){
        publications++;
    }
    public void ReadPaper(){
        papersRead++;
    }
    public void addModel(Model model){
        this.models.addFirst(model);
    }
    public ArrayDeque<Model> getModels() {
        return models;
    }
}
