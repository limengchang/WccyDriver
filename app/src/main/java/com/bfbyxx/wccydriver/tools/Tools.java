package com.bfbyxx.wccydriver.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSONObject;
import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.entity.LoginCookie;
import com.bfbyxx.wccydriver.utils.SPUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 10:49
 */
public class Tools {
    /**
     * 加载本地图片
     * http://bbs.3gstdy.com
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(Context context, String url) {
        String imgUrl = "";
        if (TextUtils.isEmpty(url)){
            if (SPUtils.getHeadImg(context).equals("test")){
                imgUrl = "";
            }else {
                imgUrl = SPUtils.getHeadImg(context);
            }
        }else {
            SPUtils.setParam(context,"headImg",url);
            imgUrl = url;
        }
        try {
            FileInputStream fis = new FileInputStream(imgUrl);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Bitmap bmp=BitmapFactory.decodeResource(context.getResources(), R.drawable.head);
            return bmp;
        }
    }

    public static void showWebView(final WebView webView, String url,final Context context){
        //获取webSettings
        WebSettings settings = webView.getSettings();
        //让webView支持JS
        settings.setJavaScriptEnabled(true);
        //加载百度网页
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (title != null) {
                    if (title.contains("404") || title.contains("System Error")|| title.contains("找不到")) {
                        //加载错误显示的页面
//                        ProjectUtil.show(mActivity,"cuowu");
//                        view.stopLoading();
//                        view.loadUrl("www.baidu.com");
//                        wbTrue = false;
                        webView.setVisibility(View.VISIBLE);
                        webView.setVisibility(View.GONE);
                    } else {
//                        tvTitle.setText(title);
//                        ProjectUtil.show(mActivity,"chenggong");
//                        wbTrue = true;
                        webView.setVisibility(View.GONE);
                        webView.setVisibility(View.VISIBLE);
                    }
                }
                super.onReceivedTitle(view, title);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                CookieSyncManager.createInstance(context);
                CookieManager cookieManager = CookieManager.getInstance();
                // 设置接收第三方Cookie
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    CookieManager.getInstance().setAcceptThirdPartyCookies(view, true);
//                }
//                cookieManager.setAcceptCookie(true);
//                cookieManager.removeSessionCookie();// 移除
                cookieManager.removeAllCookie();

//                //创建CookieSyncManager 参数是上下文
//                CookieSyncManager.createInstance(context);
//                //得到CookieManager
//                CookieManager cookieManager = CookieManager.getInstance();
//                //得到向URL中添加的Cookie的值
                //使用cookieManager..setCookie()向URL中添加Cookie
                cookieManager.setCookie(url, "android_true=1");
                cookieManager.setCookie(url, "UserId=" + SPUtils.getUser_ID(context));
                cookieManager.setCookie(url, "UserName="+SPUtils.getUser_Name(context));
                cookieManager.setCookie(url, "LoginName="+SPUtils.getUser_LoginName(context));
                cookieManager.setCookie(url, "UserType=3");//2货主，3司机
                cookieManager.setCookie(url, "UserPhone="+SPUtils.getUser_Phone(context));
                CookieSyncManager.getInstance().sync();


//                //获取cookie
//                CookieManager instance = CookieManager.getInstance();
//                //这样就可以获取到Cookie了
//                String cookie = instance.getCookie(url);

//                // 移除指定url下的指定Cookie
//                CookieManager.getInstance().setCookie(url,"android_true=");
//                view.clearHistory();//清除历史记录
            }
        });
        webView.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK ) {
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
                        if (webView.canGoBack()){
                            webView.goBack();
                            return true;
                        }else {
//                            context.finish();
                        }
                    }
                }
                return false;
            }
        });
    }

    /**
     * 解析Cookie
     */
    public static LoginCookie getLoginCookie(String cookieStr) {
        HashMap<String, String> cookieContiner = new HashMap<>();
        String[] keyvalues = cookieStr.split(";");
        for (int i = 0; i < keyvalues.length; i++) {
            String[] keyPair = keyvalues[i].split("=");
            String cookieKey = keyPair[0].trim();
            String cookieValue = keyPair.length > 1 ? keyPair[1].trim() : "";
            cookieContiner.put(cookieKey, cookieValue);
        }
        String cookie = JSONObject.toJSONString(cookieContiner);
        LoginCookie loginCookie = JSONObject.parseObject(cookie, LoginCookie.class);
        return loginCookie;
    }

    /**
     * 根据银行卡的类型代码,获取银行卡的类型文字描述
     */
    public static String getBankCardTypeByCode(String code){
        String text="未知的卡类型";
        if(code.equals("DC")){
            text = "储蓄卡";
        }else if(code.equals("CC")){
            text = "信用卡";

        }else if(code.equals("SCC")){
            text = "准贷记卡";

        }else if(code.equals("PC")){
            text = "预付费卡";

        }
        return text;
    }

    public static String waybillStatus(String status) {
        String orderStatus = "";
        if (status.equals("0") || status.equals("1")) {
            orderStatus = "待确认";
        } else if (status.equals("2")) {
            orderStatus = "待装货";
        } else if (status.equals("3")) {
            orderStatus = "确认起运";
        } else if (status.equals("4")) {
            orderStatus = "确认到达";
        } else if (status.equals("5")) {
            orderStatus = "待卸货";
        } else if (status.equals("6")) {
            orderStatus = "确认完成";
        } else if (status.equals("7")) {
            orderStatus = "已完成";
        } else if (status.equals("8")) {
            orderStatus = "异常订单";
        }
        return orderStatus;
    }

    public static String getXianjieType(String code){
        //1现结2月结3合同结
        String text="";
        if(code.equals("1")){
            text = "现结";
        }else if(code.equals("2")){
            text = "月结";
        }else if(code.equals("3")){
            text = "合同结";
        }
        return text;
    }

    public static String getOrderStatus(String status) {
        String orderStatus = "";
        if (TextUtils.isEmpty(status)){
            return orderStatus = "申述";
        }
        if (status.equals("2")||status.equals("7")) {
            orderStatus = "申述";
        }
        return orderStatus;
    }
    /**
     * 根据类型获取是充值还是提现记录
     */
    public static String getAccountReturon(String code) {
        String text = "未知";
        if (TextUtils.isEmpty(code)) {
            return text;
        }
        if (code.equals("1")) {
            text = "充值";
        } else if (code.equals("2")) {
            text = "提现";

        } else {
            text = "未知";

        }
        return text;
    }

}

