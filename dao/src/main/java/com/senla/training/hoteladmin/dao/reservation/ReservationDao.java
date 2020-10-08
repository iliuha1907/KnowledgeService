package com.senla.training.hoteladmin.dao.reservation;

import com.senla.training.hoteladmin.dao.GenericDao;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.reservation.Reservation;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.util.sort.ReservationSortCriterion;

import java.util.Date;
import java.util.List;

public interface ReservationDao extends GenericDao<Reservation, Integer> {

    List<Reservation> getSortedReservations(ReservationSortCriterion criterion);

    Long getNumberOfResidents();

    List<Reservation> getReservationsExpiredAfterDate(Date date);

    List<Reservation> getLastRoomReservations(Room room, Integer count);

    Reservation getReservationByRoomClient(Client client, Room room);

    void deactivateClientReservation(Client client, Room room);
}
