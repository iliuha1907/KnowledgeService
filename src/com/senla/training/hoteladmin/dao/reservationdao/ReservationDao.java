package com.senla.training.hoteladmin.dao.reservationdao;

import com.senla.training.hoteladmin.dao.GenericDao;
import com.senla.training.hoteladmin.model.reservation.Reservation;
import com.senla.training.hoteladmin.util.sort.ReservationSortCriterion;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

public interface ReservationDao extends GenericDao<Reservation> {

    List<Reservation> getSortedReservations(Connection connection, ReservationSortCriterion criterion);

    Integer getNumberOfResidents(Connection connection);

    List<Reservation> getVisitsExpiredAfterDate(Date date, Connection connection);

    List<Reservation> getLastRoomVisits(Integer roomId, Integer count, Connection connection);

    Reservation getClientReservation(Integer clientId, Connection connection);

    void deactivateClientReservation(Integer clientId, Connection connection);

    Reservation getByRoomClientId(Integer roomId, Integer clientId, Connection connection);
}

