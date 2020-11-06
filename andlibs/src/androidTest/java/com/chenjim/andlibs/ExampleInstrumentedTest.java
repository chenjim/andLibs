package com.chenjim.andlibs;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.chenjim.andlibs.utils.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("ccom.chenjim.andlibs.demo", appContext.getPackageName());
    }

    @Test
    public void loggerDebug() {
        Logger.init(null, Log.VERBOSE);
        ArrayList<String> list = new ArrayList<>();
        list.add(SystemClock.elapsedRealtimeNanos() + "");
        list.add(SystemClock.elapsedRealtimeNanos() + "");
        Logger.d(list);

        String[] array = {SystemClock.elapsedRealtimeNanos() + "", SystemClock.elapsedRealtimeNanos() + ""};
        Logger.d(array);
    }
}
