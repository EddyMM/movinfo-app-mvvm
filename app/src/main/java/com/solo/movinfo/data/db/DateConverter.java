package com.solo.movinfo.data.db;


import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class DateConverter {

    @TypeConverter
    public static Date toDate(Long date) {
        return new Date(date);
    }

    @TypeConverter
    public static long toTimestamp(Date date) {
        return date.getTime();
    }

}
