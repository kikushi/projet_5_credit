package proj.myapplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;



import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

//private Button ecran_on;

public class DisplayMessageActivity extends Activity {


    private Button led_on;
    private Button led_off;

    private TextView txtview;

    public BluetoothSocket btSocket2;

    public InputStream iStream2;

    public byte[] mmBuffer2;
    private Spinner mySpinner2;

    private EditText editText2;

    public static  String EXTRA_MESSAGE_2 = "com.example.myfirstapp.MESSAGE";

    private BluetoothDevice mdevice;
    public Set<BluetoothDevice> pairedDevices2;
    public ArrayAdapter<String> adapter2;

    private View.OnClickListener onBtnListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBtnClicked();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        led_on = findViewById(R.id.button_on);

        editText2 = findViewById(R.id.editText2);

        led_on.setOnClickListener(onBtnListener2);

        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);

        mySpinner2 = findViewById(R.id.spinner2);
        mySpinner2.setAdapter(adapter2);




        // Get the Intent that started this activity and extract the string
        Intent intent_ = getIntent();
        String message = intent_.getStringExtra(MainActivity.EXTRA_MESSAGE);// Converts the JSON String to an Object

        // Capture the layout's TextView and set the string as its text
        txtview = findViewById(R.id.txt2);
        txtview.setText(message);

    }


    private void onBtnClicked() {
        try {
            BluetoothAdapter myBTAdapter = BluetoothAdapter.getDefaultAdapter();
            if (myBTAdapter == null) {
                // Device doesn't support Bluetooth
                // Message a l'utilisateur pour lui dire d'activer BT
            }
            else {
                pairedDevices2 = myBTAdapter.getBondedDevices();

                if (pairedDevices2.size() > 0) {
                    // There are paired devices. Get the name and address of each paired device.
                    for (BluetoothDevice device : pairedDevices2) {
                        adapter2.add(device.getName());
                    }
                }
                else {
                    // There are no paired devices. We then try to discover available devices nearby
                }
            }

            BluetoothDevice device = getSelectedSpinnerItem();
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
            try {
                btSocket2 = device.createRfcommSocketToServiceRecord(uuid);
                if (!btSocket2.isConnected()){
                    btSocket2.connect();
                    //elseditText2.setText("Connexion réussie");

                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            OutputStream btOutputStream = btSocket2.getOutputStream();
            btOutputStream.write(editText2.getText().toString().getBytes());
            iStream2 = btSocket2.getInputStream();
            mmBuffer2 = new byte[1024];
            int numBytes; // bytes returned from read()



            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = iStream2.read(mmBuffer2);




                } catch (IOException e) {
                    e.printStackTrace();

                    break;
                }
                break;
            }




            //btSocket2.close();
            //changeView();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //creation intent



    }
    private BluetoothDevice getSelectedSpinnerItem() {
        for (BluetoothDevice device : pairedDevices2) {
            return device;
        }
        return null;
    }

}
