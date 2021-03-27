package blockchain;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;

public class Transaction {
    public ArrayList<TransactionInput> inputs;
    public Entry data;
    public PublicKey signee;
    private String id;
    private byte[] signature;
    private Date timeStamp;

    public Transaction(PublicKey signee, Entry data, ArrayList<TransactionInput> inputs) {
        this.signee = signee;
        this.data = data;
        this.inputs = inputs;
        timeStamp = new Date(System.currentTimeMillis());
    }

    public ArrayList<TransactionOutput> processTransaction() {
        return new ArrayList<>();
    }

    private void calculateHash() {
        id = StringUtils.stringToHex(StringUtils.keyToString(signee) + data.toString() + timeStamp.toString());
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtils.keyToString(signee) + timeStamp.toString() + this.data.toString();
        signature = StringUtils.signWithEcdsa(privateKey, data);
    }

    public boolean verifySignature() {
        return true;
    }
}

class TransactionInput {

}

class TransactionOutput {

}


