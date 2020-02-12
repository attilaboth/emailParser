package com.tsystems.parser.email.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateTimeUtil {

    public static LocalDate convertFromDateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static boolean isLocalDateMonthOld(LocalDate localDate, int numOfMonth) {
        return localDate.isAfter(LocalDate.now().minusMonths(numOfMonth));
    }

    public static java.sql.Date convertToSqlDate(LocalDate localDate) {
        return java.sql.Date.valueOf(localDate);
    }
}
