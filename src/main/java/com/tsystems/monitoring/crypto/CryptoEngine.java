package com.tsystems.monitoring.crypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

public class CryptoEngine {
    private static Logger logger = LoggerFactory.getLogger(CryptoEngine.class);

    private static final String ALGORITHM = "AES";
    private static final String CHARSET = "UTF-8";

    private CryptoEngine() {
    }

    public static Optional<String> encryptPasswordWithKey(final String password, final String encryptionKey) {
        byte[] encryptedPwd = null;
        try {
            SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(CHARSET), ALGORITHM);
            byte[] passwordBytes = password.getBytes(CHARSET);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            encryptedPwd = Base64.getEncoder().encode(cipher.doFinal(passwordBytes));

        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | IOException | InvalidKeyException ex) {
            ex.printStackTrace();
            logger.error(ex.getLocalizedMessage());
        }
        return Optional.ofNullable(new String(encryptedPwd));
    }

    public static Optional<String> decryptEncryptedPasswordWithKey(final String encryptedPassword, final String encryptionKey) {
        String password = null;
        try {

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            final SecretKeySpec secretKey = new SecretKeySpec(encryptionKey.getBytes(CHARSET), ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] plainTextPwdBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));

            password = new String(plainTextPwdBytes);

        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | IOException | InvalidKeyException ex) {
            ex.printStackTrace();
            logger.error(ex.getLocalizedMessage());
        }

        return Optional.ofNullable(password);

    }
}
