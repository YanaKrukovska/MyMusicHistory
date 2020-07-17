package com.ritacle.mymusichistory.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DataUtils {


    public static String convertToTimeLabel(Date date) {
        Date currentDate = new Date();

        long listenDate = date.getTime();
        long currentDateMilliseconds = currentDate.getTime();
        long minutes = ((currentDateMilliseconds - listenDate) / 1000) / 60;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String timeYesterday = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH).format(calendar.getTime());
        String timeDateOld = new SimpleDateFormat("MMMM dd, hh:mm aa", Locale.ENGLISH).format(calendar.getTime());

        if (minutes < 1) {
            return "Just now";
        } else if (minutes <= 59) {
            return minutes + " mins ago";
        } else if (minutes / 60 == 1) {
            return minutes / 60 + " hour ago";
        } else if (minutes / 60 < 24) {
            return minutes / 60 + " hours ago";
        } else if (minutes / 60 < 48) {
            return "Yesterday " + timeYesterday;
        }

        return timeDateOld;
    }

    public static Date createFullDate(int year, int month, int day, int hours, int minutes, int seconds) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hours, minutes, seconds);
        return calendar.getTime();
    }

    public static Date createDate(int year, int month, int day) {
        TimeZone.setDefault(TimeZone.getTimeZone("CET"));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, 0, 0, 0);
        return calendar.getTime();
    }

    public static String convertToString(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.getTime());
    }

    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }

    public static Date addMonth(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    public static int daysBetween(Date date1, Date date2) {
        long diff = date2.getTime() - date1.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}
