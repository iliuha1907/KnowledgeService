package com.senla.training.hoteladmin.util.id;

public class ClientIdProvider {
    private static Integer nextId = 0;

    public static Integer getNextId() {
        return ++nextId;
    }

    public static void setCurrentId(Integer id) {
        nextId = id;
    }

    public static Integer getCurrentId() {
        return nextId;
    }
}

