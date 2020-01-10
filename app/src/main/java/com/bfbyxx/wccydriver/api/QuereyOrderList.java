package com.bfbyxx.wccydriver.api;

import com.alibaba.fastjson.JSONObject;
import com.bfbyxx.wccydriver.presenter.HttpService;
import com.rxretrofitlibrary.retrofit_rx.Api.BaseApi;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 10:17
 */
public class QuereyOrderList extends BaseApi {
    private String DriverId;
    private String UserID;
    private String Rows;
    private String Page;
    private String OrderStatus;

    public String getDriverId() {
        return DriverId;
    }

    public void setDriverId(String driverId) {
        DriverId = driverId;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getRows() {
        return Rows;
    }

    public void setRows(String rows) {
        Rows = rows;
    }

    public String getPage() {
        return Page;
    }

    public void setPage(String page) {
        Page = page;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);
        JSONObject root = new JSONObject();
        root.put("DriverId", getDriverId());
        root.put("UserID", getUserID());
        root.put("Rows", getRows());
        root.put("Page", getPage());
        root.put("WaybillStatus",getOrderStatus());

        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("text/json; charset=utf-8"),
                root.toString());
        return httpService.getPageWaybillList(getHeader(),body);
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

