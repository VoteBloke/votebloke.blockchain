package org.votebloke.blockchain;

import java.security.PublicKey;

/** A representation of the outputs of a Transaction object. */
public class TransactionOutput {
  /** The public key this TransactionOutput was addressed from. */
  PublicKey author;
  /** Data associated with this TransactionOutput. */
  Entry data;
  /** The id of the Transaction object, which created this TransactionOutput. */
  String parentTransactionId;

  TransactionOutput(PublicKey author, Entry data, String parentTransactionId) {
    this.author = author;
    this.data = data;
    this.parentTransactionId = parentTransactionId;
  }

  TransactionOutput(PublicKey author, Entry data) {
    this(author, data, null);
  }

  public TransactionOutput(Transaction transaction) {
    this(transaction.signee, transaction.data, transaction.getId());
  }

  boolean isAddressedFrom(PublicKey key) {
    return key == author;
  }

  public void setParentTransactionId(String parentTransactionId) {
    this.parentTransactionId = parentTransactionId;
  }

  public String getParentTransactionId() {
    return this.parentTransactionId;
  }

  public Entry getData() {
    return this.data;
  }
}
