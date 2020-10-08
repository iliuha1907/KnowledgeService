package com.senla.training.hoteladmin.controller;

import com.senla.training.hoteladmin.dto.MessageDto;
import com.senla.training.hoteladmin.dto.ReservationDto;
import com.senla.training.hoteladmin.dto.mapper.MessageDtoMapper;
import com.senla.training.hoteladmin.dto.mapper.ReservationMapper;
import com.senla.training.hoteladmin.service.reservation.ReservationService;
import com.senla.training.hoteladmin.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;

    @PostMapping
    public MessageDto addReservation(@RequestBody ReservationDto reservation) {
        reservationService.addReservationForExistingClient(reservation.getResidentId(),
                reservation.getArrivalDate(), reservation.getDeparture());
        return messageDtoMapper.toDto("Successfully added reservation");
    }

    @DeleteMapping("/{id}")
    public MessageDto deactivateReservation(@PathVariable("id") Integer id) {
        reservationService.deactivateReservation(id);
        return messageDtoMapper.toDto("Successfully deactivated reservation");
    }

    @GetMapping()
    public List<ReservationDto> getReservations(@RequestParam(name = "criterion", defaultValue = "DEPARTURE")
                                                        String criterion) {
        return reservationMapper.listToDto(reservationService.getSortedReservations(criterion));
    }

    @GetMapping("/expired")
    public List<ReservationDto> getReservationsExpiredAfterDate(@RequestParam(name = "date", defaultValue = "2010-1-1")
                                                                            String dateString) {
        Date date = DateUtil.getDate(dateString);
        return reservationMapper.listToDto(reservationService.getReservationsExpiredAfterDate(date));
    }

    @GetMapping("/archive")
    public List<ReservationDto> getLastRoomReservations(@RequestParam(name = "id", defaultValue = "1") Integer id) {
        return reservationMapper.listToDto(reservationService.getLastRoomReservations(id));
    }

    @GetMapping("/number")
    public Long getNumberOfResidents() {
        return reservationService.getNumberOfResidents();
    }

    @PostMapping("/export/csv")
    public MessageDto exportReservations() {
        reservationService.exportReservations();
        return messageDtoMapper.toDto("Successfully exported reservations");
    }

    @PostMapping("/import/csv")
    public MessageDto importReservations() {
        reservationService.importReservations();
        return messageDtoMapper.toDto("Successfully imported reservations");
    }
}
