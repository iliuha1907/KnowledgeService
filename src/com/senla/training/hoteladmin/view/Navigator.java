package com.senla.training.hotelAdmin.view;

public class Navigator {
    private static Navigator instance;
    private Menu currentMenu;

    private Navigator(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public static Navigator getInstance(Menu currentMenu) {
        if (instance == null) {
            instance = new Navigator(currentMenu);
        }
        return instance;
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public void printMenu() {
        System.out.println(currentMenu);
    }

    public void navigate(Integer index) {
        Integer size = currentMenu.getMenuItems().size();

        if (index < 0 ||
                index > size - 1) {
            return;
        }

        if (currentMenu.getMenuItems().get(index).getNextMenu() == null) {
            currentMenu.getMenuItems().get(index).doAction();
        } else {
            currentMenu = currentMenu.getMenuItems().get(index).getNextMenu();
        }
    }
}

