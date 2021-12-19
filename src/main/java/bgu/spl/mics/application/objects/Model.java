package bgu.spl.mics.application.objects;

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
    private boolean processed=false;

    enum Status {
        PreTrained, Training, Trained, Tested
    }

    public Model(String name, Data data, Student student) {
        this.name = name;
        this.data = data;
        this.student = student;
        status=Status.PreTrained;
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
        return processed;
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
            processed=true;
    }
    public void TestModel(){
        if(status==Status.Trained)
            status=Status.Tested;
    }
    public DataBatch getNextBatch(){
        return data.getDataBatchToProcess();
    }
    public void addProcessedBatch(DataBatch batch){
        data.addProcessed(batch);
    }
}