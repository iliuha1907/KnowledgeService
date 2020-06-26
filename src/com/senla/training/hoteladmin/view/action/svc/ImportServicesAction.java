package com.senla.training.hoteladmin.view.action.svc;

import com.senla.training.hoteladmin.controller.SvcController;
import com.senla.training.hoteladmin.repo.ClientsArchiveRepoImpl;
import com.senla.training.hoteladmin.repo.ClientsRepoImpl;
import com.senla.training.hoteladmin.repo.RoomsRepoImpl;
import com.senla.training.hoteladmin.repo.SvcRepoImpl;
import com.senla.training.hoteladmin.service.*;
import com.senla.training.hoteladmin.view.IAction;

public class ImportServicesAction implements IAction {
    private SvcController svcController = SvcController.getInstance(
            SvcServiceImpl.getInstance(SvcRepoImpl.getInstance(), ServiceWriterImpl.getInstance()),
            ClientServiceImpl.getInstance(ArchivServiceImpl.getInstance(ClientsArchiveRepoImpl.getInstance()),
                    SvcServiceImpl.getInstance(SvcRepoImpl.getInstance(), ServiceWriterImpl.getInstance()),
                    ClientsRepoImpl.getInstance(), RoomsRepoImpl.getInstance(), ClientWriterImpl.getInstance()));

    @Override
    public void execute() {
        System.out.println(svcController.importServices());
    }
}

