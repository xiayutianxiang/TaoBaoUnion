package com.example.taobaounion.ui.activity;

import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseActivity;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.ui.fragment.HomeFragment;
import com.example.taobaounion.ui.fragment.RecommendFragment;
import com.example.taobaounion.ui.fragment.OnSellFragment;
import com.example.taobaounion.ui.fragment.SearchFragment;
import com.example.taobaounion.utils.LogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements  IMainActivity{

    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView mNavigationView;

    private HomeFragment mHomeFragment;
    private RecommendFragment mRecommendFragment;
    private OnSellFragment mOnSellFragment;
    private SearchFragment mSearchFragment;
    private FragmentManager mFm;

    @Override
    protected void initView() {
        initListener();
    }

    @Override
    protected void initEvent() {
        initFragment();
    }

    @Override
    public void switch2Search() {
        //switchFragment(mSearchFragment);
        //切换tab
        mNavigationView.setSelectedItemId(R.id.search);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    private void initFragment() {
        mHomeFragment = new HomeFragment();
        mRecommendFragment = new RecommendFragment();
        mOnSellFragment = new OnSellFragment();
        mSearchFragment = new SearchFragment();

        mFm = getSupportFragmentManager();

        switchFragment(mHomeFragment);//默认选中第一页
    }

    private void initListener() {
        //取消BottomNavigationView子item长按显示的Toast
        List<Integer> childIds = new ArrayList<>();
        childIds.add(R.id.home);
        childIds.add(R.id.recommend);
        childIds.add(R.id.red_packet);
        childIds.add(R.id.search);
        ViewGroup childAt = (ViewGroup) mNavigationView.getChildAt(0);
        for(int position = 0;position<childIds.size();position++){
            BottomNavigationItemView viewById = childAt.getChildAt(position).findViewById(childIds.get(position));
            viewById.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
        }

        mNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    LogUtils.d(MainActivity.this, "首页");
                    switchFragment(mHomeFragment);
                    break;
                case R.id.recommend:
                    LogUtils.d(MainActivity.this, "精选");
                    switchFragment(mRecommendFragment);
                    break;
                case R.id.red_packet:
                    LogUtils.d(MainActivity.this, "特惠");
                    switchFragment(mOnSellFragment);
                    break;
                case R.id.search:
                    LogUtils.d(MainActivity.this, "搜索");
                    switchFragment(mSearchFragment);
                    break;
            }
            return true;
        });
    }

    @Override
    protected void initPresenter() {

    }

    private BaseFragment lastOneFragment = null;

    private void switchFragment(BaseFragment targetFragment) {
        //若上一个与当前是同一个fragment，不需要切换
        if(lastOneFragment == targetFragment){
            return;
        }
        //每次添加fragment都需要开启事务
        FragmentTransaction transaction = mFm.beginTransaction();

        //修改成add和hide的方式来控制Fragment
        //新的没有被添加就把它添加进去，并且显示，同时若上一个不为空，则将上一个隐藏，并且将lastone设置为当前的fragment
        if (!targetFragment.isAdded()) {
            transaction.add(R.id.main_page_container,targetFragment);
        }else {
           transaction.show(targetFragment);
        }
        if(lastOneFragment != null){
            transaction.hide(lastOneFragment);
        }
        lastOneFragment = targetFragment;
        transaction.commit();
    }
}