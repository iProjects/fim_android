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

public class createmanufactureractivity extends AppCompatActivity {

    private final static String TAG = createmanufactureractivity.class.getSimpleName();
    Button btncreatemanufacturer;
    ImageButton imgbtncreatemanufacturer;
    TextView txtcreatemanufacturername, lblerrormsg;
	Spinner cbocreatestatus;
	statusdto _selected_statusdto;
    // Create DatabasehelperUtilz class object in your activity.
    private DatabasehelperUtilz db;
	private ProgressDialog simpleWaitDialog;
    manufacturerdto _manufacturerdto = new manufacturerdto();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_manufacturer_layout);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this will show the back arrow in the tool bar.
		
		getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));
		                    
		txtcreatemanufacturername = findViewById(R.id.txtcreatemanufacturername);
		lblerrormsg = findViewById(R.id.lblerrormsg);
					 
        imgbtncreatemanufacturer = findViewById(R.id.imgbtncreatemanufacturer);
        imgbtncreatemanufacturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
					lblerrormsg.setText("");
					String strmsg = "";
					if(txtcreatemanufacturername.getText() == null || txtcreatemanufacturername.getText().length() == 0){
						strmsg += "name cannot be null."; 
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

                    _manufacturerdto.setmanufacturer_name(txtcreatemanufacturername.getText().toString());
                    _manufacturerdto.setmanufacturer_status(_selected_statusdto.getstatus_value());
                    _manufacturerdto.setcreated_date(timeStamp);

                    //new createrecordBackgroundAsyncTask().execute(_manufacturerdto);
 
                    new checkifrecordexistsBackgroundAsyncTask().execute(_manufacturerdto.getmanufacturer_name());

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
	
	private class createrecordBackgroundAsyncTask extends AsyncTask<manufacturerdto, Void, String> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            //use the instance for showing the ProgressBar
//            _progressapisingleton.showProgress(getApplicationContext(), getApplicationContext().getResources().getString(R.string.createmanufacturerprogress), true);

            simpleWaitDialog = ProgressDialog.show(createmanufactureractivity.this,
                    "creating record in datastore...", "excecuting task...");

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(manufacturerdto... _manufacturerdto) {
            // Disk-intensive work. This runs on a background thread.
// Search through a file for the first line that contains "Hello", and return
// that line.
//            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(createmanufactureractivity.this);
            db.openDataBase();
            db.createmanufacturer(_manufacturerdto[0]);
            db.close();
			
            savemanufacturerinsharedprefs(_manufacturerdto[0]);

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

            utilz.getInstance(getApplicationContext()).globalloghandler("record successfully persisted in datastore...", TAG, 1, 1, "createmanufactureractivity.onPostExecute", "createmanufactureractivity.onPostExecute");

            final Intent manufacturerslistactivity = new Intent(getApplicationContext(), manufacturerslistactivity.class);
            startActivity(manufacturerslistactivity);

        }

    }

	/* Defined as <Params, Progress, Result>*/
    private class checkifrecordexistsBackgroundAsyncTask extends AsyncTask<String, Void, List<manufacturerdto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            //showing the ProgressBar  
            simpleWaitDialog = ProgressDialog.show(createmanufactureractivity.this, "checking if record exists in datastore...", "excecuting task...");
 
            super.onPreExecute();
        }

        @Override
        protected List<manufacturerdto> doInBackground(String... param) {
            // Disk-intensive work. This runs on a background thread.
// Search through a file for the first line that contains "Hello", and return
// that line.
            Log.e(TAG, "doInBackground");

            String _dto_name = param[0];
			Log.e(TAG, "_dto_name =  " + _dto_name);
			
            db = new DatabasehelperUtilz(createmanufactureractivity.this);
            db.openDataBase();
            List<manufacturerdto> _lstdtos = db.filtermanufacturersgivenname(param[0]);
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
        protected void onPostExecute(List<manufacturerdto> result) {
            super.onPostExecute(result);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result(String s) returned from the doInBackground() method.
            // Update UI with the found string.
            Log.e(TAG, "onPostExecute");
             
			if(result.size() > 0){
				
				simpleWaitDialog.dismiss();
				
				utilz.getInstance(getApplicationContext()).globalloghandler("record with name [ " + result.get(0).getmanufacturer_name() + " ] exists in datastore...", TAG, 1, 0, "createmanufactureractivity.onPostExecute", "createmanufactureractivity.onPostExecute");
				 
			}else{
				
				simpleWaitDialog.dismiss();
				
				Log.e(TAG, "record with name " + _manufacturerdto.getmanufacturer_name() + " does not exist in datastore.");
				
				new createrecordBackgroundAsyncTask().execute(_manufacturerdto);
 
			}
			  
        }

    }

    public void savemanufacturerinsharedprefs(manufacturerdto _manufacturerdto) {
        try {
            final String PREFS_FILE = "ntharene_prefs_manufacturers";
            // PREFS_MODE defines which apps can access the file
            final int PREFS_MODE = Context.MODE_PRIVATE;

            SharedPreferences settings = getSharedPreferences(PREFS_FILE, PREFS_MODE);
            SharedPreferences.Editor editor = settings.edit();
            // write string
            editor.putString(_manufacturerdto.getmanufacturer_name(), _manufacturerdto.getmanufacturer_name());
            editor.putString(_manufacturerdto.getmanufacturer_status(), _manufacturerdto.getmanufacturer_status());
            editor.putString(_manufacturerdto.getcreated_date(), _manufacturerdto.getcreated_date());
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
