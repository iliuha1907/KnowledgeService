package com.senla.training.hoteladmin.controller;

import com.senla.training.hoteladmin.dto.MessageDto;
import com.senla.training.hoteladmin.dto.VisitDto;
import com.senla.training.hoteladmin.dto.mapper.MessageDtoMapper;
import com.senla.training.hoteladmin.dto.mapper.VisitMapper;
import com.senla.training.hoteladmin.service.visit.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visits")
public class VisitController {

    @Autowired
    private VisitService visitService;
    @Autowired
    private VisitMapper visitMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;

    @PostMapping
    public MessageDto addVisit(@RequestBody VisitDto visit) {
        visitService.addVisit(visit.getServiceId(), visit.getClientId(), visit.getDate());
        return messageDtoMapper.toDto("Successfully added visit");
    }

    @GetMapping()
    public List<VisitDto> getSortedClientVisits(@RequestParam(name = "clientId") Integer id,
                                                @RequestParam(name = "criterion", defaultValue = "PRICE") String criterion) {
        return visitMapper.listToDto(visitService.getSortedClientVisits(id, criterion.toUpperCase()));
    }

    @PostMapping("/export/csv")
    public MessageDto exportVisits() {
        visitService.exportVisits();
        return messageDtoMapper.toDto("Successfully exported visits");
    }

    @PostMapping("/import/csv")
    public MessageDto importVisits() {
        visitService.importVisits();
        return messageDtoMapper.toDto("Successfully imported visits");
    }
}
