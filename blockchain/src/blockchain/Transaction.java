package blockchain;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * A representation of a single transaction in the blockchain - starting elections, casting a vote,
 * tallying elections.
 */
public class Transaction {
  /** The Transactions used by this Transaction to process its Entry. */
  public List<TransactionInput> inputs;

  /** The data stored in this Transaction. */
  public Entry data;

  /** The public ECDSA key of the agent signing this Transaction. */
  public PublicKey signee;

  /**
   * The 64-digit hex unique identifier of this Transaction. Calculated from this Transaction's:
   * signee, data and timeStamp.
   */
  private String id;

  /** This Transaction encrypted with the private key of the agent signing this Transaction. */
  private byte[] signature;

  /** The time stamp of creation of this Transaction. */
  private final Date timeStamp;

  /** The outputs from this Transaction. */
  public List<TransactionOutput> outputs;

  /**
   * A constructor for Transaction.
   *
   * @param signee the public ECDSA key of the agent signing this Transaction
   * @param data the data inside this Transaction
   * @param inputs the list of Transaction consisting the input for this Transaction
   */
  public Transaction(PublicKey signee, Entry data, List<TransactionInput> inputs) {
    this.signee = signee;
    this.data = data;
    this.inputs = inputs;
    timeStamp = new Date(System.currentTimeMillis());
  }

  /**
   * Processes this Transaction. Sets up the Entry object of this Transaction and calculates the
   * hash of this Transaction. Creates the TransactionOutput object associated with this Transaction.
   *
   * @return the list of outputs corresponding to: this Transaction and unconsumed Transaction from
   *     inputs
   */
  public List<TransactionOutput> processTransaction() {
    List<TransactionOutput> outputs;
    try {
      outputs = data.processEntry(inputs);
      calculateHash();
    } catch (Exception e) {
      ArrayList<TransactionOutput> unchangedOutputs = new ArrayList<>();
      for (TransactionInput inputTransaction : inputs) {
        unchangedOutputs.add(inputTransaction.transactionOut);
      }
      return unchangedOutputs;
    }
    outputs.add(new TransactionOutput(signee, data, getId()));

    this.outputs = outputs;
    return outputs;
  }

  /**
   * Calculates and internally assigns it to the id of this Transaction.
   *
   * <p>The hashed objects include: the public key of the author of this Transaction, the time stamp
   * of creation of this Transaction, the string representation of the data in this Transaction.
   */
  private void calculateHash() {
    id =
        StringUtils.hashString(
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
  public void sign(PrivateKey privateKey) {
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

  /**
   * Validates this Transactions.
   *
   * <p>Validates following:
   *
   * <ul>
   *   <li>if the public ECDSA key of the signee matches the signature
   *   <li>the recalculated hash matches the current hash of this Transaction
   *   <li>if the data inside this Transaction is valid
   * </ul>
   *
   * @return true if this Transaction is a valid Transaction - can be added to a Block object; false
   *     otherwise
   */
  public boolean validate() {
    if (!verifySignature()) {
      return false;
    }

    String currentHash = this.id;
    calculateHash();
    if (!Objects.equals(currentHash, this.id)) {
      this.id = currentHash;
      return false;
    }

    try {
      if (!data.validateEntry()) {
        return false;
      }
      ;
    } catch (Exception e) {
      return false;
    }

    return true;
  }

  /**
   * Getter of this Transaction's id.
   *
   * @return the 64-digit hex unique identifier of this Transaction
   */
  public String getId() {
    return id;
  }
}
