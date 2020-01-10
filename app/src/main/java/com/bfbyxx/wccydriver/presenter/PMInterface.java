package com.bfbyxx.wccydriver.presenter;

import com.rxretrofitlibrary.retrofit_rx.exception.ApiException;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 9:54
 */
public interface PMInterface {
    void onSuccess(String data);

    void onError(ApiException e);
}
