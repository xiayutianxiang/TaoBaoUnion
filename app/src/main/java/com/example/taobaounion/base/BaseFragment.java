package com.example.taobaounion.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taobaounion.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    private State currentState = State.NONE;
    private View mLoadSuccessView;
    private View mLoadLoadingView;
    private View mLoadErrorView;
    private View mLoadEmptyView;

    public enum State{
        NONE,LOADING,ERROR,SUCCESS,EMPTY
    }

    private Unbinder mBind;
    private FrameLayout mBaseContainer;

    @OnClick(R.id.net_work_error)
    public void retry(){
        onRetryClick();
    }

    protected void onRetryClick() {
    }

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = loadRootView(inflater,container);
        mBaseContainer = rootView.findViewById(R.id.base_container);
        loadStatesView(inflater,container);

        mBind = ButterKnife.bind(this, rootView);
        initView(rootView);
        initListener();
        initPresenter();
        loadData();
        return rootView;
    }

    /**
     * 子类若需要就复写
     */
    protected void initListener() {
    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_fragment_layout, container, false);
    }

    /**
     * 加载各种状态的View
     * @param inflater
     * @param container
     */
    private void loadStatesView(LayoutInflater inflater, ViewGroup container) {
        //success  view
        mLoadSuccessView = loadSuccessView(inflater, container);
        mBaseContainer.addView(mLoadSuccessView);

        //loading view
        mLoadLoadingView = loadLoadingView(inflater, container);
        mBaseContainer.addView(mLoadLoadingView);

        //empty view
        mLoadEmptyView = loadEmptyView(inflater, container);
        mBaseContainer.addView(mLoadEmptyView);

        //error view
        mLoadErrorView = loadErrorView(inflater, container);
        mBaseContainer.addView(mLoadErrorView);
        setUpState(State.NONE);
    }

    protected View loadEmptyView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_empty,container,false);
    }

    protected View loadErrorView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_error,container,false);
    }

    protected View loadLoadingView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_loading,container,false);
    }

    /**
     * 子类通过这个方法切换状态页面
     * @param state
     */
    public void setUpState(State state){
        this.currentState = state;
        mLoadSuccessView.setVisibility(currentState == State.SUCCESS ? View.VISIBLE : View.GONE);
        mLoadLoadingView.setVisibility(currentState == State.LOADING ? View.VISIBLE : View.GONE);
        mLoadEmptyView.setVisibility(currentState == State.EMPTY ? View.VISIBLE : View.GONE);
        mLoadErrorView.setVisibility(currentState == State.ERROR ? View.VISIBLE : View.GONE);
    }

    protected void initView(View rootView) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBind != null) {
            mBind.unbind();
        }
        release();
    }

    protected void release() {
        //释放资源
    }

    protected void initPresenter() {
        //创建Presenter
    }

    protected void loadData() {
    }

    protected  View loadSuccessView(LayoutInflater inflater, ViewGroup container){
        int resId = getRootViewId();
        return inflater.inflate(resId,container,false);
    }

    protected abstract int getRootViewId();
}
