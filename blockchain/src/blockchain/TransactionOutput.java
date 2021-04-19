package blockchain;

import java.security.PublicKey;

/** A representation of the outputs of a Transaction object. */
public class TransactionOutput {
  /** The id of this Transaction. */
  String id;
  /** The public key this TransactionOutput was addressed to. */
  PublicKey recipient;
  /** Data associated with this TransactionOutput. */
  Entry data;
  /** The id of the Transaction object, which created this TransactionOutput. */
  String parentTransactionId;

  TransactionOutput(PublicKey recipient, Entry data, String parentTransactionId) {
    this.recipient = recipient;
    this.data = data;
    this.parentTransactionId = parentTransactionId;
  }

  boolean isAddressedTo(PublicKey key) {
    return key == recipient;
  }
}
