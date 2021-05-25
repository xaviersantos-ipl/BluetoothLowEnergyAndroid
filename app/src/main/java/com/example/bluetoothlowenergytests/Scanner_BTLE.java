package com.example.bluetoothlowenergytests;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Scanner_BTLE {

    private MainActivity ma;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private long scanPeriod;
    private int signalStrength;

    public Scanner_BTLE(MainActivity mainActivity, long scanPeriod, int signalStrength){
        ma = mainActivity;
        mHandler = new Handler();

        this.scanPeriod = scanPeriod;
        this.signalStrength = signalStrength;

        final BluetoothManager bluetoothManager = (BluetoothManager) ma.getSystemService(Context.BLUETOOTH_SERVICE);

        mBluetoothAdapter = bluetoothManager.getAdapter();

        Log.e("TAG", "Scanner_BTLE: " );
    }

    public boolean isScanning(){
        return mScanning;
    }

    public void start(){
        if (!Utils.checkBluetooth(mBluetoothAdapter)){
            Utils.requestUserBluetooth(ma);
            ma.stopScan();
        }
        else{
            scanLeDevice(true);
        }
    }

    public void stop(){
        scanLeDevice(false);
    }

    private void scanLeDevice(final boolean enable){// look at scan period and wait time=scanPeriod to run whatever is inside the function run()
        final BluetoothLeScanner bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

        if (enable && !mScanning) {
            Utils.toast(ma.getApplication(), "Starting BLE scan...");

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Utils.toast(ma.getApplicationContext(), "Stoping BLE scan...");
                    mScanning = false;
                    bluetoothLeScanner.stopScan(mLeScanCallback);
                    //mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    ma.stopScan();
                }
            }, scanPeriod);
            mScanning = true;
            bluetoothLeScanner.startScan(mLeScanCallback);
            //mBluetoothAdapter.startLeScan(mLeScanCallback);
        }
    }
    public ArrayList<String> device;

    public ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.e("TAG", "onScanResult: "+ result.getDevice().getName());
            ma.addDevice(result);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            Log.e("TAG", "onBatchScanResults: " + results );
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("TAG", "onScanFailed: " + errorCode);
            super.onScanFailed(errorCode);
        }
    };

    public ArrayList<String> getDevice(){
        return device;
    }


/*
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {
            final int new_rssi = rssi;
            Log.e("TAG", "onLeScan: " );

            Log.e("TAG", "onLeScan: "+rssi );
            if (rssi > signalStrength){
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ma.addDevice(bluetoothDevice, new_rssi);
                    }
                });
            }
        }
    };

 */

}
