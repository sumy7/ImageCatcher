package com.example.imagecatcher;


import java.io.InputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ImageActivity extends Activity {
    Bitmap mBitmap = null; // 源图像
    Bitmap mBitmapAfter = null;// 处理后的图像
    Canvas mCanvas; // 画布
    MyView imageviewBefore;

    ProgressDialog progressDialog; // 处理中对话框

    // 处理结束后更新界面
    // 界面更新需在UI线程（主线程）进行，子线程无法操作（抛出异常）
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 100) {
                progressDialog.dismiss();
                DAO.sendBitmap = mBitmapAfter;
                Intent intent = new Intent(ImageActivity.this,
                        ShowAcitvity.class);
                startActivity(intent);
            } else {
                progressDialog.setProgress(msg.what);
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageviewBefore = new MyView(this);

        setContentView(imageviewBefore);

        // 设置view和listener
        // imageviewBefore = (MyView) findViewById(R.id.imageViewBefore);

        // 获取前一个acitvity传过来的图片地址
        Intent intent = getIntent();
        Uri imageuri = intent.getData();

        try {
            InputStream inputStream = getContentResolver().openInputStream(
                    imageuri);

            // 解析图片大小
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, opts);

            int mWidth = opts.outWidth;
            int mHeight = opts.outHeight;

            // 获取设备屏幕大小
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int screenHeight = getResources().getDisplayMetrics().heightPixels;
            int simple = 1;

            // 计算图片缩放倍率
            while (mWidth > screenWidth * simple) {
                simple = simple * 2;
            }

            // 读取图片
            opts.inJustDecodeBounds = false;
            opts.inSampleSize = simple;

            if (mBitmap != null) {
                mBitmap.recycle();
            }
            inputStream.close();

            inputStream = getContentResolver().openInputStream(imageuri);
            Bitmap tmpBitmap = BitmapFactory.decodeStream(inputStream, null,
                    opts);

            // 显示图片
            mBitmap = Bitmap.createBitmap(tmpBitmap.getWidth(),
                    tmpBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            mCanvas.drawBitmap(tmpBitmap, 0, 0, null);

            imageviewBefore.setBackground(mBitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // 根据菜单进行操作
        int id = item.getItemId();
        if (id == R.id.action_muke) {
            Toast.makeText(this, "木刻效果", Toast.LENGTH_LONG).show();
            DAO.Option option = new DAO.Option();
            option.what = DAO.MUKE;
            progressImage(option);
            return true;
        } else if (id == R.id.action_fudiao) {
            Toast.makeText(this, "浮雕效果", Toast.LENGTH_LONG).show();
            DAO.Option option = new DAO.Option();
            option.what = DAO.FUDIAO;
            progressImage(option);
            return true;
        } else if (id == R.id.action_youhua) {
            Toast.makeText(this, "油画效果", Toast.LENGTH_LONG).show();
            final DAO.Option option = new DAO.Option();
            option.what = DAO.YOUHUA;
            option.radius = 5;
            WidthPickerDialog dialog = new WidthPickerDialog(
                    ImageActivity.this,
                    new WidthPickerDialog.OnWidthChangedListener() {

                        public void widthChanged(int penWidth) {
                            option.radius = penWidth;
                            progressImage(option);
                        }
                    });
            dialog.show();
            dialog.setMaxWidth(mBitmap.getWidth() / 10);
            dialog.setPenWidth(option.radius);
            dialog.setTitle("设置效果半径");
            return true;
        } else if (id == R.id.action_seamcarver) {
            Toast.makeText(this, "SEAMCARVER", Toast.LENGTH_LONG).show();
            final DAO.Option option = new DAO.Option();
            option.what = DAO.SEAMCARVER;
            option.horizontalseam = mBitmap.getHeight() / 10;
            option.verticalseam = mBitmap.getWidth() / 10;
            option.protect = imageviewBefore.getViewBitmap();
            WidthHeightPickerDialog dialog = new WidthHeightPickerDialog(
                    ImageActivity.this,
                    new WidthHeightPickerDialog.OnWidthHeightChangedListener() {
                        @Override
                        public void widthChanged(int Width, int Height) {
                            option.horizontalseam = Height;
                            option.verticalseam = Width;
                            progressImage(option);
                        }
                    });
            dialog.show();
            dialog.setMaxHeight(mBitmap.getHeight() / 2);
            dialog.setMaxWidth(mBitmap.getWidth() / 2);
            dialog.setWidth(option.verticalseam);
            dialog.setHeight(option.horizontalseam);
            dialog.setTitle("设置效果缩减的宽度和高度");

        } else if (id == R.id.action_seamcarverPaintErase) {
            imageviewBefore.setPaintKind(MyView.PAINT_ERASE);
            Toast.makeText(this, "当前画笔类型：ERASE", Toast.LENGTH_LONG).show();
        } else if (id == R.id.action_seamcarverPaintProtect) {
            imageviewBefore.setPaintKind(MyView.PAINT_PROTECT);
            Toast.makeText(this, "当前画笔类型：PROTECT", Toast.LENGTH_LONG).show();
        } else if (id == R.id.action_seamcarverPaintExpose) {
            imageviewBefore.setPaintKind(MyView.PAINT_EXPOSE);
            Toast.makeText(this, "当前画笔类型：EXPOSE", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * 处理数据
     */
    private void progressImage(DAO.Option option) {
        final DAO.Option progressoption = option;
        progressDialog = new ProgressDialog(ImageActivity.this);
        progressDialog.setTitle("照片处理中...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.show();

        // 创建子线程用于处理数据，防止主线程阻塞
        new Thread() {
            public void run() {
                if (mBitmapAfter != null) {
                    mBitmapAfter.recycle();
                }
                mBitmapAfter = DAO.handlerImage(mBitmap, progressoption,
                        handler);
                handler.sendEmptyMessage(100);
            };
        }.start();
    }

}
