package com.colordiary.ssu16.colordiary;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.Calendar;


public class MonthAdapter extends BaseAdapter {
    Context mContext;
    private MonthItem[] items;
    int curYear;
    int curMonth;
    int offsetOfFirstDay;
    int lastDay;
    Calendar mCalendar;
    public MonthAdapter(Context context) {
        super();
        mContext = context;
        init();
    }
    // 1개월의 일별 데이터를 담고 있을 수 있는 MonthItem의 배열 객체 생성
    private void init() {
        items = new MonthItem[7 * 6];
        mCalendar = Calendar.getInstance();
        recalculate();
        resetDayNumbers();
    }
    private void recalculate() {
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
        offsetOfFirstDay = getOffsetOfFirstDay(dayOfWeek);
        curYear = mCalendar.get(Calendar.YEAR);
        curMonth = mCalendar.get(Calendar.MONTH);
        lastDay = getMonthLastDay(curYear, curMonth);
    }
    // Calendar.SUNDAY = 1 이지만 우리는 일요일을 오프셋 0으로 처리하고자 함.
    private int getOffsetOfFirstDay(int dayOfWeek) { return dayOfWeek - 1; }
    private int getMonthLastDay(int year, int month) {
        switch (month) {
            case 0: case 2: case 4: case 6: case 7: case 9: case 11:
                return 31;
            case 3: case 5: case 8: case 10:
                return 30;
            default: // 2월
                if(((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) return 29;
                else return 28;
        }
    }
    // 지정한 월의 일별 데이터를 새로 계산하는 메소드 정의
    private void resetDayNumbers() {
        for (int i = 0; i < 42; i++) {
            int dayNumber = (i+1)-offsetOfFirstDay;
            if (dayNumber < 1 || dayNumber > lastDay) {
                dayNumber = 0;
            }
            items[i] = new MonthItem(dayNumber);
        }
    }
    public void setPreviousMonth() {
        mCalendar.add(Calendar.MONTH, -1);
        recalculate();
        resetDayNumbers();
    }
    public void setNextMonth() {
        mCalendar.add(Calendar.MONTH, 1);
        recalculate();
        resetDayNumbers();
    }
    public int getCurYear() {
        return curYear;
    }
    public int getCurMonth() {
        return curMonth;
    }
    @Override
    public int getCount() {
        return 7 * 6;
    }
    @Override
    public Object getItem(int position) {
        return items[position];
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MonthItemView itemView;
        if (convertView == null) {
            itemView = new MonthItemView(mContext);
        } else {
            itemView = (MonthItemView) convertView;
        }
// create a params
        GridView.LayoutParams params = new GridView.LayoutParams(
                GridView.LayoutParams.MATCH_PARENT,120);
// calculate columnIndex
        int columnIndex = position % 7;
// set item data and properties
        itemView.setItem(items[position]);
        itemView.setLayoutParams(params);
        itemView.setPadding(2, 2, 2, 2);
// set properties
        itemView.setGravity(Gravity.CENTER);
        if (columnIndex == 0) {
            itemView.setTextColor(Color.DKGRAY);
        } else {
            itemView.setTextColor(Color.GRAY);
        }
        return itemView;
    } // end getView()
} // end class MonthAdapter
