package com.senla.training.hoteladmin.csvapi.writeread;

import com.senla.training.hoteladmin.csvapi.parsing.ReservationConverter;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.reservation.Reservation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReservationReaderWriter {

    private static final Logger LOGGER = LogManager.getLogger(ReservationReaderWriter.class);
    @Value("${csv.reservations.import.filePath:csv/reservations.csv}")
    private String fileNameImport;
    @Value("${csv.reservations.export.filePath:exportCsv/reservations.csv}")
    private String fileNameExport;
    @Value("${csv.separator:;}")
    private String separator;

    public void writeReservations(final List<Reservation> reservations) {
        try (FileWriter fileWriter = new FileWriter(new File(fileNameExport))) {
            for (Reservation reservation : reservations) {
                fileWriter.write(ReservationConverter.getStringFromReservation(reservation, separator) + "\n");
            }
        } catch (Exception ex) {
            LOGGER.error("Error at writing reservations: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    public List<Reservation> readReservations() {
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
