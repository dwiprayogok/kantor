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

import android.app.AlertDialog;
import android.app.Dialog;
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

public class ListSurveyLocationActivity extends DashBoardActivity {
	
	private String userID;	
	private Context mContext;
	private ListView list;
	private LazyAdapterSurvey adapter = null;
    
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
	private String isLoadSurveyLocForm;
	private DataLocal datalocal;
	
	boolean gps_enabled = false;
	boolean network_enabled = false;
	double latCurrent = 0;
	double lngCurrent = 0;
	private Button btnRefresh;
	private String moduleid;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ListSurveyLocationActivity.this;
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
    	   	isLoadSurveyLocForm = generalPrefs.getString("isLoadSurveyLocForm", null);
    	   	
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
		
        datasource = new DataSource(mContext);
        db = datasource.getWritableDatabase();
        userID = generalPrefs.getString("userID", null);
                
        Bundle bd = getIntent().getExtras();
        flagUpdate = bd.getString("flagUpdate");
        
        setContentView(R.layout.survey_list);
        setHeader(getString(R.string.SurveyActivityTitle2), true);

		btnRefresh = (Button) findViewById(R.id.btnRefresh);
		btnRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				RefreshList();
			}
		});

		list=(ListView)findViewById(R.id.list);
        lastRefresh = (TextView) findViewById(R.id.textLastRefresh);
                		
		new GetList().execute();
    }
    
    private class GetList extends AsyncTask<Void, Void, String> {
                
        @Override
        protected void onPreExecute() {
        	
        	if(dialog.isShowing())
            	dialog.dismiss();
        	
        	if(flagUpdate.equals("U"));
    			dialog.show();
        }

        @Override
        protected String doInBackground(Void... unsued) {
        	String result = "";
        	
        	try {
        		
        		if (flagUpdate.equals("U")){
        			
        			if (!GetSurveyData())
        				result = "ErrorConnection";
        			        			
        			String now;
        			Calendar c = Calendar.getInstance();
        		   	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        		    now = df.format(c.getTime());
        		    prefsEditor.putString("lastRefresh", String.valueOf(now)).commit();    		    
                }
        		        		
        		if(results != null){
        			if(results.size() > 0){
            			results.clear();
            		}
        		}
        		
        		if (Boolean.valueOf(isLoadSurveyLocForm)){
                	if (datalocal.GetFormData(mContext,moduleid,getString(R.string.url)))
                		prefsEditor.putString("isLoadSurveyLocationForm", "false").commit();
                	else
                		result = "ErrorConnection";
    			}
        		
        		Cursor cr = db.rawQuery("SELECT headertext, detailtext, surveydate, imageurl, customerid FROM surveyloclist where status = 'n' ORDER BY surveydate, headertext" ,null);
    			
    			try{
    				while(cr.moveToNext()) {
        				SearchResults sr1 = new SearchResults();
        		        sr1.setHeader(cr.getString(0));
        		        sr1.setDetail(cr.getString(1));
        		        sr1.setSurveyDate(cr.getString(2));
        		        sr1.setURL(cr.getString(3));
        		        sr1.setCustomerID(cr.getString(4));
        		        results.add(sr1);
        			}
    			}
    			finally{
    				cr.close();
    			}
    			
        	} catch (Exception e) {
        		ListSurveyLocationActivity.this.e = e;
    		}finally{
    			db.close();
			}
        	
        	return result;          	
        }

        protected void onPostExecute(String sResponse) {
        	
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
		adapter=new LazyAdapterSurvey(ListSurveyLocationActivity.this, results);
        list.setAdapter(adapter);
        list.setTextFilterEnabled(true);  
        list.setLongClickable(true);
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
		    		    
		    Cursor c = db.rawQuery("SELECT customerid FROM surveyloclist UNION ALL SELECT key FROM result where moduleid = '" + moduleid + "'" ,null);
		    
		    try{
		    	while(c.moveToNext()) {
					custIDExists.add(c.getString(0));
					Log.d(TAG, "GetSurveyData: "+ custIDExists);
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
					Log.d(TAG, "GetSurveyData: "+ custIDExistsArray.toString());
					if(i == 0){
						sbl.append("'" +custIDExistsArray[i]+ "'");
					}else{
						sbl.append(",'" +custIDExistsArray[i]+ "'");
					}
				}
			}
			Log.d(TAG, "GetSurveyData:  custIDExists " + custIDExists.toString());
			Log.d(TAG, "GetSurveyData:  custIDExistsArray " + custIDExistsArray.toString());
			Log.d(TAG, "GetSurveyData: sbl " + sbl.toString());
			
			Calendar cl = Calendar.getInstance();
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String myTime = df.format(cl.getTime());
            
            String CustIDExistsEncrypt = datalocal.encrypt(sbl.toString(),myTime);
			String theKey = datalocal.encrypt(myTime,getString(R.string.Key44));
            String userIDEncrypt = datalocal.encrypt(userID,myTime);
			Log.d(TAG, "GetSurveyData: "+ userID);

			Log.d(TAG, "GetSurveyData: CustIDExistsEncrypt" + CustIDExistsEncrypt);
			Log.d(TAG, "GetSurveyData: theKey" + theKey);
			Log.d(TAG, "GetSurveyData: userIDEncrypt" + userIDEncrypt);
			
			URL localUrl = new URL(getString(R.string.url) + "/GetSurveyLocList?a=" + URLEncoder.encode(userIDEncrypt,"UTF-8") + 
					"&b=" + URLEncoder.encode(CustIDExistsEncrypt,"UTF-8") + "&c=" + URLEncoder.encode(theKey,"UTF-8") );

			Log.d(TAG, "GetSurveyData: "+ localUrl);
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
						
			Cursor cr = db.rawQuery("SELECT id FROM surveyloclist where customerid in(" + sb.toString() + ")", null);
			
			try{
				if(cr.moveToFirst())
			    {	
			    	datasource.deleteData(db, "customerid in(" + sb.toString() + ")", "surveyloclist");
			    	datasource.deleteData(db, "moduleid = '"+moduleid+"' and idx in(" + sb.toString() + ")", "infodetail");
			    	datasource.deleteData(db, "moduleid = '"+moduleid+"' and idx in(" + sb.toString() + ")", "imageurldetail");
			    }
			}
			finally{
				cr.close();
			}

			for (int i=0;i<fields.getLength();i++) {

				Node fieldNode = fields.item(i);
				NamedNodeMap attr = fieldNode.getAttributes();

				cv.put("headertext", attr.getNamedItem("HeaderText").getNodeValue());
				cv.put("detailtext", attr.getNamedItem("DetailText").getNodeValue());
				cv.put("imageurl", attr.getNamedItem("ImageURL").getNodeValue());
				cv.put("customerid", attr.getNamedItem("CustomerID").getNodeValue());
				cv.put("surveydate", attr.getNamedItem("SurveyDate").getNodeValue());
				cv.put("custtype", attr.getNamedItem("CustType").getNodeValue());
				cv.put("status", "n");

				datasource.generateData(db, cv, "surveyloclist");
				cv.clear();
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
						

				
			docb = null;
			doc = null;
			
			return true;
			
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	
	
	}
	
	public class SearchResults {
    	 private String header = "";
    	 private String detail = "";
    	 private String surveydate = "";
    	 private String url = "";
    	 private String customerID = "";

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
    	 
    	 public void setSurveyDate(String prospectdate) {
    		 this.surveydate = prospectdate;
    	 }
    	 
    	 public String getSurveyDate() {
    		 return surveydate;
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
    
}
