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

  private String merkleRoot;
  /** The version of this Block. */
  private final String blockVersion;

  /**
   * The mining difficulty of this Block. This is the number of leading zeros required in the mined
   * hash.
   */
  private final int miningDifficulty;

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
   * Adds a Transaction to this Block.
   *
   * @param transaction the transaction to be added
   */
  public void addTransaction(Transaction transaction) {
    if (transaction.validate()) this.transactions.add(transaction);
  }

  private String getHeader() {
    return this.timeStamp.toString() + this.blockVersion + this.previousHash;
  }

  /**
   * Calculates this \code{Block}'s id. Returns a 64-digit-long representation of this Block.
   *
   * @return this Block's id
   */
  private String calculateId() {
    return StringUtils.stringToHex(this.getHeader());
  }

  /** Mines the hash of this Block. */
  public void mineHash() {
    String hashBase = getHeader() + transactions.toString();
    String targetPrefix = new String(new char[miningDifficulty]).replace("\0", " ");
    while (hash.equals("")) {
      if (targetPrefix.equals(
          StringUtils.stringToHex(hashBase + nonce).substring(0, miningDifficulty))) {
        hash = StringUtils.stringToHex(hashBase + nonce);
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
        if (!t.validate()) return false;
      }

      if (!StringUtils.stringToHex(getHeader() + transactions.toString() + nonce).equals(getHash()))
        return false;
    } catch (Exception e) {
      return false;
    }

    return true;
  }
}
