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

public class deletecropdiseasepestactivity extends AppCompatActivity {

    private final static String TAG = deletecropdiseasepestactivity.class.getSimpleName();
    Button btndeletecropdisease;
	ImageButton imgbtndeletecropdiseasepest;
    TextView txteditcropdiseasename, txteditcropdiseaseid, lblerrormsg;
	Spinner cbocreatestatus, cbocreatecropdiseasecategory;
    cropspinneradapter _cropspinneradapter;
	statusdto _selected_statusdto;
	diseasepestdto _selected_diseasepestdto;
    // Create DatabasehelperUtilz class object in your activity.
    private DatabasehelperUtilz db;
    progressapisingleton _progressapisingleton = progressapisingleton.getInstance();
    private ProgressDialog simpleWaitDialog;
    String _cropdiseaseid;
	diseasepestspinneradapter _diseasepestspinneradapter;
	statusspinneradapter _statusspinneradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_crop_disease_layout);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this will show the back arrow in the tool bar.

		getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));
		
		txteditcropdiseasename = findViewById(R.id.txteditcropdiseasename);
		txteditcropdiseaseid = findViewById(R.id.txteditcropdiseaseid);
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
		 
        List<diseasepestdto> _lstdiseasepestcategories = new ArrayList<>();
        _lstdiseasepestcategories.add(new diseasepestdto(1L, "disease", "disease"));
        _lstdiseasepestcategories.add(new diseasepestdto(2L, "pest", "pest"));

        _diseasepestspinneradapter = new diseasepestspinneradapter(getApplicationContext(), _lstdiseasepestcategories);
		 
        cbocreatecropdiseasecategory = findViewById(R.id.cbocreatecropdiseasecategory); 
        cbocreatecropdiseasecategory.setAdapter(_diseasepestspinneradapter);

        cbocreatecropdiseasecategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _selected_diseasepestdto = (diseasepestdto) parent.getItemAtPosition(position);
                utilz.getInstance(getApplicationContext()).globalloghandler(_selected_diseasepestdto.getdto_value(), TAG, 1, 1);
               
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
 
        imgbtndeletecropdiseasepest = findViewById(R.id.imgbtndeletecropdiseasepest);
        imgbtndeletecropdiseasepest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
					lblerrormsg.setText("");
					String strmsg = "";
					if(txteditcropdiseasename.getText() == null || txteditcropdiseasename.getText().length() == 0){
						strmsg += "name cannot be null."; 
					}
					if(_selected_diseasepestdto.getdto_value() == null || _selected_diseasepestdto.getdto_value().length() == 0){
						strmsg += "\nselect category."; 
					}
					if(_selected_statusdto.getstatus_value() == null || _selected_statusdto.getstatus_value().length() == 0){
						strmsg += "\nselect status."; 
					}
					if(strmsg.length() > 0){
						utilz.getInstance(getApplicationContext()).globalloghandler(strmsg, TAG, 1, 0);
						lblerrormsg.setText(strmsg);
						return;
					}
					
                    String timeStamp = utilz.getInstance(getApplicationContext()).getcurrenttimestamp();

                    cropdiseasedto _cropdiseasedto = new cropdiseasedto();
                    _cropdiseasedto.setcropdisease_Id(Long.valueOf(_cropdiseaseid));
                    _cropdiseasedto.setcropdisease_name(txteditcropdiseasename.getText().toString());
					_cropdiseasedto.setcropdisease_category(_selected_diseasepestdto.getdto_value());
					_cropdiseasedto.setcropdisease_status(_selected_statusdto.getstatus_value());
                    _cropdiseasedto.setcreated_date(timeStamp);

                    new deleterecordBackgroundAsyncTask().execute(_cropdiseasedto);

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });

        // getting cropdiseaseid from bundle
        Bundle _bundle_extras = getIntent().getExtras();

        if (_bundle_extras != null) {

            _cropdiseaseid = _bundle_extras.getString("cropdiseaseid");

            // Getting complete record details in background thread
            new getrecordBackgroundAsyncTask().execute(_cropdiseaseid);
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

    private class getrecordBackgroundAsyncTask extends AsyncTask<String, Void, cropdiseasedto> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.d(TAG, "onPreExecute");

            //use the instance for showing the ProgressBar
//            _progressapisingleton.showProgress(getApplicationContext(), getApplicationContext().getResources().getString(R.string.editcropdiseaseprogress), true);

            simpleWaitDialog = ProgressDialog.show(deletecropdiseasepestactivity.this,
                    "fetching record from datastore...", "executing task...");

            super.onPreExecute();
        }

        @Override
        protected cropdiseasedto doInBackground(String... param) {
            // Disk-intensive work. This runs on a background thread.
// Search through a file for the first line that contains "Hello", and return
// that line.
//            Log.d(TAG, "doInBackground");

            db = new DatabasehelperUtilz(deletecropdiseasepestactivity.this);
            db.openDataBase();
            Long _cropdisease_id = Long.valueOf(param[0]);
            cropdiseasedto _cropdiseasedto = db.getcropdiseasegivenid(_cropdisease_id);
            db.close();

            return _cropdiseasedto;
        }

        @Override
        protected void onProgressUpdate(Void... p) {
            super.onProgressUpdate(p);
            // Runs on the UI thread after publishProgress is invoked
            Log.d(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(cropdiseasedto result) {
            super.onPostExecute(result);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result(String s) returned from the doInBackground() method.
            // Update UI with the found string.
            Log.d(TAG, "onPostExecute");
            // threadsafetoast.getApp().showToast("onPostExecute");

            txteditcropdiseasename.setText(result.getcropdisease_name()); 
			txteditcropdiseaseid.setText(String.valueOf(result.getcropdisease_Id())); 
 
			cbocreatecropdiseasecategory.setSelection(_diseasepestspinneradapter.getIndex(cbocreatecropdiseasecategory, result.getcropdisease_category()));
			 
			cbocreatestatus.setSelection(_statusspinneradapter.getIndex(cbocreatestatus, result.getcropdisease_status()));
			 
            simpleWaitDialog.dismiss();

        }

    }

    private class deleterecordBackgroundAsyncTask extends AsyncTask<cropdiseasedto, Void, String> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.d(TAG, "onPreExecute");

            //use the instance for showing the ProgressBar
//            _progressapisingleton.showProgress(getApplicationContext(), getApplicationContext().getResources().getString(R.string.editcropdiseaseprogress), true);

            simpleWaitDialog = ProgressDialog.show(deletecropdiseasepestactivity.this,
                    "purging record from datastore...", "executing task");

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(cropdiseasedto... _cropdiseasedto) {
            // Disk-intensive work. This runs on a background thread.
// Search through a file for the first line that contains "Hello", and return
// that line.
//            Log.d(TAG, "doInBackground");

            db = new DatabasehelperUtilz(deletecropdiseasepestactivity.this);
            db.openDataBase(); 
            db.deletecropdisease(String.valueOf(_cropdiseasedto[0].getcropdisease_Id()));
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

            utilz.getInstance(getApplicationContext()).globalloghandler("record successfully purged from datastore...", TAG, 1, 1, "deletecropdiseasepestactivity.onPostExecute", "deletecropdiseasepestactivity.onPostExecute");

            final Intent cropsdiseaseslistactivity = new Intent(getApplicationContext(), cropsdiseaseslistactivity.class);
            startActivity(cropsdiseaseslistactivity);

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
