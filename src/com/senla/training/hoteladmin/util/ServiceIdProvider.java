package com.senla.training.hoteladmin.util;

public class ServiceIdProvider {
    private static Integer nextId = 0;

    public static Integer getNextId() {
        return ++nextId;
    }
}

