package com.lcodecore.tkrefreshlayout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.utils.LogUtil;

public class TbNestedScrollView extends NestedScrollView {

    private int mHeaderHight ;
    private int originScroll ;
    private RecyclerView mRecyclerView;

    public TbNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public TbNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TbNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
//        LogUtils.d(this,"dx === >" + dx);
//        LogUtils.d(this,"dy === >" + dy);
        if(target instanceof RecyclerView){
            this.mRecyclerView = (RecyclerView) target;
        }
        if(originScroll < mHeaderHight){
            scrollBy(dx,dy);
            consumed[0] = dx;
            consumed[1] = dy;
        }
        super.onNestedPreScroll(target, dx, dy, consumed, type);
    }

    //设置滑出的距离
    public void setHeaderHeight(int headerHeight){
        this.mHeaderHight = headerHeight;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        this.originScroll = t;
//        LogUtils.d(this,"vertical ---  >" + t);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    //判断子类是否滑动到了底部
    public boolean isInBottom() {
        if (mRecyclerView != null) {
            boolean c = mRecyclerView.canScrollVertically(1);
            LogUtil.i("c ---- > " + c);
            return !c;
        }
        return false;
    }
}