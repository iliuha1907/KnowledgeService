package com.senla.training.hoteladmin.util;

public class ClientIdProvider {
    private static Integer nextId = 0;

    public static Integer getNextId() {
        return ++nextId;
    }

    public static void reduceId(){
        nextId--;
    }
}

