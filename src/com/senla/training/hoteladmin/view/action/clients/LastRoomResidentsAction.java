package com.senla.training.hoteladmin.view.action.clients;

import com.senla.training.hoteladmin.controller.ClientController;
import com.senla.training.hoteladmin.view.IAction;
import com.senla.training.hoteladmin.util.UserInteraction;

public class LastRoomResidentsAction implements IAction {
    private ClientController clientController = ClientController.getInstance();

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();

        Integer roomId = userInteraction.getNaturalIntWithMessage("Enter id of the room");
        if (roomId == null) {
            return;
        }
        System.out.println(clientController.getLastResidents(roomId));
    }
}

