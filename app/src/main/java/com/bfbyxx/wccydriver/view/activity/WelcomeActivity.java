package com.bfbyxx.wccydriver.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.api.CheckUpdateApi;
import com.bfbyxx.wccydriver.api.GetVehicleModeListApi;
import com.bfbyxx.wccydriver.api.TokenApi;
import com.bfbyxx.wccydriver.application.MyApplication;
import com.bfbyxx.wccydriver.base.BaseActivity;
import com.bfbyxx.wccydriver.entity.CarType;
import com.bfbyxx.wccydriver.utils.SPUtils;
import com.bfbyxx.wccydriver.utils.UpdateVersionUtils;
import com.bfbyxx.wccydriver.utils.Utils;
import com.rxretrofitlibrary.retrofit_rx.exception.ApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 9:46
 */
public class WelcomeActivity extends BaseActivity {
    private Context context;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
//                    getIntentActivity();
                    requestVehicleModeList();
                    break;
            }
//            ProjectUtil.show(WeclcomeActivity.this, "token请求失败,正在重新请求!");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        context = WelcomeActivity.this;
//        initData();
        checkUpdateVersion();
//        getIntentActivity();
    }

    private void initData() {
        neType = 0;
        TokenApi tokenApi = new TokenApi();
        pClass.startHttpRequest(this, tokenApi);

    }

    private void getIntentActivity() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(context, LoginActivity.class));
                finish();
            }
        }, 2000);
    }

    @Override
    public void onSuccess(String data) {
        if (neType == 0) {//token
            JSONObject jsonObject = JSONObject.parseObject(data);
            String state = jsonObject.getString("state");
            if (state.equals("1")) {
                //token获取成功
                String token = jsonObject.getString("data");
                SPUtils.setParam(this, "Token", token);
                SPUtils.setParam(this, "Authorization", "Bearer " + token);
                mHandler.sendEmptyMessage(0);
            } else {
                initData();
            }
        } else if (neType == 1) {
            /**********************版本更新*************************/
            JSONObject jsonObject = JSONObject.parseObject(data);
            String url = jsonObject.getString("url");
            String version = jsonObject.getString("version");
            String message = jsonObject.getString("message");
            int oldVersion = Utils.getVersionCode(context);
            int versonCode = Integer.valueOf(version).intValue();
            if (versonCode > oldVersion) {//有新版本
                UpdateVersionUtils.showUpdaloadDialog(this, url, message);
            } else {
                getIntentActivity();
            }
        } else if (neType == 2) {//车辆类型
            List<CarType> carLsit = new Gson().fromJson(data, new TypeToken<ArrayList<CarType>>() {
            }.getType());
//            app.setCartypeLsit(cartypeLsit);
            CarType cType = new CarType();
            cType.setId("0");
            cType.setText("全部");
            carLsit.add(0, cType);
//            String strList = cartypeLsit.toString();
//            SPUtils.setParam(this,"", cartypeLsit);
            MyApplication.cartypeLsit = carLsit;
            getIntentActivity();
//            performLoginAndInitNext();
        }
    }

    @Override
    public void onError(ApiException e) {
        super.onError(e);
    }

    /**
     * 请求服务端,获取车辆类型
     */
    private void requestVehicleModeList() {
        neType = 2;
        GetVehicleModeListApi gvml = new GetVehicleModeListApi();
        gvml.setHeader(MyApplication.authorization);
        gvml.setUserHeader(MyApplication.getUserHeader());
        pClass.startHttpRequest(this, gvml);
    }

    /*******************************版本更新******************************/

    private void checkUpdateVersion() {
        neType = 1;
        CheckUpdateApi loginAccApi = new CheckUpdateApi();
        loginAccApi.setHeader(MyApplication.authorization);
        pClass.startHttpRequest(this, loginAccApi);
    }
}
