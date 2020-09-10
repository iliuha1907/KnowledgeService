package com.senla.training.hoteladmin.dao.visit;

import com.senla.training.hoteladmin.dao.HibernateDao;
import com.senla.training.hoteladmin.model.visit.Visit;
import com.senla.training.hoteladmin.util.sort.VisitSortCriterion;

import javax.persistence.EntityManager;
import java.util.List;

public interface VisitDao extends HibernateDao<Visit> {

    List<Visit> getSortedClientVisits(Integer clientId, VisitSortCriterion criterion,
                                      EntityManager entityManager);
}
