package com.senla.training.hoteladmin.view;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Navigator {

    private Menu currentMenu;

    public Navigator() {
    }

    public void setCurrentMenu(final Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public void printMenu() {
        System.out.println(currentMenu);
    }

    public void navigate(final Integer index) {
        List<MenuItem> menuItems = currentMenu.getMenuItems();
        int size = menuItems.size();

        if (index < 0
                || index > size - 1) {
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

