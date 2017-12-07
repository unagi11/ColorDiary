package com.colordiary.ssu16.colordiary;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by unagi on 2017-11-29.
 */

public class diary implements Serializable{

    private static final long serialVersionUID = 1000000L;

    private String diary_date;//날짜 YYYY-MM-DD
    private String diary_text;//일기 본문
    private String diary_image_name;//그림 이름
    private String diary_uid;
    private boolean diary_public;

    public diary() {}

    public diary (String text, String image_name, String uid) {
        setDiary_date();
        setDiary_text(text);
        setDiary_image_name(image_name);
        diary_uid = uid;
        diary_public = false;
    }

    public diary (String date, String text, String image_name, String uid) {
        setDiary_date(date);
        setDiary_text(text);
        setDiary_image_name(image_name);
        diary_uid = uid;
        diary_public = false;
    }

    String getDiary_date() {
        return diary_date;
    }

    public static String getCurrentDiaryDate() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String getCurrentDiaryTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        return sdf.format(date);
    }

    public String getDiary_time() { return getDiary_image_name().substring(0, 19); }

    public void setDiary_date() {
        diary_date = getCurrentDiaryDate();
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

    public void setDiary_public(boolean bool) {
        diary_public = bool;
    }

    public boolean isDiary_public() {
        return diary_public;
    }

    public String getDiary_uid() {
        return diary_uid;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("diary_date", diary_date);
        result.put("diary_text", diary_text);
        result.put("diary_image_name", diary_image_name);
        result.put("diary_uid", diary_uid);
        result.put("diary_public", diary_public);
        return result;
    }



}
