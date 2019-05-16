package com.tech.nyax.myapplication10;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class delete_all_activity extends AppCompatActivity {

    private final static String TAG = delete_all_activity.class.getSimpleName();
    ImageButton imgbtndeletecrop;
    TextView lblentityname, lblerrormsg;
    ProgressBar _progressBar;
    // Create DatabasehelperUtilz class object in your activity.
    private DatabasehelperUtilz db;
    private ProgressDialog simpleWaitDialog;
    String _dto_entity_type;
	StringBuilder _string_builder = new StringBuilder();
			

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_all_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this will show the back arrow in the tool bar.

        getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));

        lblentityname = findViewById(R.id.lblentityname);
        lblerrormsg = findViewById(R.id.lblerrormsg);
        _progressBar = findViewById(R.id.progressBar);

        final Bundle _bundle_extras = getIntent().getExtras();

        if (_bundle_extras != null) {

            _dto_entity_type = _bundle_extras.getString(DBContract._dto_entity_type);

            lblentityname.setText("delete all records for entity [ " + _dto_entity_type + " ].");

        }

        //_progressBar.setVisibility(View.GONE);

        imgbtndeletecrop = findViewById(R.id.imgbtndeletecrop);
        imgbtndeletecrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if (_bundle_extras != null) {

                        _dto_entity_type = _bundle_extras.getString(DBContract._dto_entity_type);

                        lblerrormsg.setText("deleting all records for entity [ " + _dto_entity_type + " ].");

                        _progressBar.setVisibility(View.VISIBLE);

                        // Getting complete record details in background thread
                        new deleteallrecordsBackgroundAsyncTask().execute(_dto_entity_type);
                    }

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
	 <Params, Progress, Result>*/
    private class deleteallrecordsBackgroundAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.d(TAG, "onPreExecute");

            //use the instance for showing the ProgressBar
//            _progressapisingleton.showProgress(getApplicationContext(), getApplicationContext().getResources().getString(R.string.editcropprogress), true);

            simpleWaitDialog = ProgressDialog.show(delete_all_activity.this,
                    "purging records from datastore...", "executing task");

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            // Disk-intensive work. This runs on a background thread.
            // Search through a file for the first line that contains "Hello", and return that line.
            // Log.d(TAG, "doInBackground");

            String _dto_entity_type = params[0];

            db = new DatabasehelperUtilz(delete_all_activity.this);
            db.openDataBase();

            //String _strresult = purgeallrecordsfromdatastore(_dto_entity_type);

            StringBuilder _string_builder = new StringBuilder();
            try {
                _string_builder = new StringBuilder();

                switch (_dto_entity_type) {
                    case "crops":
                        List<cropdto> crops_dto_lst = db.getallcrops();
                        for (cropdto _dto : crops_dto_lst) {
                            int _result = db.deletecrop(String.valueOf(_dto.getcrop_Id()));
                            if (_result == 1) {
                                _string_builder.append("successfully deleted crop [ " + _dto.getcrop_name() + " ].\n");
                                publishProgress(_string_builder.toString());
                            } else {
                                _string_builder.append("error deleting crop [ " + _dto.getcrop_name() + " ].\n");
                            }
                        }
                        break;
                    case "cropsvarieties":
                        List<cropvarietydto> cropsvarieties_dto_lst = db.getallcropsvarieties();
                        for (cropvarietydto _dto : cropsvarieties_dto_lst) {
                            int _result = db.deletecropvariety(String.valueOf(_dto.getcropvariety_Id()));
                            if (_result == 1) {
                                _string_builder.append("successfully deleted crop variety [ " + _dto.getcropvariety_name() + " ].\n");
                                publishProgress(_string_builder.toString());
                            } else {
                                _string_builder.append("error deleting crop variety [ " + _dto.getcropvariety_name() + " ].\n");
                            }
                        }
                        break;
                    case "diseasespests":
                        List<cropdiseasedto> diseasespests_dto_lst = db.getallcropsdiseases();
                        for (cropdiseasedto _dto : diseasespests_dto_lst) {
                            int _result = db.deletecropdisease(String.valueOf(_dto.getcropdisease_Id()));
                            if (_result == 1) {
                                _string_builder.append("successfully deleted disease/pest [ " + _dto.getcropdisease_name() + " ].\n");
                                publishProgress(_string_builder.toString());
                            } else {
                                _string_builder.append("error deleting disease/pest [ " + _dto.getcropdisease_name() + " ].\n");
                            }
                        }
                        break;
                    case "manufacturers":
                        List<manufacturerdto> manufacturers_dto_lst = db.getallmanufacturers();
                        for (manufacturerdto _dto : manufacturers_dto_lst) {
                            int _result = db.deletemanufacturer(String.valueOf(_dto.getmanufacturer_Id()));
                            if (_result == 1) {
                                _string_builder.append("successfully deleted manufacturer [ " + _dto.getmanufacturer_name() + " ].\n");
                                publishProgress(_string_builder.toString());
                            } else {
                                _string_builder.append("error deleting manufacturer [ " + _dto.getmanufacturer_name() + " ].\n");
                            }
                        }
                        break;
                    case "pestsinsecticides":
                        List<pestinsecticidedto> pestsinsecticides_dto_lst = db.getallpestsinsecticides();
                        for (pestinsecticidedto _dto : pestsinsecticides_dto_lst) {
                            int _result = db.deletepestinsecticide(String.valueOf(_dto.getpestinsecticide_Id()));
                            if (_result == 1) {
                                _string_builder.append("successfully deleted pesticide/insecticide [ " + _dto.getpestinsecticide_name() + " ].\n");
                                publishProgress(_string_builder.toString());
                            } else {
                                _string_builder.append("error deleting pesticide/insecticide [ " + _dto.getpestinsecticide_name() + " ].\n");
                            }
                        }
                        break;
                    case "settings":
                        List<settingdto> settings_dto_lst = db.getallsettings();
                        for (settingdto _dto : settings_dto_lst) {
                            int _result = db.deletesetting(String.valueOf(_dto.getsetting_Id()));
                            if (_result == 1) {
                                _string_builder.append("successfully deleted setting [ " + _dto.getsetting_name() + " ].\n");
                                publishProgress(_string_builder.toString());
                            } else {
                                _string_builder.append("error deleting setting [ " + _dto.getsetting_name() + " ].\n");
                            }
                        }
                        break;
                    case "categories":
                        List<categorydto> categories_dto_lst = db.getallcategories();
                        for (categorydto _dto : categories_dto_lst) {
                            int _result = db.deletecategory(String.valueOf(_dto.getcategory_Id()));
                            if (_result == 1) {
                                _string_builder.append("successfully deleted category [ " + _dto.getcategory_name() + " ].\n");
                                publishProgress(_string_builder.toString());
                            } else {
                                _string_builder.append("error deleting category [ " + _dto.getcategory_name() + " ].\n");
                            }
                        }
                        break;
                }

            } catch (Exception ex) {
                utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                _string_builder.append(ex.toString());
            }

            db.close();

            return _string_builder.toString();
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            super.onProgressUpdate(progress);
            // Runs on the UI thread after publishProgress is invoked
            Log.d(TAG, "onProgressUpdate");
            Log.d(TAG, "Task = " + progress[0] + " Task Execution Completed");
			
            _string_builder.append(progress[0]);
			
            utilz.getInstance(getApplicationContext()).globalloghandler(_string_builder.toString(), TAG, 1, 1, "delete_all_activity.onProgressUpdate", "delete_all_activity.onProgressUpdate");

            lblerrormsg.setText(_string_builder.toString());

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result(String s) returned from the doInBackground() method.
            // Update UI with the found string.
            Log.d(TAG, "onPostExecute");
            // threadsafetoast.getApp().showToast("onPostExecute");

            simpleWaitDialog.dismiss();

            _progressBar.setVisibility(View.GONE);

            utilz.getInstance(getApplicationContext()).globalloghandler(result, TAG, 1, 1, "delete_all_activity.onPostExecute", "delete_all_activity.onPostExecute");

            lblerrormsg.setText(result);

            try {
                Thread.sleep(3000);
            } catch (Exception ex) {
                utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
            }

            switch (_dto_entity_type) {
                case "crops":

                    final Intent cropslistactivity = new Intent(getApplicationContext(), cropslistactivity.class);
                    startActivity(cropslistactivity);

                    break;
                case "cropsvarieties":

                    final Intent cropsvarietieslistactivity = new Intent(getApplicationContext(), cropsvarietieslistactivity.class);
                    startActivity(cropsvarietieslistactivity);

                    break;
                case "diseasespests":

                    final Intent cropsdiseaseslistactivity = new Intent(getApplicationContext(), cropsdiseaseslistactivity.class);
                    startActivity(cropsdiseaseslistactivity);

                    break;
                case "manufacturers":

                    final Intent manufacturerslistactivity = new Intent(getApplicationContext(), manufacturerslistactivity.class);
                    startActivity(manufacturerslistactivity);

                    break;
                case "pestsinsecticides":

                    final Intent pestsinsecticideslistactivity = new Intent(getApplicationContext(), pestsinsecticideslistactivity.class);
                    startActivity(pestsinsecticideslistactivity);

                    break;
                case "settings":

                    final Intent settingslistactivity = new Intent(getApplicationContext(), settingslistactivity.class);
                    startActivity(settingslistactivity);

                    break;
                case "categories":

                    final Intent categorieslistactivity = new Intent(getApplicationContext(), categorieslistactivity.class);
                    startActivity(categorieslistactivity);

                    break;
            }

        }

    }

    String purgeallrecordsfromdatastore(String _dto_entity_type) {
        StringBuilder _string_builder = new StringBuilder();
        try {
            _string_builder = new StringBuilder();

            switch (_dto_entity_type) {
                case "crops":
                    List<cropdto> crops_dto_lst = db.getallcrops();
                    for (cropdto _dto : crops_dto_lst) {
                        int _result = db.deletecrop(String.valueOf(_dto.getcrop_Id()));
                        if (_result == 1) {
                            _string_builder.append("successfully deleted crop [ " + _dto.getcrop_name() + " ].\n");
                        } else {
                            _string_builder.append("error deleting crop [ " + _dto.getcrop_name() + " ].\n");
                        }
                    }
                    break;
                case "cropsvarieties":
                    List<cropvarietydto> cropsvarieties_dto_lst = db.getallcropsvarieties();
                    for (cropvarietydto _dto : cropsvarieties_dto_lst) {
                        int _result = db.deletecropvariety(String.valueOf(_dto.getcropvariety_Id()));
                        if (_result == 1) {
                            _string_builder.append("successfully deleted crop variety [ " + _dto.getcropvariety_name() + " ].\n");
                        } else {
                            _string_builder.append("error deleting crop variety [ " + _dto.getcropvariety_name() + " ].\n");
                        }
                    }
                    break;
                case "diseasespests":
                    List<cropdiseasedto> diseasespests_dto_lst = db.getallcropsdiseases();
                    for (cropdiseasedto _dto : diseasespests_dto_lst) {
                        int _result = db.deletecropdisease(String.valueOf(_dto.getcropdisease_Id()));
                        if (_result == 1) {
                            _string_builder.append("successfully deleted disease/pest [ " + _dto.getcropdisease_name() + " ].\n");
                        } else {
                            _string_builder.append("error deleting disease/pest [ " + _dto.getcropdisease_name() + " ].\n");
                        }
                    }
                    break;
                case "manufacturers":
                    List<manufacturerdto> manufacturers_dto_lst = db.getallmanufacturers();
                    for (manufacturerdto _dto : manufacturers_dto_lst) {
                        int _result = db.deletemanufacturer(String.valueOf(_dto.getmanufacturer_Id()));
                        if (_result == 1) {
                            _string_builder.append("successfully deleted manufacturer [ " + _dto.getmanufacturer_name() + " ].\n");
                        } else {
                            _string_builder.append("error deleting manufacturer [ " + _dto.getmanufacturer_name() + " ].\n");
                        }
                    }
                    break;
                case "pestsinsecticides":
                    List<pestinsecticidedto> pestsinsecticides_dto_lst = db.getallpestsinsecticides();
                    for (pestinsecticidedto _dto : pestsinsecticides_dto_lst) {
                        int _result = db.deletepestinsecticide(String.valueOf(_dto.getpestinsecticide_Id()));
                        if (_result == 1) {
                            _string_builder.append("successfully deleted pesticide/insecticide [ " + _dto.getpestinsecticide_name() + " ].\n");
                        } else {
                            _string_builder.append("error deleting pesticide/insecticide [ " + _dto.getpestinsecticide_name() + " ].\n");
                        }
                    }
                    break;
                case "settings":
                    List<settingdto> settings_dto_lst = db.getallsettings();
                    for (settingdto _dto : settings_dto_lst) {
                        int _result = db.deletesetting(String.valueOf(_dto.getsetting_Id()));
                        if (_result == 1) {
                            _string_builder.append("successfully deleted setting [ " + _dto.getsetting_name() + " ].\n");
                        } else {
                            _string_builder.append("error deleting setting [ " + _dto.getsetting_name() + " ].\n");
                        }
                    }
                    break;
                case "categories":
                    List<categorydto> categories_dto_lst = db.getallcategories();
                    for (categorydto _dto : categories_dto_lst) {
                        int _result = db.deletecategory(String.valueOf(_dto.getcategory_Id()));
                        if (_result == 1) {
                            _string_builder.append("successfully deleted category [ " + _dto.getcategory_name() + " ].\n");
                        } else {
                            _string_builder.append("error deleting category [ " + _dto.getcategory_name() + " ].\n");
                        }
                    }
                    break;
            }

            return _string_builder.toString();
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
            _string_builder.append(ex.toString());
            return _string_builder.toString();
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
