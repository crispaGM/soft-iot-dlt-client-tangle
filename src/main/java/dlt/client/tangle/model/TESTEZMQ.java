package dlt.client.tangle.model;

import java.io.StringReader;

import org.iota.jota.IotaAPI;
import org.iota.jota.dto.response.GetBundleResponse;
import org.iota.jota.utils.TrytesConverter;
import org.zeromq.ZMQ;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import dlt.client.tangle.enums.TransactionType;

public class TESTEZMQ {

	public static void main(String[] args) {
		ZMQ.Context context = ZMQ.context(1);
    	ZMQ.Socket socket = context.socket(ZMQ.SUB);
    	//usando configurações de teste até a definição dos tópicos
    	socket.connect("tcp://zmq.devnet.iota.org:5556");
    	socket.subscribe("tx");
    	socket.subscribe("sn");
    	
    	while (true) {
    	    System.out.println("Aguardando");

    		byte[] reply = socket.recv(0);
    	    System.out.println("Deu sinal");

    	    String[] data = (new String(reply).split(" "));
    	    System.out.println(data);
    	}
	}
	

}
