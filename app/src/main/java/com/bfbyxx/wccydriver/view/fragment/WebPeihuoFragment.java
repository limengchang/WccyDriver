package com.bfbyxx.wccydriver.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.application.MyApplication;
import com.bfbyxx.wccydriver.base.BaseFragment;
import com.bfbyxx.wccydriver.entity.LoginCookie;
import com.bfbyxx.wccydriver.tools.Tools;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.bfbyxx.wccydriver.utils.SPUtils;
import com.bfbyxx.wccydriver.view.activity.LoginActivity;
import com.bfbyxx.wccydriver.view.activity.OwnerMainActivitys;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 10:31
 */
public class WebPeihuoFragment extends BaseFragment {
    private Unbinder unbinder;
    private View view;
    private static final int REQUEST_CODE_ALBUM = 0x01;
    private static final int REQUEST_CODE_CAMERA = 0x02;
    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private String mCurrentPhotoPath;
    private String mLastPhothPath;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_null_icon)
    TextView tv_null_icon;
    private String titleTex;
    private List<String> listText = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_web, container, false);
        unbinder = ButterKnife.bind(this, view);
        //进度条初始化
        initProgressBar();
        return view;
    }

    private void initProgressBar() {
        tv_back.setVisibility(View.GONE);
        tv_title.setText("配货大厅");
        mProgressBar.setMax(100);
        //webview初始化
        initWebView(MyApplication.WebViewIP + "Weixin_Driver/Orderhall");
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                //返回
                if (mWebView != null && mWebView.canGoBack()) {
                    mWebView.goBack();
                    if (listText.size()>0){
                        listText.remove(listText.size()-1);
                        tv_title.setText(listText.get(listText.size()-1));
                    }
                    OwnerMainActivitys.showBottom(View.GONE);
                }else {
                    tv_back.setVisibility(View.GONE);
                    tv_title.setText("配货大厅");
                    OwnerMainActivitys.showBottom(View.VISIBLE);
                }
                break;

        }
    }

    private void initWebView(String onlineUrl) {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                tvClose.setVisibility(mWebView.canGoBack() ? View.VISIBLE : View.GONE);
//                mProgressBar.setVisibility(View.GONE);
                //返回
                if (mWebView != null && mWebView.canGoBack()) {
                    tv_back.setVisibility(View.VISIBLE);
                    OwnerMainActivitys.showBottom(View.GONE);
                }else {
                    tv_back.setVisibility(View.GONE);
                    OwnerMainActivitys.showBottom(View.VISIBLE);
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                CookieSyncManager.createInstance(mActivity);
                CookieManager cookieManager = CookieManager.getInstance();
                // 设置接收第三方Cookie
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    CookieManager.getInstance().setAcceptThirdPartyCookies(view, true);
                }
                cookieManager.setAcceptCookie(true);
//            cookieManager.removeSessionCookie();// 移除
//            cookieManager.removeAllCookie();


                //得到向URL中添加的Cookie的值
                //使用cookieManager..setCookie()向URL中添加Cookie
                cookieManager.setCookie(url, "android_true=1");
                cookieManager.setCookie(url, "UserId=" + SPUtils.getUser_ID(mActivity));
                cookieManager.setCookie(url, "UserName=" + SPUtils.getUser_Name(mActivity));
                cookieManager.setCookie(url, "LoginName=" + SPUtils.getUser_LoginName(mActivity));
                cookieManager.setCookie(url, "UserType=3");
                cookieManager.setCookie(url, "UserPhone=" + SPUtils.getUser_Phone(mActivity));
                CookieSyncManager.getInstance().sync();

//                //获取cookie
//                CookieManager instance = CookieManager.getInstance();
//                //这样就可以获取到Cookie了
//                String cookie = instance.getCookie(url);
                String newCookie = cookieManager.getCookie(url);
                LoginCookie cookie = Tools.getLoginCookie(newCookie);
                String strUserId = cookie.getUserId();
                if (strUserId == null) {
                    mActivity.finish();
                    startActivity(new Intent(mActivity, LoginActivity.class));
                    ProjectUtil.show(mActivity,"cookie异常，请重新登录！");
                    return;
                }
                //------使用代码 end-----
                String and_ture = cookie.getAndroid_true();
                String tLoginName = cookie.getLoginName();
                String getUserPhone = cookie.getUserPhone();
                String getUserName = cookie.getUserName();
                String getUserType = cookie.getUserType();

//                // 移除指定url下的指定Cookie
//                CookieManager.getInstance().setCookie(url,"android_true=");
//                view.clearHistory();//清除历史记录
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (title != null) {
                    if (title.contains("404") || title.contains("System Error") || title.contains("找不到") || title.contains("无法找到")) {
                        //加载错误显示的页面
                        tv_null_icon.setVisibility(View.VISIBLE);
                        tv_null_icon.setText(title);
                        mWebView.setVisibility(View.GONE);
                    } else {
                        tv_null_icon.setVisibility(View.GONE);
                        mWebView.setVisibility(View.VISIBLE);
                        titleTex = title.replace(" - 无车承运管理平台","");
                        listText.add(titleTex);
                        tv_title.setText(listText.get(listText.size()-1));
                    }
                }
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            //For Android  >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                uploadMessageAboveL = filePathCallback;
//                uploadPicture();
                chooseAlbumPic();
                return true;
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                uploadMessage = valueCallback;
//                uploadPicture();
                chooseAlbumPic();
            }
        });
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (mWebView != null && mWebView.canGoBack()) {
                            mWebView.goBack();
                            if (listText.size()>0){
                                listText.remove(listText.size()-1);
                                tv_title.setText(listText.get(listText.size()-1));
                            }
                            return true;
                        } else {
//                            context.finish();
                        }
                    }
                }
                return false;
            }
        });
        mWebView.loadUrl(onlineUrl);
    }

    /**
     * 选择相册照片
     */
    private void chooseAlbumPic() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), REQUEST_CODE_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ALBUM || requestCode == REQUEST_CODE_CAMERA) {
            if (uploadMessage == null && uploadMessageAboveL == null) {
                return;
            }
            //取消拍照或者图片选择时
            if (resultCode != RESULT_OK) {
                //一定要返回null,否则<input file> 就是没有反应
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }
                if (uploadMessageAboveL != null) {
                    uploadMessageAboveL.onReceiveValue(null);
                    uploadMessageAboveL = null;
                }
            }
            //拍照成功和选取照片时
            if (resultCode == RESULT_OK) {
                Uri imageUri = null;
                switch (requestCode) {
                    case REQUEST_CODE_ALBUM:
                        if (data != null) {
                            imageUri = data.getData();
                        }
                        break;
                    case REQUEST_CODE_CAMERA:
                        if (!TextUtils.isEmpty(mCurrentPhotoPath)) {
                            File file = new File(mCurrentPhotoPath);
                            Uri localUri = Uri.fromFile(file);
                            Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
                            mActivity.sendBroadcast(localIntent);
                            imageUri = Uri.fromFile(file);
                            mLastPhothPath = mCurrentPhotoPath;
                        }
                        break;
                }
                //上传文件
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(imageUri);
                    uploadMessage = null;
                }
                if (uploadMessageAboveL != null) {
                    uploadMessageAboveL.onReceiveValue(new Uri[]{imageUri});
                    uploadMessageAboveL = null;
                }
            }
        }
    }
}
