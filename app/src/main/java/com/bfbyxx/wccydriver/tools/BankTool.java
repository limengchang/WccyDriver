package com.bfbyxx.wccydriver.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * 处理银行相关信息的工具类
 */
public class BankTool {

    private Context context;

    public BankTool(Context context) {
        this.context = context;
    }

    public interface Callback{
        void onResult(String bankName);
    }


    /**
     * 根据银行卡编码获取银行的名字
     */
    public void getBankNameByCode(final String bankCode,final Callback callback){
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void,Void,String> task=new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    InputStream is = context.getAssets().open("bank_code_name_mapping.txt");
                    byte[] bytes = new byte[is.available()];
                    is.read(bytes);

                    String content = new String(bytes,"utf-8");
                    JSONObject jo = new JSONObject(content);
                    String bankName = jo.getString(bankCode);

                    return bankName;

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String bankName) {
                callback.onResult(bankName);
            }
        };
        task.execute();
    }


    /**
     * 根据银行卡的类型代码,获取银行卡的类型文字描述
     */
    public String getBankCardTypeByCode(String code){
        String text="未知的卡类型";
        if(code.equals("DC")){
            text = "储蓄卡";
        }else if(code.equals("CC")){
            text = "信用卡";

        }else if(code.equals("SCC")){
            text = "准贷记卡";

        }else if(code.equals("PC")){
            text = "预付费卡";

        }
        return text;
    }

}
