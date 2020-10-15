package com.androidx.dushu;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MainActivityInstrumentedTest {

//    MainActivity mainActivity;

    @Before
    public void setUp() throws Exception {
//        mainActivity =new MainActivity();
        System.out.println("setUp()>>>>>>>>>>>>>>>>");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("tearDown()<<<<<<<<<<<<<<<<");
    }

    @Test
    public void onCreate() {
        System.out.println();
//        mainActivity.OnInputFinished("123456");
    }
}