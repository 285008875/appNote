package com.maimemo.android.memonote.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maimemo.android.memonote.application.MemoNotesApplication;
import com.safframework.http.interceptor.LoggingInterceptor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceManager {
    private static final int DEFAULT_CONNECT_TIME = 10;
    private static final int DEFAULT_WRITE_TIME = 30;
    private static final int DEFAULT_READ_TIME = 30;

    private  final  OkHttpClient okHttpClient;
    private static final String BASE_URL = "xxxxxxxxx";
    private final Retrofit retrofit;

    private RetrofitServiceManager() {

        // 获取用户token
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_CONNECT_TIME, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_WRITE_TIME, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_READ_TIME, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
//        builder.addInterceptor((new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)));
        // 自动添加 Authorization token请求头
        builder.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                //从数据库获取用户token
                // 如果为空直接发送  ：一般只有登录请求可为空
                // 不为空携带token

                String token = MemoNotesApplication.getUserTokenInstanse();
                if (token == null) {
                    Request originalRequest = chain.request();
                    return chain.proceed(originalRequest);
                } else {
                    Request originalRequest = chain.request();
                    Request updateRequest = originalRequest.newBuilder().header("Authorization","Bearer "+token).build();
                    return chain.proceed(updateRequest);
                }
            }
        });
        LoggingInterceptor loggingInterceptor = new LoggingInterceptor.Builder()
                .loggable(true) // TODO: 发布到生产环境需要改成false
                .request()
                .requestTag("Request")
                .response()
                .responseTag("Response")
                .hideVerticalLine()// 隐藏竖线边框
                .build();
        builder.addInterceptor(loggingInterceptor);
        okHttpClient =  builder.build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss") //解析服务器Date字符串
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)  //设置使用okhttp网络请求
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))//添加转化库，默认是Gson
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())//添加回调库，采用RxJava
                .build();
    }

    private static class SingletonHolder {
        private static final RetrofitServiceManager INSTANCE = new RetrofitServiceManager();
    }

    /*
     * 获取RetrofitServiceManager
     **/
    public static RetrofitServiceManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public <T> T create(Class<T> service) {
        return retrofit.create(service);
    }



}
