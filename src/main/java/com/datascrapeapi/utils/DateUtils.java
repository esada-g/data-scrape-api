package com.datascrapeapi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public final class DateUtils {

    private static final DateTimeFormatter MONTH_DAY_YEAR_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private DateUtils() {
    }

    public static String setDateToOneYearBefore(String startDateValue) {
        LocalDate date = LocalDate.parse(startDateValue, MONTH_DAY_YEAR_FORMATTER);
        LocalDate previousYear = date.minusYears(1);
        return previousYear.format(MONTH_DAY_YEAR_FORMATTER);
    }

    public static String dateStringToUnixTimestamp(String dateString) {
        LocalDate date = LocalDate.parse(dateString, MONTH_DAY_YEAR_FORMATTER);
        return String.valueOf(date.atStartOfDay(ZoneOffset.UTC).toEpochSecond());
    }

    public static Date stringToDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            return dateFormat.parse(dateStr);
        }
        catch (ParseException e) {
            return new Date();
        }
    }
}
