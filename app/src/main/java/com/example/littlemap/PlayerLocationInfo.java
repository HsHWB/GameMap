package com.example.littlemap;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * Created by Administrator on 2017/10/27.
 */
public class PlayerLocationInfo {

    public String clientId = "";    // 游戏内标识玩家的ID
    public BlockPos position;           // 玩家在游戏中的位置
    public int yaw;                 // 玩家在游戏中面向的角度

    public PlayerLocationInfo() {
        position = new BlockPos(0, 0, 0);
    }

    /**
     * 缩放换算后data会不真实,所以距离会变化
     * @param x
     * @param y
     * @param z
     * @param yaw
     */
    public void setData(int x, int y, int z, int yaw) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
        this.yaw = yaw;
    }

    public void setRealData(int x, int y, int z){
        this.position.realX = x;
        this.position.realY = y;
        this.position.realZ = z;
    }



    public void updateLocation(PlayerLocationInfo newInfo) {
        setData(newInfo.position.x, newInfo.position.y, newInfo.position.z, newInfo.yaw);
        setRealData(newInfo.position.realX, newInfo.position.realY, newInfo.position.realZ);
//        setData(newInfo.position.x, newInfo.position.y, newInfo.yaw);
    }
    public static class BlockPos {
        public int x;
        public int y;
        public int z;

        public int realX;
        public int realY;
        public int realZ;

        BlockPos() {
        }

        public BlockPos(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public BlockPos(String x, String y, String z) {
            this.x = Integer.valueOf(x);
            this.y = Integer.valueOf(y);
            this.z = Integer.valueOf(z);
        }

        BlockPos(String[] params) {
            x = NumberUtils.toInt(params[0]);
            y = NumberUtils.toInt(params[1]);
            z = NumberUtils.toInt(params[2]);
        }
    }
}
