package view;

import java.util.List;

public class Menu {

    private String name;
    private List<MenuItem> menuItems;

    public void setName(final String name) {
        this.name = name;
    }

    public void setMenuItems(final List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(name).append("\n");
        for (int i = 0; i < menuItems.size(); i++) {
            result.append((i + 1)).append(".").append(menuItems.get(i).toString()).append("\n");
        }
        result.append((menuItems.size() + 1)).append(". Exit");
        return result.toString();
    }
}

