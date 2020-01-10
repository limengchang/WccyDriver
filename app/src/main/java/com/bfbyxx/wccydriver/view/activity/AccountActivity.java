package com.bfbyxx.wccydriver.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.adapter.AccountAdapter;
import com.bfbyxx.wccydriver.api.GetUserAccountApi;
import com.bfbyxx.wccydriver.api.QuereyAccountZjApi;
import com.bfbyxx.wccydriver.application.MyApplication;
import com.bfbyxx.wccydriver.base.BaseActivity;
import com.bfbyxx.wccydriver.entity.Account;
import com.bfbyxx.wccydriver.entity.UserInfo;
import com.bfbyxx.wccydriver.utils.BigDecimalUtils;
import com.bfbyxx.wccydriver.utils.HttpUtil;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.bfbyxx.wccydriver.utils.SPUtils;
import com.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//账号余额信息页面
public class AccountActivity extends BaseActivity implements OnRefreshLoadMoreListener {
    private Context context;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.lv_account)
    ListView lv_account;
    @BindView(R.id.tv_account_yue)
    TextView tv_account_yue;
    @BindView(R.id.tv_account_djzj)
    TextView tv_account_djzj;
    @BindView(R.id.tv_account_kyzj)
    TextView tv_account_kyzj;
    @BindView(R.id.srl_account_list)
    SmartRefreshLayout srl_account_list;
    @BindView(R.id.tv_null)
    TextView tv_null;

    private AccountAdapter meAdapter;
    private List<Account> accountList = new ArrayList<>();
    private int row = 10;
    private int page = 1;
    private boolean isFresh = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        context = AccountActivity.this;
        tv_title.setText("账户余额信息");
        tv_right.setText("银行卡");
        srl_account_list.setOnRefreshLoadMoreListener(this);
        meAdapter = new AccountAdapter(context, accountList);
    }
    @Override
    protected void onResume() {
        super.onResume();
        getUserAccount();
    }
    @OnClick({R.id.tv_back, R.id.tv_right, R.id.btn_cz})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                //返回
                finish();
                break;
            case R.id.tv_right:
                //银行卡绑定页面
                startActivity(new Intent(context, BankActivity.class));
                break;
            case R.id.btn_cz:
                //充值
                Intent intent = new Intent(context, WalletChargeActivity.class);
                intent.putExtra("isBond", false);
                startActivity(intent);
                break;
        }
    }

    /**
     * 获取用户资金
     */
    private void getUserAccount() {
        HttpUtil.showProgress(context,"刷新中...");
        neType = 0;
        GetUserAccountApi gvml = new GetUserAccountApi();
        gvml.setHeader(MyApplication.authorization);
        gvml.setUserid(SPUtils.getUser_ID(context));
        pClass.startHttpRequest(this, gvml);
    }

    /**
     * 请求服务端,根据资金ID获取资金流水
     */
    private void queryAccountListZj(String accountId, int page, int row) {
        neType = 1;
        QuereyAccountZjApi fapiaoApi = new QuereyAccountZjApi();
        fapiaoApi.setHeader(MyApplication.authorization);
        fapiaoApi.setAccountId(accountId);
        fapiaoApi.setPage(String.valueOf(page));
        fapiaoApi.setRow(String.valueOf(row));
        pClass.startHttpRequest(this, fapiaoApi);
    }

    @Override
    public void onSuccess(String data) {
        if (neType == 0) {
            UserInfo userInfo = new Gson().fromJson(data, UserInfo.class);
            String allMoney = BigDecimalUtils.add(userInfo.getBalance(), userInfo.getBlocked(), 2);
            tv_account_yue.setText(allMoney);
            tv_account_djzj.setText("冻结资金：" + userInfo.getBlocked());
            tv_account_kyzj.setText("可用资金：" + userInfo.getBalance());
            if (userInfo.getAccountID() != null) {
                queryAccountListZj(userInfo.getAccountID(), page, row);
            }else {
                HttpUtil.dismissProgress();
            }
        } else if (neType == 1) {
            HttpUtil.dismissProgress();
            JSONObject jo = JSONObject.parseObject(data);
            if (jo == null) {
                if (isFresh) {
                    srl_account_list.finishRefresh(true);
                } else {
                    srl_account_list.finishLoadMore(true);
                }
                tv_null.setVisibility(View.VISIBLE);
                lv_account.setVisibility(View.GONE);
                return;
            }
            String success = jo.getString("Success");
            if (success.equals("true")) {
                JSONObject object = JSONObject.parseObject(jo.getString("Data"));
                List<Account> list = JSONObject.parseArray(object.getString("List"), Account.class);
                if (isFresh) {
                    srl_account_list.finishRefresh(true);
                    if (!accountList.isEmpty()) {
                        accountList.clear();
                    }
                    if (list.isEmpty()) {
                        tv_null.setVisibility(View.VISIBLE);
                        lv_account.setVisibility(View.GONE);
                    } else {
                        tv_null.setVisibility(View.GONE);
                        lv_account.setVisibility(View.VISIBLE);
                    }
                    accountList.addAll(list);
                    lv_account.setAdapter(meAdapter);
                    meAdapter.notifyDataSetChanged();
                } else {
                    if (list.isEmpty()) {
                        // 完成加载并标记没有更多数据
                        srl_account_list.finishLoadMoreWithNoMoreData();
                        return;
                    }
                    accountList.addAll(list);
                    lv_account.setAdapter(meAdapter);
                    meAdapter.notifyDataSetChanged();
                    lv_account.setSelection(accountList.size());
                    srl_account_list.finishLoadMore(true);
                }
            } else {
                if (isFresh) {
                    srl_account_list.finishRefresh(true);
                } else {
                    srl_account_list.finishLoadMore(true);
                }
                tv_null.setVisibility(View.VISIBLE);
                lv_account.setVisibility(View.GONE);
                ProjectUtil.show(context, jo.getString("Message"));
            }
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
        HttpUtil.showProgress(context,"加载中...");
        queryAccountListZj(SPUtils.getAccountID(context), page, row);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isFresh = true;
        page = 1;
        getUserAccount();
//        queryAccountListZj(SPUtils.getAccountID(context), page, row);
    }
}
