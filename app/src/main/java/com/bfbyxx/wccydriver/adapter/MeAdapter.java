package com.bfbyxx.wccydriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.entity.MeItem;
import com.bfbyxx.wccydriver.wheel.ClickListenerAdapter;

import java.util.List;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 11:27
 */
public class MeAdapter extends BaseAdapter {
    private List<MeItem> mList;//数据源
    private LayoutInflater mInflater;//布局装载器对象
    private ClickListenerAdapter mClickListener;
    private String strMoney;

    // 通过构造方法将数据源与数据适配器关联起来
    // context:要使用当前的Adapter的界面对象
    public MeAdapter(Context context, List<MeItem> list,ClickListenerAdapter clickListener,String money) {
        mInflater = LayoutInflater.from(context);
        mList = list;
        mClickListener = clickListener;
        strMoney = money;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        //如果view未被实例化过，缓存池中没有对应的缓存
        if (convertView == null) {
            viewHolder = new ViewHolder();
            // 由于我们只需要将XML转化为View，并不涉及到具体的布局，所以第二个参数通常设置为null
            convertView = mInflater.inflate(R.layout.item_me_list, null);

            //对viewHolder的属性进行赋值
            viewHolder.tv_name = convertView.findViewById(R.id.tv_name);
            viewHolder.iv_left =  convertView.findViewById(R.id.iv_left);
            viewHolder.tv_white_bar = convertView.findViewById(R.id.tv_white_bar);
            viewHolder.iv_right = convertView.findViewById(R.id.iv_right);
            viewHolder.rl_context = convertView.findViewById(R.id.rl_context);
            viewHolder.rl_call = convertView.findViewById(R.id.rl_call);
            viewHolder.tv_call = convertView.findViewById(R.id.tv_call);
            viewHolder.tv_money = convertView.findViewById(R.id.tv_money);
            viewHolder.bottom_view = convertView.findViewById(R.id.bottom_view);

            //通过setTag将convertView与viewHolder关联
            convertView.setTag(viewHolder);
        }else{//如果缓存池中有对应的view缓存，则直接通过getTag取出viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 设置控件的数据
//        String strName = mList.get(position).get("name").toString();
        if (position==5||position==8){
            viewHolder.bottom_view.setVisibility(View.GONE);
        }else {
            viewHolder.bottom_view.setVisibility(View.VISIBLE);
        }
        String strName = mList.get(position).getName();
        if(strName.equals("拨打")){
            viewHolder.tv_white_bar.setVisibility(View.GONE);
            viewHolder.rl_call.setVisibility(View.VISIBLE);
            viewHolder.rl_context.setVisibility(View.GONE);
        }else if (strName.equals("空白")){
            viewHolder.tv_white_bar.setVisibility(View.VISIBLE);
            viewHolder.rl_call.setVisibility(View.GONE);
            viewHolder.rl_context.setVisibility(View.GONE);
        }else{
            viewHolder.rl_call.setVisibility(View.GONE);
            viewHolder.tv_white_bar.setVisibility(View.GONE);
            viewHolder.rl_context.setVisibility(View.VISIBLE);
            viewHolder.iv_left.setImageResource(mList.get(position).getLeft_icon());
            viewHolder.tv_name.setText(strName);
            if (strName.equals("账号余额")){
                viewHolder.tv_money.setVisibility(View.VISIBLE);
                viewHolder.tv_money.setText(strMoney);
            }else {
                viewHolder.tv_money.setVisibility(View.GONE);
            }

        }
        viewHolder.tv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //v表示点击动作，1表示根据该值来判断是哪个控件的点击事件，position表示点击哪个
                mClickListener.onClick(v, 1, position);
            }
        });
        return convertView;
    }

    // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
    class ViewHolder{
        TextView tv_name;
        TextView tv_white_bar,tv_call,tv_money;
        ImageView iv_right,iv_left;
        RelativeLayout rl_context,rl_call;
        View bottom_view;
    }
}

