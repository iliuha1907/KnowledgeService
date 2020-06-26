package com.senla.training.hoteladmin.view.action.svc;

import com.senla.training.hoteladmin.controller.ClientController;
import com.senla.training.hoteladmin.controller.SvcController;
import com.senla.training.hoteladmin.model.svc.ServiceType;
import com.senla.training.hoteladmin.repo.ClientsArchiveRepoImpl;
import com.senla.training.hoteladmin.repo.ClientsRepoImpl;
import com.senla.training.hoteladmin.repo.RoomsRepoImpl;
import com.senla.training.hoteladmin.repo.SvcRepoImpl;
import com.senla.training.hoteladmin.service.*;
import com.senla.training.hoteladmin.util.DateUtil;
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.view.IAction;

import java.math.BigDecimal;
import java.util.Date;

public class AddServiceAction implements IAction {
    private SvcController svcController = SvcController.getInstance(
            SvcServiceImpl.getInstance(SvcRepoImpl.getInstance(), ServiceWriterImpl.getInstance()),
            ClientServiceImpl.getInstance(ArchivServiceImpl.getInstance(ClientsArchiveRepoImpl.getInstance()),
                            SvcServiceImpl.getInstance(SvcRepoImpl.getInstance(), ServiceWriterImpl.getInstance()),
                            ClientsRepoImpl.getInstance(), RoomsRepoImpl.getInstance(), ClientWriterImpl.getInstance()));

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

        System.out.println("Enter price");
        BigDecimal price;
        try {
            price = userInteraction.getBigDecimal();
            if (price.compareTo(BigDecimal.ZERO) == -1) {
                throw new Exception();
            }
        } catch (Exception ex) {
            System.out.println("Wrong price");
            return;
        }

        ServiceType type;
        try {
            type = userInteraction.getServiceType();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return;
        }

        System.out.println("Enter date");
        Date date;
        try {
            date = DateUtil.getDate(userInteraction.getString());
        } catch (Exception ex) {
            System.out.println("Wrong date");
            return;
        }
        System.out.println(svcController.addService(price, type, id, date));
    }
}

