package proj.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private Button searchBtn;
    private Button connectBtn;
    private Spinner mySpinner;
    private TextView textView;
    private EditText editText;
    private Button sendBtn;
    private  InputStream iStream;
    private  OutputStream oStream;
    private byte[] mmBuffer;
    private String DATA;

    private Set<BluetoothDevice> pairedDevices;
    public ArrayAdapter<String> adapter;
    private BluetoothSocket btSocket;

    private View.OnClickListener searchBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            searchBtnClicked();
        }
    };
    private View.OnClickListener connectBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            connectBtnClicked();
        }
    };

    private View.OnClickListener sendBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendBtnClicked();
        }
    };
    public static  String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);

        searchBtn = findViewById(R.id.button_search);
        searchBtn.setOnClickListener(searchBtnListener);

        connectBtn = findViewById(R.id.button_connect);
        connectBtn.setOnClickListener(connectBtnListener);

        mySpinner = findViewById(R.id.spinner);
        mySpinner.setAdapter(adapter);

        editText = findViewById(R.id.editText);

        sendBtn = findViewById(R.id.button_send);
        sendBtn.setOnClickListener(sendBtnListener);

        textView = findViewById(R.id.textView);

    }

    private void searchBtnClicked() {
        BluetoothAdapter myBTAdapter = BluetoothAdapter.getDefaultAdapter();
        if (myBTAdapter == null) {
            // Device doesn't support Bluetooth
            // Message a l'utilisateur pour lui dire d'activer BT
        }
        else {
            pairedDevices = myBTAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice device : pairedDevices) {
                    adapter.add(device.getName());
                }
            }
            else {
                // There are no paired devices. We then try to discover available devices nearby
            }
        }
    }

    private void connectBtnClicked() {
        BluetoothDevice device = getSelectedSpinnerItem();
        UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee"); //Standard SerialPortService ID
        try {
            btSocket = device.createRfcommSocketToServiceRecord(uuid);
            if (!btSocket.isConnected()){
                btSocket.connect();
                textView.setText("Connexion réussie");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendBtnClicked() {
        try {
            OutputStream btOutputStream = btSocket.getOutputStream();
            btOutputStream.write(editText.getText().toString().getBytes());
            iStream = btSocket.getInputStream();
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = iStream.read(mmBuffer);




                } catch (IOException e) {
                    e.printStackTrace();

                    break;
                }
                break;
            }

            Context context = getApplicationContext();

            int duration = Toast.LENGTH_SHORT;
            String data = new String(mmBuffer);
            DATA = data;
            Toast toast = Toast.makeText(context, data, duration);
            //toast.show();
            //btSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //creation intent
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(EXTRA_MESSAGE, DATA);
        startActivity(intent);

    }

    private BluetoothDevice getSelectedSpinnerItem() {
        for (BluetoothDevice device : pairedDevices) {
           if(device.getName().equals(mySpinner.getSelectedItem().toString())) {
               return device;
           }
        }
        return null;
    }


}
