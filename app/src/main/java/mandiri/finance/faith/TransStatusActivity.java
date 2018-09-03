package mandiri.finance.faith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class TransStatusActivity extends DashBoardActivity {
			
	private DataSource datasource;
    private SQLiteDatabase db;
    private Context mContext;
    
    private SharedPreferences generalPrefs;
	private boolean flag = false;
	private SharedPreferences.Editor prefsEditor;
	private Dialog dialog;
	TableLayout table;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = TransStatusActivity.this;
                
        generalPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefsEditor = generalPrefs.edit();
        
        dialog = new Dialog(mContext, R.style.lightbox_dialog);
        dialog.setContentView(R.layout.lightbox_dialog);
    	((TextView)dialog.findViewById(R.id.dialogText)).setText("Loading...");
    	dialog.setCancelable(false);
        
    	try {
	   		String sessionStart;
    	   	String now;
    	   	String timeOut;
    	   	long diffInMin = 0;
    	   	
    	   	Calendar c = Calendar.getInstance();
    	   	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            now = df.format(c.getTime());
            
            timeOut = generalPrefs.getString("timeOut", null);
    	   	sessionStart = generalPrefs.getString("sessionStart", null);
    	   	
			Date dateStart = df.parse(sessionStart);
			Date dateEnd = df.parse(now);
			
			diffInMin = (dateEnd.getTime()/60000) - (dateStart.getTime()/60000);
			
			if ((int)diffInMin > Integer.valueOf(timeOut) || (int)diffInMin < 0){
				prefsEditor.putBoolean("finish", true).commit();
    	   		finish();
    	   		
    	   		Intent myintent = new Intent(mContext,HomeActivity.class);
    	   		myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	   		startActivity(myintent);
    	   		return;
    	   	}
    	   	
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		datasource = new DataSource(mContext);
		db = datasource.getWritableDatabase();
		
		setContentView(R.layout.trans_status);
        setHeader(getString(R.string.StatusTrans), true);
        
		flag = true;  
		
    }
    
    public void ViewReportClick(View v)
    {  	
    	ScrollView sv = (ScrollView) findViewById(R.id.sv);
    	Button btnRefresh = (Button) findViewById(R.id.btnRefresh);
    	
    	btnRefresh.setText("Refresh");
    	
    	if(table!=null){
    		sv.removeView(table);
    	}
    	
    	table = new TableLayout(this);
        table.setStretchAllColumns(true);
        table.setShrinkAllColumns(true);
        
        TableRow rowTitle = new TableRow(this);
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        TableRow rowLabel = new TableRow(this);
        
        TextView title = new TextView(this);    
        title.setText("Pending Transactions Report");
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = 2;
        rowTitle.addView(title, params);
        table.addView(rowTitle);
        
        TextView tvModule = new TextView(this);
        tvModule.setText("Module");
        tvModule.setTypeface(Typeface.DEFAULT_BOLD);
        tvModule.setPadding(5, 10, 0, 0);
        tvModule.setPaintFlags(tvModule.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        rowLabel.addView(tvModule);
                
        TextView tvAccount = new TextView(this);  
        tvAccount.setText("Account");
        tvAccount.setTypeface(Typeface.DEFAULT_BOLD);
        tvAccount.setGravity(Gravity.CENTER);
        tvAccount.setPadding(0, 10, 0, 0);
        tvAccount.setPaintFlags(tvAccount.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        rowLabel.addView(tvAccount);       
        
        table.addView(rowLabel);
        
        TableRow row;
                
        Cursor c = db.rawQuery("SELECT case when moduleid = '1' then 'Prospect' " +
        		"when moduleid = '2' then 'Survey'" +
        		"when moduleid = '3' then 'Collection'" +
        		"else 'Survey Location' end as module,count(*)account " +
        		"FROM result group by moduleid order by moduleid", null);


		Log.d(TAG, "ViewReportClick: "+db.rawQuery("SELECT case when moduleid = '1' then 'Prospect' " +
				"when moduleid = '2' then 'Survey'" +
				"when moduleid = '3' then 'Collection'" +
				"else 'Survey Location' end as module,count(*)account " +
				"FROM result group by moduleid order by moduleid", null));

		//Cursor c = db.rawQuery("SELECT sourceid, count(*)account " +
        //		"FROM locationsource group by sourceid order by sourceid", null);

		try {
	        
	        while (c.moveToNext()) {
											
				row = new TableRow(mContext);
     			
     			TextView t1 = new TextView(mContext);
     			t1.setTextColor(getResources().getColor(R.color.label_color));
     			TextView t2 = new TextView(mContext);
     			t2.setTextColor(getResources().getColor(R.color.label_color));
     			
     			t1.setPadding(5, 0, 0, 3);
     			t2.setPadding(0, 0, 0, 3);
     			
     			t1.setText(c.getString(0));
     			t2.setText(c.getString(1));
     			     			
     			t1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
     			t2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
     			
     			t2.setGravity(Gravity.CENTER);
     			                 			
     			row.addView(t1);
     			row.addView(t2);
     			
     			table.addView(row, params);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		} 
		
        
		sv.addView(table);
    }
    
    @Override
    public void onBackPressed() {
    	
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
	            	   return;
	               }
	           })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, final int id) {
	                   dialog.dismiss();	                   
	               }
	           });
	    final AlertDialog alert = builder.create();
	    alert.show();
    }
    
        
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	
    	if (db != null){
    		if (db.isOpen()){
        		db.close();	
        	}
    	}
    	
    	if(datasource != null){
        	datasource.close();
        }
    	
    	if (dialog != null){
    		if(dialog.isShowing()){
        		dialog.dismiss();
        	}
    	}
    	
    	System.gc();
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	
    	if (dialog != null){
    		if(dialog.isShowing()){
        		dialog.dismiss();
        	}
    	}
    	    
    	flag = false;
    	String now;
		Calendar c = Calendar.getInstance();
	   	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    now = df.format(c.getTime());
	    generalPrefs.edit().putString("sessionStart", String.valueOf(now)).commit();	    
   	}
    
    @Override
    public void onResume() {
    	super.onResume();
    	    		
    	if (flag == false){
    		try {
    	   		String sessionStart;
        	   	String now;
        	   	String timeOut;
        	   	long diffInMin = 0;
        	   	
        	   	Calendar c = Calendar.getInstance();
        	   	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                now = df.format(c.getTime());
                
                timeOut = generalPrefs.getString("timeOut", null);
        	   	sessionStart = generalPrefs.getString("sessionStart", null);
        	   	
    			Date dateStart = df.parse(sessionStart);
    			Date dateEnd = df.parse(now);
    			
    			diffInMin = (dateEnd.getTime()/60000) - (dateStart.getTime()/60000);
    			        	   	
        	   	if ((int)diffInMin > Integer.valueOf(timeOut) || (int)diffInMin < 0){
        	   		Toast.makeText(mContext,"Your session has been expired, please re login for security purpose", Toast.LENGTH_LONG).show();
        	   		prefsEditor.putBoolean("finish", true).commit();
             	   	
        	   		finish();
        	   		Intent myintent = new Intent(mContext,HomeActivity.class);
        	   		myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	   		startActivity(myintent);
        	   	}
        	   	
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}
    	}
    }

}