package com.bfbyxx.wccydriver.api;

import com.bfbyxx.wccydriver.presenter.HttpService;
import com.rxretrofitlibrary.retrofit_rx.Api.BaseApi;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 10:17
 */
public class GetVehicleModeListApi extends BaseApi {
    private String keyid;

    public String getKeyid() {
        return keyid;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }
    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);
        return httpService.getVehicleModeList(getHeader(),keyid);
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

