package com.bfbyxx.wccydriver.base;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bfbyxx.wccydriver.application.MyApplication;
import com.bfbyxx.wccydriver.presenter.PClass;
import com.bfbyxx.wccydriver.presenter.VInterface;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 9:46
 */
public class BaseActivity extends RxAppCompatActivity implements VInterface {
    public PClass pClass;
    public MyApplication myApplication;
    public int neType;
    protected Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        context = BaseActivity.this;
        myApplication = (MyApplication) getApplication();
        pClass = new PClass(this);
    }

    @Override
    public void onSuccess(String data) {

    }

    @Override
    public void onError(ApiException e) {
//        ProjectUtil.show(this,e.getDisplayMessage());
        e.printStackTrace();
    }

    @Override
    public void connectNetwork() {
        String mess = "亲！您的网络出问题了，请检查设置！";
        ProjectUtil.show(context,mess);
    }

    @Override
    public void showProg() {

    }

    @Override
    public void dismissProg() {

    }
}
