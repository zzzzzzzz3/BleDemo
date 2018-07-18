package com.example.liangfei.bledemo;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements BluetoothAdapter.LeScanCallback{

    private BluetoothManager bluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private Button btnOpenBle;
    private Button btnSearchDevices;
    private RecyclerView deviceListView;
    private DevicesAdapter devicesAdapter;
    private LoadingDialog progressDialog;
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattCallback mBluetoothGattCallback;

    private static final String BLUETOOTH_TAG = "BLUETOOTH_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothManager =   (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mBluetoothGattCallback = new BluetoothGattCallback() {
            @Override
            public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
                super.onPhyUpdate(gatt, txPhy, rxPhy, status);
            }

            @Override
            public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
                super.onPhyRead(gatt, txPhy, rxPhy, status);
            }

            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);

                if(status == BluetoothProfile.STATE_CONNECTED){
                    progressDialog.hide();
                    Toast.makeText(MainActivity.this,"connected",Toast.LENGTH_LONG).show();
                }else if(status == BluetoothProfile.STATE_CONNECTING){
                    progressDialog.show();
                }else {
                    Toast.makeText(MainActivity.this,"disconnect",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
                Log.d(BLUETOOTH_TAG,new String(characteristic.getValue()));
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
                Log.d(BLUETOOTH_TAG,new String(characteristic.getValue()));
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
                Log.d(BLUETOOTH_TAG,new String(characteristic.getValue()));
            }

            @Override
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorRead(gatt, descriptor, status);
                Log.d(BLUETOOTH_TAG,new String(descriptor.getValue()));
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorWrite(gatt, descriptor, status);
                Log.d(BLUETOOTH_TAG,new String(descriptor.getValue()));
            }

            @Override
            public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
                super.onReliableWriteCompleted(gatt, status);
            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                super.onReadRemoteRssi(gatt, rssi, status);
            }

            @Override
            public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
                super.onMtuChanged(gatt, mtu, status);
            }
        };
        initView();
    }

    private void initView() {
        btnOpenBle = findViewById(R.id.openble);
        btnSearchDevices = findViewById(R.id.search_devices);
        deviceListView = findViewById(R.id.decive_list);

        progressDialog = new LoadingDialog(this);

        if(mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()){
            btnOpenBle.setText("OPEN BLUETOOTH");
            btnOpenBle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openBle();
                }
            });
        }else {
            btnOpenBle.setText("BLUETOOTH OPENED");
            btnOpenBle.setEnabled(false);
        }

        btnSearchDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDevices();
            }
        });

        deviceListView.setLayoutManager(new LinearLayoutManager(this));
        devicesAdapter = new DevicesAdapter(this);
        devicesAdapter.setOnConnectListener(new DevicesAdapter.OnConnectListener() {
            @Override
            public void onConnect(BluetoothDevice device) {
                connect(device);
            }
        });
        deviceListView.setAdapter(devicesAdapter);
    }

    private void connect(BluetoothDevice device){
        Toast.makeText(this,"connect to "+device.getName(),Toast.LENGTH_LONG).show();
        bluetoothGatt = device.connectGatt(this,true,mBluetoothGattCallback);
    }

    private void searchDevices() {
        if(mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()){
            Toast.makeText(this,"please open bluetooth",Toast.LENGTH_LONG).show();
        }else {
            devicesAdapter.clearDevices();
            mBluetoothAdapter.startLeScan(this);
            deviceListView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBluetoothAdapter.stopLeScan(MainActivity.this);
                }
            },5000);
        }
    }

    public void openBle(){
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent,0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0&&resultCode == RESULT_OK){
            btnOpenBle.setText("BLUETOOTH OPENED");
            btnOpenBle.setEnabled(false);
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        devicesAdapter.addDevice(device);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.stopLeScan(this);
        }
        mBluetoothAdapter.disable();
    }
}
