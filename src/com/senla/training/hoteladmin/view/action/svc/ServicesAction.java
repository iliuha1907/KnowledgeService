package com.senla.training.hoteladmin.view.action.svc;

import com.senla.training.hoteladmin.controller.SVCController;
import com.senla.training.hoteladmin.repo.ClientsArchiveRepoImpl;
import com.senla.training.hoteladmin.repo.ClientsRepoImpl;
import com.senla.training.hoteladmin.repo.RoomsRepoImpl;
import com.senla.training.hoteladmin.repo.SVCRepoImpl;
import com.senla.training.hoteladmin.service.ArchivServiceImpl;
import com.senla.training.hoteladmin.service.ClientServiceImpl;
import com.senla.training.hoteladmin.service.SVCServiceImpl;
import com.senla.training.hoteladmin.view.IAction;

public class ServicesAction implements IAction {
    private SVCController svcController = new SVCController(SVCServiceImpl.getInstance(SVCRepoImpl.getInstance()),
            ClientServiceImpl.getInstance(ArchivServiceImpl.getInstance(ClientsArchiveRepoImpl.getInstance()),
                    ClientsRepoImpl.getInstance(), RoomsRepoImpl.getInstance()));

    @Override
    public void execute() {
        System.out.println(svcController.getServices());
    }
}

