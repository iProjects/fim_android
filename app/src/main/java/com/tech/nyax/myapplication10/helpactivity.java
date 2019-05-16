package com.tech.nyax.myapplication10;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class helpactivity extends AppCompatActivity {

    private final static String TAG = helpactivity.class.getSimpleName();
    public static final String EXTRA_DATA = "EXTRA_DATA";
    public static final int _IMAGE_CAPTURE_REQUEST_CODE = 0x3265;
    private static final int _ENABLE_BT_REQUEST_CODE = 0x4789;
    public static final int _PICK_IMAGE_REQUEST_CODE = 0x5423;
    BluetoothAdapter mBluetoothAdapter;
    String mCurrentPhotoPath = "";
    ImageView recipeImage;
    Button btncheckwifi, btntakephoto, btncheckbluetooth, btnopensharedprefs, btnreadfingerprint, btnchetharwimbo, btnvibrate, btngame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_layout);
        Log.i(TAG, "helpactivity onCreate");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this will show the back arrow in the tool bar.

        getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));

        recipeImage = findViewById(R.id.photofromcamera);

        btncheckwifi = findViewById(R.id.btncheckwifi);
        btncheckwifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    utilz.getInstance(getApplicationContext()).globalloghandler("launching wifi_activity...", TAG, 1, 1);

                    final Intent wifi_activity = new Intent(getApplicationContext(), wifi_activity.class);
                    startActivity(wifi_activity);

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });

        btntakephoto = findViewById(R.id.btntakephoto);
        btntakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    utilz.getInstance(getApplicationContext()).globalloghandler("launching camera_activity...", TAG, 1, 1);

                    final Intent camera_activity = new Intent(getApplicationContext(), camera_activity.class);
                    startActivity(camera_activity);

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });

        btncheckbluetooth = findViewById(R.id.btncheckbluetooth);
        btncheckbluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    utilz.getInstance(getApplicationContext()).globalloghandler("launching bluetooth_activity...", TAG, 1, 1);

                    final Intent bluetooth_activity = new Intent(getApplicationContext(), bluetooth_activity.class);
                    startActivity(bluetooth_activity);

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });

        btnopensharedprefs = findViewById(R.id.btnopensharedprefs);
        btnopensharedprefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    utilz.getInstance(getApplicationContext()).globalloghandler("launching preference_activity...", TAG, 1, 1);

                    Intent preference_activity = new Intent(getApplicationContext(), preference_activity.class);
                    startActivity(preference_activity);

                    try {
                        Runtime runtime = Runtime.getRuntime();

                        Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");

                        int exitValue = ipProcess.waitFor();

                        ipProcess.destroy();

                        if (exitValue == 0) {
// Success
                        } else {
// Failure
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                        utilz.getInstance(getApplicationContext()).globalloghandler(e.toString(), TAG, 1, 0);
                    }

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });

        btnreadfingerprint = findViewById(R.id.btnreadfingerprint);
        btnreadfingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    utilz.getInstance(getApplicationContext()).globalloghandler("launching fingerprint_activity...", TAG, 1, 1);

                    Intent fingerprint_activity = new Intent(getApplicationContext(), fingerprint_activity.class);
                    startActivity(fingerprint_activity);

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });

        btnchetharwimbo = findViewById(R.id.btnchetharwimbo);
        btnchetharwimbo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    utilz.getInstance(getApplicationContext()).globalloghandler("launching audio_activity...", TAG, 1, 1);

                    final Intent audio_activity = new Intent(getApplicationContext(), audio_activity.class);
                    startActivity(audio_activity);

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });

        btnvibrate = findViewById(R.id.btnvibrate);
        btnvibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    vibrate_device();

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });

        btngame = findViewById(R.id.btngame);
        btngame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    utilz.getInstance(getApplicationContext()).globalloghandler("launching game_activity...", TAG, 1, 1);

                    final Intent game_activity = new Intent(getApplicationContext(), game_activity.class);
                    startActivity(game_activity);

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });


    }

    public void vibrate_device() {
        if (does_device_support_vibrator()) {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Start time delay
            // vibrate 100 milliseconds and sleep 1000 milliseconds
            // vibrate 200 milliseconds and sleep 2000 milliseconds
            long[] pattern = {0, 100, 1000, 200, 2000};
            // 0 meaning is repeat indefinitely
            vibrator.vibrate(pattern, 0);
        }
    }

    boolean does_device_support_vibrator() {

        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (null == vibrator) {

            utilz.getInstance(getApplicationContext()).globalloghandler("device doesn't support vibrator.", TAG, 1, 0);

            return false;
        }
        if (!vibrator.hasVibrator()) {

            utilz.getInstance(getApplicationContext()).globalloghandler("device doesn't support vibrator.", TAG, 1, 0);

            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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