package com.develop.http.utils;

import android.util.Log;

import com.develop.http.BuildConfig;

/**
 * @author Angus
 */
public class LogUtils {
    public static final boolean LOG_ENABLE = BuildConfig.LOG_ENABLE;
    public static final String TAG = "httplog";

    /**
     * verbose log
     * @param msg log msg
     */
    public static void v(String msg) {
        if (LOG_ENABLE) {
            Log.v(TAG, buildMsg(msg));
        }
    }

    /**
     * verbose log
     * @param tag tag
     * @param msg log msg
     */
    public static void v(String tag, String msg) {
        if (LOG_ENABLE) {
            Log.v(TAG + "[" + tag + "]", buildMsg(msg));
        }
    }

    /**
     * debug log
     * @param msg log msg
     */
    public static void d(String msg) {
        if (LOG_ENABLE) {
            Log.d(TAG, buildMsg(msg));
        }
    }

    /**
     * debug log
     * @param tag tag
     * @param msg log msg
     */
    public static void d(String tag, String msg) {
        if (LOG_ENABLE) {
            Log.d(TAG + "[" + tag + "]", buildMsg(msg));
        }
    }

    /**
     * info log
     * @param msg log msg
     */
    public static void i(String msg) {
        if (LOG_ENABLE) {
            Log.i(TAG, buildMsg(msg));
        }
    }

    /**
     * info log
     * @param tag tag
     * @param msg log msg
     */
    public static void i(String tag, String msg) {
        if (LOG_ENABLE) {
            Log.i(TAG + "[" + tag + "]", buildMsg(msg));
        }
    }

    /**
     * warning log
     * @param msg log msg
     */
    public static void w(String msg) {
        if (LOG_ENABLE) {
            Log.w(TAG, buildMsg(msg));
        }
    }

    /**
     * warning log
     * @param msg log msg
     * @param e exception
     */
    public static void w(String msg, Exception e) {
        if (LOG_ENABLE) {
            Log.w(TAG, buildMsg(msg), e);
        }
    }

    /**
     * warning log
     * @param tag tag
     * @param msg log msg
     */
    public static void w(String tag, String msg) {
        if (LOG_ENABLE) {
            Log.w(TAG + "[" + tag + "]", buildMsg(msg));
        }
    }

    /**
     * warning log
     * @param tag tag
     * @param msg log msg
     * @param e exception
     */
    public static void w(String tag, String msg, Exception e) {
        if (LOG_ENABLE) {
            Log.w(TAG + "[" + tag + "]", buildMsg(msg), e);
        }
    }

    /**
     * error log
     * @param msg log msg
     */
    public static void e(String msg) {
        if (LOG_ENABLE) {
            Log.e(TAG, buildMsg(msg));
        }
    }

    /**
     * error log
     * @param msg log msg
     * @param e exception
     */
    public static void e(String msg, Exception e) {
        if (LOG_ENABLE) {
            Log.e(TAG, buildMsg(msg), e);
        }
    }

    /**
     * error log
     * @param tag tag
     * @param msg msg
     */
    public static void e(String tag, String msg) {
        if (LOG_ENABLE) {
            Log.e(TAG + "[" + tag + "]", buildMsg(msg));
        }
    }

    /**
     * error log
     * @param tag tag
     * @param msg log msg
     * @param e exception
     */
    public static void e(String tag, String msg, Exception e) {
        if (LOG_ENABLE) {
            Log.e(TAG + "[" + tag + "]", buildMsg(msg), e);
        }
    }

    public static String buildMsg(String msg) {
        return msg;
    }
}
