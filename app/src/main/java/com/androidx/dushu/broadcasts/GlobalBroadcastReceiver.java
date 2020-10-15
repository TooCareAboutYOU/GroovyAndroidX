package com.androidx.dushu.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


/**
 * @author zhangshuai
 * 全局监听
 */
public class GlobalBroadcastReceiver extends BroadcastReceiver {

    public static final String NORMAL_ACTION="com.androidx.dushu.broadcasts";

    private String fromContext;

    public GlobalBroadcastReceiver(String msg) {
        this.fromContext = msg;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "from："+this.fromContext, Toast.LENGTH_SHORT).show();
    }
}
