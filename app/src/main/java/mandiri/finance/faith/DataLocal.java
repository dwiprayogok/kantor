package mandiri.finance.faith;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.util.EncodingUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

public class DataLocal extends Activity{
	private String tag = DataLocal.class.getName();
	private String lastinsertprospectform = null;
    private String lastinsertsurveyform = null;
    private String lastinsertcollectionform = null;
    private String lastinsertasset = null;
	private String lastinsertzipcode = null;
	private String lastinsertproduct = null;
	private String lastinsertproductoffering = null;
	private String lastinsertsales = null;
    private String lastinsertcollectionplanform = null;
    private String lastinsertsupplier = null;
    private String lastinsertsurveylocform = null;
	private String lastinserttempatlahir = null;

    private DataSource datasource;
	private SQLiteDatabase db;
	protected static final String UTF8 = "utf-8";
	static String s = new String("mTfM0b1l3@pP");
    private static final char[] SEKRIT = s.toCharArray() ;

    public boolean GetFormData(Context context, String moduleID, String url){
    	datasource = new DataSource(context);
        db = datasource.getWritableDatabase();
        boolean isSuccess;

        isSuccess = GetData(moduleID, datasource, db, url, context);

	    ContentValues cv = new ContentValues();

	    if(moduleID == "1"){
    		cv.put("lastinsertprospectform", lastinsertprospectform);
    	}else if(moduleID == "2"){
	    	cv.put("lastinsertsurveyform", lastinsertsurveyform);
    	}else if(moduleID == "3"){
	    	cv.put("lastinsertcollectionform", lastinsertcollectionform);
    	}else if(moduleID == "6"){
	    	cv.put("lastinsertsurveylocform", lastinsertsurveylocform);
    	}else if(moduleID == "99"){
	    	cv.put("lastinsertcollectionplanform", lastinsertcollectionplanform);
    	}

		Cursor cursor = db.rawQuery("SELECT id FROM control", null);

		try{

		    if(cursor.moveToFirst())
		    {
		    	datasource.updateData(db,cv,"control",null);
		    }
		    else{
		    	datasource.generateData(db,cv,"control");
		    }
		}
		finally{
			cursor.close();
		    cv.clear();

		    if(db.isOpen()){
		    	db.close();
		    }

		    datasource.close();
		}

		return isSuccess;

	    //System.out.println("lastinsertprospectform after : " + lastinsertprospectform);
		//System.out.println("lastinsertsurveyform after : " + lastinsertsurveyform);

    }

    private boolean GetData(String moduleID, DataSource datasource, SQLiteDatabase db, String url, Context context) {
		try {
			ContentValues cv = new ContentValues();

			Cursor c = db.rawQuery("SELECT id FROM form where moduleid = '" + moduleID + "'", null);
			try{
				if(c.moveToFirst())
			    {
			    	datasource.deleteData(db, "moduleid = '" + moduleID + "'", "form");
			    }
			}
			finally{
				c.close();
			}

		    InputStream in = null;

		    Calendar cl = Calendar.getInstance();
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
            String myTime = df.format(cl.getTime());
            String theKey;

            theKey = encrypt(myTime,context.getString(R.string.Key44));
            String moduleIDEncrypt = encrypt(moduleID,myTime);


//			URL localUrl = new URL(url + "/GetField?a=" + URLEncoder.encode(moduleIDEncrypt,"UTF-8") +
//					"&b=" + URLEncoder.encode(theKey,"UTF-8"));
//			URLConnection conn = localUrl.openConnection();

			URL localUrl = new URL(url + "/GetFieldMobileSurvey?a=" + URLEncoder.encode(moduleIDEncrypt,"UTF-8") +
					"&b=" + URLEncoder.encode(theKey,"UTF-8"));
			URLConnection conn = localUrl.openConnection();

		    HttpURLConnection httpConn = (HttpURLConnection) conn;
		    httpConn.setAllowUserInteraction(false);
		    httpConn.setInstanceFollowRedirects(true);
	     	httpConn.setRequestMethod("GET");
	     	httpConn.connect();
	     	httpConn.getResponseCode();

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

			//get root from xml file
			Element root = doc.getDocumentElement();

			// now process the fields
			//NodeList fields = root.getElementsByTagName("field");

			NodeList fields = root.getElementsByTagName("fieldmobilesurvey");

			db = datasource.getWritableDatabase();

	    	for (int i=0;i<fields.getLength();i++) {
	    		Node fieldNode = fields.item(i);
				NamedNodeMap map = fieldNode.getAttributes();

				cv.put("moduleid", moduleID);
				cv.put("name", map.getNamedItem("name").getNodeValue());
				cv.put("label", map.getNamedItem("label").getNodeValue());
				cv.put("type", map.getNamedItem("type").getNodeValue());
				cv.put("required", map.getNamedItem("required").getNodeValue());
				cv.put("optionsname", map.getNamedItem("optionsname").getNodeValue());
				cv.put("optionsid", map.getNamedItem("optionsid").getNodeValue());
				cv.put("hint", map.getNamedItem("hint").getNodeValue());
				cv.put("errmsg", map.getNamedItem("errmsg").getNodeValue());
				cv.put("maxlength", map.getNamedItem("maxlength").getNodeValue());
				cv.put("issearchable", map.getNamedItem("issearchable").getNodeValue());
				cv.put("custtype", map.getNamedItem("custtype").getNodeValue());

				datasource.generateData(db, cv, "form");
				cv.clear();
	        }

			docb = null;
			doc = null;

			Node fieldNode = fields.item(0);
			NamedNodeMap map = fieldNode.getAttributes();
			lastinsertprospectform = map.getNamedItem("lastinsertprospectform").getNodeValue();
			lastinsertsurveyform = map.getNamedItem("lastinsertsurveyform").getNodeValue();
			lastinsertcollectionform = map.getNamedItem("lastinsertcollectionform").getNodeValue();
			lastinsertcollectionplanform = map.getNamedItem("lastinsertcollectionplanform").getNodeValue();
			lastinsertsurveylocform = map.getNamedItem("lastinsertsurveylocform").getNodeValue();

			return true;

		} catch (Exception e) {
			Log.e(tag,"Error occurred in GetFormData : " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

    static public String createInsert(final String tableName, final String[] columnNames) {
        if (tableName == null || columnNames == null || columnNames.length == 0) {
            throw new IllegalArgumentException();
        }
        final StringBuilder s = new StringBuilder();
        s.append("INSERT INTO ").append(tableName).append(" (");
        for (String column : columnNames) {
            s.append(column).append(" ,");
        }
        int length = s.length();
        s.delete(length - 2, length);
        s.append(") VALUES( ");
        for (int i = 0; i < columnNames.length; i++) {
            s.append(" ? ,");
        }
        length = s.length();
        s.delete(length - 2, length);
        s.append(")");
        return s.toString();
    }

    public boolean GetAssetData(Context context, String url) {
		try {
			datasource = new DataSource(context);
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

	        URL localUrl = new URL(url + "/GetAsset");
	        URLConnection conn = localUrl.openConnection();

		    HttpURLConnection httpConn = (HttpURLConnection) conn;
		    httpConn.setAllowUserInteraction(false);
		    httpConn.setInstanceFollowRedirects(true);
	     	httpConn.setRequestMethod("GET");
	     	httpConn.connect();
	     	httpConn.getResponseCode();

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

			NodeList fields = root.getElementsByTagName("field");

			for (int i=0;i<fields.getLength();i++) {
				Node fieldNode = fields.item(i);
				NamedNodeMap map = fieldNode.getAttributes();

				cv.put("assetcode", map.getNamedItem("assetcode").getNodeValue());
				cv.put("assettype", map.getNamedItem("assettype").getNodeValue());
				cv.put("assetdetail", map.getNamedItem("assetdetail").getNodeValue());

				datasource.generateData(db, cv, "asset");
				cv.clear();
			}


			Node fieldNode = fields.item(0);
			NamedNodeMap map = fieldNode.getAttributes();
			lastinsertasset = map.getNamedItem("lastinsertasset").getNodeValue();

			Cursor cursor = db.rawQuery("SELECT id FROM " + "control", null);
			try {
				if(cursor.moveToFirst())
			    {
			    	cv.put("lastinsertasset", lastinsertasset);
			    	datasource.updateData(db,cv,"control",null);
			    }
			    else{
			    	cv.put("lastinsertasset", lastinsertasset);
			    	datasource.generateData(db,cv,"control");
			    }
			}
			finally{
				cursor.close();
        	}

			cv.clear();
			docb = null;
			doc = null;

			return true;

		}
		catch (Exception e) {
			Log.e(tag,"Error occurred in GetAssetData : " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		finally{
			datasource.close();
		    db.close();
    	}
	}

    public boolean GetSupplier(Context context, String url) {
		try {

			datasource = new DataSource(context);
	        db = datasource.getWritableDatabase();
	        ContentValues cv = new ContentValues();
	        ArrayList<String> supplierExists = new ArrayList<String>();
	        ArrayList<String> supplierDelete = new ArrayList<String>();

			String [] supplierDeleteArray = null;
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

			InputStream in = null;

			SharedPreferences generalPrefs = PreferenceManager.getDefaultSharedPreferences(context);

			Calendar cl = Calendar.getInstance();
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
            String myTime = df.format(cl.getTime());
            String theKey;

            theKey = encrypt(myTime,context.getString(R.string.Key44));
            String branchID = encrypt(generalPrefs.getString("branchID", null),myTime);
            String supplierid = encrypt(sbl.toString(),myTime);

	        URL localUrl = new URL(url + "/GetSupplier_New?a=" + URLEncoder.encode(branchID,"UTF-8") +
					"&b=" + URLEncoder.encode(supplierid,"UTF-8")+
					"&c=" + URLEncoder.encode(theKey,"UTF-8"));

			Log.d("localUrl", "GetSupplier: "+ localUrl );

			URLConnection conn = localUrl.openConnection();
		    HttpURLConnection httpConn = (HttpURLConnection) conn;
		    httpConn.setAllowUserInteraction(false);
		    httpConn.setInstanceFollowRedirects(true);
	     	httpConn.setRequestMethod("GET");
	     	httpConn.connect();
	     	httpConn.getResponseCode();

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

			for (int i=0;i<fields.getLength();i++) {
				Node fieldNode = fields.item(i);
				NamedNodeMap map = fieldNode.getAttributes();

				cv.put("supplierid", map.getNamedItem("supplierid").getNodeValue());
				cv.put("Foreign_Key", map.getNamedItem("Foreign_Key").getNodeValue());
				cv.put("suppliername", map.getNamedItem("suppliername").getNodeValue());

				datasource.generateData(db, cv, "supplier");
				cv.clear();
			}

			Node fieldNode = fields.item(0);
			NamedNodeMap map = fieldNode.getAttributes();
			lastinsertsupplier = map.getNamedItem("lastinsertsupplier").getNodeValue();

			Cursor cursor = db.rawQuery("SELECT id FROM control", null);
			try {
				if(cursor.moveToFirst())
			    {
			    	cv.put("lastinsertsupplier", lastinsertsupplier);
			    	datasource.updateData(db,cv,"control",null);
			    }
			    else{
			    	cv.put("lastinsertsupplier", lastinsertsupplier);
			    	datasource.generateData(db,cv,"control");
			    }
			}
			finally{
				cursor.close();
        	}

			NodeList deleteFields = root.getElementsByTagName("delete");

			for (int i=0;i<deleteFields.getLength();i++) {
				Node fieldNodeDelete = deleteFields.item(i);
				NamedNodeMap attr = fieldNodeDelete.getAttributes();

				supplierDelete.add(attr.getNamedItem("supplierid").getNodeValue().trim());
			}

			supplierDeleteArray = supplierDelete.toArray(new String[supplierDelete.size()]);
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < supplierDeleteArray.length; i++) {
				if(i == 0){
					sb.append("'" + supplierDeleteArray[i] + "'");
				}else{
					sb.append(",'" + supplierDeleteArray[i] + "'");
				}
			}

			datasource.deleteData(db, "supplierid in(" + sb.toString() + ")", "supplier");

			docb = null;
			doc = null;

			return true;

		}
		catch (Exception e) {
			Log.e(tag,"Error occurred in GetSupplier : " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		finally{
			datasource.close();
		    db.close();
    	}
	}


	public boolean GetZipCodeData(Context context, String url) {
		try {
			datasource = new DataSource(context);
			db = datasource.getWritableDatabase();
			ContentValues cv = new ContentValues();

			Cursor cr = db.rawQuery("SELECT id FROM zipcode", null);
			try{
				if(cr.moveToFirst())
				{
					datasource.deleteData(db, null, "zipcode");
				}
			}
			finally{
				cr.close();
			}

			InputStream in = null;

			URL localUrl = new URL(url + "/GetZipCode");
			Log.d("localUrl", "GetZipCodeData: "+ localUrl);
			URLConnection conn = localUrl.openConnection();

			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			httpConn.getResponseCode();

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

			NodeList fields = root.getElementsByTagName("zipcode");

			for (int i=0;i<fields.getLength();i++) {
				Node fieldNode = fields.item(i);
				Log.d("waduh", "GetZipCodeData: "+fields.getLength());
				NamedNodeMap map = fieldNode.getAttributes();

				cv.put("KOTA", map.getNamedItem("KOTA").getNodeValue());
				cv.put("KECAMATAN", map.getNamedItem("KECAMATAN").getNodeValue());
				cv.put("KELURAHAN", map.getNamedItem("KELURAHAN").getNodeValue());
				cv.put("Description", map.getNamedItem("Description").getNodeValue());

				datasource.generateData(db, cv, "zipcode");
				cv.clear();
			}


			Node fieldNode = fields.item(0);
			NamedNodeMap map = fieldNode.getAttributes();
			lastinsertzipcode = map.getNamedItem("lastinsertzipcode").getNodeValue();

			Cursor cursor = db.rawQuery("SELECT id FROM " + "control", null);
			try {
				if(cursor.moveToFirst())
				{
					cv.put("lastinsertzipcode", lastinsertasset);
					datasource.updateData(db,cv,"control",null);
				}
				else{
					cv.put("lastinsertzipcode", lastinsertasset);
					datasource.generateData(db,cv,"control");
				}
			}
			finally{
				cursor.close();
			}

			cv.clear();
			docb = null;
			doc = null;

			return true;

		}
		catch (Exception e) {
			Log.e(tag,"Error occurred in GetZipCodeData : " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		finally{
			datasource.close();
			db.close();
		}
	}

	public boolean GetProductData(Context context, String url) {
		try {
			datasource = new DataSource(context);
			db = datasource.getWritableDatabase();
			ContentValues cv = new ContentValues();

			Cursor cr = db.rawQuery("SELECT id FROM product", null);
			try{
				if(cr.moveToFirst())
				{
					datasource.deleteData(db, null, "product");
				}
			}
			finally{
				cr.close();
			}

			InputStream in = null;

			SharedPreferences generalPrefs = PreferenceManager.getDefaultSharedPreferences(context);

			Calendar cl = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
			String myTime = df.format(cl.getTime());
			String theKey;

			theKey = encrypt(myTime,context.getString(R.string.Key44));
			String branchID = encrypt(generalPrefs.getString("branchID", null),myTime);

			URL localUrl = new URL(url + "/GetProductData?a=" + URLEncoder.encode(branchID,"UTF-8") +
					"&b=" + URLEncoder.encode(theKey,"UTF-8"));

			Log.d("localUrl", "GetProductData: "+ localUrl );

//			URL localUrl = new URL(url + "/GetProductData");
//			Log.d("localUrl", "GetProductData: "+ localUrl);
			URLConnection conn = localUrl.openConnection();

			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			httpConn.getResponseCode();

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

			NodeList fields = root.getElementsByTagName("Product");

			for (int i=0;i<fields.getLength();i++) {
				Node fieldNode = fields.item(i);
				NamedNodeMap map = fieldNode.getAttributes();

				cv.put("productid", map.getNamedItem("productid").getNodeValue());
				cv.put("productname", map.getNamedItem("productname").getNodeValue());
				//cv.put("BranchID", map.getNamedItem("BranchID").getNodeValue());
				cv.put("AssetUsedNew", map.getNamedItem("AssetUsedNew").getNodeValue());
				//cv.put("FirstInstallment", map.getNamedItem("FirstInstallment").getNodeValue());
				//cv.put("ISKPM", map.getNamedItem("ISKPM").getNodeValue());



				datasource.generateData(db, cv, "product");
				cv.clear();
			}


			Node fieldNode = fields.item(0);
			NamedNodeMap map = fieldNode.getAttributes();
            lastinsertproduct = map.getNamedItem("lastinsertproduct").getNodeValue();

			Cursor cursor = db.rawQuery("SELECT id FROM " + "control", null);
			try {
				if(cursor.moveToFirst())
				{
					cv.put("lastinsertproduct", lastinsertproduct);
					datasource.updateData(db,cv,"control",null);
				}
				else{
					cv.put("lastinsertproduct", lastinsertproduct);
					datasource.generateData(db,cv,"control");
				}
			}
			finally{
				cursor.close();
			}

			cv.clear();
			docb = null;
			doc = null;

			return true;

		}
		catch (Exception e) {
			Log.e(tag,"Error occurred in GetProductData : " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		finally{
			datasource.close();
			db.close();
		}
	}

	public boolean GetEventData(Context context, String url) {
		try {
			datasource = new DataSource(context);
			db = datasource.getWritableDatabase();
			ContentValues cv = new ContentValues();

			Cursor cr = db.rawQuery("SELECT id FROM event", null);
			try{
				if(cr.moveToFirst())
				{
					datasource.deleteData(db, null, "event");
				}
			}
			finally{
				cr.close();
			}

			InputStream in = null;


			URL localUrl = new URL(url + "/GetEventData");
			Log.d("localUrl", "GetEventData: "+ localUrl);
			URLConnection conn = localUrl.openConnection();

			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			httpConn.getResponseCode();

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

			NodeList fields = root.getElementsByTagName("Event");

			for (int i=0;i<fields.getLength();i++) {
				Node fieldNode = fields.item(i);
				NamedNodeMap map = fieldNode.getAttributes();

				cv.put("eventid", map.getNamedItem("eventid").getNodeValue());
				cv.put("eventname", map.getNamedItem("eventname").getNodeValue());

				datasource.generateData(db, cv, "event");
				cv.clear();
			}


//			Node fieldNode = fields.item(0);
//			NamedNodeMap map = fieldNode.getAttributes();
//			lastinsertproduct = map.getNamedItem("LastInsertZipCode").getNodeValue();
//
//			Cursor cursor = db.rawQuery("SELECT id FROM " + "control", null);
//			try {
//				if(cursor.moveToFirst())
//				{
//					cv.put("lastinsertproduct", lastinsertasset);
//					datasource.updateData(db,cv,"control",null);
//				}
//				else{
//					cv.put("lastinsertproduct", lastinsertasset);
//					datasource.generateData(db,cv,"control");
//				}
//			}
//			finally{
//				cursor.close();
//			}

			cv.clear();
			docb = null;
			doc = null;

			return true;

		}
		catch (Exception e) {
			Log.e(tag,"Error occurred in GetEventData : " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		finally{
			datasource.close();
			db.close();
		}
	}

	public boolean GetSalesData(Context context, String url) {
		try {
			datasource = new DataSource(context);
			db = datasource.getWritableDatabase();
			ContentValues cv = new ContentValues();

			Cursor cr = db.rawQuery("SELECT id FROM salesnew", null);
			try{
				if(cr.moveToFirst())
				{
					datasource.deleteData(db, null, "salesnew");
				}
			}
			finally{
				cr.close();
			}

			InputStream in = null;

			SharedPreferences generalPrefs = PreferenceManager.getDefaultSharedPreferences(context);

			Calendar cl = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
			String myTime = df.format(cl.getTime());
			String theKey;

			theKey = encrypt(myTime,context.getString(R.string.Key44));
			String branchID = encrypt(generalPrefs.getString("branchID", null),myTime);

			URL localUrl = new URL(url + "/GetSalesData?a=" + URLEncoder.encode(branchID,"UTF-8") +
					"&b=" + URLEncoder.encode(theKey,"UTF-8"));

			Log.d("localUrl", "GetSalesData: "+ localUrl );

//			URL localUrl = new URL(url + "/GetProductOfferingData");
//			Log.d("localUrl", "GetProductOfferingData: "+ localUrl);
			URLConnection conn = localUrl.openConnection();

			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			httpConn.getResponseCode();

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

			NodeList fields = root.getElementsByTagName("Sales");

			for (int i=0;i<fields.getLength();i++) {
				Node fieldNode = fields.item(i);
				NamedNodeMap map = fieldNode.getAttributes();

				cv.put("SupplierID", map.getNamedItem("SupplierID").getNodeValue());
				cv.put("SupplierEmployeeID", map.getNamedItem("SupplierEmployeeID").getNodeValue());
				cv.put("SupplierEmployeeName", map.getNamedItem("SupplierEmployeeName").getNodeValue());
				cv.put("SupplierEmployeePosition", map.getNamedItem("SupplierEmployeePosition").getNodeValue());
				cv.put("BranchID", map.getNamedItem("BranchID").getNodeValue());



				datasource.generateData(db, cv, "salesnew");
				cv.clear();
			}


			Node fieldNode = fields.item(0);
			NamedNodeMap map = fieldNode.getAttributes();
			lastinsertsales = map.getNamedItem("lastinsertsales").getNodeValue();

			Cursor cursor = db.rawQuery("SELECT id FROM " + "control", null);
			try {
				if(cursor.moveToFirst())
				{
					cv.put("lastinsertsales", lastinsertsales);
					datasource.updateData(db,cv,"control",null);
				}
				else{
					cv.put("lastinsertsales", lastinsertsales);
					datasource.generateData(db,cv,"control");
				}
			}
			finally{
				cursor.close();
			}

			cv.clear();
			docb = null;
			doc = null;

			return true;

		}
		catch (Exception e) {
			Log.e(tag,"Error occurred in GetProductOfferingData : " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		finally{
			datasource.close();
			db.close();
		}
	}

	public boolean GetProductOfferingData(Context context, String url) {
		try {
			datasource = new DataSource(context);
			db = datasource.getWritableDatabase();
			ContentValues cv = new ContentValues();

			Cursor cr = db.rawQuery("SELECT id FROM productoffering", null);
			try{
				if(cr.moveToFirst())
				{
					datasource.deleteData(db, null, "productoffering");
				}
			}
			finally{
				cr.close();
			}

			InputStream in = null;

			SharedPreferences generalPrefs = PreferenceManager.getDefaultSharedPreferences(context);

			Calendar cl = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
			String myTime = df.format(cl.getTime());
			String theKey;

			theKey = encrypt(myTime,context.getString(R.string.Key44));
			String branchID = encrypt(generalPrefs.getString("branchID", null),myTime);

			URL localUrl = new URL(url + "/GetProductOfferingData?a=" + URLEncoder.encode(branchID,"UTF-8") +
					"&b=" + URLEncoder.encode(theKey,"UTF-8"));

			Log.d("localUrl", "GetProductOfferingData: "+ localUrl );

//			URL localUrl = new URL(url + "/GetProductData");
//			Log.d("localUrl", "GetProductData: "+ localUrl);
			URLConnection conn = localUrl.openConnection();

			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			httpConn.getResponseCode();

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

			NodeList fields = root.getElementsByTagName("ProductOffering");

			for (int i=0;i<fields.getLength();i++) {
				Node fieldNode = fields.item(i);
				NamedNodeMap map = fieldNode.getAttributes();

				cv.put("ProductID", map.getNamedItem("ProductID").getNodeValue());
				cv.put("ProductOfferingID", map.getNamedItem("ProductOfferingID").getNodeValue());
				cv.put("Description", map.getNamedItem("Description").getNodeValue());



				datasource.generateData(db, cv, "productoffering");
				cv.clear();
			}


			Node fieldNode = fields.item(0);
			NamedNodeMap map = fieldNode.getAttributes();
			lastinsertproductoffering = map.getNamedItem("lastinsertproductoffering").getNodeValue();

			Cursor cursor = db.rawQuery("SELECT id FROM " + "control", null);
			try {
				if(cursor.moveToFirst())
				{
					cv.put("lastinsertproductoffering", lastinsertproductoffering);
					datasource.updateData(db,cv,"control",null);
				}
				else{
					cv.put("lastinsertproductoffering", lastinsertproductoffering);
					datasource.generateData(db,cv,"control");
				}
			}
			finally{
				cursor.close();
			}

			cv.clear();
			docb = null;
			doc = null;

			return true;

		}
		catch (Exception e) {
			Log.e(tag,"Error occurred in GetProductData : " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		finally{
			datasource.close();
			db.close();
		}
	}


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

             //byte[] array = md.digest(plaintext.getBytes());
 	        //StringBuffer sb = new StringBuffer();
 	        //for (int i = 0; i < array.length; ++i) {
 	        //  sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
 	        //}
 	        //return sb.toString();

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

    @SuppressWarnings("deprecation")
	protected String encryptlocal(String value, Context context) {

        try {
            final byte[] bytes = value!=null ? value.getBytes(UTF8) : new byte[0];
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SEKRIT));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(Settings.Secure.getString(context.getContentResolver(),Settings.System.ANDROID_ID).getBytes(UTF8), 20));
            return new String(Base64.encode(pbeCipher.doFinal(bytes), Base64.NO_WRAP),UTF8);

        } catch( Exception e ) {
            throw new RuntimeException(e);
        }

    }

    @SuppressWarnings("deprecation")
	protected String decryptlocal(String value, Context context){
        try {
            final byte[] bytes = value!=null ? Base64.decode(value,Base64.DEFAULT) : new byte[0];
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SEKRIT));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(Settings.Secure.getString(context.getContentResolver(),Settings.System.ANDROID_ID).getBytes(UTF8), 20));
            return new String(pbeCipher.doFinal(bytes),UTF8);

        } catch( Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
	public void onDestroy() {
    	super.onDestroy();
    	datasource.close();
	    db.close();
	}

}