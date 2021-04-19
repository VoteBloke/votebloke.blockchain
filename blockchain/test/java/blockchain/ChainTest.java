package blockchain;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChainTest {
  Chain blockchain;

  @BeforeEach
  void setUp() {
    Block genesisBlock = new Block("0", "v1", 0);
    genesisBlock.mineHash();
    blockchain = new Chain(genesisBlock);
  }

  @Test
  void isChainValid_OnlyGenesis() {
    // Chain with only the genesisBlock is valid
    assertTrue(blockchain.isChainValid());
  }

  @Test
  void isChainValid_WrongPreviousHash() {
    // Chain with a block with a wrong previousHash is not valid
    Block newBlock = new Block("something", "v1", 0);
    newBlock.mineHash();
    blockchain.addBlock(newBlock);
    assertFalse(blockchain.isChainValid());
  }

  @Test
  void addBlock_InvalidBlocksNotAdded() {
    // Invalid blocks are not added to the Chain
    int oldLength = blockchain.size();
    Block invalidBlock = new Block(blockchain.getLatestBlockHash(), "v1", 0);
    assertFalse(blockchain.addBlock(invalidBlock));
    assertEquals(oldLength, blockchain.size());
  }

  @Test
  void addBlock_ReturnsTrueIfAdded() {
    // addBlock returns true if the block is added and chain length is incremented
    Block validBlock = new Block(blockchain.getLatestBlockHash(), "v1", 0);
    validBlock.mineHash();
    assertTrue(blockchain.addBlock(validBlock));
  }

  @Test
  void addBlock_UpdatesLatestHash() {
    // addBlock updates the latestHash field
    Block newBlock = new Block(blockchain.getLatestBlockHash(), "v1", 0);
    newBlock.mineHash();
    blockchain.addBlock(newBlock);
    assertEquals(newBlock.getHash(), blockchain.getLatestBlockHash());
  }

  @Test
  void addBlock_IncrementsChainLength() {
    // addBlock increments the chain length
    Block newBlock = new Block(blockchain.getLatestBlockHash(), "v1", 0);
    newBlock.mineHash();
    int oldSize = blockchain.size();
    blockchain.addBlock(newBlock);
    assertEquals(oldSize + 1, blockchain.size());
    assertEquals(2, blockchain.size());
  }
}
