package com.senla.training.hoteladmin.util.id;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ClientIdProvider implements Externalizable {
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

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(nextId);

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        nextId = (Integer) in.readObject();
    }
}

