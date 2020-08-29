package com.senla.training.hoteladmin.util.filecsv.writeread;

import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.reservation.Reservation;
import com.senla.training.hoteladmin.util.filecsv.parsing.ReservationConverter;
import com.senla.training.injection.annotation.ConfigProperty;
import com.senla.training.injection.annotation.NeedInjectionClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

@NeedInjectionClass
public class ReservationReaderWriter {

    @ConfigProperty(propertyName = "csv.reservations.filePath", type = String.class)
    private static String fileName;
    @ConfigProperty(propertyName = "csv.separator", type = String.class)
    private static String separator;

    public static void writeReservations(List<Reservation> reservations) {
        try (FileWriter fileWriter = new FileWriter(new File(fileName))) {
            for (Reservation reservation : reservations) {
                fileWriter.write(ReservationConverter.getStringFromReservation(reservation, separator) + "\n");
            }
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    public static List<Reservation> readReservations() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)))) {
            List<Reservation> reservations = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                reservations.add(ReservationConverter.parseReservation(line, separator));
            }
            return reservations;
        } catch (Exception ex) {
            throw new BusinessException("Could not read reservations");
        }
    }
}
