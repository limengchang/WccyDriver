package com.bfbyxx.wccydriver.api;

import com.bfbyxx.wccydriver.presenter.HttpService;
import com.rxretrofitlibrary.retrofit_rx.Api.BaseApi;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * 修改密码(不需要原密码)
 */
public class ResetPwdWithoutOldPwdApi extends BaseApi {

    private String phone;
    private String newPwd;

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);
        return httpService.resetPwdWithoutOldPwd(header,userHeader,
                phone,newPwd);
    }

    @Override
    protected boolean isNeedData() {
        return false;
    }
}
