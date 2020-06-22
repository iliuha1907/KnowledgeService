package com.senla.training.hoteladmin.view;

import com.senla.training.hoteladmin.view.action.clients.*;
import com.senla.training.hoteladmin.view.action.freeRooms.*;
import com.senla.training.hoteladmin.view.action.rooms.*;
import com.senla.training.hoteladmin.view.action.svc.*;

public class Builder {
    private static Builder instance;
    private Menu rootMenu;

    private Builder(){}

    public static Builder getInstance(){
        if(instance == null){
            instance = new Builder();
        }
        return instance;
    }

    private void buildRoomsMenu(Menu roomMenu, Menu freeRoomMenu) {
        MenuItem[] itemsRoom = new MenuItem[10];
        IAction roomsPrice = new RoomsPriceAction();
        IAction roomsCapacity = new RoomsCapacityAction();
        IAction roomsStars = new RoomsStarsAction();
        IAction priceRoom = new PriceForRoomAction();
        IAction detailsRoom = new RoomDetailsAction();
        IAction addRoom = new AddRoomAction();
        IAction changePriceRoom = new ChangeRoomPriceAction();
        IAction changeStatusRoom = new ChangeRoomStatusAction();
        itemsRoom[0] = new MenuItem("Main menu", null, rootMenu);
        itemsRoom[1] = new MenuItem("Display rooms, sorted by price", roomsPrice, null);
        itemsRoom[2] = new MenuItem("Display rooms, sorted by capacity", roomsCapacity, null);
        itemsRoom[3] = new MenuItem("Display rooms, sorted by stars", roomsStars, null);
        itemsRoom[4] = new MenuItem("Display price of a room", priceRoom, null);
        itemsRoom[5] = new MenuItem("Display details of a room", detailsRoom, null);
        itemsRoom[6] = new MenuItem("Add room", addRoom, null);
        itemsRoom[7] = new MenuItem("Change price of a room", changePriceRoom, null);
        itemsRoom[8] = new MenuItem("Change status of a room", changeStatusRoom, null);
        itemsRoom[9] = new MenuItem("Free rooms menu", null, freeRoomMenu);
        roomMenu.setMenuItems(itemsRoom);
    }

    public void buildFreeRoomsMenu(Menu freeRoomMenu, Menu roomMenu) {
        MenuItem[] itemsFreeRoom = new MenuItem[7];
        IAction freeRoomsPrice = new FreeRoomsPriceAction();
        IAction freeRoomsCapacity = new FreeRoomsCapacityAction();
        IAction freeRoomsStars = new FreeRoomsStarsAction();
        IAction freeRoomsCount = new FreeRoomsCountAction();
        IAction freeRoomsDate = new FreeRoomsAfterDateAction();
        itemsFreeRoom[0] = new MenuItem("Main menu", null, rootMenu);
        itemsFreeRoom[1] = new MenuItem("Display rooms, sorted by price", freeRoomsPrice, null);
        itemsFreeRoom[2] = new MenuItem("Display rooms, sorted by capacity", freeRoomsCapacity, null);
        itemsFreeRoom[3] = new MenuItem("Display rooms, sorted by stars", freeRoomsStars, null);
        itemsFreeRoom[4] = new MenuItem("Display number of free rooms", freeRoomsCount, null);
        itemsFreeRoom[5] = new MenuItem("Display free rooms after date", freeRoomsDate, null);
        itemsFreeRoom[6] = new MenuItem("Rooms menu", null, roomMenu);
        freeRoomMenu.setMenuItems(itemsFreeRoom);
    }

    private void buildClientsMenu(Menu clientMenu) {
        MenuItem[] itemsClients = new MenuItem[7];
        IAction clientsDate = new ClientsDateAction();
        IAction clientsAlpha = new ClientsAlphabetAction();
        IAction clientsCount = new ClientsCountAction();
        IAction lastClients = new LastRoomResidentsAction();
        IAction addClient = new AddClientAction();
        IAction removeClient = new RemoveClientAction();
        itemsClients[0] = new MenuItem("Main menu", null, rootMenu);
        itemsClients[1] = new MenuItem("Display clients, sorted by departure date",
                clientsDate, null);
        itemsClients[2] = new MenuItem("Display clients, sorted by alphabet",
                clientsAlpha, null);
        itemsClients[3] = new MenuItem("Display last 3 residents of a room", lastClients, null);
        itemsClients[4] = new MenuItem("Display number of residents", clientsCount, null);
        itemsClients[5] = new MenuItem("Add resident", addClient, null);
        itemsClients[6] = new MenuItem("Remove resident", removeClient, null);
        clientMenu.setMenuItems(itemsClients);
    }

    private void buildServiceMenu(Menu svcMenu) {
        MenuItem[] itemsSVC = new MenuItem[6];
        IAction servicesDate = new ClientServicesDateAction();
        IAction servicesPrice = new ClientServicesPriceAction();
        IAction servicesAll = new ServicesAction();
        IAction addService = new AddServiceAction();
        IAction changeServicePrice = new ChangeServicePriceAction();
        itemsSVC[0] = new MenuItem("Main menu", null, rootMenu);
        itemsSVC[1] = new MenuItem("Display services of a client, sorted by date", servicesDate, null);
        itemsSVC[2] = new MenuItem("Display services of a client, sorted by price", servicesPrice, null);
        itemsSVC[3] = new MenuItem("Display services", servicesAll, null);
        itemsSVC[4] = new MenuItem("Change price of a service", changeServicePrice, null);
        itemsSVC[5] = new MenuItem("Add service", addService, null);
        svcMenu.setMenuItems(itemsSVC);
    }

    private void buildRootMenu(Menu roomMenu, Menu clientMenu, Menu svcMenu){
        IAction exportSvc = new ExportServicesAction();
        IAction exportCl = new ExportClientsAction();
        IAction exportRooms = new ExportRoomsAction();
        IAction importSvc = new ImportServicesAction();
        IAction importCl = new ImportClientsAction();
        IAction importRooms = new ImportRoomsAction();
        MenuItem[] itemsMain = new MenuItem[9];
        itemsMain[0] = new MenuItem("Room menu", null, roomMenu);
        itemsMain[1] = new MenuItem("Client menu", null, clientMenu);
        itemsMain[2] = new MenuItem("Services menu", null, svcMenu);
        itemsMain[3] = new MenuItem("Export services", exportSvc, null);
        itemsMain[4] = new MenuItem("Export clients", exportCl, null);
        itemsMain[5] = new MenuItem("Export rooms", exportRooms, null);
        itemsMain[6] = new MenuItem("Import services", importSvc, null);
        itemsMain[7] = new MenuItem("Import clients", importCl, null);
        itemsMain[8] = new MenuItem("Import rooms", importRooms, null);
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

        buildRootMenu(roomMenu,clientMenu,svcMenu);
        buildRoomsMenu(roomMenu, freeRoomMenu);
        buildFreeRoomsMenu(freeRoomMenu, roomMenu);
        buildClientsMenu(clientMenu);
        buildServiceMenu(svcMenu);
    }

    public Menu getRootMenu() {
        return rootMenu;
    }
}

