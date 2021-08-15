package com.example.taobaounion.presenter.impl;

import com.example.taobaounion.model.Api;
import com.example.taobaounion.model.domain.HomePagerContent;
import com.example.taobaounion.presenter.ICategoryPagerPresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.RetrofitManager;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.ICategoryPagerCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryPagePresenter implements ICategoryPagerPresenter {

    private static final Integer DEFAULT_PAGE = 1;
    private Map<Integer, Integer> pagesInfo = new HashMap<>();
    private Integer mCurrentPage;


    @Override
    public void getContentByCategoryId(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoading();
            }
        }

        //根据分类id去加载内容
        Integer targetPage = pagesInfo.get(categoryId);
        if (targetPage == null) {
            targetPage = DEFAULT_PAGE;
            pagesInfo.put(categoryId, DEFAULT_PAGE);
        }
        Call<HomePagerContent> task = createTask(categoryId, targetPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                LogUtils.d(this, "code --- > " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent pagerContent = response.body();
                    LogUtils.d(CategoryPagePresenter.this, "pagerContent ---> " + pagerContent.toString());
                    handleHomePageContentResult(pagerContent, categoryId);
                } else {
                    handleNetworkError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.d(this, "onFailure --- > " + t.toString());
            }
        });

    }

    private Call<HomePagerContent> createTask(int categoryId, Integer targetPage) {
        String homePagerUrl = UrlUtils.createHomePagerUrl(categoryId, targetPage);
        LogUtils.d(this, "homePagerUrl --- > " + homePagerUrl);
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        return api.getHomePageContent(homePagerUrl);
    }

    private void handleNetworkError(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onNetworkError();
            }
        }
    }

    private void handleHomePageContentResult(HomePagerContent pagerContent, int categoryId) {
        //通知UI更新数据
        List<HomePagerContent.DataBean> data = pagerContent.getData();
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (pagerContent == null || pagerContent.getData().size() == 0) {
                    callback.onEmpty();
                } else {
                    //sublist 截取并返回动态数组中的一部分  （fromIndex,toIndex）
                    List<HomePagerContent.DataBean> looperData = data.subList(data.size() - 5, data.size());
                    callback.onLooperListLoaded(looperData);
                    callback.onContentLoaded(data);
                }
            }
        }
    }

    private ArrayList<ICategoryPagerCallback> callbacks = new ArrayList<>();

    @Override
    public void loadMore(int categoryId) {
        //加载更多内容
        //1.拿到当前页码
        mCurrentPage = pagesInfo.get(categoryId);
        if (pagesInfo == null) {
            mCurrentPage = 1;
        }
        //2.页码++
        mCurrentPage++;
        //3.加载数据
        Call<HomePagerContent> task = createTask(categoryId, mCurrentPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                //结果
                int code = response.code();
                LogUtils.d(CategoryPagePresenter.this,"code --- >" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent result = response.body();
                    LogUtils.d(CategoryPagePresenter.this,"result ---- >" + result.toString());
                    handleLoaderResult(result,categoryId);
                }else {
                    handleLoaderMoreError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                //请求失败
                LogUtils.d(CategoryPagePresenter.this,t.toString());
                handleLoaderMoreError(categoryId);
            }
        });
        //4.处理数据结果
    }

    //加载更多成功
    private void handleLoaderResult(HomePagerContent result, int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (result == null || result.getData().size() == 0) {
                    callback.onLoadMoreEmpty();
                }else {
                    callback.onLoadMoreLoaded(result.getData());
                }
            }
        }
    }

    //加载更多失败
    private void handleLoaderMoreError(int categoryId) {
        mCurrentPage --;
        pagesInfo.put(categoryId,mCurrentPage);
        for (ICategoryPagerCallback callback : callbacks) {
            //分类id若等于当前id
            if (callback.getCategoryId() ==  categoryId) {
                callback.onLoadMoreError();
            }
        }
    }

    @Override
    public void reload(int categoryId) {

    }

    @Override
    public void registerViewCallback(ICategoryPagerCallback callback) {
        if (!callbacks.contains(callback)) {
            callbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(ICategoryPagerCallback callback) {
        callbacks.remove(callback);
    }
}
