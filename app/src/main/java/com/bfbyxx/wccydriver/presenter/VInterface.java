package com.bfbyxx.wccydriver.presenter;

import com.rxretrofitlibrary.retrofit_rx.exception.ApiException;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 9:47
 */
public interface VInterface {
    void onSuccess(String data);

    void onError(ApiException e);

    void connectNetwork();

    void showProg();

    void dismissProg();
}
