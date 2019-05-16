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

public class createsettingactivity extends AppCompatActivity {

    private final static String TAG = createsettingactivity.class.getSimpleName();
    Button btncreatesetting;
    ImageButton imgbtncreatesetting;
    TextView txtcreatesettingname, txtcreatesettingvalue, lblerrormsg;
	Spinner cbocreatestatus;
	statusdto _selected_statusdto;
    // Create DatabasehelperUtilz class object in your activity.
    private DatabasehelperUtilz db;
    private ProgressDialog simpleWaitDialog;
    settingdto _settingdto = new settingdto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_setting_layout);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this will show the back arrow in the tool bar.
		
		getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));
		
        txtcreatesettingname = findViewById(R.id.txtcreatesettingname);
		txtcreatesettingvalue = findViewById(R.id.txtcreatesettingvalue);
		lblerrormsg = findViewById(R.id.lblerrormsg);
					
        imgbtncreatesetting = findViewById(R.id.imgbtncreatesetting);
        imgbtncreatesetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
					lblerrormsg.setText("");
					String strmsg = "";
					if(txtcreatesettingname.getText() == null || txtcreatesettingname.getText().length() == 0){
						strmsg += "name cannot be null."; 
					}
					if(txtcreatesettingvalue.getText() == null || txtcreatesettingvalue.getText().length() == 0){
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
 
                    _settingdto.setsetting_name(txtcreatesettingname.getText().toString());
                    _settingdto.setsetting_value(txtcreatesettingvalue.getText().toString());
                    _settingdto.setsetting_status(_selected_statusdto.getstatus_value());
                    _settingdto.setcreated_date(timeStamp);

                    //new createrecordBackgroundAsyncTask().execute(_settingdto);

                    new checkifrecordexistsBackgroundAsyncTask().execute(_settingdto.getsetting_name());

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });

        cbocreatestatus = findViewById(R.id.cbocreatestatus);

        List<statusdto> _lstdata = new ArrayList<>();
        _lstdata.add(new statusdto(1L, "active", "active"));
        _lstdata.add(new statusdto(2L, "inactive", "inactive"));

        statusspinneradapter _statusspinneradapter = new statusspinneradapter(getApplicationContext(), _lstdata);
		 
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
    private class createrecordBackgroundAsyncTask extends AsyncTask<settingdto, Void, String> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            //use the instance for showing the ProgressBar
//            _progressapisingleton.showProgress(getApplicationContext(), getApplicationContext().getResources().getString(R.string.createsettingprogress), true);

            simpleWaitDialog = ProgressDialog.show(createsettingactivity.this,
                    "creating record in datastore...", "excecuting task...");

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(settingdto... _settingdto) {
            // Disk-intensive work. This runs on a background thread.
// Search through a file for the first line that contains "Hello", and return
// that line.
//            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(createsettingactivity.this);
            db.openDataBase();
            db.createsetting(_settingdto[0]);
            db.close();
			
            savesettinginsharedprefs(_settingdto[0]);

            return "1";
        }

        @Override
        protected void onProgressUpdate(Void... p) {
            super.onProgressUpdate(p);
            // Runs on the UI thread after publishProgress is invoked
            Log.e(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result(String s) returned from the doInBackground() method.
            // Update UI with the found string.
            Log.e(TAG, "onPostExecute");
            // threadsafetoast.getApp().showToast("onPostExecute");

            simpleWaitDialog.dismiss();

            utilz.getInstance(getApplicationContext()).globalloghandler("record successfully persisted in datastore...", TAG, 1, 1, "createsettingactivity.onPostExecute", "createsettingactivity.onPostExecute");

            final Intent settingslistactivity = new Intent(getApplicationContext(), settingslistactivity.class);
            startActivity(settingslistactivity);

        }

    }

	/* Defined as <Params, Progress, Result>*/
    private class checkifrecordexistsBackgroundAsyncTask extends AsyncTask<String, Void, List<settingdto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            //showing the ProgressBar  
            simpleWaitDialog = ProgressDialog.show(createsettingactivity.this, "checking if record exists in datastore...", "excecuting task...");
 
            super.onPreExecute();
        }

        @Override
        protected List<settingdto> doInBackground(String... param) {
            // Disk-intensive work. This runs on a background thread.
// Search through a file for the first line that contains "Hello", and return
// that line.
            Log.e(TAG, "doInBackground");

            String _dto_name = param[0];
			Log.e(TAG, "_dto_name =  " + _dto_name);
			
            db = new DatabasehelperUtilz(createsettingactivity.this);
            db.openDataBase();
            List<settingdto> _lstdtos = db.filtersettingsgivenname(param[0]);
            db.close();

            return _lstdtos;
        }

        @Override
        protected void onProgressUpdate(Void... p) {
            super.onProgressUpdate(p);
            // Runs on the UI thread after publishProgress is invoked
            Log.e(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(List<settingdto> result) {
            super.onPostExecute(result);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result(String s) returned from the doInBackground() method.
            // Update UI with the found string.
            Log.e(TAG, "onPostExecute");
             
			if(result.size() > 0){
				
				simpleWaitDialog.dismiss();
				
				utilz.getInstance(getApplicationContext()).globalloghandler("record with name [ " + result.get(0).getsetting_name() + " ] exists in datastore...", TAG, 1, 0, "createsettingactivity.onPostExecute", "createsettingactivity.onPostExecute");
				 
			}else{
				
				simpleWaitDialog.dismiss();
				
				Log.e(TAG, "record with name " + _settingdto.getsetting_name() + " does not exist in datastore.");
				
				new createrecordBackgroundAsyncTask().execute(_settingdto);
 
			}
			  
        }

    }

    public void savesettinginsharedprefs(settingdto _settingdto) {
        try {
            final String PREFS_FILE = "ntharene_prefs_settings";
            // PREFS_MODE defines which apps can access the file
            final int PREFS_MODE = Context.MODE_PRIVATE;

            SharedPreferences settings = getSharedPreferences(PREFS_FILE, PREFS_MODE);
            SharedPreferences.Editor editor = settings.edit();
            // write string
            editor.putString(_settingdto.getsetting_name(), _settingdto.getsetting_name());
            editor.putString(_settingdto.getsetting_value(), _settingdto.getsetting_value());
            editor.putString(_settingdto.getsetting_status(), _settingdto.getsetting_status());
            editor.putString(_settingdto.getcreated_date(), _settingdto.getcreated_date());
            // This will asynchronously save the shared preferences without holding the current thread.
            editor.apply();

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
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
