package com.cwd.ripkobe.game.collision;

import com.cwd.ripkobe.LogUtils;
import com.cwd.ripkobe.game.Ball;
import com.cwd.ripkobe.game.Hoop;

/**
 * 篮筐与篮球触碰检测
 */
public class HoopCollision extends AbstractCollision {

    private Hoop hoop;

    public HoopCollision(Hoop hoop){
        this.hoop = hoop;
    }

    @Override
    public void check(Ball ball,boolean isLeftBoard) {
        //篮球圆点坐标
        int ballCenterX;
        int ballCenterY;
        if(isLeftBoard){
            ballCenterX = ball.getX() - ball.getWidth() / 2;
            ballCenterY = ball.getY() - ball.getHeight() / 2;

        }else{
            int hoopCircleHeight = 20;
            ballCenterX = ball.getX() + ball.getWidth() / 2;
            ballCenterY = ball.getY() + ball.getHeight() / 2;
            if(ballCenterY >= hoop.getY() && ballCenterY <= hoop.getY() + hoopCircleHeight
                    && ballCenterX >= hoop.getX() - ball.getWidth() / 2 && ballCenterX <= hoop.getX() - ball.getWidth() / 4){
                //触碰篮圈正面
                if(listener != null){
                    listener.left();
                }
            }

        }
    }
}
