package mandiri.finance.faith;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.*;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class HomeActivity extends DashBoardActivity {
	private SharedPreferences generalPrefs;
	private boolean flag = false; 
	private Context mContext;
	String TAG = "HomeActivity";
	TextView versiapp;
	private DataSource datasource;
	private SQLiteDatabase db;
	DataLocal dl = new DataLocal();
	NodeList nodelist;
	ProgressDialog progress;
	private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
	private String tglserver;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = HomeActivity.this;
        generalPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		datasource = new DataSource(mContext);
		db = datasource.getWritableDatabase();
        try {
	   		String sessionStart;
    	   	String now;
    	   	String timeOut;
    	   	long diffInMin = 0;
    	   	boolean finish = generalPrefs.getBoolean("finish", false);
    	   	
    	   	Calendar c = Calendar.getInstance();
    	   	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
            now = df.format(c.getTime());
            
            timeOut = generalPrefs.getString("timeOut", null);
    	   	sessionStart = generalPrefs.getString("sessionStart", null);

			Log.d(TAG, "onCreate: "+timeOut +" dan " +sessionStart );

			Date dateStart = df.parse(sessionStart);
			Date dateEnd = df.parse(now);
			
			diffInMin = (dateEnd.getTime()/60000) - (dateStart.getTime()/60000);
			
    	   	if ((int)diffInMin > Integer.valueOf(timeOut) || (int)diffInMin < 0 || finish){
    	   		finish();
    	   		Intent myintent = new Intent(mContext,LoginActivity.class);
    	   		myintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    	   		startActivity(myintent);
    	   	}
    	   	
		} catch (ParseException e) {
			e.printStackTrace();
		}

		tglserver = generalPrefs.getString("tglserver", null);
		Log.d(TAG, "onCreate:tglserver " + tglserver);

		PackageManager manager = mContext.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo("mandiri.finance.faith", 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		progress  = new ProgressDialog(this);
		int code = info.versionCode;
		final String versionName = info.versionName;
		Log.d(TAG, "onCreate: code "+ code + "versionName " + versionName);
		flag = true;
        setContentView(R.layout.main);        
        setHeader(getString(R.string.HomeActivityTitle), false);
        versiapp = (TextView) findViewById(R.id.versiapp);
        versiapp.setText(versionName);


                
        String[] authorizedMenus = generalPrefs.getString("authorizedMenu", null).split("\\,");



		Log.d(TAG, "onCreate: authorizedMenu  " + generalPrefs.getString("authorizedMenu", null));
		Button[] buttons = new Button[3];
        
        for(int i=0; i<authorizedMenus.length; i++) {
        	{
	        	String buttonID = "main_btn" + authorizedMenus[i].trim();
	        		        	
	        	int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
	        	buttons[i] = ((Button) findViewById(resID));
	        	
	        	if(authorizedMenus[i].trim().length() > 0){
	        		if(authorizedMenus[i].trim().equals("1")){
		        		buttons[i].setTextColor(getResources().getColor(R.color.foreground1));
		        		buttons[i].setEnabled(true);
			        	buttons[i].setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sales, 0, 0);
		        	}else if(authorizedMenus[i].trim().equals("2")){
		        		buttons[i].setTextColor(getResources().getColor(R.color.foreground1));
		        		buttons[i].setEnabled(true);
			        	buttons[i].setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.survey, 0, 0);
		        	}
		        	else if(authorizedMenus[i].trim().equals("3")){
		        		buttons[i].setTextColor(getResources().getColor(R.color.foreground1));
		        		buttons[i].setEnabled(true);
			        	buttons[i].setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.collection, 0, 0);
		        	}
		        	else if(authorizedMenus[i].trim().equals("6")){
		        		buttons[i].setTextColor(getResources().getColor(R.color.foreground1));
		        		buttons[i].setEnabled(true);
			        	buttons[i].setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.location, 0, 0);
		        	}
	        	}	        	
        	}        	
        }
		loadPhoto();

    }


	private void loadPhoto() {

		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
				!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission
				.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
			requestReadPhoneStatePermission();
		} else {
			//Toast.makeText(mContext, "Camera Already Active...!!!", Toast.LENGTH_SHORT).show();
		}
	}

	private void requestReadPhoneStatePermission() {
		if (ActivityCompat.shouldShowRequestPermissionRationale(this,
				android.Manifest.permission.CAMERA)) {
			new AlertDialog.Builder(HomeActivity.this)
					.setTitle("Permission Request")
					.setMessage("Check permission")
					.setCancelable(false)
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							//re-request
							ActivityCompat.requestPermissions(HomeActivity.this,
									new String[]{
											android.Manifest.permission.CAMERA,
											android.Manifest.permission.READ_EXTERNAL_STORAGE},
									MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
						}
					})
					.show();
		} else {
			ActivityCompat.requestPermissions(this, new String[]{
							android.Manifest.permission.CAMERA,
							android.Manifest.permission.READ_EXTERNAL_STORAGE},
					MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
		}
	}

    public void onButtonClicker(View v)
    {
    	Intent intent;
    	int viewID;    	        
    	viewID = v.getId();
    	
    	//module define in here
    	if(viewID == R.id.main_btn1){
    		generalPrefs.edit().putString("moduleid", "1").commit();
    		intent = new Intent(mContext, ProspectActivity.class);
			startActivity(intent);    		
    	}else if(viewID == R.id.main_btn2){
    		generalPrefs.edit().putString("moduleid", "2").commit();
    		intent = new Intent(mContext, ListSurveyActivity.class);
			intent.putExtra("flagUpdate", "U");
			startActivity(intent);
    	}else if(viewID == R.id.main_btn3){
    		generalPrefs.edit().putString("moduleid", "3").commit();
    		intent = new Intent(mContext, ListCollectionActivity.class);
			intent.putExtra("flagUpdate", "U");
			startActivity(intent);
    	}else if(viewID == R.id.main_btn4){
    		intent = new Intent(mContext, TransStatusActivity.class);
			startActivity(intent);
    	}else if(viewID == R.id.main_btn5){    		
    		Intent myintent = new Intent(mContext,ChangePasswordActivity.class);
    		myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		myintent.putExtra("oldpassword", generalPrefs.getString("oldpassword", null));
			myintent.putExtra("username", generalPrefs.getString("username", null));
			myintent.putExtra("classname", mContext.getClass().getName());			
			startActivity(myintent);
    	}else if(viewID == R.id.main_btn6){
    		generalPrefs.edit().putString("moduleid", "6").commit();
    		intent = new Intent(mContext, ListSurveyLocationActivity.class);
			intent.putExtra("flagUpdate", "U");
			startActivity(intent);
    	}
		///menu_Adddata
		else if(viewID == R.id.main_btn9){
    		new ambilSales().execute();
			//intent = new Intent(mContext, AddData.class);
			//startActivity(intent);
		}
		//MENU_GUIDE
		else if(viewID == R.id.main_btn10){
			generalPrefs.edit().putString("moduleid", "1").commit();
			intent = new Intent(mContext, Guide_Activity.class);
			intent.putExtra("flagUpdate", "U");
			startActivity(intent);
		}

		else if(viewID == R.id.main_btn12){
			intent = new Intent(mContext, News_Survey.class);
			intent.putExtra("flagUpdate", "U");
			startActivity(intent);
		}


		
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("Confirmation");
        builder.setMessage("Do you really want to logout ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        finish();
                        Intent myintent = new Intent(mContext,LoginActivity.class);
                        myintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(myintent);
                        return;
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


    @Override
    public void onPause() {
    	super.onPause();
    	    
    	flag = false;
    	String now;
		Calendar c = Calendar.getInstance();
	   	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
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
        	   	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
                now = df.format(c.getTime());
                
                timeOut = generalPrefs.getString("timeOut", null);
        	   	sessionStart = generalPrefs.getString("sessionStart", null);
        	   	
    			Date dateStart = df.parse(sessionStart);
    			Date dateEnd = df.parse(now);
    			
    			diffInMin = (dateEnd.getTime()/60000) - (dateStart.getTime()/60000);
    			
    			if ((int)diffInMin > Integer.valueOf(timeOut) || (int)diffInMin < 0){
    				Toast.makeText(mContext,"Your session has been expired, please re login for security purpose", Toast.LENGTH_LONG).show();
    				
    				finish();
        	   		Intent myintent = new Intent(mContext,LoginActivity.class);
        	   		myintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        	   		startActivity(myintent);
        	   	}
        	   	
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}
    	}
    }
	
	@Override
    public void onDestroy() {
		super.onDestroy();
		finish();
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
				new ambilProduct().execute();
			}
			//Toast.makeText(getApplicationContext(),"Sukses DI Download", Toast.LENGTH_SHORT).show();


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
				new ambilSupplier().execute();
			}
			//Toast.makeText(getApplicationContext(),"Sukses DI Download Product", Toast.LENGTH_SHORT).show();


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
				new ambilAsset().execute();
			}
			//Toast.makeText(getApplicationContext(),"Sukses DI Download Supplier", Toast.LENGTH_SHORT).show();


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
				new ambilProductOffering().execute();
			}
			//Toast.makeText(getApplicationContext(),"Sukses DI Download Asset", Toast.LENGTH_SHORT).show();


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

	
}

