package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jmx.remote.internal.ArrayQueue;
import jdk.internal.org.objectweb.asm.Type;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
                Cluster.getInstance().addCPU(cpu);
            }
            for (String typeS:typeList) {
                GPU.Type type = GPU.Type.valueOf(typeS);
                GPU gpu = new GPU(type);
                Cluster.getInstance().addGPU(gpu);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            }
    }
}