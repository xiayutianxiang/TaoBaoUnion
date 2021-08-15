package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.presenter.IHomePresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManager;
import com.example.taobaounion.view.IHomeCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePresenter implements IHomePresenter {

    private IHomeCallback mCallback = null;

    @Override
    public void getCategories() {
        if (mCallback != null) {
            mCallback.onLoading();
        }
        //加载分类数据
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<Categories> task = api.getCategories();
        task.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                //数据结果
                int code = response.code();
                LogUtils.d(HomePresenter.this,"code --- >" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    Categories categories = response.body();
                    LogUtils.d(this,"categories --- >" + categories.toString());
                    if (categories == null || categories.getData().size() ==0) {
                        mCallback.onEmpty();
                    }else {
                        mCallback.onCategoriesLoaded(categories);
                    }
                }else {
                    //失败
                    LogUtils.i(this,"请求失败");
                    if (mCallback != null) {
                        mCallback.onNetworkError();
                    }
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                //加载失败
                LogUtils.e(this,"请求错误" + t.toString());
                if (mCallback != null) {
                    mCallback.onNetworkError();
                }
            }
        });
    }

    @Override
    public void registerViewCallback(IHomeCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void unregisterViewCallback(IHomeCallback callback) {
        mCallback = null;
    }

}
