package com.bfbyxx.wccydriver.presenter;

import com.rxretrofitlibrary.retrofit_rx.utils.GlobalValue;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kp
 * Date: 2019/4/25
 * Time: 10:05
 */
public interface HttpService {

    //获取token
    @FormUrlEncoded
    @POST("api/Tokens/GetToken")
    Observable<String> getToken(@FieldMap Map<String, String> options);

    //获取验证码
    @GET("/api/Token/SendVerifySms")
    Observable<String> getMsmCode(@Header("authorization") String authorization, @Query("phone") String phone,
                                  @Query("verifyType") String verifyType);

    //校验验证码(接口已经取消)
    @GET("/api/AppUser/CheckMsmCode?")
    Observable<String> checkMsmCode(@Header("authorization") String authorization,
                                    @Query("mobile") String mobile,
                                    @Query("code") String code);

    //根据资金ID获取资金流水
    @GET("/api/User/AccountWater_List")
    Observable<String> getAccountWaterList(@Header("authorization") String authorization,
                                           @Query("accountId") String accountId,@Query("Page") String Page,@Query("Rows") String Row);

    //根据保证金ID获取资金流水
    @GET("/api/User/GetBondWaterList")
    Observable<String> getGetBondWaterList(@Header("authorization") String authorization,
                                           @Query("userId") String userId,@Query("Page") String Page,@Query("Rows") String Row);


    //检测更新版本
    @GET("/api/User/GetVersionCode2")
    Observable<String> checkUpdateVerson(@Header("authorization") String authorization);

    //注册用户
    @POST("/api/Token/Register")
    Observable<String> register(@Header("authorization") String authorization,@Body RequestBody registerBody);

    //获取用户信息
    @GET("/api/User/GetUserAccount")
    Observable<String> getUserAccount(@Header("authorization") String authorization,
                                      @Query("userid") String userid);

    //检测手机号是否已经注册
    @GET("api/AppSys/CheckIsRegister")
    Observable<String> checkIsRegister(@Header("authorization") String authorization, @Header("user") String user,
                                       @Query("phone") String phone);

    //获取订单ID获取运单信息列表
    @POST("/api/Order/Order_Waybill_List")
    Observable<String> getOrderList(@Header("authorization") String authorization,
                                    @Body RequestBody loginBody);

    //获取司机端待接单列表以及运单列表
    @POST("/api/Order/PageWaybill_List")
    Observable<String> getPageWaybillList(@Header("authorization") String authorization,
                                    @Body RequestBody loginBody);

    //通过验证码 设置密码
    @POST("api/User/EditUserPwd")
    Observable<String> EditUserPwd(@Header("authorization") String authorization,
                                   @Body RequestBody loginBody);

    //检测手机号是否已经注册
    @GET("/api/Token/PhoneIsExists")
    Observable<String> checkPhoneNumber(@Header("authorization") String authorization,@Query("Phone") String Phone,@Query("UserType") String UserType);


    //登录用户
    @POST("/api/Token/Login")
    Observable<String> login(@Header("authorization") String authorization,
                             @Body RequestBody loginBody);

    //添加银行卡
    @POST("/api/User/SaveBankcard")
    Observable<String> addBankCard(@Header("authorization") String authorization,
                                   @Body RequestBody infoBody);

    //修改密码(需要原密码)
    @POST("/api/AppSys/ResetPwd")
    Observable<String> resetPwd(@Header("authorization") String authorization, @Header("user") String userHead,
                                @Body RequestBody loginBody);


    /**
     * 忘记密码时修改密码(不需要原密码)
     */
    @GET("/api/AppSys/ResetPwdWithoutOldPassword")
    Observable<String> resetPwdWithoutOldPwd(@Header("authorization") String authorization, @Header("user") String userHead,
                                             @Query("phone") String phone, @Query("newpassword") String newPwd);


    //文件图片上传
    @POST ("/api/AppSys/UploadCard")
    Observable<String> uploadImage(@Header("authorization") String authorization, @Header("user") String user,
                                   @Body MultipartBody fileBody, @Query("fileType") String fileType);

    //文件图片上传（不可上传证件）
    @POST ("/api/AppSys/UploadFile")
    Observable<String> uploadFile(@Header("authorization") String authorization, @Header("user") String user,
                                  @Body MultipartBody fileBody);

    //头像上传
    @Multipart
    @POST("/api/Attachments/Upload")
    Observable<String> uploadHeadImage(@Header("authorization") String authorization, @PartMap Map<String, RequestBody> map);

    //saveInfo
    @POST("/api/AppDriverLeader/SaveInfo")
    Observable<String> saveInfo(@Header("authorization") String authorization, @Header("user") String user,
                                @Body RequestBody infoBody);

    //saveInfo
    @POST("/api/AppDriverLeader/SaveInfoPart2")
    Observable<String> saveInfo2(@Header("authorization") String authorization,
                                 @Body RequestBody infoBody);

    //查找车源
    @POST("/api/AppDriverLeader/QueryDriverVehicles")
    Observable<String> queryDriverVehicles(@Header("authorization") String authorization, @Header("user") String user,
                                           @Body RequestBody infoBody);


    //我的车队
    @POST("/api/AppLeader/QueryMyDriverVehicles")
    Observable<String> queryMyDriverVehicles(@Header("authorization") String authorization, @Header("user") String user,
                                             @Body RequestBody infoBody);

    //获取车源详情 TYPE:1.车队长 2,司机 3,货主
    @POST("/api/AppDriverLeader/GetUserInfo")
    Observable<String> getDriverVehicleDetail(@Header("authorization") String authorization,
                                              @Header("user") String user, @Query("userid") String driverUserId,
                                              @Query("type") String type);

    //加入车源到我的车队
    @POST("/api/AppDriverLeader/AddDriver")
    Observable<String> addDriverVehicle(@Header("authorization") String authorization,
                                        @Header("user") String user, @Query("DriversId") String DriversId
            , @Query("DriverVehicleId") String DriverVehicleId);
    //移出车源
    @POST("/api/AppDriverLeader/RemoveDriver")
    Observable<String> removeDriverVehicle(@Header("authorization") String authorization,
                                           @Header("user") String user, @Query("DriversId") String driverUserId
            , @Query("DriverVehicleId") String DriverVehicleId);


    //收藏货主
    @POST("/api/AppDriverLeader/AddUserFavorites")
    Observable<String> addFavoritesHz(@Header("authorization") String authorization,
                                      @Header("user") String user, @Query("userId") String userId);
    //移除货主收藏
    @POST("/api/AppDriverLeader/RemoveUserFavorites")
    Observable<String> removeFavoritesHz(@Header("authorization") String authorization,
                                         @Header("user") String user, @Query("userId") String userId);

    //邀请司机加入
    @POST("/api/AppLeader/InviteDriver")
    Observable<String> inviteDriver(@Header("authorization") String authorization,
                                    @Header("user") String user,
                                    @Query("driverName") String driverUserId, @Query("driverPhone") String driverPhone);

    //备注司机
    @POST("/api/AppDriverLeader/RemarkDriver")
    Observable<String> remarkDriver(@Header("authorization") String authorization,
                                    @Header("user") String user,
                                    @Query("driverUserId") String driverUserId, @Query("remark") String driverPhone);
    //定位司机
    @POST("/api/AppLeader/AskDriverGPS")
    Observable<String> askDriverGPS(@Header("authorization") String authorization,
                                    @Header("user") String user, @Query("driverUserId") String driverUserId);

    //刷新司机定位
    @POST("/api/AppLeader/RefreshDriverGPS")
    Observable<String> refreshDriverGPS(@Header("authorization") String authorization,
                                        @Header("user") String user,
                                        @Query("driverUserId") String driverUserId, @Query("gps") String gps);

    //加入黑名单
    @POST("/api/AppDriverLeader/AddBlacklist")
    Observable<String> addBlacklist(@Header("authorization") String authorization,
                                    @Header("user") String user,
                                    @Query("driverUserId") String driverUserId, @Query("reason") String reason);

    //移出黑名单
    @POST("/api/AppDriverLeader/RemoveBlacklist")
    Observable<String> removeBlacklist(@Header("authorization") String authorization,
                                       @Header("user") String user, @Query("driverUserId") String driverUserId);

    //获取车型列表
    @GET("/api/Order/GetVehicleTypeList")
    Observable<String> getVehicleModeList(@Header("authorization") String authorization,
                                          @Query("keyid") String keyid);

    //获取司机ID
    @GET("/api/Vehicle/GetDriver")
    Observable<String> getDriverId(@Header("authorization") String authorization,
                                          @Query("UserID") String UserID);

    //获取车长
    @POST("/api/AppDriverLeader/GetVehicleLengthList")
    Observable<String> GetVehicleLengthList(@Header("authorization") String authorization,
                                            @Header("user") String user);


    //获取地区列表
    @POST("/api/AppSys/GetAreaList")
    Observable<String> getAreaList(@Header("authorization") String authorization,
                                   @Header("user") String user,
                                   @Query("level") String level, @Query("pid") String pid);


    // 获取最新消息列表（指令驱动)
    @POST("/api/AppLeader/GetNewMessageList")
    Observable<String> getNewMessageList(@Header("authorization") String authorization,
                                         @Header("user") String user);


    //获取我的消息列表
    @POST("/api/AppLeader/GetMyMessageList")
    Observable<String> getMyMessageList(@Header("authorization") String authorization, @Header("user") String user,
                                        @Body RequestBody infoBody);

    //获取未抢订单的列表
    @POST("/api/AppLeader/QueryNewOrders")
    Observable<String> queryNewOrders(@Header("authorization") String authorization, @Header("user") String user,
                                      @Body RequestBody infoBody);


    //获取已抢订单列表
    @POST("/api/AppLeader/QueryMyOrders")
    Observable<String> queryMyOrders(@Header("authorization") String authorization, @Header("user") String user,
                                     @Body RequestBody infoBody);


    //获取订单详情
    @POST("/api/AppLeader/GetOrderDetail")
    Observable<String> getOrderDetail(@Header("authorization") String authorization,
                                      @Header("user") String user, @Query("orderid") String orderid);



    //获取订单的退单记录
    @POST("/api/AppLeader/OrderReturnRecords")
    Observable<String> orderReturnRecords(@Header("authorization") String authorization,
                                          @Header("user") String user, @Query("orderid") String orderid);

    //抢单
    @POST("/api/AppLeader/OrderReturn")
    Observable<String> orderReturn(@Header("authorization") String authorization,
                                   @Header("user") String user, @Body RequestBody infoBody);


    //记笔记
    @POST("/api/AppDriverLeader/SupplementOrder")
    Observable<String> supplementOrder(@Header("authorization") String authorization,
                                       @Header("user") String user, @Body RequestBody infoBody);


    //获取订单笔记记录
    @POST("/api/AppLeader/GetMarkOrderRecords")
    Observable<String> getMarkOrderRecords(@Header("authorization") String authorization,
                                           @Header("user") String user, @Query("orderid") String orderid);


    //订单转发
    @POST("/api/AppLeader/ForwardOrder")
    Observable<String> forwardOrder(@Header("authorization") String authorization,
                                    @Header("user") String user,
                                    @Query("type") int type, @Query("driverUserId") String driverUserId);


//    //派车
//    @POST("/api/AppLeader/SendOrder")
//    Observable<String> sendOrder(@Header("authorization") String authorization,
//                                 @Header("user") String user,@Body RequestBody infoBody);

    //获取订单列表
    @POST("/api/AppDriverLeader/QueryOrder")
    Observable<String> queryOrder(@Header("authorization") String authorization, @Header("user") String user,
                                  @Body RequestBody infoBody);

    //经纪人订单列表
    @POST("/api/AppConsigner/QueryOrder")
    Observable<String> QueryOrder(@Header("authorization") String authorization,
                                  @Header("user") String user, @Body RequestBody infoBody);

    //抢单
    @POST("/api/AppDriverLeader/TakeOrder")
    Observable<String> takeOrder(@Header("authorization") String authorization,
                                 @Header("user") String user, @Query("orderId") String orderid);

    //退单
    @POST("/api/AppDriverLeader/ReturnOrder")
    Observable<String> returnOrder(@Header("authorization") String authorization,
                                   @Header("user") String user, @Body RequestBody infoBody);

    //货主退单
    @POST("/api/AppConsigner/CancelOrder")
    Observable<String> hztdOrder(@Header("authorization") String authorization,
                                 @Header("user") String user,
                                 @Query("orderId") String tfId);

    //获取订单详情
    @POST("/api/AppDriverLeader/GetOrderInfo")
    Observable<String> getOrderInfo(@Header("authorization") String authorization,
                                    @Header("user") String user, @Query("orderId") String orderid);

    //获取历史退单列表
    @POST("/api/AppDriverLeader/GetOrderReturnHistory")
    Observable<String> getOrderReturnHistory(@Header("authorization") String authorization,
                                             @Header("user") String user, @Query("orderId") String orderid);


    //补充订单费用信息(改变订单状态)
    @POST("/api/AppDriverLeader/SupplementOrderFee")
    Observable<String> supplementOrderFee(@Header("authorization") String authorization,
                                          @Header("user") String user, @Body RequestBody infoBody);

    /* //补充订单费用(不改变订单状态)
     @POST("/api/AppDriver/SupplementOrderFee")
     Observable<String> supplementOrderFeeNonStatusChange(@Header("authorization") String authorization,
                                                          @Header("user") String user,
                                                          @Body ReqSupOrderFeeBean params);
 */
    //派车
    @POST("/api/AppDriverLeader/SubmitOrder")
    Observable<String> submitOrder(@Header("authorization") String authorization,
                                   @Header("user") String user, @Query("orderId") String orderId, @Query("driverUserId") String driverUserId,
                                   @Query("vehicleId") String vehicleId);


    //临时创建司机派车
    @POST("api/AppDriverLeader/SubmitOrderTemp")
    Observable<String> submitOrderTemp(@Header("authorization") String authorization,
                                       @Header("user") String user,
                                       @Query("orderId") String orderId,
                                       @Body RequestBody infoBody);

    //记笔记备注
    @POST("/api/AppDriverLeader/RemarkOrder")
    Observable<String> remarkOrder(@Header("authorization") String authorization,
                                   @Header("user") String user, @Body RequestBody infoBody);


    //获取历史笔记记录
    @POST("/api/AppDriverLeader/GetOrderRemarkHistory")
    Observable<String> getOrderRemarkHistory(@Header("authorization") String authorization,
                                             @Header("user") String user,
                                             @Query("orderId") String orderId);

    //转发司机(弃用)
    @POST("/api/AppDriverLeader/SendOrder")
    Observable<String> sendOrder(@Header("authorization") String authorization,
                                 @Header("user") String user,
                                 @Query("orderId") String orderId, @Query("type") int type
            , @Query("driverUserId") String driverUserId);


    //一键转发订单
    @POST("api/AppDriverLeader/BusinessOrdersTransmit")
    Observable<String> businessOrdersTransmit(@Header("authorization") String authorization,
                                              @Header("user") String user,
                                              @Query("OrderId") String orderId, @Query("TargetUser") int targetUser);


   /* //运单列表(车队长)
    @POST("/api/AppDriverLeader/getTransportFormList")
    Observable<String> getTransportFormList(@Header("authorization") String authorization,
                                            @Header("user") String user, @Body ReqWayBillListBean paramsBean);*/


 /*   //运单列表(经纪人)
    @POST("/api/AppDriverLeader/getMyCreateTransportFormList")
    Observable<String> getMyCreateTransportFormList(@Header("authorization") String authorization,
                                                    @Header("user") String user, @Body ReqWayBillListBean paramsBean);*/


    //根据运单id查运单详细信息
    @POST("/api/AppDriverLeader/getOrder")
    Observable<String> getOrder(@Header("authorization") String authorization,
                                @Header("user") String user,
                                @Query("OrderId") String OrderId);

    //获取收藏货主
    @GET("/api/AppDriverLeader/GetUserFavorites")
    Observable<String> getUserFavoritesHz(@Header("authorization") String authorization,
                                          @Header("user") String user);
    //根据订单id查货物信息
    @GET("/api/AppDriverLeader/getOrderGoods")
    Observable<String> getOrderGoods(@Header("authorization") String authorization,
                                     @Header("user") String user,
                                     @Query("OrderId") String OrderId);


    //根据订单id查询司机详细navigation:运单列表/评价（订单）/受评价司机信息author:Lixz
    @GET("/api/AppDriverLeader/getDriverInfo")
    Observable<String> getDriverInfo(@Header("authorization") String authorization,
                                     @Header("user") String user,
                                     @Query("OrderId") String OrderId);

    //根据运单id获取司机详细信息
    @GET("/api/AppDriverLeader/getDriverInfoByTfId")
    Observable<String> getDriverInfoByTfId(@Header("authorization") String authorization,
                                           @Header("user") String user,
                                           @Query("tfid") String tfid);


    //根据订单号查询货主信息navigation:运单列表/评价（订单）/受评价货主信息author:lixz
    @GET("/api/AppDriverLeader/getClientInfo")
    Observable<String> getClientInfo(@Header("authorization") String authorization,
                                     @Header("user") String user,
                                     @Query("Orderid") String Orderid);

    //根据订单号查询车队长信息。navigation:运单列表/评价（订单）/受评价车队长信息。author:Lixz
    @GET("/api/AppDriverLeader/getDriverLead")
    Observable<String> getDriverLead(@Header("authorization") String authorization,
                                     @Header("user") String user,
                                     @Query("OrderId") String OrderId);

    // /api/AppDriverLeader/getTFProblemList获取运单异常记录列表；rtype记录人类型 1：司机；2：车队长；3：货主
    @GET("/api/AppDriverLeader/getTFProblemList")
    Observable<String> getTFProblemList(@Header("authorization") String authorization,
                                        @Header("user") String user,
                                        @Query("tfid") String tfid, @Query("rtype") String rtype);

    // 提交运单异常记录； 1、调用AppSysController.UploadFile接口上传图片;
    // 2、调用本接口上传问题。navigation:运单/运单列表/提交异常记录；
    @POST("/api/AppDriverLeader/PostTFProblem")
    Observable<String> postTFProblem(@Header("authorization") String authorization,
                                     @Header("user") String user, @Query("tfid") String tfid, @Query("recordtype") String recordtype,
                                     @Body RequestBody infoBody);


    //根据星级查询评价标签内容。navigation：运单/评价/列出评价标签项。author:Lixz。
    @POST("/api/AppDriverLeader/getCommentsTags")
    Observable<String> getCommentsTags(@Header("authorization") String authorization,
                                       @Header("user") String user,
                                       @Query("stargrade") int stargrade, @Query("category") int category);

    //写入评价结果。评价/提交评价。ixz
    @POST("/api/AppDriverLeader/SubmitCommentResutl")
    Observable<String> SubmitCommentResutl(@Header("authorization") String authorization,
                                           @Header("user") String user, @Body RequestBody infoBody);

    //根据订单返回指定运单跟踪所需的信息，包括跟踪记录、轨迹、问题记录navigation：运单/运单跟踪/过程记录。author:Lixz。
    @GET("/api/AppDriverLeader/getTransportFormRecord")
    Observable<String> getTransportFormRecord(@Header("authorization") String authorization,
                                              @Header("user") String user,
                                              @Query("tfid") String tfid);

    //根据运单ID返回运单轨迹点集navigation：运单/运单跟踪/运单轨迹。author:Lixz。
    @GET("/api/AppDriverLeader/getTransportFormPathPoints")
    Observable<String> getTransportFormPathPoints(@Header("authorization") String authorization,
                                                  @Header("user") String user,
                                                  @Query("tfid") String tfid);

    //根据订单ID查询待取货中的订单详细【货主信息，司机信息，货源信息，装卸地，时间要求】
    @POST("/api/AppDriverLeader/getOrderInfoInGetGoods")
    Observable<String> getOrderInfoInGetGoods(@Header("authorization") String authorization,
                                              @Header("user") String user,
                                              @Query("orderId") String orderId);


    //根据运单ID返回运单当前状态事件的信息
    @POST("/api/AppDriverLeader/getTFCurrentActionInfo")
    Observable<String> getTFCurrentActionInfo(@Header("authorization") String authorization,
                                              @Header("user") String user,
                                              @Query("tfId") String tfId);

    //根据运单ID返回运单所有的跟踪事件信息，包括files(images)
    @POST("/api/AppDriverLeader/getTFAllActionInfo")
    Observable<String> getTFAllActionInfo(@Header("authorization") String authorization,
                                          @Header("user") String user,
                                          @Query("ftfId") String tfId);

    //根据订单ID取退单历史运单/待取货详细
    @POST("/api/AppDriverLeader/getOrderBackHis")
    Observable<String> getOrderBackHis(@Header("authorization") String authorization,
                                       @Header("user") String user,
                                       @Query("orderId") String orderId);

    //根据订单id获取退单理由信息
    @GET("/api/AppDriverLeader/getOrderBackInfo")
    Observable<String> getOrderBackInfo(@Header("authorization") String authorization,
                                        @Header("user") String user, @Query("orderId") String orderId);

    //根据运单Id获取运单某个节点中的证明照片
    @POST("/api/AppDriverLeader/getTFProvePic")
    Observable<String> getTfProvePic(@Header("authorization") String authorization,
                                     @Header("user") String user,
                                     @Query("tfid") String tfId,
                                     @Query("statusvalue") int statusValue);


    //根据运单ID返回异常记录运单/待取货详细
    @POST("/api/AppDriverLeader/getTFExceptionInfo")
    Observable<String> getTFExceptionInfo(@Header("authorization") String authorization,
                                          @Header("user") String user,
                                          @Query("tfid") String tfid);

    //车队长确认收货运单/待取货详细
    @POST("/api/AppDriverLeader/checkOnLoad")
    Observable<String> checkOnLoad(@Header("authorization") String authorization,
                                   @Header("user") String user,
                                   @Query("tfid") String tfid);

    //根据订单ID获取费用详细运单/待取货详细/运费详细
    @POST("/api/AppDriverLeader/getOrderFeeInfo")
    Observable<String> getOrderFeeInfo(@Header("authorization") String authorization,
                                       @Header("user") String user,
                                       @Query("orderid") String orderid);


    //根据订单id获取运单详情信息
    @GET("/api/AppDriverLeader/getTFInfo")
    Observable<String> getTfDetailByOrderId(@Header("authorization") String authorization,
                                            @Header("user") String user,
                                            @Query("orderid") String orderid);


    //根据运单id获取运单详情信息接口
    @GET("api/AppDriverLeader/getTransportFormById")
    Observable<String> getTfDetialByTfId(@Header("authorization") String authorization,
                                         @Header("user") String user,
                                         @Query("id") String tfId);


    //车队长确认到货
    @POST("/api/AppDriverLeader/checkArrive")
    Observable<String> checkArrive(@Header("authorization") String authorization,
                                   @Header("user") String user,
                                   @Query("tfid") String tfId);

    //根据订单获id获取企业开票信息action:运单/待取货详细/运费详细/查看发票台头信息
    @POST("/api/AppDriverLeader/getInvoiceInfo")
    Observable<String> getInvoiceInfo(@Header("authorization") String authorization,
                                      @Header("user") String user,
                                      @Query("orderid") String orderid);

    //
    @POST("/api/AppDriverLeader/getDriverList")
    Observable<String> getDriverList(@Header("authorization") String authorization,
                                     @Header("user") String user,
                                     @Query("MobileTellNo") String MobileTellNo,
                                     @Query("name") String name);

    //指派司机运单/待取货详细/重新指派司机
    @POST("/api/AppDriverLeader/ArrangedDriver")
    Observable<String> arrangedDriver(@Header("authorization") String authorization,
                                      @Header("user") String user,
                                      @Query("orderid") String orderid,
                                      @Query("driverid") String driverid);

    // /api/AppDriverLeader/GetRegInfo获取已提交的资料（完善资料未完成）

    //邀请未注册司机-指派订单 已完成接受邀请并注册成为司机，便直接派单
    @POST("/api/AppDriverLeader/InvitedDriverROrder")
    Observable<String> InvitedDriverROrder(@Header("authorization") String authorization,
                                           @Header("user") String user, @Body RequestBody infoBody);

    //添加司机到车队（未注册）/邀请未注册司机-加入车队 已完成接受邀请并注册成为司机，便直接加入车队管理
    @POST("/api/AppDriverLeader/InvitedDriverRVehicles")
    Observable<String> InvitedDriverRVehicles(@Header("authorization") String authorization,
                                              @Header("user") String user, @Body RequestBody infoBody);

    //  获取《我的》界面中的信息 已完成
    @POST("/api/AppDriverLeader/GetMySelfInfo")
    Observable<String> getMySelfInfo(@Header("authorization") String authorization,
                                     @Header("user") String user);



    //发布订单
    @POST("/api/AppConsigner/CreateOrder")
    Observable<String> CreateOrder(@Header("authorization") String authorization,
                                   @Header("user") String user, @Body RequestBody infoBody);


    //  获取车队长个人信息 已完成
    @POST("/api/AppSys/GetDriverLeaderInfo")
    Observable<String> GetDriverLeaderInfo(@Header("authorization") String authorization,
                                           @Header("user") String user);

    //  获取企业信息 已完成
    @POST("/api/AppSys/GetCompanyInfo")
    Observable<String> GetCompanyInfo(@Header("authorization") String authorization,
                                      @Header("user") String user);

    //银行卡新增
    @POST("/api/BankAccounts/Create")
    Observable<String> Create(@Header("authorization") String authorization,
                              @Header("user") String user, @Body RequestBody infoBody);

    //  修改车队长个人信息 类型（1=公司职位2=邮箱3=联系地址4=备用联系电话(一)5=备用联系电话(二)）
    @POST("/api/AppSys/UpdDriverLeaderInfo")
    Observable<String> UpdDriverLeaderInfo(@Header("authorization") String authorization,
                                           @Header("user") String user, @Query("type") String type, @Query("value") String value);

    //    钱包明细分页查询
    @POST("/api/System_PlatformRunningWater/Page")
    Observable<String> getWalletDetail(@Header("authorization") String authorization, @Header("user") String user,
                                       @Body RequestBody infoBody);

    //  设定密码，只需要传送UserId,Id,Password三个字段即可，password需要经过md5加密传送过来
    @POST("/api/MyInfo_Account/SetPassword")
    Observable<String> SetPassword(@Header("authorization") String authorization, @Header("user") String user,
                                   @Body RequestBody infoBody);

    //  修改卡密码[即重设密码]
    @POST("/api/MyInfo_Account/ReSetPassword")
    Observable<String> ReSetPassword(@Header("authorization") String authorization, @Header("user") String user,
                                     @Body RequestBody infoBody);

    // 根据用户id获取资金账户信息
    @GET("/api/MyInfo_Account/GetListByUserId")
    Observable<String> GetListByUserId(@Header("authorization") String authorization,
                                       @Header("user") String user, @Query("userid") String userid);

    // 验证用户卡和密码是否一致,只需要传送Id和Password即可
    @POST("/api/MyInfo_Account/ValidataAccount")
    Observable<String> ValidataAccount(@Header("authorization") String authorization, @Header("user") String user,
                                       @Body RequestBody infoBody);

    //查询银行卡列表
    @GET("/api/User/GetBankcardList")
    Observable<String> queryBankCard(@Header("authorization") String authorization,
                                     @Query ("UserID") String UserID);

    //删除银行卡
    @GET("/api/BankAccounts/Delete")
    Observable<String> deleteBankCard(@Header("authorization") String authorization, @Header("user") String user,
                                      @Query("id") String id);


    //   平台内部支付,参数：支付方账号Id,支付方支付密码，收款方账号Id,支付金额
    @POST("/api/MyInfo_Account/PlatPay")
    Observable<String> PlatPay(@Header("authorization") String authorization, @Header("user") String user,
                               @Body RequestBody infoBody);

    //  支付运费给司机
    @POST("/api/AppDriverLeader/PayToDriver")
    Observable<String> PayToDriver(@Header("authorization") String authorization,
                                   @Header("user") String user, @Query("orderid") String orderid);



    //获取平台收费参数信息
    @GET("api/SysParameter/GetParameterFee")
    Observable<String> getParameterFee(@Header("authorization") String authorization,
                                       @Header("user") String user, @Query("orderid") String orderid);


    //获取订单费用信息
    @GET("api/AppDriverLeader/GetOrderFeeForPlat")
    Observable<String> getOrderFeeForPlat(@Header("authorization") String authorization,
                                          @Header("user") String user, @Query("orderid") String orderid);


    //获取订单当前的付费情况
    @GET("api/AppDriverLeader/GetOrderTradeFlowRecordByOrderId")
    Observable<String> getOrderTradeFlowRecordByOrderId(@Header("authorization") String authorization,
                                                        @Header("user") String user, @Query("orderid") String orderid);

    //根据订单付费给平台
    @POST("api/AppDriverLeader/PayOrderToPlatFee")
    Observable<String> payOrderToPlatFee(@Header("authorization") String authorization,
                                         @Header("user") String user, @Body RequestBody infoBody);


    //支付运费给司机
    @POST("api/AppDriverLeader/PayOrderDriverFee")
    Observable<String> payOrderDriverFee(@Header("authorization") String authorization,
                                         @Header("user") String user, @Body RequestBody infoBody);


    //货主/经纪人 确认收货
    @POST("api/AppConsigner/checkComplete")
    Observable<String> checkComplete(@Header("authorization") String authorization,
                                     @Header("user") String user, @Query("tfid") String tfId);



    //获取用户信息(注册时完善资料填写的那些信息)
    @GET("/api/AppDriverLeader/GetInfoByUserId")
    Observable<String> getUserInfo(@Header("authorization") String authorization,
                                   @Header("user") String user, @Query("userid") String userid);


    //在发起微信支付前,调用此接口获取相关信息
//    @POST(GlobalValue.BASE_URL_PRODUCT +"/api/PaymentForWeChatApp/PostPreMoney")
    @POST("/api/PaymentForWeChatApp/PostPreMoney")
    Observable<String> wxPrePay(@Header("authorization") String authorization,
                                @Body RequestBody infoBody);


    //发起支付宝支付前,调用此接口获取相关信息
    @POST("api/PaymentForAlipayApp/ToPayPage")
    Observable<String> aliPayGetInfo(@Header("authorization") String authorization,
                                     @Body RequestBody infoBody);

    //支付宝成功支付后,进行最终确认
    @POST(GlobalValue.BASEURL+"/api/PaymentForAlipayApp/PaySuccess")
    Observable<String> alipaySuccConfirm(@Header("authorization") String authorization,
                                         @Header("user") String user,
                                         @Body RequestBody infoBody);


    //第三方接口,检查银行卡(通过银行卡号,查询出相关信息)
    @GET("https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardBinCheck=true")
    Observable<String> checkBankCard(@Query("cardNo") String cardNo);


    //查询车队长(列表)
    @POST("api/AppDriverLeader/GetDriverLeaderByLine")
    Observable<String> getDriverLeaderList(@Header("authorization") String authorization,
                                           @Header("user") String user,
                                           @Body RequestBody infoBody);

    //订单投诉
    @POST("api/AppDriverLeader/SendOrderComplaint")
    Observable<String> sendOrderComplaint(@Header("authorization") String authorization,
                                          @Header("user") String user,
                                          @Query("orderid") String orderId,
                                          @Query("content") String content);
/*
    //获取司机报价列表(根据订单id)
    @POST("/api/AppDriverLeader/GetQuotationList")
    Observable<String> getDriverOfferList(@Header("authorization") String authorization,
                                          @Header("user") String user,
                                          @Body ReqOfferListBean bean);


    //派车(从司机报价列表选择司机进行派车)
    @POST("/api/AppDriver/SubmitOrder")
    Observable<String> submitOrderByDriverOffer(@Header("authorization") String authorization,
                                                @Header("user") String user,
                                                @Body ReqSubmitOrderBean params);

    //获取司机的个人信息和车辆信息总和
    @GET("api/AppDriver/GetMyInfo")
    Observable<String> getDriverTotalInfo(@Header("authorization") String authorization, @Header("user") String user,
                                          @Query("vDriverID") String driverId);

    //保存我的个人信息和车辆信息总和
    @POST("api/AppDriver/SaveDriverInfo")
    Observable<String> saveDriverTotalInfo(@Header("authorization") String authorization, @Header("user") String user,
                                           @Body SaveDriverTotalInfoBean reqBean);

    *//**
     * 提现申请
     *//*
    @POST("/api/MyInfo_DrawOrder/Create")
    Observable<String> withdrawMoney(@Header("authorization") String authorization,
                                     @Header("user") String user, @Body ReqWithdrawMoneyBean paramsBean);*/
   //保证金提现申请
    @POST("/api/User/UserTackBond")
    Observable<String> UserTackBond(@Header("authorization") String authorization,@Body RequestBody requestBody);

    //微信保证金充值
    @POST ("/api/PaymentForWeChat/BondRecharge")
    Observable<String> BondRecharge(@Header("authorization") String authorization,@Body RequestBody requestBody);

    //支付宝保证金充值
    @POST ("/api/PaymentForAlipay/BondRecharge")
    Observable<String> AlipayBondRecharge(@Header("authorization") String authorization,@Body RequestBody requestBody);
}

