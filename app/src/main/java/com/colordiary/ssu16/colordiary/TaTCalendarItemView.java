package com.colordiary.ssu16.colordiary;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

/**
 * Created by soohee on 2017-11-22.
 */


public class TaTCalendarItemView extends View {
    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackgroundToday = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackgroundEvent = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int dayOfWeek = -1;
    private boolean isStaticText = false;
    private long millis;
    private Rect rect;
    private boolean isTouchMode;
    private int dp11;
    private int dp16;
    private boolean hasEvent = false;
    private int[] mColorEvents;
    private boolean changeTextColor = false;

    public TaTCalendarItemView(Context context) {
        super(context);
        initialize();
    }

    public TaTCalendarItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        dp11 = (int) dp2px(getContext(),11);
        dp16 = (int) dp2px(getContext(),16);

        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(dp11);

        if (Build.VERSION.SDK_INT >= 23) {
            mPaintBackground.setColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
            mPaintBackgroundToday.setColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
            mPaintBackgroundEvent.setColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        }else{
            mPaintBackground.setColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
            mPaintBackgroundToday.setColor(getContext().getResources().getColor(R.color.colorAccent));
            mPaintBackgroundEvent.setColor(getContext().getResources().getColor(R.color.colorPrimary));
        }
        setClickable(true);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                Log.d("hatti.onTouchEvent", event.getAction() + "");
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
//                        setBackgroundResource(R.drawable.round_default_select);
                        rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                        isTouchMode = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isTouchMode) {
                            ((TaTCalendarView) getParent()).setCurrentSelectedView(v);
                            isTouchMode = false;
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        isTouchMode = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                            isTouchMode = false;
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
        setPadding(30, 0, 30, 0);
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));
        mPaint.setTextAlign(Paint.Align.CENTER);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        TaTCalendarView calendarView = (TaTCalendarView) getParent();
        if (calendarView.getParent() instanceof ViewPager) {
            ViewGroup parent = (ViewPager) calendarView.getParent();
            TaTCalendarItemView tagView = (TaTCalendarItemView) parent.getTag();

            if (!isStaticText && tagView != null && tagView.getTag() != null && tagView.getTag() instanceof Long) {
                long millis = (long) tagView.getTag();
                if (isSameDay(millis, this.millis)) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        canvas.drawRoundRect(xPos - dp16, getHeight() / 2 - dp16, xPos + dp16, getHeight() / 2 + dp16, 50f, 50f, mPaintBackground);
                    }else{
                        canvas.drawRect(xPos - dp16, getHeight() / 2 - dp16, xPos + dp16, getHeight() / 2 + dp16, mPaintBackground);
                    }
                }
            }
        }

        if (!isStaticText && isToday(millis)) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.drawRoundRect(xPos - dp16, getHeight() / 2 - dp16, xPos + dp16, getHeight() / 2 + dp16, 50f, 50f, mPaintBackgroundToday);
            }else{
                canvas.drawRect(xPos - dp16, getHeight() / 2 - dp16, xPos + dp16, getHeight() / 2 + dp16,  mPaintBackgroundToday);
            }
        }

        if (isStaticText) {
            // 요일 표시
            mPaint.setTypeface(Typeface.create((String)null, Typeface.BOLD));
            canvas.drawText(TaTCalendarView.DAY_OF_WEEK[dayOfWeek], xPos, yPos, mPaint);
        } else {
            // 날짜 표시
            mPaint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.NORMAL));
            if(changeTextColor) {
                mPaint.setColor(Color.LTGRAY);
            }else{
                mPaint.setColor(Color.BLACK);
            }
            canvas.drawText(calendar.get(Calendar.DATE) + "", xPos, yPos, mPaint);
        }

        if (hasEvent) {
            if (Build.VERSION.SDK_INT >= 23) {
                mPaintBackgroundEvent.setColor(ContextCompat.getColor(getContext(),mColorEvents[0]));
            }else{
                mPaintBackgroundEvent.setColor(getResources().getColor(mColorEvents[0]));
            }

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.drawRoundRect(xPos - 5, getHeight() / 2 + 20, xPos + 5, getHeight() / 2 + 30, 50f, 50f, mPaintBackground);
            }else{
                canvas.drawRect(xPos - 5, getHeight() / 2 + 20, xPos + 5, getHeight() / 2 + 30, mPaintBackground);
            }
        }

    }

    private boolean isToday(long millis) {
        Calendar cal1 = Calendar.getInstance();
        return isSameDay(cal1.getTimeInMillis(), millis);

    }

    public void setDate(long millis) {
        this.millis = millis;
        setTag(millis);
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        isStaticText = true;
    }

    public void setEvent(int... resid) {
        hasEvent = true;
        mColorEvents = resid;
    }

    public void setTextColorChange(boolean changeTextColor) {
        this.changeTextColor = changeTextColor;
    }
    public boolean isSameDay(long millis1, long millis2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(millis1);
        cal2.setTimeInMillis(millis2);
//        Log.d("hatti.calendar", "" + cal1.get(Calendar.YEAR) + "" + cal1.get(Calendar.MONTH) + "" + cal1.get(Calendar.DATE) + " VS " +
//                cal2.get(Calendar.YEAR) + "" + cal2.get(Calendar.MONTH) + "" + cal2.get(Calendar.DATE));
        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) && cal1.get(Calendar.DATE) == cal2.get(Calendar.DATE));
    }

    public boolean isStaticText() {
        return isStaticText;
    }

    public static float dp2px(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }


}