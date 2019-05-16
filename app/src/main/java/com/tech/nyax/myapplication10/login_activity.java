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
import java.util.List;

public class login_activity extends AppCompatActivity {

    private final static String TAG = login_activity.class.getSimpleName();
    Button btncreatecrop;
    TextView txtcreatecropname, txtcreatecropvariety, txtcreatecropstatus;
    // Create DatabasehelperUtilz class object in your activity.
    private DatabasehelperUtilz db;
    progressapisingleton _progressapisingleton = progressapisingleton.getInstance();
    private ProgressDialog simpleWaitDialog;
    statusdto _selected_statusdto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this will show the back arrow in the tool bar.
		
		getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));
		
        Spinner cbocreatecropstatus = findViewById(R.id.cbocreatecropstatus);

        List<statusdto> _lstdata = new ArrayList<>();
        _lstdata.add(new statusdto(1L, "active", "active"));
        _lstdata.add(new statusdto(2L, "inactive", "inactive"));

        statusspinneradapter _statusspinneradapter = new statusspinneradapter(getApplicationContext(), _lstdata);
        cbocreatecropstatus.setAdapter(_statusspinneradapter);

        cbocreatecropstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _selected_statusdto = (statusdto) parent.getItemAtPosition(position);
                utilz.getInstance(getApplicationContext()).globalloghandler(_selected_statusdto.getstatus_value(), TAG, 1, 1);
                //((TextView) parent.getChildAt(position)).setTextColor(0x00000000);
				
//				Dialog mDialog = new Dialog(getApplicationContext(), R.style.AppBaseTheme);
//				mDialog.setContentView(R.layout.fullscreen);
//				mDialog.show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

		 
        btncreatecrop = findViewById(R.id.btncreatecrop);
        btncreatecrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    txtcreatecropname = findViewById(R.id.txtcreatecropname);
                    txtcreatecropvariety = findViewById(R.id.txtcreatecropvariety);
                    txtcreatecropstatus = findViewById(R.id.txtcreatecropstatus);

                    String timeStamp = utilz.getInstance(getApplicationContext()).getcurrenttimestamp();

                    cropdto _cropdto = new cropdto();
                    _cropdto.setcrop_name(txtcreatecropname.getText().toString());
                    _cropdto.setcrop_status(_selected_statusdto.getstatus_value());
                    _cropdto.setcreated_date(timeStamp);

                    new createcropBackgroundAsyncTask().execute(_cropdto);

//					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//							getApplicationContext());
//					// set title
//					alertDialogBuilder.setTitle("Your Title");
//					// set dialog message
//					alertDialogBuilder
//							.setMessage("Click yes to exit!")
//							.setCancelable(false)
//							.setPositiveButton("Yes",
//									new DialogInterface.OnClickListener() {
//										public void onClick(DialogInterface dialog,
//												int id) {
//											// if this button is clicked, close
//											// current activity
//											//login_activity.this.finish();
//										}
//									})
//							.setNegativeButton("No",
//									new DialogInterface.OnClickListener() {
//										public void onClick(DialogInterface dialog,
//												int id) {
//											// if this button is clicked, just close
//											// the dialog box and do nothing
//											dialog.cancel();
//										}
//									});
//					// create alert dialog
//					AlertDialog alertDialog = alertDialogBuilder.create();
//					// show it
//					alertDialog.show();
	 
  
                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
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
    private class createcropBackgroundAsyncTask extends AsyncTask<cropdto, Void, String> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            //use the instance for showing the ProgressBar
//            _progressapisingleton.showProgress(getApplicationContext(), getApplicationContext().getResources().getString(R.string.createcropprogress), true);

            simpleWaitDialog = ProgressDialog.show(login_activity.this,
                    "creating record in datastore...", "excecuting task...");

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(cropdto... _cropdto) {
            // Disk-intensive work. This runs on a background thread.
// Search through a file for the first line that contains "Hello", and return
// that line.
//            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(login_activity.this);
            db.openDataBase();
            db.createcrop(_cropdto[0]);
            db.close();
			
            savecropinsharedprefs(_cropdto[0]);

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

            //simpleWaitDialog.dismiss();

            utilz.getInstance(getApplicationContext()).globalloghandler("record successfully persisted in datastore...", TAG, 1, 1, "login_activity.onPostExecute", "login_activity.onPostExecute");

            final Intent cropslistactivity = new Intent(getApplicationContext(), cropslistactivity.class);
            startActivity(cropslistactivity);

        }

    }

    public void savecropinsharedprefs(cropdto _cropdto) {
        try {
            final String PREFS_FILE = "ntharene_prefs_crops";
            // PREFS_MODE defines which apps can access the file
            final int PREFS_MODE = Context.MODE_PRIVATE;

            SharedPreferences settings = getSharedPreferences(PREFS_FILE, PREFS_MODE);
            SharedPreferences.Editor editor = settings.edit();
            // write string
            editor.putString(_cropdto.getcrop_name(), _cropdto.getcrop_name());
            editor.putString(_cropdto.getcrop_status(), _cropdto.getcrop_status());
            editor.putString(_cropdto.getcreated_date(), _cropdto.getcreated_date());
            // This will asynchronously save the shared preferences without holding the current thread.
            editor.apply();

            //ConnectToDatabase();

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }


    public void ConnectToDatabase() {
        try {

            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            String username = "sa";
            String password = "123456789";
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://127.0.0.1:1433/DATABASE;ntharenedb=" + username + ";password=" + password);

            Log.w("Connection", "open");
            Statement stmt = DbConn.createStatement();
            ResultSet reset = stmt.executeQuery(" select * from tblcrops ");

            DbConn.close();

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
            Log.w("Error connection", "" + ex.getMessage());
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
