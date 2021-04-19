package blockchain;

/** A representation of the inputs for Transaction. */
public class TransactionInput {
  /**
   * The reference to a Transaction object associated with TransactionOutput objects needed to
   * process a new Transaction.
   */
  String transactionOutputId;

  /** The TransactionOutput object behind this TransactionInput */
  TransactionOutput transactionOut;

  TransactionInput(String transactionOutputId) {
    this.transactionOutputId = transactionOutputId;
  }
}
