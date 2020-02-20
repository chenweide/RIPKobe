package com.cwd.ripkobe.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.cwd.ripkobe.R;

public class Ball implements Runnable {

    private Bitmap ball;
    private int x;
    private int y;
    private int degrees;
    private Status status;

    enum Status{
        UP,DOWN,IDLE;
    }

    private boolean isLToR = false;

    private Paint paint;

    private boolean isRunning = true;

    public Ball(Resources resources){
        x = 50;
        y = 50;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        new Thread(this).start();

        ball = BitmapFactory.decodeResource(resources, R.mipmap.ball);
    }

    @Override
    public void run() {
        while (isRunning){
//            long start = System.currentTimeMillis();
//            logic();
//            long end = System.currentTimeMillis();
//            try {
//                if (end - start < 1) {
//                    Thread.sleep(1 - (end - start));
//                }
//            }catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

    public void setRotate(int degrees){
        //篮球旋转
        this.degrees = degrees;
    }

    public void draw(Canvas canvas){
        Matrix matrix = new Matrix();
//        matrix.postTranslate(100,100);
        matrix.postRotate(isLToR ? degrees : - degrees,(float) ball.getWidth() / 2,(float) ball.getHeight() / 2);
//        matrix.postScale(0.08f,0.08f,0f,0f);
        matrix.postTranslate(x,y);
        canvas.drawBitmap(ball,matrix,paint);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isLToR() {
        return isLToR;
    }

    public void setLToR(boolean LToR) {
        isLToR = LToR;
    }

    public int getWidth() {
        return ball != null ? ball.getWidth() : 0;
    }

    public int getHeight() {
        return ball != null ? ball.getHeight() : 0;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
