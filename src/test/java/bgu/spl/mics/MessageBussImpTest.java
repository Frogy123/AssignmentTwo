package bgu.spl.mics;

import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import bgu.spl.mics.example.services.ExampleBroadcastListenerService;
import bgu.spl.mics.example.services.ExampleEventHandlerService;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class MessageBussImpTest {
    private MessageBus messageBus;
    private MicroService microService;
    private MicroService ms2;


    @BeforeEach
    public void setUp(){
        messageBus =  MessageBusImpl.getInstance();
        microService = new ExampleBroadcastListenerService("MicroService1", new String[]{"2"});
        messageBus.register(microService);
    }
    @AfterEach
    public void tearDown()
    {
        if(messageBus.isRegistered(microService))
            messageBus.unregister(microService);
    }

    @Test
    public void testRegister() {
        assertTrue(messageBus.isRegistered(microService), "MicroService should be registered.");
    }

    @Test
    public void testUnregister() {
        messageBus.unregister(microService);
        assertFalse(messageBus.isRegistered(microService), "MicroService should be unregistered.");
    }

    @Test
    public void testSubscribeEvent() {
        messageBus.subscribeEvent(ExampleEvent.class, microService);
        assertTrue(messageBus.isSubscribedToEvent(ExampleEvent.class, microService),
                "MicroService should be subscribed to the event type.");
    }

    // Test for subscribeBroadcast method
    @Test
    public void testSubscribeBroadcast() {
        messageBus.subscribeBroadcast(ExampleBroadcast.class, microService);
        assertTrue(messageBus.isSubscribedToBroadcast(ExampleBroadcast.class, microService),
                "MicroService should be subscribed to the broadcast type.");
    }







}
