package com.bfbyxx.wccydriver.view.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewMainActivity extends BaseActivity {
    @BindView(R.id.wb_mian)
    WebView wb_mian;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //获取webSettings
        WebSettings settings = wb_mian.getSettings();
        //让webView支持JS
        settings.setJavaScriptEnabled(true);
        //是否可以后退
//        wv_html.canGoBack(); m
        //后退网页
//        wb_mian.goBack();
        //加载百度网页
        wb_mian.loadUrl("http://192.168.1.242:8091/Weixin_User/Login");
        wb_mian.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                CookieManager instance = CookieManager.getInstance();
                //这样就可以获取到Cookie了
                String cookie = instance.getCookie(url);
            }
        });
//        wb_mian.setOnKeyListener(new View.OnKeyListener() {
//
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//                        //这里处理返回键事件
////                        wv_html.evaluateJavascript("javascript:phoneBackButtonListener()", new ValueCallback<String>() {
////                            @Override
////                            public void onReceiveValue(String value) {
////                                // value的值为"true"时，H5页面屏蔽手机返回键
////                                // value的值为"false"或"null"时，H5页面不屏蔽手机返回键
////                                // phoneBackButtonListener()未定义或没有返回任何数据，则value的值为"null"
////                                if ("false".equals(value) || "null".equals(value)) {
////                                    // 执行原生的处理逻辑
////                                    wv_html.goBack();
////                                }else {
////                                    ProjectUtil.show(mActivity,"即将退出");
////                                }
////                            }
////                        });
//                        if (wb_mian.canGoBack()) {
//                            wb_mian.goBack();
//                            return true;
//                        } else {
//                            return false;
//                        }
//                    }
//                }
//                return false;
//            }
//        });
    }


    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                if (wb_mian.canGoBack()) {
                    wb_mian.goBack();
                    return true;
                } else {
                    Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                    return false;
                }
            } else {

                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
