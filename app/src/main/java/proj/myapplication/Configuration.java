package proj.myapplication;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.StringSearch;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

    //declaration des buttons
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
    public static String widget_input="3303030030000300300300003033030003000030";
    public static String widget_output ="3303030030000300300300003033030003000030";
    private String widget_s="3303030030000300300300003033030003000030i";
    private String pin_config ="P-";
    private String pin_config2="";
    private static String selected = "";
    private int index_r=0;
    ListView Lv1;
    ListView Lv2;
    ArrayList<String> Target = new ArrayList<String>();
    ArrayList<String> Target2 = new ArrayList<>();
    RadioButton[] btnWord = new RadioButton[40];
    ArrayList<Button> BtnWord = new ArrayList<Button>();
    PINConfig[] btnWorld2 = new PINConfig[40];
    public byte[] mmBuffer3;
    private String pin_infos[] = new String[40];
    ArrayList<String> myList = new ArrayList<>();




    //ArrayAdapter
    public CustomAdapter arrayAdapter;
    public ArrayAdapter<String> arrayAdapter2;
    public ArrayAdapter<String> arrayAdapter3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        btSocket = Connexion.btSocket;
        arrayAdapter = new CustomAdapter(this,myList);
        arrayAdapter2= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Target2);


        //déclaration des lestViews
        Lv1 = (ListView)findViewById(R.id.simpleListView);
        Lv1.setAdapter(arrayAdapter2);
        Lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selected = Lv1.getItemAtPosition(position).toString();
                index_r=position;
            }
        });
        Lv2 =(ListView)findViewById(R.id.simpleListView2);
        Lv2.setAdapter(arrayAdapter);
        Lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selected = Lv2.getItemAtPosition(position).toString();
                index_r=position;
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
                btn_AddBitR_Clicked();

            }
        });
        // button ajouter bit W
        btn_Add_bit_W = (Button)findViewById(R.id.btn_ajouter_bit_W);
        btn_Add_bit_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_AddBitw_Clicked();
            }
        });
        btn_Add_byte_W = (Button)findViewById(R.id.btn_ajouter_byte_W);
        btn_Add_byte_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_AddByteW_Clicked();
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
                btn_refresh_cliked();
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

        // Button ajouter Byte
        btn_Add_byte_R = (Button)findViewById(R.id.btn_ajouter_byte_R);
        btn_Add_byte_R.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_AddByteR_cliked();
            }
        });

        //ajout des radiobutton

        rbtn3 =(RadioButton)findViewById(R.id.rbtn_3);
        rbtn3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    rbtn3.setChecked(false);
                }
                else {

                    widget_s = "331" +widget_s.substring(3);
                    btnWord[2] = rbtn3;
                    BtnWord.add(rbtn3);
                    pin_config = "P-3";
                    pin_config2 = pin_config2 +"3-";


                }
            }
        });
        //ajout des radiobutton
        rbtn5 =(RadioButton)findViewById(R.id.rbtn_5);
        rbtn5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                widget_s = widget_s.substring(0,4) +"1" +widget_s.substring(5) + "  "+widget_s.length();
                //ajout button dans btnword
                btnWord[4] = rbtn5;
                BtnWord.add(rbtn5);
                pin_config = "P-5";
                pin_config2 = pin_config2 +"5-";


            }
        });
        //ajout des radiobutton
        rbtn7 =(RadioButton)findViewById(R.id.rbtn_7);
        rbtn7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,6) +"1" +widget_s.substring(7);
                //ajout button dans btnword
                btnWord[6] = rbtn7;
                BtnWord.add(rbtn7);
                pin_config = "P-7";
                pin_config2 = pin_config2 +"7-";


            }
        });
        //ajout des radiobutton
        rbtn8 =(RadioButton)findViewById(R.id.rbtn_8);
        rbtn8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,7) +"1" +widget_s.substring(8);
                //ajout button dans btnword
                btnWord[7] = rbtn8;
                BtnWord.add(rbtn8);
                pin_config = "P-8";
                pin_config2 = pin_config2 +"8-";
            }
        });
        //ajout des radiobutton
        rbtn10 =(RadioButton)findViewById(R.id.rbtn_10);
        rbtn10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,9) +"1" +widget_s.substring(10);
                //ajout button dans btnword
                btnWord[9] = rbtn10;
                BtnWord.add(rbtn10);
                pin_config = "P-10";
                pin_config2 = pin_config2 +"10-";

            }
        });
        //ajout des radiobutton
        rbtn11 =(RadioButton)findViewById(R.id.rbtn_11);
        rbtn11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,10) +"1" +widget_s.substring(11);
                //ajout button dans btnword
                btnWord[10] = rbtn11;
                pin_config = "P-11";
                pin_config2 = pin_config2 +"11-";
            }
        });
        //ajout des radiobutton
        rbtn12 =(RadioButton)findViewById(R.id.rbtn_12);
        rbtn12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,11) +"1" +widget_s.substring(12);
                //ajout button dans btnword
                btnWord[11] = rbtn12;
                BtnWord.add(rbtn12);
                pin_config = "P-12";
                pin_config2 = pin_config2 +"12-";
            }
        });
        //ajout des radiobutton
        rbtn13 =(RadioButton)findViewById(R.id.rbtn_13);
        rbtn13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,12) +"1" +widget_s.substring(13);
                //ajout button dans btnword
                btnWord[12] = rbtn13;
                BtnWord.add(rbtn13);
                pin_config = "P-13";
                pin_config2 = pin_config2 +"13-";
            }
        });
        //ajout des radiobutton
        rbtn15 =(RadioButton)findViewById(R.id.rbtn_15);
        rbtn15.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,14) +"1" +widget_s.substring(15);
                //ajout button dans btnword
                btnWord[14] = rbtn15;
                BtnWord.add(rbtn15);
                pin_config = "P-15";
                pin_config2 = pin_config2 +"15-";
            }
        });
        //ajout des radiobutton
        rbtn16 =(RadioButton)findViewById(R.id.rbtn_16);
        rbtn16.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,15) +"1" +widget_s.substring(16);
                //ajout button dans btnword
                btnWord[15] = rbtn16;
                BtnWord.add(rbtn16);
                pin_config = "P-16";
                pin_config2 = pin_config2 +"16-";
            }
        });
        //ajout des radiobutton
        rbtn18 =(RadioButton)findViewById(R.id.rbtn_18);
        rbtn18.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,17) +"1" +widget_s.substring(18);
                //ajout button dans btnword
                btnWord[17] = rbtn18;
                BtnWord.add(rbtn18);
                pin_config = "P-18";
                pin_config2 = pin_config2 +"18-";
            }
        });
        //ajout des radiobutton
        rbtn19 =(RadioButton)findViewById(R.id.rbtn_19);
        rbtn19.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,18) +"1" +widget_s.substring(19);
                //ajout button dans btnword
                btnWord[18] = rbtn19;
                BtnWord.add(rbtn19);
                pin_config ="P-19";
                pin_config2 = pin_config2 +"19-";
            }
        });
        //ajout des radiobutton
        rbtn21 =(RadioButton)findViewById(R.id.rbtn_21);
        rbtn21.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,20) +"1" +widget_s.substring(21);
                //ajout button dans btnword
                btnWord[20] = rbtn21;
                BtnWord.add(rbtn21);
                pin_config = "P-21";
                pin_config2 = pin_config2 +"21-";
            }
        });
        //ajout des radiobutton
        rbtn22 =(RadioButton)findViewById(R.id.rbtn_22);
        rbtn22.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,21) +"1" +widget_s.substring(22);
                //ajout button dans btnword
                btnWord[21] = rbtn22;
                BtnWord.add(rbtn22);
                pin_config = "P-22";
                pin_config2 = pin_config2 +"22-";
            }
        });
        //ajout des radiobutton
        rbtn23 =(RadioButton)findViewById(R.id.rbtn_23);
        rbtn23.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,22) +"1" +widget_s.substring(23);
                //ajout button dans btnword
                btnWord[22] = rbtn23;
                BtnWord.add(rbtn23);
                pin_config = "P-23";
                pin_config2 = pin_config2 +"23-";
            }
        });
        //ajout des radiobutton
        rbtn24 =(RadioButton)findViewById(R.id.rbtn_24);
        rbtn24.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,23) +"1" +widget_s.substring(24);
                //ajout button dans btnword
                btnWord[23] = rbtn24;
                BtnWord.add(rbtn24);
                pin_config = "P-24";
                pin_config2 = pin_config2 +"24-";
            }
        });
        //ajout des radiobutton
        rbtn26 =(RadioButton)findViewById(R.id.rbtn_26);
        rbtn26.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,25) +"1" +widget_s.substring(26);
                //ajout button dans btnword
                btnWord[25] = rbtn26;
                BtnWord.add(rbtn26);
                pin_config = "P-26";
                pin_config2 = pin_config2 +"26-";
            }
        });
        //ajout des radiobutton
        rbtn29 =(RadioButton)findViewById(R.id.rbtn_29);
        rbtn29.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,28) +"1" +widget_s.substring(29);
                //ajout button dans btnword
                btnWord[28] = rbtn29;
                BtnWord.add(rbtn29);
                pin_config = "P-29";
                pin_config2 = pin_config2 +"29-";
            }
        });
        //ajout des radiobutton
        rbtn31 =(RadioButton)findViewById(R.id.rbtn_31);
        rbtn31.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,30) +"1" +widget_s.substring(31);
                //ajout button dans btnword
                btnWord[30] = rbtn31;
                BtnWord.add(rbtn31);
                pin_config = "P-31";
                pin_config2 = pin_config2 +"31-";
            }
        });
        //ajout des radiobutton
        rbtn32 =(RadioButton)findViewById(R.id.rbtn_32);
        rbtn32.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,31) + "1"+widget_s.substring(32);
                //ajout button dans btnword
                btnWord[31] = rbtn32;
                BtnWord.add(rbtn32);
                pin_config = "P-32";
                pin_config2 = pin_config2 +"32-";
            }
        });
        //ajout des radiobutton
        rbtn33 =(RadioButton)findViewById(R.id.rbtn_33);
        rbtn33.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,32) +"1" +widget_s.substring(33);
                //ajout button dans btnword
                btnWord[32] = rbtn33;
                BtnWord.add(rbtn33);
                pin_config = "P-33";
                pin_config2 = pin_config2 +"33-";

            }
        });
        //ajout des radiobutton
        rbtn35 =(RadioButton)findViewById(R.id.rbtn_35);
        rbtn35.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,34) +"1" +widget_s.substring(35);
                //ajout button dans btnword
                btnWord[34] = rbtn35;
                BtnWord.add(rbtn35);
                pin_config = "P-35";
                pin_config2 = pin_config2 +"35-";
            }
        });
        //ajout des radiobutton
        rbtn36 =(RadioButton)findViewById(R.id.rbtn_36);
        rbtn36.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,35) +"1" +widget_s.substring(36);
                //ajout button dans btnword
                btnWord[35] = rbtn36;
                BtnWord.add(rbtn36);
                pin_config = "P-36";
                pin_config2 = pin_config2 +"36-";
            }
        });
        //ajout des radiobutton
        rbtn37 =(RadioButton)findViewById(R.id.rbtn_37);
        rbtn37.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,36) +"1" +widget_s.substring(37);
                //ajout button dans btnword
                btnWord[36] = rbtn37;
                BtnWord.add(rbtn37);
                pin_config = "P-37";
                pin_config2 = pin_config2 +"37-";
            }
        });
        //ajout des radiobutton
        rbtn38 =(RadioButton)findViewById(R.id.rbtn_38);
        rbtn38.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,37) +"1" +widget_s.substring(38);
                //ajout button dans btnword
                btnWord[37] = rbtn38;
                pin_config = "P-38";
                pin_config2 = pin_config2 +"38-";
            }
        });
        //ajout des radiobutton
        rbtn40 =(RadioButton)findViewById(R.id.rbtn_40);
        rbtn40.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget_s = widget_s.substring(0,39) +"1" +widget_s.substring(40);
                //ajout button dans btnword
                btnWord[39] = rbtn40;
                BtnWord.add(rbtn40);
                pin_config = "P-40";
                pin_config2 = pin_config2 +"40-";
            }
        });
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





    private void btn_AddBitR_Clicked(){

        widget_s = widget_s.substring(0,40)+"i" ;
        widget_s= widget_s.replaceAll("1","P");
        String widgets = widget_s.substring(0,40);
        widget_input = widgets;
        for(int i=0;i<widget_input.length();i++){
            if(widget_input.charAt(i)=='P'){
                PINConfig pi = new PINConfig(false,true,i,'P',String.valueOf(i+1));
                btnWorld2[i] = pi;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btnWord[i].setButtonTintList(ColorStateList.valueOf(Color.GRAY));
                    btnWord[i].setClickable(false);
                    String info ="";
                    info = pin_config +";"+btnWorld2[i].getText();
                    last_pin_added =pin_config;
                    myList.add(info);
                    arrayAdapter.notifyDataSetChanged();

                }

            }
        }
        pin_config = "";
        widget_input.replaceAll("P","1");

    }
    private void btn_AddBitw_Clicked(){
        Target2.add(pin_config);
        arrayAdapter2.notifyDataSetChanged();
        widget_s = widget_s.substring(0,40)+"0" ;

        widget_s= widget_s.replaceAll("1","P");
        String widgets = widget_s.substring(0,40);
        widget_output = widgets;
        for(int i=0;i<widget_output.length();i++){
            if(widget_output.charAt(i)=='P'){
                PINConfig pi = new PINConfig(false,false,i,'P',String.valueOf(i+1));
                btnWorld2[i] = pi;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btnWord[i].setButtonTintList(ColorStateList.valueOf(Color.GRAY));
                    btnWord[i].setClickable(false);
                    last_pin_added =pin_config;

                }
            }
        }
        pin_config = "";
        widget_input.replaceAll("P","1");

    }
    private void btn_AddByteR_cliked(){
        String[] array_pin = pin_config2.split("-");
        //widget_s = widget_s.substring(0,40)+"i" ;
        int Icount=array_pin.length;
        for(int i = 0; i<array_pin.length; i++ ){
            String k = "G-"+ String.valueOf(array_pin[i]);
        }



        if(Icount==8){
            myList.add("Byte : Input" +";"+"Pin # :"+pin_config2);
            arrayAdapter.notifyDataSetChanged();

            widget_s= widget_s.replaceAll("1","G");
            String widgets = widget_s.substring(0,40);
            if(widget_output.indexOf('a')==-1){
                widgets.replaceAll("1","a");
            }
            else if(widget_output.indexOf('a')!=-1){
                widgets.replaceAll("1","b");

            }
            else{
                widgets.replaceAll("1","c");
            }
            widget_input = widgets;
            for(int i=0;i<widget_input.length();i++){
                if(widget_input.charAt(i)=='G'){
                    PINConfig pi = new PINConfig(false,true,i,'G',String.valueOf(i+1));
                    btnWorld2[i] = pi;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        btnWord[i].setButtonTintList(ColorStateList.valueOf(Color.GRAY));
                        btnWord[i].setClickable(false);
                    }
                }
            }
            last_byte_added =pin_config2;
            pin_config2 = "";
            widget_input.replaceAll("3","2");



        }
        else{
            Toast toast= Toast.makeText(getApplicationContext(),"un byte est juste de 8 bits",Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    private void btn_AddByteW_Clicked(){

        widget_s = widget_s.substring(0,40)+"o" ;
        String[] array_pin = pin_config2.split("-");
        //widget_s = widget_s.substring(0,40)+"i" ;
        int Icount=array_pin.length;
        for(int i = 0; i<array_pin.length; i++ ){
            String k = "G-"+ String.valueOf(array_pin[i]);

        }
        myList.add("Byte : Output" +";"+"Pin # :"+pin_config2);
        arrayAdapter.notifyDataSetChanged();

        if(Icount==8){


            //widget_s= widget_s.replaceAll("1","G");

            String widgets = widget_s.substring(0,40);

            if(widget_output.indexOf('a')==-1){
                widgets.replaceAll("1","a");
            }
            else if(widget_output.indexOf('a')!=-1){
                widgets.replaceAll("1","b");

            }
            else{
                widgets.replaceAll("1","c");
            }
            widget_output = widgets;
            for(int i=0;i<widget_output.length();i++){
                if(widget_output.charAt(i)=='a' || widget_output.charAt(i)=='b' || widget_output.charAt(i)=='c'){
                    PINConfig pi = new PINConfig(false,true,i,'G',String.valueOf(i+1));
                    btnWorld2[i] = pi;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        btnWord[i].setButtonTintList(ColorStateList.valueOf(Color.GRAY));
                    }
                }
            }
            widget_output.replaceAll("3","2");


        }
        else{
            Toast toast= Toast.makeText(getApplicationContext(),"un byte est juste de 8 bits",Toast.LENGTH_SHORT);
            toast.show();
        }
        last_byte_added =pin_config2;
        arrayAdapter2.notifyDataSetChanged();
    }
    private void btn_refresh_cliked(){
        Target2.clear();
        arrayAdapter2.notifyDataSetChanged();
        sendStringMessage("refreshConfigs");
        Log.i("Tag", "Received : "+ recieved);
        recieved = receiveStringMessage();
        if(recieved=="error"){
            //gestion erreur

        }
        else{
            String a_b[]=recieved.split("\\r?\\n");
            for(int i=0;i<a_b.length;i++){
                Target2.add(a_b[i]);
                arrayAdapter2.notifyDataSetChanged();
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
    private void btn_remove_clicked(){
        String aide_="Pin#:abc-";
        String pin_="";
        pin_=arrayAdapter.get_pin_info_name_info(index_r);
        int[] tab= new int[8];
        int count=0;

        if(pin_.length()==1){
            btnWord[Integer.valueOf(pin_)-1].setVisibility(View.VISIBLE);
            btnWord[Integer.valueOf(pin_)-1].setChecked(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
             btnWord[Integer.valueOf(pin_)-1].setButtonTintList(ColorStateList.valueOf(Color.parseColor("#248d51")));
            }


        }
        else{
            // recuperation de la liste de pin a supprimer
            for(int i=0;i<pin_.length();i++){
                if(aide_.indexOf(pin_.charAt(i))==-1){
                    if(aide_.indexOf(pin_.charAt(i+1))!=-1){
                        String valeur="";
                        valeur = valeur+pin_.charAt(i);
                        tab[count]=Integer.valueOf(valeur);
                        count = count+1;
                    }
                    else if(aide_.indexOf(pin_.charAt(i))==-1){
                        String valeur="";
                        valeur = valeur+pin_.charAt(i)+pin_.charAt(i+1);
                        tab[count]=Integer.valueOf(valeur);
                        count = count+1;
                        i=i+1;

                    }
                }

            }


        }




        /*

        for(int i =0;i<len;i++){
            if(convo.indexOf(selected.charAt(i))==-1 && len==3){
                pin = pin +selected.charAt(i);

            }
            else if (convo.indexOf(pin_config.charAt(i))==-1 && len==4){
                pin = pin+selected.charAt(i)+selected.charAt(i+1);
                i = i+1;
            }
        }
        */

        //btnWord[Integer.valueOf(pin)-1].setVisibility(View.VISIBLE);
        ////btnWord[Integer.valueOf(pin)-1].setChecked(false);
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           // btnWord[Integer.valueOf(pin)-1].setButtonTintList(ColorStateList.valueOf(Color.parseColor("#248d51")));
        //}
        myList.remove(index_r);
        arrayAdapter.notifyDataSetChanged();



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
            inputStream.read( mmBuffer3);
        } catch (IOException e) {
            Log.e("Tag", "Reading failed");
        }
        String b = new String(mmBuffer3);
        return b;

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
