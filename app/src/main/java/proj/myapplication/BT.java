package proj.myapplication;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.support.v7.app.AppCompatActivity;

import android.widget.ArrayAdapter;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class BT extends AppCompatActivity {

    public Set<BluetoothDevice> pairedDevices;
    public Set<BluetoothDevice> otherDevices;
    public ArrayAdapter<String> pairedAdapter = null;
    public ArrayAdapter<String> otherAdapter = null;
    public BluetoothSocket btSocket;
    public BluetoothAdapter myBTAdapter;
    public IntentFilter filter;

    public BT() {
        myBTAdapter = BluetoothAdapter.getDefaultAdapter();
        pairedDevices = myBTAdapter.getBondedDevices();

        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

    }
    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                otherDevices.add(device);
            }
        }
    };

    public void setPairedAdapter(ArrayAdapter<String> theAdapter) {
        pairedAdapter = theAdapter;
    }

    public void setOtherAdapter(ArrayAdapter<String> theAdapter) {
        otherAdapter = theAdapter;
    }

    public Set<BluetoothDevice> getPairedDevices() {
        return pairedDevices;
    }

    public Set<BluetoothDevice> getOtherDevices() {
        return otherDevices;
    }

    public BluetoothSocket getBtSocket() {
        return btSocket;
    }

    public void searchPairedDevices() {
        if (myBTAdapter == null) {
            // Device doesn't support Bluetooth
            // Message a l'utilisateur pour lui dire d'activer BT
        }
        else {
            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                pairedAdapter.clear();
                for (BluetoothDevice device : pairedDevices) {
                    pairedAdapter.add(device.getName());
                }
            }
        }
    }

    public void discoverDevices() {
        if (myBTAdapter.isDiscovering()) {
            myBTAdapter.cancelDiscovery();
        }
        myBTAdapter.startDiscovery();
    }

    public boolean connect(BluetoothDevice theDevice) {
        UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee"); //Standard SerialPortService ID
        try {
            btSocket = theDevice.createRfcommSocketToServiceRecord(uuid);
            if (!btSocket.isConnected()){
                btSocket.connect();
                this.unregisterReceiver(mReceiver);
                return true;
            }
            else {
                return false;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}
