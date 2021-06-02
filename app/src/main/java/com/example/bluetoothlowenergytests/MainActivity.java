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

import android.text.TextUtils;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluetoothlowenergytests.models.ResRepository;
import com.example.bluetoothlowenergytests.models.dto.FormDto;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.internal.Util;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener  {
    private final static String TAG = MainActivity.class.getSimpleName();

    public static final int REQUEST_ENABLE_BT = 1;

    private HashMap<String, BluetoothLowEnergyDevices> mBluetoothDevicesHashMap;
    private ArrayList<BluetoothLowEnergyDevices> mBluetoothDevicesArrayList;
    private ListAdapterBTLED adapterBTLED;

    private BroadcastReceiver mBluetoothStateUpdateReceiver;
    private Scanner_BTLE mBTLeScanner;

    private Button btnSearch;
    private Button btnSubmit;
    private NumberPicker numberPickerAge;

    private TextView textViewForm;
    private ScrollView scrollViewForm;
    private EditText editTextName, editTextTargetDevice, editTextTargetDistance;
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
        textViewForm = findViewById(R.id.textViewForm);
        scrollViewForm = findViewById(R.id.scrollViewForm);
        editTextTargetDevice = findViewById(R.id.editTextTargetDevice);
        editTextTargetDistance = findViewById(R.id.editTextTargetDistance);

        textViewForm.setVisibility(View.INVISIBLE);
        textViewForm.setEnabled(false);
        scrollViewForm.setVisibility(View.INVISIBLE);
        scrollViewForm.setEnabled(false);

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
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setEnabled(false);
        btnSubmit.setOnClickListener(this);

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
        } else if (view.getId() == R.id.btnSubmit) {
            String url = "onem2m/app_light1/light1_state_cont";

            String name = editTextName.getText().toString();
            Number age =  numberPickerAge.getValue();
            boolean gender = radioButtonMale.isChecked();
            boolean sport = checkBoxSports.isChecked();
            boolean movie = checkBoxMovies.isChecked();
            boolean series = checkBoxSeries.isChecked();
            boolean programming = checkBoxProgramming.isChecked();

            FormDto form = new FormDto(name, age, gender, sport, movie, series, programming);
            Gson gson= new GsonBuilder().setPrettyPrinting().create();
            String formString = gson.toJson(form);

            ResRepository.Companion.getInstance().addInstance(formString, url, (isSuccess) -> {
                if(isSuccess){
                    Utils.toast(getApplicationContext(), "Submit Button Pressed");
                    getLast();
                }
                return null;
            });
        }
    }

    public void addDevice(ScanResult result) {
        String address = result.getDevice().getAddress();
        String deviceName;
        int distance = Integer.parseInt(editTextTargetDistance.getText().toString());
        // distance = 10*((-rssi+A)/10n)

        if (editTextTargetDevice.getText().toString().trim().length() > 0){
            deviceName = editTextTargetDevice.getText().toString();
        } else {
            deviceName = "-»'«?'";
        }

        if (!mBluetoothDevicesHashMap.containsKey(address)){
            BluetoothLowEnergyDevices bleDevices = new BluetoothLowEnergyDevices(result.getDevice());
            bleDevices.setRssi(result.getRssi());

            mBluetoothDevicesHashMap.put(address, bleDevices);
            mBluetoothDevicesArrayList.add(bleDevices);
            String name = result.getDevice().getName();

            if (TextUtils.isEmpty(name)){
                return;
            } else if (name.contains(deviceName)){
                textViewForm.setVisibility(View.VISIBLE);
                textViewForm.setEnabled(true);
                scrollViewForm.setVisibility(View.VISIBLE);
                scrollViewForm.setEnabled(true);
                btnSubmit.setEnabled(true);
            } else {
                Utils.toast(this, "Insert the Target BLE device name you are looking for.");
                textViewForm.setVisibility(View.INVISIBLE);
                textViewForm.setEnabled(false);
                scrollViewForm.setVisibility(View.INVISIBLE);
                scrollViewForm.setEnabled(false);
                btnSubmit.setEnabled(false);
            }
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
        btnSearch.setEnabled(false);
        mBluetoothDevicesArrayList.clear();
        mBluetoothDevicesHashMap.clear();

        adapterBTLED.notifyDataSetChanged();
        Log.e(TAG, "onClick: START 2" );

        mBTLeScanner.start();
    }

    public void stopScan() {
        btnSearch.setText("Start Scan");
        btnSearch.setEnabled(true);
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
        getLast();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void getLast(){
        String url = "onem2m/app_light1/light1_state_cont/la";
        ResRepository.Companion.getInstance().getInstance(url, (isSuccess, cinDto) -> {
            if(isSuccess){
                Log.e(TAG, "ResRepository-> getLast" );
                editTextName.setText(cinDto.getCon());

                Gson gson= new GsonBuilder().setPrettyPrinting().create();
                FormDto form = gson.fromJson(cinDto.getCon(), FormDto.class);

                editTextName.setText(form.getName());
                numberPickerAge.setValue(form.getAge().intValue());

                if(form.getGender()){
                    radioButtonMale.setChecked(true);
                }else{
                    radioButtonFemale.setChecked(true);
                }
                checkBoxSports.setChecked(form.getSport());
                checkBoxMovies.setChecked(form.getMovie());
                checkBoxSeries.setChecked(form.getSeries());
                checkBoxProgramming.setChecked(form.getProgramming());
            }else{
                Utils.toast(this, "The connection to the application failed.");
            }
            return null;
        });
    }

}