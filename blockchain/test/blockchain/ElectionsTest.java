package blockchain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ElectionsTest {
  PublicKey author;

  {
    try {
      author = KeyPairGenerator.getInstance("DSA").generateKeyPair().getPublic();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }

  Elections elections;

  @BeforeEach
  void setUp() {
    elections = new Elections(author);
  }

  @Test
  public void electionsConstructors() {
    assertDoesNotThrow(() -> new Elections(author));
    assertDoesNotThrow(() -> new Elections(author, "Test question"));
  }

  @Test
  public void electionsNullIdIfNotProcessed() throws NoSuchAlgorithmException {
    assertEquals(null, elections.getId());
  }
}
