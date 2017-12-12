package com.colordiary.ssu16.colordiary;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.io.IOException;
import java.util.Calendar;

/**
 * Created by unagi on 2017-12-11.
 */

public class SingleImageView extends LinearLayoutCompat {

    ImageView imageView;

    public SingleImageView(Context context) {
        super(context);
        init(context);
    }

    public SingleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SingleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.grid_item_layout, this, true);
        imageView = (ImageView)findViewById(R.id.item_image_view);
    }

    void setImageView (String image_name, Context context) {
        try {
            diaryView.setImageViewByImageName(imageView, context, image_name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}