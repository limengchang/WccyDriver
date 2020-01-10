package com.bfbyxx.wccydriver.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 10:37
 */
public class HttpUtil {
    // 判断网络是否连接的代码
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * JSON转成Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMapForJson(String jsonStr) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonStr);

            Iterator<String> keyIter = jsonObject.keys();
            String key;
            Object value;
            Map<String, Object> valueMap = new HashMap<String, Object>();
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value.toString().equals("null") ? "" : value.toString());
            }
            return valueMap;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }
    /**
     * JSON转成Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMapForJsonArray(String jsonStr) {
        JSONObject jsonObject;
        try {
            JSONArray ary=new JSONArray(jsonStr);
            jsonObject = (JSONObject) ary.get(0);

            Iterator<String> keyIter = jsonObject.keys();
            String key;
            Object value;
            Map<String, Object> valueMap = new HashMap<String, Object>();
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value);
            }
            return valueMap;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    /**
     * JSON 转成 List<Map<>>
     */
    public static List<Map<String, Object>> getListForJson(String jsonStr) {
        List<Map<String, Object>> list = null;
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            JSONObject jsonObj;
            list = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObj = (JSONObject) jsonArray.get(i);
                list.add(getMapForJson(jsonObj.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static ProgressDialog progressDialog;

    public static void showProgress(Context context, String str) {
        progressDialog = ProgressDialog.show(context, null, str);
        progressDialog.setCancelable(true);
    }

    public static void showProgress(Context context, String str, boolean cancelable) {
        progressDialog = ProgressDialog.show(context, null, str);
        progressDialog.setCancelable(cancelable);
    }

    public static void dismissProgress() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }

    public static List<Map<String, String>> convertValueToString(List<Map<String, Object>> list) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        for (Map<String, Object> m : list) {
            Map<String, String> r = new HashMap<String, String>();
            for (String key : m.keySet()) {
                r.put(key, m.get(key).toString());
            }
            result.add(r);
        }

        return result;
    }

    /**
     * 为了解决ListView在ScrollView中只能显示一行数据的问题
     *
     */
//    public static void setListViewHeightBasedOnChildren(ListView listView) {
//        // 获取ListView对应的Adapter
//        BusLineInfoItemAdapter listAdapter = (BusLineInfoItemAdapter) listView.getAdapter();
//        if (listAdapter == null) {
//            return;
//        }
//
//        int totalHeight = 0;
//        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
//            View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(0, 0); // 计算子项View 的宽高
//            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight
//                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        // listView.getDividerHeight()获取子项间分隔符占用的高度
//        // params.height最后得到整个ListView完整显示需要的高度
//        listView.setLayoutParams(params);
//    }

    public static void showToast(final Activity activity, final int info, final long time){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                final Toast toast = Toast.makeText(activity, info, Toast.LENGTH_LONG);
                //toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        toast.cancel();
                    }
                }, time);
            }
        });
    }
}
