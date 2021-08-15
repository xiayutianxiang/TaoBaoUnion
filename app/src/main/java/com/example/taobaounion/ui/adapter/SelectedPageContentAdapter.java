package com.example.taobaounion.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.taobaounion.R;
import com.example.taobaounion.model.domain.IBaseInfo;
import com.example.taobaounion.model.domain.SelectedContent;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Url;

public class SelectedPageContentAdapter extends RecyclerView.Adapter<SelectedPageContentAdapter.InnerHolder> {

    private List<SelectedContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO> mData = new ArrayList<>();
    private OnSelectedPageItemClick mCurrentClicklistener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_page_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        SelectedContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO itemData = mData.get(position);
        holder.setData(itemData);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentClicklistener != null) {
                    mCurrentClicklistener.OnPageItemClick(itemData);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(SelectedContent content) {
        if(content.getCode() == 10000){
            List<SelectedContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO> map_data = content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
            this.mData.clear();
            this.mData.addAll(map_data);
            notifyDataSetChanged();
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder{
        //找到控件
        @BindView(R.id.selected_cover)
        public ImageView selectedCover;
        @BindView(R.id.selected_off_prise)
        public TextView offPrise;
        @BindView(R.id.selected_title)
        public TextView selectedTv;
        @BindView(R.id.selected_btn)
        public TextView selectedBuyBtn;
        @BindView(R.id.selected_original_prise)
        public TextView originalPrise;
        @BindView(R.id.selected_item_container)
        public LinearLayout itemContainer;
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(SelectedContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO itemData) {

            selectedTv.setText(itemData.getTitle());
            String pict_url = itemData.getPict_url();
            LogUtils.d(this,"pict_url --- > " + pict_url);
            if(TextUtils.isEmpty(pict_url)){
                itemContainer.setVisibility(View.GONE);
            }else {
                Glide.with(itemView).load(UrlUtils.getTicketUrl(pict_url)).into(selectedCover);
            }
           if(TextUtils.isEmpty(itemData.getCoupon_click_url())){
               originalPrise.setText("晚啦，没有优惠券了");
               selectedBuyBtn.setVisibility(View.GONE);
           }else {
               originalPrise.setText("原价" + itemData.getZk_final_price());
               selectedBuyBtn.setVisibility(View.VISIBLE);
           }

           if(TextUtils.isEmpty(itemData.getCoupon_info())){
               offPrise.setVisibility(View.GONE);
           }else {
               offPrise.setVisibility(View.VISIBLE);
               offPrise.setText(itemData.getCoupon_info());
           }
        }
    }

    public void setOnSelectedPageItemClick(OnSelectedPageItemClick listener){
        this.mCurrentClicklistener = listener;
    }

    public interface OnSelectedPageItemClick {
        void OnPageItemClick(IBaseInfo item);
    }
}
