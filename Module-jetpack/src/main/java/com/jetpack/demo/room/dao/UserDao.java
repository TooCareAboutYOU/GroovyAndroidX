package com.jetpack.demo.room.dao;

import com.jetpack.demo.room.bean.User;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * @author zhangshuai
 */
@Dao
public interface UserDao {

    @Query("select * from user")
    List<User> getUserList();

    @Query("select * from user where uid in (:uids)")
    List<User> findByIds(int[] uids);

    @Query("select * from user where first_name like :first and last_name like :last limit 1")
    User findByName(String first,String last);

    @Insert
    void insert(User... mUsers);

    @Delete
    void delete(User mUser);
}
