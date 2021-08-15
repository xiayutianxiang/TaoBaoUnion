package com.example.taobaounion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.IBaseInfo;
import com.example.taobaounion.model.domain.OnSellContent;
import com.example.taobaounion.presenter.IOnSellPagePresenter;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.ui.activity.TicketActivity;
import com.example.taobaounion.ui.adapter.OnSellContentPageAdapter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.SizeUtils;
import com.example.taobaounion.utils.TicketUtils;
import com.example.taobaounion.utils.ToastUtils;
import com.example.taobaounion.view.IOnSellPageCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import butterknife.BindView;

public class OnSellFragment extends BaseFragment implements IOnSellPageCallback, OnSellContentPageAdapter.OnSellItemClickListener {

    private IOnSellPagePresenter mOnSellPagePresenter;
    @BindView(R.id.on_sell_content_list)
    public RecyclerView mSellContentList;
    @BindView(R.id.on_sell_refresh_layout)
    public TwinklingRefreshLayout mTwinklingRefreshLayout;

    private OnSellContentPageAdapter mOnSellContentPageAdapter;

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mOnSellPagePresenter = PresenterManager.getInstance().getOnSellPagePresenter();
        mOnSellPagePresenter.registerViewCallback(this);
        mOnSellPagePresenter.getOnSellContent();
    }

    @Override
    protected void release() {
        super.release();
        if (mOnSellPagePresenter != null) {
            mOnSellPagePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_with_bar_layout,container,false);
    }

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_on_sell;
    }

    @Override
    protected void initListener() {
        super.initListener();
        mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                //加载更多
                if (mOnSellPagePresenter != null) {
                    mOnSellPagePresenter.loaderMore();
                }
            }
        });

        mOnSellContentPageAdapter.setOnSellItemClickListener(this);
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        mSellContentList.setLayoutManager(new GridLayoutManager(getContext(),2));
        mOnSellContentPageAdapter = new OnSellContentPageAdapter();
        mSellContentList.setAdapter(mOnSellContentPageAdapter);
        mSellContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(),2.5f);
                outRect.bottom = SizeUtils.dip2px(getContext(),2.5f);
                outRect.left = SizeUtils.dip2px(getContext(),2.5f);
                outRect.right = SizeUtils.dip2px(getContext(),2.5f);
            }
        });

        mTwinklingRefreshLayout.setEnableRefresh(false);
        mTwinklingRefreshLayout.setEnableLoadmore(true);
        mTwinklingRefreshLayout.setEnableOverScroll(true);
    }

    @Override
    public void onContentLoadedSuccess(OnSellContent result) {
        //getOnSellContent之后，数据加载完
        setUpState(State.SUCCESS);
        //TODO:更新UI
        mOnSellContentPageAdapter.setData(result);
        LogUtils.d(OnSellFragment.this,"result --- >" + result);
    }

    @Override
    public void onMoreLoaded(OnSellContent moreResult) {
        //
        mTwinklingRefreshLayout.finishLoadmore();
        //添加内容
        mOnSellContentPageAdapter.onMoreLoaded(moreResult);
    }

    @Override
    public void onMoreLoadedError() {
        mTwinklingRefreshLayout.finishLoadmore();
        ToastUtils.showToast("网络异常！");
    }

    @Override
    public void onMoreLoadedEmpty() {
        mTwinklingRefreshLayout.finishLoadmore();
        ToastUtils.showToast("没有更多内容");
    }

    @Override
    public void onNetworkError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onItemClickListener(IBaseInfo data) {
        TicketUtils.toTicketPage(getContext(),data);
    }
}
