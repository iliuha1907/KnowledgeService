package com.senla.training.hoteladmin.view;

import com.senla.training.hoteladmin.util.UserInteraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MenuController {

    @Autowired
    private Builder builder;
    @Autowired
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

