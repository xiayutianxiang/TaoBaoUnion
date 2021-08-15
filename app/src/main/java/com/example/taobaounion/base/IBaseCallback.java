package com.example.taobaounion.base;

public interface IBaseCallback {

    //获取分类失败
    void onNetworkError();

    //分类加载中
    void onLoading();

    //获取分类为空
    void onEmpty();
}
