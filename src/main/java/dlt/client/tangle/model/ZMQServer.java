package dlt.client.tangle.model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.zeromq.ZMQ;

/**
 *
 * @author Uellington Damasceno
 * @version 0.0.2
 */
public class ZMQServer implements Runnable {

    private Thread server;
    private final BlockingQueue<String> DLTInboundBuffer;
    //Remover após incluir a tangle local
    String address = "ZLGVEQ9JUZZWCZXLWVNTHBDX9G9KZTJP9VEERIIFHY9SIQKYBVAHIMLHXPQVE9IXFDDXNHQINXJDRPFDX";

    public ZMQServer(int bufferSize) {
        this.DLTInboundBuffer = new ArrayBlockingQueue(bufferSize);
    }

    public void start() {
        if (this.server == null) {
            this.server = new Thread(this);
            this.server.setName("CLIENT_TANGLE/ZMQ_SERVER");
            this.server.start();
        }
    }

    public void stop() {
        this.server.interrupt();
    }

    public void subscribe(String topic) {
    }

    public void unsubscribe(String topic) {
    }

    public String take() throws InterruptedException {
        return this.DLTInboundBuffer.take();
    }

    @Override
    public void run() {
    	ZMQ.Context context = ZMQ.context(1);
    	ZMQ.Socket socket = context.socket(ZMQ.SUB);
    	//usando configurações de teste até a definição dos tópicos
    	socket.connect("tcp://zmq.devnet.iota.org:5556");
    	socket.subscribe("tx");
    	socket.subscribe("sn");
    	System.out.println(this.server.isInterrupted());

    	while (!this.server.isInterrupted()) {

    		byte[] reply = socket.recv(0);

    	    String[] data = (new String(reply).split(" "));
    	    if(data[0].equals("tx")) {
        	    try {

        	    	if(data[2].equals(address)) {
        	    		System.out.println("MENSAGEM CHEGOU NO ZMQ");
        	    		this.DLTInboundBuffer.put("tx/"+data[1]);
        	    	}
        	    	
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

    	    }
    	    if(data[0].equals("sn")) {
    	    	 try {

    	    		 if(data[3].equals(address)) {
         	    		System.out.println("MENSAGEM CHEGOU NO ZMQ");

    	    			 this.DLTInboundBuffer.put("sn/"+data[2]);
    	    			 
    	    		 }
 				} catch (InterruptedException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
    	    }
        }
    }

}
