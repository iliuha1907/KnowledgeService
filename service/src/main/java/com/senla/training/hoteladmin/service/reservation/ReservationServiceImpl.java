package com.senla.training.hoteladmin.service.reservation;

import com.senla.training.hoteladmin.dao.client.ClientDao;
import com.senla.training.hoteladmin.dao.reservation.ReservationDao;
import com.senla.training.hoteladmin.dao.room.RoomDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.csvapi.writeread.ReservationReaderWriter;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.reservation.Reservation;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.senla.training.hoteladmin.util.DateUtil;
import com.senla.training.hoteladmin.util.sort.ReservationSortCriterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
public class ReservationServiceImpl implements ReservationService {

    private static final Logger LOGGER = LogManager.getLogger(ReservationServiceImpl.class);
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private ReservationReaderWriter reservationReaderWriter;
    @Value("${clients.numberOfRecords:1}")
    private Integer numberOfResidents;


    @Override
    @Transactional
    public void addReservation(Client client, Date arrivalDate, Date departureDate) {
        Room room = roomDao.getFirstFreeRoom();
        if (room == null) {
            LOGGER.error("Error at adding reservation: No free rooms");
            throw new BusinessException("No free rooms");
        }

        reservationDao.add(new Reservation(room, client, java.sql.Date.valueOf(DateUtil.getString(arrivalDate)),
                java.sql.Date.valueOf(DateUtil.getString(departureDate)), 1));
        room.setIsFree(0);
        roomDao.update(room);
    }

    @Override
    @Transactional
    public void addReservationForExistingClient(Integer clientId, Date arrivalDate, Date departureDate) {
        Client client = clientDao.getById(clientId);
        if (client == null) {
            LOGGER.error("Error at adding reservation: No such client");
            throw new BusinessException("No such client");
        }
        addReservation(client, arrivalDate, departureDate);
    }

    @Override
    @Transactional
    public void deactivateReservation(Integer clientId, Integer roomId) {
        Client client = clientDao.getById(clientId);
        Room room = roomDao.getById(roomId);
        if (client == null || room == null) {
            LOGGER.error("Error at deactivating reservation: No such entity");
            throw new BusinessException("No such entity");
        }

        reservationDao.deactivateClientReservation(client, room);
    }

    @Override
    @Transactional
    public List<Reservation> getReservationsExpiredAfterDate(Date date) {
        return reservationDao.getReservationsExpiredAfterDate(date);
    }

    @Override
    @Transactional
    public List<Reservation> getSortedReservations(ReservationSortCriterion criterion) {
        return reservationDao.getSortedReservations(criterion);
    }

    @Override
    @Transactional
    public List<Reservation> getLastRoomReservations(Integer roomId) {
        Room room = roomDao.getById(roomId);
        if (room == null) {
            LOGGER.error("Error at getting reservations: No such room");
            throw new BusinessException("No free rooms");
        }
        return reservationDao.getLastRoomReservations(room, numberOfResidents);
    }

    @Override
    @Transactional
    public Long getNumberOfResidents() {
        return reservationDao.getNumberOfResidents();
    }

    @Override
    @Transactional
    public void exportReservations() {
        reservationReaderWriter.writeReservations(reservationDao.getAll());
    }

    @Override
    @Transactional
    public void importReservations() {
        List<Reservation> reservations = reservationReaderWriter.readReservations();
        reservations.forEach(reservation -> {
            Room room = roomDao.getById(reservation.getRoom().getId());
            Client client = clientDao.getById(reservation.getResident().getId());
            if (room != null && client != null) {
                Reservation existing = reservationDao.getReservationByRoomClient(client, room);
                if (existing == null && room.getIsFree().equals(1)
                        && room.getStatus().equals(RoomStatus.SERVED.toString())) {
                    reservationDao.add(new Reservation(room, client, java.sql.Date.valueOf(
                            DateUtil.getString(reservation.getArrivalDate())),
                            java.sql.Date.valueOf(DateUtil.getString(reservation.getDeparture())),
                            reservation.getIsActive()));
                    room.setIsFree(0);
                    roomDao.update(room);
                }
            }
        });
    }
}
