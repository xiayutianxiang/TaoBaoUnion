package com.example.taobaounion.presenter;

import com.example.taobaounion.base.IBasePresenter;
import com.example.taobaounion.view.ICategoryPagerCallback;

public interface ICategoryPagerPresenter extends IBasePresenter<ICategoryPagerCallback> {

    /**
     * 根据分类id加载内容
     */
    void getContentByCategoryId(int categoryId);

    void loadMore(int categoryId);

    void reload(int categoryId);

}
