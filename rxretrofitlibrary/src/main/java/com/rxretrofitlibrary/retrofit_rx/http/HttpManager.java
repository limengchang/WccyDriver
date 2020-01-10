package com.rxretrofitlibrary.retrofit_rx.http;

import android.util.Log;

import com.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.rxretrofitlibrary.retrofit_rx.exception.FactoryException;
import com.rxretrofitlibrary.retrofit_rx.exception.RetryWhenNetworkException;
import com.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;
import com.rxretrofitlibrary.retrofit_rx.subscribers.ProgressSubscriber;
import com.rxretrofitlibrary.retrofit_rx.utils.GlobalValue;
import com.rxretrofitlibrary.retrofit_rx.utils.GsonUtil;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * http交互处理类
 * Created by WZG on 2016/7/16.
 */
public class HttpManager {
    /*软引用對象*/
    private SoftReference<HttpOnNextListener> onNextListener;
    private SoftReference<RxAppCompatActivity> appCompatActivity;
    //    private SoftReference<RxFragment> rxFragment;


    public HttpManager(HttpOnNextListener onNextListener, RxAppCompatActivity appCompatActivity) {
        this.onNextListener = new SoftReference(onNextListener);
        this.appCompatActivity = new SoftReference(appCompatActivity);
    }
    /*
    public HttpManager(HttpOnNextListener onNextListener, RxFragment rxFragment) {
        this.onNextListener = new SoftReference(onNextListener);
        this.rxFragment = new SoftReference(rxFragment);
    }*/

    /**
     * 处理http请求
     *
     * @param basePar 封装的请求数据
     */
    public void doHttpDeal(BaseApi basePar) {
        //手动创建一个OkHttpClient并设置超时时间缓存等设置
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(basePar.getConnectionTime(), TimeUnit.SECONDS);

//        builder.addInterceptor(new CookieInterceptor(basePar.isCache(), basePar.getUrl()));
        if(GlobalValue.DEBUG){
            builder.addInterceptor(getHttpLoggingInterceptor());
        }

        /*创建retrofit对象*/
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonUtil.get()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(basePar.getBaseUrl())
                .build();
        /*rx处理*/
        ProgressSubscriber subscriber = new ProgressSubscriber(basePar, onNextListener);

        Observable.Transformer<Object,Object> lifecycleTransformer;
        if(appCompatActivity.get()!=null){
            lifecycleTransformer = appCompatActivity.get().bindUntilEvent(ActivityEvent.DESTROY);
        }else{
            lifecycleTransformer = buildVoidTransformer();
        }


        Observable observable = basePar.getObservable(retrofit)
                /*失败后的retry配置*/
                .retryWhen(new RetryWhenNetworkException())
                /*异常处理*/
                .onErrorResumeNext(funcException)
                /*生命周期管理*/
//                .compose(appCompatActivity.get().bindToLifecycle())
                .compose(lifecycleTransformer)
                /*http请求线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*结果判断*/
                .map(basePar);


        /*数据回调*/
        observable.subscribe(subscriber);

    }

    public void doHttpDeal(BaseApi basePar, String url) {
        //手动创建一个OkHttpClient并设置超时时间缓存等设置
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(basePar.getConnectionTime(), TimeUnit.SECONDS);

//        builder.addInterceptor(new CookieInterceptor(basePar.isCache(), basePar.getUrl()));
        if(GlobalValue.DEBUG){
            builder.addInterceptor(getHttpLoggingInterceptor());
        }

        /*创建retrofit对象*/
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonUtil.get()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .build();
        /*rx处理*/
        ProgressSubscriber subscriber = new ProgressSubscriber(basePar, onNextListener);

        Observable.Transformer<Object,Object> lifecycleTransformer;
        if(appCompatActivity!=null){
            lifecycleTransformer = appCompatActivity.get().bindUntilEvent(ActivityEvent.DESTROY);
        }else{
            lifecycleTransformer = buildVoidTransformer();
        }


        Observable observable = basePar.getObservable(retrofit)
                /*失败后的retry配置*/
                .retryWhen(new RetryWhenNetworkException())
                /*异常处理*/
                .onErrorResumeNext(funcException)
                /*生命周期管理*/
//                .compose(appCompatActivity.get().bindToLifecycle())
                .compose(lifecycleTransformer)
                /*http请求线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*结果判断*/
                .map(basePar);


        /*数据回调*/
        observable.subscribe(subscriber);

    }


    private Observable.Transformer<Object,Object> buildVoidTransformer(){
        return new Observable.Transformer<Object,Object>() {
            @Override
            public Observable<Object> call(Observable<Object> objectObservable) {
                return objectObservable;
            }
        };
    }

    /**
     * 异常处理
     */
    Func1 funcException = new Func1<Throwable, Observable>() {
        @Override
        public Observable call(Throwable throwable) {
            return Observable.error(FactoryException.analysisExcetpion(throwable));
        }
    };



    /**
     * 日志输出
     * 自行判定是否添加
     * @return
     */
    private HttpLoggingInterceptor getHttpLoggingInterceptor(){
        //日志显示级别
        HttpLoggingInterceptor.Level level= HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                e("RxRetrofit",message);
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }


    public static void e(String TAG, String msg) {
        int LOG_MAXLENGTH=2000;
        int strLength = msg.length();
        int start = 0;
        int end = LOG_MAXLENGTH;
        for (int i = 0; i < 100; i++) {
            //剩下的文本还是大于规定长度则继续重复截取并输出
            if (strLength > end) {
                Log.e(TAG + i, msg.substring(start, end));
                start = end;
                end = end + LOG_MAXLENGTH;
            } else {
                Log.e(TAG, msg.substring(start, strLength));
                break;
            }
        }
    }

}
