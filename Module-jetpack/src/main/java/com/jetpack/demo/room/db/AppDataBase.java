package com.jetpack.demo.room.db;

import com.jetpack.demo.room.bean.User;
import com.jetpack.demo.room.dao.UserDao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * @author zhangshuai
 */
@Database(entities = {User.class},version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract UserDao getUserDao();
}
