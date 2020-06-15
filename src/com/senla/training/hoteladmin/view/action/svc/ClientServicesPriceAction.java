package com.senla.training.hoteladmin.view.action.svc;

import com.senla.training.hoteladmin.controller.SVCController;
import com.senla.training.hoteladmin.model.svc.ServiceSortCriterion;
import com.senla.training.hoteladmin.repo.ClientsArchiveRepoImpl;
import com.senla.training.hoteladmin.repo.ClientsRepoImpl;
import com.senla.training.hoteladmin.repo.RoomsRepoImpl;
import com.senla.training.hoteladmin.repo.SVCRepoImpl;
import com.senla.training.hoteladmin.service.ArchivServiceImpl;
import com.senla.training.hoteladmin.service.ClientServiceImpl;
import com.senla.training.hoteladmin.service.SVCServiceImpl;
import com.senla.training.hoteladmin.view.IAction;
import com.senla.training.hoteladmin.util.UserInteraction;

public class ClientServicesPriceAction implements IAction {
    private SVCController svcController = new SVCController(SVCServiceImpl.getInstance(SVCRepoImpl.getInstance()),
            ClientServiceImpl.getInstance(ArchivServiceImpl.getInstance(ClientsArchiveRepoImpl.getInstance()),
                    ClientsRepoImpl.getInstance(), RoomsRepoImpl.getInstance()));

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();
        System.out.println("Enter passport number of the client");
        Integer passNumber;
        try {
            passNumber = userInteraction.getInt();
            if (passNumber < 0) {
                throw new Exception();
            }
        } catch (Exception ex) {
            System.out.println("Wrong passport number");
            return;
        }
        System.out.println(svcController.getSortedClientServices(passNumber,
                ServiceSortCriterion.PRICE));
    }
}

