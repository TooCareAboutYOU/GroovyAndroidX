package com.androidx.dushu.widgets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.androidx.dushu.R;
import com.jakewharton.rxbinding3.widget.RxTextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * 验证码|密码的输入框
 *
 * @author zhangshuai
 */
public class VerityCodeViewLayout extends ConstraintLayout implements View.OnKeyListener {

    private WeakReference<Context> mContext = null;
    private View mView = null;
    private AppCompatEditText acEt1, acEt2, acEt3, acEt4, acEt5, acEt6;
    private InputMethodManager inputMethodManager = null;
    private StringBuilder stringBuilder = null;
    private List<Disposable> mList;



    public VerityCodeViewLayout(Context context) {
        super(context);
        init(context);
    }

    public VerityCodeViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VerityCodeViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        this.mContext = new WeakReference<>(context);
        mList = new ArrayList<>();
        stringBuilder = new StringBuilder();
        this.mView = View.inflate(this.mContext.get(), R.layout.view_input_verity_vode, this);
        this.acEt1 = this.mView.findViewById(R.id.acEt_1);
        this.acEt2 = this.mView.findViewById(R.id.acEt_2);
        this.acEt3 = this.mView.findViewById(R.id.acEt_3);
        this.acEt4 = this.mView.findViewById(R.id.acEt_4);
        this.acEt5 = this.mView.findViewById(R.id.acEt_5);
        this.acEt6 = this.mView.findViewById(R.id.acEt_6);

        this.acEt1.setOnKeyListener(this);
        this.acEt2.setOnKeyListener(this);
        this.acEt3.setOnKeyListener(this);
        this.acEt4.setOnKeyListener(this);
        this.acEt5.setOnKeyListener(this);
        this.acEt6.setOnKeyListener(this);

        inputMethodManager = (InputMethodManager) acEt1.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        OnEditTextListener();
    }


    @SuppressLint("CheckResult")
    private void OnEditTextListener() {

        reset(true);
        addDisposable(RxTextView.textChanges(this.acEt1).subscribe(charSequence -> {
            if (charSequence.length() == 1) {
                setFocus(acEt1, acEt2, true);
            }
        }));

        addDisposable(RxTextView.textChanges(this.acEt2).subscribe(charSequence -> {
            if (charSequence.length() == 1) {
                setFocus(acEt2, acEt3, true);
            }
        }));

        addDisposable(RxTextView.textChanges(this.acEt3).subscribe(charSequence -> {
            if (charSequence.length() == 1) {
                setFocus(acEt3, acEt4, true);
            }
        }));

        addDisposable(RxTextView.textChanges(this.acEt4).subscribe(charSequence -> {
            if (charSequence.length() == 1) {
                setFocus(acEt4, acEt5, true);
            }
        }));

        addDisposable(RxTextView.textChanges(this.acEt5).subscribe(charSequence -> {
            if (charSequence.length() == 1) {
                setFocus(acEt5, acEt6, true);
            }
        }));

        addDisposable(RxTextView.textChanges(this.acEt6).subscribe(charSequence -> {
            if (charSequence.length() == 1) {
                acEt6.setEnabled(false);
                acEt6.setCursorVisible(false);
                clearStr();
                stringBuilder.append(acEt1.getText().toString())
                        .append(acEt2.getText().toString())
                        .append(acEt3.getText().toString())
                        .append(acEt4.getText().toString())
                        .append(acEt5.getText().toString())
                        .append(acEt6.getText().toString());
                if (mCompleteListener != null) {
                    mCompleteListener.OnInputFinished(stringBuilder.toString());
                }
            }
        }));

    }

    /**
     * 监听输入框在软键盘的内容删除事件
     *
     * @param v
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
            switch (v.getId()) {
                case R.id.acEt_1: {
                    break;
                }
                case R.id.acEt_2: {
                    setFocus(this.acEt1, this.acEt2, false);
                    break;
                }
                case R.id.acEt_3: {
                    setFocus(this.acEt2, this.acEt3, false);
                    break;
                }
                case R.id.acEt_4: {
                    setFocus(this.acEt3, this.acEt4, false);
                    break;
                }
                case R.id.acEt_5: {
                    setFocus(this.acEt4, this.acEt5, false);
                    break;
                }
                case R.id.acEt_6: {
                    setFocus(this.acEt5, this.acEt6, false);
                    break;
                }
                default:
                    break;
            }
        }
        return false;
    }


    /**
     * 根据填充内容设置前一个输入框与后一个输入框的可输入性
     *
     * @param v1
     * @param v2
     * @param isFocus
     */
    private void setFocus(AppCompatEditText v1, AppCompatEditText v2, @NonNull boolean isFocus) {
        if (isFocus) {
            //v1隐藏光标 失去焦点
            v1.setCursorVisible(false);

            //v2显示光标 获得焦点
            v2.setEnabled(true);
            v2.requestFocus();
            v2.setCursorVisible(true);

            v1.setEnabled(false);
        } else {
            v1.setEnabled(true);
            v1.setCursorVisible(true);
            v1.requestFocus();
            v1.setText(null);

            v2.setEnabled(false);
            v2.setCursorVisible(false);
        }
    }

    /**
     * 重置验证码
     */
    public void reset(boolean isShowKeyBoard) {
        acEt1.setEnabled(isShowKeyBoard);
        acEt1.setCursorVisible(isShowKeyBoard);
        acEt1.setFocusableInTouchMode(isShowKeyBoard);

        acEt1.setText(null);

        if (isShowKeyBoard) {
            acEt1.requestFocus();
            inputMethodManager.showSoftInput(acEt1, InputMethodManager.SHOW_IMPLICIT);
        } else {
            closeKeyBoard();
        }

        acEt2.setText(null);
        acEt2.setEnabled(false);
        acEt2.setCursorVisible(false);


        acEt3.setText(null);
        acEt3.setEnabled(false);
        acEt3.setCursorVisible(false);

        acEt4.setText(null);
        acEt4.setEnabled(false);
        acEt4.setCursorVisible(false);

        acEt5.setText(null);
        acEt5.setEnabled(false);
        acEt5.setCursorVisible(false);

        acEt6.setText(null);
        acEt6.setEnabled(false);
        acEt6.setCursorVisible(false);
    }




    /**
     * 保存Rx系列临时生存的Disposable
     *
     * @param disposable
     */
    private void addDisposable(@NonNull Disposable disposable) {
        if (mList != null) {
            mList.add(disposable);
        }
    }

    /**
     * 清空创建的Disposable
     */
    private void clearDisposable() {
        if (this.mList.size() > 0) {
            for (Disposable disposable : this.mList) {
                if (disposable.isDisposed()) {
                    disposable.dispose();
                }
            }
            this.mList.clear();
        }
        mList = null;
    }

    /**
     * 清除临时缓存的输入性验证码
     */
    private void clearStr() {
        if (stringBuilder != null && stringBuilder.length() > 0) {
            stringBuilder.delete(0, stringBuilder.length());
        }
    }

    /**
     * 主动关闭软键盘，解决键盘的存在影响了内容页高度设置的问题
     */
    public void closeKeyBoard() {
        //如果window上view获取焦点 && view不为空
        if (inputMethodManager.isActive() && ((Activity) this.mContext.get()).getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (((Activity) this.mContext.get()).getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                inputMethodManager.hideSoftInputFromWindow(((Activity) this.mContext.get()).getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    /**
     * 退出
     */
    public void exit() {
        this.acEt1.setText(null);
        this.acEt2.setText(null);
        this.acEt3.setText(null);
        this.acEt4.setText(null);
        this.acEt5.setText(null);
        this.acEt6.setText(null);
        clearDisposable();
        clearStr();
        stringBuilder = null;
        inputMethodManager = null;
        this.mContext.clear();
        this.mContext = null;

    }


    public interface OnInputVerityCompleteListener {
        void OnInputFinished(String verityCode);
    }

    private OnInputVerityCompleteListener mCompleteListener;

    public void setOnInputVerityCompleteListener(OnInputVerityCompleteListener listener) {
        this.mCompleteListener = listener;
    }

}
