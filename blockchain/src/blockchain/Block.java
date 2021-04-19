package blockchain;

import java.util.ArrayList;
import java.util.Date;

/** This is a Block class. Represents a single block in the blockchain. */
public class Block {
  /**
   * Constructor for the Block class.
   *
   * @param previousHash the hash of the previous Block
   * @param blockVersion the version of this Block
   * @param miningDifficulty the mining difficulty of this Block
   */
  public Block(String previousHash, String blockVersion, int miningDifficulty) {
    this.previousHash = previousHash;
    this.blockVersion = blockVersion;
    this.miningDifficulty = miningDifficulty;

    id = this.calculateId();
  }

  private String id;
  private String hash = "";
  private String previousHash;
  private ArrayList<Transaction> transactions = new ArrayList<>();
  private Date timeStamp = new Date(System.currentTimeMillis());
  private int nonce = 0;
  private String merkleRoot;
  private String blockVersion;
  private int miningDifficulty;

  /**
   * toString override.
   *
   * @return the text representation of this Block.
   */
  @Override
  public String toString() {
    return ("Block. ID: "
        + this.id
        + " date: "
        + timeStamp.toString()
        + " version: "
        + blockVersion);
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
   * ID getter. ID of a Block is calculated based on the time stamp, previous hash and block
   * version.
   *
   * @return this Block's ID
   */
  public String getId() {
    return this.id;
  }

  /**
   * Hash getter.
   *
   * @return this Block's hash
   */
  public String getHash() {
    return this.hash;
  }

  /**
   * Adds a Transaction to this Block.
   *
   * @param transaction the transaction to be added
   */
  public void addTransaction(Transaction transaction) {
    if (validateTransaction(transaction)) this.transactions.add(transaction);
  }

  private boolean validateTransaction(Transaction transaction) {
    return true;
  }

  private String getHeader() {
    return this.timeStamp.toString() + this.blockVersion + this.previousHash;
  }

  /**
   * Calculates this \code{Block}'s id. Returns a 64-digit long representation of this Block.
   *
   * @return this Block's id
   */
  private String calculateId() {
    return StringUtils.hashString(this.getHeader());
  }

  /** Mines the hash of this Block. */
  public void mineHash() {
    String hashBase = getHeader() + transactions.toString();
    String targetPrefix = new String(new char[miningDifficulty]).replace("\0", " ");
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
        if (!validateTransaction(t)) {
          return false;
        }
      }

      if (!StringUtils.hashString(getHeader() + transactions.toString() + nonce).equals(getHash()))
        return false;
    } catch (Exception e) {
      return false;
    }

    return true;
  }
}
