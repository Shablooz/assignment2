package bgu.spl.mics.application.objects;

import com.sun.jmx.remote.internal.ArrayQueue;

import java.util.Queue;

/**
 * Passive object representing single student.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Student {
    public Student(int name, String department, Degree status, ArrayQueue<Model> models) {
        this.name = name;
        this.department = department;
        this.status = status;
        publications=0;
        papersRead=0;
        this.models=models;
    }

    /**
     * Enum representing the Degree the student is studying for.
     */
    enum Degree {
        MSc, PhD
    }
    public ArrayQueue<Model> models;
    private int name;
    private String department;
    private Degree status;
    private int publications;
    private int papersRead;

    public Degree getDegree() {
        return status;
    }

    public int getName() {
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
}
