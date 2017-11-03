package com.example.littlemap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/10/30.
 */
public class CircleView extends View {

    private Paint paint;
    private Context context;
    private int screenWidth;
    private int screenHeight;
    private int drawX = 100;
    private int drawY = 100;
    private int radius = 20;
//    public final int VIEW_WIDTH = 154;
//    public final int VIEW_HEIGHT = 180;


    public CircleView(Context context) {

            // TODO Auto-generated constructor stub
            this(context, null);
        }

        public CircleView(Context context, AttributeSet attrs) {
            super(context, attrs);
            // TODO Auto-generated constructor stub
            this.context = context;
            this.paint = new Paint();
            this.paint.setAntiAlias(true); //消除锯齿
            this.paint.setStyle(Paint.Style.STROKE); //绘制空心圆
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            screenWidth = display.getWidth();
            screenHeight = display.getHeight();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            this.paint.setColor(context.getResources().getColor(R.color.mm_vip_free_play));
            this.paint.setStrokeWidth(5);
            canvas.drawCircle(drawX, drawY, radius, this.paint);
            System.out.println("huehn screanWidth : " + screenWidth / 2 + "    screenHeight : " + screenHeight / 2);
            super.onDraw(canvas);
        }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    public void setPoint(int x, int y, int radius){
        this.drawX = x;
        this.drawY = y;
        this.radius = dip2px(context, radius);
        this.invalidate();
    }

    /**
         * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
         */
        public static int dip2px(Context context, float dpValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }

}
