package com.tech.nyax.myapplication10;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class preference_activity extends AppCompatActivity {

    private final static String TAG = preference_activity.class.getSimpleName();
    private static final String PREFS_FILE = "ntharene_preference";
    // reusable string object
    private static StringBuilder _string_builder = new StringBuilder();
    // Create DatabasehelperUtilz class object in your activity.
    private DatabasehelperUtilz db;
    private ProgressDialog simpleWaitDialog;
    settingdto _settingdto = new settingdto();
    Map<String, ?> allpreferences;
    Map<String, ?> allEntries;
    SharedPreferences preferences;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_layout);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingsFragment()).commit();
 
        getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));

        //set the default values we defined in the XML
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //get the values of the settings options
        // boolean silentMode = preferences.getBoolean("silent_mode", false);
        // boolean awesomeMode = preferences.getBoolean("awesome_mode", false);
        // String customStorage = preferences.getString("custom_storage", "");

        pref = getApplicationContext().getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        // editor.putBoolean("silentMode", silentMode); // Saving boolean - true/false
        // editor.putBoolean("awesomeMode", awesomeMode); // Saving boolean - true/false
        // editor.putString("customStorage", customStorage); // Saving string Save the changes in SharedPreferences
        // editor.putInt("integer", 10); // Saving integer
        // editor.putFloat("float", 10.1f); // Saving float
        // editor.putLong("long", 1000); // Saving long

        _string_builder = new StringBuilder();

        allpreferences = preferences.getAll();
        for (Map.Entry<String, ?> entry : allpreferences.entrySet()) {
            final String key = entry.getKey();
            final Object value = entry.getValue();

            editor.putString(key, value.toString()); // Saving string Save the changes in SharedPreferences

            // _string_builder.append("key [ " + key + " ]\n");
            // _string_builder.append("value [ " + value.toString() + " ]\n");
            // Log.d(TAG, _string_builder.toString());

        }

        // This will asynchronously save the s hared preferences without holding the current thread.
        editor.apply(); // commit changes

        new checkifrecordexistsBackgroundAsyncTask().execute(allpreferences);

        _string_builder = new StringBuilder();

        allEntries = pref.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            final String key = entry.getKey();
            final Object value = entry.getValue();
			
            // _string_builder.append("key [ " + key + " ]\n");
            // _string_builder.append("value [ " + value.toString() + " ]\n");
            // Log.d(TAG, _string_builder.toString());
        }

    }

    /**
     * Fragment for settings.
     */
    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }

    /* Defined as <Params, Progress, Result>*/
    public class checkifrecordexistsBackgroundAsyncTask extends AsyncTask<Map<String, ?>, Void, Map<String, List<settingdto>>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            //showing the ProgressBar  
            simpleWaitDialog = ProgressDialog.show(preference_activity.this, "checking if record exists in datastore...", "excecuting task...");
            simpleWaitDialog.setCancelable(true);

            super.onPreExecute();
        }

        @Override
        protected Map<String, List<settingdto>> doInBackground(Map<String, ?>... allpreferences) {
            // Disk-intensive work. This runs on a background thread.
// Search through a file for the first line that contains "Hello", and return
// that line.
            Log.e(TAG, "doInBackground");

            _string_builder = new StringBuilder();

            Map<String, List<settingdto>> _lst_map_dtos = new HashMap<String, List<settingdto>>();

            db = new DatabasehelperUtilz(preference_activity.this);
            db.openDataBase();

            for (Map.Entry<String, ?> entry : allpreferences[0].entrySet()) {

                final String key = entry.getKey();
                final Object value = entry.getValue();

                // _string_builder.append("key [ " + key + " ]\n");
                // _string_builder.append("value [ " + value.toString() + " ]\n");
                // Log.d(TAG, _string_builder.toString());

                List<settingdto> _lst_existing_dtos = db.filtersettingsgivenname(key);

                _lst_map_dtos.put(key, _lst_existing_dtos);

            }

            db.close();
            return _lst_map_dtos;
        }

        @Override
        protected void onProgressUpdate(Void... p) {
            super.onProgressUpdate(p);
            // Runs on the UI thread after publishProgress is invoked
            Log.e(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(Map<String, List<settingdto>> result) {
            super.onPostExecute(result);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result(String s) returned from the doInBackground() method.
            // Update UI with the found string.
            Log.e(TAG, "onPostExecute");

            for (Map.Entry<String, List<settingdto>> entry : result.entrySet()) {

                final String _dto_key = entry.getKey();
                final List<settingdto> _lst_existing_dtos = entry.getValue();

                _string_builder = new StringBuilder();

                _string_builder.append("key [ " + _dto_key + " ]\n");

                allpreferences = preferences.getAll();
				
                for (Map.Entry<String, ?> preference : allpreferences.entrySet()) {
					
                    final String _key = preference.getKey();
					
                    if (_key.equals(_dto_key)) {
						
                        final Object value = preference.getValue();

                        _string_builder.append("value [ " + value.toString() + " ]\n");
                        Log.d(TAG, _string_builder.toString());

                        String timeStamp = utilz.getInstance(getApplicationContext()).getcurrenttimestamp();

                        _settingdto.setsetting_name(_dto_key);
                        _settingdto.setsetting_value(value.toString());
                        _settingdto.setsetting_status("active");
                        _settingdto.setcreated_date(timeStamp);

                        if (_lst_existing_dtos.size() > 0) {

                            utilz.getInstance(getApplicationContext()).globalloghandler("setting with name [ " + _dto_key + " ] exists in datastore...", TAG, 1, 0);

                            //simpleWaitDialog.dismiss();

                        } else {

                            utilz.getInstance(getApplicationContext()).globalloghandler("setting with name " + _settingdto.getsetting_name() + " does not exist in datastore.\n creating setting...", TAG, 1, 1);

                            new createrecordBackgroundAsyncTask().execute(_settingdto);

                            //simpleWaitDialog.dismiss();

                        }
                    }

                }

                try {
					
                    //simpleWaitDialog.dismiss();
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {

                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.getMessage(), TAG, 1, 1);

                }

            }
			
            simpleWaitDialog.dismiss();

        }

    }

    /* Defined as <Params, Progress, Result>*/
    public class createrecordBackgroundAsyncTask extends AsyncTask<settingdto, Void, String> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            utilz.getInstance(getApplicationContext()).globalloghandler("creating record...", TAG, 1, 1);

            //use the instance for showing the ProgressBar
//            _progressapisingleton.showProgress(getApplicationContext(), getApplicationContext().getResources().getString(R.string.createsettingprogress), true);

            simpleWaitDialog = ProgressDialog.show(preference_activity.this, "creating record in datastore...", "excecuting task...");
            simpleWaitDialog.setCancelable(true);

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(settingdto... _settingdto) {
            // Disk-intensive work. This runs on a background thread.
// Search through a file for the first line that contains "Hello", and return
// that line.
//            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(preference_activity.this);
            db.openDataBase();
            db.createsetting(_settingdto[0]);
            db.close();

            return _settingdto[0].getsetting_name();
        }

        @Override
        protected void onProgressUpdate(Void... p) {
            super.onProgressUpdate(p);
            // Runs on the UI thread after publishProgress is invoked
            Log.e(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result(String s) returned from the doInBackground() method.
            // Update UI with the found string.
            Log.e(TAG, "onPostExecute");
            // threadsafetoast.getApp().showToast("onPostExecute");

            utilz.getInstance(getApplicationContext()).globalloghandler("setting [ " + result + " ] successfully persisted in datastore...", TAG, 1, 1);

            simpleWaitDialog.dismiss();

            // final Intent settingslistactivity = new Intent(getApplicationContext(), settingslistactivity.class);
            // startActivity(settingslistactivity);

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
