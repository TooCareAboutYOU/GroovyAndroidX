package com.androidx.dushu;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.androidx.dushu.broadcasts.GlobalBroadcastReceiver;
import com.androidx.dushu.databinding.ActivityMainBinding;
import com.androidx.dushu.widgets.VerityCodeViewLayout;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MainActivity extends AppCompatActivity implements VerityCodeViewLayout.OnInputVerityCompleteListener {

    private ActivityMainBinding mainBinding;
    private GlobalBroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.inputVerity.setOnInputVerityCompleteListener(this);

        mReceiver=new GlobalBroadcastReceiver(MainActivity.class.getName());
        IntentFilter intentFilter=new IntentFilter(GlobalBroadcastReceiver.NORMAL_ACTION);
        registerReceiver(mReceiver,intentFilter,GlobalBroadcastReceiver.NORMAL_ACTION,null);

        mainBinding.acImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBroadcast(new Intent(GlobalBroadcastReceiver.NORMAL_ACTION),GlobalBroadcastReceiver.NORMAL_ACTION);
//                start();
            }
        });
    }


    @Override
    public void OnInputFinished(String verityCode) {

        boolean status = verityCode.equals("123456");

        Toast.makeText(this, status ? "验证成功！" : "验证失败！！！", Toast.LENGTH_SHORT).show();
        mainBinding.inputVerity.reset(!status);
        if (status) {
            mainBinding.inputVerity.setVisibility(View.GONE);
            mainBinding.acImg.setVisibility(View.VISIBLE);
            start();
        }

    }

    private void start() {
        loadRebound(0.5f);
    }

    /**
     * FaceBook-Rebound Spring动画
     */
    private void loadRebound(float num) {
        SpringSystem system = SpringSystem.create();
        Spring spring = system.createSpring();
        spring.setEndValue(1);
        spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(100, 1));
        spring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();
                float scale = 1f - (value * num);
                mainBinding.acImg.setScaleX(scale);
                mainBinding.acImg.setScaleY(scale);
            }
        });

    }


    public String printMsg(String verityCode) {
        System.out.println(verityCode.equals("123456") ? "验证成功！" : "验证失败！！！");
        return "123456";
    }


    @Override
    protected void onDestroy() {
        if (mainBinding.inputVerity != null) {
            mainBinding.inputVerity.exit();
        }
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

}
