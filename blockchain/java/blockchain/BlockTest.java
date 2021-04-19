package blockchain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BlockTest {
    Block block;

    @BeforeEach
    void setUp() {
        block = new Block("0", "v1", 0);
    }

    @Test
    void getId() {
        Assertions.assertEquals(64, block.getId().length());
    }

    @Test
    void addTransaction() {
    }

    @Test
    void mineHash() {
        Assertions.assertEquals("", block.getHash());
        block.mineHash();
        Assertions.assertEquals(64, block.getHash().length());
    }
}
