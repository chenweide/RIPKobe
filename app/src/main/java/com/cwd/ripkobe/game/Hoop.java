package com.cwd.ripkobe.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.cwd.ripkobe.R;

public class Hoop {

    private Bitmap leftHoop,rightHoop;
    private int x,y;
    private boolean isLeft;
    private Paint paint;

    public Hoop(Resources resources){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if(leftHoop == null){
            leftHoop = BitmapFactory.decodeResource(resources, R.mipmap.left_hoop);
        }
        if(rightHoop == null){
            rightHoop = BitmapFactory.decodeResource(resources,R.mipmap.right_hoop);
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(isLeft ? leftHoop : rightHoop,x,y,paint);
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

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean left) {
        isLeft = left;
    }

    public int getWidth(){
        if(leftHoop != null){
            return leftHoop.getWidth();
        }
        return 0;
    }

    public int getHeight(){
        if(leftHoop != null){
            return leftHoop.getHeight();
        }
        return 0;
    }
}
