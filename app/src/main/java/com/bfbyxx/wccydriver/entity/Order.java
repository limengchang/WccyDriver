package com.bfbyxx.wccydriver.entity;

public class Order {
    private String	Id	;//	订单ID
    private String	OrderNo	;//	订单编号
    private String	LoadID	;//	装货地
    private String	UnLoadID	;//	卸货地
    private String	Consigner	;//	发货人
    private String	Consignerphone	;//	发货人联系电话
    private String	ConsignerProvince	;//	发货省
    private String	ConsignerCity	;//	发货市
    private String	ConsignerArea	;//	发货区
    private String	ConsignerAdress	;//	发货人详细地址
    private String	Receiver	;//	收货人
    private String	ReceiverPhone	;//	收货人电话
    private String	ReceiverProvince	;//	收货省
    private String	ReceiverCity	;//	收货市
    private String	ReceiverArea	;//	收货区
    private String	ReceiverAdress	;//	收货人详细地址
    private String	OrderStatus	;//	订单状态0待处理 1已确定
    private String	OrderFee	;//	运费价格
    private String	OrderTaxFee	;//	发票税金价格
    private String	OrderTotalFee	;//	总价格
    private String	GrossProfit	;//	预计毛利
    private String	RealGrossProfit	;//	实际毛利
    private String	SendTime	;//	要求发货时间
    private String	SettlementMethod	;//	1现结2月结3合同结
    private String	SettlementStatus	;//	结算状态1未结算2已结清
    private String	IsTicket	;//	0不开票1开票
    private String	IsMedicare	;//	0不投保险1投保险
    private String	MedicareAmount	;//	保险金额
    private String	MedicareNo	;//	投保单号
    private String	VehicleTypeTitle	;//	车型要求
    private String	NeedVehicle	;//	调度需要车辆数
    private String	OrderSource	;//	订单来源
    private String	Remark	;//	备注信息
    private String	CreateId	;//	创建ID
    private String	CreateTime	;//	创建时间
    private String	UpdateId	;//	更新ID
    private String	UpdateTime	;//	更新时间
    private String	Valid	;//	是否有效
    private String GoodsName;//商品名称
//    private String GoodsName;//商品名称
    private String GoodsWeight;//商品重量 吨
    private String Goodsvolume;//商品重量 方
    private String WaybillFee;//运费
    private String WaybillNo;//运单编号
    private String WaybillStatus;//运单状态


    public String getWaybillStatus() {
        return WaybillStatus;
    }

    public void setWaybillStatus(String waybillStatus) {
        WaybillStatus = waybillStatus;
    }

    public String getWaybillNo() {
        return WaybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        WaybillNo = waybillNo;
    }

    public String getGoodsvolume() {
        return Goodsvolume;
    }

    public void setGoodsvolume(String goodsvolume) {
        Goodsvolume = goodsvolume;
    }

    public String getWaybillFee() {
        return WaybillFee;
    }

    public void setWaybillFee(String waybillFee) {
        WaybillFee = waybillFee;
    }

    public String getGoodsWeight() {
        return GoodsWeight;
    }

    public void setGoodsWeight(String goodsWeight) {
        GoodsWeight = goodsWeight;
    }

    public String getGoodsName() {
        return GoodsName;
    }

    public void setGoodsName(String goodsName) {
        GoodsName = goodsName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getLoadID() {
        return LoadID;
    }

    public void setLoadID(String loadID) {
        LoadID = loadID;
    }

    public String getUnLoadID() {
        return UnLoadID;
    }

    public void setUnLoadID(String unLoadID) {
        UnLoadID = unLoadID;
    }

    public String getConsigner() {
        return Consigner;
    }

    public void setConsigner(String consigner) {
        Consigner = consigner;
    }

    public String getConsignerphone() {
        return Consignerphone;
    }

    public void setConsignerphone(String consignerphone) {
        Consignerphone = consignerphone;
    }

    public String getConsignerProvince() {
        return ConsignerProvince;
    }

    public void setConsignerProvince(String consignerProvince) {
        ConsignerProvince = consignerProvince;
    }

    public String getConsignerCity() {
        return ConsignerCity;
    }

    public void setConsignerCity(String consignerCity) {
        ConsignerCity = consignerCity;
    }

    public String getConsignerArea() {
        return ConsignerArea;
    }

    public void setConsignerArea(String consignerArea) {
        ConsignerArea = consignerArea;
    }

    public String getConsignerAdress() {
        return ConsignerAdress;
    }

    public void setConsignerAdress(String consignerAdress) {
        ConsignerAdress = consignerAdress;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReceiver(String receiver) {
        Receiver = receiver;
    }

    public String getReceiverPhone() {
        return ReceiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        ReceiverPhone = receiverPhone;
    }

    public String getReceiverProvince() {
        return ReceiverProvince;
    }

    public void setReceiverProvince(String receiverProvince) {
        ReceiverProvince = receiverProvince;
    }

    public String getReceiverCity() {
        return ReceiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        ReceiverCity = receiverCity;
    }

    public String getReceiverArea() {
        return ReceiverArea;
    }

    public void setReceiverArea(String receiverArea) {
        ReceiverArea = receiverArea;
    }

    public String getReceiverAdress() {
        return ReceiverAdress;
    }

    public void setReceiverAdress(String receiverAdress) {
        ReceiverAdress = receiverAdress;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getOrderFee() {
        return OrderFee;
    }

    public void setOrderFee(String orderFee) {
        OrderFee = orderFee;
    }

    public String getOrderTaxFee() {
        return OrderTaxFee;
    }

    public void setOrderTaxFee(String orderTaxFee) {
        OrderTaxFee = orderTaxFee;
    }

    public String getOrderTotalFee() {
        return OrderTotalFee;
    }

    public void setOrderTotalFee(String orderTotalFee) {
        OrderTotalFee = orderTotalFee;
    }

    public String getGrossProfit() {
        return GrossProfit;
    }

    public void setGrossProfit(String grossProfit) {
        GrossProfit = grossProfit;
    }

    public String getRealGrossProfit() {
        return RealGrossProfit;
    }

    public void setRealGrossProfit(String realGrossProfit) {
        RealGrossProfit = realGrossProfit;
    }

    public String getSendTime() {
        return SendTime;
    }

    public void setSendTime(String sendTime) {
        SendTime = sendTime;
    }

    public String getSettlementMethod() {
        return SettlementMethod;
    }

    public void setSettlementMethod(String settlementMethod) {
        SettlementMethod = settlementMethod;
    }

    public String getSettlementStatus() {
        return SettlementStatus;
    }

    public void setSettlementStatus(String settlementStatus) {
        SettlementStatus = settlementStatus;
    }

    public String getIsTicket() {
        return IsTicket;
    }

    public void setIsTicket(String isTicket) {
        IsTicket = isTicket;
    }

    public String getIsMedicare() {
        return IsMedicare;
    }

    public void setIsMedicare(String isMedicare) {
        IsMedicare = isMedicare;
    }

    public String getMedicareAmount() {
        return MedicareAmount;
    }

    public void setMedicareAmount(String medicareAmount) {
        MedicareAmount = medicareAmount;
    }

    public String getMedicareNo() {
        return MedicareNo;
    }

    public void setMedicareNo(String medicareNo) {
        MedicareNo = medicareNo;
    }

    public String getVehicleTypeTitle() {
        return VehicleTypeTitle;
    }

    public void setVehicleTypeTitle(String vehicleTypeTitle) {
        VehicleTypeTitle = vehicleTypeTitle;
    }

    public String getNeedVehicle() {
        return NeedVehicle;
    }

    public void setNeedVehicle(String needVehicle) {
        NeedVehicle = needVehicle;
    }

    public String getOrderSource() {
        return OrderSource;
    }

    public void setOrderSource(String orderSource) {
        OrderSource = orderSource;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
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

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

    public String getValid() {
        return Valid;
    }

    public void setValid(String valid) {
        Valid = valid;
    }
}
