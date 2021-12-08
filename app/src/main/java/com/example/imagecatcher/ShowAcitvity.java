package com.example.imagecatcher;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ShowAcitvity extends Activity {
    private ImageView iv_show;

    Button btn_saveimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setTheme(android.R.style.Theme_Black);
        } else {
            setTheme(android.R.style.Theme_DeviceDefault);
        }

        setContentView(R.layout.activity_show);
        iv_show = (ImageView) findViewById(R.id.iv_show);

        if (DAO.sendBitmap != null) {
            iv_show.setImageBitmap(DAO.sendBitmap);
        }

        btn_saveimage = (Button) findViewById(R.id.buttonSaveImage);

        btn_saveimage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                saveImagetoStorage();
            }
        });

    }

    /*
     * 保存图片到存储器，并返回保存图片位置
     */
    private Uri saveImagetoStorage() {
        if (DAO.sendBitmap == null) {
            Toast.makeText(this, "没有需要保存的照片", Toast.LENGTH_LONG).show();
            return null;
        }
        File path = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        path.mkdir();

        String timeStamp = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss",
                Locale.getDefault())
                .format(new Date(System.currentTimeMillis()));

        String fileName = timeStamp + ".jpg";

        File file = new File(path, fileName);

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            DAO.sendBitmap.compress(CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "照片保存失败", Toast.LENGTH_LONG).show();
            return null;
        }

        Toast.makeText(this, "照片已保存：\n" + file.toString(), Toast.LENGTH_LONG)
                .show();
        Uri uri = Uri.fromFile(file);

        // 刷新系统图片数据缓存
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(uri);
        sendBroadcast(intent);

        return uri;
    }
}
