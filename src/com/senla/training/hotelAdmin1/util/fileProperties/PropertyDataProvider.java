package com.senla.training.hotelAdmin.util.fileProperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyDataProvider {
    private static final String FILE_NAME = "resources/app.properties";
    private static final String NUMBER_OF_RECORDS = "numberOfRecords";
    private static final String CHANGE_STATUS = "changeStatus";
    private static final String MOVED_CLIENTS = "movedClientsFile";
    private static final String ROOM_CLIENTS = "roomClientsFile";
    private static final String SERVICES_FILE = "servicesFile";
    private static final String CLIENTS_CSV = "clientsCsvFile";
    private static final String ROOMS_CSV = "roomsCsv";
    private static final String SERVICES_CSV = "servicesCsv";
    private static final String SEPARATOR = "separator";

    public static Integer getNumberOfRecords() {
        try (InputStream input = new FileInputStream(FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            try {
                Integer number = Integer.parseInt(prop.getProperty(NUMBER_OF_RECORDS));
                return number;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean isChangeableStatus() {
        try (InputStream input = new FileInputStream(FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            boolean isChangeAble = Boolean.parseBoolean(prop.getProperty(CHANGE_STATUS));
            return isChangeAble;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getMovedClientsFile() {
        try (InputStream input = new FileInputStream(FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(MOVED_CLIENTS);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getRoomClientsFile() {
        try (InputStream input = new FileInputStream(FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(ROOM_CLIENTS);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getServicesFile() {
        try (InputStream input = new FileInputStream(FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(SERVICES_FILE);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getClientsCsv() {
        try (InputStream input = new FileInputStream(FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(CLIENTS_CSV);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getRoomsCsv() {
        try (InputStream input = new FileInputStream(FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(ROOMS_CSV);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getServicesCsv() {
        try (InputStream input = new FileInputStream(FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(SERVICES_CSV);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getSeparator() {
        try (InputStream input = new FileInputStream(FILE_NAME)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(SEPARATOR);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}

