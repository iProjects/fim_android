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

public class createcropdiseaseactivity extends AppCompatActivity {

    private final static String TAG = createcropdiseaseactivity.class.getSimpleName();
    Button btncreatecropdisease;
    ImageButton imgbtncreatecropdisease;
    TextView txtcreatecropdiseasename, lblerrormsg;
    Spinner cbocreatestatus, cbocreatecropdiseasecategory; 
    cropspinneradapter _cropspinneradapter;
	statusdto _selected_statusdto;
	diseasepestdto _selected_diseasepestdto;
    // Create DatabasehelperUtilz class object in your activity.
    private DatabasehelperUtilz db;
    private ProgressDialog simpleWaitDialog;
	cropdiseasedto _cropdiseasedto = new cropdiseasedto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_crop_disease_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this will show the back arrow in the tool bar.

        getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));

		txtcreatecropdiseasename = findViewById(R.id.txtcreatecropdiseasename); 
		lblerrormsg = findViewById(R.id.lblerrormsg);

        imgbtncreatecropdisease = findViewById(R.id.imgbtncreatecropdisease);
        imgbtncreatecropdisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    lblerrormsg.setText("");
					String strmsg = "";
					if(txtcreatecropdiseasename.getText() == null || txtcreatecropdiseasename.getText().length() == 0){
						strmsg += "name cannot be null."; 
					}
					if(_selected_diseasepestdto.getdto_value() == null || _selected_diseasepestdto.getdto_value().length() == 0){
						strmsg += "\ncategory cannot be null."; 
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

                    _cropdiseasedto.setcropdisease_name(txtcreatecropdiseasename.getText().toString());
                    _cropdiseasedto.setcropdisease_category(_selected_diseasepestdto.getdto_value());
                    _cropdiseasedto.setcropdisease_status(_selected_statusdto.getstatus_value());
                    _cropdiseasedto.setcreated_date(timeStamp);

                    //new createrecordBackgroundAsyncTask().execute(_cropdiseasedto);
					 
                    new checkifrecordexistsBackgroundAsyncTask().execute(_cropdiseasedto.getcropdisease_name());

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
  
        cbocreatecropdiseasecategory = findViewById(R.id.cbocreatecropdiseasecategory);

        List<diseasepestdto> _lstdiseasepestcategories = new ArrayList<>();
        _lstdiseasepestcategories.add(new diseasepestdto(1L, "disease", "disease"));
        _lstdiseasepestcategories.add(new diseasepestdto(2L, "pest", "pest"));

        diseasepestspinneradapter _diseasepestspinneradapter = new diseasepestspinneradapter(getApplicationContext(), _lstdiseasepestcategories);
		 
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
    private class createrecordBackgroundAsyncTask extends AsyncTask<cropdiseasedto, Void, String> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            //use the instance for showing the ProgressBar
//            _progressapisingleton.showProgress(getApplicationContext(), getApplicationContext().getResources().getString(R.string.createcropprogress), true);

            simpleWaitDialog = ProgressDialog.show(createcropdiseaseactivity.this,
                    "creating record in datastore...", "excecuting task...");

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(cropdiseasedto... _cropdiseasedto) {
            // Disk-intensive work. This runs on a background thread.
// Search through a file for the first line that contains "Hello", and return
// that line.
//            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(createcropdiseaseactivity.this);
            db.openDataBase();
            db.createcropdisease(_cropdiseasedto[0]);
            db.close();

            savecropinsharedprefs(_cropdiseasedto[0]);

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

            utilz.getInstance(getApplicationContext()).globalloghandler("record successfully persisted in datastore...", TAG, 1, 1, "createcropdiseaseactivity.onPostExecute", "createcropdiseaseactivity.onPostExecute");

            final Intent cropsdiseaseslistactivity = new Intent(getApplicationContext(), cropsdiseaseslistactivity.class);
            startActivity(cropsdiseaseslistactivity);

        }

    }
 
	/* Defined as <Params, Progress, Result>*/
    private class checkifrecordexistsBackgroundAsyncTask extends AsyncTask<String, Void, List<cropdiseasedto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            //showing the ProgressBar  
            simpleWaitDialog = ProgressDialog.show(createcropdiseaseactivity.this, "checking if record exists in datastore...", "excecuting task...");
 
            super.onPreExecute();
        }

        @Override
        protected List<cropdiseasedto> doInBackground(String... param) {
            // Disk-intensive work. This runs on a background thread.
// Search through a file for the first line that contains "Hello", and return
// that line.
            Log.e(TAG, "doInBackground");

            String _dto_name = param[0];
			Log.e(TAG, "_dto_name =  " + _dto_name);
			
            db = new DatabasehelperUtilz(createcropdiseaseactivity.this);
            db.openDataBase();
            List<cropdiseasedto> _lstdtos = db.filtercropsdiseasesgivenname(param[0]);
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
        protected void onPostExecute(List<cropdiseasedto> result) {
            super.onPostExecute(result);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result(String s) returned from the doInBackground() method.
            // Update UI with the found string.
            Log.e(TAG, "onPostExecute");
             
			if(result.size() > 0){
				
				simpleWaitDialog.dismiss();
				
				utilz.getInstance(getApplicationContext()).globalloghandler("record with name [ " + result.get(0).getcropdisease_name() + " ] exists in datastore...", TAG, 1, 0, "createcropdiseaseactivity.onPostExecute", "createcropdiseaseactivity.onPostExecute");
				 
			}else{
				
				simpleWaitDialog.dismiss();
				
				Log.e(TAG, "record with name " + _cropdiseasedto.getcropdisease_name() + " does not exist in datastore.");
				
				new createrecordBackgroundAsyncTask().execute(_cropdiseasedto);
 
			}
			  
        }

    }

    public void savecropinsharedprefs(cropdiseasedto _cropdiseasedto) {
        try {
            final String PREFS_FILE = "ntharene_prefs_cropsdiseases";
            // PREFS_MODE defines which apps can access the file
            final int PREFS_MODE = Context.MODE_PRIVATE;

            SharedPreferences settings = getSharedPreferences(PREFS_FILE, PREFS_MODE);
            SharedPreferences.Editor editor = settings.edit();
            // write string
            editor.putString(_cropdiseasedto.getcropdisease_name(), _cropdiseasedto.getcropdisease_name());
            editor.putString(_cropdiseasedto.getcropdisease_category(), _cropdiseasedto.getcropdisease_category());
            editor.putString(_cropdiseasedto.getcropdisease_status(), _cropdiseasedto.getcropdisease_status());
            editor.putString(_cropdiseasedto.getcreated_date(), _cropdiseasedto.getcreated_date());
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
