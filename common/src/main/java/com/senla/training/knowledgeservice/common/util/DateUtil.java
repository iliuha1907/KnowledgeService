package com.senla.training.knowledgeservice.common.util;

import javax.annotation.Nonnull;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    @Nonnull
    public static Date addPeriodToDate(@Nonnull Date source,
                                       @Nonnull DateAddingValueType type,
                                       @Nonnull Integer value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(source);
        if (DateAddingValueType.DAYS.equals(type)) {
            calendar.add(Calendar.DATE, value);
        } else if (DateAddingValueType.MONTHS.equals(type)) {
            calendar.add(Calendar.MONTH, value);
        } else if (DateAddingValueType.WEEKS.equals(type)) {
            calendar.add(Calendar.WEEK_OF_YEAR, value);
        }
        return calendar.getTime();
    }
}
