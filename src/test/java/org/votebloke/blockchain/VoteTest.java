package org.votebloke.blockchain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VoteTest {
  PublicKey voter;

  {
    try {
      voter = KeyPairGenerator.getInstance("DSA").generateKeyPair().getPublic();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }

  Vote vote;

  @BeforeEach
  void setUp() {
    vote = new Vote(voter);
  }

  @Test
  void voteConstructors() {
    Elections elections = new Elections(voter);
    assertDoesNotThrow(() -> new Vote(voter));
    assertDoesNotThrow(() -> new Vote(voter, elections));
  }
}
