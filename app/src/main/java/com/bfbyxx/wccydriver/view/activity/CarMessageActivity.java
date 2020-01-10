package com.bfbyxx.wccydriver.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.api.UploadFileApi;
import com.bfbyxx.wccydriver.application.MyApplication;
import com.bfbyxx.wccydriver.base.BaseActivity;
import com.bfbyxx.wccydriver.dialog.PhotoDisplayDialog;
import com.bfbyxx.wccydriver.dialog.SpinerPopWindow;
import com.bfbyxx.wccydriver.entity.DrivingLicOcrInfo;
import com.bfbyxx.wccydriver.entity.UploadFileResult;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.rxretrofitlibrary.retrofit_rx.utils.GsonUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CarMessageActivity extends BaseActivity implements AdapterView.OnItemClickListener, TakePhoto.TakeResultListener, InvokeListener {
    private Context context;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_car_message_number)
    TextView tv_car_message_number;
    @BindView(R.id.tv_car_message_id)
    TextView tv_car_message_id;
    @BindView(R.id.layout_person_clxx)
    LinearLayout layout_person_clxx;
    @BindView(R.id.layout_zc_clxx)
    LinearLayout layout_zc_clxx;

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
    /**
     * 记录当前点击的照片框(可点击View的id)
     */
    private int currentClickPhotoStubId;
    private TakePhoto takePhoto;
    InvokeParam invokeParam;
    private PhotoDisplayDialog photoDialog;//显示完成照片的dialog

    private SpinerPopWindow mSpinerPopWindow;
    @BindView(R.id.tv_cllx_xz)
    TextView tv_cllx_xz;
    @BindView(R.id.layout_cllx)
    LinearLayout layout_cllx;
    @BindView(R.id.btn_zc_zcwc)
    Button btn_zc_zcwc;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==102){
                HashMap<String, String> map = (HashMap<String, String>) msg.obj;
                String text = map.get("text");
                tv_cllx_xz.setText("车辆类型："+text);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_message);
        ButterKnife.bind(this);
        context = CarMessageActivity.this;
        init();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getTakePhoto().onSaveInstanceState(outState);
    }

    private void init() {
        mSpinerPopWindow = new SpinerPopWindow(context,mHandler);
        mSpinerPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //监听popupwindow取消
//                setTextImage(R.drawable.down1);
            }
        });
        String strCarNum = getIntent().getStringExtra("car_number");
        String strCarID = getIntent().getStringExtra("car_id");
        if (strCarNum.equals("zc")){
            tv_title.setText("车辆信息");
            layout_zc_clxx.setVisibility(View.VISIBLE);
            btn_zc_zcwc.setVisibility(View.VISIBLE);
        }else {
            tv_title.setText("车辆详情");
            layout_person_clxx.setVisibility(View.VISIBLE);
        }
        tv_car_message_number.setText("车牌号：" + strCarNum);
        tv_car_message_id.setText("ID：" + strCarID);

        initPhotoIvArray();
    }

    private void initPhotoIvArray() {
        photoDialog = new PhotoDisplayDialog(context);
        photoDialog.setCallback(photoDialogCallback);

        ivArrayPhoto.put(R.id.vg_driving_lic_front_take_click_stub, (ImageView) findViewById(R.id.iv_driving_lic_front));//行驶证正页
        ivArrayPhoto.put(R.id.vg_insure_card_front_take_click_stub, (ImageView) findViewById(R.id.iv_insure_card_front));//保险卡正面
        ivArrayPhoto.put(R.id.vg_truck_body_take_click_stub, (ImageView) findViewById(R.id.iv_truck_body));//车辆全身照
        ivArrayPhoto.put(R.id.vg_purchase_tax_take_click_stub, (ImageView) findViewById(R.id.iv_purchase_tax));//车辆购置税发票
    }

    /**
     * 图片展示dialog的回调
     */
    private PhotoDisplayDialog.Callback photoDialogCallback = new PhotoDisplayDialog.Callback() {
        @Override
        public void onShootAgainClicked() {//点击了重新选择/拍摄 按钮
            ProjectUtil.showUploadFileDialog(CarMessageActivity.this, CarMessageActivity.this);
        }
    };

    @OnClick({R.id.tv_back,
            R.id.vg_driving_lic_front_take_click_stub,
            R.id.vg_insure_card_front_take_click_stub,
            R.id.vg_truck_body_take_click_stub,
            R.id.vg_purchase_tax_take_click_stub,
            R.id.layout_cllx,
            R.id.btn_zc_zcwc})
    public void onViewClicked(View view) {
        currentClickPhotoStubId = view.getId();
        if (currentClickPhotoStubId == R.id.tv_back) {
            //返回
            finish();
        }else if (currentClickPhotoStubId == R.id.layout_cllx) {
            //车辆类型
            getClictSpinner(layout_cllx);
        }else if (currentClickPhotoStubId == R.id.btn_zc_zcwc) {
            //注册完成
            RegisterActivity._this.finish();
            finish();
        } else {
            String url = uploadedPhotoUrl.get(currentClickPhotoStubId);
            if (TextUtils.isEmpty(url)) {
                ProjectUtil.showUploadFileDialog(this, this);
            } else {
//                photoDialog.setImgUrl(GlobalValue.BASEURL + url).show();
                ProjectUtil.showUploadFileDialog(this, this);
            }
        }
    }

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

    private void goToTakePhoto(boolean isTake) {
        ////获取TakePhoto实例
        takePhoto = getTakePhoto();
        //设置裁剪参数
        CropOptions cropOptions = new CropOptions.Builder().create();
        //设置压缩参数
        CompressConfig compressConfig = new CompressConfig.Builder().setMaxPixel(400).create();
        takePhoto.onEnableCompress(compressConfig, true);  //设置为需要压缩
        if (isTake) {
            takePhoto.onPickFromCapture(getImageCropUri());
        } else {
            takePhoto.onPickFromGallery();
        }
    }

    //获得照片的输出保存Uri
    private Uri getImageCropUri() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }

    /**
     * 获取TakePhoto实例
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    /************后台返回数据**************/
    @Override
    public void onSuccess(String data) {
        switch (neType) {
            case 1: //上传图片成功
                UploadFileResult result = GsonUtil.get().fromJson(data, UploadFileResult.class);
                displayOcrInfo(data);

                //将上传成功返回的图片url记录下来
                uploadedPhotoUrl.put(currentClickPhotoStubId, result.getUrl());

                //将上传成功的图片,显示到对应的iv上
                ImageView iv = ivArrayPhoto.get(currentClickPhotoStubId);
                displayPhotoByUri(iv, "file://" + currentTookPhotoPath);
                break;
        }
    }

    @Override
    public void onError(ApiException e) {
        super.onError(e);
        switch (neType) {
            case 2://获取我的资料数据失败
//                loadFailTipHelper.showFailTipView();
                break;
        }
    }

    /**
     * 将url所表示的图片显示到iv上
     */
    private void displayPhotoByUri(ImageView iv, String uri) {
        if (!TextUtils.isEmpty(uri)) {
            iv.setVisibility(View.VISIBLE);
            Picasso.get().load(uri).into(iv);
        }
    }

    /**************识别出来的数据**************/
    /**
     * 显示证件ocr识别出来的信息
     */
    private void displayOcrInfo(String data) {
        try {
            org.json.JSONObject dataJo = new JSONObject(data);
            String ocrInfoStr = dataJo.getString("ocrinfo");
            if (TextUtils.isEmpty(ocrInfoStr)) {
                return;
            }
            /**
             * 根据上传的不同的证件照来判断,将json转换成不同类型的ocr识别结果对像
             */
            switch (currentClickPhotoStubId) {
                case R.id.vg_driving_lic_front_take_click_stub://行驶证
                    DrivingLicOcrInfo drivingLicOcrInfo = GsonUtil.get().fromJson(ocrInfoStr, DrivingLicOcrInfo.class);
//                    cetPlateNo.setText(drivingLicOcrInfo.getVehicleNo());
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 请求上传文件
     */
    private void requestUploadFile() {
        neType = 1;
        File imgFile = new File(currentTookPhotoPath);
        UploadFileApi api = new UploadFileApi();
        api.setHeader(MyApplication.authorization);
        api.setUserHeader(MyApplication.getUserHeader());
        api.setImg(imgFile);
        api.setFiletype(getFileTypeByPhotoStubId(currentClickPhotoStubId));

        pClass.startHttpRequest(this, api);
    }

    /**
     * 根据 拍照框点击stub的id来获取对应的照片的fileType(用于上传文件接口参数)
     */
    private int getFileTypeByPhotoStubId(int photoStubId) {
        int fileType = 0;
        switch (photoStubId) {
            case R.id.vg_driving_lic_front_take_click_stub://行驶证
                fileType = 4;
                break;
        }
        return fileType;
    }

    /**************takePhoto*****************/
    private String currentTookPhotoPath;

    @Override
    public void takeSuccess(TResult result) {
        currentTookPhotoPath = result.getImage().getOriginalPath();
        requestUploadFile();
    }

    @Override
    public void takeFail(TResult result, String msg) {
    }

    @Override
    public void takeCancel() {
    }


    /*************invokeparam***********************8*/
    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
    }

    /***************下拉列表 车辆类型**********/
    private void getClictSpinner(View view){
        mSpinerPopWindow.setWidth(view.getWidth());
        mSpinerPopWindow.showAsDropDown(view);
    }
}
