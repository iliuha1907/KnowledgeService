package com.senla.training.hoteladmin.view;

public class Menu {
    private String name;
    private MenuItem[] menuItems;

    public void setName(String name) {
        this.name = name;
    }

    public void setMenuItems(MenuItem[] menuItems) {
        this.menuItems = menuItems;
    }

    public String getName() {
        return name;
    }

    public MenuItem[] getMenuItems() {
        return menuItems;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(name + "\n");
        for (int i = 0; i < menuItems.length; i++) {
            result.append((i + 1) + ". " + menuItems[i].toString() + "\n");
        }
        result.append((menuItems.length + 1) + ". Exit");
        return result.toString();
    }
}

