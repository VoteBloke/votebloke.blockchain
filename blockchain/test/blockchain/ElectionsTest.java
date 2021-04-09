package blockchain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

public class ElectionsTest {
    PublicKey author;
    {
        try {
            author = KeyPairGenerator.getInstance("DSA").generateKeyPair().getPublic();
        } catch (
        NoSuchAlgorithmException e) {
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
    public void electionsNullIdIfNotProcessed() {
        assertEquals(null, elections.getId());
    }
}