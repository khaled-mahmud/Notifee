package com.pgdit101.iit.notifee;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Function {

    public static String Epoch2DateString(String epochSeconds, String formatString)
    {

        Date updateDate = new Date(Long.parseLong(epochSeconds));
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        return format.format(updateDate);

    }

    public static Calendar Epoch2Calendar(String epochSeconds){

        Date updateDate = new Date(Long.parseLong(epochSeconds));
        Calendar cal = Calendar.getInstance();
        cal.setTime(updateDate);
        return cal;
    }
}
