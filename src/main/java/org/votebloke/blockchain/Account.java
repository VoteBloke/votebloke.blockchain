package org.votebloke.blockchain;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

/** A representation of a single agent. */
public class Account {
  /** The public ECDSA key for this Account. This also serves as means to identify this Account. */
  private final PublicKey publicKey;
  /** The private ECDSA key for this Account. */
  private final PrivateKey privateKey;

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
   * The constructor for the Account class.
   *
   * @param publicKey the public ECDSA key of this Account
   */
  public Account(PublicKey publicKey) {
    this(publicKey, null);
  }

  /**
   * A constructor for the Account class.
   *
   * <p>Accepts a UTF-8 decoded public ECDSA key, created with SECP256p1 algorithm, encoded in X509
   * standard, DER formatted.
   *
   * @param publicKey the public key created with SECP256p1 algorithm, encoded in X509 standard and
   *     in DER format
   */
  public Account(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
    this(StringUtils.stringToPublicKey(publicKey), null);
  }

  /**
   * Generates a private and public key pair using an elliptic curve algorithm (256 bits size).
   *
   * @return the generated KeyPair
   * @throws NoSuchAlgorithmException if no ECDSA algorithm is found on the client
   * @throws InvalidAlgorithmParameterException if the spec of the encryption algorithm 'secp256r1'
   *     is not found on the client
   */
  public static KeyPair generateKeys()
      throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
    kpg.initialize(new ECGenParameterSpec("secp256r1"), new SecureRandom());
    return kpg.generateKeyPair();
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

    if (getPrivateKey() != null) {
      electionTransaction.sign(getPrivateKey());
    }

    return electionTransaction;
  }

  /**
   * This Account votes in elections.
   *
   * @param answer the answer of this Account to the elections
   * @param elections the Elections
   * @param inputTransactions the array that contains the Transaction object with the elections.
   *     Must be of length 1.
   * @return the processed and signed Transaction with a vote
   */
  public Transaction vote(
      String answer, Elections elections, ArrayList<TransactionInput> inputTransactions) {
    Vote vote = new Vote(getPublicKey(), elections, answer);
    Transaction voteTransaction = new Transaction(getPublicKey(), vote, inputTransactions);

    if (getPrivateKey() != null) {
      voteTransaction.sign(getPrivateKey());
    }

    return voteTransaction;
  }

  /**
   * Tallies elections.
   *
   * <p>Takes input transactions, creates a transaction with a tally of a single Elections object
   * passed to it and returns the signed tally transaction.
   *
   * @param inputTransactions this list of TransactionInput objects to tally. The first element of
   *     this list is an input with an Elections object and the following inputs contain Vote
   *     objects.
   * @return the signed Transaction object with a Tally object
   */
  public Transaction tally(ArrayList<TransactionInput> inputTransactions) {
    Tally tally = new Tally(getPublicKey(), null, null);
    Transaction tallyTransaction = new Transaction(getPublicKey(), tally, inputTransactions);

    if (getPrivateKey() != null) {
      tallyTransaction.sign(getPrivateKey());
    }

    return tallyTransaction;
  }

  private PrivateKey getPrivateKey() {
    return privateKey;
  }
}
