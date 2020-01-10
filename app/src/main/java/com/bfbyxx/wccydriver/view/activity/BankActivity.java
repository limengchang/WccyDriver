package com.bfbyxx.wccydriver.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.adapter.BankAdapter;
import com.bfbyxx.wccydriver.api.QueryBankCardListApi;
import com.bfbyxx.wccydriver.application.MyApplication;
import com.bfbyxx.wccydriver.base.BaseActivity;
import com.bfbyxx.wccydriver.entity.BankInfo;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.bfbyxx.wccydriver.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BankActivity extends BaseActivity {
    private Context context;
    @BindView(R.id.lv_bank)
    ListView lv_bank;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.tv_bank_null_icon)
    TextView tv_bank_null_icon;

    private List<BankInfo> bankList=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        ButterKnife.bind(this);
        context = BankActivity.this;
        tv_title.setText("银行卡信息");
        tv_right.setText("添加");
    }

    @OnClick({R.id.tv_back,R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                //返回
                finish();
                break;
            case R.id.tv_right:
                //添加银行卡页面
                startActivity(new Intent(context,MyAddBankActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestQueryBankCard();
    }

    /**
     * 请求服务端,查询银行卡列表
     */
    private void requestQueryBankCard() {
        neType = 0;
        QueryBankCardListApi api=new QueryBankCardListApi();
        api.setHeader(MyApplication.authorization);
        api.setUserId(SPUtils.getUser_ID(context));
        pClass.startHttpRequest(this,api);
    }

    @Override
    public void onSuccess(String data) {
        if (neType == 0) { //获取银行卡列表
            bankList = JSONObject.parseArray(data, BankInfo.class);
            if (bankList.isEmpty()){
                tv_bank_null_icon.setVisibility(View.VISIBLE);
                lv_bank.setVisibility(View.GONE);
                return;
            }else {
                tv_bank_null_icon.setVisibility(View.GONE);
                lv_bank.setVisibility(View.VISIBLE);
            }
            BankAdapter meAdapter = new BankAdapter(context,bankList);
            lv_bank.setAdapter(meAdapter);
            meAdapter.notifyDataSetChanged();
//            bankAdapter.setData(bankList);
//            bankAdapter.notifyDataSetChanged();

        }else if(neType==1){//删除银行卡
            ProjectUtil.show(this,data);
            requestQueryBankCard();
        }
    }
}
