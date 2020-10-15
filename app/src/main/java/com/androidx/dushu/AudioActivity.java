package com.androidx.dushu;

import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.androidx.dushu.broadcasts.VolumeChangeObserver;
import com.androidx.dushu.widgets.FreeTrialDialogView;

public class AudioActivity extends AppCompatActivity implements VolumeChangeObserver.VolumeChangeListener {

    private static final String TAG = "AudioActivity";

    private VolumeChangeObserver volumeChangeObserver;
    private AppCompatButton mShuaiView;
    private FreeTrialDialogView mFreeTrialDialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_audio);
        mShuaiView=findViewById(R.id.toast_view);
        mFreeTrialDialogView=findViewById(R.id.free_view);

        mShuaiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFreeTrialDialogView.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(AudioActivity.this, R.anim.anim_after);
                mFreeTrialDialogView.startAnimation(animation);
            }
        });

        //实例化对象并设置监听器
        volumeChangeObserver = new VolumeChangeObserver(this);
        volumeChangeObserver.setVolumeChangeListener(this);
        int initVolume = volumeChangeObserver.getCurrentMusicVolume();
        Log.i(TAG, "初始化监听器: " + initVolume);
    }

    @Override
    public void onVolumeChanged(int volume) {
        Log.i(TAG, "当前音量: " + volume);
    }

    @Override
    protected void onResume() {
        volumeChangeObserver.registerReceiver();
        super.onResume();
    }


    @Override
    protected void onPause() {
        volumeChangeObserver.unregisterReceiver();
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            Log.i(TAG, "当前点击音量键 ");
        }
        return super.onKeyDown(keyCode, event);
    }
}
