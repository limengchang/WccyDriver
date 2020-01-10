package com.bfbyxx.wccydriver.entity;

import java.io.Serializable;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 11:27
 */
public class Motorcade implements Serializable {
    /* "Id": "A389B951-974D-49DD-B37B-836AE9FCC611",
             "Sort": 0,
             "CreateTime": "2018-07-12 14:32:50",
             "UserId": "A219FB20-F3E3-4F9F-989C-F41034D4E012",
             "Name": "司机_陈定龙",
             "HeaderUrl": "",
             "Phone": "13912345678",
             "Star": 3,
             "Lcat": "",
             "DriverState": 1,
             "DoWorkStatus": 1,
             "VehicleType": "029F992C-8CE0-4361-8850-3475A814F93C",
             "VehicleTypeName": "高栏车",
             "VehicleLength": 13,
             "VehicleWeight": 32,
             "PlateNo": "粤ET7051",
             "VehicleState": 1,
             "MyCrew": 0,
             "OrderCount": null*/
    private String Id;
    private int Sort;
    private String CreateTime;
    private String UserId;
    private String Name;
    private String HeaderUrl;
    private String Phone;
    private int Star;
    private String Lcat;
    private int DriverState;
    private int DoWorkStatus;
    private String VehicleType;
    private String VehicleTypeName;
    private String VehicleId;
    private double VehicleLength;
    private int VehicleWeight;
    private String PlateNo;
    private int VehicleState;
    private int MyCrew;
    private String OrderCount;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getHeaderUrl() {
        return HeaderUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        HeaderUrl = headerUrl;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public int getStar() {
        return Star;
    }

    public void setStar(int star) {
        Star = star;
    }

    public String getLcat() {
        return Lcat;
    }

    public void setLcat(String lcat) {
        Lcat = lcat;
    }

    public int getDriverState() {
        return DriverState;
    }

    public void setDriverState(int driverState) {
        DriverState = driverState;
    }

    public int getDoWorkStatus() {
        return DoWorkStatus;
    }

    public void setDoWorkStatus(int doWorkStatus) {
        DoWorkStatus = doWorkStatus;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }

    public String getVehicleTypeName() {
        return VehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        VehicleTypeName = vehicleTypeName;
    }

    public double getVehicleLength() {
        return VehicleLength;
    }

    public void setVehicleLength(int vehicleLength) {
        VehicleLength = vehicleLength;
    }

    public int getVehicleWeight() {
        return VehicleWeight;
    }

    public void setVehicleWeight(int vehicleWeight) {
        VehicleWeight = vehicleWeight;
    }

    public String getPlateNo() {
        return PlateNo;
    }

    public void setPlateNo(String plateNo) {
        PlateNo = plateNo;
    }

    public int getVehicleState() {
        return VehicleState;
    }

    public void setVehicleState(int vehicleState) {
        VehicleState = vehicleState;
    }

    public int getMyCrew() {
        return MyCrew;
    }

    public void setMyCrew(int myCrew) {
        MyCrew = myCrew;
    }

    public String getOrderCount() {
        return OrderCount;
    }

    public void setOrderCount(String orderCount) {
        OrderCount = orderCount;
    }

    public String getVehicleId() {
        return VehicleId;
    }

    public void setVehicleId(String vehicleId) {
        VehicleId = vehicleId;
    }


    public String getVehicleInfoAssemble(){
        StringBuilder sb = new StringBuilder();

        sb.append(PlateNo);

        sb.append("/").append(VehicleLength).append("米");

        sb.append("/").append(VehicleWeight).append("吨");

        sb.append("/").append(VehicleTypeName);



        return sb.toString();
    }

}

