package com.example.taobaounion.presenter;

import com.example.taobaounion.base.IBasePresenter;
import com.example.taobaounion.model.domain.SelectedPageCategory;
import com.example.taobaounion.view.ISelectPagerCallback;

public interface ISelectPagerPresenter extends IBasePresenter<ISelectPagerCallback> {

    /**
     * 获取分类
     */
    void getCategories();

    /**
     * 根据分类获取分类内容
     * @param item
     */
    void getContentByCategory(SelectedPageCategory.DataDTO item);

    /**
     * 重新加载分类内容
     */
    void reloadContent();
}
