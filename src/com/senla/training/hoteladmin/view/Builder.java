package com.senla.training.hoteladmin.view;

import com.senla.training.hoteladmin.controller.ClientController;
import com.senla.training.hoteladmin.controller.HotelServiceController;
import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.util.sort.ClientsSortCriterion;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;
import com.senla.training.hoteladmin.view.action.clients.*;
import com.senla.training.hoteladmin.view.action.freerooms.*;
import com.senla.training.hoteladmin.view.action.rooms.*;
import com.senla.training.hoteladmin.view.action.hotelservice.*;

import java.util.ArrayList;
import java.util.List;

public class Builder {
    private static Builder instance;
    private Menu rootMenu;
    private ClientController clientController;
    private HotelServiceController hotelServiceController;
    private RoomController roomController;

    private Builder() {
        clientController = ClientController.getInstance();
        hotelServiceController = HotelServiceController.getInstance();
        roomController = RoomController.getInstance();
    }

    public static Builder getInstance() {
        if (instance == null) {
            instance = new Builder();
        }
        return instance;
    }

    private void buildRoomsMenu(Menu roomMenu, Menu freeRoomMenu) {
        List<MenuItem> itemsRoom = new ArrayList<>();
        IAction detailsRoom = new RoomDetailsAction();
        IAction addRoom = new AddRoomAction();
        IAction changePriceRoom = new ChangeRoomPriceAction();
        IAction changeStatusRoom = new ChangeRoomStatusAction();
        itemsRoom.add(new MenuItem("Main menu", rootMenu));
        itemsRoom.add(new MenuItem("Display rooms, sorted by price", () ->
                System.out.println(roomController.getSortedRooms(RoomsSortCriterion.PRICE))));

        itemsRoom.add(new MenuItem("Display rooms, sorted by capacity", () ->
                System.out.println(roomController.getSortedRooms(RoomsSortCriterion.CAPACITY))));
        itemsRoom.add(new MenuItem("Display rooms, sorted by stars", () ->
                System.out.println(roomController.getSortedRooms(RoomsSortCriterion.STARS))));
        itemsRoom.add(new MenuItem("Display price of a room", () ->
                System.out.println(roomController.getSortedRooms(RoomsSortCriterion.PRICE))));

        itemsRoom.add(new MenuItem("Display details of a room", detailsRoom));
        itemsRoom.add(new MenuItem("Add room", addRoom));
        itemsRoom.add(new MenuItem("Change price of a room", changePriceRoom));
        itemsRoom.add(new MenuItem("Change status of a room", changeStatusRoom));
        itemsRoom.add(new MenuItem("Free rooms menu", freeRoomMenu));
        roomMenu.setMenuItems(itemsRoom);
    }

    private void buildFreeRoomsMenu(Menu freeRoomMenu, Menu roomMenu) {
        List<MenuItem> itemsFreeRoom = new ArrayList<>();
        IAction freeRoomsDate = new FreeRoomsAfterDateAction();
        itemsFreeRoom.add(new MenuItem("Main menu", rootMenu));
        itemsFreeRoom.add(new MenuItem("Display rooms, sorted by price", () ->
                System.out.println(roomController.getSortedFreeRooms(RoomsSortCriterion.PRICE))));
        itemsFreeRoom.add(new MenuItem("Display rooms, sorted by capacity", () ->
                System.out.println(roomController.getSortedFreeRooms(RoomsSortCriterion.CAPACITY))));
        itemsFreeRoom.add(new MenuItem("Display rooms, sorted by stars", () ->
                System.out.println(roomController.getSortedFreeRooms(RoomsSortCriterion.STARS))));
        itemsFreeRoom.add(new MenuItem("Display number of free rooms", () ->
                System.out.println(roomController.getNumberOfFreeRooms())));
        itemsFreeRoom.add(new MenuItem("Display free rooms after date", freeRoomsDate));
        itemsFreeRoom.add(new MenuItem("Rooms menu", roomMenu));
        freeRoomMenu.setMenuItems(itemsFreeRoom);
    }

    private void buildClientsMenu(Menu clientMenu) {
        List<MenuItem> itemsClients = new ArrayList<>();
        IAction lastClients = new LastRoomResidentsAction();
        IAction addClient = new AddClientAction();
        IAction removeClient = new RemoveClientAction();
        itemsClients.add(new MenuItem("Main menu", rootMenu));
        itemsClients.add(new MenuItem("Display clients, sorted by departure date", () ->
                System.out.println(clientController.getSortedClients(ClientsSortCriterion.DEPARTURE_DATE))));
        itemsClients.add(new MenuItem("Display clients, sorted by alphabet",
                () -> System.out.println(clientController.getSortedClients(ClientsSortCriterion.ALPHABET))));
        itemsClients.add(new MenuItem("Display last residents of a room", lastClients));
        itemsClients.add(new MenuItem("Display number of residents", () ->
                System.out.println(clientController.getNumberOfResidents())));
        itemsClients.add(new MenuItem("Add resident", addClient));
        itemsClients.add(new MenuItem("Remove resident", removeClient));
        clientMenu.setMenuItems(itemsClients);
    }

    private void buildServiceMenu(Menu svcMenu) {
        List<MenuItem> itemsSVC = new ArrayList<>();
        IAction servicesDate = new ClientHotelServicesDateAction();
        IAction servicesPrice = new ClientHotelServicesPriceAction();
        IAction addService = new AddHotelServiceAction();
        IAction changeServicePrice = new ChangeHotelServicePriceAction();
        itemsSVC.add(new MenuItem("Main menu", rootMenu));
        itemsSVC.add(new MenuItem("Display services of a client, sorted by date", servicesDate));
        itemsSVC.add(new MenuItem("Display services of a client, sorted by price", servicesPrice));
        itemsSVC.add(new MenuItem("Display services", () ->
                System.out.println(hotelServiceController.getServices())));
        itemsSVC.add(new MenuItem("Change price of a service", changeServicePrice));
        itemsSVC.add(new MenuItem("Add service", addService));
        svcMenu.setMenuItems(itemsSVC);
    }

    private void buildRootMenu(Menu roomMenu, Menu clientMenu, Menu svcMenu) {
        List<MenuItem> itemsMain = new ArrayList<>();
        itemsMain.add(new MenuItem("Room menu", roomMenu));
        itemsMain.add(new MenuItem("Client menu", clientMenu));
        itemsMain.add(new MenuItem("Services menu", svcMenu));
        itemsMain.add(new MenuItem("Export services", () ->
                System.out.println(hotelServiceController.exportServices())));
        itemsMain.add(new MenuItem("Export clients", () ->
                System.out.println(clientController.exportClients())));
        itemsMain.add(new MenuItem("Export rooms", () ->
                System.out.println(roomController.exportRooms())));
        itemsMain.add(new MenuItem("Import services", () ->
                System.out.println(hotelServiceController.importServices())));
        itemsMain.add(new MenuItem("Import clients", () ->
                System.out.println(clientController.importClients())));
        itemsMain.add(new MenuItem("Import rooms", () ->
                System.out.println(roomController.importRooms())));
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

