package com.example.logowanie.services;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Barti on 23.08.13.
 */
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
