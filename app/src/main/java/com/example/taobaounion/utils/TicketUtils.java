package com.example.taobaounion.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.example.taobaounion.base.BaseApplication;
import com.example.taobaounion.model.domain.IBaseInfo;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.ui.activity.TicketActivity;

public class TicketUtils {
    public static void toTicketPage(Context context,IBaseInfo baseInfo){
        String title = baseInfo.getTitle();
        //详情的地址
        String url = baseInfo.getUrl();  //领券的url
        if(TextUtils.isEmpty(url)){
            url = baseInfo.getUrl();    //若为空，那么就去详情的界面
        }
        String cover = baseInfo.getCover();
        //拿到TicketPresenter去加载数据
        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(title,url,cover);
        Intent intent = new Intent(BaseApplication.getAppContent(), TicketActivity.class);
        context.startActivity(intent);
    }
}
