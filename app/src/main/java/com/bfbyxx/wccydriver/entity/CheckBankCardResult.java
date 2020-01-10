package com.bfbyxx.wccydriver.entity;


/**
 * 检测银行卡接(CheckBankCardApi)口返回的数据对象
 */
public class CheckBankCardResult {

    private String cardType;
    private String bank;
    private String key;
    private boolean validated;

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    @Override
    public String toString() {
        return "CheckBankCardResult{" +
                "cardType='" + cardType + '\'' +
                ", bank='" + bank + '\'' +
                ", key='" + key + '\'' +
                ", validated=" + validated +
                '}';
    }
}
