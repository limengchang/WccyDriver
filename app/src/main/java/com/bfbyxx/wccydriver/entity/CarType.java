package com.bfbyxx.wccydriver.entity;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 9:41
 */
public class CarType {
    private String Text;
    private String Id;
    public boolean isSelect=false;

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}

