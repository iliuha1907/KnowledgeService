package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.dao.DaoManager;
import com.senla.training.hoteladmin.dao.clientdao.ClientDao;
import com.senla.training.hoteladmin.dao.reservationdao.ReservationDao;
import com.senla.training.hoteladmin.dao.roomdao.RoomDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.reservation.Reservation;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.util.filecsv.writeread.ReservationReaderWriter;
import com.senla.training.hoteladmin.util.sort.ReservationSortCriterion;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.util.database.RoomFields;
import com.senla.training.injection.annotation.ConfigProperty;
import com.senla.training.injection.annotation.NeedInjectionClass;
import com.senla.training.injection.annotation.NeedInjectionField;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

@NeedInjectionClass
public class ReservationServiceImpl implements ReservationService {
    @NeedInjectionField
    private ReservationDao reservationDao;
    @NeedInjectionField
    private RoomDao roomDao;
    @NeedInjectionField
    private ClientDao clientDao;
    @NeedInjectionField
    private DaoManager daoManager;
    @ConfigProperty(propertyName = "clients.numberOfRecords", type = Integer.class)
    private Integer numberOfResidents;

    @Override
    public void addReservation(Client client, Date arrivalDate, Date departureDate) {
        Connection connection = daoManager.getConnection();
        Room room = roomDao.getFirstFreeRoom(connection);
        if (room == null) {
            throw new BusinessException("No free rooms");
        }

        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        daoManager.setConnectionAutocommit(false);
        try {
            reservationDao.add(new Reservation(room, client, arrivalDate, departureDate, true),
                    connection);
            roomDao.updateById(room.getId(), 0, RoomFields.is_free.toString(), connection);
            daoManager.commitConnection();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            throw ex;
        } finally {
            daoManager.setConnectionAutocommit(isAutocommit);
        }
    }

    @Override
    public void addReservationForExistingClient(Integer clientId, Date arrivalDate, Date departureDate) {
        Client client = clientDao.getById(clientId, daoManager.getConnection());
        if (client == null) {
            throw new BusinessException("No such client");
        }

        addReservation(client, arrivalDate, departureDate);
    }

    @Override
    public void deactivateReservation(Integer clientId) {
        Connection connection = daoManager.getConnection();
        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        Reservation reservation = reservationDao.getClientReservation(clientId, connection);
        if (reservation == null) {
            throw new BusinessException("No active reservations for such client");
        }

        daoManager.setConnectionAutocommit(false);
        try {
            reservationDao.deactivateClientReservation(clientId, connection);
            roomDao.updateById(reservation.getRoom().getId(), 1, RoomFields.is_free.toString(), connection);
            daoManager.commitConnection();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            throw ex;
        } finally {
            daoManager.setConnectionAutocommit(isAutocommit);
        }
    }

    @Override
    public List<Reservation> getReservationsExpiredAfterDate(Date date) {
        return reservationDao.getVisitsExpiredAfterDate(date, daoManager.getConnection());
    }

    @Override
    public List<Reservation> getSortedReservations(ReservationSortCriterion criterion) {
        return reservationDao.getSortedReservations(daoManager.getConnection(), criterion);
    }

    @Override
    public List<Reservation> getLastRoomVisits(Integer roomId) {
        return reservationDao.getLastRoomVisits(roomId, numberOfResidents, daoManager.getConnection());
    }

    @Override
    public Integer getNumberOfResidents() {
        return reservationDao.getNumberOfResidents(daoManager.getConnection());
    }

    @Override
    public void exportReservations() {
        ReservationReaderWriter.writeReservations(reservationDao.getAll(daoManager.getConnection()));
    }

    @Override
    public void importReservations() {
        List<Reservation> reservations = ReservationReaderWriter.readReservations();
        Connection connection = daoManager.getConnection();
        reservations.forEach(reservation -> {
            Reservation existing = reservationDao.getByRoomClientId(reservation.getRoom().getId(),
                    reservation.getResident().getId(), connection);
            Room room = roomDao.getById(reservation.getRoom().getId(), connection);
            Client client = clientDao.getById(reservation.getResident().getId(), connection);
            if (existing == null && room != null && client != null && room.isFree() && room.getStatus().equals(
                    RoomStatus.SERVED)) {
                addReservation(new Reservation(room, client, reservation.getArrivalDate(),
                        reservation.getDepartureDate(), reservation.isActive()));
            }
        });
    }

    private void addReservation(Reservation reservation) {
        Connection connection = daoManager.getConnection();
        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        daoManager.setConnectionAutocommit(false);
        try {
            reservationDao.add(reservation, connection);
            roomDao.updateById(reservation.getRoom().getId(), 0, RoomFields.is_free.toString(), connection);
            daoManager.commitConnection();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            throw ex;
        } finally {
            daoManager.setConnectionAutocommit(isAutocommit);
        }
    }
}

