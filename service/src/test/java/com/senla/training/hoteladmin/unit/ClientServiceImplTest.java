package com.senla.training.hoteladmin.unit;

import com.senla.training.hoteladmin.csvapi.writeread.ClientReaderWriter;
import com.senla.training.hoteladmin.dao.client.ClientDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.service.client.ClientServiceImpl;
import config.TestConfigurator;
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
@ContextConfiguration(classes = TestConfigurator.class)
class ClientServiceImplTest {

    private static List<Client> clients;
    @Autowired
    private ClientServiceImpl clientService;
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private ClientReaderWriter clientReaderWriter;

    @BeforeAll
    public static void setUp() {
        Client clientMike = new Client(1, "Mike", "Johnson");
        Client clientJohn = new Client(2, "John", "Robertson");
        clients = Arrays.asList(clientMike, clientJohn);
    }

    @Test
    void ClientServiceImpl_getClients() {
        Mockito.doReturn(clients).when(clientDao).getAll();
        Assertions.assertIterableEquals(clients, clientService.getClients());
    }

    @Test
    void ClientServiceImpl_getClients_BusinessException() {
        String message = "Error at getting";
        Mockito.doThrow(new BusinessException(message)).when(clientDao).getAll();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> clientService.getClients());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ClientServiceImpl_getNumberOfClients() {
        Long expectedNumber = 3L;
        Mockito.doReturn(expectedNumber).when(clientDao).getNumberOfClients();

        Assertions.assertEquals(expectedNumber, clientService.getNumberOfClients());
    }

    @Test
    void ClientServiceImpl_getNumberOfClients_BusinessException() {
        String message = "Error at getting number";
        Mockito.doThrow(new BusinessException(message)).when(clientDao).getNumberOfClients();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> clientService.getNumberOfClients());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ClientServiceImpl_exportClients_BusinessException_byWriting() {
        String message = "Error at writing clients";
        Mockito.doReturn(clients).when(clientDao).getAll();
        Mockito.doThrow(new BusinessException(message)).when(clientReaderWriter).writeClients(clients);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> clientService.exportClients());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ClientServiceImpl_exportClients_BusinessException_byGetting() {
        String message = "Error at getting clients";
        Mockito.doThrow(new BusinessException(message)).when(clientDao).getAll();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> clientService.exportClients());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ClientServiceImpl_importClients_BusinessException_byReading() {
        String message = "Error at reading clients";
        Mockito.doThrow(new BusinessException(message)).when(clientReaderWriter).readClients();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> clientService.importClients());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ClientServiceImpl_importClients_BusinessException_byAdding() {
        String message = "Error at adding client";
        Mockito.doThrow(new BusinessException(message)).when(clientDao).add(clients.get(0));
        Mockito.doReturn(clients).when(clientReaderWriter).readClients();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> clientService.importClients());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }
}
