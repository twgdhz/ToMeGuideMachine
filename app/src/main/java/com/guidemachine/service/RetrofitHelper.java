package com.guidemachine.service;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.guidemachine.util.share.SPHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/11/7 0007 17:19
 * description: 主要用于Retrofit的初始化
 */
public class RetrofitHelper {

    private Context mCntext;

    OkHttpClient client;
    GsonConverterFactory factory = GsonConverterFactory.create(new GsonBuilder().create());
    private static RetrofitHelper instance = null;
    private Retrofit mRetrofit = null;

    public static RetrofitHelper getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitHelper(context);
        }
        return instance;
    }

    private RetrofitHelper(Context mContext) {
        mCntext = mContext;
        init();
    }

    //https://www.jianshu.com/p/ea7c7e3c2daa
    Authenticator authenticator = new Authenticator() {//当服务器返回的状态码为401时，会自动执行里面的代码，也就实现了自动刷新token
        @Override
        public Request authenticate(Route route, Response response) throws IOException {
            return response.request().newBuilder()
                    .addHeader("token", "")
                    .build();
        }
    };
    Interceptor tokenInterceptor = new Interceptor() {//全局拦截器，往请求头部添加 token 字段，实现了全局添加 token
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request originalRequest = chain.request();//获取请求
            Request tokenRequest = null;
            if (TextUtils.isEmpty(SPHelper.getInstance(mCntext).getToken())) {//对 token 进行判空，如果为空，则不进行修改
                return chain.proceed(originalRequest);
            }
            tokenRequest = originalRequest.newBuilder()//往请求头中添加 token 字段
//                    .header("token", SPHelper.getInstance(mCntext).getToken())
                    .header("Authorization", SPHelper.getInstance(mCntext).getToken())
//                    .header("Authorization", "Bearer" + " " + "41626eb8-89c0-4772-b82d-13384b0bf2b2")
                    .build();
            Log.d("token", SPHelper.getInstance(mCntext).getToken());
            return chain.proceed(tokenRequest);
        }
    };

    private void init() {
        client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(tokenInterceptor)
                .authenticator(authenticator)
                .build();
        resetApp();
    }

    private void resetApp() {
        mRetrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.0.128:8080/")//java高
//                .baseUrl("http://192.168.0.157:8080/")//java刘
//                .baseUrl("http://192.168.0.102:8080/")//java龚
//                .baseUrl("http://39.98.168.124:8080/")//阿里云
                .baseUrl("http://tome3pay.zhihuiquanyu.com/")//阿里云
                .client(client)
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public RetrofitService getServer() {
        return mRetrofit.create(RetrofitService.class);//创建RetrofitService 实体类
    }
}
