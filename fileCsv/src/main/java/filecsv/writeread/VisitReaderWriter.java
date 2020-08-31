package filecsv.writeread;

import annotation.ConfigProperty;
import annotation.NeedInjectionClass;
import exception.BusinessException;
import filecsv.parsing.VisitConverter;
import model.visit.Visit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

@NeedInjectionClass
public class VisitReaderWriter {

    private static final Logger LOGGER = LogManager.getLogger(VisitReaderWriter.class);
    @ConfigProperty(propertyName = "csv.visits.filePath", type = String.class)
    private static String fileName;
    @ConfigProperty(propertyName = "csv.separator", type = String.class)
    private static String separator;

    public static void writeVisits(final List<Visit> visits) {
        try (FileWriter fileWriter = new FileWriter(new File(ClientReaderWriter.class.getClassLoader().
                getResource(fileName).getFile()))) {
            for (Visit visit : visits) {
                fileWriter.write(VisitConverter.getStringFromVisit(visit, separator) + "\n");
            }
        } catch (Exception ex) {
            LOGGER.error("Error at writing visits: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    public static List<Visit> readVisits() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(
                ClientReaderWriter.class.getClassLoader().getResource(fileName).getFile())))) {
            List<Visit> visits = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                visits.add(VisitConverter.parseVisit(line, separator));
            }
            return visits;
        } catch (Exception ex) {
            LOGGER.error("Error at reading visits: " + ex.getMessage());
            throw new BusinessException("Could not read visits");
        }
    }
}
