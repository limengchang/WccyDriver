package com.bfbyxx.wccydriver.utils;


import android.support.v7.app.AppCompatActivity;

import java.util.LinkedList;

/**
 * activity 暂存工具类
 */
public class ActivityStorer {

    private static LinkedList<AppCompatActivity> activityList;

    public static void add(AppCompatActivity activity){
        if(activityList==null){
            activityList = new LinkedList<>();
        }

        activityList.add(activity);
    }


    public static void finishAll(){
        if(activityList!=null){
            for(AppCompatActivity ac:activityList){
                ac.finish();
            }
            clearAll();
        }

    }


    private static void clearAll(){
        activityList.clear();
        activityList = null;
    }


}
