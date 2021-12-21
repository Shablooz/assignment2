package bgu.spl.mics.application.objects;

import java.util.Base64;

/**
 * Passive object representing a Deep Learning model.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Model {

    private final Data data;
    private Status status;
    private final Student student;
    private final String name;
    private boolean noUnprocessedLeft;
    private results result;
    enum Status {
        PreTrained, Training, Trained, Tested
    }
    enum results{
        None,Good,Bad
    }

    public Model(String name, Data data, Student student) {
        this.name = name;
        this.data = data;
        this.student = student;
        status=Status.PreTrained;
        noUnprocessedLeft=false;
        result=results.None;
    }
    public Boolean Test(){
        if(student.getDegree()== Student.Degree.PhD)
            return Math.random()<=0.8; //80% chance to return true
        else return Math.random()<=0.6;
    }
    public Data getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public Student getStudent() {
        return student;
    }
    public boolean getProcessingStatus(){
        return data.processed();
    }
    public boolean noUnprocessedLeft(){
        return noUnprocessedLeft;
    }
    public Status getStatus() {
        return status;
    }

    public void StartTraining(){
        if(status==Status.PreTrained)
            status=Status.Training;
    }
    public void FinishTraining(){
        if(status==Status.Training)
            status=Status.Trained;
    }
    public void FinishTesting(){
        if(status==Status.Trained)
            status=Status.Tested;
    }
    public DataBatch getNextBatch(){
        DataBatch batch= data.getDataBatchToProcess();
        if(data.UnprocessedIsEmpty())
            noUnprocessedLeft=true;
        return batch;
    }
    public void pass(){
        result=results.Good;
    }
    public void fail(){
        result=results.Bad;
    }
    public void addProcessedBatch(DataBatch batch){
        data.addProcessed(batch);
    }
}