package com.example.taobaounion.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.taobaounion.R;
import com.example.taobaounion.utils.LogUtils;

public class AutoLoopViewPager extends ViewPager {

    public static final long DEFALUT_DURATION = 3000;

    private long mDuration = DEFALUT_DURATION;

    public AutoLoopViewPager(@NonNull Context context) {
        this(context,null);
    }

    public AutoLoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoLooperStyle);
        //获取属性
        mDuration = typedArray.getInteger(R.styleable.AutoLooperStyle_duration, (int) DEFALUT_DURATION);
        //回收
        typedArray.recycle();
    }

    /**
     * 设置切换时长
     * @param duration
     */
    public void setDuration(long duration){
        mDuration = duration;
    }

    private boolean isLoop = false;

    ///开始
    public void startLoop(){
        isLoop = true;
        //先拿到当前位置
        post(task);
    }

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            int currentItem = getCurrentItem();
            currentItem++;
            setCurrentItem(currentItem);
            if (isLoop) {
                postDelayed(this, 2500);
            }
        }
    };

    //停止
    public void stopLoop(){
        isLoop = false;
        removeCallbacks(task);
    }
}
