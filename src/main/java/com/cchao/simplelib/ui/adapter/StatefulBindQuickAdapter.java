package com.cchao.simplelib.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.cchao.simplelib.LibCore;
import com.cchao.simplelib.core.CollectionHelper;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.view.state.StateSwitchable;
import com.cchao.simplelib.view.state.field.FieldStateLayout;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;
import com.kennyc.view.MultiStateView;

import java.util.List;

/**
 * 具备状态切换和分页加载的 adapter， 通过设置 StateSwitchable 来作为adapter的empty View
 * note: 分页信息 需要在请求成功后调用 solvePageData 传入
 *
 * @author cchao
 * @version 2019-07-03.
 */
public abstract class StatefulBindQuickAdapter<T> extends DataBindQuickAdapter<T> implements StateSwitchable {
    public StateSwitchable mStateView;
    public int mCurPage;

    public StatefulBindQuickAdapter(int layoutResId) {
        super(layoutResId);
    }

    public StatefulBindQuickAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    public DataBindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        initSwitchableView(parent.getContext());
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void bindToRecyclerView(RecyclerView recyclerView) {
        super.bindToRecyclerView(recyclerView);
        initLoadMore();
        initSwitchableView(recyclerView.getContext());
    }

    public void initLoadMore(){
        setLoadMoreView(new SimpleLoadMoreView());
        setOnLoadMoreListener(() -> {
            //
            loadPageData(mCurPage + 1);
        }, getRecyclerView());
    }

    public void initSwitchableView(Context context) {
        if (mStateView != null) {
            return;
        }
        mStateView = LibCore.getLibConfig().getFieldStateView(context);
        if (mStateView instanceof FieldStateLayout) {
            ((FieldStateLayout) mStateView).mFieldHeight = (int) (UiHelper.getScreenHeight() * 3.0 / 4);
        }

        setEmptyView((View) mStateView);
        setHeaderAndEmpty(true);
        mStateView.setReloadListener(click -> {
            loadPageData(1);
        });
    }

    public abstract void loadPageData(int page);

    /**
     * 处理分页数据
     */
    public void solveData(List<T> data, int curPage, int pageSize) {
        // 数据为空
        if (CollectionHelper.isEmpty(data)) {
            if (curPage == 1) {
                setViewState(MultiStateView.VIEW_STATE_EMPTY);
            }
            loadMoreEnd();
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
        if (data.size() < pageSize) {
            loadMoreEnd();
        } else {
            loadMoreComplete();
        }
    }

    /**
     * 处理分页数据
     * @see StatefulBindQuickAdapter#solveData
     */
    @Deprecated
    public void solvePageData(List<T> data, int curPage, int totalPage) {
        // 数据为空
        if (CollectionHelper.isEmpty(data)) {
            setViewState(MultiStateView.VIEW_STATE_EMPTY);
            if (curPage == 1) {
                loadMoreEnd();
            } else {
                loadMoreComplete();
            }
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
