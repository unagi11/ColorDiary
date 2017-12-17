package com.colordiary.ssu16.colordiary;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class todayfeelActivity extends AppCompatActivity implements View.OnClickListener{

    int information1 = 0;
    int information2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todayfeel);

        ImageButton cloud = (ImageButton) findViewById(R.id.cloud);
        ImageButton fog = (ImageButton) findViewById(R.id.fog);
        ImageButton hail = (ImageButton) findViewById(R.id.hail);
        ImageButton rain = (ImageButton) findViewById(R.id.rain);
        ImageButton snow = (ImageButton) findViewById(R.id.snow);
        ImageButton sun = (ImageButton) findViewById(R.id.sun);
        ImageButton thunder = (ImageButton) findViewById(R.id.thunder);
        ImageButton windy = (ImageButton) findViewById(R.id.windy);

        ImageButton unhappy = (ImageButton) findViewById(R.id.unhappy);
        ImageButton sad = (ImageButton) findViewById(R.id.sad);
        ImageButton happy = (ImageButton) findViewById(R.id.happy);
        ImageButton angry = (ImageButton) findViewById(R.id.angry);
        ImageButton veryhappy = (ImageButton) findViewById(R.id.veryhappy);
        ImageButton surprise = (ImageButton) findViewById(R.id.surprise);
        ImageButton normal = (ImageButton) findViewById(R.id.normal);
        ImageButton cool = (ImageButton) findViewById(R.id.cool);

        cloud.setOnClickListener(this);
        fog.setOnClickListener(this);
        hail.setOnClickListener(this);
        rain.setOnClickListener(this);
        snow.setOnClickListener(this);
        sun.setOnClickListener(this);
        thunder.setOnClickListener(this);
        windy.setOnClickListener(this);

        unhappy.setOnClickListener(this);
        sad.setOnClickListener(this);
        happy.setOnClickListener(this);
        angry.setOnClickListener(this);
        veryhappy.setOnClickListener(this);
        surprise.setOnClickListener(this);
        normal.setOnClickListener(this);
        cool.setOnClickListener(this);
    }
    //아래는 각각의 버튼을 누르면 해당값이 더해지고, 날씨와 기분 총 2번 선택이 이루어졌는지 확인 후 종료
    @Override
    public void onClick(View v) {
        Log.d("info_check", v.getId() + "");
        if (information1 == 0 || information2 == 0) {
            if (v.getId() == R.id.cloud || v.getId() == R.id.fog || v.getId() == R.id.hail || v.getId() == R.id.rain || v.getId() == R.id.snow || v.getId() == R.id.sun || v.getId() == R.id.thunder || v.getId() == R.id.windy) {
                if (information1 == v.getId()) {
                    information1 = 0;
                    v.setBackgroundColor(Color.WHITE);
                } else if (information1 == 0){
                    information1 = v.getId();
                    v.setBackgroundColor(Color.GRAY);
                }
            }
            else {
                if (information2 == v.getId()) {
                    information2 = 0;
                    v.setBackgroundColor(fromIDtoColor(information2));
                } else if (information2 == 0){
                    information2 = v.getId();
                    v.setBackgroundColor(fromIDtoColor(information2));
                }
            }
        }
        if (information1 != 0 && information2 != 0) {
            saveEmotionInfo(getApplicationContext());
            int color = loadEmotionInfo(getApplicationContext(), diary.getCurrentDiaryDate());
            finish();
        }
    }

    public void saveEmotionInfo(Context context)//받아온 날씨와 기분을 저장하는 함수
    {
        SharedPreferences sharedPref = context.getSharedPreferences("hello123" , context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(diary.getCurrentDiaryDate(), information2);
        editor.commit();
    }

    public static int loadEmotionInfo(Context context, String day) {
        SharedPreferences sharedPref = context.getSharedPreferences("hello123" , context.MODE_PRIVATE);
        int Emotion = sharedPref.getInt(day, 0);
        Log.d("info_load", Emotion +"");
        return fromIDtoColor(Emotion);
    }

    public static int fromIDtoColor (int Emotion) {
        if (Emotion == R.id.unhappy) {
            return Color.YELLOW;
        } else if (Emotion == R.id.sad) {
            return Color.BLUE;
        } else if (Emotion == R.id.happy) {
            return Color.argb(255,251,153,253);
        } else if (Emotion == R.id.angry) {
            return Color.RED;
        } else if (Emotion == R.id.veryhappy) {
            return Color.GREEN;
        } else if (Emotion == R.id.surprise) {
            return Color.CYAN;
        } else if (Emotion == R.id.normal) {
            return Color.GRAY;
        } else if (Emotion == R.id.cool) {
            return Color.MAGENTA;
        } else {
            return Color.WHITE;
        }
    }

}
