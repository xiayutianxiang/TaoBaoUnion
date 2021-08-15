package com.example.taobaounion.presenter;

import com.example.taobaounion.base.IBasePresenter;
import com.example.taobaounion.view.ISearchPageCallback;

public interface ISearchPresenter extends IBasePresenter<ISearchPageCallback> {

    /**
     * 获取搜索历史
     */
    void getHistories();

    /**
     * 删除搜索历史
     */
    void delHistories();

    /**
     * 搜索
     */
    void doSearch(String keyword);

    /**
     * 重新搜索
     */
    void reSearch();

    /**
     * 获取 更多的搜索 内容
     */
    void loaderMore();

    /**
     * 获取推荐词
     */
    void  getRecommendWords();
}
