package com.senla.training.hoteladmin.view;

import injection.annotation.NeedInjectionClass;
import injection.annotation.NeedInjectionField;
import com.senla.training.hoteladmin.controller.ClientController;
import com.senla.training.hoteladmin.controller.HotelServiceController;
import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.util.sort.ClientsSortCriterion;
import com.senla.training.hoteladmin.util.sort.HotelServiceSortCriterion;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NeedInjectionClass
public class Builder {
    private Menu rootMenu;
    @NeedInjectionField
    private ClientController clientController;
    @NeedInjectionField
    private HotelServiceController hotelServiceController;
    @NeedInjectionField
    private RoomController roomController;

    public Builder() {
    }

    private void buildRoomsMenu(Menu roomMenu, Menu freeRoomMenu) {
        List<MenuItem> itemsRoom = new ArrayList<>();
        itemsRoom.add(new MenuItem("Main menu", rootMenu));
        itemsRoom.add(new MenuItem("Display rooms, sorted by price", () ->
                System.out.println(roomController.getSortedRooms(RoomsSortCriterion.PRICE))));
        itemsRoom.add(new MenuItem("Display rooms, sorted by capacity", () ->
                System.out.println(roomController.getSortedRooms(RoomsSortCriterion.CAPACITY))));
        itemsRoom.add(new MenuItem("Display rooms, sorted by stars", () ->
                System.out.println(roomController.getSortedRooms(RoomsSortCriterion.STARS))));
        itemsRoom.add(new MenuItem("Display price of a room", () ->
                System.out.println(roomController.getSortedRooms(RoomsSortCriterion.PRICE))));
        itemsRoom.add(new MenuItem("Display details of a room", () -> {
            Integer id = UserInteraction.getNaturalIntWithMessage("Enter id of the room");
            if (id == null) {
                return;
            }
            System.out.println(roomController.getRoomInfo(id));
        }));
        itemsRoom.add(new MenuItem("Add room", this::addRoomAction));
        itemsRoom.add(new MenuItem("Change price of a room", this::changeRoomPriceAction));
        itemsRoom.add(new MenuItem("Change status of a room", this::changeRoomStatusAction));
        itemsRoom.add(new MenuItem("Free rooms menu", freeRoomMenu));
        roomMenu.setMenuItems(itemsRoom);
    }

    private void buildFreeRoomsMenu(Menu freeRoomMenu, Menu roomMenu) {
        List<MenuItem> itemsFreeRoom = new ArrayList<>();
        itemsFreeRoom.add(new MenuItem("Main menu", rootMenu));
        itemsFreeRoom.add(new MenuItem("Display rooms, sorted by price", () ->
                System.out.println(roomController.getSortedFreeRooms(RoomsSortCriterion.PRICE))));
        itemsFreeRoom.add(new MenuItem("Display rooms, sorted by capacity", () ->
                System.out.println(roomController.getSortedFreeRooms(RoomsSortCriterion.CAPACITY))));
        itemsFreeRoom.add(new MenuItem("Display rooms, sorted by stars", () ->
                System.out.println(roomController.getSortedFreeRooms(RoomsSortCriterion.STARS))));
        itemsFreeRoom.add(new MenuItem("Display number of free rooms", () ->
                System.out.println(roomController.getNumberOfFreeRooms())));
        itemsFreeRoom.add(new MenuItem("Display free rooms after date", () -> {
            Date date = UserInteraction.getDateWithMessage("Enter date");
            if (date == null) {
                return;
            }
            System.out.println(roomController.getFreeRoomsAfterDate(date));
        }));
        itemsFreeRoom.add(new MenuItem("Rooms menu", roomMenu));
        freeRoomMenu.setMenuItems(itemsFreeRoom);
    }

    private void buildClientsMenu(Menu clientMenu) {
        List<MenuItem> itemsClients = new ArrayList<>();
        itemsClients.add(new MenuItem("Main menu", rootMenu));
        itemsClients.add(new MenuItem("Display clients, sorted by departure date", () ->
                System.out.println(clientController.getSortedClients(ClientsSortCriterion.DEPARTURE_DATE))));
        itemsClients.add(new MenuItem("Display clients, sorted by alphabet",
                () -> System.out.println(clientController.getSortedClients(ClientsSortCriterion.ALPHABET))));
        itemsClients.add(new MenuItem("Display last residents of a room", () -> {
            Integer roomId = UserInteraction.getNaturalIntWithMessage("Enter id of the room");
            if (roomId == null) {
                return;
            }
            System.out.println(clientController.getLastResidents(roomId));
        }));
        itemsClients.add(new MenuItem("Display number of residents", () ->
                System.out.println(clientController.getNumberOfResidents())));
        itemsClients.add(new MenuItem("Add resident", this::addClientAction));
        itemsClients.add(new MenuItem("Remove resident", () -> {
            Integer id = UserInteraction.getNaturalIntWithMessage("Enter id of the client");
            if (id == null) {
                return;
            }
            System.out.println(clientController.removeResident(id));
        }));
        clientMenu.setMenuItems(itemsClients);
    }

    private void buildServiceMenu(Menu svcMenu) {
        List<MenuItem> itemsSVC = new ArrayList<>();
        itemsSVC.add(new MenuItem("Main menu", rootMenu));
        itemsSVC.add(new MenuItem("Display services of a client, sorted by date", () -> {
            Integer id = UserInteraction.getNaturalIntWithMessage("Enter id of the client");
            if (id == null) {
                return;
            }
            System.out.println(hotelServiceController.getSortedClientServices(id,
                    HotelServiceSortCriterion.DATE));
        }));
        itemsSVC.add(new MenuItem("Display services of a client, sorted by price", () -> {
            Integer id = UserInteraction.getNaturalIntWithMessage("Enter id");
            if (id == null) {
                return;
            }
            BigDecimal price = UserInteraction.getPositiveBigDecimalWithMessage("Enter price");
            if (price == null) {
                return;
            }
            System.out.println(hotelServiceController.setServicePrice(id, price));
        }));
        itemsSVC.add(new MenuItem("Display services", () ->
                System.out.println(hotelServiceController.getServices())));
        itemsSVC.add(new MenuItem("Change price of a service", this::changeHotelServicePriceAction));
        itemsSVC.add(new MenuItem("Add service", this::addHotelServiceAction));
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

    private void addClientAction() {
        String firstName = UserInteraction.getStringWithMessage("Enter first name");
        String lastName = UserInteraction.getStringWithMessage("Enter last name");
        Date arrival = UserInteraction.getDateWithMessage("Enter arrival date");
        if (arrival == null) {
            return;
        }
        Date departure = UserInteraction.getDateWithMessage("Enter departure date");
        if (departure == null) {
            return;
        }
        System.out.println(clientController.addResident(firstName, lastName, arrival, departure));
    }

    private void addHotelServiceAction() {
        Integer id = UserInteraction.getNaturalIntWithMessage("Enter id of the client");
        if (id == null) {
            return;
        }
        BigDecimal price = UserInteraction.getPositiveBigDecimalWithMessage("Enter price");
        if (price == null) {
            return;
        }
        HotelServiceType type = UserInteraction.getServiceType();
        if (type == null) {
            return;
        }
        Date date = UserInteraction.getDateWithMessage("Enter date");
        if (date == null) {
            return;
        }
        System.out.println(hotelServiceController.addService(price, type, id, date));
    }

    private void changeHotelServicePriceAction() {
        Integer id = UserInteraction.getNaturalIntWithMessage("Enter id");
        if (id == null) {
            return;
        }
        BigDecimal price = UserInteraction.getPositiveBigDecimalWithMessage("Enter price");
        if (price == null) {
            return;
        }
        System.out.println(hotelServiceController.setServicePrice(id, price));
    }

    private void addRoomAction() {
        RoomStatus roomStatus = UserInteraction.getRoomStatus();
        if (roomStatus == null) {
            return;
        }
        BigDecimal price = UserInteraction.getPositiveBigDecimalWithMessage("Enter price");
        if (price == null) {
            return;
        }
        Integer capacity = UserInteraction.getNaturalIntWithMessage("Enter capacity");
        if (capacity == null) {
            return;
        }
        Integer stars = UserInteraction.getNaturalIntWithMessage("Enter stars");
        if (stars == null) {
            return;
        }
        System.out.println(roomController.addRoom(roomStatus, price, capacity, stars));
    }

    private void changeRoomPriceAction() {
        Integer id = UserInteraction.getNaturalIntWithMessage("Enter id of the room");
        if (id == null) {
            return;
        }
        BigDecimal price = UserInteraction.getPositiveBigDecimalWithMessage("Enter price");
        if (price == null) {
            return;
        }
        System.out.println(roomController.setRoomPrice(id, price));
    }

    private void changeRoomStatusAction() {
        Integer id = UserInteraction.getNaturalIntWithMessage("Enter id of the room");
        if (id == null) {
            return;
        }
        RoomStatus roomStatus = UserInteraction.getRoomStatus();
        if (roomStatus == null) {
            return;
        }
        System.out.println(roomController.setRoomStatus(id, roomStatus));
    }
}

