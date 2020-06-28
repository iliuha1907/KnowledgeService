package com.senla.training.hoteladmin.view;

import com.senla.training.hoteladmin.controller.ClientController;
import com.senla.training.hoteladmin.controller.HotelServiceController;
import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.repository.ClientsArchiveRepositoryImpl;
import com.senla.training.hoteladmin.repository.ClientsRepositoryImpl;
import com.senla.training.hoteladmin.repository.HotelServiceRepositoryImpl;
import com.senla.training.hoteladmin.repository.RoomsRepositoryImpl;
import com.senla.training.hoteladmin.service.*;
import com.senla.training.hoteladmin.util.sort.ClientsSortCriterion;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;
import com.senla.training.hoteladmin.view.action.clients.*;
import com.senla.training.hoteladmin.view.action.freeRooms.*;
import com.senla.training.hoteladmin.view.action.rooms.*;
import com.senla.training.hoteladmin.view.action.hotelService.*;

public class Builder {
    private static Builder instance;
    private Menu rootMenu;
    private ClientController clientController;
    private HotelServiceController hotelServiceController;
    private RoomController roomController;

    private Builder() {
        clientController = ClientController.getInstance(ClientServiceImpl.
                getInstance(ArchivServiceImpl.getInstance(ClientsArchiveRepositoryImpl.getInstance()),
                        HotelServiceServiceImpl.getInstance(HotelServiceRepositoryImpl.getInstance(), HotelServiceWriterImpl.getInstance()),
                        ClientsRepositoryImpl.getInstance(), RoomsRepositoryImpl.getInstance(), ClientWriterImpl.getInstance()));

        hotelServiceController = HotelServiceController.getInstance(
                HotelServiceServiceImpl.getInstance(HotelServiceRepositoryImpl.getInstance(), HotelServiceWriterImpl.getInstance()),
                ClientServiceImpl.getInstance(ArchivServiceImpl.getInstance(ClientsArchiveRepositoryImpl.getInstance()),
                        HotelServiceServiceImpl.getInstance(HotelServiceRepositoryImpl.getInstance(), HotelServiceWriterImpl.getInstance()),
                        ClientsRepositoryImpl.getInstance(), RoomsRepositoryImpl.getInstance(), ClientWriterImpl.getInstance()));
        roomController = RoomController.getInstance(
                RoomServiceImpl.getInstance(RoomsRepositoryImpl.getInstance(), RoomWriterImpl.getInstance()));
    }

    public static Builder getInstance() {
        if (instance == null) {
            instance = new Builder();
        }
        return instance;
    }

    private void buildRoomsMenu(Menu roomMenu, Menu freeRoomMenu) {
        MenuItem[] itemsRoom = new MenuItem[10];
        IAction detailsRoom = new RoomDetailsAction();
        IAction addRoom = new AddRoomAction();
        IAction changePriceRoom = new ChangeRoomPriceAction();
        IAction changeStatusRoom = new ChangeRoomStatusAction();
        itemsRoom[0] = new MenuItem("Main menu", null, rootMenu);
        itemsRoom[1] = new MenuItem("Display rooms, sorted by price", () ->
                System.out.println(roomController.getSortedRooms(RoomsSortCriterion.PRICE)), null);

        itemsRoom[2] = new MenuItem("Display rooms, sorted by capacity", () ->
                System.out.println(roomController.getSortedRooms(RoomsSortCriterion.CAPACITY)), null);
        itemsRoom[3] = new MenuItem("Display rooms, sorted by stars", () ->
                System.out.println(roomController.getSortedRooms(RoomsSortCriterion.STARS)), null);
        itemsRoom[4] = new MenuItem("Display price of a room", () ->
                System.out.println(roomController.getSortedRooms(RoomsSortCriterion.PRICE)), null);

        itemsRoom[5] = new MenuItem("Display details of a room", detailsRoom, null);
        itemsRoom[6] = new MenuItem("Add room", addRoom, null);
        itemsRoom[7] = new MenuItem("Change price of a room", changePriceRoom, null);
        itemsRoom[8] = new MenuItem("Change status of a room", changeStatusRoom, null);
        itemsRoom[9] = new MenuItem("Free rooms menu", null, freeRoomMenu);
        roomMenu.setMenuItems(itemsRoom);
    }

    public void buildFreeRoomsMenu(Menu freeRoomMenu, Menu roomMenu) {
        MenuItem[] itemsFreeRoom = new MenuItem[7];
        IAction freeRoomsDate = new FreeRoomsAfterDateAction();
        itemsFreeRoom[0] = new MenuItem("Main menu", null, rootMenu);
        itemsFreeRoom[1] = new MenuItem("Display rooms, sorted by price", () ->
                System.out.println(roomController.getSortedFreeRooms(RoomsSortCriterion.PRICE)), null);
        itemsFreeRoom[2] = new MenuItem("Display rooms, sorted by capacity", () ->
                System.out.println(roomController.getSortedFreeRooms(RoomsSortCriterion.CAPACITY)), null);
        itemsFreeRoom[3] = new MenuItem("Display rooms, sorted by stars", () ->
                System.out.println(roomController.getSortedFreeRooms(RoomsSortCriterion.STARS)), null);
        itemsFreeRoom[4] = new MenuItem("Display number of free rooms", () ->
                System.out.println(roomController.getNumberOfFreeRooms()), null);
        itemsFreeRoom[5] = new MenuItem("Display free rooms after date", freeRoomsDate, null);
        itemsFreeRoom[6] = new MenuItem("Rooms menu", null, roomMenu);
        freeRoomMenu.setMenuItems(itemsFreeRoom);
    }

    private void buildClientsMenu(Menu clientMenu) {
        MenuItem[] itemsClients = new MenuItem[7];
        IAction lastClients = new LastRoomResidentsAction();
        IAction addClient = new AddClientAction();
        IAction removeClient = new RemoveClientAction();
        itemsClients[0] = new MenuItem("Main menu", null, rootMenu);
        itemsClients[1] = new MenuItem("Display clients, sorted by departure date", () ->
                System.out.println(clientController.getSortedClients(ClientsSortCriterion.DEPARTURE_DATE)), null);
        itemsClients[2] = new MenuItem("Display clients, sorted by alphabet",
                () -> System.out.println(clientController.getSortedClients(ClientsSortCriterion.ALPHABET)), null);
        itemsClients[3] = new MenuItem("Display last 3 residents of a room", lastClients, null);
        itemsClients[4] = new MenuItem("Display number of residents", () ->
                System.out.println(clientController.getNumberOfResidents()), null);
        itemsClients[5] = new MenuItem("Add resident", addClient, null);
        itemsClients[6] = new MenuItem("Remove resident", removeClient, null);
        clientMenu.setMenuItems(itemsClients);
    }

    private void buildServiceMenu(Menu svcMenu) {
        MenuItem[] itemsSVC = new MenuItem[6];
        IAction servicesDate = new ClientHotelServicesDateAction();
        IAction servicesPrice = new ClientHotelServicesPriceAction();
        IAction addService = new AddHotelServiceAction();
        IAction changeServicePrice = new ChangeHotelServicePriceAction();
        itemsSVC[0] = new MenuItem("Main menu", null, rootMenu);
        itemsSVC[1] = new MenuItem("Display services of a client, sorted by date", servicesDate, null);
        itemsSVC[2] = new MenuItem("Display services of a client, sorted by price", servicesPrice, null);
        itemsSVC[3] = new MenuItem("Display services", () ->
                System.out.println(hotelServiceController.getServices()), null);
        itemsSVC[4] = new MenuItem("Change price of a service", changeServicePrice, null);
        itemsSVC[5] = new MenuItem("Add service", addService, null);
        svcMenu.setMenuItems(itemsSVC);
    }

    private void buildRootMenu(Menu roomMenu, Menu clientMenu, Menu svcMenu) {
        MenuItem[] itemsMain = new MenuItem[9];
        itemsMain[0] = new MenuItem("Room menu", null, roomMenu);
        itemsMain[1] = new MenuItem("Client menu", null, clientMenu);
        itemsMain[2] = new MenuItem("Services menu", null, svcMenu);
        itemsMain[3] = new MenuItem("Export services", () ->
                System.out.println(hotelServiceController.exportServices()), null);
        itemsMain[4] = new MenuItem("Export clients", () ->
                System.out.println(clientController.exportClients()), null);
        itemsMain[5] = new MenuItem("Export rooms", () ->
                System.out.println(roomController.exportRooms()), null);
        itemsMain[6] = new MenuItem("Import services", () ->
                System.out.println(hotelServiceController.importServices()), null);
        itemsMain[7] = new MenuItem("Import clients", () ->
                System.out.println(clientController.importClients()), null);
        itemsMain[8] = new MenuItem("Import rooms", () -> {
            System.out.println(roomController.importRooms());
        },
                null);
        rootMenu.setMenuItems(itemsMain);
    }

    public void buildMenu() {
        rootMenu = new Menu();
        rootMenu.setName("Main menu");
        Menu roomMenu = new Menu();
        roomMenu.setName("Rooms menu");
        Menu freeRoomMenu = new Menu();
        freeRoomMenu.setName("Free rooms menu");
        Menu clientMenu = new Menu();
        clientMenu.setName("Clients menu");
        Menu svcMenu = new Menu();
        svcMenu.setName("Services menu");

        buildRootMenu(roomMenu, clientMenu, svcMenu);
        buildRoomsMenu(roomMenu, freeRoomMenu);
        buildFreeRoomsMenu(freeRoomMenu, roomMenu);
        buildClientsMenu(clientMenu);
        buildServiceMenu(svcMenu);
    }

    public Menu getRootMenu() {
        return rootMenu;
    }
}

