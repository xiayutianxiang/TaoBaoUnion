package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.SelectedContent;
import com.example.taobaounion.model.domain.SelectedPageCategory;
import com.example.taobaounion.presenter.ISelectPagerPresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManager;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.ISelectPagerCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectedPagePresenter implements ISelectPagerPresenter {

    private ISelectPagerCallback mViewCallback = null;
    private final Retrofit mRetrofit;
    private final Api mApi;

    public SelectedPagePresenter(){
        mRetrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = mRetrofit.create(Api.class);
    }

    @Override
    public void getCategories() {
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        }
        //拿到retrofit
        Call<SelectedPageCategory> task = mApi.getSelectedPageCategories();
        task.enqueue(new Callback<SelectedPageCategory>() {
            @Override
            public void onResponse(Call<SelectedPageCategory> call, Response<SelectedPageCategory> response) {
                int code = response.code();
                LogUtils.d(SelectedPagePresenter.this,"code --->" + code);
                if(code == HttpURLConnection.HTTP_OK){
                    SelectedPageCategory result = response.body();
                    LogUtils.d(SelectedPagePresenter.this, "response --->" + result.toString());
                    //通知UI更新
                    if (mViewCallback != null) {
                        mViewCallback.onCategoriesLoaded(result);
                    }
                }else {
                    onLoadedError();
                }
            }

            @Override
            public void onFailure(Call<SelectedPageCategory> call, Throwable t) {
                onLoadedError();
            }
        });
    }

    private void onLoadedError() {
        if (mViewCallback != null) {
            mViewCallback.onNetworkError();
        }
    }

    @Override
    public void getContentByCategory(SelectedPageCategory.DataDTO item) {
        int categoryId = item.getFavorites_id();
        String contentUrl = UrlUtils.getSelectedPageContentUrl(categoryId);
        Call<SelectedContent> task = mApi.getSelectedContent(contentUrl);
            task.enqueue(new Callback<SelectedContent>() {
                @Override
                public void onResponse(Call<SelectedContent> call, Response<SelectedContent> response) {
                    int code = response.code();
                    LogUtils.d(SelectedPagePresenter.this,"code --->" + code);
                    if(code == HttpURLConnection.HTTP_OK){
                        SelectedContent result = response.body();
                        LogUtils.d(SelectedPagePresenter.this, "response --->" + result.toString());
                        //通知UI更新
                        if (mViewCallback != null) {
                            mViewCallback.onContentLoaded(result);
                        }
                    }else {
                        onLoadedError();
                    }
                }

                @Override
                public void onFailure(Call<SelectedContent> call, Throwable t) {
                    onLoadedError();
                }
            });
    }

    @Override
    public void reloadContent() {
        this.getCategories();
    }

    @Override
    public void registerViewCallback(ISelectPagerCallback callback) {
        this.mViewCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISelectPagerCallback callback) {
        this.mViewCallback = null;
    }
}
