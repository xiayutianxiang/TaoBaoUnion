package com.example.taobaounion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.IBaseInfo;
import com.example.taobaounion.model.domain.SelectedContent;
import com.example.taobaounion.model.domain.SelectedPageCategory;
import com.example.taobaounion.presenter.ISelectPagerPresenter;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.ui.activity.TicketActivity;
import com.example.taobaounion.ui.adapter.SelectedPageContentAdapter;
import com.example.taobaounion.ui.adapter.SelectedPageLeftAdapter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.SizeUtils;
import com.example.taobaounion.utils.TicketUtils;
import com.example.taobaounion.view.ISelectPagerCallback;

import java.util.List;

import butterknife.BindView;


public class RecommendFragment extends BaseFragment implements ISelectPagerCallback,
        SelectedPageLeftAdapter.OnLeftItemClickListener,
        SelectedPageContentAdapter.OnSelectedPageItemClick {

    @BindView(R.id.left_category_list)
    public RecyclerView leftCategoryList;
    @BindView(R.id.reight_content_list)
    public RecyclerView rightContentList;
    @BindView(R.id.fragment_bar_title)
    public TextView barTitleTv;
    private ISelectPagerPresenter mSelectedPagePresenter;
    private SelectedPageLeftAdapter mLeftAdapter;
    private SelectedPageContentAdapter mRightAdapter;

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mSelectedPagePresenter = PresenterManager.getInstance().getSelectedPagePresenter();
        mSelectedPagePresenter.registerViewCallback(this);
        mSelectedPagePresenter.getCategories();
    }

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_with_bar_layout,container,false);
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        leftCategoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        mLeftAdapter = new SelectedPageLeftAdapter();
        leftCategoryList.setAdapter(mLeftAdapter);

        rightContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRightAdapter = new SelectedPageContentAdapter();
        rightContentList.setAdapter(mRightAdapter);
        //设置间距
        rightContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int topAndBottom = SizeUtils.dip2px(getContext(), 4);
                int leftAndRight = SizeUtils.dip2px(getContext(), 6);
                outRect.top = topAndBottom;
                outRect.bottom = topAndBottom;
                outRect.left = leftAndRight;
                outRect.right = leftAndRight;
            }
        });

        barTitleTv.setText("精选内容");
    }

    @Override
    protected void initListener() {
        mLeftAdapter.setOnLeftItemClickListener(this);
        mRightAdapter.setOnSelectedPageItemClick(this);
    }

    @Override
    public void onCategoriesLoaded(SelectedPageCategory categories) {
        setUpState(State.SUCCESS);
        //分类内容
        LogUtils.d(RecommendFragment.this, "SelectedPageCategory --- >" + categories);
        //更新UI，根据当前选中的分类获取分类详情
        List<SelectedPageCategory.DataDTO> data = categories.getData();
        mSelectedPagePresenter.getContentByCategory(data.get(0));

        mLeftAdapter.setData(categories);
    }

    @Override
    public void onContentLoaded(SelectedContent content) {
        mRightAdapter.setData(content);
        rightContentList.scrollToPosition(0);
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

    }

    @Override
    protected void onRetryClick() {
        //重新点击
        if (mSelectedPagePresenter != null) {
            mSelectedPagePresenter.reloadContent();
        }
    }

    @Override
    protected void release() {
        super.release();
        if (mSelectedPagePresenter != null) {
            mSelectedPagePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onLeftItemClick(SelectedPageCategory.DataDTO item) {
        mSelectedPagePresenter.getContentByCategory(item);
        //左侧的分类点击了
        LogUtils.d(this,"onLeftItemClick --- >" + item.getFavorites_title());
    }

    @Override
    public void OnPageItemClick(IBaseInfo item) {
        TicketUtils.toTicketPage(getContext(),item);
    }
}
