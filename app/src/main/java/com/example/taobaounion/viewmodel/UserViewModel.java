package com.example.taobaounion.viewmodel;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 *  ViewModel如何结合LiveData使用
 *
 * 1.继承ViewModel自定义MyViewModel
 * 2.在MyViewModel中编写获取UI数据的逻辑
 * 3.使用LiveData将获取到的UI数据抛出
 * 4.在Activity/Fragment中使用ViewModelProvider获取MyViewModel实例
 * 5.观察MyViewModel中的LiveData数据，进行对应的UI更新。
 */

public class UserViewModel extends ViewModel {

    //创建两个livedata
    private MutableLiveData<String> userLiveData;
    private MutableLiveData<Boolean> loadingLiveData;

    public UserViewModel(){
        userLiveData = new MutableLiveData<>();
        loadingLiveData = new MutableLiveData<>();
    }

    //获取用户信息,假装网络请求 2s后 返回用户信息
    public void getUserInfo() {

        loadingLiveData.setValue(true);

        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }

    public LiveData<String> getUserLiveData() {
        return userLiveData;
    }
    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    private class MyAsyncTask extends AsyncTask<Void,Void,String>{
        @Override
        protected void onPostExecute(String s) {
            loadingLiveData.setValue(false);
            userLiveData.setValue(s);//抛出用户信息
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String userName = "学习ViewModel";
            return userName;
        }
    }
}
