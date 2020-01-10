package com.bfbyxx.wccydriver.api;


import com.alibaba.fastjson.JSONObject;
import com.bfbyxx.wccydriver.presenter.HttpService;
import com.rxretrofitlibrary.retrofit_rx.Api.BaseApi;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * 检测银行卡信息的接口
 */
public class AddBankCardApi extends BaseApi {
    private String UserID;
    private String AccountName;
    private String BankName;
    private String BankCardNo;
    private String BankType;

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);
        JSONObject root = new JSONObject();
        root.put("UserID",getUserID());
        root.put("AccountName",getAccountName());
        root.put("BankName",getBankName());
        root.put("BankCardNo",getBankCardNo());
        root.put("BankType",getBankType());
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("text/json; charset=utf-8"),
                root.toString());

        return httpService.addBankCard(getHeader(),body);
    }

    @Override
    protected boolean isNeedData() {
        return false;
    }

    @Override
    public String call(String httpResult) {
        return httpResult;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getBankCardNo() {
        return BankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        BankCardNo = bankCardNo;
    }

    public String getBankType() {
        return BankType;
    }

    public void setBankType(String bankType) {
        BankType = bankType;
    }
}
