package com.guidemachine.service.manager;

import android.content.Context;


import com.guidemachine.service.RetrofitHelper;
import com.guidemachine.service.RetrofitService;
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
import rx.Observable;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/11/05 0021 14:24
 * description: 为了更方便的调用RetrofitService 中定义的方法
 */
public class DataManager {
    private RetrofitService mRetrofitService;

    public DataManager(Context context) {
        this.mRetrofitService = RetrofitHelper.getInstance(context).getServer();
    }

    public Observable<BaseBean> registerAppUser(RequestBody requestBody) {//返回的是一个泛型为Book的Observable，即被观察者
        return mRetrofitService.registerAppUser(requestBody);
    }

    public Observable<BaseBean<List<FaceRegisterBean>>> getAllFace() {
        return mRetrofitService.getAllFace();
    }

    public Observable<BaseBean> postFaceCheckComplete(RequestBody requestBody) {
        return mRetrofitService.postFaceCheckComplete(requestBody);
    }

    public Observable<BaseBean<GetCodeBean>> getCode(RequestBody requestBody) {
        return mRetrofitService.getCode(requestBody);
    }

    public Observable<BaseBean<LoginBean>> login(RequestBody requestBody) {
        return mRetrofitService.login(requestBody);
    }

    public Observable<BaseBean<ScenerySpotListBean>> getSceneryList(RequestBody requestBody, int orderType) {

        if (orderType == 1) {//景区
            return mRetrofitService.getSceneryList(requestBody);
        } else if (orderType == 4) {//厕所
            return mRetrofitService.getToiletList(requestBody);
        } else {
            return mRetrofitService.getSceneryList(requestBody);
        }
    }

    public Observable<BaseBean<ShopMarkersBean>> getShopList(String sceneryId) {
        return mRetrofitService.getShopList(sceneryId);
    }

    public Observable<BaseBean<AreaSourceBean>> getAreaSourceList(RequestBody requestBody) {
        return mRetrofitService.getAreaSourceList(requestBody);
    }

    public Observable<BaseBean<WeatherBean>> getWeather(String lng, String lat) {
        return mRetrofitService.getWeather(lng, lat);
    }

    public Observable<BaseBean> getSOSLog(RequestBody requestBody) {
        return mRetrofitService.getSOSLog(requestBody);
    }

    public Observable<BaseBean<List<FenceBean>>> getFence(RequestBody requestBody) {
        return mRetrofitService.getFence(requestBody);
    }

    public Observable<BaseBean<List<VisitHistoryBean>>> getVisitHistory(RequestBody requestBody) {
        return mRetrofitService.getVisitHistory(requestBody);
    }

    public Observable<BaseBean<ShopListBean>> selectShopList(RequestBody requestBody) {
        return mRetrofitService.selectShopList(requestBody);
    }

    public Observable<BaseBean<ShopBasicInfoBean>> selectShopBasicInfo(RequestBody requestBody) {
        return mRetrofitService.selectShopBasicInfo(requestBody);
    }

    public Observable<BaseBean<ShopGoodListBean>> selectShopGoodsList(RequestBody requestBody) {
        return mRetrofitService.selectShopGoodsList(requestBody);
    }

    public Observable<BaseBean<GoodsInfoDetailBean>> selectGoodsInfo(RequestBody requestBody) {
        return mRetrofitService.selectGoodsInfo(requestBody);
    }

    public Observable<BaseBean<List<BuyAgainBean>>> getGuideDetailGoodsList(RequestBody requestBody) {
        return mRetrofitService.getGuideDetailGoodsList(requestBody);
    }

    public Observable<BaseBean<GoodSpecBean>> selectGoodsSpec(RequestBody requestBody) {
        return mRetrofitService.selectGoodsSpec(requestBody);
    }

    public Observable<BaseBean> addGoodsToCart(RequestBody requestBody) {
        return mRetrofitService.addGoodsToCart(requestBody);
    }

    public Observable<BaseBean<ShoppingCartListBean>> getShoppingCartList(RequestBody requestBody) {
        return mRetrofitService.getShoppingCartList(requestBody);
    }

    public Observable<BaseBean> deleteCart(RequestBody requestBody) {
        return mRetrofitService.deleteCart(requestBody);
    }

    public Observable<BaseBean> updateCartKindGoodsItemNum(RequestBody requestBody) {
        return mRetrofitService.updateCartKindGoodsItemNum(requestBody);
    }

    public Observable<BaseBean<KindOrderFillingBean>> kindOrderFilling(RequestBody requestBody) {
        return mRetrofitService.kindOrderFilling(requestBody);
    }

    public Observable<BaseBean<CreateOrderBean>> createGoodsOrder(RequestBody requestBody) {
        return mRetrofitService.createGoodsOrder(requestBody);
    }

    public Observable<BaseBean<List<CartKindOrderFillingBean>>> cartKindOrderFilling(RequestBody requestBody) {
        return mRetrofitService.cartKindOrderFilling(requestBody);
    }

    public Observable<BaseBean<CreateOrderBean>> createCartOrder(RequestBody requestBody) {
        return mRetrofitService.createCartOrder(requestBody);
    }

    public Observable<BaseBean<SelectOrderBean>> selectOrders(RequestBody requestBody) {
        return mRetrofitService.selectOrders(requestBody);
    }

    public Observable<BaseBean> cancelOrder(RequestBody requestBody) {
        return mRetrofitService.cancelOrder(requestBody);
    }

    public Observable<BaseBean> buyAgain(RequestBody requestBody) {
        return mRetrofitService.buyAgain(requestBody);
    }

    public Observable<BaseBean<OrderDetailBean>> selectOrderDetail(RequestBody requestBody) {
        return mRetrofitService.selectOrderDetail(requestBody);
    }

    public Observable<BaseBean<CreateOrderRefundReserveBean>> createOrderRefundReserve(RequestBody requestBody) {
        return mRetrofitService.createOrderRefundReserve(requestBody);
    }

    public Observable<BaseBean> pushOrderRefund(RequestBody requestBody) {
        return mRetrofitService.pushOrderRefund(requestBody);
    }
    public Observable<BaseBean<GetSelfCodeBean>> getQrCode(String orderId) {
        return mRetrofitService.getQrCode(orderId);
    }

    public Observable<BaseBean<List<AdminStatisticsBean>>> selectStatisticsAdmin(RequestBody requestBody) {
        return mRetrofitService.selectStatisticsAdmin(requestBody);
    }

    public Observable<BaseBean<AdminDeviceRentNumBean>> selectStatisticsRentNumAdmin(RequestBody requestBody) {
        return mRetrofitService.selectStatisticsRentNumAdmin(requestBody);
    }

    public Observable<BaseBean<SceneryServiceInfoBean>> selectStatisticsSceneryAdmin(RequestBody requestBody) {
        return mRetrofitService.selectStatisticsSceneryAdmin(requestBody);
    }

    public Observable<BaseBean<SceneryServiceInfoDetail>> selectStatisticsSceneryRentNumAdmin(RequestBody requestBody) {
        return mRetrofitService.selectStatisticsSceneryRentNumAdmin(requestBody);
    }

    public Observable<BaseBean<SceneryServiceInfoDetail>> selectStatisticsSceneryRentNumScenery(RequestBody requestBody) {
        return mRetrofitService.selectStatisticsSceneryRentNumScenery(requestBody);
    }

    public Observable<BaseBean<List<AdminTouristTeamBean>>> selectTouristTeamScenery(RequestBody requestBody) {
        return mRetrofitService.selectTouristTeamScenery(requestBody);
    }

    public Observable<BaseBean> allotTerminalToTouristTeam(RequestBody requestBody) {
        return mRetrofitService.allotTerminalToTouristTeam(requestBody);
    }
    public Observable<BaseBean<TeamLocationBean>> selectHomeData(RequestBody requestBody) {
        return mRetrofitService.selectHomeData(requestBody);
    }
    public Observable<BaseBean> issuanceTrip(RequestBody requestBody) {
        return mRetrofitService.issuanceTrip(requestBody);
    }
    public Observable<BaseBean<List<SearchJourneyBean>>> selectTrip(RequestBody requestBody) {
        return mRetrofitService.selectTrip(requestBody);
    }
    public Observable<BaseBean> deleteTripBath(RequestBody requestBody) {
        return mRetrofitService.deleteTripBath(requestBody);
    }
    public Observable<BaseBean> clearTrip(RequestBody requestBody) {
        return mRetrofitService.clearTrip(requestBody);
    }
    public Observable<BaseBean<LoginBean>> teamAccountLogin(RequestBody requestBody) {
        return mRetrofitService.teamAccountLogin(requestBody);
    }
    public Observable<BaseBean<LoginBean>> adminLogin(RequestBody requestBody) {
        return mRetrofitService.adminLogin(requestBody);
    }
    public Observable<BaseBean<CheckGuidePhoneBean>> checkGuidePhone(String telephone) {
        return mRetrofitService.checkGuidePhone(telephone);
    }
    public Observable<BaseBean<LoginBean>> loginGuide(RequestBody requestBody) {
        return mRetrofitService.loginGuide(requestBody);
    }
    public Observable<ResponseBody> kindPay(RequestBody requestBody) {
        return mRetrofitService.kindPay(requestBody);
    }
    public Observable<BaseBean> addfence(RequestBody requestBody) {
        return mRetrofitService.addfence(requestBody);
    }
    public Observable<BaseBean<List<FenceBean>>> queryfencebyguideid(RequestBody requestBody) {
        return mRetrofitService.queryfencebyguideid(requestBody);
    }
    public Observable<BaseBean> updatefencebyguide(RequestBody requestBody) {
        return mRetrofitService.updatefencebyguide(requestBody);
    }
    public Observable<BaseBean> addlinkman(RequestBody requestBody) {
        return mRetrofitService.addlinkman(requestBody);
    }
    public Observable<BaseBean<List<LinkManListBean>>> querylinkman(RequestBody requestBody) {
        return mRetrofitService.querylinkman(requestBody);
    }
    public Observable<BaseBean> updatelinkman(RequestBody requestBody) {
        return mRetrofitService.updatelinkman(requestBody);
    }
}
