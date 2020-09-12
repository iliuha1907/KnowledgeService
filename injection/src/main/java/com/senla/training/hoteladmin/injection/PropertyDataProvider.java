package com.senla.training.hoteladmin.injection;

import com.senla.training.hoteladmin.injection.exception.IncorrectInitializationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;

public class PropertyDataProvider {

    private static final Logger LOGGER = LogManager.getLogger(ParametersConfigurator.class);

    public static Boolean getBoolean(final String key, final String fileName) {
        try (InputStream input = PropertyDataProvider.class.getClassLoader().getResourceAsStream(fileName)) {
            Properties prop = new Properties();
            prop.load(input);
            return Boolean.parseBoolean(prop.getProperty(key));
        } catch (Exception ex) {
            LOGGER.error("Error at getting boolean: " + ex.getMessage());
            throw new IncorrectInitializationException("Could not load properties: no such key or file");
        }
    }

    public static String getString(final String key, final String fileName) {
        try (InputStream input = PropertyDataProvider.class.getClassLoader().getResourceAsStream(fileName)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(key);
        } catch (Exception ex) {
            LOGGER.error("Error at getting String: " + ex.getMessage());
            throw new IncorrectInitializationException("Could not load properties: no such key or file");
        }
    }

    public static Integer getInt(final String key, final String fileName) {
        try (InputStream input = PropertyDataProvider.class.getClassLoader().getResourceAsStream(fileName)) {
            Properties prop = new Properties();
            prop.load(input);
            return Integer.parseInt(prop.getProperty(key));
        } catch (Exception ex) {
            LOGGER.error("Error at getting Integer: " + ex.getMessage());
            throw new IncorrectInitializationException("Could not load properties: no such key or file");
        }
    }
}

