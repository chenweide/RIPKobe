package com.cwd.ripkobe;

import android.util.Log;

/**
 * 日志打印
 * @author cwd
 */
public class LogUtils {

    private static final boolean DEBUG = true;
    private static final String DEFAULT_TAG = "cwd";

    public static void d(String tag,String msg){
        Log.d(tag,msg);
    }

    public static void d(String msg){
        d(DEFAULT_TAG,msg);
    }
}
