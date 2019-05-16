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

public class categorieslistactivity extends AppCompatActivity {

    private final static String TAG = categorieslistactivity.class.getSimpleName();
    // Create DatabasehelperUtilz class object in your activity.
    private DatabasehelperUtilz db;
    List<categorydto> lstcategorydtos;
    ListView categorieslistview;
    categorieslistadapter _categorieslistadapter;
    AutoCompleteTextView txtautocompletecategory;
    categoriesautocompleteadapter _categoriesautocompleteadapter;
    private categorydto _selectedautocompletedto;
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
        setContentView(R.layout.categories_list_layout);
		
		/*final Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this will show the back arrow in the tool bar.

		/* CharSequence title = "categories";
		SpannableString s = new SpannableString(title);
		s.setSpan(new ForegroundColorSpan(Color.RED), 0, title.length(),
		Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); */

        getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));

//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(128, 0, 0, 0)));
        //getSupportActionBar().setBackgroundDrawable(R.drawable.edit);

        getSupportActionBar().setIcon(R.drawable.delete);
        getSupportActionBar().setLogo(R.drawable.edit);

        // categorieslistview = findViewById(R.id.lstcategories);
        // categorieslistview.setAdapter(_categorieslistadapter);

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
        inflater.inflate(R.menu.categories_list_menu, menu);
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

                _categoriesautocompleteadapter = new categoriesautocompleteadapter(getApplicationContext(), R.layout.categories_list_layout, autoCompleteTextViewID, lstcategorydtos);
                searchAutoCompleteTextView.setAdapter(_categoriesautocompleteadapter);
                searchAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                        //this is the way to find selected object/item
                        _selectedautocompletedto = (categorydto) adapterView.getItemAtPosition(pos);
                        refreshlistfromdbonfilter(_selectedautocompletedto.getcategory_name());
                    }
                });

            } catch (Exception e) {
                Log.e(TAG, "failed to set search view letters threshold");
            }

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String query) {
                    Log.i("onQueryTextChange", query);
                    //refreshlistfromdbonfilter(query);
//                   
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);
                    refreshlistfromdbonfilter(query);
					
                    return true;
                }
            };

            //searchView.setOnQueryTextListener(queryTextListener);
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
					delete_all_activity.putExtra(DBContract._dto_entity_type, DBContract.app_entities_wrapper.categories);
                    startActivity(delete_all_activity);
                    return true;
					case R.id.create_category_activity:

                    utilz.getInstance(getApplicationContext()).globalloghandler("launching createcategoryactivity...", TAG, 1, 1);

                    final Intent createcategoryactivity = new Intent(this, createcategoryactivity.class);
                    startActivity(createcategoryactivity);
                    return true;
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
    private class fetchallBackgroundAsyncTask extends AsyncTask<Void, Void, List<categorydto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            // showing the ProgressBar 
            //simpleWaitDialog = ProgressDialog.show(categorieslistactivity.this, "loading records from datastore...", "excecuting task...");
            //simpleWaitDialog.setCancelable(true);

            super.onPreExecute();
        }

        @Override
        protected List<categorydto> doInBackground(Void... param) {
            // Disk-intensive work. This runs on a background thread. 
            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(categorieslistactivity.this);
            db.openDataBase();
            lstcategorydtos = db.getallcategories();
            db.close();

            Collections.reverse(lstcategorydtos);

            return lstcategorydtos;
        }

        @Override
        protected void onProgressUpdate(Void... param) {
            super.onProgressUpdate(param);
            // Runs on the UI thread after publishProgress is invoked
            Log.e(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(final List<categorydto> lstcategorydtos) {
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
                    _categorieslistadapter = new categorieslistadapter(getApplicationContext(), lstcategorydtos);

                    categorieslistview = findViewById(R.id.lstcategories);
                    categorieslistview.setAdapter(_categorieslistadapter);
                    _categorieslistadapter.notifyDataSetChanged();

                    int _count = lstcategorydtos.size();
                    getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring("categories [ " + _count + " ]"));

                    // on seleting row launch Edit Screen
                    categorieslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view,
                                                int position, long id) {

                            utilz.getInstance(getApplicationContext()).globalloghandler("categorieslistview.setOnItemClickListener", TAG, 1, 1);

                            categorydto _selectedautocompletedtodto = (categorydto) adapterView.getItemAtPosition(position);

                            // getting values from selected ListItem
                            String categoryid = ((TextView) view.findViewById(R.id.txtcategoryid)).getText().toString();

                            Bundle dataBundle = new Bundle();
                            dataBundle.putString("categoryid", categoryid);
//                            dataBundle.putParcelable("selectedcategorydto", _selectedautocompletedtodto);

                            // Starting new intent
                            Intent intent = new Intent(getApplicationContext(),
                                    editcategoryactivity.class);
                            // sending categoryid to next activity
                            intent.putExtras(dataBundle);

                            // starting new activity
                            startActivity(intent);
                        }

                    });

                    try {

                        asyncupdaterecyclerviewonPostExecuterunOnUiThread(lstcategorydtos);

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
    private class filterBackgroundAsyncTask extends AsyncTask<String, Void, List<categorydto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            //showing the ProgressBar 
            //simpleWaitDialog = ProgressDialog.show(categorieslistactivity.this, "loading records from datastore...", "excecuting task...");
            //simpleWaitDialog.setCancelable(true);

            lstcategorydtos = new ArrayList<>();
            _categorieslistadapter = new categorieslistadapter(getApplicationContext(), lstcategorydtos);
            _categorieslistadapter.notifyDataSetChanged();

            super.onPreExecute();
        }

        @Override
        protected List<categorydto> doInBackground(String... _searchterm) {
            // Disk-intensive work. This runs on a background thread. 
            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(categorieslistactivity.this);
            db.openDataBase();
            lstcategorydtos = db.filtercategoriesgivenname(_searchterm[0]);
            db.close();

            Log.e(TAG, String.valueOf(lstcategorydtos.size()));

            Collections.reverse(lstcategorydtos);

            return lstcategorydtos;
        }

        @Override
        protected void onProgressUpdate(Void... param) {
            super.onProgressUpdate(param);
            // Runs on the UI thread after publishProgress is invoked
            Log.e(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(final List<categorydto> lstcategorydtos) {
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
                    _categorieslistadapter = new categorieslistadapter(getApplicationContext(), lstcategorydtos);

                    categorieslistview = findViewById(R.id.lstcategories);
                    categorieslistview.setAdapter(_categorieslistadapter);
                    _categorieslistadapter.notifyDataSetChanged();

                    int _count = lstcategorydtos.size();
                    getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring("categories [ " + _count + " ]"));

                    // on seleting row launch Edit Screen
                    categorieslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            // getting values from selected ListItem
                            String categoryid = ((TextView) view.findViewById(R.id.txtcategoryid)).getText().toString();

                            Bundle dataBundle = new Bundle();
                            dataBundle.putString("categoryid", categoryid);

                            // Starting new intent
                            Intent intent = new Intent(getApplicationContext(),
                                    editcategoryactivity.class);
                            // sending categoryid to next activity
                            intent.putExtras(dataBundle);

                            // starting new activity
                            startActivity(intent);
                        }
                    });


                    try {

                        asyncupdaterecyclerviewonPostExecuterunOnUiThread(lstcategorydtos);

                    } catch (Exception ex) {
                        utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                    }


                    //simpleWaitDialog.dismiss();

                }
            });

            /*txtautocompletecategory = findViewById(R.id.txtautocompletecategory);
			txtautocompletecategory.setVisibility(View.GONE);
            txtautocompletecategory.setThreshold(1);
            _categoriesautocompleteadapter = new categoriesautocompleteadapter(getApplicationContext(), R.layout.categories_list_layout, R.id.txtautocompletecategory, lstcategorydtos);
            txtautocompletecategory.setAdapter(_categoriesautocompleteadapter);
            txtautocompletecategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
//this is the way to find selected object/item
                    _selectedautocompletedto = (categorydto) adapterView.getItemAtPosition(pos);
                    refreshlistfromdbonfilter(_selectedautocompletedto.getcategory_name());
                }
            });*/

            //simpleWaitDialog.dismiss();

        }

    }


    /*When defining an AsyncTask we can pass three types between < > brackets.
    Defined as <Params, Progress, Result>*/
    private class fetchautocompleteBackgroundAsyncTask extends AsyncTask<Void, Void, List<categorydto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            // showing the ProgressBar 
            //simpleWaitDialog = ProgressDialog.show(categorieslistactivity.this, "loading records from datastore...", "excecuting task...");
            //simpleWaitDialog.setCancelable(true);

            super.onPreExecute();
        }

        @Override
        protected List<categorydto> doInBackground(Void... param) {
            // Disk-intensive work. This runs on a background thread. 
            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(categorieslistactivity.this);
            db.openDataBase();
            List<categorydto> lstdtos = db.getallcategories();
            db.close();

            Collections.reverse(lstdtos);

            return lstdtos;
        }

        @Override
        protected void onProgressUpdate(Void... param) {
            super.onProgressUpdate(param);
            // Runs on the UI thread after publishProgress is invoked
            Log.e(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(final List<categorydto> lstcategorydtos) {
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

                    txtautocompletecategory = findViewById(R.id.txtautocompletecategory);
                    txtautocompletecategory.setVisibility(View.VISIBLE);
                    txtautocompletecategory.setThreshold(1);

                    _categoriesautocompleteadapter = new categoriesautocompleteadapter(getApplicationContext(), R.layout.categories_list_layout, R.id.txtautocompletecategory, lstcategorydtos);

                    txtautocompletecategory.setAdapter(_categoriesautocompleteadapter);

                    txtautocompletecategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                            //this is the way to find selected object/item
                            _selectedautocompletedto = (categorydto) adapterView.getItemAtPosition(pos);
                            refreshlistfromdbonfilter(_selectedautocompletedto.getcategory_name());
                        }
                    });

                    txtautocompletecategory.addTextChangedListener(new TextWatcher() {

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

    void asyncupdaterecyclerviewonPostExecuterunOnUiThread(final List<categorydto> lstcategorydtos) {

        try {

            //get handle
            RecyclerView _recyclerview = findViewById(R.id.categories_list_recycler_view);

            //set a layout manager (LinearLayoutManager in this example)
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

            _recyclerview.setLayoutManager(mLayoutManager);

            _recyclerview.setHasFixedSize(true);

            _recyclerview.setItemAnimator(new DefaultItemAnimator());
            //_recyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            //specify an adapter
            categorieslistrecyclerviewadapter _recyclerviewadapter = new categorieslistrecyclerviewadapter(getApplicationContext(), lstcategorydtos)/*{
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

					Toast.makeText(getApplicationContext(), _dto.getcategory_name() + " is selected!", Toast.LENGTH_SHORT).show();

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


}
