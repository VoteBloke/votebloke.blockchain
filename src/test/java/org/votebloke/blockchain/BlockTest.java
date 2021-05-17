package org.votebloke.blockchain;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BlockTest {
  Block block;

  @BeforeEach
  void setUp() {
    block = new Block("0", "v1", 0, null);
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
  void getUnconsumedOutputs() {
    Transaction testTransaction = new Transaction(null, null, null);
    Block block =
        new Block(
            "previousHash", "v1", 0, null, new ArrayList<Transaction>(List.of(testTransaction)));

    Assertions.assertArrayEquals(
        (new ArrayList<Transaction>(List.of(testTransaction))).toArray(),
        block.getUnsignedTransactions().toArray());
  }
}
