package com.senla.training.hoteladmin.csvapi.writeread;

import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.csvapi.parsing.VisitConverter;
import com.senla.training.hoteladmin.model.visit.Visit;
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
public class VisitReaderWriter {

    private static final Logger LOGGER = LogManager.getLogger(VisitReaderWriter.class);
    @Value("${csv.visits.import.filePath:csv/visits.csv}")
    private String fileNameImport;
    @Value("${csv.visits.export.filePath:exportCsv/visits.csv}")
    private String fileNameExport;
    @Value("${csv.separator:;}")
    private String separator;

    public void writeVisits(final List<Visit> visits) {
        try (FileWriter fileWriter = new FileWriter(new File(fileNameExport))) {
            for (Visit visit : visits) {
                fileWriter.write(VisitConverter.getStringFromVisit(visit, separator) + "\n");
            }
        } catch (Exception ex) {
            LOGGER.error("Error at writing visits: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    public List<Visit> readVisits() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(
                ClientReaderWriter.class.getClassLoader().getResource(fileNameImport).getFile())))) {
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
