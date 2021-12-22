package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Messages.*;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;
//import com.sun.org.apache.xpath.internal.operations.Mod;

/**
 * Student is responsible for sending the {@link TrainModelEvent},
 * {@link TestModelEvent} and {@link PublishResultsEvent}.
 * In addition, it must sign up for the conference publication broadcasts.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class StudentService extends MicroService {
    private Student student;
    public StudentService(String name,Student student) {
        super(name);
        this.student=student;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(PublishConferenceBroadcast.class,broadcast -> {
            for(Model model:broadcast.getResults())
                if(model.getStudent()!=student)
                    student.ReadPaper();
        });
        for(Model model : student.getModels()) {
            model.StartTraining();
            Future<Model> trainFuture =(Future<Model>) sendEvent(new TrainModelEvent(model));
                if (trainFuture != null)
                    synchronized (trainFuture){
                    model=trainFuture.get();
                    System.out.println("Finished training a model");
                    model.FinishTraining();
                    Future<Boolean> testFuture =(Future<Boolean>) sendEvent(new TestModelEvent(model));
                    if(testFuture !=null)
                        synchronized (testFuture) {
                            Boolean e=testFuture.get();
                            model.FinishTesting();
                            if(e){
                                model.pass();
                                student.PublishPaper();
                                sendEvent(new PublishResultsEvent(model));
                            }
                            else model.fail();
                        }
                    }



        }

    }
}
