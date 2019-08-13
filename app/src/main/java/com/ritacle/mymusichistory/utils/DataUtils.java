package com.ritacle.mymusichistory.utils;

import java.util.Calendar;
import java.util.Date;

public class DataUtils {


    public static String convertToTimeLabel(Date date) {
        Date currentDate = new Date();

        long listenDate = date.getTime() - 10800000;
        long currentDateMilliseconds = currentDate.getTime();
        long minutes = ((currentDateMilliseconds - listenDate) / 1000) / 60;



        String timePassed = "";
        if (minutes < 1) {
            return "Just now";
        } else if (minutes <= 59) {
            return minutes + " mins";
        } else if ( minutes / 60 ==1){
            return minutes / 60 + " hour";
        } else if (minutes / 60 < 24) {
            return minutes / 60 + " hours";
        }

        return timePassed;
    }


}
