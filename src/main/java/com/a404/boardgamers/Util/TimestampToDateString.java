package com.a404.boardgamers.Util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimestampToDateString {
    public static String convertDate(Timestamp writtenTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd HH:mm");
//        Date date = new Date();
//        Timestamp timestamp = new Timestamp(date.getTime());
        return sdf.format(writtenTime);
    }
}
