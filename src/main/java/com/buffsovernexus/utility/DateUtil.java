package com.buffsovernexus.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /**
     * Convert a standard Date object into something readable
     * @param date - The date to convert
     * @return - The converted date
     */
    public static String convertDateToPretty(Date date) {
        try {
            String format = "MM/dd/yy hh:mm a";
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Invalid Date";
        }
    }
}
