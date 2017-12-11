package com.colordiary.ssu16.colordiary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;

public class CalendarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    MonthAdapter monthViewAdapter;
    GridLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),WritediaryActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        container = (GridLayout)findViewById(R.id.include);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_calendar1) {
            LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            inflater.inflate(R.layout.activity_main, container);

            GridView monthView = (GridView) findViewById(R.id.monthView);
            monthViewAdapter = new MonthAdapter(this); // 어댑터 객체 생성 후 월별 캘린더 뷰 객체에 설정
            monthView.setAdapter(monthViewAdapter);
            // 이전 월 [ 이동 ] 버튼 클릭 시 일별 데이터를 다시 계산하는 메소드 호출하고 화면 갱신

            getSupportActionBar().setTitle(getMonthText());

            Button monthPrevious = (Button) findViewById(R.id.monthPrevious);
            monthPrevious.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    monthViewAdapter.setPreviousMonth();
                    monthViewAdapter.notifyDataSetChanged();
                    getSupportActionBar().setTitle(getMonthText());
                }});// 다음 월 [ 이동 ] 버튼 클릭 시 일별 데이터를 다시 계산하는 메소드 호출하고 화면 갱신

            Button monthNext = (Button) findViewById(R.id.monthNext);
            monthNext.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    monthViewAdapter.setNextMonth();
                    monthViewAdapter.notifyDataSetChanged();
                    getSupportActionBar().setTitle(getMonthText());
                } });
        } else if (id == R.id.nav_calendar2) {
            LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
//            inflater.inflate(R.layout, container);

        } else if (id == R.id.nav_cummunication) {

        } else if (id == R.id.nav_options) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String getMonthText() {
        int curYear = monthViewAdapter.getCurYear();
        int curMonth = monthViewAdapter.getCurMonth();
        return curYear + "년 " + (curMonth + 1) + "월";
    }

}
