package com.cchao.simplelib.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.cchao.simplelib.LibCore;
import com.cchao.simplelib.core.CollectionHelper;
import com.cchao.simplelib.view.state.StateSwitchable;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;
import com.kennyc.view.MultiStateView;

import java.util.List;

/**
 * 具备状态切换和分页加载的 adapter， 通过设置 StateSwitchable 来作为adapter的empty View
 *
 * @author cchao
 * @version 2019-07-03.
 */
public abstract class StatefulBindQuickAdapter<T> extends DataBindQuickAdapter<T> implements StateSwitchable {
    public StateSwitchable mStateView;
    int mCurPage;

    public StatefulBindQuickAdapter(int layoutResId) {
        super(layoutResId);
    }

    public StatefulBindQuickAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    public DataBindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        initSwitchableView(parent.getContext());
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void bindToRecyclerView(RecyclerView recyclerView) {
        super.bindToRecyclerView(recyclerView);
        initSwitchableView(recyclerView.getContext());
    }

    void initSwitchableView(Context context) {
        if (mStateView != null) {
            return;
        }
        mStateView = LibCore.getLibConfig().getFieldStateView(context);

        setLoadMoreView(new SimpleLoadMoreView());
        setEmptyView((View) mStateView);
        setHeaderAndEmpty(true);
        mStateView.setReloadListener(click -> {
            loadPageData(1);
        });
        setOnLoadMoreListener(new RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadPageData(mCurPage + 1);
            }
        }, getRecyclerView());

    }

    public abstract void loadPageData(int page);

    public void solveData(List<T> data) {
        setNewData(data);
    }

    /**
     * 处理分页数据
     */
    public void solvePageData(List<T> data, int curPage, int totalPage) {
        // 数据为空
        if (CollectionHelper.isEmpty(data)) {
            setViewState(MultiStateView.VIEW_STATE_EMPTY);
            loadMoreComplete();
            return;
        }

        // 填充数据
        if (curPage == 1) {
            setNewData(data);
        } else {
            addData(data);
        }

        // 处理页面信息
        mCurPage = curPage;
        if (curPage >= totalPage) {
            loadMoreEnd();
        } else {
            loadMoreComplete();
        }
    }

    @Override
    public void setViewState(int state) {
        mStateView.setViewState(state);
    }

    @Override
    public void setReloadListener(View.OnClickListener onClickListener) {
        mStateView.setReloadListener(onClickListener);
    }
}
