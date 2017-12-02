package com.colordiary.ssu16.colordiary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ReaddiaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readdiary);

        ListView listView = (ListView) findViewById(R.id.listView);
        final DiaryAdapter adapter = new DiaryAdapter();

        adapter.addItem(new diary("2017-10-16", "오늘은 놀았다.", "bitmapfile.jpg"));
        adapter.addItem(new diary("2017-10-17", "오늘도 놀았다.", "bitmapfile.jpg"));
        adapter.addItem(new diary("2017-10-18", "오늘은 게임했다.", "bitmapfile.jpg"));
        adapter.addItem(new diary("2017-10-19", "오늘은 공부했다.", "bitmapfile.jpg"));

        listView.setAdapter(adapter);
    }

    public class DiaryAdapter extends BaseAdapter {

        private ArrayList<diary> diarys = new ArrayList();

        @Override
        public int getCount() {
            return diarys.size();
        }

        @Override
        public Object getItem(int position) {
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
