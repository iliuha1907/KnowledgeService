package com.senla.training.hoteladmin.view.action.svc;

import com.senla.training.hoteladmin.controller.SvcController;
import com.senla.training.hoteladmin.model.svc.ServiceType;
import com.senla.training.hoteladmin.repo.ClientsArchiveRepoImpl;
import com.senla.training.hoteladmin.repo.ClientsRepoImpl;
import com.senla.training.hoteladmin.repo.RoomsRepoImpl;
import com.senla.training.hoteladmin.repo.SvcRepoImpl;
import com.senla.training.hoteladmin.service.*;
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.view.IAction;

import java.math.BigDecimal;

public class ChangeServicePriceAction implements IAction {
    private SvcController svcController = SvcController.getInstance(
            SvcServiceImpl.getInstance(SvcRepoImpl.getInstance(), ServiceWriterImpl.getInstance()),
            ClientServiceImpl.getInstance(ArchivServiceImpl.getInstance(ClientsArchiveRepoImpl.getInstance()),
                    ClientsRepoImpl.getInstance(), RoomsRepoImpl.getInstance(), ClientWriterImpl.getInstance()));

    @Override
    public void execute() {
        UserInteraction userInteraction = UserInteraction.getInstance();

        ServiceType type;
        try {
            type = userInteraction.getServiceType();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return;
        }

        System.out.println("Enter price");
        BigDecimal price;
        try {
            price = userInteraction.getBigDecimal();
            if (price.compareTo(BigDecimal.ZERO) == -1) {
                throw new Exception();
            }
        } catch (Exception ex) {
            System.out.println("Wrong passport number");
            return;
        }

        System.out.println(svcController.setServicePrice(type, price));
    }
}

