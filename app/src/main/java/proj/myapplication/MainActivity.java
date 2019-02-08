package proj.myapplication;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private Button searchBtn;
    private Button connectPairedBtn;
    private Button connectOtherBtn;
    private Spinner pairedSpinner;
    private Spinner otherSpinner;
    private TextView connexionSuccessTextView;
    private TextView pairedDevicesTextView;
    private TextView otherDevicesTextView;
    private EditText editText;
    private Button sendBtn;
    private BT BTObject;


    public ArrayAdapter<String> pairedAdapter;
    public ArrayAdapter<String> otherAdapter;

    private View.OnClickListener searchBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            searchBtnClicked();
        }
    };
    private View.OnClickListener connectBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.button_connectPaired) {
                connectBtnClicked(true);
            }
            else if (v.getId() == R.id.button_connectOther) {
                connectBtnClicked(false);
            }
        }
    };

    private View.OnClickListener sendBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendBtnClicked();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pairedAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        otherAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);

        BTObject = new BT();

        BTObject.setPairedAdapter(pairedAdapter);
        BTObject.setOtherAdapter(otherAdapter);

        BTObject.searchPairedDevices();

        searchBtn = (Button)findViewById(R.id.button_search);
        searchBtn.setOnClickListener(searchBtnListener);

        pairedSpinner = (Spinner)findViewById(R.id.spinner_paired);
        pairedSpinner.setAdapter(pairedAdapter);

        otherSpinner = (Spinner)findViewById(R.id.spinner_other);
        otherSpinner.setAdapter(otherAdapter);

        connectPairedBtn = (Button)findViewById(R.id.button_connectPaired);
        connectPairedBtn.setOnClickListener(connectBtnListener);

        connectOtherBtn = (Button)findViewById(R.id.button_connectOther);
        connectOtherBtn.setOnClickListener(connectBtnListener);

        editText = (EditText)findViewById(R.id.editText);

        sendBtn = (Button)findViewById(R.id.button_send);
        sendBtn.setOnClickListener(sendBtnListener);

        connexionSuccessTextView = (TextView)findViewById(R.id.textView_connexionSuccess);
        pairedDevicesTextView = (TextView)findViewById(R.id.textView_pairedDevices);
        otherDevicesTextView = (TextView)findViewById(R.id.textView_otherDevices);

    }

    private void searchBtnClicked() {
        BTObject.discoverDevices();
    }


    private void connectBtnClicked(boolean boundedPressed) {
        boolean connected = BTObject.connect(getSelectedSpinnerItem(boundedPressed));
        if (connected) {
            connexionSuccessTextView.setText(getString(R.string.connexion_success));
        }
    }

    private void sendBtnClicked() {
        try {
            BluetoothSocket btSocket = BTObject.getBtSocket();
            OutputStream btOutputStream = btSocket.getOutputStream();
            btOutputStream.write(editText.getText().toString().getBytes());
            //btSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BluetoothDevice getSelectedSpinnerItem(boolean bounded) {
        Set<BluetoothDevice> devices;
        if (bounded) {
            devices = BTObject.getPairedDevices();
            for (BluetoothDevice device : devices) {
                if(device.getName().equals(pairedSpinner.getSelectedItem().toString())) {
                    return device;
                }
            }
        }
        else {
            devices = BTObject.getOtherDevices();
            for (BluetoothDevice device : devices) {
                if(device.getName().equals(otherSpinner.getSelectedItem().toString())) {
                    return device;
                }
            }
        }

        return null;
    }
}
