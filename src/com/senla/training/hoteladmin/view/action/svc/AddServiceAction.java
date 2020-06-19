package com.senla.training.hoteladmin.view.action.svc;

import com.senla.training.hoteladmin.controller.SVCController;
import com.senla.training.hoteladmin.model.svc.ServiceType;
import com.senla.training.hoteladmin.repo.ClientsArchiveRepoImpl;
import com.senla.training.hoteladmin.repo.ClientsRepoImpl;
import com.senla.training.hoteladmin.repo.RoomsRepoImpl;
import com.senla.training.hoteladmin.repo.SVCRepoImpl;
import com.senla.training.hoteladmin.service.ArchivServiceImpl;
import com.senla.training.hoteladmin.service.ClientServiceImpl;
import com.senla.training.hoteladmin.service.SVCServiceImpl;
import com.senla.training.hoteladmin.util.DateUtil;
import com.senla.training.hoteladmin.util.UserInteraction;
import com.senla.training.hoteladmin.view.IAction;

import java.math.BigDecimal;
import java.util.Date;

public class AddServiceAction implements IAction {
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
        System.out.println(svcController.addService(price, type, passNumber, date));
    }
}

