package com.example.littlemap;

/**
 * Created by Administrator on 2017/11/3.
 */
public class PointUtils {

    /**这里要乘以2*/
    public static float MAX_RADIUS = 1;

    /**
     * 设置缩放比
     * @param width
     * @param R
     */
    public void setZoom(float width, float R){
        MAX_RADIUS = width / R;
    }

    /**
     * 算出两点间距离
     * @param myX
     * @param myY
     * @param circleX
     * @param circleY
     * @return
     */
    public double getPointsDistance(double myX, double myY, double circleX, double circleY){
        double distance;
        distance = Math.sqrt(Math.pow(circleX - myX, 2) + Math.pow(circleY - myY, 2));
        distance = zoom(distance);
        System.out.println("huehn PointUtils distance : " + distance);
        return distance;
    }

    /**
     * 获得两点斜率
     * @param myX
     * @param myY
     * @param circleX
     * @param circleY
     * @return
     */
    public double getK(double myX, double myY, double circleX, double circleY){
        double k;
        k = (circleY - myY) / (circleX - myX);
        System.out.println("huehn PointUtils K : " + k);
        return k;
    }

    /**
     * 获取圆心缩放后的坐标
     * @param K 人坐标和圈圆心的斜率
     * @param distance 在小地图换算后的人和圈圆心的距离
     * 由(circleY - myY) / (circleX - myX) = k;  (circleY - myY)^2 + (circleX - myX)^2 = distance ^ 2;
     * 得(1 + k^2) (circleX ^ 2 - myX * circleX ) ^ 2 = distance ^ 2得到circleX
     * @return
     */
    public double[] getZoomCircleXY(double K, double distance, double myX, double myY, double realMyX, double realMyY,
                                    double realCircleX, double realCircleY){
        double[] zoomCircleXY = new double[2];
        if (realCircleX >= realMyX){
            /**同一斜率下，存在左边右边，这种情况在右边*/
            zoomCircleXY[0] = myX + Math.sqrt(Math.pow(distance, 2) / (1 + Math.pow(K, 2)));
        }else {
            /**同一斜率下，存在左边右边，这种情况在左边*/
            zoomCircleXY[0] = myX - Math.sqrt(Math.pow(distance, 2) / (1 + Math.pow(K, 2)));
        }

        if (realCircleY >= realMyY){
            /**同一斜率下，存在上边下边，这种情况在上边*/
            zoomCircleXY[1] = myY + Math.sqrt(Math.pow(distance, 2) * K / (1+K));
        }else {
            zoomCircleXY[1] = myY - Math.sqrt(Math.pow(distance, 2) * K / (1+K));
        }
        return zoomCircleXY;
    }


    /**
     * 把长度缩放
     * @param a
     * @return
     */
    public double zoom(double a){
        return a / MAX_RADIUS;
    }

}
