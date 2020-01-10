package com.bfbyxx.wccydriver.view.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.adapter.MeAdapter;
import com.bfbyxx.wccydriver.api.GetUserAccountApi;
import com.bfbyxx.wccydriver.application.MyApplication;
import com.bfbyxx.wccydriver.bar.StarBar;
import com.bfbyxx.wccydriver.base.BaseFragment;
import com.bfbyxx.wccydriver.dialog.PhotoDisplayDialog;
import com.bfbyxx.wccydriver.entity.MeItem;
import com.bfbyxx.wccydriver.entity.UserInfo;
import com.bfbyxx.wccydriver.tools.Tools;
import com.bfbyxx.wccydriver.utils.HttpUtil;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.bfbyxx.wccydriver.utils.SPUtils;
import com.bfbyxx.wccydriver.utils.ToolUtil;
import com.bfbyxx.wccydriver.view.activity.BondMoneyActivity;
import com.bfbyxx.wccydriver.view.activity.SetActivity;
import com.bfbyxx.wccydriver.view.activity.WebActivity;
import com.bfbyxx.wccydriver.wheel.ClickListenerAdapter;
import com.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.rxretrofitlibrary.retrofit_rx.utils.GlobalValue;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 10:38
 */
public class MeFragment extends BaseFragment implements OnRefreshListener, TakePhoto.TakeResultListener, InvokeListener {
    Unbinder unbinder;
    private View view;
    @BindView(R.id.srl_me)
    SmartRefreshLayout srl_me;
    @BindView(R.id.tv_personInfo)
    TextView tv_personInfo;
    @BindView(R.id.iv_head)
    ImageView iv_head;
    @BindView(R.id.tv_cityname)
    TextView tv_cityname;
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.lv_me_item)
    ListView lv_me_item;
    @BindView(R.id.tv_username)
    TextView tv_username;
    @BindView(R.id.bar_star)
    StarBar bar_star;

    /**
     * 照片上传成功后,服务端返回的图片url暂存在这里
     * key:拍照框可点击View的id,value:对应拍照框图片上传成功后图片的url;
     */
    private SparseArray<String> uploadedPhotoUrl = new SparseArray<>();


    /**
     * 展示照片的iv
     * key:拍照框可点击View的id, value: 拍照框可点击View对应的显示图片的ImageView
     */
    private SparseArray<ImageView> ivArrayPhoto = new SparseArray<>();
    private PhotoDisplayDialog photoDialog;//显示完成照片的dialog
    private TakePhoto takePhoto;
    InvokeParam invokeParam;
    /**
     * 记录当前点击的照片框(可点击View的id)
     */
    private int currentClickPhotoStubId;
    private String currentTookPhotoPath = "";

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                HashMap<String, String> map = (HashMap<String, String>) msg.obj;
                String address = map.get("address");
                tv_cityname.setText("城市：" + address);
//                ProjectUtil.show1(mActivity, "allOrderAreadID:" + aId, app.iShow());
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_me, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        initPhotoIvArray(view);
        return view;
    }

    private void init() {
        getUserAccount();
        tv_back.setVisibility(View.GONE);
        tv_title.setText("个人中心");
        srl_me.setOnRefreshListener(this);
    }
    /**
     * 获取用户信息
     */
    private void getUserAccount(){
        neType = 0;
        GetUserAccountApi gvml = new GetUserAccountApi();
        gvml.setHeader(MyApplication.authorization);
        gvml.setUserid(SPUtils.getUser_ID(mActivity));
        pClass.startHttpRequest(mActivity, gvml);
    }


    ClickListenerAdapter onClickLister = new ClickListenerAdapter() {
        @Override
        public void onClick(Object objects, int type, int position) {
            if (type == 1) {
                ProjectUtil.checkCallPhone(mActivity,getString(R.string.phone_number));
            }
        }
    };

    private void onItemClick(String money){
        MeAdapter meAdapter = new MeAdapter(mActivity, getData(),onClickLister,money);
        lv_me_item.setAdapter(meAdapter);
        meAdapter.notifyDataSetChanged();

        lv_me_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent webIntent = new Intent(mActivity, WebActivity.class);
                switch (position){
                    case 3:
                        startActivity(new Intent(mActivity, BondMoneyActivity.class));
                        break;

                    case 4:
//                        startActivity(new Intent(mActivity, AccountActivity.class));
                        webIntent.putExtra("strTitle","账户余额信息");
                        webIntent.putExtra("strRight","银行卡");
                        webIntent.putExtra("strUrl",MyApplication.WebViewIP+"Weixin_Driver/UserAmount");
                        startActivity(webIntent);
                        break;
                    case 5:
                        //车辆信息
//                        startActivity(new Intent(mActivity, CarInfoActivity.class));
                        webIntent.putExtra("strTitle","车辆信息");
                        webIntent.putExtra("strRight","添加");
                        webIntent.putExtra("strUrl",MyApplication.WebViewIP+"Weixin_Driver/Vehicle");
                        startActivity(webIntent);
                        break;
                    case 6:
                        //消息中心
                        webIntent.putExtra("strTitle","消息中心");
                        webIntent.putExtra("strRight","");
                        webIntent.putExtra("strUrl",MyApplication.WebViewIP+"Weixin_Driver/MessageList");
                        startActivity(webIntent);
                        break;
                    case 8:
                        startActivity(new Intent(mActivity, SetActivity.class));
                        break;
                    case 9:
                        //分享
                        showFx();
                        break;
                }
            }
        });
    }

    //分享至微信和QQ
    private void showFx(){
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(mActivity, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(mActivity,R.layout.pop_fenxiang_item,null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.AnimBottom);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        LinearLayout layout_fx_wx = dialog.findViewById(R.id.layout_fx_wx);
        layout_fx_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ProjectUtil.shareMsg(mActivity,false);
            }
        });
        LinearLayout layout_fx_qq = dialog.findViewById(R.id.layout_fx_qq);
        layout_fx_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ProjectUtil.shareMsg(mActivity,true);
            }
        });
    }

    private List<MeItem> getData() {
        List<MeItem> dataList = new ArrayList<MeItem>();
        String[] arr_data= {"空白","拨打","空白","保证金","账号余额","车辆信息","消息中心","空白","设置","分享"};
        int[] imageId = { R.drawable.a,R.drawable.a, R.drawable.a,R.drawable.moneyg,R.drawable.moneyg,R.drawable.mytransit,R.drawable.icon_xx,R.drawable.a, R.drawable.mysetting,R.drawable.myshare };
        for (int i = 0; i < arr_data.length; i++) {
            MeItem map = new MeItem();
            map.setName(arr_data[i]);
            map.setLeft_icon(imageId[i]);
            dataList.add(map);
        }
        return dataList;
    }

    /**
     * 照片框框点击监听
     */
    @OnClick({R.id.iv_head, R.id.btn_cityname,R.id.rlayout_person})
    public void onPhotoStubClicked(View v) {
        currentClickPhotoStubId = v.getId();
        if (currentClickPhotoStubId == R.id.btn_cityname) {
            ProjectUtil.showCityAddress(mActivity, mHandler);
        }else if(currentClickPhotoStubId == R.id.rlayout_person){
//            startActivity(new Intent(mActivity, PersonMessageActivity.class));
            Intent intent = new Intent(mActivity, WebActivity.class);
            intent.putExtra("strTitle","个人资料");
            intent.putExtra("strRight","编辑");
            intent.putExtra("strUrl",MyApplication.WebViewIP+"Weixin_Driver/UserInfo");
            startActivity(intent);
        } else {
            String url = uploadedPhotoUrl.get(currentClickPhotoStubId);
            if (TextUtils.isEmpty(url)) {
                ProjectUtil.showUploadFileDialog(mActivity, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                goToTakePhoto(true);
                                break;
                            case 1:
                                goToTakePhoto(false);
                                break;
                        }
                    }
                });
            } else {
                photoDialog.setImgUrl(GlobalValue.BASEURL + url).show();
            }
        }
    }

    private void initPhotoIvArray(View view) {
        photoDialog = new PhotoDisplayDialog(mActivity);
        photoDialog.setCallback(photoDialogCallback);
        ivArrayPhoto.put(R.id.iv_head, (ImageView) view.findViewById(R.id.iv_head));
    }

    /**
     * 图片展示dialog的回调
     */
    private PhotoDisplayDialog.Callback photoDialogCallback = new PhotoDisplayDialog.Callback() {
        @Override
        public void onShootAgainClicked() {//点击了重新选择/拍摄 按钮
            ProjectUtil.showUploadFileDialog(mActivity, new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            goToTakePhoto(true);
                            break;
                        case 1:
                            goToTakePhoto(false);
                            break;
                    }
                }
            });
        }
    };

    private void goToTakePhoto(boolean isFromCamera) {
        ////获取TakePhoto实例
        takePhoto = getTakePhoto();
        //设置裁剪参数
//        CropOptions cropOptions = new CropOptions.Builder().create();
        //设置压缩参数
//        CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(800).create();
        CompressConfig compressConfig = new CompressConfig.Builder().setMaxPixel(400).create();
        takePhoto.onEnableCompress(compressConfig, true);  //设置为需要压缩
        if (isFromCamera) {
            takePhoto.onPickFromCapture(getImageCropUri());
        } else {
            takePhoto.onPickFromGallery();
        }
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    //获得照片的输出保存Uri
    private Uri getImageCropUri() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }

    @Override
    public void onResume() {
        super.onResume();
        iv_head.setImageBitmap(Tools.getLoacalBitmap(mActivity, currentTookPhotoPath)); //设置Bitmap
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        HttpUtil.showProgress(mActivity,"刷新中...");
        getUserAccount();
    }


    @Override
    public void takeSuccess(TResult result) {
//        currentTookPhotoPath = result.getImage().getOriginalPath();
        Luban.get(mActivity).load(new File(result.getImage().getOriginalPath())).setCompressListener(new OnCompressListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(File file) {
                currentTookPhotoPath = file.getAbsolutePath();
//                Bitmap bitmap = BitmapFactory.decodeFile(currentTookPhotoPath);
//                iv_head .setImageBitmap(bitmap); //设置Bitmap
                iv_head.setImageBitmap(Tools.getLoacalBitmap(mActivity, currentTookPhotoPath)); //设置Bitmap
            }

            @Override
            public void onError(Throwable e) {

            }
        }).launch();
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(String data) {
//        JSONObject jo = JSONObject.parseObject(data);
        if (neType == 0) {
            HttpUtil.dismissProgress();
            srl_me.finishRefresh(true);
            UserInfo userInfo=new Gson().fromJson(data, UserInfo.class);
            ToolUtil.saveUserInfo(mActivity,userInfo);
            String userName = userInfo.getUser_Name()==null?userInfo.getUser_Phone():userInfo.getUser_Name();
            tv_username.setText(userName);
            bar_star.setClickAble(false);
            bar_star.setStarCount(5);
            bar_star.setRating(Integer.parseInt(userInfo.getUser_Star()));
            onItemClick(userInfo.getBalance());
//            Balance
//            String success = jo.getString("Success");
//            if (success.equals("true")){
//                UserInfo userInfo=new Gson().fromJson(data, UserInfo.class);
//                tv_username.setText(userInfo.getUser_Phone());
//            }else {
//                ProjectUtil.show(mActivity,jo.getString("Message"));
//            }
        }
    }
    @Override
    public void onError(ApiException e) {
        super.onError(e);
        HttpUtil.dismissProgress();
    }
}
