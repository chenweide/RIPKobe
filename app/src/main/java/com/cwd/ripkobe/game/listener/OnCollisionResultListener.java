package com.cwd.ripkobe.game.listener;

public interface OnCollisionResultListener {

    /**
     * 触碰矩形上沿
     */
    void top();

    /**
     * 触碰矩形下沿
     */
    void bottom();

    /**
     * 触碰矩形左侧
     */
    void left();

    /**
     * 触碰矩形左上角
     */
    void leftTopCorner();

    /**
     * 触碰矩形左下角
     */
    void leftBottomCorner();

}
