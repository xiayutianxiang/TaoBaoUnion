package com.example.taobaounion.base;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mBind;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResId());
        //将整个软件置为灰色主题
       /* ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        View viewById = getWindow().getDecorView();
        viewById.setLayerType(View.LAYER_TYPE_SOFTWARE,paint);*/
        mBind = ButterKnife.bind(this);
        initView();
        initEvent();
        initPresenter();
    }

    protected abstract void initPresenter() ;

    /**
     * 需要的时候复写
     */
    protected void initEvent() {
    }

    protected abstract void initView();

    protected abstract int getLayoutResId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();
        }
        this.release();
    }

    protected void release() {
    }
}
