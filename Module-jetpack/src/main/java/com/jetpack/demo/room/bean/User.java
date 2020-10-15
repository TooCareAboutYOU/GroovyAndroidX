package com.jetpack.demo.room.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author zhangshuai
 */
@Entity
public class User {

    @PrimaryKey
    public int uid;

    @ColumnInfo(name="first_name")
    public String firstName;

    @ColumnInfo(name="last_name")
    public String lastName;

}
