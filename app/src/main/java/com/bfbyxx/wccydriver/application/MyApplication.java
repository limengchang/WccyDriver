package com.bfbyxx.wccydriver.application;

import android.app.Application;
import android.app.Notification;
import android.content.Context;

import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.entity.CarType;
import com.bfbyxx.wccydriver.sqlite.LouSQLite;
import com.bfbyxx.wccydriver.sqlite.MyCallBack;
import com.bfbyxx.wccydriver.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 9:39
 */
public class MyApplication extends Application {
    public static String authorization = "Bearer test";
    public static Context context;
    public static List<CarType> cartypeLsit = new ArrayList<CarType>();
    //    public static String WebViewIP="http://truckbroker.wap.561008.com/";
    public static String WebViewIP = "http://192.168.1.242:8091/";

    private String userPhone;
    private String userId;
    private String userName;
    private String userType;
    private int userStatus;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initJpush();
        // 初始化
        LouSQLite.init(this.getApplicationContext(), new MyCallBack());
    }

    private void initJpush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        //配置推送通知特性
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
        builder.statusBarDrawable = R.drawable.icon_login;
        builder.notificationFlags = Notification.FLAG_SHOW_LIGHTS;
        builder.notificationDefaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
        JPushInterface.setDefaultPushNotificationBuilder(builder);

        JPushInterface.setLatestNotificationNumber(this, 1);
    }

    public static String getUserHeader() {
        JSONObject user = new JSONObject();
        try {
            user.put("userid", SPUtils.getUser_ID(context));
            user.put("name", SPUtils.getUser_Phone(context));
            user.put("usertype", SPUtils.getUser_Type(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user.toString();
    }


    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }
}
