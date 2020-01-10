package com.bfbyxx.wccydriver.pay.alipay;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

/**
 * 支付宝支付的工具类
 */
public class AliPayManager {

    private Activity activity;

    public interface Callback {
        void onPayResult(Map<String, String> aliPayReturnData);
    }


    public AliPayManager(Activity activity) {
        this.activity = activity;
    }

    /**
     * 调用支付宝进行支付
     *
     * @param orderInfo
     */
    public void reqPay(String orderInfo, Callback callback) {
        AliPayTask task = new AliPayTask(activity, callback);
        task.execute(orderInfo);
    }


    public static class AliPayTask extends AsyncTask<String, Void, Map<String, String>> {

        @SuppressLint("StaticFieldLeak")
        private Activity activity;
        private Callback callback;

        AliPayTask(Activity activity, Callback callback) {
            this.activity = activity;
            this.callback = callback;
        }

        @Override
        protected Map<String, String> doInBackground(String... params) {
            PayTask alipay = new PayTask(activity);
            return alipay.payV2(params[0], true);
        }


        @Override
        protected void onPostExecute(Map<String, String> result) {
            callback.onPayResult(result);
        }
    }


}
