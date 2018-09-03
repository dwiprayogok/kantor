package mandiri.finance.faith;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import mandiri.finance.faith.Adapter.CustomAdapter;

/**
 * Created by dwi.prayogo on 10/23/2017.
 */

public class AddData extends AppCompatActivity {
    private Context mContext;
    ProgressBar progressBar_epd;
    private Toolbar toolbar;
    ImageView home,logout;
    Button tbh_sales,tbh_product,tbh_supplier,tbh_asset,tbh_prodoffering,cekemail;
    private int mProgressStatus = 0;
    private ProgressDialog mProgressDialog ;
    private DataSource datasource;
    private SQLiteDatabase db;

    String TAG = "AddData";
    String ukuransizekota="";
    private DataLocal datalocal;
    //String url = "https://mobileapp.mtf.co.id:48019/mobileapplication/AndroidFin.asmx";
    String result2 = "";
    private String isLoadProspectForm;
    private String isLoadAsset;
    private String isLoadSupplier;
    private String isLoadTempatLahir ;
    private String isLoadProduct;
    private String isLoadProductOffering;
    private SharedPreferences generalPrefs;
    private SharedPreferences.Editor prefsEditor;
    private String lastinsertasset = null;
    private String lastinsertzipcode = null;
    ProgressDialog progress;
    NodeList nodelist;
    DataLocal dl = new DataLocal();
    int position=0;
    private String branchID;
    EditText email2;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = AddData.this;
        datasource = new DataSource(mContext);
        db = datasource.getWritableDatabase();

        generalPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefsEditor = generalPrefs.edit();

        setContentView(R.layout.adddata);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setTitle("");
        }
        isLoadProspectForm = generalPrefs.getString("isLoadProspectForm", null);
        isLoadAsset = generalPrefs.getString("isLoadAsset", null);
        isLoadSupplier = generalPrefs.getString("isLoadSupplier", null);
        isLoadProduct = generalPrefs.getString("isLoadProduct", null);
        isLoadProductOffering = generalPrefs.getString("isLoadProductOffering", null);
        isLoadTempatLahir = generalPrefs.getString("isLoadTempatLahir",null);

        progress  = new ProgressDialog(this);
        branchID = generalPrefs.getString("branchID", null);

        home = (ImageView) findViewById(R.id.gmbarhome);
        logout = (ImageView) findViewById(R.id.gambarlogout);
        tbh_sales = (Button) findViewById(R.id.tbh_Sales);
        tbh_product = (Button) findViewById(R.id.tbh_product);
        tbh_supplier = (Button) findViewById(R.id.tbh_supplier);
        tbh_asset = (Button) findViewById(R.id.tbh_asset);
        tbh_prodoffering = (Button) findViewById(R.id.tbh_prodoffering);




        initbutton();

    }

    private void initbutton(){


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you really want to exit ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                finish();
                                Intent myintent = new Intent(mContext,HomeActivity.class);
                                myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(myintent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setTitle("Warning");
                builder.setMessage("Do you really want to logout ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                SharedPreferences.Editor prefsEditor = generalPrefs.edit();
                                prefsEditor.putBoolean("finish", true).commit();

                                finish();
                                Intent myintent = new Intent(mContext,HomeActivity.class);
                                myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(myintent);;
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();

            }
        });

        tbh_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new ambilSales().execute();

            }
        });



        tbh_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new ambilProduct().execute();
            }
        });

        tbh_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new ambilSupplier().execute();
            }
        });

        tbh_asset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ambilAsset().execute();


            }
        });

        tbh_prodoffering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ambilProductOffering().execute();


            }
        });



    }

    public class ambilSales extends AsyncTask<Void,Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setCancelable(false);
            progress.setTitle("Downloading");
            progress.setMessage("Get Data Sales..");
            progress.setIndeterminate(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setProgress(0);
            progress.show();


        }
        @Override
        protected Void doInBackground(Void... arg0) {

//            JsonParserClass jparser = new JsonParserClass();
//
//            String JsonObj =  jparser.getJson(url);
//
//            Log.d("Response: ambil", "> " + JsonObj);
//
//            if (JsonObj != null) {
//                try {
//
//                    JSONObject jsonObj = new JSONObject(JsonObj);
//                    JSONArray contacts = jsonObj.getJSONArray("tempatlahir");
//
//                    for (int i = 0; i < contacts.length(); i++) {
//                        JSONObject c = contacts.getJSONObject(i);
//
//                        Tempatlahir mm = new Tempatlahir();
//                        mm.setTempatID(c.getString("TempatID"));
//                        mm.setNamakota(c.getString("namakota"));
//
//                        Log.d("cekaja", "insertkota: "+c.getString("TIPE"));
//
//                        ContentValues values = new ContentValues();
//                        values.put("TempatID", c.getString("TempatID"));
//                        values.put("namakota", c.getString("namakota"));
//
//                        datasource.generateData(db, values, "tempatlahir");
//                        values.clear();
//                    }
//
//
//                } catch (final JSONException e) {
//                    Log.e(TAG, "Json parsing error: " + e.getMessage());
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(),
//                                    "Json parsing error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    });
//
//                }
//
//            } else {
//                Log.e(TAG, "Couldn't get json from server.");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(),
//                                "Couldn't get json from server. Check LogCat for possible errors!",
//                                Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
            try {
                datasource = new DataSource(mContext);
                db = datasource.getWritableDatabase();

                SharedPreferences generalPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                Calendar cl = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String myTime = df.format(cl.getTime());
                String theKey;

                theKey = dl.encrypt(myTime,getApplicationContext().getString(R.string.Key44));
                String branchID = dl.encrypt(generalPrefs.getString("branchID", null),myTime);

                URL localUrl = new URL(getString(R.string.url) + "/GetSalesData?a=" + URLEncoder.encode(branchID,"UTF-8") +
                        "&b=" + URLEncoder.encode(theKey,"UTF-8"));

                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db1 = dbf.newDocumentBuilder();
                // Download the XML file
                Document doc = db1.parse(new InputSource(localUrl.openStream()));
                doc.getDocumentElement().normalize();
                // Locate the Tag Name
                nodelist = doc.getElementsByTagName("Sales");
                Log.d(TAG, "doInBackground:nodelistnya "+nodelist);

                datasource.deletesales();
                datasource.insertSales(nodelist);

                float f = ((float)datasource.getSalesnewModelCount()/nodelist.getLength())/20;
                final int process=(int)f;

                publishProgress(process);

                Log.d(TAG, "cek proses"+process);




            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }finally{
                datasource.close();
                db.close();
            }

            return null;

        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int xx = Integer.parseInt(values[0].toString());
            if(xx>0) {

                progress.setMessage("get Data Sales..... "+String.valueOf(xx)+"%");
                progress.setProgress(values[0]);
            }

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(progress.isShowing()){
                progress.dismiss();
            }
            Toast.makeText(getApplicationContext(),"Sukses DI Download", Toast.LENGTH_SHORT).show();


        }

    }

    public class ambilProduct extends AsyncTask<Void,Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setCancelable(false);
            progress.setTitle("Downloading");
            progress.setMessage("Get Data Product..");
            progress.setIndeterminate(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setProgress(0);
            progress.show();


        }
        @Override
        protected Void doInBackground(Void... arg0) {

//            JsonParserClass jparser = new JsonParserClass();
//
//            String JsonObj =  jparser.getJson(url);
//
//            Log.d("Response: ambil", "> " + JsonObj);
//
//            if (JsonObj != null) {
//                try {
//
//                    JSONObject jsonObj = new JSONObject(JsonObj);
//                    JSONArray contacts = jsonObj.getJSONArray("tempatlahir");
//
//                    for (int i = 0; i < contacts.length(); i++) {
//                        JSONObject c = contacts.getJSONObject(i);
//
//                        Tempatlahir mm = new Tempatlahir();
//                        mm.setTempatID(c.getString("TempatID"));
//                        mm.setNamakota(c.getString("namakota"));
//
//                        Log.d("cekaja", "insertkota: "+c.getString("TIPE"));
//
//                        ContentValues values = new ContentValues();
//                        values.put("TempatID", c.getString("TempatID"));
//                        values.put("namakota", c.getString("namakota"));
//
//                        datasource.generateData(db, values, "tempatlahir");
//                        values.clear();
//                    }
//
//
//                } catch (final JSONException e) {
//                    Log.e(TAG, "Json parsing error: " + e.getMessage());
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(),
//                                    "Json parsing error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    });
//
//                }
//
//            } else {
//                Log.e(TAG, "Couldn't get json from server.");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(),
//                                "Couldn't get json from server. Check LogCat for possible errors!",
//                                Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
            try {
                datasource = new DataSource(mContext);
                db = datasource.getWritableDatabase();
                SharedPreferences generalPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                Calendar cl = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
                String myTime = df.format(cl.getTime());
                String theKey;

                theKey = dl.encrypt(myTime,getApplicationContext().getString(R.string.Key44));
                String branchID = dl.encrypt(generalPrefs.getString("branchID", null),myTime);

                URL localUrl = new URL(getString(R.string.url) + "/GetProductData?a=" + URLEncoder.encode(branchID,"UTF-8") +
                        "&b=" + URLEncoder.encode(theKey,"UTF-8"));
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db1 = dbf.newDocumentBuilder();
                // Download the XML file
                Document doc = db1.parse(new InputSource(localUrl.openStream()));
                doc.getDocumentElement().normalize();
                // Locate the Tag Name
                nodelist = doc.getElementsByTagName("Product");
                Log.d(TAG, "doInBackground:nodelistnya "+nodelist);

                datasource.deleteProduct();
                datasource.insertProduct(nodelist);

                float f = ((float)datasource.getProductModelCount()/nodelist.getLength())/20;
                final int process=(int)f;

                publishProgress(process);

                Log.d(TAG, "cek proses"+process);




            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }finally{
                datasource.close();
                db.close();
            }

            return null;

        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int xx = Integer.parseInt(values[0].toString());
            if(xx>0) {

                progress.setMessage("get Data Product..... "+String.valueOf(xx)+"%");
                progress.setProgress(values[0]);
            }

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(progress.isShowing()){
                progress.dismiss();
            }
            Toast.makeText(getApplicationContext(),"Sukses DI Download Product", Toast.LENGTH_SHORT).show();


        }

    }


    public class ambilSupplier extends AsyncTask<Void,Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setCancelable(false);
            progress.setTitle("Downloading");
            progress.setMessage("Get Data Supplier..");
            progress.setIndeterminate(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setProgress(0);
            progress.show();


        }
        @Override
        protected Void doInBackground(Void... arg0) {

//            JsonParserClass jparser = new JsonParserClass();
//
//            String JsonObj =  jparser.getJson(url);
//
//            Log.d("Response: ambil", "> " + JsonObj);
//
//            if (JsonObj != null) {
//                try {
//
//                    JSONObject jsonObj = new JSONObject(JsonObj);
//                    JSONArray contacts = jsonObj.getJSONArray("tempatlahir");
//
//                    for (int i = 0; i < contacts.length(); i++) {
//                        JSONObject c = contacts.getJSONObject(i);
//
//                        Tempatlahir mm = new Tempatlahir();
//                        mm.setTempatID(c.getString("TempatID"));
//                        mm.setNamakota(c.getString("namakota"));
//
//                        Log.d("cekaja", "insertkota: "+c.getString("TIPE"));
//
//                        ContentValues values = new ContentValues();
//                        values.put("TempatID", c.getString("TempatID"));
//                        values.put("namakota", c.getString("namakota"));
//
//                        datasource.generateData(db, values, "tempatlahir");
//                        values.clear();
//                    }
//
//
//                } catch (final JSONException e) {
//                    Log.e(TAG, "Json parsing error: " + e.getMessage());
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(),
//                                    "Json parsing error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    });
//
//                }
//
//            } else {
//                Log.e(TAG, "Couldn't get json from server.");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(),
//                                "Couldn't get json from server. Check LogCat for possible errors!",
//                                Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
            try {
                datasource = new DataSource(mContext);
                db = datasource.getWritableDatabase();
                ArrayList<String> supplierExists = new ArrayList<String>();



                String [] supplierExistsArray = null;
                StringBuilder sbl = new StringBuilder();
                sbl.append("");

                Cursor cr = db.rawQuery("SELECT supplierid FROM supplier", null);
                try{
                    while(cr.moveToNext())
                    {
                        supplierExists.add(cr.getString(0));
                    }
                }
                finally{
                    cr.close();
                }

                if (supplierExists != null){

                    supplierExistsArray = supplierExists.toArray(new String[supplierExists.size()]);

                    for (int i = 0; i < supplierExistsArray.length; i++) {
                        if(i == 0){
                            sbl.append(supplierExistsArray[i]);
                        }else{
                            sbl.append("," +supplierExistsArray[i]);
                        }
                    }
                }
                SharedPreferences generalPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                Calendar cl = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
                String myTime = df.format(cl.getTime());
                String theKey;

                theKey = dl.encrypt(myTime,getApplicationContext().getString(R.string.Key44));
                String branchID = dl.encrypt(generalPrefs.getString("branchID", null),myTime);
                String supplierid = dl.encrypt(sbl.toString(),myTime);

                URL localUrl = new URL(getString(R.string.url) + "/GetSupplier_New?a=" + URLEncoder.encode(branchID,"UTF-8") +
                        "&b=" + URLEncoder.encode(supplierid,"UTF-8")+
                        "&c=" + URLEncoder.encode(theKey,"UTF-8"));

                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db1 = dbf.newDocumentBuilder();
                // Download the XML file
                Document doc = db1.parse(new InputSource(localUrl.openStream()));
                doc.getDocumentElement().normalize();
                // Locate the Tag Name
                nodelist = doc.getElementsByTagName("data");
                Log.d(TAG, "doInBackground:nodelistnya "+nodelist);

                datasource.deleteSupplier();
                datasource.insertSupplier(nodelist);

                float f = ((float)datasource.getSupplierModelCount()/nodelist.getLength())/20;
                final int process=(int)f;

                publishProgress(process);

                Log.d(TAG, "cek proses"+process);




            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }finally{
                datasource.close();
                db.close();
            }

            return null;

        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int xx = Integer.parseInt(values[0].toString());
            if(xx>0) {

                progress.setMessage("get Data Supplier..... "+String.valueOf(xx)+"%");
                progress.setProgress(values[0]);
            }

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(progress.isShowing()){
                progress.dismiss();
            }
            Toast.makeText(getApplicationContext(),"Sukses DI Download Supplier", Toast.LENGTH_SHORT).show();


        }

    }


    public class ambilAsset extends AsyncTask<Void,Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setCancelable(false);
            progress.setTitle("Downloading");
            progress.setMessage("Get Data Asset..");
            progress.setIndeterminate(false);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setProgress(0);
            progress.show();


        }
        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                datasource = new DataSource(mContext);
                db = datasource.getWritableDatabase();
                ContentValues cv = new ContentValues();

                Cursor cr = db.rawQuery("SELECT id FROM asset", null);
                try{
                    if(cr.moveToFirst())
                    {
                        datasource.deleteData(db, null, "asset");
                    }
                }
                finally{
                    cr.close();
                }

                InputStream in = null;


                URL localUrl = new URL(getString(R.string.url) + "/GetAsset? ");

                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db1 = dbf.newDocumentBuilder();
                Document doc = db1.parse(new InputSource(localUrl.openStream()));
                doc.getDocumentElement().normalize();
                nodelist = doc.getElementsByTagName("field");
                Log.d(TAG, "doInBackground:nodelistnya "+nodelist);

                datasource.deleteassetdata();
                datasource.insertAssetData(nodelist);

                float f = ((float)datasource.getAssetDataCount()/nodelist.getLength())/20;
                final int process=(int)f;

                publishProgress(process);

                Log.d(TAG, "cek proses"+process);


            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }finally{
                datasource.close();
                db.close();
            }

            return null;
            }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int xx = Integer.parseInt(values[0].toString());
            if(xx>0) {

                progress.setMessage("get Data Asset..... "+String.valueOf(xx)+"%");
                progress.setProgress(values[0]);
            }

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(progress.isShowing()){
                progress.dismiss();
            }
            Toast.makeText(getApplicationContext(),"Sukses DI Download Asset", Toast.LENGTH_SHORT).show();


        }

    }


    public class ambilProductOffering extends AsyncTask<Void,Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setCancelable(false);
            progress.setTitle("Downloading");
            progress.setMessage("Get Data Product Offering..");
            progress.setIndeterminate(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setProgress(0);
            progress.show();


        }
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                datasource = new DataSource(mContext);
                db = datasource.getWritableDatabase();
                SharedPreferences generalPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                Calendar cl = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
                String myTime = df.format(cl.getTime());
                String theKey;

                theKey = dl.encrypt(myTime,getApplicationContext().getString(R.string.Key44));
                String branchID = dl.encrypt(generalPrefs.getString("branchID", null),myTime);

                URL localUrl = new URL(getString(R.string.url) + "/GetProductOfferingData?a=" + URLEncoder.encode(branchID,"UTF-8") +
                        "&b=" + URLEncoder.encode(theKey,"UTF-8"));
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db1 = dbf.newDocumentBuilder();
                // Download the XML file
                Document doc = db1.parse(new InputSource(localUrl.openStream()));
                doc.getDocumentElement().normalize();
                // Locate the Tag Name
                nodelist = doc.getElementsByTagName("ProductOffering");
                Log.d(TAG, "doInBackground:nodelistnya "+nodelist);

                datasource.deleteProductOffering();
                datasource.insertProductOffering(nodelist);

                float f = ((float)datasource.getProductOfferingModelCount()/nodelist.getLength())/20;
                final int process=(int)f;

                publishProgress(process);

                Log.d(TAG, "cek proses"+process);




            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }finally{
                datasource.close();
                db.close();
            }

            return null;

        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int xx = Integer.parseInt(values[0].toString());
            if(xx>0) {

                progress.setMessage("get Data Product Offering..... "+String.valueOf(xx)+"%");
                progress.setProgress(values[0]);
            }

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(progress.isShowing()){
                progress.dismiss();
            }
            Toast.makeText(getApplicationContext(),"Sukses DI Download Product Offering", Toast.LENGTH_SHORT).show();


        }

    }























    private class ProgressBarAsync extends AsyncTask<Void, Integer, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressStatus = 0;
        }


        @Override
        protected Void doInBackground(Void...params) {
            while(mProgressStatus<100){
                try{

                    mProgressStatus++;
                    publishProgress(mProgressStatus/10);
                    Thread.sleep(100);

                }catch(Exception e){
                    Log.d("Exception", e.toString());
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressDialog.setProgress(mProgressStatus);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();
        }
    }

}
