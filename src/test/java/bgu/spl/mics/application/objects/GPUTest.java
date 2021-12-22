package bgu.spl.mics.application.objects;

import static org.junit.Assert.*;

import bgu.spl.mics.application.objects.Cluster;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Model;
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
public class GPUTest{
    private GPU gpu;
    private Model model;
    private Data data;
    private Student student;
    @Before
    public void setUp(){
        data = new Data(Data.Type.Text,4);
        student = new Student("Yoav", "CS", Student.Degree.MSc);
        model = new Model("M1", data, student);
        GPU gpu = new GPU(GPU.Type.GTX1080);
    }
    @Test
    public void testActivate(){
        gpu.Activatetest();
        assertTrue(gpu.isActive());
    }
    @Test
    public void testDeactivate(){
        gpu.deactivate();
        assertFalse(gpu.isActive());
    }
    @Test
    public void testOnTick(){
        int tick = gpu.getTicks();
        gpu.OnTick();
        assertEquals(gpu.getTicks(), tick + 1);
    }


}