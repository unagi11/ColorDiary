package com.colordiary.ssu16.colordiary;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

public class SetLockActivity extends AppCompatActivity {

    EditText editText;
    String password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        editText = (EditText)findViewById(R.id.password_input);
        editText.setHint("input 4-number password");
        password = null;

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 4) {
                    if (password == null) {
                        password = s.toString();
                        editText.setText("");
                        editText.setHint("input one more same number");
                    } else {
                        if (password.compareTo(s.toString()) == 0 ) {
                            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("hello123" , Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("password", password);
                            editor.commit();
                            Toast.makeText(getApplicationContext(), "비밀번호가 정상적으로 설정되었습니다.", Toast.LENGTH_LONG);
                            finish();
                        } else {
                            password = null;
                            editText.setText("");
                            editText.setHint("Opps! you inputed wrong number. input 4-number");
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editText.addTextChangedListener(textWatcher);
    }
}
