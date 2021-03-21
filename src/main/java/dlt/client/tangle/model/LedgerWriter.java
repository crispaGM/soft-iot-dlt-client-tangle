package dlt.client.tangle.model;

import dlt.client.tangle.services.ILedgerWriter;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.iota.jota.IotaAPI;
import org.iota.jota.dto.response.GetBundleResponse;
import org.iota.jota.dto.response.SendTransferResponse;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Transfer;
import org.iota.jota.utils.SeedRandomGenerator;
import org.iota.jota.utils.TrytesConverter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


/**
 *
 * @author Uellington Damasceno
 * @version 0.0.1
 */
public class LedgerWriter implements ILedgerWriter, Runnable {

    private IotaAPI api;
    private Thread DLTOutboundMonitor;
    private final BlockingQueue<Transaction> DLTOutboundBuffer;
    private String tag = "cloud1/fog";
    
    
    
   //Variáveis da API
    String address = "ZLGVEQ9JUZZWCZXLWVNTHBDX9G9KZTJP9VEERIIFHY9SIQKYBVAHIMLHXPQVE9IXFDDXNHQINXJDRPFDXNYVAPLZAW";
    int depth = 3;
    int minimumWeightMagnitude = 9;
    int securityLevel = 2;
    

    public LedgerWriter(String protocol, String url, int port, int bufferSize) {
        this.api = new IotaAPI.Builder()
                .protocol(protocol)
                .host(url)
                .port(port)
                .build();
        this.DLTOutboundBuffer = new ArrayBlockingQueue(bufferSize);
    }

    @Override
    public void put(Transaction transaction) throws InterruptedException {
        this.DLTOutboundBuffer.put(transaction);
    }

    public void start() {
        if (this.DLTOutboundMonitor == null) {
            this.DLTOutboundMonitor = new Thread(this);
            this.DLTOutboundMonitor.setName("CLIENT_TANGLE/DLT_OUTBOUND_MONITOR");
            this.DLTOutboundMonitor.start();
        }
    }

    public void stop() {
        this.DLTOutboundMonitor.interrupt();
    }

    @Override
    public void run() {
        while (!this.DLTOutboundMonitor.isInterrupted()) {
            try {
                Transaction transaction = this.DLTOutboundBuffer.take();
            	Gson gson = new Gson();
            	String json = gson.toJson(transaction);
            	writeToTangle(json);

                
            } catch (InterruptedException ex) {
                this.DLTOutboundMonitor.interrupt();
            }
        }
    }
    
public Transaction getTransactionByHash(String hashTransaction) {
	GetBundleResponse response = api.getBundle(hashTransaction);
    String transactionString =  TrytesConverter.trytesToAscii(response.getTransactions().get(0).getSignatureFragments().substring(0,2186));
    
	Transaction transaction = new Gson().fromJson(transactionString, Transaction.class);
	
	return transaction; 

}
	public void writeToTangle(String message) {


		String myRandomSeed = SeedRandomGenerator.generateNewSeed();
		String messageTrytes = TrytesConverter.asciiToTrytes(message);
		String tagTrytes = TrytesConverter.asciiToTrytes(tag);
		Transfer zeroValueTransaction = new Transfer(address, 0, messageTrytes, tagTrytes);
		ArrayList<Transfer> transfers = new ArrayList<Transfer>();
		transfers.add(zeroValueTransaction);

		try {
			SendTransferResponse response = api.sendTransfer(myRandomSeed, securityLevel, depth,
					minimumWeightMagnitude, transfers, null, null, false, false, null);
		} catch (ArgumentException e) {
			// Handle error
			e.printStackTrace();
		}

	}
}
