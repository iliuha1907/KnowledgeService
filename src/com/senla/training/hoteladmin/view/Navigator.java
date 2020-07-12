package com.senla.training.hoteladmin.view;

import com.senla.training.hoteladmin.annotation.NeedDiClass;

import java.util.List;

@NeedDiClass
public class Navigator {
    private Menu currentMenu;

    public Navigator() {
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
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

