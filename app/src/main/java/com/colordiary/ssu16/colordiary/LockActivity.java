package com.colordiary.ssu16.colordiary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class LockActivity extends AppCompatActivity {

    EditText editText;
    boolean Locked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_lock);
         editText = (EditText)findViewById(R.id.password_input);
         TextWatcher watcher = new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
                 int cnt = s.length();
                 if (cnt >= 4) {
                     if (loadPassword(getApplicationContext()).compareTo(s.toString()) == 0) {
                         Locked = false;
                         finish();
                     }
                 }
             }

             @Override
             public void afterTextChanged(Editable s) {

             }
         };
        editText.addTextChangedListener(watcher);
    }

    @Override
    public void finish() {
        if (Locked) {
            finishAffinity();
        }
        else {
            super.finish();
        }
    }

    public static String loadPassword(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("hello123" , context.MODE_PRIVATE);
        String password = sharedPref.getString("password", "");
        return password;
    }
}
