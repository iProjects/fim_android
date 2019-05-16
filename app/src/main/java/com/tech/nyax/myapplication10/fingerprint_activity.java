package com.tech.nyax.myapplication10;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class fingerprint_activity extends AppCompatActivity {

    public static final String TAG = fingerprint_activity.class.getSimpleName();
    private static final int REQUEST_DISCOVERABLE_BT = 2; // Unique request code
    private static final int DISCOVERABLE_DURATION = 120; // Discoverable duration time in seconds 0 means always discoverable maximum value is 3600
    TextView txtbluetoothinfo;
	private ImageView img_photo;
    Button btnscanbluetooth, btnenablebluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fingerprint_layout);
        Log.e(TAG, "fingerprint_activity onCreate");

        getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));

        try {

            txtbluetoothinfo = findViewById(R.id.txtbluetoothinfo);
            txtbluetoothinfo.setText("");

			//get the Image View at the main.xml file
			img_photo = findViewById(R.id.img_photo);
			img_photo.setImageResource(R.drawable.biometric_authentication);

            btnscanbluetooth = findViewById(R.id.btnscanbluetooth);
            btnscanbluetooth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
 
                    utilz.getInstance(getApplicationContext()).globalloghandler("bluetooth scan initiated...", TAG, 1, 1);
 
                }
            });
			
            btnenablebluetooth = findViewById(R.id.btnenablebluetooth);
            btnenablebluetooth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    utilz.getInstance(getApplicationContext()).globalloghandler("enabling bluetooth...", TAG, 1, 1);
 

                }
            });

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DISCOVERABLE_BT) {
            if (resultCode == RESULT_OK) {
				// Device is discoverable
				utilz.getInstance(getApplicationContext()).globalloghandler("Device is discoverable.", TAG, 1, 0);
            } else if (resultCode == RESULT_CANCELED) {
				// Device is not discoverable
				utilz.getInstance(getApplicationContext()).globalloghandler("Device is not discoverable.", TAG, 1, 0);
            }
        }
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onResume() {

        super.onResume();
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
