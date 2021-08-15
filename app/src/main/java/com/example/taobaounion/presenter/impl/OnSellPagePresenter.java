package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.OnSellContent;
import com.example.taobaounion.presenter.IOnSellPagePresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManager;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.IOnSellPageCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OnSellPagePresenter implements IOnSellPagePresenter {

    public static final int DEFAULT_PAGE = 1;
    private int mCurrentPage = DEFAULT_PAGE;
    private IOnSellPageCallback mOnSellPageCallback = null;
    private final Api mApi;


    public OnSellPagePresenter() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    @Override
    public void getOnSellContent() {
        if(mIsLoading) {
            return;
        }
        mIsLoading = true;
        //通知UI状态为加载中..
        if(mOnSellPageCallback != null) {
            mOnSellPageCallback.onLoading();
        }
        //获取特惠内容
        String targetUrl = UrlUtils.getOnSellPageContentUrl(mCurrentPage);
        Call<OnSellContent> task = mApi.getOnSellPageContent(targetUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call,Response<OnSellContent> response) {
                mIsLoading = false;
                int code = response.code();
                if(code == HttpURLConnection.HTTP_OK) {
                    OnSellContent result = response.body();
                    onSuccess(result);
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call,Throwable t) {
                LogUtils.d(OnSellPagePresenter.class,"error --- >" + t.toString());
                onError();
            }
        });
    }

    private void onSuccess(OnSellContent result) {
        if(mOnSellPageCallback != null) {
            try {
                if(isEmpty(result)) {
                    onEmpty();
                } else {
                    mOnSellPageCallback.onContentLoadedSuccess(result);
                }
            } catch(Exception e) {
                e.printStackTrace();
                onEmpty();
            }
        }
    }

    private boolean isEmpty(OnSellContent content) {
        int size = content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
        return size == 0;
    }

    private void onEmpty() {
        if(mOnSellPageCallback != null) {
            mOnSellPageCallback.onEmpty();
        }
    }

    private void onError() {
        mIsLoading = false;
        if(mOnSellPageCallback != null) {
            mOnSellPageCallback.onNetworkError();
        }
    }

    @Override
    public void reLoaded() {
        //重新加载
        this.getOnSellContent();
    }

    /**
     * 当前加载状态
     */
    private boolean mIsLoading = false;

    @Override
    public void loaderMore() {
        if(mIsLoading) {
            return;
        }
        mIsLoading = true;
        //加载更多
        mCurrentPage++;
        //去加载更多内容
        String targetUrl = UrlUtils.getOnSellPageContentUrl(mCurrentPage);
        Call<OnSellContent> task = mApi.getOnSellPageContent(targetUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call,Response<OnSellContent> response) {
                mIsLoading = false;
                int code = response.code();
                if(code == HttpURLConnection.HTTP_OK) {
                    OnSellContent result = response.body();
                    onMoreLoaded(result);
                } else {
                    onMoreLoadedError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call,Throwable t) {
                onMoreLoadedError();
            }
        });
    }

    private void onMoreLoadedError() {
        mIsLoading = false;
        mCurrentPage--;
        mOnSellPageCallback.onMoreLoadedError();
    }

    /**
     * 加载更多的结果，通知UI更新.
     *
     * @param result
     */
    private void onMoreLoaded(OnSellContent result) {
        if(mOnSellPageCallback != null) {
            if(isEmpty(result)) {
                mCurrentPage--;
                mOnSellPageCallback.onMoreLoadedEmpty();
            } else {
                mOnSellPageCallback.onMoreLoaded(result);
            }
        }
    }

    @Override
    public void registerViewCallback(IOnSellPageCallback callback) {
        this.mOnSellPageCallback = callback;
    }

    @Override
    public void unregisterViewCallback(IOnSellPageCallback callback) {
        this.mOnSellPageCallback = null;
    }


}
