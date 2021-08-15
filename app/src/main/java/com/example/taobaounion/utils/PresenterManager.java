package com.example.taobaounion.utils;

import com.example.taobaounion.presenter.ICategoryPagerPresenter;
import com.example.taobaounion.presenter.IHomePresenter;
import com.example.taobaounion.presenter.IOnSellPagePresenter;
import com.example.taobaounion.presenter.ISearchPresenter;
import com.example.taobaounion.presenter.ISelectPagerPresenter;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.presenter.impl.CategoryPagePresenter;
import com.example.taobaounion.presenter.impl.HomePresenter;
import com.example.taobaounion.presenter.impl.OnSellPagePresenter;
import com.example.taobaounion.presenter.impl.SearchPresenter;
import com.example.taobaounion.presenter.impl.SelectedPagePresenter;
import com.example.taobaounion.presenter.impl.TicketPresenter;

public class PresenterManager {

    private final ICategoryPagerPresenter mCategoryPagerPresenter;
    private final IHomePresenter mHomePresenter;
    private final ITicketPresenter mTicketPresenter;
    private final ISelectPagerPresenter mSelectedPagePresenter;
    private final IOnSellPagePresenter mOnSellPagePresenter;
    private final ISearchPresenter mSearchPresenter;

    private  PresenterManager(){
        mCategoryPagerPresenter = new CategoryPagePresenter();
        mHomePresenter = new HomePresenter();
        mTicketPresenter = new TicketPresenter();
        mSelectedPagePresenter = new SelectedPagePresenter();
        mOnSellPagePresenter = new OnSellPagePresenter();
        mSearchPresenter = new SearchPresenter();
    }

    private static final PresenterManager ourInstance = new PresenterManager();

    public static PresenterManager getInstance(){
        return ourInstance;
    }

    public ICategoryPagerPresenter getCategoryPagePresenter(){
        return mCategoryPagerPresenter;
    }

    public IHomePresenter getHomePresenter() {
        return mHomePresenter;
    }

    public ITicketPresenter getTicketPresenter() {
        return mTicketPresenter;
    }

    public ISelectPagerPresenter getSelectedPagePresenter(){
        return mSelectedPagePresenter;
    }
    public IOnSellPagePresenter getOnSellPagePresenter(){
        return mOnSellPagePresenter;
    }

    public ISearchPresenter getSearchPresenter(){
        return mSearchPresenter;
    }
}
