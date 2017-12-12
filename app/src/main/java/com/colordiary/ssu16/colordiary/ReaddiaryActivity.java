package com.colordiary.ssu16.colordiary;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.os.Build.ID;

public class ReaddiaryActivity extends AppCompatActivity {

    static final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    static final DatabaseReference diarysRef = rootRef.child("diarys");
    static String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private int MODE = 0;
//    static final DatabaseReference myRef = rootRef.child("diarys").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("diary");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readdiary);

        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final ListView listView = (ListView) findViewById(R.id.listView);
        final DiaryAdapter adapter = new DiaryAdapter(getApplicationContext());

        MODE = getIntent().getIntExtra("MODE", 0);

        diarysRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.deleteItemAll();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    diary temp = postSnapshot.getValue(diary.class);
                    Log.d("hello", ": " + MODE);
                    if (MODE == 0 && (myUid.compareTo(temp.getDiary_uid()) == 0)) {
                        adapter.addItem(temp);
                    }
                    if (MODE == 1 && temp.isDiary_public()) {
                        adapter.addItem(temp);
                    }
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //WritediaryActivity.postFirebaseDatabase(false, adapter.getItem(position));//데이터 삭제
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ReadOnediaryActivity.class);
                intent.putExtra("DIARY", adapter.getItem(position));
                intent.putExtra("MODE", MODE);
                startActivity(intent);
            }
        });

        listView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        String date = diary.getCurrentDiaryDate();
        String image_name = diary.getCurrentDiaryTime() + ".jpg";
        diary mdiary = new diary(date, "", image_name, FirebaseAuth.getInstance().getCurrentUser().getUid());
        WritediaryActivity.postFirebaseDatabase(true, mdiary);
        WritediaryActivity.postFirebaseDatabase(false, mdiary);
        super.onResume();
    }

}
