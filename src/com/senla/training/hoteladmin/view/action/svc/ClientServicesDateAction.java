package com.senla.training.hoteladmin.view.action.svc;

import com.senla.training.hoteladmin.controller.SvcController;
import com.senla.training.hoteladmin.util.sort.ServiceSortCriterion;
import com.senla.training.hoteladmin.repo.ClientsArchiveRepoImpl;
import com.senla.training.hoteladmin.repo.ClientsRepoImpl;
import com.senla.training.hoteladmin.repo.RoomsRepoImpl;
import com.senla.training.hoteladmin.repo.SvcRepoImpl;
import com.senla.training.hoteladmin.service.ArchivServiceImpl;
import com.senla.training.hoteladmin.service.ClientServiceImpl;
import com.senla.training.hoteladmin.service.SvcServiceImpl;
import com.senla.training.hoteladmin.view.IAction;
import com.senla.training.hoteladmin.util.UserInteraction;

public class ClientServicesDateAction implements IAction {
    private SvcController svcController = SvcController.getInstance(SvcServiceImpl.getInstance(SvcRepoImpl.getInstance()),
            ClientServiceImpl.getInstance(ArchivServiceImpl.getInstance(ClientsArchiveRepoImpl.getInstance()),
                    ClientsRepoImpl.getInstance(), RoomsRepoImpl.getInstance()));

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();
        System.out.println("Enter id of the client");
        Integer id;
        try {
            id = userInteraction.getInt();
            if (id < 0) {
                throw new Exception();
            }
        } catch (Exception ex) {
            System.out.println("Wrong id");
            return;
        }
        System.out.println(svcController.getSortedClientServices(id,
                ServiceSortCriterion.DATE));


    }
}

