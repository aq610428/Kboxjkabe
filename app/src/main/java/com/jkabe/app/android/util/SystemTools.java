/**
 *
 */
package com.jkabe.app.android.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.amap.api.maps.model.LatLng;
import com.jkabe.app.android.base.BaseApplication;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * @类名: SystemTools<br>
 * @author： weilai<br>
 * @version：1.0 2015/12/2 0002 10:41 weilai 发布
 */
public final class SystemTools {

    /**
     *
     * @return
     */
    public static boolean isCarnumberNO(String carNumber) {
        String pattern = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{1}(([A-HJ-Z]{1}[A-HJ-NP-Z0-9]{5})|([A-HJ-Z]{1}(([DF]{1}[A-HJ-NP-Z0-9]{1}[0-9]{4})|([0-9]{5}[DF]{1})))|([A-HJ-Z]{1}[A-D0-9]{1}[0-9]{3}警)|([A-HJ-Z]{1}[A-Z0-9]{1}[0-9]{3}港)|([A-HJ-Z]{1}[A-Z0-9]{1}[0-9]{3}澳)))|([0-9]{6}使)|((([沪粤川云桂鄂陕蒙藏黑辽渝]{1}A)|鲁B|闽D|蒙E|蒙H)[0-9]{4}领)|(WJ[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼·•]{1}[0-9]{4}[TDSHBXJ0-9]{1})|([VKHBSLJNGCE]{1}[A-DJ-PR-TVY]{1}[0-9]{5})";
        return Pattern.matches(pattern, carNumber) == true ? true : false;
    }

    public static String mathKmOne(int mile) {
        return fomatData((Float.valueOf(mile) / 1000), "0.0") + "";
    }

    public static String fomatData(float f, String s) {
        DecimalFormat decimalFormat = new DecimalFormat(s);
        return decimalFormat.format(f);
    }


    public static String mathKmTwo(int mile) {
        String value = fomatData((Float.valueOf(mile) / 1000), "0.00") + "";
        return value;
    }

    public static String mathMinute(int miao) {
        int h = miao / 3600;
        int f = (miao - h * 3600) / 60;//235
        return h + "时" + f + "分";
    }


    public static String cutOutTwo(float value) {
        return String.format("%.2f", value);
    }


    public static LatLng getLatLng(double lat, double lon, Activity activity) {
        LatLng latLng = null;
        try {
            CoordinateConverter converter = new CoordinateConverter(activity);
            converter.from(CoordinateConverter.CoordType.GPS);
            DPoint dPoint = new DPoint();
            dPoint.setLatitude(lat);
            dPoint.setLongitude(lon);
            converter.coord(dPoint);
            DPoint desLatLng = converter.convert();
            latLng = new LatLng(desLatLng.getLatitude(), desLatLng.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return latLng;
    }


    public static List<String> getList() {
        List<String> list = new ArrayList<>();
        list.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
        list.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
        list.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");
        list.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg");
        list.add("http://a0.att.hudong.com/56/12/01300000164151121576126282411.jpg");
        return list;
    }


    public static List<String> getListTools() {
        List<String> list_title = new ArrayList<>();
        list_title.add("企业门牌照");
        list_title.add("企业整体外观照");
        list_title.add("企业内部环境照");
        list_title.add("维修工时收费标准");
        list_title.add("救援工具照");
        return list_title;
    }


    /**
     * 调用第三方浏览器打开
     *
     * @param url 要浏览的资源地址
     */
    public static void openBrowser(Activity activity, String url) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            ComponentName componentName = intent.resolveActivity(activity.getPackageManager());
            activity.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        } else {
            Toast.makeText(activity, "请下载浏览器", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 根据应用包名，跳转到应用市场
     *
     * @param activity    承载跳转的Activity
     * @param packageName 所需下载（评论）的应用包名
     */
    public static void shareAppShop(Activity activity, String packageName) {
        try {
            Uri uri = Uri.parse("market://details?id=" + packageName);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, "您没有安装应用市场", Toast.LENGTH_SHORT).show();
        }
    }


    public static void hideKeyBoard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }


    /**
     * 安装apk
     *
     * @param
     * @param file
     */
    public static void installApk(Activity activity, File file) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(activity, "com.jkabe.app.android.provider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        activity.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 重启整个APP
     * @param context
     */
    public static void restartAPP(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(BaseApplication.getContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 获取当前应用程序的版本号
     */
    public static int getAppVersionCode(Context context) {
        int versionCode = 1;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
        }
        return versionCode;
    }


    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            LogUtils.e("VersionInfo" + "Exception    " + e);
        }
        return versionName;
    }


    /**
     * 验证手机号码是否合法
     * 176, 177, 178;
     * 180, 181, 182, 183, 184, 185, 186, 187, 188, 189;
     * 145, 147;
     * 130, 131, 132, 133, 134, 135, 136, 137, 138, 139;
     * 150, 151, 152, 153, 155, 156, 157, 158, 159;
     *
     * "13"代表前两位为数字13,
     * "[0-9]"代表第二位可以为0-9中的一个,
     * "[^4]" 代表除了4
     * "\\d{8}"代表后面是可以是0～9的数字, 有8位。
     */
    public static boolean isMobileNumber(String mobiles) {
        String telRegex = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$";
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex);
    }


    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public static void callPhone(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }

    /******打开地图导航*****/
    public static void openMap(String address, Context mContext) {
        if (isInstallByread("com.autonavi.minimap")) {//判断是否安装高德
            invokingGD(address, mContext);
            return;
        } else if (isInstallByread("com.baidu.BaiduMap")) {//判断是否安装百度
            invokingBD(address, mContext);
            return;
        } else {
            ToastUtil.showToast("您未安装地图软件");
        }

    }


    /**
     * 判断是否安装目标应用
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    public static boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }


    /****打开高德地图*****/
    public static void invokingGD(String address, Context mContext) {
        //  com.autonavi.minimap这是高德地图的包名
        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("androidamap://navi?sourceApplication=应用名称&lat=" + "&dev=0"));
        intent.setPackage("com.autonavi.minimap");
        intent.setData(Uri.parse("androidamap://poi?sourceApplication=softname&keywords=" + address));

        if (isInstallByread("com.autonavi.minimap")) {
            mContext.startActivity(intent);
        }
    }

    public static void invokingBD(String address, Context mContext) {
        //  com.baidu.BaiduMap这是高德地图的包名
        //调起百度地图客户端try {
        Intent intent = new Intent();
        intent.setData(Uri.parse("baidumap://map/geocoder?src=openApiDemo&address=" + address));
        mContext.startActivity(intent); //启动调用
    }

    /****打开高德地图*****/
    public static void openGaoDeMap(double lon, double lat, String describle, Activity activity) {
        try {
            double[] st = bdToGaoDe(lat, lon);
            StringBuilder loc = new StringBuilder();
            loc.append("androidamap://viewMap?sourceApplication=XX");
            loc.append("&poiname=");
            loc.append(describle);
            loc.append("&lat=");
            loc.append(st[1]);
            loc.append("&lon=");
            loc.append(st[0]);
            loc.append("&dev=0");
            Intent intent = Intent.getIntent(loc.toString());
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openBaiduMap(double longitude, double latitude, String describle, Activity activity) {
        try {
            double[] st = gaoDeToBaidu(longitude, latitude);
            StringBuilder loc = new StringBuilder();
            loc.append("intent://map/direction?origin=latlng:");
            loc.append(latitude);
            loc.append(",");
            loc.append(longitude);
            loc.append("|name:");
            loc.append("我的位置");
            loc.append("&destination=latlng:");
            loc.append(st[1]);
            loc.append(",");
            loc.append(st[0]);
            loc.append("|name:");
            loc.append(describle);
            loc.append("&mode=driving");
            loc.append("&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            Intent intent = Intent.getIntent(loc.toString());
            activity.startActivity(intent); //启动调用
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double[] bdToGaoDe(double bd_lat, double bd_lon) {
        double[] gd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        gd_lat_lon[0] = z * Math.cos(theta);
        gd_lat_lon[1] = z * Math.sin(theta);
        return gd_lat_lon;
    }

    public static double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }

}
