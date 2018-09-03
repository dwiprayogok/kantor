package mandiri.finance.faith;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePasswordActivity extends Activity {
	private SharedPreferences generalPrefs;
	private boolean flag = false; 
	private Context mContext;
	private EditText newPass, confirmPass;
	private DataLocal datalocal;
	private String theKey;
	private String oldPassword;
	private String userName;
	private Dialog dialog;
	private String newPassword = "";
	private String confirmPassword = "";
	private Exception e = null;
	private String StringClassname;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        mContext = ChangePasswordActivity.this;
        generalPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());        
                
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
		
		datalocal = new DataLocal();
		setContentView(R.layout.changepassword);
		
		Bundle bd = getIntent().getExtras();
		oldPassword = datalocal.decryptlocal(bd.getString("oldpassword"),this);
		userName = datalocal.decryptlocal(bd.getString("username"),this);
		StringClassname = bd.getString("classname");
						
		newPass = (EditText) findViewById(R.id.newpassword);
		confirmPass = (EditText) findViewById(R.id.confirmpassword);
		
		newPass.setText("");
		confirmPass.setText("");
		
		dialog = new Dialog(mContext, R.style.lightbox_dialog);
        dialog.setContentView(R.layout.lightbox_dialog);
    	((TextView)dialog.findViewById(R.id.dialogText)).setText("Loading...");
    	dialog.setCancelable(false);
		
		flag = true;
    }
    
    private class ChangePassword extends AsyncTask<Void, Void, String> {
    	
    	@Override
        protected void onPreExecute() {
        	dialog.show();        	
        }

		@Override
		protected String doInBackground(Void... params) {
			
			String message = "";
        	Calendar c = Calendar.getInstance();
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
            String myTime = df.format(c.getTime());
            
            theKey = datalocal.encrypt(myTime,getString(R.string.Key44));
            
            System.out.println("oldPassword : " + oldPassword);
            System.out.println("userName : " + userName);
        	
			try {
				InputStream in = null;
				URL url;
				url = new URL(getString(R.string.url) +
						"/AndroidChangePassword?&a=" + URLEncoder.encode(datalocal.encrypt(oldPassword,myTime),"UTF-8") + 
						"&b=" + URLEncoder.encode(datalocal.encrypt(newPassword,myTime),"UTF-8") + 
						"&c=" + URLEncoder.encode(datalocal.encrypt(generalPrefs.getString("userID", null),myTime),"UTF-8")+
						"&d=" + URLEncoder.encode(datalocal.encrypt(userName,myTime),"UTF-8")+
						"&e=" + URLEncoder.encode(theKey,"UTF-8"));
				
				URLConnection conn;
				
				conn = url.openConnection();
				HttpURLConnection httpConn = (HttpURLConnection) conn;
    		    httpConn.setAllowUserInteraction(false);
    		    httpConn.setInstanceFollowRedirects(true);
    	     	httpConn.setRequestMethod("GET");
    	     	httpConn.connect();
        	   
    	     	in = httpConn.getInputStream();			
        		
    		    Document doc = null;
    		    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    		    DocumentBuilder db;
    		    
    		    try {
    		    	db = dbf.newDocumentBuilder();
    		    	doc = db.parse(in);
    		    } catch (Exception e) {
    		    	e.printStackTrace();
    		    }
    		    doc.getDocumentElement().normalize();
    			
    			//get root from xml file
    			Element root = doc.getDocumentElement();			

    			// now process the fields
    			NodeList header = root.getElementsByTagName("data");	
    			Node childnode = header.item(0);
    			NamedNodeMap map = childnode.getAttributes();
    			
    			message = map.getNamedItem("Message").getNodeValue().trim();
    	     	
			} catch (Exception e) {
    			e.printStackTrace();
    			ChangePasswordActivity.this.e = e;
    		}

			return message;
		}
		@Override
        protected void onPostExecute(String sResponse) {
			
			if (e==null) {
				if(sResponse.equals("Success")){
					
					Toast.makeText(mContext,"Password has been changed successfully, please re-login with New Password",Toast.LENGTH_LONG).show();
					finish();
					Intent myintent = new Intent(mContext,LoginActivity.class);
					myintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(myintent);
				}else{
					Toast.makeText(mContext,sResponse,Toast.LENGTH_LONG).show();
				}
			}
			else {
        		Toast.makeText(mContext, "Connection time out", Toast.LENGTH_SHORT).show();
        		e = null;
        	}
        	
        	dialog.dismiss();
		}
    }
    
    public void ChangePasswordClick(View v)
    {  	
    	newPassword = newPass.getText().toString();
        confirmPassword = confirmPass.getText().toString();
        
        if((newPassword.trim().length() == 0) || (confirmPassword.trim().length() == 0))
        {    
    		
    		if(newPassword.trim().length() == 0)
            {           
    			newPass.setError("Enter New Password");
            }
    		
    		
    		if(confirmPassword.trim().length() == 0)
            {           
    			confirmPass.setError("Enter Confirm Password");  
            }
        }else{
        	
        	if (newPassword.length() >= 8){
                Pattern pattern = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20})");
                
                Matcher matcher = pattern.matcher(newPassword);   

                if(matcher.matches()){                      

                    if (newPassword.equals(confirmPassword)){
                    	new ChangePassword().execute();						
                    }
                    else{
                    	Toast.makeText(mContext,"New Password Not equal with confirm password",Toast.LENGTH_LONG).show();
                    }
                    
	                }
                else{
                	Toast.makeText(mContext,"New Password must contain at least 1 upper case and numeric",Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(mContext,"Minimum password is 8 characters in length",Toast.LENGTH_LONG).show();
	            }                       
        	}

    }
    
    
    @Override
    public void onBackPressed() {
		finish();
		
		Intent myintent = null;
		
		try {
			myintent = new Intent(mContext, Class.forName(StringClassname));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		startActivity(myintent);
		return;
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
	
}

