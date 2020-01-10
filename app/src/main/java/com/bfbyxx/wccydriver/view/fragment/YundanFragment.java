package com.bfbyxx.wccydriver.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.adapter.YundanAdapter;
import com.bfbyxx.wccydriver.api.QuereyOrderList;
import com.bfbyxx.wccydriver.application.MyApplication;
import com.bfbyxx.wccydriver.base.BaseFragment;
import com.bfbyxx.wccydriver.entity.Order;
import com.bfbyxx.wccydriver.utils.HttpUtil;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.bfbyxx.wccydriver.utils.SPUtils;
import com.bfbyxx.wccydriver.view.activity.WebOrderActivity;
import com.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 10:31
 */
public class YundanFragment extends BaseFragment implements OnRefreshLoadMoreListener, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemClickListener {
    private Unbinder unbinder;
    private View view;

    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_null)
    TextView tv_null;
    @BindView(R.id.lv_myorder)
    ListView lv_myorder;
    @BindView(R.id.srl_order_list)
    SmartRefreshLayout srl_order_list;
    private List<Order> orderList = new ArrayList<>();
    @BindView(R.id.rg_main)
    RadioGroup rg_main;

    private String orderType="2";
    private int row = 10;
    private int page = 1;
    YundanAdapter meAdapter;
    private boolean isFresh = true;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                HashMap<String, String> map = (HashMap<String, String>) msg.obj;
                String msgTxt = map.get("aName");
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_yundan, container, false);
        unbinder = ButterKnife.bind(this, view);
        tv_back.setVisibility(View.GONE);
        tv_title.setText("运单管理");
        init();
        return view;
    }
    private void init() {
        srl_order_list.setOnRefreshLoadMoreListener(this);
        rg_main.setOnCheckedChangeListener(this);
        tv_back.setVisibility(View.GONE);
        lv_myorder.setVisibility(View.VISIBLE);
        lv_myorder.setOnItemClickListener(this);
        tv_title.setText("运单管理");
        meAdapter = new YundanAdapter(mActivity, orderList);
    }

    /**
     * 请求服务端,获取订单ID获取运单信息列表
     */
    private void quereyOrderList(int page,String type) {
        HttpUtil.showProgress(mActivity, "加载中...");
        neType = 0;
//        QuereyOrderList quereyOrderList = new QuereyOrderList();
//        quereyOrderList.setHeader(MyApplication.authorization);
//        quereyOrderList.setUserID(SPUtils.getUser_ID(mActivity));
//        quereyOrderList.setPage(String.valueOf(page));
//        quereyOrderList.setRows(String.valueOf(row));
//        quereyOrderList.setOrderStatus(type);//0全部，1发布中，2进行中
//        pClass.startHttpRequest(mActivity, quereyOrderList);

        QuereyOrderList quereyOrderList = new QuereyOrderList();
        quereyOrderList.setHeader(MyApplication.authorization);
        quereyOrderList.setDriverId(SPUtils.getDriver_ID(mActivity));
        quereyOrderList.setUserID(SPUtils.getUser_ID(mActivity));
        quereyOrderList.setPage(String.valueOf(page));
        quereyOrderList.setRows(String.valueOf(row));
        quereyOrderList.setOrderStatus(type);//1待接单 2进行中 7 已结束
        pClass.startHttpRequest(mActivity, quereyOrderList);
    }

    @Override
    public void onSuccess(String data) {
        HttpUtil.dismissProgress();
        JSONObject jo = JSONObject.parseObject(data);
        if (neType == 0) {
            String success = jo.getString("Success");
            if (success.equals("true")) {
                JSONObject strData = JSONObject.parseObject(jo.getString("Data"));
                List<Order> list = JSONObject.parseArray(strData.getString("List"), Order.class);
                if (isFresh) {
                    srl_order_list.finishRefresh(true);
                    if (!orderList.isEmpty()) {
                        orderList.clear();
                    }
                    if (list.isEmpty()) {
                        tv_null.setVisibility(View.VISIBLE);
                        lv_myorder.setVisibility(View.GONE);
                    } else {
                        tv_null.setVisibility(View.GONE);
                        lv_myorder.setVisibility(View.VISIBLE);
                    }
                    orderList.addAll(list);
                    lv_myorder.setAdapter(meAdapter);
                    meAdapter.notifyDataSetChanged();
                }else {
                    if (list.isEmpty()) {
                        // 完成加载并标记没有更多数据
                        srl_order_list.finishLoadMoreWithNoMoreData();
                        return;
                    }
                    orderList.addAll(list);
                    lv_myorder.setAdapter(meAdapter);
                    meAdapter.notifyDataSetChanged();
                    lv_myorder.setSelection(orderList.size());
                    srl_order_list.finishLoadMore(true);
                }
            } else {
                if (isFresh) {
                    srl_order_list.finishRefresh(true);
                }else {
                    srl_order_list.finishLoadMore(true);
                }
                tv_null.setVisibility(View.VISIBLE);
                lv_myorder.setVisibility(View.GONE);
                ProjectUtil.show(mActivity, jo.getString("Message"));
            }
        }else if(neType == 1){
            JSONObject strData = JSONObject.parseObject(jo.getString("Data"));
            List<Order> list = JSONObject.parseArray(strData.getString("List"), Order.class);
            if (list.isEmpty()) {
                // 完成加载并标记没有更多数据
                srl_order_list.finishLoadMoreWithNoMoreData();
                return;
            }
            orderList.addAll(list);
            lv_myorder.setAdapter(meAdapter);
            meAdapter.notifyDataSetChanged();
            lv_myorder.setSelection(orderList.size());
            srl_order_list.finishLoadMore(true);
        }
    }


    @Override
    public void onError(ApiException e) {
        super.onError(e);
    }


    @Override
    public void onResume() {
        super.onResume();
        quereyOrderList(page,orderType);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getCheckedRadioButtonId()) {
            case R.id.rb_jxz:
                //进行中
                isFresh = true;
                orderType = "2";
                page = 1;
                quereyOrderList(page,orderType);
                break;
            case R.id.rb_yjs:
                //已结束
                isFresh = true;
                orderType = "7";
                page = 1;
                quereyOrderList(page,orderType);
                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isFresh = true;
        page = 1;
        quereyOrderList(page,orderType);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isFresh = false;
        page = page + 1;
        quereyOrderList(page,orderType);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent(mActivity, WebActivity.class);
//        intent.putExtra("strTitle","运单详情");
//        intent.putExtra("strRight","");
//        intent.putExtra("strUrl",MyApplication.WebViewIP+"Weixin_Waybill/WaybillInfo?wbId="+orderList.get(position).getId());
//        startActivity(intent);

        Intent intent = new Intent(mActivity, WebOrderActivity.class);
        intent.putExtra("strTitle","运单详情");
        intent.putExtra("strRight","申述");
        intent.putExtra("strUrl",orderList.get(position).getId());
        intent.putExtra("strOrNo",orderList.get(position).getWaybillNo());
        startActivity(intent);
    }
}

