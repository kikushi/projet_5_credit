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
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
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

import static proj.myapplication.Connexion.btSocket;

public class Configuration extends AppCompatActivity {
    //declaration des commandes

    private OutputStream btOutputStream;
    private InputStream btInputStream;
    private Button btn_Add_bit_R;
    private Button btn_Add_byte_R;
    private Button btn_Add_bit_W;
    private Button btn_Add_byte_W;
    private Button btn_remove;
    private Button btn_refresh;
    private Button btn_load;
    private Button btn_save;
    private Button btn_del;
    private Button btn_save_and_start;

    private String config_name;
    private String received;
    private String last_pin_added;
    private String last_byte_added;

    private ArrayList<PINConfig> PINConfig_elements;

    //declaration des buttons
    private int[] rbtnIDs;

    private RadioButton rbtn3;
    private RadioButton rbtn5;
    private RadioButton rbtn7;
    private RadioButton rbtn8;
    private RadioButton rbtn10;
    private RadioButton rbtn11;
    private RadioButton rbtn12;
    private RadioButton rbtn13;
    private RadioButton rbtn15;
    private RadioButton rbtn16;
    private RadioButton rbtn18;
    private RadioButton rbtn19;
    private RadioButton rbtn21;
    private RadioButton rbtn22;
    private RadioButton rbtn23;
    private RadioButton rbtn24;
    private RadioButton rbtn26;
    private RadioButton rbtn29;
    private RadioButton rbtn31;
    private RadioButton rbtn32;
    private RadioButton rbtn33;
    private RadioButton rbtn35;
    private RadioButton rbtn36;
    private RadioButton rbtn37;
    private RadioButton rbtn38;
    private RadioButton rbtn40;


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

    private String widget_s="3303030030000300300300003033030003000030i";
    private boolean[] selectedPins;
    private String pin_config ="P-";
    private String pin_config2="";
    private PINConfig selectedElement;
    private String selected;
    boolean ok;

    ArrayList<Integer> inBitPins= new ArrayList<>();
    ArrayList<Integer> outBitPins= new ArrayList<>();
    ArrayList<ArrayList<Integer>> inBytePins= new ArrayList<>();
    ArrayList<ArrayList<Integer>> outBytePins= new ArrayList<>();




    ListView configNamesListView;
    public ListView configElementsListView;
    ArrayList<String> Target = new ArrayList<String>();
    ArrayList<String> Target2 = new ArrayList<>();
    RadioButton[] btnWord = new RadioButton[40];
    ArrayList<Button> BtnWord = new ArrayList<Button>();
    PINConfig[] btnWorld2 = new PINConfig[40];
    public byte[] mmBuffer3;
    private String pin_infos[] = new String[40];

    public int index_selected_element;
    public String selected_configname;

    public static String widget_input;
    public static String widget_output;
    public int nbInputBit, nbInputByte, nbOutputBit, nbOutputByte;
    ArrayList<PINConfig> configElementsList;
    boolean[] PINIsAvailable;




    //ArrayAdapter
    public CustomAdapter configElementsAdapter;
    public ArrayAdapter<String> configNamesAdapter;


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

        widget_input="2202020020000200200200002022020002000020";
        widget_output ="2202020020000200200200002022020002000020";
        nbInputBit = 0;
        nbInputByte = 0;
        nbOutputBit = 0;
        nbOutputByte = 0;

        rbtnIDs = new int[40];
        selectedPins = new boolean[40];

        index_selected_element = -1;
        selected_configname = "";

        configElementsList= new ArrayList<>();
        configElementsAdapter = new CustomAdapter(this, configElementsList);

        configNamesAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Target2);

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
                true,   //PIN#40
        };


        //déclaration des listViews
        configNamesListView = (ListView)findViewById(R.id.listview_confignames);
        configNamesListView.setAdapter(configNamesAdapter);
        configNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selected_configname = configNamesAdapter.getItem(position);
                configNamesListView.setSelector(R.color.lime_green);
            }
        });
        configElementsListView =(ListView)findViewById(R.id.listview_elements);
        configElementsListView.setAdapter(configElementsAdapter);
        configElementsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                index_selected_element = position;
                configElementsListView.setSelector(R.color.lime_green);
                //selected = configElementsListView.getItemAtPosition(position).toString();
            }
        });

        // button load
        btn_load = (Button)findViewById(R.id.btn_load);
        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Load_Clicked();
            }
        });

        // button save and start
        btn_save_and_start = (Button)findViewById(R.id.btn_save_and_start);
        btn_save_and_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Save_And_Start_Clicked();
            }
        });

        // button save
        btn_save = (Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Save_Clicked();
            }
        });

        // button delete
        btn_del = (Button)findViewById(R.id.btn_delete);
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Del_Clicked();
            }
        });

        // button addbitIn
        btn_Add_bit_R = (Button)findViewById(R.id.btn_ajouter_bit_R);
        btn_Add_bit_R.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_AddBit_Clicked(true);

            }
        });

        // button addBitOut
        btn_Add_bit_W = (Button)findViewById(R.id.btn_ajouter_bit_W);
        btn_Add_bit_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_AddBit_Clicked(false);
            }
        });

        // button addByteIn
        btn_Add_byte_R = (Button)findViewById(R.id.btn_ajouter_byte_R);
        btn_Add_byte_R.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_AddByte_Clicked(true);
            }
        });

        // button addByteOut
        btn_Add_byte_W = (Button)findViewById(R.id.btn_ajouter_byte_W);
        btn_Add_byte_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_AddByte_Clicked(false);
            }
        });

        // EditText Configname


        configNameET = (EditText)findViewById(R.id.editText);
        configNameET.setFilters(new InputFilter[]{filter});

        // button refresh
        btn_refresh = (Button)findViewById(R.id.btn_refresh);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Refresh_Clicked();
            }
        });

        // button remove
        btn_remove= (Button)findViewById(R.id.btn_remove);
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_remove_clicked();
            }
        });

        //ajout des radiobutton
        rbtnIDs[0] = R.id.rbtn_1;
        rbtnIDs[1] = R.id.rbtn_2;
        rbtnIDs[2] = R.id.rbtn_3;
        rbtnIDs[3] = R.id.rbtn_4;
        rbtnIDs[4] = R.id.rbtn_5;
        rbtnIDs[5] = R.id.rbtn_6;
        rbtnIDs[6] = R.id.rbtn_7;
        rbtnIDs[7] = R.id.rbtn_8;
        rbtnIDs[8] = R.id.rbtn_9;
        rbtnIDs[9] = R.id.rbtn_10;
        rbtnIDs[10] = R.id.rbtn_11;
        rbtnIDs[11] = R.id.rbtn_12;
        rbtnIDs[12] = R.id.rbtn_13;
        rbtnIDs[13] = R.id.rbtn_14;
        rbtnIDs[14] = R.id.rbtn_15;
        rbtnIDs[15] = R.id.rbtn_16;
        rbtnIDs[16] = R.id.rbtn_17;
        rbtnIDs[17] = R.id.rbtn_18;
        rbtnIDs[18] = R.id.rbtn_19;
        rbtnIDs[19] = R.id.rbtn_20;
        rbtnIDs[20] = R.id.rbtn_21;
        rbtnIDs[21] = R.id.rbtn_22;
        rbtnIDs[22] = R.id.rbtn_23;
        rbtnIDs[23] = R.id.rbtn_24;
        rbtnIDs[24] = R.id.rbtn_25;
        rbtnIDs[25] = R.id.rbtn_26;
        rbtnIDs[26] = R.id.rbtn_27;
        rbtnIDs[27] = R.id.rbtn_28;
        rbtnIDs[28] = R.id.rbtn_29;
        rbtnIDs[29] = R.id.rbtn_30;
        rbtnIDs[30] = R.id.rbtn_31;
        rbtnIDs[31] = R.id.rbtn_32;
        rbtnIDs[32] = R.id.rbtn_33;
        rbtnIDs[33] = R.id.rbtn_34;
        rbtnIDs[34] = R.id.rbtn_35;
        rbtnIDs[35] = R.id.rbtn_36;
        rbtnIDs[36] = R.id.rbtn_37;
        rbtnIDs[37] = R.id.rbtn_38;
        rbtnIDs[38] = R.id.rbtn_39;
        rbtnIDs[39] = R.id.rbtn_40;

        for (int i=0; i<40; i++) {
            //rbtnIDs[i] = R.id.rbtn_1 + i;
            if (PINIsAvailable[i]) {
                findViewById(rbtnIDs[i]).setOnClickListener(RadioBtnListener);
            }
        }
    }

    private View.OnClickListener RadioBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rbtnView = (RadioButton)v;
            int rbtnClickedId = rbtnView.getId();
            int rbtnClickedIndex = getIndexofID(rbtnClickedId);


            //int rbtnClickedIndex = rbtnClickedId - rbtnIDs[0];

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

    public int getIndexofID(int rbtnClickedId) {
        for (int i=0;i<40;i++) {
            if (rbtnIDs[i]==rbtnClickedId)
                return i;
        }
        return -1;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_configuration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_comm_changeNip:
                btn_changeNip_clicked();
                return true;

            case R.id.menu_comm_disconnect:
                //disconnect
                sendStringMessage("disconnect");
                ChangeView(Connexion.class);
                return true;

            case R.id.menu_comm_close_app:
                sendStringMessage("disconnect");
                this.finishAffinity();
                return true;
        }
        return false;
    }

    private void btn_AddBit_Clicked(boolean isInput) {
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
        //Verifier que le nombre de bits total <= 10

        if (nbBit+nbOfPins > 10) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "A maximum of 10 input pins and 10 output pins is allowed per configuration.\nYou already have "+ nbBit +" pins in your configuration.",
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

                //Add '1' in widget_input at position (PinNB-1)
                if (isInput) {
                    widget_input = widget_input.substring(0, pinNB-1)+'1'+widget_input.substring(pinNB);
                    nbInputBit++;
                }
                else {
                    widget_output = widget_output.substring(0, pinNB-1)+'1'+widget_output.substring(pinNB);
                    nbOutputBit++;
                }

                //Cree un objet PINConfig
                String text = "PIN # " + String.valueOf(pinNB);
                PINConfig pi = new PINConfig(false, isInput, 'P', text);
                pi.setPinNumber(pinNB);

                //Add l'objet créé au listview
                configElementsList.add(pi);
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

    private void btn_AddByte_Clicked(boolean isInput){

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
                letter = GetFirstCharAvailable(widget_input);
                for (int i=0; i<8; i++) {
                    pinNBs[i] = indexesOfSelectedPins.get(i)+1;
                    //Add 'a', 'b' or 'c' in widget_input or widget_output at positions
                    widget_input = widget_input.substring(0, pinNBs[i]-1)+letter+widget_input.substring(pinNBs[i]);
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
                letter = GetFirstCharAvailable(widget_output);
                for (int i=0; i<8; i++) {
                    pinNBs[i] = indexesOfSelectedPins.get(i)+1;
                    //Add 'a', 'b' or 'c' in widget_input or widget_output at positions
                    widget_output = widget_output.substring(0, pinNBs[i]-1)+letter+widget_output.substring(pinNBs[i]);
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
            PINConfig pi = new PINConfig(true, isInput, letter, text);
            pi.setPinNumbers(pinNBs);

            //Add l'objet créé au listview
            configElementsList.add(pi);
            configElementsAdapter.notifyDataSetChanged();
        }
    }

    private char GetFirstCharAvailable(String widget_string) {
        char letter = 'a';
        for (int i=0; i<3; i++) {
            if (widget_input.indexOf('a') == -1) {
                return letter;
            }
            letter++;
        }
        return 'c';
    }

    private void btn_Refresh_Clicked() {
        configNamesAdapter.clear();
        configNamesAdapter.notifyDataSetChanged();
        sendStringMessage("refreshConfigs");
        Log.i("Tag", "Received : "+ received);
        received = receiveStringMessage();

        if(received.equals("empty")) {
            Toast.makeText(getApplicationContext(), "There are no configurations saved!", Toast.LENGTH_SHORT).show();
        }
        else if(!received.equals("error")) {
            String splittedStr[]= received.split("\\r?\\n");

            for(int i=0;i<splittedStr.length;i++){
                if (splittedStr[i]!="null"){
                    configNamesAdapter.add(splittedStr[i]);
                    configNamesAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void btn_Load_Clicked() {
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

        sendStringMessage("loadConfig;"+selected_configname+";");

        received = receiveStringMessage();
        String[] splittedStr = received.split(";",2);
        widget_input = splittedStr[0];
        widget_output = splittedStr[1];

        if(widget_input.equals("error")|| widget_output.equals("error")) {
            Toast.makeText(getApplicationContext(), "An error has occured.", Toast.LENGTH_SHORT).show();
        }
        else {
            init(new String[]{widget_input,widget_output});
            addbit();
            addbyte();
        }

    }

    private void btn_Del_Clicked() {
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

                                    sendStringMessage("deleteConfig;"+ selected_configname +";");
                                    received =receiveStringMessage();

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

    private void btn_Save_Clicked() {
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
                                    ok = true;
                                    sendStringMessage("saveConfig;" + configNameET.getText().toString() + ";" + widget_input + ";" + widget_output + ";");
                                    received = receiveStringMessage();
                                    if (received.equals("error")) {
                                        Toast.makeText(getApplicationContext(), "An error has occured.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                    .setNegativeButton(R.string.No,  null)
                    .show();
        }
    }

    private void btn_Save_And_Start_Clicked() {

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
                                    ok = true;
                                    sendStringMessage("start;" + configNameET.getText().toString() + ";" + widget_input + ";" + widget_output + ";");
                                    received = receiveStringMessage();
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

    private void btn_changeNip_clicked() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setIcon(R.drawable.warning_icon);
        alert.setMessage("Enter your chosen Nip");
        alert.setTitle("Warning !");

        alert.setView(edittext);

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                String NIP = edittext.getText().toString();
                sendStringMessage("changeNIP;"+NIP+";");
                received = receiveStringMessage();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });
        alert.show();
    }

    private void btn_remove_clicked() {
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


            //
            if(!pinConfig.getIsByte()){
                //Bit
                //Remettre le rbtn disponible
                rbtn = (RadioButton)findViewById(rbtnIDs[pinConfig.getPinNumber()-1]);
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
                    rbtn = (RadioButton)findViewById(rbtnIDs[pinConfig.getPinNumbers(i)-1]);
                    rbtn.setClickable(true);
                    rbtn.setChecked(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rbtn.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#248d51")));
                    }
                }
            }
        }
    }

    private void sendStringMessage(String mot){
        try {
            btOutputStream.write(mot.getBytes());

        }
        catch (IOException e) {
            Log.e("Tag", "Writing failed. Tried to write : "+ mot);
        }
    }

    private String receiveStringMessage() {
        byte [] mmBuffer3 = new byte[1024];
        try {
            btInputStream.read(mmBuffer3);
        } catch (IOException e) {
            Log.e("Tag", "Reading failed");
        }
        String b = new String(mmBuffer3);
        return b.substring(0,b.indexOf(0));

    }

    public void ChangeView(Class activity) {
        Intent intent = new Intent(getApplicationContext(), activity);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        sendStringMessage("disconnect");
        ChangeView(Connexion.class);
    }
    public void addbit(){
        for(Integer i:inBitPins){
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
        for(Integer i:outBitPins){
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
    public void addbyte(){

        StringBuilder strBuilder = new StringBuilder("PINS # ");

        for(ArrayList<Integer> byteGroup:inBytePins){
            int [] PinNumbers = new int[8];
            for (int i=0; i<8; i++) {
                //
                PinNumbers[i] = byteGroup.get(i);

                //Add 'a', 'b' or 'c' in widget_input or widget_output at positions
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
            PINConfig pi = new PINConfig(true, true,'G', text);
            pi.setPinNumbers(PinNumbers);

            //Add l'objet créé au listview
            configElementsList.add(pi);
            configElementsAdapter.notifyDataSetChanged();
        }
        strBuilder = new StringBuilder("PINS # ");

        for(ArrayList<Integer> byteGroup:outBytePins){
            int [] PinNumbers = new int[8];
            for (int i=0; i<8; i++) {
                //
                PinNumbers[i] = byteGroup.get(i);

                //Add 'a', 'b' or 'c' in widget_input or widget_output at positions
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
            PINConfig pi = new PINConfig(true, false,'G', text);
            pi.setPinNumbers(PinNumbers);

            //Add l'objet créé au listview
            configElementsList.add(pi);
            configElementsAdapter.notifyDataSetChanged();
        }



    }
    private void init( String [] inputsOutputs) {

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
}
