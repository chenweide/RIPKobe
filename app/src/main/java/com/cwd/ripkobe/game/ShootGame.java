package com.cwd.ripkobe.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.cwd.ripkobe.R;

public class ShootGame extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    public static final String TAG = "ShootGame";

    public static final int BALL_MAX_BOUNCE_HEIGHT = 200;
    //每次弹跳之前球的高度
    private int beforeBounceHeight;
    private boolean isBouncing;

    private SurfaceHolder surfaceHolder;
    private Thread thread;
    private Canvas canvas;

    private Paint bgPaint;

    private boolean isRunning = false;

    private boolean isLeftBoard = true;
    //篮板坐标
    private int boardX,boardY;
    private int ballX,ballY;
    private int ballInitY;
    //开始起跳球x坐标
    private int startBounceBallX,startBounceBallY;

    private Bitmap bg;
    private Ball ball;
    private BackBoard backBoard;
    private int ballDegrees;

    public ShootGame(Context context) {
        this(context,null);
    }

    public ShootGame(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShootGame(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
        initPaint();
    }

    private void initPaint(){
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
        boardX = 0;
        boardY = getHeight() / 2;

        if(isLeftBoard){
            ballX = getWidth();
        }else{
            ballX = -ball.getWidth();
        }
        ballInitY = getHeight() - 300;
        ballY = ballInitY;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning){
            long start = System.currentTimeMillis();
            draw();
            logic();
            long end = System.currentTimeMillis();
            Log.d(TAG,"time-->" + (end - start));
            try {
                if (end - start < 5) {
                    Thread.sleep(5 - (end - start));
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void logic(){
        ballDegrees = (ballDegrees + 5) % 360;
        if(isLeftBoard){
            ballX -= 6;
            if(ballX < -ball.getWidth()){
                ballX = getWidth();
            }
        }else{
            ballX += 6;
            if(ballX > getWidth()){
                ballX = -ball.getWidth();
            }
        }

        ballLogic();
    }

    private void ballLogic(){

        if(ball.getStatus() == Ball.Status.UP){
            //抛物线与x轴相交两点的距离
            int distance = (int) (Math.sqrt(4f / 50f * BALL_MAX_BOUNCE_HEIGHT) / Math.abs(1f / 50f));
            //根据当前球起跳的位置平移抛物线在坐标系的位置
            int x = (ballX - (startBounceBallX - distance + distance / 2));
            ballY = x * x / 50 + ballInitY - BALL_MAX_BOUNCE_HEIGHT - (ballInitY - startBounceBallY);
            Log.d(TAG,ballY+"==ballY");
            if(ballY > ballInitY){
                ballY = ballInitY;
            }
            Log.d(TAG,distance+"==distance");
        }
//        if(beforeBounceHeight - ballY <= BALL_MAX_BOUNCE_HEIGHT && ball.getStatus() == Ball.Status.UP){
//            ballY -= 10;
//        }else{
//            if((ballY <= ballInitY) && (ball.getStatus() == Ball.Status.UP || ball.getStatus() == Ball.Status.DOWN)){
//                ball.setStatus(Ball.Status.DOWN);
//                ballY += 10;
//            }else{
//                ball.setStatus(Ball.Status.IDLE);
//            }
//        }
    }

    private void draw(){
        try{
            canvas = surfaceHolder.lockCanvas();
            if(canvas != null){
                clearDraw();
                drawBg(canvas);
                drawBackboard();
                drawBall();
            }
        }catch (Exception e){

        }finally {
            if(canvas != null){
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }


    }

    private void drawBg(Canvas canvas){
        if(bg == null){
            bg = BitmapFactory.decodeResource(getResources(), R.mipmap.kobe);
        }
        float scale = (float) getWidth() / bg.getWidth();
        Matrix matrix = new Matrix();
        float s = 0.9f;
        matrix.postScale(s,s);
        matrix.postTranslate(0,0);
        canvas.drawBitmap(bg,matrix,bgPaint);
    }

    private void drawBackboard(){
        if(backBoard == null){
            backBoard = new BackBoard(getResources());
        }
        backBoard.setLeft(isLeftBoard);
        backBoard.setX(boardX);
        backBoard.setY(boardY);
        backBoard.draw(canvas);
    }

    private void drawBall(){
        if(ball == null){
            ball = new Ball(getResources());
        }
        ball.draw(canvas);
        ball.setRotate(ballDegrees);
        ball.setLToR(!isLeftBoard);
        ball.setX(ballX);
        ball.setY(ballY);
    }

    private void clearDraw(){
        Paint p = new Paint();
        //清屏
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(p);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

//                isLeftBoard = !isLeftBoard;
//                if(isLeftBoard){
//                    boardX = 0;
//                    boardY = getHeight() / 2;
//                }else {
//                    boardX = getWidth();
//                    boardY = getHeight() / 2;
//                }

                beforeBounceHeight = ballY;
                startBounceBallX = ballX;
                startBounceBallY = ballY;
                ball.setStatus(Ball.Status.UP);
                break;

            default:
                break;
        }
        return true;
    }


}
