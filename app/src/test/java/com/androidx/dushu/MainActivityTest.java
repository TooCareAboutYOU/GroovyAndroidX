package com.androidx.dushu;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MainActivityTest {


    @BeforeClass
    public static void start() {
        System.out.println("start()>>>>>>>>>>>>>>>>");
    }

    @AfterClass
    public static void after() {
        System.out.println("after()<<<<<<<<<<<<<<<<");
    }

    @Before
    public void setUp() throws Exception {
        System.out.println("############### setUp() 每个test方法前都会调用");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("############### tearDown() 每个test方法后都会调用");
    }

    @Test
    public void onCreate() {
        System.out.println("onCreate()");
        MainActivity mainActivity = new MainActivity();
        String msg = mainActivity.printMsg("123456");
        String result = "123456";
        Assert.assertTrue(result == msg);
    }
}