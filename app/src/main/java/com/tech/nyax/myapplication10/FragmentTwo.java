package com.tech.nyax.myapplication10;

//import android.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Arrays;
import java.util.List;

public class FragmentTwo extends Fragment {
	
    public static final int REQUEST_CODE = 11;
    public static final int RESULT_CODE = 12;
    public static final String EXTRA_KEY_TEST = "testKey";
    private Button btnconnecttowifi;
    private Button btnchecknetworkconnectivity;
	private static final String SEARCHTERM_ARG = "name";

    public FragmentTwo() {

    }

    public static FragmentTwo newInstance(final String searchterm) {
       final FragmentTwo myFragment = new FragmentTwo();
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
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_two, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        btnconnecttowifi = view.findViewById(R.id.btnconnecttowifi);
        btnconnecttowifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//Do your stuff to place
                    ConnectToNetworkWEP("scylla", "ntharene312");
                } catch (Exception ex) {
// TODO: Handle the error.
                }
            }
        });
        btnchecknetworkconnectivity = view.findViewById(R.id.btnchecknetworkconnectivity);
        btnchecknetworkconnectivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//Do your stuff to place
                    boolean _isnetworkavailable = isNetworkAvailable(getContext());
                    if (_isnetworkavailable) {
                        utilz.getInstance(getActivity().getApplicationContext()).globalloghandler("network available", FragmentTwo.class.getSimpleName(), 1, 0, "network available", "network available");
                    }
                } catch (Exception ex) {
// TODO: Handle the error.
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            String testResult = data.getStringExtra(EXTRA_KEY_TEST);
// TODO: Do something with your extra data
        }
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

    public boolean ConnectToNetworkWEP(String networkSSID, String password) {
        try {
            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + networkSSID + "\""; // Please note the quotes. String should contain SSID in quotes
            conf.wepKeys[0] = "\"" + password + "\""; //Try it with quotes first
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.allowedGroupCiphers.set(WifiConfiguration.AuthAlgorithm.OPEN);
            conf.allowedGroupCiphers.set(WifiConfiguration.AuthAlgorithm.SHARED);
            WifiManager wifiManager = (WifiManager)
                    this.getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            int networkId = wifiManager.addNetwork(conf);
            if (networkId == -1) {
//Try it again with no quotes in case of hex password
                conf.wepKeys[0] = password;
                networkId = wifiManager.addNetwork(conf);
            }
            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration i : list) {
                if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    wifiManager.reconnect();
                    break;
                }
            }
//WiFi Connection success, return true
            utilz.getInstance(getActivity().getApplicationContext()).globalloghandler("connected to wifi", FragmentTwo.class.getSimpleName(), 1, 0, "connected to wifi", "connected to wifi");
            return true;

        } catch (Exception ex) {
            utilz.getInstance(getActivity().getApplicationContext()).globalloghandler(ex.toString(), FragmentTwo.class.getSimpleName(), 1, 0, "FragmentTwo.ConnectToNetworkWEP", "FragmentTwo.ConnectToNetworkWEP");
            System.out.println(Arrays.toString(ex.getStackTrace()));
            return false;
        }
    }

    /**
     * If network connectivity is available, will return true
     *
     * @param context the current context
     * @return boolean true if a network connection is available
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            Log.d("NetworkCheck", "isNetworkAvailable: No");
            return false;
        }
// get network info for all of the data interfaces (e.g. WiFi, 3G, LTE, etc.)
        NetworkInfo[] info = connectivity.getAllNetworkInfo();
// make sure that there is at least one interface to test against
        if (info != null) {
// iterate through the interfaces
            for (int i = 0; i < info.length; i++) {
// check this interface for a connected state
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    Log.d("NetworkCheck", "isNetworkAvailable: Yes");
                    return true;
                }
            }
        }
        return false;
    }

}
