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
public class GetDriverIdApi extends BaseApi {
    //http://192.168.1.242:8092/api/Vehicle/GetDriver?UserID=
    private String UserID;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);
        return httpService.getDriverId(getHeader(),getUserID());
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

