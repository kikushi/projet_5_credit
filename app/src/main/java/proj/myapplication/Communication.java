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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Communication extends AppCompatActivity {

    private Switch ledSwitch;

    private EditText editText;
    private Button sendBtn;
    private BluetoothSocket btSocket = null;
    private Button ajouterbtn;
    private Button supprimerBtn;
    private Button Deconnecter;

    private IntentFilter filter_aclDisconnected;
    private TextView rponse_RPI;
    public byte[] mmbuffer;
    public InputStream istream;
    public OutputStream oStream;
    private String m = "on";
    private String mn = "off";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);


        editText = (EditText)findViewById(R.id.editText_msg);

        sendBtn = (Button)findViewById(R.id.button_send);
        sendBtn.setOnClickListener(sendBtnListener);

        ajouterbtn = (Button)findViewById(R.id.buttonajouter);
        ajouterbtn.setOnClickListener(ajouterBtnListener);
        ajouterbtn.isShown();


        supprimerBtn = (Button)findViewById(R.id.buttonsupprimer);
        supprimerBtn.setOnClickListener(supprimerBtnListener);

        Deconnecter = (Button)findViewById(R.id.buttondeconnecter);
        Deconnecter.setOnClickListener(deconnecterBtnListener);


        filter_aclDisconnected = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(mReceiverActionAclDisconnected, filter_aclDisconnected);

        rponse_RPI = (TextView)findViewById(R.id.RPI);
        ledSwitch = (Switch)findViewById(R.id.switch1);
        ledSwitch.setChecked(false);
        ledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    sendMessage(m);
                } else {
                    sendMessage(mn);

                }
            }
        });


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
    private View.OnClickListener ajouterBtnListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            ajouterBtnClicked();
        }

    };
    private View.OnClickListener supprimerBtnListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            SupprimerBtnCliked();

        }

    };
    private View.OnClickListener deconnecterBtnListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            DcnBtnCliked();
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
            ReponseFromRpi();
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

    private void ajouterBtnClicked(){
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context," Ajout de Widget",Toast.LENGTH_SHORT);
        toast.show();
        Connexion.nbwidgt+=1;
        ledSwitch.setVisibility(View.VISIBLE);


    }
    private void SupprimerBtnCliked(){
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context," Suppression de Widget",Toast.LENGTH_SHORT);
        toast.show();
        Connexion.nbwidgt = Connexion.nbwidgt -1;
        ledSwitch.setVisibility(View.GONE);



    }
    private void DcnBtnCliked(){
        try{
            btSocket.close();
            btSocket = null;
            Connexion.nbwidgt= Connexion.nbwidgt -1;
            ChangeView2();


        }
        catch (IOException e) {
            e.printStackTrace();

        }

    }
    private void ChangeView2() {
        Context context = getApplicationContext();

        Intent intent = new Intent(context, Connexion.class);
        if (intent != null) {
            startActivity(intent);
        }
    }
    private void ReponseFromRpi(){
        try{

            istream = btSocket.getInputStream();
            mmbuffer = new byte[1024];
            int numbytes;
            //keep listenning to the Input stream until an exeception error
            while(true){
                try{
                    numbytes = istream.read(mmbuffer);
                }catch (IOException e){
                    e.printStackTrace();
                    break;
                }
                break;

            }

           //btSocket.close();

        }
        catch (IOException e){
            e.printStackTrace();

        }
        String s = new String(mmbuffer);
        rponse_RPI.setText(s);



    }
    private void sendMessage(String message){
        try{
            OutputStream btOutputStream = btSocket.getOutputStream();
            btOutputStream.write(message.toString().getBytes());
            ReponseFromRpi();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
