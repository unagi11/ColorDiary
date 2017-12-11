package com.colordiary.ssu16.colordiary;

import android.graphics.Bitmap;

public class ImageItem {
    private Bitmap bitmap;
    private String name;

    public ImageItem(Bitmap image, String name) {
        super();
        this.bitmap = image;
        this.name = name;
    }

    public Bitmap getImage() {
        return bitmap;
    }

    public void setImage(Bitmap image) {
        this.bitmap = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}