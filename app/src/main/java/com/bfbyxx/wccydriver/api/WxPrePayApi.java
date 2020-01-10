package com.bfbyxx.wccydriver.api;

import com.bfbyxx.wccydriver.pay.Constants;
import com.bfbyxx.wccydriver.presenter.HttpService;
import com.rxretrofitlibrary.retrofit_rx.Api.BaseApi;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * 微信预支付接口,发起微信支付前,需要调用此接口来获取相关的信息
 */
public class WxPrePayApi extends BaseApi {

    private String userId;
    private int accountType; //0：对私账户，1：对公账户
    private String feeMoney; //金额

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getFeeMoney() {
        return feeMoney;
    }

    public void setFeeMoney(String feeMoney) {
        this.feeMoney = feeMoney;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);
        JSONObject root = new JSONObject();
        try {
            root.put("UserId",userId);
            root.put("AccountType", accountType);
            root.put("FeeMoney", feeMoney);
            root.put("AppId", Constants.APP_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("text/json; charset=utf-8"),
                root.toString());
        return httpService.wxPrePay(getHeader(),body);
    }

    @Override
    protected boolean isNeedData() {
        return true;
    }
}
