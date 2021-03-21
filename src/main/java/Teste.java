import java.util.ArrayList;

import org.iota.jota.IotaAPI;
import org.iota.jota.dto.response.SendTransferResponse;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Transfer;
import org.iota.jota.utils.SeedRandomGenerator;
import org.iota.jota.utils.TrytesConverter;

import dlt.client.tangle.model.LedgerWriter;

public class Teste {

	
	
	public static void main(String[] args) {
	   	String tag = "cloud1/fog1";
	   	IotaAPI	api = new IotaAPI.Builder()
                .protocol("https")
                .host("nodes.devnet.iota.org")
                .port(443)
                .build();
		//Variáveis da API
	    String address = "ZLGVEQ9JUZZWCZXLWVNTHBDX9G9KZTJP9VEERIIFHY9SIQKYBVAHIMLHXPQVE9IXFDDXNHQINXJDRPFDXNYVAPLZAW";
	    int depth = 3;
	    int minimumWeightMagnitude = 9;
	    int securityLevel = 2;
		System.out.println("Mensagem ai");

		String myRandomSeed = SeedRandomGenerator.generateNewSeed();
		String messageTrytes = TrytesConverter.asciiToTrytes("{\"target\":\"\",\"timestamp\":1612017322100}");
		String tagTrytes = TrytesConverter.asciiToTrytes(tag);
		Transfer zeroValueTransaction = new Transfer(address, 0, messageTrytes, tagTrytes);
		ArrayList<Transfer> transfers = new ArrayList<Transfer>();
		transfers.add(zeroValueTransaction);

		try {
			SendTransferResponse response = api.sendTransfer(myRandomSeed, securityLevel, depth,
					minimumWeightMagnitude, transfers, null, null, false, false, null);
			System.out.println(response.getTransactions());
		} catch (ArgumentException e) {
			// Handle error
			e.printStackTrace();
		}
	
	}
}
