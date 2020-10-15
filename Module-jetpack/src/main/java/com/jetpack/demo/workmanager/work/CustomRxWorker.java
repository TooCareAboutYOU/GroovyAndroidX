package com.jetpack.demo.workmanager.work;

import android.content.Context;
import android.util.Log;

import com.jetpack.demo.workmanager.WorkManagerActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.RxWorker;
import androidx.work.WorkerParameters;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;

/**
 * @author zhangshuai
 * RxWorker处理线程
 */
public class CustomRxWorker extends RxWorker {

    private static final String TAG = WorkManagerActivity.TAG;

    public static final String WORK_TAG=CustomRxWorker.class.getSimpleName();

    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public CustomRxWorker(@NonNull final Context appContext,
                          @NonNull final WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public Single<Result> createWork() {
        Log.i(TAG, "CustomRxWorker.createWork: "+getInputData().getString("key_work"));
        return Observable.range(1,5)
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(final Integer mInteger) throws Exception {
                        return Observable.just(String.valueOf(mInteger));
                    }
                })
                .toList()
                .map(new Function<List<String>, Result>() {
                    @Override
                    public Result apply(final List<String> mStrings) throws Exception {
                        String[] strings=new String[mStrings.size()];
                        mStrings.toArray(strings);
                        return Result.success(new Data.Builder().putStringArray("STRING",strings).build());
                    }
                });
    }
}
