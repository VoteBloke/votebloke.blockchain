package org.votebloke.blockchain;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TransactionInputTest {
  TransactionInput transactionInput;
  TransactionOutput transactionOutput =
      new TransactionOutput(
          new Transaction(
              Account.generateKeys().getPublic(),
              new Elections(Account.generateKeys().getPublic(), "test", new String[] {"a1"}),
              null));

  TransactionInputTest() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {}

  @Test
  void getTransactionOutReturnsExpectedObject() {
    transactionInput = new TransactionInput(transactionOutput);
    Assertions.assertEquals(transactionOutput, transactionInput.getTransactionOut());
  }
}
