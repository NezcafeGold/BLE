package com.example.nezcafe.blev21;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class BluetoothActivity extends AppCompatActivity {

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // Whenever a remote Bluetooth device is found
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);


                        adapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    };
    private ArrayAdapter adapter;
    private ToggleButton toggleButton;
    private BluetoothAdapter bluetoothAdapter;
    private ListView listview;
    private static final int ENABLE_BT_REQUEST_CODE =2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //adapter.clear();

        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        listview = (ListView) findViewById(R.id.listView);



        adapter = new ArrayAdapter
                (this,android.R.layout.simple_list_item_1);
        listview.setAdapter(adapter);

        if (bluetoothAdapter.isEnabled()) toggleButton.setChecked(true);
        workWithBt();

    }

    public void onToggleClicked(View view) {

        adapter.clear();

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(BluetoothActivity.this, "BT is not supported", Toast.LENGTH_SHORT).show();
        } else {

           workWithBt();




        }
    }

    protected void discoverDevices(){
        // To scan for remote Bluetooth devices
        if (bluetoothAdapter.startDiscovery()) {
            Toast.makeText(getApplicationContext(), "Discovering other bluetooth devices...",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Discovery failed to start.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    protected void onResume() {
        super.onResume();
        // Register the BroadcastReceiver for ACTION_FOUND
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(broadcastReceiver, filter);


    }

    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(broadcastReceiver);
    }

    private void workWithBt(){

        if (toggleButton.isChecked()) {

            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetoothIntent, ENABLE_BT_REQUEST_CODE);
            } else {
                Toast.makeText(getApplicationContext(), "Your device has already been enabled." + "\n" + "Scanning for remote Bluetooth devices...",
                        Toast.LENGTH_SHORT).show();
                //поиск устройств
                discoverDevices(); // TODO: 11.09.2016
                // делает устройство доступным для поиска
                //makeDiscoverable(); // TODO: 11.09.2016
            }
        } else {
            bluetoothAdapter.disable();
            adapter.clear();
            Toast.makeText(getApplicationContext(), "BT is disable", Toast.LENGTH_SHORT).show();
        }
    }

}
