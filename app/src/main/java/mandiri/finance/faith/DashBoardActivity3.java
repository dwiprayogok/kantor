package mandiri.finance.faith;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public abstract class DashBoardActivity3 extends Activity {
	private Uri imageUri;
    private static int Action_Code = 0;
    private int flag; 
    private DataSource datasource;
    private SQLiteDatabase db;
    private String tableName;
    
    private int buttonId1;
    private int buttonId2;
    private int buttonId3;
    private int buttonId4;    
    private int buttonId5;
    private int buttonId6;
    private int buttonId7;
    private int buttonId8;


	private int buttonId1_survey;
	private int buttonId2_survey;
	private int buttonId3_survey;
	private int buttonId4_survey;

//	private int button1_survey;
//	private int button2_survey;
//	private int button3_survey;
//	private int button4_survey;
    
    private boolean deleteFlag1 = false; 
    private boolean deleteFlag2 = false; 
    private boolean deleteFlag3  = false;
    private boolean deleteFlag4  = false;    
    private boolean deleteFlag5 = false; 
    private boolean deleteFlag6 = false; 
    private boolean deleteFlag7  = false;
    private boolean deleteFlag8  = false;


	private boolean deleteFlag1_survey = false;
	private boolean deleteFlag2_survey = false;
	private boolean deleteFlag3_survey  = false;
	private boolean deleteFlag4_survey  = false;

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
	ImageButton button1_survey1,button2_survey2,button3_survey3,button4_survey4;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        mContext = DashBoardActivity3.this;
        tableName = "image";
        generalPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        
        datasource = new DataSource(mContext);
        db = datasource.getWritableDatabase();

		button1_survey1 = (ImageButton) findViewById(R.id.button1_survey);
		button2_survey2 = (ImageButton) findViewById(R.id.button2_survey);
		button3_survey3 = (ImageButton) findViewById(R.id.button3_survey);
		button4_survey4 = (ImageButton) findViewById(R.id.button4_survey);
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
	   	}else if((Action_Code == buttonId6) && deleteFlag6 == true){
	   		listItems.add("Remove");
	   	}else if((Action_Code == buttonId7) && deleteFlag7 == true){
	   		listItems.add("Remove");
	   	}else if((Action_Code == buttonId8) && deleteFlag8 == true){
	   		listItems.add("Remove");
	   	}

		else if((Action_Code == buttonId1_survey) && deleteFlag1_survey == true){
			listItems.add("Remove");
		}

		else if((Action_Code == buttonId2_survey) && deleteFlag2_survey == true){
			listItems.add("Remove");
		}

		else if((Action_Code == buttonId3_survey) && deleteFlag3_survey == true){
			listItems.add("Remove");
		}

		else if((Action_Code == buttonId4_survey) && deleteFlag4_survey == true){
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



					else if (Action_Code == buttonId1_survey){
						deleteFlag1_survey = false;
					}
					else if (Action_Code == buttonId2_survey){
						deleteFlag2_survey = false;
					}
					else if (Action_Code == buttonId3_survey){
						deleteFlag3_survey = false;
					}
					else if (Action_Code == buttonId4_survey){
						deleteFlag4_survey = false;
					}

					
					ImageButton button = (ImageButton) findViewById(Action_Code);
					button.setImageBitmap(null);
					button.setImageResource(R.drawable.gallery);

					button1_survey1 = (ImageButton) findViewById(Action_Code);
					button1_survey1.setImageBitmap(null);
					button1_survey1.setImageResource(R.drawable.gallery);

					button2_survey2 = (ImageButton) findViewById(Action_Code);
					button2_survey2.setImageBitmap(null);
					button2_survey2.setImageResource(R.drawable.gallery);

					button3_survey3 = (ImageButton) findViewById(Action_Code);
					button3_survey3.setImageBitmap(null);
					button3_survey3.setImageResource(R.drawable.gallery);

					button4_survey4 = (ImageButton) findViewById(Action_Code);
					button4_survey4.setImageBitmap(null);
					button4_survey4.setImageResource(R.drawable.gallery);
					
					datasource.deleteData(db, " buttonId = '" + Action_Code + "'", tableName);

					//hapusfoto();
					
					dialog.dismiss();
					
					Toast.makeText(mContext, "Picture has been removed", Toast.LENGTH_SHORT).show();

				}
				
			}
		});

		alertDialog = builder.create();
		alertDialog.show();
  	}


	//ini panggil id dari imagebutton yg buat ambil foto
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
    	   	}else if(viewID == R.id.button5){
    	   		buttonId5 = R.id.button5;
    			Action_Code = R.id.button5;
    			
    			if(deleteFlag5 == true)
    				buildintent();    	
    	   	}else if(viewID == R.id.button6){
    	   		buttonId6 = R.id.button6;
    			Action_Code = R.id.button6;
    			
    			if(deleteFlag6 == true)
    				buildintent();    	
    	   	}else if(viewID == R.id.button7){
    	   		buttonId7 = R.id.button7;
    			Action_Code = R.id.button7;
    			
    			if(deleteFlag7 == true)
    				buildintent();    	
    	   	}else if(viewID == R.id.button8){
    	   		buttonId8 = R.id.button8;
    			Action_Code = R.id.button8;
    			
    			if(deleteFlag8 == true)
    				buildintent();    	
    	   	}
       	
           return true;
       }
   };
   
   public void onButtonClick(View v)
   {
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
	   	}else if(viewID == R.id.button5){
	   		buttonId5 = R.id.button5;
			Action_Code = R.id.button5;
	   	}else if(viewID == R.id.button6){
	   		buttonId6 = R.id.button6;
			Action_Code = R.id.button6;
	   	}else if(viewID == R.id.button7){
	   		buttonId7 = R.id.button7;
			Action_Code = R.id.button7;
	   	}else if(viewID == R.id.button8){
	   		buttonId8 = R.id.button8;
			Action_Code = R.id.button8;
	   	}
	   	
	   	ImageButton button = (ImageButton) v.findViewById(Action_Code);   
		
	   	Cursor c = db.rawQuery("SELECT imageStr FROM " + tableName + " WHERE buttonId = '"
				+ Integer.toString(Action_Code) +"'", null);
	 	   
        if(c.moveToFirst()){
        	
        	try{	 				 				
 				if(button!=null){
 					
 					button.setImageBitmap(null);	 					
 					byte[] imageAsBytes = Base64.decode(c.getString(0).getBytes(), Base64.DEFAULT);
 					
 					Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
 					bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
 					
 					button.setImageBitmap(bitmap); 					
 					
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
	   
	   String fileName = "photoSurvey.jpg";
       ContentValues values = new ContentValues();
       
       values.put(MediaStore.Images.Media.TITLE, fileName);
       values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
       
       imageUri = getContentResolver().insert(
	   			MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
      
	   	if (imageUri != null) {
	 	   Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	 	   intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
	                     
	 	   startActivityForResult(intent, Action_Code);
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
	    		   DashBoardActivity3.this.e = e;
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
	    		   	
	    		   	File cacheDir = getBaseContext().getCacheDir();
	    		    File f = new File(cacheDir, "picSurvey");     
	    		    FileInputStream fis = null;
	    		    
	    		    try {
	    		        fis = new FileInputStream(f);
	    		    } catch (FileNotFoundException e) {	    		        
	    		        e.printStackTrace();
	    		    }
	    		    Bitmap bitmap = BitmapFactory.decodeStream(fis);
	    		    bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
	    		    
	    		    if(bitmap!=null && button!=null)
	    		   		button.setImageBitmap(bitmap);	    		   		
	    		   	
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
   
   public void onActivityResult(int requestCode, int resultCode, final Intent data) {
               
       if (requestCode == Action_Code) {
    	       	   
    	   if (resultCode == RESULT_OK){
    		   datax = data;
    		       		   
    		   if(dialog != null && dialog.isShowing())
    			   dialog.dismiss();
    		   
    		   new PictureProcessing().execute();
			   new PictureProcessing3().execute();

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
                 			   getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
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
                 			   getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
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
                 			   getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
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
                 			   getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
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
			bitmap2.compress(CompressFormat.JPEG, 80, bos);
			
			File cacheDir = getBaseContext().getCacheDir();
			File f = new File(cacheDir, "picSurvey");

			try {
			    FileOutputStream out = new FileOutputStream(f);
			    
			    bitmap2.compress(Bitmap.CompressFormat.JPEG, 80, out);
			    
			    out.flush();
			    out.close();

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
    		throw new RuntimeException(e);
 	   	}
    	finally{
    		System.gc();
    	}
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













   //----PART 2 ---------------------///

	public void onButtonClick3(View v)
	{
		int viewID;
		viewID = v.getId();

		if(viewID == R.id.button1_survey){
			buttonId1_survey = R.id.button1_survey;
			Action_Code = R.id.button1_survey;

		}else if(viewID == R.id.button2_survey){
			buttonId2_survey = R.id.button2_survey;
			Action_Code = R.id.button2_survey;

		}else if(viewID == R.id.button3_survey){
			buttonId3_survey = R.id.button3_survey;
			Action_Code = R.id.button3_survey;

		}else if(viewID == R.id.button4_survey){
			buttonId4_survey = R.id.button4_survey;
			Action_Code = R.id.button4_survey;
		}

		//ImageButton button = (ImageButton) v.findViewById(Action_Code);

		button1_survey1= (ImageButton) v.findViewById(Action_Code);
		button2_survey2 = (ImageButton) v.findViewById(Action_Code);
		button3_survey3= (ImageButton) v.findViewById(Action_Code);
		button4_survey4 = (ImageButton) v.findViewById(Action_Code);

		Cursor c = db.rawQuery("SELECT imageStr FROM " + tableName + " WHERE buttonId = '"
				+ Integer.toString(Action_Code) +"'", null);

		if(c.moveToFirst()){

			try{
//				if(button!=null){
				if (button1_survey1 !=null || button2_survey2 !=null ||
						button3_survey3 !=null || button4_survey4 !=null){

					//button.setImageBitmap(null);

				button1_survey1.setImageBitmap(null);
				button2_survey2.setImageBitmap(null);
				button3_survey3.setImageBitmap(null);
				button4_survey4.setImageBitmap(null);

					byte[] imageAsBytes = Base64.decode(c.getString(0).getBytes(), Base64.DEFAULT);

					Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
					bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);

					//button.setImageBitmap(bitmap);


				button1_survey1.setImageBitmap(bitmap);
				button2_survey2.setImageBitmap(bitmap);
				button3_survey3.setImageBitmap(bitmap);
				button4_survey4.setImageBitmap(bitmap);


				if (Action_Code == buttonId1_survey){
						deleteFlag1_survey = true;
					}else if (Action_Code == buttonId2_survey){
						deleteFlag2_survey = true;
					}else if (Action_Code == buttonId3_survey){
						deleteFlag3_survey = true;
					}else if (Action_Code == buttonId4_survey){
						deleteFlag4_survey = true;
					}

					//button.setOnLongClickListener(listener3);

					button1_survey1.setOnLongClickListener(listener3);
					button2_survey2.setOnLongClickListener(listener3);
					button3_survey3.setOnLongClickListener(listener3);
					button4_survey4.setOnLongClickListener(listener3);

					bitmap=null;
					System.gc();
				}

			}
			finally{
				c.close();
			}
		}else{
			startCamera3();
		}

	}

	private void startCamera3(){

		if(dialog != null && dialog.isShowing())
			dialog.dismiss();

		String fileName = "photoSurvey.jpg";
		ContentValues values = new ContentValues();

		values.put(MediaStore.Images.Media.TITLE, fileName);
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

		imageUri = getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

		if (imageUri != null) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

			startActivityForResult(intent, Action_Code);
		}

	}

   View.OnLongClickListener listener3 = new View.OnLongClickListener() {
	   public boolean onLongClick(View v) {

		   int viewID;
		   viewID = v.getId();

		   if(viewID == R.id.button1_survey){
			   buttonId1_survey = R.id.button1_survey;
			   Action_Code = R.id.button1_survey;

			   if(deleteFlag1_survey == true)
				   buildintent();
		   }else if(viewID == R.id.button2_survey){
			   buttonId2_survey = R.id.button2_survey;
			   Action_Code = R.id.button2_survey;

			   if(deleteFlag2_survey == true)
				   buildintent();
		   }else if(viewID == R.id.button3_survey){
			   buttonId3_survey = R.id.button3_survey;
			   Action_Code = R.id.button3_survey;

			   if(deleteFlag3_survey == true)
				   buildintent();
		   }else if(viewID == R.id.button4_survey){
			   buttonId4_survey = R.id.button4_survey;
			   Action_Code = R.id.button4_survey;

			   if(deleteFlag4_survey == true)
				   buildintent();
		   }

		   return true;
	   }
   };



	private class PictureProcessing3 extends AsyncTask<Void, Void, String> {

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
					PictureAttach3();
				else if(flag != 0)
					PictureAttach3();
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
				DashBoardActivity3.this.e = e;
				result = e.getMessage();
			}

			return result;
		}

		@Override
		protected void onPostExecute(String sResponse) {
			if(dialog != null && dialog.isShowing())
				dialog.dismiss();

			if (e==null && sResponse.length()==0 && sResponse!=null){
				//ImageButton button = (ImageButton) findViewById(Action_Code);
				button1_survey1 = (ImageButton) findViewById(Action_Code);
				button2_survey2 = (ImageButton) findViewById(Action_Code);
				button3_survey3 = (ImageButton) findViewById(Action_Code);
				button4_survey4 = (ImageButton) findViewById(Action_Code);
//
//				if(button!=null)
//					button.setImageBitmap(null);

				if(button1_survey1!=null)
					button1_survey1.setImageBitmap(null);

				if(button2_survey2!=null)
					button2_survey2.setImageBitmap(null);

				if(button3_survey3!=null)
					button3_survey3.setImageBitmap(null);

				if(button4_survey4!=null)
					button4_survey4.setImageBitmap(null);

				File cacheDir = getBaseContext().getCacheDir();
				File f = new File(cacheDir, "picSurvey");
				FileInputStream fis = null;

				try {
					fis = new FileInputStream(f);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				Bitmap bitmap = BitmapFactory.decodeStream(fis);
				bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);

//				if(bitmap!=null && button!=null)
//					button.setImageBitmap(bitmap);

				if(bitmap!=null && button1_survey1!=null)
					button1_survey1.setImageBitmap(bitmap);

				if(bitmap!=null && button2_survey2!=null)
					button2_survey2.setImageBitmap(bitmap);

				if(bitmap!=null && button3_survey3!=null)
					button3_survey3.setImageBitmap(bitmap);

				if(bitmap!=null && button4_survey4!=null)
					button4_survey4.setImageBitmap(bitmap);

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

	private void PictureAttach3(){

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
			bitmap2.compress(CompressFormat.JPEG, 80, bos);

			File cacheDir = getBaseContext().getCacheDir();
			File f = new File(cacheDir, "picSurvey");

			try {
				FileOutputStream out = new FileOutputStream(f);

				bitmap2.compress(Bitmap.CompressFormat.JPEG, 80, out);

				out.flush();
				out.close();

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

				if (Action_Code == buttonId1_survey){
					deleteFlag1_survey = true;
				}else if (Action_Code == buttonId2_survey){
					deleteFlag2_survey = true;
				}else if (Action_Code == buttonId3_survey){
					deleteFlag3_survey = true;
				}else if (Action_Code == buttonId4_survey){
					deleteFlag4_survey = true;
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
	private void hapusfoto(){
		try{
			datasource.deleteData(db, " buttonId = '" + Action_Code + "'", tableName);
		}catch (Exception e){
			e.printStackTrace();
		}

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