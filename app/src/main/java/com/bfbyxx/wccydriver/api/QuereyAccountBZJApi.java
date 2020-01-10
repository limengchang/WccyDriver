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
public class QuereyAccountBZJApi extends BaseApi {
    private String userId;
    private String Page;
    private String Row;

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPage() {
        return Page;
    }

    public void setPage(String page) {
        Page = page;
    }

    public String getRow() {
        return Row;
    }

    public void setRow(String row) {
        Row = row;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);
        return httpService.getGetBondWaterList(getHeader(),getUserId(),getPage(),getRow());
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

