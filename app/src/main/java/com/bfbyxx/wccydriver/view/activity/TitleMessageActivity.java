package com.bfbyxx.wccydriver.view.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.base.BaseActivity;
import com.bfbyxx.wccydriver.dialog.DialogUtils;
import com.bfbyxx.wccydriver.entity.PhraseAdapter;
import com.bfbyxx.wccydriver.sqlite.LouSQLite;
import com.bfbyxx.wccydriver.sqlite.MyCallBack;
import com.bfbyxx.wccydriver.sqlite.Phrase;
import com.bfbyxx.wccydriver.sqlite.PhraseEntry;
import com.bfbyxx.wccydriver.wheel.ClickListenerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TitleMessageActivity extends BaseActivity {
    private Context context;
    @BindView(R.id.lv_set)
    ListView lv_set;
    @BindView(R.id.tv_title)
    TextView tv_title;
    //  查找
    List<Phrase> cList = new ArrayList<Phrase>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_message);
        ButterKnife.bind(this);
        context = TitleMessageActivity.this;
        getSqliteData();
    }

     private void getSqliteData() {
         cList = LouSQLite.query(MyCallBack.TABLE_NAME_PHRASE, "select * from " + MyCallBack.TABLE_NAME_PHRASE, null);
         PhraseAdapter pAdapter = new PhraseAdapter(context, cList, true, deleteClick);
         lv_set.setAdapter(pAdapter);
         pAdapter.notifyDataSetChanged();
         if (!cList.isEmpty()) {
             tv_title.setText("消息(" + cList.size() + ")");
         } else {
             tv_title.setText("消息");
         }
     }

    ClickListenerAdapter deleteClick = new ClickListenerAdapter() {
        @Override
        public void onClick(Object objects, int type, final int position) {
            if (type == 1) {
                new DialogUtils(context, R.style.dialog, "您确定删除该数据？", new DialogUtils.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            // 从数据库中删除
                            LouSQLite.delete(MyCallBack.TABLE_NAME_PHRASE, PhraseEntry.COLEUM_NAME_ID + "=?", new String[]{cList.get(position).getId()});
                            getSqliteData();
                        }
                        dialog.dismiss();
                    }
                }).show();
            } else {

            }
        }
    };

    @OnClick({R.id.tv_back,R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                //返回
                finish();
                break;
        }
    }
}
