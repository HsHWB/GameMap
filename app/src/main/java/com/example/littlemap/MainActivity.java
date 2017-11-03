package com.example.littlemap;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public final static String id = "12";
    private RadarView radarView;
    private Button peopleChange;
    private Button circleChange;
    private Button circleLocationChange;
    private Button circleRadiusChange;
    private CircleView circleView;
    private static WindowManager wm;
    private View mFrameView = null;
    private WindowManager.LayoutParams params = null;

    private int RANDOM = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radarView = new RadarView(MainActivity.this);
        if (mFrameView != null) {
            return;
        }
        this.mFrameView = LayoutInflater.from(MainActivity.this).inflate(R.layout.float_rader_layer, null);
        RelativeLayout main_layer = (RelativeLayout) this.mFrameView.findViewById(R.id.radar_layout);
        main_layer.addView(radarView);

        wm = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE));
        params = new WindowManager.LayoutParams();

        // 设置window type
        params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
        params.gravity = Gravity.CENTER;
        // 设置Window flag
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        params.width = Toolkits.dip2px(this, radarView.VIEW_WIDTH);
        params.height = Toolkits.dip2px(this, radarView.VIEW_HEIGHT);


        final Display display = wm.getDefaultDisplay();

        params.x = display.getWidth() - params.width;
        params.y = -Toolkits.dip2px(this, 50);
        wm.addView(mFrameView, params);

        radarView.setOnChangeSizeListener(new RadarView.changeSizeListener() {
                                              @Override
                                              public void onClick(int w, int h) {
                                                  params.width = Toolkits.dip2px(MainActivity.this, w);
                                                  params.height = Toolkits.dip2px(MainActivity.this, h);
                                                  params.x = display.getWidth() - params.width;
                                                  //params.y = params.y + y;
                                                  wm.updateViewLayout(mFrameView, params);
                                              }
                                          });
        initView();

//        circleView = new CircleView(MainActivity.this);
//        if (mFrameView != null) {
//            return;
//        }
//        this.mFrameView = LayoutInflater.from(MainActivity.this).inflate(R.layout.float_rader_layer, null);
//        RelativeLayout main_layer = (RelativeLayout) this.mFrameView.findViewById(R.id.radar_layout);
//        main_layer.addView(circleView);
//
//        wm = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE));
//        params = new WindowManager.LayoutParams();
//
//        // 设置window type
//        params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
//        params.gravity = Gravity.CENTER;
//        // 设置Window flag
//        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//
//        params.width = Toolkits.dip2px(this, circleView.VIEW_WIDTH);
//        params.height = Toolkits.dip2px(this, circleView.VIEW_HEIGHT);
//
//
//        final Display display = wm.getDefaultDisplay();
//
//        params.x = display.getWidth() - params.width;
//        params.y = -Toolkits.dip2px(this, 50);
//        wm.addView(mFrameView, params);
//
//        initView();
    }

    private void initView(){
        peopleChange = (Button) findViewById(R.id.mm_radar_people);
        circleChange = (Button) findViewById(R.id.mm_radar_circle);

//        circleLocationChange = (Button) findViewById(R.id.circle_change_location);
//        circleRadiusChange = (Button) findViewById(R.id.circle_add_radius);


        peopleChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radarView.setList(0);
            }
        });

        circleChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radarView.setList(1);
            }
        });

//        circleLocationChange.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Random random = new Random();
//                int x = random.nextInt(RANDOM);
//                int y = random.nextInt(RANDOM);
//                circleView.setPoint(x, y, 20);
//                System.out.println("huehn circle random x : " + x + "    y : " + y);
//            }
//        });
//        circleRadiusChange.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Random random = new Random();
//                int radius = random.nextInt(RANDOM);
//                circleView.setPoint(200, 200, radius);
//                System.out.println("huehn circle random radius : " + radius);
//            }
//        });
    }
}
