package com.jetpack.demo.workmanager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jetpack.demo.R;
import com.jetpack.demo.workmanager.work.CustomRxWorker;
import com.jetpack.demo.workmanager.work.ParamsWorker;
import com.jetpack.demo.workmanager.work.ProgressWorker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

/**
 * @author zhangshuai
 * 官方教程地址：https://developer.android.google.cn/topic/libraries/architecture/workmanager/how-to/define-work
 * OneTimeWorkRequest: 一次性工作
 * PeriodicWorkRequest: 定期工作
 *
 */
public class WorkManagerActivity extends AppCompatActivity {

    public static final String TAG = "MainActivitys";

    private static final String KEY = "key_work";

    //设置工作约束
    private Constraints constraints;
    private Data inputData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_manager);

        init();

        findViewById(R.id.acBtnParamsWorker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadParamsWorker();
            }
        });

        findViewById(R.id.acBtnProgressWorker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                loadProgressWorker();
            }
        });

        findViewById(R.id.acBtnRxWorker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                rxWorker();
            }
        });
    }

    void init() {
        constraints = new Constraints.Builder()
                .setRequiresStorageNotLow(true) //不在存储容量不足时执行
                .setRequiresBatteryNotLow(true) //不在电量不足时执行
                .setRequiresDeviceIdle(false)    //在待机状态时执行
                .setRequiresCharging(true)      //不在充电时执行
                .build();

        //定义任务输入和输出
        inputData = new Data.Builder().putString(KEY, "我是MainActivity来的")
                .build();
    }

    private void loadParamsWorker(){
        OneTimeWorkRequest paramsRequest = new OneTimeWorkRequest.Builder(ParamsWorker.class)
                .addTag(ParamsWorker.TAG_PARAMS)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build();
//                .setInitialDelay(3, TimeUnit.SECONDS)  //设置延时3秒
//                .setBackoffCriteria(BackoffPolicy.LINEAR, OneTimeWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS) //设置重试和退避政策

        WorkManager.getInstance(this)
                .getWorkInfoByIdLiveData(paramsRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null) {
                            switch (workInfo.getState()) {
                                case SUCCEEDED: {
                                    String str = workInfo.getOutputData()
                                            .getString(KEY);
                                    Log.i(TAG, "返回数据: " + str);
                                    break;
                                }
                                default:
                                    break;
                            }

                        }
                    }
                });
        WorkManager.getInstance(this).enqueue(paramsRequest);
    }

    private void loadProgressWorker() {
        OneTimeWorkRequest progressRequest = new OneTimeWorkRequest.Builder(ProgressWorker.class)
                .addTag(ProgressWorker.TAG_PROGRESS)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(this)
                .getWorkInfoByIdLiveData(progressRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null) {
                            Log.i(TAG, "当前状态: " + workInfo.getState());

                            Data data=workInfo.getProgress();

                            if (workInfo.getState() == WorkInfo.State.FAILED) {
                                Log.i(TAG,"进度异常："+data.getString(ProgressWorker.ERROR));
                            }else {
                                Log.i(TAG, "返回进度: " + data.getInt(ProgressWorker.PROGRESS,-1));
                            }
                        }
                    }
                });

        WorkManager.getInstance(this).enqueue(progressRequest);

//        WorkManager.getInstance(this)
//                .beginWith(progressRequest)
//                .then(paramsRequest)
//                .enqueue();
    }

    private void rxWorker(){
        OneTimeWorkRequest progressRequest = new OneTimeWorkRequest.Builder(CustomRxWorker.class)
                .addTag(CustomRxWorker.WORK_TAG)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(this)
                .getWorkInfoByIdLiveData(progressRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null) {
                            Log.i(TAG, "当前状态: " + workInfo.getState()+", "+workInfo.toString());
                        }
                    }
                });

        WorkManager.getInstance(this).enqueue(progressRequest);
    }

}
