package com.example.taobaounion.ui.adapter;

import android.content.Context;
import android.graphics.Paint;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.taobaounion.R;
import com.example.taobaounion.model.domain.HomePagerContent;
import com.example.taobaounion.model.domain.IBaseInfo;
import com.example.taobaounion.model.domain.ILinearItemInfo;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.UrlUtils;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LinearItemContentAdapter extends RecyclerView.Adapter<LinearItemContentAdapter.InnerHolder>{

    private List<ILinearItemInfo> mData = new ArrayList<>();
    private View mItemView;

    private int count = 0;
    private OnlistItemClickListener mItemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LogUtils.d(this,"onCreateViewHolder --- >" + count); //测试onCreateViewHolder执行了多少次
        mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_linear_goods_content, parent, false);
        count++;
        return new InnerHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        //设置数据
        ILinearItemInfo dataBean = mData.get(position);
        holder.setData(dataBean);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(dataBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<? extends ILinearItemInfo> contents) {
        mData.clear();
        mData.addAll(contents);
        notifyDataSetChanged();
    }

    public void addData(List<? extends ILinearItemInfo> contents) {
        //添加前拿到原来的size
        int olderSize = mData.size();
        mData.addAll(contents);
        //olderSize表示在哪里开始添加，contents.size()表示添加多少条数据
        notifyItemRangeChanged(olderSize,contents.size());
    }

    public class InnerHolder extends RecyclerView.ViewHolder{
        //找到控件
        @BindView(R.id.goods_cover)
        public ImageView coverIv;

        @BindView(R.id.goods_title)
        public TextView title;

        @BindView(R.id.goods_off_prise)
        public TextView offPrise;

        @BindView(R.id.goods_after_off_prise)
        public TextView afterOffPrise;

        @BindView(R.id.goods_before_prise)
        public TextView beforePrise;

        @BindView(R.id.goods_sell_count)
        public TextView sellCount;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, mItemView);
        }

        public void setData(ILinearItemInfo data) {
            Context context = itemView.getContext();
            title.setText(data.getTitle());
//            //动态计算size去请求图片
//            ViewGroup.LayoutParams layoutParams = cover.getLayoutParams();
//            int width = layoutParams.width;
//            int height = layoutParams.height;
//            int coverSize = (width > height ? height : width)/2;
            //Glide加载图片

            String cover = data.getCover();
            LogUtils.d(LinearItemContentAdapter.this,"cover path --- > " + cover);
            if(!TextUtils.isEmpty(cover)){
                String coverPath = UrlUtils.getTicketUrl(data.getCover());
                Glide.with(context).load(coverPath).into(coverIv);
            }else {
                coverIv.setImageResource(R.mipmap.ic_launcher);
            }
            Long couponAmount = data.getCouponAmount();
            String finalPrice = data.getFinalPrise();
            float resultPrise = Float.parseFloat(finalPrice) - couponAmount;
            offPrise.setText(String.format(context.getString(R.string.text_goods_off_prise),couponAmount));
            afterOffPrise.setText(String.format("%.2f",resultPrise));
            beforePrise.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            beforePrise.setText(String.format(context.getString(R.string.text_goods_before_prise),finalPrice));
            sellCount.setText(String.format(context.getString(R.string.text_goods_sell_count),data.getVolume()));
        }
    }

    public void setOnlistItemClickListener(OnlistItemClickListener listener){
        this.mItemClickListener = listener;

    }

    public interface OnlistItemClickListener{
        void onItemClick(IBaseInfo item);
    }
}
