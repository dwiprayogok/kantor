package mandiri.finance.faith;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.InstanceIdResult;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mandiri.finance.faith.Interface.GetAllPermissionV2;
//import com.google.android.gcm.GCMRegistrar;

public class LoginActivity extends Activity {
	protected boolean _active = true;
	protected String strimei = "";
	protected String strloginid = "";
	protected String strpassword = "";
	private String lastinsertprospectform = null;
	private String lastinsertsurveyform = null;
	private String lastinsertcollectionform = null;
	private String lastinsertcollectionplanform = null;
	private String lastinsertasset = null;
	private String lastinsertzipcode = null;
	private String lastinsertsupplier = null;
	private String lastinsertsurveylocform = null;
	private String lastinsertproduct = null;
	private String lastinsertproductoffering = null;
	private String lastinserttempatlahir = null;
	private String lastinsertsales = null;

	private String errMessage = "";
	private String timeOut;
	private String branchID;
	private String branchName;
	private Boolean isSingleBranch;
	private String flag;
	private String itemBranchID;
	private String itemBranchName;
	private String newVersion;
	private String authorizedMenu;
	private String userID;
	private String passwordExpiredFlag;
	private long locationInterval;
	private String kodeBank;
	private String noRek;
	private String WebURLAddress;
	private String tglserver;

	private DataSource datasource;
	private SQLiteDatabase db;
	private DataLocal datalocal;

	private EditText entry;
	private EditText password;
	private Spinner branchSpinner;
	private TextView tvBranch;
	private Button btnLogin;

	private Exception e = null;
	private Context mContext;
	private SharedPreferences generalPrefs;

	private String theKey;
	private Dialog dialog;
	private BranchAdapter aa;
	//private AutoUpdateApk aua;
	private ArrayList<SearchResults> results = new ArrayList<SearchResults>();

	private boolean updateCheck = false;

	String isLoadProspectForm = "0";
	String isLoadSurveyForm = "0";
	String isLoadCollectionForm = "0";
	String isLoadAsset = "0";
	String isLoadZipCode = "0";
	String isLoadProduct = "0";
	String isLoadProductOffering = "0";
	String isLoadTempatLahir = "0";
	String isLoadSales = "0";
	String isLoadSupplier = "0";
	String isLoadCollectionPlanForm = "0";
	String isLoadSurveyLocForm = "0";
	String branchAddress = "";
	String branchPhone = "";
	String TAG = "Login";
	public String refreshToken = "";
	private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
	List<String> permissionList = new ArrayList<>();
	GetAllPermissionV2 getAllPermission;
	int PERMISSION_ALL = 101;
	private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    String currentVersion = "";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		mContext = LoginActivity.this;
		//checkversion();
        new GetVersionCode().execute();
		datalocal = new DataLocal();
		datasource = new DataSource(mContext);
		db = datasource.getWritableDatabase();
		getAllPermission = new GetAllPermissionV2(mContext);
		generalPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); //tambahan 11-9-2017


		datasource.createTableControl(db);
		datasource.createTableResult(db);
		datasource.createTableForm(db);
		datasource.createTableAsset(db);
		datasource.createTableSurveyList(db);
		//datasource.createTableCollectionList(db);
		//datasource.createTableCollectionResult(db);
		datasource.createTableInfoDetail(db);
		datasource.createTableImageURLDetail(db);
		datasource.createTableLocationSource(db);
		datasource.createTableImage(db);
		datasource.createTableSupplier(db);
		datasource.createTableSurveyLocList(db);
		//datasource.createTableImageCollection(db);//tambahanbaru
		//datasource.createTablePrintCash(db);//tambahan untuk re-print
		datasource.createTableImageSurveyLocation(db); // tambahan untuk image di surveylocation
		datasource.createTableGuide(db); // tambahan untuk table guide
		//datasource.createTableGrading(db); //tambahan untuk grading customer
		datasource.createTableZipCode(db); //
		datasource.createTableProduct(db); // tambahan table product
		datasource.createTableProductOffering(db); // tambahan table productOffering
		datasource.createTableSizeZipcode(db); //
		datasource.createTableSales(db); //tambahan
		datasource.createTableEvent(db); //tambahan event
		datasource.createTableNews(db);


		entry = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		btnLogin = (Button) findViewById(R.id.btnLogin);

		branchSpinner = (Spinner) findViewById(R.id.spBranch);
		tvBranch = (TextView) findViewById(R.id.tvBranch);

		entry.setVisibility(View.VISIBLE);
		password.setVisibility(View.VISIBLE);
		branchSpinner.setVisibility(View.GONE);
		tvBranch.setVisibility(View.GONE);
		flag = "";

		dialog = new Dialog(mContext, R.style.lightbox_dialog);
		dialog.setContentView(R.layout.lightbox_dialog);
		((TextView) dialog.findViewById(R.id.dialogText)).setText("Loading...");
		dialog.setCancelable(false);


		if (getAllPermission.checkPermission()) {
			//Toast.makeText(getApplicationContext(),"Permission IMEI HAVE BEEN GRANTED",Toast.LENGTH_SHORT).show();
			//pindahintent();
		}

		//trigger 'loadIMEI
		//loadIMEI();
		//loadPhoto();



    	/*
    	Integer i = 0;    	
    	Cursor ti = db.rawQuery("PRAGMA table_info(result)", null);
	    if ( ti.moveToFirst() ) {
	        do {
	        	i = i + 1;
	        } while (ti.moveToNext());
	    }
	    	    
	    if(i < 14){
	    	datasource.dropTable(db, "result");
	    	datasource.dropTable(db, "form");
	    		    	
	    	datasource.createTableResult(db);
	    	datasource.createTableForm(db);	    	
	    }
	    
	    ti.close();
	    */

		db.close();
        try {
             currentVersion = getPackageManager().getPackageInfo(getPackageName(),0).versionName;
        } catch (NameNotFoundException e1) {
            e1.printStackTrace();
        }

		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pindahintent();
			}
		});

	}

	private void checkversion(){
		AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
		try
		{
			PackageManager manager = mContext.getPackageManager();
			PackageInfo info = manager.getPackageInfo("mandiri.finance.faith", 0);
			int code = info.versionCode;
			final String versionName = info.versionName;
			Log.d(TAG, "checkversion: code"+ code + "nama versinya adalah " + versionName);

			if (!versionName.equals(versionName)) {


//    				    	if (aua == null && !updateCheck){
//    				    		aua = new AutoUpdateApk(getApplicationContext());
//    				    		aua.checkUpdatesManually();
//    				    		updateCheck = true;
//    				    	}

				builder.setIcon(android.R.drawable.ic_dialog_alert);
				builder.setTitle("Alert");
				builder.setMessage("Please update app to latest version");
				builder.setCancelable(false);

				builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dlg, int sumthin) {
						if (!updateCheck){
							Intent intent = new Intent(Intent.ACTION_VIEW,
									Uri.parse(WebURLAddress));
							startActivity(intent);
							updateCheck = true;
						}

						WelcomeActivity.startedApp = false;
						dlg.dismiss();
						return;
					}
				});

				builder.show();
			}else {
				Toast.makeText(mContext,"Versinya masih sama !!",Toast.LENGTH_LONG).show();
			}
		}
		catch(NameNotFoundException nnf)
		{
			nnf.printStackTrace();
		}
	}


	public void loadIMEI() {
		if (ActivityCompat.checkSelfPermission(this,
				android.Manifest.permission.READ_PHONE_STATE)
				!= PackageManager.PERMISSION_GRANTED && (ActivityCompat.checkSelfPermission(this,
				android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
				ActivityCompat.checkSelfPermission(this, android.Manifest.permission
						.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
			requestReadPhoneStatePermission();
		} else {
			pindahintent();
		}
	}

	private void loadPhoto() {

		if (ActivityCompat.checkSelfPermission(this,
				android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
				ActivityCompat.checkSelfPermission(this, android.Manifest.permission
						.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			requestReadPhoneStatePermission();
		} else {
			Toast.makeText(mContext, "Camera Already Active...!!!", Toast.LENGTH_SHORT).show();
		}
	}


	private void requestReadPhoneStatePermission() {
//		if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//				android.Manifest.permission.READ_PHONE_STATE)&&
//				ActivityCompat.shouldShowRequestPermissionRationale(this,
//						android.Manifest.permission.CAMERA)) {
//			new AlertDialog.Builder(LoginActivity.this)
//					.setTitle("Permission Request")
//					.setMessage("Check permission")
//					.setCancelable(false)
//					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int which) {
//							//re-request
//							ActivityCompat.requestPermissions(LoginActivity.this,
//									new String[]{
//									android.Manifest.permission.READ_PHONE_STATE,},
//									MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
//							ActivityCompat.requestPermissions(LoginActivity.this,
//									new String[]{
//											android.Manifest.permission.CAMERA,
//											android.Manifest.permission.READ_EXTERNAL_STORAGE},
//									MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//						}
//					})
//					.show();
//		} else {
//			ActivityCompat.requestPermissions(this, new String[]{
//							android.Manifest.permission.READ_PHONE_STATE},
//					MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
//			ActivityCompat.requestPermissions(this, new String[]{
//							android.Manifest.permission.CAMERA,
//							android.Manifest.permission.READ_EXTERNAL_STORAGE},
//					MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//		}

//		if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//				android.Manifest.permission.CAMERA)) {
//			new AlertDialog.Builder(LoginActivity.this)
//					.setTitle("Permission Request")
//					.setMessage("Check permission")
//					.setCancelable(false)
//					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int which) {
//							//re-request
//							ActivityCompat.requestPermissions(LoginActivity.this,
//									new String[]{
//											android.Manifest.permission.CAMERA,
//											android.Manifest.permission.READ_EXTERNAL_STORAGE},
//									MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//						}
//					})
//					.show();
//		} else {
//			ActivityCompat.requestPermissions(this, new String[]{
//							android.Manifest.permission.CAMERA,
//							android.Manifest.permission.READ_EXTERNAL_STORAGE},
//					MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {

//		if(requestCode==PERMISSION_ALL)
//		{
//			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//				// Camera permission has been granted, preview can be displayed
//				Log.i(TAG, "permission has now been granted. Showing preview.");
//				Toast.makeText(getApplicationContext(),"Permission IMEI HAVE BEEN GRANTED",Toast.LENGTH_SHORT).show();
//				pindahintent();
//			} else {
//				Log.i(TAG, "CAMERA permission was NOT granted.");
//			}
//		}
//
//
//		if (permissionList.size()>0) {
//			for (int i = 0; i < permissionList.size(); i++) {
//
//				if (requestCode == i) {
//					// BEGIN_INCLUDE(permission_result)
//					// Received permission result for camera permission.
//					Log.i(TAG, "Received response for Camera permission request.");
//
//					// Check if the only required permission has been granted
//					if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//						// Camera permission has been granted, preview can be displayed
//						Log.i(TAG, "CAMERA permission has now been granted. Showing preview.");
//						Toast.makeText(getApplicationContext(), "Camera Already Active...!!!", Toast.LENGTH_SHORT).show();
//						permissionList.remove(i);
//
//					} else {
//						Log.i(TAG, "CAMERA permission was NOT granted.");
//						Toast.makeText(getApplicationContext(), "CAMERA permission was NOT granted", Toast.LENGTH_SHORT).show();
//
//					}
//
//
//				} else {
//					super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//				}
//
//			}
//		}


		if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE
				|| requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(getApplicationContext(), "Permission IMEI and Camera HAVE BEEN GRANTED", Toast.LENGTH_SHORT).show();
				pindahintent();
			} else {
				Toast.makeText(getApplicationContext(), "permission gagal", Toast.LENGTH_SHORT).show();

			}
		}

//		if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
//			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//				Toast.makeText(getApplicationContext(), "Camera Already Active...!!!", Toast.LENGTH_SHORT).show();
//			}
//		} else {
//			Toast.makeText(getApplicationContext(), "permission gagal", Toast.LENGTH_SHORT).show();
//
//		}

	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		WelcomeActivity.startedApp = false;
		finish();
		return;
	}

	public void pindahintent() {
		LocationManager locationManager = (LocationManager) mContext.getApplicationContext()
				.getSystemService(Context.LOCATION_SERVICE);


		if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			buildAlertMessage();
			return;
		}

		TelephonyManager m_telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (flag.length() == 0) {
			Log.d(TAG, "pindahintent: flagnya APaanya ya " + flag);

			//strimei = m_telephonyManager.getDeviceId().trim();
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

				return;
			}
			strimei = m_telephonyManager.getDeviceId().trim();
			strloginid = entry.getText().toString().trim();
			strpassword = password.getText().toString().trim();
			Log.d(TAG, "pindahintent: " + strimei + " dan " + strloginid + " dan " + strpassword);

			//validasi usernamdanpassword
			if ((strloginid.trim().length() == 0) || (strpassword.trim().length() == 0)) {

				if (strloginid.trim().length() == 0) {
					entry.setError("Enter Username");
				}


				if (strpassword.trim().length() == 0) {
					password.setError("Enter Password");
				}
			} else if (strpassword.trim().length() == 0) {
				password.setError("Enter Password");
			} else {

				new CheckAuthorized().execute();
			}

		} else {
			if (itemBranchID.equals("-")) {
				Toast.makeText(mContext, "Please select branch", Toast.LENGTH_SHORT).show();
				return;
			} else {
				new GetBranchInfo().execute();
			}

		}
	}

	public void LoginClick(View v) {
		LocationManager locationManager = (LocationManager) mContext.getApplicationContext()
				.getSystemService(Context.LOCATION_SERVICE);

		if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			buildAlertMessage();
			return;
		}


		if (flag.length() == 0) {

			TelephonyManager m_telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

				return;
			}
			strimei = m_telephonyManager.getDeviceId().trim();
        	strloginid = entry.getText().toString().trim();
        	strpassword = password.getText().toString().trim();

        	//validasi usernamdanpassword
	    	if((strloginid.trim().length() == 0) || (strpassword.trim().length() == 0))
	        {

	    		if(strloginid.trim().length() == 0)
	            {
	    			entry.setError("Enter Username");
	            }


	    		if(strpassword.trim().length() == 0)
	            {
	        		password.setError("Enter Password");
	            }
	        }
	    	else if(strpassword.trim().length() == 0)
	        {
	    		password.setError( "Enter Password" );
	        }
	    	else{

	    		new CheckAuthorized().execute();
	    	}

    	}else{
    		if(itemBranchID.equals("-")){
    			Toast.makeText(mContext, "Please select branch", Toast.LENGTH_SHORT).show();
    			return;
    		}else{
    			new GetBranchInfo().execute();
    		}

    	}

	}



    private class GetVersionCode extends AsyncTask<Void, String, String> {
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        @Override
        protected String doInBackground(Void... voids) {
            String newVersion = "";
            try {
				newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + mContext.getPackageName())
						.timeout(10000)
						.get()
						.select(".hAyfc .htlgb")
						//.get(5)
						.get(7)
						.ownText();

                Log.d(TAG, "doInBackground: newVersion " + newVersion);
				Log.d(TAG, "doInBackground:currentVersion " + currentVersion);
				Log.d(TAG, "doInBackground:getPackageName " +  mContext.getPackageName());
                return newVersion;

            } catch (Exception e) {
                return newVersion;
            }
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
			Log.d(TAG, "onPostExecute: currentVersion" + currentVersion);
            Log.d(TAG, "onPostExecute: onlineVersion1" + onlineVersion);


			if (!currentVersion.equals(onlineVersion)) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);

				alertDialogBuilder.setTitle(LoginActivity.this.getString(R.string.app_name));
				alertDialogBuilder.setMessage(LoginActivity.this.getString(R.string.update_message));
				alertDialogBuilder.setCancelable(false);
				alertDialogBuilder.setPositiveButton(R.string.update_now, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						LoginActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
						dialog.cancel();
					}
				});
				alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				alertDialogBuilder.show();
			}else {
				Toast.makeText(mContext,"Versinya adalah = " + currentVersion,Toast.LENGTH_SHORT).show();
			}


            Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);
        }
    }



    private void buildAlertMessage() {
	    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
	    builder.setIcon(android.R.drawable.ic_dialog_info);
	    builder.setTitle("Confirmation");
	    builder.setMessage("Location service not active, activated now ?")
	           .setCancelable(false)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, final int id) {
	            	   Intent myintent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	            	   startActivity(myintent);           	   
	            	   return;
	               }
	           })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, final int id) {
	                   dialog.cancel();
	                   return;
	               }	
	           });
	    final AlertDialog alert = builder.create();
	    alert.show();
	}
    
    private class GetBranchInfo extends AsyncTask<Void, Void, String> {
    	@Override
        protected void onPreExecute() {
        	dialog.show();        	
        }
    	
		@Override
		protected String doInBackground(Void... arg0) {
			try {
    			String branchAddress = "";
    			String branchPhone = "";
    			String encyptedBranchID = "";
    			
    			Calendar c = Calendar.getInstance();
        		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
                String myTime = df.format(c.getTime());
                
                encyptedBranchID = datalocal.encrypt(itemBranchID,myTime);
            	theKey = datalocal.encrypt(myTime,getString(R.string.Key44));
            	            	
            	InputStream in = null;    		    
    		    URL url = new URL(getString(R.string.url) +
    		    		"/GetBranchInfo?&a=" + URLEncoder.encode(encyptedBranchID,"UTF-8") + 
    					"&b=" + URLEncoder.encode(theKey,"UTF-8"));

				Log.d(TAG, "doInBackground: "+ getString(R.string.url) +
						"/GetBranchInfo?&a=" + URLEncoder.encode(encyptedBranchID,"UTF-8") +
						"&b=" + URLEncoder.encode(theKey,"UTF-8"));
				URLConnection conn = url.openConnection();
    		    
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
    			Element root = doc.getDocumentElement();			

    			NodeList header = root.getElementsByTagName("branchinfo");	
    			Node childnode = header.item(0);
    			NamedNodeMap map = childnode.getAttributes();
    			    			
    			branchAddress = map.getNamedItem("BranchAddress").getNodeValue().trim();
    			branchPhone = map.getNamedItem("BranchPhone").getNodeValue().trim();
    			    			
    			SharedPreferences.Editor prefsEditor = generalPrefs.edit();		        
		        prefsEditor.putString("branchAddress", branchAddress).commit();
		        prefsEditor.putString("branchPhone", branchPhone).commit();
		        prefsEditor.putString("branchID", itemBranchID).commit();
		        prefsEditor.putString("branchName", itemBranchName).commit();

				Log.d(TAG, "doInBackground: "+ branchAddress + " dan" + branchPhone
											+ "lagi" + itemBranchID+ " aduh " + itemBranchName);

			} catch (Exception e) {
    			Log.e("Login","GetBranchInfo : " + e.getMessage());
    			e.printStackTrace();
    			LoginActivity.this.e = e;
    		}  		
    		
			return null;		
		}
		
		@Override
        protected void onPostExecute(String sResponse) {
        	if (e==null) {    			
    			finish();
    			Intent myintent = new Intent(mContext,HomeActivity.class);
    			startActivity(myintent);
        	}else {
        		Toast.makeText(mContext, "Connection time out", Toast.LENGTH_SHORT).show();
        		e = null;
        	}
        	
        	dialog.dismiss();
        	
		}
    	
    }
    
        
    private class CheckAuthorized extends AsyncTask<Void, Void, String> {
    	AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                
        @Override
        protected void onPreExecute() {
        	dialog.show();        	
        }
        
    	@Override
		protected String doInBackground(Void... params) {
    		
    		db = datasource.getWritableDatabase();
    		String isAuthorized = "0";
    		
    		Cursor cc = db.rawQuery("SELECT lastinsertprospectform, lastinsertsurveyform, " +
    				"lastinsertcollectionform, lastinsertasset, lastinsertsupplier, lastinsertcollectionplanform, " +
    				"lastinsertsurveylocform, lastinsertzipcode, lastinsertproduct, lastinsertproductoffering, " +
					"lastinserttempatlahir, lastinsertsales FROM control", null);

			Log.d(TAG, "doInBackground:cek query "+ db.rawQuery("SELECT lastinsertprospectform, lastinsertsurveyform, " +
					"lastinsertcollectionform, lastinsertasset, lastinsertsupplier, lastinsertcollectionplanform, " +
					"lastinsertsurveylocform, lastinsertzipcode, lastinsertproduct, lastinsertproductoffering, " +
					"lastinserttempatlahir, lastinsertsales FROM control", null));

			try {
    			    			
    			Calendar c = Calendar.getInstance();
        		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
                String myTime = df.format(c.getTime());
                
                strimei = datalocal.encrypt(strimei,myTime);
            	strloginid = datalocal.encrypt(strloginid,myTime);
            	strpassword = datalocal.encrypt(strpassword,myTime);
            	theKey = datalocal.encrypt(myTime,getString(R.string.Key44));

				Log.d(TAG, "doInBackground: "+
						" cek_imei "+strimei
				+" cek_loginid "+strloginid
				+" cek_password "+strpassword
				+" cek_key "+theKey);

				try{
	        		if(cc.moveToFirst())
	    		    {        		    	
	    		    	if(cc.getString(0)==null){
	    		    		lastinsertprospectform = "";
	    		    	}else{
	    		    		lastinsertprospectform = datalocal.encrypt(cc.getString(0),myTime);
	    		    	}
	    		    	
	    		    	if(cc.getString(1)==null){
	    		    		lastinsertsurveyform = "";
	    		    	}else{
	    		    		lastinsertsurveyform = datalocal.encrypt(cc.getString(1),myTime);
	    		    	}
	    		    	
	    		    	if(cc.getString(2)==null){
	    		    		lastinsertcollectionform = "";
	    		    	}else{
	    		    		lastinsertcollectionform = datalocal.encrypt(cc.getString(2),myTime);
	    		    	}
	    		    	
	    		    	if(cc.getString(3)==null){
	    		    		lastinsertasset = "";
	    		    	}else{
	    		    		lastinsertasset = datalocal.encrypt(cc.getString(3),myTime);
	    		    	}
	    		    	
	    		    	if(cc.getString(4)==null){
	    		    		lastinsertsupplier = "";
	    		    	}else{
	    		    		lastinsertsupplier = datalocal.encrypt(cc.getString(4),myTime);
	    		    	}
	    		    	
	    		    	if(cc.getString(5)==null){
	    		    		lastinsertcollectionplanform = "";
	    		    	}else{
	    		    		lastinsertcollectionplanform = datalocal.encrypt(cc.getString(5),myTime);
	    		    	}
	    		    	
	    		    	if(cc.getString(6)==null){
	    		    		lastinsertsurveylocform = "";
	    		    	}else{
	    		    		lastinsertsurveylocform = datalocal.encrypt(cc.getString(6),myTime);
	    		    	}

						if(cc.getString(7)==null){
							lastinsertzipcode = "";
						}else{
							lastinsertzipcode = datalocal.encrypt(cc.getString(7),myTime);
						}

						if(cc.getString(8)==null){
							lastinsertproduct = "";
						}else{
							lastinsertproduct = datalocal.encrypt(cc.getString(8),myTime);
						}

						if(cc.getString(9)==null){
							lastinsertproductoffering = "";
						}else{
							lastinsertproductoffering = datalocal.encrypt(cc.getString(9),myTime);
						}
						if(cc.getString(10)==null){
							lastinserttempatlahir = "";
						}else{
							lastinserttempatlahir = datalocal.encrypt(cc.getString(10),myTime);
						}
						if(cc.getString(11)==null){
							lastinsertsales = "";
						}else{
							lastinsertsales = datalocal.encrypt(cc.getString(11),myTime);
						}
	    		    	
	    		    }else{
	    		    	lastinsertprospectform = "";
	    		    	lastinsertsurveyform = "";
	    		    	lastinsertcollectionform = "";
	    		    	lastinsertasset = "";
						lastinsertzipcode = "";
						lastinsertproduct = "";//tambahan
						lastinsertproductoffering = "";//tambahan
						lastinserttempatlahir = "";//tambahan
						lastinsertsales = ""; //tambahan
	    		    	lastinsertsupplier = "";
	    		    	lastinsertcollectionplanform = "";
	    		    	lastinsertsurveylocform = "";
	    		    }
	    		}catch (Exception e) {
	    			e.printStackTrace();	    			
	    		}
	    		
            	            	
            	InputStream in = null;    		    
    		    URL url = new URL(getString(R.string.url) +
//    		    		"/GetAndroidLoginData2?&a=" + URLEncoder.encode(strimei,"UTF-8") +
						"/GetAndroidLoginData3?&a=" + URLEncoder.encode(strimei,"UTF-8") +
						"&b=" + URLEncoder.encode(strloginid,"UTF-8") +
    					"&c=" + URLEncoder.encode(strpassword,"UTF-8") + 
    					"&d=" + URLEncoder.encode(theKey,"UTF-8") + 
    					"&e=" + URLEncoder.encode(lastinsertprospectform,"UTF-8") + 
    					"&f=" + URLEncoder.encode(lastinsertsurveyform,"UTF-8") +
    					"&g=" + URLEncoder.encode(lastinsertcollectionform,"UTF-8") + 
    					"&h=" + URLEncoder.encode(lastinsertasset,"UTF-8")+ 
    					"&k=" + URLEncoder.encode(lastinsertsupplier,"UTF-8")+
    					"&l=" + URLEncoder.encode(lastinsertcollectionplanform,"UTF-8")+
    					"&m=" + URLEncoder.encode(lastinsertsurveylocform,"UTF-8")+
						"&n=" + URLEncoder.encode(lastinsertzipcode,"UTF-8")+
						"&o=" + URLEncoder.encode(lastinsertproduct,"UTF-8")+
						"&p=" + URLEncoder.encode(lastinsertproductoffering,"UTF-8")+
						"&q=" + URLEncoder.encode(lastinserttempatlahir,"UTF-8")+
						"&r=" + URLEncoder.encode(lastinsertsales,"UTF-8"));
				Log.d(TAG, "doInBackground: url "+ url);


				Log.d(TAG, "doInBackground: cek urlEncoder"+
						URLEncoder.encode(strimei,"UTF-8") +
						"&b=" + URLEncoder.encode(strloginid,"UTF-8") +
						"&c=" + URLEncoder.encode(strpassword,"UTF-8") +
						"&d=" + URLEncoder.encode(theKey,"UTF-8") +
						"&e=" + URLEncoder.encode(lastinsertprospectform,"UTF-8") +
						"&f=" + URLEncoder.encode(lastinsertsurveyform,"UTF-8") +
						"&g=" + URLEncoder.encode(lastinsertcollectionform,"UTF-8") +
						"&h=" + URLEncoder.encode(lastinsertasset,"UTF-8")+
						"&k=" + URLEncoder.encode(lastinsertsupplier,"UTF-8")+
						"&l=" + URLEncoder.encode(lastinsertcollectionplanform,"UTF-8")+
						"&m=" + URLEncoder.encode(lastinsertsurveylocform,"UTF-8")+
						"&n=" + URLEncoder.encode(lastinsertzipcode,"UTF-8")+
						"&o=" + URLEncoder.encode(lastinsertproduct,"UTF-8")+
						"&p=" + URLEncoder.encode(lastinsertproductoffering,"UTF-8")+
						"&q=" + URLEncoder.encode(lastinserttempatlahir,"UTF-8")+
						"&r=" + URLEncoder.encode(lastinsertsales,"UTF-8"));

				URLConnection conn = url.openConnection();
    		    
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
    			
    			isAuthorized = map.getNamedItem("IsAuthorized").getNodeValue().trim();
    			isLoadProspectForm = map.getNamedItem("IsLoadProspectForm").getNodeValue().trim();
    			isLoadSurveyForm = map.getNamedItem("IsLoadSurveyForm").getNodeValue().trim();
    			isLoadCollectionForm = map.getNamedItem("IsLoadCollectionForm").getNodeValue().trim();
    			isLoadAsset = map.getNamedItem("IsLoadAsset").getNodeValue().trim();
    			isLoadSupplier = map.getNamedItem("IsLoadSupplier").getNodeValue().trim();
    			isLoadCollectionPlanForm = map.getNamedItem("IsLoadCollectionPlanForm").getNodeValue().trim();
    			isLoadSurveyLocForm = map.getNamedItem("IsLoadSurveyLocForm").getNodeValue().trim();
				isLoadZipCode = map.getNamedItem("isLoadZipCode").getNodeValue().trim();
				isLoadProduct = map.getNamedItem("isLoadProduct").getNodeValue().trim(); //tambahan
				isLoadProductOffering = map.getNamedItem("isLoadProductOffering").getNodeValue().trim(); //tambahan
				isLoadTempatLahir = map.getNamedItem("isLoadTempatLahir").getNodeValue().trim(); //tambahan
				isLoadSales = map.getNamedItem("isLoadSales").getNodeValue().trim(); //tambahan
    			    			
    			errMessage = map.getNamedItem("ErrMessage").getNodeValue().trim();
    			timeOut = map.getNamedItem("TimeOutinMinutes").getNodeValue().trim();
    			branchID = map.getNamedItem("BranchID").getNodeValue().trim();
    			branchName = map.getNamedItem("BranchName").getNodeValue().trim();
    			branchAddress = map.getNamedItem("BranchAddress").getNodeValue().trim();
    			branchPhone = map.getNamedItem("BranchPhone").getNodeValue().trim();
    			isSingleBranch = Boolean.valueOf(map.getNamedItem("IsSingleBranch").getNodeValue().trim());
    			//newVersion= map.getNamedItem("NewVersion").getNodeValue().trim();
    			authorizedMenu = map.getNamedItem("AuthorizedMenu").getNodeValue().trim();
    			userID = map.getNamedItem("UserID").getNodeValue().trim();
    			//WebURLAddress = map.getNamedItem("WebURLAddress").getNodeValue().trim();
    			passwordExpiredFlag = map.getNamedItem("PasswordExpiredFlag").getNodeValue().trim();
    			locationInterval = Long.valueOf(map.getNamedItem("LocationInterval").getNodeValue().trim());
    			kodeBank = map.getNamedItem("KodeBank").getNodeValue().trim();
    			noRek = map.getNamedItem("NoRek").getNodeValue().trim();
				tglserver = map.getNamedItem("tglserver").getNodeValue().trim();


				Log.d(TAG, "doInBackground: yang kedua "
						+isAuthorized
						+ isLoadProspectForm
						+ isLoadSurveyForm
						+ isLoadCollectionForm
						+ isLoadAsset
						+ isLoadCollectionPlanForm
						+ isLoadSurveyLocForm
						+ isLoadZipCode
						+ isLoadProduct
						+ isLoadTempatLahir
						+ isLoadSales
						+ isLoadProductOffering
						+ errMessage
						+ timeOut
						+ branchID
						+ branchName
						+ branchAddress
						+ branchPhone
						+ isSingleBranch
						//+ newVersion
						+ authorizedMenu
						+ userID
						//+ WebURLAddress
						+ passwordExpiredFlag
						+ locationInterval
						+ kodeBank
						+ noRek
						+ tglserver);

				db = null;
    			doc = null;
		            			    			
    		} catch (Exception e) {
    			Log.e("Login","GetLoginData : " + e.getMessage());
    			e.printStackTrace();
    			LoginActivity.this.e = e;
    		} finally{
        		cc.close();
        		db.close();
        	}   		
    		
			return isAuthorized;
		};	
    	
		@Override
        protected void onPostExecute(String sResponse) {
        	if (e==null) {

				Log.d(TAG, "onPostExecute: "+ sResponse);

				if (sResponse.equals("1")){
        			
        			Calendar c = Calendar.getInstance();
            		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
                    String myTime = df.format(c.getTime());
                    
        			generalPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
              	   
             	   	SharedPreferences.Editor prefsEditor = generalPrefs.edit();		        
    		        prefsEditor.putString("timeOut", timeOut);
    		        prefsEditor.putString("branchID", branchID);
    		        prefsEditor.putString("branchName", branchName);
    		        prefsEditor.putString("branchAddress", branchAddress);
    		        prefsEditor.putString("branchPhone", branchPhone);
    		        prefsEditor.putString("authorizedMenu", authorizedMenu);
    		        prefsEditor.putString("userID", userID);
    		        prefsEditor.putString("isLoadProspectForm", isLoadProspectForm);
    		        prefsEditor.putString("isLoadSurveyForm", isLoadSurveyForm);
    		        prefsEditor.putString("isLoadCollectionForm", isLoadCollectionForm);
    		        prefsEditor.putString("isLoadAsset", isLoadAsset);
    		        prefsEditor.putString("isLoadSupplier", isLoadSupplier);
    		        prefsEditor.putString("isLoadCollectionPlanForm", isLoadCollectionPlanForm);
    		        prefsEditor.putString("isLoadSurveyLocForm", isLoadSurveyLocForm);
					prefsEditor.putString("isLoadZipCode", isLoadZipCode);
					prefsEditor.putString("isLoadProduct", isLoadProduct);
					prefsEditor.putString("isLoadProductOffering", isLoadProductOffering);
					prefsEditor.putString("isLoadTempatLahir", isLoadTempatLahir);
					prefsEditor.putString("isLoadSales", isLoadSales);

    		        //prefsEditor.putString("webURLAddress", WebURLAddress);
    		        prefsEditor.putString("sessionStart", myTime);
    		        prefsEditor.putString("moduleid", "0");
    		        prefsEditor.putBoolean("finish", false);
    		        prefsEditor.putLong("locationInterval", locationInterval);
    		        prefsEditor.putString("kodeBank", kodeBank);
    		        prefsEditor.putString("noRek", noRek);
    		        prefsEditor.putString("username", datalocal.encryptlocal(entry.getText().toString().trim(),mContext));
    		        prefsEditor.putString("oldpassword", datalocal.encryptlocal(password.getText().toString().trim(),mContext));
					prefsEditor.putString("tglserver", tglserver);
    		        prefsEditor.commit();




					Log.d(TAG, "onPostBro: "
							+"\n"+""+ datalocal.encryptlocal(entry.getText().toString().trim(),mContext) //username
							+"\n"+""+ datalocal.encryptlocal(password.getText().toString().trim(),mContext)//password
							+"\n"+"myTime"+ myTime
							+"\n"+"isLoadProspectForm"+ isLoadProspectForm
							+"\n"+"isLoadSurveyForm"+ isLoadSurveyForm
							+"\n"+isLoadCollectionForm+ isLoadCollectionForm
							+"\n"+isLoadAsset+ isLoadAsset
							+"\n"+"isLoadCollectionPlanForm"+ isLoadCollectionPlanForm
							+"\n"+"isLoadSurveyLocForm"+ isLoadSurveyLocForm
							+"\n"+"isLoadZipCode"+ isLoadZipCode
							+"\n"+"isLoadProduct"+ isLoadProduct
							+"\n"+"isLoadProductOffering"+ isLoadProductOffering
							+"\n"+"isLoadTempatLahir"+ isLoadTempatLahir
							+"\n"+"isLoadSales"+ isLoadSales
							+"\n"+"errMessage" + errMessage
							+"\n"+"timeOut" + timeOut
							+"\n"+"branchID" + branchID
							+"\n"+"locationInterval" + locationInterval
							+"\n"+"branchName" + branchName
							+"\n"+"branchAddress" + branchAddress
							+"\n"+"branchPhone" + branchPhone
							+"\n"+"isSingleBranch" + isSingleBranch
							//+"\n"+"newVersion" + newVersion
							+"\n"+"authorizedMenu" + authorizedMenu
							+"\n"+"userID" + userID
							+"\n"+"WebURLAddress" + WebURLAddress
							+"\n"+"passwordExpiredFlag" + passwordExpiredFlag
							+"\n"+"locationInterval" + locationInterval
							+"\n"+"kodeBank" + kodeBank
							+"\n"+"noRek" + noRek
							+"\n"+"tglserver" + tglserver);
    				try
    				{
    				    PackageManager manager = mContext.getPackageManager();
    				    PackageInfo info = manager.getPackageInfo("index.finance.faith", 0);
    				    //int code = info.versionCode;
    				    final String versionName = info.versionName;
//
//    				    if (!versionName.equals(newVersion)) {
//    				    	dialog.dismiss();

							if (!versionName.equals(versionName)) {
								dialog.dismiss();

    				    	builder.setIcon(android.R.drawable.ic_dialog_alert);
    				    	builder.setTitle("Alert");
    						builder.setMessage("Please update app to latest version");
    						builder.setCancelable(false);
    						
    						builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
    							public void onClick(DialogInterface dlg, int sumthin) {
    								if (!updateCheck){
    									Intent intent = new Intent(Intent.ACTION_VIEW, 
										Uri.parse(WebURLAddress));
										startActivity(intent);
    									updateCheck = true;
    								}
    								
    								WelcomeActivity.startedApp = false;
    								dlg.dismiss();
    						    	return;
    							}
    						});
    						
    						builder.show();
    						return;
    				    }					    
    				}
    				catch(NameNotFoundException nnf)
    				{
    				    nnf.printStackTrace();
    				}
    				    				
    				if (passwordExpiredFlag.equals("x")) {
    					
    					Intent myintent = new Intent(mContext,ChangePasswordActivity.class);
    					myintent.putExtra("oldpassword", generalPrefs.getString("oldpassword", null));
    					myintent.putExtra("username", generalPrefs.getString("username", null));
    					myintent.putExtra("classname", mContext.getClass().getName());
    					startActivity(myintent);
    					
    					Toast.makeText(LoginActivity.this, "You must change your password", Toast.LENGTH_LONG).show();
						finish();
						return;
					}
    				
    				updateCheck = false;    				
    				startService(new Intent(mContext,DataService.class));
    				RegisterWithGCM();
    				    				    				
    				if(isSingleBranch){
    					finish();
    					Intent myintent = new Intent(mContext,HomeActivity.class);
    					startActivity(myintent);
    				}else{
    					entry.setVisibility(View.GONE);
    			    	password.setVisibility(View.GONE);
    			    	branchSpinner.setVisibility(View.VISIBLE);
    			    	tvBranch.setVisibility(View.VISIBLE);
    			    	
    			    	//value select one = "-"
    			    	branchName = "Select One|" + branchName;
    			    	branchID = "-|" + branchID;
    			    	
    			    	String[]optsName = branchName.split("\\|");
    			    	String[]optsID = branchID.split("\\|");
    			    	    			    	
    			    	for (int i = 0; i < optsName.length; i++) {
    				    	SearchResults sr1 = new SearchResults();
    				        sr1.setName(optsName[i]);
    				        sr1.setID(optsID[i]);
    				        results.add(sr1);
    				    }
    			    	
    			    	aa = new BranchAdapter(mContext, results);    			    	    			    	 
    			    	branchSpinner.setAdapter(aa);    											
    					aa.notifyDataSetChanged();
    					itemBranchID = "-";
    					
    					branchSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    					    @Override
    					    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    					    	SearchResults result = (SearchResults) parent.getItemAtPosition(pos);
    					    	itemBranchID = result.getID();
    					    	itemBranchName = result.getName();
    				        }

    					    @Override
    					    public void onNothingSelected(AdapterView<?> parentView) {
    					        
    					    }

    					});
       					
    					flag = "Multiple Branch";
    					
    				}    				
    							
    			  }
    			  else{
    				  	entry.setText("");
    				  	password.setText("");
    				  	entry.requestFocus();
    				  	
    				  	if (generalPrefs != null){
    	         		   generalPrefs.edit().clear().commit();
    	         	   	}
    				  
    				  	builder.setIcon(android.R.drawable.ic_dialog_alert);
    				    builder.setTitle("Alert");
    					builder.setMessage(errMessage.trim());
    					builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
    						public void onClick(DialogInterface dlg, int sumthin) {
    							dlg.dismiss();
    						}
    					})
    					.show();    					
    		      }
        	}else {
        		Toast.makeText(mContext, "Connection time out", Toast.LENGTH_SHORT).show();
        		e = null;
        	}
        	
        	dialog.dismiss();
        }    
    }
    
    public class SearchResults {
	   	 private String name = "";
	   	 private String id = "";
	   	 
	   	 public void setName(String name) {
	   		 this.name = name;
	   	 }
	
	   	 public String getName() {
	   		 return name;
	   	 }
	
	   	 public void setID(String id) {
	   		 this.id = id;
	   	 }
	
	   	 public String getID() {
	   		 return id;
	   	 }
	   	 
	}
    
    private void RegisterWithGCM()
    {
		refreshToken = FirebaseInstanceId.getInstance().getToken();
		Log.d(TAG, "RegisterWithGCM: "+ refreshToken);

		if (refreshToken == null || refreshToken.equals("")) {
			Toast.makeText(mContext,"Tokennya KOSONG",Toast.LENGTH_SHORT).show();
        }else {
        	SharedPreferences.Editor prefsEditor = generalPrefs.edit();
            prefsEditor.putString("registrationID", refreshToken).commit();
			Log.d(TAG, "RegisterWithGCM: "+ refreshToken);
        }

//		GCMRegistrar.checkDevice(this);
//    	GCMRegistrar.checkManifest(this);
//
//        final String regId = GCMRegistrar.getRegistrationId(mContext);
//
//        Log.v("regId", regId);
//
//        if (regId.equals("")) {
//        	GCMRegistrar.register(mContext, getString(R.string.SenderId));
//        }else {
//        	SharedPreferences.Editor prefsEditor = generalPrefs.edit();
//            prefsEditor.putString("registrationID", regId).commit();
//        }

    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	
    	if (dialog != null){
    		if(dialog.isShowing()){
        		dialog.dismiss();
        	}
    	}   
   	}
    
	@Override
	public void onDestroy() {
    	super.onDestroy();
    	    	
    	if (datasource != null){
    		datasource.close();
    	}   
    	
    	if (db != null){
    		if (db.isOpen()){
        		db.close();
        	}
    	}    	
    	
    	if (dialog != null){
    		if(dialog.isShowing()){
        		dialog.dismiss();
        	}
    	}
    	
    	if (aa != null){
    		aa.notifyDataSetInvalidated();
    		aa = null;
    	}    	
    	
    	ImageLoader imgLoader = new ImageLoader(mContext);
    	    	
    	if (imgLoader != null){
    		imgLoader.clearCache();
    		imgLoader = null;
    	}
    	
	}
}

