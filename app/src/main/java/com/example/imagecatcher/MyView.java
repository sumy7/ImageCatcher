package com.example.imagecatcher;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View {

    public final static int PAINT_ERASE = Color.WHITE;
    public final static int PAINT_PROTECT = Color.RED;
    public final static int PAINT_EXPOSE = Color.BLUE;

    private Paint paint;
    private Canvas paper;
    private Bitmap bitmap;
    private Bitmap background;

    private Paint touming;
    private Paint textPaint;

    private float preX;
    private float preY;

    private int paintstatic;

    public MyView(Context context) {
        super(context);
        paint = new Paint();
        paint.setStrokeWidth(50);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(PAINT_PROTECT);
        paintstatic = PAINT_PROTECT;

        touming = new Paint();
        touming.setAlpha(100);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50);

        ScreenInfo screenInfo = new ScreenInfo((Activity) context);
        bitmap = Bitmap.createBitmap(screenInfo.getWidthPixels(),
                screenInfo.getHeightPixels(), Config.ARGB_8888);

        paper = new Canvas(bitmap);

        background = bitmap;
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setStrokeWidth(50);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(PAINT_PROTECT);
        paintstatic = PAINT_PROTECT;

        touming = new Paint();
        touming.setAlpha(100);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50);

        ScreenInfo screenInfo = new ScreenInfo((Activity) context);
        bitmap = Bitmap.createBitmap(screenInfo.getWidthPixels(),
                screenInfo.getHeightPixels(), Config.ARGB_8888);

        paper = new Canvas(bitmap);

        background = bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(bitmap, 0, 0, touming);

        if (paintstatic == PAINT_PROTECT) {
            canvas.drawText("PROTECT", 50, 50, textPaint);
        } else if (paintstatic == PAINT_EXPOSE) {
            canvas.drawText("EXPOSE", 50, 50, textPaint);
        } else {
            canvas.drawText("ERASE", 50, 50, textPaint);
        }
        // super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Log.i("hehe", "X" + event.getX());
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            preX = event.getX();
            preY = event.getY();
            invalidate();
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float x = event.getX();
            float y = event.getY();
            this.paper.drawLine(preX, preY, x, y, this.paint);
            preX = x;
            preY = y;
            invalidate();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
            this.paper.drawLine(preX, preY, x, y, this.paint);
            invalidate();
        }
        return true;
    }

    public Bitmap getViewBitmap() {
        return bitmap;
    }

    public void setPaintKind(int kind) {
        if (kind == PAINT_ERASE) {
            paint.setColor(PAINT_ERASE);
            paintstatic = PAINT_ERASE;
        } else if (kind == PAINT_PROTECT) {
            paint.setColor(PAINT_PROTECT);
            paintstatic = PAINT_PROTECT;
        } else if (kind == PAINT_EXPOSE) {
            paint.setColor(PAINT_EXPOSE);
            paintstatic = PAINT_EXPOSE;
        }
    }

    public void setBackground(Bitmap back) {
        background = back;
        if (bitmap != null)
            bitmap.recycle();
        bitmap = Bitmap.createBitmap(background.getWidth(),
                background.getHeight(), Config.ARGB_8888);
        bitmap.eraseColor(PAINT_ERASE);
        paper = new Canvas(bitmap);
    }
}
