package com.colordiary.ssu16.colordiary;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.colordiary.ssu16.colordiary.ReaddiaryActivity.diarysRef;

public class WritediaryActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private static final int MAKE_NEW_DIARY_MODE = 0;
    private static final int EDIT_DIARY_MODE = 1;

    private Bitmap finalbitmap = null;
    private StorageReference mStorageRef;
    private int mode = 0;

    EditText editText_Diary;
    ImageButton imageButton_Diary;
    diary my_diary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writediary);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mode = getIntent().getIntExtra("MODE", 0);

        editText_Diary = (EditText) findViewById(R.id.DiaryEditText);
        imageButton_Diary = (ImageButton) findViewById(R.id.DiaryImage);

        if (mode == EDIT_DIARY_MODE) {
            my_diary = (diary) getIntent().getSerializableExtra("DIARY");
            editText_Diary.setText(my_diary.getDiary_text());
            File file = getApplicationContext().getFileStreamPath(my_diary.getDiary_image_name()); //불러오기
            if(file.exists()) {
                Bitmap diary_image = BitmapFactory.decodeFile(file.getPath());
                imageButton_Diary.setImageBitmap(diary_image);
            } else Log.d("hello", "이미지 없음");
        }

        imageButton_Diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(WritediaryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(WritediaryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        new AlertDialog.Builder(WritediaryActivity.this)
                                .setTitle("Request Permission Rationale")
                                .setMessage("사진 불러오기 기능을 사용하기 위해서는 READ_EXTERNAL_STORAGE 권한을 설정해야 합니다.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions(WritediaryActivity.this,
                                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                                    }
                                }).show();
                    } else {
                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(WritediaryActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                } else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //ACTION_PIC과 차이점?
                    intent.setType("image/*"); //이미지만 보이게
                    //Intent 시작 - 갤러리앱을 열어서 원하는 이미지를 선택할 수 있다.
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            //이미지를 하나 골랐을때
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
                Uri bitmapUri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), bitmapUri);
                int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                finalbitmap = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);
                final float scale = getResources().getDisplayMetrics().density;
                int dpWidthInPx = (int) (150 * scale);
                int dpHeightInPx = (int) (150 * scale);
                imageButton_Diary.setLayoutParams(new LinearLayout.LayoutParams(dpWidthInPx, dpHeightInPx));
                imageButton_Diary.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageButton_Diary.setImageBitmap(finalbitmap);
                imageButton_Diary.setBackgroundColor(Color.TRANSPARENT);
            } else {
                Toast.makeText(this, "사진 불러오기 취소 되었습니다.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Oops! 로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //or switch문을 이용하면 될듯 하다.
        if (id == android.R.id.home) {
            Toast.makeText(this, "홈아이콘 클릭", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        } else if (id == R.id.save_button) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    new AlertDialog.Builder(this)
                            .setTitle("Request Permission Rationale")
                            .setMessage("일기 저장기능을 사용하기 위해서는 WRITE_EXTERNAL_STORAGE 권한을 설정해야 합니다.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(WritediaryActivity.this,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                                }
                            }).show();
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
            } else { // 저장하기 구현
                String date = diary.getCurrentDiaryDate();
                String text = editText_Diary.getText().toString();
                String image_name = diary.getCurrentDiaryTime() + ".jpg";
                if (mode == MAKE_NEW_DIARY_MODE) {
                    my_diary = new diary(date, text, image_name, FirebaseAuth.getInstance().getCurrentUser().getUid());
                } else if (mode == EDIT_DIARY_MODE) {
                    my_diary.setDiary_text(text);
                }

                if (finalbitmap != null)//이미지 저장
                    try {
                        SaveBitmapToFileCache(finalbitmap, my_diary.getDiary_image_name());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                if (text.isEmpty()) {
                    Toast.makeText(this, "내용이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    postFirebaseDatabase(true, my_diary);
                    finish();
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public static void postFirebaseDatabase(boolean add, diary t_diary){
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){ //새로 저장 , 갱신이 필요해
            postValues = t_diary.toMap();
            childUpdates.put(t_diary.getDiary_time(), postValues);
            diarysRef.updateChildren(childUpdates);
        } else { //데이터 삭제
            diarysRef.child(t_diary.getDiary_time()).setValue(null);
        }
    }

    private void SaveBitmapToFileCache(Bitmap bitmap, String filename) throws FileNotFoundException {
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        InputStream inputStream = openFileInput(filename);

        StorageReference imageRef = mStorageRef.child("images/" + filename);
        imageRef.putStream(inputStream)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }
}