package injection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyDataProvider {

    public static Boolean getBoolean(String key, String fileName) {
        InputStream input = PropertyDataProvider.class.getClassLoader().getResourceAsStream(fileName);
        if (input == null) {
            throw new IncorrectInitializationException("Could not load properties: no such file");
        }

        try {
            Properties prop = new Properties();
            prop.load(input);
            return Boolean.parseBoolean(prop.getProperty(key));
        } catch (IOException ex) {
            throw new IncorrectInitializationException("Could not load properties: no such key");
        }
    }

    public static String getString(String key, String fileName) {
        InputStream input = PropertyDataProvider.class.getClassLoader().getResourceAsStream(fileName);
        if (input == null) {
            throw new IncorrectInitializationException("Could not load properties: no such file");
        }

        try {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(key);
        } catch (IOException ex) {
            throw new IncorrectInitializationException("Could not load properties: no such key");
        }
    }

    public static Integer getInt(String key, String fileName) {
        InputStream input = PropertyDataProvider.class.getClassLoader().getResourceAsStream(fileName);
        if (input == null) {
            throw new IncorrectInitializationException("Could not load properties: no such file");
        }

        try {
            Properties prop = new Properties();
            prop.load(input);
            return Integer.parseInt(prop.getProperty(key));
        } catch (IOException ex) {
            throw new IncorrectInitializationException("Could not load properties: no such key");
        }
    }
}

