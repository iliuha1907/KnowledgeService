package com.senla.training.hoteladmin.runner;

import com.senla.training.hoteladmin.annotation.TestService;
import com.senla.training.hoteladmin.util.initializing.ParamConfigurator;

public class TestRunner {

    public static void main(String[] args) {
        TestService testService = new TestService();
        ParamConfigurator.init(testService);
        testService.checkClientsRepository();
        testService.checkHotelServiceRepository();
        testService.checkRoomsRepository();
        testService.checkId();
        testService.checkName();
    }
}

