package org.votebloke.blockchain;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
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
  void constructorsDoNotThrow() {
    Assertions.assertDoesNotThrow(() -> new Account(null, null));
    Assertions.assertDoesNotThrow(() -> new Account(Account.generateKeys().getPublic()));
    Assertions.assertDoesNotThrow(
        () ->
            new Account(
                "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEiGe"
                    + "OT38/VIf5VdBFRwI8eQga86vzR3q4Ks1RPtT/PInoSZO2"
                    + "fw84kKsL1ZQfmx9TP0KgUcpQLJmkcnfIQQxyuA=="));
  }

  @Test
  void constructorAcceptsBase64EncodedPublicKeyInDerFormat() {
    Assertions.assertDoesNotThrow(
        () ->
            new Account(
                "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE34xUy"
                    + "5Kwf6iywVGAjFipmGA7cyboeclnD7VOxiVqC5fTxfzDCt"
                    + "+3oDOPx0EVKJJfRCJ4v8CIPNLqxA+bLSdhfA=="));
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

  @Test
  void voteTestReturnsValidTransaction() {
    Transaction electionsTransaction =
        ac.createElections("test question", new String[] {"answer1", "answer2"});
    Transaction voteTransaction =
        ac.vote(
            "answer1",
            (Elections) electionsTransaction.data,
            new ArrayList<TransactionInput>(List.of(new TransactionInput(electionsTransaction))));
    Assertions.assertTrue(voteTransaction.validate());
  }

  @Test
  void publicKeyOfAccountConstructedWithStringMatchesOriginalKey()
      throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, InvalidKeySpecException {
    PublicKey publicKey = Account.generateKeys().getPublic();
    Account account = new Account(StringUtils.keyToString(publicKey));
    Assertions.assertEquals(publicKey, account.getPublicKey());
  }
}
