package com.bfbyxx.wccydriver.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.adapter.SetAdapter;
import com.bfbyxx.wccydriver.api.CheckUpdateApi;
import com.bfbyxx.wccydriver.application.MyApplication;
import com.bfbyxx.wccydriver.base.BaseActivity;
import com.bfbyxx.wccydriver.entity.MeItem;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.bfbyxx.wccydriver.utils.UpdateVersionUtils;
import com.bfbyxx.wccydriver.utils.Utils;
import com.rxretrofitlibrary.retrofit_rx.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetActivity extends BaseActivity {
    private Context context;
    @BindView(R.id.lv_set)
    ListView lv_set;
    @BindView(R.id.tv_title)
    TextView tv_title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
        context = SetActivity.this;
        tv_title.setText("设置");
        SetAdapter meAdapter = new SetAdapter(context, getData());
        lv_set.setAdapter(meAdapter);
        meAdapter.notifyDataSetChanged();
        onItemClick();
    }

    private void onItemClick() {
        lv_set.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //修改密码
                        Intent intent = new Intent(context, ForgetActivity.class);
                        intent.putExtra("type", "忘记密码");
                        startActivity(intent);
                        break;
                    case 1:
                        //设置/修改支付密码
//                        Intent intent2 = new Intent(context, SettingPaypwdActivity.class);
//                        intent2.putExtra("setPwd","设置资金密码");
//                        startActivity(intent2);
                        Intent web = new Intent(context, WebActivity.class);
                        web.putExtra("strTitle", "设置资金密码");
                        web.putExtra("strRight", "");
                        web.putExtra("strUrl", MyApplication.WebViewIP + "Weixin_Driver/PayPwdEdit");
                        startActivity(web);
                        break;
                    case 2:
                        checkUpdateVersion();
                        break;
                    case 3:
                        showLoginQuitConfirmDialog();
                        break;
                }
            }
        });

    }

    private List<MeItem> getData() {
        List<MeItem> dataList = new ArrayList<MeItem>();
        String[] arr_data = {"忘记密码", "设置资金密码", "系统更新", "退出系统"};
        for (int i = 0; i < arr_data.length; i++) {
            MeItem map = new MeItem();
            map.setName(arr_data[i]);
            dataList.add(map);
        }
        return dataList;
    }

    @OnClick({R.id.tv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                //返回
                finish();
                break;
        }
    }

    @Override
    public void onSuccess(String data) {
        if (neType == 1) {
            /**********************版本更新*************************/
            JSONObject jsonObject = JSONObject.parseObject(data);
            String url = jsonObject.getString("url");
            String version = jsonObject.getString("version");
            String message = jsonObject.getString("message");
            int oldVersion = Utils.getVersionCode(context);
            int versonCode = Integer.valueOf(version).intValue();
            if (versonCode > oldVersion) {//有新版本
                UpdateVersionUtils.showUpdaloadDialog(this, url, message);
            } else {
                getIntentActivity();
            }
        }
    }

    private void getIntentActivity() {
        ProjectUtil.show(context, "当前已是最新版本");
    }

    @Override
    public void onError(ApiException e) {
        super.onError(e);
    }

    /*******************************版本更新******************************/
    private void checkUpdateVersion() {
        neType = 1;
        CheckUpdateApi loginAccApi = new CheckUpdateApi();
        loginAccApi.setHeader(MyApplication.authorization);
        pClass.startHttpRequest(this, loginAccApi);
    }

    /**
     * 跳转到登录界面
     */
    private void showLoginQuitConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("是否退出系统");

        builder.setNegativeButton("否",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.setPositiveButton("是",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }
}
