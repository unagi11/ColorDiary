package com.colordiary.ssu16.colordiary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReaddiaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readdiary);

        final ListView listView = (ListView) findViewById(R.id.listView);
        final DiaryAdapter adapter = new DiaryAdapter();

/*        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            Toast.makeText(getApplicationContext(), mUser.getProviderId() + ", " + mUser.getDisplayName() + ", " + mUser.getUid(), Toast.LENGTH_SHORT).show();
                            // firebase , 장어국, NHH0igL3sge6oTg1cZ19ufDx1k43
                            // Send token to your backend via HTTPS
                            // ...
                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                });*/

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference myRef = rootRef.child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("diary");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("hello", dataSnapshot.getKey());
                diary temp = dataSnapshot.getValue(diary.class);
                adapter.addItem(temp);
                listView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.deleteItemAll();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                    List messages = postSnapshot.getValue(t);
                    if( messages == null ) {
                        adapter.addItem(new diary("XXXX-XX-XX", "일기가 없습니다.", ""));
                    }
                    else {
                        adapter.addItem(new diary(messages.get(0).toString(), messages.get(1).toString(), messages.get(2).toString()));
                        System.out.println("The first message is: " + messages.get(0).toString());
                        System.out.println("The second message is: " + messages.get(1).toString());
                        System.out.println("The third message is: " + messages.get(2).toString());
                        /*if (diaryData.size() < adapter.getCount()) { //

                        }
                        else if (diaryData.size() == adapter.getCount()) { // 내용이 수정된 경우 내용을 새로 가져와 class내용을 변경해준다..
                            adapter.getItem(Integer.parseInt(postSnapshot.getKey())).setDiary_date(messages.get(0).toString());
                            adapter.getItem(Integer.parseInt(postSnapshot.getKey())).setDiary_text(messages.get(1).toString());
                            adapter.getItem(Integer.parseInt(postSnapshot.getKey())).setDiary_image_name(messages.get(2).toString());
                        } else if (diaryData.size() > adapter.getCount()) { //database보다 지금 불러온게 적다.
                            adapter.addItem(new diary(messages.get(0).toString(), messages.get(1).toString(), messages.get(2).toString()));
                        }
                    }
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("firebase", "onCancelled: " + databaseError.getMessage());
            }
        });*/

        listView.setAdapter(adapter);
    }

    public class DiaryAdapter extends BaseAdapter {

        private ArrayList<diary> diarys = new ArrayList();

        public void deleteItem(int position) { diarys.remove(position); }

        public void deleteItemAll() { diarys.clear(); }

        @Override
        public int getCount() {
            return diarys.size();
        }

        @Override
        public diary getItem(int position) {
            return diarys.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            diaryView view = (diaryView) convertView;
            if (convertView == null)
                view = new diaryView(getApplicationContext());

            diary diary = diarys.get(position);
            view.setDate_textView(diary.getDiary_date());
            view.setDiary_textView(diary.getDiary_text());
            view.setDiary_imageView(diary.getDiary_image_name(), getApplicationContext());

            return view;
        }

        void addItem(diary diary) {diarys.add(diary);}
    }

}
