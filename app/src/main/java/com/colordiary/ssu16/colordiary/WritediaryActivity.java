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
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Debug;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class WritediaryActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private Bitmap finalbitmap = null;

    EditText editText_Diary;

    ImageButton imageButton_Diary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writediary);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editText_Diary = (EditText)findViewById(R.id.DiaryEditText);
        imageButton_Diary = (ImageButton)findViewById(R.id.DiaryImage);
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
                Uri uri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                finalbitmap = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);
                final float scale = getResources().getDisplayMetrics().density;
                int dpWidthInPx  = (int) (150 * scale);
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

            Toast.makeText(this, "저장버튼 클릭", Toast.LENGTH_SHORT).show();

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
                if (finalbitmap != null)
                    SaveBitmapToFileCache(finalbitmap);
                finish();
/*               File file = getFileStreamPath("bitmapfile.jpg");
                if(file.exists()) {
                    ImageView imageview = (ImageView)findViewById(R.id.imageView);
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                    imageview.setImageBitmap(bitmap);
                }
                else
                    Log.d("hello", "응 그딴 파일 없어 ~");
*/
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();
    }

    private void SaveDiaryTOJSON() {
        Log.d("hello", editText_Diary.getText().toString());
    }

    private void SaveBitmapToFileCache(Bitmap bitmap) {
        String filename = "bitmapfile.jpg";
        FileOutputStream outputStream = null;
        try
        {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                outputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
