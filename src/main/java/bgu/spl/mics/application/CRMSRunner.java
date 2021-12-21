package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.services.CPUService;
import bgu.spl.mics.application.services.GPUService;
import bgu.spl.mics.application.services.StudentService;
import bgu.spl.mics.application.services.TimeService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jmx.remote.internal.ArrayQueue;
import jdk.internal.org.objectweb.asm.Type;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    public static void main(String[] args) {
        Set<Thread> threadSet=Thread.getAllStackTraces().keySet(); //debug
        File input = new File("example_input.json");
        //File input = new File(args[0]); /* FINAL TOUCH */
        try {
            JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
            JsonObject fileObject = fileElement.getAsJsonObject();
            Integer TickTime = fileObject.get("TickTime").getAsInt();
            Integer Duration = fileObject.get("Duration").getAsInt();

            JsonArray jsonArrayStudents = fileObject.get("Students").getAsJsonArray();
            List<Student> students = new ArrayList<>();
            for (JsonElement studentElement:jsonArrayStudents) {
                JsonObject studentJsonObject = studentElement.getAsJsonObject();
                String name = studentJsonObject.get("name").getAsString();
                String department = studentJsonObject.get("department").getAsString();
                String statusS = studentJsonObject.get("status").getAsString();
                Student.Degree degree = Student.Degree.valueOf(statusS);
                JsonArray jsonArrayModels = studentJsonObject.get("models").getAsJsonArray();
                Student student = new Student(name, department, degree);
                for (JsonElement modelElement:jsonArrayModels) {
                    JsonObject modelJsonObject = modelElement.getAsJsonObject();
                    String modelName = studentJsonObject.get("name").getAsString();
                    String typeS = modelJsonObject.get("type").getAsString();
                    Data.Type type = Data.Type.valueOf(typeS);
                    int size = modelJsonObject.get("size").getAsInt();
                    Data data = new Data(type, size);
                    Model model = new Model(modelName, data, student);
                    student.addModel(model);
                }
                students.add(student);
            }
            JsonArray jsonArrayConferences = fileObject.get("Conferences").getAsJsonArray();
            List<ConfrenceInformation> conferences = new ArrayList<>();
            for (JsonElement conferenceElement:jsonArrayConferences) {
                JsonObject conferenceJsonObject = conferenceElement.getAsJsonObject();
                String name = conferenceJsonObject.get("name").getAsString();
                int date = conferenceJsonObject.get("date").getAsInt();
                ConfrenceInformation confrenceInformation = new ConfrenceInformation(name, date);
                conferences.add(confrenceInformation);
            }
            JsonArray cpusJson = fileObject.get("CPUS").getAsJsonArray();
            List<Integer> coresList = new ArrayList<>();
            for(JsonElement cpuElement: cpusJson){
                coresList.add(cpuElement.getAsInt());
            }
            JsonArray gpusJson = fileObject.get("GPUS").getAsJsonArray();
            List<String> typeList = new ArrayList<>();
            for(JsonElement gpuElement: gpusJson){
                typeList.add(gpuElement.getAsString());
            }
            for (Integer cores:coresList) {
                CPU cpu = new CPU(cores);
                CPUService cpuService=new CPUService(cores+"",cpu);
                Thread cpuThread=new Thread(cpuService);
                cpuThread.start();
                Cluster.getInstance().addCPU(cpu);
            }
            Object waitForGPU=new Object();
            for (String typeS:typeList) {
                GPU.Type type = GPU.Type.valueOf(typeS);
                GPU gpu = new GPU(type);
                GPUService gpuService=new GPUService(type+"",gpu,waitForGPU);
                Thread gpuThread=new Thread(gpuService);
                gpuThread.start();
                Cluster.getInstance().addGPU(gpu);
            }
            synchronized (waitForGPU){
                try{
                    waitForGPU.wait(); //wait for at least one gpu to finish subscribing, before sending models for training
                }
                catch (InterruptedException ignored){}
            }

            for(Student student: students){
                StudentService studentService=new StudentService(student.getName(),student);
                Thread sThread=new Thread(studentService);
                sThread.start();
            }
            Cluster.getInstance().initialize();
            TimeService timeService=new TimeService(TickTime, Duration);
            Thread timeThread=new Thread(timeService);
            timeThread.start();

            //OUTPUT
            StringBuilder OutputS = new StringBuilder();
            OutputS.append("Students:\n");
            for(Student student:students){
                StringBuilder studentDetails = new StringBuilder("{\nName: " + student.getName() + "\n");
                studentDetails.append("Department: ").append(student.getDepartment()).append("\n");
                studentDetails.append("Status: ").append(student.getDegree()).append("\n");
                studentDetails.append("Publications: ").append(student.getDepartment()).append("\n");//TODO: publications
                studentDetails.append("Paper Read: ").append(student.getPapersRead()).append("\n");//TODO: paper read
                studentDetails.append("Train Models:\n");
                for (Model model:student.getModels()){
                    if (model.getStatus() == Model.Status.Trained || model.getStatus() == Model.Status.Tested){
                        studentDetails.append("{\nModel Name: ").append(model.getName()).append("\n");
                        studentDetails.append("Data:{\n");
                        studentDetails.append("Type: ").append(model.getData().getType()).append("\n");
                        studentDetails.append("Size: ").append(model.getData().getSize()).append("\n");
                        studentDetails.append("Status: ").append(model.getStatus()).append("\n");
                        studentDetails.append("Result: ").append(model.getName()).append("\n}\n");//TODO: result
                    }
                }
                studentDetails.append("\n}\n");
                OutputS.append(studentDetails);
            }
            OutputS.append("Conferences:\n");
            for (ConfrenceInformation conference: conferences){
                StringBuilder conferenceDetails = new StringBuilder("{\n");
                conferenceDetails.append("Name: ").append(conference.getName()).append("\n");
                conferenceDetails.append("Date: ").append(conference.getDate()).append("\n");
                conferenceDetails.append("Train Models:\n");
                for (Model model:conference.getModelsPublished()){
                    conferenceDetails.append("{\nModel Name: ").append(model.getName()).append("\n");
                    conferenceDetails.append("Data:{\n");
                    conferenceDetails.append("Type: ").append(model.getData().getType()).append("\n");
                    conferenceDetails.append("Size: ").append(model.getData().getSize()).append("\n");
                    conferenceDetails.append("Status: ").append(model.getStatus()).append("\n");
                    conferenceDetails.append("Result: ").append(model.getName()).append("\n}\n");//TODO: result
                }
                conferenceDetails.append("\n}\n");
                OutputS.append(conferenceDetails).append("\n");
            }
            OutputS.append("cpuTimeUsed: ").append("\n");//TODO: cpu time used
            OutputS.append("gpuTimeUsed: ").append("\n");//TODO: gpu time used
            OutputS.append("batchesProcessed: ").append("\n");//TODO: batched processed
            File Output = new File("Output.txt");
            FileWriter myWriter = new FileWriter("Output.txt");
            myWriter.write(String.valueOf(OutputS));
            myWriter.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
            }

    }
}