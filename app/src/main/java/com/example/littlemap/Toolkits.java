package com.example.littlemap;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;


import org.apache.commons.lang3.StringUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.CRC32;

/**
 * Created by kebo@yy.com on 2015/9/7 0007.
 */

public final class Toolkits {
    private static String TAG = "Toolkits";

    public static String listToString(List<String> data) {
        String str = "";
        for (int i = 0; i < data.size(); i++) {
            str += data.get(i) + ",";
        }
        str = str.substring(0, str.length() - 1);
        return str;
    }

    public static String[] splitName(String str) {
        if (!str.contains(",")) {
            String result[] = {"", ""};
            result[0] = str;
            return result;
        }
        return str.split(",");
    }

    public static boolean withinFiveMin(String dataTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'");
        Date curDate = new Date(System.currentTimeMillis());
        try {
            Date d1 = formatter.parse(formatter.format(curDate));
            Date d2 = formatter.parse(dataTime);
            long diff = d1.getTime() - d2.getTime();
            if (diff / 1000 <= 5) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static String getGameVer(String verStr) {
        if (!StringUtils.isEmpty(verStr)) {
            String temp = verStr;
            if (temp.substring(temp.length() - 1, temp.length()).equals("0")) {
                return verStr.substring(0, verStr.length() - 2);
            }
        }
        return verStr;
    }

    public static int getRandNum(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }

    /*
     * 是否wifi环境
     */
    public static boolean isInWifiEnvir(Context context) {
        ConnectivityManager tm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = tm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    // 获取App的版本信息
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionName = "";
        }
        return versionName;
    }

    public static int getAppVersionCode(Context context) {
        int versionCode = 0;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取app当前版本信息
     * @param context
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }


    /**
     * 网络类型
     * @param paramContext
     * @return
     */
    public static String getNetWorkStateStr(Context paramContext) {
        NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if ((localNetworkInfo != null) && (localNetworkInfo.isAvailable())) {
            return localNetworkInfo.getTypeName();
        }
        return null;
    }


    /**
     * 将ip的整数形式转换成ip形式
     *
     * @param ipInt
     * @return
     */
    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }
    //判断当前使用网络类型为 2G、3G、4G 网络类型
    /**
     * NETWORK_TYPE_UNKNOWN = 0;
     * NETWORK_TYPE_GPRS = 1;       2G
     * NETWORK_TYPE_EDGE = 2;       2G
     * NETWORK_TYPE_UMTS = 3;       3G
     * NETWORK_TYPE_CDMA = 4;       2G
     * NETWORK_TYPE_EVDO_0 = 5;     3G
     * NETWORK_TYPE_EVDO_A = 6;     3G
     * NETWORK_TYPE_1xRTT = 7;      2G
     * NETWORK_TYPE_HSDPA = 8;      3G
     * NETWORK_TYPE_HSUPA = 9;      3G
     * NETWORK_TYPE_HSPA = 10;      3G
     * NETWORK_TYPE_IDEN = 11;      2G
     * NETWORK_TYPE_EVDO_B = 12;    3G
     * NETWORK_TYPE_LTE = 13;       4G
     * NETWORK_TYPE_EHRPD = 14;     3G
     * NETWORK_TYPE_HSPAP = 15;     3G
     * NETWORK_TYPE_GSM = 16;       2G
     * NETWORK_TYPE_TD_SCDMA = 17;  3G
     * NETWORK_TYPE_IWLAN = 18;     4G
     */

    public final static int NEWWORK_2G = 0;
    public final static int NEWWORK_3G = 1;
    public final static int NEWWORK_4G = 2;
    public final static int NEWWORK_UNKNOWN = -1;

    public static int getMobileNetWorkType(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
            case 16:
                return NEWWORK_2G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case 17:
                return NEWWORK_3G;
            case TelephonyManager.NETWORK_TYPE_LTE:
            case 18:
                return NEWWORK_4G;
            default:
                return NEWWORK_UNKNOWN;

        }
    }

    public static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return true;
            default:
                return false;
        }
    }

    public static String toCrc(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        CRC32 crc32 = new CRC32();
        crc32.update(str.getBytes());
        return String.format("%08x", crc32.getValue());
    }

    /*
     * 像素转换
     */
    public static int dip2px(Context context, int dipValue) {
        float m = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }



    public static String getMD5(String key) {
        String cacheKey = "";
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return cacheKey;
    }

    public static String getFileMD5(File file) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            FileInputStream is = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            DigestInputStream dis = new DigestInputStream(bis, mDigest);
            while (dis.read() != -1) ;
            cacheKey = bytesToHexString(dis.getMessageDigest().digest());

            dis.close();
            bis.close();
            is.close();
        } catch (NoSuchAlgorithmException e) {
            cacheKey = file.getAbsolutePath();
        } catch (IOException e) {
            cacheKey = file.getAbsolutePath();
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static Bitmap getRoundCornerBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundX = width;


        Bitmap bitmap2 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap2);

        final int color = 0xffe0e0e0;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);

        paint.setColor(color);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundX, roundX, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return bitmap2;
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            return false;
        }

        NetworkInfo networkinfo = manager.getActiveNetworkInfo();

        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }

        return true;
    }

    public static int getAvigationBarHight(Context context) {
        Resources resources = context.getResources();
        int navigationBarHight = 0;
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
        if (!hasHomeKey) { //判断没有HOME键时存在导航栏
            navigationBarHight = resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"));
        }
        return navigationBarHight;
    }


    public final static String MCVERSION = "mc";
    public final static String APPVERSION = "app";
    public final static String SYSTEMINFO = "system";
    public final static String NETQUALITY = "quality";




    public static boolean isVisitor(int boxId) {
        return boxId > 1000000000;
    }


    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }


    public static boolean isGenuineMcMarketFromApp = false;
}
