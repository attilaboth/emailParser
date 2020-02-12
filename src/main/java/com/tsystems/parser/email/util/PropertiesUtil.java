package com.tsystems.parser.email.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesUtil {

    private static final String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    private PropertiesUtil() {
    }

    public static Properties parsePropertiesFile(String filePath) {
        Properties properties = new Properties();
        System.out.println("path:" + path);
        File propertiesFile = new File(path.concat(filePath));
        //File propertiesFile = new File(filePath.trim());

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(propertiesFile))) {
            properties.load(bis);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return properties;
    }

}
