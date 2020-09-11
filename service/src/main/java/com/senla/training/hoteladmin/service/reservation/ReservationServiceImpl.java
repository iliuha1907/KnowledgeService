package com.senla.training.hoteladmin.service.reservation;

import com.senla.training.hoteladmin.annotationapi.ConfigProperty;
import com.senla.training.hoteladmin.annotationapi.NeedInjectionClass;
import com.senla.training.hoteladmin.annotationapi.NeedInjectionField;
import com.senla.training.hoteladmin.dao.EntityManagerProvider;
import com.senla.training.hoteladmin.dao.client.ClientDao;
import com.senla.training.hoteladmin.dao.reservation.ReservationDao;
import com.senla.training.hoteladmin.dao.reservation.ReservationDaoImpl;
import com.senla.training.hoteladmin.dao.room.RoomDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.csvapi.writeread.ReservationReaderWriter;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.reservation.Reservation;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.model.room.Room_;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.senla.training.hoteladmin.util.DateUtil;
import com.senla.training.hoteladmin.util.sort.ReservationSortCriterion;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Date;
import java.util.List;

@NeedInjectionClass
public class ReservationServiceImpl implements ReservationService {

    private static final Logger LOGGER = LogManager.getLogger(ReservationServiceImpl.class);
    @NeedInjectionField
    private ReservationDao reservationDao = new ReservationDaoImpl();
    @NeedInjectionField
    private RoomDao roomDao;
    @NeedInjectionField
    private ClientDao clientDao;
    @ConfigProperty(propertyName = "clients.numberOfRecords", type = Integer.class)
    private Integer numberOfResidents;


    @Override
    public void addReservation(Client client, Date arrivalDate, Date departureDate) {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Room room = roomDao.getFirstFreeRoom(entityManager);
        if (room == null) {
            LOGGER.error("Error at adding reservation: No free rooms");
            throw new BusinessException("No free rooms");
        }

        transaction.begin();
        try {
            reservationDao.add(new Reservation(room, client, java.sql.Date.valueOf(DateUtil.getString(arrivalDate)),
                            java.sql.Date.valueOf(DateUtil.getString(departureDate)), 1),
                    entityManager);
            roomDao.updateById(room.getId(), 0, Room_.IS_FREE, entityManager);
        } catch (Exception ex) {
            transaction.rollback();
            throw ex;
        }
        transaction.commit();
    }

    @Override
    public void addReservationForExistingClient(Integer clientId, Date arrivalDate, Date departureDate) {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        Client client = clientDao.getById(clientId, entityManager);
        if (client == null) {
            LOGGER.error("Error at adding reservation: No such client");
            throw new BusinessException("No such client");
        }
        addReservation(client, arrivalDate, departureDate);
    }

    @Override
    public void deactivateReservation(Integer clientId, Integer roomId) {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Client client = clientDao.getById(clientId, entityManager);
        if (client == null) {
            LOGGER.error("Error at deactivating reservation: No such client");
            throw new BusinessException("No such client");
        }

        transaction.begin();
        try {
            reservationDao.deactivateClientReservation(clientId, roomId, entityManager);
        } catch (Exception ex) {
            transaction.rollback();
            throw ex;
        }
        transaction.commit();
    }

    @Override
    public List<Reservation> getReservationsExpiredAfterDate(Date date) {
        return reservationDao.getReservationsExpiredAfterDate(date, EntityManagerProvider.getEntityManager());
    }

    @Override
    public List<Reservation> getSortedReservations(ReservationSortCriterion criterion) {
        return reservationDao.getSortedReservations(criterion, EntityManagerProvider.getEntityManager());
    }

    @Override
    public List<Reservation> getLastRoomVisits(Integer roomId) {
        return reservationDao.getLastRoomVisits(roomId, numberOfResidents, EntityManagerProvider.getEntityManager());
    }

    @Override
    public Long getNumberOfResidents() {
        return reservationDao.getNumberOfResidents(EntityManagerProvider.getEntityManager());
    }

    @Override
    public void exportReservations() {
        ReservationReaderWriter.writeReservations(reservationDao
                .getAll(EntityManagerProvider.getEntityManager()));
    }

    @Override
    public void importReservations() {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<Reservation> reservations = ReservationReaderWriter.readReservations();
        transaction.begin();
        try {
            reservations.forEach(reservation -> {
                Reservation existing = reservationDao.getReservationByRoomClient(reservation.getRoom().getId(),
                        reservation.getResident().getId(), entityManager);
                Room room = roomDao.getById(reservation.getRoom().getId(), entityManager);
                Client client = clientDao.getById(reservation.getResident().getId(), entityManager);
                if (existing == null && room != null && client != null && room.getIsFree().equals(1)
                        && room.getStatus().equals(
                        RoomStatus.SERVED)) {
                    reservationDao.add(new Reservation(room, client, java.sql.Date.valueOf(
                            DateUtil.getString(reservation.getArrivalDate())),
                            java.sql.Date.valueOf(DateUtil.getString(reservation.getDeparture())),
                            reservation.getIsActive()), entityManager);
                    roomDao.updateById(room.getId(), 0, Room_.IS_FREE, entityManager);
                }
            });
        } catch (Exception ex) {
            transaction.rollback();
            throw ex;
        }
        transaction.commit();
    }
}
