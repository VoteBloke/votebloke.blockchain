package blockchain;

import java.security.PrivateKey;
import java.security.PublicKey;

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

  /**
   * Returns the public ECDSA of this Account.
   *
   * @return the public ECDSA key of this Account
   */
  public PublicKey getPublicKey() {
    return publicKey;
  }
}