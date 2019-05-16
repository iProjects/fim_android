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

public class crops_diseases_list_fragment extends Fragment {

    private final static String TAG = crops_diseases_list_fragment.class.getSimpleName();
    public static final int REQUEST_CODE = 11;
    public static final int RESULT_CODE = 12;
    // Our identifier for obtaining the name from arguments
    public static final String CROPS_DISEASES_FRAGMENT_TAG = "crops_diseases_list_fragment";
    public static final String CROPS_DISEASES_TAG = "crop_disease_name";
    String crop_disease_name;
    private DatabasehelperUtilz db;
    List<cropdto> lstresultsdtos;
    ListView recordslistview;
    cropslistadapter _cropslistadapter;
    AutoCompleteTextView txtautocompletecrop;
    cropsautocompleteadapter _cropsautocompleteadapter;
    private cropdto _selectedcrop;
    String _searchterm;
    // Our identifier for obtaining the name from arguments
    private static final String SEARCHTERM_ARG = "name";
    TextView lblrecordcount;

    //The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    // Required
    public crops_diseases_list_fragment() {

    }

    // The static constructor. This is the only way that you should instantiate
    // the fragment yourself

    public static crops_diseases_list_fragment newInstance(final String searchterm) {
        final crops_diseases_list_fragment myFragment = new crops_diseases_list_fragment();
        // The 1 below is an optimization, being the number of arguments that will
        // be added to this bundle. If you know the number of arguments you will add
        // to the bundle it stops additional allocations of the backing map. If
        // unsure, you can construct Bundle without any arguments
        final Bundle args = new Bundle(1);
        // This stores the argument as an argument in the bundle. Note that even if
        // the 'name' parameter is NULL then this will work, so you should consider
        // at this point if the parameter is mandatory and if so check for NULL and
        // throw an appropriate error if so
        args.putString(SEARCHTERM_ARG, searchterm);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        final Bundle arguments = getArguments();

        try {

            if (arguments == null) {

                utilz.getInstance(getActivity().getApplicationContext()).globalloghandler("arguments cannot be null.", TAG, 1, 0);

            } else if (!arguments.containsKey(SEARCHTERM_ARG)) {

                utilz.getInstance(getActivity().getApplicationContext()).globalloghandler("arguments missing key [ " + SEARCHTERM_ARG + " ].", TAG, 1, 0);

            } else {
                _searchterm = arguments.getString(SEARCHTERM_ARG);
            }

            if (_searchterm == null || _searchterm.isEmpty()) {

                new fetchallBackgroundAsyncTask().execute();

            } else if (_searchterm != null && !_searchterm.isEmpty()) {

                new filterBackgroundAsyncTask().execute(_searchterm);

            } else {

                new fetchallBackgroundAsyncTask().execute();
            }

        } catch (Exception ex) {
            utilz.getInstance(getActivity().getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    /*When defining an AsyncTask we can pass three types between < > brackets.
    Defined as <Params, Progress, Result>*/
    private class fetchallBackgroundAsyncTask extends AsyncTask<Void, Void, List<search_cropdiseasepest_dto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            // showing the ProgressBar 
            // simpleWaitDialog = ProgressDialog.show(crops_diseases_list_fragment.this,
            // "loading crops from datastore...", "");
            // simpleWaitDialog.setCancelable(true);

            // lstresultsdtos = new ArrayList<>();
            // _cropslistadapter = new cropslistadapter(getActivity().getApplicationContext(), lstresultsdtos);
            // _cropslistadapter.notifyDataSetChanged();

            super.onPreExecute();
        }

        @Override
        protected List<search_cropdiseasepest_dto> doInBackground(Void... param) {
            // Disk-intensive work. This runs on a background thread. 
            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(getActivity().getApplicationContext());
            db.openDataBase();
            List<search_cropdiseasepest_dto> lstresultsdtos = db.getpesticidesinsecticidesgivendiseasepestname("");
            db.close();

            if (null == lstresultsdtos) {
                List<search_cropdiseasepest_dto> _lstresultsdtos = new ArrayList<>();
                return _lstresultsdtos;
            }

            return lstresultsdtos;
        }

        @Override
        protected void onProgressUpdate(Void... param) {
            super.onProgressUpdate(param);
            // Runs on the UI thread after publishProgress is invoked
            Log.e(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(final List<search_cropdiseasepest_dto> lstresultsdtos) {
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

                    Log.e(TAG, String.valueOf(lstresultsdtos.size()));

                    Collections.reverse(lstresultsdtos);

                    search_cropsdiseasespests_list_adapter _search_list_adapter = new search_cropsdiseasespests_list_adapter(getActivity().getApplicationContext(), lstresultsdtos);

                    recordslistview = getActivity().findViewById(R.id.lstrecords);

                    recordslistview.setAdapter(_search_list_adapter);

                    _search_list_adapter.notifyDataSetChanged();

                    int _count = lstresultsdtos.size();
                    Log.e(TAG, "record count [ " + _count + " ]");

                    lblrecordcount = getActivity().findViewById(R.id.lblrecordcount);
                    lblrecordcount.setText("[ " + _count + " ]");

                    try {

                        asyncupdaterecyclerviewonPostExecuterunOnUiThread(lstresultsdtos);

                    } catch (Exception ex) {
                        utilz.getInstance(getActivity().getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                    }


                }
            });


        }

    }


    /*When defining an AsyncTask we can pass three types between < > brackets.
    Defined as <Params, Progress, Result>*/
    private class filterBackgroundAsyncTask extends AsyncTask<String, Void, List<search_cropdiseasepest_dto>> {

        @Override
        protected void onPreExecute() {

            // This runs on the UI thread before the background thread executes.
            // Do pre-thread tasks such as initializing variables.
            Log.e(TAG, "onPreExecute");

            //showing the ProgressBar 
            // simpleWaitDialog = ProgressDialog.show(crops_diseases_list_fragment.this,
            // "loading crops from datastore...", "");
            // simpleWaitDialog.setCancelable(true);

            // lstresultsdtos = new ArrayList<>();
            // _cropslistadapter = new cropslistadapter(getActivity().getApplicationContext(), lstresultsdtos);
            // _cropslistadapter.notifyDataSetChanged();

            super.onPreExecute();
        }

        @Override
        protected List<search_cropdiseasepest_dto> doInBackground(String... _searchterm) {
            // Disk-intensive work. This runs on a background thread. 
            Log.e(TAG, "doInBackground");

            db = new DatabasehelperUtilz(getActivity().getApplicationContext());
            db.openDataBase();
            List<search_cropdiseasepest_dto> lstresultsdtos = db.getpesticidesinsecticidesgivendiseasepestname(_searchterm[0]);
            db.close();

            if (null == lstresultsdtos) {
                List<search_cropdiseasepest_dto> _lstresultsdtos = new ArrayList<>();
                return _lstresultsdtos;
            }

            return lstresultsdtos;
        }

        @Override
        protected void onProgressUpdate(Void... param) {
            super.onProgressUpdate(param);
            // Runs on the UI thread after publishProgress is invoked
            Log.e(TAG, "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(final List<search_cropdiseasepest_dto> lstresultsdtos) {
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

                    Log.e(TAG, String.valueOf(lstresultsdtos.size()));

                    Collections.reverse(lstresultsdtos);

                    search_cropsdiseasespests_list_adapter _search_list_adapter = new search_cropsdiseasespests_list_adapter(getActivity().getApplicationContext(), lstresultsdtos);

                    recordslistview = getActivity().findViewById(R.id.lstrecords);

                    recordslistview.setAdapter(_search_list_adapter);

                    _search_list_adapter.notifyDataSetChanged();

                    int _count = lstresultsdtos.size();
                    Log.e(TAG, "record count [ " + _count + " ]");

                    lblrecordcount = getActivity().findViewById(R.id.lblrecordcount);
                    lblrecordcount.setText("[ " + _count + " ]");

                    try {

                        asyncupdaterecyclerviewonPostExecuterunOnUiThread(lstresultsdtos);

                    } catch (Exception ex) {
                        utilz.getInstance(getActivity().getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                    }


                }
            });


        }

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //final Bundle arguments = getArguments();

        /* try {

            if (arguments == null || !arguments.containsKey(CROPS_DISEASES_TAG)) {
                // Set a default or error as you see fit
            } else {
                crop_disease_name = arguments.getString(CROPS_DISEASES_TAG);
            }

            if (crop_disease_name.isEmpty()) {

                new fetchallBackgroundAsyncTask().execute();

            } else {

                new filterBackgroundAsyncTask().execute(crop_disease_name);
            }
        } catch (Exception ex) {
            utilz.getInstance(getActivity().getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        } */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.crops_diseases_list_fragment, container, false);
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

    void asyncupdaterecyclerviewonPostExecuterunOnUiThread(final List<search_cropdiseasepest_dto> lstresultsdtos) {

        try {

            List<search_cropdiseasepest_dto> _lstsearchdtos = new ArrayList<>();

            int _counta = 0;

            for (int i = 0; i < lstresultsdtos.size(); i++) {

                _counta++;

                search_cropdiseasepest_dto _dto = lstresultsdtos.get(i);

                search_cropdiseasepest_dto _searchdto = new search_cropdiseasepest_dto();

                _searchdto.setdto_Id(Long.valueOf(_counta));
                _searchdto.setcrop_diseasepest_name(_dto.getcrop_diseasepest_name());
                _searchdto.setpestinsecticide_name(_dto.getpestinsecticide_name());
                _searchdto.setpestinsecticide_manufacturer(_dto.getpestinsecticide_manufacturer());

                _lstsearchdtos.add(_searchdto);
            }

            //get handle
            RecyclerView _recyclerview = getActivity().findViewById(R.id.diseasespests_list_recycler_view);

            //set a layout manager (LinearLayoutManager in this example)
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

            _recyclerview.setLayoutManager(mLayoutManager);

            _recyclerview.setHasFixedSize(true);

            _recyclerview.setItemAnimator(new DefaultItemAnimator());
            //_recyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            //specify an adapter
            search_diseasespests_list_recyclerview_adapter _recyclerviewadapter = new search_diseasespests_list_recyclerview_adapter(getActivity().getApplicationContext(), _lstsearchdtos)/*{
			@Override
			public void load() {
			 do your stuff here */
			/* This method is automatically call while user reach at end of your list. 
			}}*/;

            _recyclerview.setAdapter(_recyclerviewadapter);

            // adding custom divider line
            _recyclerview.addItemDecoration(new SimpleBlueDivider(getActivity().getApplicationContext()));

            // row click listener
			/* _recyclerview.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), _recyclerview, new RecyclerTouchListener.ClickListener() {

				@Override
				public void onClick(View view, int position) {

					Toast.makeText(getActivity().getApplicationContext(), _dto.getcrop_name() + " is selected!", Toast.LENGTH_SHORT).show();

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
            utilz.getInstance(getActivity().getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }

    }


}




