package com.senla.training.hoteladmin.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    private static final Logger LOGGER = LogManager.getLogger(DateUtil.class);
    private static final String TIME_ZONE = "GMT";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd");

    static {
        TimeZone.setDefault(TimeZone.getTimeZone(TIME_ZONE));
    }

    public static Date getDate(final String data) {
        try {
            return simpleDateFormat.parse(data);
        } catch (Exception ex) {
            LOGGER.error("Error at getting date: Wrong input");
            return null;
        }
    }

    public static String getString(final Date date) {
        return simpleDateFormat.format(date);
    }
}

