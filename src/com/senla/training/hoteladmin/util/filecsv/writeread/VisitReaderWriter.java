package com.senla.training.hoteladmin.util.filecsv.writeread;

import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.visit.Visit;
import com.senla.training.hoteladmin.util.filecsv.parsing.VisitConverter;
import com.senla.training.injection.annotation.ConfigProperty;
import com.senla.training.injection.annotation.NeedInjectionClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

@NeedInjectionClass
public class VisitReaderWriter {

    @ConfigProperty(propertyName = "csv.visits.filePath", type = String.class)
    private static String fileName;
    @ConfigProperty(propertyName = "csv.separator", type = String.class)
    private static String separator;

    public static void writeVisits(List<Visit> visits) {
        try (FileWriter fileWriter = new FileWriter(new File(fileName))) {
            for (Visit visit : visits) {
                fileWriter.write(VisitConverter.getStringFromVisit(visit, separator) + "\n");
            }
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    public static List<Visit> readVisits() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)))) {
            List<Visit> visits = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                visits.add(VisitConverter.parseVisit(line, separator));
            }
            return visits;
        } catch (Exception ex) {
            throw new BusinessException("Could not read visits");
        }
    }
}
