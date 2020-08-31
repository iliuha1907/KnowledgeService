package runner;


import dao.DaoManager;
import injection.DependencyInjector;
import util.UserInteraction;
import view.MenuController;

import java.util.Arrays;

public class Runner {

    private static DaoManager daoManager;

    public static void main(String[] args) {
        UserInteraction.startWorking();
        try {
            DependencyInjector.init(Arrays.asList("service", "dao", "controller", "view", "filecsv"));
            daoManager = DependencyInjector.getClassInstance(DaoManager.class);
            MenuController menuController = DependencyInjector.getClassInstance(MenuController.class);
            menuController.run();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("Shutting app down");
        } finally {
            UserInteraction.stopWorking();
            if (daoManager != null) {
                try {
                    daoManager.closeConnection();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    System.out.println("Shutting app down");
                }
            }
        }
    }
}

