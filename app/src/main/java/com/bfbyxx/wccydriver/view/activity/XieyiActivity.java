package com.bfbyxx.wccydriver.view.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XieyiActivity extends BaseActivity {
    private Context context;
    @BindView(R.id.wb_xieyi)
    WebView wb_xieyi;
    @BindView(R.id.tv_title)
    TextView tv_title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xieyi);
        ButterKnife.bind(this);
        context = XieyiActivity.this;
        tv_title.setText("协议内容");
        init();

    }

    private void init() {
        //获取webSettings
        WebSettings settings = wb_xieyi.getSettings();
        //让webView支持JS
        settings.setJavaScriptEnabled(true);
        //加载百度网页
        wb_xieyi.loadUrl("http://www.baidu.com");
        wb_xieyi.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                CookieManager instance = CookieManager.getInstance();
                //这样就可以获取到Cookie了
                String cookie = instance.getCookie(url);
            }
        });
        wb_xieyi.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        //这里处理返回键事件
//                        wv_html.evaluateJavascript("javascript:phoneBackButtonListener()", new ValueCallback<String>() {
//                            @Override
//                            public void onReceiveValue(String value) {
//                                // value的值为"true"时，H5页面屏蔽手机返回键
//                                // value的值为"false"或"null"时，H5页面不屏蔽手机返回键
//                                // phoneBackButtonListener()未定义或没有返回任何数据，则value的值为"null"
//                                if ("false".equals(value) || "null".equals(value)) {
//                                    // 执行原生的处理逻辑
//                                    wv_html.goBack();
//                                }else {
//                                    ProjectUtil.show(mActivity,"即将退出");
//                                }
//                            }
//                        });
                        if (wb_xieyi.canGoBack()) {
                            wb_xieyi.goBack();
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
                return false;
            }
        });
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
