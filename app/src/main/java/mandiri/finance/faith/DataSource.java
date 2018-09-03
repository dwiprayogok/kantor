package mandiri.finance.faith;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import mandiri.finance.faith.Model.Supplier;
import mandiri.finance.faith.Model.Tempatlahir;
import mandiri.finance.faith.Model.ZipCode;

public class DataSource extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "myDb";
	private String lastinsertasset = null;
	private String lastinsertsales = null;
	private String lastinsertproduct = null;
	private String lastinsertsupplier = null;
	private String lastinsertproductoffering = null;
	public DataSource(Context context) {
		super(context, DATABASE_NAME , null, 40);
	 }
	
	public List<String> getAllLabels(SQLiteDatabase db, String selectQuery){
				
        List<String> labels = new ArrayList<String>();
 
        // Select All Query
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list        
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
 
        // closing connection
        cursor.close();
                
        return labels;
    }
	
	public void createTableImage(SQLiteDatabase db){
    	db.execSQL("DROP TABLE IF EXISTS image;");
    	db.execSQL("CREATE TABLE if not exists image" + 
    		" (id INTEGER PRIMARY KEY AUTOINCREMENT, imageStr TEXT, buttonId TEXT, lat TEXT, lng TEXT);");        
    }

	public void createTableImageCollection(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS imagecollection;");
		db.execSQL("CREATE TABLE if not exists imagecollection" +
				" (id INTEGER PRIMARY KEY AUTOINCREMENT, imageStr TEXT, buttonId TEXT, lat TEXT, lng TEXT);");
	}


	public void createTableImageSurveyLocation(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS imagesurveylocation;");
		db.execSQL("CREATE TABLE if not exists imagesurveylocation" +
				" (id INTEGER PRIMARY KEY AUTOINCREMENT, imageStr TEXT, buttonId TEXT, lat TEXT, lng TEXT);");
	}

	public void createTableResult(SQLiteDatabase db){
    	db.execSQL("CREATE TABLE if not exists result (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"fieldname TEXT, fieldvalue TEXT, imagestr TEXT, imagecount TEXT, moduleid TEXT, key TEXT, lat TEXT, lng TEXT, dtmupd TEXT, " +
			"userid TEXT, imagelat TEXT, imagelng TEXT, custtype TEXT, grading TEXT, branchID TEXT);");
    }


	public void createTablePrintCash(SQLiteDatabase db){
		db.execSQL("CREATE TABLE if not exists printcash (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"fieldname TEXT, fieldvalue TEXT, moduleid TEXT, key TEXT, userid TEXT, receiptNo TEXT, insNo TEXT, " +
				"dueDate TEXT, custNamePrint TEXT, collInitial TEXT, licensePlate TEXT, lasttransaction TEXT,"+
				"status TEXT, strTotal TEXT, paidBy TEXT, counter INT, paiddate TEXT);");
	}

	public void createTableForm(SQLiteDatabase db){
    	db.execSQL("CREATE TABLE if not exists form" + 
    		" (id INTEGER PRIMARY KEY AUTOINCREMENT, moduleid TEXT, name TEXT, label TEXT, " +
    		"type TEXT, required TEXT, optionsname TEXT, optionsid TEXT, hint TEXT, errmsg TEXT, maxlength TEXT," +
				" issearchable TEXT, custtype TEXT);");
    } 
	
	public void createTableInfoDetail(SQLiteDatabase db){
    	db.execSQL("CREATE TABLE if not exists infodetail" + 
    		" (id INTEGER PRIMARY KEY AUTOINCREMENT, moduleid TEXT, idx TEXT, infolabel TEXT, infotext TEXT);");        
    }
	
	public void createTableImageURLDetail(SQLiteDatabase db){
    	db.execSQL("CREATE TABLE if not exists imageurldetail" + 
    		" (id INTEGER PRIMARY KEY AUTOINCREMENT, moduleid TEXT, idx TEXT, imageurl TEXT);");        
    }


	public void createTableGuide(SQLiteDatabase db){
		db.execSQL("CREATE TABLE if not exists guide" +
				" (id INTEGER PRIMARY KEY AUTOINCREMENT, pertanyaan TEXT, isi_pertanyaan TEXT, closing_pertanyaan TEXT);");
	}


	public void createTableGrading(SQLiteDatabase db){
		db.execSQL("CREATE TABLE if not exists grading" +
				" (id INTEGER PRIMARY KEY AUTOINCREMENT, IDNumber TEXT, CustomerID TEXT, CustomerGrade TEXT);");
	}


	public void createTableSizeZipcode(SQLiteDatabase db){
		db.execSQL("CREATE TABLE if not exists SizeZipcode" +
				" (id INTEGER PRIMARY KEY AUTOINCREMENT, TotalSize TEXT);");
	}




//	public void createTableControl(SQLiteDatabase db){
//    	db.execSQL("CREATE TABLE if not exists control (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//			"lastinsertprospectform TEXT, lastinsertsurveyform TEXT, lastinsertcollectionform TEXT, " +
//			"lastinsertasset TEXT, lastinsertsupplier TEXT, lastinsertcollectionplanform TEXT, " +
//			"lastinsertsurveylocform TEXT);");
//    }




	public void createTableControl(SQLiteDatabase db){
		db.execSQL("CREATE TABLE if not exists control (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"lastinsertprospectform TEXT, lastinsertsurveyform TEXT, lastinsertcollectionform TEXT, " +
				"lastinsertasset TEXT, lastinsertsupplier TEXT, lastinsertcollectionplanform TEXT, " +
				"lastinsertsurveylocform TEXT, lastinsertzipcode TEXT, lastinsertproduct TEXT, " +
				"lastinsertproductoffering TEXT, lastinserttempatlahir TEXT, lastinsertsales TEXT);");
	}
	
	public void createTableAsset(SQLiteDatabase db){
    	db.execSQL("CREATE TABLE if not exists asset (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"assetcode TEXT, assettype TEXT, assetdetail TEXT);");
    }
	
	public void createTableSupplier(SQLiteDatabase db){
    	db.execSQL("CREATE TABLE if not exists supplier (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"supplierid TEXT, Foreign_Key TEXT, suppliername TEXT);");
    }

	public void createTableProduct(SQLiteDatabase db){
		db.execSQL("CREATE TABLE if not exists product (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"productid TEXT, productname TEXT, AssetUsedNew TEXT);");
	}

	public void createTableNews(SQLiteDatabase db){
		db.execSQL("CREATE TABLE if not exists news" +
				" (id INTEGER PRIMARY KEY AUTOINCREMENT, header TEXT, isi TEXT, footer TEXT);");
	}


	public void createTableProductOffering(SQLiteDatabase db){
		db.execSQL("CREATE TABLE if not exists productoffering (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"ProductID TEXT, ProductOfferingID TEXT, Description TEXT);");
	}

	public void createTableEvent(SQLiteDatabase db){
		db.execSQL("CREATE TABLE if not exists event (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"eventid TEXT, eventname TEXT);");
	}


	public void createTableSales(SQLiteDatabase db){
		db.execSQL("CREATE TABLE if not exists salesnew (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"SupplierID TEXT, SupplierEmployeeID TEXT, SupplierEmployeeName TEXT, SupplierEmployeePosition TEXT, " +
				"BranchID TEXT);");
	}
	
	public void createTableSurveyList(SQLiteDatabase db){
    	db.execSQL("CREATE TABLE if not exists surveylist (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"headertext TEXT, detailtext TEXT, prospectdate TEXT, imageurl TEXT, customerid TEXT, status TEXT, distance INT, custtype TEXT);");
    }
	
	public void createTableSurveyLocList(SQLiteDatabase db){
    	db.execSQL("CREATE TABLE if not exists surveyloclist (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"headertext TEXT, detailtext TEXT, surveydate TEXT, imageurl TEXT, customerid TEXT, status TEXT, custtype TEXT);");
    }

	public void createTableZipCode(SQLiteDatabase db){
		db.execSQL("CREATE TABLE if not exists zipcode (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"KOTA TEXT, KECAMATAN TEXT, KELURAHAN TEXT, Description TEXT);");
	}





	public void createTableCollectionList(SQLiteDatabase db){
		db.execSQL("CREATE TABLE if not exists collectionlist (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"headertext TEXT, detailtext TEXT, duedate TEXT, contractno TEXT, status TEXT, insno TEXT,"+
				"collinitial TEXT, distance INT, taskdate TEXT, receiptno TEXT, insamount INT, licenseplate TEXT, lat TEXT, lng TEXT, OD INTEGER, installamount REAL);");
	}
	
	public void createTableCollectionResult(SQLiteDatabase db){
    	db.execSQL("CREATE TABLE if not exists collectionresult (id INTEGER PRIMARY KEY AUTOINCREMENT, result TEXT);");
    }
		
	public void createTableLocationSource(SQLiteDatabase db){
    	db.execSQL("CREATE TABLE if not exists locationsource (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"lat TEXT, lng TEXT, dtmupd TEXT, userid TEXT, sourceid TEXT);");
    } 
	
	public void dropTable(SQLiteDatabase db, String tableName){
    	db.execSQL("DROP TABLE IF EXISTS " + tableName + ";" );        
    } 
    
    public void generateData(SQLiteDatabase db, ContentValues cv, String tableName){        
        db.insert(tableName, null, cv);        
    }
    
    public void updateData(SQLiteDatabase db, ContentValues cv, String tableName, String whereClause){    
    	db.update(tableName, cv, whereClause, null);       	
    }
    
    public void deleteData(SQLiteDatabase db, String whereClause, String tableName){    
    	db.delete(tableName, whereClause, null);       	
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        
    }

		// untuk zipcode
	public void deletekelurahan(){
		SQLiteDatabase db = this.getWritableDatabase();

		String DELETE_KELURAHAN_TBLL =  "DELETE FROM zipcode";

		db.execSQL(DELETE_KELURAHAN_TBLL);


	}

	public int getKelurahanModelCount() {
		int num = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		try{
			String QUERY = "SELECT * FROM zipcode";
			Cursor cursor = db.rawQuery(QUERY, null);
			num = cursor.getCount();
			Log.d("ceklagi", "getKelurahanModelCount: "+num);
			cursor.close();
			db.close();
			return num;
		}catch (Exception e){
			Log.e("error",e+"");
		}
		return 0;
	}

	public void insertprovinsi(NodeList json){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		db.beginTransaction();
		try{
			Log.d("ceklagi", "insertprovinsi: "+json.getLength());
			for (int i=0;i<json.getLength();i++) {
				Node fieldNode = json.item(i);
				Log.d("waduh", "GetZipCodeData: "+json.getLength());
				NamedNodeMap map = fieldNode.getAttributes();

				cv.put("KOTA", map.getNamedItem("KOTA").getNodeValue());
				cv.put("KECAMATAN", map.getNamedItem("KECAMATAN").getNodeValue());
				cv.put("KELURAHAN", map.getNamedItem("KELURAHAN").getNodeValue());
				cv.put("Description", map.getNamedItem("Description").getNodeValue());
				db.insert("zipcode","",cv);
				//datasource.generateData(db, cv, "zipcode");

				ZipCode ZipCode = new ZipCode();
				ZipCode.setKota(map.getNamedItem("KOTA").getNodeValue());
				ZipCode.setKecamatan(map.getNamedItem("KECAMATAN").getNodeValue());
				ZipCode.setKelurahan(map.getNamedItem("KELURAHAN").getNodeValue());
				ZipCode.setZipcode(map.getNamedItem("Description").getNodeValue());

				cv.clear();
			}


			Node fieldNode = json.item(0);
			NamedNodeMap map = fieldNode.getAttributes();
			lastinsertasset = map.getNamedItem("lastinsertzipcode").getNodeValue();

			Cursor cursor = db.rawQuery("SELECT id FROM " + "control", null);
			try {
				if(cursor.moveToFirst())
				{
					cv.put("lastinsertzipcode", lastinsertasset);
					db.update("control",cv,null,null);
					//datasource.updateData(db,cv,"control",null);
				}
				else{
					cv.put("lastinsertzipcode", lastinsertasset);
					db.update("control",cv,null,null);
					//datasource.generateData(db,cv,"control");
				}
			}
			finally{
				cursor.close();
			}
			db.setTransactionSuccessful();
		}catch (Exception e){
			Log.e("problem",e+"");
		}
		finally {
			db.endTransaction();
		}

	}
		// untuk zipcode




	// untuk tempat Sales
	public void deletesales(){
		SQLiteDatabase db = this.getWritableDatabase();

		String DELETE_salesnew_TBLL =  "DELETE FROM salesnew";

		db.execSQL(DELETE_salesnew_TBLL);


	}

	public int getSalesnewModelCount() {
		int num = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		try{
			String QUERY = "SELECT * FROM salesnew";
			Cursor cursor = db.rawQuery(QUERY, null);
			num = cursor.getCount();
			Log.d("cek", "getSalesnewModelCount: "+ num);
			cursor.close();
			db.close();
			return num;
		}catch (Exception e){
			Log.e("error",e+"");
		}
		return 0;
	}

	public void insertSales(NodeList json){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		db.beginTransaction();
		try{
			Log.d("ceklagi", "insertSales: "+json.getLength());
			for (int i=0;i<json.getLength();i++) {
				Node fieldNode = json.item(i);
				Log.d("waduh", "GetSalesData: "+json.getLength());



				NamedNodeMap map = fieldNode.getAttributes();

				cv.put("SupplierID", map.getNamedItem("SupplierID").getNodeValue());
				cv.put("SupplierEmployeeID", map.getNamedItem("SupplierEmployeeID").getNodeValue());
				cv.put("SupplierEmployeeName", map.getNamedItem("SupplierEmployeeName").getNodeValue());
				cv.put("SupplierEmployeePosition", map.getNamedItem("SupplierEmployeePosition").getNodeValue());
				cv.put("BranchID", map.getNamedItem("BranchID").getNodeValue());

				db.insert("salesnew","",cv);

				Supplier Supplier = new Supplier();
				Supplier.setSupplierid(map.getNamedItem("SupplierID").getNodeValue());
				Supplier.setSupplierEmployeeID(map.getNamedItem("SupplierEmployeeID").getNodeValue());
				Supplier.setSupplierEmployeeName(map.getNamedItem("SupplierEmployeeName").getNodeValue());
				Supplier.setSupplierEmployeePosition(map.getNamedItem("SupplierEmployeePosition").getNodeValue());

				Log.d("adada", "insertSales: "+ map.getNamedItem("SupplierID").getNodeValue());
				//datasource.generateData(db, cv, "zipcode");
				cv.clear();
			}


			Node fieldNode = json.item(0);
			NamedNodeMap map = fieldNode.getAttributes();
			lastinsertsales = map.getNamedItem("lastinsertsales").getNodeValue();

			Cursor cursor = db.rawQuery("SELECT id FROM " + "control", null);
			try {
				if(cursor.moveToFirst())
				{
					cv.put("lastinsertsales", lastinsertsales);
					db.update("control",cv,null,null);
					//datasource.updateData(db,cv,"control",null);
				}
				else{
					cv.put("lastinsertsales", lastinsertsales);
					db.update("control",cv,null,null);
					//datasource.generateData(db,cv,"control");
				}
			}
			finally{
				cursor.close();
			}
			db.setTransactionSuccessful();
		}catch (Exception e){
			Log.e("problem",e+"");
		}
		finally {
			db.endTransaction();
		}

	}






	// untuk tempat ProductOFfering
	public void deleteProductOffering(){
		SQLiteDatabase db = this.getWritableDatabase();

		String DELETE_salesnew_TBLL =  "DELETE FROM productoffering";

		db.execSQL(DELETE_salesnew_TBLL);


	}

	public int getProductOfferingModelCount() {
		int num = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		try{
			String QUERY = "SELECT * FROM productoffering";
			Cursor cursor = db.rawQuery(QUERY, null);
			num = cursor.getCount();
			Log.d("cek", "getProductModelCount: "+ num);
			cursor.close();
			db.close();
			return num;
		}catch (Exception e){
			Log.e("error",e+"");
		}
		return 0;
	}

	public void insertProductOffering(NodeList json){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		db.beginTransaction();
		try{
			Log.d("ceklagi", "insertProductOffering: "+json.getLength());
			for (int i=0;i<json.getLength();i++) {
				Node fieldNode = json.item(i);

				Log.d("waduh", "GetproductOFfering: "+json.getLength());
				NamedNodeMap map = fieldNode.getAttributes();


				cv.put("ProductID", map.getNamedItem("ProductID").getNodeValue());
				cv.put("ProductOfferingID", map.getNamedItem("ProductOfferingID").getNodeValue());
				cv.put("Description", map.getNamedItem("Description").getNodeValue());


				db.insert("productoffering","",cv);
				//datasource.generateData(db, cv, "zipcode");
				cv.clear();
			}


			Node fieldNode = json.item(0);
			NamedNodeMap map = fieldNode.getAttributes();
			lastinsertproductoffering = map.getNamedItem("lastinsertproductoffering").getNodeValue();

			Cursor cursor = db.rawQuery("SELECT id FROM " + "control", null);
			try {
				if(cursor.moveToFirst())
				{
					cv.put("lastinsertproductoffering", lastinsertproductoffering);
					db.update("control",cv,null,null);
					//datasource.updateData(db,cv,"control",null);
				}
				else{
					cv.put(lastinsertproductoffering, lastinsertproductoffering);
					db.update("control",cv,null,null);
					//datasource.generateData(db,cv,"control");
				}
			}
			finally{
				cursor.close();
			}
			db.setTransactionSuccessful();
		}catch (Exception e){
			Log.e("problem",e+"");
		}
		finally {
			db.endTransaction();
		}

	}



	// untuk tempat Product
	public void deleteProduct(){
		SQLiteDatabase db = this.getWritableDatabase();

		String DELETE_salesnew_TBLL =  "DELETE FROM product";

		db.execSQL(DELETE_salesnew_TBLL);


	}

	public int getProductModelCount() {
		int num = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		try{
			String QUERY = "SELECT * FROM product";
			Cursor cursor = db.rawQuery(QUERY, null);
			num = cursor.getCount();
			Log.d("cek", "getProductModelCount: "+ num);
			cursor.close();
			db.close();
			return num;
		}catch (Exception e){
			Log.e("error",e+"");
		}
		return 0;
	}

	public void insertProduct(NodeList json){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		db.beginTransaction();
		try{
			Log.d("ceklagi", "insertproduct: "+json.getLength());
			for (int i=0;i<json.getLength();i++) {
				Node fieldNode = json.item(i);

				Log.d("waduh", "Getproduct: "+json.getLength());
				NamedNodeMap map = fieldNode.getAttributes();

				cv.put("productid", map.getNamedItem("productid").getNodeValue());
				cv.put("productname", map.getNamedItem("productname").getNodeValue());
				//cv.put("BranchID", map.getNamedItem("BranchID").getNodeValue());
				cv.put("AssetUsedNew", map.getNamedItem("AssetUsedNew").getNodeValue());

				db.insert("product","",cv);
				//datasource.generateData(db, cv, "zipcode");
				cv.clear();
			}


			Node fieldNode = json.item(0);
			NamedNodeMap map = fieldNode.getAttributes();
			lastinsertproduct = map.getNamedItem("lastinsertproduct").getNodeValue();

			Cursor cursor = db.rawQuery("SELECT id FROM " + "control", null);
			try {
				if(cursor.moveToFirst())
				{
					cv.put("lastinsertproduct", lastinsertproduct);
					db.update("control",cv,null,null);
					//datasource.updateData(db,cv,"control",null);
				}
				else{
					cv.put("lastinsertproduct", lastinsertproduct);
					db.update("control",cv,null,null);
					//datasource.generateData(db,cv,"control");
				}
			}
			finally{
				cursor.close();
			}
			db.setTransactionSuccessful();
		}catch (Exception e){
			Log.e("problem",e+"");
		}
		finally {
			db.endTransaction();
		}

	}


	// untuk tempat Supplier
	public void deleteSupplier(){
		SQLiteDatabase db = this.getWritableDatabase();

		String DELETE_salesnew_TBLL =  "DELETE FROM supplier";

		db.execSQL(DELETE_salesnew_TBLL);


	}

	public int getSupplierModelCount() {
		int num = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		try{
			String QUERY = "SELECT * FROM supplier";
			Cursor cursor = db.rawQuery(QUERY, null);
			num = cursor.getCount();
			Log.d("cek", "getSupplierModelCount: "+ num);
			cursor.close();
			db.close();
			return num;
		}catch (Exception e){
			Log.e("error",e+"");
		}
		return 0;
	}

	public void insertSupplier(NodeList json){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		db.beginTransaction();
		try{

			for (int i=0;i<json.getLength();i++) {
				Node fieldNode = json.item(i);

				Log.d("waduh", "GetSupplier: "+json.getLength());
				NamedNodeMap map = fieldNode.getAttributes();

				cv.put("supplierid", map.getNamedItem("supplierid").getNodeValue());
				cv.put("Foreign_Key", map.getNamedItem("Foreign_Key").getNodeValue());
				cv.put("suppliername", map.getNamedItem("suppliername").getNodeValue());

				db.insert("supplier","",cv);
				//datasource.generateData(db, cv, "zipcode");
				cv.clear();
			}


			Node fieldNode = json.item(0);
			NamedNodeMap map = fieldNode.getAttributes();
			lastinsertsupplier = map.getNamedItem("lastinsertsupplier").getNodeValue();

			Cursor cursor = db.rawQuery("SELECT id FROM " + "control", null);
			try {
				if(cursor.moveToFirst())
				{
					cv.put("lastinsertsupplier", lastinsertsupplier);
					db.update("control",cv,null,null);
					//datasource.updateData(db,cv,"control",null);
				}
				else{
					cv.put("lastinsertsupplier", lastinsertsupplier);
					db.update("control",cv,null,null);
					//datasource.generateData(db,cv,"control");
				}
			}
			finally{
				cursor.close();
			}
			db.setTransactionSuccessful();
		}catch (Exception e){
			Log.e("problem",e+"");
		}
		finally {
			db.endTransaction();
		}

	}



	//untuk asset data
	public void deleteassetdata(){
		SQLiteDatabase db = this.getWritableDatabase();

		String DELETE_tempatlahir_TBLL =  "DELETE FROM asset";

		db.execSQL(DELETE_tempatlahir_TBLL);


	}

	public int getAssetDataCount() {
		int num = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		try{
			String QUERY = "SELECT * FROM asset";
			Cursor cursor = db.rawQuery(QUERY, null);
			num = cursor.getCount();
			Log.d("cek", "getAssetDataCount: "+ num);
			cursor.close();
			db.close();
			return num;
		}catch (Exception e){
			Log.e("error",e+"");
		}
		return 0;
	}

	public void insertAssetData(NodeList json){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		db.beginTransaction();
		try{

			for (int i=0;i<json.getLength();i++) {
				Node fieldNode = json.item(i);
				Log.d("waduh", "insertAssetData: "+json.getLength());
				NamedNodeMap map = fieldNode.getAttributes();

				cv.put("assetcode", map.getNamedItem("assetcode").getNodeValue());
				cv.put("assettype", map.getNamedItem("assettype").getNodeValue());
				cv.put("assetdetail", map.getNamedItem("assetdetail").getNodeValue());

				db.insert("asset","",cv);
				cv.clear();
			}


			Node fieldNode = json.item(0);
			NamedNodeMap map = fieldNode.getAttributes();
			lastinsertasset = map.getNamedItem("lastinsertasset").getNodeValue();

			Cursor cursor = db.rawQuery("SELECT id FROM " + "control", null);
			try {
				if(cursor.moveToFirst())
				{
					cv.put("lastinsertasset", lastinsertasset);
					db.update("control",cv,null,null);
				}
				else{
					cv.put("lastinsertasset", lastinsertasset);
					db.update("control",cv,null,null);

				}
			}
			finally{
				cursor.close();
			}
			db.setTransactionSuccessful();
		}catch (Exception e){
			Log.e("problem",e+"");
		}
		finally {
			db.endTransaction();
		}

	}



	public ArrayList<String> getAllProduct(String baruataulama) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> provinsi = null;
		try{
			provinsi = new ArrayList<String>();
			String QUERY = "SELECT distinct productname FROM product WHERE AssetUsedNew = '"+ baruataulama +"' order by productname";
			//String QUERY = "SELECT distinct productname FROM product order by productname";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{

					provinsi.add(cursor.getString(0));
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		return provinsi;
	}
	public String getProductID(String input) {
		SQLiteDatabase db = this.getReadableDatabase();
		String result="";
		String id="";
		try{
			String QUERY =
					"SELECT  productid"+
							" FROM product"
							+" WHERE productname"
							+" IN ('"+input+"');";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{
					id=cursor.getString(0);
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		result= String.valueOf(id);
		return result;
	}

	public ArrayList<String> getAllproductoffering (String id) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> arrlist = null;
		try{
			arrlist = new ArrayList<String>();
			String QUERY = "SELECT distinct Description FROM productoffering "
					+" WHERE  ProductID = '" + id + "' order by Description asc ";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{

					if(!cursor.getString(0).equals(" ") | !cursor.getString(0).equals(" ") )
					{arrlist.add(cursor.getString(0));}
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		return arrlist;
	}
	public String getProductofferingID(String input) {
		SQLiteDatabase db = this.getReadableDatabase();
		String result="";
		String id="";
		try{
			String QUERY =
					"SELECT  ProductOfferingID"+
							" FROM productoffering"
							+" WHERE Description"
							+" IN ('"+input+"');";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{
					id=cursor.getString(0);
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		result= String.valueOf(id);
		return result;
	}


	public ArrayList<String> getAllEvent() {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> provinsi = null;
		try{
			provinsi = new ArrayList<String>();
			String QUERY = "SELECT distinct eventname FROM event order by eventname";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{

					provinsi.add(cursor.getString(0));
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		return provinsi;
	}
	public String getEventID(String input) {
		SQLiteDatabase db = this.getReadableDatabase();
		String result="";
		String id="";
		try{
			String QUERY =
					"SELECT  eventid"+
							" FROM event"
							+" WHERE eventname"
							+" IN ('"+input+"');";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{
					id=cursor.getString(0);
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		result= String.valueOf(id);
		return result;
	}




	public ArrayList<String> getAllSupplier() {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> provinsi = null;
		try{
			provinsi = new ArrayList<String>();
			String QUERY = "SELECT suppliername FROM supplier order by suppliername ASC";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{

					provinsi.add(cursor.getString(0));
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		return provinsi;
	}
	public String getSupplierID(String input) {
		SQLiteDatabase db = this.getReadableDatabase();
		String result="";
		String id="";
		try{
			String QUERY =
					"SELECT  Foreign_Key"+
							" FROM supplier"
							+" WHERE suppliername"
							+" IN ('"+input+"');";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{
					id=cursor.getString(0);
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		result= String.valueOf(id);
		return result;
	}
	public String getSupplierID2(String input) {
		SQLiteDatabase db = this.getReadableDatabase();
		String result="";
		String id="";
		try{
			String QUERY =
					"SELECT  supplierid"+
							" FROM supplier"
							+" WHERE suppliername"
							+" IN ('"+input+"');";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{
					id=cursor.getString(0);
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		result= String.valueOf(id);
		return result;
	}




	public ArrayList<String> getAllSalesman(String cabang,String id) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sales = "SL";
		ArrayList<String> arrlist = null;
		try{
			arrlist = new ArrayList<String>();
			String QUERY = "SELECT SupplierEmployeeName FROM salesnew "
					+" WHERE  BranchID = '" + cabang +"' and SupplierID = '" + id +"' and SupplierEmployeePosition = '" + sales +"'" +
					" order by SupplierEmployeeName asc ";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{

					if(!cursor.getString(0).equals(" ") | !cursor.getString(0).equals(" ") )
					{arrlist.add(cursor.getString(0));}
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		return arrlist;
	}
	public String getSalesmanID(String input) {
		SQLiteDatabase db = this.getReadableDatabase();
		String result="";
		String id="";
		try{
			String QUERY =
					"SELECT SupplierID"+
							" FROM salesnew"
							+" WHERE SupplierEmployeeName"
							+" IN ('"+input+"');";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{
					id=cursor.getString(0);
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		result= String.valueOf(id);
		return result;
	}



	public ArrayList<String> getAllSalesSPV(String cabang,String id) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sales = "SV";
		ArrayList<String> arrlist = null;
		try{
			arrlist = new ArrayList<String>();
			String QUERY = "SELECT SupplierEmployeeName FROM salesnew "
					+" WHERE  BranchID = '" + cabang +"' and SupplierID = '" + id +"' and SupplierEmployeePosition = '" + sales +"'" +
					"  order by SupplierEmployeeName asc ";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{

					if(!cursor.getString(0).equals(" ") | !cursor.getString(0).equals(" ") )
					{arrlist.add(cursor.getString(0));}
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		return arrlist;
	}
	public String getSalesSPVID(String input) {
		SQLiteDatabase db = this.getReadableDatabase();
		String result="";
		String id="";
		try{
			String QUERY =
					"SELECT SupplierID"+
							" FROM salesnew"
							+" WHERE SupplierEmployeeName"
							+" IN ('"+input+"');";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{
					id=cursor.getString(0);
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		result= String.valueOf(id);
		return result;
	}

	public ArrayList<String> getAllSalesKoordinasi(String cabang,String id) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sales = "SC";
		ArrayList<String> arrlist = null;
		try{
			arrlist = new ArrayList<String>();
			String QUERY = "SELECT SupplierEmployeeName FROM salesnew "
					+" WHERE  BranchID = '" + cabang +"' and SupplierID = '" + id +"' and SupplierEmployeePosition = '" + sales +"' " +
					" order by SupplierEmployeeName asc ";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{

					if(!cursor.getString(0).equals(" ") | !cursor.getString(0).equals(" ") )
					{arrlist.add(cursor.getString(0));}
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		return arrlist;
	}
	public String getSalesKoordinasiID(String input) {
		SQLiteDatabase db = this.getReadableDatabase();
		String result="";
		String id="";
		try{
			String QUERY =
					"SELECT SupplierID"+
							" FROM salesnew"
							+" WHERE SupplierEmployeeName"
							+" IN ('"+input+"');";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{
					id=cursor.getString(0);
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		result= String.valueOf(id);
		return result;
	}

	public ArrayList<String> getAllSalesAdmin(String cabang,String id) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sales = "AM";
		ArrayList<String> arrlist = null;
		try{
			arrlist = new ArrayList<String>();
			String QUERY = "SELECT SupplierEmployeeName FROM salesnew "
					+" WHERE  BranchID = '" + cabang +"' and SupplierID = '" + id +"' and SupplierEmployeePosition = '" + sales +"' " +
					" order by SupplierEmployeeName asc ";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{

					if(!cursor.getString(0).equals(" ") | !cursor.getString(0).equals(" ") )
					{arrlist.add(cursor.getString(0));}
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		return arrlist;
	}
	public String getSalesAdminID(String input) {
		SQLiteDatabase db = this.getReadableDatabase();
		String result="";
		String id="";
		try{
			String QUERY =
					"SELECT SupplierID"+
							" FROM salesnew"
							+" WHERE SupplierEmployeeName"
							+" IN ('"+input+"');";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{
					id=cursor.getString(0);
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		result= String.valueOf(id);
		return result;
	}




	public ArrayList<String> getAllKota() {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> provinsi = null;
		try{
			provinsi = new ArrayList<String>();
			String QUERY = "SELECT DISTINCT KOTA  FROM zipcode order by KOTA asc";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{

					provinsi.add(cursor.getString(0));
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		return provinsi;
	}
	public ArrayList<String> getAllKecamatan(String kecamatan) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> arrlist = null;
		try{
			arrlist = new ArrayList<String>();
			String QUERY = "SELECT DISTINCT KECAMATAN FROM zipcode "
					+" WHERE KOTA = '"+kecamatan+"' order by KECAMATAN asc";
			//String QUERY = "SELECT DISTINCT KECAMATAN FROM zipcode order by KECAMATAN asc";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{

					if(!cursor.getString(0).equals("") | !cursor.getString(0).equals("") )
					{
						arrlist.add(cursor.getString(0));
						Log.d("cekdb", "getAllKecamatan: "+arrlist);
					}
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		return arrlist;
	}
	public ArrayList<String> getAllKelurahan(String kecamatan,String kota) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> arrlist = null;
		try{
			arrlist = new ArrayList<String>();
			String QUERY = "SELECT DISTINCT KELURAHAN FROM zipcode"
					+" WHERE KOTA = '"+kota+"' and KECAMATAN = '"+kecamatan+"' order by kelurahan asc";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{

					if(!cursor.getString(0).trim().equals("") )
					{arrlist.add(cursor.getString(0));}
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		return arrlist;
	}
	public ArrayList<String> getAllZipCode(String kota,String kecamatan, String kelurahan) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> arrlist = null;
		try{
			arrlist = new ArrayList<String>();
			String QUERY = "SELECT DISTINCT Description FROM zipcode"
					+" WHERE KOTA  = '"+kota+"' and KECAMATAN = '"+kecamatan+"' and KELURAHAN = '"+kelurahan+"' ";
			Log.d("cek", "getAllZipCode:  QUERY "+ QUERY);
			Cursor cursor = db.rawQuery(QUERY, null);
			Log.d("cek", "getAllZipCode: cursor "+ cursor);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{

					if(!cursor.getString(0).equals(" ") | !cursor.getString(0).equals(" ") )
					{arrlist.add(cursor.getString(0));}
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		return arrlist;
	}


	public ArrayList<String> getAllZipcode2() {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> provinsi = null;
		try{
			provinsi = new ArrayList<String>();
			String QUERY = "SELECT DISTINCT Description FROM zipcode order by Description asc";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{

					provinsi.add(cursor.getString(0));
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		return provinsi;
	}
	public ArrayList<String> getAllKota2(String Description) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> arrlist = null;
		try{
			arrlist = new ArrayList<String>();
			String QUERY = "SELECT DISTINCT KOTA FROM zipcode "
					+" WHERE Description = '"+Description+"' order by KOTA asc";
			//String QUERY = "SELECT DISTINCT KECAMATAN FROM zipcode order by KECAMATAN asc";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{

					if(!cursor.getString(0).equals("") | !cursor.getString(0).equals("") )
					{
						arrlist.add(cursor.getString(0));
						Log.d("cekdb", "getAllKecamatan: "+arrlist);
					}
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		return arrlist;
	}
	public ArrayList<String> getAllKecamatan2(String Description,String kota) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> arrlist = null;
		try{
			arrlist = new ArrayList<String>();
			String QUERY = "SELECT DISTINCT KECAMATAN FROM zipcode"
					+" WHERE Description = '"+Description+"' and KOTA = '"+kota+"' order by KECAMATAN asc";
			Cursor cursor = db.rawQuery(QUERY, null);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{

					if(!cursor.getString(0).trim().equals("") )
					{arrlist.add(cursor.getString(0));}
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		return arrlist;
	}
	public ArrayList<String> getAllKelurahan2(String Description,String kota, String kecamatan) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> arrlist = null;
		try{
			arrlist = new ArrayList<String>();
			String QUERY = "SELECT DISTINCT KELURAHAN FROM zipcode"
					+" WHERE Description  = '"+Description+"' and KOTA = '"+kota+"' and KECAMATAN = '"+kecamatan+"' " +
					"order by KELURAHAN asc";
			Log.d("cek", "getAllZipCode:  QUERY "+ QUERY);
			Cursor cursor = db.rawQuery(QUERY, null);
			Log.d("cek", "getAllZipCode: cursor "+ cursor);
			if(!cursor.isLast())
			{
				while (cursor.moveToNext())
				{

					if(!cursor.getString(0).equals(" ") | !cursor.getString(0).equals(" ") )
					{arrlist.add(cursor.getString(0));}
				}
			}
			cursor.close();
			db.close();
		}catch (Exception e){
			Log.e("error",e+"");
		}
		return arrlist;
	}








}


