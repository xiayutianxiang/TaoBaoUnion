package com.example.taobaounion.base;

public interface IBasePresenter<T>{

    void registerViewCallback(T callback);

    void unregisterViewCallback(T callback);
}
