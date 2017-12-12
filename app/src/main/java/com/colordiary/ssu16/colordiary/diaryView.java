package com.colordiary.ssu16.colordiary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by unagi on 2017-11-29.
 */

public class diaryView extends LinearLayoutCompat implements Serializable{

    private static final long serialVersionUID = 1000001L;

    TextView date_textView;
    TextView diary_textView;
    ImageView diary_imageView;

    public diaryView(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.a_diary, this, true);

        date_textView = (TextView) findViewById(R.id.date_TextView);
        diary_textView = (TextView) findViewById(R.id.diary_TextView);
        diary_imageView = (ImageView) findViewById(R.id.diary_ImageView);
    }

    void setDate_textView(String diary_date) { date_textView.setText(diary_date); }

    void setDiary_textView(String diary_text) { diary_textView.setText(diary_text); }

    void setDiary_imageView(String diary_image_name, Context context) throws IOException {
        setImageViewByImageName(diary_imageView, context, diary_image_name);
    }

    static void setImageViewByImageName(final ImageView view, Context context , String diary_image_name) throws IOException {
        File file = context.getFileStreamPath(diary_image_name); //불러오기
        if(file.exists()) {
            Bitmap diary_image = BitmapFactory.decodeFile(file.getPath());
            view.setImageBitmap(diary_image);
        } else {
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = mStorageRef.child("images/" + diary_image_name);
            final File localFile = File.createTempFile("images", "jpg");
            imageRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap diary_image = BitmapFactory.decodeFile(localFile.getPath());
                            view.setImageBitmap(diary_image);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle failed download
                    // ...
                }
            });
        }

    }
}
