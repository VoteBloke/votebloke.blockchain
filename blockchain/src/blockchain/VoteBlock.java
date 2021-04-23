package blockchain;

import java.util.List;

public class VoteBlock extends Block {
  /**
   * Constructor for the Block class.
   *
   * @param previousHash the hash of the previous Block
   * @param blockVersion the version of this Block
   * @param miningDifficulty the mining difficulty of this Block
   * @param unconsumedTransactions the list of unconsumed TransactionOutputs
   */
  public VoteBlock(
      String previousHash,
      String blockVersion,
      int miningDifficulty,
      List<TransactionOutput> unconsumedTransactions) {
    super(previousHash, blockVersion, miningDifficulty, unconsumedTransactions);
  }
}
