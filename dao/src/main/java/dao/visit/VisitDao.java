package dao.visit;

import dao.GenericDao;
import model.visit.Visit;
import util.sort.VisitSortCriterion;

import java.sql.Connection;
import java.util.List;

public interface VisitDao extends GenericDao<Visit> {

    List<Visit> getSortedClientVisits(Integer clientId, VisitSortCriterion criterion,
                                      Connection connection);
}

