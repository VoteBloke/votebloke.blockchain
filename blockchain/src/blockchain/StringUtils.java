package blockchain;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.util.Base64;

class StringUtils {
    /** Converts a string to a hex representation of its bytes.
     * First converts the text to bytes then to hex.
     *
     * @param text the string to be converted.
     * @return the hex representation of byte-representation of the \code{text}
     */
    public static String stringToHex(String text) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedHeader = md.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexHash = new StringBuilder();
            for (byte b : hashedHeader) {
                String hexDigits = Integer.toHexString(0xff & b);
                if(hexDigits.length() == 1) hexHash.append("0");
                hexHash.append(hexDigits);
            }
            return hexHash.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String keyToString(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static byte[] signWithEcdsa(PrivateKey privateKey, String data) {
        return new byte[] {};
    }
}
