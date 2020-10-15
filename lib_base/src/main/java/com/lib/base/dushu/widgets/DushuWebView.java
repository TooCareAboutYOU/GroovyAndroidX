package com.lib.base.dushu.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.ArrayList;
import java.util.List;
/**
 * @author zhangshuai
 */
public class DushuWebView extends WebView {

    private Context mContext;
    private WebSettings webSettings = null;
    private ActionMode mActionMode = null;
    private ActionSelectListener mActionSelectListener = null;
    private List<String> mActionList = new ArrayList<String>();

    public DushuWebView(Context context) {
        super(context);
        this.mContext = context;
    }

    public DushuWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setWebViewClient();
    }

    public DushuWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }


    private void setWebViewClient() {
        setScrollBarStyle(SCROLLBARS_INSIDE_OVERLAY);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus(View.FOCUS_DOWN);
        this.webSettings = getSettings();

        //闪烁，关闭硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        jsMethod();
        scaleMethod();
        contentMethod();
        fileCacheMethod();
        offlineLoadingMethod();
        otherSettings();
    }



    @Override
    public android.view.ActionMode startActionMode(android.view.ActionMode.Callback callback) {
        ActionMode actionMode = super.startActionMode(callback);
        return resolveActionMode(actionMode);
    }


    @Override
    public android.view.ActionMode startActionMode(android.view.ActionMode.Callback callback, int type) {
        ActionMode actionMode = super.startActionMode(callback, type);
        return resolveActionMode(actionMode);
    }

    /**
     * 监听点击处理
     */
    private ActionMode resolveActionMode(ActionMode actionMode) {
        if (actionMode != null) {
            final Menu menu = actionMode.getMenu();
            mActionMode = actionMode;
            menu.clear();
            for (int i = 0; i < mActionList.size(); i++) {
                menu.add(mActionList.get(i));
            }
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        getSelectedData((String) item.getTitle());
                        releaseAction();
                        return true;
                    }
                });
            }
        }
        mActionMode = actionMode;
        return actionMode;

    }


    private void releaseAction() {
        if (mActionMode != null) {
            mActionMode.finish();
            mActionMode = null;
        }
    }

    /**
     * 点击的时候 获取网页选中的文本，回调到原生中的js接口
     *
     * @param title 传入点击的item文本，一起通过js返回给原生接口
     */
    private void getSelectedData(String title) {
        String js = "(function getSelectedText() {" +
                "var txt;" +
                "var title = \"" + title + "\";" +
                "var anchorOffset;" +
                "var focusOffset;" +
                "var isCollapsed;"+
                "if (window.getSelection) {" +
                    "txt = window.getSelection().toString();" +
                    "anchorOffset = window.getSelection().anchorOffset.toString();"+
                    "focusOffset = window.getSelection().focusOffset.toString();"+
                    "isCollapsed = window.getSelection().isCollapsed;"+
                "} else if (window.document.getSelection) {" +
                    "txt = window.document.getSelection().toString();" +
                    "anchorOffset = window.document.getSelection().anchorOffset.toString();"+
                    "focusOffset = window.document.getSelection().focusOffset.toString();"+
                    "isCollapsed = window.document.getSelection().isCollapsed;"+
                "} else if (window.document.selection) {" +
                    "txt = window.document.selection.createRange().text;" +
                    "anchorOffset = window.document.selection.anchorOffset.toString();"+
                    "focusOffset = window.document.selection.focusOffset.toString();"+
                    "isCollapsed = window.document.selection.isCollapsed;"+
                "}" +
                "JSInterface.callback(txt,title,isCollapsed,anchorOffset,focusOffset);" +
                "})()";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript("javascript:" + js, null);
        } else {
            loadUrl("javascript:" + js);
        }
    }

    public void linkJsInterface() {
        addJavascriptInterface(new ActionSelectInterface(this), "JSInterface");
    }

    /**
     * 设置弹出的action列表
     *
     * @param mList
     */
    public void setActionList(List<String> mList) {
        mActionList = mList;
    }

    /**
     * 设置点击回调监听
     *
     * @param actionSelectListener
     */
    public void setActionSelectListener(ActionSelectListener actionSelectListener) {
        this.mActionSelectListener = actionSelectListener;
    }

    /**
     * 隐藏消失action
     */
    public void dismissAction() {
        releaseAction();
    }


    private class ActionSelectInterface {

        DushuWebView mContext;

        public ActionSelectInterface(DushuWebView mContext) {
            this.mContext = mContext;
        }

        @JavascriptInterface
        public void callback(final String value, final String title,boolean isCollapsed,String anchorOffset,String focusOffset) {
            if (mActionSelectListener != null) {
                mActionSelectListener.onClickEvent(value, title,isCollapsed,anchorOffset,focusOffset);
            }
        }
    }











    /**
     * JS处理
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void jsMethod() {
        //支持JS
        this.webSettings.setJavaScriptEnabled(true);
        //支持通过JS打开窗口
        this.webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    }


    /**
     * 缩放处理
     */
    private void scaleMethod() {
        //将图片调整到合适webview的大小
        this.webSettings.setUseWideViewPort(true);
        //缩放至屏幕的大小
        this.webSettings.setLoadWithOverviewMode(true);
        //支持缩放。与BuiltInZoomControls一起用
        this.webSettings.setBuiltInZoomControls(true);
        //因此原生的缩放控件
        this.webSettings.setDisplayZoomControls(false);
        //webSettings.setSupportZoom(false); //表示webview不可以缩放，设置什么都不缩放
    }

    /**
     * 内容布局
     */
    private void contentMethod() {
        //支持内容重新布局
        this.webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //多窗口
        this.webSettings.setSupportMultipleWindows(false);
    }


    /**
     * 文件缓存
     */
    private void fileCacheMethod() {
        //关闭webview中缓存
        this.webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //设置可以访问文件
        this.webSettings.setAllowFileAccess(true);
        //烤漆DOM storage API 功能
        this.webSettings.setDomStorageEnabled(true);
    }

    /**
     * 离线加载
     */
    private void offlineLoadingMethod() {
        //开启database storage API 功能
        this.webSettings.setDatabaseEnabled(true);
        //开启Application Caches功能
        this.webSettings.setAppCacheEnabled(true);
        //设置Application Caches缓存的目录
        this.webSettings.setAppCachePath("dushu");

    }


    /**
     * 其他设置
     */
    private void otherSettings() {
        //当webview调用requestFocus时，为webview设置节点
        this.webSettings.setNeedInitialFocus(true);
        //支持自动加载图片
        this.webSettings.setLoadsImagesAutomatically(true);
        //设置编码格式
        this.webSettings.setDefaultTextEncodingName("UTF-8");
        //设置默认字体大小
        this.webSettings.setDefaultFontSize(20);

        this.webSettings.setUseWideViewPort(true);

//        loadWebViewClient();
    }

//    private void loadWebViewClient() {
//        setWebViewClient(new WebViewClient() {
//
//            /**
//             *  拦截URL请求，重定向（有2个方法，一个是兼容5.0以下，一个是兼容5.0以上，保险起见两个都重写）。
//             * 无论返回true还是false，只要为WebView设置了WebViewClient，系统就不会再将url交给第三方的浏览器去处理了。
//             * 1、返回false，代表将url交给当前WebView加载，也就是正常的加载状态；
//             * 2、返回true，代表开发者已经对url进行了处理，WebView就不会再对这个url进行加载了。
//             *    另外，使用post的方式加载页面，此方法不会被调用。
//             */
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView webView, com.tencent.smtt.export.external.interfaces.WebResourceRequest webResourceRequest) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    webView.loadUrl(webResourceRequest.getUrl().toString());
//                }
//                return false;
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
//                return false;
//            }
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//            }
//
//            /**
//             * 页面加载时和页面加载完毕时调用。
//             */
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//            }
//
//            /**
//             * 重写此方法才能处理浏览器中的按键事件。
//             */
//            @Override
//            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
//                return super.shouldOverrideKeyEvent(view, event);
//            }
//
//
//            /**
//             * 页面每一次请求资源之前都会调用这个方法(非UI线程调用)。
//             */
//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView webView, com.tencent.smtt.export.external.interfaces.WebResourceRequest webResourceRequest) {
//                return super.shouldInterceptRequest(webView, webResourceRequest);
//            }
//
//            /**
//             * 页面加载资源时调用，每加载一个资源（比如图片）就调用一次。
//             */
//            @Override
//            public void onLoadResource(WebView view, String url) {
//                super.onLoadResource(view, url);
//            }
//
//            /**
//             * 加载页面的服务器出现错误（比如404）时回调。
//             */
//            @Override
//            public void onReceivedError(WebView webView, com.tencent.smtt.export.external.interfaces.WebResourceRequest webResourceRequest, com.tencent.smtt.export.external.interfaces.WebResourceError webResourceError) {
//                super.onReceivedError(webView, webResourceRequest, webResourceError);
//            }
//
//            /**
//             * 重写此方法可以让webview处理https请求。
//             */
//            @Override
//            public void onReceivedSslError(WebView webView, com.tencent.smtt.export.external.interfaces.SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
//                super.onReceivedSslError(webView, sslErrorHandler, sslError);
//            }
//
//            /**
//             * 更新历史记录。
//             */
//            @Override
//            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
//                super.doUpdateVisitedHistory(view, url, isReload);
//            }
//
//            /**
//             * 应用程序重新请求网页数据。
//             */
//            @Override
//            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
//                super.onFormResubmission(view, dontResend, resend);
//            }
//
//            /**
//             * 获取返回信息授权请求。
//             */
//            @Override
//            public void onReceivedHttpAuthRequest(WebView webView, com.tencent.smtt.export.external.interfaces.HttpAuthHandler httpAuthHandler, String s, String s1) {
//                super.onReceivedHttpAuthRequest(webView, httpAuthHandler, s, s1);
//            }
//
//            /**
//             * WebView发生缩放改变时调用。
//             */
//            @Override
//            public void onScaleChanged(WebView view, float oldScale, float newScale) {
//                super.onScaleChanged(view, oldScale, newScale);
//            }
//
//            /**
//             *  Key事件未被加载时调用。
//             */
//            @Override
//            public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
//                super.onUnhandledKeyEvent(view, event);
//            }
//        });
//    }
}
