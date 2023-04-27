package com.kalmac.filibrary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {
    public static String FormatDate(String date){

        Date newDate = null;
        try {
            newDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return new SimpleDateFormat("MMM dd, yyyy").format(newDate);
    }
}
