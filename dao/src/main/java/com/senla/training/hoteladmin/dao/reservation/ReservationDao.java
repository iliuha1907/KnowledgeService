package com.senla.training.hoteladmin.dao.reservation;

import com.senla.training.hoteladmin.dao.HibernateDao;
import com.senla.training.hoteladmin.model.reservation.Reservation;
import com.senla.training.hoteladmin.util.sort.ReservationSortCriterion;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

public interface ReservationDao extends HibernateDao<Reservation> {

    List<Reservation> getSortedReservations(ReservationSortCriterion criterion, EntityManager entityManager);

    Long getNumberOfResidents(EntityManager entityManager);

    List<Reservation> getReservationsExpiredAfterDate(Date date, EntityManager entityManager);

    List<Reservation> getLastRoomVisits(Integer roomId, Integer count, EntityManager entityManager);

    Reservation getReservationByRoomClient(Integer clientId, Integer roomId, EntityManager entityManager);

    void deactivateClientReservation(Integer clientId, Integer roomId, EntityManager entityManager);
}
