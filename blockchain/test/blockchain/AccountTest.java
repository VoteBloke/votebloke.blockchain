package blockchain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class AccountTest {
    Account ac;
    Block block;

    @BeforeEach
    void setup() throws NoSuchAlgorithmException, IOException {
        ac = Account.createAccount();
        block = new Block("0", "v1", 0);
    }

    @Test
    boolean doKeysMatch() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return ac.validatePrivate(ac.getPrivateKey());
    }

    @Test
    void startElections() {
        String[] opts = {"ledwo", "nie da sie"};
        ac.createElections("jak zyc...", opts);
    }
}
