package bgu.spl.mics;
import static org.junit.Assert.*;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.Student;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;


public class MessageBusTest {
    private ExampleEvent event;
    private ExampleBroadcast broadcast;
    public MessageBusImpl bus;
    MicroService microService;

    @Before
    public void setUp(){
        bus = MessageBusImpl.getInstence();
        event=new ExampleEvent("Arbitrary");
        microService=new MicroService("Test") {
            @Override
            protected void initialize() {

            }
        };
        broadcast=new ExampleBroadcast("Arbitrary");
    }
    @Test
    public void testSubscribeBroadcast(){
        bus.subscribeEvent(event.getClass(),microService);
        ConcurrentHashMap<Class<? extends Message>, Deque<MicroService>> hashMap = bus.getSubscribedMircoServiceEvent();
        assertEquals(microService, hashMap.get(event.getClass()).getFirst());
    }
    @Test
    public void testComplete(){


    }
    @Test
    public void testSendBroadcast(){
        bus.subscribeBroadcast(broadcast.getClass(),microService);
        ConcurrentHashMap<Class<? extends Message>, Deque<MicroService>> hashMap = bus.getSubscribedMircoServiceBroadCasts();
        assertEquals(microService, hashMap.get(broadcast.getClass()).getFirst());
    }
    @Test
    public void testSendEvent(){


    }
    @Test
    public void testRegister(){


    }
    @Test
    public void testUnregister(){
        bus.subscribeEvent(event.getClass(),microService);

    }
    @Test
    public void testAwaitMessage(){
        bus.subscribeEvent(event.getClass(),microService);

    }

}