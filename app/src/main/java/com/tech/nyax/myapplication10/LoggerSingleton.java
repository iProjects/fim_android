package com.tech.nyax.myapplication10;

import android.content.Context;
import android.util.Log;

public class LoggerSingleton {

    private static LoggerSingleton singleInstance;
    private final Context context;

    public LoggerSingleton(Context context) {
        this.context = context;
    }

    public static LoggerSingleton getInstance(Context context) {
        if (singleInstance == null)
            singleInstance = new LoggerSingleton(context);
        return singleInstance;
    }

    public void logtotoast(String message, Integer messagetype) {
        ToastGenerate.getInstance(context).createToastMessage(message, messagetype);
    }

    public void logtonotification(String message, String title, String ticker, String tag) {
        NotificationManagerActivity.getInstance(context).shownotificationmsg(message, title, ticker, tag);
    }

    public void logtologcat(String message, int type, String TAG) {
        switch (type) {
            case 0:
                Log.d(TAG, message);
                break;
            case 1:
                Log.e(TAG, message);
                break;
            case 2:
                Log.i(TAG, message);
                break;
            case 3:
                Log.v(TAG, message);
                break;
            case 4:
                Log.w(TAG, message);
                break;
            default:
                Log.v(TAG, message);
        }

    }

}

