package com.nhatdear.sademo.helpers;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by nhatdear on 4/25/17.
 */

public class DateUtils {
    /**
     * Convert string yyyy-MM-dd to date
     * @param  stringDate string of Date
     * @return Date
     */
    public static Date convertStringToDate(String stringDate) throws ParseException {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(stringDate);
    }

    /**
     * Get month from Date as Int
     * @param date input
     * @return int begin from 1 to 12
     */
    public static int getMonthFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * get year from date
     * @param date date
     * @return int
     */
    public static int getYearFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * get year from date
     * @param date date
     * @return int
     */
    public static int getYearFromDate(String date) throws ParseException {
        return getYearFromDate(convertStringToDate(date));
    }

    /**
     * Get month from Date as Int
     * @param date input
     * @return int
     */
    public static int getMonthFromDate(String date) throws ParseException {
        return getMonthFromDate(convertStringToDate(date));
    }

    /**
     * Get quarter from Date as Int
     * @param date input
     * @return int
     */
    public static int getQuarterFromDate(String date) throws ParseException {
        int month = getMonthFromDate(convertStringToDate(date));
        if (month >= 1 && month < 4) {
            return 1;
        } else if (month >= 4 && month < 7) {
            return 2;
        } else if (month >= 7 && month < 10) {
            return 3;
        } else {
            return 4;
        }
    }

    /**
     * get days in year
     * @param dateString date as String
     * @return int
     * @throws ParseException
     */
    public static int getDaysInYear(String dateString) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(convertStringToDate(dateString));
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        long thisYear = cal.getTimeInMillis();

        cal.add(Calendar.YEAR, 1);
        long nextYear = cal.getTimeInMillis();
        long diff = nextYear - thisYear;
        return (int) (diff / (24 * 60 * 60 * 1000));
    }

    public static int getWeeksInYear(String dateString) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(convertStringToDate(dateString));
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 31);

        int ordinalDay = cal.get(Calendar.DAY_OF_YEAR);
        int weekDay = cal.get(Calendar.DAY_OF_WEEK) - 1; // Sunday = 0
        return (ordinalDay - weekDay + 10) / 7;
    }
    /**
     * get day number in year
     * @param dateString date as string
     * @return int
     * @throws ParseException
     */
    public static int getDayInYear(String dateString) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(convertStringToDate(dateString));
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * get week number in year
     * @param dateString date as string
     * @return int
     * @throws ParseException
     */
    public static int getWeekInYear(String dateString) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(convertStringToDate(dateString));
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
}
