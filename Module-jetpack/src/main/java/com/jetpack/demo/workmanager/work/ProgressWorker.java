package com.jetpack.demo.workmanager.work;

import android.content.Context;
import android.util.Log;

import com.jetpack.demo.workmanager.WorkManagerActivity;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * @author zhangshuai
 */
public class ProgressWorker extends Worker {

    private static final String TAG = WorkManagerActivity.TAG;

    private static final String KEY = "key_work";
    public static final String TAG_PROGRESS = "params_work";

    public static final String PROGRESS = "progress_work";
    public static final String ERROR = "error_work";

    public ProgressWorker(@NonNull final Context context,
                          @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);
        setProgressAsync(setProgress(0));
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "ProgressWorker.doWork(): "+getInputData().getString(KEY));
        setProgressAsync(setProgress(100));
        return Result.success();
//        return Result.failure(setInfo("错误，执行异常!!!"));
    }

    private Data setProgress(int progress){
        return new Data.Builder().putInt(PROGRESS, progress).build();
    }

    private Data setInfo(String msg){
        return new Data.Builder().putString(ERROR, msg).build();
    }
}
