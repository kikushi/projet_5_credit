package proj.myapplication;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class Configuration extends AppCompatActivity {

    public static String inputPins;
    public static String outputPins;
    private int nbInputBit, nbOutputBit;

    //Communication
    private OutputStream btOutputStream;
    private InputStream btInputStream;
    private String received;

    //RadioButtons
    private int[] rbtnIDs;
    private int rbtn01ID;
    private boolean[] selectedPins;
    boolean[] PINIsAvailable;

    //EditText ConfigName
    private EditText configNameET;
    private String blockCharacterSet = "/<>|:?*.%'\\~#^$&!\";";
    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    //Arrays de pinNBs pour Load
    ArrayList<Integer> inBitPins= new ArrayList<>();
    ArrayList<Integer> outBitPins= new ArrayList<>();
    ArrayList<ArrayList<Integer>> inBytePins= new ArrayList<>();
    ArrayList<ArrayList<Integer>> outBytePins= new ArrayList<>();

    //ListViews
    //ConfigElements (Top)
    private ListView configElementsListView;
    private CustomAdapter configElementsAdapter;
    private ArrayList<PINConfig> configElementsList;
    private int index_selected_element;
    //ConfigNames (Bottom)
    private ListView configNamesListView;
    private ArrayAdapter<String> configNamesAdapter;
    private String selected_configname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Tag", "onCreate - Configuration");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        //Bluetooth
        try {
            btOutputStream = Connexion.btSocket.getOutputStream();
            btInputStream = Connexion.btSocket.getInputStream();
        } catch (IOException e) {
            Log.e("Tag", "socket's getOutputStream() method failed", e);
            ChangeView(Connexion.class);
        }

        //Variables initialisations
        inputPins ="2202020020000200200200002022020002000020";
        outputPins ="2202020020000200200200002022020002000020";
        nbInputBit = 0;
        nbOutputBit = 0;
        rbtnIDs = new int[40];
        selectedPins = new boolean[40];
        index_selected_element = -1;
        selected_configname = "";

        configElementsList= new ArrayList<>();
        configElementsAdapter = new CustomAdapter(this, configElementsList);

        configNamesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());

        //GPIO available vs non avalable on Rpi
        PINIsAvailable = new boolean[] {
                false,  //PIN#1
                false,  //PIN#2
                true,   //PIN#3
                false,  //PIN#4
                true,   //PIN#5
                false,  //PIN#6
                true,   //PIN#7
                true,   //PIN#8
                false,  //PIN#9
                true,   //PIN#10
                true,   //PIN#11
                true,   //PIN#12
                true,   //PIN#13
                false,  //PIN#14
                true,   //PIN#15
                true,   //PIN#16
                false,  //PIN#17
                true,   //PIN#18
                true,   //PIN#19
                false,  //PIN#20
                true,   //PIN#21
                true,   //PIN#22
                true,   //PIN#23
                true,   //PIN#24
                false,  //PIN#25
                true,   //PIN#26
                false,  //PIN#27
                false,  //PIN#28
                true,   //PIN#29
                false,  //PIN#30
                true,   //PIN#31
                true,   //PIN#32
                true,   //PIN#33
                false,  //PIN#34
                true,   //PIN#35
                true,   //PIN#36
                true,   //PIN#37
                true,   //PIN#38
                false,  //PIN#39
                true    //PIN#40
        };

        //Initialisation des ListViews
        configNamesListView = findViewById(R.id.listview_confignames);
        configNamesListView.setAdapter(configNamesAdapter);
        configNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selected_configname = configNamesAdapter.getItem(position);
                configNamesListView.setSelector(R.color.lime_green);
            }
        });
        configElementsListView = findViewById(R.id.listview_elements);
        configElementsListView.setAdapter(configElementsAdapter);
        configElementsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                index_selected_element = position;
                configElementsListView.setSelector(R.color.lime_green);
            }
        });

        //EditText Configname
        configNameET = findViewById(R.id.editText);
        configNameET.setFilters(new InputFilter[]{filter});

        //RadioButtons
        rbtn01ID = R.id.rbtn01;

        for (int i=0; i<40; i++) {
            rbtnIDs[i] = rbtn01ID + i;
            RadioButton rbtn = findViewById(rbtnIDs[i]);
            rbtn.setText(i+1);
            if (PINIsAvailable[i]) {
                rbtn.setOnClickListener(RadioBtnListener);
            }
        }

        //Button addBitIn
        Button btn_Add_bit_R = findViewById(R.id.btn_ajouter_bit_R);
        btn_Add_bit_R.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Btn_AddBit_Clicked(true);

            }
        });

        //Button addBitOut
        Button btn_Add_bit_W = findViewById(R.id.btn_ajouter_bit_W);
        btn_Add_bit_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Btn_AddBit_Clicked(false);
            }
        });

        //Button addByteIn
        Button btn_Add_byte_R = findViewById(R.id.btn_ajouter_byte_R);
        btn_Add_byte_R.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Btn_AddByte_Clicked(true);
            }
        });

        //Button addByteOut
        Button btn_Add_byte_W = findViewById(R.id.btn_ajouter_byte_W);
        btn_Add_byte_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Btn_AddByte_Clicked(false);
            }
        });

        //Button remove
        Button btn_remove = findViewById(R.id.btn_remove);
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Btn_Remove_Clicked();
            }
        });

        //Button refresh
        Button btn_refresh = findViewById(R.id.btn_refresh);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Btn_Refresh_Clicked();
            }
        });

        //Button load
        Button btn_load = findViewById(R.id.btn_load);
        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Load_Clicked();
            }
        });

        //Button delete
        Button btn_del = findViewById(R.id.btn_delete);
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Btn_Del_Clicked();
            }
        });

        //Button save
        Button btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Btn_Save_Clicked();
            }
        });

        //Button save and start
        Button btn_save_and_start = findViewById(R.id.btn_save_and_start);
        btn_save_and_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Btn_Save_And_Start_Clicked();
            }
        });
    }

    private View.OnClickListener RadioBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rbtnView = (RadioButton)v;
            int rbtnClickedId = rbtnView.getId();
            int rbtnClickedIndex = rbtnClickedId - rbtn01ID;

            if (selectedPins[rbtnClickedIndex]) {
                //Le rbtn est déja sélectionné
                //On déselectionne le rbtn
                rbtnView.setChecked(false);
                selectedPins[rbtnClickedIndex] = false;
            }
            else {
                //Le rbtn n'est pas sélectionné
                //On Sélectionne le rbtn
                rbtnView.setChecked(true);
                selectedPins[rbtnClickedIndex] = true;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_configuration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_comm_changeNip:
                Btn_ChangeNip_Clicked();
                return true;

            case R.id.menu_comm_disconnect:
                //disconnect
                SendStringCommand("disconnect");
                ChangeView(Connexion.class);
                return true;

            case R.id.menu_comm_close_app:
                SendStringCommand("disconnect");
                this.finishAffinity();
                return true;
        }
        return false;
    }

    private void Btn_AddBit_Clicked(boolean isInput) {
        int nbBit;
        int nbOfPins = 0;

        if (isInput) {
            nbBit = nbInputBit;
        }
        else {
            nbBit = nbOutputBit;
        }

        ArrayList<Integer> indexesOfSelectedPins = new ArrayList<>();
        for (int i=0; i<selectedPins.length; i++) {
            if (selectedPins[i]) {
                //Si la pin est sélectionnée
                indexesOfSelectedPins.add(i);
                nbOfPins++;
            }
        }
        //Verifier que le nombre de bits total <= 8

        if (nbBit+nbOfPins > 8) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "A maximum of 8 input pins and 8 output pins is allowed per configuration.\nYou already have "+ nbBit +" pins in your configuration.",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.END, 0, 0);
            toast.show();
        }
        else {
            //Deselectionne toutes les pins
            selectedPins = new boolean[40];

            for (int i=0; i<nbOfPins; i++) {
                //Pour chaque PIN a ajouter

                int pinNB = indexesOfSelectedPins.get(i)+1;

                //Add '1' in inputPins at position (PinNB-1)
                if (isInput) {
                    inputPins = inputPins.substring(0, pinNB-1)+'1'+ inputPins.substring(pinNB);
                    nbInputBit++;
                }
                else {
                    outputPins = outputPins.substring(0, pinNB-1)+'1'+ outputPins.substring(pinNB);
                    nbOutputBit++;
                }

                //Cree un objet PINConfig
                String text = "PIN # " + String.valueOf(pinNB);
                PINConfig pc = new PINConfig(false, isInput, 'P', text);
                pc.setPinNumber(pinNB);

                //Add l'objet créé au listview
                configElementsList.add(pc);
                configElementsAdapter.notifyDataSetChanged();

                //Modifie le rbtn de la liste de gauche
                RadioButton rbtnClicked = findViewById(rbtnIDs[pinNB-1]);
                rbtnClicked.setClickable(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rbtnClicked.setButtonTintList(ColorStateList.valueOf(Color.GRAY));
                }
            }
        }
    }

    private void Btn_AddByte_Clicked(boolean isInput){

        int nbOfPins = 0;
        ArrayList<Integer> indexesOfSelectedPins = new ArrayList<>();
        for (int i=0; i<selectedPins.length; i++) {
            if (selectedPins[i]) {
                //Si la pin est sélectionnée
                indexesOfSelectedPins.add(i);
                nbOfPins++;
            }
        }

        //Verifier que le nombre de bits selectionné est de 8
        if (nbOfPins != 8) {
            Toast toast = Toast.makeText(getApplicationContext(),"You must select 8 pins in order to add a byte",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.END, 0, 0);
            toast.show();
        }

        else {
            //Deselectionne toutes les pins
            selectedPins = new boolean[40];
            int [] pinNBs = new int[8];
            char letter;
            StringBuilder strBuilder = new StringBuilder("PINS # ");

            if (isInput) {
                //Determiner quelle lettre est disponible
                letter = GetFirstCharAvailable(inputPins);
                for (int i=0; i<8; i++) {
                    pinNBs[i] = indexesOfSelectedPins.get(i)+1;
                    //Add 'a', 'b' or 'c' in inputPins or outputPins at positions
                    inputPins = inputPins.substring(0, pinNBs[i]-1)+letter+ inputPins.substring(pinNBs[i]);
                    strBuilder.append(String.valueOf(pinNBs[i])).append('-');

                    //Modifie les rbtns de la liste de gauche
                    RadioButton rbtnClicked = findViewById(rbtnIDs[pinNBs[i]-1]);
                    rbtnClicked.setClickable(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rbtnClicked.setButtonTintList(ColorStateList.valueOf(Color.GRAY));
                    }
                }
            }
            else {
                //Determiner quelle lettre est disponible
                letter = GetFirstCharAvailable(outputPins);
                for (int i=0; i<8; i++) {
                    pinNBs[i] = indexesOfSelectedPins.get(i)+1;
                    //Add 'a', 'b' or 'c' in inputPins or outputPins at positions
                    outputPins = outputPins.substring(0, pinNBs[i]-1)+letter+ outputPins.substring(pinNBs[i]);
                    strBuilder.append(String.valueOf(pinNBs[i])).append('-');
                    //Modifie les rbtns de la liste de gauche
                    RadioButton rbtnClicked = findViewById(rbtnIDs[pinNBs[i]-1]);
                    rbtnClicked.setClickable(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rbtnClicked.setButtonTintList(ColorStateList.valueOf(Color.GRAY));
                    }
                }
            }
            //Remove last '-'
            String text = new String(strBuilder).substring(0, strBuilder.length()-1);
            //Cree un objet PINConfig
            PINConfig pc = new PINConfig(true, isInput, letter, text);
            pc.setPinNumbers(pinNBs);

            //Add l'objet créé au listview
            configElementsList.add(pc);
            configElementsAdapter.notifyDataSetChanged();
        }
    }

    private char GetFirstCharAvailable(String widget_string) {
        char letter = 'a';
        for (int i=0; i<3; i++) {
            if (widget_string.indexOf(letter) == -1) {
                return letter;
            }
            letter++;
        }
        return 'c';
    }

    private void Btn_Refresh_Clicked() {
        SendStringCommand("refreshConfigs");
        Log.i("Tag", "Received : "+ received);
        received = ReceiveStringMessage();

        if(received.equals("empty")) {
            Toast.makeText(getApplicationContext(), "There are no configurations saved!", Toast.LENGTH_SHORT).show();
        }
        else if(!received.equals("error")) {
            String splittedStr[]= received.split("\\r?\\n");

            configNamesAdapter.clear();
            for (String aSplittedStr : splittedStr) {
                if (!aSplittedStr.equals("null")) {
                    configNamesAdapter.add(aSplittedStr);
                    configNamesAdapter.notifyDataSetChanged();
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

    private void Btn_Del_Clicked() {
        if (!selected_configname.equals("")) {
            new AlertDialog.Builder(Configuration.this)
                    .setTitle("Warning !")
                    .setMessage("Voulez vous vraiment supprimer la configuration \""+selected_configname+"\" ?")
                    .setIcon(R.drawable.warning_icon)
                    .setPositiveButton(R.string.Yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //Remove l'element de la listview
                                    configNamesAdapter.remove(selected_configname);
                                    configNamesListView.setSelector(android.R.color.transparent);
                                    configNamesAdapter.notifyDataSetChanged();

                                    SendStringCommand("deleteConfig;"+ selected_configname +";");
                                    received = ReceiveStringMessage();

                                    if (received.equals("error")){
                                        Toast.makeText(getApplicationContext(), "An error has occured.", Toast.LENGTH_SHORT).show();
                                    }
                                    selected_configname = "";
                                }
                            })
                    .setNegativeButton(R.string.No,  null)
                    .show();
        }
    }

    private void Btn_Save_Clicked() {
        String configName = configNameET.getText().toString();
        if (configName.equals("")) {
            Toast.makeText(getApplicationContext(), "Error : You must pick a configuration name before saving it", Toast.LENGTH_LONG).show();
        }
        else if (blockCharacterSet.contains(("" + configName))) {
            Toast.makeText(getApplicationContext(), "Error : Configuration name contains prohibited characters", Toast.LENGTH_SHORT).show();
        }
        else {
            new AlertDialog.Builder(Configuration.this)
                    .setTitle("Warning !")
                    .setMessage("Saving will overwrite any existing configuration with the same name already on the Raspberry Pi\n\nConfiguration Name chosen : " + configName + "\n\nContinue ?")
                    .setIcon(R.drawable.warning_icon)
                    .setPositiveButton(R.string.Yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    SendStringCommand("saveConfig;" + configNameET.getText().toString() + ";" + inputPins + ";" + outputPins + ";");
                                    received = ReceiveStringMessage();
                                    if (received.equals("error")) {
                                        Toast.makeText(getApplicationContext(), "An error has occured.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                    .setNegativeButton(R.string.No,  null)
                    .show();
        }
    }

    private void Btn_Save_And_Start_Clicked() {

        String configName = configNameET.getText().toString();
        if (configName.equals("")) {
            Toast.makeText(getApplicationContext(), "Error : You must pick a configuration name before saving it", Toast.LENGTH_LONG).show();
        }
        else if (blockCharacterSet.contains(("" + configName))) {
            Toast.makeText(getApplicationContext(), "Error : Configuration name contains prohibited characters", Toast.LENGTH_SHORT).show();
        }
        else {
            new AlertDialog.Builder(Configuration.this)
                    .setTitle("Warning !")
                    .setMessage("Saving will overwrite any existing configuration with the same name already on the Raspberry Pi\n\nConfiguration Name chosen : " + configName + "\n\nContinue ?")
                    .setIcon(R.drawable.warning_icon)
                    .setPositiveButton(R.string.Yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    SendStringCommand("start;" + configNameET.getText().toString() + ";" + inputPins + ";" + outputPins + ";");
                                    received = ReceiveStringMessage();
                                    if (received.equals("error")) {
                                        Toast.makeText(getApplicationContext(), "An error has occured.", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        ChangeView(Communication.class);
                                    }
                                }
                            })
                    .setNegativeButton(R.string.No,  null)
                    .show();
        }
    }

    private void Btn_ChangeNip_Clicked() {
        int maximumChars = 8;
        final EditText edittext = new EditText(this);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        edittext.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maximumChars)});

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Enter your new NIP.\nYour NIP must have a maximum of 8 characters");
        alert.setTitle("Change NIP");
        alert.setView(edittext);
        alert.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
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
        });
        alert.setNegativeButton(R.string.Cancel, null);
        alert.show();
    }

    private void Btn_Remove_Clicked() {
        Remove(false, configElementsAdapter.get_element(index_selected_element));
    }

    private void Remove(boolean calledByLoad, PINConfig pinConfig) {
        if (pinConfig != null) {
            RadioButton rbtn;

            //Remove l'element de la listview
            if (!calledByLoad) {
                configElementsList.remove(index_selected_element);
            }
            index_selected_element = -1;
            configElementsListView.setSelector(android.R.color.transparent);
            configElementsAdapter.notifyDataSetChanged();

            if(!pinConfig.getIsByte()){
                //Bit
                //Remettre le rbtn disponible
                rbtn = findViewById(rbtnIDs[pinConfig.getPinNumber()-1]);
                rbtn.setClickable(true);
                rbtn.setChecked(false);
                if (pinConfig.getIsInput())
                    nbInputBit--;
                else
                    nbOutputBit--;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rbtn.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#248d51")));
                }
            }
            else {
                //Byte
                //Remettre les rbtns disponibles
                for (int i=0; i<8;i++) {
                    rbtn = findViewById(rbtnIDs[pinConfig.getPinNumbers(i)-1]);
                    rbtn.setClickable(true);
                    rbtn.setChecked(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rbtn.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#248d51")));
                    }
                }
            }
        }
    }

    private void btn_Load_Clicked() {
        if (selected_configname.equals("")) {
            Toast.makeText(getApplicationContext(), "You must select a configuration before loading it", Toast.LENGTH_SHORT).show();
        }
        else {
            //Remove les elements de la listview
            for (Iterator<PINConfig> it = configElementsList.iterator(); it.hasNext();) {
                PINConfig item = it.next();
                Remove(true, item);
                it.remove();
            }
            //Remove les boutons déjà checked
            for (int i=0; i<40; i++) {
                if (selectedPins[i]) {
                    //Le rbtn est sélectionné
                    //On déselectionne le rbtn
                    ((RadioButton)findViewById(rbtnIDs[i])).setChecked(false);
                    selectedPins[i] = false;
                }
            }
            //Set le edittext avec le configname
            configNameET.setText(selected_configname);

            //Load la config
            inBitPins= new ArrayList<>();
            outBitPins= new ArrayList<>();
            inBytePins= new ArrayList<>();
            outBytePins= new ArrayList<>();

            SendStringCommand("loadConfig;" + selected_configname + ";");

            received = ReceiveStringMessage();
            String[] splittedStr = received.split(";",2);
            inputPins = splittedStr[0];
            outputPins = splittedStr[1];

            if(inputPins.equals("error")|| outputPins.equals("error")) {
                Toast.makeText(getApplicationContext(), "An error has occured.", Toast.LENGTH_SHORT).show();
            }
            else {
                InitArraysWithConfig(new String[]{inputPins, outputPins});
                AddBitsWithLoad();
                AddBytesWithLoad();
            }
        }
    }

    public void AddBitsWithLoad(){
        for (Integer i : inBitPins) {
            //Cree un objet PINConfig
            String text = "PIN # " + String.valueOf(i);
            PINConfig pi = new PINConfig(false, true, 'P', text);
            pi.setPinNumber(i);

            //Add l'objet créé au listview
            configElementsList.add(pi);
            configElementsAdapter.notifyDataSetChanged();

            //Modifie le rbtn de la liste de gauche
            RadioButton rbtnClicked = findViewById(rbtnIDs[i-1]);
            rbtnClicked.setClickable(false);
            rbtnClicked.setChecked(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rbtnClicked.setButtonTintList(ColorStateList.valueOf(Color.GRAY));
            }

        }
        for (Integer i : outBitPins) {
            //Cree un objet PINConfig
            String text = "PIN # " + String.valueOf(i);
            PINConfig pi = new PINConfig(false, false, 'P', text);
            pi.setPinNumber(i);

            //Add l'objet créé au listview
            configElementsList.add(pi);
            configElementsAdapter.notifyDataSetChanged();

            //Modifie le rbtn de la liste de gauche
            RadioButton rbtnClicked = findViewById(rbtnIDs[i-1]);
            rbtnClicked.setClickable(false);
            rbtnClicked.setChecked(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rbtnClicked.setButtonTintList(ColorStateList.valueOf(Color.GRAY));
            }
        }
    }

    public void AddBytesWithLoad(){

        StringBuilder strBuilder = new StringBuilder("PINS # ");

        for(ArrayList<Integer> byteGroup:inBytePins){
            int [] PinNumbers = new int[8];
            for (int i=0; i<8; i++) {
                //
                PinNumbers[i] = byteGroup.get(i);

                //Add 'a', 'b' or 'c' in inputPins or outputPins at positions
                strBuilder.append(String.valueOf(byteGroup.get(i))).append('-');

                //Modifie les rbtns de la liste de gauche
                RadioButton rbtnClicked = findViewById(rbtnIDs[byteGroup.get(i)-1]);
                rbtnClicked.setClickable(false);
                rbtnClicked.setChecked(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rbtnClicked.setButtonTintList(ColorStateList.valueOf(Color.GRAY));
                }
            }
            //Remove last '-'
            String text = new String(strBuilder).substring(0, strBuilder.length()-1);
            //Cree un objet PINConfig
            PINConfig pc = new PINConfig(true, true,'G', text);
            pc.setPinNumbers(PinNumbers);

            //Add l'objet créé au listview
            configElementsList.add(pc);
            configElementsAdapter.notifyDataSetChanged();
        }
        strBuilder = new StringBuilder("PINS # ");

        for(ArrayList<Integer> byteGroup:outBytePins){
            int [] PinNumbers = new int[8];
            for (int i=0; i<8; i++) {
                //
                PinNumbers[i] = byteGroup.get(i);

                //Add 'a', 'b' or 'c' in inputPins or outputPins at positions
                strBuilder.append(String.valueOf(byteGroup.get(i))).append('-');

                //Modifie les rbtns de la liste de gauche
                RadioButton rbtnClicked = findViewById(rbtnIDs[byteGroup.get(i)-1]);
                rbtnClicked.setClickable(false);
                rbtnClicked.setChecked(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rbtnClicked.setButtonTintList(ColorStateList.valueOf(Color.GRAY));
                }
            }
            //Remove last '-'
            String text = new String(strBuilder).substring(0, strBuilder.length()-1);
            //Cree un objet PINConfig
            PINConfig pc = new PINConfig(true, false,'G', text);
            pc.setPinNumbers(PinNumbers);

            //Add l'objet créé au listview
            configElementsList.add(pc);
            configElementsAdapter.notifyDataSetChanged();
        }
    }

    private void InitArraysWithConfig(String[] inputsOutputs) {

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

    private void SendStringCommand(String command){
        try {
            btOutputStream.write(command.getBytes());
        }
        catch (IOException e) {
            Log.e("Tag", "Writing failed. Tried to write : " + command);
        }
    }

    private String ReceiveStringMessage() {
        byte [] buffer = new byte[1024];
        try {
            btInputStream.read(buffer);
        } catch (IOException e) {
            Log.e("Tag", "Reading failed");
        }
        String b = new String(buffer);
        return b.substring(0,b.indexOf(0));
    }

    public void ChangeView(Class activity) {
        Intent intent = new Intent(getApplicationContext(), activity);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        SendStringCommand("disconnect");
        ChangeView(Connexion.class);
    }

}
