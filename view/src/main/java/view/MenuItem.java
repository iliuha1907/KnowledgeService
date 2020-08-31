package view;

public class MenuItem {

    private String title;
    private IAction action;
    private Menu nextMenu;

    public MenuItem(final String title, final Menu nextMenu) {
        this.title = title;
        this.nextMenu = nextMenu;
    }

    public MenuItem(final String title, final IAction action) {
        this.title = title;
        this.action = action;
    }

    public void doAction() {
        action.execute();
    }

    public Menu getNextMenu() {
        return nextMenu;
    }

    @Override
    public String toString() {
        return title;
    }
}

