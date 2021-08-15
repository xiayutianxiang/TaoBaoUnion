package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.TicketParams;
import com.example.taobaounion.model.domain.TicketResult;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManager;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.ITicketCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TicketPresenter implements ITicketPresenter {

    private ITicketCallback mViewCallback;
    private String mCover = null;
    private TicketResult mTicketResult;

    enum LoadState{
        LOADING,SUCCESS,ERROR,NONE
    }

    private LoadState mCurrentState = LoadState.NONE;
    @Override
    public void getTicket(String title, String url, String cover) {
        this.mCover = cover;
        this.onTicketLoading();
        LogUtils.d(TicketPresenter.this,"title --- > " + title);
        LogUtils.d(TicketPresenter.this,"url --- > " + url);
        LogUtils.d(TicketPresenter.this,"cover --- > " + cover);
        String ticketUrl = UrlUtils.getTicketUrl(url);
        //去获取淘口令
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        TicketParams ticketParams = new TicketParams(ticketUrl,title);
        Call<TicketResult> task = api.getTicket(ticketParams);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                int code = response.code();
                LogUtils.d(TicketPresenter.this,"result code --- >" + code);
                if (code == HttpURLConnection.HTTP_OK){
                    mTicketResult = response.body();
                    LogUtils.d(TicketPresenter.class,"result --- >" + mTicketResult.toString());
                    //通知UI更新
                    onTicketLoadedSuccess();
                }else {
                    //失败
                    onLoadedTicketError();
                }
            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {
                if (mViewCallback != null) {
                    mViewCallback.onNetworkError();
                }
            }
        });
    }

    private void onTicketLoadedSuccess() {
        if (mViewCallback != null) {
            mViewCallback.onTicketLoaded(mCover, mTicketResult);
        }else {
            mCurrentState = LoadState.SUCCESS;
        }
    }

    private void onLoadedTicketError() {
        if (mViewCallback != null) {
            mViewCallback.onNetworkError();
        }else {
            mCurrentState = LoadState.ERROR;
        }
    }

    @Override
    public void registerViewCallback(ITicketCallback callback) {
        this.mViewCallback = callback;
        if (mCurrentState != LoadState.NONE){
            //说明状态改变了，更新UI
            if(mCurrentState == LoadState.SUCCESS){
                onTicketLoadedSuccess();
            }else if(mCurrentState == LoadState.ERROR) {
                onLoadedTicketError();
            }else if(mCurrentState == LoadState.LOADING){
                onTicketLoading();
            }
        }
    }

    @Override
    public void unregisterViewCallback(ITicketCallback callback) {
        this.mViewCallback = null;
    }

    private void onTicketLoading() {
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        }else {
            mCurrentState = LoadState.LOADING;
        }
    }
}
