package com.cwd.ripkobe.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
import com.cwd.ripkobe.game.listener.OnShootGameListener;

import java.util.Random;

public class ShootGame extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    public static final String TAG = "ShootGame";

    public static final int BALL_MAX_BOUNCE_HEIGHT = 200;

    private SurfaceHolder surfaceHolder;
    private Thread thread;
    private Canvas canvas;

    private Paint bgPaint;
    private Paint scorePaint;

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
    private int score;
    /**
     * 上一步是否得分，防止计分区域重复计分
     */
    private boolean isGoalLastStep;

    private OnShootGameListener listener;

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

    public void setOnShootGameListener(OnShootGameListener listener){
        this.listener = listener;
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
        scorePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scorePaint.setColor(Color.YELLOW);
        scorePaint.setTextSize(150);
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

        ballBounceLogic();
        scoreLogic();
    }

    /**
     * 篮球抛物线计算
     */
    private void ballBounceLogic(){

        if(ball.getStatus() == Ball.Status.BOUNCING){
            //抛物线与x轴相交两点的距离
            int distance = (int) (Math.sqrt(4f / 50f * BALL_MAX_BOUNCE_HEIGHT) / Math.abs(1f / 50f));
            //根据当前球起跳的位置平移抛物线在坐标系的位置
            int x;
            if(isLeftBoard){
                x = (ballX - (startBounceBallX - distance + distance / 2));
            }else{
                x = (ballX - (startBounceBallX - distance + distance / 2)) - distance;
            }
            //ballInitY - startBounceBallY 连续跳动需要重新计算 Y
            ballY = x * x / 50 + ballInitY - BALL_MAX_BOUNCE_HEIGHT - (ballInitY - startBounceBallY);
            Log.d(TAG,ballY+"==ballY");
            if(ballY >= ballInitY){
                ballY = ballInitY;
                ball.setStatus(Ball.Status.IDLE);
            }
            Log.d(TAG,distance+"==distance");
        }
    }

    /**
     * 得分判定
     */
    private void scoreLogic(){
        //篮板与篮筐链接部分的宽度
        int connWidth = 20;
        Hoop hoop = backBoard.getHoop();
        if(isLeftBoard){
            if(ball.getX() >= hoop.getX() + connWidth && ball.getX() + ball.getWidth() <= hoop.getX() + hoop.getWidth()
                && ball.getY() >= hoop.getY() && ball.getY() <= hoop.getY() + 30 && !isGoalLastStep){
                //进球得分
                goal();
            }
        }else{
            if(ball.getX() >= hoop.getX() && ball.getX() + ball.getWidth() <= hoop.getX() + hoop.getWidth() - connWidth
                    && ball.getY() >= hoop.getY() && ball.getY() <= hoop.getY() + 30 && !isGoalLastStep){
                //进球得分
                goal();
            }
        }
    }

    private void goal(){
        Log.d(TAG,"score--");
        score++;
        isGoalLastStep = true;
        if(listener != null){
            listener.onGoal();
        }
//        resetBackBoard();
    }

    /**
     * 得分后重置篮板高度（随机），改变方向
     */
    private void resetBackBoard(){
        //todo 待适配
        isLeftBoard = !isLeftBoard;
        boardY = new Random().nextInt(1000) + 500;
        if(isLeftBoard){
            boardX = 0;
        }else {
            boardX = getWidth();
        }
    }

    private void draw(){
        try{
            canvas = surfaceHolder.lockCanvas();
            if(canvas != null){
                clearDraw();
                drawBg();
                drawBall();
                drawBackboard();
                drawScore();
            }
        }catch (Exception e){

        }finally {
            if(canvas != null){
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void drawScore(){
        canvas.drawText(String.valueOf(score),200,280,scorePaint);
    }

    private void drawBg(){
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

//                isLeftBoard = false;

                if(listener != null){
                    listener.onClick();
                }
                startBounceBallX = ballX;
                startBounceBallY = ballY;
                ball.setStatus(Ball.Status.BOUNCING);
                //每次点击重置
                isGoalLastStep = false;
                break;

            default:
                break;
        }
        return true;
    }


}
