package com.androidx.dushu;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidx.dushu.databinding.ActivityWebBinding;
import com.lib.base.dushu.widgets.ActionSelectListener;
import com.lib.base.dushu.widgets.DushuWebView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangshuai
 */
public class WebActivity extends AppCompatActivity {
    ActivityWebBinding mBinding = null;
    WebSettings webSettings = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_web);

        mBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.webView.clearFormData();
                mBinding.webView.clearHistory();
                mBinding.webView.clearFocus();
                mBinding.webView.reload();
            }
        });

        mBinding.webView.setWebViewClient(new WebViewClient() {
            private boolean mLastLoadFailed = false;

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                mBinding.swipeRefreshLayout.setRefreshing(false);

                if (!mLastLoadFailed) {
                    DushuWebView dushuWebView = (DushuWebView) webView;
                    dushuWebView.linkJsInterface();
                }
            }

            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                mLastLoadFailed = true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.getUrl().toString());
                }
                return true;
            }
        });

        List<String> mActionList = new ArrayList<String>() {
            {
                add("复制");
                add("取消划线");
                add("分享");
            }
        };

        mBinding.webView.setActionList(mActionList);
        mBinding.webView.linkJsInterface();

        mBinding.webView.setActionSelectListener(new ActionSelectListener() {
            @Override
            public void onClickEvent(String value, String title, boolean isCollapsed, String anchorOffset, String focusOffset) {
                String msg = title + "\n"
                        + value + "\n"
                        + isCollapsed + "\n"
                        + anchorOffset + "\n"
                        + focusOffset;
                Log.i("onClickEvent", msg);
                Toast.makeText(WebActivity.this, msg, Toast.LENGTH_SHORT).show();

            }
        });
        //https://mp.weixin.qq.com/s/NNn2Kxyh2NW7uatmDcA0Jw
        mBinding.webView.loadUrl("file:///android_asset/long_text_2020-04-08-17-02-53.html");
    }


    /**
     * 激活WebView为活跃状态，能正常执行网页的响应
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mBinding.webView != null) {
            if (this.webSettings != null) {
                this.webSettings.setJavaScriptEnabled(true);
            }

            //当页面被失去焦点被切换到后台不可见状态，需要执行onPause
            //通过onPause动作通知内核暂停所有的动作，比如DOM的解析、plugin的执行、JavaScript执行。
            mBinding.webView.onResume();
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mBinding.webView != null) {
            //恢复pauseTimers状态
            mBinding.webView.resumeTimers();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBinding.webView != null) {
            //当页面被失去焦点被切换到后台不可见状态，需要执行onPause
            //通过onPause动作通知内核暂停所有的动作，比如DOM的解析、plugin的执行、JavaScript执行。
            mBinding.webView.onPause();

            //当应用程序(存在webview)被切换到后台时，这个方法不仅仅针对当前的webview而是全局的全应用程序的webview
            //它会暂停所有webview的layout，parsing，javascripttimer。降低CPU功耗。
            mBinding.webView.pauseTimers();

            mBinding.webView.dismissAction();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBinding.webView != null) {
            if (this.webSettings != null) {
                this.webSettings.setJavaScriptEnabled(false);
            }
            mBinding.webView.stopLoading();
        }
    }


    @Override
    protected void onDestroy() {
        if (mBinding.webView != null) {
            ((ViewGroup) mBinding.webView.getParent()).removeView(mBinding.webView);
            mBinding.webView.clearHistory();
            mBinding.webView.clearFormData();
            mBinding.webView.destroy();
        }
        super.onDestroy();

    }
}
