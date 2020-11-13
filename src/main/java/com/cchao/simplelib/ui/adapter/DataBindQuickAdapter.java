package com.cchao.simplelib.ui.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.cchao.simplelib.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author cchao
 * @version 8/11/18.
 */
public abstract class DataBindQuickAdapter<T> extends BaseQuickAdapter<T, DataBindQuickAdapter.DataBindViewHolder> {

    public DataBindQuickAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    public DataBindQuickAdapter(int layoutResId) {
        super(layoutResId, null);
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
        if (binding == null) {
            return super.getItemView(layoutResId, parent);
        }
        View view = binding.getRoot();
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding);
        return view;
    }

    public static class DataBindViewHolder extends BaseViewHolder {

        public DataBindViewHolder(View view) {
            super(view);
        }

        public ViewDataBinding getBinding() {
            return (ViewDataBinding) itemView.getTag(R.id.BaseQuickAdapter_databinding_support);
        }
    }
}
