package com.example.pdedio.sendsnap.helpers;

import org.androidannotations.annotations.EBean;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by pawel on 12.03.2017.
 */
@EBean
public class DateHelper {

    public boolean isDateGreaterThanNow(Date date) {
        return this.getCurrentDate().before(date);
    }

    public Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        return calendar.getTime();
    }
}
