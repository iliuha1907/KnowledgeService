package com.senla.training.hoteladmin.util;

import java.util.Arrays;
import java.util.List;

public class ListOfPackagesProvider {

    public static List<String> getPackagesForInjection() {
        return Arrays.asList("com.senla.training.hoteladmin.service", "com.senla.training.hoteladmin.dao",
                "com.senla.training.hoteladmin.controller", "com.senla.training.hoteladmin.view",
                "com.senla.training.hoteladmin.csvapi", "com.senla.training.hoteladmin.util");
    }
}
