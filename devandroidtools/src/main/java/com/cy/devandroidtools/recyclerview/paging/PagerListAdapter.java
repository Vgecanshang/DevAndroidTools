package com.cy.devandroidtools.recyclerview.paging;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.cy.devandroidtools.recyclerview.base.ViewHolder;


/**
 * 分页适配器
 *
 * @author cy
 * @date 2020/5/28.
 */
public class PagerListAdapter<T> extends PagedListAdapter< T , ViewHolder> {

    private ListPagedManager.PagerManagerListener<T> pagerManagerListener;

    public PagerListAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback , ListPagedManager.PagerManagerListener<T> pagerManagerListener) {
        super(diffCallback);
        this.pagerManagerListener = pagerManagerListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.createViewHolder(parent.getContext() , LayoutInflater.from(parent.getContext()).inflate(pagerManagerListener.getItemLayoutId(), null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(pagerManagerListener != null){
            pagerManagerListener.convert(holder , getItem(position) , position);
            holder.getConvertView().setOnClickListener( v ->
                    pagerManagerListener.onItemClick(v , position , getItem(position)));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    public ListPagedManager.PagerManagerListener getPagerManagerListener() {
        return pagerManagerListener;
    }

    public void setPagerManagerListener(ListPagedManager.PagerManagerListener pagerManagerListener) {
        this.pagerManagerListener = pagerManagerListener;
    }
}
