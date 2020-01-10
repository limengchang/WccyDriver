package com.bfbyxx.wccydriver.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.api.SetPasswordApi;
import com.bfbyxx.wccydriver.application.MyApplication;
import com.bfbyxx.wccydriver.base.BaseActivity;
import com.bfbyxx.wccydriver.utils.MD5Utils;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.bfbyxx.wccydriver.wheel.PayPsdInputView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by MARK on 2018/7/7.
 * 设置支付密码
 */

public class SettingPaypwdActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.ppiv_pwd)
    PayPsdInputView ppivPwd; //设置支付密码输入框
    @BindView(R.id.ppiv_confirm_pwd)
    PayPsdInputView ppivConfirmPwd; //确认支付密码输入框

    @BindView(R.id.ppiv_pwd2)
    PayPsdInputView ppivPwd2; //设置新支付密码输入框
    @BindView(R.id.ppiv_confirm_pwd2)
    PayPsdInputView ppivConfirmPwd2; //确认新支付密码输入框
    @BindView(R.id.ppiv_pwd_yuan)
    PayPsdInputView ppiv_pwd_yuan;//原密码

    @BindView(R.id.linear_set_pwd_one)
    LinearLayout linear_set_pwd_one;
    @BindView(R.id.linear_set_pwd_two)
    LinearLayout linear_set_pwd_two;

    private boolean payPwdTrue = true;
    private String setPwd="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pay_pwd);
        setPwd = getIntent().getStringExtra("setPwd");
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void onSuccess(String data) {
        if(neType==0){
            JSONObject jsonObject=JSONObject.parseObject(data);
            String state=jsonObject.getString("state");
            if(state.equals("1")){
                this.finish();
            }
            ProjectUtil.show(this,jsonObject.getString("msg"));
        }
    }

    private void initView() {
        if (setPwd.equals("设置支付密码")){
            payPwdTrue = true;
            linear_set_pwd_one.setVisibility(View.VISIBLE);
            linear_set_pwd_two.setVisibility(View.GONE);
        }else {
            payPwdTrue = false;
            linear_set_pwd_one.setVisibility(View.GONE);
            linear_set_pwd_two.setVisibility(View.VISIBLE);
        }
        tv_title.setText(setPwd);
    }

    @OnClick({R.id.tv_back, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                this.finish();
                break;
            case R.id.btn_submit://点击提交按钮
                if (payPwdTrue){
                    if(checkInputIsCorrect()){
                        requestSetPwd();
                    }
                }else {
                    if(checkInputIsCorrect2()){
                        ProjectUtil.show(this,"成功");
                    }
                }
                break;
        }
    }


    /**
     * 请求服务端,设置支付密码
     */
    private void requestSetPwd(){
        neType=0;
        SetPasswordApi spa = new SetPasswordApi();
        spa.setHeader(MyApplication.authorization);
        spa.setUserHeader(MyApplication.getUserHeader());
//        spa.setUserId(app.getUserId());

        String pwd = ppivPwd.getPasswordString();
        spa.setPassword(MD5Utils.getPwd(pwd));

        pClass.startHttpRequest(this, spa);
    }


    /**
     * 校验输入是否正确,在请求服务端设置支付密码前用调用此方法
     */
    private boolean checkInputIsCorrect(){
        if(ppivPwd.pwdIsCompletion() && ppivConfirmPwd.pwdIsCompletion()){

            String pwd = ppivPwd.getPasswordString();
            String confirmPwd = ppivConfirmPwd.getPasswordString();

            if(pwd.equals(confirmPwd)){
                return true;
            }else{
                ProjectUtil.show(this,"两次输入的新密码不一致");
                return false;
            }

        }else{
            ProjectUtil.show(this,"请将密码输入完整");
            return false;
        }
    }
    private boolean checkInputIsCorrect2(){
        if(ppiv_pwd_yuan.pwdIsCompletion()&&ppivPwd2.pwdIsCompletion() && ppivConfirmPwd2.pwdIsCompletion()){

            String pwd = ppivPwd2.getPasswordString();
            String confirmPwd = ppivConfirmPwd2.getPasswordString();

            if(pwd.equals(confirmPwd)){
                return true;
            }else{
                ProjectUtil.show(this,"两次输入的新密码不一致");
                return false;
            }

        }else{
            ProjectUtil.show(this,"请将密码输入完整");
            return false;
        }
    }


}
