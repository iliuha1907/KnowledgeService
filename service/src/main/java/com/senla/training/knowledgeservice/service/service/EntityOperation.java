package com.senla.training.knowledgeservice.service.service;

public enum EntityOperation {

    ADDING,
    UPDATING;

    public String toString() {
        return name().toLowerCase();
    }
}
