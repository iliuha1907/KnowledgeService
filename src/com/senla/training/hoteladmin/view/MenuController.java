package com.senla.training.hoteladmin.view;

import injection.annotation.NeedInjectionClass;
import injection.annotation.NeedInjectionField;
import com.senla.training.hoteladmin.util.UserInteraction;

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

