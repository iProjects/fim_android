package com.tech.nyax.myapplication10;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class msgutilsactivity extends AppCompatActivity {

    private final static String TAG = msgutilsactivity.class.getSimpleName();
    public static final String MESSAGE_CONSTANT = "com.tech.nyax.myapplication10";
    TextView txtmessage;
    String _msg;
    private ArrayList<String> _messages = new ArrayList<>();
    SpannableStringBuilder _strbuilder = new SpannableStringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msgutilsactivity_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this will show the back arrow in the tool bar.

        getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));

        txtmessage = findViewById(R.id.txtmessage);

        // getting fields from bundle
        Bundle _bundle_extras = getIntent().getExtras();

        if (_bundle_extras != null) {
            _msg = _bundle_extras.getString("message");
            //_messages = _bundle_extras.getStringArrayList("messages");
            _messages = _bundle_extras.getBundle("messages").getStringArrayList("messages");
            txtmessage.setText(_msg);

            for (String _message : _messages) {
                SpannableString _SpannableString = new SpannableString(_message);
                _SpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.custom_greenish)), 0, _message.length(), 0);
                _strbuilder.append(_SpannableString);
            }
            txtmessage.setText(_strbuilder.toString());
        }

        // View view = findViewById(R.id.txtmessage);
        // setupUI(view);

    }

    public void setupUI(View view) {
        String s = "inside";
//Set up touch listener for non-text box views to hide keyboard.
        if ((view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard();
                    return false;
                }
            });
        }
		if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard();
                    return false;
                }
            });
        }
//If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        Bundle _bundle_extras = getIntent().getExtras();
        if (_bundle_extras != null) {
            _msg = _bundle_extras.getString("message");
            txtmessage.setText(_msg);
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.home_menu:

                    utilz.getInstance(getApplicationContext()).globalloghandler("launching MainActivity...", TAG, 1, 1);

                    final Intent _MainActivity = new Intent(this, MainActivity.class);
                    startActivity(_MainActivity);
                    return true;
                default:
                    break;
            }
            return super.onOptionsItemSelected(item);
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
            return false;
        }
    }


}
