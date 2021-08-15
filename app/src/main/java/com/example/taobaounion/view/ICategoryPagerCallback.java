package com.example.taobaounion.view;

import com.example.taobaounion.base.IBaseCallback;
import com.example.taobaounion.model.domain.HomePagerContent;

import java.util.List;

public interface ICategoryPagerCallback extends IBaseCallback {

    /**
     *数据加载回来
     * @param contents
     */
    void onContentLoaded(List<HomePagerContent.DataBean> contents);

    /**
     * 加载更多网络错误
     * @param category
     */
    void onLoadMoreError();

    /**
     * 没有更多内容
     * @param category
     */
    void onLoadMoreEmpty();

    /**
     * 更多内容为空
     * @param contents
     */
    void onLoadMoreLoaded(List<HomePagerContent.DataBean> contents);

    /**
     * 加载了更多内容
     * @param contents
     */
    void onLooperListLoaded(List<HomePagerContent.DataBean> contents);

    //获取分类ID
    int getCategoryId();
}
