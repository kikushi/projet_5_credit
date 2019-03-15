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
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Communication extends AppCompatActivity {

    private EditText editText;
    private Button sendBtn;
    private Button readFileBtn;
    private Button writeFileBtn;
    private TextView textFileContent;
    private BluetoothSocket btSocket = null;

    public static String fileContent;

    private IntentFilter filter_aclDisconnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);

        readFileBtn = (Button)findViewById(R.id.button_readFile);
        writeFileBtn = (Button)findViewById(R.id.button_writeFile);

        textFileContent = (TextView)findViewById(R.id.text_fileContent);



        filter_aclDisconnected = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(mReceiverActionAclDisconnected, filter_aclDisconnected);

        btSocket = Connexion.btSocket;
    }
    @Override
    public void onBackPressed() {
        ChangeView(Connexion.class);
    }

    private View.OnClickListener sendBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SendBtnClicked();
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
                ChangeView(Connexion.class);
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

    private void ChangeView(Class activity) {
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
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    public void readFile(View v) throws IOException{
        String value = "";
        FileInputStream inputStream = openFileInput("myfile.txt");
        StringBuilder Stringb = new StringBuilder();
        int content;
        while((content = inputStream.read())!=-1){
            value = String.valueOf(Stringb.append((char)content));
        }
        inputStream.close();
        textFileContent.setText(value);
    }

    public void writeFile(View v) {

        String filename = "myfile.txt";
        String fileContents = "000001111100000111112222222222";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiverActionAclDisconnected);
    }

}
