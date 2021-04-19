package blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

class StringUtils {
    /**
     * Converts a string to a hex representation of its bytes.
     * First converts the text to bytes then to hex.
     *
     * @param text A string to be converted.
     * @return Hex representation of byte-representation of the \code{text}.
     */
    public static String hashString(String text) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedHeader = md.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexHash = new StringBuilder();
            for (byte b : hashedHeader) {
                String hexDigits = Integer.toHexString(0xff & b);
                if (hexDigits.length() == 1) hexHash.append("0");
                hexHash.append(hexDigits);
            }
            return hexHash.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
