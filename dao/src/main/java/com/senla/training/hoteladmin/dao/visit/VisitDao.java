package com.senla.training.hoteladmin.dao.visit;

import com.senla.training.hoteladmin.dao.GenericDao;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.visit.Visit;
import com.senla.training.hoteladmin.util.sort.VisitSortCriterion;

import java.util.List;

public interface VisitDao extends GenericDao<Visit, Integer> {

    List<Visit> getSortedClientVisits(Client client, VisitSortCriterion criterion);
}
