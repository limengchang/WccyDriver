package com.bfbyxx.wccydriver.entity;

import java.io.Serializable;

/**
 * Created by MARK on 2018/8/8.
 */

public class BankInfo implements Serializable{
    private String CreateId;
    private String CreateTime;
    private String UpdateId;
    private String UpdateUser;
    private String UpdateTime;
    private int Deleted;
    private String UserId;
    private String AccountNo;
    private String AccountName;
    private String AccountBank;
    private String AccountCategory;
    private String Mobile;
    private String AccountType;
    private String Id;
    private String BankCardNo;
    private String BankName;
    private String BankType;

    public String getBankType() {
        return BankType;
    }

    public void setBankType(String bankType) {
        BankType = bankType;
    }
    public String getBankCardNo() {
        return BankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        BankCardNo = bankCardNo;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getAccountCategory() {
        return AccountCategory;
    }

    public void setAccountCategory(String accountCategory) {
        AccountCategory = accountCategory;
    }

    public String getCreateId() {
        return CreateId;
    }

    public void setCreateId(String createId) {
        CreateId = createId;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getUpdateId() {
        return UpdateId;
    }

    public void setUpdateId(String updateId) {
        UpdateId = updateId;
    }

    public String getUpdateUser() {
        return UpdateUser;
    }

    public void setUpdateUser(String updateUser) {
        UpdateUser = updateUser;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

    public int getDeleted() {
        return Deleted;
    }

    public void setDeleted(int deleted) {
        Deleted = deleted;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getAccountNo() {
        return AccountNo;
    }

    public void setAccountNo(String accountNo) {
        AccountNo = accountNo;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getAccountBank() {
        return AccountBank;
    }

    public void setAccountBank(String accountBank) {
        AccountBank = accountBank;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    @Override
    public String toString() {
        return "BankInfo{" +
                "CreateId='" + CreateId + '\'' +
                ", CreateTime='" + CreateTime + '\'' +
                ", UpdateId='" + UpdateId + '\'' +
                ", UpdateUser='" + UpdateUser + '\'' +
                ", UpdateTime='" + UpdateTime + '\'' +
                ", Deleted=" + Deleted +
                ", UserId='" + UserId + '\'' +
                ", AccountNo='" + AccountNo + '\'' +
                ", AccountName='" + AccountName + '\'' +
                ", AccountBank='" + AccountBank + '\'' +
                ", AccountCategory='" + AccountCategory + '\'' +
                ", Mobile='" + Mobile + '\'' +
                ", AccountType='" + AccountType + '\'' +
                ", Id='" + Id + '\'' +
                '}';
    }
}
