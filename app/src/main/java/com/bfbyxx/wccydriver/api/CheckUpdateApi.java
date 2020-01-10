package com.bfbyxx.wccydriver.api;

import com.bfbyxx.wccydriver.presenter.HttpService;
import com.rxretrofitlibrary.retrofit_rx.Api.BaseApi;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 10:10
 */
public class CheckUpdateApi extends BaseApi {
    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);

        return httpService.checkUpdateVerson(getHeader());
    }

    @Override
    protected boolean isNeedData() {
        return false;
    }

    @Override
    public String call(String httpResult) {
        return httpResult;
    }
}

