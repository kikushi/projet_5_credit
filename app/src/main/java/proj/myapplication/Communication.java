package proj.myapplication;

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
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.OutputStream;

public class Communication extends AppCompatActivity {

    private EditText editText;
    private Button sendBtn;
    private Button disconnectBtn;
    private BluetoothSocket btSocket = null;

    private IntentFilter filter_aclDisconnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);

        editText = (EditText)findViewById(R.id.editText_msg);

        sendBtn = (Button)findViewById(R.id.button_send);
        sendBtn.setOnClickListener(sendBtnListener);

        disconnectBtn = (Button)findViewById(R.id.button_disconnect);
        disconnectBtn.setOnClickListener(disconnectBtnListener);

        filter_aclDisconnected = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(mReceiverActionAclDisconnected, filter_aclDisconnected);

        btSocket = Connexion.btSocket;
    }
    @Override
    public void onBackPressed() {
        ChangeView();
    }

    private View.OnClickListener sendBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SendBtnClicked();
        }
    };

    private View.OnClickListener disconnectBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ChangeView();
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
                if (btSocket != null) {
                    try {
                        btSocket.close();
                        btSocket = null;
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //Retour a la page de connexion
                ChangeView();
            }
        }
    };

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

    private void ChangeView() {
        Context context = getApplicationContext();
        if (btSocket != null) {
            try {
                btSocket.close();
                btSocket = null;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(context, Connexion.class);
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiverActionAclDisconnected);
    }

}
