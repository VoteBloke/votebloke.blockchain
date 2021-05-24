package org.votebloke.blockchain;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BlockTest {
  Block block;
  KeyPair keyPair;

  @BeforeEach
  void setUp() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
    block = new Block("0", "v1", 0, null);
    keyPair = Account.generateKeys();
  }

  @Test
  void getId() {
    Assertions.assertEquals(64, block.getId().length());
  }

  @Test
  void addTransaction() {}

  @Test
  void mineHash_IsOfLength64() {
    Assertions.assertEquals("", block.getHash());
    block.mineHash();
    Assertions.assertEquals(64, block.getHash().length());
  }

  @Test
  void isBlockValid_NoHashNotValid() {
    // Block with no hash is not valid
    Assertions.assertFalse(block.isBlockValid());
  }

  @Test
  void isBlockValid_MinedIsValid() {
    // Mined block is valid
    block.mineHash();
    Assertions.assertTrue(block.isBlockValid());
  }

  @Test
  void constructorDoesNotThrow() {
    Assertions.assertDoesNotThrow(() -> new Block("previousHash", "v1", 0, null, null));
  }

  @Test
  void getUnsignedTransactionsReturnsItemsPassedInConstructor() {
    Transaction testTransaction = new Transaction(null, null, null);
    Block block =
        new Block(
            "previousHash", "v1", 0, null, new ArrayList<Transaction>(List.of(testTransaction)));

    Assertions.assertArrayEquals(
        (new ArrayList<Transaction>(List.of(testTransaction))).toArray(),
        block.getUnsignedTransactions(null).toArray());
  }

  @Test
  void getUnsignedTransactionsUpdatedAfterAddingTransaction() {
    Elections testElections =
        new Elections(keyPair.getPublic(), "testQuestion", new String[] {"a1"});
    Transaction testTransaction = new Transaction(keyPair.getPublic(), testElections, null);
    block.addTransaction(testTransaction);

    Assertions.assertEquals(testTransaction, block.getUnsignedTransactions().get(0));
  }

  @Test
  void getUnsignedTransactionsEmptyAfterSigningLastTransaction() {
    Elections testElections =
        new Elections(keyPair.getPublic(), "testQuestion", new String[] {"a1"});
    Transaction testTransaction = new Transaction(keyPair.getPublic(), testElections, null);
    Block testBlock =
        new Block("previousHash", "v1", 0, null, new ArrayList<>(List.of(testTransaction)));

    String signedData =
        new String(
            Base64.encodeBase64(
                StringUtils.signWithEcdsa(keyPair.getPrivate(), testTransaction.getSignData())));

    Assertions.assertDoesNotThrow(
        () -> testBlock.signTransaction(testTransaction.getId(), signedData));

    Assertions.assertEquals(0, testBlock.getUnsignedTransactions().size());
    Assertions.assertEquals(1, testBlock.getTransactions().size());
  }
}
