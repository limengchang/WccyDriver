package com.bfbyxx.wccydriver.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.bfbyxx.wccydriver.sqlite.LouSQLite;
import com.bfbyxx.wccydriver.sqlite.MyCallBack;
import com.bfbyxx.wccydriver.sqlite.Phrase;
import com.bfbyxx.wccydriver.view.activity.TitleMessageActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * 极光推送消息广播接收器
 */
public class JPushMessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了自定义消息：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了通知");
            // 在这里可以做些统计，或者做些其他工作
            if(bundle!=null) {
                String extras = bundle.getString(JPushInterface.EXTRA_ALERT);
                if (!TextUtils.isEmpty(extras)){
                    // 插入一个数据到数据库
                    Phrase phrase = new Phrase(extras, extras);
                    LouSQLite.insert(MyCallBack.TABLE_NAME_PHRASE, phrase);
                }
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            System.out.println("用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
            Intent i = new Intent(context, TitleMessageActivity.class);  //自定义打开的界面
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else {
//            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }
}


