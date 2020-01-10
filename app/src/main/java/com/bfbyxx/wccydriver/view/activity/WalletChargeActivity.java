package com.bfbyxx.wccydriver.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bfbyxx.wccydriver.api.AliPayApi;
import com.bfbyxx.wccydriver.api.WxBondPayApi;
import com.google.gson.Gson;
import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.api.AliPaySuccConfirmApi;
import com.bfbyxx.wccydriver.application.MyApplication;
import com.bfbyxx.wccydriver.base.BaseActivity;
import com.bfbyxx.wccydriver.pay.MoneyTextWatcher;
import com.bfbyxx.wccydriver.pay.WxPrePayInfo;
import com.bfbyxx.wccydriver.pay.alipay.AliPayManager;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.bfbyxx.wccydriver.utils.SPUtils;
import com.bfbyxx.wccydriver.wxapi.WxPayManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 钱包充值界面
 */
public class WalletChargeActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.et_amount)
    EditText etAmount; //金额输入框

    @BindViews({R.id.rb_wx_pay, R.id.rb_ali_pay})
    RadioButton[] payWayRadios;

    private boolean isBond;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_charge);
        ButterKnife.bind(this);
        isBond = getIntent().getBooleanExtra("isBond", false);

        initViews();
    }


    private void initViews() {
        tv_title.setText("钱包充值");

        if (isBond){
            etAmount.setText("1000.00");
        }

        etAmount.addTextChangedListener(new MoneyTextWatcher(etAmount).setDigits(1));

        for (RadioButton rb : payWayRadios) {
            rb.setOnClickListener(radioClickListener);
        }
    }

    @OnClick({R.id.tv_back, R.id.btn_next_step})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.btn_next_step://点击确认
                if (checkInputIsCorrect()) {
                    performClickBtnNextStep();
                }
                break;
        }
    }

    private void performClickBtnNextStep() {
        switch (currSelectedRadioId) {
            case R.id.rb_wx_pay: //微信支付
                requestWxPrePay();
                break;
            case R.id.rb_ali_pay://支付宝
                requestAliPayInfo();
                break;
        }
    }


    /**
     * 请求服务端,获取支付宝支付的相关信息
     */
    private void requestAliPayInfo() {
        neType = 1;
        AliPayApi sapi = new AliPayApi();

        sapi.setHeader(MyApplication.authorization);

        sapi.setUserId(SPUtils.getUser_ID(context));
        String amount = etAmount.getText().toString().trim();
        sapi.setMoneyPrice(amount);
        sapi.setRechargeType(2);
        sapi.setRechargeRemark("");
        sapi.setPayType("0");

        pClass.startHttpRequest(this, sapi);
    }

    /**
     * 请求服务端微信预支付
     */
    private void requestWxPrePay() {
        neType = 0;
        WxBondPayApi api = new WxBondPayApi();
        api.setHeader(MyApplication.authorization);

        api.setUserId(SPUtils.getUser_ID(context));
        String amount = etAmount.getText().toString().trim();
        api.setMoneyPrice(amount);
        api.setRechargeType(2);
        api.setRechargeRemark("");
        api.setPayType("0");

//        pClass.startHttpRequest(this, api, GlobalValue.BASE_URL_PRODUCT);
        pClass.startHttpRequest(this, api);
    }

    //记录当前选择的支付方式RadioButton的id
    private int currSelectedRadioId = -1;

    /**
     * 支付方式RadioButton点击监听
     */
    private View.OnClickListener radioClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            currSelectedRadioId = v.getId();
            for (RadioButton rb : payWayRadios) {
                if (currSelectedRadioId != rb.getId()) {
                    rb.setChecked(false);
                }
            }
        }
    };


    @Override
    public void onSuccess(String data) {
        switch (neType) {
            case 0: //微信预支付接口调用成功
                WxPrePayInfo wxPrePayInfo = new Gson().fromJson(data, WxPrePayInfo.class);

                WxPayManager wxPayMgr = new WxPayManager(this);
                wxPayMgr.reqPay(wxPrePayInfo);

                finish();
                break;
            case 1:
                performAliPay(data);
                break;
            case 2://支付宝最终支付成功确认接口
                jumpToChargeResult(true);
                break;
        }
    }


    /**
     * 跳转至充值结果界面
     */
    private void jumpToChargeResult(boolean isSuccess) {
        Intent it = new Intent(context, WalletChargeResultActivity.class);
        it.putExtra("isSuccess", isSuccess);
        startActivity(it);
        finish();
    }


    private void performAliPay(String orderInfo) {
        AliPayManager mgr = new AliPayManager(this);

        mgr.reqPay(orderInfo, new AliPayManager.Callback() {
            @Override
            public void onPayResult(Map<String, String> returnData) {
                String resultStatus = returnData.get("resultStatus");
                if (resultStatus.equals("9000")) {
//                    ProjectUtil.show(WalletChargeActivity.this,"充值成功");
//                    finish();
                    String result = returnData.get("result");
                    requestAliPaySuccConfirm(result);

                }
            }
        });
    }


    /**
     * 请求服务端,进行支付宝支付的最终成功确认
     */
    private void requestAliPaySuccConfirm(String resultJson) {
        neType = 2;

        AliPaySuccConfirmApi api = new AliPaySuccConfirmApi();
        api.setHeader(MyApplication.authorization);
//        api.setUserHeader(app.getUserHeader());

        try {
            JSONObject resultJo = new JSONObject(resultJson);

            String sign = resultJo.getString("sign");
            api.setSign(sign);

            String signType = resultJo.getString("sign_type");
            api.setSignType(signType);

            JSONObject tradePayRespJo = resultJo.getJSONObject("alipay_trade_app_pay_response");

            String outTradeNo = tradePayRespJo.getString("out_trade_no");
            api.setOutTradeNo(outTradeNo);

            String tradeNo = tradePayRespJo.getString("trade_no");
            api.setTradeNo(tradeNo);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        pClass.startHttpRequest(this, api);

    }

//    @Override
//    public void onError(ApiException e) {
//        ProjectUtil.show(this, e.getDisplayMessage());
//        if (neType == 2) {//支付宝最终支付成功确认接口
//            jumpToChargeResult(false);
//        }
//    }


    /**
     * 校验输入的信息是否正确
     *
     * @return
     */
    private boolean checkInputIsCorrect() {
        String amount = etAmount.getText().toString().trim();
        if (!amount.isEmpty()) {
            double dAmount = Double.valueOf(amount);
            if (dAmount <= 0) {
                ProjectUtil.show(this, "输入正确的金额");
                return false;
            }
        } else {
            ProjectUtil.show(this, "输入正确的金额");
            return false;
        }

        if (currSelectedRadioId == -1) {
            ProjectUtil.show(this, "请选择支付方式");
            return false;
        }

        return true;

    }


}
