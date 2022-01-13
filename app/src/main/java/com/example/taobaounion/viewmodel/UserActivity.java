package com.example.taobaounion.viewmodel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.taobaounion.R;

public class UserActivity extends AppCompatActivity {

    private static final String TAG = "UserActivity";
    private ProgressBar loading;
    private TextView tvUserName;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Log.d(TAG,"onCreate...");
        tvUserName = findViewById(R.id.textView);
        loading = findViewById(R.id.progressBar);
        button = findViewById(R.id.button);
        //获取viewModel实例
        ViewModelProvider modelProvider = new ViewModelProvider(this,new ViewModelProvider.AndroidViewModelFactory(getApplication()));
        UserViewModel userViewModel = modelProvider.get(UserViewModel.class);

        //观察用户信息
        userViewModel.getUserLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvUserName.setText(s);
            }
        });

        userViewModel.getLoadingLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                loading.setVisibility(aBoolean? View.VISIBLE:View.INVISIBLE);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userViewModel.getUserInfo();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop...");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }
}