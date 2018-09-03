package mandiri.finance.faith;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListSurveyActivity extends DashBoardActivity3 {
	
	private String userID;	
	private Context mContext;
	private ListView list;
	private LazyAdapter adapter = null;
    
    private ArrayList<SearchResults> results = new ArrayList<SearchResults>();
        
    private EditText filterText = null;
    private TextView lastRefresh;
    private DataSource datasource;
    private SQLiteDatabase db;
    private Exception e = null;
    private String flagUpdate;
    private SharedPreferences generalPrefs;
	private boolean flag = false;
	private Dialog dialog;
	private SharedPreferences.Editor prefsEditor;
	private String isLoadSurveyForm;
	private String isLoadZipCode;
	private DataLocal datalocal;
	String result2 = "";
	//private long PERIOD = 0; // 1800000 = 30 menit, 60000 = 1 menit
	//private LocationManager locationManager;
	boolean gps_enabled = false;
	boolean network_enabled = false;
	double latCurrent = 0;
	double lngCurrent = 0;
	String TAG = "ListSurveyActivity";
	private String moduleid;
    private Button btnRefresh;
	String registrationID = "";
	private int sizeInt = 0 ;
	private String ukuranzipcode = "";
	ProgressDialog progress;
	NodeList nodelist;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ListSurveyActivity.this;

		datasource = new DataSource(mContext);
		db = datasource.getWritableDatabase();
		datalocal = new DataLocal();

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
    	   	isLoadSurveyForm = generalPrefs.getString("isLoadSurveyForm", null);
			isLoadZipCode = generalPrefs.getString("isLoadZipCode", null);

			Log.d(TAG, "onCreate: "+"timeout"+ timeOut + " dan sessionya "
					+ sessionStart+ " dan surveyformnya "+ isLoadSurveyForm );
			Log.d(TAG, "onCreate: isLoadZipCode " + isLoadZipCode);

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


		flag = true;
		moduleid = generalPrefs.getString("moduleid", null);
        userID = generalPrefs.getString("userID", null);
		registrationID = generalPrefs.getString("registrationID", null);


		Bundle bd = getIntent().getExtras();
        flagUpdate = bd.getString("flagUpdate");


		setContentView(R.layout.survey_list);
        setHeader(getString(R.string.SurveyActivityTitle), true);

		btnRefresh = (Button) findViewById(R.id.btnRefresh);
		btnRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				RefreshList();
			}
		});

        list=(ListView)findViewById(R.id.list);
        lastRefresh = (TextView) findViewById(R.id.textLastRefresh);
        //PERIOD = generalPrefs.getLong("locationInterval", 0);
        prefsEditor.putString("sourceid", "surveyor").commit();

		new GetList().execute();
    }
    
    private class GetList extends AsyncTask<Void, Void, String> {
                
        @Override
        protected void onPreExecute() {
        	
        	if(dialog.isShowing())
            	dialog.dismiss();
        	
        	if(flagUpdate.equals("U"));
    			dialog.show();
    		
        	//handlelocation();
        }

        @Override
        protected String doInBackground(Void... unsued) {
        	String result = "";


        	try {
        		if (flagUpdate.equals("U")){

					db = datasource.getWritableDatabase();
//        			AlarmManager mgr=(AlarmManager)getSystemService(ALARM_SERVICE);
//
//        			Intent i = new Intent(mContext, LocationPoller.class);
//
//        			Bundle bundle = new Bundle();
//        			LocationPollerParameter parameter = new LocationPollerParameter(bundle);
//        			parameter.setIntentToBroadcastOnCompletion(new Intent(mContext, LocationReceiver.class));
//        			// try GPS and fall back to NETWORK_PROVIDER
//        			parameter.setProviders(new String[] {LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER});
//        			parameter.setTimeout(120000);
//        			i.putExtras(bundle);
//
//        			boolean alarmUp = (PendingIntent.getBroadcast(mContext, 0,
//        			        new Intent(i), PendingIntent.FLAG_NO_CREATE) != null);
//
//        			if (!alarmUp)
//        			{
//        			    System.out.println("Alarm is not active");
//        			    PendingIntent pi=PendingIntent.getBroadcast(mContext, 0, i, 0);
//        				mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), PERIOD, pi);
//        			}else{
//        				System.out.println("Alarm is already active");
//        			}
        			if (!GetSurveyData())
        				result = "ErrorConnection";

					String now;
					Calendar c = Calendar.getInstance();
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					now = df.format(c.getTime());
					prefsEditor.putString("lastRefresh", String.valueOf(now)).commit();

					if(results != null){
						if(results.size() > 0){
							results.clear();
						}
					}
					if (Boolean.valueOf(isLoadSurveyForm)){
						if (datalocal.GetFormData(mContext,moduleid,getString(R.string.url)))
							prefsEditor.putString("isLoadSurveyForm", "false").commit();
						else
							result = "ErrorConnection";
					}

					//checksize();

                }



        		Cursor cr = db.rawQuery("SELECT headertext, detailtext, prospectdate, imageurl, customerid " +
						"FROM surveylist where status = 'n' ORDER BY distance,prospectdate, headertext" ,null);
    			
    			try{
    				while(cr.moveToNext()) {
        				SearchResults sr1 = new SearchResults();
        		        sr1.setHeader(cr.getString(0));
        		        sr1.setDetail(cr.getString(1));
        		        sr1.setProspectDate(cr.getString(2));
        		        sr1.setURL(cr.getString(3));
        		        sr1.setCustomerID(cr.getString(4));
        		        results.add(sr1);
        			}
    			}
    			finally{
    				cr.close();
    			}
    			
        	} catch (Exception e) {
        		ListSurveyActivity.this.e = e;
    		}finally{
    			db.close();
			}
        	
        	return result;          	
        }

        protected void onPostExecute(String sResponse) {
			Log.d(TAG, "onPostExecute: "+sResponse);
			if (e==null && sResponse.length()==0) {
        		
        		filterText = (EditText) findViewById(R.id.search_box);
        		filterText.setText(""); 
                filterText.addTextChangedListener(filterTextWatcher);
                
                if(generalPrefs.getString("lastRefresh", "").length() > 0){
                	lastRefresh.setText("Last synchronized at : " + generalPrefs.getString("lastRefresh", ""));
                }else{
                	lastRefresh.setText("Never synchronized before");
                }                

                GetList();
        		
                if(dialog.isShowing())
                	dialog.dismiss();
                
        	}else {
        		
        		if(dialog.isShowing())
                	dialog.dismiss();
        		
        		Toast.makeText(mContext, "Connection time out", Toast.LENGTH_SHORT).show();
        		lastRefresh.setText("Never synchronized before");
				e = null;
        	}
        }
    }
    
    private void GetList(){		
		adapter=new LazyAdapter(ListSurveyActivity.this, results);
        list.setAdapter(adapter);
        list.setTextFilterEnabled(true);  
        list.setLongClickable(true);
        //list.setOnItemLongClickListener(listLongClickListener);
    }

	private void checksize(){

		try{
			Cursor cs = db.rawQuery("SELECT TotalSize FROM SizeZipcode", null);

			try {
				if (cs.moveToFirst()) {
					ukuranzipcode = cs.getString(0);
					Log.d(TAG, "doinbackground: ukuranzipcode "+ ukuranzipcode);
				}
			} finally {
				cs.close();
			}

			if(ukuranzipcode.equals("")){
				if (!GetSizeZipCode())
					result2 = "ErrorConnection";
					Log.d(TAG, "checksize: "+"DOWNLOAD DULU");

				if (Boolean.valueOf(isLoadZipCode)) {
					new ambilZipCode().execute();

//					if (datalocal.GetZipCodeData(mContext, getString(R.string.url)))
//						prefsEditor.putString("isLoadZipCode", "false").commit();
//					else
//						result2 = "ErrorConnection";
				}
			}else{
				Log.d(TAG, "checksize: "+"Sizenya sama dong");


			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private void RefreshList(){
		try {
			flagUpdate = "U";
			new GetList().execute();
		} catch (Exception e) {
			Toast.makeText(mContext, "Connection time out", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			return;
		}
	}

	private boolean GetSurveyData(){
		try{

			if(! db.isOpen()){
				db = datasource.getWritableDatabase();
			}

			InputStream in = null;
			ContentValues cv = new ContentValues();

			ArrayList<String> customeridDelete = new ArrayList<String>();
			ArrayList<String> custIDExists = new ArrayList<String>();

			String [] custIDExistsArray = null;
			String [] customeridDeleteArray = null;

			Cursor c = db.rawQuery("SELECT customerid FROM surveylist UNION ALL SELECT key " +
					"FROM result where moduleid = '" + moduleid + "'" ,null);

			try{
				while(c.moveToNext()) {
					custIDExists.add(c.getString(0));
				}
			}
			finally{
				c.close();
			}

			StringBuilder sbl = new StringBuilder();
			sbl.append("");

			if (custIDExists != null){
				custIDExistsArray = custIDExists.toArray(new String[custIDExists.size()]);

				for (int i = 0; i < custIDExistsArray.length; i++) {
					if(i == 0){
						sbl.append("'" +custIDExistsArray[i]+ "'");
					}else{
						sbl.append(",'" +custIDExistsArray[i]+ "'");
					}
				}
			}

			Calendar cl = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String myTime = df.format(cl.getTime());

			String CustIDExistsEncrypt = datalocal.encrypt(sbl.toString(),myTime);
			String RegistrationID = datalocal.encrypt(generalPrefs.getString("registrationID", null), myTime);
			String theKey = datalocal.encrypt(myTime,getString(R.string.Key44));
			String userIDEncrypt = datalocal.encrypt(userID,myTime);

			URL localUrl = new URL(getString(R.string.url)
					+ "/GetSurveyList?a=" + URLEncoder.encode(userIDEncrypt,"UTF-8") +
					"&b=" + URLEncoder.encode(CustIDExistsEncrypt,"UTF-8")
					+ "&c=" + URLEncoder.encode(theKey,"UTF-8") +
					"&d=" + URLEncoder.encode(RegistrationID,"UTF-8"));

			Log.d(TAG, "GetSurveyList: "+ localUrl);

			URLConnection conn = localUrl.openConnection();

			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			in = httpConn.getInputStream();

			Document doc = null;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docb;

			try {
				docb = dbf.newDocumentBuilder();
				doc = docb.parse(in);
			} catch (Exception e) {
				e.printStackTrace();
			}

			doc.getDocumentElement().normalize();
			Element root = doc.getDocumentElement();

			NodeList fields = root.getElementsByTagName("data");
			NodeList detailFields = root.getElementsByTagName("datadetail");
			NodeList imageURL = root.getElementsByTagName("imageurl");
			NodeList customeridfields = root.getElementsByTagName("delete");

			for (int i=0;i<customeridfields.getLength();i++) {
				Node fieldNode = customeridfields.item(i);
				NamedNodeMap attr = fieldNode.getAttributes();

				customeridDelete.add(attr.getNamedItem("CustomerID").getNodeValue().trim());
			}

			customeridDeleteArray = customeridDelete.toArray(new String[customeridDelete.size()]);
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < customeridDeleteArray.length; i++) {
				if(i == 0){
					sb.append("'" +customeridDeleteArray[i]+ "'");
				}else{
					sb.append(",'" +customeridDeleteArray[i]+ "'");
				}
			}

			Cursor cr = db.rawQuery("SELECT id FROM surveylist where customerid in(" + sb.toString() + ")", null);

			try{
				if(cr.moveToFirst())
				{
					datasource.deleteData(db, "customerid in(" + sb.toString() + ")", "surveylist");
					datasource.deleteData(db, "moduleid = '"+moduleid+"' and idx in(" + sb.toString() + ")", "infodetail");
					datasource.deleteData(db, "moduleid = '"+moduleid+"' and idx in(" + sb.toString() + ")", "imageurldetail");
				}
			}
			finally{
				cr.close();
			}

			for (int i=0;i<detailFields.getLength();i++) {
				Node fieldNode = detailFields.item(i);
				NamedNodeMap attr = fieldNode.getAttributes();

				cv.put("idx", attr.getNamedItem("CustomerID").getNodeValue());
				cv.put("moduleid", moduleid);
				cv.put("infolabel", attr.getNamedItem("InfoLabel").getNodeValue());
				cv.put("infotext", attr.getNamedItem("InfoText").getNodeValue());

				datasource.generateData(db, cv, "infodetail");
				cv.clear();
			}

			for (int i=0;i<imageURL.getLength();i++) {
				Node fieldNode = imageURL.item(i);
				NamedNodeMap attr = fieldNode.getAttributes();

				cv.put("moduleid", moduleid);
				cv.put("idx", attr.getNamedItem("CustomerID").getNodeValue());
				cv.put("imageurl", attr.getNamedItem("ImageURL").getNodeValue());

				datasource.generateData(db, cv, "imageurldetail");
				cv.clear();
			}

			for (int i=0;i<fields.getLength();i++) {

				double distanceCalc = 0;


				Node fieldNode = fields.item(i);
				NamedNodeMap attr = fieldNode.getAttributes();

				//hitung jarak, lambat...
				//GeoPoint gpCurrentLocation = new GeoPoint((int)(latCurrent * 1E6),(int)(lngCurrent * 1E6));
				//if(gpCurrentLocation != null){
				//	distanceCalc = directionsDistance(gpCurrentLocation, attr.getNamedItem("DetailText").getNodeValue().replace("\n", " ").replace(" ", "%20"));
				//}

				cv.put("headertext", attr.getNamedItem("HeaderText").getNodeValue());
				cv.put("detailtext", attr.getNamedItem("DetailText").getNodeValue());
				cv.put("prospectdate", attr.getNamedItem("ProspectDate").getNodeValue());
				cv.put("imageurl", attr.getNamedItem("ImageURL").getNodeValue());
				cv.put("customerid", attr.getNamedItem("CustomerID").getNodeValue());
				cv.put("custtype", attr.getNamedItem("CustType").getNodeValue());
				cv.put("status", "n");
				cv.put("distance", distanceCalc);

				datasource.generateData(db, cv, "surveylist");
				cv.clear();
			}

			docb = null;
			doc = null;

			return true;

		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}


	}

	private boolean GetSizeZipCode(){
		try{
			if(! db.isOpen()){
				db = datasource.getWritableDatabase();
			}

			InputStream in = null;
			ContentValues cv = new ContentValues();
			Cursor cr = db.rawQuery("SELECT TotalSize FROM SizeZipcode", null);
			try{
				if(cr.moveToFirst())
				{
					datasource.deleteData(db, null, "SizeZipcode");
				}
			}
			finally{
				cr.close();
			}
			URL localUrl = new URL(getString(R.string.url) +"/GetSizeZipCode");
			Log.d(TAG, "GetSizeZipCode: "+ localUrl);

			URLConnection conn = localUrl.openConnection();

			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			in = httpConn.getInputStream();

			Document doc = null;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docb;

			try {
				docb = dbf.newDocumentBuilder();
				doc = docb.parse(in);
			} catch (Exception e) {
				e.printStackTrace();
			}

			doc.getDocumentElement().normalize();
			Element root = doc.getDocumentElement();

			NodeList fields = root.getElementsByTagName("sizezipcode");
			for (int i=0;i<fields.getLength();i++) {
				Node fieldNode = fields.item(i);
				NamedNodeMap attr = fieldNode.getAttributes();

				cv.put("TotalSize", attr.getNamedItem("TotalSize").getNodeValue());

//				SearchResults sr1 = new SearchResults();
//				sr1.setJumlah(attr.getNamedItem("TotalSize").getNodeValue());

				datasource.generateData(db, cv, "SizeZipcode");
				cv.clear();
			}



			docb = null;
			doc = null;

			return true;

		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	///dibawah yg defaultnya.
//    public void refreshClick(View v){
//		try{
//			flagUpdate = "U";
//	    	new GetList().execute();
//		}
//		catch (Exception e) {
//			Toast.makeText(mContext, "Connection time out", Toast.LENGTH_SHORT).show();
//			e.printStackTrace();
//			return;
//		}
//
//    }


	private class ambilZipCode extends AsyncTask<Void, Integer, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

//			if(dialog.isShowing())
//				dialog.dismiss();
//
//			if(flagUpdate.equals("U"));
//			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {


			try {
				datasource = new DataSource(mContext);
				db = datasource.getWritableDatabase();


				URL url = new URL("http://123.231.241.53:48100/mobileapplication/AndroidFin.asmx/GetZipCode");
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db1 = dbf.newDocumentBuilder();
				Document doc = db1.parse(new InputSource(url.openStream()));
				doc.getDocumentElement().normalize();
				nodelist = doc.getElementsByTagName("zipcode");
				Log.d(TAG, "doInBackground:nodelistnya "+nodelist);

				datasource.deletekelurahan();
				datasource.insertprovinsi(nodelist);

				float f = ((float)datasource.getKelurahanModelCount()/nodelist.getLength())/20;
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
				Log.d(TAG, "onProgressUpdate: zipcode "+String.valueOf(xx)+"%");
				Log.d(TAG, "onProgressUpdate: zipcode2 "+values[0]);

			}

		}

		@Override
		protected void onPostExecute(Void args) {
//			if(dialog.isShowing())
//				dialog.dismiss();
//			Toast.makeText(mContext,"Zipcode Berhasil Terunduh",Toast.LENGTH_SHORT).show();

		}
	}


	public class SearchResults {
    	 private String header = "";
    	 private String detail = "";
    	 private String prospectdate = "";
    	 private String url = "";
    	 private String customerID = "";
		private String jumlah = "";

		public void setJumlah(String jumlah) {
			this.jumlah = jumlah;
		}

		public String getJumlah() {
			return jumlah;
		}

		public void setHeader(String header) {
    		 this.header = header;
    	 }

    	 public String getHeader() {
    		 return header;
    	 }

    	 public void setDetail(String detail) {
    		 this.detail = detail;
    	 }

    	 public String getDetail() {
    		 return detail;
    	 }
    	 
    	 public void setProspectDate(String prospectdate) {
    		 this.prospectdate = prospectdate;
    	 }
    	 
    	 public String getProspectDate() {
    		 return prospectdate;
    	 }

    	 public void setURL(String url) {
    		 this.url = url;
    	 }

    	 public String getURL() {
    		 return url;
    	 }

    	 public void setCustomerID(String customerID) {
    		 this.customerID = customerID;
       	 }

       	 public String getCustomerID() {
       		 return customerID;
       	 } 	 
       	
	}
    
    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {        	
        	adapter.getFilter().filter(s.toString());
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                int count) {
        	
        }

    };

    
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
    	
    	if (dialog != null){
    		if(dialog.isShowing()){
        		dialog.dismiss();
        	}
    	}
    	
    	if (db != null){
    		if (db.isOpen()){
        		db.close();	
        	}
    	}
    	
    	if(datasource != null){
        	datasource.close();
        }
    	
    	if (adapter != null){
			adapter.notifyDataSetInvalidated();
			adapter = null;
	   	}
		
    	if (filterText != null){
			filterText.removeTextChangedListener(filterTextWatcher);
			filterText = null;
 	   	}
    	
    	if(list!=null){
    		list.setAdapter(null);
   			list = null;
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


    /*
    private OnItemLongClickListener listLongClickListener = new OnItemLongClickListener() {
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			handlelocation();

			RelativeLayout rl = (RelativeLayout) view;
			TextView tvCustName = (TextView) rl.getChildAt(1);
			final TextView tvLocation = (TextView) rl.getChildAt(2);

			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			AlertDialog alertDialog;

			List<String> listItems = new ArrayList<String>();

		   	listItems.add("View in map");
	  		final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);

	  		builder.setTitle(tvCustName.getText().toString());

			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse("http://maps.google.com/maps?daddr="+ tvLocation.getText().toString() + "&dirflg=d" ) );
					intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

					startActivity(intent);
					dialog.dismiss();
				}
			});

			alertDialog = builder.create();
			alertDialog.show();

			return false;

			}
    };

    private boolean UpdateDistance(){
		try{

			Cursor c = db.rawQuery("SELECT customerid, detailtext FROM surveylist where status = 'n' ORDER BY distance, prospectdate, headertext" ,null);

			try{
				while(c.moveToNext()) {

					int distanceCalc = 0;
    				GeoPoint gpCurrentLocation = null;

    				gpCurrentLocation = new GeoPoint((int)(latCurrent * 1E6),(int)(lngCurrent * 1E6));

					if(gpCurrentLocation != null){
						distanceCalc = directionsDistance(gpCurrentLocation, c.getString(1).replace("\n", " ").replace(" ", "%20"));
					}

					if(distanceCalc>0){
						ContentValues cv = new ContentValues();
	    				cv.put("distance", distanceCalc);
	    				datasource.updateData(db, cv, "surveylist", " customerid = '" + c.getString(0) + "'");
	    				cv.clear();
					}

    			}
			}
			finally{
				c.close();
				c = null;
			}

		return true;

		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	private int directionsDistance(GeoPoint start, String dest) {

	    String jsonURL = "http://maps.googleapis.com/maps/api/directions/json?";
	    final StringBuffer sBuf = new StringBuffer(jsonURL);
	    sBuf.append("origin=");
	    sBuf.append(start.getLatitudeE6()/1E6);
	    sBuf.append(',');
	    sBuf.append(start.getLongitudeE6()/1E6);
	    sBuf.append("&destination=");
	    sBuf.append(dest);
	    sBuf.append("&avoid=tolls&sensor=true&mode=driving");

	    HttpGet httpGet = new HttpGet(sBuf.toString());
	    HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;

            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }

        } catch (ClientProtocolException e) {

        } catch (IOException e) {

        }

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject = new JSONObject(stringBuilder.toString());

            JSONArray array = jsonObject.getJSONArray("routes");
            JSONObject routes = array.getJSONObject(0);
            JSONArray legs = routes.getJSONArray("legs");
            JSONObject steps = legs.getJSONObject(0);
        	JSONObject distance = steps.getJSONObject("distance");

        	DecimalFormat dForm = new DecimalFormat("#");
        	return Integer.valueOf(dForm.format(distance.getDouble("value")/1000));

        } catch (JSONException e) {
        	e.printStackTrace();
        	return 0;
        }

	}

	*/



	/*
	private void handlelocation() {

		if (locationManager == null) {
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		}

		try {
			gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}

		try {
			network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		if (!gps_enabled && !network_enabled)
			return;

		if (gps_enabled) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
			Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

			if(lastLocation != null && (latCurrent == 0.0 || lngCurrent == 0.0) ){
				latCurrent = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
				lngCurrent = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
			}

		} else {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
			Location lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			if(lastLocation != null && (latCurrent == 0.0 || lngCurrent == 0.0) ){
				latCurrent = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
				lngCurrent = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
			}

		}

	}

	LocationListener locationListenerGps = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
			locationManager.removeUpdates(this);
			locationManager.removeUpdates(locationListenerNetwork);
		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(mContext, "Provider disabled, GPS turned off", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(mContext,"Provider enabled, GPS turned on", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Toast.makeText(mContext, "Provider status changed", Toast.LENGTH_SHORT).show();
		}
	};

	LocationListener locationListenerNetwork = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
			locationManager.removeUpdates(this);
			locationManager.removeUpdates(locationListenerGps);
		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(mContext, "Provider disabled, GPS turned off", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(mContext, "Provider enabled, GPS turned on", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Toast.makeText(mContext, "Provider status changed", Toast.LENGTH_SHORT).show();
		}
	};

	private void updateWithNewLocation(Location newLocation) {
		latCurrent = newLocation.getLatitude();
		lngCurrent = newLocation.getLongitude();
	}

	*/
    
}
