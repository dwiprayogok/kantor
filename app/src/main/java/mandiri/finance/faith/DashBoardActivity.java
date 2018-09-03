package mandiri.finance.faith;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import mandiri.finance.faith.Interface.GPSTracker;
import mandiri.finance.faith.Model.Model_Customer;
import mandiri.finance.faith.Model.Supplier;

public abstract class DashBoardActivity extends Activity {
	private Uri imageUri;
    private static int Action_Code = 0;
	private static int Action_Code2 = 0;
    private int flag; 
    private DataSource datasource;
    private SQLiteDatabase db;
    private String tableName;
	private String tableImageColl;

    private boolean deleteFlag1 = false; 
    private boolean deleteFlag2 = false; 
    private boolean deleteFlag3  = false;
    private boolean deleteFlag4  = false;
	private boolean deleteFlag5 = false;
	private boolean deleteFlag77 = false;
    
    private int buttonId1;
    private int buttonId2;
    private int buttonId3;
    private int buttonId4;
	private int buttonId5;
	private int buttonId77;

    
    private String image_str = "", latinya2 = "", longinya2 = "";
	private String image_str_coll = "";
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
	String TAG = "DashBoardActivity";
	GPSTracker gps;
	String alamat_customer = "";
	private String custAddress;
	Double lati ;
	Double longi;
	Model_Customer model_customer = new Model_Customer();
	ImageButton imageButton,imageButton77;


	int position=0;
	private String branchID;
	View v;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        mContext = DashBoardActivity.this;
        tableName = "image";
		tableImageColl = "imagecollection";
        generalPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        gps = new GPSTracker(mContext);
        datasource = new DataSource(mContext);
        db = datasource.getWritableDatabase();
		branchID = generalPrefs.getString("branchID", null);
		imageButton = (ImageButton) findViewById(R.id.button5);
		imageButton77 = (ImageButton) findViewById(R.id.button77);






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
   
   private void buildintent(){
	   
	   	List<String> listItems = new ArrayList<String>();
	    
	   	//listItems.add("Camera");
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
		}
		else if((Action_Code == buttonId77) && deleteFlag77 == true){
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
					//startCamera2();
					dialog.dismiss();
				}
				else if (items[item] == "Gallery") {
					flag = 1;
					
					if (Build.VERSION.SDK_INT > 19) {
						Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(i, Action_Code);
					}else{
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						
						startActivityForResult(intent, Action_Code);
					}
					
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
			    	   } else if (Action_Code == buttonId5){
						deleteFlag5 = false;
					   }
					else if (Action_Code == buttonId77){
						deleteFlag77 = false;

					}

					
					ImageButton button = (ImageButton) findViewById(Action_Code);


					button.setImageBitmap(null);
					button.setImageResource(R.drawable.gallery);




					datasource.deleteData(db, " buttonId = '" + Action_Code + "'", tableName);
					hapusfoto();
					//datasource.deleteData(db, " buttonId = '" + Action_Code + "'", tableImageColl);
					
					dialog.dismiss();
					
					Toast.makeText(mContext, "Picture has been removed", Toast.LENGTH_SHORT).show();
				}

			}
		});

		alertDialog = builder.create();
		alertDialog.show();
   }
   
   View.OnLongClickListener listener = new View.OnLongClickListener() {
       public boolean onLongClick(View v) {
       	
       	int viewID;    	        
    	   	viewID = v.getId();
    	   	
    	   	if(viewID == R.id.button1){
    	   		buttonId1 = R.id.button1;
    			Action_Code = R.id.button1;
    			
    			if(deleteFlag1 == true)
    				buildintent();    			
    	   	}else if(viewID == R.id.button2){
    	   		buttonId2 = R.id.button2;
    			Action_Code = R.id.button2;
    			
    			if(deleteFlag2 == true)
    				buildintent();    	
    	   	}else if(viewID == R.id.button3){
    	   		buttonId3 = R.id.button3;
    			Action_Code = R.id.button3;
    			
    			if(deleteFlag3 == true)
    				buildintent();    	
    	   	}else if(viewID == R.id.button4){
    	   		buttonId4 = R.id.button4;
    			Action_Code = R.id.button4;
    			
    			if(deleteFlag4 == true)
    				buildintent();    	
    	   	}

       	
           return true;
       }
   };



   
   public void onButtonClick(View v) {
	   int viewID;
	   viewID = v.getId();

	   	if(viewID == R.id.button1){
	   		buttonId1 = R.id.button1;
			Action_Code = R.id.button1;
	   	}else if(viewID == R.id.button2){
	   		buttonId2 = R.id.button2;
			Action_Code = R.id.button2;
	   	}else if(viewID == R.id.button3){
	   		buttonId3 = R.id.button3;
			Action_Code = R.id.button3;
	   	}else if(viewID == R.id.button4){
	   		buttonId4 = R.id.button4;
			Action_Code = R.id.button4;
	   	}

	   	//ImageButton button = (ImageButton) v.findViewById(Action_Code);
	   ImageView button = (ImageView) v.findViewById(Action_Code);

	   	Cursor c = db.rawQuery("SELECT imageStr FROM "
				+ tableName + " WHERE buttonId = '" + Integer.toString(Action_Code) +"'", null);

        if(c.moveToFirst()){

        	try{
 				if(button!=null){

 					button.setImageBitmap(null);
 					byte[] imageAsBytes = Base64.decode(c.getString(0).getBytes(), Base64.DEFAULT);

 					Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
 					//bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
					bitmap = Bitmap.createScaledBitmap(bitmap, 350, 350, false);

 					button.setImageBitmap(bitmap);

 					if (Action_Code == buttonId1){
 		    			deleteFlag1 = true;
 		    		}else if (Action_Code == buttonId2){
 		    			deleteFlag2 = true;
 		    		}else if (Action_Code == buttonId3){
 		    			deleteFlag3 = true;
 		    		}else if (Action_Code == buttonId4){
 		    			deleteFlag4 = true;
 		    		}

 					button.setOnLongClickListener(listener);

 					bitmap=null;
 					System.gc();
 				}

	 		}
	 		finally{
	 			c.close();
	 		}
        }else{
        	startCamera();
        }


   }
   private void startCamera(){
	   if(dialog != null && dialog.isShowing())
		   dialog.dismiss();
	   
	   String fileName = "photoProspect.jpg";
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
				new PictureProcessing2().execute();
    		   /*
    		   new Thread(new Runnable() {
	   	            public void run() {
	   	            	try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							runOnUiThread(new Runnable() {
	                            @Override public void run() {

	                           	 if(flag == 0 && imageUri != null){
	                           		 PictureAttach();

	                           		 if(dialog.isShowing())
	                          		 		dialog.dismiss();
	                               	 Toast.makeText(mContext, "Picture has been attached", Toast.LENGTH_SHORT).show();
	                           	 }else{
	                           		 if(dialog.isShowing())
	                           			 dialog.dismiss();
	                           	 }

	                           	 if(flag == 0 && imageUri != null){
	                             	   File imageFile = new File(getPath(imageUri));
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
	                   });

	   	            }
	   	        }).start();
	   	        */
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
							sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
									Uri.parse("file://" + Environment.getExternalStorageDirectory())));

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
	    		   
	    		   if(flag == 0 && imageUri != null) {
					   //loadcustomer();
					   PictureAttach();
				   }
					else if(flag != 0) {
					   //loadcustomer();
					   PictureAttach();

				   }
				   else
					   result = "imageUri = null";


	    		   	SystemClock.sleep(2000);
	    		   
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
	    		   DashBoardActivity.this.e = e;
	    		   result = e.getMessage();
	    	   }
    	   
	    	   return result;	   
	       }
	       
	       @Override
	       protected void onPostExecute(String sResponse) {
	    	   if(dialog != null && dialog.isShowing())
    			   dialog.dismiss();
	    	   
	    	   if (e==null && sResponse.length()==0){
	    		   //ImageButton button = (ImageButton) findViewById(Action_Code);
				   ImageView button = (ImageView) findViewById(Action_Code);


	    		   	if(button!=null)
						button.setImageBitmap(null);

	    		   	File cacheDir = getBaseContext().getCacheDir();
	    		    File f = new File(cacheDir, "picProspect");     
	    		    FileInputStream fis = null;
	    		    
	    		    try {
	    		        fis = new FileInputStream(f);
	    		    } catch (FileNotFoundException e) {	    		        
	    		        e.printStackTrace();
	    		    }
	    		    Bitmap bitmap = BitmapFactory.decodeStream(fis);
	    		    //bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
				   bitmap = Bitmap.createScaledBitmap(bitmap, 350, 350, false);
	    		    
	    		    if(bitmap!=null && button!=null) {
						button.setImageBitmap(bitmap);
					}
	    		   	
	    		    try {
						fis.close();
						cacheDir.delete();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	    		    
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
	    		    	
	    	//ImageButton button = (ImageButton) findViewById(Action_Code);
	    	//button.setImageBitmap(bitmap);
	    	//bitmap = null;
	    	
			System.gc();
		   
			Bitmap bitmap2 = null;
			
			bitmap2 = Bitmap.createBitmap(BitmapFactory.decodeFile(picturePath,o) , 0, 0, 
					o.outWidth, o.outHeight, matrix, true);

			bos = new ByteArrayOutputStream();
			//bitmap2.compress(CompressFormat.JPEG, 80, bos);
			bitmap2.compress(CompressFormat.JPEG, 100, bos);
			
			File cacheDir = getBaseContext().getCacheDir();
			File f = new File(cacheDir, "picProspect");

			try {
			    FileOutputStream out = new FileOutputStream(f);
			    
			    bitmap2.compress(Bitmap.CompressFormat.JPEG, 80, out);
			    
			    out.flush();
			    out.close();

				//timestampItAndSave2(f.getAbsolutePath());

			} catch (FileNotFoundException e) {
			    e.printStackTrace();
			} catch (IOException e) {
			    e.printStackTrace();
			}
			
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
   		throw new RuntimeException(e);
	   	}
   	finally{
   		System.gc();
   	}
  }

	public void timestampItAndSave2(String toEdit) {
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


		Double lat21 = gps.getLatitude();
		Double lon21 = gps.getLongitude();


		//alamat_customer = model_customer.getAlamat();

		Log.d(TAG, "timestampItAndSave2: "+"latitudenya : " + lat21 +" longitudenya adalah : "+ lon21 + "alamatnya"+alamat_customer);


		//String latnya = String.valueOf(lat2);
		String latnya = lat21.toString();
		String lonnya = lon21.toString();
		//String lonnya = String.valueOf(lon2);

		Log.d(TAG, "timestampItAndSave2: latnya dan lonnya adalah :"+latnya + lonnya);





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

		//cs.drawText(alamat_customer,xsiteid,ysiteid, tPaint);
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
	         e.printStackTrace();
	     }
   		
	    return rotate;
   }


	///--------IMAGE COLLECTION----------------\\\
	public void onButtonClick2(View v) {
		int viewID;
		viewID = v.getId();

		if(viewID == R.id.button5){
			buttonId5 = R.id.button5;
			Action_Code = R.id.button5;
		}else if(viewID == R.id.button77){
			buttonId77 = R.id.button77;
			Action_Code = R.id.button77;
		}

		//ImageButton button = (ImageButton) v.findViewById(Action_Code);
		imageButton = (ImageButton) v.findViewById(Action_Code);
		imageButton77 = (ImageButton) v.findViewById(Action_Code);

		Cursor c = db.rawQuery("SELECT imageStr FROM "
				+ tableName + " WHERE buttonId = '" + Integer.toString(Action_Code) +"'", null);

		if(c.moveToFirst()){

			try{
				if(imageButton!=null && imageButton77 !=null){

					imageButton.setImageBitmap(null);
					imageButton77.setImageBitmap(null);
					byte[] imageAsBytes = Base64.decode(c.getString(0).getBytes(), Base64.DEFAULT);

					Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
					bitmap = Bitmap.createScaledBitmap(bitmap, 350, 350, false);

					imageButton.setImageBitmap(bitmap);
					imageButton77.setImageBitmap(bitmap);

					if (Action_Code == buttonId5){
						deleteFlag5 = true;
					}
					if (Action_Code == buttonId77){
						deleteFlag77 = true;
					}

					imageButton.setOnLongClickListener(listener2);
					imageButton77.setOnLongClickListener(listener2);

					bitmap=null;
					System.gc();
				}

			}
			finally{
				c.close();
			}
		}else{
			startCamera2();
		}


	}

	private void startCamera2(){


		if(dialog != null && dialog.isShowing())
			dialog.dismiss();

		String fileName = "photoProspect.jpg";
		//String fileName = "photoCollection.jpg";

		ContentValues values = new ContentValues();

		values.put(MediaStore.Images.Media.TITLE, fileName);
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

		imageUri = getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

		startActivityForResult(intent, Action_Code);
	}


	View.OnLongClickListener listener2 = new View.OnLongClickListener() {
		public boolean onLongClick(View v) {

			int viewID;
			viewID = v.getId();

			if(viewID == R.id.button5){
				buttonId5 = R.id.button5;
				Action_Code = R.id.button5;

				if(deleteFlag5 == true)
					buildintent();
			}

			if(viewID == R.id.button77){
				buttonId77 = R.id.button77;
				Action_Code = R.id.button77;

				if(deleteFlag77 == true)
					buildintent();
			}


			return true;
		}
	};



	private class PictureProcessing2 extends AsyncTask<Void, Void, String> {

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

				if(flag == 0 && imageUri != null) {
					loadcustomer();
					PictureAttach2();
				}
				else if(flag != 0) {
					loadcustomer();
					PictureAttach2();

				}
				else
					result = "imageUri = null";


				SystemClock.sleep(2000);



			}catch (Exception e) {
				DashBoardActivity.this.e = e;
				result = e.getMessage();
			}

			return result;
		}

		@Override
		protected void onPostExecute(String sResponse) {
			if(dialog != null && dialog.isShowing())
				dialog.dismiss();

			if (e==null && sResponse.length()==0){
				//ImageButton button = (ImageButton) findViewById(Action_Code);
				imageButton = (ImageButton) findViewById(Action_Code);


				/*if(button!=null)
					button.setImageBitmap(null);*/

				if(imageButton!=null)
					imageButton.setImageBitmap(null);

				if(imageButton77!=null)
					imageButton77.setImageBitmap(null);

				File cacheDir = getBaseContext().getCacheDir();
				File f = new File(cacheDir, "picProspect");
				FileInputStream fis = null;

				try {
					fis = new FileInputStream(f);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				Bitmap bitmap = BitmapFactory.decodeStream(fis);
				bitmap = Bitmap.createScaledBitmap(bitmap, 350, 350, false);

				/*if(bitmap!=null && button!=null) {
					button.setImageBitmap(bitmap);
				}*/

				if(bitmap!=null && imageButton!=null) {
					imageButton.setImageBitmap(bitmap);
				}
				if(bitmap!=null && imageButton77!=null) {
					imageButton77.setImageBitmap(bitmap);
				}

				try {
					fis.close();
					cacheDir.delete();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				bitmap=null;
				System.gc();
			}else
				Toast.makeText(mContext, sResponse, Toast.LENGTH_SHORT).show();
		}
	}

	void loadcustomer(){
			latinya2 = model_customer.getLat();
			longinya2 = model_customer.getLon();
		Log.d(TAG, "loadcustomer: latlongnya "+latinya2 +"dan"+longinya2);

	}
	private void PictureAttach2(){

		try{

			String picturePath = "";
			int exifOrientation;
			final int reqWidth = 320;
			final int reqHeight = 240;
			int scale = 2;
			ByteArrayOutputStream bos = null;
			image_str_coll = "";
			String latinya22 = "";
			String longinya22 = "";
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

			//ImageButton button = (ImageButton) findViewById(Action_Code);
			//button.setImageBitmap(bitmap);
			//bitmap = null;

			System.gc();

			Bitmap bitmap2 = null;

			bitmap2 = Bitmap.createBitmap(BitmapFactory.decodeFile(picturePath,o) , 0, 0,
					o.outWidth, o.outHeight, matrix, true);

			bos = new ByteArrayOutputStream();
			bitmap2.compress(CompressFormat.JPEG, 100, bos);

			File cacheDir = getBaseContext().getCacheDir();
			File f = new File(cacheDir, "picProspect");

			try {
				FileOutputStream out = new FileOutputStream(f);

				bitmap2.compress(Bitmap.CompressFormat.JPEG, 80, out);

				out.flush();
				out.close();

				timestampItAndSave2(f.getAbsolutePath());

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			bitmap2.recycle();
			bitmap2 = null;
			matrix.reset();
			matrix = null;

			System.gc();

			byte[] byt = bos.toByteArray();
			if (image_str != null){
				image_str = Base64.encodeToString(byt,Base64.DEFAULT);
			}else{
				image_str = "";}

			//latinya2 = lati.toString();
			//longinya2 = longi.toString();

			latinya22 = latinya2;
			longinya22 = longinya2;


			bos.flush();
			bos.close();
			byt = null;
			bos = null;
			System.gc();

			Cursor cursor = db.rawQuery("SELECT id FROM " + tableName + " WHERE buttonId = '" + Integer.toString(Action_Code) +"'", null);

			if(cursor.moveToFirst())
			{
				ContentValues cv = new ContentValues();
				//cv.put("imageStr", image_str_coll);
				cv.put("imageStr", image_str);
				datasource.updateData(db,cv,tableName," buttonId = '" + Action_Code + "'");
				cv.clear();
			}else{
				ContentValues cv = new ContentValues();

				if (Action_Code == buttonId5){
					deleteFlag5 = true;
				}else if (Action_Code == buttonId77){
					deleteFlag77 = true;
				}

				//cv.put("imageStr", image_str_coll);
				cv.put("imageStr", image_str);
				cv.put("buttonId", Action_Code);
//				cv.put("lat", "");
//				cv.put("lng", "");
				//dibawaheditannya dwi
//				cv.put("lat", latinya2);
//				cv.put("lng", longinya2);
				cv.put("lat", latinya22);
				cv.put("lng", longinya22);
				Log.d(TAG, "PictureAttach2: cek latitudenya customer " + latinya22+" dan longitudenya customer " + longinya22);
				//Log.d(TAG, "PictureAttach2: "+image_str_coll);
				Log.d(TAG, "PictureAttach2: "+image_str);
				datasource.generateData(db,cv,tableName);

				cv.clear();
				cursor.close();

			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally{
			System.gc();
		}
	}
	private void hapusfoto(){
		try{
			datasource.deleteData(db, " buttonId = '" + Action_Code + "'", tableName);
		}catch (Exception e){
			e.printStackTrace();
		}

	}


	private boolean checkgambar2() {

		try {
			boolean good = true;
			if (imageButton.getWidth() == 0 || imageButton.getDrawable() == null){
				Toast.makeText(mContext,"Ambil Gambar Dulu yaa.",Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(mContext,"gambar udah ada",Toast.LENGTH_SHORT).show();
			}

			return good;
		} catch (Exception e) {
			Log.e("DashBoardActivity", "Error in checkgambar2() : " + e.getMessage());
			e.printStackTrace();
			return false;
		}



	}
	///--------IMAGE COLLECTION----------------\\\


   /*
   private void handlelocation(){
	    
	   	if (locationManager == null){
	   		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	   	}
	   	
	   	latString = "";
	   	lngString = "";
	   	    	
	   	//exceptions will be thrown if provider is not permitted.
	   	try{gps_enabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);}
	   	catch(Exception ex){}
	   
	   	try{network_enabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);}
	   	catch(Exception ex){}
	   
	   	//don't start listeners if no provider is enabled
	   	//if(!gps_enabled && !network_enabled)
	    //  return;	           
	   	//if(gps_enabled){
	   	//	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);        	
	   	//}else{
	   	//	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
	   	//}
	   	
	   	if(!gps_enabled && !network_enabled)
	   		return;
	   	else
	   		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
                    	                
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
       	Toast.makeText(mContext, "Provider enabled, GPS turned on", Toast.LENGTH_SHORT).show();
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
	   double latitude = newLocation.getLatitude();
       double longitude = newLocation.getLongitude();
	    
       latString = Double.toString(latitude);
       lngString = Double.toString(longitude);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
   
   */


   @Override
   public void onPause() {
	super.onPause();
	
		if(dialog != null && dialog.isShowing())
			   dialog.dismiss();
   }
	
   @Override
   public void onDestroy() {
	   super.onDestroy();
	   
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
	   
	   try {
		   File cacheDir = getBaseContext().getCacheDir();
		   File f = new File(cacheDir, "picSurvey");     
		   FileInputStream fis = null;
		   
	       fis = new FileInputStream(f);
	       
	       fis.close();
		   cacheDir.delete();
	   } catch (FileNotFoundException e) {	    		        
	       e.printStackTrace();
	   } catch (IOException e) {
		   e.printStackTrace();
	   }
	   	   
   }
   
 }