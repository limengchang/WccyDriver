package com.bfbyxx.wccydriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bfbyxx.wccydriver.R;
import com.bfbyxx.wccydriver.entity.Order;
import com.bfbyxx.wccydriver.tools.Tools;

import java.util.List;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 11:27
 */
public class OrderAdapter extends BaseAdapter {
    private List<Order> mList;//数据源
    private LayoutInflater mInflater;//布局装载器对象

    // 通过构造方法将数据源与数据适配器关联起来
    // context:要使用当前的Adapter的界面对象
    public OrderAdapter(Context context, List<Order> list) {
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
            convertView = mInflater.inflate(R.layout.item_order_list, null);

            //对viewHolder的属性进行赋值
            viewHolder.tv_order_no = convertView.findViewById(R.id.tv_order_no);
            viewHolder.tv_order_address_s = convertView.findViewById(R.id.tv_order_address_s);
            viewHolder.tv_order_address_e = convertView.findViewById(R.id.tv_order_address_e);
            viewHolder.tv_order_huowu_name = convertView.findViewById(R.id.tv_order_huowu_name);
            viewHolder.tv_order_send_time = convertView.findViewById(R.id.tv_order_send_time);
            viewHolder.tv_order_status = convertView.findViewById(R.id.tv_order_status);
            //通过setTag将convertView与viewHolder关联
            convertView.setTag(viewHolder);
        }else{//如果缓存池中有对应的view缓存，则直接通过getTag取出viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 设置控件的数据
        viewHolder.tv_order_status.setText(Tools.getXianjieType(mList.get(position).getSettlementMethod()));//1现结2月结3合同结
        viewHolder.tv_order_no.setText("运费："+ mList.get(position).getWaybillFee());
        String sProvince = mList.get(position).getConsignerProvince();
        String sCity = mList.get(position).getConsignerCity();
        viewHolder.tv_order_address_s.setText(sProvince+" "+sCity);
        String eProvince = mList.get(position).getReceiverProvince();
        String eCity = mList.get(position).getReceiverCity();
        viewHolder.tv_order_address_e.setText(eProvince+" "+eCity);

        String strWeight = mList.get(position).getGoodsWeight()==null?"0":mList.get(position).getGoodsWeight();
        String strVolume = mList.get(position).getGoodsvolume()==null?"0":mList.get(position).getGoodsvolume();
        Double w = Double.parseDouble(strWeight);
        Double v = Double.parseDouble(strVolume);
        if (mList.get(position).getGoodsWeight()==null){
            viewHolder.tv_order_huowu_name.setText(mList.get(position).getGoodsName()+" / "+v.intValue()+"方");
        }else {
            viewHolder.tv_order_huowu_name.setText(mList.get(position).getGoodsName()+" / "+w.intValue()+"吨");
        }

        viewHolder.tv_order_send_time.setText("发货时间："+mList.get(position).getSendTime());
        return convertView;
    }

    // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
    class ViewHolder{
        TextView tv_order_no,tv_order_address_s,tv_order_address_e,tv_order_huowu_name,tv_order_send_time,tv_order_status;
    }
}

