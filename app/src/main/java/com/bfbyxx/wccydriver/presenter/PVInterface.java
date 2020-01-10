package com.bfbyxx.wccydriver.presenter;

import com.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 9:53
 */
public interface PVInterface {
    void startHttpRequest(RxAppCompatActivity appCompatActivity, BaseApi baseApi);
}
