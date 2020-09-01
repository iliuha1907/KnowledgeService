package service;

import annotation.ConfigProperty;
import annotation.NeedInjectionClass;
import annotation.NeedInjectionField;
import dao.DaoManager;
import dao.reservation.ReservationDao;
import dao.room.RoomDao;
import dao.client.ClientDao;
import exception.BusinessException;
import filecsv.writeread.ReservationReaderWriter;
import model.client.Client;
import model.reservation.Reservation;
import model.room.Room;
import model.room.RoomStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import model.room.RoomField;
import util.sort.ReservationSortCriterion;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

@NeedInjectionClass
public class ReservationServiceImpl implements ReservationService {

    private static final Logger LOGGER = LogManager.getLogger(ReservationServiceImpl.class);
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
    public void addReservation(final Client client, final Date arrivalDate, final Date departureDate) {
        Connection connection = daoManager.getConnection();
        Room room = roomDao.getFirstFreeRoom(connection);
        if (room == null) {
            LOGGER.error("Error at adding reservation: No free rooms");
            throw new BusinessException("No free rooms");
        }

        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        daoManager.setConnectionAutocommit(false);
        try {
            reservationDao.add(new Reservation(room, client, arrivalDate, departureDate, true),
                    connection);
            roomDao.updateById(room.getId(), 0, RoomField.IS_FREE.toString().toLowerCase(),
                    connection);
            daoManager.connectionCommit();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            throw ex;
        } finally {
            daoManager.setConnectionAutocommit(isAutocommit);
        }
    }

    @Override
    public void addReservationForExistingClient(final Integer clientId, final Date arrivalDate,
                                                final Date departureDate) {
        Client client = clientDao.getById(clientId, daoManager.getConnection());
        if (client == null) {
            LOGGER.error("Error at adding reservation: No such client");
            throw new BusinessException("No such client");
        }

        addReservation(client, arrivalDate, departureDate);
    }

    @Override
    public void deactivateReservation(final Integer clientId) {
        Connection connection = daoManager.getConnection();
        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        Reservation reservation = reservationDao.getClientReservation(clientId, connection);
        if (reservation == null) {
            LOGGER.error("Error at deactivating reservation: No active reservations for such client");
            throw new BusinessException("No active reservations for such client");
        }

        daoManager.setConnectionAutocommit(false);
        try {
            reservationDao.deactivateClientReservation(clientId, connection);
            roomDao.updateById(reservation.getRoom().getId(), 1,
                    RoomField.IS_FREE.toString().toLowerCase(), connection);
            daoManager.connectionCommit();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            throw ex;
        } finally {
            daoManager.setConnectionAutocommit(isAutocommit);
        }
    }

    @Override
    public List<Reservation> getReservationsExpiredAfterDate(final Date date) {
        return reservationDao.getVisitsExpiredAfterDate(date, daoManager.getConnection());
    }

    @Override
    public List<Reservation> getSortedReservations(final ReservationSortCriterion criterion) {
        return reservationDao.getSortedReservations(daoManager.getConnection(), criterion);
    }

    @Override
    public List<Reservation> getLastRoomVisits(final Integer roomId) {
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
        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        daoManager.setConnectionAutocommit(false);
        try {
            reservations.forEach(reservation -> {
                Reservation existing = reservationDao.getByRoomClientId(reservation.getRoom().getId(),
                        reservation.getResident().getId(), connection);
                Room room = roomDao.getById(reservation.getRoom().getId(), connection);
                Client client = clientDao.getById(reservation.getResident().getId(), connection);
                if (existing == null && room != null && client != null && room.isFree()
                        && room.getStatus().equals(
                        RoomStatus.SERVED)) {
                    reservationDao.add(new Reservation(room, client, reservation.getArrivalDate(),
                            reservation.getDepartureDate(), reservation.isActive()), connection);
                    roomDao.updateById(room.getId(), 0, RoomField.IS_FREE.toString().toLowerCase(),
                            connection);
                }
            });
            daoManager.connectionCommit();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            throw ex;
        } finally {
            daoManager.setConnectionAutocommit(isAutocommit);
        }
    }
}

