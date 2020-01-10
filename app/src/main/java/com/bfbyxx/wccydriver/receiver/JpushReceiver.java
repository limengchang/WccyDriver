package com.bfbyxx.wccydriver.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;

import com.bfbyxx.wccydriver.R;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * 极光推送广播接收器
 */
public class JpushReceiver extends BroadcastReceiver {

    private static final int MSG_TYPE_NEW_ORDER_TIP=1;//新订单提醒

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if(bundle!=null){
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if(!TextUtils.isEmpty(extras)){
                try {
                    JSONObject jo = new JSONObject(extras);
                    int jMessgeType = jo.getInt("JMessgeType");
                    handleMsg(context,jMessgeType);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void handleMsg(Context context,int msgType){
        switch (msgType) {
            case MSG_TYPE_NEW_ORDER_TIP:
                //此处播放新订单语音提示
                playNewOrderTipAudio(context);
                break;
        }
    }


    private static MediaPlayer sMediaPlayer;

    /**
     * 播放新订单语音提示
     */
    private void playNewOrderTipAudio(Context context){
        if(sMediaPlayer ==null){
            sMediaPlayer = MediaPlayer.create(context, R.raw.new_order_tip);
        }
        if(!sMediaPlayer.isPlaying()){
            sMediaPlayer.start();
        }
    }


}

