package com.colordiary.ssu16.colordiary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Layout;
import android.util.Log;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.colordiary.ssu16.colordiary.ReaddiaryActivity.diarysRef;
import static com.colordiary.ssu16.colordiary.ReaddiaryActivity.myUid;

public class CalendarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    MonthAdapter monthViewAdapter;
    GridLayout container;
    private GridView gridView;
    private DiaryAdapter2 gridAdapter;

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

        changeView(R.id.nav_calendar1);
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
            changeView(id);
        } else if (id == R.id.nav_calendar2) {
            changeView(id);
        } else if (id == R.id.nav_DiaryList) {
            Intent intent = new Intent(getApplicationContext(),ReaddiaryActivity.class);
            intent.putExtra("MODE", 0);
            startActivity(intent);
        } else if (id == R.id.nav_cummunication) {
            Intent intent = new Intent(getApplicationContext(),ReaddiaryActivity.class);
            intent.putExtra("MODE", 1);
            startActivity(intent);
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

    public class DiaryAdapter2 extends ArrayAdapter {
        private ArrayList<diary> diarys = new ArrayList();

        public DiaryAdapter2(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
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
            SingleImageView view = (SingleImageView) convertView;
            if (convertView == null)
                view = new SingleImageView(getApplicationContext());
            diary diary = diarys.get(position);
            view.setImageView(diary.getDiary_image_name(), getApplicationContext());
            return view;
        }
        void addItem(diary diary) {diarys.add(diary);}
    }

    private void changeView (int index) {
        LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        container = (GridLayout)findViewById(R.id.include);

        if (container.getChildCount() > 0) {
            container.removeViewAt(0);
        }

        View view = null;
        switch (index) {
            case R.id.nav_calendar1 :
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

                monthView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        String Month = Integer.toString(monthViewAdapter.getCurMonth() + 1);
                        String Day = Integer.toString(((MonthItem)monthViewAdapter.getItem(position)).getDay());

                        if (Month.length() == 1)
                            Month = "0" + Month;
                        if (Day.length() == 1)
                            Day = "0" + Day;

                        final String Date = "" + monthViewAdapter.getCurYear() + "-" + Month + "-" + Day;
                        final ListView main_listView = (ListView) findViewById(R.id.main_ListView);
                        final DiaryAdapter diaryAdapter = new DiaryAdapter(getApplicationContext());

                        diarysRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                diaryAdapter.deleteItemAll();
                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    diary temp = postSnapshot.getValue(diary.class);
                                    if ((myUid.compareTo(temp.getDiary_uid()) == 0 && Date.compareTo(temp.getDiary_date()) == 0)) {
                                        diaryAdapter.addItem(temp);
                                    }
                                }
                                main_listView.setAdapter(diaryAdapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
                break;

            case R.id.nav_calendar2 :
                getSupportActionBar().setTitle("Gallery Calendar");
                inflater.inflate(R.layout.activity_calendar2, container);
                gridView = (GridView) findViewById(R.id.grid_view);
                gridAdapter = new DiaryAdapter2(this, R.layout.grid_item_layout);
                diarysRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        gridAdapter.deleteItemAll();
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            diary temp = postSnapshot.getValue(diary.class);
                            if ((myUid.compareTo(temp.getDiary_uid()) == 0)) {
                                gridAdapter.addItem(temp);
                            }
                        }
                        gridView.setAdapter(gridAdapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), ReadOnediaryActivity.class);
                        intent.putExtra("DIARY", gridAdapter.getItem(position));
                        intent.putExtra("MODE", 0);//private
                        startActivity(intent);
                    }
                });
                gridView.setAdapter(gridAdapter);
                break;
        }

        if (view != null) {
            container.addView(view);
        }
    }


    private void changeView2 (int index) {
    }


}
