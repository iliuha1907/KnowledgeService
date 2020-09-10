package com.senla.training.hoteladmin.csvapi.writeread;

import com.senla.training.hoteladmin.annotationapi.ConfigProperty;
import com.senla.training.hoteladmin.annotationapi.NeedInjectionClass;
import com.senla.training.hoteladmin.csvapi.parsing.ReservationConverter;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.reservation.Reservation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

@NeedInjectionClass
public class ReservationReaderWriter {

    private static final Logger LOGGER = LogManager.getLogger(ReservationReaderWriter.class);
    @ConfigProperty(propertyName = "csv.reservations.import.filePath", type = String.class)
    private static String fileNameImport;
    @ConfigProperty(propertyName = "csv.reservations.export.filePath", type = String.class)
    private static String fileNameExport;
    @ConfigProperty(propertyName = "csv.separator", type = String.class)
    private static String separator;

    public static void writeReservations(final List<Reservation> reservations) {
        try (FileWriter fileWriter = new FileWriter(new File(fileNameExport))) {
            for (Reservation reservation : reservations) {
                fileWriter.write(ReservationConverter.getStringFromReservation(reservation, separator) + "\n");
            }
        } catch (Exception ex) {
            LOGGER.error("Error at writing reservations: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    public static List<Reservation> readReservations() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(
                ClientReaderWriter.class.getClassLoader().getResource(fileNameImport).getFile())))) {
            List<Reservation> reservations = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                reservations.add(ReservationConverter.parseReservation(line, separator));
            }
            return reservations;
        } catch (Exception ex) {
            LOGGER.error("Error at reading reservations: " + ex.getMessage());
            throw new BusinessException("Could not read reservations");
        }
    }
}
