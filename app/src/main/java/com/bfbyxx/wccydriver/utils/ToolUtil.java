package com.bfbyxx.wccydriver.utils;

import android.content.Context;

import com.bfbyxx.wccydriver.entity.UserInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ToolUtil {
    public static String getNowDateTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public static void saveUserInfo(Context context, UserInfo userInfo){
        //将登录返回的信息保存到SPUtils
        SPUtils.setParam(context, "User_ID",userInfo.getUser_ID()==null?"":userInfo.getUser_ID());
        SPUtils.setParam(context, "User_OpenID",userInfo.getUser_OpenID()==null?"":userInfo.getUser_OpenID());
        SPUtils.setParam(context, "User_Name",userInfo.getUser_Name()==null?userInfo.getUser_Phone():userInfo.getUser_Name());
        SPUtils.setParam(context, "User_LoginName",userInfo.getUser_LoginName()==null?"":userInfo.getUser_LoginName());
        SPUtils.setParam(context, "User_Pwd",userInfo.getUser_Pwd()==null?"":userInfo.getUser_Pwd());
        SPUtils.setParam(context, "String User_Email",userInfo.getUser_Email()==null?"":userInfo.getUser_Email());
        SPUtils.setParam(context, "User_Phone",userInfo.getUser_Phone()==null?"":userInfo.getUser_Phone());
        SPUtils.setParam(context, "User_State",userInfo.getUser_State()==null?"":userInfo.getUser_State());
        SPUtils.setParam(context, "User_Star",userInfo.getUser_Star()==null?"":userInfo.getUser_Star());
        SPUtils.setParam(context, "User_Type",userInfo.getUserId()==null?"":userInfo.getUserId());
        SPUtils.setParam(context, "User_Sign",userInfo.getUser_Sign()==null?"":userInfo.getUser_Sign());
        SPUtils.setParam(context, "User_OrderCount",userInfo.getUser_OrderCount()==null?"":userInfo.getUser_OrderCount());
        SPUtils.setParam(context, "User_IDCard",userInfo.getUser_IDCard()==null?"":userInfo.getUser_IDCard());
        SPUtils.setParam(context, "User_Card_A",userInfo.getUser_Card_A()==null?"":userInfo.getUser_Card_A());
        SPUtils.setParam(context, "User_Card_B",userInfo.getUser_Card_B()==null?"":userInfo.getUser_Card_B());
        SPUtils.setParam(context, "User_IsDelete",userInfo.getUser_IsDelete()==null?"":userInfo.getUser_IsDelete());
        SPUtils.setParam(context, "User_CreateID",userInfo.getUser_CreateID()==null?"":userInfo.getUser_CreateID());
        SPUtils.setParam(context, "User_CreateTime",userInfo.getUser_CreateTime()==null?"":userInfo.getUser_CreateTime());
        SPUtils.setParam(context, "User_Head",userInfo.getUser_Head()==null?"":userInfo.getUser_Head());
        SPUtils.setParam(context, "Balance",userInfo.getBalance()==null?"":userInfo.getBalance());
        SPUtils.setParam(context, "Blocked",userInfo.getBlocked()==null?"":userInfo.getBlocked());
        SPUtils.setParam(context, "AccountID",userInfo.getAccountID()==null?"":userInfo.getAccountID());

    }

}
