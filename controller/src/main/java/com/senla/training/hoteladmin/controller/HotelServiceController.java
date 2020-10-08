package com.senla.training.hoteladmin.controller;

import com.senla.training.hoteladmin.dto.HotelServiceDto;
import com.senla.training.hoteladmin.dto.MessageDto;
import com.senla.training.hoteladmin.dto.mapper.HotelServiceMapper;
import com.senla.training.hoteladmin.dto.mapper.MessageDtoMapper;
import com.senla.training.hoteladmin.service.hotelservice.HotelServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
public class HotelServiceController {

    @Autowired
    private HotelServiceService hotelServiceService;
    @Autowired
    private HotelServiceMapper hotelServiceMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;

    @GetMapping()
    public List<HotelServiceDto> getServices() {
        return hotelServiceMapper.listToDto(hotelServiceService.getServices());
    }

    @PostMapping
    public MessageDto addService(@RequestBody HotelServiceDto hotelService) {
        hotelServiceService.addService(hotelService.getPrice(),
                (hotelService.getType()));
        return messageDtoMapper.toDto("Successfully added service");
    }

    @PatchMapping("/{id}")
    public MessageDto updateService(@RequestBody HotelServiceDto hotelService,
                                @PathVariable("id") Integer id) {
        hotelServiceService.updateService(hotelServiceMapper.toEntity(hotelService), id);
        return messageDtoMapper.toDto("Successfully updated service");
    }

    @PostMapping("/export/csv")
    public MessageDto exportServices() {
        hotelServiceService.exportServices();
        return messageDtoMapper.toDto("Successfully exported services");
    }

    @PostMapping("/import/csv")
    public MessageDto importServices() {
        hotelServiceService.importServices();
        return messageDtoMapper.toDto("Successfully imported services");
    }
}
