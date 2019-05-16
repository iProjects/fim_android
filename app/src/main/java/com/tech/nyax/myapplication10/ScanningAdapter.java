package com.tech.nyax.myapplication10;

import java.util.List;

public interface ScanningAdapter {
    void startScanning();

    void startScanning(String[] uuids);

    void stopScanning();

    List<BTDevice> getFoundDeviceList();
}