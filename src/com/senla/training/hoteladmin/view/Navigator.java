package com.senla.training.hoteladmin.view;

public class Navigator {
    private Menu currentMenu;

    public Navigator(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public void printMenu() {
        System.out.println(currentMenu);
    }

    public boolean navigate(Integer index) {
        if (index == currentMenu.getMenuItems().length) {
            return false;
        }

        if (index < 0 ||
                index > currentMenu.getMenuItems().length - 1) {
            return true;
        }

        if (currentMenu.getMenuItems()[index].getNextMenu() == null) {
            currentMenu.getMenuItems()[index].doAction();
        } else {
            currentMenu = currentMenu.getMenuItems()[index].getNextMenu();
        }
        return true;
    }
}

