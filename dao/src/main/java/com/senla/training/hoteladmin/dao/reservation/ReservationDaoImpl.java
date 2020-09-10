package com.senla.training.hoteladmin.dao.reservation;

import com.senla.training.hoteladmin.annotationapi.NeedInjectionClass;
import com.senla.training.hoteladmin.dao.HibernateAbstractDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.reservation.Reservation;
import com.senla.training.hoteladmin.model.reservation.Reservation_;
import com.senla.training.hoteladmin.util.sort.ReservationSortCriterion;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

@NeedInjectionClass
public class ReservationDaoImpl extends HibernateAbstractDao<Reservation> implements ReservationDao {

    @Override
    public Class<Reservation> getEntityClass() {
        return Reservation.class;
    }

    @Override
    public List<Reservation> getSortedReservations(ReservationSortCriterion criterion, EntityManager entityManager) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Reservation> query = criteriaBuilder.createQuery(Reservation.class);
            Root<Reservation> reservationRoot = query.from(Reservation.class);
            reservationRoot.join(Reservation_.RESIDENT);
            query.select(reservationRoot).distinct(true);
            TypedQuery<Reservation> typedQuery = entityManager.createQuery(query);
            return typedQuery.getResultList();
        } catch (Exception ex) {
            logger.error("Error at getting sorted reservations: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public Long getNumberOfResidents(EntityManager entityManager) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
            Root<Reservation> root = query.from(Reservation.class);
            query.select(criteriaBuilder.countDistinct(root)).where(criteriaBuilder.equal(
                    root.get(Reservation_.IS_ACTIVE), 1));
            return entityManager.createQuery(query).getSingleResult();
        } catch (Exception ex) {
            logger.error("Error at getting number of residents: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public List<Reservation> getReservationsExpiredAfterDate(Date date, EntityManager entityManager) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Reservation> query = criteriaBuilder.createQuery(Reservation.class);
            Root<Reservation> root = query.from(Reservation.class);
            query.select(root).where(criteriaBuilder.lessThan(root.get(Reservation_.DEPARTURE_DATE),
                    date));
            return entityManager.createQuery(query).getResultList();
        } catch (Exception ex) {
            logger.error("Error at getting visits expired after date: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public List<Reservation> getLastRoomVisits(Integer roomId, Integer count, EntityManager entityManager) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Reservation> query = criteriaBuilder.createQuery(Reservation.class);
            Root<Reservation> root = query.from(Reservation.class);
            query.select(root).where(criteriaBuilder.equal(root.get(Reservation_.ROOM),
                    roomId), criteriaBuilder.equal(root.get(Reservation_.IS_ACTIVE), 0));
            return entityManager.createQuery(query).setMaxResults(count).getResultList();
        } catch (Exception ex) {
            logger.error("Error at getting last room visits: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public Reservation getReservationByRoomClient(Integer clientId, Integer roomId, EntityManager entityManager) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Reservation> query = criteriaBuilder.createQuery(Reservation.class);
            Root<Reservation> root = query.from(Reservation.class);
            query.select(root).where(criteriaBuilder.equal(root.get(Reservation_.RESIDENT),
                    clientId), criteriaBuilder.equal(root.get(Reservation_.ROOM), roomId),
                    criteriaBuilder.equal(root.get(Reservation_.IS_ACTIVE), 1));
            return entityManager.createQuery(query).getSingleResult();
        } catch (NoResultException noResultException) {
            logger.error("Error at getting client reservations: no entity!");
            return null;
        } catch (Exception ex) {
            logger.error("Error at getting client reservations: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public void deactivateClientReservation(Integer clientId, Integer roomId, EntityManager entityManager) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaUpdate<Reservation> update = criteriaBuilder.createCriteriaUpdate(Reservation.class);
            Root<Reservation> root = update.from(Reservation.class);
            update.set(Reservation_.IS_ACTIVE, 0);
            update.where(criteriaBuilder.equal(criteriaBuilder.equal(root.get(Reservation_.RESIDENT),
                    clientId), criteriaBuilder.equal(root.get(Reservation_.ROOM), roomId)));
            entityManager.createQuery(update).executeUpdate();
        } catch (Exception ex) {
            logger.error("Error at deactivating client reservations: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }
}
