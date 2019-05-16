package com.tech.nyax.myapplication10;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class pestsinsecticideslistactivity extends AppCompatActivity {

    private final static String TAG = pestsinsecticideslistactivity.class.getSimpleName();
    // Create DatabasehelperUtilz class object in your activity.
    private DatabasehelperUtilz db;
    List<pestinsecticidedto> lstpestinsecticidedtos;
    ListView pestsinsecticideslistview;
    pestsinsecticideslistadapter _pestsinsecticideslistadapter;
    AutoCompleteTextView txtautocompletepestinsecticide;
    pestsinsecticidesautocompleteadapter _pestsinsecticidesautocompleteadapter;
    private pestinsecticidedto _selectedautocompletedto;
    private ProgressDialog simpleWaitDialog;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private SearchView.OnClickListener clickListener;
    private SearchView.OnCloseListener closeListener;

    //The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pestsinsecticides_list_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this will show the back arrow in the tool bar.

        getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));

        try {

            new fetchautocompleteBackgroundAsyncTask().execute();
            refreshlistfromdbonfilter("");

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pestsinsecticides_list_menu, menu);
		
         /*MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setQueryHint(getString(R.string.search_hint));
            SearchAdapter searchAdapter = new SearchAdapter(this);
			searchView.setSubmitButtonEnabled(true);
//            searchView.setSuggestionsAdapter(searchAdapter);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);
                    
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    refreshlistfromdbonfilter(query);
                    Log.i("onQueryTextSubmit", query);
                    
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
                case R.id.delete_all_menu:

                    utilz.getInstance(getApplicationContext()).globalloghandler("launching delete_all_activity...", TAG, 1, 1);

                    final Intent delete_all_activity = new Intent(this, delete_all_activity.class);
					delete_all_activity.putExtra(DBContract._dto_entity_type, DBContract.app_entities_wrapper.pestsinsecticides);
                    startActivity(delete_all_activity);
                    return true;
					case R.id.create_pestinsecticide_activity:

                    utilz.getInstance(getApplicationContext()).globalloghandler("launching createpestinsecticideactivity...", TAG, 1, 1);

                    final Intent createpestinsecticideactivity = new Intent(this, createpestinsecticideactivity.class);
                    startActivity(createpestinsecticideactivity);
                    return true;
                case R.id.home_menu:

                    utilz.getInstance(getApplicationContext()).globalloghandler("launching MainActivity...", TAG, 1, 1);

                    final Intent _MainActivity = new Intent(this, MainActivity.class);
                    startActivity(_MainActivity);
                    return true;
                default:
                    break;
            }
            //searchView.setOnQueryTextListener(queryTextListener);
            return super.onOptionsItemSelected(item);
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
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
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    /*When defining an AsyncTask we can pass three types between < > brackets.
    Defined as <Params, Progress, Result>*/
    private class fetchallBackgroundAsyncTask extends AsyncTask<Void, Void, List<pestinsecticidedto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            // showing the ProgressBar 
            //simpleWaitDialog = ProgressDialog.show(pestsinsecticideslistactivity.this, "loading records from datastore...", "excecuting task...");
            //simpleWaitDialog.setCancelable(true);

            super.onPreExecute();
        }

        @Override
        protected List<pestinsecticidedto> doInBackground(Void... param) {
            // Disk-intensive work. This runs on a background thread. 
            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(pestsinsecticideslistactivity.this);
            db.openDataBase();
            lstpestinsecticidedtos = db.getallpestsinsecticides();
            db.close();

            return lstpestinsecticidedtos;
        }

        @Override
        protected void onProgressUpdate(Void... param) {
            super.onProgressUpdate(param);
            // Runs on the UI thread after publishProgress is invoked
            Log.e(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(final List<pestinsecticidedto> lstpestinsecticidedtos) {
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

                    for (pestinsecticidedto _pestinsecticidedto : lstpestinsecticidedtos) {
                        _pestinsecticidedto = buildpesticideinsecticidedto(_pestinsecticidedto);
                    }

                    Log.e(TAG, "pesticides/insecticides [ " + String.valueOf(lstpestinsecticidedtos.size()) + " ].");

                    Collections.reverse(lstpestinsecticidedtos);

                    _pestsinsecticideslistadapter = new pestsinsecticideslistadapter(getApplicationContext(), lstpestinsecticidedtos);
                    pestsinsecticideslistview = findViewById(R.id.lstpestsinsecticides);
                    pestsinsecticideslistview.setAdapter(_pestsinsecticideslistadapter);

                    int _count = lstpestinsecticidedtos.size();
                    getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring("pesticides/insecticides [ " + _count + " ]"));

                    // on seleting row launch Edit Screen
                    pestsinsecticideslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view,
                                                int position, long id) {

                            utilz.getInstance(getApplicationContext()).globalloghandler("pestsinsecticideslistview.setOnItemClickListener", TAG, 1, 1);

                            pestinsecticidedto _selectedpestinsecticidedto = (pestinsecticidedto) adapterView.getItemAtPosition(position);

                            // getting values from selected ListItem
                            String pestinsecticideid = ((TextView) view.findViewById(R.id.txtpestinsecticideid)).getText().toString();

                            Bundle dataBundle = new Bundle();
                            dataBundle.putString("pestinsecticideid", pestinsecticideid);
//                            dataBundle.putParcelable("selectedpestinsecticidedto", _selectedpestinsecticidedto);

                            // Starting new intent
                            Intent intent = new Intent(getApplicationContext(),
                                    editpestinsecticideactivity.class);
                            // sending pestinsecticideid to next activity
                            intent.putExtras(dataBundle);

                            // starting new activity
                            startActivity(intent);
                        }

                    });

                    try {

                        asyncupdaterecyclerviewonPostExecuterunOnUiThread(lstpestinsecticidedtos);

                    } catch (Exception ex) {
                        utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                    }


                }
            });

            //simpleWaitDialog.dismiss();

        }

    }


    /*When defining an AsyncTask we can pass three types between < > brackets.
    Defined as <Params, Progress, Result>*/
    private class filterBackgroundAsyncTask extends AsyncTask<String, Void, List<pestinsecticidedto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            //showing the ProgressBar 
            //simpleWaitDialog = ProgressDialog.show(pestsinsecticideslistactivity.this, "loading pestsinsecticides from datastore...", "");
            //simpleWaitDialog.setCancelable(true);

            lstpestinsecticidedtos = new ArrayList<>();
            _pestsinsecticideslistadapter = new pestsinsecticideslistadapter(getApplicationContext(), lstpestinsecticidedtos);
            _pestsinsecticideslistadapter.notifyDataSetChanged();

            super.onPreExecute();
        }

        @Override
        protected List<pestinsecticidedto> doInBackground(String... _searchterm) {
            // Disk-intensive work. This runs on a background thread. 
            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(pestsinsecticideslistactivity.this);
            db.openDataBase();
            lstpestinsecticidedtos = db.filterpestsinsecticidesgivenname(_searchterm[0]);
            db.close();

            return lstpestinsecticidedtos;
        }

        @Override
        protected void onProgressUpdate(Void... param) {
            super.onProgressUpdate(param);
            // Runs on the UI thread after publishProgress is invoked
            Log.e(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(final List<pestinsecticidedto> lstpestinsecticidedtos) {
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

                    for (pestinsecticidedto _pestinsecticidedto : lstpestinsecticidedtos) {
                        _pestinsecticidedto = buildpesticideinsecticidedto(_pestinsecticidedto);
                    }

                    Log.e(TAG, String.valueOf(lstpestinsecticidedtos.size()));

                    Collections.reverse(lstpestinsecticidedtos);

                    _pestsinsecticideslistadapter = new pestsinsecticideslistadapter(getApplicationContext(), lstpestinsecticidedtos);
                    pestsinsecticideslistview = findViewById(R.id.lstpestsinsecticides);
                    pestsinsecticideslistview.setAdapter(_pestsinsecticideslistadapter);

                    int _count = lstpestinsecticidedtos.size();
                    getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring("pesticides/insecticides [ " + _count + " ]"));

                    // on seleting row launch Edit Screen
                    pestsinsecticideslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            // getting values from selected ListItem
                            String pestinsecticideid = ((TextView) view.findViewById(R.id.txtpestinsecticideid)).getText().toString();

                            Bundle dataBundle = new Bundle();
                            dataBundle.putString("pestinsecticideid", pestinsecticideid);

                            // Starting new intent
                            Intent intent = new Intent(getApplicationContext(),
                                    editpestinsecticideactivity.class);
                            // sending pestinsecticideid to next activity
                            intent.putExtras(dataBundle);

                            // starting new activity
                            startActivity(intent);
                        }
                    });

                    try {

                        asyncupdaterecyclerviewonPostExecuterunOnUiThread(lstpestinsecticidedtos);

                    } catch (Exception ex) {
                        utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                    }


                }
            });

            //simpleWaitDialog.dismiss();

        }

    }

    /*When defining an AsyncTask we can pass three types between < > brackets.
    Defined as <Params, Progress, Result>*/
    private class fetchautocompleteBackgroundAsyncTask extends AsyncTask<Void, Void, List<pestinsecticidedto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            // showing the ProgressBar 
            //simpleWaitDialog = ProgressDialog.show(pestsinsecticideslistactivity.this, "loading records from datastore...", "excecuting task...");
            //simpleWaitDialog.setCancelable(true);

            super.onPreExecute();
        }

        @Override
        protected List<pestinsecticidedto> doInBackground(Void... param) {
            // Disk-intensive work. This runs on a background thread. 
            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(pestsinsecticideslistactivity.this);
            db.openDataBase();
            List<pestinsecticidedto> lstpestinsecticidedtos = db.getallpestsinsecticides();
            db.close();

            return lstpestinsecticidedtos;
        }

        @Override
        protected void onProgressUpdate(Void... param) {
            super.onProgressUpdate(param);
            // Runs on the UI thread after publishProgress is invoked
            Log.e(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(final List<pestinsecticidedto> lstpestinsecticidedtos) {
//            super.onPostExecute(result);
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result returned from the doInBackground() method.
            // Update UI with the response.
            Log.e(TAG, "onPostExecute");

            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating data into ui
                     * */

                    for (pestinsecticidedto _pestinsecticidedto : lstpestinsecticidedtos) {
                        _pestinsecticidedto = buildpesticideinsecticidedto(_pestinsecticidedto);
                    }

                    Log.e(TAG, "pesticides/insecticides [ " + String.valueOf(lstpestinsecticidedtos.size()) + " ].");

                    Collections.reverse(lstpestinsecticidedtos);

                    txtautocompletepestinsecticide = findViewById(R.id.txtautocompletepestinsecticide);
                    txtautocompletepestinsecticide.setVisibility(View.VISIBLE);
                    txtautocompletepestinsecticide.setThreshold(1);

                    _pestsinsecticidesautocompleteadapter = new pestsinsecticidesautocompleteadapter(getApplicationContext(), R.layout.pestsinsecticides_list_layout, R.id.txtautocompletepestinsecticide, lstpestinsecticidedtos);

                    txtautocompletepestinsecticide.setAdapter(_pestsinsecticidesautocompleteadapter);

                    txtautocompletepestinsecticide.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                            //this is the way to find selected object/item
                            _selectedautocompletedto = (pestinsecticidedto) adapterView.getItemAtPosition(pos);
                            refreshlistfromdbonfilter(_selectedautocompletedto.getpestinsecticide_name());
                        }
                    });

                    txtautocompletepestinsecticide.addTextChangedListener(new TextWatcher() {

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


                }
            });

            //simpleWaitDialog.dismiss();

        }

    }

    void asyncupdaterecyclerviewonPostExecuterunOnUiThread(final List<pestinsecticidedto> lstpestinsecticidedtos) {

        try {

            //get handle
            RecyclerView _recyclerview = findViewById(R.id.pestsinsecticides_list_recycler_view);

            //set a layout manager (LinearLayoutManager in this example)
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

            _recyclerview.setLayoutManager(mLayoutManager);

            _recyclerview.setHasFixedSize(true);

            _recyclerview.setItemAnimator(new DefaultItemAnimator());
            //_recyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            //specify an adapter
            pestsinsecticideslistrecyclerviewadapter _recyclerviewadapter = new pestsinsecticideslistrecyclerviewadapter(getApplicationContext(), lstpestinsecticidedtos)/*{
			@Override
			public void load() {
			 do your stuff here */
			/* This method is automatically call while user reach at end of your list. 
			}}*/;

            _recyclerview.setAdapter(_recyclerviewadapter);

            // adding custom divider line
            _recyclerview.addItemDecoration(new SimpleBlueDivider(getApplicationContext()));

            // row click listener
			/* _recyclerview.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), _recyclerview, new RecyclerTouchListener.ClickListener() {

				@Override
				public void onClick(View view, int position) {

					Toast.makeText(getApplicationContext(), _dto.getpestinsecticide_name() + " is selected!", Toast.LENGTH_SHORT).show();

				}

				@Override
				public void onLongClick(View view, int position) {

				}
			})); */


            if (_recyclerview.getLayoutManager() instanceof LinearLayoutManager) {

                final LinearLayoutManager _linearLayoutManager = (LinearLayoutManager)
                        _recyclerview.getLayoutManager();

                _recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                        super.onScrolled(recyclerView, dx, dy);

                        totalItemCount = _linearLayoutManager.getItemCount();

                        lastVisibleItem = _linearLayoutManager.findLastVisibleItemPosition();

                        if (!loading && totalItemCount <= (lastVisibleItem +
                                visibleThreshold)) {

                            if (onLoadMoreListener != null) {
                                onLoadMoreListener.onLoadMore();
                            }

                            loading = true;
                        }
                    }
                });
            }

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }

    }

    pestinsecticidedto buildpesticideinsecticidedto(pestinsecticidedto _pestinsecticidedto) {
        try {

            db = new DatabasehelperUtilz(pestsinsecticideslistactivity.this);
            db.openDataBase();

            manufacturerdto _manufacturerdto = db.getmanufacturergivenid(Long.valueOf(_pestinsecticidedto.getpestinsecticide_manufacturer_id()));

            _pestinsecticidedto.setpestinsecticide_manufacturer_name(_manufacturerdto.getmanufacturer_name());

            cropdiseasedto _cropdiseasedto = db.getcropdiseasegivenid(Long.valueOf(_pestinsecticidedto.getpestinsecticide_crop_disease_id()));

            _pestinsecticidedto.setpestinsecticide_crop_disease_name(_cropdiseasedto.getcropdisease_name());

            return _pestinsecticidedto;

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
            return null;
        }

    }


}
