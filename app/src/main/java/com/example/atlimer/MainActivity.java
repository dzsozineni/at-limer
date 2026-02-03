package com.example.atlimer;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends Activity {

    BluetoothAdapter btAdapter;
    ArrayList<String> devices = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayList<BluetoothDevice> deviceObjects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        ListView list = findViewById(R.id.listDevices);
        Button scan = findViewById(R.id.btnScan);

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, devices);
        list.setAdapter(adapter);

        scan.setOnClickListener(v -> scanDevices());

        list.setOnItemClickListener((parent, view, pos, id) -> {
            BluetoothDevice dev = deviceObjects.get(pos);
            BtManager.connect(dev);

            Intent i = new Intent(this, DashboardActivity.class);
            startActivity(i);
        });
    }

    void scanDevices() {
        devices.clear();
        deviceObjects.clear();
        adapter.notifyDataSetChanged();

        Set<BluetoothDevice> paired = btAdapter.getBondedDevices();
        for (BluetoothDevice d : paired) {
            devices.add(d.getName() + "\n" + d.getAddress());
            deviceObjects.add(d);
        }
        adapter.notifyDataSetChanged();
    }
}
