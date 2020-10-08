package com.senla.training.hoteladmin.service.visit;

import com.senla.training.hoteladmin.model.visit.Visit;

import java.util.Date;
import java.util.List;

public interface VisitService {

    void addVisit(Integer serviceId, Integer clientId, Date date);

    List<Visit> getSortedClientVisits(Integer clientId, String criterion);

    void exportVisits();

    void importVisits();
}
