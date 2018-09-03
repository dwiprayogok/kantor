package mandiri.finance.faith;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class Pick_Ekonomi extends LinearLayout {
    private TextView label,label2;
    private LinearLayout ll;
    private String title;
    private Button btnShow;
    private Dialog dialog;
    private ListView lv;
    private EditText searchText;
    String TAG = "Pick_Ekonomi";
    private Spinner
            Spin_sektoreko1,Spin_sektoreko2;
    private String val_sektoreko1,
            val_sektoreko2;
    private    String idproduk="";
    private    String idprodOffering = "";
    private    String idproduk2="";
    private    String idprodOffering2 = "";
    private DataSource datasource;
    private SQLiteDatabase db;

    private    String nmsektoreko1="";
    private    String nmsektoreko2="";


    int position=0;
    private String branchID;
    private ArrayAdapter<String> adapter_sektoreko1;
    private ArrayAdapter<String> adapter_sektoreko2;
    private ArrayAdapter<String> adapter_koordinasi;


    private SharedPreferences generalPrefs;
    private SharedPreferences.Editor prefsEditor;
    Context mcontext;

    public Pick_Ekonomi(Context context, String labelText) {
        super(context);
        mcontext = context;
        datasource = new DataSource(context);
        db = datasource.getWritableDatabase();

        generalPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = generalPrefs.edit();

        branchID = generalPrefs.getString("branchID", null);
        ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        ll.setLayoutParams(params);
        ll.setPadding(4, 10, 4, 0);

        label = new TextView(context);
        label2 = new TextView(context);
        label.setText(labelText);
        label.setTextColor(getResources().getColor(R.color.label_color));
        label2.setText("Kecamatan");
        label2.setTextColor(getResources().getColor(R.color.label_color));



        val_sektoreko1 = null;
        val_sektoreko2 = null;


        title = label.getText().toString();
        Spin_sektoreko1 = new Spinner(context,Spinner.MODE_DROPDOWN);
        Spin_sektoreko2 =  new Spinner(context,Spinner.MODE_DROPDOWN);

        try {

            ArrayList<String> myResArray1 = new ArrayList<>();
            if(myResArray1!=null){
                myResArray1.clear();
            }
            myResArray1.add("Select One");
            myResArray1.addAll(datasource.getAllProduct("N"));

            adapter_sektoreko1 = new ArrayAdapter<>(context,R.layout.spinner,myResArray1);
            adapter_sektoreko1.notifyDataSetChanged();
            Spin_sektoreko1.setAdapter(adapter_sektoreko1);

            Spin_sektoreko1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(!Spin_sektoreko1.getSelectedItem().toString().equals("Select One")) {
                        nmsektoreko1= Spin_sektoreko1.getSelectedItem().toString();
                        idproduk=datasource.getProductID(nmsektoreko1);
                        Log.d(TAG, "onItemSelected: nmsektoreko1 idproduk  -- "+idproduk);
                        spin2(idproduk);


                    }

                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }catch (Exception e){

        }
        ll.addView(label);
        ll.addView(Spin_sektoreko1);
        ll.addView(label2);
        ll.addView(Spin_sektoreko2);




        this.addView(ll);

    }

    private void spin2(String id){
        ArrayList<String> myResArray2 = new ArrayList<>();
        if(myResArray2!=null){
            myResArray2.clear();
        }

        myResArray2.add("Select One");
        myResArray2.addAll(datasource.getAllproductoffering(id));

        adapter_sektoreko2 = new ArrayAdapter<>(mcontext,R.layout.spinner,myResArray2);
        adapter_sektoreko2.notifyDataSetChanged();
        Spin_sektoreko2.setAdapter(adapter_sektoreko2);
        Spin_sektoreko2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!Spin_sektoreko2.getSelectedItem().toString().equals("Select One")) {
                    nmsektoreko2= Spin_sektoreko2.getSelectedItem().toString();
                    Log.d(TAG, "onItemSelected: nmsektoreko2 -- "+nmsektoreko2);
                    idprodOffering=datasource.getProductofferingID(nmsektoreko2);
                    Log.d(TAG, "onItemSelected: idprodOffering nmsektoreko2 -- "+idprodOffering);
                    checkspinner();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    private void checkspinner(){
        String seko1 = Spin_sektoreko1.getSelectedItem().toString();
        Log.d(TAG, "checkspinner: seko1 "+ seko1);
        idproduk2=datasource.getProductID(seko1);
        Log.d(TAG, "checkspinner:idproduk2  " + idproduk2);

        String seko2 = Spin_sektoreko2.getSelectedItem().toString();
        Log.d(TAG, "checkspinner: seko2 "+ seko2);
        idprodOffering2=datasource.getProductofferingID(seko2);
        Log.d(TAG, "checkspinner:idprodOffering2  " + idprodOffering2);

        val_sektoreko1 = idproduk2 ;
        val_sektoreko2 = idprodOffering2;

    }




    public String getLabel()
    {
        return label.getText().toString();
    }

    public String getValueEko1()
    {

        return  val_sektoreko1 ;
        //return (String) val_kota;
    }

    public String getValueEko2()
    {

        return  val_sektoreko2 ;
        //return (String) val_kota;
    }



    public void setErrorMsg(String v)
    {
        if(Spin_sektoreko1.getSelectedItem().toString().equals("Select One")){
            TextView textView= (TextView)Spin_sektoreko1.getSelectedView();
            textView.setError(v);
            textView.setTextColor(Color.RED);
            textView.setText(v);
        }

        if(Spin_sektoreko2.getSelectedItem().toString().equals("Select One")){
            TextView textView= (TextView)Spin_sektoreko2.getSelectedView();
            textView.setError(v);
            textView.setTextColor(Color.RED);
            textView.setText(v);
        }
    }




}


