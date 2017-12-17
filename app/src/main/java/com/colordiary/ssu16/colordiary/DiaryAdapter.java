package com.colordiary.ssu16.colordiary;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by unagi on 2017-12-12.
 */

public class DiaryAdapter extends BaseAdapter {

    Context mycontext;

    private ArrayList<diary> diarys = new ArrayList();

    public DiaryAdapter (Context context) {
        super();
        mycontext = context;
    }

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
            view = new diaryView(mycontext);

        diary diary = diarys.get(position);
        view.setDate_textView(diary.getDiary_date());
        view.setDiary_textView(diary.getDiary_text());
        try {
            view.setDiary_imageView(diary.getDiary_image_name(), mycontext);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }
    void addItem(diary diary) {diarys.add(diary);}
}