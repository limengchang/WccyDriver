package com.bfbyxx.wccydriver.api;

import com.bfbyxx.wccydriver.presenter.HttpService;
import com.rxretrofitlibrary.retrofit_rx.Api.BaseApi;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by MARK on 2018/6/15.
 */

public class UploadFileApi extends BaseApi {
    private File img;

    /**
     * 身份证 = 1,
     * 银行卡 = 2,
     * 驾驶证 = 3,
     * 行驶证 = 4,
     * 营业执照 = 5,
     *
     * 此参数用来标识传的文件是什么类型的证件照,只有当需要进行证件识别的场景
     * 才需要传此参数
     */
    private int filetype;

    public void setFiletype(int filetype) {
        this.filetype = filetype;
    }

    public File getImg() {
        return img;
    }

    public void setImg(File img) {
        this.img = img;
    }


    @Override
    public Observable getObservable(Retrofit retrofit) {

        HttpService httpService = retrofit.create(HttpService.class);
        MultipartBody.Builder builder = new MultipartBody.Builder();

//        JSONObject root = new JSONObject();
//        root.put("file", getImg());
//        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("image/*"),
//                root.toString());


        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), getImg());
        builder.addFormDataPart("file", getImg().getName(), requestBody);
        builder.addFormDataPart("filetype", filetype + "");
        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return httpService.uploadFile(getHeader(), getUserHeader(), multipartBody);

//        Map<String, RequestBody> map = new HashMap<>();
//        map.put("Id", RequestBody.create(MediaType. parse("text/plain"), ""));
//
//        map.put(parseImageMapKey("head",getImg().getName()),RequestBody.create(MediaType.parse("image/*"), getImg()));
//
//        return httpService.uploadHeadImage(getHeader(), map);

    }

    @Override
    protected boolean isNeedData() {
        return true;
    }
}
