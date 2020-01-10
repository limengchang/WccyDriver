package com.bfbyxx.wccydriver.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.adapter.OrderAdapter;
import com.bfbyxx.wccydriver.api.QuereyOrderList;
import com.bfbyxx.wccydriver.application.MyApplication;
import com.bfbyxx.wccydriver.base.BaseFragment;
import com.bfbyxx.wccydriver.entity.Order;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.bfbyxx.wccydriver.utils.SPUtils;
import com.bfbyxx.wccydriver.view.activity.WebActivity;
import com.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.alibaba.fastjson.JSON.parseArray;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 11:25
 */
public class PeihuoFragment extends BaseFragment implements OnRefreshLoadMoreListener, AdapterView.OnItemClickListener {
    Unbinder unbinder;
    private View view;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_null)
    TextView tv_null;
    @BindView(R.id.wb_peihuo)
    WebView wb_peihuo;

    private List<Order> motoList = new ArrayList<>();//全部车源数据列表
    OrderAdapter orderAdapter;
    private int row = 10;
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_peihuo, container, false);
        unbinder = ButterKnife.bind(this, view);
        tv_back.setVisibility(View.GONE);
        tv_title.setText("配货大厅-待接单");
//        Tools.showWebView(wb_peihuo, MyApplication.WebViewIP+"Weixin_Driver/WaitOrder",mActivity);
        return view;
    }

    private void initOnRefreshLoad() {
//        tv_back.setVisibility(View.GONE);
//        tv_title.setText("配货大厅-待接单");
        lv.setOnItemClickListener(this);
        orderAdapter = new OrderAdapter(mActivity, motoList);
        requestGetDriverVehicles(0,page);
        srl.setOnRefreshLoadMoreListener(this);
    }

    /**
     * 请求服务,获取订单ID获取运单信息列表
     */
    private void requestGetDriverVehicles(int po,int page) {
        neType = po;
        QuereyOrderList quereyOrderList = new QuereyOrderList();
        quereyOrderList.setHeader(MyApplication.authorization);
        quereyOrderList.setDriverId(SPUtils.getDriver_ID(mActivity));
        quereyOrderList.setUserID(SPUtils.getUser_ID(mActivity));
        quereyOrderList.setPage(String.valueOf(page));
        quereyOrderList.setRows(String.valueOf(row));
        quereyOrderList.setOrderStatus("1");//1待接单 2进行中 7 已结束
        pClass.startHttpRequest(mActivity, quereyOrderList);
    }

    @Override
    public void onSuccess(String data) {
        ProjectUtil.dismissProgress();
        JSONObject jo = JSONObject.parseObject(data);
        JSONObject strData = JSONObject.parseObject(jo.getString("Data"));
        List<Order> list = JSONObject.parseArray(strData.getString("List"), Order.class);
        if (neType == 0) { //获取全部车源列表成功
            if (!motoList.isEmpty()) {
                motoList.clear();
            }
            if (list.isEmpty()) {
                tv_null.setVisibility(View.VISIBLE);
                lv.setVisibility(View.GONE);
            } else {
                tv_null.setVisibility(View.GONE);
                lv.setVisibility(View.VISIBLE);
            }
            motoList.addAll(list);
            lv.setAdapter(orderAdapter);
            orderAdapter.notifyDataSetChanged();
            srl.finishRefresh(true);
        }else if(neType == 1){
            if (list.isEmpty()) {
                // 完成加载并标记没有更多数据
                srl.finishLoadMoreWithNoMoreData();
                return;
            }
            motoList.addAll(list);
            lv.setAdapter(orderAdapter);
            orderAdapter.notifyDataSetChanged();
            lv.setSelection(motoList.size());
            srl.finishLoadMore(true);
        }
    }

    @Override
    public void onError(ApiException e) {

    }

    @Override
    public void onResume() {
        super.onResume();
        initOnRefreshLoad();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        ProjectUtil.showProgress(getContext(),"加载中...");
        page = page + 1;
        requestGetDriverVehicles(1,page);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        ProjectUtil.showProgress(mActivity,"刷新中...");
        page = 1;
        requestGetDriverVehicles(0,page);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mActivity, WebActivity.class);
        intent.putExtra("strTitle","待接单详情");
        intent.putExtra("strRight","");
        intent.putExtra("strUrl",MyApplication.WebViewIP+"Weixin_Driver/WaitOrderInfo?wbId="+motoList.get(position).getId());
        startActivity(intent);
    }
}
