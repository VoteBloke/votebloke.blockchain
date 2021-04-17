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

  /**
   * Calculates and internally assigns it to the id of this Transaction.
   *
   * <p>The hashed objects include: the public key of the author of this Transaction, the time stamp
   * of creation of this Transaction, the string representation of the data in this Transaction.
   */
  private void calculateHash() {
    id =
        StringUtils.stringToHex(
            StringUtils.keyToString(signee) + data.toString() + timeStamp.toString());
  }

  /**
   * Signs the data with an ECDSA private key.
   *
   * <p>Signed data includes: the public key of the signee, the time stamp and the string
   * representation of the data in this Transaction.
   *
   * @param privateKey the private ECDSA key used to encrypt the data
   */
  public void generateSignature(PrivateKey privateKey) {
    String data = StringUtils.keyToString(signee) + timeStamp.toString() + this.data.toString();
    signature = StringUtils.signWithEcdsa(privateKey, data);
  }

  /**
   * Verifies if this Transaction's signee matches the signature.
   *
   * <p>The public ECDSA key is verified against the encrypted data.
   *
   * @return true if the signature matches the signee; false otherwise
   */
  public boolean verifySignature() {
    return StringUtils.verifyEcdsa(
        signee,
        StringUtils.keyToString(signee) + timeStamp.toString() + this.data.toString(),
        signature);
  }
}

class TransactionInput {
  /**
   * The reference to a Transaction object associated with TransactionOutput objects needed to
   * process a new Transaction.
   */
  String transactionOutputId;

  /** The TransactionOutput object behind this TransactionInput */
  TransactionOutput transactionOut;

  TransactionInput(String transactionOutputId) {
    this.transactionOutputId = transactionOutputId;
  }
}

class TransactionOutput {
  /** The id of this Transaction. */
  String id;
  /** The public key this TransactionOutput was addressed to. */
  PublicKey recipient;
  /** Data associated with this TransactionOutput. */
  Entry data;
  /** The id of the Transaction object, which created this TransactionOutput. */
  String parentTransactionId;

  TransactionOutput(PublicKey recipient, Entry data, String parentTransactionId) {
    this.recipient = recipient;
    this.data = data;
    this.parentTransactionId = parentTransactionId;
  }

  boolean isAdressedTo(PublicKey key) {
    return key == recipient;
  }
}
