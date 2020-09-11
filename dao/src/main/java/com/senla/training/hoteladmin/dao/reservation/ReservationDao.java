package com.senla.training.hoteladmin.dao.reservation;

import com.senla.training.hoteladmin.dao.HibernateDao;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.reservation.Reservation;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.util.sort.ReservationSortCriterion;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

public interface ReservationDao extends HibernateDao<Reservation> {

    List<Reservation> getSortedReservations(ReservationSortCriterion criterion, EntityManager entityManager);

    Long getNumberOfResidents(EntityManager entityManager);

    List<Reservation> getReservationsExpiredAfterDate(Date date, EntityManager entityManager);

    List<Reservation> getLastRoomReservations(Room room, Integer count, EntityManager entityManager);

    Reservation getReservationByRoomClient(Client client, Room room, EntityManager entityManager);

    void deactivateClientReservation(Client client, Room room, EntityManager entityManager);
}
