package com.bfbyxx.wccydriver.view.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.api.GetVehicleModeListApi;
import com.bfbyxx.wccydriver.application.MyApplication;
import com.bfbyxx.wccydriver.base.BaseActivity;
import com.bfbyxx.wccydriver.entity.CarType;
import com.bfbyxx.wccydriver.utils.CheckPermissionsUtil;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.bfbyxx.wccydriver.view.fragment.MeFragment;
import com.bfbyxx.wccydriver.view.fragment.WebPeihuoFragment;
import com.bfbyxx.wccydriver.view.fragment.WebYundanFragment;
import com.rxretrofitlibrary.retrofit_rx.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 10:23
 */
public class OwnerMainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{
    public static Activity _this;
    @BindView(R.id.broker_rb_fahuo)
    RadioButton brokerRbFahuo;
    @BindView(R.id.broker_rb_dingdan)
    RadioButton brokerRbDingdan;
    @BindView(R.id.broker_rb_me)
    RadioButton brokerRbMe;

    private Fragment currentFragment = new Fragment();
    private FragmentManager manager;
//    private PeihuoFragment peihuoFragment;
//    private YundanFragment yundanFragment;
    private WebYundanFragment webYundanFragment;
    private WebPeihuoFragment webPeihuoFragment;
    private MeFragment meFragment;

    public static LinearLayout layout_bottom;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main);
        ButterKnife.bind(this);
        _this = this;
        initView();
//        requestVehicleModeList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckPermissionsUtil cpu = new CheckPermissionsUtil(this);
        cpu.resumeCheck();
    }

    /**
     * 请求服务端,获取车辆类型
     */
    private void requestVehicleModeList(){
        neType = 0;
        GetVehicleModeListApi gvml = new GetVehicleModeListApi();
        gvml.setHeader(MyApplication.authorization);
        gvml.setKeyid("3696DB9C-46C5-4A6E-89BC-B725F9B70D3D");
        pClass.startHttpRequest(this, gvml);
    }
    @Override
    public void onSuccess(String data) {
        if (neType == 0) {//车辆类型
            List<CarType> carLsit=new Gson().fromJson(data, new TypeToken<ArrayList<CarType>>(){}.getType());
//            app.setCartypeLsit(cartypeLsit);
            CarType cType = new CarType();
            cType.setId("0");
            cType.setText("全部");
            carLsit.add(0,cType);
//            String strList = cartypeLsit.toString();
//            SPUtils.setParam(this,"", cartypeLsit);
            MyApplication.cartypeLsit = carLsit;
        }
    }
    @Override
    public void onError(ApiException e) {
        super.onError(e);
    }

    private void initView() {
        layout_bottom = findViewById(R.id.layout_bottom);
        manager = getSupportFragmentManager();
        RadioGroup bottomMenu = findViewById(R.id.broker_bottomMenu);
//        peihuoFragment = new PeihuoFragment();
//        yundanFragment = new YundanFragment();
        webPeihuoFragment = new WebPeihuoFragment();
        webYundanFragment = new WebYundanFragment();
        meFragment = new MeFragment();
        bottomMenu.setOnCheckedChangeListener(this);
        bottomMenu.check(R.id.broker_rb_fahuo);
    }

    public static void showBottom(int view){
        layout_bottom.setVisibility(view);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.broker_rb_fahuo:
//                showFragment(peihuoFragment);
                showFragment(webPeihuoFragment);
                brokerRbFahuo.setTextColor(Color.parseColor("#295296"));
                brokerRbDingdan.setTextColor(Color.parseColor("#666666"));
                brokerRbMe.setTextColor(Color.parseColor("#666666"));
                break;
            case R.id.broker_rb_dingdan:
//                showFragment(yundanFragment);
                showFragment(webYundanFragment);
                brokerRbFahuo.setTextColor(Color.parseColor("#666666"));
                brokerRbDingdan.setTextColor(Color.parseColor("#295296"));
                brokerRbMe.setTextColor(Color.parseColor("#666666"));
                break;
            case R.id.broker_rb_me:
                showFragment(meFragment);
                brokerRbFahuo.setTextColor(Color.parseColor("#666666"));
                brokerRbDingdan.setTextColor(Color.parseColor("#666666"));
                brokerRbMe.setTextColor(Color.parseColor("#295296"));
                break;
        }
    }

//    public void getFragment(int checkedId) {
//        getSupportFragmentManager().beginTransaction().replace(R.id.broker_layout_content
//                , FragmentFactory.getFragment(checkedId)).commit();
//    }

    private void showFragment(Fragment fragment) {

        if (currentFragment != fragment) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.hide(currentFragment);
            currentFragment = fragment;
            if (!fragment.isAdded()) {
                transaction.add(R.id.broker_layout_content, fragment).show(fragment).commit();
            } else {
                transaction.show(fragment).commit();
            }
        }
    }


    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ProjectUtil.show(context,"再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

