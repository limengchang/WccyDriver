package com.bfbyxx.wccydriver.api;

import com.alibaba.fastjson.JSONObject;
import com.bfbyxx.wccydriver.presenter.HttpService;
import com.rxretrofitlibrary.retrofit_rx.Api.BaseApi;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;

public class UserTackBondApi  extends BaseApi {
    private String UserId;
    private String Amount;

    @Override
    public String getUserId() {
        return UserId;
    }

    @Override
    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);
        JSONObject root = new JSONObject();
        root.put("UserId",getUserId());
        root.put("Amount",getAmount());
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("text/json; charset=utf-8"),
                root.toString());

        return httpService.UserTackBond(getHeader(),body);
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
