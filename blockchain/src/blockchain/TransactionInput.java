package blockchain;

/** A representation of the inputs for Transaction. */
public class TransactionInput {
  /** The TransactionOutput object behind this TransactionInput. */
  TransactionOutput transactionOut;

  TransactionInput(TransactionOutput transactionOut) {
    this.transactionOut = transactionOut;
  }

  TransactionInput(Transaction transaction) {
    this.transactionOut = new TransactionOutput(transaction);
  }
}
