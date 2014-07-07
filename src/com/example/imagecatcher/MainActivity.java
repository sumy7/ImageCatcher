package com.example.imagecatcher;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Button btn_gallery;
    private Button btn_camera;

    private static final int ALBUM_REQUEST_CODE = 233333;
    private static final int CAMERA_REQUEST_CODE = 2233;
    
    private File tempFile = new File(
            Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            getPhotoFileName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_gallery = (Button) findViewById(R.id.btn_gallery);
        btn_camera=(Button) findViewById(R.id.btn_camera);
        
        btn_gallery.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "请选择一张图片", Toast.LENGTH_LONG)
                        .show();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "选择一张图片"),
                        ALBUM_REQUEST_CODE);
            }
        });

        btn_camera.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraintent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);

                cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(tempFile));
                startActivityForResult(cameraintent, CAMERA_REQUEST_CODE);

            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ALBUM_REQUEST_CODE && resultCode == RESULT_OK) {
            Intent intent = new Intent(MainActivity.this, ImageActivity.class);
            intent.setData(data.getData());
            startActivity(intent);
        } else if (requestCode == CAMERA_REQUEST_CODE
                && resultCode == RESULT_OK) {
            Intent intent = new Intent(MainActivity.this, ImageActivity.class);
            intent.setData(Uri.fromFile(tempFile));
            startActivity(intent);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss", Locale.getDefault());
        return dateFormat.format(date) + ".jpg";
    }

}