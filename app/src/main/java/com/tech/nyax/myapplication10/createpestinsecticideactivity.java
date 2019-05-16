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

public class createpestinsecticideactivity extends AppCompatActivity {

    private final static String TAG = createpestinsecticideactivity.class.getSimpleName();
    Button btncreatepestinsecticide;
    ImageButton imgbtncreatepestinsecticide;
    TextView txtcreatepestinsecticidename, lblerrormsg;
    Spinner cbodiseasepest, cbocreatestatus, cbopestinsecticidecategory, cbopestinsecticidemanufacturer;
    List<diseasepestdto> lstcropdiseasedtos;
    diseasepestdto _selecteddiseasepest;
    pestinsecticidecategorydto _selected_pestinsecticidecategorydto;
    pestinsecticidecategoryspinneradapter _pestinsecticidecategoryspinneradapter;
    statusdto _selected_statusdto;
    statusspinneradapter _statusspinneradapter;
    List<manufacturerdto> lstmanufacturerdtos;
    manufacturerdto _selectedmanufacturer;
    manufacturerspinneradapter _manufacturerspinneradapter;
    categorydto _selected_categorydto;
    // Create DatabasehelperUtilz class object in your activity.
    private DatabasehelperUtilz db;
    private ProgressDialog simpleWaitDialog;
    pestinsecticidedto _pestinsecticidedto = new pestinsecticidedto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_pestinsecticide_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this will show the back arrow in the tool bar.

        getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));

        txtcreatepestinsecticidename = findViewById(R.id.txtcreatepestinsecticidename);
        lblerrormsg = findViewById(R.id.lblerrormsg);

        imgbtncreatepestinsecticide = findViewById(R.id.imgbtncreatepestinsecticide);
        imgbtncreatepestinsecticide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    lblerrormsg.setText("");
                    String strmsg = "";
                    if (txtcreatepestinsecticidename.getText() == null || txtcreatepestinsecticidename.getText().length() == 0) {
                        strmsg += "name cannot be null.";
                    }
                    if (_selected_categorydto == null) {
                        strmsg += "\nselect category.";
                    } else {
                        if (_selected_categorydto.getcategory_name() == null || _selected_categorydto.getcategory_name().length() == 0) {
                            strmsg += "\nselect category.";
                        }
                    }
                    if (_selecteddiseasepest == null) {
                        strmsg += "\nselect disease/pest.";
                    } else {
                        if (_selecteddiseasepest.getdto_value() == null || _selecteddiseasepest.getdto_value().length() == 0) {
                            strmsg += "\nselect disease/pest.";
                        }
                    }
                    if (_selectedmanufacturer == null) {
                        strmsg += "\nselect manufacturer.";
                    } else {
                        if (_selectedmanufacturer.getmanufacturer_name() == null || _selectedmanufacturer.getmanufacturer_name().length() == 0) {
                            strmsg += "\nselect manufacturer.";
                        }
                    }
                    if (_selected_statusdto.getstatus_value() == null || _selected_statusdto.getstatus_value().length() == 0) {
                        strmsg += "\nselect status.";
                    }
                    if (strmsg.length() > 0) {
                        utilz.getInstance(getApplicationContext()).globalloghandler(strmsg, TAG, 1, 0);
                        lblerrormsg.setText(strmsg);
                        return;
                    }

                    String timeStamp = utilz.getInstance(getApplicationContext()).getcurrenttimestamp();

                    _pestinsecticidedto.setpestinsecticide_name(txtcreatepestinsecticidename.getText().toString());
                    _pestinsecticidedto.setpestinsecticide_category(_selected_categorydto.getcategory_name());
                    _pestinsecticidedto.setpestinsecticide_crop_disease_id(String.valueOf(_selecteddiseasepest.getdto_id()));
                    _pestinsecticidedto.setpestinsecticide_manufacturer_id(String.valueOf(_selectedmanufacturer.getmanufacturer_Id()));
                    _pestinsecticidedto.setpestinsecticide_status(_selected_statusdto.getstatus_value());
                    _pestinsecticidedto.setcreated_date(timeStamp);

                    //new createrecordBackgroundAsyncTask().execute(_pestinsecticidedto);

                    new checkifrecordexistsBackgroundAsyncTask().execute(_pestinsecticidedto.getpestinsecticide_name());

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });

        /* List<pestinsecticidecategorydto> _lstpestinsecticidecategories = new ArrayList<>();
        _lstpestinsecticidecategories.add(new pestinsecticidecategorydto(1L, "INSECTICIDES", "INSECTICIDES"));
        _lstpestinsecticidecategories.add(new pestinsecticidecategorydto(2L, "FUNGICIDES", "FUNGICIDES"));
		_lstpestinsecticidecategories.add(new pestinsecticidecategorydto(3L, "MITICIDES", "MITICIDES"));
		_lstpestinsecticidecategories.add(new pestinsecticidecategorydto(4L, "HERBICIDES", "HERBICIDES"));
		_lstpestinsecticidecategories.add(new pestinsecticidecategorydto(5L, "SOIL SOLUTIONS", "SOIL SOLUTIONS"));
		_lstpestinsecticidecategories.add(new pestinsecticidecategorydto(6L, "FOLIAR FERTILIZER", "FOLIAR FERTILIZER"));

         _pestinsecticidecategoryspinneradapter = new pestinsecticidecategoryspinneradapter(getApplicationContext(), _lstpestinsecticidecategories);
		
		cbopestinsecticidecategory = findViewById(R.id.cbopestinsecticidecategory); 
        cbopestinsecticidecategory.setAdapter(_pestinsecticidecategoryspinneradapter);
        
        cbopestinsecticidecategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _selected_pestinsecticidecategorydto = (pestinsecticidecategorydto) parent.getItemAtPosition(position);
				
                utilz.getInstance(getApplicationContext()).globalloghandler(_selected_pestinsecticidecategorydto.getdto_value(), TAG, 1, 1);
               
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        }); */

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

        new getallactivecategoriesrecordsBackgroundAsyncTask().execute();

        new getallactivecropsdiseasesrecordsBackgroundAsyncTask().execute();

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
    private class createrecordBackgroundAsyncTask extends AsyncTask<pestinsecticidedto, Void, String> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            //use the instance for showing the ProgressBar
//            _progressapisingleton.showProgress(getApplicationContext(), getApplicationContext().getResources().getString(R.string.createpestinsecticideprogress), true);

            simpleWaitDialog = ProgressDialog.show(createpestinsecticideactivity.this,
                    "creating record in datastore...", "excecuting task...");

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(pestinsecticidedto... _pestinsecticidedto) {
            // Disk-intensive work. This runs on a background thread.
// Search through a file for the first line that contains "Hello", and return
// that line.
//            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(createpestinsecticideactivity.this);
            db.openDataBase();
            db.createpestinsecticide(_pestinsecticidedto[0]);
            db.close();

            savepestinsecticideinsharedprefs(_pestinsecticidedto[0]);

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

            StringBuilder _result = new StringBuilder();

            _result.append("name [ " + _pestinsecticidedto.getpestinsecticide_name() + " ].\n");

            _result.append("category [ " + _pestinsecticidedto.getpestinsecticide_category() + " ].\n");

            _result.append("disease/pest [ " + _pestinsecticidedto.getpestinsecticide_crop_disease_id() + " ].\n");

            _result.append("manufacturer [ " + _pestinsecticidedto.getpestinsecticide_manufacturer_id() + " ].\n");

            _result.append("status [ " + _pestinsecticidedto.getpestinsecticide_status() + " ].\n");

            _result.append("created date [ " + _pestinsecticidedto.getcreated_date() + " ].\n");

            utilz.getInstance(getApplicationContext()).globalloghandler("record successfully persisted in datastore...\n" + _result.toString(), TAG, 1, 1, "createpestinsecticideactivity.onPostExecute", "createpestinsecticideactivity.onPostExecute");

            final Intent pestsinsecticideslistactivity = new Intent(getApplicationContext(), pestsinsecticideslistactivity.class);
            startActivity(pestsinsecticideslistactivity);

        }

    }

    /* Defined as <Params, Progress, Result>*/
    private class checkifrecordexistsBackgroundAsyncTask extends AsyncTask<String, Void, List<pestinsecticidedto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            //showing the ProgressBar  
            simpleWaitDialog = ProgressDialog.show(createpestinsecticideactivity.this, "checking if record exists in datastore...", "excecuting task...");

            super.onPreExecute();
        }

        @Override
        protected List<pestinsecticidedto> doInBackground(String... param) {
            // Disk-intensive work. This runs on a background thread.
// Search through a file for the first line that contains "Hello", and return
// that line.
            Log.e(TAG, "doInBackground");

            String _dto_name = param[0];
            Log.e(TAG, "_dto_name =  " + _dto_name);

            db = new DatabasehelperUtilz(createpestinsecticideactivity.this);
            db.openDataBase();
            List<pestinsecticidedto> _lstdtos = db.filterpestsinsecticidesgivenname(param[0]);
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
        protected void onPostExecute(List<pestinsecticidedto> result) {
            super.onPostExecute(result);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result(String s) returned from the doInBackground() method.
            // Update UI with the found string.
            Log.e(TAG, "onPostExecute");

            if (result.size() > 0) {

                simpleWaitDialog.dismiss();

                utilz.getInstance(getApplicationContext()).globalloghandler("record with name [ " + result.get(0).getpestinsecticide_name() + " ] exists in datastore...", TAG, 1, 0, "createpestinsecticideactivity.onPostExecute", "createpestinsecticideactivity.onPostExecute");

            } else {

                simpleWaitDialog.dismiss();

                Log.e(TAG, "record with name " + _pestinsecticidedto.getpestinsecticide_name() + " does not exist in datastore.");

                new createrecordBackgroundAsyncTask().execute(_pestinsecticidedto);

            }

        }

    }

    public void savepestinsecticideinsharedprefs(pestinsecticidedto _pestinsecticidedto) {
        try {
            final String PREFS_FILE = "ntharene_prefs_pesticidesinsecticides";
            // PREFS_MODE defines which apps can access the file
            final int PREFS_MODE = Context.MODE_PRIVATE;

            SharedPreferences settings = getSharedPreferences(PREFS_FILE, PREFS_MODE);
            SharedPreferences.Editor editor = settings.edit();
            // write string
            editor.putString(_pestinsecticidedto.getpestinsecticide_name(), _pestinsecticidedto.getpestinsecticide_name());
            editor.putString(_pestinsecticidedto.getpestinsecticide_category(), _pestinsecticidedto.getpestinsecticide_category());
            editor.putString(_pestinsecticidedto.getpestinsecticide_status(), _pestinsecticidedto.getpestinsecticide_status());
            editor.putString(_pestinsecticidedto.getcreated_date(), _pestinsecticidedto.getcreated_date());
            editor.putString(_pestinsecticidedto.getpestinsecticide_crop_disease_id(), _pestinsecticidedto.getpestinsecticide_crop_disease_id());
            editor.putString(_pestinsecticidedto.getpestinsecticide_manufacturer_id(), _pestinsecticidedto.getpestinsecticide_manufacturer_id());
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
    private class getallactivecropsdiseasesrecordsBackgroundAsyncTask extends AsyncTask<Void, Void, List<cropdiseasedto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.d(TAG, "onPreExecute");

            //use the instance for showing the ProgressBar
//            _progressapisingleton.showProgress(getApplicationContext(), getApplicationContext().getResources().getString(R.string.editcropprogress), true);

            //simpleWaitDialog = ProgressDialog.show(createcropdiseaseactivity.this, "fetching record from datastore...", "executing task...");

            super.onPreExecute();
        }

        @Override
        protected List<cropdiseasedto> doInBackground(Void... param) {
            // Disk-intensive work. This runs on a background thread.
            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(getApplicationContext());
            db.openDataBase();
            List<cropdiseasedto> lstcropdiseasedtos = db.getallcropsdiseases();
            db.close();

            Collections.reverse(lstcropdiseasedtos);

            return lstcropdiseasedtos;
        }

        @Override
        protected void onProgressUpdate(Void... p) {
            super.onProgressUpdate(p);
            // Runs on the UI thread after publishProgress is invoked
            Log.d(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(final List<cropdiseasedto> lstcropdiseasedtos) {
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

                    List<diseasepestdto> _lstdiseasespestsdto = convertcropdiseasedtointospinnerdto(lstcropdiseasedtos);

                    int _count = _lstdiseasespestsdto.size();
                    Log.e(TAG, "diseasespests count [ " + _count + " ]");

                    diseasepestspinneradapter _diseasepestspinneradapter = new diseasepestspinneradapter(getApplicationContext(), _lstdiseasespestsdto);

                    cbodiseasepest = findViewById(R.id.cbodiseasepest);

                    cbodiseasepest.setAdapter(_diseasepestspinneradapter);

                    cbodiseasepest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            _selecteddiseasepest = (diseasepestdto) parent.getItemAtPosition(position);

                            utilz.getInstance(getApplicationContext()).globalloghandler(_selecteddiseasepest.getdto_value(), TAG, 1, 1);

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

            //simpleWaitDialog = ProgressDialog.show(createcropdiseaseactivity.this, "fetching record from datastore...", "executing task...");

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

                    cbopestinsecticidemanufacturer = findViewById(R.id.cbopestinsecticidemanufacturer);

                    cbopestinsecticidemanufacturer.setAdapter(_manufacturerspinneradapter);

                    cbopestinsecticidemanufacturer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    List<diseasepestdto> convertcropdiseasedtointospinnerdto(List<cropdiseasedto> _lstcropdiseasespests) {
        List<diseasepestdto> _lstdiseasespestsdto = new ArrayList<>();

        for (cropdiseasedto dto : _lstcropdiseasespests) {
            diseasepestdto _dto = new diseasepestdto();
            _dto.setdto_id(dto.getcropdisease_Id());
            _dto.setdto_key(dto.getcropdisease_name());
            _dto.setdto_value(dto.getcropdisease_name());
            _lstdiseasespestsdto.add(_dto);
        }

        return _lstdiseasespestsdto;
    }

    List<manufacturer_spinner_dto> convertmanufacturerdtointospinnerdto(List<manufacturerdto> _lstmanufacturers) {
        List<manufacturer_spinner_dto> _lstmanufacturer_spinner_dto = new ArrayList<>();

        for (manufacturerdto dto : _lstmanufacturers) {
            manufacturer_spinner_dto _dto = new manufacturer_spinner_dto();
            _dto.setdto_id(dto.getmanufacturer_Id());
            _dto.setdto_key(dto.getmanufacturer_name());
            _dto.setdto_value(dto.getmanufacturer_name());
            _lstmanufacturer_spinner_dto.add(_dto);
        }

        return _lstmanufacturer_spinner_dto;
    }

    /* Defined as <Params, Progress, Result> */
    private class getallactivecategoriesrecordsBackgroundAsyncTask extends AsyncTask<Void, Void, List<categorydto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.d(TAG, "onPreExecute");

            //use the instance for showing the ProgressBar
//            _progressapisingleton.showProgress(getApplicationContext(), getApplicationContext().getResources().getString(R.string.editcropprogress), true);

            //simpleWaitDialog = ProgressDialog.show(createcropdiseaseactivity.this, "fetching record from datastore...", "executing task...");

            super.onPreExecute();
        }

        @Override
        protected List<categorydto> doInBackground(Void... param) {
            // Disk-intensive work. This runs on a background thread.
            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(getApplicationContext());
            db.openDataBase();
            List<categorydto> lstcategoriesdtos = db.getallcategories();
            db.close();

            Collections.reverse(lstcategoriesdtos);

            return lstcategoriesdtos;
        }

        @Override
        protected void onProgressUpdate(Void... p) {
            super.onProgressUpdate(p);
            // Runs on the UI thread after publishProgress is invoked
            Log.d(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(final List<categorydto> lstcategoriesdtos) {
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

                    int _count = lstcategoriesdtos.size();
                    Log.e(TAG, "categories count [ " + _count + " ]");

                    categoriespinneradapter _categoriespinneradapter = new categoriespinneradapter(getApplicationContext(), lstcategoriesdtos);

                    cbopestinsecticidecategory = findViewById(R.id.cbopestinsecticidecategory);

                    cbopestinsecticidecategory.setAdapter(_categoriespinneradapter);

                    cbopestinsecticidecategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            _selected_categorydto = (categorydto) parent.getItemAtPosition(position);

                            utilz.getInstance(getApplicationContext()).globalloghandler(_selected_categorydto.getcategory_name(), TAG, 1, 1);

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
