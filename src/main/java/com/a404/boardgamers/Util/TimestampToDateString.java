package com.a404.boardgamers.Util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampToDateString {
    public static String convertDate(Timestamp writtenTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
//        Date date = new Date();
//        Timestamp timestamp = new Timestamp(date.getTime());
        return sdf.format(writtenTime);
    }
}