package blockchain;

import java.util.ArrayList;
import java.util.Date;


/**
 * This is a Block class.
 * Represents a single block in the blockchain.
 */
public class Block {
    /**
     * Constructor for the Block class.
     *
     * @param previousHash     Hash of the previous Block.
     * @param blockVersion     Version of this Block.
     * @param miningDifficulty Mining difficulty of this Block.
     */
    public Block(String previousHash, String blockVersion, int miningDifficulty) {
        this.previousHash = previousHash;
        this.blockVersion = blockVersion;
        this.miningDifficulty = miningDifficulty;

        id = this.calculateId();
    }

    String id;
    String hash = "";
    String previousHash;
    ArrayList<Transaction> transactions = new ArrayList<>();
    Date timeStamp = new Date(System.currentTimeMillis());
    int nonce = 0;
    String merkleRoot;
    String blockVersion;
    int miningDifficulty;

    /**
     * toString override.
     *
     * @return A text representation of this Block.
     */
    @Override
    public String toString() {
        return ("Block. ID: " +
                this.id +
                " date: " +
                timeStamp.toString() +
                " version: " +
                blockVersion
        );
    }

    /**
     * timeStamp getter.
     *
     * @return A Date object with the date of creation of this Block.
     */
    public Date getTimeStamp() {
        return this.timeStamp;
    }

    /**
     * ID getter.
     * ID of a Block is calculated based on the time stamp, previous hash and block version.
     *
     * @return This Block's ID.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Hash getter.
     *
     * @return This Block's hash.
     */
    public String getHash() {
        return this.hash;
    }

    /**
     * Adds a Transaction to this Block.
     *
     * @param transaction Transaction to be added.
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
     * Calculates this \code{Block}'s id.
     * Returns a 64-digit long representation of this Block.
     *
     * @return This Block's id.
     */
    private String calculateId() {
        return StringUtils.stringToHex(this.getHeader());
    }

    /**
     * Mines the hash of this Block.
     */
    public void mineHash() {
        String hashBase = this.getHeader() + this.transactions.toString();
        String targetPrefix = new String(new char[this.miningDifficulty]).replace("\0", " ");
        while (this.hash.equals("")) {
            if (targetPrefix.equals(StringUtils.stringToHex(hashBase + nonce).substring(0,
                    this.miningDifficulty))) {
                this.hash = StringUtils.stringToHex(hashBase + nonce);
            } else {
                nonce += 1;
            }
        }
    }
}

