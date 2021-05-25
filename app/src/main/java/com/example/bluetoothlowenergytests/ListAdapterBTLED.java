package com.example.bluetoothlowenergytests;

import android.app.Activity;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ListAdapterBTLED extends ArrayAdapter<BluetoothLowEnergyDevices> {

    private String TAG = "LIST ADAPTER - ";

    Activity activity;
    int layoutResourceID;
    ArrayList<BluetoothLowEnergyDevices> devices;

    public ListAdapterBTLED(Activity activity,
                            int layoutResourceID,
                            ArrayList<BluetoothLowEnergyDevices> devices){
        super(activity.getApplicationContext(),
                layoutResourceID,
                devices);

        this.activity = activity;
        this.layoutResourceID = layoutResourceID;
        this.devices = devices;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater =
                    (LayoutInflater) activity
                            .getApplicationContext()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutResourceID, parent, false);
        }

        BluetoothLowEnergyDevices device = devices.get(position);
        String name = device.getName();
        String address = device.getAddress();
        int rssi = device.getRssi();

        TextView textViewName = (TextView) convertView.findViewById(R.id.textViewName);

        if (name != null && name.length() > 0){
            textViewName.setText(device.getName());
        }
        else{
            textViewName.setText("No name for device.");
        }

        TextView textViewRssi = (TextView) convertView.findViewById(R.id.textViewRssi);
        textViewRssi.setText("RSSI: " + Integer.toString(rssi));

        TextView textViewMacAddress = (TextView) convertView.findViewById(R.id.textViewMacAddress);
        if (address != null && address.length() > 0){
            textViewMacAddress.setText(device.getAddress());
        }
        else{
            textViewMacAddress.setText("No MAC Address.");
        }
        return convertView;
    }

}
