package blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] ret = md.digest("A word".getBytes(StandardCharsets.UTF_8));
        System.out.print(ret);
    }
}
