package com.rxretrofitlibrary.retrofit_rx.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author hxb
 */
public class GsonUtil {

    public static Gson get(){
        GsonBuilder gb = new GsonBuilder();
        return gb.create();
    }
}
