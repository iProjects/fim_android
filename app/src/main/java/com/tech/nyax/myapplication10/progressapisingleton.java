package com.tech.nyax.myapplication10;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class progressapisingleton {
    public static progressapisingleton singletoninstance = null;
    private Dialog mDialog;
    ProgressBar mProgressBar;

    public static progressapisingleton getInstance() {
        if (singletoninstance == null) {
            singletoninstance = new progressapisingleton();
        }
        return singletoninstance;
    }

    public void showProgress(Context context, String message, boolean cancelable) {
        mDialog = new Dialog(context);
// no tile for the dialog
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.progress_bar_dialog_layout);
        mProgressBar = mDialog.findViewById(R.id.progress_bar);
//        mProgressBar.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.material_blue_gray_500), PorterDuff.Mode.SRC_IN);
//        TextView progressText = (TextView) mDialog.findViewById(R.id.progress_text);
//        progressText.setText("" + message);
//        progressText.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
// you can change or add this line according to your need
        mProgressBar.setIndeterminate(true);
        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        mDialog.show();
    }

    public void hideProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
    