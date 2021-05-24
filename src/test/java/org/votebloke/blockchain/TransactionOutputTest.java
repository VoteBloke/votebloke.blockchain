package org.votebloke.blockchain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.event.annotation.BeforeTestExecution;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

class TransactionOutputTest {
  Elections testElections;
  PublicKey author;

  @BeforeEach
  void setUpFields() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
    author = Account.generateKeys().getPublic();
    testElections = new Elections(author);
  }

  @Test
  void constructorsDoNotThrow() {
    Assertions.assertDoesNotThrow(
        () ->
            new TransactionOutput(
                author, testElections, "parentTransactionsId"));
    Assertions.assertDoesNotThrow(
        () -> new TransactionOutput(author, testElections));
  }

  @Test
  void isAddressedMatchesAuthor() {
    TransactionOutput to = new TransactionOutput(author, testElections);
    Assertions.assertTrue(to.isAddressedFrom(author));
  }

  @Test
  void getDataMatchesPassedData() {
    TransactionOutput to = new TransactionOutput(author, testElections);
    Assertions.assertEquals(testElections, to.getData());
  }
}
