package dlt.client.tangle.model;

import dlt.client.tangle.services.ILedgerReader;
import dlt.client.tangle.services.ILedgerSubscriber;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Uellington Damasceno
 * @version 0.1.0
 */
public class LedgerReader implements ILedgerReader, Runnable {

    private Thread DLTInboundMonitor;
    private final Map<String, Set<ILedgerSubscriber>> topics;
    private ZMQServer server;
    private String message;
    public LedgerReader() {
        this.topics = new HashMap();
    }

    public void start() {
        if (this.DLTInboundMonitor == null) {
            this.DLTInboundMonitor = new Thread(this);
            this.DLTInboundMonitor.setName("CLIENT_TANGLE/DLT_INBOUND_MONITOR");
            this.DLTInboundMonitor.start();
        }
    }

    public void stop() {
        this.DLTInboundMonitor.interrupt();
    }

    public void setZMQServer(ZMQServer server) {
        this.server = server;
    }
    @Override
    public void subscribe(String topic, ILedgerSubscriber subscriber) {
		System.out.println("DEU SUB");

    	if (topic != null) {        
            Set<ILedgerSubscriber> subscribers = this.topics.get(topic);
    		System.out.println("Olhando o subscriber");
    		System.out.println(subscribers);

            if (subscribers != null) {

                subscribers.add(subscriber);
            } else {

                subscribers = new HashSet();
                subscribers.add(subscriber);
                this.topics.put(topic, subscribers);
            }
        }
		System.out.println("Os topicos");
		System.out.println(this.topics.toString());

    }

    @Override
    public void unsubscribe(String topic, ILedgerSubscriber subscriber) {
        if (topic != null) {
            Set<ILedgerSubscriber> subscribers = this.topics.get(topic);
            if (subscribers != null && !subscribers.isEmpty()) {
                subscribers.remove(subscriber);
            } else {
            	
                subscribers = new HashSet();
                this.topics.put(topic, subscribers);
            }
        }
    }

    @Override
    public void run() {

    	while (!this.DLTInboundMonitor.isInterrupted()) {
            try {
            	
            	String data[] = this.server.take().split("/");

            	String topic = data[0];
            	String message = data[1];
            	notifyAll(topic,message);
 	    		

            } catch (InterruptedException ex) {
                this.DLTInboundMonitor.interrupt();
            }
        }
    }

    private void notifyAll(String topic, Object object) {
        System.out.println("NOTIFICANDO TODOS");
    	if (topic != null && !topic.isEmpty()) {
    		System.out.println("TOPICOS");
    		System.out.println(this.topics.toString());
            Set<ILedgerSubscriber> subscribers = this.topics.get(topic);
            if (subscribers != null && !subscribers.isEmpty()) {
            	System.out.println("NOTIFICOU HARD");
                subscribers.forEach(sub -> sub.update(object));
            }
        }
    }
    
    public String getMessage() {
    	return this.message;
    }
    
    public Thread getDLTInboundMonitor() {
    	return this.DLTInboundMonitor;
    	
    }
    
    

}
