package com.bfbyxx.wccydriver.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.application.MyApplication;
import com.bfbyxx.wccydriver.base.BaseActivity;
import com.bfbyxx.wccydriver.entity.MeItem;
import com.bfbyxx.wccydriver.tools.Tools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CarInfoActivity extends BaseActivity {
    private Context context;
    @BindView(R.id.lv_car_info)
    ListView lv_car_info;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.wb_car_info)
    WebView wb_car_info;

    String[] arr_data= {"湘A·99999","湘A·12345","湘A·TS899","湘A·AP123"};
    String[] arr_id= {"10","89","21","6"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info);
        ButterKnife.bind(this);
        context = CarInfoActivity.this;
        tv_title.setText("车辆信息");
        Tools.showWebView(wb_car_info, MyApplication.WebViewIP+"Weixin_Driver/Vehicle",context);
//        CarInfoAdapter meAdapter = new CarInfoAdapter(context, getData());
//        lv_car_info.setAdapter(meAdapter);
//        meAdapter.notifyDataSetChanged();
//        onItemClick();
    }

    private void onItemClick() {
        lv_car_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(context,CarMessageActivity.class);
                    intent.putExtra("car_number",arr_data[position]);
                    intent.putExtra("car_id",arr_id[position]);
                    startActivity(intent);
            }
        });

    }

    private List<MeItem> getData() {
        List<MeItem> dataList = new ArrayList<MeItem>();
        for (int i = 0; i < arr_data.length; i++) {
            MeItem map = new MeItem();
            map.setName(arr_data[i]);
            map.setRight_icon(arr_id[i]);
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

}
