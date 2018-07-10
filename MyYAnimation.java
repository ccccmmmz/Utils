package com.airsaid.mpermissionutils;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

/**
 * Created by ligen
 * date:2018/7/10
 * 描述:
 */
public class MyYAnimation extends Animation {
    //中心点
    private int centerX, centerY;
    private Camera camera = new Camera();

    //默认为反转360度
    private float Degress = 360;

    //默认周期为3秒
    long time = 3 * 1000;

    MyYAnimation() {}

    /**
     * 翻转角度
     */
    MyYAnimation(int degress) {
        this.Degress = degress;
    }

    /**
     * 翻转角度以及时间设置

     */
    MyYAnimation(int degress, long time) {
        this(degress);
        if (time != 0)
            this.time = time;
    }

    /**
     * 获取坐标，定义动画时间
     *
     */
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        //获得中心点坐标
        centerX = width / 2;
        centerY = width / 2;
        //动画执行时间 自行定义
        setDuration(time);
        setInterpolator(new DecelerateInterpolator());
    }


    /**
     * 旋转的角度设置
     */
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final Matrix matrix = t.getMatrix();
        //保存现场用于restore恢复
//        camera.save();
        //中心是Y轴旋转，这里可以自行设置X轴 Y轴 Z轴
        camera.rotateY(Degress * interpolatedTime);
        //把我们的摄像头加在变换矩阵上
        camera.getMatrix(matrix);
        //设置翻转中心点
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
        //camera位置复位
//        camera.restore();
    }

}
