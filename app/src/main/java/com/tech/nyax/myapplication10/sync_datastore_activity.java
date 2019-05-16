package com.tech.nyax.myapplication10;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.net.Uri;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.HashMap;

/* import org.apache.http.NameValuePair;*/
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.support.v4.content.ContextCompat;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.Manifest;

public class sync_datastore_activity extends AppCompatActivity {

    private final static String TAG = sync_datastore_activity.class.getSimpleName();
    Button btncreatedatabase, btnimportdatabase, btnexportdatabase, btnsyncdatabase;
    TextView txtserveripaddress, txtserverport, txtdatabasename, lblmsg;
    private static final int REQUEST_CODE = 0x9345;
    private static final int IMPORT_DATABASE_REQUEST_CODE = 0x9363;
    private ProgressDialog simpleWaitDialog;
    // Create DatabasehelperUtilz class object in your activity.
    private DatabasehelperUtilz db;
    responsedto _responsedto = new responsedto();
    // Unique request code
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 0x98;
    // Unique request code
    private static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 0x99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sync_datastore_layout);

		/*final Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this will show the back arrow in the tool bar.

		/* CharSequence title = "crops";
		SpannableString s = new SpannableString(title);
		s.setSpan(new ForegroundColorSpan(Color.RED), 0, title.length(),
		Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); */

        getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));

//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(128, 0, 0, 0)));
        //getSupportActionBar().setBackgroundDrawable(R.drawable.edit);

        getSupportActionBar().setIcon(R.drawable.delete);
        getSupportActionBar().setLogo(R.drawable.edit);

        txtdatabasename = findViewById(R.id.txtdatabasename);

        lblmsg = findViewById(R.id.lblmsg);

        txtserveripaddress = findViewById(R.id.txtserveripaddress);
        txtserverport = findViewById(R.id.txtserverport);

        txtserveripaddress.setText(getResources().getString(R.string.txtserveripaddresshint));
        txtserverport.setText(getResources().getString(R.string.txtserverporthint));

        btncreatedatabase = findViewById(R.id.btncreatedatabase);
        btncreatedatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				
				lblmsg.setText("");
				String strmsg = "";
				if(txtdatabasename.getText() == null || txtdatabasename.getText().length() == 0){
					strmsg = "database name cannot be null."; 
				} 
				if(strmsg.length() > 0){
					utilz.getInstance(getApplicationContext()).globalloghandler(strmsg, TAG, 1, 0);
					lblmsg.setText(strmsg);
					return;
				}
					
                utilz.getInstance(getApplicationContext()).globalloghandler("create database task running...", TAG, 1, 1);

                String _database_name = txtdatabasename.getText().toString();

                new createDataBaseBackgroundTask().execute(_database_name);

            }
        });

        btnimportdatabase = findViewById(R.id.btnimportdatabase);
        btnimportdatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

				lblmsg.setText("");
                utilz.getInstance(getApplicationContext()).globalloghandler("import database task running...", TAG, 1, 1);

                showFileChooser();
                openGallery();
            }
        });

        btnexportdatabase = findViewById(R.id.btnexportdatabase);
        btnexportdatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

				lblmsg.setText("");
                utilz.getInstance(getApplicationContext()).globalloghandler("export database task running...", TAG, 1, 1);

                new exportDatabaseBackgroundTask().execute();

            }
        });


        btnsyncdatabase = findViewById(R.id.btnsyncdatabase);
        btnsyncdatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

				lblmsg.setText("");
                utilz.getInstance(getApplicationContext()).globalloghandler("sync database task running...", TAG, 1, 1);

                new syncDatabaseBackgroundTask().execute();

            }
        });

        check_Runtime_Permissions();

        hideSoftKeyboard();

    }

    public void openGallery() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                utilz.getInstance(getApplicationContext()).globalloghandler("READ_EXTERNAL_STORAGE permission required.", TAG, 1, 0);

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);

            } else {

                utilz.getInstance(getApplicationContext()).globalloghandler("READ_EXTERNAL_STORAGE permission granted.", TAG, 1, 1);

                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_database_to_import)), IMPORT_DATABASE_REQUEST_CODE);
            }
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    public void showFileChooser() {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            // Update with mime types
            intent.setType("*/*");
            // Update with additional mime types here using a String[].
            String[] mimeTypes = new String[]{"*/sqlite3"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            // Only pick openable and local files. Theoretically we could pull files from google drive
            // or other applications that have networked files, but that's unnecessary for this example.
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            // intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            // REQUEST_CODE = <some-integer>
            //startActivityForResult(intent, IMPORT_DATABASE_REQUEST_CODE);
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_database_to_import)), IMPORT_DATABASE_REQUEST_CODE);

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case IMPORT_DATABASE_REQUEST_CODE: {
                if (resultCode == RESULT_OK) {
                    if (null != data) {

                        // file selected
                        utilz.getInstance(getApplicationContext()).globalloghandler("file selected.", TAG, 1, 1);
                        //Import the file
                        importFile(data.getData());
						upload_imported_db(data.getData());
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    // Device is not discoverable
                    utilz.getInstance(getApplicationContext()).globalloghandler("no file selected.", TAG, 1, 0);
                }
                break;
            }
        }

        // Import the file
        //importFile(data.getData());
    }

	void upload_imported_db(Uri _uri){
		
      new importDatabaseBackgroundTask().execute(_uri);
	  
	}
	
    public void importFile(Uri uri) {
        try {
            String fileName = getFileName(uri);
            String tempFileName = "ntharenedbtempfile.sqlite3";
            // The temp file could be whatever you want
            File tempFile = createtmpfile();
            Log.e(TAG, "importFile: tempFile " + tempFile.toString());
            copyToTempFile(uri, tempFile);
            // Done!
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    public File createtmpfile() {

        String tempfilename = "ntharenedbtempfile.sqlite3";
        String tempfoldername = "/tempdatabases/";
        File newtempdir = null;
        File newtempfile = null;

        if (Environment.isExternalStorageEmulated()) {
                
				/* if you want to set backup in specific folder name
				be careful , foldername must initial like this : "/myFolder" . don't forget "/" at begin of folder name you could define foldername like this : "/myOutterFolder/MyInnerFolder" and so on */
            newtempdir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), tempfoldername);
            // if there was no folder at this path , it create it .

            try {
                if (!newtempdir.exists()) {
                    newtempdir.mkdirs();
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }

            newtempfile = new File(newtempdir, tempfilename);

            try {
                if (!newtempfile.exists()) {
                    newtempfile.createNewFile();
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }

        } else {

            newtempdir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), tempfoldername);
            // if there was no folder at this path , it create it .

            try {
                if (!newtempdir.exists()) {
                    newtempdir.mkdirs();
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }

            newtempfile = new File(newtempdir, tempfilename);

            try {
                if (!newtempfile.exists()) {
                    newtempfile.createNewFile();
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }

        }
        return newtempfile;
    }

    /**
     * Obtains the file name for a URI using content resolvers. Taken from the following link
     * https://developer.android.com/training/secure-file-sharing/retrieve-info.html#RetrieveFileInfo
     *
     * @param uri a uri to query
     * @return the file name with no path
     * @throws IllegalArgumentException if the query is null, empty, or the column doesn't exist
     */
    private String getFileName(Uri uri) throws IllegalArgumentException {
        // Obtain a cursor with information regarding this uri
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            throw new IllegalArgumentException("Can't obtain file name, cursor is empty");
        }
        cursor.moveToFirst();
        String fileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
        cursor.close();
        return fileName;
    }

    /**
     * Copies a uri reference to a temporary file
     *
     * @param uri      the uri used as the input stream
     * @param tempFile the file used as an output stream
     * @return the input tempFile for convenience
     */
    private void copyToTempFile(Uri uri, File tempFile) {
        File backup = new File(uri.getPath());
        expota_impota.getInstance(getApplicationContext()).importDatabasegivenbackuppath(backup, tempFile);
    }

    public void hideSoftKeyboard() {
        try {
            // InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            // inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    /* <Params, Progress, Result> */
    private class createDataBaseBackgroundTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            simpleWaitDialog = ProgressDialog.show(sync_datastore_activity.this, "creating database...", "excecuting task...");

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                DatabasehelperUtilz db = new DatabasehelperUtilz(getApplicationContext());

                String result = db.create_DataBase_given_name(params[0]);
                return result;
            } catch (IOException ex) {
                utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                return ex.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String _info = "\ncreate database task executed sucessfully...\n" + result;

            utilz.getInstance(getApplicationContext()).globalloghandler(_info, TAG, 1, 1, "sync_datastore_activity.onPostExecute", "sync_datastore_activity.onPostExecute");

            lblmsg.setText(_info);

            Log.e(TAG, "onPostExecute");
            simpleWaitDialog.dismiss();

        }

        @Override
        protected void onProgressUpdate(Void... Result) {
            super.onProgressUpdate(Result);
        }
    }

    /* <Params, Progress, Result> */
    private class exportDatabaseBackgroundTask extends AsyncTask<Void, Void, responsedto> {

        @Override
        protected void onPreExecute() {

            simpleWaitDialog = ProgressDialog.show(sync_datastore_activity.this, "exporting database...", "excecuting task...");

            super.onPreExecute();
        }

        @Override
        protected responsedto doInBackground(Void... params) {
            _responsedto = expota_impota.getInstance(getApplicationContext()).exportDatabase();
            return _responsedto;
        }

        @Override
        protected void onPostExecute(responsedto result) {
            super.onPostExecute(result);
            Log.e(TAG, "onPostExecute");
            simpleWaitDialog.dismiss();

            String _info = "export database task executed sucessfully...\n" + result.getresponsesuccessmessage() + "\n" + result.getresponseerrormessage();

            utilz.getInstance(getApplicationContext()).globalloghandler(_info, TAG, 1, 1, "sync_datastore_activity.onPostExecute", "sync_datastore_activity.onPostExecute");

            lblmsg.setText(_info);

        }

        @Override
        protected void onProgressUpdate(Void... progress) {
            super.onProgressUpdate(progress);
        }
    }

    /* <Params, Progress, Result> */
    private class importDatabaseBackgroundTask extends AsyncTask<Uri, Void, String> {

        @Override
        protected void onPreExecute() {

            simpleWaitDialog = ProgressDialog.show(sync_datastore_activity.this, "importing database...", "excecuting task...");

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Uri... params) {
            String _import_db = expota_impota.getInstance(getApplicationContext()).import_Database_given_uri(params[0]);
            return _import_db;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e(TAG, "onPostExecute");
            simpleWaitDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Void... progress) {
            super.onProgressUpdate(progress);
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

                    final Intent MainActivity = new Intent(this, MainActivity.class);
                    startActivity(MainActivity);
                    return true;
                default:
                    break;
            }
            return super.onOptionsItemSelected(item);
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 1);
            return false;
        }
    }

    /* <Params, Progress, Result> */
    private class syncDatabaseBackgroundTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {

            simpleWaitDialog = ProgressDialog.show(sync_datastore_activity.this, "synchronizing database...", "excecuting task...");

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result;

            result = synchronizerecordsfromservertodevice();

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e(TAG, "onPostExecute");
            simpleWaitDialog.dismiss();

            utilz.getInstance(getApplicationContext()).globalloghandler(result, TAG, 1, 1, "sync_datastore_activity.onPostExecute", "sync_datastore_activity.onPostExecute");

            lblmsg.setText(result);

        }

        @Override
        protected void onProgressUpdate(Void... result) {
            super.onProgressUpdate(result);
        }
    }

    public String synchronizerecordsfromservertodevice() {
        StringBuilder _result = new StringBuilder();
        try {

            _result.append(fetchcropsfromserver());
            _result.append(fetchmanufacturersfromserver());
            _result.append(fetchdiseasespestsfromserver());
            _result.append(fetchcategoriesfromserver());
            _result.append(fetchpestsinsecticidesfromserver());
            _result.append(fetchsettingsfromserver());
            _result.append(fetchcropsvarietiesfromserver());

            return _result.toString();

        } catch (Exception ex) {
            _result.append(utilz.getInstance(getApplicationContext()).format_spannable_error_string(ex.toString()).toString() + "\n");
            return _result.toString();
        }
    }

    String fetchcropsfromserver() {
        StringBuilder _result = new StringBuilder();
        try {
            // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds

            int CONNECTION_TIMEOUT = DBContract.synchronize_server_api.CONNECTION_TIMEOUT;
            int READ_TIMEOUT = DBContract.synchronize_server_api.READ_TIMEOUT;
            HttpURLConnection conn;
            URL url = null;
            String _url;

            String _ipaddress = txtserveripaddress.getText().toString();
            String _port = txtserverport.getText().toString();

            // Enter URL address where your php file resides

            //url = new URL("http://192.168.152.1:90/androidapi/get_all_crops.php");

            //_url = "http://" + _ipaddress + ":" + _port + "/androidapi/get_all_crops.php";

            _url = String.format("http://%s:%s/%s", _ipaddress, _port, DBContract.synchronize_server_api.get_all_crops);

            url = new URL(_url);

            // Setup HttpURLConnection class to send and receive data from php and mysql
            //call openConnection() on a URL instance. Since openConnection() returns a URLConnection, you need to explicitly cast the returned value
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("username", "username")
                    .appendQueryParameter("password", "password");
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                // Read data sent from server
                InputStream input = conn.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                StringBuilder _response = new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {
                    _response.append(line);
                    Log.e(TAG, "line [ " + line + " ]");

                    if (line.contains("<b")) {
                        _result.append(line + "\n");
                    }
                }

                Log.e(TAG, "_response [ " + _response.toString() + " ]");

                final List<cropdto> lst_dtos = new ArrayList<>();

                try {

                    JSONObject jsonObj = new JSONObject(_response.toString());
                    Log.e(TAG, "jsonObj [ " + jsonObj.toString() + " ]");

                    JSONObject _jobj = (JSONObject) jsonObj.get(DBContract.app_entities_wrapper.crops);
                    Log.e(TAG, "_jobj [ " + _jobj.toString() + " ]");

                    JSONArray _names = _jobj.names();
                    Log.e(TAG, "_names [ " + _names.toString() + " ]");
                    Log.e(TAG, "_names.length() [ " + _names.length() + " ]");

                    Iterator _keys = _jobj.keys();

                    while (_keys.hasNext()) {

                        // Loop to get the dynamic key
                        String currentDynamicKey = (String) _keys.next();
                        Log.e(TAG, "currentDynamicKey [ " + currentDynamicKey + " ]");

                        JSONObject _currentobj = (JSONObject) _jobj.get(currentDynamicKey);
                        Log.e(TAG, "_currentobj [ " + _currentobj.toString() + " ]");

                        Iterator _dto_obj_keys = _currentobj.keys();

                        int _pos = 0;

                        cropdto _currentdto = new cropdto();

                        while (_dto_obj_keys.hasNext()) {

                            // Loop to get the dynamic key
                            String _current_dto_obj_key = (String) _dto_obj_keys.next();
                            Log.e(TAG, "_current_dto_obj_key [ " + _current_dto_obj_key + " ]");

                            String _value = (String) _currentobj.get(_current_dto_obj_key);
                            Log.e(TAG, "_value [ " + _value.toString() + " ]");

                            _pos++;
                            Log.e(TAG, "_pos [ " + _pos + " ]");

                            switch (_current_dto_obj_key) {
                                case DBContract.cropsentitytable.CROP_ID:
                                    _currentdto.setcrop_Id(Long.valueOf(_value));
                                    break;
                                case DBContract.cropsentitytable.CROP_NAME:
                                    _currentdto.setcrop_name(_value);
                                    break;
                                case DBContract.cropsentitytable.CROP_STATUS:
                                    _currentdto.setcrop_status(_value);
                                    break;
                                case DBContract.cropsentitytable.CREATED_DATE:
                                    _currentdto.setcreated_date(_value);
                                    break;
                            }

                        }

                        lst_dtos.add(_currentdto);

                    }

                    Log.e(TAG, "lst_dtos.size() [ " + lst_dtos.size() + " ]");

                    _result.append(synchronizecropstodevice(lst_dtos));

                } catch (final Exception e) {
                    _result.append(utilz.getInstance(getApplicationContext()).format_spannable_error_string(e.getMessage()).toString() + "\n");
                    Log.e(TAG, e.toString());
                }


            } else {
                Log.e(TAG, "unsuccessful");
            }

            return _result.toString();

        } catch (Exception ex) {
            _result.append(utilz.getInstance(getApplicationContext()).format_spannable_error_string(ex.toString()).toString() + "\n");
            return _result.toString();
        }
    }

    String synchronizecropstodevice(List<cropdto> _lst_dtos) {
        StringBuilder _result = new StringBuilder();
        try {

            db = new DatabasehelperUtilz(sync_datastore_activity.this);
            db.openDataBase();

            int _existing_records = 0;
            int _inserted_records = 0;

            _result.append(DBContract.app_entities_wrapper.crops + "\n");

            _result.append("[ " + _lst_dtos.size() + " ] records retrieved from server.\n");

            for (int i = 0; i < _lst_dtos.size(); i++) {
                //get dto from server
                cropdto _dto_from_server = _lst_dtos.get(i);

                //check if dto exists in device 
                List<cropdto> _lst_filtered_dtos = db.filtercropsgivenname(_dto_from_server.getcrop_name());

                int _filter_count = _lst_filtered_dtos.size();

                Log.e(TAG, "_filter_count [ " + _filter_count + " ]");

                if (_lst_filtered_dtos.size() > 0) {
                    //record exists in device insert

                    cropdto _dto_in_device = _lst_filtered_dtos.get(0);

                    if (_dto_in_device == null) {
                        //record not in device insert
                        db.createcrop(_dto_from_server);
                        Log.e(TAG, "inserted crop [ " + _dto_from_server.getcrop_name() + " ]");
                        _result.append("inserted crop [ " + _dto_from_server.getcrop_name() + " ].\n");
                        _inserted_records++;
                    } else {
                        //record exists in device skip insert 
                        Log.e(TAG, "crop with name [ " + _dto_from_server.getcrop_name() + " ] exists in device.\n");
                        _result.append("crop with name [ " + _dto_from_server.getcrop_name() + " ] exists in device.\n");
                        _existing_records++;
                    }

                } else {
                    //record not in device insert
                    db.createcrop(_dto_from_server);
                    Log.e(TAG, "inserted crop [ " + _dto_from_server.getcrop_name() + " ]");
                    _result.append("inserted crop [ " + _dto_from_server.getcrop_name() + " ].\n");
                    _inserted_records++;
                }
            }

            db.close();

            _result.append("[ " + _existing_records + " ] records already exists in device.\n");
            _result.append("[ " + _inserted_records + " ] records inserted.\n");

            return _result.toString();

        } catch (Exception ex) {
            _result.append(utilz.getInstance(getApplicationContext()).format_spannable_error_string(ex.toString()).toString() + "\n");
            return _result.toString();
        }
    }

    String fetchcropsvarietiesfromserver() {
        StringBuilder _result = new StringBuilder();
        try {
            // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds

            int CONNECTION_TIMEOUT = DBContract.synchronize_server_api.CONNECTION_TIMEOUT;
            int READ_TIMEOUT = DBContract.synchronize_server_api.READ_TIMEOUT;
            HttpURLConnection conn;
            URL url = null;
            String _url;

            String _ipaddress = txtserveripaddress.getText().toString();
            String _port = txtserverport.getText().toString();

            // Enter URL address where your php file resides

            _url = String.format("http://%s:%s/%s", _ipaddress, _port, DBContract.synchronize_server_api.get_all_cropsvarieties);

            url = new URL(_url);

            // Setup HttpURLConnection class to send and receive data from php and mysql
            //call openConnection() on a URL instance. Since openConnection() returns a URLConnection, you need to explicitly cast the returned value
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("username", "username")
                    .appendQueryParameter("password", "password");
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

            int response_code = conn.getResponseCode();

            switch (response_code) {
                case 200:
                    //all went ok - read response
                    break;
                case 301:
                case 302:
                case 307:
                    //handle redirect - for example, re-post to the new location
                    break;
                default:
                    //do something sensible
            }

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                // Read data sent from server
                InputStream input = conn.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                StringBuilder _response = new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {
                    _response.append(line);
                    Log.e(TAG, "line [ " + line + " ]");

                    if (line.contains("<b")) {
                        _result.append(line + "\n");
                    }
                }

                Log.e(TAG, "_response [ " + _response.toString() + " ]");

                final List<cropvarietydto> lst_dtos = new ArrayList<>();

                try {

                    JSONObject jsonObj = new JSONObject(_response.toString());
                    Log.e(TAG, "jsonObj [ " + jsonObj.toString() + " ]");

                    JSONObject _jobj = (JSONObject) jsonObj.get(DBContract.app_entities_wrapper.cropsvarieties);
                    Log.e(TAG, "_jobj [ " + _jobj.toString() + " ]");

                    JSONArray _names = _jobj.names();
                    Log.e(TAG, "_names [ " + _names.toString() + " ]");
                    Log.e(TAG, "_names.length() [ " + _names.length() + " ]");

                    Iterator _keys = _jobj.keys();

                    while (_keys.hasNext()) {

                        // Loop to get the dynamic key
                        String currentDynamicKey = (String) _keys.next();
                        Log.e(TAG, "currentDynamicKey [ " + currentDynamicKey + " ]");

                        JSONObject _currentobj = (JSONObject) _jobj.get(currentDynamicKey);
                        Log.e(TAG, "_currentobj [ " + _currentobj.toString() + " ]");

                        Iterator _dto_obj_keys = _currentobj.keys();

                        int _pos = 0;

                        cropvarietydto _currentdto = new cropvarietydto();

                        while (_dto_obj_keys.hasNext()) {

                            // Loop to get the dynamic key
                            String _current_dto_obj_key = (String) _dto_obj_keys.next();
                            Log.e(TAG, "_current_dto_obj_key [ " + _current_dto_obj_key + " ]");

                            String _value = (String) _currentobj.get(_current_dto_obj_key);
                            Log.e(TAG, "_value [ " + _value.toString() + " ]");

                            _pos++;
                            Log.e(TAG, "_pos [ " + _pos + " ]");

                            switch (_current_dto_obj_key) {
                                case DBContract.cropsvarietiesentitytable.CROP_VARIETY_ID:
                                    _currentdto.setcropvariety_Id(Long.valueOf(_value));
                                    break;
                                case DBContract.cropsvarietiesentitytable.CROP_VARIETY_NAME:
                                    _currentdto.setcropvariety_name(_value);
                                    break;
                                case DBContract.cropsvarietiesentitytable.CROP_VARIETY_STATUS:
                                    _currentdto.setcropvariety_status(_value);
                                    break;
                                case DBContract.cropsvarietiesentitytable.CROP_VARIETY_CROP_ID:
                                    _currentdto.setcropvariety_crop_id(_value);
                                    break;
                                case DBContract.cropsvarietiesentitytable.CROP_VARIETY_MANUFACTURER_ID:
                                    _currentdto.setcropvariety_manufacturer_id(_value);
                                    break;
                                case DBContract.cropsvarietiesentitytable.CREATED_DATE:
                                    _currentdto.setcreated_date(_value);
                                    break;
                            }

                        }

                        lst_dtos.add(_currentdto);

                    }

                    Log.e(TAG, "lst_dtos.size() [ " + lst_dtos.size() + " ]");

                    _result.append(synchronizecropsvarietiestodevice(lst_dtos));

                } catch (final Exception e) {
                    _result.append(utilz.getInstance(getApplicationContext()).format_spannable_error_string(e.getMessage()).toString() + "\n");
                    Log.e(TAG, e.toString());
                }

            } else {
                Log.e(TAG, "unsuccessful");
            }

            return _result.toString();

        } catch (Exception ex) {
            _result.append(utilz.getInstance(getApplicationContext()).format_spannable_error_string(ex.toString()).toString() + "\n");
            return _result.toString();
        }
    }

    String synchronizecropsvarietiestodevice(List<cropvarietydto> _lst_dtos) {
        StringBuilder _result = new StringBuilder();
        try {

            db = new DatabasehelperUtilz(sync_datastore_activity.this);
            db.openDataBase();

            int _existing_records = 0;
            int _inserted_records = 0;

            _result.append(DBContract.app_entities_wrapper.cropsvarieties + "\n");

            _result.append("[ " + _lst_dtos.size() + " ] records retrieved from server.\n");

            for (int i = 0; i < _lst_dtos.size(); i++) {
                //get dto from server
                cropvarietydto _dto_from_server = _lst_dtos.get(i);

                //check if dto exists in device 
                List<cropvarietydto> _lst_filtered_dtos = db.filtercropsvarietiesgivenname(_dto_from_server.getcropvariety_name());

                int _filter_count = _lst_filtered_dtos.size();

                Log.e(TAG, "_filter_count [ " + _filter_count + " ]");

                if (_lst_filtered_dtos.size() > 0) {
                    //record exists in device insert

                    cropvarietydto _dto_in_device = _lst_filtered_dtos.get(0);

                    if (_dto_in_device == null) {
                        //record not in device insert
                        db.createcropvariety(_dto_from_server);
                        Log.e(TAG, "inserted record [ " + _dto_from_server.getcropvariety_name() + " ]");
                        _inserted_records++;
                    } else {
                        //record exists in device skip insert
                        Log.e(TAG, "record with name [ " + _dto_from_server.getcropvariety_name() + " ] exists in device");
                        _existing_records++;
                    }

                } else {
                    //record not in device insert
                    db.createcropvariety(_dto_from_server);
                    Log.e(TAG, "inserted record [ " + _dto_from_server.getcropvariety_name() + " ]");
                    _inserted_records++;
                }
            }

            db.close();

            _result.append("[ " + _existing_records + " ] records already exists in device.\n");
            _result.append("[ " + _inserted_records + " ] records inserted.\n");

            return _result.toString();

        } catch (Exception ex) {
            _result.append(utilz.getInstance(getApplicationContext()).format_spannable_error_string(ex.toString()).toString() + "\n");
            return _result.toString();
        }
    }

    String fetchmanufacturersfromserver() {
        StringBuilder _result = new StringBuilder();
        try {
            // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds

            int CONNECTION_TIMEOUT = DBContract.synchronize_server_api.CONNECTION_TIMEOUT;
            int READ_TIMEOUT = DBContract.synchronize_server_api.READ_TIMEOUT;
            HttpURLConnection conn;
            URL url = null;
            String _url;

            String _ipaddress = txtserveripaddress.getText().toString();
            String _port = txtserverport.getText().toString();

            // Enter URL address where your php file resides

            //url = new URL("http://192.168.152.1:90/androidapi/get_all_manufacturers.php");

            //_url = "http://" + _ipaddress + ":" + _port + "/androidapi/get_all_manufacturers.php";

            _url = String.format("http://%s:%s/%s", _ipaddress, _port, DBContract.synchronize_server_api.get_all_manufacturers);

            url = new URL(_url);

            // Setup HttpURLConnection class to send and receive data from php and mysql
            //call openConnection() on a URL instance. Since openConnection() returns a URLConnection, you need to explicitly cast the returned value
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("username", "username")
                    .appendQueryParameter("password", "password");
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                // Read data sent from server
                InputStream input = conn.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                StringBuilder _response = new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {
                    _response.append(line);
                    Log.e(TAG, "line [ " + line + " ]");

                    if (line.contains("<b")) {
                        _result.append(line + "\n");
                    }
                }

                Log.e(TAG, "_response [ " + _response.toString() + " ]");

                final List<manufacturerdto> lst_dtos = new ArrayList<>();

                try {

                    JSONObject jsonObj = new JSONObject(_response.toString());
                    Log.e(TAG, "jsonObj [ " + jsonObj.toString() + " ]");

                    JSONObject _jobj = (JSONObject) jsonObj.get(DBContract.app_entities_wrapper.manufacturers);
                    Log.e(TAG, "_jobj [ " + _jobj.toString() + " ]");

                    JSONArray _names = _jobj.names();
                    Log.e(TAG, "_names [ " + _names.toString() + " ]");
                    Log.e(TAG, "_names.length() [ " + _names.length() + " ]");

                    Iterator _keys = _jobj.keys();

                    while (_keys.hasNext()) {

                        // Loop to get the dynamic key
                        String currentDynamicKey = (String) _keys.next();
                        Log.e(TAG, "currentDynamicKey [ " + currentDynamicKey + " ]");

                        JSONObject _currentobj = (JSONObject) _jobj.get(currentDynamicKey);
                        Log.e(TAG, "_currentobj [ " + _currentobj.toString() + " ]");

                        Iterator _dto_obj_keys = _currentobj.keys();

                        int _pos = 0;

                        manufacturerdto _currentdto = new manufacturerdto();

                        while (_dto_obj_keys.hasNext()) {

                            // Loop to get the dynamic key
                            String _current_dto_obj_key = (String) _dto_obj_keys.next();
                            Log.e(TAG, "_current_dto_obj_key [ " + _current_dto_obj_key + " ]");

                            String _value = (String) _currentobj.get(_current_dto_obj_key);
                            Log.e(TAG, "_value [ " + _value.toString() + " ]");

                            _pos++;
                            Log.e(TAG, "_pos [ " + _pos + " ]");

                            switch (_current_dto_obj_key) {
                                case DBContract.manufacturersentitytable.MANUFACTURER_ID:
                                    _currentdto.setmanufacturer_Id(Long.valueOf(_value));
                                    break;
                                case DBContract.manufacturersentitytable.MANUFACTURER_NAME:
                                    _currentdto.setmanufacturer_name(_value);
                                    break;
                                case DBContract.manufacturersentitytable.MANUFACTURER_STATUS:
                                    _currentdto.setmanufacturer_status(_value);
                                    break;
                                case DBContract.manufacturersentitytable.CREATED_DATE:
                                    _currentdto.setcreated_date(_value);
                                    break;
                            }

                        }

                        lst_dtos.add(_currentdto);

                    }

                    Log.e(TAG, "lst_dtos.size() [ " + lst_dtos.size() + " ]");

                    _result.append(synchronizemanufacturerstodevice(lst_dtos));

                } catch (final Exception e) {
                    _result.append(utilz.getInstance(getApplicationContext()).format_spannable_error_string(e.getMessage()).toString() + "\n");
                    Log.e(TAG, e.toString());
                }


            } else {
                Log.e(TAG, "unsuccessful");
            }

            return _result.toString();

        } catch (Exception ex) {
            _result.append(utilz.getInstance(getApplicationContext()).format_spannable_error_string(ex.toString()).toString() + "\n");
            return _result.toString();
        }
    }

    String synchronizemanufacturerstodevice(List<manufacturerdto> _lst_dtos) {
        StringBuilder _result = new StringBuilder();
        try {

            db = new DatabasehelperUtilz(sync_datastore_activity.this);
            db.openDataBase();

            int _existing_records = 0;
            int _inserted_records = 0;

            _result.append(DBContract.app_entities_wrapper.manufacturers + "\n");

            _result.append("[ " + _lst_dtos.size() + " ] records retrieved from server.\n");

            for (int i = 0; i < _lst_dtos.size(); i++) {
                //get dto from server
                manufacturerdto _dto_from_server = _lst_dtos.get(i);

                //check if dto exists in device 
                List<manufacturerdto> _lst_filtered_dtos = db.filtermanufacturersgivenname(_dto_from_server.getmanufacturer_name());

                int _filter_count = _lst_filtered_dtos.size();

                Log.e(TAG, "_filter_count [ " + _filter_count + " ]");

                if (_lst_filtered_dtos.size() > 0) {
                    //record exists in device insert

                    manufacturerdto _dto_in_device = _lst_filtered_dtos.get(0);

                    if (_dto_in_device == null) {
                        //record not in device insert
                        db.createmanufacturer(_dto_from_server);
                        Log.e(TAG, "inserted manufacturer [ " + _dto_from_server.getmanufacturer_name() + " ]");
                        _result.append("inserted manufacturer [ " + _dto_from_server.getmanufacturer_name() + " ].\n");
                        _inserted_records++;
                    } else {
                        //record exists in device skip insert
                        Log.e(TAG, "manufacturer with name [ " + _dto_from_server.getmanufacturer_name() + " ] exists in device.\n");
                        _result.append("manufacturer with name [ " + _dto_from_server.getmanufacturer_name() + " ] exists in device.\n");
                        _existing_records++;
                    }

                } else {
                    //record not in device insert
                    db.createmanufacturer(_dto_from_server);
                    Log.e(TAG, "inserted manufacturer [ " + _dto_from_server.getmanufacturer_name() + " ]");
                    _result.append("inserted manufacturer [ " + _dto_from_server.getmanufacturer_name() + " ].\n");
                    _inserted_records++;
                }
            }

            db.close();

            _result.append("[ " + _existing_records + " ] records already exists in device.\n");
            _result.append("[ " + _inserted_records + " ] records inserted.\n");

            return _result.toString();

        } catch (Exception ex) {
            _result.append(utilz.getInstance(getApplicationContext()).format_spannable_error_string(ex.toString()).toString() + "\n");
            return _result.toString();
        }
    }

    String fetchdiseasespestsfromserver() {
        StringBuilder _result = new StringBuilder();
        try {
            // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds

            int CONNECTION_TIMEOUT = DBContract.synchronize_server_api.CONNECTION_TIMEOUT;
            int READ_TIMEOUT = DBContract.synchronize_server_api.READ_TIMEOUT;
            HttpURLConnection conn;
            URL url = null;
            String _url;

            String _ipaddress = txtserveripaddress.getText().toString();
            String _port = txtserverport.getText().toString();

            // Enter URL address where your php file resides

            //url = new URL("http://192.168.152.1:90/androidapi/get_all_diseasespests.php");

            //_url = "http://" + _ipaddress + ":" + _port + "/androidapi/get_all_diseasespests.php";

            _url = String.format("http://%s:%s/%s", _ipaddress, _port, DBContract.synchronize_server_api.get_all_diseasespests);

            url = new URL(_url);

            // Setup HttpURLConnection class to send and receive data from php and mysql
            //call openConnection() on a URL instance. Since openConnection() returns a URLConnection, you need to explicitly cast the returned value
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("username", "username")
                    .appendQueryParameter("password", "password");
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                // Read data sent from server
                InputStream input = conn.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                StringBuilder _response = new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {
                    _response.append(line);
                    Log.e(TAG, "line [ " + line + " ]");

                    if (line.contains("<b")) {
                        _result.append(line + "\n");
                    }
                }

                Log.e(TAG, "_response [ " + _response.toString() + " ]");

                final List<cropdiseasedto> lst_dtos = new ArrayList<>();

                try {

                    JSONObject jsonObj = new JSONObject(_response.toString());
                    Log.e(TAG, "jsonObj [ " + jsonObj.toString() + " ]");

                    JSONObject _jobj = (JSONObject) jsonObj.get(DBContract.app_entities_wrapper.diseasespests);
                    Log.e(TAG, "_jobj [ " + _jobj.toString() + " ]");

                    JSONArray _names = _jobj.names();
                    Log.e(TAG, "_names [ " + _names.toString() + " ]");
                    Log.e(TAG, "_names.length() [ " + _names.length() + " ]");

                    Iterator _keys = _jobj.keys();

                    while (_keys.hasNext()) {

                        // Loop to get the dynamic key
                        String currentDynamicKey = (String) _keys.next();
                        Log.e(TAG, "currentDynamicKey [ " + currentDynamicKey + " ]");

                        JSONObject _currentobj = (JSONObject) _jobj.get(currentDynamicKey);
                        Log.e(TAG, "_currentobj [ " + _currentobj.toString() + " ]");

                        Iterator _dto_obj_keys = _currentobj.keys();

                        int _pos = 0;

                        cropdiseasedto _currentdto = new cropdiseasedto();

                        while (_dto_obj_keys.hasNext()) {

                            // Loop to get the dynamic key
                            String _current_dto_obj_key = (String) _dto_obj_keys.next();
                            Log.e(TAG, "_current_dto_obj_key [ " + _current_dto_obj_key + " ]");

                            String _value = (String) _currentobj.get(_current_dto_obj_key);
                            Log.e(TAG, "_value [ " + _value.toString() + " ]");

                            _pos++;
                            Log.e(TAG, "_pos [ " + _pos + " ]");

                            switch (_current_dto_obj_key) {
                                case DBContract.cropsdiseasesentitytable.CROP_DISEASE_ID:
                                    _currentdto.setcropdisease_Id(Long.valueOf(_value));
                                    break;
                                case DBContract.cropsdiseasesentitytable.CROP_DISEASE_NAME:
                                    _currentdto.setcropdisease_name(_value);
                                    break;
                                case DBContract.cropsdiseasesentitytable.CROP_DISEASE_CATEGORY:
                                    _currentdto.setcropdisease_category(_value);
                                    break;
                                case DBContract.cropsdiseasesentitytable.CROP_DISEASE_STATUS:
                                    _currentdto.setcropdisease_status(_value);
                                    break;
                                case DBContract.cropsdiseasesentitytable.CREATED_DATE:
                                    _currentdto.setcreated_date(_value);
                                    break;
                            }

                        }

                        lst_dtos.add(_currentdto);

                    }

                    Log.e(TAG, "lst_dtos.size() [ " + lst_dtos.size() + " ]");

                    _result.append(synchronizediseasespeststodevice(lst_dtos));

                } catch (final Exception e) {
                    _result.append(utilz.getInstance(getApplicationContext()).format_spannable_error_string(e.getMessage()).toString() + "\n");
                    Log.e(TAG, e.toString());
                }


            } else {
                Log.e(TAG, "unsuccessful");
            }

            return _result.toString();

        } catch (Exception ex) {
            _result.append(utilz.getInstance(getApplicationContext()).format_spannable_error_string(ex.toString()).toString() + "\n");
            return _result.toString();
        }
    }

    String synchronizediseasespeststodevice(List<cropdiseasedto> _lst_dtos) {
        StringBuilder _result = new StringBuilder();
        try {

            db = new DatabasehelperUtilz(sync_datastore_activity.this);
            db.openDataBase();

            int _existing_records = 0;
            int _inserted_records = 0;

            _result.append(DBContract.app_entities_wrapper.diseasespests + "\n");

            _result.append("[ " + _lst_dtos.size() + " ] records retrieved from server.\n");

            for (int i = 0; i < _lst_dtos.size(); i++) {
                //get dto from server
                cropdiseasedto _dto_from_server = _lst_dtos.get(i);

                //check if dto exists in device 
                List<cropdiseasedto> _lst_filtered_dtos = db.filtercropsdiseasesgivenname(_dto_from_server.getcropdisease_name());

                int _filter_count = _lst_filtered_dtos.size();

                Log.e(TAG, "_filter_count [ " + _filter_count + " ]");

                if (_lst_filtered_dtos.size() > 0) {
                    //record exists in device insert

                    cropdiseasedto _dto_in_device = _lst_filtered_dtos.get(0);

                    if (_dto_in_device == null) {
                        //record not in device insert
                        db.createcropdisease(_dto_from_server);
                        Log.e(TAG, "inserted disease/pest [ " + _dto_from_server.getcropdisease_name() + " ]");
                        _result.append("inserted disease/pest [ " + _dto_from_server.getcropdisease_name() + " ].\n");
                        _inserted_records++;
                    } else {
                        //record exists in device skip insert 
                        Log.e(TAG, "disease/pest with name [ " + _dto_from_server.getcropdisease_name() + " ] exists in device.\n");
                        _result.append("disease/pest with name [ " + _dto_from_server.getcropdisease_name() + " ] exists in device.\n");
                        _existing_records++;
                    }

                } else {
                    //record not in device insert
                    db.createcropdisease(_dto_from_server);
                    Log.e(TAG, "inserted disease/pest [ " + _dto_from_server.getcropdisease_name() + " ]");
                    _result.append("inserted disease/pest [ " + _dto_from_server.getcropdisease_name() + " ].\n");
                    _inserted_records++;
                }
            }

            db.close();

            _result.append("[ " + _existing_records + " ] records already exists in device.\n");
            _result.append("[ " + _inserted_records + " ] records inserted.\n");

            return _result.toString();

        } catch (Exception ex) {
            _result.append(utilz.getInstance(getApplicationContext()).format_spannable_error_string(ex.toString()).toString() + "\n");
            return _result.toString();
        }
    }

    String fetchcategoriesfromserver() {
        StringBuilder _result = new StringBuilder();
        try {
            // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds

            int CONNECTION_TIMEOUT = DBContract.synchronize_server_api.CONNECTION_TIMEOUT;
            int READ_TIMEOUT = DBContract.synchronize_server_api.READ_TIMEOUT;
            HttpURLConnection conn;
            URL url = null;
            String _url;

            String _ipaddress = txtserveripaddress.getText().toString();
            String _port = txtserverport.getText().toString();

            // Enter URL address where your php file resides

            //url = new URL("http://192.168.152.1:90/androidapi/get_all_categories.php");

            //_url = "http://" + _ipaddress + ":" + _port + "/androidapi/get_all_categories.php";

            _url = String.format("http://%s:%s/%s", _ipaddress, _port, DBContract.synchronize_server_api.get_all_categories);

            url = new URL(_url);

            // Setup HttpURLConnection class to send and receive data from php and mysql
            //call openConnection() on a URL instance. Since openConnection() returns a URLConnection, you need to explicitly cast the returned value
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("username", "username")
                    .appendQueryParameter("password", "password");
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                // Read data sent from server
                InputStream input = conn.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                StringBuilder _response = new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {
                    _response.append(line);
                    Log.e(TAG, "line [ " + line + " ]");

                    if (line.contains("<b")) {
                        _result.append(line + "\n");
                    }
                }

                Log.e(TAG, "_response [ " + _response.toString() + " ]");

                final List<categorydto> lst_dtos = new ArrayList<>();

                try {

                    JSONObject jsonObj = new JSONObject(_response.toString());
                    Log.e(TAG, "jsonObj [ " + jsonObj.toString() + " ]");

                    JSONObject _jobj = (JSONObject) jsonObj.get(DBContract.app_entities_wrapper.categories);
                    Log.e(TAG, "_jobj [ " + _jobj.toString() + " ]");

                    JSONArray _names = _jobj.names();
                    Log.e(TAG, "_names [ " + _names.toString() + " ]");
                    Log.e(TAG, "_names.length() [ " + _names.length() + " ]");

                    Iterator _keys = _jobj.keys();

                    while (_keys.hasNext()) {

                        // Loop to get the dynamic key
                        String currentDynamicKey = (String) _keys.next();
                        Log.e(TAG, "currentDynamicKey [ " + currentDynamicKey + " ]");

                        JSONObject _currentobj = (JSONObject) _jobj.get(currentDynamicKey);
                        Log.e(TAG, "_currentobj [ " + _currentobj.toString() + " ]");

                        Iterator _dto_obj_keys = _currentobj.keys();

                        int _pos = 0;

                        categorydto _currentdto = new categorydto();

                        while (_dto_obj_keys.hasNext()) {

                            // Loop to get the dynamic key
                            String _current_dto_obj_key = (String) _dto_obj_keys.next();
                            Log.e(TAG, "_current_dto_obj_key [ " + _current_dto_obj_key + " ]");

                            String _value = (String) _currentobj.get(_current_dto_obj_key);
                            Log.e(TAG, "_value [ " + _value.toString() + " ]");

                            _pos++;
                            Log.e(TAG, "_pos [ " + _pos + " ]");

                            switch (_current_dto_obj_key) {
                                case DBContract.categoriesentitytable.CATEGORY_ID:
                                    _currentdto.setcategory_Id(Long.valueOf(_value));
                                    break;
                                case DBContract.categoriesentitytable.CATEGORY_NAME:
                                    _currentdto.setcategory_name(_value);
                                    break;
                                case DBContract.categoriesentitytable.CATEGORY_STATUS:
                                    _currentdto.setcategory_status(_value);
                                    break;
                                case DBContract.categoriesentitytable.CREATED_DATE:
                                    _currentdto.setcreated_date(_value);
                                    break;
                            }

                        }

                        lst_dtos.add(_currentdto);

                    }

                    Log.e(TAG, "lst_dtos.size() [ " + lst_dtos.size() + " ]");

                    _result.append(synchronizecategoriestodevice(lst_dtos));

                } catch (final Exception e) {
                    _result.append(utilz.getInstance(getApplicationContext()).format_spannable_error_string(e.getMessage()).toString() + "\n");
                    Log.e(TAG, e.toString());
                }


            } else {
                Log.e(TAG, "unsuccessful");
            }

            return _result.toString();

        } catch (Exception ex) {
            _result.append(utilz.getInstance(getApplicationContext()).format_spannable_error_string(ex.toString()).toString() + "\n");
            return _result.toString();
        }
    }

    String synchronizecategoriestodevice(List<categorydto> _lst_dtos) {
        StringBuilder _result = new StringBuilder();
        try {

            db = new DatabasehelperUtilz(sync_datastore_activity.this);
            db.openDataBase();

            int _existing_records = 0;
            int _inserted_records = 0;

            _result.append(DBContract.app_entities_wrapper.categories + "\n");

            _result.append("[ " + _lst_dtos.size() + " ] records retrieved from server.\n");

            for (int i = 0; i < _lst_dtos.size(); i++) {
                //get dto from server
                categorydto _dto_from_server = _lst_dtos.get(i);

                //check if dto exists in device 
                List<categorydto> _lst_filtered_dtos = db.filtercategoriesgivenname(_dto_from_server.getcategory_name());

                int _filter_count = _lst_filtered_dtos.size();

                Log.e(TAG, "_filter_count [ " + _filter_count + " ]");

                if (_lst_filtered_dtos.size() > 0) {
                    //record exists in device insert

                    categorydto _dto_in_device = _lst_filtered_dtos.get(0);

                    if (_dto_in_device == null) {
                        //record not in device insert
                        db.createcategory(_dto_from_server);
                        Log.e(TAG, "inserted category [ " + _dto_from_server.getcategory_name() + " ]");
                        _result.append("inserted category [ " + _dto_from_server.getcategory_name() + " ].\n");
                        _inserted_records++;
                    } else {
                        //record exists in device skip insert 
                        Log.e(TAG, "category with name [ " + _dto_from_server.getcategory_name() + " ] exists in device.\n");
                        _result.append("category with name [ " + _dto_from_server.getcategory_name() + " ] exists in device.\n");
                        _existing_records++;
                    }

                } else {
                    //record not in device insert
                    db.createcategory(_dto_from_server);
                    Log.e(TAG, "inserted category [ " + _dto_from_server.getcategory_name() + " ]");
                    _result.append("inserted category [ " + _dto_from_server.getcategory_name() + " ].\n");
                    _inserted_records++;
                }
            }

            db.close();

            _result.append("[ " + _existing_records + " ] records already exists in device.\n");
            _result.append("[ " + _inserted_records + " ] records inserted.\n");

            return _result.toString();

        } catch (Exception ex) {
            _result.append(utilz.getInstance(getApplicationContext()).format_spannable_error_string(ex.toString()).toString() + "\n");
            return _result.toString();
        }
    }

    String fetchpestsinsecticidesfromserver() {
        StringBuilder _result = new StringBuilder();
        try {
            // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds

            int CONNECTION_TIMEOUT = DBContract.synchronize_server_api.CONNECTION_TIMEOUT;
            int READ_TIMEOUT = DBContract.synchronize_server_api.READ_TIMEOUT;
            HttpURLConnection conn;
            URL url = null;
            String _url;

            String _ipaddress = txtserveripaddress.getText().toString();
            String _port = txtserverport.getText().toString();

            // Enter URL address where your php file resides

            //url = new URL("http://192.168.152.1:90/androidapi/get_all_pestsinsecticides.php");

            //_url = "http://" + _ipaddress + ":" + _port + "/androidapi/get_all_pestsinsecticides.php";

            _url = String.format("http://%s:%s/%s", _ipaddress, _port, DBContract.synchronize_server_api.get_all_pestsinsecticides);

            url = new URL(_url);

            // Setup HttpURLConnection class to send and receive data from php and mysql
            //call openConnection() on a URL instance. Since openConnection() returns a URLConnection, you need to explicitly cast the returned value
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("username", "username")
                    .appendQueryParameter("password", "password");
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                // Read data sent from server
                InputStream input = conn.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                StringBuilder _response = new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {
                    _response.append(line);
                    Log.e(TAG, "line [ " + line + " ]");

                    if (line.contains("<b")) {
                        _result.append(line + "\n");
                    }
                }

                Log.e(TAG, "_response [ " + _response.toString() + " ]");

                final List<pestinsecticidedto> lst_dtos = new ArrayList<>();

                try {

                    JSONObject jsonObj = new JSONObject(_response.toString());
                    Log.e(TAG, "jsonObj [ " + jsonObj.toString() + " ]");

                    JSONObject _jobj = (JSONObject) jsonObj.get(DBContract.app_entities_wrapper.pestsinsecticides);
                    Log.e(TAG, "_jobj [ " + _jobj.toString() + " ]");

                    JSONArray _names = _jobj.names();
                    Log.e(TAG, "_names [ " + _names.toString() + " ]");
                    Log.e(TAG, "_names.length() [ " + _names.length() + " ]");

                    Iterator _keys = _jobj.keys();

                    while (_keys.hasNext()) {

                        // Loop to get the dynamic key
                        String currentDynamicKey = (String) _keys.next();
                        Log.e(TAG, "currentDynamicKey [ " + currentDynamicKey + " ]");

                        JSONObject _currentobj = (JSONObject) _jobj.get(currentDynamicKey);
                        Log.e(TAG, "_currentobj [ " + _currentobj.toString() + " ]");

                        Iterator _dto_obj_keys = _currentobj.keys();

                        int _pos = 0;

                        pestinsecticidedto _currentdto = new pestinsecticidedto();

                        while (_dto_obj_keys.hasNext()) {

                            // Loop to get the dynamic key
                            String _current_dto_obj_key = (String) _dto_obj_keys.next();
                            Log.e(TAG, "_current_dto_obj_key [ " + _current_dto_obj_key + " ]");

                            String _value = (String) _currentobj.get(_current_dto_obj_key);
                            Log.e(TAG, "_value [ " + _value.toString() + " ]");

                            _pos++;
                            Log.e(TAG, "_pos [ " + _pos + " ]");

                            switch (_current_dto_obj_key) {
                                case DBContract.pestsinsecticidesentitytable.PESTINSECTICIDE_ID:
                                    _currentdto.setpestinsecticide_Id(Long.valueOf(_value));
                                    break;
                                case DBContract.pestsinsecticidesentitytable.PESTINSECTICIDE_NAME:
                                    _currentdto.setpestinsecticide_name(_value);
                                    break;
                                case DBContract.pestsinsecticidesentitytable.PESTINSECTICIDE_CATEGORY:
                                    _currentdto.setpestinsecticide_category(_value);
                                    break;
                                case DBContract.pestsinsecticidesentitytable.PESTINSECTICIDE_STATUS:
                                    _currentdto.setpestinsecticide_status(_value);
                                    break;
                                case DBContract.pestsinsecticidesentitytable.CREATED_DATE:
                                    _currentdto.setcreated_date(_value);
                                    break;
                                case DBContract.pestsinsecticidesentitytable.PESTINSECTICIDE_CROP_DISEASE_ID:
                                    _currentdto.setpestinsecticide_crop_disease_id(_value);
                                    break;
                                case DBContract.pestsinsecticidesentitytable.PESTINSECTICIDE_MANUFACTURER_ID:
                                    _currentdto.setpestinsecticide_manufacturer_id(_value);
                                    break;
                            }

                        }

                        lst_dtos.add(_currentdto);

                    }

                    Log.e(TAG, "lst_dtos.size() [ " + lst_dtos.size() + " ]");

                    _result.append(synchronizepestsinsecticidestodevice(lst_dtos));

                } catch (final Exception e) {
                    _result.append(utilz.getInstance(getApplicationContext()).format_spannable_error_string(e.getMessage()).toString() + "\n");
                    Log.e(TAG, e.toString());
                }


            } else {
                Log.e(TAG, "unsuccessful");
            }

            return _result.toString();

        } catch (Exception ex) {
            _result.append(utilz.getInstance(getApplicationContext()).format_spannable_error_string(ex.toString()).toString() + "\n");
            return _result.toString();
        }
    }

    String synchronizepestsinsecticidestodevice(List<pestinsecticidedto> _lst_dtos) {
        StringBuilder _result = new StringBuilder();
        try {

            db = new DatabasehelperUtilz(sync_datastore_activity.this);
            db.openDataBase();

            int _existing_records = 0;
            int _inserted_records = 0;

            _result.append(DBContract.app_entities_wrapper.pestsinsecticides + "\n");

            _result.append("[ " + _lst_dtos.size() + " ] records retrieved from server.\n");

            for (int i = 0; i < _lst_dtos.size(); i++) {
                //get dto from server
                pestinsecticidedto _dto_from_server = _lst_dtos.get(i);

                //check if dto exists in device 
                List<pestinsecticidedto> _lst_filtered_dtos = db.filterpestsinsecticidesgivenname(_dto_from_server.getpestinsecticide_name());

                int _filter_count = _lst_filtered_dtos.size();

                Log.e(TAG, "_filter_count [ " + _filter_count + " ]");

                if (_lst_filtered_dtos.size() > 0) {
                    //record exists in device insert

                    pestinsecticidedto _dto_in_device = _lst_filtered_dtos.get(0);

                    if (_dto_in_device == null) {
                        //record not in device insert
                        db.createpestinsecticide(_dto_from_server);
                        Log.e(TAG, "inserted pesticide/insecticide [ " + _dto_from_server.getpestinsecticide_name() + " ]");
                        _result.append("inserted pesticide/insecticide [ " + _dto_from_server.getpestinsecticide_name() + " ].\n");
                        _inserted_records++;
                    } else {
                        //record exists in device skip insert 
                        Log.e(TAG, "pesticide/insecticide with name [ " + _dto_from_server.getpestinsecticide_name() + " ] exists in device.\n");
                        _result.append("pesticide/insecticide with name [ " + _dto_from_server.getpestinsecticide_name() + " ] exists in device.\n");
                        _existing_records++;
                    }

                } else {
                    //record not in device insert
                    db.createpestinsecticide(_dto_from_server);
                    Log.e(TAG, "inserted pesticide/insecticide [ " + _dto_from_server.getpestinsecticide_name() + " ]");
                    _result.append("inserted pesticide/insecticide [ " + _dto_from_server.getpestinsecticide_name() + " ].\n");
                    _inserted_records++;
                }
            }

            db.close();

            _result.append("[ " + _existing_records + " ] records already exists in device.\n");
            _result.append("[ " + _inserted_records + " ] records inserted.\n");

            return _result.toString();

        } catch (Exception ex) {
            _result.append(utilz.getInstance(getApplicationContext()).format_spannable_error_string(ex.toString()).toString() + "\n");
            return _result.toString();
        }
    }

    String fetchsettingsfromserver() {
        StringBuilder _result = new StringBuilder();
        try {
            // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds

            int CONNECTION_TIMEOUT = DBContract.synchronize_server_api.CONNECTION_TIMEOUT;
            int READ_TIMEOUT = DBContract.synchronize_server_api.READ_TIMEOUT;
            HttpURLConnection conn;
            URL url = null;
            String _url;

            String _ipaddress = txtserveripaddress.getText().toString();
            String _port = txtserverport.getText().toString();

            // Enter URL address where your php file resides

            //url = new URL("http://192.168.152.1:90/androidapi/get_all_settings.php");

            //_url = "http://" + _ipaddress + ":" + _port + "/androidapi/get_all_settings.php";

            _url = String.format("http://%s:%s/%s", _ipaddress, _port, DBContract.synchronize_server_api.get_all_settings);

            url = new URL(_url);

            // Setup HttpURLConnection class to send and receive data from php and mysql
            //call openConnection() on a URL instance. Since openConnection() returns a URLConnection, you need to explicitly cast the returned value
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("username", "username")
                    .appendQueryParameter("password", "password");
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                // Read data sent from server
                InputStream input = conn.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                StringBuilder _response = new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {
                    _response.append(line);
                    Log.e(TAG, "line [ " + line + " ]");

                    if (line.contains("<b")) {
                        _result.append(line + "\n");
                    }
                }

                Log.e(TAG, "_response [ " + _response.toString() + " ]");

                final List<settingdto> lst_dtos = new ArrayList<>();

                try {

                    JSONObject jsonObj = new JSONObject(_response.toString());
                    Log.e(TAG, "jsonObj [ " + jsonObj.toString() + " ]");

                    JSONObject _jobj = (JSONObject) jsonObj.get(DBContract.app_entities_wrapper.settings);
                    Log.e(TAG, "_jobj [ " + _jobj.toString() + " ]");

                    JSONArray _names = _jobj.names();
                    Log.e(TAG, "_names [ " + _names.toString() + " ]");
                    Log.e(TAG, "_names.length() [ " + _names.length() + " ]");

                    Iterator _keys = _jobj.keys();

                    while (_keys.hasNext()) {

                        // Loop to get the dynamic key
                        String currentDynamicKey = (String) _keys.next();
                        Log.e(TAG, "currentDynamicKey [ " + currentDynamicKey + " ]");

                        JSONObject _currentobj = (JSONObject) _jobj.get(currentDynamicKey);
                        Log.e(TAG, "_currentobj [ " + _currentobj.toString() + " ]");

                        Iterator _dto_obj_keys = _currentobj.keys();

                        int _pos = 0;

                        settingdto _currentdto = new settingdto();

                        while (_dto_obj_keys.hasNext()) {

                            // Loop to get the dynamic key
                            String _current_dto_obj_key = (String) _dto_obj_keys.next();
                            Log.e(TAG, "_current_dto_obj_key [ " + _current_dto_obj_key + " ]");

                            String _value = (String) _currentobj.get(_current_dto_obj_key);
                            Log.e(TAG, "_value [ " + _value.toString() + " ]");

                            _pos++;
                            Log.e(TAG, "_pos [ " + _pos + " ]");

                            switch (_current_dto_obj_key) {
                                case DBContract.settingsentitytable.SETTING_ID:
                                    _currentdto.setsetting_Id(Long.valueOf(_value));
                                    break;
                                case DBContract.settingsentitytable.SETTING_NAME:
                                    _currentdto.setsetting_name(_value);
                                    break;
                                case DBContract.settingsentitytable.SETTING_VALUE:
                                    _currentdto.setsetting_value(_value);
                                    break;
                                case DBContract.settingsentitytable.SETTING_STATUS:
                                    _currentdto.setsetting_status(_value);
                                    break;
                                case DBContract.settingsentitytable.CREATED_DATE:
                                    _currentdto.setcreated_date(_value);
                                    break;
                            }

                        }

                        lst_dtos.add(_currentdto);

                    }

                    Log.e(TAG, "lst_dtos.size() [ " + lst_dtos.size() + " ]");

                    _result.append(synchronizesettingstodevice(lst_dtos));

                } catch (final Exception e) {
                    _result.append(utilz.getInstance(getApplicationContext()).format_spannable_error_string(e.getMessage()).toString() + "\n");
                    Log.e(TAG, e.toString());
                }


            } else {
                Log.e(TAG, "unsuccessful");
            }

            return _result.toString();

        } catch (Exception ex) {
            _result.append(utilz.getInstance(getApplicationContext()).format_spannable_error_string(ex.toString()).toString() + "\n");
            return _result.toString();
        }
    }

    String synchronizesettingstodevice(List<settingdto> _lst_dtos) {
        StringBuilder _result = new StringBuilder();
        try {

            db = new DatabasehelperUtilz(sync_datastore_activity.this);
            db.openDataBase();

            int _existing_records = 0;
            int _inserted_records = 0;

            _result.append(DBContract.app_entities_wrapper.settings + "\n");

            _result.append("[ " + _lst_dtos.size() + " ] records retrieved from server.\n");

            for (int i = 0; i < _lst_dtos.size(); i++) {
                //get dto from server
                settingdto _dto_from_server = _lst_dtos.get(i);

                //check if dto exists in device 
                List<settingdto> _lst_filtered_dtos = db.filtersettingsgivenname(_dto_from_server.getsetting_name());

                int _filter_count = _lst_filtered_dtos.size();

                Log.e(TAG, "_filter_count [ " + _filter_count + " ]");

                if (_lst_filtered_dtos.size() > 0) {
                    //record exists in device insert

                    settingdto _dto_in_device = _lst_filtered_dtos.get(0);

                    if (_dto_in_device == null) {
                        //record not in device insert
                        db.createsetting(_dto_from_server);
                        Log.e(TAG, "inserted setting [ " + _dto_from_server.getsetting_name() + " ]");
                        _result.append("inserted setting [ " + _dto_from_server.getsetting_name() + " ].\n");
                        _inserted_records++;
                    } else {
                        //record exists in device skip insert
                        Log.e(TAG, "setting with name [ " + _dto_from_server.getsetting_name() + " ] exists in device.\n");
                        _result.append("setting with name [ " + _dto_from_server.getsetting_name() + " ] exists in device.\n");
                        _existing_records++;
                    }

                } else {
                    //record not in device insert
                    db.createsetting(_dto_from_server);
                    Log.e(TAG, "inserted setting [ " + _dto_from_server.getsetting_name() + " ]");
                    _result.append("inserted setting [ " + _dto_from_server.getsetting_name() + " ].\n");
                    _inserted_records++;
                }
            }

            db.close();

            _result.append("[ " + _existing_records + " ] records already exists in device.\n");
            _result.append("[ " + _inserted_records + " ] records inserted.\n");

            return _result.toString();

        } catch (Exception ex) {
            _result.append(utilz.getInstance(getApplicationContext()).format_spannable_error_string(ex.toString()).toString() + "\n");
            return _result.toString();
        }
    }

    void check_Runtime_Permissions() {
        try {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);

            } else {
                //Permission granted
                utilz.getInstance(getApplicationContext()).globalloghandler("WRITE_EXTERNAL_STORAGE PERMISSION GRANTED", TAG, 1, 1);
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);

            } else {
                //Permission granted
                utilz.getInstance(getApplicationContext()).globalloghandler("READ_EXTERNAL_STORAGE PERMISSION GRANTED", TAG, 1, 1);
            }

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE:

                for (int i = 0; i < grantResults.length; i++) {
                    //perms.put(permissions[i], grantResults[i]);
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        // permissions granted.
                        utilz.getInstance(getApplicationContext()).globalloghandler("permission [ " + permissions[i] + " ] granted.", TAG, 1, 1);
                    } else {
                        // permissions not granted.
                        utilz.getInstance(getApplicationContext()).globalloghandler("permission [ " + permissions[i] + " ] not granted.", TAG, 1, 0);
                    }
                }

                break;
            case READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE:

                for (int i = 0; i < grantResults.length; i++) {
                    //perms.put(permissions[i], grantResults[i]);
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        // permissions granted.
                        utilz.getInstance(getApplicationContext()).globalloghandler("permission [ " + permissions[i] + " ] granted.", TAG, 1, 1);
                    } else {
                        // permissions not granted.
                        utilz.getInstance(getApplicationContext()).globalloghandler("permission [ " + permissions[i] + " ] not granted.", TAG, 1, 0);
                    }
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}






