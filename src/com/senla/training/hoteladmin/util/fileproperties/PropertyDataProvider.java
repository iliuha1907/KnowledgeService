package com.senla.training.hoteladmin.util.fileproperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyDataProvider {

    public static String getMovedClientsFile() {
        try (InputStream input = new FileInputStream(PropertyNamesProvider.FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(PropertyNamesProvider.MOVED_CLIENTS,
                    DefaultPropertyValuesProvider.DEFAULT_MOVED_CLIENTS);
        } catch (IOException ex) {
            return DefaultPropertyValuesProvider.DEFAULT_MOVED_CLIENTS;
        }
    }

    public static String getRoomClientsFile() {
        try (InputStream input = new FileInputStream(PropertyNamesProvider.FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(PropertyNamesProvider.ROOM_CLIENTS,
                    DefaultPropertyValuesProvider.DEFAULT_ROOM_CLIENTS);
        } catch (IOException ex) {
            return DefaultPropertyValuesProvider.DEFAULT_ROOM_CLIENTS;
        }
    }

    public static String getServicesFile() {
        try (InputStream input = new FileInputStream(PropertyNamesProvider.FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(PropertyNamesProvider.SERVICES_FILE,
                    DefaultPropertyValuesProvider.DEFAULT_SERVICES_FILE);
        } catch (IOException ex) {
            return DefaultPropertyValuesProvider.DEFAULT_SERVICES_FILE;
        }
    }

    public static String getClientsCsv() {
        try (InputStream input = new FileInputStream(PropertyNamesProvider.FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(PropertyNamesProvider.CLIENTS_CSV,
                    DefaultPropertyValuesProvider.DEFAULT_CLIENTS_CSV);
        } catch (IOException ex) {
            return DefaultPropertyValuesProvider.DEFAULT_CLIENTS_CSV;
        }
    }

    public static String getRoomsCsv() {
        try (InputStream input = new FileInputStream(PropertyNamesProvider.FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(PropertyNamesProvider.ROOMS_CSV,
                    DefaultPropertyValuesProvider.DEFAULT_ROOMS_CSV);
        } catch (IOException ex) {
            return DefaultPropertyValuesProvider.DEFAULT_ROOMS_CSV;
        }
    }

    public static String getServicesCsv() {
        try (InputStream input = new FileInputStream(PropertyNamesProvider.FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(PropertyNamesProvider.SERVICES_CSV,
                    DefaultPropertyValuesProvider.DEFAULT_SERVICES_CSV);
        } catch (IOException ex) {
            return DefaultPropertyValuesProvider.DEFAULT_SERVICES_CSV;
        }
    }

    public static String getSeparator() {
        try (InputStream input = new FileInputStream(PropertyNamesProvider.FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(PropertyNamesProvider.SEPARATOR,
                    DefaultPropertyValuesProvider.DEFAULT_SEPARATOR);
        } catch (IOException ex) {
            return DefaultPropertyValuesProvider.DEFAULT_SEPARATOR;
        }
    }

    public static String getClientIdFile() {
        try (InputStream input = new FileInputStream(PropertyNamesProvider.FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(PropertyNamesProvider.CLIENTS_ID,
                    DefaultPropertyValuesProvider.DEFAULT_CLIENTS_ID);
        } catch (IOException ex) {
            return DefaultPropertyValuesProvider.DEFAULT_CLIENTS_ID;
        }
    }

    public static String getRoomIdFile() {
        try (InputStream input = new FileInputStream(PropertyNamesProvider.FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(PropertyNamesProvider.ROOMS_ID,
                    DefaultPropertyValuesProvider.DEFAULT_ROOMS_ID);
        } catch (IOException ex) {
            return DefaultPropertyValuesProvider.DEFAULT_ROOMS_ID;
        }
    }

    public static String getHotelServiceIdFile() {
        try (InputStream input = new FileInputStream(PropertyNamesProvider.FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(PropertyNamesProvider.SERVICES_ID,
                    DefaultPropertyValuesProvider.DEFAULT_SERVICES_ID);
        } catch (IOException ex) {
            return DefaultPropertyValuesProvider.DEFAULT_SERVICES_ID;
        }
    }

    public static Boolean getBoolean(String key, String fileName) {
        try (InputStream input = new FileInputStream(fileName)) {
            Properties prop = new Properties();
            prop.load(input);
            return Boolean.parseBoolean(prop.getProperty(key));
        } catch (IOException ex) {
            return null;
        }
    }

    public static Integer getInt(String key, String fileName) {
        try (InputStream input = new FileInputStream(fileName)) {
            Properties prop = new Properties();
            prop.load(input);
            return Integer.parseInt(prop.getProperty(key));
        } catch (IOException ex) {
            return null;
        }
    }
}

