package com.example.taobaounion.view;

import com.example.taobaounion.base.IBaseCallback;
import com.example.taobaounion.model.domain.TicketResult;

public interface ITicketCallback extends IBaseCallback {

    //加载结果，淘口令生成成功后ui继承的接口
    void onTicketLoaded(String cover, TicketResult result);
}
