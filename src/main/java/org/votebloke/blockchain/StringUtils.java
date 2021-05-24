package org.votebloke.blockchain;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

/** A set of utility functions for the votebloke.blockchain library. */
public class StringUtils {
  /**
   * Converts a string to a hex representation of its hashed bytes. First converts the text to bytes
   * then to hex.
   *
   * @param text the string to be converted.
   * @return the hex representation of byte-representation of the \code{text}
   */
  public static String hashString(String text) {
    try {
      final MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] hashedHeader = md.digest(text.getBytes(StandardCharsets.UTF_8));
      StringBuilder hexHash = new StringBuilder();
      for (byte b : hashedHeader) {
        String hexDigits = Integer.toHexString(0xff & b);
        if (hexDigits.length() == 1) {
          hexHash.append("0");
        }
        hexHash.append(hexDigits);
      }
      return hexHash.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Encodes a key to base64.
   *
   * @param key the public key to encode
   * @return the 64 base string encoded key
   */
  public static String keyToString(Key key) {
    if (key != null) {
      return Base64.getEncoder().encodeToString(key.getEncoded());
      } else {
      return "";
    }
  }

  /**
   * Converts a string encoded public key to a PublicKey object.
   *
   * @param string the provided base 64 encoded public key
   * @return the ECDSA public key
   * @throws NoSuchAlgorithmException if there is no provider of the ECDSA algorithm
   * @throws InvalidKeySpecException if the encoded public key does not match the ECDSA key factory
   */
  public static PublicKey stringToPublicKey(String string)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    X509EncodedKeySpec publicKeySpec =
        new X509EncodedKeySpec(Base64.getDecoder().decode(string.getBytes(StandardCharsets.UTF_8)));
    KeyFactory keyFactory = KeyFactory.getInstance("EC");
    return keyFactory.generatePublic(publicKeySpec);
  }

  /**
   * Encrypts data with a private ECDSA key.
   *
   * @param privateKey the ECDSA key used to encrypt data
   * @param data the data to be encrypted
   * @return the encrypted data
   */
  public static byte[] signWithEcdsa(PrivateKey privateKey, String data) {
    byte[] encryptedData;

    try {
      Signature dsa = Signature.getInstance("SHA256withECDSA");
      dsa.initSign(privateKey);
      dsa.update(data.getBytes());
      encryptedData = dsa.sign();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return encryptedData;
  }

  /**
   * Verifies that a signature matches a public ECDSA key.
   *
   * @param key the provided public ECDSA key to verify
   * @param data the data which was encrypted with a private key
   * @param signature the encrypted data
   * @return true if the public key matches the private key used to encrypt data; false otherwise
   */
  public static boolean verifyEcdsa(PublicKey key, String data, byte[] signature) {
    try {
      Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA");
      ecdsaVerify.initVerify(key);
      ecdsaVerify.update(data.getBytes());
      return ecdsaVerify.verify(signature);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Tests whether a key pair matches.
   *
   * @param privateKey the private key
   * @param publicKey the public key
   * @return true if keys match; false otherwise
   */
  public static boolean keysMatch(PrivateKey privateKey, PublicKey publicKey) {
    byte[] challenge = new byte[10000];
    ThreadLocalRandom.current().nextBytes(challenge);

    Signature signature;
    try {
      signature = Signature.getInstance("SHA256withECDSA");
      signature.initSign(privateKey);
      signature.update(challenge);
      byte[] encryptedChallenge = signature.sign();

      signature.initVerify(publicKey);
      signature.update(challenge);
      return signature.verify(encryptedChallenge);

    } catch (Exception e) {
      return false;
    }
  }
}
