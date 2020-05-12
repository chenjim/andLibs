package com.chenjim.andlibs.recyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chenjim.andlibs.customview.BaseCustomViewModel;
import com.chenjim.andlibs.recyclerview.BaseViewHolder;

import java.util.ArrayList;

/**
 * @description：
 * @fileName: BaseListAdapter
 * @author: jim.chen
 * @date: 2020/4/29
 */
public abstract class BaseListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private ArrayList<BaseCustomViewModel> mItems;

    public void setData(ArrayList<BaseCustomViewModel> mItems) {
        this.mItems = mItems;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        if (mItems != null && mItems.size() > 0) {
            return mItems.size();
        }
        return 0;
    }
}
