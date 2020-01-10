package com.bfbyxx.wccydriver.api;

import com.alibaba.fastjson.JSONObject;
import com.bfbyxx.wccydriver.presenter.HttpService;
import com.rxretrofitlibrary.retrofit_rx.Api.BaseApi;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 10:16
 */
public class ForgetPwdApi extends BaseApi {
    private String Phone;
    private String Password;
    private String UserType;

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);

        JSONObject root = new JSONObject();
        root.put("Phone", getPhone());
        root.put("Password", getPassword());
        root.put("UserType", getUserType());

        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("text/json; charset=utf-8"),
                root.toString());
        return httpService.EditUserPwd(getHeader(),body);
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

