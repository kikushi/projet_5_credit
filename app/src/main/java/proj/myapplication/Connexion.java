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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Connexion extends AppCompatActivity {

    private UUID uuid;
    private Spinner pairedSpinner;
    private Button connectOtherBtn;
    private Spinner otherSpinner;

    public Set<BluetoothDevice> pairedDevices;
    public Set<BluetoothDevice> otherDevices;
    public ArrayAdapter<String> pairedAdapter = null;
    public ArrayAdapter<String> otherAdapter = null;

    public static BluetoothSocket btSocket = null;
    public BluetoothAdapter myBtAdapter;

    public IntentFilter filter_found;
    public IntentFilter filter_discoveryFinished;
    public IntentFilter filter_disconnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Tag", "onCreate - Connexion");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        pairedAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, R.id.tv_spinner);
        pairedAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        otherAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, R.id.tv_spinner);
        otherAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        pairedDevices = new HashSet<>();
        otherDevices = new HashSet<>();

        //Bluetooth
        myBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (myBtAdapter == null) {
            //Bluetooth not supported by device
            finish();
        }
        else if (!myBtAdapter.isEnabled()) {
            //Bluetooth is disabled
            myBtAdapter.enable();
        }

        uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee"); //Standard SerialPortService ID

        filter_found = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter_discoveryFinished = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter_disconnected = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        //Widgets
        Button getPairedBtn = findViewById(R.id.button_get_paired);
        getPairedBtn.setOnClickListener(getPairedBtnListener);

        Button searchOtherBtn = findViewById(R.id.button_search_other);
        searchOtherBtn.setOnClickListener(searchOtherBtnListener);

        pairedSpinner = findViewById(R.id.spinner_paired);
        pairedSpinner.setAdapter(pairedAdapter);
        pairedSpinner.setDropDownVerticalOffset(30);

        otherSpinner = findViewById(R.id.spinner_other);
        otherSpinner.setDropDownVerticalOffset(30);
        otherSpinner.setAdapter(otherAdapter);

        Button connectPairedBtn = findViewById(R.id.button_connectPaired);
        connectPairedBtn.setOnClickListener(connectBtnListener);

        connectOtherBtn = findViewById(R.id.button_connectOther);
        connectOtherBtn.setOnClickListener(connectBtnListener);

    }
    @Override
    public void onStart() {
        super.onStart();
        Log.i("Tag", "onStart - Connexion");

        if (btSocket != null) {
            try {
                btSocket.getInputStream().close();
            } catch (IOException e) {
                Log.e("Tag", "InputStream's close() method failed");
            } catch (NullPointerException e) {
                Log.e("Tag", "tried to access InputStream while it was null");
            }

            try {
                btSocket.getOutputStream().close();
            } catch (IOException e) {
                Log.e("Tag", "OutputStream's close() method failed");
            } catch (NullPointerException e) {
                Log.e("Tag", "tried to access OutputStream while it was null");
            }
            try {
                btSocket.close();
            } catch (IOException e) {
                Log.e("Tag", "Socket's close() method failed");
            } catch (NullPointerException e) {
                Log.e("Tag", "tried to access btSocket while it was null");
            }
        }
        btSocket = null;

        registerReceiver(mReceiverActionFound, filter_found);
        registerReceiver(mReceiverActionDiscoveryFinished, filter_discoveryFinished);

        GetPairedDevices(); //Updates known devices spinner

        otherSpinner.setVisibility(View.INVISIBLE);
        connectOtherBtn.setVisibility(View.INVISIBLE);

        try {
            unregisterReceiver(mReceiverActionAclDisconnected);
        } catch(IllegalArgumentException e) {
            Log.e("Tag", "Unregistering receivermReceiverActionAclDisconnected failed");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_connexion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_comm_close_app:
                this.finishAffinity();
                return true;
        }
        return false;
    }

    private View.OnClickListener getPairedBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            GetPairedDevices();
        }
    };

    private View.OnClickListener searchOtherBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SearchOtherDevices();
        }
    };

    private View.OnClickListener connectBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean success = false;
            if (v.getId() == R.id.button_connectPaired) {
                success = Connect(true);
            }
            else if (v.getId() == R.id.button_connectOther) {
                success = Connect(false);
            }
            if (success) {
                registerReceiver(mReceiverActionAclDisconnected, filter_disconnected);
                ChangeView(Configuration.class);
            }
            else {
                btSocket = null;
                Toast.makeText(getApplicationContext(), getString(R.string.connexion_failed), Toast.LENGTH_SHORT).show();
            }
        }
    };

    // Create a BroadcastReceiver for Bluetooth.ACTION_FOUND
    private final BroadcastReceiver mReceiverActionFound = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Device Found
                Log.i("Tag", "Device Found");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                otherDevices.add(device);
                String name = device.getName();
                if (name == null) {
                    name = device.getAddress();
                }
                if (!Contains(otherAdapter, name)) {
                    otherAdapter.add(name);
                }
            }
        }
    };

    // Create a BroadcastReceiver for Bluetooth.ACTION_DISCOVERY_FINISHED
    private final BroadcastReceiver mReceiverActionDiscoveryFinished = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //Discovery is finished
                myBtAdapter.cancelDiscovery();
                findViewById(R.id.tv_lookingNearbyDevices).setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), getString(R.string.Discovery_finished), Toast.LENGTH_SHORT).show();
                unregisterReceiver(mReceiverActionDiscoveryFinished);
            }
        }
    };

    // Create a BroadcastReceiver for Bluetooth.ACTION_ACL_DISCONNECTED
    private final BroadcastReceiver mReceiverActionAclDisconnected = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //Device disconnected
                Log.i("Tag", "Disconnected");
                //Retour a la page de connexion
                if (getApplicationContext() != Connexion.this) {
                    ChangeView(Connexion.class);
                }
                Toast.makeText(Connexion.this, R.string.connexion_with_host_ended, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void GetPairedDevices() {
        if (myBtAdapter != null) {
            pairedDevices = myBtAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                pairedAdapter.clear();
                for (BluetoothDevice device : pairedDevices) {
                    pairedAdapter.add(device.getName());
                }
            }
        }
    }

    private void SearchOtherDevices() {
        if (myBtAdapter.isDiscovering()) {
            myBtAdapter.cancelDiscovery();
        }
        myBtAdapter.startDiscovery();
        registerReceiver(mReceiverActionDiscoveryFinished, filter_discoveryFinished);
        otherSpinner.setVisibility(View.VISIBLE);
        connectOtherBtn.setVisibility(View.VISIBLE);
        findViewById(R.id.tv_lookingNearbyDevices).setVisibility(View.VISIBLE);
    }

    private boolean Connect(boolean boundedPressed) {
        BluetoothDevice device = GetSelectedSpinnerItem(boundedPressed);

        if (device == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_device_selected), Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            if (btSocket == null) {
                try {
                    btSocket = device.createInsecureRfcommSocketToServiceRecord(uuid);
                } catch (IOException e) {
                    Log.e("Tag", "Socket's create() method failed", e);
                }
                try {
                    btSocket.connect();
                } catch (IOException e) {
                    Log.e("Tag", "Socket's connect() method failed", e);
                }
                return btSocket.isConnected();
            }
            return false;
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

    private boolean Contains(ArrayAdapter<String> theAdapter, String element) {
        try {
            if (!theAdapter.isEmpty()) {
                for (int i = 0; i < theAdapter.getCount(); i++) {
                    if (Objects.requireNonNull(theAdapter.getItem(i)).equals(element)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (NullPointerException e) {
            Log.e("Tag", "Nullptr");
            return true;
        }
    }

    public void ChangeView(Class activity) {
        Intent intent = new Intent(getApplicationContext(), activity);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    @Override
    protected void onStop() {
        Log.i("Tag", "onStop() - Connexion");
        super.onStop();
        try {
            unregisterReceiver(mReceiverActionFound);
        } catch(IllegalArgumentException e) {
            Log.e("Tag", "Unregistering receiver mReceiverActionFound failed", e);
        }
        try {
            unregisterReceiver(mReceiverActionDiscoveryFinished);
        } catch(IllegalArgumentException e) {
            Log.e("Tag", "Unregistering receiver mReceiverActionDiscoveryFinished failed", e);
        }
    }

    @Override
    protected void onDestroy() {
        Log.i("Tag", "onDestroy() - Connexion");
        super.onDestroy();
        try {
            unregisterReceiver(mReceiverActionAclDisconnected);
        } catch(IllegalArgumentException e) {
            Log.e("Tag", "Unregistering receiver mReceiverActionAclDisconnected failed");
        }
    }
}
