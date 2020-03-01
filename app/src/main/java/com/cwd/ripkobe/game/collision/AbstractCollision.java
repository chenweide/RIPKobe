package com.cwd.ripkobe.game.collision;

import com.cwd.ripkobe.game.Ball;
import com.cwd.ripkobe.game.listener.OnCollisionResultListener;

/**
 * 与篮球的碰撞检测
 */
public abstract class AbstractCollision {

    public OnCollisionResultListener listener;

    public abstract void check(Ball ball,boolean isLeftBoard);

    public void setOnCollisionResultListener(OnCollisionResultListener listener){
        this.listener = listener;
    }

}
