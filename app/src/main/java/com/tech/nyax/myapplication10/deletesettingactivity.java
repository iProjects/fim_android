package com.tech.nyax.myapplication10;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class deletesettingactivity extends AppCompatActivity {

    private final static String TAG = deletesettingactivity.class.getSimpleName();
    Button btndeletesetting;
	ImageButton imgbtndeletesetting;
    TextView txteditsettingid, txteditsettingname, txteditsettingvalue, lblerrormsg;
	Spinner cbocreatestatus;
    statusdto _selected_statusdto;
    // Create DatabasehelperUtilz class object in your activity.
    private DatabasehelperUtilz db;
    progressapisingleton _progressapisingleton = progressapisingleton.getInstance();
    private ProgressDialog simpleWaitDialog;
    String _settingid;
	statusspinneradapter _statusspinneradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_setting_layout);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this will show the back arrow in the tool bar.

		getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));
		
		txteditsettingname = findViewById(R.id.txteditsettingname);
		txteditsettingid = findViewById(R.id.txteditsettingid);
		txteditsettingvalue = findViewById(R.id.txteditsettingvalue); 
		lblerrormsg = findViewById(R.id.lblerrormsg);

        List<statusdto> _lstdata = new ArrayList<>();
        _lstdata.add(new statusdto(1L, "active", "active"));
        _lstdata.add(new statusdto(2L, "inactive", "inactive"));

        _statusspinneradapter = new statusspinneradapter(getApplicationContext(), _lstdata);
		  
        cbocreatestatus = findViewById(R.id.cbocreatestatus); 
        cbocreatestatus.setAdapter(_statusspinneradapter);

        cbocreatestatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _selected_statusdto = (statusdto) parent.getItemAtPosition(position);
                utilz.getInstance(getApplicationContext()).globalloghandler(_selected_statusdto.getstatus_value(), TAG, 1, 1);
                 
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
		
        imgbtndeletesetting = findViewById(R.id.imgbtndeletesetting);
        imgbtndeletesetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
					lblerrormsg.setText("");
					String strmsg = "";
					if(txteditsettingname.getText() == null || txteditsettingname.getText().length() == 0){
						strmsg += "name cannot be null."; 
					}
					if(txteditsettingvalue.getText() == null || txteditsettingvalue.getText().length() == 0){
						strmsg += "\nvalue cannot be null."; 
					}
					if(_selected_statusdto.getstatus_value() == null || _selected_statusdto.getstatus_value().length() == 0){
						strmsg += "\nstatus cannot be null."; 
					}
					if(strmsg.length() > 0){
						utilz.getInstance(getApplicationContext()).globalloghandler(strmsg, TAG, 1, 0);
						lblerrormsg.setText(strmsg);
						return;
					}
					
                    String timeStamp = utilz.getInstance(getApplicationContext()).getcurrenttimestamp();

                    settingdto _settingdto = new settingdto();
                    _settingdto.setsetting_Id(Long.valueOf(_settingid));
                    _settingdto.setsetting_name(txteditsettingname.getText().toString());
                    _settingdto.setsetting_value(txteditsettingvalue.getText().toString());
					_settingdto.setsetting_status(_selected_statusdto.getstatus_value());
                    _settingdto.setcreated_date(timeStamp);

                    new deleterecordBackgroundAsyncTask().execute(_settingdto);

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });

        // getting settingid from bundle
        Bundle _bundle_extras = getIntent().getExtras();

        if (_bundle_extras != null) {

            _settingid = _bundle_extras.getString("settingid");

            // Getting complete record details in background thread
            new getrecordBackgroundAsyncTask().execute(_settingid);
        }

    }

    /*Params the type of the parameters sent to the task upon execution.
    Progress the type of the progress units published during the background computation
    Result the type of the result of the background computation.*/

    /*When defining an AsyncTask we can pass three types between < > brackets.
    Defined as <Params, Progress, Result>*/

    /*onPreExecute() : invoked on the UI thread before the task is executed
    doInBackground(): invoked on the background thread immediately after onPreExecute() finishes
executing.
        onProgressUpdate(): invoked on the UI thread after a call to publishProgress(Progress...).
    onPostExecute(): invoked on the UI thread after the background computation finishes*/

    /* AsyncTasks should ideally be used for short operations (a few seconds at the most.)
     An asynchronous task is defined by 3 generic types, called Params, Progress and Result, and 4 steps,
     called onPreExecute(), doInBackground(), onProgressUpdate() and onPostExecute().
     In onPreExecute() you can define code, which need to be executed before background processing starts.
     doInBackground have code which needs to be executed in background, here in doInBackground() we can
     send results to multiple times to event thread by publishProgress() method, to notify background processing
     has been completed we can return results simply.
     onProgressUpdate() method receives progress updates from doInBackground() method, which is published
     via publishProgress() method, and this method can use this progress update to update event thread
     onPostExecute() method handles results returned by doInBackground() method.
     The generic types used are
     Params, the type of the parameters sent to the task upon execution
     Progress, the type of the progress units published during the background computation.
             Result, the type of the result of the background computation.
     If an async task not using any types, then it can be marked as Void type.
     An running async task can be cancelled by calling cancel(boolean) method.
     */

    private class getrecordBackgroundAsyncTask extends AsyncTask<String, Void, settingdto> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.d(TAG, "onPreExecute");

            //use the instance for showing the ProgressBar
//            _progressapisingleton.showProgress(getApplicationContext(), getApplicationContext().getResources().getString(R.string.editsettingprogress), true);

            simpleWaitDialog = ProgressDialog.show(deletesettingactivity.this,
                    "fetching record from datastore...", "executing task...");

            super.onPreExecute();
        }

        @Override
        protected settingdto doInBackground(String... param) {
            // Disk-intensive work. This runs on a background thread.
// Search through a file for the first line that contains "Hello", and return
// that line.
//            Log.d(TAG, "doInBackground");

            db = new DatabasehelperUtilz(deletesettingactivity.this);
            db.openDataBase(); 
            Long _setting_id = Long.valueOf(param[0]);
            settingdto _settingdto = db.getsettinggivenid(_setting_id);
            db.close();

            return _settingdto;
        }

        @Override
        protected void onProgressUpdate(Void... p) {
            super.onProgressUpdate(p);
            // Runs on the UI thread after publishProgress is invoked
            Log.d(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(settingdto result) {
            super.onPostExecute(result);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result(String s) returned from the doInBackground() method.
            // Update UI with the found string.
            Log.d(TAG, "onPostExecute");
            // threadsafetoast.getApp().showToast("onPostExecute");

            txteditsettingname.setText(result.getsetting_name());
            txteditsettingvalue.setText(result.getsetting_value()); 
			txteditsettingid.setText(String.valueOf(result.getsetting_Id())); 

			cbocreatestatus.setSelection(_statusspinneradapter.getIndex(cbocreatestatus, result.getsetting_status()));
			 
            simpleWaitDialog.dismiss();

        }

    }

    private class deleterecordBackgroundAsyncTask extends AsyncTask<settingdto, Void, String> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.d(TAG, "onPreExecute");

            //use the instance for showing the ProgressBar
//            _progressapisingleton.showProgress(getApplicationContext(), getApplicationContext().getResources().getString(R.string.editsettingprogress), true);

            simpleWaitDialog = ProgressDialog.show(deletesettingactivity.this,
                    "purging record from datastore...", "executing task");

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(settingdto... _settingdto) {
            // Disk-intensive work. This runs on a background thread.
// Search through a file for the first line that contains "Hello", and return
// that line.
//            Log.d(TAG, "doInBackground");

            db = new DatabasehelperUtilz(deletesettingactivity.this);
            db.openDataBase(); 
            db.deletesetting(String.valueOf(_settingdto[0].getsetting_Id()));
            db.close();

            return "1";
        }

        @Override
        protected void onProgressUpdate(Void... p) {
            super.onProgressUpdate(p);
            // Runs on the UI thread after publishProgress is invoked
            Log.d(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result(String s) returned from the doInBackground() method.
            // Update UI with the found string.
            Log.d(TAG, "onPostExecute");
            // threadsafetoast.getApp().showToast("onPostExecute");

            simpleWaitDialog.dismiss();

            utilz.getInstance(getApplicationContext()).globalloghandler("record successfully purged from datastore...", TAG, 1, 1, "deletesettingactivity.onPostExecute", "deletesettingactivity.onPostExecute");

            final Intent settingslistactivity = new Intent(getApplicationContext(), settingslistactivity.class);
            startActivity(settingslistactivity);

        }

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

