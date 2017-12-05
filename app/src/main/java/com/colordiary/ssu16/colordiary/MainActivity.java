package com.colordiary.ssu16.colordiary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // 월별 캘린더 어댑터
 MonthAdapter monthViewAdapter;
// 월을 표시하는 텍스트뷰
TextView monthText;
@Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main); // 레이아웃에 정의된 월별 캘린더 뷰 객체 참조
    GridView monthView = (GridView) findViewById(R.id.monthView);
    monthViewAdapter = new MonthAdapter(this); // 어댑터 객체 생성 후 월별 캘린더 뷰 객체에 설정
    monthView.setAdapter(monthViewAdapter);
    monthText = (TextView) findViewById(R.id.monthText);
    setMonthText();
    // 이전 월 [ 이동 ] 버튼 클릭 시 일별 데이터를 다시 계산하는 메소드 호출하고 화면 갱신
    Button monthPrevious = (Button) findViewById(R.id.monthPrevious);
    monthPrevious.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            monthViewAdapter.setPreviousMonth();
            monthViewAdapter.notifyDataSetChanged();
            setMonthText();}});// 다음 월 [ 이동 ] 버튼 클릭 시 일별 데이터를 다시 계산하는 메소드 호출하고 화면 갱신
    Button monthNext = (Button) findViewById(R.id.monthNext);
    monthNext.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            monthViewAdapter.setNextMonth();
            monthViewAdapter.notifyDataSetChanged();
            setMonthText(); } }); } // end method onCreate()

    private void setMonthText() {
        int curYear = monthViewAdapter.getCurYear();
        int curMonth = monthViewAdapter.getCurMonth();
        monthText.setText(curYear + "년 " + (curMonth + 1) + "월");
    }
}
