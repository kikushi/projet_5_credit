package proj.myapplication;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import static proj.myapplication.Connexion.btSocket;

public class Configuration extends AppCompatActivity {
    //declaration des commandes
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
    private int etat_btn_change_nip=0;

    private EditText myconfigname;
    private String NIP="";
    private String config_name;
    private String recieved;
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


    //declaration des variables
    private EditText mytext;

    private String widget_s="3303030030000300300300003033030003000030i";
    private boolean[] selectedPins;
    private String pin_config ="P-";
    private String pin_config2="";
    private PINConfig selectedElement;
    private String selected;
    public int index_selected_element;
    ListView Lv1;
    public ListView configElementsListView;
    ArrayList<String> Target = new ArrayList<String>();
    ArrayList<String> Target2 = new ArrayList<>();
    RadioButton[] btnWord = new RadioButton[40];
    ArrayList<Button> BtnWord = new ArrayList<Button>();
    PINConfig[] btnWorld2 = new PINConfig[40];
    public byte[] mmBuffer3;
    private String pin_infos[] = new String[40];


    public static String widget_input;
    public static String widget_output;
    public int nbInputBit, nbInputByte, nbOutputBit, nbOutputByte;
    ArrayList<PINConfig> configElementsList;
    boolean[] PINIsAvailable;




    //ArrayAdapter
    public CustomAdapter configElementsAdapter;
    public ArrayAdapter<String> configNamesAdapter;
    public ArrayAdapter<String> arrayAdapter3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        widget_input="2202020020000200200200002022020002000020";
        widget_output ="2202020020000200200200002022020002000020";
        nbInputBit = 0;
        nbInputByte = 0;
        nbOutputBit = 0;
        nbOutputByte = 0;

        rbtnIDs = new int[40];
        selectedPins = new boolean[40];

        index_selected_element = -1;
        btSocket = Connexion.btSocket;

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
        Lv1 = (ListView)findViewById(R.id.listview_confignames);
        Lv1.setAdapter(configNamesAdapter);
        Lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selected = Lv1.getItemAtPosition(position).toString();
                index_selected_element =position;
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
                btn_load_Cliked();
            }
        });

        // button save and start
        btn_save_and_start = (Button)findViewById(R.id.btn_save_and_start);
        btn_save_and_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Save_And_Start_Cliked();

            }
        });

        // button save
        btn_save = (Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            btn_save_clicked();
                                        }
                                    }
        );
        // button del
        btn_del = (Button)findViewById(R.id.btn_delete);
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_del_Cliked();
            }
        });




        // button ajouter bit R
        btn_Add_bit_R = (Button)findViewById(R.id.btn_ajouter_bit_R);
        btn_Add_bit_R.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_AddBit_Clicked(true);

            }
        });
        // button ajouter bit W
        btn_Add_bit_W = (Button)findViewById(R.id.btn_ajouter_bit_W);
        btn_Add_bit_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_AddBit_Clicked(false);
            }
        });

        // Button ajouter Byte
        btn_Add_byte_R = (Button)findViewById(R.id.btn_ajouter_byte_R);
        btn_Add_byte_R.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_AddByte_Clicked(true);
            }
        });


        btn_Add_byte_W = (Button)findViewById(R.id.btn_ajouter_byte_W);
        btn_Add_byte_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_AddByte_Clicked(false);
            }
        });

 /*
        // Edittext
        mytext = (EditText)findViewById(R.id.editText);
        mytext.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                NIP = mytext.getText().toString();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

*/


        // edittext2


        myconfigname = (EditText)findViewById(R.id.editText);
        myconfigname.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                //code de validation
                int count =0;
                String audi = myconfigname.getText().toString();
                // ajouter le "" aux commandes interdites
                Character op[] = {'/','<','>','|',':','?','*','.','%'};
                for(int i=0;i<9;i++){
                    if(audi.indexOf(op[i])!=-1){
                        count = count+1;

                    }
                }
                if(count ==0){
                    config_name = myconfigname.getText().toString();


                }

                if(count!=0){
                    new AlertDialog.Builder(Configuration.this)
                            .setTitle("GOD MODE")
                            .setMessage("Impossible d'enregistrer la configuration. Nom invalide")
                            .setIcon(R.drawable.ic_launcher_foreground)
                            .setPositiveButton("Oui",
                                    new DialogInterface.OnClickListener() {
                                        @TargetApi(11)
                                        public void onClick(DialogInterface dialog, int id) {

                                            dialog.cancel();
                                        }
                                    }).show();


                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

        // button refresh
        btn_refresh = (Button)findViewById(R.id.btn_refresh);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_refresh_clicked();
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

        rbtnIDs[0]=R.id.rbtn_1;
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
                etat_btn_change_nip =1;
                btn_changeNip_cliked();
                return true;

            case R.id.menu_comm_disconnect:
                //disconnect
                sendStringMessage("disconnect");
                try{
                    Connexion.btSocket.close();
                }
                catch(IOException e){
                    Log.e("Tag", "btsocket's close() method failed", e);
                }
                ChangeView(Connexion.class);
                return true;

            case R.id.menu_comm_close_app:
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
                    "A maximum of 10 input pins and 10 output pins is allowed per configuration.\nYou already have "+ nbBit +"pins in your configuration.",
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    RadioButton rbtnClicked = findViewById(rbtnIDs[pinNB-1]);
                    rbtnClicked.setButtonTintList(ColorStateList.valueOf(Color.GRAY));
                    rbtnClicked.setClickable(false);
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        RadioButton rbtnClicked = findViewById(rbtnIDs[pinNBs[i]-1]);
                        rbtnClicked.setButtonTintList(ColorStateList.valueOf(Color.GRAY));
                        rbtnClicked.setClickable(false);
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        RadioButton rbtnClicked = findViewById(rbtnIDs[pinNBs[i]-1]);
                        rbtnClicked.setButtonTintList(ColorStateList.valueOf(Color.GRAY));
                        rbtnClicked.setClickable(false);
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

    private void btn_refresh_clicked(){
        Target2.clear();
        configNamesAdapter.notifyDataSetChanged();
        sendStringMessage("refreshConfigs");
        Log.i("Tag", "Received : "+ recieved);
        recieved = receiveStringMessage();
        if(recieved=="error"){
            //gestion erreur

        }
        else if(recieved=="empty"){
            Toast.makeText(getApplicationContext(), "There are no configurations saved!", Toast.LENGTH_SHORT).show();
        }
        else{
            String a_b[]=recieved.split("\\r?\\n");
            for(int i=0;i<a_b.length;i++){
                if (a_b[i]!="null"){
                    Target2.add(a_b[i]);
                    configNamesAdapter.notifyDataSetChanged();
                }
            }
        }


    }
    private void btn_load_Cliked(){

        sendStringMessage("loadConfig;"+selected+";");

        recieved = receiveStringMessage();

        if(recieved=="error"){
            //gestion erreur

        }
        else {
            String[] splitedStr =recieved.split(";",2);
            widget_input=splitedStr[0];
            widget_output=splitedStr[1];

        }
        widget_output.replaceAll("3","2");
        widget_output.replaceAll("P","1");
        widget_input.replaceAll("P","1");
        widget_input.replaceAll("3","2");
        Toast toast2= Toast.makeText(getApplicationContext(),widget_input,Toast.LENGTH_SHORT);
        toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
        toast2.show();
        Toast toast3= Toast.makeText(getApplicationContext(),widget_input,Toast.LENGTH_SHORT);
        toast3.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
        toast3.show();

    }
    private void btn_del_Cliked(){
        new AlertDialog.Builder(Configuration.this)
                .setTitle("GOD MODE")
                .setMessage("voulez vous vraiment supprimer la configuration?")
                .setIcon(R.drawable.ic_launcher_foreground)
                .setPositiveButton("Oui",
                        new DialogInterface.OnClickListener() {
                            @TargetApi(11)
                            public void onClick(DialogInterface dialog, int id) {
                                sendStringMessage("delete;");
                                sendStringMessage(selected+";");
                                recieved=receiveStringMessage();
                                if (recieved=="error"){
                                    //gestion error
                                }
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                }).show();
    }
    private void btn_save_clicked(){
        new AlertDialog.Builder(Configuration.this)
                .setTitle("GOD MODE")
                .setMessage("voulez vous vraiment sauvegarder la configuration?")
                .setIcon(R.drawable.ic_launcher_foreground)
                .setPositiveButton("Oui",
                        new DialogInterface.OnClickListener() {
                            @TargetApi(11)
                            public void onClick(DialogInterface dialog, int id) {
                                    sendStringMessage("saveConfig;"+config_name+";"+widget_input+";"+widget_output+";");
                                    recieved = receiveStringMessage();
                                    if(recieved=="error"){
                                        //gestion erreur

                                    }
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();

    }
    private void btn_Save_And_Start_Cliked(){
        sendStringMessage("start;"+config_name+";"+widget_input+";"+widget_output+";");
        recieved = receiveStringMessage();
        if(recieved=="error"){
            //gestion error

        }else {
            ChangeView(Communication.class);
        }

    }
    private void btn_changeNip_cliked(){
        if(etat_btn_change_nip ==1){
            final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_layout, null);

            final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
            Button button1 = (Button) dialogView.findViewById(R.id.buttonSubmit);
            Button button2 = (Button) dialogView.findViewById(R.id.buttonCancel);

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBuilder.dismiss();
                }
            });
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // DO SOMETHINGS
                    sendStringMessage("changeNip;"+editText.getText().toString()+";");
                    recieved=receiveStringMessage();
                    if(recieved=="error"){
                        //gestion erreur

                    }
                    dialogBuilder.dismiss();
                }
            });

            dialogBuilder.setView(dialogView);
            dialogBuilder.show();
        }

    }

    private void btn_remove_clicked() {

        PINConfig pinConfig= configElementsAdapter.get_element(index_selected_element);
        if (pinConfig != null) {
            RadioButton rbtn;

            //Remove l'element de la listview
            configElementsList.remove(index_selected_element);
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
            OutputStream btOutputStream = Connexion.btSocket.getOutputStream();
            btOutputStream.write(mot.getBytes());

        }
        catch (IOException e) {
            Log.e("Tag", "Writing failed. Tried to write : "+ mot);
        }
    }
    private String receiveStringMessage() {
        mmBuffer3 = new byte[1024];
        try {
            InputStream inputStream = btSocket.getInputStream();
            inputStream.read(mmBuffer3);
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






    /*
    toast
    Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
    toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
    toast2.show();
     private void getFromInterne_input() throws IOException{
        String value = "";
        FileInputStream inputStream = openFileInput("input.txt");
        StringBuilder Stringb = new StringBuilder();
        int content;
        while((content = inputStream.read())!=-1){
            value = String.valueOf(Stringb.append((char)content));
        }
        widget_input = value;

    }
    private void getFromInterne_output() throws IOException{
        String value = "";
        FileInputStream inputStream = openFileInput("output.txt");
        StringBuilder Stringb = new StringBuilder();
        int content;
        while((content = inputStream.read())!=-1){
            value = String.valueOf(Stringb.append((char)content));
        }
        widget_output = value;
    }
    private void saveInterne_input() throws IOException {
        FileOutputStream outputStream = openFileOutput("input.txt",MODE_PRIVATE);
        String numero = widget_input;
        outputStream.write(numero.getBytes());
        outputStream.close();
    }
    private void saveInterne_output() throws IOException {
        FileOutputStream outputStream = openFileOutput("output.txt",MODE_PRIVATE);
        String numero = widget_output;
        outputStream.write(numero.getBytes());
        outputStream.close();
    }
     */

}
