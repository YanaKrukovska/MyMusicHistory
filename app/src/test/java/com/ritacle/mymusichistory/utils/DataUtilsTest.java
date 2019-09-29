package com.ritacle.mymusichistory.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static com.ritacle.mymusichistory.utils.DataUtils.*;

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
}