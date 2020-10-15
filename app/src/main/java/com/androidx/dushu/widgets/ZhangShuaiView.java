package com.androidx.dushu.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.androidx.dushu.R;
import com.androidx.dushu.databinding.LayoutDatabindingBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

/**
 * @author zhangshuai
 */
public class ZhangShuaiView extends ConstraintLayout {

    private Context mContext;
    private LayoutDatabindingBinding mBinding;

    public ZhangShuaiView(@NonNull Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public ZhangShuaiView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public ZhangShuaiView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        View rootView = inflate(this.mContext, R.layout.layout_databinding, this);
        rootView.setTag("layout/layout_databinding_0");
        mBinding = DataBindingUtil.bind(rootView);
    }

}
