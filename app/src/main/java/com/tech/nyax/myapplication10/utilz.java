package com.tech.nyax.myapplication10;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Handler;
import android.os.Looper;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.content.SharedPreferences;
import android.widget.ProgressBar;
import android.text.Spannable;
import android.text.SpannableString;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class utilz {

    private static Context context; //application context
    private Handler mainThreadHandler;
    private static utilz singleInstance;
    private ProgressDialog simpleWaitDialog;
    private final static String TAG = utilz.class.getSimpleName();

    public utilz(Context context) {
        this.context = context;
    }

    public static utilz getInstance(Context context) {
        if (singleInstance == null)
            singleInstance = new utilz(context);
        return singleInstance;
    }

    public Handler getMainThreadHandler() {
        if (mainThreadHandler == null) {
            mainThreadHandler = new Handler(Looper.getMainLooper());
        }
        return mainThreadHandler;
    }

    public void globalloghandler(String message, String tag, int logtype) {
        LoggerSingleton.getInstance(context).logtologcat(message, logtype, tag);
    }

    public void globallogwithtoasthandler(String message, String tag, int logtype, int messagetype) {
        LoggerSingleton.getInstance(context).logtologcat(message, logtype, tag);
        LoggerSingleton.getInstance(context).logtotoast(message, messagetype);
    }

    public void globalloghandler(String message, String tag, int logtype, int messagetype) {
        LoggerSingleton.getInstance(context).logtologcat(message, logtype, tag);
        LoggerSingleton.getInstance(context).logtotoast(message, messagetype);
    }

    public void globalloghandlerwithnotification(String message, String tag, int logtype, int messagetype, String title, String ticker) {

        message = formatspannablestring(message).toString();
        title = formatspannablestring(title).toString();
        ticker = formatspannablestring(ticker).toString();
        tag = formatspannablestring(tag).toString();

        LoggerSingleton.getInstance(context).logtologcat(message, logtype, tag);
        LoggerSingleton.getInstance(context).logtotoast(message, messagetype);
        LoggerSingleton.getInstance(context).logtonotification(message, title, ticker, tag);
    }

    public void globalloghandler(String message, String tag, int logtype, int messagetype, String title, String ticker) {

        message = formatspannablestring(message).toString();
        title = formatspannablestring(title).toString();
        ticker = formatspannablestring(ticker).toString();
        tag = formatspannablestring(tag).toString();

        LoggerSingleton.getInstance(context).logtologcat(message, logtype, tag);
        LoggerSingleton.getInstance(context).logtotoast(message, messagetype);
        LoggerSingleton.getInstance(context).logtonotification(message, title, ticker, tag);
    }

    public void logtofile(Exception ex) {
        FileWriter writer = null;
        File file = null;

        try {
            String filename;
            final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
            String timestamp;

            timestamp = dateFormat.format(new Date());
            filename = context.getFilesDir() + "/logfiles/" + "errorlog_" + timestamp + ".txt";
            file = new File(filename);

            if (!file.exists()) {
                file.mkdir();
            }

            // Write the stacktrace to the file
            writer = new FileWriter(file, true);
            for (StackTraceElement element : ex.getStackTrace()) {
                writer.write(element.toString());
            }
        } catch (Exception _ex) {
            Log.e(TAG, "Exception in custom exception handler", _ex);
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (Exception _ex) {
                Log.e(TAG, "Exception in custom exception handler", _ex);
            }
        }

    }

    public String getcurrenttimestamp() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String _current_time_stamp = dateFormat.format(new Date());
        return _current_time_stamp;
    }

    public SpannableString formatspannablestring(String texttoformat) {
        SpannableString spannable_string = new SpannableString(texttoformat);
        spannable_string.setSpan(new ForegroundColorSpan(Color.GREEN), 0, texttoformat.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable_string;
    }

    public SpannableString format_spannable_error_string(String texttoformat) {
        SpannableString spannable_string = new SpannableString(texttoformat);
        spannable_string.setSpan(new ForegroundColorSpan(Color.RED), 0, texttoformat.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable_string;
    }

    public void showprogressdialog(String title, String message) {
        simpleWaitDialog = ProgressDialog.show(context,
                title,
                message);
    }

    public void dismissprogressdialog() {
        simpleWaitDialog.dismiss();
    }

    public void retrieveSharedPreferences(final String PREFS_FILE, final String KEY_STRING, final String DEFAULT_VALUE) {
        try {
            // PREFS_MODE defines which apps can access the file
            final int PREFS_MODE = Context.MODE_PRIVATE;

            SharedPreferences settings = context.getSharedPreferences(PREFS_FILE, PREFS_MODE);

            // read a string value ( with default)
            String str = settings.getString(KEY_STRING, DEFAULT_VALUE);

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    public void setSharedPreferences(final String PREFS_FILE, final String KEY_STRING, final String DEFAULT_VALUE) {
        try {
            // PREFS_MODE defines which apps can access the file
            final int PREFS_MODE = Context.MODE_PRIVATE;

            SharedPreferences settings = context.getSharedPreferences(PREFS_FILE, PREFS_MODE);

            SharedPreferences.Editor editor = settings.edit();

            // write string
            editor.putString(KEY_STRING, DEFAULT_VALUE);

            // This will asynchronously save the shared preferences without holding the current thread.
            editor.apply();

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    public String root_system_path() {
        ContextWrapper cw = new ContextWrapper(context);
        String _defaultdbpath = context.getFilesDir().getParent();
        return _defaultdbpath;
    }

    public String Photos_internal_storage() {
        ContextWrapper cw = new ContextWrapper(context);
		String _defaultdbpath = context.getFilesDir().getParent() + "/photos";
        File storageDir = new File(_defaultdbpath); 
        if (!storageDir.mkdirs()) {
            if (!storageDir.exists()) {
                globalloghandler("failed to create directory", TAG, 1, 0);
                return null;
            }
        }

        return _defaultdbpath;
    }


}




