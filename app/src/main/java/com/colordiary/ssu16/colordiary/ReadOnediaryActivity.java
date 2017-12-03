package com.colordiary.ssu16.colordiary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class ReadOnediaryActivity extends AppCompatActivity {

    TextView date_textView;
    TextView diary_textView;
    ImageView diary_imageView;
    diary diary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_diary);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        diary = (diary)intent.getSerializableExtra("DIARY");

        date_textView = (TextView) findViewById(R.id.date_TextView);
        diary_textView = (TextView) findViewById(R.id.diary_TextView);
        diary_imageView = (ImageView) findViewById(R.id.diary_ImageView);

        date_textView.setText(diary.getDiary_date());
        diary_textView.setText(diary.getDiary_text());
        File file = getApplicationContext().getFileStreamPath(diary.getDiary_image_name()); //불러오기
        if(file.exists()) {
            Bitmap diary_image = BitmapFactory.decodeFile(file.getPath());
            diary_imageView.setImageBitmap(diary_image);
        } else Log.d("hello", "이미지 없음");    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //or switch문을 이용하면 될듯 하다.
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        if (id == R.id.delete_button) {
            WritediaryActivity.postFirebaseDatabase(false, diary);
            finish();
            return true;
        }
        if (id == R.id.edit_button) {
            Toast.makeText(this, "내용수정 클릭", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}