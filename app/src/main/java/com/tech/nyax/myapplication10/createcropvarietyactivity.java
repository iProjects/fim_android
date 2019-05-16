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

public class createcropvarietyactivity extends AppCompatActivity {

    private final static String TAG = createcropvarietyactivity.class.getSimpleName();
    Button btncreatecropvariety;
    ImageButton imgbtncreatecropvariety;
    TextView txtcreatecropvarietyname, lblerrormsg;
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
	private ProgressDialog simpleWaitDialog;
	cropvarietydto _cropvarietydto = new cropvarietydto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_crop_variety_layout);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this will show the back arrow in the tool bar.
		
		getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));
		
		txtcreatecropvarietyname = findViewById(R.id.txtcreatecropvarietyname);
		lblerrormsg = findViewById(R.id.lblerrormsg);
		
        imgbtncreatecropvariety = findViewById(R.id.imgbtncreatecropvariety);
        imgbtncreatecropvariety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    lblerrormsg.setText("");
					String strmsg = "";
					if(txtcreatecropvarietyname.getText() == null || txtcreatecropvarietyname.getText().length() == 0){
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
 
                    _cropvarietydto.setcropvariety_name(txtcreatecropvarietyname.getText().toString());
					_cropvarietydto.setcropvariety_crop_id(String.valueOf(_selectedcrop.getcrop_Id()));
					_cropvarietydto.setcropvariety_manufacturer_id(String.valueOf(_selectedmanufacturer.getmanufacturer_Id()));
                    _cropvarietydto.setcropvariety_status(_selected_statusdto.getstatus_value());
                    _cropvarietydto.setcreated_date(timeStamp);

                    //new createrecordBackgroundAsyncTask().execute(_cropvarietydto);

                    new checkifrecordexistsBackgroundAsyncTask().execute(_cropvarietydto.getcropvariety_name(), _cropvarietydto.getcropvariety_crop_id(), _cropvarietydto.getcropvariety_manufacturer_id());

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
  
        new getallactivecropsrecordsBackgroundAsyncTask().execute();
 
        new getallactivemanufacturersrecordsBackgroundAsyncTask().execute();
 
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
    private class createrecordBackgroundAsyncTask extends AsyncTask<cropvarietydto, Void, String> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            //use the instance for showing the ProgressBar
//            _progressapisingleton.showProgress(getApplicationContext(), getApplicationContext().getResources().getString(R.string.createcropprogress), true);

            simpleWaitDialog = ProgressDialog.show(createcropvarietyactivity.this,
                    "creating record in datastore...", "excecuting task...");

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(cropvarietydto... _cropvarietydto) {
            // Disk-intensive work. This runs on a background thread.
// Search through a file for the first line that contains "Hello", and return
// that line.
//            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(createcropvarietyactivity.this);
            db.openDataBase();
            db.createcropvariety(_cropvarietydto[0]);
            db.close();

			savecropinsharedprefs(_cropvarietydto[0]);

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

            utilz.getInstance(getApplicationContext()).globalloghandler("record successfully persisted in datastore...", TAG, 1, 1, "createcropvarietyactivity.onPostExecute", "createcropvarietyactivity.onPostExecute");

            final Intent cropsvarietieslistactivity = new Intent(getApplicationContext(), cropsvarietieslistactivity.class);
            startActivity(cropsvarietieslistactivity);

        }

    }

	/* Defined as <Params, Progress, Result>*/
    private class checkifrecordexistsBackgroundAsyncTask extends AsyncTask<String, Void, List<cropvarietydto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            //showing the ProgressBar  
            simpleWaitDialog = ProgressDialog.show(createcropvarietyactivity.this, "checking if record exists in datastore...", "excecuting task...");
 
            super.onPreExecute();
        }

        @Override
        protected List<cropvarietydto> doInBackground(String... param) {
            // Disk-intensive work. This runs on a background thread.
// Search through a file for the first line that contains "Hello", and return
// that line.
            Log.e(TAG, "doInBackground");

            String _dto_name = param[0];
			String _dto_crop_id = param[1];
			String _dto_manufacturer_id = param[2];
			
			Log.e(TAG, "_dto_name =  " + _dto_name);
			Log.e(TAG, "_dto_crop_id =  " + _dto_crop_id);
			Log.e(TAG, "_dto_manufacturer_id =  " + _dto_manufacturer_id);
			
            db = new DatabasehelperUtilz(createcropvarietyactivity.this);
            db.openDataBase();
            List<cropvarietydto> _lstdtos = db.filtercropsvarietiesgivenname(param[0]);
			
			List<cropvarietydto> _dto_lst = db.getallcropsvarieties();
			List<cropvarietydto> _existing_dto_lst = new ArrayList<>();
			
			boolean _name_exists = false;
			boolean _crop_exists = false;
			boolean _manufacturer_exists = false;
			
			for (cropvarietydto _cropvarietydto : _dto_lst) {
				if(_cropvarietydto.getcropvariety_name().toLowerCase() ==_dto_name.toString().toLowerCase()) { 
					_name_exists = true;
				}
				if(_cropvarietydto.getcropvariety_crop_id().toLowerCase() == _dto_crop_id.toString().toLowerCase()) { 
					_crop_exists = true;
				}
				if(_cropvarietydto.getcropvariety_manufacturer_id().toLowerCase() == _dto_manufacturer_id.toString().toLowerCase()) { 
					_manufacturer_exists = true;
				}
				if(_name_exists && _crop_exists && _manufacturer_exists) {
					_existing_dto_lst.add(_cropvarietydto);
				}
			}
			
            db.close();

            return _existing_dto_lst;
        }

        @Override
        protected void onProgressUpdate(Void... p) {
            super.onProgressUpdate(p);
            // Runs on the UI thread after publishProgress is invoked
            Log.e(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(List<cropvarietydto> result) {
            super.onPostExecute(result);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result(String s) returned from the doInBackground() method.
            // Update UI with the found string.
            Log.e(TAG, "onPostExecute");
			
            db = new DatabasehelperUtilz(createcropvarietyactivity.this);
            db.openDataBase();
			
			if(result.size() > 0){
				
				simpleWaitDialog.dismiss();
				
				utilz.getInstance(getApplicationContext()).globalloghandler("record with name [ " + result.get(0).getcropvariety_name() + " ], crop [ " + db.filtercropgivenid(result.get(0).getcropvariety_crop_id()).get(0) + " ], manufacturer [ " + db.filtermanufacturergivenid(result.get(0).getcropvariety_manufacturer_id()).get(0) + " ]  exists in datastore...", TAG, 1, 0, "createcropvarietyactivity.onPostExecute", "createcropvarietyactivity.onPostExecute");
				 
			}else{
				
				simpleWaitDialog.dismiss();
				
				Log.e(TAG, "record with name " + _cropvarietydto.getcropvariety_name() + " does not exist in datastore.");
				
				new createrecordBackgroundAsyncTask().execute(_cropvarietydto);
 
			}
			
            db.close();
  
        }

    }

    public void savecropinsharedprefs(cropvarietydto _cropvarietydto) {
        try {
            final String PREFS_FILE = "ntharene_prefs_cropsvarieties";
            // PREFS_MODE defines which apps can access the file
            final int PREFS_MODE = Context.MODE_PRIVATE;

            SharedPreferences settings = getSharedPreferences(PREFS_FILE, PREFS_MODE);
            SharedPreferences.Editor editor = settings.edit();
            // write string
            editor.putString(_cropvarietydto.getcropvariety_name(), _cropvarietydto.getcropvariety_name());
            editor.putString(_cropvarietydto.getcropvariety_status(), _cropvarietydto.getcropvariety_status());
            editor.putString(_cropvarietydto.getcreated_date(), _cropvarietydto.getcreated_date());
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
