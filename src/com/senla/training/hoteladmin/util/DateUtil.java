package com.senla.training.hoteladmin.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    private static final String timeZone = "GMT";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd");

    static {
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
    }

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

