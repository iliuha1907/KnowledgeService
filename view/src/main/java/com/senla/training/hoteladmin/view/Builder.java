package com.senla.training.hoteladmin.view;

import com.senla.training.hoteladmin.annotationapi.ConfigProperty;
import com.senla.training.hoteladmin.annotationapi.NeedInjectionClass;
import com.senla.training.hoteladmin.annotationapi.NeedInjectionField;
import com.senla.training.hoteladmin.controller.*;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.util.sort.ReservationSortCriterion;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;
import com.senla.training.hoteladmin.util.sort.VisitSortCriterion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NeedInjectionClass
public class Builder {

    @ConfigProperty(propertyName = "date.dateFormat", type = String.class)
    private String dateFormat;
    private Menu rootMenu;
    @NeedInjectionField
    private ClientController clientController;
    @NeedInjectionField
    private HotelServiceController hotelServiceController;
    @NeedInjectionField
    private RoomController roomController;
    @NeedInjectionField
    private VisitController visitController;
    @NeedInjectionField
    private ReservationController reservationController;

    public Builder() {
    }

    private void buildRoomsMenu(final Menu roomMenu, final Menu freeRoomMenu) {
        List<MenuItem> itemsRoom = new ArrayList<>();
        itemsRoom.add(new MenuItem("Main menu", rootMenu));
        itemsRoom.add(new MenuItem("Display rooms, sorted by price", () ->
                System.out.println(roomController.getSortedRooms(RoomsSortCriterion.PRICE))));
        itemsRoom.add(new MenuItem("Display rooms, sorted by capacity", () ->
                System.out.println(roomController.getSortedRooms(RoomsSortCriterion.CAPACITY))));
        itemsRoom.add(new MenuItem("Display rooms, sorted by stars", () ->
                System.out.println(roomController.getSortedRooms(RoomsSortCriterion.STARS))));
        itemsRoom.add(new MenuItem("Display price of a room", () -> {
            Integer id = UserInteraction.getNaturalIntWithMessage("Enter id of the room");
            if (id == null) {
                return;
            }
            System.out.println(roomController.getPriceRoom(id));
        }));
        itemsRoom.add(new MenuItem("Display details of a room", () -> {
            Integer id = UserInteraction.getNaturalIntWithMessage("Enter id of the room");
            if (id == null) {
                return;
            }
            System.out.println(roomController.getRoom(id));
        }));
        itemsRoom.add(new MenuItem("Add room", this::addRoomAction));
        itemsRoom.add(new MenuItem("Change price of a room", this::changeRoomPriceAction));
        itemsRoom.add(new MenuItem("Change status of a room", this::changeRoomStatusAction));
        itemsRoom.add(new MenuItem("Free rooms menu", freeRoomMenu));
        roomMenu.setMenuItems(itemsRoom);
    }

    private void buildFreeRoomsMenu(final Menu freeRoomMenu, final Menu roomMenu) {
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
        itemsFreeRoom.add(new MenuItem("Display free rooms and expired reservations after date", () -> {
            Date date = UserInteraction.getDateWithMessage("Enter date(" + dateFormat + ")");
            if (date == null) {
                return;
            }
            System.out.println(reservationController.getReservationsExpiredAfterDate(date) + "\n"
                    + roomController.getFreeRooms());
        }));
        itemsFreeRoom.add(new MenuItem("Rooms menu", roomMenu));
        freeRoomMenu.setMenuItems(itemsFreeRoom);
    }

    private void buildClientsMenu(final Menu clientMenu) {
        List<MenuItem> itemsClients = new ArrayList<>();
        itemsClients.add(new MenuItem("Main menu", rootMenu));
        itemsClients.add(new MenuItem("Display reservations and clients, sorted by departure date", () ->
                System.out.println(reservationController.getSortedReservations(
                        ReservationSortCriterion.DEPARTURE))));
        itemsClients.add(new MenuItem("Display reservations and clients, sorted by alphabet",
                () -> System.out.println(reservationController.getSortedReservations(
                        ReservationSortCriterion.NAME))));
        itemsClients.add(new MenuItem("Display last reservations and residents of a room", () -> {
            Integer roomId = UserInteraction.getNaturalIntWithMessage("Enter id of the room");
            if (roomId == null) {
                return;
            }
            System.out.println(reservationController.getLastRoomReservations(roomId));
        }));
        itemsClients.add(new MenuItem("Display number of residents", () ->
                System.out.println(reservationController.getNumberOfResidents())));
        itemsClients.add(new MenuItem("Display number of clients", () ->
                System.out.println(clientController.getNumberOfClients())));
        itemsClients.add(new MenuItem("Display clients", () ->
                System.out.println(clientController.getClients())));
        itemsClients.add(new MenuItem("Add client", this::addClientAction));
        itemsClients.add(new MenuItem("Remove resident", () -> {
            Integer clientId = UserInteraction.getNaturalIntWithMessage("Enter id of the client");
            if (clientId == null) {
                return;
            }
            Integer roomId = UserInteraction.getNaturalIntWithMessage("Enter id of the room");
            if (roomId == null) {
                return;
            }
            System.out.println(reservationController.deactivateReservation(clientId, roomId));
        }));
        itemsClients.add(new MenuItem("Add resident from clients", this::addResidentFromClientAction));
        clientMenu.setMenuItems(itemsClients);
    }

    private void buildServiceMenu(final Menu svcMenu) {
        List<MenuItem> itemsSVC = new ArrayList<>();
        itemsSVC.add(new MenuItem("Main menu", rootMenu));
        itemsSVC.add(new MenuItem("Display service of a client, sorted by date", () -> {
            Integer id = UserInteraction.getNaturalIntWithMessage("Enter id of the client");
            if (id == null) {
                return;
            }
            System.out.println(visitController.getSortedClientVisits(id,
                    VisitSortCriterion.DATE));
        }));
        itemsSVC.add(new MenuItem("Display service of a client, sorted by price", () -> {
            Integer id = UserInteraction.getNaturalIntWithMessage("Enter id of the client");
            if (id == null) {
                return;
            }
            System.out.println(visitController.getSortedClientVisits(id,
                    VisitSortCriterion.PRICE));
        }));
        itemsSVC.add(new MenuItem("Display services", () ->
                System.out.println(hotelServiceController.getServices())));
        itemsSVC.add(new MenuItem("Change price of a service", this::changeHotelServicePriceAction));
        itemsSVC.add(new MenuItem("Add service", this::addHotelServiceAction));
        itemsSVC.add(new MenuItem("Add service visit ", () -> {
            Integer clientId = UserInteraction.getNaturalIntWithMessage("Enter id of a client");
            if (clientId == null) {
                return;
            }
            Integer serviceId = UserInteraction.getNaturalIntWithMessage("Enter id of the service");
            if (serviceId == null) {
                return;
            }
            Date date = UserInteraction.getDateWithMessage("Enter date(" + dateFormat + ")");
            if (date == null) {
                return;
            }
            System.out.println(visitController.addVisit(serviceId, clientId, date));
        }));
        svcMenu.setMenuItems(itemsSVC);
    }

    private void buildRootMenu(final Menu roomMenu, final Menu clientMenu, final Menu svcMenu) {
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
        itemsMain.add(new MenuItem("Export reservations", () ->
                System.out.println(reservationController.exportReservations())));
        itemsMain.add(new MenuItem("Export visits", () ->
                System.out.println(visitController.exportVisits())));
        itemsMain.add(new MenuItem("Import services", () ->
                System.out.println(hotelServiceController.importServices())));
        itemsMain.add(new MenuItem("Import clients", () ->
                System.out.println(clientController.importClients())));
        itemsMain.add(new MenuItem("Import rooms", () ->
                System.out.println(roomController.importRooms())));
        itemsMain.add(new MenuItem("Import reservations", () ->
                System.out.println(reservationController.importReservations())));
        itemsMain.add(new MenuItem("Import visits", () ->
                System.out.println(visitController.importVisits())));
        rootMenu.setMenuItems(itemsMain);
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

    private void addResidentFromClientAction() {
        Integer id = UserInteraction.getNaturalIntWithMessage("Enter id of the client");
        if (id == null) {
            return;
        }
        Date arrival = UserInteraction.getDateWithMessage("Enter arrival date(" + dateFormat + ")");
        if (arrival == null) {
            return;
        }
        Date departure = UserInteraction.getDateWithMessage("Enter departure date(" + dateFormat + ")");
        if (departure == null) {
            return;
        }
        System.out.println(reservationController.addReservationForExistingClient(id, arrival, departure));
    }

    private void addClientAction() {
        String firstName = UserInteraction.getStringWithMessage("Enter first name");
        String lastName = UserInteraction.getStringWithMessage("Enter last name");
        System.out.println(clientController.addClient(firstName, lastName));
    }

    private void addHotelServiceAction() {
        BigDecimal price = UserInteraction.getPositiveBigDecimalWithMessage("Enter price");
        if (price == null) {
            return;
        }
        HotelServiceType type = UserInteraction.getServiceType();
        if (type == null) {
            return;
        }
        System.out.println(hotelServiceController.addService(price, type));
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

