package com.ritacle.mymusichistory.utils;

import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.ritacle.mymusichistory.utils.DataUtils.addDays;
import static com.ritacle.mymusichistory.utils.DataUtils.addMonth;
import static com.ritacle.mymusichistory.utils.DataUtils.convertToString;
import static com.ritacle.mymusichistory.utils.DataUtils.convertToTimeLabel;
import static com.ritacle.mymusichistory.utils.DataUtils.createDate;

public class DataUtilsTest {

    @Test
    public void convertToTimeLabelJustNow() {
        Assert.assertEquals("Just now", convertToTimeLabel(new Date()));
    }

    @Test
    public void convertToTimeLabelYesterday() {

    }

    @Test
    public void convertToTimeLabelOld() {

    }

    @Test
    public void get7DaysFromNow() {
        Assert.assertEquals("2019-08-19", convertToString(addDays(createDate(2019, 8, 26), -7)));
    }

    @Test
    public void getOneMonthFromNow() {
        Assert.assertEquals("2019-07-26", convertToString(addMonth(createDate(2019, 8, 26), -1)));
    }

    @Test
    public void compareApi26And23UTCTime() {
        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        utc.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Date date26 = DataUtils.createFullDate(utc.getYear(), utc.getMonthValue(), utc.getDayOfMonth(), utc.getHour(), utc.getMinute(), utc.getSecond());

        DateFormat dateFormat = DateFormat.getDateTimeInstance(1, 1, Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("utc"));
        Date date23 = dateFormat.getCalendar().getTime();
        Assert.assertEquals(date26.getHours(), date23.getHours());
    }
}