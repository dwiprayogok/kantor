package mandiri.finance.faith;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import mandiri.finance.faith.Interface.GPSTracker;

public abstract class DashBoardActivity2 extends Activity {
	private Uri imageUri;
    private static int Action_Code = 0;
    private int flag; 
    private DataSource datasource;
    private SQLiteDatabase db;
    private String tableName;
    
    private boolean deleteFlag1 = false; 
    private boolean deleteFlag2 = false; 
    private boolean deleteFlag3  = false;
    private boolean deleteFlag4  = false;
    
    private boolean deleteFlag5 = false; 
    private boolean deleteFlag6 = false; 
    private boolean deleteFlag7  = false;
    private boolean deleteFlag8  = false;
    
    private int buttonId1;
    private int buttonId2;
    private int buttonId3;
    private int buttonId4;
    
    private int buttonId5;
    private int buttonId6;
    private int buttonId7;
    private int buttonId8;
    
    private String image_str = "";
    private Intent datax;
    private Context mContext;
    private ProgressDialog dialog;
    private SharedPreferences generalPrefs;
    
    //private LocationManager locationManager;
    boolean gps_enabled=false;
    boolean network_enabled=false;
    
	//private String latString = "";
    //private String lngString = "";
    private Exception e = null;
    Bitmap bitmap = null;
	String TAG = "DashBoardActivity";
	GPSTracker gps;
	String alamat_customer = "";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        mContext = DashBoardActivity2.this;
        tableName = "image";
        generalPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		gps = new GPSTracker(mContext);
        datasource = new DataSource(mContext);
        db = datasource.getWritableDatabase();
		alamat_customer = "Taman Senopati";
        if (savedInstanceState != null)
        {
        	imageUri = savedInstanceState.getParcelable("imageCaptureUri");
        }

    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putParcelable("imageCaptureUri", imageUri);
    }

    
    public void setHeader(String title, boolean btnHomeVisible)
    {
    		ViewStub stub = (ViewStub) findViewById(R.id.vsHeader);
    		View inflated = stub.inflate();
          
    		TextView txtTitle = (TextView) inflated.findViewById(R.id.txtHeading);
    		txtTitle.setText(title);
    		
    		Button btnHome = (Button) inflated.findViewById(R.id.btnHome);
    		if(!btnHomeVisible)
    			btnHome.setVisibility(View.INVISIBLE);
    }    
    
   public void btnHomeClick(View v)
    {	   
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
   
   public void btnLogoutClick(View v)
   {
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
   
   public void onButtonClick(View v)
   {
	   int viewID;    	        
	   viewID = v.getId();
	   	
	   	if(viewID == R.id.button1){
	   		buttonId1 = R.id.button1;
			Action_Code = R.id.button1;
			buildintent();
	   	}else if(viewID == R.id.button2){
	   		buttonId2 = R.id.button2;
			Action_Code = R.id.button2;
			buildintent();		
	   	}else if(viewID == R.id.button3){
	   		buttonId3 = R.id.button3;
			Action_Code = R.id.button3;
			buildintent();  
	   	}else if(viewID == R.id.button4){
	   		buttonId4 = R.id.button4;
			Action_Code = R.id.button4;
			buildintent();  
	   	}else if(viewID == R.id.button5){
	   		buttonId5 = R.id.button5;
			Action_Code = R.id.button5;
			buildintent();  
	   	}else if(viewID == R.id.button6){
	   		buttonId6 = R.id.button6;
			Action_Code = R.id.button6;
			buildintent();  
	   	}else if(viewID == R.id.button7){
	   		buttonId7 = R.id.button7;
			Action_Code = R.id.button7;
			buildintent();  
	   	}else if(viewID == R.id.button8){
	   		buttonId8 = R.id.button8;
			Action_Code = R.id.button8;
			buildintent();  
	   	}
   	
   }
   
   private void buildintent(){
	   
	   	List<String> listItems = new ArrayList<String>();
	    
	   	listItems.add("Camera");
  		//listItems.add("Gallery");
  		
	   	if((Action_Code == buttonId1) && deleteFlag1 == true){
	   		listItems.add("Remove");
	   	}else if((Action_Code == buttonId2) && deleteFlag2 == true){
	   		listItems.add("Remove");
	   	}else if((Action_Code == buttonId3) && deleteFlag3 == true){
	   		listItems.add("Remove");
	   	}else if((Action_Code == buttonId4) && deleteFlag4 == true){
	   		listItems.add("Remove");
	   	}else if((Action_Code == buttonId5) && deleteFlag5 == true){
	   		listItems.add("Remove");
	   	}else if((Action_Code == buttonId6) && deleteFlag6 == true){
	   		listItems.add("Remove");
	   	}else if((Action_Code == buttonId7) && deleteFlag7 == true){
	   		listItems.add("Remove");
	   	}else if((Action_Code == buttonId8) && deleteFlag8 == true){
	   		listItems.add("Remove");
	   	}
	   	
	   	final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);
	   	    
	    AlertDialog.Builder builder =
		new AlertDialog.Builder(mContext);
		AlertDialog alertDialog;
		builder.setTitle("Select Action");
					
		builder.setItems(items, new DialogInterface.OnClickListener() {  

			public void onClick(DialogInterface dialog, int item) {
				
				if (items[item] == "Camera") {
					//handlelocation();
					
					flag = 0;
					startCamera();
					dialog.dismiss();
				}
				else if (items[item] == "Gallery") {
					flag = 1;
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					
					startActivityForResult(intent, Action_Code);
					//startActivityForResult(Intent.createChooser(intent,
					//	"Select Picture"), Action_Code);
					dialog.dismiss();		
				}
				else{
					if (Action_Code == buttonId1){
			    		   deleteFlag1 = false;
			    	   }
					   else if (Action_Code == buttonId2){
			    		   deleteFlag2 = false;
			    	   }
			    	   else if (Action_Code == buttonId3){
			    		   deleteFlag3 = false;
			    	   }
			    	   else if (Action_Code == buttonId4){
			    		   deleteFlag4 = false;
			    	   }
			    	   else if (Action_Code == buttonId5){
			    		   deleteFlag5 = false;
			    	   }
			    	   else if (Action_Code == buttonId6){
			    		   deleteFlag6 = false;
			    	   }
			    	   else if (Action_Code == buttonId7){
			    		   deleteFlag7 = false;
			    	   }
			    	   else if (Action_Code == buttonId8){
			    		   deleteFlag8 = false;
			    	   }
					
					ImageButton button = (ImageButton) findViewById(Action_Code);
					button.setImageBitmap(null);
					button.setImageResource(R.drawable.gallery);
					
					datasource.deleteData(db, " buttonId = '" + Action_Code + "'", tableName);
					
					dialog.dismiss();
					
					Toast.makeText(mContext, "Picture has been removed", Toast.LENGTH_SHORT).show();
				}
				
			}
		});

		alertDialog = builder.create();
		alertDialog.show();
   }
   
   private void startCamera(){
	   
	   if(dialog != null && dialog.isShowing())
		   dialog.dismiss();
	   
	   String fileName = "photo.jpg";
       ContentValues values = new ContentValues();
       
       values.put(MediaStore.Images.Media.TITLE, fileName);
       values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
       
       imageUri = getContentResolver().insert(
		   MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
       
       Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                     
       startActivityForResult(intent, Action_Code);       
       
   }

   public void onActivityResult(int requestCode, int resultCode, final Intent data) {
               
       if (requestCode == Action_Code) {
    	       	   
    	   if (resultCode == RESULT_OK){
    		   datax = data;
    		       		   
    		   if(dialog != null && dialog.isShowing())
    			   dialog.dismiss();
    		   
    		   new PictureProcessing().execute();    		   

    	   }
    	   else if (resultCode == RESULT_CANCELED) {
    		   
    		   if (flag ==0){
    			   
    			   File imageFile = new File(getPath(imageUri));    			   
    			   
        		   if(imageFile.exists()) {
        			   if (Build.VERSION.SDK_INT >= 4.4) {
        				   Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                           Uri contentUri = Uri.fromFile(imageFile);
                 		   mediaScanIntent.setData(contentUri);
                           this.sendBroadcast(mediaScanIntent);
                           try {
                 			   getContentResolver() .delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                 					  MediaStore.Images.Media.DATA
                 		              + "='"
                 		              + imageFile.getPath()
                 		              + "'", null);
                 		   } catch (Exception e) {
                 			   e.printStackTrace();
                 		   }
                           
        			   }else{
        				   sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        				   try {
                 			   getContentResolver() .delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                 					  MediaStore.Images.Media.DATA
                 		              + "='"
                 		              + imageFile.getPath()
                 		              + "'", null);
                 		   } catch (Exception e) {
                 			   e.printStackTrace();
                 		   }
        			   }        			   
        		    }  		   
    		   }
    		   
	           Toast.makeText(mContext, "Picture not attached", Toast.LENGTH_SHORT).show();
	       } 
	       else {
				if (flag ==0){
				   
				   File imageFile = new File(getPath(imageUri));
				   
				   if(imageFile.exists()) {
					   if (Build.VERSION.SDK_INT >= 4.4) {
        				   Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                           Uri contentUri = Uri.fromFile(imageFile);
                           mediaScanIntent.setData(contentUri);
                           this.sendBroadcast(mediaScanIntent);
                           
                           try {
                 			   getContentResolver() .delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                 					  MediaStore.Images.Media.DATA
                 		              + "='"
                 		              + imageFile.getPath()
                 		              + "'", null);
                 		   } catch (Exception e) {
                 			   e.printStackTrace();
                 		   }                           
        			   }else{
        				   sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        				   
        				   try {
                 			   getContentResolver() .delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                 					  MediaStore.Images.Media.DATA
                 		              + "='"
                 		              + imageFile.getPath()
                 		              + "'", null);
                 		   } catch (Exception e) {
                 			   e.printStackTrace();
                 		   }
        			   }					   
				    }
				}
				
	           Toast.makeText(mContext, "Picture not attached", Toast.LENGTH_SHORT).show();
	       }
       }
   }

	private class PictureProcessing extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {

			if(dialog != null && dialog.isShowing())
				dialog.dismiss();

			dialog = ProgressDialog.show(mContext, "", "Attach Picture...", true);
		}

		@Override
		protected String doInBackground(Void... params) {
			String result = "";

			try{

				if(flag == 0 && imageUri != null)
					PictureAttach();
				else if(flag != 0)
					PictureAttach();
				else
					result = "imageUri = null";

	    		   	/*
	    		   	if(flag == 0 && imageUri != null){
					   File imageFile = new File(getPath(imageUri));
						   getContentResolver() .delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					          MediaStore.Images.Media.DATA
					              + "='"
					              + imageFile.getPath()
					              + "'", null);
					}else{
						result = "imageUri = null";
					}
		          	*/

			}catch (Exception e) {
				System.out.println("PictureProcessing, e.getMessage() : " + e.getMessage());
				DashBoardActivity2.this.e = e;
				result = e.getMessage();
			}

			return result;
		}

		@Override
		protected void onPostExecute(String sResponse) {
			if(dialog != null && dialog.isShowing())
				dialog.dismiss();

			if (e==null && sResponse.length()==0 && sResponse!=null){
				ImageButton button = (ImageButton) findViewById(Action_Code);

				if(button!=null)
					button.setImageBitmap(null);

				if(bitmap!=null && button!=null)
					button.setImageBitmap(bitmap);

				bitmap=null;
				System.gc();
			}else
				Toast.makeText(mContext, sResponse, Toast.LENGTH_SHORT).show();
		}
	}
   
   private void PictureAttach(){
	   
	   	try{
	   		
	   		String picturePath = "";
			int exifOrientation;
			final int reqWidth = 320;
			final int reqHeight = 240;
			int scale = 2;
			ByteArrayOutputStream bos = null;
			image_str = "";
			
			//Bitmap bitmap = null;
					   
			if (flag ==0){
				picturePath = getPath(imageUri);
				exifOrientation = getCameraPhotoOrientation(mContext,imageUri,picturePath);	         			   
			}
			else{
				picturePath = getPath(datax.getData());
				exifOrientation = getCameraPhotoOrientation(mContext,datax.getData(),picturePath);	         			   
		   	}
			
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(picturePath, o);
					     		   
			if (o.outHeight > reqHeight || o.outWidth > reqWidth) {
		     	if (o.outWidth > o.outHeight) {
		       		scale = Math.round((float)o.outHeight / (float)reqHeight);
		     	} else {
		       		scale = Math.round((float)o.outWidth / (float)reqWidth);
		     	}
		 	}
			
			System.gc();
			
			Matrix matrix = new Matrix();
	    	matrix.postRotate(exifOrientation);
	    	
	    	o.inJustDecodeBounds = false;
	    	o.inSampleSize = scale;	           
	    	BitmapFactory.decodeFile(picturePath, o);
	    	
	    	bitmap=null;
	    	bitmap = Bitmap.createBitmap(BitmapFactory.decodeFile(picturePath, o) , 0, 0, 
	    		o.outWidth, o.outHeight, matrix, true);
	    	bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
	    	
	    	//ImageButton button = (ImageButton) findViewById(Action_Code);
	    	//button.setImageBitmap(bitmap);
	    	//bitmap = null;
	    	
			System.gc();
		   
			Bitmap bitmap2 = null;
			
			bitmap2 = Bitmap.createBitmap(BitmapFactory.decodeFile(picturePath,o) , 0, 0, 
					o.outWidth, o.outHeight, matrix, true);

			bos = new ByteArrayOutputStream();
			bitmap2.compress(CompressFormat.JPEG, 100, bos);
			
			bitmap2.recycle();
			bitmap2 = null;
	    	matrix.reset();
	    	matrix = null;
	    	
			System.gc();
		   
	   		byte[] byt = bos.toByteArray();
	   		image_str = Base64.encodeToString(byt,Base64.DEFAULT);
	   		
	   		bos.flush();
    		bos.close(); 
    		byt = null; 			   
    		bos = null;
    		System.gc();
    		
    		if(bitmap!=null)
    	   		System.out.println("bitmap!=null");
    	   	else
    	   		System.out.println("bitmap==null");
    		
	   	   	Cursor cursor = db.rawQuery("SELECT id FROM " + tableName + " WHERE buttonId = '" + Integer.toString(Action_Code) +"'", null);
	   		
    		if(cursor.moveToFirst())
	    	{		    	  
    			ContentValues cv = new ContentValues();
	    		cv.put("imageStr", image_str);
	    		datasource.updateData(db,cv,tableName," buttonId = '" + Action_Code + "'");	
	    		cv.clear();
	    	}else{
	    		ContentValues cv = new ContentValues();
	    		
	    		if (Action_Code == buttonId1){
	    			deleteFlag1 = true;
	    		}else if (Action_Code == buttonId2){
	    			deleteFlag2 = true;
	    		}else if (Action_Code == buttonId3){
	    			deleteFlag3 = true;
	    		}else if (Action_Code == buttonId4){
	    			deleteFlag4 = true;
	    		}else if (Action_Code == buttonId5){
	    			deleteFlag5 = true;
	    		}else if (Action_Code == buttonId6){
	    			deleteFlag6 = true;
	    		}else if (Action_Code == buttonId7){
	    			deleteFlag7 = true;
	    		}else if (Action_Code == buttonId8){
	    			deleteFlag8 = true;
	    		}
    	   
	    	cv.put("imageStr", image_str);
	    	cv.put("buttonId", Action_Code);
	    	cv.put("lat", "");
	    	cv.put("lng", "");
	    		       
    		datasource.generateData(db,cv,tableName);
    		
    		cv.clear();
    		cursor.close();    		
    		
	    	}
    	}
    	catch (Exception e) {
    		Log.d("Picture Attach", "Failed to do something: " + e.getMessage());
    		throw new RuntimeException(e);
 	   	}
    	finally{
    		System.gc();
    	}
   }


	private void timestampItAndSave2(String toEdit)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;

		Log.d(TAG, "timestampItAndSave2: "+toEdit);
		Bitmap bitmap = BitmapFactory.decodeFile(toEdit);
		//        Bitmap src = BitmapFactory.decodeResource(); // the original file is cuty.jpg i added in resources
		Bitmap dest = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateTime = sdf.format(Calendar.getInstance().getTime()); // reading local time in the system

		Canvas cs = new Canvas(dest);
		Paint tPaint = new Paint();
		tPaint.setTextSize(20);
		tPaint.setColor(Color.YELLOW);
		tPaint.setStyle(Paint.Style.FILL);
		cs.drawBitmap(bitmap, 0f, 0f, null);


		float height = tPaint.measureText("yY");
		float xx= cs.getWidth();
		float yy= cs.getHeight();

		Double lat2 = gps.getLatitude();
		Double lon2 = gps.getLongitude();

		Log.d(TAG, "timestampItAndSave2: "+"latitudenya : " + lat2 +" longitudenya adalah : "+lon2);

		String latnya = lat2.toString();
		String lonnya = lon2.toString();

		String latlongnya = latnya + lonnya;
		Log.d(TAG, "timestampItAndSave2: latlongnya adalah "+latlongnya);


		float xdt=20;
		float ydt=yy-20;

		float xlat=(xx/2)+50;
		float ylat=ydt;

		float xsiteid=xdt=20;
		float ysiteid=yy-60;

		if(bitmap.getWidth()<bitmap.getHeight()){

			xdt=20;
			ydt=yy-40;

			xsiteid=xdt=20;
			ysiteid=yy-80;

			xlat=20;
			ylat=ydt+20;
		}

		Log.d(TAG, "timestampItAndSave2: time "+xdt+" "+xdt+" lat "+xlat+" "+ylat);

		cs.drawText(alamat_customer,xsiteid,ysiteid, tPaint);
		Log.d(TAG, "timestampItAndSave2: "+alamat_customer);
		cs.drawText(dateTime,xdt,ydt, tPaint);
		cs.drawText(trimlatlong(latnya+ "," +lonnya), xlat,ylat, tPaint);
		try {
			dest.compress(Bitmap.CompressFormat.JPEG, 85, new FileOutputStream(new File(toEdit)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String trimlatlong(String latlong){
		String[] toparse = latlong.split(",") ;
		Log.d(TAG, "trimlatlong: to parse "+toparse);
		String lat = toparse[0];
		String lon = toparse[1];
		Log.d(TAG, "trimlatlong: hasil parse"+lat + lon);
		lat = trimGpsString(lat);
		lon = trimGpsString(lon);

		return lat+","+lon;
	}
	public static String trimGpsString(String gostring) {
		String[] golist= gostring.split("\\.");
		Log.d("Task ac", "trimGpsString: "+gostring+" "+golist[0].toString()+" | "+golist.length);
		String s=golist[1];



		int length = s.length();

		String xresult;
		//Check whether or not the string contains at least four characters; if not, this method is useless

		if(length>5){
			xresult =s.substring(0, length - (length-5)) + "";
			xresult=golist[0]+"."+xresult;
			Log.d("", "trimGpsString: "+xresult);
			return xresult;
		}
		return gostring;

	}


   public String getPath(Uri uri) {
	   	String result;
	    String[] projection = { MediaStore.Images.Media.DATA };
	    
	    Cursor cu = null;
	    
	    cu = getContentResolver().query(uri, projection, null, null, null);
	    
	    int column_index = cu.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    
	    cu = getContentResolver().query(uri, projection, null, null, null);
	    	    
	    try {
	    	cu.moveToFirst();
		    result = cu.getString(column_index);
	    } finally {
	    	cu.close();
	    }    	
	    
	    System.gc(); 
	    return result;
	}
   
   public static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath){
	     int rotate = 0;
	     try {
	         context.getContentResolver().notifyChange(imageUri, null);
	         File imageFile = new File(imagePath);
	         ExifInterface exif = new ExifInterface(
                 imageFile.getAbsolutePath());
	         int orientation = exif.getAttributeInt(
                 ExifInterface.TAG_ORIENTATION,
                 ExifInterface.ORIENTATION_NORMAL);
	         	         
	         switch (orientation) {
	         case ExifInterface.ORIENTATION_ROTATE_270:
	             rotate = 270;
	             break;
	         case ExifInterface.ORIENTATION_ROTATE_180:
	             rotate = 180;
	             break;
	         case ExifInterface.ORIENTATION_ROTATE_90:
	             rotate = 90;
	             break;
	         }
	         
	         imageFile = null;
	         System.gc();
	     } catch (Exception e) {
	    	 System.out.println("getCameraPhotoOrientation Error");
	         e.printStackTrace();
	     }
   		
	    return rotate;
   }
   
   @Override
   public void onPause() {
	super.onPause();
	
		if(dialog != null && dialog.isShowing())
			   dialog.dismiss();
   }
   
   
   @Override
   public void onDestroy() {
	   super.onDestroy();
	   
	   System.out.println("onDestroy DA2");
	   
	   if (db != null){
		   if (db.isOpen()){
			   datasource.deleteData(db, "", tableName);
			   db.close();
			   db = null;
       		}
	   }
		   
	   if(datasource != null){
		   	datasource.close();
		   	datasource = null;
	   }
	   
	   if (dialog != null){
			if(dialog.isShowing()){
				dialog.dismiss();
			}
		}
	   
   }
   
   @Override
   public void onConfigurationChanged(Configuration newConfig) {
       super.onConfigurationChanged(newConfig);
       
       System.out.println("onConfigurationChanged");
   }
   
 }