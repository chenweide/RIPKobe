package com.cwd.ripkobe.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.cwd.ripkobe.R;

public class BackBoard {

    /**
     * 这里的x坐标被加上了自身的宽度
     */
    private int x,y;
    private boolean isLeft = true;

    private Paint paint;

    private Bitmap leftBoard,rightBoard;
    private Hoop hoop;

    public BackBoard(Resources resources){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        leftBoard = BitmapFactory.decodeResource(resources, R.mipmap.backboard_left);
        rightBoard = BitmapFactory.decodeResource(resources, R.mipmap.backboard_right);
        hoop = new Hoop(resources);
    }

    public void draw(Canvas canvas){
        if(isLeft){
            canvas.drawBitmap(leftBoard,x,y,paint);
            hoop.setX(x + leftBoard.getWidth());
            hoop.setY(y + leftBoard.getHeight() - 100);
        }else {
            canvas.drawBitmap(rightBoard,x,y,paint);
            hoop.setX(x - hoop.getWidth());
            hoop.setY(y + rightBoard.getHeight() - 100);
        }
        hoop.setLeft(isLeft);
        hoop.draw(canvas);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setLeft(boolean left) {
        isLeft = left;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public int getWidth(){
        return isLeft ? (leftBoard != null ? leftBoard.getWidth() : 0) : (rightBoard != null ? rightBoard.getWidth() : 0);
    }

    public int getHeight(){
        return isLeft ? (leftBoard != null ? leftBoard.getHeight() : 0) : (rightBoard != null ? rightBoard.getHeight() : 0);
    }

    public Hoop getHoop() {
        return hoop;
    }
}
