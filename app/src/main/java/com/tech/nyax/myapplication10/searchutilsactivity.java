package com.tech.nyax.myapplication10;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.ProgressBar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class searchutilsactivity extends AppCompatActivity {

    private final static String TAG = searchutilsactivity.class.getSimpleName();
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private SearchView.OnClickListener clickListener;
    private SearchView.OnCloseListener closeListener;

    List<cropdto> lstresultsdtos;
    List<cropdto> lstautocompletecropdtos;
    cropdto _selectedcrop;
    cropsautocompleteadapter _cropsautocompleteadapter;
    cropslistadapter _cropslistadapter;

    AutoCompleteTextView txtautocompleterecord;
    searchautocompleteadapter _searchautocompleteadapter;
    searchautocompletedto _selected_searchautocompletedto;
    List<searchautocompletedto> _lstsearchautocompletedtos;

    Spinner cbosearchrecord;
    searchspinneradapter _searchspinneradapter;
    searchspinnerdto _selected_searchspinnerdto;

    // Create DatabasehelperUtilz class object in your activity.
    private DatabasehelperUtilz db;
    private ProgressDialog simpleWaitDialog;
	private ProgressBar progressbarsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_utils_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this will show the back arrow in the tool bar.

        getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));

        getSupportActionBar().setIcon(R.drawable.delete);
        getSupportActionBar().setLogo(R.drawable.edit);

        try {

            List<searchspinnerdto> _lstsearchspinnerdto = new ArrayList<>();
            _lstsearchspinnerdto.add(new searchspinnerdto(1L, "crop", "crop"));
            _lstsearchspinnerdto.add(new searchspinnerdto(2L, "diseasepest", "disease/pest"));

            searchspinneradapter _searchspinneradapter = new searchspinneradapter(getApplicationContext(), _lstsearchspinnerdto);

            cbosearchrecord = findViewById(R.id.cbosearchrecord);
            cbosearchrecord.setAdapter(_searchspinneradapter);

            cbosearchrecord.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    _selected_searchspinnerdto = (searchspinnerdto) parent.getItemAtPosition(position);

                    utilz.getInstance(getApplicationContext()).globalloghandler(_selected_searchspinnerdto.getdto_value(), searchutilsactivity.class.getSimpleName(), 1, 1);

                    populateautocompletedtogivensearchfilter(_selected_searchspinnerdto.getdto_key());

					refreshlistfromdbonfilter("");

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
					refreshlistfromdbonfilter("");
                }
            });


            refreshlistfromdbonfilter("");


        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), searchutilsactivity.class.getSimpleName(), 1, 0);
        }
    }

    void populateautocompletedtogivensearchfilter(String _selected_searchspinnerdto) {
        try {

            switch (_selected_searchspinnerdto) {
                case "crop":

                    new populateautocompletecropdtogivensearchfilterBackgroundAsyncTask().execute();

                    break;
                case "diseasepest":

                    new populateautocompletecropdiseasepestdtogivensearchfilterBackgroundAsyncTask().execute();

                    break;

            }

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), searchutilsactivity.class.getSimpleName(), 1, 0);
        }
    }

    /*When defining an AsyncTask we can pass three types between < > brackets.
    Defined as <Params, Progress, Result>*/
    private class populateautocompletecropdtogivensearchfilterBackgroundAsyncTask extends AsyncTask<Void, Void, List<cropdto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            _lstsearchautocompletedtos = new ArrayList<>();

            super.onPreExecute();
        }

        @Override
        protected List<cropdto> doInBackground(Void... param) {
            // Disk-intensive work. This runs on a background thread. 
            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(searchutilsactivity.this);
            db.openDataBase();
            List<cropdto> lstresultsdtos = db.getallcrops();
            db.close();

            Collections.reverse(lstresultsdtos);

            return lstresultsdtos;
        }

        @Override
        protected void onProgressUpdate(Void... param) {
            super.onProgressUpdate(param);
            // Runs on the UI thread after publishProgress is invoked
            Log.e(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(final List<cropdto> lstresultsdtos) {
//            super.onPostExecute(result);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result returned from the doInBackground() method.
            // Update UI with the response.
            Log.e(TAG, "onPostExecute");

            runOnUiThread(new Runnable() {
                public void run() {
                    try {

                        utilz.getInstance(getApplicationContext()).globalloghandler("record count [ " + lstresultsdtos.size() + " ]", searchutilsactivity.class.getSimpleName(), 1, 1);

                        for (int i = 0; i < lstresultsdtos.size(); i++) {

                            cropdto _dto = lstresultsdtos.get(i);

                            searchautocompletedto _searchautocompletedto = new searchautocompletedto();

                            _searchautocompletedto.setdto_id(Long.valueOf(i));
                            _searchautocompletedto.setdto_key(_dto.getcrop_name());
                            _searchautocompletedto.setdto_value(_dto.getcrop_name());

                            _lstsearchautocompletedtos.add(_searchautocompletedto);
                        }

                        txtautocompleterecord = findViewById(R.id.txtautocompleterecord);
                        txtautocompleterecord.setVisibility(View.VISIBLE);
                        txtautocompleterecord.setThreshold(1);

                        _searchautocompleteadapter = new searchautocompleteadapter(getApplicationContext(), R.layout.search_utils_layout, R.id.txtautocompleterecord, _lstsearchautocompletedtos);

                        txtautocompleterecord.setAdapter(_searchautocompleteadapter);

                        txtautocompleterecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {

                                //this is the way to find selected object/item
                                _selected_searchautocompletedto = (searchautocompletedto) adapterView.getItemAtPosition(pos);

                                refreshlistfromdbonfilter(_selected_searchautocompletedto.getdto_value());

                            }
                        });

                        txtautocompleterecord.addTextChangedListener(new TextWatcher() {

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                refreshlistfromdbonfilter(s.toString());
                            }

                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });

                    } catch (Exception ex) {
                        utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), searchutilsactivity.class.getSimpleName(), 1, 0);
                    }

                }
            });


        }

    }

    /*When defining an AsyncTask we can pass three types between < > brackets.
    Defined as <Params, Progress, Result>*/
    private class populateautocompletecropdiseasepestdtogivensearchfilterBackgroundAsyncTask extends AsyncTask<Void, Void, List<cropdiseasedto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            _lstsearchautocompletedtos = new ArrayList<>();

            super.onPreExecute();
        }

        @Override
        protected List<cropdiseasedto> doInBackground(Void... param) {
            // Disk-intensive work. This runs on a background thread. 
            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(searchutilsactivity.this);
            db.openDataBase();
            List<cropdiseasedto> lstresultsdtos = db.getallcropsdiseases();
            db.close();

            Collections.reverse(lstresultsdtos);

            return lstresultsdtos;
        }

        @Override
        protected void onProgressUpdate(Void... param) {
            super.onProgressUpdate(param);
            // Runs on the UI thread after publishProgress is invoked
            Log.e(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(final List<cropdiseasedto> lstresultsdtos) {
//            super.onPostExecute(result);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result returned from the doInBackground() method.
            // Update UI with the response.
            Log.e(TAG, "onPostExecute");

            runOnUiThread(new Runnable() {
                public void run() {
                    try {

                        utilz.getInstance(getApplicationContext()).globalloghandler("record count [ " + lstresultsdtos.size() + " ]", searchutilsactivity.class.getSimpleName(), 1, 1);

                        for (int i = 0; i < lstresultsdtos.size(); i++) {

                            cropdiseasedto _dto = lstresultsdtos.get(i);

                            searchautocompletedto _searchautocompletedto = new searchautocompletedto();

                            _searchautocompletedto.setdto_id(Long.valueOf(i));
                            _searchautocompletedto.setdto_key(_dto.getcropdisease_name());
                            _searchautocompletedto.setdto_value(_dto.getcropdisease_name());

                            _lstsearchautocompletedtos.add(_searchautocompletedto);
                        }

                        txtautocompleterecord = findViewById(R.id.txtautocompleterecord);
                        txtautocompleterecord.setVisibility(View.VISIBLE);
                        txtautocompleterecord.setThreshold(1);

                        _searchautocompleteadapter = new searchautocompleteadapter(getApplicationContext(), R.layout.search_utils_layout, R.id.txtautocompleterecord, _lstsearchautocompletedtos);

                        txtautocompleterecord.setAdapter(_searchautocompleteadapter);

                        txtautocompleterecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {

                                //this is the way to find selected object/item
                                _selected_searchautocompletedto = (searchautocompletedto) adapterView.getItemAtPosition(pos);

                                refreshlistfromdbonfilter(_selected_searchautocompletedto.getdto_value());

                            }
                        });

                        txtautocompleterecord.addTextChangedListener(new TextWatcher() {

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                refreshlistfromdbonfilter(s.toString());
                            }

                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });

                    } catch (Exception ex) {
                        utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), searchutilsactivity.class.getSimpleName(), 1, 0);
                    }

                }
            });


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        /* MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setQueryHint(getString(R.string.search_hint));
            searchView.setSubmitButtonEnabled(true);

            SearchAdapter searchAdapter = new SearchAdapter(this);
//            searchView.setSuggestionsAdapter(searchAdapter);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            // optional: set the letters count after which the search will begin to 1
            // the default is 2
            try {
                int autoCompleteTextViewID = getResources().getIdentifier("android:id/search_src_text", null, null);
                AutoCompleteTextView searchAutoCompleteTextView = (AutoCompleteTextView)
                        searchView.findViewById(autoCompleteTextViewID);
                searchAutoCompleteTextView.setThreshold(1);

                //_searchautocompleteadapter = new searchautocompleteadapter(getApplicationContext(), R.layout.search_utils_layout, autoCompleteTextViewID, _lstsearchautocompletedtos);
				  
                //searchAutoCompleteTextView.setAdapter(_searchautocompleteadapter);
				
                searchAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                        //this is the way to find selected object/item 
						//_selected_searchautocompletedto = (searchautocompletedto) adapterView.getItemAtPosition(pos); 
						//refreshlistfromdbonfilter(_selected_searchautocompletedto.getdto_value());
					
                    }
                });

            } catch (Exception e) {
                Log.e(TAG, "failed to set search view letters threshold");
				utilz.getInstance(getApplicationContext()).globalloghandler("failed to set search view letters threshold.", searchutilsactivity.class.getSimpleName(), 1, 0);
            }

            queryTextListener = new SearchView.OnQueryTextListener() {
				
                @Override
                public boolean onQueryTextChange(String query) {
                    Log.i("onQueryTextChange", query);
                    //refreshlistfromdbonfilter(query);
//                    utilz.getInstance(getApplicationContext()).globalloghandler(newText, searchutilsactivity.class.getSimpleName(), 1, 0, "onQueryTextChange", "onQueryTextChange");
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);
                    //refreshlistfromdbonfilter(query);
//                    utilz.getInstance(getApplicationContext()).globalloghandler(query, searchutilsactivity.class.getSimpleName(), 1, 0, "onQueryTextSubmit", "onQueryTextSubmit");
                    return true;
                }
            };

            searchView.setOnQueryTextListener(queryTextListener);
            searchView.setOnSearchClickListener(clickListener);
            searchView.setOnCloseListener(closeListener);
        } */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.home_menu:

                    utilz.getInstance(getApplicationContext()).globalloghandler("launching MainActivity...", searchutilsactivity.class.getSimpleName(), 1, 1);

                    final Intent _MainActivity = new Intent(this, MainActivity.class);
                    startActivity(_MainActivity);
                    return true;
                default:
                    break;
            }
            return super.onOptionsItemSelected(item);
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), searchutilsactivity.class.getSimpleName(), 1, 0);
            return false;
        }
    }

    public void refreshlistfromdbonfilter(String _searchterm) {
        try {
            if (_searchterm != null) {
                if (_searchterm.isEmpty()) {

                    new fetchallBackgroundAsyncTask().execute();

                } else {

                    new filterBackgroundAsyncTask().execute(_searchterm);
                }
            } else {

                new fetchallBackgroundAsyncTask().execute();
            }
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), searchutilsactivity.class.getSimpleName(), 1, 0);
        }
    }

    /*When defining an AsyncTask we can pass three types between < > brackets.
    Defined as <Params, Progress, Result>*/
    private class fetchallBackgroundAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            // showing the ProgressBar 
            //simpleWaitDialog = ProgressDialog.show(searchutilsactivity.this, "loading records from datastore...", "excecuting task...");
            //simpleWaitDialog.setCancelable(true);
			
			showprogressbar();

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... param) {
            // Disk-intensive work. This runs on a background thread. 
            Log.e(TAG, "doInBackground");

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... progress) {
            super.onProgressUpdate(progress);
            // Runs on the UI thread after publishProgress is invoked
            Log.e(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result returned from the doInBackground() method.
            // Update UI with the response.
            Log.e(TAG, "onPostExecute");

            runOnUiThread(new Runnable() {
                public void run() {

                    if (_selected_searchspinnerdto == null) {
                        String crops_list_fragment_TAG = "crops_list_fragment";

                        getSupportFragmentManager()
                                .beginTransaction()
                                .addToBackStack(crops_list_fragment_TAG)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .replace(R.id.container, crops_list_fragment.newInstance(""), crops_list_fragment_TAG)
                                .commit();
                        return;
                    }

                    switch (_selected_searchspinnerdto.getdto_key()) {
                        case "crop":

                            String crops_list_fragment_TAG = "crops_list_fragment";

                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .addToBackStack(crops_list_fragment_TAG)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                    .replace(R.id.container, crops_list_fragment.newInstance(""), crops_list_fragment_TAG)
                                    .commit();

                            break;
                        case "diseasepest":

                            String crops_diseases_list_fragment_TAG = "crops_diseases_list_fragment";

                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .addToBackStack(crops_diseases_list_fragment_TAG)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                    .replace(R.id.container, crops_diseases_list_fragment.newInstance(""), crops_diseases_list_fragment_TAG)
                                    .commit();

                            break;
                    }


                }
            });

            //simpleWaitDialog.dismiss();

			hideprogressbar();

        }

    }

    /*When defining an AsyncTask we can pass three types between < > brackets.
    Defined as <Params, Progress, Result>*/
    private class filterBackgroundAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            //showing the ProgressBar 
            //simpleWaitDialog = ProgressDialog.show(searchutilsactivity.this, "loading records from datastore...", "excecuting task...");
            //simpleWaitDialog.setCancelable(true);

			showprogressbar();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... _searchterm) {
            // Disk-intensive work. This runs on a background thread. 
            Log.e(TAG, "doInBackground");

            return _searchterm[0];
        }

        @Override
        protected void onProgressUpdate(Void... param) {
            super.onProgressUpdate(param);
            // Runs on the UI thread after publishProgress is invoked
            Log.e(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(final String _searchterm) {
//            super.onPostExecute(result);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result returned from the doInBackground() method.
            // Update UI with the response.
            Log.e(TAG, "onPostExecute");

            runOnUiThread(new Runnable() {
                public void run() {

                    switch (_selected_searchspinnerdto.getdto_key()) {
                        case "crop":

                            String crops_list_fragment_TAG = "crops_list_fragment";

                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .addToBackStack(crops_list_fragment_TAG)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                    .replace(R.id.container, crops_list_fragment.newInstance(_searchterm), crops_list_fragment_TAG)
                                    .commit();

                            break;
                        case "diseasepest":

                            String crops_diseases_list_fragment_TAG = "crops_diseases_list_fragment";

                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .addToBackStack(crops_diseases_list_fragment_TAG)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                    .replace(R.id.container, crops_diseases_list_fragment.newInstance(_searchterm), crops_diseases_list_fragment_TAG)
                                    .commit();

                            break;
                    }


                }
            });

            //simpleWaitDialog.dismiss();

			hideprogressbar();

        }

    }

	void showprogressbar(){		
        progressbarsearch = findViewById(R.id.progressbarsearch);
        progressbarsearch.setVisibility(View.VISIBLE);
        //An indeterminate ProgressBar shows a cyclic animation without an indication of progress. Basic indeterminate ProgressBar (spinning wheel)
        progressbarsearch.setIndeterminate(true);
		//progressbarsearch.setCancelable(true);
		//progressbarsearch.setCanceledOnTouchOutside(true);
	}
	
	void hideprogressbar(){
		progressbarsearch.setVisibility(View.GONE);
	}
	
	
}
