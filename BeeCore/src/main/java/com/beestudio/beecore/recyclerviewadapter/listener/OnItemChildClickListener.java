package com.beestudio.beecore.recyclerviewadapter.listener;

import android.view.View;

import com.beestudio.beecore.recyclerviewadapter.BaseQuickAdapter;

import androidx.annotation.NonNull;


/**
 * @author: limuyang
 * @date: 2019-12-03
 * @Description:
 */
public interface OnItemChildClickListener {
    /**
     * callback method to be invoked when an item child in this view has been click
     *
     * @param adapter  BaseQuickAdapter
     * @param view     The view whihin the ItemView that was clicked
     * @param position The position of the view int the adapter
     */
    void onItemChildClick(@NonNull BaseQuickAdapter<?,?> adapter, @NonNull View view, int position);
}
