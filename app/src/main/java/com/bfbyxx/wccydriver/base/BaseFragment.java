package com.bfbyxx.wccydriver.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bfbyxx.wccydriver.presenter.PClass;
import com.bfbyxx.wccydriver.presenter.VInterface;
import com.bfbyxx.wccydriver.utils.ProjectUtil;
import com.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 9:46
 */
public class BaseFragment extends RxFragment implements VInterface {
    protected PClass pClass;
    public  int neType;
    protected RxAppCompatActivity mActivity;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pClass = new PClass(this);
        mActivity = (RxAppCompatActivity) getActivity();

    }

    @Override
    public void onSuccess(String data) {

    }

    @Override
    public void onError(ApiException e) {
        ProjectUtil.show(mActivity,e.getDisplayMessage());
    }

    @Override
    public void connectNetwork() {
        String mess = "亲！您的网络出问题了，请检查设置！";
        ProjectUtil.show(mActivity,mess);
    }

    @Override
    public void showProg() {

    }

    @Override
    public void dismissProg() {

    }
}
