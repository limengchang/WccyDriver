package com.bfbyxx.wccydriver.entity;

/**
 * 行驶证识别结果信息数据对象
 */
public class DrivingLicOcrInfo {

    private String VehicleNo;
    private String VehicleType;
    private String Name;
    private String Address;
    private String Nature;
    private String Brand;
    private String VehicleNumber;
    private String EngineNumber;
    private String RegTime;
    private String CertTime;

    public String getVehicleNo() {
        return VehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        VehicleNo = vehicleNo;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getNature() {
        return Nature;
    }

    public void setNature(String nature) {
        Nature = nature;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getVehicleNumber() {
        return VehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        VehicleNumber = vehicleNumber;
    }

    public String getEngineNumber() {
        return EngineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        EngineNumber = engineNumber;
    }

    public String getRegTime() {
        return RegTime;
    }

    public void setRegTime(String regTime) {
        RegTime = regTime;
    }

    public String getCertTime() {
        return CertTime;
    }

    public void setCertTime(String certTime) {
        CertTime = certTime;
    }
}
