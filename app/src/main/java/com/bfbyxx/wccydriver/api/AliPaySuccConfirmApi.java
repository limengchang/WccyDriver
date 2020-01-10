package com.bfbyxx.wccydriver.api;

import com.bfbyxx.wccydriver.presenter.HttpService;
import com.rxretrofitlibrary.retrofit_rx.Api.BaseApi;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;


/**
 * 支付宝支付 成功确认接口
 * 支付宝sdk返回支付成功信息后,应调用此接口请求服务端来进行最终的成功确认
 */
public class AliPaySuccConfirmApi extends BaseApi {

    private String outTradeNo;
    private String tradeNo;
    private String sign;
    private String signType;

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);
        JSONObject root = new JSONObject();

        try {

            root.put("out_trade_no",outTradeNo);
            root.put("sign", sign);
            root.put("sign_type", signType);
            root.put("trade_no", tradeNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("text/json; charset=utf-8"),
                root.toString());

        return httpService.alipaySuccConfirm(getHeader(),getUserHeader(), body);
    }

    @Override
    protected boolean isNeedData() {

        return true;
    }
}
