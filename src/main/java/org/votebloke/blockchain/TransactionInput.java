package org.votebloke.blockchain;

/** A representation of the inputs for Transaction. */
public class TransactionInput {
  /** The TransactionOutput object behind this TransactionInput. */
  TransactionOutput transactionOut;

  public TransactionInput(TransactionOutput transactionOut) {
    this.transactionOut = transactionOut;
  }

  public TransactionInput(Transaction transaction) {
    this.transactionOut = new TransactionOutput(transaction);
  }

  public TransactionOutput getTransactionOut() {
    return transactionOut;
  }
}
