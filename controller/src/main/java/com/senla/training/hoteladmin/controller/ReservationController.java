package com.senla.training.hoteladmin.controller;

import com.senla.training.hoteladmin.dto.MessageDto;
import com.senla.training.hoteladmin.dto.ReservationDto;
import com.senla.training.hoteladmin.dto.mapper.MessageDtoMapper;
import com.senla.training.hoteladmin.dto.mapper.ReservationMapper;
import com.senla.training.hoteladmin.service.reservation.ReservationService;
import com.senla.training.hoteladmin.util.sort.ReservationSortCriterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto addReservation(@RequestBody ReservationDto reservation) {
        reservationService.addReservationForExistingClient(reservation.getResidentId(),
                reservation.getArrivalDate(), reservation.getDeparture());
        return messageDtoMapper.toDto("Successfully added reservation");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto deactivateReservation(@PathVariable("id") Integer id) {
        reservationService.deactivateReservation(id);
        return messageDtoMapper.toDto("Successfully deactivated reservation");
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public List<ReservationDto> getReservations(@RequestParam(name = "criterion", defaultValue = "DEPARTURE")
                                                            ReservationSortCriterion criterion,
                                                @RequestParam(name = "expiredAfterDate", required = false)
                                                @DateTimeFormat(pattern = "yyyy-M-dd") Date date) {
        return reservationMapper.listToDto(reservationService.getSortedReservations(criterion, date));
    }

    @GetMapping("/archive")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public List<ReservationDto> getLastRoomReservations(@RequestParam(name = "roomId") Integer roomId) {
        return reservationMapper.listToDto(reservationService.getLastRoomReservations(roomId));
    }

    @GetMapping("/number")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public Long getNumberOfResidents() {
        return reservationService.getNumberOfResidents();
    }

    @PostMapping("/export/csv")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto exportReservations() {
        reservationService.exportReservations();
        return messageDtoMapper.toDto("Successfully exported reservations");
    }

    @PostMapping("/import/csv")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto importReservations() {
        reservationService.importReservations();
        return messageDtoMapper.toDto("Successfully imported reservations");
    }
}
