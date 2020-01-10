package com.bfbyxx.wccydriver.entity;

import java.io.Serializable;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 11:27
 */
public class MeItem implements Serializable {
    private int left_icon;
    private String name;
    private String right_icon;

    public int getLeft_icon() {
        return left_icon;
    }

    public void setLeft_icon(int left_icon) {
        this.left_icon = left_icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRight_icon() {
        return right_icon;
    }

    public void setRight_icon(String right_icon) {
        this.right_icon = right_icon;
    }
}

