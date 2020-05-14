package com.chenjim.andlibs.recyclerview;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chenjim.andlibs.customview.BaseCustomView;
import com.chenjim.andlibs.customview.BaseCustomViewModel;

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

    public abstract BaseCustomView getItemView(Context context);

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseCustomView itemView = getItemView(parent.getContext());
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemView.setLayoutParams(layoutParams);
        return new BaseViewHolder(itemView);
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
