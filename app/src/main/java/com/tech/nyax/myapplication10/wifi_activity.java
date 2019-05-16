package com.tech.nyax.myapplication10;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class wifi_activity extends AppCompatActivity {

    public static final String TAG = wifi_activity.class.getSimpleName();
    TextView txtWifiInfo;
    Button btnturnwifion, btnturnwifioff, btnScan, btnconnecttowifi, btnEnumerate_Network_Interface_Addresses;
    /* This is the API to use when performing Wi-Fi specific operations. To perform operations that pertain to network connectivity at an abstract level, use ConnectivityManager.
    Instances of this class must be obtained using Context.getSystemService(Class) with the argument WifiManager.class or Context.getSystemService(String) with the argument Context.WIFI_SERVICE. It deals with several categories of items:
    The list of configured networks. The list can be viewed and updated, and attributes of individual entries can be modified.
    The currently active Wi-Fi network, if any. Connectivity can be established or torn down, and dynamic information about the state of the network can be queried.
    Results of access point scans, containing enough information to make decisions about what access point to connect to.
    It defines the names of various Intent actions that are broadcast upon any sort of change in Wi-Fi state.*/
    WifiManager _wifi_manager;
    ConnectivityManager _connectivity_manager;
    WifiScanReceiver wifiReceiver;
    /* a list of access points found in the most recent scan. An app must hold ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION permission in order to get valid results. If there is a remote exception (e.g., either a communication problem with the system service or an exception within the framework) an empty list will be returned. */
    List<ScanResult> _wifi_scan_results;
    /* a list of all the networks configured for the current foreground user. Not all fields of WifiConfiguration are returned.
    Only the following fields are filled in:
    networkId
    SSID
    BSSID
    priority
    allowedProtocols
    allowedKeyManagement
    allowedAuthAlgorithms
    allowedPairwiseCiphers
    allowedGroupCiphers */
    List<WifiConfiguration> _configured_networks;
    // Unique request code
    private static final int ACCESS_WIFI_STATE_PERMISSION_REQUEST_CODE = 3;
    // Unique request code
    private static final int CHANGE_WIFI_STATE_PERMISSION_REQUEST_CODE = 4;
    // Unique request code
    private static final int ACCESS_NETWORK_STATE_PERMISSION_REQUEST_CODE = 5;
    // Unique request code
    private static final int READ_PHONE_STATE_PERMISSION_REQUEST_CODE = 6;
    // Unique request code
    private static final int WRITE_SETTINGS_PERMISSION_REQUEST_CODE = 7;
    // reusable string object
    private static StringBuilder _string_builder = new StringBuilder();
    
    private static final int WIFICIPHER_NOPASS = 10;
    private static final int WIFICIPHER_WEP = 11;
    private static final int WIFICIPHER_WPA = 12;
    SharedPreferences _SharedPreferences = null;
    SharedPreferences.Editor _SharedPreferences_editor = null;
    private static final String SECURE_OPEN = "OPEN";
    private static final String SECURE_WPA = "WPA";
    private static final String SECURE_WPA2 = "WPA2";
    private static final String SECURE_WEP = "WEP";
    private static final String PREFS_FILE = "wifi_prefs";
    private static final int SECURITY_PSK = 13;
    private static final int SECURITY_EAP = 14;
    private static final int SECURITY_WEP = 15;
    private static final int SECURITY_NONE = 16;
    private static final int WIFI_CONFIG_PRIORITY = 40;
    private static final int WIFI_CONFIG_DEFAULT_INDEX = 0;
	final String wifi_SSID = "scylla";
    final String wifi_Pass = "ntharene231";
    final String WIFI_ESSID = "scylla";
    final String WIFI_PWD = "ntharene231";
    private static final int WIFI_AP_STATE_UNKNOWN = 17;
    private static final int WIFI_AP_STATE_DISABLING = 18;
    private static final int WIFI_AP_STATE_ENABLED = 19;
    private static final int WIFI_AP_STATE_FAILED = 20;
    private static final int WIFI_AP_STATE_ENABLING = 21;
    private static final int WIFI_AP_STATE_DISABLED = 22;
    private static final int WIFI_SCAN_TIMEOUT = 20000;
    private static final int WAIT_FOR_SCAN_RESULT = 20000;
    private final String AP_CONFIG_FILE = "";
    private final boolean WIFI_CONFIG_DONE = true;
    // Unique request code
    private static final int ACTION_PICK_WIFI_NETWORK_REQUEST_CODE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_layout);
        Log.e(TAG, "wifi_activity onCreate");

        getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));

        txtWifiInfo = findViewById(R.id.txtWifiInfo);
        txtWifiInfo.setText("");

        btnturnwifion = findViewById(R.id.btnturnwifion);
        btnturnwifion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    _string_builder = new StringBuilder();
                    _string_builder.append("turning wifi on...\n");

                    boolean _is_wifi_enabled = _wifi_manager.setWifiEnabled(true);

                    if (_is_wifi_enabled)
                        _string_builder.append("wifi turned on.\n");

                    utilz.getInstance(getApplicationContext()).globalloghandler(_string_builder.toString(), TAG, 1, 1);

                    txtWifiInfo.setText(_string_builder.toString());

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });

        btnturnwifioff = findViewById(R.id.btnturnwifioff);
        btnturnwifioff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    _string_builder = new StringBuilder();
                    _string_builder.append("turning wifi off...\n");

                    boolean _is_wifi_disabled = _wifi_manager.setWifiEnabled(false);

                    if (_is_wifi_disabled)
                        _string_builder.append("wifi turned off.\n");

                    utilz.getInstance(getApplicationContext()).globalloghandler(_string_builder.toString(), TAG, 1, 1);

                    txtWifiInfo.setText(_string_builder.toString());

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });

        btnScan = findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (check_wifi_validity()) {

                        _string_builder = new StringBuilder();
                        utilz.getInstance(getApplicationContext()).globalloghandler("wifi scan initiated...", TAG, 1, 1);

                        _string_builder.append(getResources().getString(R.string.txtWifiInfo) + "\n");

                        txtWifiInfo.setText(_string_builder.toString());

                        _wifi_manager.startScan();

                    }

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });

        btnEnumerate_Network_Interface_Addresses = findViewById(R.id.btnEnumerate_Network_Interface_Addresses);
        btnEnumerate_Network_Interface_Addresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (check_wifi_validity()) {

                        _string_builder = new StringBuilder();

                        _string_builder.append("Enumerating Network Interface Addresses...\n");

                        utilz.getInstance(getApplicationContext()).globalloghandler(_string_builder.toString(), TAG, 1, 1);

                        txtWifiInfo.setText(_string_builder.toString());

                        Enumerate_Network_Interface_Addresses();

                    }

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });

        btnconnecttowifi = findViewById(R.id.btnconnecttowifi);
        btnconnecttowifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (check_wifi_validity()) {

                        _string_builder = new StringBuilder();
                        utilz.getInstance(getApplicationContext()).globalloghandler("connecting to wifi access point...", TAG, 1, 1);

                        boolean _ConnectToNetworkWEP = ConnectToNetworkWEP(wifi_SSID, wifi_Pass);

                        utilz.getInstance(getApplicationContext()).globalloghandler("_ConnectToNetworkWEP [ " + _ConnectToNetworkWEP + " ].", TAG, 1, 1);

                        boolean _ConnectToNetworkWPA = ConnectToNetworkWPA(wifi_SSID, wifi_Pass);

                        utilz.getInstance(getApplicationContext()).globalloghandler("_ConnectToNetworkWPA [ " + _ConnectToNetworkWPA + " ].", TAG, 1, 1);

                        utilz.getInstance(getApplicationContext()).globalloghandler("make bluetooth discoverable...", TAG, 1, 1);

                        Intent _pick_wifi_Intent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);

                        startActivityForResult(_pick_wifi_Intent, ACTION_PICK_WIFI_NETWORK_REQUEST_CODE);

                    }

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });

        try {

            _SharedPreferences = getApplicationContext().getSharedPreferences(PREFS_FILE, MODE_PRIVATE);

            _SharedPreferences_editor = _SharedPreferences.edit();

            _wifi_manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            wifiReceiver = new WifiScanReceiver();

            //Register the BroadcastReceiver.
            IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

            registerReceiver(wifiReceiver, filter);

            check_Runtime_Permissions();

            writePermission();

            relaunch_scan_immediately();

            Enumerate_Network_Interface_Addresses();

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_PICK_WIFI_NETWORK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Device is discoverable
                utilz.getInstance(getApplicationContext()).globalloghandler("wifi network selected.", TAG, 1, 1);

                NetworkInfo _NetworkInfo = data.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

                WifiInfo _WifiInfo = data.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);

            } else if (resultCode == RESULT_CANCELED) {
                // Device is not discoverable
                utilz.getInstance(getApplicationContext()).globalloghandler("wifi network not selected.", TAG, 1, 0);
            }
        }


    }

    boolean check_wifi_validity() {
        try {

            _wifi_manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            if (_wifi_manager == null) {
                // Device doesn't support wifi

                utilz.getInstance(getApplicationContext()).globalloghandler("Device doesn't support wifi.", TAG, 1, 0);

                return false;

            } else {
                // Device support wifi

                utilz.getInstance(getApplicationContext()).globalloghandler("Device support wifi.", TAG, 1, 1);

            }

            if (!_wifi_manager.isWifiEnabled()) {
                //wifi not enabled.
                utilz.getInstance(getApplicationContext()).globalloghandler("wifi not enabled.", TAG, 1, 0);

                return false;

            } else {
                //wifi enabled.
                utilz.getInstance(getApplicationContext()).globalloghandler("wifi enabled.", TAG, 1, 1);

            }

            return true;

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
            return false;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {

        //Register the BroadcastReceiver.
        IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        registerReceiver(wifiReceiver, filter);

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        //unregister the BroadcastReceiver inside onDestroy
        unregisterReceiver(wifiReceiver);
        super.onDestroy();
    }

    private class WifiScanReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {

            try {

                /* the list of access points found in the most recent scan. An app must hold ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION permission in order to get valid results. If there is a remote exception (e.g., either a communication problem with the system service or an exception within the framework) an empty list will be returned. */
                List<ScanResult> wifiScanList = _wifi_manager.getScanResults();

                for (int i = 0; i < wifiScanList.size(); i++) {

                    String _info = (wifiScanList.get(i)).toString() + "\n" + wifiScanList.get(i).level;

                    _string_builder.append("[ " + _info + " ]\n");

                    Log.e(TAG, "[ " + _string_builder.toString() + " ].");

                    utilz.getInstance(getApplicationContext()).globalloghandler("[ " + _string_builder.toString() + " ].", TAG, 1, 1);

                    // info += ((wifiScanList.get(i)).toString());
                    // txtWifiInfo.append(info + "\n\n");
                    // Log.e("scanResult", "Speed of wifi [ " + wifiScanList.get(i).level + " ]");//The db level of signal
                    // boolean isconnected = ConnectToNetworkWEP("scylla", "ntharene231");
                }
                txtWifiInfo.setText(_string_builder.toString());

            } catch (Exception ex) {
                utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
            }
        }
    }

    //connects to a Wi-Fi access point with WEP encryption, given an SSID and the password.
    public boolean ConnectToNetworkWEP(String networkSSID, String password) {
        try {
            WifiConfiguration conf = new WifiConfiguration();
            // Please note the quotes. String should contain SSID in quotes
            /* The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405). */
            conf.SSID = "\"" + networkSSID + "\"";
            conf.wepKeys[0] = "\"" + password + "\""; //Try it with quotes first
            /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            /* The set of group ciphers supported by this configuration. See WifiConfiguration.GroupCipher for descriptions of the values. Defaults to CCMP, TKIP, WEP104, WEP40. */
            conf.allowedGroupCiphers.set(WifiConfiguration.AuthAlgorithm.OPEN);
            conf.allowedGroupCiphers.set(WifiConfiguration.AuthAlgorithm.SHARED);

            int networkId = _wifi_manager.addNetwork(conf);

            _string_builder.append("networkId [ " + networkId + " ]\n");

            Log.e(TAG, "[ " + _string_builder.toString() + " ].");

            utilz.getInstance(getApplicationContext()).globalloghandler("[ " + _string_builder.toString() + " ].", TAG, 1, 1);

            if (networkId == -1) {
                //Try it again with no quotes in case of hex password
                conf.wepKeys[0] = password;
                networkId = _wifi_manager.addNetwork(conf);
            }

            List<WifiConfiguration> list = _wifi_manager.getConfiguredNetworks();

            for (WifiConfiguration i : list) {

                _string_builder.append("SSID [ " + i.SSID + " ]\n");

                _string_builder.append("networkId [ " + i.networkId + " ]\n");

                Log.e(TAG, "[ " + _string_builder.toString() + " ].");

                utilz.getInstance(getApplicationContext()).globalloghandler("[ " + _string_builder.toString() + " ].", TAG, 1, 1);

                if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    /* Disassociate from the currently active access point. This may result in the asynchronous delivery of state change events.Returns true if the operation succeeded. */
                    _wifi_manager.disconnect();
					/* Allow a previously configured network to be associated with. If attemptConnect is true, an attempt to connect to the selected network is initiated. This may result in the asynchronous delivery of state change events.
		Note: If an application's target SDK version is Build.VERSION_CODES.LOLLIPOP or newer, network communication may not use Wi-Fi even if Wi-Fi is connected; traffic may instead be sent through another network, such as cellular data, Bluetooth tethering, or Ethernet. For example, traffic will never use a Wi-Fi network that does not provide Internet access (e.g. a wireless printer), if another network that does offer Internet access (e.g. cellular data) is available. Applications that need to ensure that their network traffic uses Wi-Fi should use APIs such as Network.bindSocket(java.net.Socket), Network.openConnection(java.net.URL), or ConnectivityManager.bindProcessToNetwork(Network) to do so. Applications are not allowed to enable networks created by other applications.
		Parameters
		netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks().
		attemptConnect 	boolean: The way to select a particular network to connect to is specify true for this parameter.
		Returns true if the operation succeeded */
                    _wifi_manager.enableNetwork(i.networkId, true);
                    //Reconnect to the currently active access point, if we are currently disconnected.
                    _wifi_manager.reconnect();
                    break;
                }
            }
            txtWifiInfo.setText(_string_builder.toString());

            //WiFi Connection success, return true
            return true;
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
            return false;
        }
    }

    //connects to a Wi-Fi access point with WPA2 encryption.
    public boolean ConnectToNetworkWPA(String networkSSID, String password) {
        try {
            WifiConfiguration conf = new WifiConfiguration();
            // Please note the quotes. String should contain SSID in quotes
            /* The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405). */
            conf.SSID = "\"" + networkSSID + "\"";
            // Pre-shared key for use with WPA-PSK.
            conf.preSharedKey = "\"" + password + "\"";
            conf.status = WifiConfiguration.Status.ENABLED;
            /* The set of authentication protocols supported by this configuration. See WifiConfiguration.AuthAlgorithm for descriptions of the values. Defaults to automatic selection. */
            conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            /* The set of group ciphers supported by this configuration. See WifiConfiguration.GroupCipher for descriptions of the values. Defaults to CCMP, TKIP, WEP104, WEP40. */
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            /* The set of pairwise ciphers for WPA supported by this configuration. See WifiConfiguration.PairwiseCipher for descriptions of the values. Defaults to CCMP, TKIP. */
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

            Log.e("connecting", conf.SSID + " " + conf.preSharedKey);

            _wifi_manager.addNetwork(conf);

            Log.e("after connecting", conf.SSID + " " + conf.preSharedKey);

            List<WifiConfiguration> list = _wifi_manager.getConfiguredNetworks();

            for (WifiConfiguration i : list) {

                _string_builder.append("SSID [ " + i.SSID + " ]\n");

                _string_builder.append("networkId [ " + i.networkId + " ]\n");

                Log.e(TAG, "[ " + _string_builder.toString() + " ].");

                utilz.getInstance(getApplicationContext()).globalloghandler("[ " + _string_builder.toString() + " ].", TAG, 1, 1);

                if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    /* Disassociate from the currently active access point. This may result in the asynchronous delivery of state change events.Returns true if the operation succeeded. */
                    _wifi_manager.disconnect();
					/* Allow a previously configured network to be associated with. If attemptConnect is true, an attempt to connect to the selected network is initiated. This may result in the asynchronous delivery of state change events.
		Note: If an application's target SDK version is Build.VERSION_CODES.LOLLIPOP or newer, network communication may not use Wi-Fi even if Wi-Fi is connected; traffic may instead be sent through another network, such as cellular data, Bluetooth tethering, or Ethernet. For example, traffic will never use a Wi-Fi network that does not provide Internet access (e.g. a wireless printer), if another network that does offer Internet access (e.g. cellular data) is available. Applications that need to ensure that their network traffic uses Wi-Fi should use APIs such as Network.bindSocket(java.net.Socket), Network.openConnection(java.net.URL), or ConnectivityManager.bindProcessToNetwork(Network) to do so. Applications are not allowed to enable networks created by other applications.
		Parameters
		netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks().
		attemptConnect 	boolean: The way to select a particular network to connect to is specify true for this parameter.
		Returns true if the operation succeeded */
                    _wifi_manager.enableNetwork(i.networkId, true);
                    //Reconnect to the currently active access point, if we are currently disconnected.
                    _wifi_manager.reconnect();
                    Log.e("re connecting", i.SSID + " " + conf.preSharedKey);
                    break;
                }
            }
            txtWifiInfo.setText(_string_builder.toString());
            //WiFi Connection success, return true
            return true;
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
            return false;
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

    void check_Runtime_Permissions() {
        try {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, ACCESS_WIFI_STATE_PERMISSION_REQUEST_CODE);

            } else {
                //Permission granted
                utilz.getInstance(getApplicationContext()).globalloghandler("ACCESS_WIFI_STATE PERMISSION GRANTED", TAG, 1, 1);
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CHANGE_WIFI_STATE}, CHANGE_WIFI_STATE_PERMISSION_REQUEST_CODE);

            } else {
                //Permission granted
                utilz.getInstance(getApplicationContext()).globalloghandler("CHANGE_WIFI_STATE PERMISSION GRANTED", TAG, 1, 1);
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, ACCESS_NETWORK_STATE_PERMISSION_REQUEST_CODE);

            } else {
                //Permission granted
                utilz.getInstance(getApplicationContext()).globalloghandler("ACCESS_NETWORK_STATE PERMISSION GRANTED", TAG, 1, 1);
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_PERMISSION_REQUEST_CODE);

            } else {
                //Permission granted
                utilz.getInstance(getApplicationContext()).globalloghandler("READ_PHONE_STATE PERMISSION GRANTED", TAG, 1, 1);
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_SETTINGS}, WRITE_SETTINGS_PERMISSION_REQUEST_CODE);

            } else {
                //Permission granted
                utilz.getInstance(getApplicationContext()).globalloghandler("WRITE_SETTINGS PERMISSION GRANTED", TAG, 1, 1);
            }

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ACCESS_WIFI_STATE_PERMISSION_REQUEST_CODE:

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
            case CHANGE_WIFI_STATE_PERMISSION_REQUEST_CODE:

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
            case ACCESS_NETWORK_STATE_PERMISSION_REQUEST_CODE:

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
            case READ_PHONE_STATE_PERMISSION_REQUEST_CODE:

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
            case WRITE_SETTINGS_PERMISSION_REQUEST_CODE:

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

    public boolean setSsidAndPassword(Context context, String ssid, String ssidPassword) {
        try {

            Method getConfigMethod = _wifi_manager.getClass().getMethod("getWifiApConfiguration");

            WifiConfiguration wifiConfig = (WifiConfiguration) getConfigMethod.invoke(_wifi_manager);
            /* The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405). */
            wifiConfig.SSID = ssid;
            // Pre-shared key for use with WPA-PSK.
            wifiConfig.preSharedKey = ssidPassword;

            Method setConfigMethod = _wifi_manager.getClass().getMethod("setWifiApConfiguration", WifiConfiguration.class);

            setConfigMethod.invoke(_wifi_manager, wifiConfig);

            return true;
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
            return false;
        }
    }


    /**
     * @param context Context
     * @return true if device is connected to Internet
     */
    public static boolean isNetworkConnected(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void saveWepConfig(String SSID, String Password, boolean Hidden) {
        WifiConfiguration conf = new WifiConfiguration();
        /* The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405). */
        conf.SSID = "\"".concat(SSID).concat("\"");
        conf.status = WifiConfiguration.Status.DISABLED;
        conf.priority = 40;
        conf.hiddenSSID = Hidden;
        /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
        /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK WPA-EAP. */
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        /* The set of security protocols supported by this configuration. See WifiConfiguration.Protocol for descriptions of the values. Defaults to WPA RSN. */
        conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        /* The set of authentication protocols supported by this configuration. See WifiConfiguration.AuthAlgorithm for descriptions of the values. Defaults to automatic selection. */
        conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
        /* The set of pairwise ciphers for WPA supported by this configuration. See WifiConfiguration.PairwiseCipher for descriptions of the values. Defaults to CCMP, TKIP. */
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        /* The set of group ciphers supported by this configuration. See WifiConfiguration.GroupCipher for descriptions of the values. Defaults to CCMP, TKIP, WEP104, WEP40. */
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);

        if (isHexWepKey(Password)) conf.wepKeys[0] = Password;
        else conf.wepKeys[0] = "\"".concat(Password).concat("\"");
        conf.wepTxKeyIndex = 0;

        int networkId = _wifi_manager.addNetwork(conf);
        if (networkId != -1) {
            /* Allow a previously configured network to be associated with. If attemptConnect is true, an attempt to connect to the selected network is initiated. This may result in the asynchronous delivery of state change events.
		Note: If an application's target SDK version is Build.VERSION_CODES.LOLLIPOP or newer, network communication may not use Wi-Fi even if Wi-Fi is connected; traffic may instead be sent through another network, such as cellular data, Bluetooth tethering, or Ethernet. For example, traffic will never use a Wi-Fi network that does not provide Internet access (e.g. a wireless printer), if another network that does offer Internet access (e.g. cellular data) is available. Applications that need to ensure that their network traffic uses Wi-Fi should use APIs such as Network.bindSocket(java.net.Socket), Network.openConnection(java.net.URL), or ConnectivityManager.bindProcessToNetwork(Network) to do so. Applications are not allowed to enable networks created by other applications.
		Parameters
		netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks().
		attemptConnect 	boolean: The way to select a particular network to connect to is specify true for this parameter.
		Returns true if the operation succeeded */
            _wifi_manager.enableNetwork(networkId, true);
        }

    }

    private boolean isHexWepKey(String s) {
        if (s == null) {
            return false;
        }

        int len = s.length();
        if (len != 10 && len != 26 && len != 58) {
            return false;
        }

        for (int i = 0; i < len; ++i) {
            char c = s.charAt(i);
            if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')) {
                continue;
            }
            return false;
        }
        return true;
    }

    // private static int getSecurity(WifiConfiguration config) {
    // if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK)) {
    // return SECURITY_PSK;
    // }
    // if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP) || config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X)) {
    // return SECURITY_EAP;
    // }
    // return (config.wepKeys[0] != null) ? SECURITY_WEP : SECURITY_NONE;
    // }

    private void reenableAllAps(Context ctx) {

        List<WifiConfiguration> configurations = _wifi_manager.getConfiguredNetworks();
        if (configurations != null) {
            for (WifiConfiguration config : configurations) {
				/* Allow a previously configured network to be associated with. If attemptConnect is true, an attempt to connect to the selected network is initiated. This may result in the asynchronous delivery of state change events.
		Note: If an application's target SDK version is Build.VERSION_CODES.LOLLIPOP or newer, network communication may not use Wi-Fi even if Wi-Fi is connected; traffic may instead be sent through another network, such as cellular data, Bluetooth tethering, or Ethernet. For example, traffic will never use a Wi-Fi network that does not provide Internet access (e.g. a wireless printer), if another network that does offer Internet access (e.g. cellular data) is available. Applications that need to ensure that their network traffic uses Wi-Fi should use APIs such as Network.bindSocket(java.net.Socket), Network.openConnection(java.net.URL), or ConnectivityManager.bindProcessToNetwork(Network) to do so. Applications are not allowed to enable networks created by other applications.
		Parameters
		netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks().
		attemptConnect 	boolean: The way to select a particular network to connect to is specify true for this parameter.
		Returns true if the operation succeeded */
                _wifi_manager.enableNetwork(config.networkId, false);
            }
        }
    }

    private String getSsid(WifiInfo info) {
        String ssid = info.getSSID();
        if (ssid != null) {
            return ssid;
        }
		/*
        OK, it's not in the connectionInfo; we have to go hunt
        ing for it
        */
        List<WifiConfiguration> networks = _wifi_manager.getConfiguredNetworks();
        int length = networks.size();
        for (int i = 0; i < length; i++) {
            if (networks.get(i).networkId == info.getNetworkId()) {
                return networks.get(i).SSID;
            }
        }
        return null;
    }

    public boolean setWifiApEnabled(Context context, boolean enabled) {

        try {
            if (enabled) {
                _wifi_manager.setWifiEnabled(false);
            }
            Method method = _wifi_manager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            return (Boolean) method.invoke(_wifi_manager, null, enabled);
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
            return false;
        }
    }

    /**
     * create wifi spot
     *
     * @return Boolean
     */
    // public Boolean createWifiAp(String name, String password) {
    // /**
    // * ensure close wifi
    // */
    // if (_wifi_manager.isWifiEnabled()) {
    // _wifi_manager.setWifiEnabled(false);
    // }
    // /**
    // * ensure close wifi spot that exists
    // */
    // if (isWifiApEnabled()) {
    // if (closeWifiAp()) {
    // Log.e(TAG, "closeWifiAp succeed");
    // } else {
    // Log.e(TAG, "closeWifiAp failed");
    // }
    // }
    // try {
    // Method method = _wifi_manager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
    // WifiConfiguration configuration = new WifiConfiguration();
    // configuration.SSID = name;
    // Pre-shared key for use with WPA-PSK.
    // configuration.preSharedKey = password;
    // return (Boolean) method.invoke(_wifi_manager, configuration, true);
    // } catch (Exception e) {
    // Log.e(TAG, "createWifiAp:", e);
    // return false;
    // }
    // }
    private boolean setDefaultValue(Context ctx, boolean default_ssid, boolean clear_password) {

        // if (_wifi_manager == null) {
        // return false;
        // }
        // WifiConfiguration wifiAPConfig = _wifi_manager.getWifiApConfiguration();
        // if (wifiAPConfig == null) {
        // return false;
        // }
        // if (default_ssid) {
        // TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        // String deviceId = tm.getDeviceId();
        // String lastFourDigits = "";
        // if ((deviceId != null) && (deviceId.length() > 3)) {
        // lastFourDigits = deviceId.substring(deviceId.length() - 4);
        // }
        // wifiAPConfig.SSID = Build.MODEL;
        // if ((!TextUtils.isEmpty(lastFourDigits)) && (wifiAPConfig.SSID != null) && (wifiAPConfig.SSID.indexOf(lastFourDigits) < 0)) {
        // wifiAPConfig.SSID += " " + lastFourDigits;
        // }
        // }
        // if (clear_password) {
        // wifiAPConfig.preSharedKey = "";
        // }
        // _wifi_manager.setWifiApConfiguration(wifiAPConfig);
        return true;
    }

    /**
     * WifiConfiguration
     *
     * @param ssid
     * @param password
     * @param type
     * @return
     */
    public static WifiConfiguration createWifiConfiguration(String ssid, String password, int type) {
        WifiConfiguration config = new WifiConfiguration();
        /* The set of authentication protocols supported by this configuration. See WifiConfiguration.AuthAlgorithm for descriptions of the values. Defaults to automatic selection. */
        config.allowedAuthAlgorithms.clear();
        /* The set of group ciphers supported by this configuration. See WifiConfiguration.GroupCipher for descriptions of the values. Defaults to CCMP, TKIP, WEP104, WEP40. */
        config.allowedGroupCiphers.clear();
        /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
        config.allowedKeyManagement.clear();
        /* The set of pairwise ciphers for WPA supported by this configuration. See WifiConfiguration.PairwiseCipher for descriptions of the values. Defaults to CCMP, TKIP. */
        config.allowedPairwiseCiphers.clear();
        /* The set of security protocols supported by this configuration. See WifiConfiguration.Protocol for descriptions of the values. Defaults to WPA RSN. */
        config.allowedProtocols.clear();
        /* The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405). */
        config.SSID = "\"" + ssid + "\"";
        if (type == WIFICIPHER_NOPASS) {
            //            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            //            config.wepTxKeyIndex = 0;
            //            config.wepKeys[0] = "";
            //            config.wepTxKeyIndex = 0;
        } else if (type == WIFICIPHER_WEP) {
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + password + "\"";
            /* The set of authentication protocols supported by this configuration. See WifiConfiguration.AuthAlgorithm for descriptions of the values. Defaults to automatic selection. */
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            /* The set of group ciphers supported by this configuration. See WifiConfiguration.GroupCipher for descriptions of the values. Defaults to CCMP, TKIP, WEP104, WEP40. */
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

            config.wepTxKeyIndex = 0;
        } else if (type == WIFICIPHER_WPA) {
            // Pre-shared key for use with WPA-PSK.
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            /* The set of authentication protocols supported by this configuration. See WifiConfiguration.AuthAlgorithm for descriptions of the values. Defaults to automatic selection. */
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            /* The set of group ciphers supported by this configuration. See WifiConfiguration.GroupCipher for descriptions of the values. Defaults to CCMP, TKIP, WEP104, WEP40. */
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            /* The set of pairwise ciphers for WPA supported by this configuration. See WifiConfiguration.PairwiseCipher for descriptions of the values. Defaults to CCMP, TKIP. */
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            /* The set of security protocols supported by this configuration. See WifiConfiguration.Protocol for descriptions of the values. Defaults to WPA RSN. */
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }

    public String execute() {
        List<WifiConfiguration> networks = _wifi_manager.getConfiguredNetworks();
        for (WifiConfiguration w : networks) {

            _string_builder.append("ID: [ " + Integer.toString(w.networkId) + " ]\n");

            _string_builder.append("Name: [ " + w.SSID + " ]\n");

            _string_builder.append("Status: [ " + w.status + " ]\n");

            Log.e(TAG, "[ " + _string_builder.toString() + " ].");

        }
        return _string_builder.toString();
    }

    public static boolean hasKnownNetwork(WifiManager wifiMan) {
        List<WifiConfiguration> known = wifiMan.getConfiguredNetworks();
        /* the list of access points found in the most recent scan. An app must hold ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION permission in order to get valid results. If there is a remote exception (e.g., either a communication problem with the system service or an exception within the framework) an empty list will be returned. */
        List<ScanResult> scanned = wifiMan.getScanResults();
        for (ScanResult scan : scanned) {
            for (WifiConfiguration know : known) {
                if (scan.SSID.equals(know.SSID)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean setAP(boolean shouldOpen) {
        String securityType = "", password = "", ssid = "";
        WifiConfiguration configuration = new WifiConfiguration();
        /* The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405). */
        configuration.SSID = _SharedPreferences.getString(Constants.PREFS_SSID, ssid);
        securityType = _SharedPreferences.getString(Constants.PREFS_SECURITY, securityType);
        password = _SharedPreferences.getString(Constants.PREFS_PASSWORD, password);
        if (securityType.equals(SECURE_OPEN)) {
            /* The set of authentication protocols supported by this configuration. See WifiConfiguration.AuthAlgorithm for descriptions of the values. Defaults to automatic selection. */
            configuration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        } else {
            if (securityType.equals((SECURE_WPA))) {
                /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
                configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            } else if (securityType.equals(SECURE_WPA2)) {
                /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
                configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
            }
            // Pre-shared key for use with WPA-PSK.
            configuration.preSharedKey = password;
        }
        _wifi_manager.setWifiEnabled(false);
        try {
            //USE REFLECTION TO GET METHOD "SetWifiAPEnabled"
//            /*if (isHtc())
//                setHTCSSID(configuration);*/
            Method method = _wifi_manager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(_wifi_manager, configuration, shouldOpen);
            _SharedPreferences_editor.putBoolean(Constants.PREFS_REFLECT_STATUS, true);
            _SharedPreferences_editor.commit();
            return true;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void onUpdateData(int intReason) {
        Log.e(TAG, "Fetching hotspot connectivity information");

        try {
            Log.e(TAG, "Checking if the hotspot is on");

            Method isWifiApEnabled = _wifi_manager.getClass().getDeclaredMethod("isWifiApEnabled");

            Method getWifiApConfiguration = _wifi_manager.getClass().getDeclaredMethod("getWifiApConfiguration");

            final WifiConfiguration wifSettings = (WifiConfiguration) getWifiApConfiguration.invoke(_wifi_manager);

            final ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.TetherSettings");

            if ((Boolean) isWifiApEnabled.invoke(_wifi_manager)) {
                Log.e(TAG, "Checking if the periodic ticker is enabled");

                Log.e(TAG, "Starting a new periodic ticker to check neighbours");

                try {
                    Log.e(TAG, "Counting connections through hotspot");
                    Scanner scaAddresses = new Scanner(new File("/proc/net/arp"));
                    Integer intConnections = 0;
                    while (scaAddresses.hasNextLine()) {
                        Log.e(TAG, scaAddresses.nextLine());
                        intConnections++;
                    }
                    intConnections = intConnections - 1;
                    scaAddresses.close();
                    Log.e(TAG, intConnections + " or more devices connected");
                    // if (wifSettings.SSID == null) {
                    // edtInformation.expandedTitle(getString(R.string.ssid_missing));
                    // } else {
                    // edtInformation.expandedTitle(wifSettings.SSID);
                    // }
                    // edtInformation.expandedBody(getQuantityString(R.plurals.connection, intConnections, intConnections));
                    // edtInformation.clickIntent(new Intent().setComponent(comp));
                    // edtInformation.icon(R.drawable.ic_dashclock);
                    // edtInformation.status(intConnections.toString());
                    // edtInformation.visible(intConnections > 0 || getBoolean("always", true));
                    // doUpdate(edtInformation);
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    Log.e(TAG, "Stopping the periodic checker.");
                    return;
                } catch (Exception e) {
                    Log.e(TAG, "Encountered an error", e);
                }
            } else {
                Log.e(TAG, "Hotspot is off");

            }
        } catch (Exception e) {
            Log.e(TAG, "Encountered an error", e);
        }
        //edtInformation.icon(R.drawable.ic_dashclock);
    }

    public boolean onPreferenceTreeClick(final PreferenceScreen preferenceScreen, final Preference preference) {
        Intent intent;
        final PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(PREFS_FILE);
        List<WifiConfiguration> wifiList = null;
        if (preference.getKey().equals(PREFS_FILE)) {
            /** Clear the wifi list. */
            preferenceCategory.removeAll();
            wifiList = _wifi_manager.getConfiguredNetworks();
        }
        if (wifiList == null) {
            wifiList = Collections.emptyList();
        }
        for (final WifiConfiguration wifi : wifiList) {
            if (wifi != null && wifi.SSID != null) {
                // Friendly SSID-Name
//                final Matcher matcher = QUOTATION_DELIMITER.matcher(wifi.SSID);
//                final String ssid = matcher.replaceAll("");
//                // Add PreferenceScreen for each network
//                final PreferenceScreen ssidItem = getPreferenceManager().createPreferenceScreen(this);
//                intent = new Intent(getApplicationContext(), wifi_activity.class);
//                intent.putExtra("SSID", ssid);
//                ssidItem.setPersistent(false);
//                ssidItem.setKey("wifiNetwork" + ssid);
//                ssidItem.setTitle(ssid);
//                ssidItem.setIntent(intent);
//                if (WifiConfiguration.Status.CURRENT == wifi.status) {
//                    ssidItem.setSummary(R.string.connected);
//                } else {
//                    ssidItem.setSummary(R.string.notInRange);
//                }
//                preferenceCategory.addPreference(ssidItem);
            }
        }
        return false;
    }

    public boolean removeNetwork(Context context, int networkId, String ssid) {
        if (!_wifi_manager.isWifiEnabled()) {
            _wifi_manager.setWifiEnabled(true);
        }
        List<WifiConfiguration> list = _wifi_manager.getConfiguredNetworks();
        if (list.size() == 0) {
            return true;
        }
        String SSID = ssid;
        Iterator<WifiConfiguration> iterator = list.iterator();
        while (iterator.hasNext()) {
            WifiConfiguration wifiConfiguration = iterator.next();
            if (wifiConfiguration.networkId == networkId && wifiConfiguration.SSID.equals(SSID)) {
				/* Remove the specified network from the list of configured networks. This may result in the asynchronous delivery of state change events. Applications are not allowed to remove networks created by other applications.
				Parameters
				netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks(). Returns true if the operation succeeded */
                return _wifi_manager.removeNetwork(wifiConfiguration.networkId);
            }
        }
        return true;
    }

    public boolean isWIFIconnected(Context appContext) {
        String OBD_SSID = "scylla";
        boolean networkStateChanged = false;
        // Grab connection information
        ConnectivityManager conMgr = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConnectInfo = conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        // Grab Wifi connection information

        WifiInfo wifiInfo = _wifi_manager.getConnectionInfo();
        /*
         * Check if Wifi Connection is detected If not, display alert dialog to
         * switch to turn on Wifi and select OBD network
         */
        if (wifiConnectInfo.isConnected()) {
            /*
             * Check if current wifi network is OBD Network, if not Check for
             * the OBD network among current WifiConfigurations If found, switch
             * to OBD Network
             */
            Log.e(TAG, "The current Wifi SSID is: " + wifiInfo.getSSID());
            int initialNetworkID = wifiInfo.getNetworkId();
            if (!wifiInfo.getSSID().equals(OBD_SSID)) {
                // Grab list of all current user configured networks
                List<WifiConfiguration> wifiList = _wifi_manager.getConfiguredNetworks();
                // Mark network state changed to true
                for (WifiConfiguration result : wifiList) {
                    /*
                     * In Wifi Configuration, SSID surround with quotes Consider
                     * storing matched networkID so this process is not
                     * necessary every time log files are downloaded from
                     * Gryphon
                     */
                    if (result.SSID != null && result.SSID.equals("\"" + OBD_SSID + "\"")) {
                        // Disconnect wifi before switching
                        /* Disassociate from the currently active access point. This may result in the asynchronous delivery of state change events.Returns true if the operation succeeded. */
                        _wifi_manager.disconnect();
                        // Switch to matched SSID
						/* Allow a previously configured network to be associated with. If attemptConnect is true, an attempt to connect to the selected network is initiated. This may result in the asynchronous delivery of state change events.
		Note: If an application's target SDK version is Build.VERSION_CODES.LOLLIPOP or newer, network communication may not use Wi-Fi even if Wi-Fi is connected; traffic may instead be sent through another network, such as cellular data, Bluetooth tethering, or Ethernet. For example, traffic will never use a Wi-Fi network that does not provide Internet access (e.g. a wireless printer), if another network that does offer Internet access (e.g. cellular data) is available. Applications that need to ensure that their network traffic uses Wi-Fi should use APIs such as Network.bindSocket(java.net.Socket), Network.openConnection(java.net.URL), or ConnectivityManager.bindProcessToNetwork(Network) to do so. Applications are not allowed to enable networks created by other applications.
		Parameters
		netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks().
		attemptConnect 	boolean: The way to select a particular network to connect to is specify true for this parameter.
		Returns true if the operation succeeded */
                        _wifi_manager.enableNetwork(result.networkId, true);
                        //Reconnect to the currently active access point, if we are currently disconnected.
                        _wifi_manager.reconnect();
                        networkStateChanged = true;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public void connect(final String ssid) {
        Log.e(TAG, "trying to connect to AP:" + ssid);
        final List<WifiConfiguration> hosts = _wifi_manager.getConfiguredNetworks();
        int networkId = -1;
        for (WifiConfiguration conf : hosts) {
            Log.e(TAG, "trying host:" + conf.SSID);
            if (conf.SSID.equalsIgnoreCase("\"" + ssid + "\"")) {
                networkId = conf.networkId;
                Log.e(TAG, "found hosts AP in Android with ID:" + networkId);
                break;
            }
        }
		/* Allow a previously configured network to be associated with. If attemptConnect is true, an attempt to connect to the selected network is initiated. This may result in the asynchronous delivery of state change events.
		Note: If an application's target SDK version is Build.VERSION_CODES.LOLLIPOP or newer, network communication may not use Wi-Fi even if Wi-Fi is connected; traffic may instead be sent through another network, such as cellular data, Bluetooth tethering, or Ethernet. For example, traffic will never use a Wi-Fi network that does not provide Internet access (e.g. a wireless printer), if another network that does offer Internet access (e.g. cellular data) is available. Applications that need to ensure that their network traffic uses Wi-Fi should use APIs such as Network.bindSocket(java.net.Socket), Network.openConnection(java.net.URL), or ConnectivityManager.bindProcessToNetwork(Network) to do so. Applications are not allowed to enable networks created by other applications.
		Parameters
		netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks().
		attemptConnect 	boolean: The way to select a particular network to connect to is specify true for this parameter.
		Returns true if the operation succeeded */
        _wifi_manager.enableNetwork(networkId, true);
    }

    public void connect_sim_network() {
        try {
            String ssid = "";
            TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            // Not getNetworkOperator wrt Roaming
            String simOperator = tel.getSimOperator();
            if (simOperator != null) {
                int mcc = Integer.parseInt(simOperator.substring(0, 3));
                int mnc = Integer.parseInt(simOperator.substring(3));
                if (mcc == 208) {
                    if ((mnc >= 9) && (mnc <= 13)) {
                        ssid = "SFR WiFi Mobile";
                    }
                    if ((mnc >= 15) && (mnc <= 16)) {
                        ssid = "FreeWifi_secure";
                    }
                }
                if (!ssid.isEmpty()) {
                    WifiEnterpriseConfig enterpriseConfig = new WifiEnterpriseConfig();
                    // EAP SIM / AKA for Mobile Phones
                    enterpriseConfig.setEapMethod(WifiEnterpriseConfig.Eap.SIM);
                    SmsManager sm = SmsManager.getDefault();
                    Bundle b = sm.getCarrierConfigValues();
                    String NAI_suffix = b.getString(SmsManager.MMS_CONFIG_NAI_SUFFIX);
                    // IMSI : 208(mcc) + 15(mnc) + 0000XXXXXX + @...
                    // Use 1 + IMSI (See RFC4186)
                    //enterpriseConfig.setIdentity("1" + tel.getSubscriberId() + "@" + NAI_suffix);

                    WifiConfiguration wifiConfig = new WifiConfiguration();
                    /* The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405). */
                    wifiConfig.SSID = ssid;
                    //wifiConfig.priority = 0; // Use lower priority than known APs
                    wifiConfig.status = WifiConfiguration.Status.ENABLED;
                    /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
                    wifiConfig.allowedKeyManagement.clear();
                    wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
                    wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);
                    wifiConfig.enterpriseConfig = enterpriseConfig;

                    _wifi_manager.setWifiEnabled(true);
                    /* Disassociate from the currently active access point. This may result in the asynchronous delivery of state change events.Returns true if the operation succeeded. */
                    _wifi_manager.disconnect();
                    List<WifiConfiguration> list = _wifi_manager.getConfiguredNetworks();
                    for (WifiConfiguration i : list) {
                        if (i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
							/* Remove the specified network from the list of configured networks. This may result in the asynchronous delivery of state change events. Applications are not allowed to remove networks created by other applications.
							Parameters
							netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks(). Returns true if the operation succeeded */
                            _wifi_manager.removeNetwork(i.networkId);
                        }
                    }
                    int networkId = _wifi_manager.addNetwork(wifiConfig);
                    if (networkId != -1) {
                        //Reconnect to the currently active access point, if we are currently disconnected.
                        _wifi_manager.reconnect();
						/* Allow a previously configured network to be associated with. If attemptConnect is true, an attempt to connect to the selected network is initiated. This may result in the asynchronous delivery of state change events.
		Note: If an application's target SDK version is Build.VERSION_CODES.LOLLIPOP or newer, network communication may not use Wi-Fi even if Wi-Fi is connected; traffic may instead be sent through another network, such as cellular data, Bluetooth tethering, or Ethernet. For example, traffic will never use a Wi-Fi network that does not provide Internet access (e.g. a wireless printer), if another network that does offer Internet access (e.g. cellular data) is available. Applications that need to ensure that their network traffic uses Wi-Fi should use APIs such as Network.bindSocket(java.net.Socket), Network.openConnection(java.net.URL), or ConnectivityManager.bindProcessToNetwork(Network) to do so. Applications are not allowed to enable networks created by other applications.
		Parameters
		netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks().
		attemptConnect 	boolean: The way to select a particular network to connect to is specify true for this parameter.
		Returns true if the operation succeeded */
                        _wifi_manager.enableNetwork(networkId, true);
                    }
                }
            }
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    public int configure(String security, String password, boolean bHex, final ScanResult sr) {
        if (_wifi_manager == null) {
            Log.e(TAG, "Null WifiManager");
            return -1;
        }
        WifiConfiguration wConf = new WifiConfiguration();
        //http://developer.android.com/reference/android/net/wifi/WifiConfiguration.html#SSID
        /* The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405). */
        wConf.SSID = "\"" + sr.SSID + "\"";
        wConf.BSSID = sr.BSSID;
        wConf.priority = 40;

        if (security == SECURE_WEP) {
            //hex not quoted
            wConf.wepKeys[0] = bHex ? password : "\"" + password + "\"";
            wConf.wepTxKeyIndex = 0;
            /* The set of authentication protocols supported by this configuration. See WifiConfiguration.AuthAlgorithm for descriptions of the values. Defaults to automatic selection. */
            wConf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            wConf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
            wConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            /* The set of group ciphers supported by this configuration. See WifiConfiguration.GroupCipher for descriptions of the values. Defaults to CCMP, TKIP, WEP104, WEP40. */
            wConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        } else if (security == SECURE_WPA || security == SECURE_WPA2) {
            /* The set of security protocols supported by this configuration. See WifiConfiguration.Protocol for descriptions of the values. Defaults to WPA RSN. */
            wConf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wConf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
            wConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            /* The set of pairwise ciphers for WPA supported by this configuration. See WifiConfiguration.PairwiseCipher for descriptions of the values. Defaults to CCMP, TKIP. */
            wConf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wConf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            /* The set of group ciphers supported by this configuration. See WifiConfiguration.GroupCipher for descriptions of the values. Defaults to CCMP, TKIP, WEP104, WEP40. */
            wConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            wConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            // Pre-shared key for use with WPA-PSK.
            wConf.preSharedKey = "\"".concat("mamadoudiop").concat("\"");
        } else if (security == SECURE_OPEN) {
            /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
            wConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        return _wifi_manager.addNetwork(wConf);
    }

    /**
     * Saves a WEP WIFI Configuration Profile
     *
     * @params SSID, PASSWORD
     * - WiFi SSID and PASSWORD should be passed in.
     */
    public boolean saveWEPConfig(String SSID, String PASSWORD) {
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration wc = new WifiConfiguration();
        // IMP! This should be in Quotes!!
        /* The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405). */
        wc.SSID = "\"" + SSID + "\"";
        wc.hiddenSSID = true;
        wc.status = WifiConfiguration.Status.DISABLED;
        wc.priority = 40;
        /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
        wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        /* The set of security protocols supported by this configuration. See WifiConfiguration.Protocol for descriptions of the values. Defaults to WPA RSN. */
        wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        /* The set of authentication protocols supported by this configuration. See WifiConfiguration.AuthAlgorithm for descriptions of the values. Defaults to automatic selection. */
        wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
        /* The set of pairwise ciphers for WPA supported by this configuration. See WifiConfiguration.PairwiseCipher for descriptions of the values. Defaults to CCMP, TKIP. */
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        /* The set of group ciphers supported by this configuration. See WifiConfiguration.GroupCipher for descriptions of the values. Defaults to CCMP, TKIP, WEP104, WEP40. */
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        // This is the WEP Password
        wc.wepKeys[0] = "\"" + PASSWORD + "\"";
        wc.wepTxKeyIndex = 0;
        WifiManager wifiManag = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
        boolean res1 = wifiManag.setWifiEnabled(true);
        int res = wifi.addNetwork(wc);
        Log.e("WifiPreference", "add Network returned " + res);
        boolean saved = wifi.saveConfiguration();
        Log.e("WifiPreference", "saveConfiguration returned " + saved);
		/* Allow a previously configured network to be associated with. If attemptConnect is true, an attempt to connect to the selected network is initiated. This may result in the asynchronous delivery of state change events.
		Note: If an application's target SDK version is Build.VERSION_CODES.LOLLIPOP or newer, network communication may not use Wi-Fi even if Wi-Fi is connected; traffic may instead be sent through another network, such as cellular data, Bluetooth tethering, or Ethernet. For example, traffic will never use a Wi-Fi network that does not provide Internet access (e.g. a wireless printer), if another network that does offer Internet access (e.g. cellular data) is available. Applications that need to ensure that their network traffic uses Wi-Fi should use APIs such as Network.bindSocket(java.net.Socket), Network.openConnection(java.net.URL), or ConnectivityManager.bindProcessToNetwork(Network) to do so. Applications are not allowed to enable networks created by other applications.
		Parameters
		netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks().
		attemptConnect 	boolean: The way to select a particular network to connect to is specify true for this parameter.
		Returns true if the operation succeeded */
        boolean b = wifi.enableNetwork(res, true);
        Log.e("WifiPreference", "enableNetwork returned " + b);
        return saved;
    }

    static int getSecurity(WifiConfiguration config) {
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK)) {
            return SECURITY_PSK;
        }
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP) || config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X)) {
            return SECURITY_EAP;
        }
        return (config.wepKeys[0] != null) ? SECURITY_WEP : SECURITY_NONE;
    }

    String get_Security_given_config(WifiConfiguration config) {
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK)) {
            return "SECURITY_PSK";
        }
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP) || config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X)) {
            return "SECURITY_EAP";
        }
        return (config.wepKeys[0].toString() != null) ? "SECURITY_WEP" : "SECURITY_NONE";
    }

    String get_Security_given_scanResult(ScanResult scanResult) {
        if (findNetworkInExistingConfig(scanResult.BSSID).allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK)) {
            return "SECURITY_PSK";
        }
        if (findNetworkInExistingConfig(scanResult.BSSID).allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP) || findNetworkInExistingConfig(scanResult.BSSID).allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X)) {
            return "SECURITY_EAP";
        }
        return (findNetworkInExistingConfig(scanResult.BSSID).wepKeys[0].toString() != null) ? "SECURITY_WEP" : "SECURITY_NONE";
    }

    public Wifi getWifiDetail(Context context) {
        String service = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(service);
        NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
        boolean isWIFI = false;
        boolean notDone = false;
        Wifi wifiDetail = new Wifi();
        if (activeNetwork == null) {
            isWIFI = false;
        } else {
            int connectionType = activeNetwork.getType();
            switch (connectionType) {
                case (ConnectivityManager.TYPE_MOBILE):
                    isWIFI = false;
                    break;
                case (ConnectivityManager.TYPE_WIFI):
                    isWIFI = true;
                    break;
                default:
                    break;
            }
        }
        wifiDetail.setWifi(isWIFI);
        if (!isWIFI) {
            notDone = false;
            return wifiDetail;
        } else {
            String srvc = Context.WIFI_SERVICE;
            WifiManager _wifi_manager = (WifiManager) context.getSystemService(srvc);
            WifiInfo info = _wifi_manager.getConnectionInfo();
            if (info.getBSSID() != null) {
                int strength = WifiManager.calculateSignalLevel(info.getRssi(), 11);
                int ipAddress = info.getIpAddress();
                int speed = info.getLinkSpeed();
                int networkId = info.getNetworkId();
                int rssi = info.getRssi();
                String macAddress = info.getMacAddress();
                String ssid = info.getSSID();
                //ssid = ssid.substring(1, ssid.length() - 1);
                SupplicantState supState = info.getSupplicantState();
                String detailedInfo = supState.toString();
                String units = WifiInfo.LINK_SPEED_UNITS;
                //String cSummary = String.format("Connected to %s at %s%s. Strength %s", ssid, speed, units, strength);
                wifiDetail.setDetailedInfo(detailedInfo);
                wifiDetail.setIpAddress(ipAddress);
                wifiDetail.setMacAddress(macAddress);
                wifiDetail.setNetworkId(networkId);
                wifiDetail.setRssi(rssi);
                wifiDetail.setSpeed(speed);
                wifiDetail.setSsid(ssid);
                wifiDetail.setStrength(strength);
                wifiDetail.setUnits(units);
                wifiDetail.setPreferred(false);
            }
            // List available networks
            ArrayList<WifiPreference> preference = new ArrayList<WifiPreference>();
            List<WifiConfiguration> configs = _wifi_manager.getConfiguredNetworks();
            for (int i = 0; i < configs.size(); i++) {
                WifiPreference wifiPref = new WifiPreference();
                String ssid = configs.get(i).SSID;
                ssid = ssid.substring(1, ssid.length() - 1);
                if (ssid.equalsIgnoreCase(wifiDetail.getSsid())) {
                    wifiPref.setConnected(true);
                    wifiDetail.setPreferred(true);
                } else {
                    wifiPref.setConnected(false);
                }
                /* The set of authentication protocols supported by this configuration. See WifiConfiguration.AuthAlgorithm for descriptions of the values. Defaults to automatic selection. */
                String authAlgorithm = configs.get(i).allowedAuthAlgorithms.toString();
                /* The set of group ciphers supported by this configuration. See WifiConfiguration.GroupCipher for descriptions of the values. Defaults to CCMP, TKIP, WEP104, WEP40. */
                String groupCiphers = configs.get(i).allowedGroupCiphers.toString();
                String pairwiseCiphers = configs.get(i).
                        /* The set of pairwise ciphers for WPA supported by this configuration. See WifiConfiguration.PairwiseCipher for descriptions of the values. Defaults to CCMP, TKIP. */
                                allowedPairwiseCiphers.toString();
                /* The set of security protocols supported by this configuration. See WifiConfiguration.Protocol for descriptions of the values. Defaults to WPA RSN. */
                String protocols = configs.get(i).allowedProtocols.toString();
                String bssid = configs.get(i).BSSID;
                int networkid = configs.get(i).networkId;
                int priority = configs.get(i).priority;
                int status = configs.get(i).status;
                /* The set of authentication protocols supported by this configuration. See WifiConfiguration.AuthAlgorithm for descriptions of the values. Defaults to automatic selection. */
                wifiPref.setAuthAlgorithms(authAlgorithm);
                wifiPref.setBssid(bssid);
                wifiPref.setGroupCiphers(groupCiphers);
                /* The set of pairwise ciphers for WPA supported by this configuration. See WifiConfiguration.PairwiseCipher for descriptions of the values. Defaults to CCMP, TKIP. */
                wifiPref.setPairwiseCiphers(pairwiseCiphers);
                wifiPref.setPriority(priority);
                wifiPref.setSsid(ssid);
                wifiPref.setProtocols(protocols);
                wifiPref.setNetworkid(networkid);
                wifiPref.setStatus(status);
                preference.add(wifiPref);
            }
            wifiDetail.setPreference(preference);
        }
        notDone = false;
        return wifiDetail;
    }

    protected void setNewWifi(String ssid) {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
        boolean foundAKnownNetwork = false;
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration wifiConfiguration : configuredNetworks) {
            if (wifiConfiguration.SSID.equals("\"" + ssid + "\"")) {
                foundAKnownNetwork = true;
				/* Allow a previously configured network to be associated with. If attemptConnect is true, an attempt to connect to the selected network is initiated. This may result in the asynchronous delivery of state change events.
		Note: If an application's target SDK version is Build.VERSION_CODES.LOLLIPOP or newer, network communication may not use Wi-Fi even if Wi-Fi is connected; traffic may instead be sent through another network, such as cellular data, Bluetooth tethering, or Ethernet. For example, traffic will never use a Wi-Fi network that does not provide Internet access (e.g. a wireless printer), if another network that does offer Internet access (e.g. cellular data) is available. Applications that need to ensure that their network traffic uses Wi-Fi should use APIs such as Network.bindSocket(java.net.Socket), Network.openConnection(java.net.URL), or ConnectivityManager.bindProcessToNetwork(Network) to do so. Applications are not allowed to enable networks created by other applications.
		Parameters
		netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks().
		attemptConnect 	boolean: The way to select a particular network to connect to is specify true for this parameter.
		Returns true if the operation succeeded */
                boolean result = wifiManager.enableNetwork(wifiConfiguration.networkId, true);
                if (result) {
                    utilz.getInstance(getApplicationContext()).globalloghandler("Now connected to known network \"" + ssid + "\". If you want to set a new WPA key, please delete the network first.", TAG, 1, 0);
                } else {
                    utilz.getInstance(getApplicationContext()).globalloghandler("Connection to a known network failed.", TAG, 1, 0);
                }
            }
        }
        if (!foundAKnownNetwork) {
            //setupNewNetwork(ssid, wifiManager);
        }
    }

    public static boolean configureNetwork(Context context) {
        Log.e(TAG, "Attempting to auto-configure network");
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (manager == null)
            return false;
        Resources res = context.getResources();
        WifiConfiguration config = new WifiConfiguration();
        try {
            boolean useDhcp = res.getBoolean(R.bool.network_use_dhcp);
            ContentResolver cr = context.getContentResolver();
            if (!useDhcp) {
                Settings.System.putString(cr, Settings.System.WIFI_STATIC_IP, res.getString(R.string.network_static_ip));
                Settings.System.putString(cr, Settings.System.WIFI_STATIC_NETMASK, res.getString(R.string.network_static_netmask));
                Settings.System.putString(cr, Settings.System.WIFI_STATIC_GATEWAY, res.getString(R.string.network_static_gateway));
                Settings.System.putString(cr, Settings.System.WIFI_STATIC_DNS1, res.getString(R.string.network_static_dns_1));
            }
            Settings.System.putInt(cr, Settings.System.WIFI_USE_STATIC_IP, useDhcp ? 0 : 1);
            /* The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405). */
            config.SSID = res.getString(R.string.network_wifi_essid);
            /* The set of authentication protocols supported by this configuration. See WifiConfiguration.AuthAlgorithm for descriptions of the values. Defaults to automatic selection. */
            config.allowedAuthAlgorithms.clear();
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            /* The set of group ciphers supported by this configuration. See WifiConfiguration.GroupCipher for descriptions of the values. Defaults to CCMP, TKIP, WEP104, WEP40. */
            config.allowedGroupCiphers.clear();
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
            config.allowedKeyManagement.clear();
            // Added for WPA2
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);
            /* The set of pairwise ciphers for WPA supported by this configuration. See WifiConfiguration.PairwiseCipher for descriptions of the values. Defaults to CCMP, TKIP. */
            config.allowedPairwiseCiphers.clear();
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            /* The set of group ciphers supported by this configuration. See WifiConfiguration.GroupCipher for descriptions of the values. Defaults to CCMP, TKIP, WEP104, WEP40. */
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            /* The set of authentication protocols supported by this configuration. See WifiConfiguration.AuthAlgorithm for descriptions of the values. Defaults to automatic selection. */
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.LEAP);
            /* The set of security protocols supported by this configuration. See WifiConfiguration.Protocol for descriptions of the values. Defaults to WPA RSN. */
            config.allowedProtocols.clear();
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            // Pre-shared key for use with WPA-PSK.
            config.preSharedKey = res.getString(R.string.network_wifi_wpa_Key);
            config.status = WifiConfiguration.Status.ENABLED;
            if (!res.getString(R.string.network_wifi_bssid).equals("auto")) {
                config.BSSID = res.getString(R.string.network_wifi_bssid);
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "A resource was not found. This should never happen");
            return false;
        }
        // Remove all the other networks. (Go Highlander on this thing... THERE
        // CAN BE ONLY ONE)
        List<WifiConfiguration> networks = manager.getConfiguredNetworks();
        for (WifiConfiguration network : networks) {
			/* Remove the specified network from the list of configured networks. This may result in the asynchronous delivery of state change events. Applications are not allowed to remove networks created by other applications.
			Parameters
			netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks(). Returns true if the operation succeeded */
            if (!manager.removeNetwork(network.networkId)) {
                Log.e(TAG, "Woops, can't remove network: " + network.SSID);
            }
        }
        int networkId = manager.addNetwork(config);
        if (networkId == -1) {
            return false;
        }
		/* Allow a previously configured network to be associated with. If attemptConnect is true, an attempt to connect to the selected network is initiated. This may result in the asynchronous delivery of state change events.
		Note: If an application's target SDK version is Build.VERSION_CODES.LOLLIPOP or newer, network communication may not use Wi-Fi even if Wi-Fi is connected; traffic may instead be sent through another network, such as cellular data, Bluetooth tethering, or Ethernet. For example, traffic will never use a Wi-Fi network that does not provide Internet access (e.g. a wireless printer), if another network that does offer Internet access (e.g. cellular data) is available. Applications that need to ensure that their network traffic uses Wi-Fi should use APIs such as Network.bindSocket(java.net.Socket), Network.openConnection(java.net.URL), or ConnectivityManager.bindProcessToNetwork(Network) to do so. Applications are not allowed to enable networks created by other applications.
		Parameters
		netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks().
		attemptConnect 	boolean: The way to select a particular network to connect to is specify true for this parameter.
		Returns true if the operation succeeded */
        manager.enableNetwork(networkId, true);
        /* Technically we are the only network */
        manager.saveConfiguration();
        return true;
    }

    /**
     * Saves a WEP WIFI Configuration Profile.
     *
     * @param ssid     - WIFI network SSID.
     * @param password - WIFI network password.
     */
    public boolean configure_WEP_Config(String ssid, String password) {
        WifiConfiguration wifiConfig = new WifiConfiguration();
        /* The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405). */
        wifiConfig.SSID = "\"" + ssid + "\"";
        wifiConfig.hiddenSSID = true;
        wifiConfig.status = WifiConfiguration.Status.CURRENT;
        wifiConfig.priority = WIFI_CONFIG_PRIORITY;
        /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        /* The set of security protocols supported by this configuration. See WifiConfiguration.Protocol for descriptions of the values. Defaults to WPA RSN. */
        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        /* The set of authentication protocols supported by this configuration. See WifiConfiguration.AuthAlgorithm for descriptions of the values. Defaults to automatic selection. */
        wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
        /* The set of pairwise ciphers for WPA supported by this configuration. See WifiConfiguration.PairwiseCipher for descriptions of the values. Defaults to CCMP, TKIP. */
        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        /* The set of group ciphers supported by this configuration. See WifiConfiguration.GroupCipher for descriptions of the values. Defaults to CCMP, TKIP, WEP104, WEP40. */
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        wifiConfig.wepKeys[WIFI_CONFIG_DEFAULT_INDEX] = "\"" + password + "\"";
        wifiConfig.wepTxKeyIndex = WIFI_CONFIG_DEFAULT_INDEX;
        _wifi_manager.setWifiEnabled(true);
        int result = _wifi_manager.addNetwork(wifiConfig);
        boolean isSaveSuccessful = _wifi_manager.saveConfiguration();
		/* Allow a previously configured network to be associated with. If attemptConnect is true, an attempt to connect to the selected network is initiated. This may result in the asynchronous delivery of state change events.
		Note: If an application's target SDK version is Build.VERSION_CODES.LOLLIPOP or newer, network communication may not use Wi-Fi even if Wi-Fi is connected; traffic may instead be sent through another network, such as cellular data, Bluetooth tethering, or Ethernet. For example, traffic will never use a Wi-Fi network that does not provide Internet access (e.g. a wireless printer), if another network that does offer Internet access (e.g. cellular data) is available. Applications that need to ensure that their network traffic uses Wi-Fi should use APIs such as Network.bindSocket(java.net.Socket), Network.openConnection(java.net.URL), or ConnectivityManager.bindProcessToNetwork(Network) to do so. Applications are not allowed to enable networks created by other applications.
		Parameters
		netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks().
		attemptConnect 	boolean: The way to select a particular network to connect to is specify true for this parameter.
		Returns true if the operation succeeded */
        boolean isNetworkEnabled = _wifi_manager.enableNetwork(result, true);
        if (Constants.DEBUG_MODE_ENABLED) {
            Log.e(TAG, "add Network returned." + result);
            Log.e(TAG, "saveConfiguration returned." + isSaveSuccessful);
            Log.e(TAG, "enableNetwork returned." + isNetworkEnabled);
        }
        return isSaveSuccessful;
    }

    /**
     * Enable/disable wifi
     *
     * @param enabled
     * @return WifiAP state
     */
    private int setWifiApEnabled(boolean enabled) {
        if (enabled && null != _wifi_manager.getConnectionInfo()) {
            _wifi_manager.setWifiEnabled(false);
            try {
                Thread.sleep(1500);
            } catch (Exception e) {
            }
        }
        int state = WIFI_AP_STATE_UNKNOWN;
        try {
            _wifi_manager.setWifiEnabled(false);
            Method method1 = _wifi_manager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            WifiConfiguration netConfig = new WifiConfiguration();
            /* The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405). */
            netConfig.SSID = WIFI_ESSID;
            /* The set of authentication protocols supported by this configuration. See WifiConfiguration.AuthAlgorithm for descriptions of the values. Defaults to automatic selection. */
            netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            /* The set of security protocols supported by this configuration. See WifiConfiguration.Protocol for descriptions of the values. Defaults to WPA RSN. */
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
            netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            /* The set of pairwise ciphers for WPA supported by this configuration. See WifiConfiguration.PairwiseCipher for descriptions of the values. Defaults to CCMP, TKIP. */
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            /* The set of group ciphers supported by this configuration. See WifiConfiguration.GroupCipher for descriptions of the values. Defaults to CCMP, TKIP, WEP104, WEP40. */
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            // Pre-shared key for use with WPA-PSK.
            netConfig.preSharedKey = WIFI_PWD;
            method1.invoke(_wifi_manager, netConfig, enabled);
            Method method2 = _wifi_manager.getClass().getMethod("getWifiApState");
            state = (Integer) method2.invoke(_wifi_manager);
        } catch (Exception e) {
            Log.e("WiFi¼àÌý·þÎñ", e.getMessage());
        }
        if (!enabled) {
            int loopMax = 10;
            while (loopMax > 0 && (getWifiAPState() == WIFI_AP_STATE_DISABLING || getWifiAPState() == WIFI_AP_STATE_ENABLED || getWifiAPState() == WIFI_AP_STATE_FAILED)) {
                try {
                    Thread.sleep(500);
                    loopMax--;
                } catch (Exception e) {
                }
            }
            _wifi_manager.setWifiEnabled(true);
        } else if (enabled) {
            int loopMax = 10;
            while (loopMax > 0 && (getWifiAPState() == WIFI_AP_STATE_ENABLING || getWifiAPState() == WIFI_AP_STATE_DISABLED || getWifiAPState() == WIFI_AP_STATE_FAILED)) {
                try {
                    Thread.sleep(500);
                    loopMax--;
                } catch (Exception e) {
                }
            }
        }
        return state;
    }

    private int getWifiAPState() {
        int state = 1;
        return state;
    }

    protected void sensor_manager() {
        // the sensor manager, providing all sensor services of the device
        Object tmpMan = getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        if (tmpMan == null) {
            Log.e("KIPPEN", "No sensor services detected on device. It does not make sense to start, I rather quit myself.");
            this.finish();
        }
        SensorManager senseMan = (SensorManager) tmpMan;
        // wifi specific services
        tmpMan = getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (tmpMan == null) {
            Log.e("KIPPEN", "No WIFI capabilities detected on device. Would not know how to talk to my master any other way. Too sad ...");
            this.finish();
        }
        WifiManager wifiMan = (WifiManager) tmpMan;
        // connect to host network
        WifiConfiguration wc = new WifiConfiguration();
        /* The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405). */
        wc.SSID = "\"" + WIFI_ESSID + "\"";
        if (WIFI_PWD != null) {
            // Pre-shared key for use with WPA-PSK.
            wc.preSharedKey = "\"" + WIFI_PWD + "\"";
            /* The set of group ciphers supported by this configuration. See WifiConfiguration.GroupCipher for descriptions of the values. Defaults to CCMP, TKIP, WEP104, WEP40. */
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
            wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            /* The set of pairwise ciphers for WPA supported by this configuration. See WifiConfiguration.PairwiseCipher for descriptions of the values. Defaults to CCMP, TKIP. */
            wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            /* The set of security protocols supported by this configuration. See WifiConfiguration.Protocol for descriptions of the values. Defaults to WPA RSN. */
            wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        }
        wc.status = WifiConfiguration.Status.ENABLED;
        // create the client socket for data transmission
//    NetworkingTask.setup(SERVER_IP, 10001, wifiMan, wc);
//    NetworkingTask networkTask = NetworkingTask.getInstance();
//    networkTask.start();
//    // FIXME remove or adjust, since hard-coded
//    // the config which is send to each client, i.e. all wifi's to measure
//    // INACTIVE Set<String> essids = new HashSet<String>();
//    // INACTIVE essids.add(WIFI_ESSID); // measure this wifi ...
//    // INACTIVE essids.add("WirsingRouter5"); // ... measure that wifi ...
//    // INACTIVE ClientConfigData config = new ClientConfigData();
//    // INACTIVE config.setConfig(ConfigType.MEASURE_AP_ESSID, essids);
//    // send a simple ping to the server to notify about our presence
//    networkTask.sendPacket(new PingData());
//    // the battery status measurement
//    batteryReceiver = new BatterySensing();
//    // the accelerometer
//    accSense = senseMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//    // move sensing via orientation and acceleration (TODO)
//    moveSenseMagnetic = senseMan.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//    // general sensor listener
//    sensorListener = new MotionSensing();
//    // the wifi measuring sensor
//    // INACTIVE wifiMan.setWifiEnabled(true);
//    // INACTIVE wifiReceiver = new WifiSensing(wifiMan, config);
//    Log.i("KIPPEN", "Android client activity created.");
    }

    @SuppressLint("NewApi")
    private LinkedList<ScanResult> getWifiInfo() {
        boolean mDebug = false;
        if (mDebug) {
            WifiInfo wifiInfo = _wifi_manager.getConnectionInfo();
            System.out.println(wifiInfo.getSSID() + " " + wifiInfo.getIpAddress() + " " + wifiInfo.getLinkSpeed() + " " + wifiInfo.getMacAddress() + " " + wifiInfo.getNetworkId() + " " + wifiInfo.getRssi() + " " + wifiInfo.getBSSID());
        }
        /* the list of access points found in the most recent scan. An app must hold ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION permission in order to get valid results. If there is a remote exception (e.g., either a communication problem with the system service or an exception within the framework) an empty list will be returned. */
        List<ScanResult> data = _wifi_manager.getScanResults();
        LinkedList<ScanResult> wifis = null;
        if (null != data) {
            wifis = new LinkedList<ScanResult>();
            for (int i = 0; i < data.size(); i++) {
                ScanResult scanResult = data.get(i);
                mDebug = false;
                if (mDebug) {
                    System.out.println("æ— çº¿ç½‘ç»œå??ç§°  = " + scanResult.SSID + " capabilities = " + scanResult.capabilities + " frequency = " + scanResult.frequency + " level = " + scanResult.level + " timestamp = " + scanResult.timestamp + " describeContents = " + scanResult.describeContents() + " BSSID = " + scanResult.BSSID);
                }
                wifis.add(scanResult);
            }
        }
        List<WifiConfiguration> networks = _wifi_manager.getConfiguredNetworks();
        if (null != networks) {
            for (WifiConfiguration configuration : networks) {
                if (mDebug) {
                    System.out.println("configuration = " + configuration);
                }
            }
        }
//    HashMap<String, String> hashMap = getConfigWifiInfos();
//    LinkedList<ScanResult> tmpList = new LinkedList<ScanResult>();
//    for (ScanResult scanResult : wifis) {
//        scanResult.capabilities = "WIFIå¯†ç ?æ²¡æœ‰é…?ç½®è¿‡";
//        for (String key : hashMap.keySet()) {
//            String ssid = scanResult.SSID;
//            mDebug = true;
//            if (mDebug) {
//                System.out.println("scanResult.SSID = " + ssid + " string = " + key + " password = " + hashMap.get(key));
//            }
//            boolean flag = ssid.equals(key);
//            if (flag) {
//                tmpList.add(scanResult);
//                scanResult.capabilities = hashMap.get(key);
//                break;
//            }
//        }
//    }
        //handleWifi(wifis, tmpList);
        return wifis;
    }

    /**
     * Start AccessPoint mode with the specified
     * configuration. If the radio is already running in
     * AP mode, update the new configuration
     * Note that starting in access point mode disables station
     * mode operation
     *
     * @param wifiConfig SSID, security and channel details as part of WifiConfiguration
     * @return {@code true} if the operation succeeds, {@code false} otherwise
     */
    public boolean setWifiApEnabled(WifiConfiguration wifiConfig, boolean enabled) {
        try {
            if (// disable WiFi in any case
                    enabled) {
                _wifi_manager.setWifiEnabled(false);
            }
            Method method = _wifi_manager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            return (Boolean) method.invoke(_wifi_manager, wifiConfig, enabled);
        } catch (Exception e) {
            Log.e(this.getClass().toString(), "", e);
            return false;
        }
    }

    /**
     * Saves a WEP WIFI Configuration Profile.
     *
     * @param ssid     - WIFI network SSID.
     * @param password - WIFI network password.
     */
    public boolean save_WEP_Config(String ssid, String password) {
        WifiConfiguration wifiConfig = new WifiConfiguration();
        /* The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405). */
        wifiConfig.SSID = "\"" + ssid + "\"";
        wifiConfig.hiddenSSID = true;
        wifiConfig.status = WifiConfiguration.Status.DISABLED;
        wifiConfig.priority = WIFI_CONFIG_PRIORITY;
        /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        /* The set of security protocols supported by this configuration. See WifiConfiguration.Protocol for descriptions of the values. Defaults to WPA RSN. */
        /* The set of security protocols supported by this configuration. See WifiConfiguration.Protocol for descriptions of the values. Defaults to WPA RSN. */
        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        /* The set of authentication protocols supported by this configuration. See WifiConfiguration.AuthAlgorithm for descriptions of the values. Defaults to automatic selection. */
        wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
        /* The set of pairwise ciphers for WPA supported by this configuration. See WifiConfiguration.PairwiseCipher for descriptions of the values. Defaults to CCMP, TKIP. */
        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        /* The set of group ciphers supported by this configuration. See WifiConfiguration.GroupCipher for descriptions of the values. Defaults to CCMP, TKIP, WEP104, WEP40. */
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        wifiConfig.wepKeys[WIFI_CONFIG_DEFAULT_INDEX] = "\"" + password + "\"";
        wifiConfig.wepTxKeyIndex = WIFI_CONFIG_DEFAULT_INDEX;
        _wifi_manager.setWifiEnabled(true);
        int result = _wifi_manager.addNetwork(wifiConfig);
        boolean isSaveSuccessful = _wifi_manager.saveConfiguration();
		/* Allow a previously configured network to be associated with. If attemptConnect is true, an attempt to connect to the selected network is initiated. This may result in the asynchronous delivery of state change events.
		Note: If an application's target SDK version is Build.VERSION_CODES.LOLLIPOP or newer, network communication may not use Wi-Fi even if Wi-Fi is connected; traffic may instead be sent through another network, such as cellular data, Bluetooth tethering, or Ethernet. For example, traffic will never use a Wi-Fi network that does not provide Internet access (e.g. a wireless printer), if another network that does offer Internet access (e.g. cellular data) is available. Applications that need to ensure that their network traffic uses Wi-Fi should use APIs such as Network.bindSocket(java.net.Socket), Network.openConnection(java.net.URL), or ConnectivityManager.bindProcessToNetwork(Network) to do so. Applications are not allowed to enable networks created by other applications.
		Parameters
		netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks().
		attemptConnect 	boolean: The way to select a particular network to connect to is specify true for this parameter.
		Returns true if the operation succeeded */
        boolean isNetworkEnabled = _wifi_manager.enableNetwork(result, true);
        if (Constants.DEBUG_MODE_ENABLED) {
            Log.e(TAG, "add Network returned." + result);
            Log.e(TAG, "saveConfiguration returned." + isSaveSuccessful);
            Log.e(TAG, "enableNetwork returned." + isNetworkEnabled);
        }
        return isSaveSuccessful;
    }

    public boolean set_Wifi_Ap_Enabled(boolean enabled) {
        if (Constants.DEBUG_MODE_ENABLED) {
            Log.e(TAG, "setWifiApEnabled(" + enabled + ")");
        }
        if (enabled  // disable WiFi in any case
                ) {
            _wifi_manager.setWifiEnabled(false);
        }
        try {
            // TODO comment from here
        /*
				Method getWifiApConfigurationMethod = _wifi_manager.getClass().getMethod("getWifiApConfiguration");
				Object config = getWifiApConfigurationMethod.invoke(_wifi_manager);
				*/
            // configuration = null works for many devices
            Method setWifiApEnabledMethod = _wifi_manager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            return (Boolean) setWifiApEnabledMethod.invoke(_wifi_manager, null, enabled);
        } catch (Exception e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    /**
     * This method will create the access point.
     */
    public void createWifiAccessPoint(boolean sIshidden) {
        if (_wifi_manager.isWifiEnabled())
            _wifi_manager.setWifiEnabled(false);
        WifiConfiguration netConfig = new WifiConfiguration();
        /* The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405). */
        netConfig.SSID = WIFI_ESSID;
        /* The set of authentication protocols supported by this configuration. See WifiConfiguration.AuthAlgorithm for descriptions of the values. Defaults to automatic selection. */
        netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        netConfig.hiddenSSID = sIshidden;
        if (setWifiApActive(netConfig, true))
            Log.e(TAG, "Access Point created");
        else
            Log.e(TAG, "Access Point creation failed");
        while (!(Boolean) isWifiApActive()) {
        }
    }

    private boolean setWifiApActive(WifiConfiguration netConfig, boolean sIshidden) {
        boolean isWIFI = false;
        return isWIFI;
    }

    private boolean isWifiApActive() {
        boolean isWIFI = false;
        return isWIFI;
    }

    private void updateLockState(Intent i) {
        String clase = getClass().getPackage().getName() + ".UnlockAtHome";
        SharedPreferences settings = getSharedPreferences(clase, Context.MODE_PRIVATE);
        Map<String, ?> settingsMap = settings.getAll();
        if (settingsMap.size() < 1) {
            // Require at least one configured net
            return;
        }
        NetworkInfo info = (NetworkInfo) i.getParcelableExtra(android.net.ConnectivityManager.EXTRA_NETWORK_INFO);
        if (info != null && info.getType() == android.net.ConnectivityManager.TYPE_WIFI) {
            WifiInfo wifi_info = _wifi_manager.getConnectionInfo();
            String Current_SSID = wifi_info.getSSID() + " [" + wifi_info.getNetworkId() + "]";
            if (info.isConnected()) {
                List<WifiConfiguration> networks = _wifi_manager.getConfiguredNetworks();
                for (WifiConfiguration network : networks) {
                    /* The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405). */
                    String SSID = network.SSID;
                    SSID = SSID.replaceAll("^\"", "");
                    SSID = SSID.replaceAll("\"$", "");
                    SSID = SSID + " [" + network.networkId + "]";
                    if (SSID.equals(Current_SSID)) {
                        for (String key : settingsMap.keySet()) {
                            if (SSID.equals(key) && settings.getBoolean(key, false)) {
                                // Log.e(TAG, "S '"+SSID+"' K '"+key+"'");
                                //setLockPattern(false);
                                return;
                            }
                        }
                    }
                }
            }
        }
        //setLockPattern(true);
    }

    public void configure_wifi(String ssid, String password, String type) {
        final ProgressDialog pd;
        pd = ProgressDialog.show(getApplicationContext(), "Configuring network", "Fetching values...", false);
        WifiConfiguration wfc = new WifiConfiguration();
        /* The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405). */
        wfc.SSID = "\"".concat(ssid).concat("\"");
        wfc.status = WifiConfiguration.Status.DISABLED;
        wfc.priority = 40;
        if (type.equalsIgnoreCase("open")) {
            /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
            wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            /* The set of security protocols supported by this configuration. See WifiConfiguration.Protocol for descriptions of the values. Defaults to WPA RSN. */
            wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wfc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            /* The set of authentication protocols supported by this configuration. See WifiConfiguration.AuthAlgorithm for descriptions of the values. Defaults to automatic selection. */
            /* The set of authentication protocols supported by this configuration. See WifiConfiguration.AuthAlgorithm for descriptions of the values. Defaults to automatic selection. */
            wfc.allowedAuthAlgorithms.clear();
            /* The set of pairwise ciphers for WPA supported by this configuration. See WifiConfiguration.PairwiseCipher for descriptions of the values. Defaults to CCMP, TKIP. */
            wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            /* The set of group ciphers supported by this configuration. See WifiConfiguration.GroupCipher for descriptions of the values. Defaults to CCMP, TKIP, WEP104, WEP40. */
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        } else if (type.equalsIgnoreCase("WEP")) {
            /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
            wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            /* The set of security protocols supported by this configuration. See WifiConfiguration.Protocol for descriptions of the values. Defaults to WPA RSN. */
            wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wfc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            /* The set of authentication protocols supported by this configuration. See WifiConfiguration.AuthAlgorithm for descriptions of the values. Defaults to automatic selection. */
            wfc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            wfc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            /* The set of pairwise ciphers for WPA supported by this configuration. See WifiConfiguration.PairwiseCipher for descriptions of the values. Defaults to CCMP, TKIP. */
            wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            /* The set of group ciphers supported by this configuration. See WifiConfiguration.GroupCipher for descriptions of the values. Defaults to CCMP, TKIP, WEP104, WEP40. */
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            // if (StringUtils.sHex(password))
            // wfc.wepKeys[0] = password;
            // else
            wfc.wepKeys[0] = "\"".concat(password).concat("\"");
            wfc.wepTxKeyIndex = 0;
        } else if (type.equalsIgnoreCase("WPA/WPA2")) {
            /* The set of security protocols supported by this configuration. See WifiConfiguration.Protocol for descriptions of the values. Defaults to WPA RSN. */
            wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wfc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
            wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            /* The set of pairwise ciphers for WPA supported by this configuration. See WifiConfiguration.PairwiseCipher for descriptions of the values. Defaults to CCMP, TKIP. */
            wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            /* The set of group ciphers supported by this configuration. See WifiConfiguration.GroupCipher for descriptions of the values. Defaults to CCMP, TKIP, WEP104, WEP40. */
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            // Pre-shared key for use with WPA-PSK.
            wfc.preSharedKey = "\"".concat(password).concat("\"");
        } else {
            // dont know what to do. Exit?
        }
        // Enable wifi first
        if (!_wifi_manager.isWifiEnabled())
            _wifi_manager.setWifiEnabled(true);
        WifiConfiguration found = findNetworkInExistingConfig(wfc.SSID);
        /* Disassociate from the currently active access point. This may result in the asynchronous delivery of state change events.Returns true if the operation succeeded. */
        _wifi_manager.disconnect();
        if (found == null) {
        } else {
            Log.e(TAG, "Removing network " + found.networkId);
			/* Remove the specified network from the list of configured networks. This may result in the asynchronous delivery of state change events. Applications are not allowed to remove networks created by other applications.
			Parameters
			netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks(). Returns true if the operation succeeded */
            _wifi_manager.removeNetwork(found.networkId);
            _wifi_manager.saveConfiguration();
        }
        pd.show(getApplicationContext(), "Configuring network", "Adding the newly configured network...", false);
        int networkId = _wifi_manager.addNetwork(wfc);
        if (networkId != -1) {
            /* Allow a previously configured network to be associated with. If attemptConnect is true, an attempt to connect to the selected network is initiated. This may result in the asynchronous delivery of state change events.
		Note: If an application's target SDK version is Build.VERSION_CODES.LOLLIPOP or newer, network communication may not use Wi-Fi even if Wi-Fi is connected; traffic may instead be sent through another network, such as cellular data, Bluetooth tethering, or Ethernet. For example, traffic will never use a Wi-Fi network that does not provide Internet access (e.g. a wireless printer), if another network that does offer Internet access (e.g. cellular data) is available. Applications that need to ensure that their network traffic uses Wi-Fi should use APIs such as Network.bindSocket(java.net.Socket), Network.openConnection(java.net.URL), or ConnectivityManager.bindProcessToNetwork(Network) to do so. Applications are not allowed to enable networks created by other applications.
		Parameters
		netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks().
		attemptConnect 	boolean: The way to select a particular network to connect to is specify true for this parameter.
		Returns true if the operation succeeded */
            _wifi_manager.enableNetwork(networkId, false);
            /* Reconnect to the currently active access point, even if we are already connected. This may result in the asynchronous delivery of state change events. Returns true if the operation succeeded. */
            _wifi_manager.reassociate();
            final Handler handler = new Handler();
            Timer t = new Timer();
            t.schedule(new TimerTask() {

                           @Override
                           public void run() {
                               handler.post(new Runnable() {

                                   public void run() {
                                       pd.dismiss();
                                       new AlertDialog.Builder(getApplicationContext()).setTitle("New network has been configured! Do you want to exit?").setPositiveButton("Exit", new DialogInterface.OnClickListener() {

                                           public void onClick(DialogInterface dialog, int whichButton) {
                                               System.exit(0);
                                           }
                                       }).setCancelable(true).create().show();
                                   }
                               });
                           }
                       }, // 3 seconds wait to configure
                    3 * 1000L);
        }
    }

    private WifiConfiguration findNetworkInExistingConfig(String ssid) {
        Iterator localIterator = _wifi_manager.getConfiguredNetworks().iterator();
        WifiConfiguration localWifiConfiguration;
        do {
            if (!localIterator.hasNext())
                return null;
            localWifiConfiguration = (WifiConfiguration) localIterator.next();
        } while (!localWifiConfiguration.SSID.equals("\"" + ssid + "\""));
        return localWifiConfiguration;
    }

    /**
     * Associate the device to given SSID
     * If the device is already associated with a WiFi, disconnect and forget it,
     * We don't verify whether the connection is successful or not, leave this to the test
     */
    public boolean connect_To_Wifi(String knownSSID) {
        //If Wifi is not enabled, enable it
        if (!_wifi_manager.isWifiEnabled()) {
            Log.e(TAG, "Wifi is not enabled, enable it");
            _wifi_manager.setWifiEnabled(true);
        }
        /* the list of access points found in the most recent scan. An app must hold ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION permission in order to get valid results. If there is a remote exception (e.g., either a communication problem with the system service or an exception within the framework) an empty list will be returned. */
        List<ScanResult> netList = _wifi_manager.getScanResults();
        if (netList == null) {
            // if no scan results are available, start active scan
            _wifi_manager.startScan();
            boolean mScanResultIsAvailable = false;
            long startTime = System.currentTimeMillis();
            while (!mScanResultIsAvailable) {
                if ((System.currentTimeMillis() - startTime) > WIFI_SCAN_TIMEOUT) {
                    return false;
                }
                // wait for the scan results to be available
                synchronized (this) {
                    // wait for the scan result to be available
                    try {
                        this.wait(WAIT_FOR_SCAN_RESULT);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if ((_wifi_manager.getScanResults() == null) || (_wifi_manager.getScanResults().size() <= 0)) {
                        continue;
                    }
                    mScanResultIsAvailable = true;
                }
            }
        }
        netList = _wifi_manager.getScanResults();
        for (int i = 0; i < netList.size(); i++) {
            ScanResult sr = netList.get(i);
            if (sr.SSID.equals(knownSSID)) {
                Log.e(TAG, "found " + knownSSID + " in the scan result list");
                WifiConfiguration config = new WifiConfiguration();
                /* The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405). */
                config.SSID = convertToQuotedString(sr.SSID);
                /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                int networkId = _wifi_manager.addNetwork(config);
                // Connect to network by disabling others.
				/* Allow a previously configured network to be associated with. If attemptConnect is true, an attempt to connect to the selected network is initiated. This may result in the asynchronous delivery of state change events.
		Note: If an application's target SDK version is Build.VERSION_CODES.LOLLIPOP or newer, network communication may not use Wi-Fi even if Wi-Fi is connected; traffic may instead be sent through another network, such as cellular data, Bluetooth tethering, or Ethernet. For example, traffic will never use a Wi-Fi network that does not provide Internet access (e.g. a wireless printer), if another network that does offer Internet access (e.g. cellular data) is available. Applications that need to ensure that their network traffic uses Wi-Fi should use APIs such as Network.bindSocket(java.net.Socket), Network.openConnection(java.net.URL), or ConnectivityManager.bindProcessToNetwork(Network) to do so. Applications are not allowed to enable networks created by other applications.
		Parameters
		netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks().
		attemptConnect 	boolean: The way to select a particular network to connect to is specify true for this parameter.
		Returns true if the operation succeeded */
                _wifi_manager.enableNetwork(networkId, true);
                _wifi_manager.saveConfiguration();
                _wifi_manager.reconnect();
                break;
            }
        }
        List<WifiConfiguration> netConfList = _wifi_manager.getConfiguredNetworks();
        if (netConfList.size() <= 0) {
            Log.e(TAG, knownSSID + " is not available");
            return false;
        }
        return true;
    }

    private String convertToQuotedString(String ssid) {
        return "\"".concat(ssid).concat("\"");
    }

    private void setWifiAccessPointEnabled(boolean enabled) {
        WifiConfiguration configuration;
        try {
            Method method = WifiManager.class.getMethod("getWifiApConfiguration");
            configuration = (WifiConfiguration) method.invoke(_wifi_manager);
            Method method1 = _wifi_manager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            // true
            method1.invoke(_wifi_manager, configuration, enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WifiConfiguration CreateConfiguration(String ssid, String password, int type) {
        WifiConfiguration configuration = new WifiConfiguration();
        /* The set of authentication protocols supported by this configuration. See WifiConfiguration.AuthAlgorithm for descriptions of the values. Defaults to automatic selection. */
        configuration.allowedAuthAlgorithms.clear();
        /* The set of group ciphers supported by this configuration. See WifiConfiguration.GroupCipher for descriptions of the values. Defaults to CCMP, TKIP, WEP104, WEP40. */
        configuration.allowedGroupCiphers.clear();
        /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
        configuration.allowedKeyManagement.clear();
        /* The set of pairwise ciphers for WPA supported by this configuration. See WifiConfiguration.PairwiseCipher for descriptions of the values. Defaults to CCMP, TKIP. */
        configuration.allowedPairwiseCiphers.clear();
        /* The set of security protocols supported by this configuration. See WifiConfiguration.Protocol for descriptions of the values. Defaults to WPA RSN. */
        configuration.allowedProtocols.clear();
        /* The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405). */
        configuration.SSID = "\"" + ssid + "\"";
        WifiConfiguration tempConfiguration = IsExists(ssid, _wifi_manager);
        if (tempConfiguration != null) {
			/* Remove the specified network from the list of configured networks. This may result in the asynchronous delivery of state change events. Applications are not allowed to remove networks created by other applications.
			Parameters
			netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks(). Returns true if the operation succeeded */
            _wifi_manager.removeNetwork(tempConfiguration.networkId);
        }
        // WIFICIPHER_NOPASS
        if (type == 1) {
            configuration.wepKeys[0] = "";
            /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            configuration.wepTxKeyIndex = 0;
        }
        // WIFICIPHER_WEP
        if (type == 2) {
            configuration.hiddenSSID = true;
            configuration.wepKeys[0] = "\"" + password + "\"";
            /* The set of authentication protocols supported by this configuration. See WifiConfiguration.AuthAlgorithm for descriptions of the values. Defaults to automatic selection. */
            configuration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            /* The set of group ciphers supported by this configuration. See WifiConfiguration.GroupCipher for descriptions of the values. Defaults to CCMP, TKIP, WEP104, WEP40. */
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            configuration.wepTxKeyIndex = 0;
        }
        // WIFICIPHER_WPA
        if (type == 3) {
            // Pre-shared key for use with WPA-PSK.
            configuration.preSharedKey = "\"" + password + "\"";
            configuration.hiddenSSID = true;
            /* The set of authentication protocols supported by this configuration. See WifiConfiguration.AuthAlgorithm for descriptions of the values. Defaults to automatic selection. */
            configuration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            /* The set of pairwise ciphers for WPA supported by this configuration. See WifiConfiguration.PairwiseCipher for descriptions of the values. Defaults to CCMP, TKIP. */
            configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            /* The set of group ciphers supported by this configuration. See WifiConfiguration.GroupCipher for descriptions of the values. Defaults to CCMP, TKIP, WEP104, WEP40. */
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            configuration.status = WifiConfiguration.Status.ENABLED;
        }
        return configuration;
    }

    private WifiConfiguration IsExists(String ssid, WifiManager wifi_manager) {
        Iterator localIterator = wifi_manager.getConfiguredNetworks().iterator();
        WifiConfiguration localWifiConfiguration;
        do {
            if (!localIterator.hasNext())
                return null;
            localWifiConfiguration = (WifiConfiguration) localIterator.next();
        } while (!localWifiConfiguration.SSID.equals("\"" + ssid + "\""));
        return localWifiConfiguration;
    }

    public void garage_door_opener(String ssid) {
        Settings.System.putInt(getContentResolver(), Settings.System.WIFI_USE_STATIC_IP, 0);

        List<WifiConfiguration> networks = _wifi_manager.getConfiguredNetworks();
        WifiConfiguration foundConfig = null;
        for (WifiConfiguration config : networks) {
            if (config.SSID.equals("\"" + ssid + "\"")) {
                foundConfig = config;
                break;
            }
            _string_builder.append(config.SSID + ", " + new Integer(config.SSID.length()));
        }
        if (foundConfig != null) {
            _string_builder.append(foundConfig.networkId);
            int n = 0;
            try {
				/* Allow a previously configured network to be associated with. If attemptConnect is true, an attempt to connect to the selected network is initiated. This may result in the asynchronous delivery of state change events.
		Note: If an application's target SDK version is Build.VERSION_CODES.LOLLIPOP or newer, network communication may not use Wi-Fi even if Wi-Fi is connected; traffic may instead be sent through another network, such as cellular data, Bluetooth tethering, or Ethernet. For example, traffic will never use a Wi-Fi network that does not provide Internet access (e.g. a wireless printer), if another network that does offer Internet access (e.g. cellular data) is available. Applications that need to ensure that their network traffic uses Wi-Fi should use APIs such as Network.bindSocket(java.net.Socket), Network.openConnection(java.net.URL), or ConnectivityManager.bindProcessToNetwork(Network) to do so. Applications are not allowed to enable networks created by other applications.
		Parameters
		netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks().
		attemptConnect 	boolean: The way to select a particular network to connect to is specify true for this parameter.
		Returns true if the operation succeeded */
                boolean success = _wifi_manager.enableNetwork(foundConfig.networkId, true);
                _string_builder.append(new Boolean(success).toString());
                if (success) {
                    _string_builder.append("Connecting to: " + ssid);
                }
            } catch (Exception e) {
                _string_builder.append(e.toString() + ", " + n);
            }
        } else {
            _string_builder.append(ssid + " not found");
        }
    }

    public void cleanNetworks() {
        WifiInfo connectionInfo = _wifi_manager.getConnectionInfo();
        List<WifiConfiguration> configuredNetworks = _wifi_manager.getConfiguredNetworks();
        for (WifiConfiguration wifiConfiguration : configuredNetworks) {
            if (shouldBeCleaned(wifiConfiguration, connectionInfo)) {
				/* Remove the specified network from the list of configured networks. This may result in the asynchronous delivery of state change events. Applications are not allowed to remove networks created by other applications.
				Parameters
				netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks(). Returns true if the operation succeeded */
                boolean removeNetwork = _wifi_manager.removeNetwork(wifiConfiguration.networkId);
                Log.e(TAG, "Removed network " + wifiConfiguration.SSID + ":" + wifiConfiguration.BSSID + "->" + removeNetwork);
            }
        }
    }

    private boolean shouldBeCleaned(WifiConfiguration wifiConfiguration, WifiInfo connectionInfo) {
        return false;
    }

    /**
     * Update the network: either create a new network or modify an existing network
     *
     * @param config the new network configuration
     */
    private static void updateNetwork(WifiManager wifiManager, WifiConfiguration config) {
        Integer foundNetworkID = findNetworkInExistingConfig(wifiManager, config.SSID);
        if (foundNetworkID != null) {
            Log.i(TAG, "Removing old configuration for network " + config.SSID);
			/* Remove the specified network from the list of configured networks. This may result in the asynchronous delivery of state change events. Applications are not allowed to remove networks created by other applications.
			Parameters
			netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks(). Returns true if the operation succeeded */
            wifiManager.removeNetwork(foundNetworkID);
            wifiManager.saveConfiguration();
        }
        int networkId = wifiManager.addNetwork(config);
        if (networkId >= 0) {
            // Try to disable the current network and start a new one.
			/* Allow a previously configured network to be associated with. If attemptConnect is true, an attempt to connect to the selected network is initiated. This may result in the asynchronous delivery of state change events.
		Note: If an application's target SDK version is Build.VERSION_CODES.LOLLIPOP or newer, network communication may not use Wi-Fi even if Wi-Fi is connected; traffic may instead be sent through another network, such as cellular data, Bluetooth tethering, or Ethernet. For example, traffic will never use a Wi-Fi network that does not provide Internet access (e.g. a wireless printer), if another network that does offer Internet access (e.g. cellular data) is available. Applications that need to ensure that their network traffic uses Wi-Fi should use APIs such as Network.bindSocket(java.net.Socket), Network.openConnection(java.net.URL), or ConnectivityManager.bindProcessToNetwork(Network) to do so. Applications are not allowed to enable networks created by other applications.
		Parameters
		netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks().
		attemptConnect 	boolean: The way to select a particular network to connect to is specify true for this parameter.
		Returns true if the operation succeeded */
            if (wifiManager.enableNetwork(networkId, true)) {
                Log.i(TAG, "Associating to network " + config.SSID);
                wifiManager.saveConfiguration();
            } else {
                Log.e(TAG, "Failed to enable network " + config.SSID);
            }
        } else {
            Log.e(TAG, "Unable to add network " + config.SSID);
        }
    }

    private static Integer findNetworkInExistingConfig(WifiManager wifiManager, String ssid) {
        Iterable<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals(ssid)) {
                return existingConfig.networkId;
            }
        }
        return null;
    }

    void loadAccesspointConfiguration() {
        DataInputStream in = null;
        try {
            WifiConfiguration config = new WifiConfiguration();
            in = new DataInputStream(new BufferedInputStream(new FileInputStream(AP_CONFIG_FILE)));
            int version = in.readInt();
            if (version != 1) {
                Log.e(TAG, "Bad version on hotspot configuration file, set defaults");
                setDefaultApConfiguration();
                return;
            }
            config.SSID = in.readUTF();
            int authType = in.readInt();
            config.allowedKeyManagement.set(authType);
            if (authType != WifiConfiguration.KeyMgmt.NONE) {
                // Pre-shared key for use with WPA-PSK.
                config.preSharedKey = in.readUTF();
            }
            //mWifiApConfig = config;
        } catch (IOException ignore) {
            setDefaultApConfiguration();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private void setDefaultApConfiguration() {

        final Bundle result = new Bundle();
        try {
            final String method = expectString("method");
            if (_wifi_manager == null) {
                Log.e(TAG, "Couldn't get WifiManager reference; goodbye!");
                return;
            }
            // argument is missing in the middle of an implementation.
            if ("enableWifi".equals(method)) {
                result.putBoolean("result", _wifi_manager.setWifiEnabled(true));
            } else if ("disableWifi".equals(method)) {
                result.putBoolean("result", _wifi_manager.setWifiEnabled(false));
            } else if ("addOpenNetwork".equals(method)) {
                final String ssid = expectString("ssid");
                final WifiConfiguration config = new WifiConfiguration();
                // A string SSID _must_ be enclosed in double-quotation marks
                /* The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405). */
                config.SSID = convertToQuotedString(ssid);
                // KeyMgmt should be NONE only
                final BitSet keymgmt = new BitSet();
                keymgmt.set(WifiConfiguration.KeyMgmt.NONE);
                /* The set of key management protocols supported by this configuration. See WifiConfiguration.KeyMgmt for descriptions of the values. Defaults to WPA-PSK, WPA-EAP. */
                config.allowedKeyManagement = keymgmt;
                result.putInt("result", _wifi_manager.addNetwork(config));
            } else if ("addWpaPskNetwork".equals(method)) {
                final String ssid = expectString("ssid");
                final String psk = expectString("psk");
                final WifiConfiguration config = new WifiConfiguration();
                // A string SSID _must_ be enclosed in double-quotation marks
                config.SSID = convertToQuotedString(ssid);
                // Likewise for the psk
                // Pre-shared key for use with WPA-PSK.
                config.preSharedKey = convertToQuotedString(psk);
                result.putInt("result", _wifi_manager.addNetwork(config));
            } else if ("associateNetwork".equals(method)) {
                final int id = expectInteger("id");
                result.putBoolean("result", _wifi_manager.enableNetwork(id, true));
            } else if ("disconnect".equals(method)) {
                result.putBoolean("result", _wifi_manager.disconnect());
            } else if ("disableNetwork".equals(method)) {
                final int id = expectInteger("id");
                result.putBoolean("result", _wifi_manager.disableNetwork(id));
            } else if ("isWifiEnabled".equals(method)) {
                result.putBoolean("result", _wifi_manager.isWifiEnabled());
            } else if ("getIpAddress".equals(method)) {
                final WifiInfo info = _wifi_manager.getConnectionInfo();
                final int addr = info.getIpAddress();
                // IP address is stored with the first octet in the lowest byte
                final int a = (addr >> 0) & 0xff;
                final int b = (addr >> 8) & 0xff;
                final int c = (addr >> 16) & 0xff;
                final int d = (addr >> 24) & 0xff;
                result.putString("result", String.format("%s.%s.%s.%s", a, b, c, d));
            } else if ("getSSID".equals(method)) {
                final WifiInfo info = _wifi_manager.getConnectionInfo();
                result.putString("result", info.getSSID());
            } else if ("removeAllNetworks".equals(method)) {
                boolean success = true;
                List<WifiConfiguration> netlist = _wifi_manager.getConfiguredNetworks();
                if (netlist == null) {
                    success = false;
                } else {
                    for (WifiConfiguration config : netlist) {
						/* Remove the specified network from the list of configured networks. This may result in the asynchronous delivery of state change events. Applications are not allowed to remove networks created by other applications.
						Parameters
						netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks(). Returns true if the operation succeeded */
                        success &= _wifi_manager.removeNetwork(config.networkId);
                    }
                }
                result.putBoolean("result", success);
            } else if ("removeNetwork".equals(method)) {
                final int id = expectInteger("id");
                result.putBoolean("result", _wifi_manager.removeNetwork(id));
            } else if ("saveConfiguration".equals(method)) {
                result.putBoolean("result", _wifi_manager.saveConfiguration());
            } else if ("getSupplicantState".equals(method)) {
                String state = _wifi_manager.getConnectionInfo().getSupplicantState().name();
                result.putString("result", state);
            } else {
                Log.e(TAG, String.format("Didn't recognize method '%s'", method));
                return;
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
            return;
        }
    }

    private int expectInteger(String id) {
        int _id = 0;
        return _id;
    }

    private String expectString(String id) {
        String _id = "";
        return _id;
    }

    public void installConferenceWiFi(final Context context) {
        // Create config
        WifiConfiguration config = new WifiConfiguration();
        // Must be in double quotes to tell system this is an ASCII SSID and passphrase.
        /* The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405). */
        config.SSID = String.format("\"%s\"", Constants.WIFI_SSID);
        // Pre-shared key for use with WPA-PSK.
        config.preSharedKey = String.format("\"%s\"", Constants.WIFI_PASSPHRASE);
        // Store config
        final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int netId = wifiManager.addNetwork(config);
        if (netId != -1) {
			/* Allow a previously configured network to be associated with. If attemptConnect is true, an attempt to connect to the selected network is initiated. This may result in the asynchronous delivery of state change events.
		Note: If an application's target SDK version is Build.VERSION_CODES.LOLLIPOP or newer, network communication may not use Wi-Fi even if Wi-Fi is connected; traffic may instead be sent through another network, such as cellular data, Bluetooth tethering, or Ethernet. For example, traffic will never use a Wi-Fi network that does not provide Internet access (e.g. a wireless printer), if another network that does offer Internet access (e.g. cellular data) is available. Applications that need to ensure that their network traffic uses Wi-Fi should use APIs such as Network.bindSocket(java.net.Socket), Network.openConnection(java.net.URL), or ConnectivityManager.bindProcessToNetwork(Network) to do so. Applications are not allowed to enable networks created by other applications.
		Parameters
		netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks().
		attemptConnect 	boolean: The way to select a particular network to connect to is specify true for this parameter.
		Returns true if the operation succeeded */
            wifiManager.enableNetwork(netId, false);
            boolean result = wifiManager.saveConfiguration();
            if (!result) {
                Log.e(TAG, "Unknown error while calling WiFiManager.saveConfiguration()");
                utilz.getInstance(getApplicationContext()).globalloghandler(getResources().getString(R.string.wifi_install_error_message), TAG, 1, 0);
            }
        } else {
            Log.e(TAG, "Unknown error while calling WiFiManager.addNetwork()");
            utilz.getInstance(getApplicationContext()).globalloghandler(getResources().getString(R.string.wifi_install_error_message), TAG, 1, 0);
        }
    }

    private boolean connectWifi2(final String id) {
        String ssid = '"' + id + '"';
        List<WifiConfiguration> wifiConfigurations = _wifi_manager.getConfiguredNetworks();
        for (WifiConfiguration configuration : wifiConfigurations) {
            if (configuration.SSID.contains(ssid)) {
                return connectWifi(configuration.networkId, configuration.SSID);
            }
        }
        return false;
    }

    boolean connectWifi(int networkId, String ssid) {
        return true;
    }

    public boolean Connect(String SSID, String Password, int Type) {
        if (!this.openWifi()) {
            return false;
        }
        // çŠ¶æ€?å?˜æˆ?WIFI_STATE_ENABLEDçš„æ—¶å€™æ‰?èƒ½æ‰§è¡Œä¸‹é?¢çš„è¯­å?¥
        while (_wifi_manager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            try {
                // ä¸ºäº†é?¿å…?ç¨‹åº?ä¸€ç›´whileå¾ªçŽ¯ï¼Œè®©å®ƒç?¡ä¸ª100æ¯«ç§’åœ¨æ£€æµ‹â€¦â€¦
                Thread.currentThread();
                Thread.sleep(100);
            } catch (InterruptedException ie) {

            }
        }
        WifiConfiguration wifiConfig = this.CreateConfiguration(SSID, Password, //wifi Configuration
                Type);
        if (wifiConfig == null) {
            return false;
        }
        WifiConfiguration tempConfig = this.IsExists(SSID, _wifi_manager);
        if (tempConfig != null) {
			/* Remove the specified network from the list of configured networks. This may result in the asynchronous delivery of state change events. Applications are not allowed to remove networks created by other applications.
			Parameters
			netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks(). Returns true if the operation succeeded */
            _wifi_manager.removeNetwork(tempConfig.networkId);
        }
        int netID = _wifi_manager.addNetwork(wifiConfig);
		/* Allow a previously configured network to be associated with. If attemptConnect is true, an attempt to connect to the selected network is initiated. This may result in the asynchronous delivery of state change events.
		Note: If an application's target SDK version is Build.VERSION_CODES.LOLLIPOP or newer, network communication may not use Wi-Fi even if Wi-Fi is connected; traffic may instead be sent through another network, such as cellular data, Bluetooth tethering, or Ethernet. For example, traffic will never use a Wi-Fi network that does not provide Internet access (e.g. a wireless printer), if another network that does offer Internet access (e.g. cellular data) is available. Applications that need to ensure that their network traffic uses Wi-Fi should use APIs such as Network.bindSocket(java.net.Socket), Network.openConnection(java.net.URL), or ConnectivityManager.bindProcessToNetwork(Network) to do so. Applications are not allowed to enable networks created by other applications.
		Parameters
		netId 	int: the ID of the network as returned by addNetwork(WifiConfiguration) or getConfiguredNetworks().
		attemptConnect 	boolean: The way to select a particular network to connect to is specify true for this parameter.
		Returns true if the operation succeeded */
        boolean bRet = _wifi_manager.enableNetwork(netID, false);
        return bRet;
    }

    boolean openWifi() {
        boolean bRet = true;
        return bRet;
    }

    void open_portable_hotspot() {

        if (_wifi_manager.isWifiEnabled()) {
            _wifi_manager.setWifiEnabled(false);
        }
        Method[] wmMethods = _wifi_manager.getClass().getDeclaredMethods();   //Get all declared methods in WifiManager class
        boolean methodFound = false;
        for (Method method : wmMethods) {
            if (method.getName().equals("setWifiApEnabled")) {
                methodFound = true;
                WifiConfiguration netConfig = new WifiConfiguration();
                /* The network's SSID. Can either be a UTF-8 string, which must be enclosed in double quotation marks (e.g., "MyNetwork"), or a string of hex digits, which are not enclosed in quotes (e.g., 01a243f405). */
                netConfig.SSID = "\"" + "TinyBox" + "\"";
                /* The set of authentication protocols supported by this configuration. See WifiConfiguration.AuthAlgorithm for descriptions of the values. Defaults to automatic selection. */
                netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);

                try {
                    boolean apstatus = (Boolean) method.invoke(_wifi_manager, netConfig, true);
                    for (Method isWifiApEnabledmethod : wmMethods) {
                        if (isWifiApEnabledmethod.getName().equals("isWifiApEnabled")) {
                            while (!(Boolean) isWifiApEnabledmethod.invoke(_wifi_manager)) {
                            }
                            ;
                            for (Method method1 : wmMethods) {
                                if (method1.getName().equals("getWifiApState")) {
                                    int apstate;
                                    apstate = (Integer) method1.invoke(_wifi_manager);
                                }
                            }
                        }
                    }
                    if (apstatus) {
                        System.out.println("SUCCESS");

                    } else {
                        System.out.println("FAILED");

                    }

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public WifiConfiguration getWifiConfiguration(final WifiManager wifiMgr, final ScanResult scanResult) {
        if (scanResult.BSSID == null || scanResult.SSID == null || scanResult.SSID.isEmpty() || scanResult.BSSID.isEmpty())
            return null;
        final String ssid = convertToQuotedString(scanResult.SSID);
        final String bssid = scanResult.BSSID;
        final String security = get_Security_given_scanResult(scanResult);

        final List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();
        if (configurations == null)
            return null;

        for (final WifiConfiguration config : configurations) {
            if (bssid.equals(config.BSSID) || ssid.equals(config.SSID)) {
                final String configSecurity = get_Security_given_config(config);
                if (Objects.equals(security, configSecurity))
                    return config;
            }
        }
        return null;
    }

    public static void removeSSIDFromConfiguredNetwork(Context context, String ssid) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled())
            wifiManager.setWifiEnabled(true);

        List<WifiConfiguration> configuredWifiList = wifiManager.getConfiguredNetworks();
        for (int x = 0; x < configuredWifiList.size(); x++) {
            WifiConfiguration i = configuredWifiList.get(x);
            if (i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
                Log.e(TAG, "Removing network: " + i.SSID);
                wifiManager.removeNetwork(i.networkId);
                return;
            }
        }
    }

    public List<WifiConfiguration> configuredWifiList() {

        if (!_wifi_manager.isWifiEnabled())
            _wifi_manager.setWifiEnabled(true);

        List<WifiConfiguration> configuredWifiList = _wifi_manager.getConfiguredNetworks();
        for (int x = 0; x < configuredWifiList.size(); x++) {
            WifiConfiguration i = configuredWifiList.get(x);
            if (i.SSID != null) {
                Log.e(TAG, " network: " + i.SSID);
            }
        }
        return configuredWifiList;
    }

    WifiConfiguration getWifiConfiguration(@NonNull final WifiManager wifiMgr, @NonNull final WifiConfiguration configToFind) {
        final String ssid = configToFind.SSID;
        if (ssid.isEmpty()) {
            return null;
        }

        final String bssid = configToFind.BSSID;

        final String security = get_Security_given_config(configToFind);


        final List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();
        if (configurations == null) {
            Log.e(TAG, "NULL configs");
            return null;
        }

        for (final WifiConfiguration config : configurations) {
            if (bssid.equals(config.BSSID) || ssid.equals(config.SSID)) {
                final String configSecurity = get_Security_given_config(config);
                if (Objects.equals(security, configSecurity))
                    return config;
            }
        }
        Log.e(TAG, "Couldn't find " + ssid);
        return null;
    }

    private static int getMaxPriority(@NonNull final WifiManager wifiManager) {
        final List<WifiConfiguration> configurations = wifiManager.getConfiguredNetworks();
        int pri = 0;
        for (final WifiConfiguration config : configurations) {
            if (config.priority > pri) {
                pri = config.priority;
            }
        }
        return pri;
    }

    private static int shiftPriorityAndSave(@NonNull final WifiManager wifiMgr) {
        final List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();
        sortByPriority(configurations);
        final int size = configurations.size();
        for (int i = 0; i < size; i++) {
            final WifiConfiguration config = configurations.get(i);
            config.priority = i;
            wifiMgr.updateNetwork(config);
        }
        wifiMgr.saveConfiguration();
        return size;
    }

    private boolean disableAllButOne(@NonNull final WifiManager wifiManager, @Nullable final WifiConfiguration config) {
        @Nullable final List<WifiConfiguration> configurations = wifiManager.getConfiguredNetworks();
        if (configurations == null || config == null || configurations.isEmpty())
            return false;
        boolean result = false;

        for (WifiConfiguration wifiConfig : configurations)
            if (wifiConfig.networkId == config.networkId)
                result = wifiManager.enableNetwork(wifiConfig.networkId, true);
            else
                wifiManager.disableNetwork(wifiConfig.networkId);
        Log.e(TAG, "disableAllButOne " + result);
        return result;
    }

    @SuppressWarnings("UnusedReturnValue")
    private boolean disableAllButOne(@NonNull final WifiManager wifiManager, @Nullable final ScanResult scanResult) {
        @Nullable final List<WifiConfiguration> configurations = wifiManager.getConfiguredNetworks();
        if (configurations == null || scanResult == null || configurations.isEmpty())
            return false;
        boolean result = false;
        for (WifiConfiguration wifiConfig : configurations)
            if (Objects.equals(scanResult.BSSID, wifiConfig.BSSID) && Objects.equals(scanResult.SSID, convertToQuotedString(wifiConfig.SSID)))
                result = wifiManager.enableNetwork(wifiConfig.networkId, true);
            else
                wifiManager.disableNetwork(wifiConfig.networkId);
        return result;
    }

    public boolean reEnableNetworkIfPossible(@NonNull final WifiManager wifiManager, @Nullable final ScanResult scanResult) {
        @Nullable final List<WifiConfiguration> configurations = wifiManager.getConfiguredNetworks();
        if (configurations == null || scanResult == null || configurations.isEmpty())
            return false;
        boolean result = false;
        for (WifiConfiguration wifiConfig : configurations)
            if (Objects.equals(scanResult.BSSID, wifiConfig.BSSID) && Objects.equals(scanResult.SSID, convertToQuotedString(wifiConfig.SSID))) {
                result = wifiManager.enableNetwork(wifiConfig.networkId, true);
                break;
            }
        Log.e(TAG, "reEnableNetworkIfPossible " + result);
        return result;
    }

    public static void uninstallConferenceWiFi(final Context context) {
        // Create conferenceConfig
        WifiConfiguration conferenceConfig = get_Conference_Wifi_Config();

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration wifiConfig : configuredNetworks) {
            if (wifiConfig.SSID.equals(conferenceConfig.SSID)) {
                Log.e(TAG, "Removing network: " + wifiConfig.networkId);
                wifiManager.removeNetwork(wifiConfig.networkId);
            }
        }
    }

    public static boolean isWiFiApConfigured(final Context context) {
        final WifiManager wifiManager =
                (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        final List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();

        if (configs == null) return false;

        // Check for existing APs.
        final String conferenceSSID = getConferenceWifiConfig().SSID;
        for (WifiConfiguration config : configs) {
            if (conferenceSSID.equalsIgnoreCase(config.SSID)) return true;
        }
        return false;
    }

    private static WifiConfiguration get_Conference_Wifi_Config() {
        return null;
    }

    private static ScanResult getConferenceWifiConfig() {
        return null;
    }

    /*@ProtoMethod(description = "Connect to mContext given Wifi network with mContext given 'wpa', 'wep', 'open' type and mContext password", example = "")
    @ProtoMethodParam(params = {"ssidName", "type", "password"})*/
    public void connectWifi(String networkSSID, String type, String networkPass) {

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\""; // Please note the quotes. String
        // should contain ssid in quotes

        if (type.equals("wep")) {
            // wep
            conf.wepKeys[0] = "\"" + networkPass + "\"";
            conf.wepTxKeyIndex = 0;
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        } else if (type.equals("wpa")) {
            // wpa
            conf.preSharedKey = "\"" + networkPass + "\"";
        } else if (type.equals("open")) {
            // open
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();

                break;
            }
        }

    }

    static void reenableAllHotspots(@NonNull WifiManager wifi) {
        final List<WifiConfiguration> configurations = wifi.getConfiguredNetworks();
        if (configurations != null && !configurations.isEmpty())
            for (final WifiConfiguration config : configurations)
                wifi.enableNetwork(config.networkId, false);
    }

    public static void connectToKnownWifi(Context context, String ssid) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            if (i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
            }
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    private static boolean checkForExcessOpenNetworkAndSave(@NonNull final ContentResolver resolver, @NonNull final WifiManager wifiMgr) {
        final List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();
        sortByPriority(configurations);

        boolean modified = false;
        int tempCount = 0;
        final int numOpenNetworksKept = Build.VERSION.SDK_INT >= 17
                ? Settings.Secure.getInt(resolver, Settings.Global.WIFI_NUM_OPEN_NETWORKS_KEPT, 10)
                : Settings.Secure.getInt(resolver, Settings.Secure.WIFI_NUM_OPEN_NETWORKS_KEPT, 10);

        for (int i = configurations.size() - 1; i >= 0; i--) {
            final WifiConfiguration config = configurations.get(i);
            if (Objects.equals(SECURITY_NONE, getSecurity(config))) {
                tempCount++;
                if (tempCount >= numOpenNetworksKept) {
                    modified = true;
                    wifiMgr.removeNetwork(config.networkId);
                }
            }
        }
        return !modified || wifiMgr.saveConfiguration();

    }

    private static void sortByPriority(List<WifiConfiguration> configurations) {
    }

    /**
     * Helper method to decide whether to bypass conference WiFi setup.  Return true if
     * WiFi AP is already configured (WiFi adapter enabled) or WiFi configuration is complete
     * as per shared preference.
     */
    public boolean shouldBypassWiFiSetup(final Context context) {
        final WifiManager wifiManager =
                (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        // Is WiFi on?
        if (wifiManager.isWifiEnabled()) {
            // Check for existing APs.
            final List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
            final String conferenceSSID = getConferenceWifiConfig().SSID;
            for (WifiConfiguration config : configs) {
                if (conferenceSSID.equalsIgnoreCase(config.SSID)) return true;
            }
        }
        return WIFI_CONFIG_DONE;
//    return WIFI_CONFIG_DONE.equals(getWiFiConfigStatus(context));
    }

    private boolean getWiFiConfigStatus(Context context) {
        return true;
    }

    public WifiConfiguration get_WifiConfiguration_given_ssid(String networkSSID, String type, String networkPass) {

        WifiConfiguration conf = new WifiConfiguration();
        WifiConfiguration conf_to_return = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\""; // Please note the quotes. String
        // should contain ssid in quotes

        if (type.equals("wep")) {
            // wep
            conf.wepKeys[0] = "\"" + networkPass + "\"";
            conf.wepTxKeyIndex = 0;
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        } else if (type.equals("wpa")) {
            // wpa
            conf.preSharedKey = "\"" + networkPass + "\"";
        } else if (type.equals("open")) {
            // open
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
                conf_to_return = conf;
            }
        }
        return conf_to_return;
    }

    public boolean toggle_wifi_hotspot(Context context, boolean apState, String hotspotName) {

        WifiManager wifimanager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifimanager.setWifiEnabled(false);

        WifiConfiguration netConfig = new WifiConfiguration();

        netConfig.SSID = hotspotName;
        netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);

        try {
            if (apState) {

                Method method = WifiManager.class.getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                method.invoke(wifimanager, netConfig, apState);

                utilz.getInstance(getApplicationContext()).globalloghandler("WiFi Hotspot \'" + netConfig.SSID + "\' is Created!", TAG, 1, 0);

                return true;
            } else {

                Method method = WifiManager.class.getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                method.invoke(wifimanager, netConfig, false);

                utilz.getInstance(getApplicationContext()).globalloghandler("WiFi Hotspot \'" + netConfig.SSID + "\' is Disabled!", TAG, 1, 0);

                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return false;
    }

    public WifiConfiguration gethotspotConfiguration(Context context) {

        WifiConfiguration config = null;
        WifiManager wifimanager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Method[] methods = WifiManager.class.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().equals("getWifiApConfiguration")) {
                try {
                    config = (WifiConfiguration) m.invoke(wifimanager);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    Log.d("Testing", e.getCause().toString());
                }
            }
        }
        return config;
    }

    public void writePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 200);

            }
        }
    }

    public void level_of_current_connection() {

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

// Level of a Scan Result
        List<ScanResult> wifiList = wifiManager.getScanResults();
        for (ScanResult scanResult : wifiList) {
            int level = WifiManager.calculateSignalLevel(scanResult.level, 5);
            System.out.println("Level is " + level + " out of 5");

// Level of current connection
            int rssi = wifiManager.getConnectionInfo().getRssi();
            int _level = WifiManager.calculateSignalLevel(rssi, 5);
            System.out.println("Level is " + _level + " out of 5");

            utilz.getInstance(getApplicationContext()).globalloghandler("Level is " + _level + " out of 5", TAG, 1, 0);
        }

    }

    public void relaunch_scan_immediately() {

        IntentFilter i = new IntentFilter();
        i.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        final boolean ScanAsFastAsPossible = true;

        registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context c, Intent i) {
                // Code to execute when SCAN_RESULTS_AVAILABLE_ACTION event occurs
                WifiManager w = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
                scanResultHandler(w.getScanResults()); // your method to handle Scan results
                if (ScanAsFastAsPossible) w.startScan(); // relaunch scan immediately
                else { /* Schedule the scan to be run later here */}
            }
        }, i);

        // Launch  wifiscanner the first time here (it will call the broadcast receiver above)
        final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
// without starting scan, we may never receive any scan results
        boolean _startScan = wifiManager.startScan();

    }

    public void scanResultHandler(List<ScanResult> scanResults) {

        final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

//get current connected SSID for comparison to ScanResult
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String currentSSID = wifiInfo.getSSID();

        if (scanResults != null) {
            for (ScanResult scanResult : scanResults) {

                final String ssid = scanResult.SSID;
                final String bssid = scanResult.BSSID;

                Iterable<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();

                if (existingConfigs != null) {
                    for (WifiConfiguration existingConfig : existingConfigs) {
                        String existingSSID = existingConfig.SSID;
                        String configSecurity = get_Security_given_config(existingConfig);

                        if (bssid.equals(existingConfig.BSSID) || ssid.equals(existingConfig.SSID)) {
                            _string_builder.append("bssid [ " + bssid + " ].\n");
                            _string_builder.append("ssid [ " + ssid + " ].\n");
                            _string_builder.append("Security [ " + configSecurity + " ].\n");

                            //utilz.getInstance(getApplicationContext()).globalloghandler(_string_builder.toString(), TAG, 1, 0);

                            txtWifiInfo.setText(_string_builder.toString());

                        }
                    }
                }
            }
        }
    }

    public void Enumerate_Network_Interface_Addresses() {
        final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        final String macAddress = wifiInfo.getMacAddress();
        final String[] macParts = macAddress.split(":");
        final byte[] macBytes = new byte[macParts.length];

        for (int i = 0; i < macParts.length; i++) {
            macBytes[i] = (byte) Integer.parseInt(macParts[i], 16);
        }

        try {
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();

            while (e.hasMoreElements()) {

                NetworkInterface networkInterface = e.nextElement();

                _string_builder.append("Hardware Address [ " + networkInterface.getHardwareAddress() + " ].\n");

                Enumeration<InetAddress> _InetAddresses = networkInterface.getInetAddresses();

                while (_InetAddresses.hasMoreElements()) {

                    InetAddress _InetAddress = _InetAddresses.nextElement();

                    _string_builder.append("Host Address [ " + _InetAddress.getHostAddress() + " ].\n");

                    _string_builder.append("Host Name [ " + _InetAddress.getHostName() + " ].\n");

                }

                _string_builder.append("Display Name [ " + networkInterface.getDisplayName() + " ].\n");

                //utilz.getInstance(getApplicationContext()).globalloghandler(_string_builder.toString(), TAG, 1, 0);

                txtWifiInfo.setText(_string_builder.toString());

                if (Arrays.equals(networkInterface.getHardwareAddress(), macBytes)) {
                    //return networkInterface.getInetAddresses();
                }
            }
        } catch (SocketException e) {
            Log.e(TAG, "Unable to Enumerate NetworkInterfaces.");
            Log.e(TAG, e.toString());
        }
    }


}

class WifiPreference {
    String authAlgorithm;
    String bssid;
    String groupCiphers;
    String pairwiseCiphers;
    int priority;
    String protocols;
    int networkid;
    int status;
    String ssid;
    boolean connected;
    boolean preferred;

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isPreferred() {
        return preferred;
    }

    public void setPreferred(boolean preferred) {
        this.preferred = preferred;
    }

    public String getAuthAlgorithm() {
        return authAlgorithm;
    }

    public void setAuthAlgorithms(String authAlgorithm) {
        this.authAlgorithm = authAlgorithm;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getGroupCiphers() {
        return groupCiphers;
    }

    public void setGroupCiphers(String groupCiphers) {
        this.groupCiphers = groupCiphers;
    }

    public String getPairwiseCiphers() {
        return pairwiseCiphers;
    }

    public void setPairwiseCiphers(String pairwiseCiphers) {
        this.pairwiseCiphers = pairwiseCiphers;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getProtocols() {
        return protocols;
    }

    public void setProtocols(String protocols) {
        this.protocols = protocols;
    }

    public int getNetworkid() {
        return networkid;
    }

    public void setNetworkid(int networkid) {
        this.networkid = networkid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

}

class Wifi {
    String detailedInfo;
    int ipAddress;
    String macAddress;
    int networkId;
    int rssi;
    int speed;
    String ssid;
    int strength;
    String units;
    boolean connected;
    boolean preferred;
    List<WifiPreference> preference;
    boolean iswifi;

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isPreferred() {
        return preferred;
    }

    public void setPreferred(boolean preferred) {
        this.preferred = preferred;
    }

    public String getDetailedInfo() {
        return detailedInfo;
    }

    public void setDetailedInfo(String detailedInfo) {
        this.detailedInfo = detailedInfo;
    }

    public int getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(int ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public int getNetworkId() {
        return networkId;
    }

    public void setNetworkId(int networkId) {
        this.networkId = networkId;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public List<WifiPreference> getPreference() {
        return preference;
    }

    public void setPreference(List<WifiPreference> preference) {
        this.preference = preference;
    }

    public boolean getWifi() {
        return iswifi;
    }

    public void setWifi(boolean iswifi) {
        this.iswifi = iswifi;
    }


}





