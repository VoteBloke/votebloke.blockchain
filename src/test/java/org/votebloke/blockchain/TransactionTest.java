package org.votebloke.blockchain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TransactionTest {

  @Test
  void transactionConstructorsDoNotThrow() {
    Assertions.assertDoesNotThrow(() -> new Transaction(null, null, null));
  }

  @Test
  void newlyConstructedTransactionHasId() {
    Transaction transaction = new Transaction(null, null, null);
    Assertions.assertNotNull(transaction.getId());
  }
}
