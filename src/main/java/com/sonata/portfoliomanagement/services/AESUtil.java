package com.sonata.portfoliomanagement.services;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AESUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int IV_SIZE = 12;
    private static final int TAG_SIZE = 128;
    private static final int SALT_SIZE = 16;
    private static final int KEY_SIZE = 256;
    private static final int ITERATIONS = 65536;

    // the approach usde here is to use a combination of a master key and
    // a randomly generated salt.
    // The salt is unique for each encryption operation
    // and is used to derive a unique key for each encryption.
    // The salt and the encrypted data are then stored together,
    // while the master key is kept secure
    // (e.g., in an environment variable or a secure vault)
    private static final String MASTER_KEY = "SuperSecretMasterKey"; // Ensure this key is securely stored
    private static final Logger LOGGER = Logger.getLogger(AESUtil.class.getName());

    private static SecretKeySpec deriveKey(byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(MASTER_KEY.toCharArray(), salt, ITERATIONS, KEY_SIZE);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public static String encrypt(String input) throws Exception {
        try {
            // Generate a random salt
            byte[] salt = new byte[SALT_SIZE];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);

            // Derive the key using the salt
            SecretKeySpec key = deriveKey(salt);

            // Generate a random IV
            byte[] iv = new byte[IV_SIZE];
            random.nextBytes(iv);

            // Initialize the cipher for encryption
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_SIZE, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);

            // Encrypt the input
            byte[] cipherText = cipher.doFinal(input.getBytes());

            // Combine salt, IV, and cipherText
            byte[] encrypted = new byte[salt.length + iv.length + cipherText.length];
            System.arraycopy(salt, 0, encrypted, 0, salt.length);
            System.arraycopy(iv, 0, encrypted, salt.length, iv.length);
            System.arraycopy(cipherText, 0, encrypted, salt.length + iv.length, cipherText.length);

            // Encode to Base64
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Encryption failed", e);
            throw new Exception("Encryption failed", e);
        }
    }

    public static String decrypt(String encrypted) throws Exception {
        try {
            // Decode from Base64
            byte[] decoded = Base64.getDecoder().decode(encrypted);

            // Extract the salt
            byte[] salt = new byte[SALT_SIZE];
            System.arraycopy(decoded, 0, salt, 0, salt.length);

            // Derive the key using the extracted salt
            SecretKeySpec key = deriveKey(salt);

            // Extract the IV
            byte[] iv = new byte[IV_SIZE];
            System.arraycopy(decoded, salt.length, iv, 0, iv.length);

            // Extract the cipherText
            byte[] cipherText = new byte[decoded.length - salt.length - iv.length];
            System.arraycopy(decoded, salt.length + iv.length, cipherText, 0, cipherText.length);

            // Initialize the cipher for decryption
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_SIZE, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);

            // Decrypt the cipherText
            byte[] plainText = cipher.doFinal(cipherText);

            // Convert to string
            return new String(plainText);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Decryption failed", e);
            throw new Exception("Decryption failed", e);
        }
    }
}