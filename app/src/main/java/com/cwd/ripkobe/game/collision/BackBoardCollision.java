package com.cwd.ripkobe.game.collision;

import com.cwd.ripkobe.LogUtils;
import com.cwd.ripkobe.game.BackBoard;
import com.cwd.ripkobe.game.Ball;
import com.cwd.ripkobe.game.listener.OnCollisionResultListener;

/**
 * 篮板与篮球碰撞检测
 */
public class BackBoardCollision extends AbstractCollision {

    private BackBoard backBoard;

    public BackBoardCollision(BackBoard backBoard){
        this.backBoard = backBoard;
    }

    @Override
    public void check(Ball ball,boolean isLeftBoard) {
        //篮球圆点坐标
        int ballCenterX;
        int ballCenterY;
        //todo 暂时不计算篮板后空的部分
        if(isLeftBoard){
            ballCenterX = ball.getX() - ball.getWidth() / 2;
            ballCenterY = ball.getY() - ball.getHeight() / 2;

        }else{
            ballCenterX = ball.getX() + ball.getWidth() / 2;
            ballCenterY = ball.getY() + ball.getHeight() / 2;
            if(ballCenterX >= backBoard.getX() && ballCenterX <= backBoard.getX() + backBoard.getWidth()
                && ballCenterY <= backBoard.getY() - ball.getHeight() / 4 && ballCenterY >= backBoard.getY() - ball.getHeight() / 2){
                //触碰篮板上沿
                LogUtils.d("触碰到篮板上沿->ballCenterX:" + ballCenterX);
                if(listener != null){
                    listener.top();
                }
            }else if(ballCenterX >= backBoard.getX() && ballCenterX <= backBoard.getX() + backBoard.getWidth()
                    && ballCenterY <= backBoard.getY() + backBoard.getHeight() + ball.getHeight() / 2 && ballCenterY >= backBoard.getY() + backBoard.getHeight() - ball.getHeight() / 4){
                //触碰篮板下沿
                if(listener != null){
                    listener.bottom();
                }
            }else if(ballCenterY >= backBoard.getY() && ballCenterY <= backBoard.getY() + backBoard.getHeight()
                && ballCenterX >= backBoard.getX() - ball.getWidth() / 2 && ballCenterX <= backBoard.getX() - ball.getWidth() / 4){
                //触碰篮板正面
                LogUtils.d("触碰到篮板正面->ballCenterY:" + ballCenterY);
                if(listener != null){
                    listener.left();
                }
            }else if(Math.pow(backBoard.getX() - ballCenterX,2) + Math.pow(backBoard.getY() - ballCenterY,2) <= Math.pow(ball.getWidth() / 2.0,2)
                && ballCenterX < backBoard.getX() && ballCenterY < backBoard.getY()){
                //触碰篮板左上角
                if(listener != null){
                    listener.leftTopCorner();
                }
            }else if(Math.pow(backBoard.getX() - ballCenterX,2) + Math.pow(ballCenterY - backBoard.getY(),2) <= Math.pow(ball.getWidth() / 2.0,2)
                    && ballCenterX < backBoard.getX() && ballCenterY > backBoard.getY()){
                //触碰篮板左下角
                if(listener != null){
                    listener.leftBottomCorner();
                }
            }

        }
    }
}
