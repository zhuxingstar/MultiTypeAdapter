package com.senierr.adapter.core;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 将指定类型的数据和视图生成为ViewHolder
 *
 * @author zhouchunjie
 * @date 2017/9/25
 */

public abstract class ViewHolderWrapper<T> {

    protected @Nullable MultiTypeAdapter multiTypeAdapter;
    private @NonNull Class<T> dataCls;
    private @LayoutRes int layoutId;

    private @Nullable OnItemClickListener onItemClickListener;
    private @Nullable SparseArray<OnItemChildClickListener> onItemChildClickListeners;

    public ViewHolderWrapper(@NonNull Class<T> dataCls, @LayoutRes int layoutId) {
        this.dataCls = dataCls;
        this.layoutId = layoutId;
    }

    public long getItemId(T item) {
        return RecyclerView.NO_ID;
    }

    public void onViewRecycled(RVHolder holder) {}

    public boolean onFailedToRecycleView(RVHolder holder) {
        return false;
    }

    public void onViewAttachedToWindow(RVHolder holder) {}

    public void onViewDetachedFromWindow(RVHolder holder) {}

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {}

    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {}

    /**
     * 指定占据列数
     *
     * @param item 待处理数据
     * @return
     */
    public int getSpanSize(T item) {
        return 1;
    }

    /**
     * 任务分配询问
     *
     * @param item 待处理数据
     * @return 是否接受处理此数据
     */
    public boolean onAcceptAssignment(T item) {
        return true;
    }

    public abstract void onBindViewHolder(@NonNull RVHolder holder, @NonNull T item);

    public void onBindViewHolder(@NonNull RVHolder holder, @NonNull T item, @NonNull List<Object> payloads) {
        onBindViewHolder(holder, item);
    }

    @NonNull
    public RVHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        final RVHolder holder = RVHolder.create(parent, layoutId);
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(holder, holder.getLayoutPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return onItemClickListener.onLongClick(holder, holder.getLayoutPosition());
                }
            });
        }
        if (onItemChildClickListeners != null) {
            for (int i = 0; i < onItemChildClickListeners.size(); i++) {
                int key = onItemChildClickListeners.keyAt(i);
                final OnItemChildClickListener onItemChildClickListener = onItemChildClickListeners.get(key);
                View childView = holder.getView(key);
                if (childView != null && onItemChildClickListener != null) {
                    childView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onItemChildClickListener.onClick(holder, view, holder.getLayoutPosition());
                        }
                    });
                    childView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            return onItemChildClickListener.onLongClick(holder, view, holder.getLayoutPosition());
                        }
                    });
                }
            }
        }
        return holder;
    }

    @Nullable
    public final MultiTypeAdapter getMultiTypeAdapter() {
        return multiTypeAdapter;
    }

    public final void setMultiTypeAdapter(@Nullable MultiTypeAdapter multiTypeAdapter) {
        this.multiTypeAdapter = multiTypeAdapter;
    }

    @NonNull
    public final Class<T> getDataCls() {
        return dataCls;
    }

    public final void setDataCls(@NonNull Class<T> dataCls) {
        this.dataCls = dataCls;
    }

    public final int getLayoutId() {
        return layoutId;
    }

    public final void setLayoutId(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
    }

    @Nullable
    public final OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public final void setOnItemClickListener(@Nullable OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Nullable
    public final OnItemChildClickListener getOnItemChildClickListener(@IdRes int childId) {
        if (onItemChildClickListeners != null) {
            return onItemChildClickListeners.get(childId);
        }
        return null;
    }

    public final void setOnItemChildClickListener(@IdRes int childId, @NonNull OnItemChildClickListener onItemChildClickListener) {
        if (onItemChildClickListeners == null) {
            onItemChildClickListeners = new SparseArray<>();
        }
        onItemChildClickListeners.put(childId, onItemChildClickListener);
    }

    /**
     * 列表项点击事件
     */
    public static abstract class OnItemClickListener {

        public void onClick(RVHolder viewHolder, int position) {}

        public boolean onLongClick(RVHolder viewHolder, int position) {
            return false;
        }
    }

    /**
     * 子控件点击事件
     */
    public static abstract class OnItemChildClickListener {

        public void onClick(RVHolder viewHolder, View view, int position) {}

        public boolean onLongClick(RVHolder viewHolder, View view, int position) {
            return false;
        }
    }
}
