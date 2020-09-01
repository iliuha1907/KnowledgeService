package service;

import model.visit.Visit;
import util.sort.VisitSortCriterion;

import java.util.Date;
import java.util.List;

public interface VisitService {

    void addVisit(Integer serviceId, Integer clientId, Date date);

    List<Visit> getSortedClientVisits(Integer clientId, VisitSortCriterion criterion);

    void exportVisits();

    void importVisits();
}

