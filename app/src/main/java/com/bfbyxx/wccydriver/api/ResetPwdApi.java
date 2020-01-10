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

public class ResetPwdApi extends BaseApi {
    private String Phone;
    private String Password;
    private int UserType;
    private String NewPassword;

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

    public int getUserType() {
        return UserType;
    }

    public void setUserType(int userType) {
        UserType = userType;
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
        root.put("Phone", getPhone());
        root.put("Password", getPassword());
        root.put("UserType", getUserType());
        root.put("NewPassword",getNewPassword());
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("text/json; charset=utf-8"),
                root.toString());

        JSONObject user = new JSONObject();
        user.put("userid", getUserId());
        user.put("name", getUserName());
        user.put("roleid", getRoleId());
//        RequestBody userHead=RequestBody.create(okhttp3.MediaType.parse("text/json; charset=utf-8"),
//                user.toJSONString());
        return httpService.resetPwd(getHeader(),user.toString(),body);
    }


    @Override
    protected boolean isNeedData() {
        return false;
    }
}
