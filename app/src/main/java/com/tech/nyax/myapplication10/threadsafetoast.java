package com.tech.nyax.myapplication10;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

public class threadsafetoast extends Application {

    private static Context context; //application context
    private Handler mainThreadHandler;
    private Toast toast;

    public Handler getMainThreadHandler() {
        if (mainThreadHandler == null) {
            mainThreadHandler = new Handler(Looper.getMainLooper());
        }
        return mainThreadHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static threadsafetoast getApp() {
        return (threadsafetoast) context;
    }

    /**
     * Thread safe way of displaying toast.
     *
     * @param message
     */
    public void showToast(final String message) {
        getMainThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(message)) {
                    if (toast != null) {
                        toast.cancel(); //dismiss current toast if visible
                        toast.setText(message);
                    } else {
                        toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                    }
                    toast.show();
                }
            }
        });
    }
}