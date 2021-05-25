package com.example.bluetoothlowenergytests;

import android.bluetooth.BluetoothDevice;

public class BluetoothLowEnergyDevices {
    private final BluetoothDevice bluetoothDevice;
    private int rssi;
    public BluetoothLowEnergyDevices(BluetoothDevice bluetoothDevice){this.bluetoothDevice = bluetoothDevice;}
    public String getAddress(){return bluetoothDevice.getAddress();}
    public String getName(){return  bluetoothDevice.getName();}
    public void setRssi(int rssi){this.rssi = rssi;}
    public int getRssi(){return rssi;}
}
