package bgu.spl.mics;
import static org.junit.Assert.*;

import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.services.StudentService;
import bgu.spl.mics.application.services.TimeService;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class FutureTest {
    private Future<MicroService> future;

    @Before
    public  void setUp() throws Exception{
        future = new Future<>();
    }
    @After
    public void tearDown() throws Exception{

    }
    @Test
    public void testGet(){
        assertNull(future.get(2, TimeUnit.SECONDS));
    }
}