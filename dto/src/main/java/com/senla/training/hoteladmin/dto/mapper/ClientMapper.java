package com.senla.training.hoteladmin.dto.mapper;

import com.senla.training.hoteladmin.dto.ClientDto;
import com.senla.training.hoteladmin.model.client.Client;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClientMapper {

    @Autowired
    private ModelMapper mapper;

    public List<ClientDto> listToDto(List<Client> clients) {
        List<ClientDto> clientDtos = new ArrayList<>();
        clients.forEach(client -> {
            if (client != null) {
                clientDtos.add(mapper.map(client, ClientDto.class));
            }
        });
        return clientDtos;
    }
}
