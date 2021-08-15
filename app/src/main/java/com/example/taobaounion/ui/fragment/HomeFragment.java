package com.example.taobaounion.ui.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.model.domain.Categories;
import com.example.taobaounion.presenter.IHomePresenter;
import com.example.taobaounion.presenter.impl.HomePresenter;
import com.example.taobaounion.ui.activity.IMainActivity;
import com.example.taobaounion.ui.activity.MainActivity;
import com.example.taobaounion.ui.activity.ScanQrCodeActivity;
import com.example.taobaounion.ui.adapter.HomePagerAdapter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.view.IHomeCallback;
import com.google.android.material.tabs.TabLayout;
import com.vondear.rxfeature.activity.ActivityCodeTool;
import com.vondear.rxfeature.activity.ActivityScanerCode;

import butterknife.BindView;


public class HomeFragment extends BaseFragment implements IHomeCallback {

    private IHomePresenter mHomePresenter;

    @BindView(R.id.home_pager)
    public ViewPager mHomePager;

    @BindView(R.id.home_indicator)
    public TabLayout mTabLayout;

    @BindView(R.id.home_page_input_box)
    public EditText mEditText;

    @BindView(R.id.scan_icon)
    public ImageView mScanIcon;
    private HomePagerAdapter mHomePagerAdapter;

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View rootView) {
        mTabLayout.setupWithViewPager(mHomePager);
        //给Viewpager设置适配器
        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        mHomePager.setAdapter(mHomePagerAdapter);
    }

    @Override
    protected void initPresenter() {
        //创建presenter
        mHomePresenter = PresenterManager.getInstance().getHomePresenter();
        mHomePresenter.registerViewCallback(this);
    }

    @Override
    protected void initListener() {
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                ((IMainActivity) activity).switch2Search();
            }
        });

        mScanIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到扫码界面
                startActivity(new Intent(getActivity(), ScanQrCodeActivity.class));
            }
        });
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_home_fragment_layout,container,false);
    }

    @Override
    protected void loadData() {
        //加载数据
        mHomePresenter.getCategories();
    }

    @Override
    public void onCategoriesLoaded(Categories categories) {
        setUpState(State.SUCCESS);
        LogUtils.d(this, "onCategoriesLoaded...");
        if (mHomePagerAdapter != null) {
            //预加载全部分类数据
            //mHomePager.setOffscreenPageLimit(categories.getData().size());
            mHomePagerAdapter.setCategoryList(categories);
        }
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
    protected void release() {
        if (mHomePresenter != null) {
            mHomePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected void onRetryClick() {
        //网络错误，重新点击率
        //重新加载分类内容
        if (mHomePresenter != null) {
            mHomePresenter.getCategories();
        }
    }
}
