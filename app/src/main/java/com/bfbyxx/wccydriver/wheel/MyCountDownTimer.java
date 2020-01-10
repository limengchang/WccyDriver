package com.bfbyxx.wccydriver.wheel;

import android.os.CountDownTimer;
import android.widget.Button;

/**
 * Created by MARK on 2018/7/19.
 */

//复写倒计时
public class MyCountDownTimer extends CountDownTimer {
    public Button btn_djs;

    public MyCountDownTimer(long millisInFuture, long countDownInterval, Button btn_djs) {
        super(millisInFuture, countDownInterval);
        this.btn_djs=btn_djs;
    }

    //计时过程
    @Override
    public void onTick(long l) {
        //防止计时过程中重复点击
        btn_djs.setClickable(false);
        btn_djs.setText(l/1000+"s");

    }

    //计时完毕的方法
    @Override
    public void onFinish() {
        //重新给Button设置文字
        btn_djs.setText("重新获取验证码");
        //设置可点击
        btn_djs.setClickable(true);
    }
}

