package com.bfbyxx.wccydriver.api;

import com.alibaba.fastjson.JSONObject;
import com.bfbyxx.wccydriver.presenter.HttpService;
import com.rxretrofitlibrary.retrofit_rx.Api.BaseApi;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by MARK on 2018/6/14.
 */

public class RegisterAccApi extends BaseApi {
    private String mobile;
    private String pwd;
    private int userType;
    private String NewPassword;


    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNewPassword() {
        return NewPassword;
    }

    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);

        JSONObject root = new JSONObject();
        root.put("Phone", getMobile());
        root.put("Password", getPwd());
        root.put("UserType", 3);//2货主 3司机
//        root.put("NewPassword", getNewPassword());
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("text/json; charset=utf-8"),
                root.toString());
        return httpService.register(getHeader(),body);
    }

    @Override
    protected boolean isNeedData() {
        return true;
    }

    @Override
    public String call(String httpResult) {
        return httpResult;
    }
}
