package com.jetpack.demo.room;

import android.os.Bundle;
import android.view.View;

import com.jetpack.demo.JetpackApplication;
import com.jetpack.demo.R;
import com.jetpack.demo.room.bean.User;
import com.jetpack.demo.room.dao.UserDao;

import androidx.appcompat.app.AppCompatActivity;

public class RoomActivity extends AppCompatActivity {


    UserDao mUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        mUserDao= JetpackApplication.getAppDataBase().getUserDao();

        findViewById(R.id.acBtnInsert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                for (int i = 1, len = 11; i < len; i++) {
                    User user=new User();
                    user.uid=i;
                    user.firstName="Hello_"+i;
                    user.lastName="World_"+i;
                    mUserDao.insert(user);
                }
            }
        });

        findViewById(R.id.acBtnQueryAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

            }
        });

        findViewById(R.id.acBtnQueryAllByUIds).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

            }
        });

        findViewById(R.id.acBtnQueryAllByName).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

            }
        });

        findViewById(R.id.acBtnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

            }
        });
    }



}
