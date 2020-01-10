package com.rxretrofitlibrary.retrofit_rx.Api;

import com.alibaba.fastjson.JSONObject;
import com.rxretrofitlibrary.retrofit_rx.exception.HttpTimeException;
import com.rxretrofitlibrary.retrofit_rx.utils.GlobalValue;

import retrofit2.Retrofit;
import rx.Observable;
import rx.functions.Func1;


/**
 * 请求数据统一封装类
 * Created by WZG on 2016/7/16.
 */
public abstract class BaseApi implements Func1<String, String> {
    /*是否能取消加载框*/
    private boolean cancel = false;
    /*是否显示加载框*/
    private boolean showProgress = true;
    /*是否需要缓存处理*/
    private boolean cache = false;
    /*基础url*/
    private String baseUrl = GlobalValue.BASEURL;
    /*方法-如果需要缓存必须设置这个参数；不需要不用設置*/
    private String mothed;
    /*超时时间-默认6秒*/
    private int connectionTime = 6;
    /*有网情况下的本地缓存时间默认60秒*/
    private int cookieNetWorkTime = 60;
    /*无网络的情况下本地缓存时间默认30天*/
    private int cookieNoNetWorkTime = 24 * 60 * 60 * 30;

    //请求头
    public String header;

    //add
    public String userHeader;
    public String userId;
    public String userName;
    public String roleId;

    public String getUserHeader() {
        return userHeader;
    }

    public void setUserHeader(String userHeader) {
        this.userHeader = userHeader;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    /**
     * 设置参数
     *
     * @param retrofit
     * @return
     */
    public abstract Observable getObservable(Retrofit retrofit);

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getCookieNoNetWorkTime() {
        return cookieNoNetWorkTime;
    }

    public void setCookieNoNetWorkTime(int cookieNoNetWorkTime) {
        this.cookieNoNetWorkTime = cookieNoNetWorkTime;
    }

    public int getCookieNetWorkTime() {
        return cookieNetWorkTime;
    }

    public void setCookieNetWorkTime(int cookieNetWorkTime) {
        this.cookieNetWorkTime = cookieNetWorkTime;
    }

    public String getMothed() {
        return mothed;
    }

    public int getConnectionTime() {
        return connectionTime;
    }

    public void setConnectionTime(int connectionTime) {
        this.connectionTime = connectionTime;
    }

    public void setMothed(String mothed) {
        this.mothed = mothed;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getUrl() {
        return baseUrl + mothed;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public String call(String httpResult) {

        BaseResultEntity baseResult = JSONObject.parseObject(httpResult, BaseResultEntity.class);
        //status==0 接口调用失败
//        if (baseResult.getState() == 0) {
//            throw new HttpTimeException(baseResult.getMsg());
//        }

        /*
         * 接口调用成功
         * 根据子类实现的isNeedData方法的返回值来判断是返回data还是msg
         */
        if(isNeedData()){
            return baseResult.getData();
        }else{
            return baseResult.getMsg();
        }
    }

    protected abstract boolean isNeedData();

}
