package bgu.spl.mics.application;

import bgu.spl.mics.application.Messages.TrainModelEvent;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

import java.util.Set;

/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    public static void main(String[] args) {
        Set<Thread> threadSet=Thread.getAllStackTraces().keySet(); //debug
       // Student student=new Student("Ben",)
       // Data data=new Data(Data.Type.Images,30000);
       // Model model=new Model("test",data)
       // TrainModelEvent event=new TrainModelEvent(Model)
    }
}
