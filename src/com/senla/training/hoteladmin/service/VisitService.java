package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.model.visit.Visit;
import com.senla.training.hoteladmin.util.sort.VisitSortCriterion;

import java.util.Date;
import java.util.List;

public interface VisitService {

    void addVisit(Integer serviceId, Integer clientId, Date date);

    List<Visit> getSortedClientVisits(Integer clientId, VisitSortCriterion criterion);
}

