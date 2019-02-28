package com.guidemachine.service;


import com.guidemachine.service.entity.AdminDeviceRentNumBean;
import com.guidemachine.service.entity.AdminStatisticsBean;
import com.guidemachine.service.entity.AdminTouristTeamBean;
import com.guidemachine.service.entity.AreaSourceBean;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.BuyAgainBean;
import com.guidemachine.service.entity.CartKindOrderFillingBean;
import com.guidemachine.service.entity.CheckGuidePhoneBean;
import com.guidemachine.service.entity.CreateOrderBean;
import com.guidemachine.service.entity.CreateOrderRefundReserveBean;
import com.guidemachine.service.entity.FaceRegisterBean;
import com.guidemachine.service.entity.FenceBean;
import com.guidemachine.service.entity.GetCodeBean;
import com.guidemachine.service.entity.GetSelfCodeBean;
import com.guidemachine.service.entity.GoodSpecBean;
import com.guidemachine.service.entity.GoodsInfoDetailBean;
import com.guidemachine.service.entity.InputOrderBean;
import com.guidemachine.service.entity.KindOrderFillingBean;
import com.guidemachine.service.entity.LinkManListBean;
import com.guidemachine.service.entity.LoginBean;
import com.guidemachine.service.entity.OrderDetailBean;
import com.guidemachine.service.entity.SceneryServiceInfoBean;
import com.guidemachine.service.entity.SceneryServiceInfoDetail;
import com.guidemachine.service.entity.ScenerySpotListBean;
import com.guidemachine.service.entity.SearchJourneyBean;
import com.guidemachine.service.entity.SelectOrderBean;
import com.guidemachine.service.entity.ShopBasicInfoBean;
import com.guidemachine.service.entity.ShopGoodListBean;
import com.guidemachine.service.entity.ShopListBean;
import com.guidemachine.service.entity.ShopMarkersBean;
import com.guidemachine.service.entity.ShoppingCartListBean;
import com.guidemachine.service.entity.TeamLocationBean;
import com.guidemachine.service.entity.VisitHistoryBean;
import com.guidemachine.service.entity.WeatherBean;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/11/05 0021 10:21
 * description: URL类
 */

public interface RetrofitService {
    //添加人脸
    @POST("user/anon/addFace")
    Observable<BaseBean> registerAppUser(@Body RequestBody requestBody);

    // 查询全部脸部特征
    @GET("user/anon/selectAllFace")
    Observable<BaseBean<List<FaceRegisterBean>>> getAllFace();

    //人脸校验完成后发送请求
    @POST("device/anon/allotTerminalToUser")
    Observable<BaseBean> postFaceCheckComplete(@Body RequestBody requestBody);

    //获取验证码
    @POST("sys/sendSms")
    Observable<BaseBean<GetCodeBean>> getCode(@Body RequestBody requestBody);

    //登陆
    @POST("sys/loginSms")
    Observable<BaseBean<LoginBean>> login(@Body RequestBody requestBody);

    //根据景区id获取景点id
    @POST("app/appdata/sceneryspot")
    Observable<BaseBean<ScenerySpotListBean>> getSceneryList(@Body RequestBody requestBody);

    //通过景区id获取厕所接口
    @POST("app/appdata/toilet")
    Observable<BaseBean<ScenerySpotListBean>> getToiletList(@Body RequestBody requestBody);

    //查询商铺marker集合
    @GET("app/appdata/anon/selectShopInfo")
    Observable<BaseBean<ShopMarkersBean>> getShopList(@Query("sceneryId") String sceneryId);

    //通过景区id查询发射源接口
    @POST("app/appdata/position")
    Observable<BaseBean<AreaSourceBean>> getAreaSourceList(@Body RequestBody requestBody);

    //查询天气
    @GET("app/appdata/weather")
    Observable<BaseBean<WeatherBean>> getWeather(@Query("lng") String lng,
                                                 @Query("lat") String lat);

    //SOS上传接口
    @POST("app/sos/upload")
    Observable<BaseBean> getSOSLog(@Body RequestBody requestBody);

    //通过IMEI查询电子围栏接口
    @POST("app/appdata/fence")
    Observable<BaseBean<List<FenceBean>>> getFence(@Body RequestBody requestBody);

    //游客历史景点访问记录查询接口
    @POST("app/appdata/positionvisithistory")
    Observable<BaseBean<List<VisitHistoryBean>>> getVisitHistory(@Body RequestBody requestBody);

    //根据景区id查询商铺列表信息
    @POST("order/anon/selectShopList")
    Observable<BaseBean<ShopListBean>> selectShopList(@Body RequestBody requestBody);

    //查询商铺信息
    @POST("order/anon/selectShopBasicInfo")
    Observable<BaseBean<ShopBasicInfoBean>> selectShopBasicInfo(@Body RequestBody requestBody);

    //查询商铺商品信息列表	(需判断一下是否有商品再进行遍历)
    @POST("order/anon/selectShopGoodsList")
    Observable<BaseBean<ShopGoodListBean>> selectShopGoodsList(@Body RequestBody requestBody);

    //查询商品信息(即商品详情)
    @POST("order/goods/selectGoodsInfo")
    Observable<BaseBean<GoodsInfoDetailBean>> selectGoodsInfo(@Body RequestBody requestBody);

    //商品详情页逛了又逛
    @POST("order/anon/selectStrollAgainGoods")
    Observable<BaseBean<List<BuyAgainBean>>> getGuideDetailGoodsList(@Body RequestBody requestBody);

    //查询商品规格信息
    @POST("order/anon/selectGoodsSpec")
    Observable<BaseBean<GoodSpecBean>> selectGoodsSpec(@Body RequestBody requestBody);

    //添加商品到购物车
    @POST("order/cart/addGoodsToCart")
    Observable<BaseBean> addGoodsToCart(@Body RequestBody requestBody);

    //查询购物车信息
    @POST("order/cart/selectCart")
    Observable<BaseBean<ShoppingCartListBean>> getShoppingCartList(@Body RequestBody requestBody);

    //删除购物车商品项
    @POST("order/cart/deleteCart")
    Observable<BaseBean> deleteCart(@Body RequestBody requestBody);

    //修改购物车商品项的数量
    @POST("order/cart/updateCart")
    Observable<BaseBean> updateCartKindGoodsItemNum(@Body RequestBody requestBody);

    //创建订单填写页数据（直接购买）
    @POST("order/order/createOrderReserve")
    Observable<BaseBean<KindOrderFillingBean>> kindOrderFilling(@Body RequestBody requestBody);

    //单个实体商品订单填写页提交订单，生成订单并返回支付页面数据（直接购买）
    @POST("order/order/createOrder")
    Observable<BaseBean<CreateOrderBean>> createGoodsOrder(@Body RequestBody requestBody);

    //创建购物车订单填写页数据数量
    @POST("order/cart/createCartOrderReserve")
    Observable<BaseBean<List<CartKindOrderFillingBean>>> cartKindOrderFilling(@Body RequestBody requestBody);

    //购物车实体商品订单填写页提交订单，生成订单并返回支付页面数据
    @POST("order/order/createCartOrder")
    Observable<BaseBean<CreateOrderBean>> createCartOrder(@Body RequestBody requestBody);

    //搜索订单
    @POST("order/order/selectOrder")
    Observable<BaseBean<SelectOrderBean>> selectOrders(@Body RequestBody requestBody);

    //取消订单
    @POST("order/order/cancelOrder")
    Observable<BaseBean> cancelOrder(@Body RequestBody requestBody);

    //再来一单
    @POST("order/cart/buyAgain")
    Observable<BaseBean> buyAgain(@Body RequestBody requestBody);

    //查询订单详情
    @POST("order/order/selectOrderDetail")
    Observable<BaseBean<OrderDetailBean>> selectOrderDetail(@Body RequestBody requestBody);

    //查询订单详情
    @POST("order/order/createOrderRefundReserve")
    Observable<BaseBean<CreateOrderRefundReserveBean>> createOrderRefundReserve(@Body RequestBody requestBody);

    //申请退款提交
    @POST("order/order/pushOrderRefund")
    Observable<BaseBean> pushOrderRefund(@Body RequestBody requestBody);

    //创建自提二维码
    @GET("order/order/getQrCode")
    Observable<BaseBean<GetSelfCodeBean>> getQrCode(@Query("orderId") String orderId);

    /* 以下是智慧全域模块*/

    //超级管理员主界面数据统计（折线图）
    @POST("app/statistics/selectStatisticsAdmin")
    Observable<BaseBean<List<AdminStatisticsBean>>> selectStatisticsAdmin(@Body RequestBody requestBody);

    //超级管理员统计景区服务商租赁次数
    @POST("app/statistics/selectStatisticsRentNumAdmin")
    Observable<BaseBean<AdminDeviceRentNumBean>> selectStatisticsRentNumAdmin(@Body RequestBody requestBody);

    //超级管理员景区服务商信息
    @POST("app/statistics/selectStatisticsSceneryAdmin")
    Observable<BaseBean<SceneryServiceInfoBean>> selectStatisticsSceneryAdmin(@Body RequestBody requestBody);

    //超级管理员统计景区服务商租赁次数及设备数（详情页）
    @POST("app/statistics/selectStatisticsSceneryRentNumAdmin")
    Observable<BaseBean<SceneryServiceInfoDetail>> selectStatisticsSceneryRentNumAdmin(@Body RequestBody requestBody);

    //景区管理员统计景区服务商租赁次数及设备数
    @POST("app/statistics/selectStatisticsSceneryRentNumScenery")
    Observable<BaseBean<SceneryServiceInfoDetail>> selectStatisticsSceneryRentNumScenery(@Body RequestBody requestBody);

    //景区管理员查询景区服务商的旅游团
    @POST("app/statistics/selectTouristTeamScenery")
    Observable<BaseBean<List<AdminTouristTeamBean>>> selectTouristTeamScenery(@Body RequestBody requestBody);

    //导游端添加设备至旅游团
    @POST("app/guideEnd/allotTerminalToTouristTeam")
    Observable<BaseBean> allotTerminalToTouristTeam(@Body RequestBody requestBody);

    //导游端首页数据(全团定位页面)
    @POST("app/guideEnd/selectHomeData")
    Observable<BaseBean<TeamLocationBean>> selectHomeData(@Body RequestBody requestBody);

    //导游端发布行程
    @POST("app/guideEnd/issuanceTrip")
    Observable<BaseBean> issuanceTrip(@Body RequestBody requestBody);

    //导游端查询行程
    @POST("app/guideEnd/selectTrip")
    Observable<BaseBean<List<SearchJourneyBean>>> selectTrip(@Body RequestBody requestBody);

    // 导游端批量删除行程
    @POST("app/guideEnd/deleteTripBath")
    Observable<BaseBean> deleteTripBath(@Body RequestBody requestBody);

    //导游端清空行程
    @POST("app/guideEnd/clearTrip")
    Observable<BaseBean> clearTrip(@Body RequestBody requestBody);

    //团账号登陆
    @POST("sys/loginTouristTeam")
    Observable<BaseBean<LoginBean>> teamAccountLogin(@Body RequestBody requestBody);

    //后台账号登录（管理员）
    @POST("sys/loginApp")
    Observable<BaseBean<LoginBean>> adminLogin(@Body RequestBody requestBody);

    //验证是否是导游的电话号码
    // /user/anon/telephone?telephone=17310515059
    @GET("user/anon/telephone")
    Observable<BaseBean<CheckGuidePhoneBean>> checkGuidePhone(@Query("telephone") String telephone);

    //导游登录
    @POST("sys/loginGuide")
    Observable<BaseBean<LoginBean>> loginGuide(@Body RequestBody requestBody);

    //二维码扫码支付
    @POST("order/order/kindPay")
    Observable<ResponseBody> kindPay(@Body RequestBody requestBody);

    //导游端添加电子围栏
    @POST("app/appdata/addfence")
    Observable<BaseBean> addfence(@Body RequestBody requestBody);

    //导游查询电子围栏
    @POST("app/appdata/queryfencebyguideid")
    Observable<BaseBean<List<FenceBean>>> queryfencebyguideid(@Body RequestBody requestBody);

    //导游端绑定电子围栏
    @POST("app/appdata/updatefencebyguide")
    Observable<BaseBean> updatefencebyguide(@Body RequestBody requestBody);

    //导游端添加紧急联系人
    @POST("app/touristteam/addlinkman")
    Observable<BaseBean> addlinkman(@Body RequestBody requestBody);

    //查询紧急联系人
    @POST("app/touristteam/querylinkman")
    Observable<BaseBean<List<LinkManListBean>>> querylinkman(@Body RequestBody requestBody);

    //修改紧急联系人
    @POST("app/touristteam/updatelinkman")
    Observable<BaseBean> updatelinkman(@Body RequestBody requestBody);
}
