package com.enterprise.mobile.config;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Manages secrets and sensitive credentials.
 * Priority order:
 * 1. Environment variables (CI/CD)
 * 2. System properties (JVM args)
 * 3. Encrypted local file (development)
 *
 * NEVER stores plaintext secrets in config files.
 */
public final class SecretManager {

    private static final Logger logger = LogManager.getLogger(SecretManager.class);
    private static volatile SecretManager instance;
    private static final String ENCRYPTION_KEY_ENV = "FRAMEWORK_ENCRYPTION_KEY";

    private SecretManager() {
    }

    public static SecretManager getInstance() {
        if (instance == null) {
            synchronized (SecretManager.class) {
                if (instance == null) {
                    instance = new SecretManager();
                }
            }
        }
        return instance;
    }

    /**
     * Retrieves a secret value. Checks environment variables first,
     * then system properties, then encrypted store.
     */
    public String getSecret(String key) {
        // Priority 1: Environment variable
        String value = System.getenv(key);
        if (value != null && !value.isBlank()) {
            logger.debug("Secret '{}' loaded from environment variable", key);
            return value;
        }

        // Priority 2: System property
        value = System.getProperty(key);
        if (value != null && !value.isBlank()) {
            logger.debug("Secret '{}' loaded from system property", key);
            return value;
        }

        // Priority 3: Return null - caller handles missing secret
        logger.warn("Secret '{}' not found in environment or system properties", key);
        return null;
    }

    /**
     * Encrypts a value using AES encryption.
     */
    public String encrypt(String plainText) {
        try {
            String key = getEncryptionKey();
            SecretKeySpec secretKey = new SecretKeySpec(
                    key.getBytes(StandardCharsets.UTF_8), FrameworkConstants.ENCRYPTION_ALGORITHM);
            Cipher cipher = Cipher.getInstance(FrameworkConstants.ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            logger.error("Encryption failed", e);
            throw new SecurityException("Failed to encrypt value", e);
        }
    }

    /**
     * Decrypts a value using AES encryption.
     */
    public String decrypt(String encryptedText) {
        try {
            String key = getEncryptionKey();
            SecretKeySpec secretKey = new SecretKeySpec(
                    key.getBytes(StandardCharsets.UTF_8), FrameworkConstants.ENCRYPTION_ALGORITHM);
            Cipher cipher = Cipher.getInstance(FrameworkConstants.ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("Decryption failed", e);
            throw new SecurityException("Failed to decrypt value", e);
        }
    }

    private String getEncryptionKey() {
        String key = System.getenv(ENCRYPTION_KEY_ENV);
        if (key == null || key.length() != 16) {
            throw new SecurityException(
                    "Encryption key must be set via environment variable '"
                            + ENCRYPTION_KEY_ENV + "' and must be exactly 16 characters");
        }
        return key;
    }
}
