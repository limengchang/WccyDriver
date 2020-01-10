package com.bfbyxx.wccydriver.api;


import com.bfbyxx.wccydriver.presenter.HttpService;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * 查询银行卡列表接口
 */
public class QueryBankCardListApi extends BaseApi2{

    private String userId;

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);
        return httpService.queryBankCard(getHeader(),userId);
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
