package com.bfbyxx.wccydriver.model;

import com.bfbyxx.wccydriver.presenter.PMInterface;
import com.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 9:56
 */
public class MClass implements MInterface, HttpOnNextListener {
    PMInterface pmInterface;

    public MClass(PMInterface pmInterface) {
        this.pmInterface = pmInterface;
    }

    @Override
    public void startHttpRequest(RxAppCompatActivity appCompatActivity, BaseApi baseApi) {
        HttpManager httpManager = new HttpManager(this, appCompatActivity);
        httpManager.doHttpDeal(baseApi);
    }

    @Override
    public void onNext(String resulte, String mothead) {
        pmInterface.onSuccess(resulte);
    }

    @Override
    public void onError(ApiException e) {
        pmInterface.onError(e);
    }
}
