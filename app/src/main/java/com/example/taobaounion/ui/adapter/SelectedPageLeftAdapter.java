package com.example.taobaounion.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseApplication;
import com.example.taobaounion.model.domain.SelectedPageCategory;

import java.util.ArrayList;
import java.util.List;

public class SelectedPageLeftAdapter extends RecyclerView.Adapter<SelectedPageLeftAdapter.InnerHolder> {

    private List<SelectedPageCategory.DataDTO> mData = new ArrayList<>();
    private int mCurrentPosition = 0;
    private OnLeftItemClickListener mItemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(BaseApplication.getAppContent()).inflate(R.layout.item_selected_page_left, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        SelectedPageCategory.DataDTO dataDTO = mData.get(position);
        TextView itemTv = holder.itemView.findViewById(R.id.selected_page_left_tv);
        if(mCurrentPosition == position){
            itemTv.setBackgroundColor(itemTv.getResources().getColor(R.color.pageleft));
        }else {
            itemTv.setBackgroundColor(itemTv.getResources().getColor(R.color.white));
        }
        itemTv.setText(dataDTO.getFavorites_title());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null && mCurrentPosition != position) {
                    //修改当前选中的位置
                    mCurrentPosition = position;
                    mItemClickListener.onLeftItemClick(dataDTO);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(SelectedPageCategory categories) {
        List<SelectedPageCategory.DataDTO> data = categories.getData();
        if (data != null) {
            this.mData.clear();
            this.mData.addAll(data);
            notifyDataSetChanged();
        }
        if(mData.size()>0){
            mItemClickListener.onLeftItemClick(mData.get(mCurrentPosition));
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setOnLeftItemClickListener(OnLeftItemClickListener listener){

        this.mItemClickListener = listener;
    }

    public interface OnLeftItemClickListener{
        void onLeftItemClick(SelectedPageCategory.DataDTO item);
    }
}
