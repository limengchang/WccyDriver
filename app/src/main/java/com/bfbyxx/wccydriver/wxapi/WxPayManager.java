package com.bfbyxx.wccydriver.wxapi;


import android.content.Context;

import com.bfbyxx.wccydriver.pay.Constants;
import com.bfbyxx.wccydriver.pay.WxPrePayInfo;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信支付工具类
 */
public class WxPayManager {
    /**
     * 支付结果errorCode
     */
    public static final int PAY_RESULT_SUCC=1; //支付成功
    public static final int PAY_RESULT_ERROR=-1; //错误
    public static final int PAY_RESULT_CANCEL=2; //用户取消

    private Context context;

    public WxPayManager(Context context) {
        this.context = context;

    }

    /**
     * 发起微信支付;
     */
    public void reqPay(WxPrePayInfo wppi){
        if(wppi.getPrepayid()==null || wppi.getPrepayid().isEmpty()){
            ProjectUtil.show(context,"预支付信息错误");
            return;
        }

        IWXAPI api = WXAPIFactory.createWXAPI(context, null);
        api.registerApp(Constants.APP_ID);
        
        PayReq payReq = new PayReq();
        payReq.appId=Constants.APP_ID;//appId
        payReq.partnerId = wppi.getMchid();//商户id
        payReq.prepayId= wppi.getPrepayid();//
        payReq.packageValue = wppi.getPackageName();
        payReq.nonceStr = wppi.getNoncestr();
        payReq.timeStamp = wppi.getTimestamp();
        payReq.sign = wppi.getSign();

        api.sendReq(payReq);

    }
}
