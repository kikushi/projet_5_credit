package proj.myapplication;

import android.content.res.ColorStateList;
import android.graphics.Color;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
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
    private Button btn_Add_bit_R;
    private Button btn_Add_byte_R;
    private Button btn_Add_bit_W;
    private Button btn_Add_byte_W;
    private Button btn_remove;
    private Button btn_refresh;
    private Button btn_load;
    private Button btn_save;
    private Button btn_del;
    private Button btn_change_nip;
    private Button btn_disconnect;
    private Button btn_save_and_start;
    private int etat_btn_change_nip=0;
    private int etat_btn_save =0;
    private String nom_config;

    private EditText mytext;
    private EditText myconfigname;
    private String NIP="";
    private String config_name;
    private String recieved;




    private Button btn_OK;

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


    static String nbwidget_read;
    static  String nbwidget_saved;
    public static String widget_input="3303030030000300300300003033030003000030";
    public static String widget_output ="3303030030000300300300003033030003000030";
    private String widget_s="3303030030000300300300003033030003000030i";
    private String mode = "i";
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
    public InputStream iStream;
    private String gm = "adja/Fama/Dia";
    public String config_names_received[] = gm.split("/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Target);
        final ArrayAdapter<String> arrayAdapter2= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Target2);

        btSocket = Connexion.btSocket;
        Lv1 = (ListView)findViewById(R.id.simpleListView);
        Lv1.setAdapter(arrayAdapter);
        Lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selected = Lv1.getItemAtPosition(position).toString();
                Toast toast2= Toast.makeText(getApplicationContext(),selected,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
                index_r=position;
            }
        });
        Lv2 =(ListView)findViewById(R.id.simpleListView3);
        Lv2.setAdapter(arrayAdapter2);
        Lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selected = Lv2.getItemAtPosition(position).toString();
                Toast toast2= Toast.makeText(getApplicationContext(),selected,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
                index_r=position;
            }
        });
        // button load
        btn_load = (Button)findViewById(R.id.btn_load);
        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendStringMessage("loadConfig-");
                sendStringMessage(selected);

                try{
                    //sendStringMessage(selected);
                    recieved = receiveStringMessage();
                    //widget_output = receiveStringMessage();
                }
                catch(IOException e){

                }


            }
        });
        // button save and start
        btn_save_and_start = (Button)findViewById(R.id.btn_save_and_start);
        btn_save_and_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendStringMessage("start");
                ChangeView(Communication.class);
            }
        });
        // button disconnect
        btn_disconnect = (Button)findViewById(R.id.btn_disconnect);
        btn_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //disconnect
                sendStringMessage("disconnect");
                try{
                    Connexion.btSocket.close();
                    ChangeView(Connexion.class);
                }
                catch(IOException e){

                }
            }
        });




        // button save
        btn_save = (Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            new AlertDialog.Builder(Configuration.this)
                                                    .setTitle("GOD MODE")
                                                    .setMessage("voulez vous vraiment supprimer la configuration?")
                                                    .setIcon(R.drawable.ic_launcher_foreground)
                                                    .setPositiveButton("Oui",
                                                            new DialogInterface.OnClickListener() {
                                                                @TargetApi(11)
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    etat_btn_save = 1;

                                                                    btnsavecliked();
                                                                    dialog.cancel();
                                                                }
                                                            })
                                                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                                        @TargetApi(11)
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            etat_btn_save =0;

                                                            dialog.cancel();
                                                        }
                                                    }).show();



                                        }
                                    }
        );
        // button del
        btn_del = (Button)findViewById(R.id.btn_delete);
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Configuration.this)
                        .setTitle("GOD MODE")
                        .setMessage("voulez vous vraiment supprimer la configuration?")
                        .setIcon(R.drawable.ic_launcher_foreground)
                        .setPositiveButton("Oui",
                                new DialogInterface.OnClickListener() {
                                    @TargetApi(11)
                                    public void onClick(DialogInterface dialog, int id) {

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
        });




        // button ajouter bit R
        btn_Add_bit_R = (Button)findViewById(R.id.btn_ajouter_bit_R);
        btn_Add_bit_R.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Target2.add(pin_config);
                arrayAdapter2.notifyDataSetChanged();
                AddBitRBtnClicked();

            }
        });
        // button ajouter bit W
        btn_Add_bit_W = (Button)findViewById(R.id.btn_ajouter_bit_W);
        btn_Add_bit_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Target2.add(pin_config);
                arrayAdapter2.notifyDataSetChanged();
                AddBitwBtnClicked();

            }
        });
        btn_Add_byte_W = (Button)findViewById(R.id.btn_ajouter_byte_W);
        btn_Add_byte_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddByteWBtnClicked();
                arrayAdapter2.notifyDataSetChanged();

            }
        });
        // Edittext
        mytext = (EditText)findViewById(R.id.editText);
        mytext.setVisibility(View.INVISIBLE);
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
        // edittext2
        myconfigname = (EditText)findViewById(R.id.editText2);
        myconfigname.setVisibility(View.VISIBLE);
        myconfigname.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                //code de validation
                int count =0;
                String audi = myconfigname.getText().toString();
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
                config_name = "toto";
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
                sendStringMessage("refreshConfigs");
                try{
                    recieved = receiveStringMessage();
                }catch (IOException e){

                }
                String a_b[]=recieved.split("\\r?\\n");
                for(int i=0;i<a_b.length;i++){
                    Target.add(a_b[i]);
                    arrayAdapter.notifyDataSetChanged();
                }


            }
        });


        // button remove
        btn_remove= (Button)findViewById(R.id.btn_remove);
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String convo ="PG-";
                String pin ="";
                int len = selected.length();

                for(int i =0;i<len;i++){
                    if(convo.indexOf(selected.charAt(i))==-1 && len==3){
                        pin = pin +selected.charAt(i);

                    }
                    else if (convo.indexOf(pin_config.charAt(i))==-1 && len==4){
                        pin = pin+selected.charAt(i)+selected.charAt(i+1);
                        i = i+1;
                    }
                }


                btnWord[Integer.valueOf(pin)-1].setVisibility(View.VISIBLE);
                btnWord[Integer.valueOf(pin)-1].setChecked(false);
                btnWord[Integer.valueOf(pin)-1].setButtonTintList(ColorStateList.valueOf(Color.parseColor("#248d51")));
                //btnWord[Integer.valueOf(pin)-1].setButtonDrawable(android.R.color.holo_green_light);
                Target2.remove(index_r);
                arrayAdapter2.notifyDataSetChanged();
                //RenmoveBtnClicked();
                //pin_config ="P-";
                //pin_config2 ="";


            }
        });
        // boutton change nip
        btn_change_nip  =(Button)findViewById(R.id.btn_changer_nip);
        btn_change_nip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etat_btn_change_nip =1;
                changeNipcliked();
                sendStringMessage("changeNIP");
            }
        });

        // Button ajouter Byte
        btn_Add_byte_R = (Button)findViewById(R.id.btn_ajouter_byte_R);
        btn_Add_byte_R.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendStringMessage("writeByte");

                AddByteRBtnClicked();
                arrayAdapter2.notifyDataSetChanged();

                //arrayAdapter.notifyDataSetChanged();
            }
        });

        //ajout des radiobutton
        rbtn3 =(RadioButton)findViewById(R.id.rbtn_3);
        rbtn3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast toast= Toast.makeText(getApplicationContext()," pin 3 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = "331" +widget_s.substring(3);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s.toString(),Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
                btnWord[2] = rbtn3;
                BtnWord.add(rbtn3);
                pin_config = "P-3";
                pin_config2 = pin_config2 +"3-";
            }
        });
        //ajout des radiobutton
        rbtn5 =(RadioButton)findViewById(R.id.rbtn_5);
        rbtn5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast toast= Toast.makeText(getApplicationContext()," pin 5 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,4) +"1" +widget_s.substring(5) + "  "+widget_s.length();
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 7 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,6) +"1" +widget_s.substring(7);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 8 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,7) +"1" +widget_s.substring(8);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 10 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,9) +"1" +widget_s.substring(10);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 11 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,10) +"1" +widget_s.substring(11);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 3 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,11) +"1" +widget_s.substring(12);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 13 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,12) +"1" +widget_s.substring(13);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 15 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,14) +"1" +widget_s.substring(15);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 16 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,15) +"1" +widget_s.substring(16);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 18 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,17) +"1" +widget_s.substring(18);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 19 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,18) +"1" +widget_s.substring(19);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 21 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,20) +"1" +widget_s.substring(21);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 22 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,21) +"1" +widget_s.substring(22);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 23 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,22) +"1" +widget_s.substring(23);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 24 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,23) +"1" +widget_s.substring(24);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 26 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,25) +"1" +widget_s.substring(26);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 29 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,28) +"1" +widget_s.substring(29);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 31 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,30) +"1" +widget_s.substring(31);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 32 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,31) + "1"+widget_s.substring(32);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 33 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,32) +"1" +widget_s.substring(33);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 35 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,34) +"1" +widget_s.substring(35);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 36 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,35) +"1" +widget_s.substring(36);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 37 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,36) +"1" +widget_s.substring(37);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 38 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,37) +"1" +widget_s.substring(38);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
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
                Toast toast= Toast.makeText(getApplicationContext()," pin 40 selected",Toast.LENGTH_SHORT);
                toast.show();
                widget_s = widget_s.substring(0,39) +"1" +widget_s.substring(40);
                Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
                toast2.show();
                //ajout button dans btnword
                btnWord[39] = rbtn40;
                BtnWord.add(rbtn40);
                pin_config = "P-40";
                pin_config2 = pin_config2 +"40-";
            }
        });
    }
    private View.OnClickListener AddBitBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AddBitRBtnClicked();
        }
    };
    private View.OnClickListener AddByteBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AddByteRBtnClicked();
        }
    };




    private void RenmoveBtnClicked(){
        Toast toast2= Toast.makeText(getApplicationContext(),widget_s,Toast.LENGTH_SHORT);
        toast2.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
        toast2.show();

    }
    private void AddBitRBtnClicked(){
        widget_s = widget_s.substring(0,40)+"i" ;


        try {
            saveInterne_input();
            saveInterne_output();
        }catch (IOException e){

        }

        widget_s= widget_s.replaceAll("1","P");
        String widgets = widget_s.substring(0,40);
        widget_input = widgets;
        for(int i=0;i<widget_input.length();i++){
            if(widget_input.charAt(i)=='P'){
                PINConfig pi = new PINConfig(false,true,i,'P',String.valueOf(i+1));
                btnWorld2[i] = pi;
                //btnWord[i].setButtonTintList("#248d51");
                btnWord[i].setButtonTintList(ColorStateList.valueOf(Color.GRAY));
                //btnWord[i].setPadding(31, 0, 0, 0);
                //btnWord[i].setVisibility(View.INVISIBLE);
                //btnWord[i].setVisibility(View.GONE);
                pin_config2 = "";

            }
        }


    }
    private void AddBitwBtnClicked(){
        widget_s = widget_s.substring(0,40)+"0" ;

        try {
            saveInterne_input();
            saveInterne_output();
        }catch (IOException e){

        }

        widget_s= widget_s.replaceAll("1","P");
        String widgets = widget_s.substring(0,40);
        widget_output = widgets;
        for(int i=0;i<widget_output.length();i++){
            if(widget_output.charAt(i)=='P'){
                PINConfig pi = new PINConfig(false,true,i,'P',String.valueOf(i+1));
                btnWorld2[i] = pi;
                btnWord[i].setButtonTintList(ColorStateList.valueOf(Color.GRAY));
            }
        }

    }
    private void AddByteRBtnClicked(){
        String[] array_pin = pin_config2.split("-");
        //widget_s = widget_s.substring(0,40)+"i" ;
        int Icount=array_pin.length;
        for(int i = 0; i<array_pin.length; i++ ){
            String k = "G-"+ String.valueOf(array_pin[i]);
            Target2.add(k);
        }

        if(Icount==8){

            widget_s= widget_s.replaceAll("1","G");
            String widgets = widget_s.substring(0,40);
            widget_input = widgets;
            for(int i=0;i<widget_input.length();i++){
                if(widget_input.charAt(i)=='G'){
                    PINConfig pi = new PINConfig(false,true,i,'G',String.valueOf(i+1));
                    btnWorld2[i] = pi;
                    btnWord[i].setButtonTintList(ColorStateList.valueOf(Color.GRAY));
                }
            }

            //Target2.add(pin_config);
            try {
                saveInterne_input();
                saveInterne_output();
            }catch (IOException e){

            }

        }
        else{
            Toast toast= Toast.makeText(getApplicationContext(),"un byte est juste de 8 bits",Toast.LENGTH_SHORT);
            toast.show();
        }

    }
    private void AddByteWBtnClicked(){
        widget_s = widget_s.substring(0,40)+"o" ;
        String[] array_pin = pin_config2.split("-");
        //widget_s = widget_s.substring(0,40)+"i" ;
        int Icount=array_pin.length;
        for(int i = 0; i<array_pin.length; i++ ){
            String k = "G-"+ String.valueOf(array_pin[i]);
            Target2.add(k);
        }

        if(Icount==8){

            widget_s= widget_s.replaceAll("1","G");
            String widgets = widget_s.substring(0,40);
            widget_output = widgets;
            for(int i=0;i<widget_output.length();i++){
                if(widget_output.charAt(i)=='G'){
                    PINConfig pi = new PINConfig(false,true,i,'G',String.valueOf(i+1));
                    btnWorld2[i] = pi;
                    btnWord[i].setButtonTintList(ColorStateList.valueOf(Color.GRAY));
                }
            }

            //Target2.add(pin_config);
            try {
                saveInterne_input();
                saveInterne_output();
            }catch (IOException e){

            }

        }
        else{
            Toast toast= Toast.makeText(getApplicationContext(),"un byte est juste de 8 bits",Toast.LENGTH_SHORT);
            toast.show();
        }
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
    private void changeNipcliked(){
        if(etat_btn_change_nip ==1){
            mytext.setVisibility(View.VISIBLE);

        }
        Toast toast= Toast.makeText(getApplicationContext(),NIP,Toast.LENGTH_SHORT);
        toast.show();

    }
    private void loadbtncliked(){

        sendStringMessage("loadConfig");
        sendStringMessage(selected);
        try{
            recieved = receiveStringMessage();
            //widget_output = receiveStringMessage();
        }
        catch(IOException e){

        }
        Toast toast= Toast.makeText(getApplicationContext(),recieved,Toast.LENGTH_SHORT);
        toast.show();


    }
    private void btnsavecliked(){
        if(etat_btn_save==1){
            sendStringMessage("saveConfig");
            sendStringMessage(config_name);
            sendStringMessage(widget_input);
            sendStringMessage(widget_output);

        }

    }
    private void sendStringMessage(String mot){
        try {
            OutputStream btOutputStream = Connexion.btSocket.getOutputStream();
            btOutputStream.write(mot.getBytes());
            //btSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    private String receiveStringMessage()throws IOException{
        mmBuffer3 = new byte[1024];
        ByteArrayInputStream input = new ByteArrayInputStream(mmBuffer3);
        InputStream inputStream = btSocket.getInputStream();
        inputStream.read( mmBuffer3);
        String b = new String(mmBuffer3);
        return b;

    }

    public void ChangeView(Class activity) {
        Intent intent = new Intent(getApplicationContext(), activity);
        startActivity(intent);
    }

}
