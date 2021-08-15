package com.example.taobaounion.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.taobaounion.model.domain.HomePagerContent;
import com.example.taobaounion.model.domain.IBaseInfo;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class LooperPagerAdapter extends PagerAdapter {

    private List<HomePagerContent.DataBean> mData = new ArrayList<>();
    private OnLooperPagerItemClickListener mLooperClickListener = null;

    public int getDataSize(){
        return mData.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //取模，得到真正的索引值
        int realPosition = position % mData.size();
        HomePagerContent.DataBean dataBean = mData.get(realPosition);
//        LogUtils.d(this,"measuredHeight --- >" + measuredHeight);
//        LogUtils.d(this,"measuredWidth --- >" + measuredWidth);
//        int ivSize = (measuredWidth > measuredHeight ? measuredHeight : measuredWidth) / 2;
        String coverUrl = UrlUtils.getCoverPath(dataBean.getPict_url());
        ImageView iv = new ImageView(container.getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(layoutParams);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLooperClickListener != null) {
                    HomePagerContent.DataBean item = mData.get(realPosition);
                    mLooperClickListener.onLooperItemClick(item);
                }
            }
        });
        Glide.with(container.getContext()).load(coverUrl).into(iv);
        container.addView(iv);
        return iv;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void setData(List<HomePagerContent.DataBean> contents) {
        mData.clear();
        mData.addAll(contents);
        notifyDataSetChanged();
    }

    public void setOnLooperPagerItemClickListener(OnLooperPagerItemClickListener listener){
        this.mLooperClickListener = listener;
    }

    public interface OnLooperPagerItemClickListener{
        void onLooperItemClick(IBaseInfo item);
    }
}
