package com.cloudk.tooks;

import android.util.Log;

/**
 * Created by dong on 2018/1/29.
 * 功能:用于决定是否打印日志
 *
 */

public class MyLog  {
    // TODO: 如果MSG为空，Log就会出现println needs a message
    public static void d(String TAG,String MSG,boolean isOpen) {
        if(isOpen) {
            if(MSG == null)
                MSG = "";
            Log.d(TAG,MSG);
        }
    }

    public static void v(String TAG,String MSG,boolean isOpen) {
        if(isOpen) {
            if (MSG == null)
                MSG = "";
            Log.v(TAG, MSG);
        }
    }

    public static void i(String TAG,String MSG,boolean isOpen) {
        if (isOpen) {
            if (MSG == null)
                MSG = "";
            Log.i(TAG, MSG);
        }
    }

    public static void e(String TAG,String MSG,boolean isOpen) {
        if(isOpen) {
            if(MSG == null)
                MSG = "";
            Log.e(TAG,MSG);
        }
    }

    public static void w(String TAG,String MSG,boolean isOpen) {
        if(isOpen) {
            if(MSG == null)
                MSG = "";
            Log.d(TAG,MSG);
        }
    }

    public static void wtf(String TAG,String MSG,boolean isOpen) {
        if(isOpen) {
            if(MSG == null)
                MSG = "";
            Log.wtf(TAG,MSG);
        }
    }

}
