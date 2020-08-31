package filecsv.writeread;

import annotation.ConfigProperty;
import annotation.NeedInjectionClass;
import exception.BusinessException;
import filecsv.parsing.ReservationConverter;
import model.reservation.Reservation;
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
    @ConfigProperty(propertyName = "csv.reservations.filePath", type = String.class)
    private static String fileName;
    @ConfigProperty(propertyName = "csv.separator", type = String.class)
    private static String separator;

    public static void writeReservations(final List<Reservation> reservations) {
        try (FileWriter fileWriter = new FileWriter(new File(ClientReaderWriter.class.getClassLoader().
                getResource(fileName).getFile()))) {
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
                ClientReaderWriter.class.getClassLoader().getResource(fileName).getFile())))) {
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
