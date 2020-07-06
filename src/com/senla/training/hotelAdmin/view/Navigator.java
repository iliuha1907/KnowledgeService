package com.senla.training.hoteladmin.view;

import java.util.List;

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
        List<MenuItem> menuItems = currentMenu.getMenuItems();
        int size = menuItems.size();

        if (index < 0 ||
                index > size - 1) {
            return;
        }

        MenuItem menuItem = menuItems.get(index);
        if (menuItem.getNextMenu() == null) {
            menuItem.doAction();
        } else {
            currentMenu = menuItem.getNextMenu();
        }
    }
}

