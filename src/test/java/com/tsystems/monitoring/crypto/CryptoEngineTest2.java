package com.tsystems.monitoring.crypto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CryptoEngineTest2 {

    private final String PWD = "Dash2018Boards_10/2018";
    private final String KEY = "8912082589120825";
    private final String ENC_PWD = "/F5hqqcMfIDbxjHb24ttssIfzCQ9qdutYd8kWdjmn6Y=";

    @Test
    @DisplayName("encryptPasswordWithKey2")
    void encryptPasswordWithKey() {

        Optional<String> encryptedPwd = CryptoEngine.encryptPasswordWithKey(PWD, KEY);
        assertNotNull(encryptedPwd.get());
        assertEquals(ENC_PWD, encryptedPwd.get());
    }

    @Test
    @DisplayName("decryptEncryptedPasswordWithKey2")
    void decryptEncryptedPasswordWithKey() {

        Optional<String> decryptedPassword = CryptoEngine.decryptEncryptedPasswordWithKey(ENC_PWD, KEY);
        assertNotNull(decryptedPassword.get());
        assertEquals(PWD, decryptedPassword.get());

    }

}
