package com.bfbyxx.wccydriver.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bfbyxx.wccydriver.BuildConfig;
import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.application.MyApplication;
import com.bfbyxx.wccydriver.base.BaseActivity;
import com.bfbyxx.wccydriver.entity.LoginCookie;
import com.bfbyxx.wccydriver.tools.Tools;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.bfbyxx.wccydriver.utils.SPUtils;

import java.io.File;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebActivity extends BaseActivity {
    private Context context;
    private static final int REQUEST_CODE_ALBUM = 0x01;
    private static final int REQUEST_CODE_CAMERA = 0x02;
    private static final int REQUEST_CODE_PERMISSION_CAMERA = 0x03;
    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private String mCurrentPhotoPath;
    private String mLastPhothPath;
    private Thread mThread;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.tv_null_icon)
    TextView tv_null_icon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        context = WebActivity.this;
        //进度条初始化
        initProgressBar();
    }

    @OnClick({R.id.tv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                //返回
                onBackPressed();
                break;
            case R.id.tv_right:
                String bj = tv_right.getText().toString().trim();
                if (bj.equals("编辑")) {
                    // 无参数调用 JS的方法
                    mWebView.loadUrl("javascript:EditShow();");
                } else if (bj.equals("银行卡")) {
                    //银行卡绑定页面
                    startActivity(new Intent(context, BankActivity.class));
                } else if (bj.equals("添加")) {
                    Intent intent1 = new Intent(context, WebActivity.class);
                    intent1.putExtra("strTitle", "车辆信息");
                    intent1.putExtra("strRight", "");
                    intent1.putExtra("strUrl", MyApplication.WebViewIP + "Weixin_Driver/VehicleAdd");
                    startActivity(intent1);
                } else {
                    tv_right.setText("");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mThread = null;
        mHandler = null;
    }

    private void initProgressBar() {
        String strTitle = getIntent().getStringExtra("strTitle");
        String strRight = getIntent().getStringExtra("strRight");
        String strUrl = getIntent().getStringExtra("strUrl");
        tv_title.setText(strTitle);
        tv_right.setText(strRight);
        mProgressBar.setMax(100);
        //webview初始化
        initWebView(strUrl);
    }

    private void initWebView(String onlineUrl) {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                tvClose.setVisibility(mWebView.canGoBack() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                CookieSyncManager.createInstance(context);
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
                cookieManager.setCookie(url, "UserId=" + SPUtils.getUser_ID(context));
                cookieManager.setCookie(url, "UserName=" + SPUtils.getUser_Name(context));
                cookieManager.setCookie(url, "LoginName=" + SPUtils.getUser_LoginName(context));
                cookieManager.setCookie(url, "UserType=3");
                cookieManager.setCookie(url, "UserPhone=" + SPUtils.getUser_Phone(context));
                CookieSyncManager.getInstance().sync();

//                //获取cookie
//                CookieManager instance = CookieManager.getInstance();
//                //这样就可以获取到Cookie了
//                String cookie = instance.getCookie(url);
                String newCookie = cookieManager.getCookie(url);
                LoginCookie cookie = Tools.getLoginCookie(newCookie);
                String strUserId = cookie.getUserId();
                if (strUserId == null) {
                    finish();
                    OwnerMainActivity._this.finish();
                    startActivity(new Intent(context, LoginActivity.class));
                    ProjectUtil.show(context, "cookie异常，请重新登录！");
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
        mWebView.loadUrl(onlineUrl);
//        mWebView.reload();
    }

    @Override
    public void onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            takePhoto();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults == null && grantResults.length == 0) {
            return;
        }
        if (requestCode == REQUEST_CODE_PERMISSION_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                // Permission Denied
                new AlertDialog.Builder(context)
                        .setTitle("无法拍照")
                        .setMessage("您未授予拍照权限")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent localIntent = new Intent();
                                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                                startActivity(localIntent);
                            }
                        }).create().show();
            }
        }
    }

    /**
     * 选择相机或者相册
     */
    public void uploadPicture() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WebActivity.this);
        builder.setTitle("请选择图片上传方式");
        //取消对话框
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //一定要返回null,否则<input type='file'>
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }
                if (uploadMessageAboveL != null) {
                    uploadMessageAboveL.onReceiveValue(null);
                    uploadMessageAboveL = null;
                }
            }
        });

        builder.setPositiveButton("相机", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(mLastPhothPath)) {
                    //上一张拍照的图片删除
                    mThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            File file = new File(mLastPhothPath);
                            if (file != null) {
                                file.delete();
                            }
                            mHandler.sendEmptyMessage(1);
                        }
                    });
                    mThread.start();
                } else {
                    //请求拍照权限
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        takePhoto();
                    } else {
                        ActivityCompat.requestPermissions(WebActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION_CAMERA);
                    }
                }
            }
        });
        builder.setNegativeButton("相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chooseAlbumPic();
            }
        });
        builder.create().show();
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        StringBuilder fileName = new StringBuilder();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileName.append(UUID.randomUUID()).append("_upload.png");
        File tempFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            Uri uri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        mCurrentPhotoPath = tempFile.getAbsolutePath();
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                            sendBroadcast(localIntent);
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

