package com.tsystems.monitoring.crypto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class CryptoEngineTest {

    static Stream<Arguments> passwordAndKeyArguments() {
        return Stream.of(Arguments.of("Dash2018Boards_10/2018", "8912082589120825", "/F5hqqcMfIDbxjHb24ttssIfzCQ9qdutYd8kWdjmn6Y="));
    }

    @ParameterizedTest
    @MethodSource("passwordAndKeyArguments")
    @DisplayName("encryptPasswordWithKey")
    void encryptPasswordWithKey(String pwd, String key, String encPwd) {

        Optional<String> encryptedPwd = CryptoEngine.encryptPasswordWithKey(pwd, key);
        assertNotNull(encryptedPwd.get());
        assertEquals(encPwd, encryptedPwd.get());
    }

    @ParameterizedTest
    @MethodSource("passwordAndKeyArguments")
    @DisplayName("decryptEncryptedPasswordWithKey")
    void decryptEncryptedPasswordWithKey(String pwd, String key, String encPwd) {

        Optional<String> decryptedPassword = CryptoEngine.decryptEncryptedPasswordWithKey(encPwd, key);
        assertNotNull(decryptedPassword.get());
        assertEquals(pwd, decryptedPassword.get());

    }
}
