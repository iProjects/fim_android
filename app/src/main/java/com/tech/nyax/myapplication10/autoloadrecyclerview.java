// package com.tech.nyax.myapplication10;

// import android.os.Bundle;
// import android.os.Handler;
// import android.support.v7.app.AppCompatActivity;
// import android.support.v7.widget.LinearLayoutManager;
// import android.support.v7.widget.RecyclerView;
// import android.support.v7.widget.Toolbar;
// import android.util.Log;
// import android.widget.TextView;

// import com.android.volley.Request;
// import com.android.volley.Response;
// import com.android.volley.VolleyError;
// import com.android.volley.VolleyLog;

// import org.json.JSONArray;
// import org.json.JSONException;
// import org.json.JSONObject;

// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// public class autoloadrecyclerview extends AppCompatActivity {
	
    // private static final String TAG = "MainActivity";
    // private Toolbar toolbar;
    // private TextView tvEmptyView;
    // private RecyclerView mRecyclerView;
    // private DataAdapter mAdapter;
    // private LinearLayoutManager mLayoutManager;
    // private int mStart = 0, mEnd = 20;
    // private List<cropdto> studentList;
    // private List<cropdto> mTempCheck;
    // public static int pageNumber;
    // public int total_size = 0;
    // protected Handler handler;

    // @Override
    // protected void onCreate(Bundle savedInstanceState) {
        // super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        // pageNumber = 1;
        // toolbar = (Toolbar) findViewById(R.id.toolbar);
        // tvEmptyView = (TextView) findViewById(R.id.empty_view);
        // mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // studentList = new ArrayList<>();
        // mTempCheck = new ArrayList<>();
        // handler = new Handler();
        // if (toolbar != null) {
            // setSupportActionBar(toolbar);
            // getSupportActionBar().setTitle("Android Students");
        // }
        // mRecyclerView.setHasFixedSize(true);
        // mLayoutManager = new LinearLayoutManager(this);
        // mRecyclerView.setLayoutManager(mLayoutManager);
        // mAdapter = new DataAdapter(studentList, mRecyclerView);
        // mRecyclerView.setAdapter(mAdapter);
        // GetGroupData("" + mStart, "" + mEnd);
        // mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            // @Override
            // public void onLoadMore() {
                // if (mTempCheck.size() > 0) {
                    // studentList.add(null);
                    // mAdapter.notifyItemInserted(studentList.size() - 1);
                    // int start = pageNumber * 20;
                    // start = start + 1;
                    // ++pageNumber;
                    // mTempCheck.clear();
                    // GetData("" + start, "" + mEnd);
                // }
            // }
        // });
    // }

    // public void GetData(final String LimitStart, final String LimitEnd) {
        // Map<String, String> params = new HashMap<>();
        // params.put("LimitStart", LimitStart);
        // params.put("Limit", LimitEnd);
        // Custom_Volly_Request jsonObjReq = new Custom_Volly_Request(Request.Method.POST,
                // "Your php file link", params,
                // new Response.Listener<JSONObject>() {
                    // @Override
                    // public void onResponse(JSONObject response) {
                        // Log.d("ResponseSuccess", response.toString());
// // handle the data from the servoce
                    // }
                // }, new Response.ErrorListener() {
            // @Override
            // public void onErrorResponse(VolleyError error) {
                // VolleyLog.d("ResponseErrorVolly: " + error.getMessage());
            // }
        // });
    // }

    // // load initial data
    // private void loadData(int start, int end, boolean notifyadapter) {
        // for (int i = start; i <= end; i++) {
            // studentList.add(new cropdto("Student " + i, "androidstudent" + i + "@gmail.com"));
            // if (notifyadapter)
                // mAdapter.notifyItemInserted(studentList.size());
        // }
    // }


// }






