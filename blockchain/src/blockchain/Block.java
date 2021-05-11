package blockchain;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** This is a Block class. Represents a single block in the blockchain. */
public class Block {
  /**
   * The id of this Block. ID of a Block is calculated based on the time stamp, previous hash and
   * block version.
   */
  private String id;
  /**
   * The mined hash of this Block. Hash of this Block is mined based on the hash of the previous
   * Block in the Chain, the time stamp of creating this Block, the version of this Block and
   * Transactions in this Block.
   */
  private String hash = "";

  /** The hash of the previous Block in the Chain. */
  private final String previousHash;

  /** The Transactions added to this Block. */
  private ArrayList<Transaction> transactions = new ArrayList<>();

  /** The time stamp of the creation of this Block. */
  private final Date timeStamp = new Date(System.currentTimeMillis());

  /** The nonce of this Block. */
  private int nonce = 0;

  /** The version of this Block. */
  private final String blockVersion;

  /**
   * The mining difficulty of this Block. This is the number of leading zeros required in the mined
   * hash.
   */
  private final int miningDifficulty;

  /**
   * The list of unconsumed TransactionOutputs. Those outputs were not yet used up by Transactions.
   */
  private ArrayList<TransactionOutput> unconsumedOutputs;

  /**
   * Constructor for the Block class.
   *
   * @param previousHash the hash of the previous Block
   * @param blockVersion the version of this Block
   * @param miningDifficulty the mining difficulty of this Block
   * @param unconsumedOutputs the pool of unconsumed TransactionOutputs
   */
  public Block(
      String previousHash,
      String blockVersion,
      int miningDifficulty,
      ArrayList<TransactionOutput> unconsumedOutputs) {
    this.previousHash = previousHash;
    this.blockVersion = blockVersion;
    this.miningDifficulty = miningDifficulty;
    if (unconsumedOutputs == null) {
      this.unconsumedOutputs = new ArrayList<>();
    } else {
      this.unconsumedOutputs = unconsumedOutputs;
    }
    id = this.calculateId();
  }

  /**
   * toString override.
   *
   * @return the text representation of this Block.
   */
  @Override
  public String toString() {
    return ("Block. ID: " + this.id + " date: " + timeStamp + " version: " + blockVersion);
  }

  /**
   * Adds a Transaction to this Block.
   *
   * <p>Performs processing and validation of the passed Transaction object. Updates the pool of
   * available, unconsumed TransactionOutput objects in this Block by removing the input
   * Transactions consumed by the transaction and adding the outputs of the transaction.
   *
   * @param transaction the transaction to be added
   */
  public void addTransaction(Transaction transaction) {
    if (transaction.inputs != null) {
      for (TransactionInput inputTransaction : transaction.inputs) {
        if (!unconsumedOutputs.contains(inputTransaction.transactionOut)) {
          return;
        }
      }

      transaction.inputs.forEach(
          transactionInput -> unconsumedOutputs.remove(transactionInput.transactionOut));
    }
    unconsumedOutputs.addAll(transaction.outputs);

    if (transaction.validate()) {
      this.transactions.add(transaction);
    }
  }

  /**
   * Calculates this \code{Block}'s id. Returns a 64-digit-long representation of this Block.
   *
   * @return this Block's id
   */
  private String calculateId() {
    return StringUtils.hashString(this.getHeader());
  }

  /** Mines the hash of this Block. */
  public void mineHash() {
    String hashBase = getHeader() + transactions.toString();
    String targetPrefix = new String(new char[miningDifficulty]).replace("\0", "0");
    while (hash.equals("")) {
      if (targetPrefix.equals(
          StringUtils.hashString(hashBase + nonce).substring(0, miningDifficulty))) {
        hash = StringUtils.hashString(hashBase + nonce);
      } else {
        nonce += 1;
      }
    }
  }

  /**
   * Validates this Block.
   *
   * @return true if this Block is valid; false otherwise
   */
  public boolean isBlockValid() {
    try {
      for (Transaction t : transactions) {
        if (!t.validate()) {
          return false;
        }
      }

      if (!StringUtils.hashString(getHeader() + transactions.toString() + nonce)
          .equals(getHash())) {
        return false;
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return false;
    }

    return true;
  }

  /**
   * timeStamp getter.
   *
   * @return a Date object with the date of creation of this Block.
   */
  public Date getTimeStamp() {
    return this.timeStamp;
  }

  /**
   * ID getter.
   *
   * @return this Block's ID
   */
  public String getId() {
    return this.id;
  }

  /**
   * Hash getter.
   *
   * @return this Block's mined hash
   */
  public String getHash() {
    return this.hash;
  }

  /**
   * Getter of unconsumed outputs of this Transaction.
   *
   * @return the unconsumed TransactionOutputs of this Block
   */
  public ArrayList<TransactionOutput> getUnconsumedOutputs() {
    return unconsumedOutputs;
  }

  /**
   * Returns entries authored by the provided public key.
   *
   * <p>Searches through the unconsumed TransactionOutput objects stored in this Block and returns
   * those authored by the provided ECDSA key.
   *
   * @param author the public ECDSA key of the agent who authored the Entry objects looked for in
   *     this method
   * @return the array of the Entry objects authored by the provided public key
   */
  public List<TransactionOutput> authoredBy(PublicKey author) {
    List<TransactionOutput> outputs = new ArrayList<>();
    unconsumedOutputs.forEach(
        transactionOutput -> {
          if (transactionOutput.isAddressedFrom(author)) {
            outputs.add(transactionOutput);
          }
        });

    return outputs;
  }

  /**
   * Returns entries authored by the provided public key.
   *
   * <p>Searches through the unconsumed TransactionOutput objects stored in this Block and returns
   * those authored by the provided ECDSA key.
   *
   * @param author the public ECDSA key of the agent who authored the Entry objects looked for in
   *     this method
   * @param selectedClass the fully specified class name of objects included in the return value
   * @return the array of the Entry objects authored by the provided public key and of type
   *     specified by selectedClass
   */
  public <T extends Entry> List<TransactionOutput> authoredBy(
      PublicKey author, Class<T> selectedClass) {
    List<TransactionOutput> outputs = new ArrayList<>();
    unconsumedOutputs.forEach(
        transactionOutput -> {
          if (transactionOutput.isAddressedFrom(author)
              && selectedClass.isInstance(transactionOutput.data)) {
            outputs.add(transactionOutput);
          }
        });

    return outputs;
  }

  /**
   * Returns active Elections in this Block.
   *
   * @param caller the public ECDSA key of the agent who authored the returned Elections objects. If
   *     null returns all active Elections
   * @return the list of active Elections objects authored by the caller
   */
  public List<Elections> getOpenElections(PublicKey caller) {
    ArrayList<Elections> activeElections = new ArrayList<>();
    unconsumedOutputs.forEach(
        transactionOutput -> {
          if (transactionOutput.data instanceof Elections
              && (caller == null || transactionOutput.isAddressedFrom(caller))) {
            activeElections.add((Elections) transactionOutput.data);
          }
        });

    return activeElections;
  }

  public final String getPreviousHash() {
    return this.previousHash;
  }

  private String getHeader() {
    return this.timeStamp + this.blockVersion + this.previousHash;
  }
}
