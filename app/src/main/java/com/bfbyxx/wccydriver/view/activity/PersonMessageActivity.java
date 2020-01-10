package com.bfbyxx.wccydriver.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.application.MyApplication;
import com.bfbyxx.wccydriver.base.BaseActivity;
import com.bfbyxx.wccydriver.tools.Tools;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.bfbyxx.wccydriver.utils.SPUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonMessageActivity extends BaseActivity {
    private Context context;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_person_set_address)
    TextView tv_person_set_address;
    @BindView(R.id.tv_person_username)
    TextView tv_person_username;
    @BindView(R.id.tv_userphone)
    TextView tv_userphone;
    @BindView(R.id.wb_person)
    WebView wb_person;
    @BindView(R.id.tv_right)
    TextView tv_right;

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                HashMap<String, String> map = (HashMap<String, String>) msg.obj;
                String address = map.get("address");
                tv_person_set_address.setText(address);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_message);
        ButterKnife.bind(this);
        context = PersonMessageActivity.this;
        tv_title.setText("个人资料");
        tv_right.setText("编辑");
        Tools.showWebView(wb_person, MyApplication.WebViewIP+"Weixin_Driver/UserInfo",context);
//        init();
    }

    private void init(){
        tv_person_username.setText(SPUtils.getPersonData(context,"User_Name"));
        tv_userphone.setText(SPUtils.getUser_Phone(context));
    }

    @OnClick({R.id.tv_back,R.id.tv_person_check_address,R.id.tv_wc_sfcxx,R.id.tv_wc_jszxx,R.id.tv_wc_ysxkzxx,R.id.tv_right})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.tv_back:
                //返回
                finish();
                break;
            case R.id.tv_person_check_address:
                //选择地址
                ProjectUtil.showCityAddress(context, mHandler);
                break;
            case R.id.tv_wc_sfcxx:
                //完善身份证信息
                intent.setClass(context,WanshanPersonActivity.class);
                intent.putExtra("wanshanType","身份证信息");
                startActivity(intent);
                break;
            case R.id.tv_wc_jszxx:
                //完善驾驶证信息
                intent.setClass(context,WanshanPersonActivity.class);
                intent.putExtra("wanshanType","驾驶证信息");
                startActivity(intent);
                break;
            case R.id.tv_wc_ysxkzxx:
                //完善运输许可证信息
                intent.setClass(context,WanshanPersonActivity.class);
                intent.putExtra("wanshanType","运输许可证信息");
                startActivity(intent);
                break;
            case R.id.tv_right:
                // 无参数调用 JS的方法
                wb_person.loadUrl("javascript:EditShow();");
                break;
        }
    }

}
