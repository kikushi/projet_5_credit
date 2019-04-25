package proj.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Communication extends AppCompatActivity {

    private OutputStream btOutputStream;
    private InputStream btInputStream;
    private String[] inputsOutputs;
    private ArrayList<Integer> inBitPins;
    private ArrayList<Integer> outBitPins;
    private ArrayList<ArrayList<Integer>> inBytePins;
    private ArrayList<ArrayList<Integer>> outBytePins;

    private String received;

    private Button showHideHintBtn;
    private boolean hintVisible;
    private ArrayList<Switch> inBitsSw;
    private int inBitsTvId;
    private int inBitsSwId;
    private ArrayList<TextView> inBytesValTv;
    private int inBytesTvId;
    private int inBytesValTvId;

    private int outBitsTvId;
    private int outBitsSwId;
    private ArrayList<EditText> outBytesEt;
    private int outBytesTvId;
    private int outBytesEtId;
    private int outBytesBtnId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Tag", "onCreate - Communication");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);

        //Bluetooth
        try {
            btOutputStream = Connexion.btSocket.getOutputStream();
            btInputStream = Connexion.btSocket.getInputStream();
        } catch (IOException e) {
            Log.e("Tag", "socket's getOutputStream() method failed", e);
            Intent intent = new Intent(this, Connexion.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }

        //Saving ID of first of each widget for future use
        inBitsTvId = R.id.tv_pinnbI0;
        inBitsSwId = R.id.sw_I0;
        inBytesTvId = R.id.tv_ByteI0;
        inBytesValTvId = R.id.tv_ByteValI0;
        outBitsTvId = R.id.tv_pinnbO0;
        outBitsSwId = R.id.sw_O0;
        outBytesTvId = R.id.tv_ByteO0;
        outBytesEtId = R.id.et_ByteO0;
        outBytesBtnId = R.id.btn_ByteO0;

        //Inputs
        Button updateInputsBtn = findViewById(R.id.btn_UpdateAllInputs);
        updateInputsBtn.setOnClickListener(UpdateAllInputsBtnListener);

        showHideHintBtn = findViewById(R.id.btn_ShowHideHint);
        showHideHintBtn.setOnClickListener(ShowHideHintBtnListener);
        hintVisible = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Tag", "onStart - Communication");

        //Init config Arrays
        //Initiate inputs and outputs array
        inputsOutputs = new String[] {
                Configuration.inputPins, //Inputs
                Configuration.outputPins  //Outputs
        };
        //2202020020000200200200002022020002000020

        //Pin numbers for each widget initiation
        inBitPins = new ArrayList<>(0);
        outBitPins = new ArrayList<>(0);
        inBytePins = new ArrayList<>(0);
        outBytePins = new ArrayList<>(0);

        //Input widgets initiation
        //Bits
        ArrayList<TextView> inBitsTv = new ArrayList<>(0);
        inBitsSw = new ArrayList<>(0);
        //Bytes
        ArrayList<TextView> inBytesTv = new ArrayList<>(0);
        inBytesValTv = new ArrayList<>(0);

        //Output widgets initiation
        //Outputs
        //Bit
        ArrayList<TextView> outBitsTv = new ArrayList<>(0);
        ArrayList<Switch> outBitsSw = new ArrayList<>(0);
        //Bytes
        ArrayList<TextView> outBytesTv = new ArrayList<>(0);
        outBytesEt = new ArrayList<>(0);
        ArrayList<Button> outBytesBtn = new ArrayList<>(0);

        init();

        //Input Bit Widgets
        for (int i = 0; i<inBitPins.size();i++) {
            //TextView
            inBitsTv.add((TextView) findViewById(inBitsTvId+i));
            inBitsTv.get(i).setVisibility(View.VISIBLE);
            inBitsTv.get(i).setText("PIN #"+inBitPins.get(i).toString()+" : ");
            //Switch
            inBitsSw.add((Switch) findViewById(inBitsSwId+i));
            inBitsSw.get(i).setVisibility(View.VISIBLE);
        }
        //Input Byte Widgets
        for (int i = 0; i<inBytePins.size();i++) {
            //TextView
            inBytesTv.add((TextView) findViewById(inBytesTvId+i));
            inBytesTv.get(i).setVisibility(View.VISIBLE);
            inBytesTv.get(i).setOnClickListener(InBytesTvListener);
            //TextView
            inBytesValTv.add((TextView) findViewById(inBytesValTvId+i));
            inBytesValTv.get(i).setVisibility(View.VISIBLE);

        }
        //Output Bit Widgets
        for (int i = 0; i<outBitPins.size();i++) {
            //TextView
            outBitsTv.add((TextView) findViewById(outBitsTvId+i));
            outBitsTv.get(i).setVisibility(View.VISIBLE);
            outBitsTv.get(i).setText("PIN #"+outBitPins.get(i).toString()+" : ");
            //Switch
            outBitsSw.add((Switch) findViewById(outBitsSwId+i));
            outBitsSw.get(i).setVisibility(View.VISIBLE);
            outBitsSw.get(i).setOnCheckedChangeListener(OutBitsSwListener);
        }

        //Output Byte Widgets
        for (int i = 0; i<outBytePins.size();i++) {
            //TextView
            outBytesTv.add((TextView) findViewById(outBytesTvId+i));
            outBytesTv.get(i).setVisibility(View.VISIBLE);
            outBytesTv.get(i).setOnClickListener(OutBytesTvListener);
            //EditText
            outBytesEt.add((EditText) findViewById(outBytesEtId+i));
            outBytesEt.get(i).setVisibility(View.VISIBLE);
            //Button
            outBytesBtn.add((Button) findViewById(outBytesBtnId+i));
            outBytesBtn.get(i).setVisibility(View.VISIBLE);
            outBytesBtn.get(i).setOnClickListener(OutBytesBtnListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_communication, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_comm_changeConfig:
                finish();
                return true;

            case R.id.menu_comm_changeNip:
                Btn_ChangeNip_Clicked();
                return true;

            case R.id.menu_comm_disconnect:
                SendStringCommand("disconnect");
                Intent intent = new Intent(this, Connexion.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;

            case R.id.menu_comm_close_app:
                SendStringCommand("disconnect");
                finishAffinity();
                return true;
        }
        return false;
    }

    private View.OnClickListener UpdateAllInputsBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            StringBuilder strBuilder = new StringBuilder();
            int numberOfBitElements = 0;
            int numberOfByteElements = 0;

         //Assemble every PIN number in a string
            //Bits
            for (int pin : inBitPins) {
                //Transform to string, and add a 0 in front if only one character
                strBuilder.append(Add0IfOneCharacter(Integer.toString(pin)));
                numberOfBitElements ++;
            }
            //Bytes
            for (ArrayList<Integer> byteGroup : inBytePins) {
                strBuilder.append(GetBytePinsToString(byteGroup));
                numberOfByteElements ++;
            }

            if (strBuilder.length() == 0) {
                Toast.makeText(getApplicationContext(), "You don't have any input PINS !", Toast.LENGTH_SHORT).show();
            }
            else {
         //Send the pinNBs to get the data from and get the data in "data"
                 String data = UpdateInputs(strBuilder.toString());
                 if (data.equals("error")) {
                     Toast.makeText(getApplicationContext(), "Reading ", Toast.LENGTH_SHORT).show();
                 }
                 else {
                     //Apply received data to widgets
                     //Bits
                     for (int i=0; i<numberOfBitElements; i++) {
                         inBitsSw.get(i).setChecked(data.charAt(i) == '1');
                     }
                     //Bytes
                     for (int i=0; i<numberOfByteElements; i++) {
                         //Get substring of 8 binary numbers
                         String subString = data.substring(8*i +numberOfBitElements, 8*i + numberOfBitElements + 8);
                         //Convert substring in decimal
                         int decimalValue = Integer.parseInt(subString, 2);
                         //Set text of TextView to decimal value that was read
                         inBytesValTv.get(i).setText(String.valueOf(decimalValue));
                     }
                 }

            }
        }
    };

    private String GetBytePinsToString(ArrayList<Integer> pinNBs) {
        StringBuilder strBuilder = new StringBuilder();
        String pinNB;
        for (int i=0; i<8; i++) {
            pinNB = Add0IfOneCharacter(pinNBs.get(i).toString());
            strBuilder.append(pinNB);
        }
        return strBuilder.toString();
    }

    private String Add0IfOneCharacter(String myString) {
        if (myString.length() == 1) {
            return "0" + myString;
        }
        else {
            return myString;
        }
    }

    private String UpdateInputs(String pinNBs) {
        SendStringCommand("updateInputs;" + pinNBs);
        received = ReceiveStringMessage();
        return received;
    }

    private View.OnClickListener ShowHideHintBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (hintVisible) {
                findViewById(R.id.Hint_Layout).setVisibility(View.GONE);
                showHideHintBtn.setText(R.string.show_gpio_hint);
                hintVisible = false;
            }
            else {
                findViewById(R.id.Hint_Layout).setVisibility(View.VISIBLE);
                showHideHintBtn.setText(R.string.hide_gpio_hint);
                hintVisible = true;
            }
        }
    };

    private View.OnClickListener InBytesTvListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int textViewClickedNumber = v.getId() - inBytesTvId;
            String toastText = GetBytePinsForToastDisplay(inBytePins.get(textViewClickedNumber));
            Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
        }
    };

    private CompoundButton.OnCheckedChangeListener OutBitsSwListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton btnView, boolean isChecked) {
            int switchClickedNumber = btnView.getId() - outBitsSwId;
            int pinNB = outBitPins.get(switchClickedNumber);
            int data = isChecked ? 1 : 0;

            WriteBit(pinNB, data);
        }
    };

    private void WriteBit(int pinNB, int data) {
        SendStringCommand("writeBit;" + String.valueOf(pinNB) + ';' + String.valueOf(data) + ';');
        received = ReceiveStringMessage();
    }

    private View.OnClickListener OutBytesTvListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int textViewClickedNumber = v.getId() - outBytesTvId;
            String toastText = GetBytePinsForToastDisplay(outBytePins.get(textViewClickedNumber));
            Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
        }
    };

    private View.OnClickListener OutBytesBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int buttonClickedNumber = v.getId() - outBytesBtnId;
            String text = outBytesEt.get(buttonClickedNumber).getText().toString();
            if (!text.equals("")) {
                int data = Integer.valueOf(text);
                if (data >= 0 && data <= 255) {
                    ArrayList<Integer> pinNBs = outBytePins.get(buttonClickedNumber);
                    String binaryData = Integer.toBinaryString(data);

                    WriteByte(GetBytePinsToString(pinNBs), AddZerosUntilEightBits(binaryData));
                }
                else {
                    Toast.makeText(getApplicationContext(), "Data to be sent must be in between 0 and 255", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "There's nothing to send !", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private String AddZerosUntilEightBits(String myString) {
        int nbBits = myString.length();
        if (nbBits < 8) {
            return ("00000000" + myString).substring(nbBits);
        }
        else {
            return myString;
        }
    }

    private void WriteByte(String pinNBs, String data) {
        SendStringCommand("writeByte;" + pinNBs + ';' + data + ';');
        received = ReceiveStringMessage();
    }



    private String GetBytePinsForToastDisplay(ArrayList<Integer> pinNBs) {
        StringBuilder strBuilder = new StringBuilder("PINS #[");
        for (int i=0; i<8; i++) {
            strBuilder.append(pinNBs.get(i).toString());
            if (i==7) {
                strBuilder.append("]");
            }
            else {
                strBuilder.append(",");
            }
        }
        return strBuilder.toString();
    }

    private void init() {
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                //Inputs

                //Bits
                inBitPins = GetCharPositions(inputsOutputs[i], '1');

                //Bytes
                if (inputsOutputs[i].contains("a")) {
                    inBytePins.add(GetCharPositions(inputsOutputs[i], 'a'));
                }
                if (inputsOutputs[i].contains("b")) {
                    inBytePins.add(GetCharPositions(inputsOutputs[i], 'b'));
                }
                if (inputsOutputs[i].contains("c")) {
                    inBytePins.add(GetCharPositions(inputsOutputs[i], 'c'));
                }
            }
            else {
                //Outputs

                //Bits
                outBitPins = GetCharPositions(inputsOutputs[i], '1');

                //Bytes
                if (inputsOutputs[i].contains("a")) {
                    outBytePins.add(GetCharPositions(inputsOutputs[i], 'a'));
                }
                if (inputsOutputs[i].contains("b")) {
                    outBytePins.add(GetCharPositions(inputsOutputs[i], 'b'));
                }
                if (inputsOutputs[i].contains("c")) {
                    outBytePins.add(GetCharPositions(inputsOutputs[i], 'c'));
                }
            }
        }
    }

    private ArrayList<Integer> GetCharPositions(String myString, char myChar) {
        ArrayList<Integer> tmpPinList = new ArrayList<>(0);
        int pinNB = 1;

        for (char ch : myString.toCharArray()) {
            if (ch == myChar) {
                tmpPinList.add(pinNB);
            }
            pinNB++;
        }
        return tmpPinList;
    }

    private void Btn_ChangeNip_Clicked() {
        int maximumChars = 8;
        final EditText edittext = new EditText(this);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        edittext.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maximumChars)});

        AlertDialog alertdialog = new AlertDialog.Builder(this)
                .setMessage("Enter your new NIP.\nYour NIP must have a maximum of 8 characters")
                .setTitle("Change NIP")
                .setView(edittext)
                .setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String NIP = edittext.getText().toString();
                        if (!NIP.equals("")) {
                            SendStringCommand("changeNIP;" + NIP + ";");
                            received = ReceiveStringMessage();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "You must enter a NIP before pressing OK.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.Cancel, null)
                .show();
        alertdialog.setCanceledOnTouchOutside(false);
    }

    private void SendStringCommand(String mot){
        try {
            btOutputStream.write(mot.getBytes());

        }
        catch (IOException e) {
            Log.e("Tag", "Writing failed. Tried to write : "+ mot);
        }
    }

    private String ReceiveStringMessage() {
        byte [] mmBuffer3 = new byte[1024];
        try {
            btInputStream.read(mmBuffer3);
        } catch (IOException e) {
            Log.e("Tag", "Reading failed");
        }
        String b = new String(mmBuffer3);
        return b.substring(0,b.indexOf(0));

    }
}
