package com.tech.nyax.myapplication10;

import android.content.Context;
import android.os.Build;

import java.util.List;

public class BluetoothScanningFactory implements ScanningAdapter {
    private ScanningAdapter mScanningAdapter;

    public BluetoothScanningFactory(Context context) {
        if (isNewerAPI()) {
            mScanningAdapter = LollipopBluetoothLEScanAdapter.getInstance(context);
        } else {
            mScanningAdapter = JellyBeanBluetoothLEScanAdapter.getInstance(context);
        }
    }

    private boolean isNewerAPI() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @Override
    public void startScanning(String[] uuids) {
        mScanningAdapter.startScanning(uuids);
    }

    @Override
    public void startScanning() {
        mScanningAdapter.startScanning();
    }

    @Override
    public void stopScanning() {
        mScanningAdapter.stopScanning();
    }

    @Override
    public List<BTDevice> getFoundDeviceList() {
        return mScanningAdapter.getFoundDeviceList();
    }
}