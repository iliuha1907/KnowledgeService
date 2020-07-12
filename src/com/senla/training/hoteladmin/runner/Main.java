package com.senla.training.hoteladmin.runner;

import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.util.initializing.DiInitializer;
import com.senla.training.hoteladmin.util.initializing.ParamConfigurator;
import com.senla.training.hoteladmin.view.MenuController;

public class Main {

    public static void main(String[] args) {
        UserInteraction.startWorking();
        DiInitializer diInitializer = new DiInitializer();
        ParamConfigurator.init(diInitializer);
        diInitializer.init();
        MenuController menuController = diInitializer.getMenuController();
        menuController.run();
        UserInteraction.stopWorking();
    }
}

