package com.bfbyxx.wccydriver.presenter;

import com.bfbyxx.wccydriver.model.MClass;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 9:55
 */
public class PClass implements PVInterface, PMInterface{
    VInterface vInterface;
    MClass mClass;

    public PClass(VInterface vInterface) {
        this.vInterface = vInterface;
        mClass = new MClass(this);
    }

    @Override
    public void startHttpRequest(RxAppCompatActivity appCompatActivity, BaseApi baseApi) {
        if (!ProjectUtil.isNetworkConnected(appCompatActivity)) {
            vInterface.connectNetwork();
        } else {
            if (baseApi.isShowProgress()) {
                vInterface.showProg();
            }
            mClass.startHttpRequest(appCompatActivity, baseApi);
        }
    }

    @Override
    public void onSuccess(String data) {
        vInterface.dismissProg();
        vInterface.onSuccess(data);
    }

    @Override
    public void onError(ApiException e) {
        vInterface.dismissProg();
        vInterface.onError(e);
    }
}
