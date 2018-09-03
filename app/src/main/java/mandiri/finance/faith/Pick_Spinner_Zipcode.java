package mandiri.finance.faith;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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

/**
 * Created by dwi.prayogo on 11/3/2017.
 */

public class Pick_Spinner_Zipcode extends LinearLayout {
    private TextView label,label2,label3,label4;
    private LinearLayout ll;
    private String title;
    private Button btnShow;
    private Dialog dialog;
    private ListView lv;
    private EditText searchText;
    String TAG = "Pick_Spinner_Zipcode";
    private Spinner
            spin_kota,spin_kecamatan,spin_kelurahan,spin_zipcode;
    private String val_kota,
                    val_kecamatan,
                    val_kelurahan,
                    val_zipcode;
    private DataSource datasource;
    private SQLiteDatabase db;

    private    String idkota="";
    private    String nmkota="";
    private    String idsalesman = "";
    private    String nmkecamatan="";
    private    String idSalesSpv ="";
    private    String nmkelurahan="";
    private    String idSalesKoor ="";
    private    String nmzipcode="";
    private    String idSalesAdm ="";
    private    String nmSalesAdm="";

    int position=0;
    private String branchID;
    private ArrayAdapter<String> adapter_supplier;
    private ArrayAdapter<String> adapter_salesman;
    private ArrayAdapter<String> adapter_salesspv;
    private ArrayAdapter<String> adapter_koordinasi;
    private ArrayAdapter<String> adapter_admin;

    private SharedPreferences generalPrefs;
    private SharedPreferences.Editor prefsEditor;
    Context mcontext;

    public Pick_Spinner_Zipcode(Context context, String labelText) {
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
        label3 = new TextView(context);
        label4 = new TextView(context);
        label.setText(labelText);
        label.setTextColor(getResources().getColor(R.color.label_color));
        label2.setText("Kecamatan");
        label2.setTextColor(getResources().getColor(R.color.label_color));
        label3.setText("Kelurahan");
        label3.setTextColor(getResources().getColor(R.color.label_color));
        label4.setText("ZipCode");
        label4.setTextColor(getResources().getColor(R.color.label_color));


        val_kota = null;
        val_kecamatan = null;
        val_kelurahan = null;
        val_zipcode = null;

        title = label.getText().toString();
        spin_kota = new Spinner(context,Spinner.MODE_DROPDOWN);
        spin_kecamatan =  new Spinner(context,Spinner.MODE_DROPDOWN);
        spin_kelurahan =  new Spinner(context,Spinner.MODE_DROPDOWN);
        spin_zipcode = new Spinner(context,Spinner.MODE_DROPDOWN);


        try {

            ArrayList<String> myResArray1 = new ArrayList<>();
            if(myResArray1!=null){
                myResArray1.clear();
            }
            myResArray1.add("-Zipcode-");
            myResArray1.addAll(datasource.getAllZipcode2());

            adapter_supplier = new ArrayAdapter<>(context,R.layout.spinner,myResArray1);
            adapter_supplier.notifyDataSetChanged();
            spin_zipcode.setAdapter(adapter_supplier);

            spin_zipcode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(!spin_zipcode.getSelectedItem().toString().equals("-Zipcode-")) {
                        nmzipcode= spin_zipcode.getSelectedItem().toString();
                        Log.d(TAG, "onItemSelected: Kota -- "+nmzipcode);
                        spin2(nmzipcode);
                    }

                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });




//            ArrayList<String> myResArray2 = new ArrayList<>();
//            if(myResArray2!=null){
//                myResArray2.clear();
//            }
//            Log.d(TAG, "Pick_Spinner1: idsupplier"+idsupplier);
//            myResArray2.add("-Salesman-");
//            myResArray2.addAll(datasource.getAllSalesman(branchID,idsupplier));
//            Log.d(TAG, "Pick_Spinner2: idsupplier"+idsupplier);
//            ArrayAdapter<String> adapter_salesman = new ArrayAdapter<>(context,R.layout.spinner,myResArray2);
//            spin_salesman.setAdapter(adapter_salesman);
//            try{
//                Log.d(TAG, "Pick_Spinner3: idsupplier"+idsupplier);
//                Namesalesman = datasource.getSalesmanName(idsupplier);
//                Log.d(TAG, "Pick_Spinner4: idsupplier"+idsupplier);
//                Log.d(TAG, "ceknamawilayah: "+Namesalesman + " -- " + myResArray2.size());
//                for (int ii = 0; ii < myResArray2.size(); ii++) {
//                    if (myResArray2.get(ii).trim()
//                            .equals(Namesalesman.trim())) {
//                        spin_salesman.setSelection(ii);
//                        break;
//                    }
//                }
//
//            }catch (Exception e){e.printStackTrace();}
//
//            spin_salesman.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(final AdapterView<?> parent, View view, int position, long id) {
//                    if(!spin_salesman.getSelectedItem().toString().equals("-Salesman-")) {
//                        if(!spin_salesman.getSelectedItem().toString().equals(" ")) {
//
//                        }
//
//                        nmsalesman= spin_salesman.getSelectedItem().toString();
//                        idsalesman=datasource.getSalesmanID(nmsalesman);
//                        Log.d(TAG, "onItemSelected: idsalesman "+idsalesman);
//
//                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });


        }catch (Exception e){

        }
        ll.addView(label4);
        ll.addView(spin_zipcode);
        ll.addView(label);
        ll.addView(spin_kota);
        ll.addView(label2);
        ll.addView(spin_kecamatan);
        ll.addView(label3);
        ll.addView(spin_kelurahan);




        this.addView(ll);

    }

    private void spin2(String id){
        ArrayList<String> myResArray2 = new ArrayList<>();
        if(myResArray2!=null){
            myResArray2.clear();
        }

        myResArray2.add("-Kota-");
        myResArray2.addAll(datasource.getAllKota2(id));



        adapter_salesman = new ArrayAdapter<>(mcontext,R.layout.spinner,myResArray2);
        adapter_salesman.notifyDataSetChanged();
        spin_kota.setAdapter(adapter_salesman);
        spin_kota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spin_kota.getSelectedItem().toString().equals("-Kota-")) {
                    nmkota= spin_kota.getSelectedItem().toString();
                    Log.d(TAG, "onItemSelected: Kota -- "+nmkota);

                    spin3(nmzipcode,nmkota);
                    Log.d(TAG, "onItemSelected: Kota "+nmkota);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void spin3(String zipcode,String kota){
        ArrayList<String> myResArray3 = new ArrayList<>();
        if(myResArray3!=null){
            myResArray3.clear();
        }
        Log.d(TAG, "Pick_Spinner1: nmsupplier"+nmkota);
        myResArray3.add("-Kecamatan-");
        myResArray3.addAll(datasource.getAllKecamatan2(zipcode,kota));
        Log.d(TAG, "Pick_Spinner2: nmsupplier"+nmkota);


        adapter_salesspv = new ArrayAdapter<>(mcontext,R.layout.spinner,myResArray3);
        adapter_salesspv.notifyDataSetChanged();
        spin_kecamatan.setAdapter(adapter_salesspv);
        spin_kecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spin_kecamatan.getSelectedItem().toString().equals("-Kecamatan-")) {
                    nmkecamatan= spin_kecamatan.getSelectedItem().toString();

                    Log.d(TAG, "onItemSelected: Kecamatan -- "+nmkecamatan);

                    spin4(nmzipcode,nmkota,nmkecamatan);
                    Log.d(TAG, "onItemSelected: Kecamatan "+nmkecamatan);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void spin4(String zipcode,String kota,String kecamatan){
        ArrayList<String> myResArray4 = new ArrayList<>();
        if(myResArray4!=null){
            myResArray4.clear();
        }

        myResArray4.add("-Kelurahan-");
        myResArray4.addAll(datasource.getAllKelurahan2(zipcode,kota,kecamatan));

        adapter_koordinasi = new ArrayAdapter<>(mcontext,R.layout.spinner,myResArray4);
        adapter_koordinasi.notifyDataSetChanged();
        spin_kelurahan.setAdapter(adapter_koordinasi);
        spin_kelurahan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spin_kelurahan.getSelectedItem().toString().equals("-Kelurahan-")) {
                    nmkelurahan= spin_kelurahan.getSelectedItem().toString();

                    Log.d(TAG, "onItemSelected: ZipCode -- "+nmkelurahan);
                    checkspinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void checkspinner(){
        String prod = spin_kota.getSelectedItem().toString();
        String prodoff = spin_kecamatan.getSelectedItem().toString();

        String supp = spin_kelurahan.getSelectedItem().toString();
        String salesm = spin_zipcode.getSelectedItem().toString();

        Log.d(TAG, "checkspinner: "+"prod"+prod+"prodoff"+prodoff+"supp"+supp+"salesm"+salesm);
        val_kota = prod ;
        val_kecamatan = prodoff;
        val_kelurahan = supp;
        val_zipcode = salesm;
    }




    public String getLabel()
    {
        return label.getText().toString();
    }

    public String getValue()
    {

        return  val_kota + val_kecamatan + val_kelurahan + val_zipcode;
        //return (String) val_kota;
    }

    public void setErrorMsg(String v)
    {
        btnShow.setError(v);
    }




}

