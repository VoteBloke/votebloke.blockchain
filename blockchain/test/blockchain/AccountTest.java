package blockchain;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountTest {
  Account ac;
  Block block;

  @BeforeEach
  void setup() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
    ac = Account.createAccount();
    block = new Block("0", "v1", 0, null);
  }

  @Test
  void createElectionsTestReturnsValidTransaction() {
    Transaction electionsTransaction =
        ac.createElections("test question", new String[] {"answer1", "answer2"});
    Assertions.assertTrue(electionsTransaction.validate());
  }

  @Test
  void createElectionsTestAddingToBlockDoesNotThrow() {
    Transaction electionsTransaction =
        ac.createElections("test question", new String[] {"answer1", "answer2"});
    Assertions.assertDoesNotThrow(() -> block.addTransaction(electionsTransaction));
  }
}
