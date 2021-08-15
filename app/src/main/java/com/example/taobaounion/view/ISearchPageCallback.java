package com.example.taobaounion.view;

import com.example.taobaounion.base.IBaseCallback;
import com.example.taobaounion.model.domain.Histories;
import com.example.taobaounion.model.domain.SearchRecommend;
import com.example.taobaounion.model.domain.SearchResult;

import java.util.List;

public interface ISearchPageCallback extends IBaseCallback {

    /**
     * 搜索历史结果
     * @param histories
     */
    void onHistoriesLoaded(Histories histories);

    /**
     * 删除历史记录完成
     */
    void onHistoriesDeleted();

    /**
     * 搜索结果成功
     * @param result
     */
    void onSearchSuccess(SearchResult result);

    /**
     * 加载到了更多内容
     * @param result
     */
    void onMoreLoaded(SearchResult result);

    /**
     * 加载更多网络错误
     */
    void onMoreLoadedError();

    /**
     *没有亘更多内容
     */
    void onMoreLoadedEmpty();

    void onRecommendWordsLoaded(List<SearchRecommend.DataDTO> recommendWords);
}
