package com.tsystems.parser.email.model;

import com.tsystems.monitoring.crypto.CryptoEngine;

import java.util.Optional;
import java.util.Properties;

public class MailboxProperties {

    private String exchangeHost;
    private String userName;
    private String password;
    private String domain;
    private String emailAddress;


    public String getExchangeHost() {
        return exchangeHost;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getDomain() {
        return domain;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MailboxProperties{");
        sb.append("exchangeHost='").append(exchangeHost).append('\'');
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", domain='").append(domain).append('\'');
        sb.append(", emailAddress='").append(emailAddress).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static class MailboxPropertiesBuilder {

        private Properties properties;
        private String exchangeHost;
        private String userName;
        private String password;
        private String domain;
        private String emailAddress;

        public MailboxPropertiesBuilder(Properties properties) {
            this.properties = properties;
        }

        public MailboxPropertiesBuilder setExchangeHost(String exchangeHost) {
            this.exchangeHost = exchangeHost;
            return this;
        }

        public MailboxPropertiesBuilder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public MailboxPropertiesBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public MailboxPropertiesBuilder setDomain(String domain) {
            this.domain = domain;
            return this;
        }

        public MailboxPropertiesBuilder setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

        public Properties getProperties() {
            return properties;
        }

        public MailboxProperties build() {
            return new MailboxProperties(this);
        }
    }

    private MailboxProperties(MailboxPropertiesBuilder builder) {
        Properties properties = builder.getProperties();

        Optional<String> encryptedPwd = CryptoEngine.
                decryptEncryptedPasswordWithKey(
                        properties.getProperty("ENC(PWD)"),
                        properties.getProperty("ENCRYPT_KEY"));

        this.exchangeHost = properties.getProperty("HOST");
        this.userName = properties.getProperty("USER_NAME");
        this.password = encryptedPwd.isPresent() ? encryptedPwd.get() : "";
        this.domain = properties.getProperty("DOMAIN");
        this.emailAddress = properties.getProperty("EMAIL_ADDRESS");

    }

}
