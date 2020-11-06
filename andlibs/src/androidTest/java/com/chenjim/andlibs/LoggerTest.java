package com.chenjim.andlibs;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.chenjim.andlibs.utils.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author chenjim  me@h89.cn
 * @version 1.0
 * @description {@link Logger}相关测试
 * @date 2020/12/11
 */
@RunWith(AndroidJUnit4.class)
public class LoggerTest {

    @Test
    public void testLoggerWrite() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Logger.init(appContext, Log.VERBOSE);

        long time0 = SystemClock.elapsedRealtime();
        int index = 0;
        do {
            Logger.d("index=" + index++);
        } while (index < 1000);
        System.out.println("ExampleInstrumentedTest:cost time Logger:" + (SystemClock.elapsedRealtime() - time0));

        String log = Logger.getLastLogger(700);
        System.out.println("ExampleInstrumentedTest:read:" + log);


        System.out.println("ExampleInstrumentedTest:read:" + Logger.getStackTrace());
    }

    @Test
    public void testLogger() {
        long time0 = SystemClock.elapsedRealtime();
        int index = 0;
        do {
            Logger.d("index=" + index++);
        } while (index < 1000);
        System.out.println("ExampleInstrumentedTest:cost time Logger not write:" + (SystemClock.elapsedRealtime() - time0));
    }


    @Test
    public void testLog() {
        long time0 = SystemClock.elapsedRealtime();
        int index = 0;
        do {
            Log.d("testLog", "index=" + index++);
        } while (index < 1000);
        System.out.println("ExampleInstrumentedTest:cost time log:" + (SystemClock.elapsedRealtime() - time0));
    }

}
