package com.example.taobaounion.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private final Retrofit mRetrofit;
    private static final RetrofitManager sInstance = new RetrofitManager();

    public static RetrofitManager getInstance(){
        return sInstance;
    }

    private RetrofitManager(){
        //创建Retrofit
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit(){
        return mRetrofit;
    }
}
