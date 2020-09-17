package com.senla.training.hoteladmin.dao.visit;

import com.senla.training.hoteladmin.dao.AbstractDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.hotelservice.HotelService_;
import com.senla.training.hoteladmin.model.visit.Visit;
import com.senla.training.hoteladmin.model.visit.Visit_;
import com.senla.training.hoteladmin.util.sort.VisitSortCriterion;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@Component
public class VisitDaoImpl extends AbstractDao<Visit> implements VisitDao {

    @Override
    public Class<Visit> getEntityClass() {
        return Visit.class;
    }

    @Override
    public List<Visit> getSortedClientVisits(Client client, VisitSortCriterion criterion) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Visit> query = criteriaBuilder.createQuery(Visit.class);
            Root<HotelService> serviceRoot = query.from(HotelService.class);
            Join<HotelService, Visit> visitJoin = serviceRoot.join(HotelService_.clientVisits);
            query.select(visitJoin).where(criteriaBuilder.equal(visitJoin.get(Visit_.client), client));
            if (criterion.equals(VisitSortCriterion.DATE)) {
                query.orderBy(criteriaBuilder.asc(visitJoin.get(Visit_.date)));
            } else if (criterion.equals(VisitSortCriterion.PRICE)) {
                query.orderBy(criteriaBuilder.asc(serviceRoot.get(HotelService_.price)));
            }
            TypedQuery<Visit> typedQuery = entityManager.createQuery(query);
            return typedQuery.getResultList();
        } catch (Exception ex) {
            logger.error("Error at getting sorted client visits: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }
}
