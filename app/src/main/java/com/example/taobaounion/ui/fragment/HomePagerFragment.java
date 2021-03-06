package com.example.taobaounion.ui.fragment;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.custom.AutoLoopViewPager;
import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.model.domain.HomePagerContent;
import com.example.taobaounion.model.domain.IBaseInfo;
import com.example.taobaounion.presenter.ICategoryPagerPresenter;
import com.example.taobaounion.ui.adapter.LinearItemContentAdapter;
import com.example.taobaounion.ui.adapter.LooperPagerAdapter;
import com.example.taobaounion.utils.Constants;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.SizeUtils;
import com.example.taobaounion.utils.TicketUtils;
import com.example.taobaounion.utils.ToastUtils;
import com.example.taobaounion.view.ICategoryPagerCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.view.TbNestedScrollView;

import java.util.List;

import butterknife.BindView;

public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback, LinearItemContentAdapter.OnlistItemClickListener, LooperPagerAdapter.OnLooperPagerItemClickListener {

    private ICategoryPagerPresenter mCategoryPagePresenter;
    private int mMaterialId;

    @BindView(R.id.home_pager_content_list)
    public RecyclerView mContentList;
    private LinearItemContentAdapter mContentAdapter;

    @BindView(R.id.looper_pager)
    public AutoLoopViewPager looperPager;

    @BindView(R.id.home_pager_title)
    public TextView currentCategoryTitle;

    @BindView(R.id.looper_point_container)
    public LinearLayout looperPointContainer;

    @BindView(R.id.home_pager_refresh)
    public TwinklingRefreshLayout twinklingRefreshLayout;

    @BindView(R.id.home_pager_parent)
    public LinearLayout homePagerParent;

    @BindView(R.id.home_pager_nested)
    public TbNestedScrollView homePagerNested;

    @BindView(R.id.home_pager_header_container)
    public LinearLayout homeHeaderContainer;
    private LooperPagerAdapter mLooperPagerAdapter;

    public static HomePagerFragment newInstance(Categories.DataBean category) {
        HomePagerFragment homePagerFragment = new HomePagerFragment();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.getTitle());
        bundle.putInt(Constants.KEY_HOME_MATERIAL_ID, category.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        //?????????????????????
        looperPager.startLoop();
        LogUtils.d(this,"onResume....");
    }

    @Override
    public void onPause() {
        super.onPause();
        //?????????????????????
        looperPager.stopLoop();
        LogUtils.d(this,"onPause....");
    }

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void initView(View rootView) {

        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 10;
                outRect.bottom = 10;

            }
        });
        //?????????????????????
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        //??????,???????????????
        mContentAdapter = new LinearItemContentAdapter();
        mContentList.setAdapter(mContentAdapter);

        //????????????????????? ?????????
        mLooperPagerAdapter = new LooperPagerAdapter();

        looperPager.setAdapter(mLooperPagerAdapter);
        looperPager.setDuration(5000);
        //??????refresh????????????
        twinklingRefreshLayout.setEnableRefresh(false); //??????????????????
        twinklingRefreshLayout.setEnableLoadmore(true); //??????????????????
    }

    @Override
    protected void initListener() {
        //OnGlobalLayoutListener ???ViewTreeObserver???????????????
        //??????????????????????????????????????????????????????ViewTreeObserver?????????????????????????????????????????????????????????(observer)??????????????????????????????????????????????????????
        homePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (homeHeaderContainer == null){
                    return;
                }

                int headerHeight = homeHeaderContainer.getMeasuredHeight();
                homePagerNested.setHeaderHeight(headerHeight);
                int measuredHeight = homePagerParent.getMeasuredHeight();
//                LogUtils.d(this,"measuredHeight --- >" + measuredHeight);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mContentList.getLayoutParams();
                layoutParams.height = measuredHeight;
                mContentList.setLayoutParams(layoutParams);
                if (measuredHeight != 0){
                    homePagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        looperPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int targetPosition = position % (mLooperPagerAdapter.getDataSize() + 1);
//                LogUtils.d(this,"targetPosition --- >" + targetPosition);
                //???????????????
                updateLooperIndicator(targetPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) { //????????????

            }
        });

        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                LogUtils.d(this,"?????????onLoadMore...");
                twinklingRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        twinklingRefreshLayout.finishLoadmore();
                        //TODO:??????????????????
                        if (mCategoryPagePresenter != null) {
                            mCategoryPagePresenter.loadMore(mMaterialId);
                        }
                    }
                },2000);
            }
        });

        mContentAdapter.setOnlistItemClickListener(this);
        mLooperPagerAdapter.setOnLooperPagerItemClickListener(this);
    }

    //???????????????
    private void updateLooperIndicator(int targetPosition) {
        for (int i = 0; i < looperPointContainer.getChildCount(); i++) {
            View child = looperPointContainer.getChildAt(i);
            if (i == targetPosition) {
                child.setBackgroundResource(R.drawable.shape_indicator_point);
            } else {
                child.setBackgroundResource(R.drawable.shape_indicator_point_unselect);
            }
        }
    }

    @Override
    protected void initPresenter() {
        mCategoryPagePresenter = PresenterManager.getInstance().getCategoryPagePresenter();
        mCategoryPagePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        mMaterialId = arguments.getInt(Constants.KEY_HOME_MATERIAL_ID);
        LogUtils.d(this, "title --- >" + title);
        LogUtils.d(this, "material --- >" + mMaterialId);
        if (mCategoryPagePresenter != null) {
            mCategoryPagePresenter.getContentByCategoryId(mMaterialId);
        }
        //??????????????????
        if (currentCategoryTitle != null) {
            currentCategoryTitle.setText(title);
        }
    }

    @Override
    public void onContentLoaded(List<HomePagerContent.DataBean> contents) {
        //??????????????????
        //TODO:??????ui
        mContentAdapter.setData(contents);
        setUpState(State.SUCCESS);
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
    public void onLoadMoreError() {
        ToastUtils.showToast("????????????...");
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onLoadMoreEmpty() {
        Toast.makeText(getContext(),"???????????????",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadMoreLoaded(List<HomePagerContent.DataBean> contents) {
        //?????????????????????????????????
        mContentAdapter.addData(contents);
        //?????????????????????????????????
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();
        }
        Toast.makeText(getContext(),"?????????" + contents.size() + "?????????",Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {
        LogUtils.d(this, "looper --- > " + contents.size());
        //????????????????????????????????????
        mLooperPagerAdapter.setData(contents);
        int dx = (Integer.MAX_VALUE / 2) % contents.size();
        int target = (Integer.MAX_VALUE / 2) - dx;

        looperPager.setCurrentItem(target);
        looperPointContainer.removeAllViews();
        //?????????
        for (int i = 0; i < contents.size(); i++) {
            View point = new View(getContext());
            int size = SizeUtils.dip2px(getContext(), 8);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            layoutParams.leftMargin = SizeUtils.dip2px(getContext(), 8);
            layoutParams.rightMargin = SizeUtils.dip2px(getContext(), 8);
            point.setLayoutParams(layoutParams);
            looperPointContainer.addView(point);
        }
    }

    @Override
    public int getCategoryId() {
        return mMaterialId;
    }

    /**
     * ??????
     */
    @Override
    protected void release() {
        if (mCategoryPagePresenter != null) {
            mCategoryPagePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onItemClick(IBaseInfo item) {
        //?????????????????????
        //LogUtils.d(this,"item click ---- >" + item);
        handleItemClick(item);
    }

    @Override
    public void onLooperItemClick(IBaseInfo item) {
        //?????????????????????
        //LogUtils.d(this,"onLooperItemClick ---- >" + item);
        handleItemClick(item);
    }

    //??????????????????
    private void handleItemClick(IBaseInfo item) {

        TicketUtils.toTicketPage(getContext(),item);
    }
}
