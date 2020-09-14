package com.senla.training.hoteladmin.service.reservation;

import com.senla.training.hoteladmin.dao.EntityManagerProvider;
import com.senla.training.hoteladmin.dao.client.ClientDao;
import com.senla.training.hoteladmin.dao.reservation.ReservationDao;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

@Component
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private static final Logger LOGGER = LogManager.getLogger(ReservationServiceImpl.class);
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private EntityManagerProvider entityManagerProvider;
    @Autowired
    private ReservationReaderWriter reservationReaderWriter;
    @Value("${clients.numberOfRecords:1}")
    private Integer numberOfResidents;


    @Override
    public void addReservation(Client client, Date arrivalDate, Date departureDate) {
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        Room room = roomDao.getFirstFreeRoom(entityManager);
        if (room == null) {
            LOGGER.error("Error at adding reservation: No free rooms");
            throw new BusinessException("No free rooms");
        }

        try {
            reservationDao.add(new Reservation(room, client, java.sql.Date.valueOf(DateUtil.getString(arrivalDate)),
                            java.sql.Date.valueOf(DateUtil.getString(departureDate)), 1),
                    entityManager);
            roomDao.updateByAttribute(room.getId(), Room_.id, 0, Room_.isFree, entityManager);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public void addReservationForExistingClient(Integer clientId, Date arrivalDate, Date departureDate) {
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        Client client = clientDao.getById(clientId, entityManager);
        if (client == null) {
            LOGGER.error("Error at adding reservation: No such client");
            throw new BusinessException("No such client");
        }
        addReservation(client, arrivalDate, departureDate);
    }

    @Override
    public void deactivateReservation(Integer clientId, Integer roomId) {
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        Client client = clientDao.getById(clientId, entityManager);
        Room room = roomDao.getById(roomId, entityManager);
        if (client == null || room == null) {
            LOGGER.error("Error at deactivating reservation: No such entity");
            throw new BusinessException("No such entity");
        }

        try {
            reservationDao.deactivateClientReservation(client, room, entityManager);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<Reservation> getReservationsExpiredAfterDate(Date date) {
        return reservationDao.getReservationsExpiredAfterDate(date, entityManagerProvider.getEntityManager());
    }

    @Override
    public List<Reservation> getSortedReservations(ReservationSortCriterion criterion) {
        return reservationDao.getSortedReservations(criterion, entityManagerProvider.getEntityManager());
    }

    @Override
    public List<Reservation> getLastRoomReservations(Integer roomId) {
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        Room room = roomDao.getById(roomId, entityManager);
        if (room == null) {
            LOGGER.error("Error at getting reservations: No such room");
            throw new BusinessException("No free rooms");
        }
        return reservationDao.getLastRoomReservations(room, numberOfResidents, entityManager);
    }

    @Override
    public Long getNumberOfResidents() {
        return reservationDao.getNumberOfResidents(entityManagerProvider.getEntityManager());
    }

    @Override
    public void exportReservations() {
        reservationReaderWriter.writeReservations(reservationDao
                .getAll(entityManagerProvider.getEntityManager()));
    }

    @Override
    public void importReservations() {
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        List<Reservation> reservations = reservationReaderWriter.readReservations();
        try {
            reservations.forEach(reservation -> {
                Room room = roomDao.getById(reservation.getRoom().getId(), entityManager);
                Client client = clientDao.getById(reservation.getResident().getId(), entityManager);
                if (room != null && client != null) {
                    Reservation existing = reservationDao.getReservationByRoomClient(client,
                            room, entityManager);
                    if (existing == null && room.getIsFree().equals(1)
                            && room.getStatus().equals(
                            RoomStatus.SERVED)) {
                        reservationDao.add(new Reservation(room, client, java.sql.Date.valueOf(
                                DateUtil.getString(reservation.getArrivalDate())),
                                java.sql.Date.valueOf(DateUtil.getString(reservation.getDeparture())),
                                reservation.getIsActive()), entityManager);
                        roomDao.updateByAttribute(room.getId(), Room_.id, 0, Room_.isFree, entityManager);
                    }
                }
            });
        } catch (Exception ex) {
            throw ex;
        }
    }
}
