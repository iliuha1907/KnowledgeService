package com.senla.training.hoteladmin.controller;

import com.senla.training.hoteladmin.model.visit.Visit;
import com.senla.training.hoteladmin.util.sort.VisitSortCriterion;
import com.senla.training.injection.annotation.NeedInjectionClass;
import com.senla.training.injection.annotation.NeedInjectionField;
import com.senla.training.hoteladmin.service.VisitService;

import java.util.Date;
import java.util.List;

@NeedInjectionClass
public class VisitController {
    @NeedInjectionField
    private VisitService visitService;

    public String addVisit(Integer serviceId, Integer clientId, Date date) {
        try {
            visitService.addVisit(serviceId, clientId, date);
            return "Successfully added visit";
        } catch (Exception ex) {
            return "Error at adding visit: " + ex.getMessage();
        }
    }

    public String getSortedClientVisits(Integer clientId, VisitSortCriterion criterion) {
        List<Visit> visits;
        try {
            visits = visitService.getSortedClientVisits(clientId, criterion);
        } catch (Exception ex) {
            return "Error at getting visits: " + ex.getMessage();
        }

        StringBuilder result = new StringBuilder("Visits:\n");
        visits.forEach(visit -> {
            result.append(visit).append("\n");
        });
        return result.toString();
    }
}

