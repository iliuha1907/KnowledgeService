package com.senla.training.hoteladmin.view.action.clients;

import com.senla.training.hoteladmin.controller.ClientController;
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.view.IAction;

public class RemoveClientAction implements IAction {
    private ClientController clientController = ClientController.getInstance();

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();

        Integer id = userInteraction.getNaturalIntWithMessage("Enter id of the client");
        if (id == null) {
            return;
        }
        System.out.println(clientController.removeResident(id));
    }
}

