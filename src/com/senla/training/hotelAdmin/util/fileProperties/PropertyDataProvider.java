package com.senla.training.hoteladmin.util.fileproperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyDataProvider {

    public static Integer getNumberOfRecords() {
        try (InputStream input = new FileInputStream(PropertyNamesProvider.FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return Integer.parseInt((String) prop.getOrDefault(PropertyNamesProvider.NUMBER_OF_RECORDS,
                    DefaultPropertyValuesProvider.DEFAULT_NUMBER_RESIDENTS));
        } catch (IOException ex) {
            return DefaultPropertyValuesProvider.DEFAULT_NUMBER_RESIDENTS;
        }
    }

    public static boolean isChangeableStatus() {
        try (InputStream input = new FileInputStream(PropertyNamesProvider.FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return Boolean.parseBoolean((String) prop.getOrDefault(PropertyNamesProvider.CHANGE_STATUS,
                    DefaultPropertyValuesProvider.DEFAULT_IS_CHANGEABLE));
        } catch (IOException ex) {
            return DefaultPropertyValuesProvider.DEFAULT_IS_CHANGEABLE;
        }
    }

    public static String getMovedClientsFile() {
        try (InputStream input = new FileInputStream(PropertyNamesProvider.FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return (String) prop.getOrDefault(PropertyNamesProvider.MOVED_CLIENTS,
                    DefaultPropertyValuesProvider.DEFAULT_MOVED_CLIENTS);
        } catch (IOException ex) {
            return DefaultPropertyValuesProvider.DEFAULT_MOVED_CLIENTS;
        }
    }

    public static String getRoomClientsFile() {
        try (InputStream input = new FileInputStream(PropertyNamesProvider.FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return (String) prop.getOrDefault(PropertyNamesProvider.ROOM_CLIENTS,
                    DefaultPropertyValuesProvider.DEFAULT_ROOM_CLIENTS);
        } catch (IOException ex) {
            return DefaultPropertyValuesProvider.DEFAULT_ROOM_CLIENTS;
        }
    }

    public static String getServicesFile() {
        try (InputStream input = new FileInputStream(PropertyNamesProvider.FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return (String) prop.getOrDefault(PropertyNamesProvider.SERVICES_FILE,
                    DefaultPropertyValuesProvider.DEFAULT_SERVICES_FILE);
        } catch (IOException ex) {
            return DefaultPropertyValuesProvider.DEFAULT_SERVICES_FILE;
        }
    }

    public static String getClientsCsv() {
        try (InputStream input = new FileInputStream(PropertyNamesProvider.FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(PropertyNamesProvider.CLIENTS_CSV);
        } catch (IOException ex) {
            return DefaultPropertyValuesProvider.DEFAULT_CLIENTS_CSV;
        }
    }

    public static String getRoomsCsv() {
        try (InputStream input = new FileInputStream(PropertyNamesProvider.FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(PropertyNamesProvider.ROOMS_CSV);
        } catch (IOException ex) {
            return DefaultPropertyValuesProvider.DEFAULT_ROOMS_CSV;
        }
    }

    public static String getServicesCsv() {
        try (InputStream input = new FileInputStream(PropertyNamesProvider.FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(PropertyNamesProvider.SERVICES_CSV);
        } catch (IOException ex) {
            return DefaultPropertyValuesProvider.DEFAULT_SERVICES_CSV;
        }
    }

    public static String getSeparator() {
        try (InputStream input = new FileInputStream(PropertyNamesProvider.FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(PropertyNamesProvider.SEPARATOR);
        } catch (IOException ex) {
            return DefaultPropertyValuesProvider.DEFAULT_SEPARATOR;
        }
    }

    public static String getClientIdFile() {
        try (InputStream input = new FileInputStream(PropertyNamesProvider.FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(PropertyNamesProvider.CLIENTS_ID);
        } catch (IOException ex) {
            return DefaultPropertyValuesProvider.DEFAULT_CLIENTS_ID;
        }
    }

    public static String getRoomIdFile() {
        try (InputStream input = new FileInputStream(PropertyNamesProvider.FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(PropertyNamesProvider.ROOMS_ID);
        } catch (IOException ex) {
            return DefaultPropertyValuesProvider.DEFAULT_ROOMS_ID;
        }
    }

    public static String getHotelServiceIdFile() {
        try (InputStream input = new FileInputStream(PropertyNamesProvider.FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(PropertyNamesProvider.SERVICES_ID);
        } catch (IOException ex) {
            return DefaultPropertyValuesProvider.DEFAULT_SERVICES_ID;
        }
    }
}

