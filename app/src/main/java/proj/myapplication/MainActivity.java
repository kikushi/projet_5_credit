package proj.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private UUID uuid;
    private Button searchBtn;
    private Button connectPairedBtn;
    private Button connectOtherBtn;
    private Spinner pairedSpinner;
    private Spinner otherSpinner;
    private TextView informationsTextView;
    private TextView pairedDevicesTextView;
    private TextView otherDevicesTextView;
    private EditText editText;
    private Button sendBtn;

    public Set<BluetoothDevice> pairedDevices;
    public Set<BluetoothDevice> otherDevices;
    public ArrayAdapter<String> pairedAdapter = null;
    public ArrayAdapter<String> otherAdapter = null;
    public BluetoothSocket btSocket = null;
    public BluetoothAdapter myBTAdapter;
    public IntentFilter filter_found;
    public IntentFilter filter_aclDisconnected;
    public IntentFilter filter_discoveryFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pairedAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        otherAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);

        //Bluetooth
        myBTAdapter = BluetoothAdapter.getDefaultAdapter();
        pairedDevices = new HashSet<BluetoothDevice>();
        otherDevices = new HashSet<BluetoothDevice>();
        uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID

        filter_found = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter_aclDisconnected = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter_discoveryFinished = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiverActionFound, filter_found);
        registerReceiver(mReceiverActionAclDisconnected, filter_aclDisconnected);
        registerReceiver(mReceiverActionDiscoveryFinished, filter_discoveryFinished);

        //Widgets
        searchBtn = (Button)findViewById(R.id.button_search);
        searchBtn.setOnClickListener(searchBtnListener);

        pairedSpinner = (Spinner)findViewById(R.id.spinner_paired);
        pairedSpinner.setAdapter(pairedAdapter);

        otherSpinner = (Spinner)findViewById(R.id.spinner_other);
        otherSpinner.setAdapter(otherAdapter);
        otherSpinner.setVisibility(View.INVISIBLE);

        connectPairedBtn = (Button)findViewById(R.id.button_connectPaired);
        connectPairedBtn.setOnClickListener(connectBtnListener);

        connectOtherBtn = (Button)findViewById(R.id.button_connectOther);
        connectOtherBtn.setOnClickListener(connectBtnListener);
        connectOtherBtn.setVisibility(View.INVISIBLE);

        editText = (EditText)findViewById(R.id.editText);
        editText.setVisibility(View.INVISIBLE);

        sendBtn = (Button)findViewById(R.id.button_send);
        sendBtn.setOnClickListener(sendBtnListener);
        sendBtn.setVisibility(View.INVISIBLE);

        informationsTextView = (TextView)findViewById(R.id.textView_informations);
        pairedDevicesTextView = (TextView)findViewById(R.id.textView_pairedDevices);
        otherDevicesTextView = (TextView)findViewById(R.id.textView_otherDevices);

        SearchPairedDevices();
    }

    private View.OnClickListener searchBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SearchBtnClicked();
        }
    };

    private View.OnClickListener connectBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean success = false;
            if (v.getId() == R.id.button_connectPaired) {
                success = ConnectBtnClicked(true);
            }
            else if (v.getId() == R.id.button_connectOther) {
                success = ConnectBtnClicked(false);
            }
            if (success) {
                informationsTextView.setText(getString(R.string.connexion_success) + btSocket.getRemoteDevice().getName());
                informationsTextView.setVisibility(View.VISIBLE);
                editText.setVisibility(View.VISIBLE);
                sendBtn.setVisibility(View.VISIBLE);
            }
        }
    };

    private View.OnClickListener sendBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SendBtnClicked();
        }
    };

    // Create a BroadcastReceiver for Bluetooth.ACTION_FOUND
    private final BroadcastReceiver mReceiverActionFound = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.i("TAG", "DeviceFound");
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Device Found
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                otherDevices.add(device);
                if (!Contains(otherAdapter, device.getName())) {
                    otherAdapter.add(device.getName());
                }
            }
        }
    };

    // Create a BroadcastReceiver for Bluetooth.ACTION_ACL_DISCONNECTED
    private final BroadcastReceiver mReceiverActionAclDisconnected = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //Device disconnected
                Log.i("TAG", "Disconnected");
                sendBtn.setVisibility(View.INVISIBLE);
                editText.setVisibility(View.INVISIBLE);
                informationsTextView.setText(getString(R.string.connexion_with_host_ended));
                try {btSocket.close();} catch (IOException e) { e.printStackTrace();}
                btSocket = null;
                SearchPairedDevices();
                //Retour a la page de connexion
            }
        }
    };

    // Create a BroadcastReceiver for Bluetooth.ACTION_DISCOVERY_FINISHED
    private final BroadcastReceiver mReceiverActionDiscoveryFinished = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //Discovery is finished
                informationsTextView.setText(getString(R.string.device_discovery_finished));
                myBTAdapter.cancelDiscovery();
                unregisterReceiver(mReceiverActionDiscoveryFinished);
            }
        }
    };

    private void SearchBtnClicked() {
        if (myBTAdapter.isDiscovering()) {
            myBTAdapter.cancelDiscovery();
        }
        myBTAdapter.startDiscovery();
        registerReceiver(mReceiverActionDiscoveryFinished, filter_discoveryFinished);
        otherSpinner.setVisibility(View.VISIBLE);
        connectOtherBtn.setVisibility(View.VISIBLE);
        informationsTextView.setText(getString(R.string.looking_for_nearby_devices));
    }

    private boolean ConnectBtnClicked(boolean boundedPressed) {
        //A optimiser

        try {
            if (btSocket == null) {
                BluetoothDevice device = GetSelectedSpinnerItem(boundedPressed);
                if (device == null) {
                    informationsTextView.setText(getString(R.string.no_device_selected));
                }
                else {
                    btSocket = GetSelectedSpinnerItem(boundedPressed).createRfcommSocketToServiceRecord(uuid);
                    btSocket.connect();
                    if (btSocket.isConnected()) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private void SendBtnClicked() {
        try {
            OutputStream btOutputStream = btSocket.getOutputStream();
            btOutputStream.write(editText.getText().toString().getBytes());
            //btSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BluetoothDevice GetSelectedSpinnerItem(boolean bounded) {
        if (bounded) {
            for (BluetoothDevice device : pairedDevices) {
                if(device.getName().equals(pairedSpinner.getSelectedItem().toString())) {
                    return device;
                }
            }
            //No device selected
            return null;
        }
        else {
            for (BluetoothDevice device : otherDevices) {
                if(device.getName().equals(otherSpinner.getSelectedItem().toString())) {
                    return device;
                }
            }
        }
        return null;
    }

    private void SearchPairedDevices() {
        if (myBTAdapter == null) {
            // Device doesn't support Bluetooth
            // Message a l'utilisateur pour lui dire d'activer BT
        }
        else {
            pairedDevices = myBTAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                pairedAdapter.clear();
                for (BluetoothDevice device : pairedDevices) {
                    pairedAdapter.add(device.getName());
                }
            }
        }
    }

    private boolean Contains(ArrayAdapter<String> theAdapter, String element) {
        for (int i = 0; i < theAdapter.getCount(); i++) {
            if (theAdapter.getItem(i).equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiverActionFound);
        unregisterReceiver(mReceiverActionAclDisconnected);
        unregisterReceiver(mReceiverActionDiscoveryFinished);
    }

}
