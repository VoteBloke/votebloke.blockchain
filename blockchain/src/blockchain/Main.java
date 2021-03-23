package blockchain;

import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        Block b = new Block();
        System.out.println(b);
        System.out.println(b.calculateHash());
    }
}
