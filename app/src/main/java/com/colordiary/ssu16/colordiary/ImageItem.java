package com.colordiary.ssu16.colordiary;

import android.graphics.Bitmap;



public class ImageItem {
    private Bitmap image;

    public ImageItem(Bitmap image, String title) {
        super();
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
}