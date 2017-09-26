package com.senierr.rvadapter.wrapper;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.TextView;

import com.senierr.adapterlib.ViewWrapper;
import com.senierr.adapterlib.util.RVHolder;
import com.senierr.rvadapter.R;
import com.senierr.rvadapter.bean.NormalBean;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */

public class NormalWrapper2 extends ViewWrapper<NormalBean> {

    @Override
    public RVHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return RVHolder.create(parent, R.layout.layout_normal_2);
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, @NonNull NormalBean item) {
        TextView textView = holder.getView(R.id.tv_text);
        textView.setText(item.getContent() + ", size: " + item.getId());
    }

    @Override
    public int getSpanSize(NormalBean item) {
        return item.getId();
    }
}
