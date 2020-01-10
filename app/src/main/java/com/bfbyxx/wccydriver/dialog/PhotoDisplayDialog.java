package com.bfbyxx.wccydriver.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.bfbyxx.wccydriver.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 10:41
 */
public class PhotoDisplayDialog extends Dialog {

    @BindView(R.id.pv)
    PhotoView photoView;
    @BindView(R.id.btn_shoot_again)
    Button btnShootAgain;

    private String imgUrl;

    private Callback mCallback;

    private boolean shootAgainBtnVisible=true;//是否显示 "重新选择/拍摄"图片按钮

    public interface Callback{
        void onShootAgainClicked();
    }

    public PhotoDisplayDialog(@NonNull Context context) {
        super(context, R.style.common_dialog);
    }

    public void setCallback(PhotoDisplayDialog.Callback callback){
        mCallback=callback;
    }

    public PhotoDisplayDialog setShootAgainBtnVisible(boolean shootAgainBtnVisible) {
        this.shootAgainBtnVisible = shootAgainBtnVisible;
        return this;
    }

    public PhotoDisplayDialog setImgUrl(String imgUrl){
        this.imgUrl=imgUrl;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_show_photo);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        ButterKnife.bind(this, this);

        if(shootAgainBtnVisible){
            btnShootAgain.setVisibility(View.VISIBLE);
        }else{
            btnShootAgain.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Picasso.get().load(imgUrl).into(photoView);
    }

    @OnClick(R.id.btn_shoot_again)
    public void onViewClicked(View v){
        dismiss();
        if(mCallback!=null){
            mCallback.onShootAgainClicked();
        }
    }

    @Override
    public void show() {
        super.show();

    }
}

