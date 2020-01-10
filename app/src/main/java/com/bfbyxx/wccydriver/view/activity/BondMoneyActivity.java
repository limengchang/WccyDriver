package com.bfbyxx.wccydriver.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.adapter.AccountAdapter;
import com.bfbyxx.wccydriver.api.GetUserAccountApi;
import com.bfbyxx.wccydriver.api.QuereyAccountBZJApi;
import com.bfbyxx.wccydriver.api.UserTackBondApi;
import com.bfbyxx.wccydriver.application.MyApplication;
import com.bfbyxx.wccydriver.base.BaseActivity;
import com.bfbyxx.wccydriver.entity.Account;
import com.bfbyxx.wccydriver.entity.UserInfo;
import com.bfbyxx.wccydriver.utils.BigDecimalUtils;
import com.bfbyxx.wccydriver.utils.HttpUtil;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.bfbyxx.wccydriver.utils.SPUtils;
import com.google.gson.Gson;
import com.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BondMoneyActivity extends BaseActivity implements OnRefreshLoadMoreListener {


    @BindView(R.id.tv_back)
    TextView mTvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.bond_btn)
    TextView mBondBtn;
    @BindView(R.id.bond_text_money)
    TextView mBondTextMoney;
    @BindView(R.id.bond_btn_back)
    Button mBondBtnBack;
    @BindView(R.id.lv_account)
    ListView mLvAccount;
    @BindView(R.id.tv_null)
    LinearLayout mTvNull;
    @BindView(R.id.srl_account_list)
    SmartRefreshLayout mSrlAccountList;

    private AccountAdapter meAdapter;
    private List<Account> accountList = new ArrayList<>();
    private int row = 10;
    private int page = 1;
    private boolean isFresh = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bond_money);
        ButterKnife.bind(this);

        SpannableString spanstring = new SpannableString("缴纳1000元");
        //背景颜色
        spanstring.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanstring.setSpan(new ForegroundColorSpan(Color.RED), 2, spanstring.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBondBtn.setText(spanstring);

        context = BondMoneyActivity.this;
        mTvTitle.setText("保证金信息");
        mSrlAccountList.setOnRefreshLoadMoreListener(this);

        meAdapter = new AccountAdapter(context, accountList);

        getUserAccount();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.tv_back, R.id.bond_btn, R.id.bond_btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                //返回
                finish();
                break;

            case R.id.bond_btn:
                //充值
                Intent intent = new Intent(context, WalletChargeActivity.class);
                intent.putExtra("isBond", true);
                startActivity(intent);
                break;

            case R.id.bond_btn_back:
                //退还
                showDialog();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showDialog() {

        final AlertDialog builder = new AlertDialog.Builder(this)
                .create();
        builder.show();
        if (builder.getWindow() == null) return;
        builder.getWindow().setContentView(R.layout.bond_back_dialog);//设置弹出框加载的布局

        ImageView mDialogDelete = builder.findViewById(R.id.dialog_delete);
        final TextView moneyBack = builder.findViewById(R.id.bond_money_back_numbel);
        Button btnSure = builder.findViewById(R.id.btn_bond_sure);

        moneyBack.setText("1000.00");

        mDialogDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserTackBond(Float.valueOf(moneyBack.getText().toString().trim()));
                builder.dismiss();
            }
        });
    }

    private void UserTackBond( float money) {
        neType = 2;
        UserTackBondApi bondApi = new UserTackBondApi();
        bondApi.setHeader(MyApplication.authorization);
        bondApi.setUserId(SPUtils.getUser_ID(this));
        bondApi.setAmount(String.valueOf(money));
        pClass.startHttpRequest(this, bondApi);
    }

    /**
     * 获取用户资金
     */
    private void getUserAccount() {
        HttpUtil.showProgress(context, "刷新中...");
        neType = 0;
        GetUserAccountApi gvml = new GetUserAccountApi();
        gvml.setHeader(MyApplication.authorization);
        gvml.setUserid(SPUtils.getUser_ID(context));
        pClass.startHttpRequest(this, gvml);
    }

    /**
     * 请求服务端,根据保证金ID获取资金流水
     */
    private void queryAccountListBZJ(String userId, int page, int row) {
        neType = 1;
        QuereyAccountBZJApi fapiaoApi = new QuereyAccountBZJApi();
        fapiaoApi.setHeader(MyApplication.authorization);
        fapiaoApi.setUserId(userId);
        fapiaoApi.setPage(String.valueOf(page));
        fapiaoApi.setRow(String.valueOf(row));
        pClass.startHttpRequest(this, fapiaoApi);
    }

    @Override
    public void onSuccess(String data) {
        if (neType == 0) {
            UserInfo userInfo = new Gson().fromJson(data, UserInfo.class);
            String allMoney = BigDecimalUtils.add(userInfo.getBalance(), userInfo.getBlocked(), 2);

            mBondTextMoney.setText("" + userInfo.getBondPrice());

            if (!TextUtils.isEmpty(userInfo.getUser_ID())) {
                queryAccountListBZJ(userInfo.getUser_ID(), page, row);
            } else {
                HttpUtil.dismissProgress();
            }

        } else if (neType == 1) {
            HttpUtil.dismissProgress();
            JSONObject jo = JSONObject.parseObject(data);
            if (jo == null) {
                if (isFresh) {
                    mSrlAccountList.finishRefresh(true);
                } else {
                    mSrlAccountList.finishLoadMore(true);
                }
                return;
            }
            String success = jo.getString("Success");
            if (success.equals("true")) {
                JSONObject object = JSONObject.parseObject(jo.getString("Data"));
                List<Account> list = JSONObject.parseArray(object.getString("List"), Account.class);
                if (isFresh) {
                    mSrlAccountList.finishRefresh(true);
                    if (!accountList.isEmpty()) {
                        accountList.clear();
                    }
                    if (list.isEmpty()) {
                        mTvNull.setVisibility(View.VISIBLE);
                        mLvAccount.setVisibility(View.GONE);
                    } else {
                        mTvNull.setVisibility(View.GONE);
                        mLvAccount.setVisibility(View.VISIBLE);
                    }
                    accountList.addAll(list);
                    mLvAccount.setAdapter(meAdapter);
                    meAdapter.notifyDataSetChanged();
                } else {
                    if (list.isEmpty()) {
                        // 完成加载并标记没有更多数据
                        mSrlAccountList.finishLoadMoreWithNoMoreData();
                        return;
                    }
                    accountList.addAll(list);
                    mLvAccount.setAdapter(meAdapter);
                    meAdapter.notifyDataSetChanged();
                    mLvAccount.setSelection(accountList.size());
                    mSrlAccountList.finishLoadMore(true);
                }
            } else {
                if (isFresh) {
                    mSrlAccountList.finishRefresh(true);
                } else {
                    mSrlAccountList.finishLoadMore(true);
                }
                mTvNull.setVisibility(View.VISIBLE);
                mLvAccount.setVisibility(View.GONE);
                ProjectUtil.show(context, jo.getString("Message"));
            }
        } else if (neType == 2){
            ProjectUtil.show(this, "保证金提现成功！");
            getUserAccount();
        }
    }


    @Override
    public void onError(ApiException e) {
        super.onError(e);
        HttpUtil.dismissProgress();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isFresh = false;
        page = page + 1;
        HttpUtil.showProgress(context, "加载中...");
        queryAccountListBZJ(SPUtils.getUser_ID(context), page, row);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isFresh = true;
        page = 1;
        getUserAccount();
    }
}
