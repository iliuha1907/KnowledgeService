package com.senla.training.hoteladmin.annotation;

import com.senla.training.hoteladmin.repository.ClientsRepository;
import com.senla.training.hoteladmin.repository.HotelServiceRepository;
import com.senla.training.hoteladmin.repository.RoomsRepository;

public class TestService {
    @ConfigProperty(configName = "resources/app.properties", propertyName = "testService.roomsRepository")
    private RoomsRepository roomsRepository;
    @ConfigProperty(configName = "resources/app.properties", propertyName = "testService.clientsRepository")
    private ClientsRepository clientsRepository;
    @ConfigProperty(configName = "resources/app.properties", propertyName = "testService.hotelServiceRepository")
    private HotelServiceRepository hotelServiceRepository;
    @ConfigProperty(configName = "resources/app.properties", propertyName = "testService.name",
            type = String.class)
    private String name;
    @ConfigProperty(configName = "resources/app.properties", propertyName = "testService.id",
            type = Integer.class)
    private Integer id;

    public void checkRoomsRepository() {
        System.out.println(roomsRepository.getClass().getSimpleName());
    }

    public void checkClientsRepository() {
        System.out.println(clientsRepository.getClass().getSimpleName());
    }

    public void checkHotelServiceRepository() {
        System.out.println(hotelServiceRepository.getClass().getSimpleName());
    }

    public void checkId() {
        System.out.println(id);
    }

    public void checkName() {
        System.out.println(name);
    }
}

