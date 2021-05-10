package blockchain;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

/** A representation of a single agent. */
public class Account {
  /** The public ECDSA key for this Account. This also serves as means to identify this Account. */
  private PublicKey publicKey;
  /** The private ECDSA key for this Account. */
  private PrivateKey privateKey;

  /**
   * The constructor for the Account class.
   *
   * @param publicKey the public ECDSA key of this Account
   * @param privateKey the private ECDSA key of this Account
   */
  public Account(PublicKey publicKey, PrivateKey privateKey) {
    this.publicKey = publicKey;
    this.privateKey = privateKey;
  }

  public static KeyPair generateKeys()
      throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
    kpg.initialize(new ECGenParameterSpec("secp256r1"), new SecureRandom());
    return kpg.generateKeyPair();
  }

  public void exportKeys(String dir) throws IOException {
    FileOutputStream out;

    out = new FileOutputStream(dir + ".key");
    out.write(getPrivateKey().getEncoded());
    out.close();

    out = new FileOutputStream(dir + ".pub");
    out.write(getPublicKey().getEncoded());
    out.close();
  }

  /**
   * Creates a new Account object.
   *
   * @return a new Account object with a private and public ECDSA keys
   * @throws NoSuchAlgorithmException if no ECDSA algorithm is found on the client
   * @throws InvalidAlgorithmParameterException if the spec of the encryption algorithm 'secp256r1'
   *     is not found on the client
   */
  public static Account createAccount()
      throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
    KeyPair newKeys = generateKeys();
    return new Account(newKeys.getPublic(), newKeys.getPrivate());
  }

  /**
   * Returns the public ECDSA of this Account.
   *
   * @return the public ECDSA key of this Account
   */
  public PublicKey getPublicKey() {
    return publicKey;
  }

  /**
   * This Account calls elections.
   *
   * <p>Creates a new Transaction object with an Elections inside of it. Processes the Transaction
   * and signs it.
   *
   * @param question a question to ask during elections
   * @param answers an array of possible answers to elections
   * @return a processed and signed Transaction object; ready to be added to a Block object
   */
  public Transaction createElections(String question, String[] answers) {
    Elections elections = new Elections(getPublicKey(), question, answers);
    Transaction electionTransaction = new Transaction(getPublicKey(), elections, null);
    electionTransaction.processTransaction();
    electionTransaction.sign(getPrivateKey());

    return electionTransaction;
  }

  private PrivateKey getPrivateKey() {
    return privateKey;
  }
}
