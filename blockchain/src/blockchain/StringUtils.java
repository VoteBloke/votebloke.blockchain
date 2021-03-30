package blockchain;

import java.nio.charset.StandardCharsets;
import java.security.*;
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

    /** Encrypts data with a private ECDSA key.
     *
     * @param privateKey the ECDSA key used to encrypt data
     * @param data the data to be encrypted
     * @return the encrypted data
     */
    public static byte[] signWithEcdsa(PrivateKey privateKey, String data) {
        byte[] encryptedData;

        try {
            Signature dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            dsa.update(data.getBytes());
            encryptedData = dsa.sign();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }

        return encryptedData;
    }

    /** Verifies that a signature matches a public ECDSA key.
     *
     * @param key the provided public ECDSA key to verify
     * @param data the data which was encrypted with a private key
     * @param signature the encrypted data
     * @return true if the public key matches the private key used to encrypt data; false otherwise
     */
    public static boolean verifyEcdsa(PublicKey key, String data, byte[] signature) {
        try{
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(key);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
