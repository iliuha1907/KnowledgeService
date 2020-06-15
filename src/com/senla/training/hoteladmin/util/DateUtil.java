package com.senla.training.hoteladmin.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static Date getDate(String data) {
        DateFormat inputFormatter = new SimpleDateFormat("dd.MM.yyyy");
        try {
            return inputFormatter.parse(data);
        } catch (Exception ex) {
            throw new RuntimeException("Invalid data");
        }

    }

    public static String getStr(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return simpleDateFormat.format(date);
    }
}

