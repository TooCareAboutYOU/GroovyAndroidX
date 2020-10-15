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
public class ParamsWorker extends Worker {

    private static final String TAG = WorkManagerActivity.TAG;

    public static final String KEY = "key_work";
    public static final String TAG_PARAMS = "params_work";

    public ParamsWorker(@NonNull final Context context,
                        @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "ParamsWorker.doWork(): ");
        Data outputData=null;
        for (String tag : getTags()) {
            if (tag.equals(TAG_PARAMS)) {
                Log.i(TAG, "标记 ParamsWorker："+tag);
                if (getInputData() == null) {
                    outputData=new Data.Builder().putString(KEY,"Failed ！！！, I am from ParamsWorker").build();
                    return Result.failure(outputData);
                }else{
                    String data=getInputData().getString(KEY);
                    outputData=new Data.Builder().putString(KEY,"Success, I am from ParamsWorker, "+data).build();
                    return Result.success(outputData);
                }
            }else {
                Log.w(TAG, "ParamsWorker 非标记目标对象："+tag);
            }
        }
        return Result.retry();
    }
}
