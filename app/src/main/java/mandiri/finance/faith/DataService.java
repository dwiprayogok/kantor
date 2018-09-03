package mandiri.finance.faith;

import java.security.InvalidKeyException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.util.EncodingUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;

public class DataService extends Service {
	private Updater updater;
	public boolean isRunning = false;
	private DataSource datasource;
    private SQLiteDatabase db;
	private Context mContext;
    
    @Override
	public IBinder onBind(Intent intent) {
	    return null;
	}

	@Override
	public synchronized void onCreate() {	    
	    super.onCreate();
	    updater = new Updater();
	}	
	
	@Override
    public synchronized int onStartCommand(Intent intent, int flags, int startId) {		
        if (!this.isRunning){
        	updater.start();
			this.isRunning = true;
        }
        return super.onStartCommand(intent, flags, startId);
    }
	
	@Override
	public synchronized void onDestroy() {
	    super.onDestroy();
	    
	    if(!this.isRunning){
	    	updater.interrupt();
	    	
	    	if(db.isOpen()){
	    		db.close();
	    	}
	    	
	    	if(datasource != null){
	        	datasource.close();
	        }
	    	
	    }
	    updater = null;
	}

	class Updater extends Thread {
		static final long delay = 60000 * 1; //1 menit = 60000 
		private boolean isRunning = false;
				
		public Updater(){
			super("Updater");
		}
		
		@Override
		public void run(){
			isRunning = true;
			
			while(isRunning){
				try {
					
					if (isOnline()){						
						saveresult();
						savelocation();
					}
					
				    Thread.sleep(delay);
				} catch (InterruptedException e) {
					isRunning = false;					
				}
			}		
		}
		
	}
		
	private void saveresult(){
		datasource = new DataSource(DataService.this);
		db = datasource.getWritableDatabase();
		String SOAP_ACTION = "http://layanan.mobilefin/simpanhasilProspect2";
		String NAMESPACE = "http://layanan.mobilefin/";
		String METHOD_NAME = "simpanhasilProspect2";
		String URL = getString(R.string.url);
		String outputresult;
		
		Calendar cl = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
        String myTime = df.format(cl.getTime());				
		String theKey = encrypt(myTime,getString(R.string.Key44));
	   
//	   	Cursor c = db.rawQuery("SELECT id, imagestr, imagecount, fieldname, fieldvalue, moduleid, key, " +
//	   			"lat, lng, dtmupd, userid, imagelat, imagelng, custtype, paiddate, grade FROM result" ,null);

		Cursor c = db.rawQuery("SELECT id, imagestr, imagecount, fieldname, fieldvalue, moduleid, key, " +
				"lat, lng, dtmupd, userid, imagelat, imagelng, custtype, grading, branchID FROM result" ,null);
	   	while(c.moveToNext()) {
			Log.d("ceklagi", "saveresult: cek query "+ c);
	   		outputresult = "";
							    						    	
	   		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	   			   			   		
   			request.addProperty("a",encrypt(c.getString(1),myTime));
			request.addProperty("b",encrypt(c.getString(2),myTime));			
			request.addProperty("c",encrypt(c.getString(3),myTime));
			request.addProperty("d",encrypt(c.getString(4),myTime));
			request.addProperty("e",encrypt(c.getString(5),myTime));
			request.addProperty("f",encrypt(c.getString(6),myTime));
			request.addProperty("g",encrypt(c.getString(7),myTime));
			request.addProperty("h",encrypt(c.getString(8),myTime));
			request.addProperty("j",encrypt(c.getString(9),myTime));
			request.addProperty("k",encrypt(c.getString(10),myTime));			
			request.addProperty("l",encrypt(c.getString(11),myTime));
			request.addProperty("m",encrypt(c.getString(12),myTime));
			request.addProperty("n",encrypt(c.getString(13),myTime));
			request.addProperty("p",encrypt(c.getString(14),myTime));
			request.addProperty("q",encrypt(c.getString(15),myTime));
			request.addProperty("o",theKey);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	   		envelope.dotNet = true;
	   		envelope.setOutputSoapObject(request);
        
	   		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,120000); 

	   		try {
	   			androidHttpTransport.call(SOAP_ACTION, envelope);
	   			
	   			if (envelope.bodyIn instanceof SoapFault) {
	   		        outputresult = ((SoapFault) envelope.bodyIn).faultstring;
					Log.d("cek", "saveresult: "+outputresult);

				} else {
	   		    	SoapObject response = (SoapObject) envelope.bodyIn;
	   		    	outputresult =  response.getProperty(0).toString();
					Log.d("cek", "saveresult: yang else"+outputresult);
				}
	   			
	   		}
	   		catch (Exception e) {
	   			outputresult = e.getMessage();
	   			continue;
	   		}
	   		
	   		System.out.println("outputresult saveresult : " + outputresult);
	   		       
	        if (outputresult.contentEquals("Success")){	        		        	
	        	datasource.deleteData(db, " id = '" + c.getString(0) + "'", "result");	    	   		   	   				        	   	   
	        }
	   }
	   	
	   c.close();
	   db.close();
	   datasource.close();
	   System.gc();	
	}
	
	private void savelocation(){
		datasource = new DataSource(DataService.this);
		db = datasource.getWritableDatabase();
		
		String SOAP_ACTION = "http://layanan.mobilefin/simpanlokasi";
		String NAMESPACE = "http://layanan.mobilefin/";
		String METHOD_NAME = "simpanlokasi";
		String URL = getString(R.string.url);
		String outputresult;
		
		Calendar cl = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
        String myTime = df.format(cl.getTime());
		String theKey = encrypt(myTime,getString(R.string.Key44));
			   	
	   	String lat = "", lng = "", dtmupd = "", userid = encrypt("0",myTime), sourceid = "";
		
	   	Cursor c = db.rawQuery("SELECT id, lat, lng, dtmupd, userid, sourceid " +
			"FROM locationsource order by sourceid,dtmupd" ,null);
	   		   	
	   	while(c.moveToNext()) {   		
	   		outputresult = "";
	   		
	   		if(c.getString(1) != null)
	   			lat = encrypt(c.getString(1),myTime);
	   		
	   		if(c.getString(2) != null)
	   			lng = encrypt(c.getString(2),myTime);
	   		
	   		if(c.getString(3) != null)
	   			dtmupd = encrypt(c.getString(3),myTime);
	   		
	   		if(c.getString(4) != null)
	   			userid = encrypt(c.getString(4),myTime);
	   		
	   		if(c.getString(5) != null)
	   			sourceid = encrypt(c.getString(5),myTime);
	   									    						    	
	   		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	   		
	   		request.addProperty("a",lat);
			request.addProperty("b",lng);
			request.addProperty("c",dtmupd);
			request.addProperty("d",userid);	
			request.addProperty("e",sourceid);
			request.addProperty("f",theKey);	   		
			
	   		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	   		envelope.dotNet = true;
	   		envelope.setOutputSoapObject(request);
        
	   		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,120000); 
	   			   		
	   		try {
	   			androidHttpTransport.call(SOAP_ACTION, envelope);
	   			
	   			if (envelope.bodyIn instanceof SoapFault) {
	   		        outputresult = ((SoapFault) envelope.bodyIn).faultstring;
	   		       
	   		    } else {
	   		    	SoapObject response = (SoapObject) envelope.bodyIn;
	   		    	outputresult =  response.getProperty(0).toString();
	   		    }	   			
	   		}
	   		catch (Exception e) {
	   			outputresult = e.getMessage();			        	  
	   			continue;
	   		}
	   		
	   		System.out.println("outputresult savelocation : " + outputresult);
	   		
	        if (outputresult.contentEquals("Success")){
	        	datasource.deleteData(db, " id = '" + c.getString(0) + "'", "locationsource");			        	   	   
	        }
	   	}
	   	
	   	c.close();
	   	db.close();
	   	datasource.close();
	   	System.gc();	
	}
	
	public boolean isRunning(){
		return this.isRunning;		
	}
	
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}


	//awalnya private dwi ubah dari public
	public String encrypt(String plaintext, String key) {
	 	   try {
	 		   	String encrypted = null;

	 		    java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
	 	        	        	        
	 	        byte[] byteKey = md.digest(key.getBytes());
	 	        byte[] plaintextByte = EncodingUtils.getAsciiBytes(plaintext);	        
	 	        Cipher cipher;
	 			cipher = Cipher.getInstance("DESEDE/ECB/PKCS5Padding");
	 			
	 	        SecretKeySpec myKey = new SecretKeySpec(byteKey,"DESede");
	 	        cipher.init(Cipher.ENCRYPT_MODE, myKey);
	 			

	 	       	byte[] encryptedPlainText = cipher.doFinal(plaintextByte);			
	 			
	 	       	encrypted = Base64.encodeToString(encryptedPlainText, 0);
	 	       	return encrypted;
	 	        
	 	    } catch (java.security.NoSuchAlgorithmException e) {
	 	    	e.printStackTrace();
	 	    }
	 	    catch (NoSuchPaddingException e) {
	 			e.printStackTrace();
	 		}
	 	    catch (InvalidKeyException e) {
	 			e.printStackTrace();
	 		}
	 		catch (BadPaddingException e) {
	 			e.printStackTrace();
	 		}
	 		catch (IllegalBlockSizeException e) {
	 			e.printStackTrace();
	 		}
	 		
	 	    return null;
	 	}
}


