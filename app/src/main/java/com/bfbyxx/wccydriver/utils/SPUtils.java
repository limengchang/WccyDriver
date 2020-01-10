package com.bfbyxx.wccydriver.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 9:40
 */
public class SPUtils {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "LOGINDATA";


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context, String key, Object object) {

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }

        editor.commit();
    }

    public static void setSave(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context, String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clearAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().commit();
    }

    /**
     * 清除指定数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("定义的键名");
        editor.commit();
    }

    public static String getPersonData(Context context, String value) {
        return (String) SPUtils.getParam(context, value, "test");
    }

    /***********************************Start*/
    public static String getDriver_ID(Context context) {
        return (String) SPUtils.getParam(context, "Driver_ID", "test");
    }

    public static String getUser_ID(Context context) {
        return (String) SPUtils.getParam(context, "User_ID", "test");
    }

    public static String getUser_OpenID(Context context) {
        return (String) SPUtils.getParam(context, "User_OpenID", "test");
    }

    public static String getUser_Name(Context context) {
        return (String) SPUtils.getParam(context, "User_Name", "test");
    }

    public static String getUser_LoginName(Context context) {
        return (String) SPUtils.getParam(context, "User_LoginName", "test");
    }

    public static String getUser_Pwd(Context context) {
        return (String) SPUtils.getParam(context, "User_Pwd", "test");
    }
    public static String getUserPwd(Context context) {
        return (String) SPUtils.getParam(context, "pwd", "test");
    }

    public static String getUser_Email(Context context) {
        return (String) SPUtils.getParam(context, "User_Email", "test");
    }

    public static String getUser_Phone(Context context) {
        return (String) SPUtils.getParam(context, "User_Phone", "test");
    }

    public static String getUser_State(Context context) {
        return (String) SPUtils.getParam(context, "User_State", "test");
    }

    public static String getUser_Star(Context context) {
        return (String) SPUtils.getParam(context, "User_Star", "test");
    }

    public static String getUser_Type(Context context) {
        return (String) SPUtils.getParam(context, "User_Type", "test");//2货主，3司机
    }

    public static String getUser_Sign(Context context) {
        return (String) SPUtils.getParam(context, "User_Sign", "test");
    }

    public static String getUser_OrderCount(Context context) {
        return (String) SPUtils.getParam(context, "User_OrderCount", "test");
    }

    public static String getUser_IDCard(Context context) {
        return (String) SPUtils.getParam(context, "User_IDCard", "test");
    }

    public static String getUser_Card_A(Context context) {
        return (String) SPUtils.getParam(context, "User_Card_A", "test");
    }

    public static String getUser_Card_B(Context context) {
        return (String) SPUtils.getParam(context, "User_Card_B", "test");
    }

    public static String getUser_IsDelete(Context context) {
        return (String) SPUtils.getParam(context, "User_IsDelete", "test");
    }

    public static String getUser_CreateID(Context context) {
        return (String) SPUtils.getParam(context, "User_CreateID", "test");
    }

    public static String getUser_CreateTime(Context context) {
        return (String) SPUtils.getParam(context, "User_CreateTime", "test");
    }

    public static String getUser_Head(Context context) {
        return (String) SPUtils.getParam(context, "User_Head", "test");
    }

    public static String getBalance(Context context) {
        return (String) SPUtils.getParam(context, "Balance", "test");
    }

    public static String getBlocked(Context context) {
        return (String) SPUtils.getParam(context, "Blocked", "test");
    }

    public static String getAccountID(Context context) {
        return (String) SPUtils.getParam(context, "AccountID", "test");
    }
    public static String getHeadImg(Context context) {
        return (String) SPUtils.getParam(context, "headImg", "test");
    }
    public static String getToken(Context context) {
        return (String) SPUtils.getParam(context, "Token", "test");
    }
    /***********************************end*/

    public static String getCarTypeLsit(Context context) {
        return (String) SPUtils.getParam(context, "cartypeLsit", "test");
    }
}
