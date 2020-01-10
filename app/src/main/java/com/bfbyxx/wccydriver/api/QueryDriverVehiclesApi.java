package com.bfbyxx.wccydriver.api;

import com.bfbyxx.wccydriver.presenter.HttpService;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 11:29
 */
public class QueryDriverVehiclesApi extends BaseApi2 {

    @Override
    public Observable main(Retrofit retrofit, String authorization, String user, RequestBody infoBody) {
        HttpService httpService = retrofit.create(HttpService.class);
        return httpService.queryDriverVehicles(header,userHeader,infoBody);
    }


    @Override
    protected boolean isNeedData() {
        return true;
    }

}
