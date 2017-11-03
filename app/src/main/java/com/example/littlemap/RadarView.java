package com.example.littlemap;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by linxiaoqiang on 2016/6/29 0029.
 * 小地图
 */
public class RadarView extends FrameLayout {
    private List<PlayerLocationInfo> players = new ArrayList<>();
    private List<PlayerLocationInfo> dataList = new ArrayList<>();
    private Context context = null;
    private FrameLayout mainLayer = null;
    private boolean isActive = false;
    private PlayerLocationInfo myPoint = null;
    private final int MY_IMG_SIZE = 70;
    private final int OTHER_IMG_SIZE = 35;//普通玩家图片大小
    private int maxRateLen = 0;
    private final static int minLength = 20;
    private static int increaseLength = 50;
    private TextView scaleTextview = null;
    private TextView myPointTextview = null;
    private ImageView openViewBtn = null;
    private CircleView circleView;
    private RelativeLayout rootView = null;
    private changeSizeListener listener = null;
    public final int VIEW_WIDTH = 154;
    public final int VIEW_HEIGHT = 180;
    private final int VIEW_OPEN_BTN_WIDTH = 40;
    private final int VIEW_OPEN_BTN_HEIGHT = 40;

    private static int maxPeople = 2;
    private static int randomSize = 100;
    private static int increanLength = 10;

    public RadarView(Context context) {
        super(context);
        this.context = context;
        inti();
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inti();
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        inti();
    }

    public void setOnChangeSizeListener(changeSizeListener listener) {
        this.listener = listener;
    }

    public interface changeSizeListener {
        void onClick(int w, int h);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RadarView(Activity context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        inti();
    }

    public void destory() {
        isActive = false;
        if (players != null) {
            players.clear();
            players = null;
        }
    }

    private void inti() {
        myPoint = new PlayerLocationInfo();
        if (dataList.size() == 0){
            for (int i = 0; i < maxPeople; i++){
//                Random rand = new Random();
//                int x = rand.nextInt(randomSize);
//                int y = rand.nextInt(randomSize);
//                int z = rand.nextInt(randomSize);
//                dataList.get(i).setData(x, y, z, 1);
//                dataList.get(i).setRealData(x, y, z);
                int x = 0;
                int y = 0;
                int z = 0;
                PlayerLocationInfo playerLocationInfo = new PlayerLocationInfo();
                if (i == 0){
                    playerLocationInfo.clientId = MainActivity.id;
                }else {
                    playerLocationInfo.clientId = "13";
                }

                playerLocationInfo.setData(x, y, z, 1);
                playerLocationInfo.setRealData(x, y, z);
                dataList.add(playerLocationInfo);
                System.out.println("huehn init x : " + x + "   y : " + y + "   z : " + z + "   id : " + playerLocationInfo.clientId);
            }
        }
        addView();
    }

    private void statUpdateUiThread() {
        isActive = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isActive) {
                    try {
                        handler.sendEmptyMessage(0);
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void addView() {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.rader_view, null);
        addView(view, new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        rootView = (RelativeLayout) view.findViewById(R.id.root_view);
        openViewBtn = (ImageView) view.findViewById(R.id.open_view_btn);
        myPointTextview = (TextView) view.findViewById(R.id.my_point_textview);
        scaleTextview = (TextView) view.findViewById(R.id.scale_textview);
        mainLayer = (FrameLayout) view.findViewById(R.id.main_layer);
        mainLayer.setRotation(180);
//        circleView = (CircleView) view.findViewById(R.id.circle_view);
        circleView = new CircleView(context);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) circleView.getLayoutParams();
        lp.leftMargin = 0;
        lp.topMargin = 0;
        lp.width = OTHER_IMG_SIZE;
        lp.height = OTHER_IMG_SIZE;
        mainLayer.addView(circleView, lp);
        for (int i = 0; i < maxPeople - 1; i++) {
            LayoutParams lp1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            ImageView item = new ImageView(context);
            item.setVisibility(View.GONE);
            lp1.leftMargin = 0;
            lp1.topMargin = 0;
            lp1.width = OTHER_IMG_SIZE;
            lp1.height = OTHER_IMG_SIZE;
            mainLayer.addView(item, lp1);
        }
    }

    public void doViewClickEvent() {
//        ViewUtil.hideSystemUI(rootView);
//        ViewUtil.hideSystemUI(context.getWindow().getDecorView());
        if (openViewBtn.getVisibility() == View.VISIBLE) {
            openViewBtn.setVisibility(View.GONE);
            rootView.setVisibility(View.VISIBLE);
            statUpdateUiThread();
            if (listener != null) {
                listener.onClick(VIEW_WIDTH, VIEW_HEIGHT);
            }
        } else {
            openViewBtn.setVisibility(View.VISIBLE);
            rootView.setVisibility(View.GONE);
            stop();
            if (listener != null) {
                listener.onClick(VIEW_OPEN_BTN_WIDTH, VIEW_OPEN_BTN_HEIGHT);
            }
        }
    }
    public boolean isSmallBtnOpen() {
        return openViewBtn.getVisibility() == View.VISIBLE;
    }

    public void openSmallBtn() {
        openViewBtn.setVisibility(View.VISIBLE);
        rootView.setVisibility(View.GONE);
        stop();
        if (listener != null) {
            listener.onClick(VIEW_OPEN_BTN_WIDTH, VIEW_OPEN_BTN_HEIGHT);
        }
    }

    /**
     * 模拟数据
     * @param type 圈的位置变还是人的位置变
     */
    public void setList(int type){
//        /**随机*/
//        if (type == 0){
//            /**人变*/
//            for (int i = 0; i < maxPeople; i++){
//                if (dataList.get(i).clientId.equals(MainActivity.id)) {
//                    Random rand = new Random();
//                    int x = rand.nextInt(randomSize);
//                    int y = rand.nextInt(randomSize);
//                    int z = rand.nextInt(randomSize);
//                    dataList.get(i).setData(x, y, z, 1);
//        dataList.get(i).setRealData(x, y, z);
//                    System.out.println("huehn scale change people x : " + x + "   y : " + y + "   z : " + z + "   id : " + dataList.get(i).clientId);
//                }else {
//                    System.out.println("huehn scale change people my x : " + dataList.get(1).position.x + "   y : " + dataList.get(1).position.y + "   z : " + dataList.get(1).position.z + "   id : " + dataList.get(i).clientId);
//                }
//            }
//        }else if (type == 1){
//            /**圈变*/
//            for (int i = 0; i < maxPeople; i++){
//                if (!dataList.get(i).clientId.equals(MainActivity.id)) {
//                    Random rand = new Random();
//                    int x = rand.nextInt(randomSize);
//                    int y = rand.nextInt(randomSize);
//                    int z = rand.nextInt(randomSize);
//                    dataList.get(i).setData(x, y, z, 1);
//                    dataList.get(i).setRealData(x, y, z);
//                    System.out.println("huehn scale change circle x : " + x + "   y : " + y + "   z : " + z + "   id : " + dataList.get(i).clientId);
//                }
//            }
//        }

        /**递增X*/
        if (type == 0){
            /**人变*/
            for (int i = 0; i < maxPeople; i++){
                if (dataList.get(i).clientId.equals(MainActivity.id)) {
                    int x = dataList.get(i).position.x + increanLength;
                    int y = dataList.get(i).position.y;
                    int z = dataList.get(i).position.z;
                    dataList.get(i).setData(x, y, z, 1);
                    dataList.get(i).setRealData(x, y, z);
                    System.out.println("huehn scale change people x : " + x + "   y : " + y + "   z : " + z + "   id : " + dataList.get(i).clientId);
                }else {
                    System.out.println("huehn scale change people my x : " + dataList.get(1).position.x + "   y : " + dataList.get(1).position.y + "   z : " + dataList.get(1).position.z + "   id : " + dataList.get(i).clientId);
                }
            }
        }else if (type == 1){
            /**圈变*/
            for (int i = 0; i < maxPeople; i++){
                if (!dataList.get(i).clientId.equals(MainActivity.id)) {
                    int x = dataList.get(i).position.x + increanLength;
                    int y = dataList.get(i).position.y;
                    int z = dataList.get(i).position.x;
                    dataList.get(i).setData(x, y, z, 1);
                    dataList.get(i).setRealData(x, y, z);
                    System.out.println("huehn scale change circle x : " + x + "   y : " + y + "   z : " + z + "   id : " + dataList.get(i).clientId);
                }
            }
        }
//
//        /**递增Y*/
//        if (type == 0){
//            /**人变*/
//            for (int i = 0; i < maxPeople; i++){
//                if (dataList.get(i).clientId.equals(MainActivity.id)) {
//                    int x = dataList.get(i).position.x;
//                    int y = dataList.get(i).position.y + increanLength;
//                    int z = dataList.get(i).position.x;
//                    dataList.get(i).setData(x, y, z, 1);
//        dataList.get(i).setRealData(x, y, z);
//                    System.out.println("huehn scale change people x : " + x + "   y : " + y + "   z : " + z + "   id : " + dataList.get(i).clientId);
//                }else {
//                    System.out.println("huehn scale change people my x : " + dataList.get(1).position.x + "   y : " + dataList.get(1).position.y + "   z : " + dataList.get(1).position.z + "   id : " + dataList.get(i).clientId);
//                }
//            }
//        }else if (type == 1){
//            /**圈变*/
//            for (int i = 0; i < maxPeople; i++){
//                if (!dataList.get(i).clientId.equals(MainActivity.id)) {
//                    int x = dataList.get(i).position.x;
//                    int y = dataList.get(i).position.y + increanLength;
//                    int z = dataList.get(i).position.x;
//                    dataList.get(i).setData(x, y, z, 1);
//        dataList.get(i).setRealData(x, y, z);
//                    System.out.println("huehn scale change circle x : " + x + "   y : " + y + "   z : " + z + "   id : " + dataList.get(i).clientId);
//                }
//            }
//        }


//        /**递减X*/
//        if (type == 0){
//            /**人变*/
//            for (int i = 0; i < maxPeople; i++){
//                if (dataList.get(i).clientId.equals(MainActivity.id)) {
//                    int x = dataList.get(i).position.x - increanLength;
//                    int y = dataList.get(i).position.y;
//                    int z = dataList.get(i).position.x;
//                    dataList.get(i).setData(x, y, z, 1);
//        dataList.get(i).setRealData(x, y, z);
//                    System.out.println("huehn scale change people x : " + x + "   y : " + y + "   z : " + z + "   id : " + dataList.get(i).clientId);
//                }else {
//                    System.out.println("huehn scale change people my x : " + dataList.get(1).position.x + "   y : " + dataList.get(1).position.y + "   z : " + dataList.get(1).position.z + "   id : " + dataList.get(i).clientId);
//                }
//            }
//        }else if (type == 1){
//            /**圈变*/
//            for (int i = 0; i < maxPeople; i++){
//                if (!dataList.get(i).clientId.equals(MainActivity.id)) {
//                    int x = dataList.get(i).position.x - increanLength;
//                    int y = dataList.get(i).position.y;
//                    int z = dataList.get(i).position.x;
//                    dataList.get(i).setData(x, y, z, 1);
//        dataList.get(i).setRealData(x, y, z);
//                    System.out.println("huehn scale change circle x : " + x + "   y : " + y + "   z : " + z + "   id : " + dataList.get(i).clientId);
//                }
//            }
//        }

//        /**递减Y*/
//        if (type == 0){
//            /**人变*/
//            for (int i = 0; i < maxPeople; i++){
//                if (dataList.get(i).clientId.equals(MainActivity.id)) {
//                    int x = dataList.get(i).position.x;
//                    int y = dataList.get(i).position.y - increanLength;
//                    int z = dataList.get(i).position.x;
//                    dataList.get(i).setData(x, y, z, 1);
//        dataList.get(i).setRealData(x, y, z);
//                    System.out.println("huehn scale change people x : " + x + "   y : " + y + "   z : " + z + "   id : " + dataList.get(i).clientId);
//                }else {
//                    System.out.println("huehn scale change people my x : " + dataList.get(1).position.x + "   y : " + dataList.get(1).position.y + "   z : " + dataList.get(1).position.z + "   id : " + dataList.get(i).clientId);
//                }
//            }
//        }else if (type == 1){
//            /**圈变*/
//            for (int i = 0; i < maxPeople; i++){
//                if (!dataList.get(i).clientId.equals(MainActivity.id)) {
//                    int x = dataList.get(i).position.x;
//                    int y = dataList.get(i).position.y - increanLength;
//                    int z = dataList.get(i).position.x;
//                    dataList.get(i).setData(x, y, z, 1);
//        dataList.get(i).setRealData(x, y, z);
//                    System.out.println("huehn scale change circle x : " + x + "   y : " + y + "   z : " + z + "   id : " + dataList.get(i).clientId);
//                }
//            }
//        }
        refreshUi();

    }

    private void refreshUi() {
        players.clear();
//        List<PlayerLocationInfo> playerPoints = PlayerLocationMgr.getInstance().getPlayerLocationList();
        List<PlayerLocationInfo> playerPoints = new ArrayList<>();
        playerPoints.addAll(dataList);

        System.out.println("huehn map playerPoints.size() == " + playerPoints.size());
        for (int i = 0; i < playerPoints.size(); i++) {
            PlayerLocationInfo point = new PlayerLocationInfo();
            point.updateLocation(playerPoints.get(i));
            point.clientId = playerPoints.get(i).clientId;
            players.add(point);
        }
        if (players == null || players.size() == 0) {
            return;
        }
        if (mainLayer == null || mainLayer.getChildCount() == 0) {
            return;
        }
        getLocalInViewPoint();
        for (int i = 0; i < mainLayer.getChildCount(); i++) {
//            ImageView item = (ImageView) mainLayer.getChildAt(i);
//            item.setVisibility(View.GONE);
            mainLayer.getChildAt(i).setVisibility(View.GONE);
        }

        for (int i = 0; i < players.size(); i++) {
            if (mainLayer.getChildAt(i) != null) {
                if (mainLayer.getChildAt(i) instanceof CircleView){
                    if (!players.get(i).clientId.equals(MainActivity.id)){
                    RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) circleView.getLayoutParams();
                    lp1.leftMargin = players.get(i).position.x;
                    lp1.topMargin = players.get(i).position.y;
                    circleView.setRotation(players.get(i).yaw);
                    circleView.setPoint(players.get(i).position.x, players.get(i).position.y, 30);
                    }
                }
                if (mainLayer.getChildAt(i) instanceof ImageView){
                    ImageView item = (ImageView) mainLayer.getChildAt(i);
                    item.setVisibility(View.VISIBLE);
                    LayoutParams lp = (LayoutParams) item.getLayoutParams();
                    lp.leftMargin = players.get(i).position.x;
                    lp.topMargin = players.get(i).position.y;
                    item.setLayoutParams(lp);
                    item.setRotation(players.get(i).yaw);
                    item.setBackgroundDrawable(getResources().getDrawable(R.drawable.myself_arrow));
                    lp.width = MY_IMG_SIZE;
                    lp.height = MY_IMG_SIZE;
                }
//                if (players.get(i).clientId.equals(MainActivity.id)) {
//                    ImageView item = (ImageView) mainLayer.getChildAt(i);
//                    item.setVisibility(View.VISIBLE);
//                    LayoutParams lp = (LayoutParams) item.getLayoutParams();
//                    lp.leftMargin = players.get(i).position.x;
//                    lp.topMargin = players.get(i).position.y;
//                    item.setLayoutParams(lp);
//                    item.setRotation(players.get(i).yaw);
//                    item.setBackgroundDrawable(getResources().getDrawable(R.drawable.myself_arrow));
//                    lp.width = MY_IMG_SIZE;
//                    lp.height = MY_IMG_SIZE;
//                }
//                else {
////                    item.setBackgroundDrawable(getResources().getDrawable(R.drawable.other_arrow));
//                    RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) circleView.getLayoutParams();
//                    lp1.leftMargin = players.get(i).position.x;
//                    lp1.topMargin = players.get(i).position.y;
////                    circleView.setLayoutParams(lp1);
//                    circleView.setRotation(players.get(i).yaw);
//                    circleView.setPoint(players.get(i).position.x, players.get(i).position.y, 30);
////                    lp1.width = OTHER_IMG_SIZE;
////                    lp1.height = OTHER_IMG_SIZE;
//                }
//                System.out.println("huehn final point id : " + players.get(i).clientId
//                        + "    leftMargin : " + players.get(i).position.x
//                        + "    topMargin : " + players.get(i).position.y
////                        + "    lp.width : " + lp.width
////                        + "    lp.height : " + lp.height
//                );
            }
        }
    }

    private boolean getLocalInViewPoint() {
        int cX;
        int cY;
        int viewSize = mainLayer.getWidth();
        if (maxRateLen <= 0) {
            maxRateLen = viewSize / 2 - MY_IMG_SIZE;
        }
        cX = viewSize / 2 - MY_IMG_SIZE / 2;
        cY = viewSize / 2 - MY_IMG_SIZE / 2;
        boolean isExistMyPoint = false;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).clientId.equals(MainActivity.id)) {
                myPoint.position.x = players.get(i).position.x;
//                myPoint.position.z = players.get(i).position.z;
                myPoint.position.y = players.get(i).position.y;
                isExistMyPoint = true;
                myPointTextview.setText(String.format(
                        context.getString(R.string.my_point), myPoint.position.x, myPoint.position.y, myPoint.position.z));
                break;
            }
        }

        if (!isExistMyPoint) {
            return false;
        }

        //只有自己一个人的时候
        if (players.size() == 1) {
            scaleTextview.setText("1：1");
            players.get(0).position.x = cX;
            players.get(0).position.y = cY;
            System.out.println("huehn map only me");
            return true;
        }

        //平移坐标，以自己为中心点
        for (int i = 0; i < players.size(); i++) {
            players.get(i).position.x = players.get(i).position.x - myPoint.position.x;
            players.get(i).position.y = players.get(i).position.y - myPoint.position.y;

            System.out.println("huehn center id : " + players.get(i).clientId + "   x : " + players.get(i).position.x + "    y : " + players.get(i).position.y);
        }

        //以画布中心，根据y,x坐标，超出画布则等比缩小
        int maxLengthY = 0;
        int maxLengthX = 0;

        //找到最长缩放边距
        for (int i = 0; i < players.size(); i++) {
            if (Math.abs(players.get(i).position.y) > maxLengthY) {
                maxLengthY = Math.abs(players.get(i).position.y);
            }
            if (Math.abs(players.get(i).position.x) > maxLengthX) {
                maxLengthX = Math.abs(players.get(i).position.x);
            }
        }

        int tempMaxLen = maxLengthX;
        if (maxLengthX < maxLengthY) {
            tempMaxLen = maxLengthY;
        }

        // 缩放比
        if (tempMaxLen > maxRateLen) {
            maxRateLen += increaseLength;
//            maxRateLen += tempMaxLen - maxRateLen;
//            maxRateLen = tempMaxLen;
        } else {
            if (maxRateLen >= minLength + increaseLength && tempMaxLen + increaseLength < maxRateLen) {
                maxRateLen -= increaseLength;
//                maxRateLen -= tempMaxLen - maxRateLen;
//                maxRateLen = -tempMaxLen;
//                if (maxRateLen <= 0){
//                    maxRateLen = viewSize / 2 - MY_IMG_SIZE;
//                }
            }
        }

        if (tempMaxLen > maxRateLen){

        }else {

        }

        // 缩放计算

        float scale;
        scale = (float) viewSize / 2 / maxRateLen;
        int circleX = 0;
        int circleY = 0;
        int myX = 0;
        int myY = 0;
        for (int i = 0; i < players.size(); i++) {
            players.get(i).position.y = (int) (players.get(i).position.y * scale) + viewSize / 2 - OTHER_IMG_SIZE / 2;
            players.get(i).position.x = (int) (players.get(i).position.x * scale) + viewSize / 2 - OTHER_IMG_SIZE / 2;
            if (players.get(i).clientId.equals(MainActivity.id)) {
                players.get(i).position.x = cX;
                players.get(i).position.y = cY;
//                myX = cX;
//                myY = cY;
                myX = players.get(i).position.realX;
                myY = players.get(i).position.realY;
            }else {
//                circleX = players.get(i).position.x;
//                circleY = players.get(i).position.y;
                circleX = players.get(i).position.realX;
                circleY = players.get(i).position.realY;
            }
            System.out.println("huehn scale : id : " + players.get(i).clientId + "    x : " + players.get(i).position.x + "   z : " + players.get(i).position.y);
        }
        int rate = maxRateLen / increaseLength;
//        scaleTextview.setText("1：" + rate);
        System.out.println("huehn distance : circleX : " + circleX + "   myX : " + myX + "   circleX - myX : " + (circleX - myX)
        + "    circleY : " + circleY + "   myY : " + myY + "     circleY - myY : " + (circleY - myY));
        System.out.println("huehn distance : (int)Math.sqrt(Math.pow(circleX - myX, 2) : " + (int)Math.sqrt(Math.pow(circleX - myX, 2))
         + "     Math.pow(circleY - myY, 2) : " + Math.pow(circleY - myY, 2) + "      result : " + (int)Math.sqrt(Math.pow(circleX - myX, 2) + Math.pow(circleY - myY, 2)));
        System.out.println("huehn distance :  realCircleX : " + circleX + "   realCircleY : " + circleY + "     (int)Math.sqrt(Math.pow(circleX - myX, 2) : " +
                (int)Math.sqrt(Math.pow(circleX - myX, 2)) + "     Math.pow(circleY - myY, 2) : " + Math.pow(circleY - myY, 2) + "      result : " + (int)Math.sqrt(Math.pow(circleX - myX, 2) + Math.pow(circleY - myY, 2)));
        scaleTextview.setText("distance : " + (int)Math.sqrt(Math.pow(circleX - myX, 2) + Math.pow(circleY - myY, 2)) + "  m");
        System.out.println("huehn map : rate = " + rate);
        return true;
    }

    public void start() {
        statUpdateUiThread();
    }

    public void stop() {
        isActive = false;
    }

    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(@NonNull Message msg) {
            super.dispatchMessage(msg);
            refreshUi();
        }
    };
}
