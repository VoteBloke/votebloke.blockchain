package blockchain;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.util.concurrent.ThreadLocalRandom;

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

  public static KeyPair generateKeys() throws NoSuchAlgorithmException {
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
    kpg.initialize(2048);

    return kpg.generateKeyPair();
  }

  private void exportKeys(String dir) throws IOException {

    FileOutputStream out;

    out = new FileOutputStream(dir + ".key");
    out.write(privateKey.getEncoded());
    out.close();

    out = new FileOutputStream(dir + ".pub");
    out.write(getPublicKey().getEncoded());
    out.close();
  }

  public static Account createAccount() throws NoSuchAlgorithmException, IOException {
    KeyPair newKeys = generateKeys();

    Account ret  = new Account(newKeys.getPublic(), newKeys.getPrivate());
    // Save created keys in working directory

    ret.exportKeys("./testStorage/key");

    return  ret;

  }

  public boolean validatePrivate(PrivateKey priv) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

    byte[] chall = new byte[10000];
    ThreadLocalRandom.current().nextBytes(chall);

    Signature sig = Signature.getInstance("SHA256withRSA");
    sig.initSign(priv);
    sig.update(chall);
    byte[] signature = sig.sign();

    //verify
    sig.initVerify(publicKey);
    sig.update(chall);


    return sig.verify(signature);
  }

  /**
   * Returns the public ECDSA of this Account.
   *
   * @return the public ECDSA key of this Account
   */
  public PublicKey getPublicKey() {
    return publicKey;
  }

  public PrivateKey getPrivateKey() {
    // remove for production!!!
    return privateKey;
  }

  public Elections createElections(String question, String [] options) {
    return new Elections(publicKey, question, options);
  }
}
