package com.senla.training.hoteladmin.controller;

import com.senla.training.hoteladmin.dto.MessageDto;
import com.senla.training.hoteladmin.dto.RoomDto;
import com.senla.training.hoteladmin.dto.mapper.MessageDtoMapper;
import com.senla.training.hoteladmin.dto.mapper.RoomMapper;
import com.senla.training.hoteladmin.service.room.RoomService;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto addRoom(@RequestBody RoomDto room) {
        roomService.addRoom(room.getStatus(), room.getPrice(),
                room.getCapacity(), room.getStars());
        return messageDtoMapper.toDto("Successfully added room");
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public List<RoomDto> getRooms(@RequestParam(name = "criterion", defaultValue = "PRICE")
                                          RoomsSortCriterion criterion,
                                  @RequestParam(name = "freeOnly", defaultValue = "false") Boolean freeOnly) {
        if (freeOnly) {
            if (criterion.equals(RoomsSortCriterion.NATURAL)) {
                return roomMapper.listToDto(roomService.getFreeRooms());
            }
            return roomMapper.listToDto(roomService.getSortedFreeRooms(criterion));
        }
        return roomMapper.listToDto(roomService.getSortedRooms(criterion));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto updateRoom(@RequestBody RoomDto room,
                                 @PathVariable("id") Integer id) {
        roomService.updateRoom(roomMapper.toEntity(room), id);
        return messageDtoMapper.toDto("Successfully updated service");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public RoomDto getRoom(@PathVariable("id") Integer id) {
        return roomMapper.toDto(roomService.getRoom(id));
    }

    @GetMapping("/{id}/price")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public BigDecimal getRoomPrice(@PathVariable("id") Integer id) {
        return roomService.getPriceRoom(id);
    }

    @GetMapping("/number")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public Long getNumberOfRooms(@RequestParam(name = "freeOnly", defaultValue = "false") Boolean freeOnly) {
        return roomService.getNumberOfRooms(freeOnly);
    }

    @PostMapping("/import/csv")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto importRooms() {
        roomService.importRooms();
        return messageDtoMapper.toDto("Successfully imported rooms");
    }

    @PostMapping("/export/csv")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto exportRooms() {
        roomService.exportRooms();
        return messageDtoMapper.toDto("Successfully exported rooms");
    }
}
