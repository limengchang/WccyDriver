package com.bfbyxx.wccydriver.api;

import com.bfbyxx.wccydriver.presenter.HttpService;
import com.rxretrofitlibrary.retrofit_rx.Api.BaseApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 10:05
 */
public class TokenApi extends BaseApi {
    private String userid = "74505D20-D91C-4D9E-9662-AEA3348763BA";
    private String username = "openzen";
    private String password = "IL561008";

    public TokenApi() {
        setShowProgress(false);
        setCache(false);
        setMothed("token");
        setCookieNoNetWorkTime(24*60*60*3);//3天
        setCookieNetWorkTime(24*60*60*3);//3天
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);
        Map<String, String> options = new HashMap<>();
        options.put("userid", userid);
        options.put("username", username);
        options.put("password", password);

        return httpService.getToken(options);
    }

    @Override
    public String call(String httpResult) {
        return httpResult;
    }

    @Override
    protected boolean isNeedData() {
        return true;
    }
}

