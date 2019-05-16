package com.tech.nyax.myapplication10;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ToastGenerate {

    private static ToastGenerate singleInstance;
    private final Context context;

    public ToastGenerate(Context context) {
        this.context = context;
    }

    public static ToastGenerate getInstance(Context context) {
        if (singleInstance == null)
            singleInstance = new ToastGenerate(context);
        return singleInstance;
    }

    private Handler mainThreadHandler;

    public Handler getMainThreadHandler() {
        if (mainThreadHandler == null) {
            mainThreadHandler = new Handler(Looper.getMainLooper());
        }
        return mainThreadHandler;
    }


    //pass message and message type to this method
    public void createToastMessage(String message, int type) {
//inflate the custom layout
        LayoutInflater layoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout toastLayout = (LinearLayout)
                layoutInflater.inflate(R.layout.layout_custom_toast, null);
        TextView toastShowMessage = toastLayout.findViewById(R.id.lbltoastmsg);
        switch (type) {
            case 0:
//if the message type is 0 fail toaster method will call
                createFailToast(toastLayout, toastShowMessage, message);
                break;
            case 1:
//if the message type is 1 success toaster method will call
                createSuccessToast(toastLayout, toastShowMessage, message);
                break;
            case 2:
                createWarningToast(toastLayout, toastShowMessage, message);
//if the message type is 2 warning toaster method will call
                break;
            default:
                createFailToast(toastLayout, toastShowMessage, message);
        }
    }

    //Failure toast message method
    private final void createFailToast(LinearLayout toastLayout, TextView toastMessage, String
            message) {
        toastLayout.setBackgroundColor(context.getResources().getColor(R.color.error_toast_background));
        toastMessage.setText(message);
        toastMessage.setTextColor(context.getResources().getColor(R.color.error_toast_text));
        showToast(toastLayout);
    }

    //warning toast message method
    private final void createWarningToast(LinearLayout toastLayout, TextView toastMessage,
                                          String message) {
        toastLayout.setBackgroundColor(context.getResources().getColor(R.color.warning_toast_background));
        toastMessage.setText(message);
        toastMessage.setTextColor(context.getResources().getColor(R.color.warning_toast_text));
        showToast(toastLayout);
    }

    //success toast message method
    private final void createSuccessToast(LinearLayout toastLayout, TextView toastMessage, String
            message) {
        toastLayout.setBackgroundColor(context.getResources().getColor(R.color.success_toast_background));
        toastMessage.setText(message);
        toastMessage.setTextColor(context.getResources().getColor(R.color.success_toast_text));
        showToast(toastLayout);
    }

    private void showToast(final View view) {

        Toast toast = new Toast(context);
        int yOffset = Math.max(0, view.getHeight() - toast.getYOffset());
        toast.setGravity(Gravity.END | Gravity.CENTER_HORIZONTAL, 0, yOffset);
//        toast.setGravity(Gravity.END, 0, 0); // show message in the top of the device
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();

    }


}
