package com.bfbyxx.wccydriver.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.api.AddBankCardApi;
import com.bfbyxx.wccydriver.api.CheckBankCardApi;
import com.bfbyxx.wccydriver.base.BaseActivity;
import com.bfbyxx.wccydriver.entity.CheckBankCardResult;
import com.bfbyxx.wccydriver.tools.BankTool;
import com.bfbyxx.wccydriver.utils.ActivityStorer;
import com.bfbyxx.wccydriver.utils.HttpUtil;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.bfbyxx.wccydriver.utils.SPUtils;
import com.bfbyxx.wccydriver.wheel.ClearEditText;
import com.rxretrofitlibrary.retrofit_rx.exception.ApiException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by MARK on 2018/6/8.
 */

public class MyAddBankActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.chikaren)
    ClearEditText tvChikaren; //持卡人输入框

    @BindView(R.id.cardnum)
    ClearEditText tvCardNo; //卡号输入框

    @BindView(R.id.bank_card_type)
    TextView bank_card_type; //银行卡类型

    @BindView(R.id.khh)
    TextView tvKhh;//开户行

    @BindView(R.id.addbanknext)
    Button addbanknext;

    private String bankType = "";

    private BankTool bankTool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbank);
        ActivityStorer.add(this);
        ButterKnife.bind(this);
        initData();
        initView();
    }


    private void initData() {
        bankTool = new BankTool(this);
    }


    private void initView() {
        tv_title.setText("添加银行卡");
    }


    private CheckBankCardResult bcInfo;

    @Override
    public void onSuccess(String data) {
        HttpUtil.dismissProgress();
        if (neType == 0) { //检测银行卡号响应成功
            bcInfo = new Gson().fromJson(data, CheckBankCardResult.class);
            boolean validated = bcInfo.isValidated();
            if (validated) {//银行卡有效
                performGetBankName(bcInfo.getBank());
            } else {
                ProjectUtil.show(MyAddBankActivity.this, "无效的银行卡");
                tvKhh.setText("");
                bank_card_type.setText("");
            }
        } else if (neType == 1) {
            JSONObject jo = JSONObject.parseObject(data);
            String success = jo.getString("Success");
            if (success.equals("true")) {
                ProjectUtil.show(context, "添加成功");
                finish();
            } else {
                ProjectUtil.show(context, jo.getString("Message"));
            }
        }
    }

    @Override
    public void onError(ApiException e) {
        super.onError(e);
        HttpUtil.dismissProgress();
    }

    private void performGetBankName(String bankCode) {
        bankTool.getBankNameByCode(bankCode,
                new BankTool.Callback() {
                    @Override
                    public void onResult(String bankName) {
                        if (TextUtils.isEmpty(bankName)) {
                            ProjectUtil.show(MyAddBankActivity.this, "未知的银行卡");
                            tvKhh.setText("");
                            bank_card_type.setText("");
                            return;
                        }
                        bankType = bcInfo.getCardType();
                        tvKhh.setText(bankName);
                        bank_card_type.setText(bankTool.getBankCardTypeByCode(bcInfo.getCardType()));
//                        //跳转到下个界面
//                        Intent it = new Intent(MyAddBankActivity.this, AddBankCardConfirmActivity.class);
//                        it.putExtra("bankName", bankName);
//                        it.putExtra("cardNo", tvCardNo.getText().toString().trim());
//                        String cardType = bankTool.getBankCardTypeByCode(bcInfo.getCardType());
//                        it.putExtra("cardType", cardType);
//                        it.putExtra("photoNo", tvPhoneNo.getText().toString().trim());
//                        it.putExtra("chikaren", tvChikaren.getText().toString().trim());
//                        startActivity(it);
                    }
                });
    }


    @OnClick({R.id.tv_back, R.id.addbanknext, R.id.bank_card_type, R.id.khh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                this.finish();
                break;
            case R.id.addbanknext:
//                String cardNo = tvCardNo.getText().toString().trim();//银行卡号
                if (checkInputIsCorrect(true)) {
//                    requestCheckBankCard(cardNo);
                    HttpUtil.showProgress(context, "添加中...");
                    addBankCard();
                }
                break;
            case R.id.bank_card_type:
                String cardNo = tvCardNo.getText().toString().trim();//银行卡号
                if (checkInputIsCorrect(false)) {
                    HttpUtil.showProgress(context, "检测中...");
                    requestCheckBankCard(cardNo);
                }
                break;
            case R.id.khh:
                String cardNo1 = tvCardNo.getText().toString().trim();//银行卡号
                if (checkInputIsCorrect(false)) {
                    HttpUtil.showProgress(context, "检测中...");
                    requestCheckBankCard(cardNo1);
                }
                break;
        }
    }


    /**
     * 校验输入的信息是否正确
     */
    private boolean checkInputIsCorrect(boolean isTrue) {
        String cardNo = tvCardNo.getText().toString().trim();
        String chikaren = tvChikaren.getText().toString().trim();
        String khh = tvKhh.getText().toString().trim();
        String bankCardType = bank_card_type.getText().toString().trim();
        if (isTrue) {
            if (cardNo.isEmpty() || chikaren.isEmpty() || khh.isEmpty() || bankCardType.isEmpty()) {
                ProjectUtil.show(this, "请将信息填写完整");
                return false;
            }
        } else {
            if (cardNo.isEmpty() || chikaren.isEmpty()) {
                ProjectUtil.show(this, "请将信息填写完整");
                return false;
            }
        }
        if (cardNo.length() < 16) {
            ProjectUtil.show(this, "卡号一般不低于16位数");
            return false;
        }
        return true;
    }


    /**
     * 检查银行卡
     */
    private void requestCheckBankCard(String cardNo) {
        neType = 0;
        CheckBankCardApi api = new CheckBankCardApi();
        api.setCardNo(cardNo);
        pClass.startHttpRequest(this, api);
    }

    /**
     * 添加银行卡
     */
    private void addBankCard() {
        neType = 1;
        AddBankCardApi api = new AddBankCardApi();
        api.setUserID(SPUtils.getUser_ID(context));
        api.setAccountName(tvChikaren.getText().toString().trim());
        api.setBankName(tvKhh.getText().toString().trim());
        api.setBankCardNo(tvCardNo.getText().toString().trim());
        api.setBankType(bankType);
        pClass.startHttpRequest(this, api);
    }


}
