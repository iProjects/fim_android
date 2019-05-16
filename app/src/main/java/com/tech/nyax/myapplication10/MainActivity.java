package com.tech.nyax.myapplication10;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();
    public static final String MESSAGE_CONSTANT = "com.tech.nyax.myapplication10";
    Button btncropslist;
    Button btncropsvarietieslist;
    Button btnmanufacturerslist;
    Button btnsettingslist;
    Button btncropsdiseaseslist;
    Button btnpestsinsecticideslist;
    Button btnsearch, btncategorieslist, btndatabaseutils, btnexit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));

        // Setup handler for uncaught exceptions.
        final Thread.UncaughtExceptionHandler defaultHandler =
                Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                try {
                    handleUncaughtException(e);
                    System.exit(1);
                } catch (Throwable e2) {
                    Log.e(TAG, "Exception in custom exception handler", e2);
                    defaultHandler.uncaughtException(thread, e);
                }
            }
        });

        btncropslist = findViewById(R.id.btncropslist);
        btncropslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                utilz.getInstance(getApplicationContext()).globalloghandler("launching cropslistactivity...", TAG, 1, 1);

                final Intent cropslistactivity = new Intent(getApplicationContext(), cropslistactivity.class);
                startActivity(cropslistactivity);
            }
        });

        btncropsvarietieslist = findViewById(R.id.btncropsvarietieslist);
        btncropsvarietieslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                utilz.getInstance(getApplicationContext()).globalloghandler("launching cropsvarietieslistactivity...", TAG, 1, 1);

                final Intent cropsvarietieslistactivity = new Intent(getApplicationContext(), cropsvarietieslistactivity.class);
                startActivity(cropsvarietieslistactivity);
            }
        });

        btnmanufacturerslist = findViewById(R.id.btnmanufacturerslist);
        btnmanufacturerslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                utilz.getInstance(getApplicationContext()).globalloghandler("launching manufacturerslistactivity...", TAG, 1, 1);

                final Intent manufacturerslistactivity = new Intent(getApplicationContext(), manufacturerslistactivity.class);
                startActivity(manufacturerslistactivity);
            }
        });

        btnsettingslist = findViewById(R.id.btnsettingslist);
        btnsettingslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                utilz.getInstance(getApplicationContext()).globalloghandler("launching settingslistactivity...", TAG, 1, 1);

                final Intent settingslistactivity = new Intent(getApplicationContext(), settingslistactivity.class);
                startActivity(settingslistactivity);
            }
        });

        btncropsdiseaseslist = findViewById(R.id.btncropsdiseaseslist);
        btncropsdiseaseslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                utilz.getInstance(getApplicationContext()).globalloghandler("launching cropsdiseaseslistactivity...", TAG, 1, 1);

                final Intent cropsdiseaseslistactivity = new Intent(getApplicationContext(), cropsdiseaseslistactivity.class);
                startActivity(cropsdiseaseslistactivity);
            }
        });

        btnpestsinsecticideslist = findViewById(R.id.btnpestsinsecticideslist);
        btnpestsinsecticideslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                utilz.getInstance(getApplicationContext()).globalloghandler("launching pestsinsecticideslistactivity...", TAG, 1, 1);

                final Intent pestsinsecticideslistactivity = new Intent(getApplicationContext(), pestsinsecticideslistactivity.class);
                startActivity(pestsinsecticideslistactivity);
            }
        });

        btnsearch = findViewById(R.id.btnsearch);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                utilz.getInstance(getApplicationContext()).globalloghandler("launching searchutilsactivity...", TAG, 1, 1);

                final Intent searchutilsactivity = new Intent(getApplicationContext(), searchutilsactivity.class);
                startActivity(searchutilsactivity);
            }
        });

        btncategorieslist = findViewById(R.id.btncategorieslist);
        btncategorieslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                utilz.getInstance(getApplicationContext()).globalloghandler("launching categorieslistactivity...", TAG, 1, 1);

                final Intent categorieslistactivity = new Intent(getApplicationContext(), categorieslistactivity.class);
                startActivity(categorieslistactivity);
            }
        });

        btndatabaseutils = findViewById(R.id.btndatabaseutils);
        btndatabaseutils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                utilz.getInstance(getApplicationContext()).globalloghandler("launching database_utils_activity...", TAG, 1, 1);

                final Intent database_utils_activity = new Intent(getApplicationContext(), database_utils_activity.class);
                startActivity(database_utils_activity);

            }
        });

        btnexit = findViewById(R.id.btnexit);
        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                utilz.getInstance(getApplicationContext()).globalloghandler("exiting app...", TAG, 1, 1);

                try {
                    Thread.sleep(500);
					finish();
                    System.exit(0);
                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }

            }
        });


    }

    private void handleUncaughtException(Throwable e) throws IOException {
        Log.e(TAG, "Uncaught exception logged to local file", e);
        // Create a new unique file
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
        String timestamp;
        File file = null;
        while (file == null || file.exists()) {
            timestamp = dateFormat.format(new Date());
            file = new File(getFilesDir(), "nyax_crash_log_" + timestamp + ".txt");
        }
        Log.i(TAG, "Trying to create log file " + file.getPath());
        file.createNewFile();

        // Write the stacktrace to the file
        FileWriter writer = null;
        try {
            writer = new FileWriter(file, true);
            for (StackTraceElement element : e.getStackTrace()) {
                writer.write(element.toString());
            }
        } finally {
            if (writer != null) writer.close();
        }

        MediaScannerConnection.scanFile(this, new String[]{file.getPath()}, null, null);
        getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(file)));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Override defining menu resource
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //Override for preparing items (setting visibility, change text, change icon...)

        for (int i = 0; i < menu.size(); i++) {

            MenuItem menu_item = menu.getItem(i);

            switch (menu_item.getItemId()) {
                case R.id.databaseutilsactivityMenu:
                    menu_item.setVisible(true);
                    return true;
                case R.id.aboutMenu:
                    menu_item.setVisible(true);
                    return true;
                case R.id.helpMenu:
                    menu_item.setVisible(true);
                    return true;
                case R.id.signOutMenu:
                    menu_item.setVisible(true);
                    return true;
                default:
                    menu_item.setVisible(true);
                    return true;
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Override it for handling items
        try {
            switch (item.getItemId()) {
                case R.id.databaseutilsactivityMenu:

                    utilz.getInstance(getApplicationContext()).globalloghandler("loading database_utils_activity...", TAG, 1, 1);

                    final Intent database_utils_activity = new Intent(getApplicationContext(), database_utils_activity.class);
                    startActivity(database_utils_activity);

                    return true;//return true, if is handled
                case R.id.aboutMenu:
                    // Code for About goes here

                    utilz.getInstance(getApplicationContext()).globalloghandler("launching about activity...", TAG, 1, 1);

                    // Create a new instance of Intent to start activity
                    final Intent aboutactivity = new Intent(getApplicationContext(), aboutactivity.class);
                    // Start activity
                    startActivity(aboutactivity);

                    return true;//return true, if is handled
                case R.id.helpMenu:
                    // Code for Help goes here

                    utilz.getInstance(getApplicationContext()).globalloghandler("launching help activity...", TAG, 1, 1);

                    final Intent helpactivity = new Intent(getApplicationContext(), helpactivity.class);
                    startActivity(helpactivity);

                    return true;//return true, if is handled
                case R.id.signOutMenu:
                    // SignOut method call goes here

                    utilz.getInstance(getApplicationContext()).globalloghandler("sign out utility not yet implemented...", TAG, 1, 1);

                    return true;//return true, if is handled
                default:
                    return super.onOptionsItemSelected(item);
            }
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
            return false;
        }
    }


}





















