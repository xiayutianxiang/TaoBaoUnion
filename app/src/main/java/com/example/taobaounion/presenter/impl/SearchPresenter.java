package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.Histories;
import com.example.taobaounion.model.domain.SearchRecommend;
import com.example.taobaounion.model.domain.SearchResult;
import com.example.taobaounion.presenter.ISearchPresenter;
import com.example.taobaounion.utils.JsonCacheUtil;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManager;
import com.example.taobaounion.view.ISearchPageCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchPresenter implements ISearchPresenter {

    private static final int DEFAULT_PAGE = 0;

    private Api mApi ;
    private ISearchPageCallback mSearchPageCallback = null;

    private int mCurrentPage = DEFAULT_PAGE;  //搜索当前页
    private static final String KEY_HISTORIES = "key_histories";
    private String mCurrentKeyWord;
    private int mHistoriesMaxSize = DEFAULT_HISTORIES_SIZE;
    public static final int DEFAULT_HISTORIES_SIZE = 10;
    private JsonCacheUtil mJsonCacheUtil;

    public SearchPresenter() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
        mJsonCacheUtil = JsonCacheUtil.getInstance();
    }

    @Override
    public void getHistories() {
        Histories histories = mJsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        if ( mSearchPageCallback != null){
            mSearchPageCallback.onHistoriesLoaded(histories);
        }
    }

    @Override
    public void delHistories() {
        mJsonCacheUtil.delCache(KEY_HISTORIES);
        if (mSearchPageCallback != null) {
            mSearchPageCallback.onHistoriesDeleted();
        }
    }

    private void saveHistory(String history) {

        mJsonCacheUtil = JsonCacheUtil.getInstance();
        Histories histories = mJsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        //如果存在就删掉再添加
        List<String> historiesList = null;
        if (histories != null && histories.getHistories() != null) {
            historiesList = histories.getHistories();
            if (historiesList.contains(history)) {
                historiesList.remove(history);
            }
        }
        //处理没有数据的情况
        if (historiesList == null) {
            historiesList = new ArrayList<>();
        }
        if (histories == null) {
            histories = new Histories();
            histories.setHistories(historiesList);
        }
        //对个数进行限制
        if (historiesList.size() > mHistoriesMaxSize) {
            historiesList = historiesList.subList(0, mHistoriesMaxSize);
        }
        //添加记录
        historiesList.add(history);
        mJsonCacheUtil.saveCache(KEY_HISTORIES, histories);
    }

    @Override
    public void doSearch(String keyword) {
        if(mCurrentKeyWord == null || !mCurrentKeyWord.equals(keyword)){
            this.saveHistory(keyword);
            this.mCurrentKeyWord = keyword;
        }
        if (mSearchPageCallback != null) {
            mSearchPageCallback.onLoading();
        }
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, keyword);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                LogUtils.d(SearchPresenter.this, "doSearch code --- >" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    SearchResult result = response.body();
                    handlerSearchResult(result);
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                onError();
            }
        });
    }

    private void onError() {
        if (mSearchPageCallback != null) {
            mSearchPageCallback.onNetworkError();
        }
    }

    private void handlerSearchResult(SearchResult result) {
        if (mSearchPageCallback != null) {
            if (isResultEmpty(result)) {
                //数据为空
                mSearchPageCallback.onEmpty();
            } else {
                mSearchPageCallback.onSearchSuccess(result);
            }
        }
    }

    private boolean isResultEmpty(SearchResult result) {
        try {
            return result == null || result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size() == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void reSearch() {
        if (mCurrentKeyWord == null) {
            if (mSearchPageCallback != null) {
                mSearchPageCallback.onEmpty();
            }
        } else {
            this.doSearch(mCurrentKeyWord);
        }
    }

    @Override
    public void loaderMore() {
        mCurrentPage++;
        //进行搜索
        if (mCurrentKeyWord == null) {
            if (mSearchPageCallback != null) {
                mSearchPageCallback.onEmpty();
            }
        } else {
            doSearchMore();
        }
    }

    private void doSearchMore() {
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, mCurrentKeyWord);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                LogUtils.d(SearchPresenter.this, " doSearchMore code --- >" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    SearchResult result = response.body();
                    handlerMoreSearchResult(result);
                } else {
                    onLoadedMoreError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                onLoadedMoreError();
            }
        });
    }

    //加载更多内容成功
    private void handlerMoreSearchResult(SearchResult result) {
        if (mSearchPageCallback != null) {
            if (isResultEmpty(result)) {
                //数据为空
                mSearchPageCallback.onMoreLoadedEmpty();
            } else {
                mSearchPageCallback.onMoreLoaded(result);
            }
        }
    }

    //加载更多失内容败
    private void onLoadedMoreError() {
        mCurrentPage--;
        if (mSearchPageCallback != null) {
            mSearchPageCallback.onMoreLoadedError();
        }
    }

    @Override
    public void getRecommendWords() {
        Call<SearchRecommend> task = mApi.getRecommendWords();
        task.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                int code = response.code();
                LogUtils.d(SearchPresenter.this, "code --- >" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    SearchRecommend result = response.body();
                    LogUtils.d(SearchPresenter.this, "result --- >" + result.getData());
                    if (mSearchPageCallback != null) {
                        mSearchPageCallback.onRecommendWordsLoaded(result.getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchRecommend> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void registerViewCallback(ISearchPageCallback callback) {
        this.mSearchPageCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISearchPageCallback callback) {
        this.mSearchPageCallback = null;
    }
}
