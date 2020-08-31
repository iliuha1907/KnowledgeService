package controller;

import annotation.NeedInjectionClass;
import annotation.NeedInjectionField;
import exception.BusinessException;
import model.visit.Visit;
import service.VisitService;
import util.sort.VisitSortCriterion;

import java.util.Date;
import java.util.List;

@NeedInjectionClass
public class VisitController {

    @NeedInjectionField
    private VisitService visitService;

    public String addVisit(final Integer serviceId, final Integer clientId, final Date date) {
        try {
            visitService.addVisit(serviceId, clientId, date);
            return "Successfully added visit";
        } catch (Exception ex) {
            return "Error at adding visit: " + ex.getMessage();
        }
    }

    public String getSortedClientVisits(final Integer clientId, final VisitSortCriterion criterion) {
        List<Visit> visits;
        try {
            visits = visitService.getSortedClientVisits(clientId, criterion);
        } catch (Exception ex) {
            return "Error at getting visits: " + ex.getMessage();
        }

        StringBuilder result = new StringBuilder("Visits:\n");
        visits.forEach(visit ->
                result.append(visit).append("\n")
        );
        return result.toString();
    }

    public String exportVisits() {
        try {
            visitService.exportVisits();
            return "Successfully exported visits";
        } catch (BusinessException ex) {
            return ex.getMessage();
        }
    }

    public String importVisits() {
        try {
            visitService.importVisits();
            return "Successfully imported visits";
        } catch (BusinessException ex) {
            return ex.getMessage();
        }
    }
}

