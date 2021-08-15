package com.example.taobaounion.view;

import com.example.taobaounion.base.IBaseCallback;
import com.example.taobaounion.model.domain.SelectedContent;
import com.example.taobaounion.model.domain.SelectedPageCategory;

public interface ISelectPagerCallback extends IBaseCallback {

    /**
     * 分类内容结果
     * @param categories 分类内容
     */
    void onCategoriesLoaded(SelectedPageCategory categories);

    /**
     * 内容
     * @param content
     */
    void onContentLoaded(SelectedContent content);
}
