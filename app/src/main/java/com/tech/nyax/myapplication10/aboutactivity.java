package com.tech.nyax.myapplication10;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class aboutactivity extends AppCompatActivity {
    private final static String TAG = aboutactivity.class.getSimpleName();
    //define list to show directory
    List<DirectoryModel> rootDir = new ArrayList<>();
    TextView txtversionname, txtversioncode, txtinstalltime, txtlastupdatetime;
    // reusable string object
    private static StringBuilder _string_builder = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
        Log.i(TAG, "aboutactivity onCreate");

		getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this will show the back arrow in the tool bar.
		
		getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));
		
        txtversionname = findViewById(R.id.txtversionname);
        txtversioncode = findViewById(R.id.txtversioncode);
        txtinstalltime = findViewById(R.id.txtinstalltime);
        txtlastupdatetime = findViewById(R.id.txtlastupdatetime);

        get_version_info();

        get_installation_info();
		
		get_device_info();

    }

    void get_version_info() {
        try {
            // Reference to Android's package manager
            PackageManager packageManager = getApplicationContext().getPackageManager();
            // Getting package info of this application
            PackageInfo info = packageManager.getPackageInfo(getApplicationContext().getPackageName(), 0);
            // Version code
            txtversioncode.setText(info.versionCode);
            // Version name
            txtversionname.setText(info.versionName);

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    void get_installation_info() {
        try {
            // Reference to Android's package manager
            PackageManager packageManager = getApplicationContext().getPackageManager();
            // Getting package info of this application
            PackageInfo info = packageManager.getPackageInfo(getApplicationContext().getPackageName(), 0);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            String firstInstallTime = dateFormat.format(new Date(info.firstInstallTime));

            // Install time. Units are as per currentTimeMillis().
            txtinstalltime.setText(String.valueOf(firstInstallTime));

            String lastUpdateTime = dateFormat.format(new Date(info.lastUpdateTime));

            // Last update time. Units are as per currentTimeMillis().
            txtlastupdatetime.setText(String.valueOf(lastUpdateTime));

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

	void get_device_info(){
		try{

			_string_builder = new StringBuilder();
			_string_builder.append("BRAND [ " + android.os.Build.BRAND + "].\n");
			_string_builder.append("MANUFACTURER [ " + android.os.Build.MANUFACTURER + "].\n");
			_string_builder.append("MODEL [ " + android.os.Build.MODEL + "].\n");

			utilz.getInstance(getApplicationContext()).globalloghandler(_string_builder.toString(), TAG, 1, 1);

		} catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
	}
	
    public void createfile(View v) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "ntharene_city_" + timeStamp;
        String textToWrite = "...........................\n";
        textToWrite += fileName + "\n";
        textToWrite += timeStamp + "\n";
        textToWrite += TAG + "\n";
        textToWrite += Environment.isExternalStorageEmulated() + "\n";
        textToWrite += "...........................\n";
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(textToWrite.getBytes());
            fileOutputStream.close();

            utilz.getInstance(getApplicationContext()).globalloghandler("created file \n" + fileName, TAG, 1, 1);

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler("error creating file ...\n" + ex.toString(), TAG, 1, 0);
        }
    }

    public void getdirectorylistz(View v) {
        try {
//to Fetch Directory Call function with root directory.
            String rootPath = Environment.getExternalStorageDirectory().toString();
            // return ==> /storage/emulated/0/
            getDirectory(rootPath);

            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_MOUNTED)) {
// Available to read and write
            }
            if (state.equals(Environment.MEDIA_MOUNTED) ||
                    state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
// Available to at least read
            }

            String currentDBPath = "/data/" + getPackageName() + "/databases/";
            File storageDir = null;
            storageDir = new File(Environment.getExternalStorageDirectory()
                    + currentDBPath);
            storageDir.mkdirs();
            if (!storageDir.exists()) {
                Log.e("currentDBPath", "failed to create directory");
            }

            // Access your app's Private documents directory
            File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                    "YourAppDirectory");
// Make the directory if it does not yet exist
            file.mkdirs();

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    private void getDirectory(String currDir) { // pass device root directory
        File f = new File(currDir);
        File[] files = f.listFiles();
        if (files != null) {
            if (files.length > 0) {
                rootDir.clear();
                for (File inFile : files) {
                    if (inFile.isDirectory()) { //return true if it's directory
// is directory
                        DirectoryModel dir = new DirectoryModel();
                        dir.setDirName(inFile.toString().replace("/storage/emulated/0", ""));
                        dir.setDirType(0); // set 0 for directory
                        rootDir.add(dir);
                    } else if (inFile.isFile()) { // return true if it's file
//is file
                        DirectoryModel dir = new DirectoryModel();
                        dir.setDirName(inFile.toString().replace("/storage/emulated/0", ""));
                        dir.setDirType(1); // set 1 for file
                        rootDir.add(dir);
                    }
                }
            }
            printDirectoryList();
        }
    }

    //print directory list in logs
    private void printDirectoryList() {
        for (int i = 0; i < rootDir.size(); i++) {
            Log.e(TAG, "printDirectoryLogs: " + rootDir.get(i).toString());
        }
    }

    public Boolean ExportDB(String DATABASE_NAME, String packageName, String folderName) {

        //DATABASE_NAME including ".db" at the end like "mayApp.db"
        String DBName = DATABASE_NAME.substring(0, DATABASE_NAME.length() - 3);
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/" + packageName + "/databases/" + DATABASE_NAME;
        // getting app db path
        File sd = Environment.getExternalStorageDirectory(); // getting phone SD card path
        // if you want to set backup in specific  folder name
        String backupPath = sd.getAbsolutePath() + folderName;
/* be careful , foldername must initial like this : "/myFolder" . don't forget "/" at begin
of folder name
you could define foldername like this : "/myOutterFolder/MyInnerFolder" and so on ...
*/
        File dir = new File(backupPath);
        if (!dir.exists()) // if there was no folder at this path , create it .
        {
            dir.mkdirs();
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        Date date = new Date();
        /* use date including file name for arrange them and preventing to make file with the same*/
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(backupPath, DBName + "(" + dateFormat.format(date) + ").db");
        try {
            if (currentDB.exists() && !backupDB.exists()) {
                source = new FileInputStream(currentDB).getChannel();
                destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
                return true;
            }
            return false;
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
            return false;
        }
    }

    public void expota(View v) {
        try {
            ExportDB("ntharene_city.db", getPackageName(), "/ntharene_city/databases");
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    public void showToastMessage(final String message, final int length) {
        View root = findViewById(android.R.id.content);
        Toast toast = Toast.makeText(getApplicationContext(), message, length);
        int yOffset = Math.max(0, root.getHeight() - toast.getYOffset());
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, yOffset);
        toast.show();
    }

    public void chetharwimbo(View v) {
        try {

            MediaPlayer mMediaPlayer = MediaPlayer.create(this, R.raw.get_you_home);
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.reset();
                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.get_you_home);
            }
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer player) {
// Called when the MediaPlayer is ready to play
                    player.start();
                }
            }); // Set callback for when prepareAsync() finishes
            mMediaPlayer.prepareAsync(); // Prepare asynchronously to not block the Main Thread
            //mMediaPlayer.start();
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
// ... react appropriately ...
// The MediaPlayer has moved to the Error state, must be reset!
// Then return true if the error has been handled
                    return true;
                }
            });

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
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
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 1);
            return false;
        }
    }


}






