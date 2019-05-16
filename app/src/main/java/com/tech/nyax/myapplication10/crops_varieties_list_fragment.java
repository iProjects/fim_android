package com.tech.nyax.myapplication10;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class crops_varieties_list_fragment extends Fragment {

    private final static String TAG = crops_varieties_list_fragment.class.getSimpleName();
    public static final int REQUEST_CODE = 11;
    public static final int RESULT_CODE = 12;
    // Our identifier for obtaining the name from arguments
    public static final String CROPS_FRAGMENT_TAG = "crops_varieties_list_fragment";
    public static final String CROP_NAME_TAG = "crop_name";
    String crop_name;
    private DatabasehelperUtilz db;
    List<cropdto> lstcropdtos;
    ListView cropslistview;
    cropslistadapter _cropslistadapter;
    AutoCompleteTextView txtautocompletecrop;
    cropsautocompleteadapter _cropsautocompleteadapter;
    private cropdto _selectedcrop;

    // Required
    public crops_varieties_list_fragment() {

    }

    // The static constructor. This is the only way that you should instantiate
// the fragment yourself
    public static crops_varieties_list_fragment newInstance() {
        return new crops_varieties_list_fragment();
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        final Bundle arguments = getArguments();

        try {

            if (arguments == null) {

                utilz.getInstance(getActivity().getApplicationContext()).globalloghandler("arguments cannot be null.", TAG, 1, 0);

            } else if (!arguments.containsKey(CROP_NAME_TAG)) {

                utilz.getInstance(getActivity().getApplicationContext()).globalloghandler("arguments missing key [ " + CROP_NAME_TAG + " ].", TAG, 1, 0);

            } else {
                crop_name = arguments.getString(CROP_NAME_TAG);
            }

            if (crop_name == null) {

                new fetchallBackgroundAsyncTask().execute();

            } else if (crop_name != null) {

                new filterBackgroundAsyncTask().execute(crop_name);

            } else {

                new fetchallBackgroundAsyncTask().execute();
            }

        } catch (Exception ex) {
            utilz.getInstance(getActivity().getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    /*When defining an AsyncTask we can pass three types between < > brackets.
    Defined as <Params, Progress, Result>*/
    private class fetchallBackgroundAsyncTask extends AsyncTask<Void, Void, List<cropdto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            // showing the ProgressBar 
            // simpleWaitDialog = ProgressDialog.show(cropslistactivity.this,
            // "loading crops from datastore...", "");
            // simpleWaitDialog.setCancelable(true);

            lstcropdtos = new ArrayList<>();
            _cropslistadapter = new cropslistadapter(getActivity().getApplicationContext(), lstcropdtos);
            _cropslistadapter.notifyDataSetChanged();

            super.onPreExecute();
        }

        @Override
        protected List<cropdto> doInBackground(Void... param) {
            // Disk-intensive work. This runs on a background thread. 
            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(getActivity().getApplicationContext());
            db.openDataBase();
            lstcropdtos = db.getallcrops();
            _cropslistadapter.notifyDataSetChanged();
            db.close();

			if(null == lstcropdtos){
				List<cropdto> _lstcropdtos =  new ArrayList<>();
				return _lstcropdtos;
			}
			
            Collections.reverse(lstcropdtos);

            return lstcropdtos;
        }

        @Override
        protected void onProgressUpdate(Void... param) {
            super.onProgressUpdate(param);
            // Runs on the UI thread after publishProgress is invoked
            Log.e(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(final List<cropdto> lstcropdtos) {
//            super.onPostExecute(result);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result returned from the doInBackground() method.
            // Update UI with the response.
            Log.e(TAG, "onPostExecute");

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating data into ListView
                     * */
                    _cropslistadapter = new cropslistadapter(getActivity().getApplicationContext(), lstcropdtos);
                    cropslistview = getActivity().findViewById(R.id.lstcrops);
                    cropslistview.setAdapter(_cropslistadapter);
                    _cropslistadapter.notifyDataSetChanged();

                    int _count = lstcropdtos.size();
                    Log.e(TAG, "crop count [ " + _count + " ]");

                }
            });


        }

    }


    /*When defining an AsyncTask we can pass three types between < > brackets.
    Defined as <Params, Progress, Result>*/
    private class filterBackgroundAsyncTask extends AsyncTask<String, Void, List<cropdto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            //showing the ProgressBar 
            // simpleWaitDialog = ProgressDialog.show(cropslistactivity.this,
            // "loading crops from datastore...", "");
            // simpleWaitDialog.setCancelable(true);

            lstcropdtos = new ArrayList<>();
            _cropslistadapter = new cropslistadapter(getActivity().getApplicationContext(), lstcropdtos);
            _cropslistadapter.notifyDataSetChanged();

            super.onPreExecute();
        }

        @Override
        protected List<cropdto> doInBackground(String... _searchterm) {
            // Disk-intensive work. This runs on a background thread. 
            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(getActivity().getApplicationContext());
            db.openDataBase();
            lstcropdtos = db.filtercropsgivenname(_searchterm[0]);
            _cropslistadapter.notifyDataSetChanged();
            db.close();

			if(null == lstcropdtos){
				List<cropdto> _lstcropdtos =  new ArrayList<>();
				return _lstcropdtos;
			}
			
            Log.e(TAG, String.valueOf(lstcropdtos.size()));

            Collections.reverse(lstcropdtos);

            return lstcropdtos;
        }

        @Override
        protected void onProgressUpdate(Void... param) {
            super.onProgressUpdate(param);
            // Runs on the UI thread after publishProgress is invoked
            Log.e(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(final List<cropdto> lstcropdtos) {
//            super.onPostExecute(result);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result returned from the doInBackground() method.
            // Update UI with the response.
            Log.e(TAG, "onPostExecute");

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating data into ListView
                     * */
                    _cropslistadapter = new cropslistadapter(getActivity().getApplicationContext(), lstcropdtos);
                    cropslistview = getActivity().findViewById(R.id.lstcrops);
                    cropslistview.setAdapter(_cropslistadapter);
                    _cropslistadapter.notifyDataSetChanged();

                    int _count = lstcropdtos.size();
                    Log.e(TAG, "crop count [ " + _count + " ]");

                }
            });


        }

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //final Bundle arguments = getArguments();

        /* try {

            if (arguments == null || !arguments.containsKey(CROP_NAME_TAG)) {
                // Set a default or error as you see fit
            } else {
                crop_name = arguments.getString(CROP_NAME_TAG);
            }

            if (crop_name.isEmpty()) {

                new fetchallBackgroundAsyncTask().execute();

            } else {

                new filterBackgroundAsyncTask().execute(crop_name);
            }
        } catch (Exception ex) {
            utilz.getInstance(getActivity().getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        } */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.crops_varieties_list_fragment, container, false);
    }

    public void replaceFragment(Fragment fragment, String tag) {
//Get current fragment placed in container
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.container);
//Prevent adding same fragment on top
        /*if (currentFragment.getClass() == fragment.getClass()) {
            return;
        }*/
//If fragment is already on stack, we can pop back stack to prevent stack infinite growth
        if (getActivity().getSupportFragmentManager().findFragmentByTag(tag) != null) {
            getActivity().getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
//Otherwise, just replace fragment
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(tag)
                .add(R.id.container, fragment, tag)
                .commit();
    }

    // Initializing and starting the second fragment
    public void lauchfragmenttwo() {
        getActivity().
                getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(CROPS_FRAGMENT_TAG)
                .replace(R.id.container, FragmentTwo.newInstance(""), CROPS_FRAGMENT_TAG)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            String testResult = data.getStringExtra(CROP_NAME_TAG);
// TODO: Do something with your extra data
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
///Override defining menu resource
        inflater.inflate(R.menu.menu_resource_id, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
//Override for preparing items (setting visibility, change text, change icon...)
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//Override it for handling items
        int menuItemId = item.getItemId();
        switch (menuItemId) {
            case R.id.first_item_id:
                return true; //return true, if is handled
            default:
                return super.onOptionsItemSelected(item);
        }
//        return super.onOptionsItemSelected(item);
    }


}
