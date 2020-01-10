package com.bfbyxx.wccydriver.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.api.GetDriverIdApi;
import com.bfbyxx.wccydriver.api.LoginAccApi;
import com.bfbyxx.wccydriver.application.MyApplication;
import com.bfbyxx.wccydriver.base.BaseActivity;
import com.bfbyxx.wccydriver.entity.UserInfo;
import com.bfbyxx.wccydriver.utils.HttpUtil;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.bfbyxx.wccydriver.utils.SPUtils;
import com.bfbyxx.wccydriver.wheel.ClearLoginEditText;
import com.bfbyxx.wccydriver.wheel.ClearLoginEditText1;
import com.rxretrofitlibrary.retrofit_rx.exception.ApiException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 10:11
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.edit_name)
    ClearLoginEditText editName;
    @BindView(R.id.edit_pwd)
    ClearLoginEditText1 editPwd;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private String WHITE_SPACE = " ";

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1001) {
                //进入主界面
                startActivity(new Intent(context, OwnerMainActivitys.class));
//                startActivity(new Intent(context, WebViewMainActivity.class));
                finish();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        String name = SPUtils.getUser_Phone(context);
        String pwd = SPUtils.getUserPwd(context);
        if (name.equals("test") && pwd.equals("test")) {
        } else if (name.equals("") || pwd.equals("")) {
        } else {
            int length = name.trim().replaceAll(WHITE_SPACE, "").length();
            if (length >= 11) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < length; i++) {
                    builder.append(name.charAt(i));
                    if (i == 2 || i == 6) {
                        if (i != length - 1)
                            builder.append(WHITE_SPACE);
                    }
                }
                editName.setText(builder);
            }
            editPwd.setText(pwd);
        }
        editName.addTextChangedListener(textWatch);
        //设置版本号
//        textVersion.setText("版本V"+ ProjectUtil.getVersionName(this));
    }

    private TextWatcher textWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (count == 1) {
                int length = s.toString().length();
                if (length == 3 || length == 8) {
                    editName.setText(s + " ");
                    editName.setSelection(editName.getText().toString().length());
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    @OnClick({R.id.btn_submit, R.id.tv_forgetpwd, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                neType = 1;
                String phoneNum = editName.getText().toString().trim().replaceAll(WHITE_SPACE, "");
                ;
                String pwd = editPwd.getText().toString().trim();
                if (phoneNum.equals("") || pwd.equals("")) {
                    ProjectUtil.show(this, "请输入用户名或密码!");
                } else {
                    HttpUtil.showProgress(this, " 登录中...");
                    LoginAccApi loginAccApi = new LoginAccApi();
                    loginAccApi.setHeader(MyApplication.authorization);
                    loginAccApi.setMobile(phoneNum);
                    loginAccApi.setPwd(pwd);
                    loginAccApi.setUserType("3");//2货主,3司机
                    pClass.startHttpRequest(this, loginAccApi);
                }
                break;
            case R.id.tv_forgetpwd:
                //忘记密码
                Intent intent = new Intent(this, ForgetActivity.class);
                intent.putExtra("type", "忘记密码");
                startActivity(intent);
                break;
            case R.id.btn_register:
                //注册
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    /**
     * 请求服务端,获取司机id
     */
    private void getDriverId(String userID) {
        neType = 0;
        GetDriverIdApi gvml = new GetDriverIdApi();
        gvml.setHeader(MyApplication.authorization);
        gvml.setUserID(userID);
        pClass.startHttpRequest(this, gvml);
    }

    @Override
    public void onSuccess(String data) {
        HttpUtil.dismissProgress();
        JSONObject jo = JSONObject.parseObject(data);
        if (neType == 1) {
            String success = jo.getString("Success");
            if (success.equals("true")) {
                SPUtils.setParam(context, "name", editName.getText().toString().trim());
                SPUtils.setParam(context, "pwd", editPwd.getText().toString().trim());
                UserInfo userInfo = new Gson().fromJson(data, UserInfo.class);
                //将登录返回的信息保存到SPUtils
                SPUtils.setParam(context, "User_ID", userInfo.getUserId());
                SPUtils.setParam(context, "User_Name", userInfo.getUserName() == null ? "" : userInfo.getUserName());
                SPUtils.setParam(context, "User_Phone", userInfo.getUserPhone());
                SPUtils.setParam(context, "User_Type", userInfo.getUserType());//2货主，3司机
                SPUtils.setParam(context, "Token", userInfo.getToken());
                getDriverId(userInfo.getUserId());
            } else {
                ProjectUtil.show(context, jo.getString("Message"));
            }
        } else if (neType == 0) {
            String success = jo.getString("Success");
            if (success.equals("true")) {
                JSONObject strData = JSONObject.parseObject(jo.getString("Data"));
                String driverId = strData.getString("Id") == null ? "00000000-0000-0000-0000-000000000000" : strData.getString("Id");
                //将登录返回的信息保存到SPUtils
                SPUtils.setParam(context, "Driver_ID", driverId);
            }
            mHandler.sendEmptyMessage(1001);
        }
    }

    @Override
    public void onError(ApiException e) {
        super.onError(e);
        HttpUtil.dismissProgress();
    }
}

