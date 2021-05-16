package org.votebloke.blockchain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TransactionInputTest {
  TransactionInput transactionInput;
  TransactionOutput transactionOutput = new TransactionOutput(new Transaction(null, null, null));

  @Test
  void getTransactionOutReturnsExpectedObject() {
    transactionInput = new TransactionInput(transactionOutput);
    Assertions.assertEquals(transactionOutput, transactionInput.getTransactionOut());
  }
}
