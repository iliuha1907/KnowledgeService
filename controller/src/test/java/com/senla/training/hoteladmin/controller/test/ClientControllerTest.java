package com.senla.training.hoteladmin.controller.test;

import com.senla.training.hoteladmin.controller.ClientController;
import com.senla.training.hoteladmin.csvapi.writeread.ClientReaderWriter;
import com.senla.training.hoteladmin.dao.client.ClientDao;
import com.senla.training.hoteladmin.dto.ClientDto;
import com.senla.training.hoteladmin.dto.MessageDto;
import com.senla.training.hoteladmin.dto.config.DtoMapperConfiguration;
import com.senla.training.hoteladmin.dto.mapper.ClientMapper;
import com.senla.training.hoteladmin.dto.mapper.MessageDtoMapper;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.client.Client;
import config.ControllerTestConfigurator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ControllerTestConfigurator.class, DtoMapperConfiguration.class})
public class ClientControllerTest {

    private static List<Client> clients;
    private static List<ClientDto> clientsDto;
    @Autowired
    ClientController clientController;
    @Autowired
    ClientDao clientDao;
    @Autowired
    ClientReaderWriter clientReaderWriter;
    @Autowired
    ClientMapper clientMapper;
    @Autowired
    MessageDtoMapper messageDtoMapper;

    @BeforeAll
    public static void setUp() {
        Client clientMike = new Client(1, "Mike", "Johnson");
        Client clientJohn = new Client(2, "John", "Robertson");
        clients = Arrays.asList(clientMike, clientJohn);

        ClientDto clientDtoMike = new ClientDto(1, "Mike", "Johnson");
        ClientDto clientDtoJohn = new ClientDto(2, "John", "Robertson");
        clientsDto = Arrays.asList(clientDtoMike, clientDtoJohn);
    }

    @Test
    void ClientController_addClient() {
        String message = "Successfully added client";
        MessageDto messageDto = new MessageDto(message);
        Mockito.doReturn(clients).when(clientDao).getAll();
        Mockito.doReturn(messageDto).when(messageDtoMapper).toDto(message);

        Assertions.assertEquals(messageDto, clientController.addClient(clientsDto.get(0)));
    }

    @Test
    void ClientController_getClients() {
        Mockito.doReturn(clients).when(clientDao).getAll();
        Mockito.doReturn(clientsDto).when(clientMapper).listToDto(clients);
        Assertions.assertIterableEquals(clientsDto, clientController.getClients());
    }

    @Test
    void ClientController_getClients_BusinessException() {
        String message = "Error at getting";
        Mockito.doThrow(new BusinessException(message)).when(clientDao).getAll();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> clientController.getClients());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ClientController_getNumberOfClients() {
        Long expectedNumber = 3L;
        Mockito.doReturn(expectedNumber).when(clientDao).getNumberOfClients();

        Assertions.assertEquals(expectedNumber, clientController.getNumberOfClients());
    }

    @Test
    void ClientController_getNumberOfClients_BusinessException() {
        String message = "Error at getting number";
        Mockito.doThrow(new BusinessException(message)).when(clientDao).getNumberOfClients();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> clientController.getNumberOfClients());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ClientController_exportClients() {
        String message = "Successfully exported clients";
        MessageDto messageDto = new MessageDto(message);
        Mockito.reset(clientReaderWriter);
        Mockito.doReturn(clients).when(clientDao).getAll();
        Mockito.doReturn(messageDto).when(messageDtoMapper).toDto(message);

        Assertions.assertEquals(messageDto, clientController.exportClients());
    }

    @Test
    void ClientController_exportClients_BusinessException_byWriting() {
        String message = "Error at writing clients";
        Mockito.doReturn(clients).when(clientDao).getAll();
        Mockito.doThrow(new BusinessException(message)).when(clientReaderWriter).writeClients(clients);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> clientController.exportClients());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ClientController_exportClients_BusinessException_byGetting() {
        String message = "Error at getting clients";
        Mockito.doThrow(new BusinessException(message)).when(clientDao).getAll();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> clientController.exportClients());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ClientController_importClients() {
        String message = "Successfully imported clients";
        MessageDto messageDto = new MessageDto(message);
        Mockito.reset(clientDao);
        Mockito.doReturn(clients).when(clientDao).getAll();
        Mockito.doReturn(clients).when(clientReaderWriter).readClients();
        Mockito.doReturn(messageDto).when(messageDtoMapper).toDto(message);


        Assertions.assertEquals(messageDto, clientController.importClients());
    }

    @Test
    void ClientController_importClients_BusinessException_byReading() {
        String message = "Error at reading clients";
        Mockito.doThrow(new BusinessException(message)).when(clientReaderWriter).readClients();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> clientController.importClients());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ClientController_importClients_BusinessException_byAdding() {
        String message = "Error at importing clients";
        Mockito.doThrow(new BusinessException(message)).when(clientDao).add(clients.get(0));
        Mockito.doReturn(clients).when(clientReaderWriter).readClients();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> clientController.importClients());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }
}
