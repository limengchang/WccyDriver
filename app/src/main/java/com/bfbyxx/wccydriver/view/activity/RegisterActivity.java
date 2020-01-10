package com.bfbyxx.wccydriver.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.api.CheckIsRegisterApi;
import com.bfbyxx.wccydriver.api.RegisterAccApi;
import com.bfbyxx.wccydriver.api.SmsApi;
import com.bfbyxx.wccydriver.application.MyApplication;
import com.bfbyxx.wccydriver.base.BaseActivity;
import com.bfbyxx.wccydriver.entity.UserInfo;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.bfbyxx.wccydriver.utils.SPUtils;
import com.bfbyxx.wccydriver.wheel.ClearEditText;
import com.bfbyxx.wccydriver.wheel.MyCountDownTimer;
import com.rxretrofitlibrary.retrofit_rx.exception.ApiException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


//注册页面
public class RegisterActivity extends BaseActivity {
    public static Activity _this = null;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.reg_edit_name)
    ClearEditText reg_edit_name;//手机号
    @BindView(R.id.reg_edit_code)
    ClearEditText reg_edit_code;//验证码
    @BindView(R.id.layout_one)
    LinearLayout layout_one;
    @BindView(R.id.layout_two)
    LinearLayout layout_two;
    @BindView(R.id.reg_btn_getcode)
    Button reg_btn_getcode;
    @BindView(R.id.reg_password)
    ClearEditText reg_password;//输入密码
    @BindView(R.id.reg_password_sure)
    ClearEditText reg_password_sure;//确认密码
    @BindView(R.id.reg_ckbox_procotol)
    CheckBox reg_ckbox_procotol;

    private String sendSmsCodeSuccMobile = "";
    private String checkCode = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        _this = this;
        init();
    }

    private void init() {
        tv_title.setText("注册");
        layout_one.setVisibility(View.VISIBLE);
    }

    private boolean checkInputIsCorrect(int checkType) {
        String cMobile = reg_edit_name.getText().toString().trim();
        String cCode = reg_edit_code.getText().toString().trim();
        String cPwd = reg_password.getText().toString().trim();
        String cSurePwd = reg_password_sure.getText().toString().trim();
        sendSmsCodeSuccMobile = cMobile;
        if (checkType == 1) {
            if (TextUtils.isEmpty(cMobile)) {
                ProjectUtil.show(this, "请输入手机号");
                return false;
            }
            if (TextUtils.isEmpty(cMobile) || !ProjectUtil.isMobileNO(cMobile)) {
                ProjectUtil.show(this, "请填写正确的手机号码");
                return false;
            }
            if (TextUtils.isEmpty(cCode)) {
                ProjectUtil.show(this, "请输入验证码");
                return false;
            }

//            if(!ProjectUtil.getMD5(cCode).equals(checkCode) && !cCode.equals("0000")){ // 为了方便测试,留一个后门,输入验证码为0000,可以通过
//                ProjectUtil.show(this, "验证码错误");
//                return false;
//            }
            if (!ProjectUtil.getMD5(cCode).equals(checkCode)) {
                ProjectUtil.show(this, "验证码错误");
                return false;
            }
        } else if (checkType == 2) {
            if (TextUtils.isEmpty(cMobile) || !ProjectUtil.isMobileNO(cMobile)) {
                ProjectUtil.show(this, "请填写正确的手机号码");
                return false;
            }
        } else if (checkType == 3) {
            if (TextUtils.isEmpty(cPwd)) {
                ProjectUtil.show(this, "密码不能为空");
                return false;
            }
            if (TextUtils.isEmpty(cSurePwd)) {
                ProjectUtil.show(this, "确认密码不能为空");
                return false;
            }
            if (!cPwd.equals(cSurePwd)) {
                ProjectUtil.show(this, "两次输入的密码不一致");
                return false;
            }
            if (cPwd.length() < 6 && cSurePwd.length() < 6) {
                ProjectUtil.show(this, "密码必须大于6位数!");
                return false;
            }
            if (!reg_ckbox_procotol.isChecked()) {
                ProjectUtil.show(this, "请先同意无车承运风险协议");
                return false;
            }
        }


//        if(!cMobile.equals(sendSmsCodeSuccMobile)){
//            ProjectUtil.show(this, "请重获取验证码");
//            return false;
//        }
//
//        if(!regCkboxProcotol.isChecked()){
//            ProjectUtil.show(this, "请勾选同意用户协议及声明");
//            return false;
//        }

        return true;
    }

    @OnClick({R.id.tv_back, R.id.reg_btn_getcode, R.id.reg_btn_submit, R.id.btn_peison_finish, R.id.reg_text_protocol})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                //返回
                finish();
                break;
            case R.id.reg_btn_getcode:
                if (checkInputIsCorrect(2)) {
//                    //获取验证码按钮开始倒计时
//                    MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60*1000, 1000, reg_btn_getcode);
//                    myCountDownTimer.start();

                    requestCheckIsRegister(sendSmsCodeSuccMobile);
                }
                break;
            case R.id.reg_btn_submit:
                //下一步
                if (checkInputIsCorrect(1)) {
                    tv_title.setText("密码设置");
                    layout_one.setVisibility(View.GONE);
                    layout_two.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_peison_finish:
                //完善个人资料
                if (checkInputIsCorrect(3)) {
                    requestRegister();
//                    Intent intent = new Intent(context,WanshanPersonActivity.class);
//                    intent.putExtra("wanshanType","个人信息");
//                    startActivity(intent);
                }
                break;
            case R.id.reg_text_protocol:
                //查看协议
//                startActivity(new Intent(context,XieyiActivity.class));
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("strTitle", "协议内容");
                intent.putExtra("strRight", "");
                intent.putExtra("strUrl", MyApplication.WebViewIP + "Weixin_User/xieyi");
                startActivity(intent);
                break;
        }
    }

    /**
     * 请求服务端,进行注册
     */
    private void requestRegister() {
        String mobile = reg_edit_name.getText().toString().trim();
        String pwd = reg_password.getText().toString().trim();

        neType = 0;
        RegisterAccApi registerAccApi = new RegisterAccApi();
        registerAccApi.setHeader(MyApplication.authorization);
        registerAccApi.setMobile(mobile);
        registerAccApi.setPwd(pwd);
        registerAccApi.setNewPassword("");
        pClass.startHttpRequest(this, registerAccApi);
    }

    /**
     * 请求服务端,检测手机号是否已注册
     */
    private void requestCheckIsRegister(String phone) {
        neType = 1;
        CheckIsRegisterApi api = new CheckIsRegisterApi();
        api.setHeader(MyApplication.authorization);
        api.setPhone(phone);
        api.setUserType("3");//2货主 3司机

        pClass.startHttpRequest(this, api);
    }

    /**
     * 请求服务端,发送手机验证码
     */
    private void requestSendSmsCode() {
        neType = 2;
        SmsApi smsApi = new SmsApi();
        smsApi.setHeader(MyApplication.authorization);
        smsApi.setMobile(reg_edit_name.getText().toString().trim());
        smsApi.setUserType("3");//2货主，3司机
        smsApi.setVerifyType("1");//1.用户注册 2.重置密码 3.重置支付密码 4.更新联系方式
        pClass.startHttpRequest(RegisterActivity.this, smsApi);
    }


    @Override
    public void onSuccess(String data) {
        JSONObject jo = JSONObject.parseObject(data);
        String success = jo.getString("Success");
        if (neType == 0) {//注册成功
            if (success.equals("true")) {
                ProjectUtil.show(context, "注册成功");
                UserInfo userInfo = new Gson().fromJson(data, UserInfo.class);
                myApplication.setUserId(userInfo.getUserId());
                myApplication.setUserName(userInfo.getUserName());
                myApplication.setUserPhone(userInfo.getUserPhone());
                SPUtils.setParam(this, "name", userInfo.getUserPhone());
                String pwd = reg_password.getText().toString().trim();
                SPUtils.setParam(this, "pwd", pwd);
                finish();
            } else {
                ProjectUtil.show(context, "注册失败");
            }
        } else if (neType == 1) {
            if (success.equals("true")) {
                //获取验证码
                requestSendSmsCode();
            } else {
                ProjectUtil.show(context, "手机号码已注册");
            }
        } else if (neType == 2) {
            String code = jo.getString("code");
            if (success.equals("true")) {
                checkCode = code;
                sendSmsCodeSuccMobile = reg_edit_name.getText().toString().trim();
                //获取验证码按钮开始倒计时
                MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60 * 1000, 1000, reg_btn_getcode);
                myCountDownTimer.start();
            } else {
                ProjectUtil.show(context, "验证码获取失败");
            }
        }
    }

    @Override
    public void onError(ApiException e) {
        super.onError(e);
    }
}
