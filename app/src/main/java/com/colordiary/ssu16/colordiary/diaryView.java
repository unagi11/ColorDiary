package com.colordiary.ssu16.colordiary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * Created by unagi on 2017-11-29.
 */

public class diaryView extends LinearLayoutCompat {

    TextView date_textView;
    TextView diary_textView;
    ImageView diary_imageView;

    public diaryView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.a_diary, this, true);

        date_textView = (TextView) findViewById(R.id.date_TextView);
        diary_textView = (TextView) findViewById(R.id.diary_TextView);
        diary_imageView = (ImageView) findViewById(R.id.diary_ImageView);
    }

    void setDate_textView(String diary_date) { date_textView.setText(diary_date); }

    void setDiary_textView(String diary_text) { diary_textView.setText(diary_text); }

    void setDiary_imageView(String diary_image_name, Context context) {
        File file = context.getFileStreamPath(diary_image_name); //불러오기
        if(file.exists()) {
            Bitmap diary_image = BitmapFactory.decodeFile(file.getPath());
            diary_imageView.setImageBitmap(diary_image);
        } else Log.d("hello", "이미지 없음");
    }
}
