package com.senla.training.hoteladmin.dao.visit;

import com.senla.training.hoteladmin.annotationapi.NeedInjectionClass;
import com.senla.training.hoteladmin.dao.HibernateAbstractDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.hotelservice.HotelService_;
import com.senla.training.hoteladmin.model.visit.Visit;
import com.senla.training.hoteladmin.model.visit.Visit_;
import com.senla.training.hoteladmin.util.sort.VisitSortCriterion;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.stream.Collectors;

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
            Root<HotelService> serviceRoot = query.from(HotelService.class);
            Join<HotelService, Visit> visitJoin = serviceRoot.join(HotelService_.CLIENT_VISITS);
            query.select(visitJoin);
            if (criterion.equals(VisitSortCriterion.DATE)) {
                query.orderBy(criteriaBuilder.asc(visitJoin.get(Visit_.DATE)));
            } else if (criterion.equals(VisitSortCriterion.PRICE)) {
                query.orderBy(criteriaBuilder.asc(serviceRoot.get(HotelService_.PRICE)));
            }
            TypedQuery<Visit> typedQuery = entityManager.createQuery(query);
            return typedQuery.getResultList().stream()
                    .filter(visit -> visit.getClient().getId().equals(clientId))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            logger.error("Error at getting sorted client visits: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }
}
