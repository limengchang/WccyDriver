package com.bfbyxx.wccydriver.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.adapter.CatTypeAdapter;
import com.bfbyxx.wccydriver.application.MyApplication;
import com.bfbyxx.wccydriver.entity.CarType;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 10:33
 */
public class SpinerPopWindow extends PopupWindow {
    private LayoutInflater inflater;
    List<CarType> cList = MyApplication.cartypeLsit;
    public SpinerPopWindow(Context context,final Handler mHandler) {
        super(context);
        inflater=LayoutInflater.from(context);
        init(context,mHandler);
    }

    private void init(Context context, final Handler mHandler){
        View view = inflater.inflate(R.layout.spiner_window_layout, null);
        setContentView(view);
        setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);
        ListView mListView = view.findViewById(R.id.listview);
        CatTypeAdapter ctAdapter = new CatTypeAdapter(context, cList);
        mListView.setAdapter(ctAdapter);
        ctAdapter.notifyDataSetChanged();
        //设置PopupWindow动画
        setAnimationStyle(R.style.style_pop_animation);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();
                Message msg = new Message();
                msg.what = 102;
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("text", cList.get(position).getText());
                map.put("id", cList.get(position).getId());
                msg.obj = map;
                mHandler.sendMessage(msg);
            }
        });
    }
}

