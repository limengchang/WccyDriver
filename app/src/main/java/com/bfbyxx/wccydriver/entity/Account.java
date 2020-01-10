package com.bfbyxx.wccydriver.entity;

public class Account {
    private String Amount;//金额
    private String CreateTime;//时间
    private String AType;//类型

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getAType() {
        return AType;
    }

    public void setAType(String AType) {
        this.AType = AType;
    }
}
