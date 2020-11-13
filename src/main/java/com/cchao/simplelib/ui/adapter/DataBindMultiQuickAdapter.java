package com.cchao.simplelib.ui.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.cchao.simplelib.R;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;
import java.util.Map;

/**
 * 多样式的 dataBinding的 BaseMultiItemQuickAdapter
 *
 * @author cchao
 * @version 8/11/18.
 */
public abstract class DataBindMultiQuickAdapter<T extends MultiItemEntity> extends BaseMultiItemQuickAdapter<T, DataBindMultiQuickAdapter.DataBindViewHolder> {

    public DataBindMultiQuickAdapter(List<T> data) {
        super(data);
        for (Map.Entry<Integer, Integer> entry : getTypeLayoutMap().entrySet()) {
            addItemType(entry.getKey(), entry.getValue());
        }
    }

    public abstract Map<Integer, Integer> getTypeLayoutMap();

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
