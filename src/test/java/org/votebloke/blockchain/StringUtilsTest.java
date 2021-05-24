package org.votebloke.blockchain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

class StringUtilsTest {
  @Test
  void testKeyPairMatch() {
    try {
      KeyPair kpg = Account.generateKeys();
      Assertions.assertTrue(StringUtils.keysMatch(kpg.getPrivate(), kpg.getPublic()));

      KeyPair kpg2 = Account.generateKeys();
      Assertions.assertFalse(StringUtils.keysMatch(kpg.getPrivate(), kpg2.getPublic()));
      Assertions.assertFalse(StringUtils.keysMatch(kpg2.getPrivate(), kpg.getPublic()));
    } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
      e.printStackTrace();
      Assertions.fail();
    }
  }

  @Test
  void testStringToPublicKeyMatchesTheKey()
      throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, InvalidKeySpecException {
    PublicKey publicKey = Account.generateKeys().getPublic();
    String encodedKey = StringUtils.keyToString(publicKey);
    PublicKey decodedKey = StringUtils.stringToPublicKey(encodedKey);
    Assertions.assertEquals(publicKey, decodedKey);
  }
}
