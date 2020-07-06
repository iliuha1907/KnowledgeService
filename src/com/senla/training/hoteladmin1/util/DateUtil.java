package com.senla.training.hoteladmin.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public static Date getDate(String data) {
        try {
            return simpleDateFormat.parse(data);
        } catch (Exception ex) {
            return null;
        }

    }

    public static String getString(Date date) {
        return simpleDateFormat.format(date);
    }
}

