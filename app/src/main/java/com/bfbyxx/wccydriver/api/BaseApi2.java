package com.bfbyxx.wccydriver.api;

import com.bfbyxx.wccydriver.presenter.HttpService;
import com.rxretrofitlibrary.retrofit_rx.Api.BaseApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 11:30
 */
public abstract class BaseApi2 extends BaseApi {

    private JSONObject jsonObject;
    private int rows;
    private int page;
    private JSONArray jsonFilters = new JSONArray();
    private String where;

    public void setWhere(String where) {
        this.where = where;
    }

    /*public BaseApi2(){
            jsonObject=new JSONObject();
        }*/
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void set(String name, Object value) {
        if (jsonObject == null) jsonObject = new JSONObject();
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void add(String name, String type, Object value) {

        try {
            org.json.JSONObject j = new org.json.JSONObject();
            j.put("Name", name);
            j.put("Type", type);
            j.put("Value", value);
            jsonFilters.put(j);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Observable main(Retrofit retrofit, String authorization, String user, RequestBody infoBody) {
        return null;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);
        JSONObject root = new JSONObject();
        try {
            if (jsonObject != null)
                root.put("Param", getJsonObject());
            root.put("Rows", getRows());
            root.put("Page", getPage());
            root.put("Where", this.where);

            if (jsonFilters.length() > 0) {
                root.put("Filter", jsonFilters);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("text/json; charset=utf-8"),
                root.toString());
        return main(retrofit, getHeader(), getUserHeader(), body);
        //httpService.queryDriverVehicles(getHeader(), getUserHeader(), body);
    }


}

