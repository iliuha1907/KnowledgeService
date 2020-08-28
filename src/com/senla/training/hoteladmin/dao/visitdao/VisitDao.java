package com.senla.training.hoteladmin.dao.visitdao;

import com.senla.training.hoteladmin.dao.GenericDao;
import com.senla.training.hoteladmin.model.visit.Visit;
import com.senla.training.hoteladmin.util.sort.VisitSortCriterion;

import java.sql.Connection;
import java.util.List;

public interface VisitDao extends GenericDao<Visit> {

    List<Visit> getSortedClientVisits(Integer clientId, VisitSortCriterion criterion,
                                      Connection connection);
}

