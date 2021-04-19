package blockchain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BlockTest {
  Block block;

  @BeforeEach
  void setUp() {
    block = new Block("0", "v1", 0);
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
}
