package com.senla.training.injection;

import com.senla.training.injection.exception.IncorrectInitializationException;

import java.io.InputStream;
import java.util.Properties;

public class PropertyDataProvider {

    public static Boolean getBoolean(String key, String fileName) {
        try (InputStream input = PropertyDataProvider.class.getClassLoader().getResourceAsStream(fileName)) {
            Properties prop = new Properties();
            prop.load(input);
            return Boolean.parseBoolean(prop.getProperty(key));
        } catch (Exception ex) {
            throw new IncorrectInitializationException("Could not load properties: no such key or file");
        }
    }

    public static String getString(String key, String fileName) {
        try (InputStream input = PropertyDataProvider.class.getClassLoader().getResourceAsStream(fileName)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(key);
        } catch (Exception ex) {
            throw new IncorrectInitializationException("Could not load properties: no such key or file");
        }
    }

    public static Integer getInt(String key, String fileName) {
        try (InputStream input = PropertyDataProvider.class.getClassLoader().getResourceAsStream(fileName)) {
            Properties prop = new Properties();
            prop.load(input);
            return Integer.parseInt(prop.getProperty(key));
        } catch (Exception ex) {
            throw new IncorrectInitializationException("Could not load properties: no such key or file");
        }
    }
}

