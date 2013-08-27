package com.navigps.services;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateProvider {
    private static DateProvider ourInstance = new DateProvider();

    public static DateProvider getInstance() {
        return ourInstance;
    }

    private DateProvider() {
    }
    public String getDate() {
        SimpleDateFormat simpleDateHere = new SimpleDateFormat(
                "yyyy-MM-dd kk:mm:ss");
        String date = simpleDateHere.format(new Date());

        return date;

    }
}
