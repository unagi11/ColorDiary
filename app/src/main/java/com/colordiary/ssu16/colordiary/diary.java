package com.colordiary.ssu16.colordiary;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by unagi on 2017-11-29.
 */

public class diary {
    private String diary_date;//날짜
    private String diary_text;//일기 본문
    private String diary_image_name;//그림 이름

    diary (String date, String text, String image_name) {
        setDiary_date(date);
        setDiary_text(text);
        setDiary_image_name(image_name);
    }

    String getDiary_date() {
        return diary_date;
    }

    public void setDiary_date() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        diary_date = sdf.format(date);
    }

    public void setDiary_date(String date) {
        diary_date = date;
    }

    public String getDiary_text() {
        return diary_text;
    }

    public void setDiary_text(String text) {
        diary_text = text;
    }

    public String getDiary_image_name() {
        return diary_image_name;
    }

    public void setDiary_image_name(String image_name) {
        diary_image_name = image_name;
    }

}
