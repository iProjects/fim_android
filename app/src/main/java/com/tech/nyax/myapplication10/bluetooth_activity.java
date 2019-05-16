package com.tech.nyax.myapplication10;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import android.content.res.Resources;
import android.content.res.TypedArray;

import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.os.Handler;

import static android.provider.Settings.Secure.ANDROID_ID;

public class bluetooth_activity extends AppCompatActivity {

    public static final String TAG = bluetooth_activity.class.getSimpleName();
    // Unique request code
    private static final int REQUEST_DISCOVERABLE_BT = 2;
    // Unique request code
    private static final int REQUEST_ENABLE_BT = 1;
    // Unique request code
    private static final int BLUETOOTH_PERMISSION_REQUEST_CODE = 3;
    // Unique request code
    private static final int BLUETOOTH_ADMIN_PERMISSION_REQUEST_CODE = 4;
    // Discoverable duration time in seconds 0 means always discoverable maximum value is 3600
    private static final int DISCOVERABLE_DURATION = 0;
    TextView txtbluetoothinfo;
    Button btnscanbluetooth, btnenablebluetooth, btnmakebluetoothdiscoverable, btndiscovernearbydevices, btnconnect_to_bluetooth_device, btnsend_data_to_bluetooth_divice, btnreceive_data_to_bluetooth_divice;
    ImageView imgbluetoothstatus;
    // reusable string object
    private static StringBuilder _string_builder = new StringBuilder();
    //BroadcastReceiver for ACTION_FOUND
    private BroadcastReceiver bluetooth_broadcast_receiver;
    //Declare a BluetoothAdapter
	/* Represents the local Bluetooth adapter (Bluetooth radio). The BluetoothAdapter is the entry-point for all Bluetooth interaction. Using this, you can discover other Bluetooth devices, 
	query a list of bonded (paired) devices, 
	instantiate a BluetoothDevice using a known MAC address, 
	and create a BluetoothServerSocket to listen for communications from other devices. */
    private BluetoothAdapter _bluetooth_adapter;
    //Declare a BluetoothDevice
    /* Represents a remote Bluetooth device. Use this to request a connection with a remote device through a BluetoothSocket or query information about the device such as its name, address, class, and bonding state. */
    BluetoothDevice _device;
    //Declare a BluetoothSocket
    /* Represents the interface for a Bluetooth socket (similar to a TCP Socket).This is the connection point that allows an application to exchange data with another Bluetooth device using InputStream and OutputStream. */
    private BluetoothSocket _socket;
    //Declare a BluetoothServerSocket
    /* Represents an open server socket that listens for incoming requests (similar to a TCP ServerSocket). In order to connect two Android devices, one device must open a server socket with this class. When a remote Bluetooth device makes a connection request to this device, the device accepts the connection, then returns a connected BluetoothSocket. */
    private BluetoothServerSocket _server_socket;
    //Declare a BluetoothClass
    /* Describes the general characteristics and capabilities of a Bluetooth device. This is a read-only set of properties that defines the device's classes and services. Although this information provides a useful hint regarding a device's type, the attributes of this class don't necessarily describe all Bluetooth profiles and services that the device supports. */
    private BluetoothClass _bluetooth_class;
    //socket Input\Output streams
    private InputStream _inStream;
    private OutputStream _outStream;

    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;

    /* A Bluetooth profile is a wireless interface specification for Bluetooth-based communication between devices. An example is the Hands-Free profile. For a mobile phone to connect to a wireless headset, both devices must support the Hands-Free profile.
    The Headset profile provides support for Bluetooth headsets to be used with mobile phones. Android provides the BluetoothHeadset class, which is a proxy for controlling the Bluetooth Headset Service. This includes both Bluetooth Headset and Hands-Free (v1.5) profiles. The BluetoothHeadset class includes support for AT commands.*/
    BluetoothHeadset mBluetoothHeadset;

    // SPP UUID service – this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private Handler mHandler; // handler that gets info from Bluetooth service

    static Handler h;

    final int handlerState = 0; //used to identify handler message

    final int RECIEVE_MESSAGE = 1;    // Status for Handler
    private StringBuilder recDataString = new StringBuilder();
    private StringBuilder sb = new StringBuilder();

    private ConnectedThread mConnectedThread;

    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address = "98:D3:31:FD:21:00";

    DataOutputStream os;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_layout);

        Log.e(TAG, "bluetooth_activity onCreate");

        getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));

        txtbluetoothinfo = findViewById(R.id.txtbluetoothinfo);
        txtbluetoothinfo.setText("");

        btnscanbluetooth = findViewById(R.id.btnscanbluetooth);
        btnscanbluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (check_bluetooth_validity()) {

                        _string_builder.append(getResources().getString(R.string.txtbluetoothinfo));

                        txtbluetoothinfo.setText(_string_builder.toString());

                        utilz.getInstance(getApplicationContext()).globalloghandler("bluetooth scan initiated...", bluetooth_activity.class.getSimpleName(), 1, 1);

                        //paired devices.
                        show_paired_bluetooth_devices();

                        show_found_bluetooth_devices();

                    }

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }

            }
        });

        btnenablebluetooth = findViewById(R.id.btnenablebluetooth);
        btnenablebluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (check_bluetooth_validity()) {

                        utilz.getInstance(getApplicationContext()).globalloghandler("enabling bluetooth...", TAG, 1, 1);

                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

                    }

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }

            }
        });

        btnmakebluetoothdiscoverable = findViewById(R.id.btnmakebluetoothdiscoverable);
        btnmakebluetoothdiscoverable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (check_bluetooth_validity()) {

                        utilz.getInstance(getApplicationContext()).globalloghandler("make bluetooth discoverable...", TAG, 1, 1);

                        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

                        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION);

                        startActivityForResult(discoverableIntent, REQUEST_DISCOVERABLE_BT);

                    }

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }

            }
        });

        btndiscovernearbydevices = findViewById(R.id.btndiscovernearbydevices);
        btndiscovernearbydevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (check_bluetooth_validity()) {

                        utilz.getInstance(getApplicationContext()).globalloghandler("discovering nearby bluetooth devices...", TAG, 1, 1);

                        show_paired_bluetooth_devices();

                        if (!_bluetooth_adapter.isDiscovering()) {

                            //start discovering the nearby bluetooth devices by calling startDiscovery
                            _bluetooth_adapter.startDiscovery();

                        }

                    }

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }

            }
        });

        btnconnect_to_bluetooth_device = findViewById(R.id.btnconnect_to_bluetooth_device);
        btnconnect_to_bluetooth_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (check_bluetooth_validity()) {

                        utilz.getInstance(getApplicationContext()).globalloghandler("connecting to bluetooth device...", TAG, 1, 1);

                        connect_to_bluetooth_device();
                    }

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }

            }
        });

        btnsend_data_to_bluetooth_divice = findViewById(R.id.btnsend_data_to_bluetooth_divice);
        btnsend_data_to_bluetooth_divice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (check_bluetooth_validity()) {

                        utilz.getInstance(getApplicationContext()).globalloghandler("sending data to bluetooth device...", TAG, 1, 1);

                        send_data_to_bluetooth_device();

                    }

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }

            }
        });

        btnreceive_data_to_bluetooth_divice = findViewById(R.id.btnreceive_data_to_bluetooth_divice);
        btnreceive_data_to_bluetooth_divice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (check_bluetooth_validity()) {

                        utilz.getInstance(getApplicationContext()).globalloghandler("receiving data from bluetooth device...", TAG, 1, 1);

                        receive_from_bluetooth_device();

                    }

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }

            }
        });

        try {

            imgbluetoothstatus = findViewById(R.id.imgbluetoothstatus);
            IconDrawable iconDrawable = new
                    IconDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.inactive), ContextCompat.getColor(getApplicationContext(), R.color.inactive));
            imgbluetoothstatus.setImageDrawable(iconDrawable);

            check_Runtime_Permissions();

            register_broadcast_receiver();

            register_message_handler();

            connectBluetoothHeadset();

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }

    }

    void register_message_handler() {

        h = new Handler() {

            public void handleMessage(android.os.Message msg) {

                utilz.getInstance(getApplicationContext()).globalloghandler("GOGOGGO...", TAG, 1, 1);

                utilz.getInstance(getApplicationContext()).globalloghandler("msg.what is " + msg.what, TAG, 1, 1);

                if (msg.what == RECIEVE_MESSAGE) { // if receive massage

                    utilz.getInstance(getApplicationContext()).globalloghandler("NONONO...", TAG, 1, 1);

                    String readMessage = (String) msg.obj; // msg.arg1 = bytes from connect thread
                    sb.append(readMessage); //keep appending to string until ~
                    int endOfLineIndex = sb.indexOf("~"); // determine the end-of-line
                    if (endOfLineIndex > 0) { // make sure there data before ~
                        String dataInPrint = sb.substring(0, endOfLineIndex); // extract string

                        utilz.getInstance(getApplicationContext()).globalloghandler("Data Received = " + dataInPrint, TAG, 1, 1);

                        sb.delete(0, sb.length()); //clear all string data
// strIncom =” “;
                        dataInPrint = "";
                    }
                }
            }
        };

    }

    void register_broadcast_receiver() {

        bluetooth_broadcast_receiver = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {

                try {

                    String action = intent.getAction();

                    Log.e(TAG, "action [ " + action + " ].");

                    utilz.getInstance(getApplicationContext()).globalloghandler("action [ " + action + " ].", TAG, 1, 1);

                    //Device found
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        // Get the BluetoothDevice object from the Intent
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                        _device = device;

                        // the name and address
                        String _device_info = device.getName() + "\n" + device.getAddress();

                        _string_builder.append("[ " + _device_info + " ]\n");

                        Log.e(TAG, "[ " + _string_builder.toString() + " ].");

                        utilz.getInstance(getApplicationContext()).globalloghandler("[ " + _string_builder.toString() + " ].", TAG, 1, 1);

                        txtbluetoothinfo.setText(_string_builder.toString());
                    }

                    //Bluetooth state changed.
                    if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                        // Get the current state.
                        BluetoothDevice _state = intent.getParcelableExtra(BluetoothDevice.EXTRA_BOND_STATE);

                        // Get the previous state.
                        BluetoothDevice _previous_state = intent.getParcelableExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE);

                        // the name and address
                        String _device_info = _state.getName() + "\n" + _state.getAddress();

                        _string_builder.append("[ " + _device_info + " ]\n");

                        Log.e(TAG, "[ " + _string_builder.toString() + " ].");

                        utilz.getInstance(getApplicationContext()).globalloghandler("[ " + _string_builder.toString() + " ].", TAG, 1, 1);

                        txtbluetoothinfo.setText(_string_builder.toString());
                    }

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }

            }
        };

        //Register the BroadcastReceiver.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);

        registerReceiver(bluetooth_broadcast_receiver, filter);

    }

    boolean check_bluetooth_validity() {
        try {

            _bluetooth_adapter = BluetoothAdapter.getDefaultAdapter();

            if (_bluetooth_adapter == null) {
                // Device doesn't support Bluetooth

                utilz.getInstance(getApplicationContext()).globalloghandler("Device doesn't support Bluetooth.", TAG, 1, 0);

                return false;

            } else {
                // Device support Bluetooth

                utilz.getInstance(getApplicationContext()).globalloghandler("Device support Bluetooth.", TAG, 1, 1);

            }

            if (!_bluetooth_adapter.isEnabled()) {
                //bluetooth not enabled.
                utilz.getInstance(getApplicationContext()).globalloghandler("Bluetooth not enabled.", TAG, 1, 0);

                return false;

            } else {
                //bluetooth enabled.
                utilz.getInstance(getApplicationContext()).globalloghandler("Bluetooth enabled.", TAG, 1, 1);

            }

            return true;

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
            return false;
        }

    }

    public void sendFile(Uri uri, BluetoothSocket bs) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(getContentResolver().openInputStream(uri));
        OutputStream os = bs.getOutputStream();
        try {
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            // we need to know how may bytes were read to write them to the byteBuffer
            int len = 0;
            while ((len = bis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
        } finally {
            bis.close();
            os.flush();
            os.close();
            bs.close();
        }
    }

    public class clientSock extends Thread {
        public void run() {
            try {
                os.writeBytes("anything you want"); // anything you want
                os.flush();
            } catch (Exception e1) {
                e1.printStackTrace();
                return;
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);
                    // Send the obtained bytes to the UI activity.
                    Message readMsg = mHandler.obtainMessage(
                            MessageConstants.MESSAGE_READ, numBytes, -1,
                            mmBuffer);
                    readMsg.sendToTarget();
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }

        // Call this from the main activity to send data to the remote device.
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);

                // Share the sent message with the UI activity.
                Message writtenMsg = mHandler.obtainMessage(
                        MessageConstants.MESSAGE_WRITE, -1, -1, mmBuffer);
                writtenMsg.sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);

                // Send a failure message back to the activity.
                Message writeErrorMsg =
                        mHandler.obtainMessage(MessageConstants.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("toast",
                        "Couldn't send data to the other device");
                writeErrorMsg.setData(bundle);
                mHandler.sendMessage(writeErrorMsg);
            }
        }

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

    // Defines several constants used when transmitting messages between the
    // service and the UI.
    private interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;

        // ... (Add other message types here as needed.)
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;


        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket
            // because mmServerSocket is final.
            BluetoothServerSocket tmp = null;

            try {
                // MY_UUID is the app's UUID string, also used by the client code.
                tmp = _bluetooth_adapter.listenUsingRfcommWithServiceRecord("device_name", MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned.
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket's accept() method failed", e);
                    break;
                }

                if (socket != null) {
                    // A connection was accepted. Perform work associated with
                    // the connection in a separate thread.
                    //manageMyConnectedSocket(socket);
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

    void ping_bluetooth_devices(BluetoothDevice device) {
        try {

            Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});

            BluetoothSocket clientSocket = (BluetoothSocket) m.invoke(device, 1);

            clientSocket.connect();

            os = new DataOutputStream(clientSocket.getOutputStream());

            new clientSock().start();

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    void show_found_bluetooth_devices() {
        try {

            if (!check_bluetooth_validity()) {
                return;
            }

            BluetoothScanningFactory scanningFactory = new BluetoothScanningFactory(getApplicationContext());
            scanningFactory.startScanning();

            List<BTDevice> bluetoothDeviceList = scanningFactory.getFoundDeviceList();

            for (BTDevice device : bluetoothDeviceList) {
                // the name and address
                String _device_info = "name [ " + device.getName() + " ] \n address [ " + device.getAddress() + " ] \n";

                _string_builder.append("[ " + _device_info + " ]\n");

                Log.e(TAG, "[ " + _string_builder.toString() + " ].");

                utilz.getInstance(getApplicationContext()).globalloghandler("[ " + _string_builder.toString() + " ].", TAG, 1, 1);

                //ping_bluetooth_devices(device);
            }
            txtbluetoothinfo.setText(_string_builder.toString());

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    void show_paired_bluetooth_devices() {
        try {

            //This returns a set of BluetoothDevice objects representing paired devices.
            Set<BluetoothDevice> pairedDevices = _bluetooth_adapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice device : pairedDevices) {
                    //name
                    String deviceName = device.getName();
                    // MAC address
                    String deviceHardwareAddress = device.getAddress();

                    // the name and address
                    String _device_info = "name [ " + device.getName() + " ] \n address [ " + device.getAddress() + " ] \n";

                    _string_builder.append("[ " + _device_info + " ]\n");

                    Log.e(TAG, "[ " + _string_builder.toString() + " ].");

                    utilz.getInstance(getApplicationContext()).globalloghandler("[ " + _string_builder.toString() + " ].", TAG, 1, 1);

                    ping_bluetooth_devices(device);
                }
                txtbluetoothinfo.setText(_string_builder.toString());

            }

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    void check_Runtime_Permissions() {
        try {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, BLUETOOTH_PERMISSION_REQUEST_CODE);

            } else {
                //Permission granted
                utilz.getInstance(getApplicationContext()).globalloghandler("BLUETOOTH PERMISSION GRANTED", TAG, 1, 1);
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, BLUETOOTH_ADMIN_PERMISSION_REQUEST_CODE);

            } else {
                //Permission granted
                utilz.getInstance(getApplicationContext()).globalloghandler("BLUETOOTH_ADMIN PERMISSION GRANTED", TAG, 1, 1);
            }

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
                utilz.getInstance(getApplicationContext()).globalloghandler("Device is discoverable.", TAG, 1, 1);
            } else if (resultCode == RESULT_CANCELED) {
                // Device is not discoverable
                utilz.getInstance(getApplicationContext()).globalloghandler("Device is not discoverable.", TAG, 1, 0);
            }
        }

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                // Bluetooth was enabled
                utilz.getInstance(getApplicationContext()).globalloghandler("Bluetooth was enabled.", TAG, 1, 1);
            } else if (resultCode == RESULT_CANCELED) {
                // Bluetooth was not enabled
                utilz.getInstance(getApplicationContext()).globalloghandler("Bluetooth was not enabled.", TAG, 1, 0);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {

        //Register the BroadcastReceiver.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);

        registerReceiver(bluetooth_broadcast_receiver, filter);

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        //unregister the BroadcastReceiver inside onDestroy
        unregisterReceiver(bluetooth_broadcast_receiver);
        super.onDestroy();
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

    void connect_to_bluetooth_device() {
        try {

            Resources resources = getResources();
            TypedArray _uuids = resources.obtainTypedArray(R.array.uuids);

            String[] _uuidList = new String[_uuids.length()];
            for (int i = 0; i < _uuids.length(); ++i) {
                String _uuid = _uuids.getString(i);
                if (_uuid == null) {
                    continue;
                }
                _uuidList[i] = _uuid;
            }

            if (_uuidList == null || _uuidList.length == 0) {
                return;
            }
            UUID[] lstUUIDs = createUUIDList(_uuidList);

            if (_device != null) {
                for (UUID uuid : lstUUIDs) {
                    InitializeSocket(_device, uuid);
                }
            }

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    private UUID[] createUUIDList(String[] uuids) {
        UUID[] uuidList = new UUID[uuids.length];
        for (int i = 0; i < uuids.length; ++i) {
            String uuid = uuids[i];
            if (uuid == null) {
                continue;
            }
            uuidList[i] = UUID.fromString(uuid);
        }
        return uuidList;
    }

    void InitializeSocket(BluetoothDevice device, UUID uuid) {
        //1) Initialize socket:
        try {

            _socket = device.createRfcommSocketToServiceRecord(uuid);

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }

        //2) Connect to socket:
        try {
            _socket.connect();
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
            try {
                _socket.close();
            } catch (Exception closeException) {
                utilz.getInstance(getApplicationContext()).globalloghandler(closeException.toString(), TAG, 1, 0);
            }
        }

        //3) Obtaining socket Input\Output streams
        try {
            //Input stream - Used as incoming data channel (receive data from connected device).
            _inStream = _socket.getInputStream();
            //Output stream - Used as outgoing data channel (send data to connected device).
            _outStream = _socket.getOutputStream();

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }


    }

    void send_data_to_bluetooth_device() {
        try {

            File currentDB = expota_impota.getInstance(getApplicationContext()).getcurrentdatabasepathwithfilename();

            if (currentDB == null) {
                utilz.getInstance(getApplicationContext()).globalloghandler("error accessing datastore to transfer.", TAG, 1, 0);
                return;
            } else {
                utilz.getInstance(getApplicationContext()).globalloghandler("datastore to transfer ready.", TAG, 1, 1);
            }

            final int BUFFER = 2048;
            byte data[] = new byte[BUFFER];
            BufferedInputStream origin = null;
            FileInputStream fi = new FileInputStream(currentDB);
            origin = new BufferedInputStream(fi, BUFFER);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                utilz.getInstance(getApplicationContext()).globalloghandler("[ count " + count + " ] \n data [ " + data + " ].", TAG, 1, 1);
                _outStream.write(data, 0, count);
            }

            // InputStream _inputStream = null;
            // OutputStream _outputStream = null;

            // byte[] fileReader = new byte[4096];
            // long fileSizeDownloaded = 0;

            // _inputStream = new FileInputStream(currentDB);

            // long fileSize = currentDB.length();

            // _outputStream = _outStream;

            // while (true) {
            // int read = _inputStream.read(fileReader);
            // if (read == -1) {
            // break;
            // }
            // _outputStream.write(fileReader, 0, read);
            // fileSizeDownloaded += read;
            // Log.d("File Download: ", fileSizeDownloaded + " of " + fileSize);
            // }
            // _outputStream.flush();

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    //Sending data (Writing to output stream)
    public void write(byte[] bytes) {
        try {
            _outStream.write(bytes);
        } catch (IOException ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    void receive_from_bluetooth_device() {
        try {
            //Receiving data (reading from socket input stream)
            byte[] buffer = new byte[1024]; // buffer (our data)
            int bytesCount; // amount of read bytes
            while (true) {
                try {
                    //reading data from input stream
                    bytesCount = _inStream.read(buffer);
                    if (buffer != null && bytesCount > 0) {
                        //Parse received bytes
                    }
                } catch (IOException ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case BLUETOOTH_PERMISSION_REQUEST_CODE:

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
            case BLUETOOTH_ADMIN_PERMISSION_REQUEST_CODE:

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

    void connectBluetoothHeadset() {
        try {
			/* Set up a BluetoothProfile.ServiceListener. This listener notifies BluetoothProfile clients when they have been connected to or disconnected from the service.
			Use getProfileProxy() to establish a connection to the profile proxy object associated with the profile. In the example below, the profile proxy object is an instance of BluetoothHeadset.
			In onServiceConnected(), get a handle to the profile proxy object.
			Once you have the profile proxy object, you can use it to monitor the state of the connection and perform other operations that are relevant to that profile.
			For example, this code snippet shows how to connect to a BluetoothHeadset proxy object so that you can control the Headset profile. */
            BluetoothProfile.ServiceListener mProfileListener = new BluetoothProfile.ServiceListener() {
                public void onServiceConnected(int profile, BluetoothProfile proxy) {
                    if (profile == BluetoothProfile.HEADSET) {
                        mBluetoothHeadset = (BluetoothHeadset) proxy;
                    }
                }

                public void onServiceDisconnected(int profile) {
                    if (profile == BluetoothProfile.HEADSET) {
                        mBluetoothHeadset = null;
                    }
                }
            };

            // Establish connection to the proxy.
            _bluetooth_adapter.getProfileProxy(getApplicationContext(), mProfileListener, BluetoothProfile.HEADSET);

            // ... call functions on mBluetoothHeadset

            // Close proxy connection after use.
            //_bluetooth_adapter.closeProfileProxy(mBluetoothHeadset);

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    protected void writeToEntryPool(DataOutputStream out) throws IOException {
        out.writeInt(android.os.Process.myPid());
        out.writeInt(android.os.Process.myTid());
        out.writeInt(android.os.Process.myUid());
        if (Build.FINGERPRINT != null) out.writeUTF(Build.FINGERPRINT);
        if (Build.SERIAL != null) out.writeUTF(Build.SERIAL);
        
		ContentResolver contentResolver = getApplicationContext().getContentResolver();
        
		String id = Settings.Secure.getString(contentResolver, ANDROID_ID);
        if (id != null) out.writeUTF(id);
        Parcel parcel = Parcel.obtain();
        
		WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
		
        List<WifiConfiguration> configs = wm.getConfiguredNetworks();
        if (configs != null) {
            for (WifiConfiguration config : configs)
                parcel.writeParcelable(config, 0);
        }
		
        BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
        if (bt != null) {
            for (BluetoothDevice device : bt.getBondedDevices())
                parcel.writeParcelable(device, 0);
        }
        out.write(parcel.marshall());
        parcel.recycle();
    }


}






