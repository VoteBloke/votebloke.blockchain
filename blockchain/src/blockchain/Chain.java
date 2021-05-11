package blockchain;

import java.util.ArrayList;

/**
 * Represents the blockchain. Performs following operations:
 *
 * <ul>
 *   <li>Adding a Block to this blockchain.
 *   <li>Validating this blockchain.
 * </ul>
 */
public class Chain {
  private final ArrayList<Block> blockchain = new ArrayList<>();
  private String latestBlockHash;

  /**
   * Represents a blockchain.
   *
   * @param genesisBlock the first Block in the blockchain
   */
  public Chain(Block genesisBlock) {
    blockchain.add(genesisBlock);
    this.latestBlockHash = genesisBlock.getHash();
  }

  /**
   * Validates this blockchain.
   *
   * @return true if this blockchain is valid, false otherwise
   */
  public boolean isChainValid() {
    try {
      if (!blockchain.get(0).isBlockValid()) {
        return false;
      }
      for (int i = 1; i < blockchain.size(); i++) {
        Block block = blockchain.get(i);
        Block previousBlock = blockchain.get(i - 1);
        if (!block.isBlockValid()) {
          return false;
        }
        if (!previousBlock.getHash().equals(block.getPreviousHash())) {
          return false;
        }
      }
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Adds a Block to this Chain.
   *
   * @param block the valid Block to add
   * @return true if the Block was added successfully; false otherwise
   */
  public boolean addBlock(Block block) {
    if (block.isBlockValid()) {
      blockchain.add(block);
      latestBlockHash = block.getHash();
      return true;
    } else {
      return false;
    }
  }

  /**
   * Returns the hash of the newest block in this Chain.
   *
   * @return the hash of the newest block in this chain
   */
  public String getLatestBlockHash() {
    return latestBlockHash;
  }

  /**
   * Return the length of this Chain.
   *
   * @return the length of this Chain (including the genesis block)
   */
  public int size() {
    return blockchain.size();
  }

  /**
   * Returns the Block from this Chain.
   *
   * @param position the position of the Block to return
   * @return the Block from this Chain
   */
  public Block getBlockAt(int position) {
    return blockchain.get(position);
  }
}
