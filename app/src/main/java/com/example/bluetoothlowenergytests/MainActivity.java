package com.example.bluetoothlowenergytests;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener  {
    private final static String TAG = MainActivity.class.getSimpleName();

    public static final int REQUEST_ENABLE_BT = 1;
    /*
    private int serverAge;
    private boolean isServerMaleChecked, isServerFemaleChecked, isServerCheckMovieChecked, isServerCheckSeriesChecked, isServerCheckSportsChecked, isServerCheckProgrammingChecked;
*/
    private HashMap<String, BluetoothLowEnergyDevices> mBluetoothDevicesHashMap;
    private ArrayList<BluetoothLowEnergyDevices> mBluetoothDevicesArrayList;
    private ListAdapterBTLED adapterBTLED;
    private HashMap<String, String> bluetoothDeviceInfo;

    private BroadcastReceiver mBluetoothStateUpdateReceiver;
    private Scanner_BTLE mBTLeScanner;

    private Button btnSearch;
    private NumberPicker numberPickerAge;

    private EditText editTextName;
    private RadioButton radioButtonMale, radioButtonFemale;
    private CheckBox checkBoxSports, checkBoxMovies, checkBoxSeries, checkBoxProgramming;
    private LinearLayout form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        form = findViewById(R.id.form);
        numberPickerAge = findViewById(R.id.numberPickerAge);
        numberPickerAge.setMaxValue(99);
        numberPickerAge.setMinValue(1);
        numberPickerAge.setValue(25);
        editTextName = findViewById(R.id.editText);
        radioButtonMale = findViewById(R.id.radio1);
        radioButtonFemale = findViewById(R.id.radio2);
        checkBoxSports = findViewById(R.id.checkboxSports);
        checkBoxMovies = findViewById(R.id.checkboxMovies);
        checkBoxSeries = findViewById(R.id.checkboxSeries);
        checkBoxProgramming = findViewById(R.id.checkboxProgramming);
        
/*
        Instead of listing devices, catch all devices, and if the one we want is one of them (by name, for example),
        change the contents of all inputs fields with the one in the server.

        bluetoothDeviceInfo = adapterBTLED.getBluetoothDeviceInfo(); // get the info of the devices caught in range

        String deviceName = bluetoothDeviceInfo.get("name"); // get the name of the device
        if (deviceName == "[TV] TV - cozinha"){ // if the device name is equal to the wanted name, change all input fields

            // set values that will come from server

            numberPickerAge.setValue(serverAge);
            radioButtonMale.setChecked(isServerMaleChecked);
            radioButtonFemale.setChecked(isServerFemaleChecked);
            checkBoxSports.setChecked(isServerCheckSportsChecked);
            checkBoxMovies.setChecked(isServerCheckMovieChecked);
            checkBoxSeries.setChecked(isServerCheckSeriesChecked);
            checkBoxProgramming.setChecked(isServerCheckProgrammingChecked);
        }
        // info was changed!
 */

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
            Utils.toast(getApplicationContext(), "BLE not supported");
            finish();
        }

        mBluetoothStateUpdateReceiver = new BroadcastReceiver(getApplicationContext());
        mBTLeScanner = new Scanner_BTLE(this, 15000, -75);// ScanPeriod e SignalStrength são adaptaveis as nossa necessidades;

        mBluetoothDevicesHashMap = new HashMap<>();
        mBluetoothDevicesArrayList = new ArrayList<>();

        adapterBTLED = new ListAdapterBTLED(this, R.layout.activity_device_scan, mBluetoothDevicesArrayList);

        ListView listView = new ListView(this);

        listView.setAdapter(adapterBTLED);
        listView.setOnItemClickListener(this);

        btnSearch = findViewById(R.id.btn_scan);
        ((ScrollView) findViewById(R.id.scrollView)).addView(listView);
        findViewById(R.id.btn_scan).setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (requestCode == RESULT_OK) {
                Utils.toast(getApplicationContext(), "Thanks! BL turned on!");
            } else if (requestCode == RESULT_CANCELED) {
                Utils.toast(getApplicationContext(), "Please turn on Bluetooth.");
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_scan) {
            Utils.toast(getApplicationContext(), "Scan Button Pressed");
            if (!mBTLeScanner.isScanning()) {
                Log.e(TAG, "onClick: START" );
                startScan();
            } else {
                Log.e(TAG, "onClick: STOP" );
                stopScan();
            }
        }
    }

    public void addDevice(ScanResult result) {
        String address = result.getDevice().getAddress();

        if (!mBluetoothDevicesHashMap.containsKey(address)){
            BluetoothLowEnergyDevices bleDevices = new BluetoothLowEnergyDevices(result.getDevice());
            bleDevices.setRssi(result.getRssi());

            mBluetoothDevicesHashMap.put(address, bleDevices);
            mBluetoothDevicesArrayList.add(bleDevices);
        }
        else{
            mBluetoothDevicesHashMap.get(address).setRssi(result.getRssi());
        }
        adapterBTLED.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mBluetoothStateUpdateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBluetoothStateUpdateReceiver);
        stopScan();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopScan();
    }

    public void startScan(){
        btnSearch.setText("Scanning...");

        mBluetoothDevicesArrayList.clear();
        mBluetoothDevicesHashMap.clear();

        adapterBTLED.notifyDataSetChanged();
        Log.e(TAG, "onClick: START 2" );

        mBTLeScanner.start();
    }

    public void stopScan() {
        btnSearch.setText("Scan Stopped");
        mBTLeScanner.stop();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}