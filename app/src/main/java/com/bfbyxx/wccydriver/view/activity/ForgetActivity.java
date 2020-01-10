package com.bfbyxx.wccydriver.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.api.ForgetPwdApi;
import com.bfbyxx.wccydriver.api.ResetPwdApi;
import com.bfbyxx.wccydriver.api.SmsApi;
import com.bfbyxx.wccydriver.application.MyApplication;
import com.bfbyxx.wccydriver.base.BaseActivity;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.bfbyxx.wccydriver.utils.SPUtils;
import com.bfbyxx.wccydriver.wheel.ClearEditText;
import com.bfbyxx.wccydriver.wheel.MyCountDownTimer;
import com.rxretrofitlibrary.retrofit_rx.exception.ApiException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.btn_forget_getCode)
    Button btn_forget_getCode;
    @BindView(R.id.et_forget_phone)
    ClearEditText et_forget_phone;//手机号
    @BindView(R.id.et_forget_code)
    ClearEditText et_forget_code;//验证码
    @BindView(R.id.et_forget_new_pwd)
    ClearEditText et_forget_new_pwd;//新密码
    @BindView(R.id.et_forget_new_pwd_sure)
    ClearEditText et_forget_new_pwd_sure;//确认新密码
    private String checkCode = "";
    private String type = "";
    @BindView(R.id.et_yuan_pwd)
    ClearEditText et_yuan_pwd;//修改原密码
    @BindView(R.id.et_new_pwd)
    ClearEditText et_new_pwd;//修改新密码
    @BindView(R.id.et_new_pwd_sure)
    ClearEditText et_new_pwd_sure;//修改确认新密码
    @BindView(R.id.layout_forget)
    LinearLayout layout_forget;
    @BindView(R.id.layout_update)
    LinearLayout layout_update;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        et_forget_phone.setText(SPUtils.getUser_Phone(context));
        type = getIntent().getStringExtra("type");
        if (type.equals("忘记密码")) {
            layout_forget.setVisibility(View.VISIBLE);
            layout_update.setVisibility(View.GONE);
        } else {
            layout_forget.setVisibility(View.GONE);
            layout_update.setVisibility(View.VISIBLE);
        }
        tv_title.setText(type);
    }

    //忘记
    private boolean checkInputIsCorrect(int checkType) {
        String cMobile = et_forget_phone.getText().toString().trim();
        String cCode = et_forget_code.getText().toString().trim();
        String cPwd = et_forget_new_pwd.getText().toString().trim();
        String cSurePwd = et_forget_new_pwd_sure.getText().toString().trim();
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
        } else if (checkType == 2) {
            if (TextUtils.isEmpty(cMobile) || !ProjectUtil.isMobileNO(cMobile)) {
                ProjectUtil.show(this, "请填写正确的手机号码");
                return false;
            }
        }
        return true;
    }

    //修改
    private boolean checkUpdate() {
        String cCode = et_yuan_pwd.getText().toString().trim();
        String cPwd = et_new_pwd.getText().toString().trim();
        String cSurePwd = et_new_pwd_sure.getText().toString().trim();
        if (TextUtils.isEmpty(cCode)) {
            ProjectUtil.show(this, "请输入原密码");
            return false;
        }
        if (!SPUtils.getUserPwd(context).equals(cCode)) {
            ProjectUtil.show(this, "原密码错误，请重试");
            return false;
        }
        if (TextUtils.isEmpty(cPwd)) {
            ProjectUtil.show(this, "新密码不能为空");
            return false;
        }
        if (TextUtils.isEmpty(cSurePwd)) {
            ProjectUtil.show(this, "确认新密码不能为空");
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
        return true;
    }

    @OnClick({R.id.tv_back, R.id.btn_forget_getCode, R.id.btn_submit_forget, R.id.btn_submit, R.id.tv_forgetpwd_rt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                //返回
                finish();
                break;
            case R.id.btn_forget_getCode:
                //获取验证码
                //获取验证码按钮开始倒计时
                if (checkInputIsCorrect(2)) {
//                    MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60*1000, 1000, btn_forget_getCode);
//                    myCountDownTimer.start();
                    requestSendSmsCode();
                }
                break;
            case R.id.btn_submit_forget:
                //提交
                if (checkInputIsCorrect(1)) {
//                    finish();
                    setLoginPwd();
                }
                break;
            case R.id.btn_submit:
                //修改提交
                if (checkUpdate()) {
                    finish();
//                    setUpdatePwd();
                }
                break;
            case R.id.tv_forgetpwd_rt:
                //忘记密码
                Intent intent = new Intent(this, ForgetActivity.class);
                intent.putExtra("type", "忘记密码");
                startActivity(intent);
                break;
        }
    }


    /**
     * 请求,发送短信验证码到手机
     */
    private void requestSendSmsCode() {
        neType = 1;
        SmsApi smsApi = new SmsApi();
        smsApi.setHeader(MyApplication.authorization);
        smsApi.setMobile(et_forget_phone.getText().toString().trim());
        smsApi.setUserType("3");//2货主，3司机
        smsApi.setVerifyType("1");//1.用户注册 2.重置密码 3.重置支付密码 4.更新联系方式
        pClass.startHttpRequest(ForgetActivity.this, smsApi);
    }

    /**
     * 通过验证码请求 设置密码
     */
    private void setLoginPwd() {
        neType = 2;
        ForgetPwdApi api = new ForgetPwdApi();
        api.setHeader(MyApplication.authorization);
        String phone = et_forget_phone.getText().toString().trim();
        api.setPhone(phone);
        String pwd = et_forget_new_pwd.getText().toString().trim();
        api.setPassword(pwd);
        api.setUserType("3");//2货主,3司机
        pClass.startHttpRequest(this, api);
    }

    /**
     * 请求服务端,修改密码
     */
    private void setUpdatePwd() {
        neType = 3;
        String oldPwd = et_yuan_pwd.getText().toString().trim();
        String newPwd = et_new_pwd.getText().toString().trim();

        ResetPwdApi resetPwdApi = new ResetPwdApi();
        resetPwdApi.setHeader(MyApplication.authorization);
        resetPwdApi.setPhone(et_forget_phone.getText().toString().trim());
        resetPwdApi.setPassword(oldPwd);
        resetPwdApi.setNewPassword(newPwd);
        resetPwdApi.setUserType(1);
        resetPwdApi.setUserId(myApplication.getUserId());
        resetPwdApi.setUserName(myApplication.getUserName());
        resetPwdApi.setRoleId("1");
        pClass.startHttpRequest(this, resetPwdApi);
    }


    @Override
    public void onSuccess(String data) {
        switch (neType) {
            case 1://发送短信验证码成功
                JSONObject jo = JSONObject.parseObject(data);
                String success = jo.getString("Success");
                String code = jo.getString("code");
                if (success.equals("true")) {
                    checkCode = code;
                    //获取验证码按钮开始倒计时
                    MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60 * 1000, 1000, btn_forget_getCode);
                    myCountDownTimer.start();
                } else {
                    ProjectUtil.show(context, "验证码获取失败");
                }
                break;
            case 2:
                //设置密码
                ProjectUtil.show(this, "设置密码成功!");
                finish();
                break;
            case 3:
                //修改密码成功
                finish();
                ProjectUtil.show(this, data);
                break;
        }
    }

    @Override
    public void onError(ApiException e) {
        super.onError(e);
    }
}
