package com.senla.training.hoteladmin.dao.visit;

import com.senla.training.hoteladmin.annotationapi.NeedInjectionClass;
import com.senla.training.hoteladmin.dao.HibernateAbstractDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.visit.Visit;
import com.senla.training.hoteladmin.model.visit.Visit_;
import com.senla.training.hoteladmin.util.sort.VisitSortCriterion;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@NeedInjectionClass
public class VisitDaoImpl extends HibernateAbstractDao<Visit> implements VisitDao {

    @Override
    public Class<Visit> getEntityClass() {
        return Visit.class;
    }

    @Override
    public List<Visit> getSortedClientVisits(Integer clientId, VisitSortCriterion criterion, EntityManager entityManager) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Visit> query = criteriaBuilder.createQuery(Visit.class);
            Root<Visit> visit = query.from(Visit.class);
            visit.join(Visit_.SERVICE);
            query.select(visit).distinct(true);
            TypedQuery<Visit> typedQuery = entityManager.createQuery(query);
            return typedQuery.getResultList();
        } catch (Exception ex) {
            logger.error("Error at getting sorted client visits: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }
}
