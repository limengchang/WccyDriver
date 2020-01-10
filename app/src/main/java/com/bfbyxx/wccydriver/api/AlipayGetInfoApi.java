package com.bfbyxx.wccydriver.api;

import com.bfbyxx.wccydriver.presenter.HttpService;
import com.rxretrofitlibrary.retrofit_rx.Api.BaseApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;

public class AlipayGetInfoApi extends BaseApi {

    private String tradeNo;//订单号
    private String subject;//标题
    private String totalAmount;//金额
    private String userId;
    private String body;

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);
        Date date = new Date();
        tradeNo = String.valueOf(date.getTime());


        JSONObject root = new JSONObject();

        try {
            root.put("trade_no", tradeNo);
            root.put("subject", subject);
            root.put("total_amount", totalAmount);
            root.put("userid", userId);
            root.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("text/json; charset=utf-8"),
                root.toString());

        return httpService.aliPayGetInfo(header,body);
    }

    @Override
    protected boolean isNeedData() {
        return true;
    }
}
