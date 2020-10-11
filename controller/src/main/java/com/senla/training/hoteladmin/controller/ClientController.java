package com.senla.training.hoteladmin.controller;

import com.senla.training.hoteladmin.dto.ClientDto;
import com.senla.training.hoteladmin.dto.MessageDto;
import com.senla.training.hoteladmin.dto.mapper.ClientMapper;
import com.senla.training.hoteladmin.dto.mapper.MessageDtoMapper;
import com.senla.training.hoteladmin.service.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;

    @GetMapping
    public List<ClientDto> getClients() {
        return clientMapper.listToDto(clientService.getClients());
    }

    @PostMapping
    public MessageDto addClient(@RequestBody ClientDto client) {
        clientService.addClient(client.getName(), client.getLastName());
        return messageDtoMapper.toDto("Successfully added client");
    }

    @GetMapping("/total")
    public Long getNumberOfClients() {
        return clientService.getNumberOfClients();
    }

    @PostMapping("/export/csv")
    public MessageDto exportClients() {
        clientService.exportClients();
        return messageDtoMapper.toDto("Successfully exported clients");
    }

    @PostMapping("/import/csv")
    public MessageDto importClients() {
        clientService.importClients();
        return messageDtoMapper.toDto("Successfully imported clients");
    }
}
