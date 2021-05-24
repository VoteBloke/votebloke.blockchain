package org.votebloke.blockchain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

class TransactionTest {

  KeyPair keyPair;
  Elections elections;

  @BeforeEach
  void setUp() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
    keyPair = Account.generateKeys();
    elections = new Elections(keyPair.getPublic(), "test", new String[] {"ans1"});
  }

  @Test
  void transactionConstructorsDoNotThrow() {
    Assertions.assertDoesNotThrow(() -> new Transaction(keyPair.getPublic(), elections, null));
  }

  @Test
  void newlyConstructedTransactionHasId() {
    Transaction transaction = new Transaction(keyPair.getPublic(), elections, null);
    Assertions.assertNotNull(transaction.getId());
  }
}
