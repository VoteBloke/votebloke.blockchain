package blockchain;

import java.security.PrivateKey;
import java.security.PublicKey;

public class Account {
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public Account(PublicKey publicKey, PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }
}
