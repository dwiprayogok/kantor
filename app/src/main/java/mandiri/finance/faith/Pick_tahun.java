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
import java.util.Calendar;

/**
 * Created by dwi.prayogo on 11/8/2017.
 */

public class Pick_tahun extends LinearLayout {
    private TextView label;
    private LinearLayout ll;
    private String title;
    String TAG = "Pick_tahun";
    private Spinner
            spin_kota;
    private String val_kota;




    int position=0;
    private String branchID;
    private ArrayAdapter<String> adapter_supplier;
    String tahun22;

    private SharedPreferences generalPrefs;
    private SharedPreferences.Editor prefsEditor;
    Context mcontext;

    public Pick_tahun(Context context, String labelText,String N) {
        super(context);
        mcontext = context;

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
        label.setText(labelText);
        label.setTextColor(getResources().getColor(R.color.label_color));

        val_kota = null;

        title = label.getText().toString();
        spin_kota = new Spinner(context,Spinner.MODE_DIALOG);

        if(N.equals("N")){
            try {
                ArrayList<String> arrayListYear=new ArrayList<>();
                int year,startYear,endYear;

                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                startYear=year-2;
                endYear=startYear+2;


                arrayListYear.clear();
                arrayListYear.add("Select One");
                for(int i=startYear;i<=endYear;i++){

                    arrayListYear.add(String.valueOf(i));

                }

                spin_kota.setAdapter(new ArrayAdapter<String>(mcontext, R.layout.spinner, arrayListYear));

                spin_kota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        tahun22 = spin_kota.getSelectedItem().toString();
                        Log.d(TAG, "onItemSelected:tahun22 " + tahun22);
                        val_kota = tahun22;
                        Log.d(TAG, "onItemSelected: val_kota " + val_kota);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }catch (Exception e){

            }
        }else{
            try {
                ArrayList<String> arrayListYear=new ArrayList<>();
                int year,startYear,endYear;

                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                startYear=year-10;
                endYear=startYear+10;


                arrayListYear.clear();
                arrayListYear.add("Select One");
                for(int i=startYear;i<=endYear;i++){

                    arrayListYear.add(String.valueOf(i));

                }

                spin_kota.setAdapter(new ArrayAdapter<String>(mcontext, R.layout.spinner, arrayListYear));

                spin_kota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        tahun22 = spin_kota.getSelectedItem().toString();
                        Log.d(TAG, "onItemSelected:tahun22 " + tahun22);
                        val_kota = tahun22;
                        Log.d(TAG, "onItemSelected: val_kota " + val_kota);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }catch (Exception e){

            }
        }



        ll.addView(label);
        ll.addView(spin_kota);
        this.addView(ll);

    }



    public String getLabel()
    {
        return label.getText().toString();
    }

    public String getValue()
    {

        return  val_kota ;
        //
    }

    public void setErrorMsg(String v)
    {
        if(spin_kota.getSelectedItem().toString().equals("Select One")){
            TextView textView= (TextView)spin_kota.getSelectedView();
            textView.setError(v);
            textView.setTextColor(Color.RED);
            textView.setText(v);
        }
    }




}


