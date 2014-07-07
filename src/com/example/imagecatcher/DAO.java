package com.example.imagecatcher;


import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.os.Handler;

public class DAO {
    public static Bitmap sendBitmap;

    public static int MUKE = 1;
    public static int FUDIAO = 2;
    public static int YOUHUA = 3;
    public static int SEAMCARVER = 4;

    public static class Option {
        int what; // 执行的操作
        int radius; // 油画效果的半径
        int horizontalseam;
        int verticalseam;
        Bitmap protect;
    }

    public static Handler handler = null;

    public static Bitmap handlerImage(Bitmap image, Option option,
            Handler processhandler) {
        handler = processhandler;
        if (option.what == MUKE) {
            return muke(image);
        }
        if (option.what == FUDIAO) {
            return fudiao(image);
        }
        if (option.what == YOUHUA) {
            return youhua(image, option.radius);
        }
        if (option.what == SEAMCARVER) {
            return seamCarver(image, option.protect, option.horizontalseam,
                    option.verticalseam);
        }
        return null;
    }

    /*
     * 木刻效果
     * 
     * 颜色平均值大于128设置为黑色，否则置为白色
     */
    private static Bitmap muke(Bitmap image) {
        float step = (float) (100.0 / image.getWidth());
        float now = 0;

        Bitmap ret = Bitmap.createBitmap(image.getWidth(), image.getHeight(),
                Config.ARGB_8888);
        for (int i = 0; i < image.getWidth(); i++) {

            handler.sendEmptyMessage((int) now);
            now += step;

            for (int j = 0; j < image.getHeight(); j++) {

                int color = image.getPixel(i, j);
                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);
                if ((r + g + b) / 3 > 128)
                    ret.setPixel(i, j, Color.BLACK);
                else
                    ret.setPixel(i, j, Color.WHITE);
            }
        }
        return ret;
    }

    /*
     * 浮雕效果
     * 
     * 与相邻像素颜色差值加上一个常数128
     */
    private static Bitmap fudiao(Bitmap image) {
        float step = (float) (100.0 / image.getWidth());
        float now = 0;

        Bitmap ret = Bitmap.createBitmap(image.getWidth() - 1,
                image.getHeight() - 1, Config.ARGB_8888);
        for (int i = 0; i < image.getWidth() - 1; i++) {

            handler.sendEmptyMessage((int) now);
            now += step;

            for (int j = 0; j < image.getHeight() - 1; j++) {

                int color1 = image.getPixel(i, j);
                int color2 = image.getPixel(i + 1, j + 1);

                int r1 = Color.red(color1);
                int g1 = Color.blue(color1);
                int b1 = Color.blue(color1);

                int r2 = Color.red(color2);
                int g2 = Color.blue(color2);
                int b2 = Color.blue(color2);

                int r = (r1 - r2 + 128 > 255) ? 255 : r1 - r2 + 128;
                int g = (g1 - g2 + 128 > 255) ? 255 : g1 - g2 + 128;
                int b = (b1 - b2 + 128 > 255) ? 255 : b1 - b2 + 128;

                if (r < 0)
                    r = 0;
                if (g < 0)
                    g = 0;
                if (b < 0)
                    b = 0;
                ret.setPixel(i, j, Color.rgb(r, g, b));
            }
        }
        return ret;
    }

    /*
     * 油画效果
     * 
     * 用半径内的随机一个像素代替本像素
     */
    public static Bitmap youhua(Bitmap image, int radius) {
        float step = (float) (100.0 / image.getWidth());
        float now = 0;

        Bitmap ret = Bitmap.createBitmap(image.getWidth(), image.getHeight(),
                Config.ARGB_8888);
        for (int i = 0; i < image.getWidth(); i++) {
            handler.sendEmptyMessage((int) now);
            now += step;
            for (int j = 0; j < image.getHeight(); j++) {
                Random random = new Random();
                int a = i + random.nextInt(radius * 2) - radius;
                int b = j + random.nextInt(radius * 2) - radius;
                if (a >= image.getWidth())
                    a = image.getWidth() - 1;
                if (a < 0)
                    a = 0;
                if (b >= image.getHeight())
                    b = image.getHeight() - 1;
                if (b < 0)
                    b = 0;
                ret.setPixel(i, j, image.getPixel(a, b));
            }
        }
        return ret;
    }

    /*
     * 压轴效果
     */
    public static Bitmap seamCarver(Bitmap image, Bitmap protect,
            int horizontalSeam, int verticalSeam) {

        float step = (float) (100.0 / (horizontalSeam + verticalSeam));
        float now = 0;

        SeamCarver seamCarver = new SeamCarver(image, protect);
        for (int i = 0; i < horizontalSeam; i++) {
            handler.sendEmptyMessage((int) now);
            now += step;

            seamCarver.removeHorizontalSeam(seamCarver.findHorizontalSeam());
        }
        for (int j = 0; j < verticalSeam; j++) {
            handler.sendEmptyMessage((int) now);
            now += step;

            seamCarver.removeVerticalSeam(seamCarver.findVerticalSeam());
        }
        return seamCarver.picture();
    }
}
