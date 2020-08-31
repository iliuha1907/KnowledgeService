package view;

import annotation.NeedInjectionClass;
import annotation.NeedInjectionField;
import util.UserInteraction;

@NeedInjectionClass
public class MenuController {

    @NeedInjectionField
    private Builder builder;
    @NeedInjectionField
    private Navigator navigator;

    public MenuController() {
    }

    public void run() {
        builder.buildMenu();
        navigator.setCurrentMenu(builder.getRootMenu());
        boolean stop = false;
        while (!stop) {
            navigator.printMenu();
            Integer choice = UserInteraction.getInt();
            if (choice == null) {
                System.out.println("Wrong input");
                continue;
            }
            choice -= 1;
            if (choice.equals(navigator.getCurrentMenu().getMenuItems().size())) {
                stop = true;
            } else {
                navigator.navigate(choice);
            }
        }
    }
}

