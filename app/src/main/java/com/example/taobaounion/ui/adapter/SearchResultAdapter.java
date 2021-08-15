//package com.example.taobaounion.ui.adapter;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.taobaounion.R;
//import com.example.taobaounion.model.domain.SearchResult;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.InnerHolder> {
//
//    private List<SearchResult.DataDTO.TbkDgMaterialOptionalResponseDTO.ResultListDTO.MapDataDTO> mData = new ArrayList<>();
//    @NonNull
//    @Override
//    public SearchResultAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_linear_goods_content, parent, false);
//        return new InnerHolder(item);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull SearchResultAdapter.InnerHolder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return mData.size();
//    }
//
//    public void setData(SearchResult result) {
//        List<SearchResult.DataDTO.TbkDgMaterialOptionalResponseDTO.ResultListDTO.MapDataDTO> map_data = result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
//        mData.clear();
//        mData.addAll(map_data);
//        notifyDataSetChanged();
//    }
//
//    public class InnerHolder extends RecyclerView.ViewHolder{
//        public InnerHolder(@NonNull View itemView) {
//            super(itemView);
//        }
//    }
//}
