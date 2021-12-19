package bgu.spl.mics.application;

import bgu.spl.mics.application.Messages.TrainModelEvent;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;
import bgu.spl.mics.application.services.GPUService;
import bgu.spl.mics.application.services.StudentService;

import java.util.Set;
import java.util.concurrent.TimeUnit;


/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    public static void main(String[] args) {
        Student student=new Student("Ben","non",Student.Degree.MSc);
        Data data=new Data(Data.Type.Images,30000);
        Model model=new Model("test",data,student);
        student.addModel(model);
   //     TrainModelEvent event=new TrainModelEvent(model);
        StudentService service=new StudentService("service",student);
        GPU gpu=new GPU(GPU.Type.RTX2080);
        GPUService gpuService=new GPUService("serser",gpu);
        Thread thread=new Thread(gpuService);
        Set<Thread> threadSet=Thread.getAllStackTraces().keySet(); //debug
        thread.start();
        Thread t1=new Thread(service);
        t1.start();

    }
}
