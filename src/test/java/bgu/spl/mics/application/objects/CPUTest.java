package bgu.spl.mics.application.objects;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class CPUTest {
    private CPU cpu;

    @Before
    public void setUp() throws Exception{
        cpu = new CPU(16);
    }
    @Test
    public void testTick(){
        int time = cpu.getTimeUsed();
        cpu.tick();
        assertEquals(time + 1, cpu.getTimeUsed());
    }
    @Test
    public void testprocessBatch(){
        int batched = cpu.getBatchesProcessed();
        cpu.processBatch();
        assertEquals(batched + 1, cpu.getBatchesProcessed());
    }
    @After
    public void tearDown() throws Exception{

    }
}
