package blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

public class Block {
    /**
     * This is a Block class.
     * <p>
     * This is a longer description of the Block class.
     * </p>
     */
    public Block() {
    }

    String hash;
    String previousHash;
    ArrayList<Transaction> transactions = new ArrayList<>();
    Date timeStamp = new Date(System.currentTimeMillis());
    int nonce;
    String merkleRoot;
    String blockVersion = "v1";
    int miningDifficulty;

    @Override
    public String toString() {
        return ("Block. Hash: " +
                hash +
                " date: " +
                timeStamp.toString() +
                " version: " +
                blockVersion
        );
    }

    public String calculateHash() throws NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest((timeStamp.toString() + blockVersion).getBytes(StandardCharsets.UTF_8)).toString();
    }
}

