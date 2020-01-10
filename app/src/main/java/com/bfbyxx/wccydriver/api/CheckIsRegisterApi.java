package com.bfbyxx.wccydriver.api;

import com.bfbyxx.wccydriver.presenter.HttpService;
import com.rxretrofitlibrary.retrofit_rx.Api.BaseApi;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * 检测手机号是否已被注册
 */
public class CheckIsRegisterApi extends BaseApi {
    private String Phone;
    private String UserType;

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
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
        return httpService.checkPhoneNumber(getHeader(),Phone,UserType);
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
