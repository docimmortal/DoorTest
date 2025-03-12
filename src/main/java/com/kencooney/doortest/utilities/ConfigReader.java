package com.kencooney.doortest.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigReader {

	private Properties properties;

    public ConfigReader(String filePath) {
        properties = new Properties();
        
        String currentDir = Paths.get("").toAbsolutePath().normalize().toString();
        System.out.println("Current working directory: " + currentDir);
        filePath = currentDir+"\\"+filePath;
        
        try (InputStream input =new FileInputStream(filePath)) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static void main(String[] args) {
        ConfigReader configReader = new ConfigReader("config.properties");
        String usernameUrl = configReader.getProperty("bbs.usernameUrl");
        String returnUrl = configReader.getProperty("bbs.returnUrl");

        System.out.println("Username URL: " + usernameUrl);
        System.out.println("Return URL: " + returnUrl);
       
    }
}
