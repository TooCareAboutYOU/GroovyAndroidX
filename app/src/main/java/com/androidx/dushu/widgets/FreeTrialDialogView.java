package com.androidx.dushu.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import com.androidx.dushu.R;
import com.androidx.dushu.databinding.LayoutFreeTrialViewBinding;

/**
 * @author zhangshuai
 * 视频详情页 免费试听提示弹框
 */
public class FreeTrialDialogView extends ConstraintLayout {

    private Context mContext;
    private LayoutFreeTrialViewBinding mBinding;

    public FreeTrialDialogView(Context context) {
        super(context);
        this.mContext=context;
        init();
    }

    public FreeTrialDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        init();
    }

    public FreeTrialDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        init();
    }

    /**
     * 初始化绑定view
     */
    private void init(){
        View view=inflate(this.mContext, R.layout.layout_free_trial_view,this);
        view.setTag("layout/layout_free_trial_view_0");
        mBinding= DataBindingUtil.bind(view);
    }

    @SuppressLint("ShowToast")
    public void setText(String txt){
        if (mBinding != null) {
            mBinding.acTvMsg.setText(txt);
        }
    }


    /**
     * 解绑
     */
    public void releaseView(){
        if (mBinding != null) {
            mBinding.unbind();
        }
    }
}
