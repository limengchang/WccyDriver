package com.bfbyxx.wccydriver.model;

import com.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 9:56
 */
public interface MInterface {
    void startHttpRequest(RxAppCompatActivity appCompatActivity, BaseApi baseApi);
}
