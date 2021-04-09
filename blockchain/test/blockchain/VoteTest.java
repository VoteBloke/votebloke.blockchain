package blockchain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

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
        assertDoesNotThrow(() -> new Vote(voter));
    }
}