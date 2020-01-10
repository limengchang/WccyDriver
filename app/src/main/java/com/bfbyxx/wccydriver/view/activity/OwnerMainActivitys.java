package com.bfbyxx.wccydriver.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.bar.BottomBar;
import com.bfbyxx.wccydriver.base.BaseActivity;
import com.bfbyxx.wccydriver.utils.CheckPermissionsUtil;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.bfbyxx.wccydriver.view.fragment.MeFragment;
import com.bfbyxx.wccydriver.view.fragment.WebPeihuoFragment;
import com.bfbyxx.wccydriver.view.fragment.WebYundanFragment;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 10:23
 */
public class OwnerMainActivitys extends BaseActivity {
    public static Activity _this;
    public static RelativeLayout rLayout_bottom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_mains);
        _this = this;
        rLayout_bottom = findViewById(R.id.rLayout_bottom);
        initView();
    }

    private void initView() {
        BottomBar bottomBar = findViewById(R.id.bottom_bar);
        bottomBar.setContainer(R.id.fl_container)
                .setTitleBeforeAndAfterColor("#080808", "#2C5BB3")
                .addItem(WebPeihuoFragment.class,
                        "配货大厅",
                        R.drawable.listnormal2x,
                        R.drawable.listselected2x)
                .addItem(WebYundanFragment.class,
                        "运单管理",
                        R.drawable.alaremnormal2x,
                        R.drawable.alaremselected2x)
                .addItem(MeFragment.class,
                        "个人中心",
                        R.drawable.nav_icon_chat_nor2x,
                        R.drawable.nav_icon_chat_pre2x)
                .build();
    }

    public static void showBottom(int view) {
        rLayout_bottom.setVisibility(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckPermissionsUtil cpu = new CheckPermissionsUtil(this);
        cpu.resumeCheck();
    }


    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ProjectUtil.show(context, "再按一次退出程序");
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

