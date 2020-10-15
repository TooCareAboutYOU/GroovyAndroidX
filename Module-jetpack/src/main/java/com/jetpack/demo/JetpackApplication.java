package com.jetpack.demo;

import android.app.Application;

import com.jetpack.demo.room.db.AppDataBase;

import androidx.room.Room;

/**
 * @author zhangshuai
 */
public class JetpackApplication extends Application { // implements Configuration.Provider {

    private static AppDataBase sAppDataBase;

    public static AppDataBase getAppDataBase(){
        return sAppDataBase;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initDataBase();
    }

    private void initDataBase(){
        sAppDataBase= Room.databaseBuilder(this,AppDataBase.class,"User.db").build();
    }

}
