package com.example.taobaounion.ui.adapter;

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
import com.example.taobaounion.model.domain.IBaseInfo;
import com.example.taobaounion.model.domain.OnSellContent;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnSellContentPageAdapter extends RecyclerView.Adapter<OnSellContentPageAdapter.InnerHolder> {

    private List<OnSellContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO> mData = new ArrayList<>();
    private OnSellItemClickListener mClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_on_sell_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        //TODO:绑定数据
        OnSellContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO mapDataDTO = mData.get(position);
        holder.setData(mapDataDTO);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemClickListener(mapDataDTO);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(OnSellContent result) {
        mData.clear();
        mData.addAll(result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data());
        notifyDataSetChanged();
    }

    /**
     * 加载更多内容
     * @param moreResult
     */
    public void onMoreLoaded(OnSellContent moreResult) {
        List<OnSellContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO> moreData = moreResult.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
        mData.addAll(moreData);
        notifyItemChanged(mData.size() - 1,moreData.size());
    }

    public class InnerHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.on_sell_cover)
        public ImageView sellCover;
        @BindView(R.id.on_sell_content_title_tv)
        public TextView sellContentTv;
        @BindView(R.id.on_sell_off_prise_tv)
        public TextView sellOffPriseTv;
        @BindView(R.id.on_sell_origin_prise_tv)
        public TextView sellOriginPriseTv;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(OnSellContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO data){
            String click_url = data.getPict_url();
            LogUtils.d(OnSellContentPageAdapter.this,"click_url --->" + click_url);
            String coverPath = UrlUtils.getCoverPath(click_url);
            Glide.with(sellCover.getContext()).load(coverPath).into(sellCover);

            String title = data.getTitle();
            sellContentTv.setText(title);
            String originPrise = data.getZk_final_price();
            sellOriginPriseTv.setText("￥" + originPrise);
            sellOriginPriseTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            int couponAmount = data.getCoupon_amount();
            float originPriseFloat = Float.parseFloat(originPrise);
            float finalPrise = originPriseFloat - couponAmount;
            sellOffPriseTv.setText("券后价：" + String.format("%.2f",finalPrise)+"元");
        }
    }

    public void setOnSellItemClickListener(OnSellItemClickListener listener){
        this.mClickListener = listener;
    }

    public interface OnSellItemClickListener{
        void onItemClickListener(IBaseInfo data);
    }
}
