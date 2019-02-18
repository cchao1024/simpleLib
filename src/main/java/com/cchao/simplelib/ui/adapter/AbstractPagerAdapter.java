package com.cchao.simplelib.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 抽象的 viewPager adapter，需复写抽象方法 convert
 * @param <T>
 */
public abstract class AbstractPagerAdapter<T> extends PagerAdapter {

    private final SparseArray<View> mViews;

    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mData;
    protected final int mItemLayoutId;

    public AbstractPagerAdapter(Context context, int layoutId, List<T> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mItemLayoutId = layoutId;
        this.mData = data;

        mViews = new SparseArray<View>(data.size());
    }

    @Override
    public int getCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    public T getItem(int position) {
        if (mData != null && mData.size() > position) {
            return mData.get(position);
        }
        return null;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViews.get(position);
        if (view == null) {
            view = View.inflate(mContext, mItemLayoutId, null);
            assert view != null;
            mViews.put(position, view);
        }
        convert(view, position, getItem(position));

        container.addView(view);
        return view;
    }

    public abstract void convert(View convertView, int position, T item);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

}
