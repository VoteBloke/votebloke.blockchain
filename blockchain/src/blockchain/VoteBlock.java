package blockchain;

public class VoteBlock extends Block {
  /**
   * Constructor for the Block class.
   *
   * @param previousHash the hash of the previous Block
   * @param blockVersion the version of this Block
   * @param miningDifficulty the mining difficulty of this Block
   */
  public VoteBlock(String previousHash, String blockVersion, int miningDifficulty) {
    super(previousHash, blockVersion, miningDifficulty);
  }
}
