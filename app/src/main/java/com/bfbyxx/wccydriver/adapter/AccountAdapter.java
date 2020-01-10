package com.bfbyxx.wccydriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.entity.Account;
import com.bfbyxx.wccydriver.tools.Tools;

import java.util.List;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 11:27
 */
public class AccountAdapter extends BaseAdapter {
    private List<Account> mList;//数据源
    private LayoutInflater mInflater;//布局装载器对象

    // 通过构造方法将数据源与数据适配器关联起来
    // context:要使用当前的Adapter的界面对象
    public AccountAdapter(Context context, List<Account> list) {
        mInflater = LayoutInflater.from(context);
        mList = list;
    }
    @Override
    //ListView需要显示的数据数量
    public int getCount() {
        return mList.size();
    }

    @Override
    //指定的索引对应的数据项
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    //指定的索引对应的数据项ID
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        //如果view未被实例化过，缓存池中没有对应的缓存
        if (convertView == null) {
            viewHolder = new ViewHolder();
            // 由于我们只需要将XML转化为View，并不涉及到具体的布局，所以第二个参数通常设置为null
            convertView = mInflater.inflate(R.layout.item_account_list, null);

            //对viewHolder的属性进行赋值
            viewHolder.tv_account_type = convertView.findViewById(R.id.tv_account_type);
            viewHolder.tv_account_money = convertView.findViewById(R.id.tv_account_money);
            viewHolder.tv_account_sxf = convertView.findViewById(R.id.tv_account_sxf);
            viewHolder.tv_account_time = convertView.findViewById(R.id.tv_account_time);

            //通过setTag将convertView与viewHolder关联
            convertView.setTag(viewHolder);
        }else{//如果缓存池中有对应的view缓存，则直接通过getTag取出viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 设置控件的数据
        String srtTypeName = Tools.getAccountReturon(mList.get(position).getAType());
        viewHolder.tv_account_type.setText(srtTypeName);
        if (srtTypeName.equals("充值")){
            viewHolder.tv_account_money.setText("+"+mList.get(position).getAmount());
            viewHolder.tv_account_sxf.setVisibility(View.GONE);
            viewHolder.tv_account_sxf.setText("");
            viewHolder.tv_account_money.setTextColor(mInflater.getContext().getResources().getColor(R.color.red));
        }else {
            viewHolder.tv_account_money.setText("-"+mList.get(position).getAmount());
            viewHolder.tv_account_sxf.setVisibility(View.GONE);
            viewHolder.tv_account_sxf.setText("手续费：0.6");
            viewHolder.tv_account_money.setTextColor(mInflater.getContext().getResources().getColor(R.color.green));
        }
        viewHolder.tv_account_time.setText(mList.get(position).getCreateTime());

        return convertView;
    }

    // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
    class ViewHolder{
        TextView tv_account_money,tv_account_type,tv_account_sxf,tv_account_time;
    }
}

