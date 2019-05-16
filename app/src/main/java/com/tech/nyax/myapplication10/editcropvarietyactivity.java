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

public class editcropvarietyactivity extends AppCompatActivity {

    private final static String TAG = editcropvarietyactivity.class.getSimpleName();
    Button btnupdatecropvariety;
	ImageButton imgbtnupdatecropvariety;
    TextView txteditcropvarietyid, txteditcropvarietyname, lblerrormsg;
	Spinner cbocreatecrop, cbocreatestatus, cbocropvarietymanufacturer;
    List<cropdto> lstcropdtos;
	private cropdto _selectedcrop;
    cropspinneradapter _cropspinneradapter;
	statusdto _selected_statusdto;
	List<manufacturerdto> lstmanufacturerdtos;
	manufacturerdto _selectedmanufacturer;
    manufacturerspinneradapter _manufacturerspinneradapter;
    // Create DatabasehelperUtilz class object in your activity.
    private DatabasehelperUtilz db;
    progressapisingleton _progressapisingleton = progressapisingleton.getInstance();
    private ProgressDialog simpleWaitDialog;
    String _cropvarietyid;
	statusspinneradapter _statusspinneradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_crop_variety_layout);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this will show the back arrow in the tool bar.
		
		getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));
		
		txteditcropvarietyname = findViewById(R.id.txteditcropvarietyname);
		txteditcropvarietyid = findViewById(R.id.txteditcropvarietyid);
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
		
        imgbtnupdatecropvariety = findViewById(R.id.imgbtnupdatecropvariety);
        imgbtnupdatecropvariety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
					lblerrormsg.setText("");
					String strmsg = "";
					if(txteditcropvarietyname.getText() == null || txteditcropvarietyname.getText().length() == 0){
						strmsg += "name cannot be null."; 
					} 
					if(_selectedcrop.getcrop_name() == null || _selectedcrop.getcrop_name().length() == 0){
						strmsg += "\nselect crop."; 
					}
					if(_selectedmanufacturer.getmanufacturer_name() == null || _selectedmanufacturer.getmanufacturer_name().length() == 0){
						strmsg += "\nselect manufacturer."; 
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

                    cropvarietydto _cropvarietydto = new cropvarietydto();
                    _cropvarietydto.setcropvariety_Id(Long.valueOf(_cropvarietyid));
                    _cropvarietydto.setcropvariety_name(txteditcropvarietyname.getText().toString());
					_cropvarietydto.setcropvariety_crop_id(String.valueOf(_selectedcrop.getcrop_Id()));
					_cropvarietydto.setcropvariety_manufacturer_id(String.valueOf(_selectedmanufacturer.getmanufacturer_Id()));
					_cropvarietydto.setcropvariety_status(_selected_statusdto.getstatus_value());
                    _cropvarietydto.setcreated_date(timeStamp);

                    new saverecordBackgroundAsyncTask().execute(_cropvarietydto);

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });

        new getallactivecropsrecordsBackgroundAsyncTask().execute();
 
        new getallactivemanufacturersrecordsBackgroundAsyncTask().execute();
 
        // getting cropid from bundle
        Bundle _bundle_extras = getIntent().getExtras();

        if (_bundle_extras != null) {

            _cropvarietyid = _bundle_extras.getString("cropvarietyid");

            // Getting complete record details in background thread
            new getrecordBackgroundAsyncTask().execute(_cropvarietyid);
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

    private class getrecordBackgroundAsyncTask extends AsyncTask<String, Void, cropvarietydto> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.d(TAG, "onPreExecute");

            //use the instance for showing the ProgressBar
//            _progressapisingleton.showProgress(getApplicationContext(), getApplicationContext().getResources().getString(R.string.editcropvarietyprogress), true);

            simpleWaitDialog = ProgressDialog.show(editcropvarietyactivity.this,
                    "fetching record from datastore...", "executing task...");

            super.onPreExecute();
        }

        @Override
        protected cropvarietydto doInBackground(String... param) {
            // Disk-intensive work. This runs on a background thread.
// Search through a file for the first line that contains "Hello", and return
// that line.
//            Log.d(TAG, "doInBackground");

            db = new DatabasehelperUtilz(editcropvarietyactivity.this);
            db.openDataBase();
            //db.createtables();
            Long _crop_variety_id = Long.valueOf(param[0]);
            cropvarietydto _cropvarietydto = db.getcropvarietygivenid(_crop_variety_id);
            db.close();

            return _cropvarietydto;
        }

        @Override
        protected void onProgressUpdate(Void... p) {
            super.onProgressUpdate(p);
            // Runs on the UI thread after publishProgress is invoked
            Log.d(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(cropvarietydto result) {
            super.onPostExecute(result);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result(String s) returned from the doInBackground() method.
            // Update UI with the found string.
            Log.d(TAG, "onPostExecute");
            // threadsafetoast.getApp().showToast("onPostExecute");

            txteditcropvarietyname.setText(result.getcropvariety_name()); 
			txteditcropvarietyid.setText(String.valueOf(result.getcropvariety_Id())); 

			cbocreatestatus.setSelection(_statusspinneradapter.getIndex(cbocreatestatus, result.getcropvariety_status()));
			 
            simpleWaitDialog.dismiss();

        }

    }

    private class saverecordBackgroundAsyncTask extends AsyncTask<cropvarietydto, Void, String> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.d(TAG, "onPreExecute");

            //use the instance for showing the ProgressBar
//            _progressapisingleton.showProgress(getApplicationContext(), getApplicationContext().getResources().getString(R.string.editcropvarietyprogress), true);

            simpleWaitDialog = ProgressDialog.show(editcropvarietyactivity.this,
                    "updating record in datastore...", "executing task");

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(cropvarietydto... _cropvarietydto) {
            // Disk-intensive work. This runs on a background thread.
// Search through a file for the first line that contains "Hello", and return
// that line.
//            Log.d(TAG, "doInBackground");

            db = new DatabasehelperUtilz(editcropvarietyactivity.this);
            db.openDataBase();
            db.updatecropvariety(_cropvarietydto[0]);
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

            //simpleWaitDialog.dismiss();

            utilz.getInstance(getApplicationContext()).globalloghandler("record successfully persisted in datastore...", TAG, 1, 1, "editcropvarietyactivity.onPostExecute", "editcropvarietyactivity.onPostExecute");

            final Intent cropsvarietieslistactivity = new Intent(getApplicationContext(), cropsvarietieslistactivity.class);
            startActivity(cropsvarietieslistactivity);

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

	
    /* Defined as <Params, Progress, Result> */
    private class getallactivecropsrecordsBackgroundAsyncTask extends AsyncTask<Void, Void, List<cropdto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.d(TAG, "onPreExecute");

            //use the instance for showing the ProgressBar
//            _progressapisingleton.showProgress(getApplicationContext(), getApplicationContext().getResources().getString(R.string.editcropprogress), true);

            //simpleWaitDialog = ProgressDialog.show(createcropvarietyactivity.this, "fetching record from datastore...", "executing task...");

            lstcropdtos = new ArrayList<>();
            _cropspinneradapter = new cropspinneradapter(getApplicationContext(), lstcropdtos);

            super.onPreExecute();
        }

        @Override
        protected List<cropdto> doInBackground(Void... param) {
            // Disk-intensive work. This runs on a background thread.
            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(getApplicationContext());
            db.openDataBase();
            lstcropdtos = db.getallcrops();
            db.close();

            Collections.reverse(lstcropdtos);

            return lstcropdtos;
        }

        @Override
        protected void onProgressUpdate(Void... p) {
            super.onProgressUpdate(p);
            // Runs on the UI thread after publishProgress is invoked
            Log.d(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(final List<cropdto> lstcropdtos) {
            //            super.onPostExecute(result);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result returned from the doInBackground() method.
            // Update UI with the response.
            Log.e(TAG, "onPostExecute");

            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating data into ListView
                     * */

                    int _count = lstcropdtos.size();
                    Log.e(TAG, "crops count [ " + _count + " ]");

                    _cropspinneradapter = new cropspinneradapter(getApplicationContext(), lstcropdtos);
					
					cbocreatecrop = findViewById(R.id.cbocreatecrop);
                    cbocreatecrop.setAdapter(_cropspinneradapter);

                    cbocreatecrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            _selectedcrop = (cropdto) parent.getItemAtPosition(position);

                            utilz.getInstance(getApplicationContext()).globalloghandler(_selectedcrop.getcrop_name(), TAG, 1, 1);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });


                }
            });


        }

    }

    /* Defined as <Params, Progress, Result> */
    private class getallactivemanufacturersrecordsBackgroundAsyncTask extends AsyncTask<Void, Void, List<manufacturerdto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.d(TAG, "onPreExecute");

            //use the instance for showing the ProgressBar
//            _progressapisingleton.showProgress(getApplicationContext(), getApplicationContext().getResources().getString(R.string.editcropprogress), true);

            //simpleWaitDialog = ProgressDialog.show(createcropvarietyactivity.this, "fetching record from datastore...", "executing task...");

            lstmanufacturerdtos = new ArrayList<>();
            _manufacturerspinneradapter = new manufacturerspinneradapter(getApplicationContext(), lstmanufacturerdtos);

            super.onPreExecute();
        }

        @Override
        protected List<manufacturerdto> doInBackground(Void... param) {
            // Disk-intensive work. This runs on a background thread.
            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(getApplicationContext());
            db.openDataBase();
            lstmanufacturerdtos = db.getallmanufacturers();
            db.close();

            Collections.reverse(lstmanufacturerdtos);

            return lstmanufacturerdtos;
        }

        @Override
        protected void onProgressUpdate(Void... p) {
            super.onProgressUpdate(p);
            // Runs on the UI thread after publishProgress is invoked
            Log.d(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(final List<manufacturerdto> lstmanufacturerdtos) {
            //            super.onPostExecute(result);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result returned from the doInBackground() method.
            // Update UI with the response.
            Log.e(TAG, "onPostExecute");

            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating data into ListView
                     * */
 
                    int _count = lstmanufacturerdtos.size();
                    Log.e(TAG, "manufacturers count [ " + _count + " ]");

                    _manufacturerspinneradapter = new manufacturerspinneradapter(getApplicationContext(), lstmanufacturerdtos);
					
					cbocropvarietymanufacturer = findViewById(R.id.cbocropvarietymanufacturer);
                    cbocropvarietymanufacturer.setAdapter(_manufacturerspinneradapter);

                    cbocropvarietymanufacturer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            _selectedmanufacturer = (manufacturerdto) parent.getItemAtPosition(position);

                            utilz.getInstance(getApplicationContext()).globalloghandler(_selectedmanufacturer.getmanufacturer_name(), TAG, 1, 1);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });


                }
            });


        }

    }

	
	
	
	
	
	
	
	
	
	
	
	
	

}
