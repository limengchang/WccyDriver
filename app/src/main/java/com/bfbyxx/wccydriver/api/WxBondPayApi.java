package com.bfbyxx.wccydriver.api;

import com.bfbyxx.wccydriver.pay.Constants;
import com.bfbyxx.wccydriver.presenter.HttpService;
import com.rxretrofitlibrary.retrofit_rx.Api.BaseApi;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * 微信预支付接口,发起微信支付前,需要调用此接口来获取相关的信息
 */
public class WxBondPayApi extends BaseApi {

    private String UserId;
    private String MoneyPrice;
    private int RechargeType; //1:余额充值,  2:保证金充值
    private String RechargeRemark; //充值说明
    private String PayType; //0:App，1:Wap H5，2:微信JSAPI
    private String WechatOpenId; //openId

    @Override
    public String getUserId() {
        return UserId;
    }

    @Override
    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getMoneyPrice() {
        return MoneyPrice;
    }

    public void setMoneyPrice(String moneyPrice) {
        MoneyPrice = moneyPrice;
    }

    public int getRechargeType() {
        return RechargeType;
    }

    public void setRechargeType(int rechargeType) {
        RechargeType = rechargeType;
    }

    public String getRechargeRemark() {
        return RechargeRemark;
    }

    public void setRechargeRemark(String rechargeRemark) {
        RechargeRemark = rechargeRemark;
    }

    public String getPayType() {
        return PayType;
    }

    public void setPayType(String payType) {
        PayType = payType;
    }

    public String getWechatOpenId() {
        return WechatOpenId;
    }

    public void setWechatOpenId(String wechatOpenId) {
        WechatOpenId = wechatOpenId;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);
        JSONObject root = new JSONObject();
        try {
            root.put("UserId",userId);
            root.put("MoneyPrice ", MoneyPrice);
            root.put("RechargeType", RechargeType);
            root.put("RechargeRemark",RechargeRemark);

            root.put("PayType ", PayType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("text/json; charset=utf-8"),
                root.toString());
        return httpService.BondRecharge(getHeader(),body);
    }

    @Override
    protected boolean isNeedData() {
        return true;
    }
}
