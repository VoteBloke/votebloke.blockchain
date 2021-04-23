package blockchain;

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

  boolean isAddressedFrom(PublicKey key) {
    return key == author;
  }
}
