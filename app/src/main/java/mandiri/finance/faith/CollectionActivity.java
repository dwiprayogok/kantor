package mandiri.finance.faith;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.http.NameValuePair;
import org.jpos.iso.ISOUtil;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

//import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
//import com.google.android.gms.vision.barcode.Barcode;
//import com.google.android.gms.vision.text.Line;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost.TabSpec;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mandiri.finance.faith.Interface.GPSTracker;
import mandiri.finance.faith.Interface.Utility;
import mandiri.finance.faith.Model.Model_Customer;
import mandiri.finance.faith.Module.DirectionFinder;
import mandiri.finance.faith.Module.DirectionFinderListener;
import mandiri.finance.faith.Module.Route;


public class CollectionActivity extends DashBoardActivity implements com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback,DirectionFinderListener {
	private String contractNo;
	private String custName;
	private String custNamePrint;
	private String custAddress;
	private String dueDate;
	private String taskDate;
	private String insNo;
	private String insAmount;
	private String inOverdue;

	private String latiaja;
	private String longiaja;
	private String collInitial = "";
	private String receiptNo = "";
	private String licensePlate = "";

	private String branchName = "";
	private String branchAddress = "";
	private String branchPhone = "";

	private Context mContext;

	private TabHost tabHost = null;
	private LazyAdapterImage adapter;
	private Exception e = null;

	private ArrayList<String> label = new ArrayList<String>();
	private ArrayList<String> info = new ArrayList<String>();

	private DataSource datasource;
	private SQLiteDatabase db;
	private Form theForm;
	private String addressText = "";

	private Button btnCheckIn;
	private Button btnMyLocation;


	private LocationManager locationManager;
	boolean gps_enabled = false;
	boolean network_enabled = false;

	private String latLongString = "";
	private String latString = "";
	private String lngString = "";

	private SharedPreferences generalPrefs;
	private boolean flag = false;
	private Dialog dialog;
	private SharedPreferences.Editor prefsEditor;
	private boolean isMapImageValid = false;
	private RadioGroup radiogroup;

	private ImageView gambarfoto;
	private ImageView hasilfoto;
	private GoogleMap googleMap;
	private GoogleMap Nmap;
	private MapFragment mapFragment;
	private String moduleid;
	ServerSocket serverSocket;
	private WakeLock wl;
	private static final int CAMERA_REQUEST = 1888;
	private static final int SELECT_PICTURE = 0;
	Uri selectedImageUri;
	private String selectedImagePath;
	static final LatLng Menteng = new LatLng(-6.19772, 106.8233113);
	Location currentLocation = new Location("current");
	Marker cloc;
	Marker dloc;
	LocationRequest locationRequest;
	private GoogleApiClient googleApiClient;
	private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
	private String userchoosen;
	GPSTracker gps;
	Double clongitude;
	Double clatitude;
	String TAG = "CollectionActivity";
	private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
	private String alamat_customer = "", kota = "", state = "", negara = "";
	private List<Marker> originMarkers = new ArrayList<>();
	private List<Marker> destinationMarkers = new ArrayList<>();
	private List<Polyline> polylinePaths = new ArrayList<>();
	private ProgressDialog progressDialog;
	private String originText = "";
	LinearLayout linearLayout;
	private String tableImageCollection;
	Double Lat_Customer;
	Double Lon_customer;
	private FloatingActionButton btnGetDirection;
	private RelativeLayout relative;
	String baru;
	String strTotal = "0";
	double total = 0.0;
	String fieldValue = "";
	private DataLocal datalocal;
	private String userID;
	private String paidBy;
	private String tableName;
	private Button submit;
	private String lasttransaction="";
	private String tanggalbayar="";
	private String tanggalbayar2="";
	private Uri imageUri;
	private boolean deleteFlag5 = false;
	private int buttonId5;
	ImageButton imageButton;
	private static int Action_Code = 0;
	private int flag2;
	private Intent datax;
	private String image_str = "", latinya2 = "", longinya2 = "";
	private String image_str_coll = "";
	private boolean isgambarValid = false;
	private String status = "";
	private Dialog dialog2;
	int satu = 1;
	int dua = 2;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = CollectionActivity.this;
		datalocal = new DataLocal();
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		wl.acquire();

		generalPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		prefsEditor = generalPrefs.edit();
		dialog = new Dialog(mContext, R.style.lightbox_dialog);
		dialog.setContentView(R.layout.lightbox_dialog);
		((TextView) dialog.findViewById(R.id.dialogText)).setText("Loading...");
		dialog.setCancelable(false);


		try {
			String sessionStart;
			String now;
			String timeOut;
			long diffInMin = 0;

			Calendar c = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
			now = df.format(c.getTime());

			timeOut = generalPrefs.getString("timeOut", null);
			sessionStart = generalPrefs.getString("sessionStart", null);

			Date dateStart = df.parse(sessionStart);
			Date dateEnd = df.parse(now);

			diffInMin = (dateEnd.getTime() / 60000) - (dateStart.getTime() / 60000);

			if ((int) diffInMin > Integer.valueOf(timeOut)
					|| (int) diffInMin < 0) {
				prefsEditor.putBoolean("finish", true).commit();

				finish();
				Intent myintent = new Intent(mContext, HomeActivity.class);
				myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(myintent);
				return;
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		flag = true;
		moduleid = generalPrefs.getString("moduleid", null);
		branchName = generalPrefs.getString("branchName", null);
		branchAddress = generalPrefs.getString("branchAddress", null);
		branchPhone = generalPrefs.getString("branchPhone", null);
		userID = generalPrefs.getString("userID", null);

		datasource = new DataSource(mContext);
		db = datasource.getWritableDatabase();
		tableName = "printcash";


		tableImageCollection = "imagecollection";
		imageButton = (ImageButton) findViewById(R.id.button5);

		Bundle b = getIntent().getExtras();
		contractNo = b.getString("contractNo");
		custName = b.getString("custName");
		custNamePrint = b.getString("custName");
		custAddress = b.getString("custAddress");
		dueDate = b.getString("dueDate");
		taskDate = b.getString("taskDate");
		insNo = b.getString("insNo");
		insAmount = b.getString("insAmount");
		latiaja = b.getString("latitudenya");
		longiaja = b.getString("longitudenya");
		inOverdue = b.getString("OD");


		//ceklatlongcustomer();

		Log.d(TAG, "onCreate: amount " + insAmount);
		Log.d(TAG, "onCreate: duedate " + dueDate);
		Log.d(TAG, "onCreate: no contract: "+contractNo);
		Log.d(TAG, "onCreate: userIDnya berapa : " + userID);



		if (custNamePrint.length() > 18) {
			custNamePrint = custNamePrint.substring(0, 18);
		}

		try{
			if(latiaja.equals("") || longiaja.equals("")){
				Toast.makeText(mContext,"Koordinat Customer Tidak Ada",Toast.LENGTH_LONG).show();
			} else{

				model_customer.setLat(latiaja);
				model_customer.setLon(longiaja);
				Log.d(TAG, "onCreate: adalah "
						+"latiaja : "+latiaja+"\n"
						+"longiaja : " + longiaja);

				Lat_Customer = Double.parseDouble(latiaja);
				Lon_customer = Double.parseDouble(longiaja);

				/*model_customer.setLat(Lat_Customer);
				model_customer.setLon(Lon_customer);*/

				Log.d(TAG, "onCreate: adalah "
						+"Lat_Customer : "+Lat_Customer+"\n"
						+"Lon_Customer : " + Lon_customer);

				//Toast.makeText(mContext,"Koordinat posisi Customer Ada",Toast.LENGTH_LONG).show();
			}



		}catch (Exception e){
			e.printStackTrace();

		}

		setContentView(R.layout.tabs);
		setHeader(getString(R.string.CollectionActivityTitle), true);

		tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();

		new createTabs().execute();


	}

	private class createTabs extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			dialog.show();
		}

		@Override
		protected String doInBackground(Void... arg0) {
			try {

				Cursor c = db.rawQuery(
						"SELECT infolabel, infotext FROM infodetail where moduleid = '" + moduleid +
								"' and idx = '" + contractNo + "'", null);

				try {
					while (c.moveToNext()) {
						label.add(c.getString(0));
						info.add(c.getString(1));
					}
				} finally {
					c.close();
				}

				Cursor cr1 = db.rawQuery("SELECT collInitial, receiptno, licenseplate FROM collectionlist where contractno = '"
						+ contractNo + "' and insno = '" + insNo + "'", null);

				try {
					if (cr1.moveToFirst()) {
						collInitial = cr1.getString(0);
						receiptNo = cr1.getString(1);
						licensePlate = cr1.getString(2);
						//Log.d(TAG, "doInBackground: dapetin dari sqlite"+collInitial+receiptNo+licensePlate);
					}
				} finally {
					cr1.close();
				}

			} catch (Exception e) {
				CollectionActivity.this.e = e;
			} finally {
				db.close();
			}

			return "";
		}

		protected void onPostExecute(String sResponse) {
			if (e == null) {

				TabSpec infoSpec = tabHost.newTabSpec("Info");
				infoSpec.setIndicator("Info", null);
				infoSpec.setContent(new TabHost.TabContentFactory() {
					public View createTabContent(String tag) {
						final float SCALE = getBaseContext().getResources().getDisplayMetrics().density;
						float valueDips = 5.0f;
						int valuePixels = (int) (valueDips * SCALE + 0.5f); // 0.5f for rounding

						ScrollView sv = new ScrollView(mContext);
						LinearLayout panel = new LinearLayout(mContext);
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT);

						panel.setLayoutParams(params);
						panel.setOrientation(android.widget.LinearLayout.VERTICAL);
						panel.setPadding(valuePixels, 0, valuePixels, 0);

						TextView tvHeader = new TextView(mContext);
						tvHeader.setText(custName);
						tvHeader.setLayoutParams(params);
						tvHeader.setTextColor(mContext.getResources().getColor(R.color.label_color));
						tvHeader.setTypeface(null, Typeface.BOLD);
						tvHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
						tvHeader.setSingleLine(true);
						tvHeader.setPadding(0, 10, 0, 0);

						TextView tvContractNo = new TextView(mContext);
						tvContractNo.setText(contractNo);
						tvContractNo.setLayoutParams(params);
						tvContractNo.setTextColor(mContext.getResources().getColor(R.color.label_color));
						tvContractNo.setTypeface(null, Typeface.ITALIC);
						tvContractNo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
						tvContractNo.setSingleLine(true);

						TextView tvAddress = new TextView(mContext);
						tvAddress.setText(custAddress);
						tvAddress.setLayoutParams(params);
						tvAddress.setTextColor(mContext.getResources().getColor(R.color.label_color));
						tvAddress.setTypeface(null, Typeface.ITALIC);
						tvAddress.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
						tvAddress.setMaxLines(2);
						tvAddress.setPadding(0, 0, 0, 10);

						panel.addView(tvHeader);
						panel.addView(tvContractNo);
						panel.addView(tvAddress);

						TableLayout tl = new TableLayout(mContext);
						tl.setLayoutParams(params);
						TableRow row;

						//int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
						//		(float) 1, getResources().getDisplayMetrics());

						for (int i = 0; i < label.size(); i++) {
							row = new TableRow(mContext);

							TextView t1 = new TextView(mContext);
							t1.setTextColor(getResources().getColor(R.color.label_color));
							TextView t2 = new TextView(mContext);
							t2.setTextColor(getResources().getColor(R.color.label_color));
							TextView t3 = new TextView(mContext);
							t3.setTextColor(getResources().getColor(R.color.label_color));

							t1.setPadding(0, 0, 0, 3);
							t2.setPadding(1, 0, 1, 3);
							t3.setPadding(0, 0, 0, 3);

							t1.setText(label.get(i));
							t2.setText(" : ");
							t3.setText(info.get(i).trim());

							t1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
							t2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
							t3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

							row.addView(t1);
							row.addView(t2);
							row.addView(t3);

							tl.addView(row, params);
						}

						tl.setPadding(0, 0, 0, 10);
						panel.addView(tl);




						final Button btnPrint = new Button(mContext, null, android.R.attr.buttonStyleSmall);
						btnPrint.setText("Print");
						btnPrint.setLayoutParams(params);
						btnPrint.setVisibility(View.GONE);

						btnPrint.setOnClickListener(new Button.OnClickListener() {
							public void onClick(View v) {

								WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
								Method[] wmMethods = wifi.getClass().getDeclaredMethods();
								for (Method method : wmMethods) {
									if (method.getName().equals("isWifiApEnabled")) {
										try {

											if ((Boolean) method.invoke(wifi)) {
												new PrintInfo().execute();
											} else {
												Intent tetherSettings = new Intent();
												tetherSettings.setClassName("com.android.settings", "com.android.settings.TetherSettings");
												startActivity(tetherSettings);

												Toast.makeText(mContext, "Please turn on portable Wi-Fi hotspot", Toast.LENGTH_LONG).show();
												return;
											}

										} catch (IllegalArgumentException e) {
											e.printStackTrace();
										} catch (IllegalAccessException e) {
											e.printStackTrace();
										} catch (InvocationTargetException e) {
											e.printStackTrace();
										}
									}
								}

							}
						});


						panel.addView(btnPrint);

						sv.addView(panel);

						return sv;
					}
				});

				// Tab for Map
				TabSpec mapSpec = tabHost.newTabSpec("Map");
				mapSpec.setIndicator("Map", null);
				mapSpec.setContent(new TabHost.TabContentFactory() {
					public View createTabContent(String tag) {
						final RelativeLayout panel = new RelativeLayout(mContext);
						RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT);

						panel.setLayoutParams(params);

						//loadinfo();

						View view = View.inflate(mContext, R.layout.map, null);
						panel.addView(view);
						//CheckGpsStatus();
						connectGmapApiClient();

						btnMyLocation = new Button(mContext, null, android.R.attr.buttonStyleSmall);
						btnMyLocation.setText("My Location");
						btnMyLocation.setLayoutParams(params);
						btnMyLocation.setPadding(0, 0, 0, 0);
						btnMyLocation.setTypeface(null, Typeface.BOLD);
						btnMyLocation.setBackgroundColor(ContextCompat.getColor(mContext,(R.color.md_white_12)));
						btnMyLocation.setTextColor(getResources().getColor(R.color.white));
						btnMyLocation.setVisibility(View.VISIBLE);
						btnMyLocation.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {

								currentlocation();
								//moveMap();
							}
						});
						//btnMyLocation.setOnClickListener(myLocationOnClickListener);
						((ViewGroup) view).addView(btnMyLocation);

						btnCheckIn = new Button(mContext, null, android.R.attr.buttonStyleSmall);
						btnCheckIn.setText("Check In");
						btnCheckIn.setLayoutParams(params);
						btnCheckIn.setPadding(0, 0, 0, 0);
						btnCheckIn.setTypeface(null, Typeface.BOLD);
						btnCheckIn.setBackgroundColor(ContextCompat.getColor(mContext,(R.color.md_white_12)));
						btnCheckIn.setTextColor(getResources().getColor(R.color.white));
						btnCheckIn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								CheckInPosition();
							}
						});
						//btnCheckIn.setOnClickListener(checkInOnClickListener);
						btnCheckIn.setVisibility(View.GONE);
						((ViewGroup) view).addView(btnCheckIn);

						try {
							initiliazeMap();

						} catch (Exception e) {
							e.printStackTrace();
						}


						return panel;
					}
				});

				// Tab for Form
				TabSpec formSpec = tabHost.newTabSpec("AddData");
				formSpec.setIndicator("Add Data", null);
				formSpec.setContent(new TabHost.TabContentFactory() {
					public View createTabContent(final String tag) {
						final float SCALE = getBaseContext().getResources().getDisplayMetrics().density;
						float valueDips = 5.0f;
						int valuePixels = (int) (valueDips * SCALE + 0.5f); // 0.5f for rounding

						ScrollView sv = new ScrollView(mContext);

						final LinearLayout panel = new LinearLayout(mContext);
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT);

						panel.setLayoutParams(params);
						panel.setOrientation(android.widget.LinearLayout.VERTICAL);
						panel.setPadding(valuePixels, 0, valuePixels, 0);

						sv.setBackgroundColor(mContext.getResources().getColor(R.color.all_background));


						loadPhoto();

						TextView tv = new TextView(mContext);
						tv.setTextColor(getResources().getColor(R.color.label_color));

						tv.setPadding(5, 5, 0, 0);
						tv.setText("Final Result");
						panel.addView(tv);


						TextView tv2 = new TextView(mContext);
						tv2.setTextColor(getResources().getColor(R.color.label_color));

						tv2.setPadding(10, 10, 0, 0);
						tv2.setText("Take Photo");
						panel.addView(tv2);

						gambarfoto = new ImageView(mContext);
						gambarfoto.setImageResource(R.drawable.gallery);
						gambarfoto.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								startCamera2();
							}
						});
						gambarfoto.setOnLongClickListener(new View.OnLongClickListener() {
							@Override
							public boolean onLongClick(View v) {
								AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
								builder.setIcon(android.R.drawable.ic_dialog_info);
								builder.setTitle("Information");
								builder.setMessage("Apakah Anda Ingin Menghapus Fotonya ?")
										.setCancelable(false)
										.setNegativeButton("No", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												dialog.dismiss();
											}
										})
										.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
											public void onClick(final DialogInterface dialog, final int id) {
												hapusfoto();
												gambarfoto.setImageBitmap(null);
												gambarfoto.setImageResource(R.drawable.gallery);
												dialog.dismiss();
												Toast.makeText(mContext, "Picture has been removed", Toast.LENGTH_SHORT).show();
												isgambarValid = false;
												return;
											}
										});

								final AlertDialog alert = builder.create();
								alert.show();


								return true;
							}
						});
						panel.addView(gambarfoto);


						//panel.addView(View.inflate(mContext, R.layout.take_photo, null));

						radiogroup = new RadioGroup(mContext);
						radiogroup.setOrientation(RadioGroup.HORIZONTAL);

						RadioButton[] rbtResult = new RadioButton[3];
						rbtResult[0] = new RadioButton(mContext);
						rbtResult[0].setText("Cash ");
						rbtResult[0].setId(0);
						rbtResult[0].setTextColor(getResources().getColor(R.color.label_color));
						radiogroup.addView(rbtResult[0]);

						rbtResult[1] = new RadioButton(mContext);
						rbtResult[1].setText("EDC  ");
						rbtResult[1].setId(satu);
						rbtResult[1].setTextColor(getResources().getColor(R.color.label_color));
						radiogroup.addView(rbtResult[1]);

						rbtResult[2] = new RadioButton(mContext);
						rbtResult[2].setText("Not Paid ");
						rbtResult[2].setId(dua);
						rbtResult[2].setTextColor(getResources().getColor(R.color.label_color));
						radiogroup.addView(rbtResult[2]);

						radiogroup.check(0);

						panel.addView(radiogroup);

						final Button btnNext = new Button(mContext, null, android.R.attr.buttonStyleSmall);
						btnNext.setText("Next");
						btnNext.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
						panel.addView(btnNext);

						btnNext.setOnClickListener(new Button.OnClickListener() {
							public void onClick(View v) {

								disableRadioButton();
								btnNext.setVisibility(View.GONE);

								if (getValue().equals("Cash") || getValue().equals("EDC")) {

									try {

										db = datasource.getWritableDatabase();

										theForm = new Form();

										Cursor c = db.rawQuery("SELECT name, label, type, required, optionsname, " +
												"optionsid, hint, errmsg, maxlength, issearchable FROM form where moduleid = '" + moduleid + "'", null);

										try {
											while (c.moveToNext()) {

												FormField tempField = new FormField();
												tempField.setName(c.getString(0));
												tempField.setLabel(c.getString(1));
												tempField.setType(c.getString(2));

												if (c.getString(3).equals("Y"))
													tempField.setRequired(true);
												else
													tempField.setRequired(false);

												tempField.setOptionsName(c.getString(4));
												tempField.setOptionsID(c.getString(5));
												tempField.setHint(c.getString(6));
												tempField.setErrmsg(c.getString(7));
												tempField.setMaxLength(c.getString(8));

												if (c.getString(9).equals("Y"))
													tempField.setIsSearchable(true);
												else
													tempField.setIsSearchable(false);

												theForm.getFields().add(tempField);
											}
										} finally {
											c.close();
											db.close();
										}

									} catch (Exception e) {
										Log.e(tag, "Error occurred in GetFormDataFromLocal : " + e.getMessage());
										e.printStackTrace();
									}

									int i;
									String initialText;


									for (i = 0; i < theForm.fields.size(); i++) {

										initialText = "";



										if (theForm.fields.elementAt(i).getName().equals("InstallmentAmount"))
											initialText = insAmount;


										if (theForm.fields.elementAt(i).getType().equals("text")) {
											theForm.fields.elementAt(i).obj = new EditBox(mContext, " " + theForm.fields.elementAt(i).getLabel(), initialText, theForm.fields.elementAt(i).getHint(), theForm.fields.elementAt(i).getMaxLength());
											((EditBox) theForm.fields.elementAt(i).obj).makeChar();
											panel.addView((View) theForm.fields.elementAt(i).obj);
										} else if (theForm.fields.elementAt(i).getType().equals("number")) {
											theForm.fields.elementAt(i).obj = new EditBox(mContext, " " + theForm.fields.elementAt(i).getLabel(), initialText, theForm.fields.elementAt(i).getHint(), theForm.fields.elementAt(i).getMaxLength());
											((EditBox) theForm.fields.elementAt(i).obj).makeNumber();
											panel.addView((View) theForm.fields.elementAt(i).obj);
										} else if (theForm.fields.elementAt(i).getType().equals("decimal")) {
											theForm.fields.elementAt(i).obj = new EditBox(mContext, " " + theForm.fields.elementAt(i).getLabel(), initialText, theForm.fields.elementAt(i).getHint(), theForm.fields.elementAt(i).getMaxLength());
											((EditBox) theForm.fields.elementAt(i).obj).makeDecimal();
											panel.addView((View) theForm.fields.elementAt(i).obj);
										} else if (theForm.fields.elementAt(i).getType().equals("multiline")) {
											theForm.fields.elementAt(i).obj = new EditBox(mContext, " " + theForm.fields.elementAt(i).getLabel(), initialText, theForm.fields.elementAt(i).getHint(), theForm.fields.elementAt(i).getMaxLength());
											((EditBox) theForm.fields.elementAt(i).obj).makeMultiLine();
											panel.addView((View) theForm.fields.elementAt(i).obj);
										} else if (theForm.fields.elementAt(i).getType().equals("pickone")) {
											theForm.fields.elementAt(i).obj = new PickOne(mContext, (" " + theForm.fields.elementAt(i).getLabel()), theForm.fields.elementAt(i).getOptionsName(), theForm.fields.elementAt(i).getOptionsID(), theForm.fields.elementAt(i).getIsSearchable());
											panel.addView((View) theForm.fields.elementAt(i).obj);
										} else if (theForm.fields.elementAt(i).getType().equals("pickonechoice")) {
											theForm.fields.elementAt(i).obj = new PickOneChoice(mContext, (" " + theForm.fields.elementAt(i).getLabel()));
											panel.addView((View) theForm.fields.elementAt(i).obj);
										} else if (theForm.fields.elementAt(i).getType().equals("radiobutton")) {
											theForm.fields.elementAt(i).obj = new RadioBtn(mContext, (" " + theForm.fields.elementAt(i).getLabel()), theForm.fields.elementAt(i).getOptionsName(), theForm.fields.elementAt(i).getOptionsID());
											panel.addView((View) theForm.fields.elementAt(i).obj);
										} else if (theForm.fields.elementAt(i).getType().equals("date")) {
											theForm.fields.elementAt(i).obj = new PickDate(mContext, (" " + theForm.fields.elementAt(i).getLabel()));
											panel.addView((View) theForm.fields.elementAt(i).obj);
										} else if (theForm.fields.elementAt(i).getType().equals("pickonesupplier")) {
											theForm.fields.elementAt(i).obj = new PickOneSupplier(mContext, (" " + theForm.fields.elementAt(i).getLabel()), "supplier", "suppliername", "supplierid");
											panel.addView((View) theForm.fields.elementAt(i).obj);
										}
									}

									/*Button btn = new Button(mContext, null, android.R.attr.buttonStyleSmall);
									btn.setText("Submit");
									btn.setLayoutParams(new LayoutParams(
											ViewGroup.LayoutParams.MATCH_PARENT,
											ViewGroup.LayoutParams.WRAP_CONTENT));*/

									submit = new Button(mContext, null, android.R.attr.buttonStyleSmall);
									submit.setText("Submit");
									submit.setLayoutParams(new LayoutParams(
											ViewGroup.LayoutParams.MATCH_PARENT,
											ViewGroup.LayoutParams.WRAP_CONTENT));



									submit.setOnClickListener(new Button.OnClickListener() {
										public void onClick(View v) {
											boolean check;
											boolean cekgambar;
											boolean cekgambar2;


											locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

											if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
												buildLocationAlert();
												return;
											}



											if (isMapImageValid == false) {
												Toast.makeText(mContext, "Please check in first", Toast.LENGTH_SHORT).show();
												return;
											}




											if (isgambarValid == false){
												AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
												builder.setIcon(android.R.drawable.ic_dialog_info);
												builder.setTitle("Information");
												builder.setMessage(R.string.information2)
														.setCancelable(false)
														.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
															public void onClick(final DialogInterface dialog, final int id) {
																dialog.dismiss();
																return;
															}
														});

												final AlertDialog alert = builder.create();
												alert.show();
												return;
											}


											cekgambar = checkgambar2();
											check = CheckForm();


											if (!cekgambar) {
												Toast.makeText(mContext, "Take Photo FIRST !!! ", Toast.LENGTH_SHORT).show();
												return;
											}

											if (!check) {
												Toast.makeText(mContext, "Enter all required (!) fields", Toast.LENGTH_SHORT).show();
												return;
											}

											//buildAlertSave();

											WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
											Method[] wmMethods = wifi.getClass().getDeclaredMethods();
											for (Method method : wmMethods) {
												if (method.getName().equals("isWifiApEnabled")) {
													try {

														if ((Boolean) method.invoke(wifi)) {
															buildAlertSave();
														} else {
															Intent tetherSettings = new Intent();
															tetherSettings.setClassName("com.android.settings", "com.android.settings.TetherSettings");
															startActivity(tetherSettings);
															Toast.makeText(mContext, "Please turn on portable Wi-Fi hotspot", Toast.LENGTH_LONG).show();
															return;
														}

													} catch (IllegalArgumentException e) {
														e.printStackTrace();
													} catch (IllegalAccessException e) {
														e.printStackTrace();
													} catch (InvocationTargetException e) {
														e.printStackTrace();
													}
												}
											}


										}
									});
									panel.addView(submit);
									//panel.addView(btn);
								} else {
									try {
										//not paid

										theForm = new Form();

										db = datasource.getWritableDatabase();

										//moduleid = 99
										Cursor c = db.rawQuery("SELECT name, label, type, required, " +
												"optionsname, optionsid, hint, errmsg, maxlength, issearchable FROM form where moduleid = '99'", null);

										try {
											while (c.moveToNext()) {

												FormField tempField = new FormField();
												tempField.setName(c.getString(0));
												tempField.setLabel(c.getString(1));
												tempField.setType(c.getString(2));

												if (c.getString(3).equals("Y"))
													tempField.setRequired(true);
												else
													tempField.setRequired(false);

												tempField.setOptionsName(c.getString(4));
												tempField.setOptionsID(c.getString(5));
												tempField.setHint(c.getString(6));
												tempField.setErrmsg(c.getString(7));
												tempField.setMaxLength(c.getString(8));

												if (c.getString(9).equals("Y"))
													tempField.setIsSearchable(true);
												else
													tempField.setIsSearchable(false);

												theForm.getFields().add(tempField);
											}
										} finally {
											c.close();
											db.close();
										}

									} catch (Exception e) {
										Log.e(tag, "Error occurred in GetFormDataFromLocal : " + e.getMessage());
										e.printStackTrace();
									}

									int i;

									for (i = 0; i < theForm.fields.size(); i++) {

										if (theForm.fields.elementAt(i).getType().equals("text")) {
											theForm.fields.elementAt(i).obj = new EditBox(mContext, " " + theForm.fields.elementAt(i).getLabel(), "", theForm.fields.elementAt(i).getHint(), theForm.fields.elementAt(i).getMaxLength());
											((EditBox) theForm.fields.elementAt(i).obj).makeChar();
											panel.addView((View) theForm.fields.elementAt(i).obj);
										} else if (theForm.fields.elementAt(i).getType().equals("number")) {
											theForm.fields.elementAt(i).obj = new EditBox(mContext, " " + theForm.fields.elementAt(i).getLabel(), "", theForm.fields.elementAt(i).getHint(), theForm.fields.elementAt(i).getMaxLength());
											((EditBox) theForm.fields.elementAt(i).obj).makeNumber();
											panel.addView((View) theForm.fields.elementAt(i).obj);
										} else if (theForm.fields.elementAt(i).getType().equals("decimal")) {
											theForm.fields.elementAt(i).obj = new EditBox(mContext, " " + theForm.fields.elementAt(i).getLabel(), "", theForm.fields.elementAt(i).getHint(), theForm.fields.elementAt(i).getMaxLength());
											((EditBox) theForm.fields.elementAt(i).obj).makeDecimal();
											panel.addView((View) theForm.fields.elementAt(i).obj);
										} else if (theForm.fields.elementAt(i).getType().equals("multiline")) {
											theForm.fields.elementAt(i).obj = new EditBox(mContext, " " + theForm.fields.elementAt(i).getLabel(), "", theForm.fields.elementAt(i).getHint(), theForm.fields.elementAt(i).getMaxLength());
											((EditBox) theForm.fields.elementAt(i).obj).makeMultiLine();
											panel.addView((View) theForm.fields.elementAt(i).obj);
										} else if (theForm.fields.elementAt(i).getType().equals("pickone")) {
											theForm.fields.elementAt(i).obj = new PickOne(mContext, (" " + theForm.fields.elementAt(i).getLabel()), theForm.fields.elementAt(i).getOptionsName(), theForm.fields.elementAt(i).getOptionsID(), theForm.fields.elementAt(i).getIsSearchable());
											panel.addView((View) theForm.fields.elementAt(i).obj);
										} else if (theForm.fields.elementAt(i).getType().equals("pickonechoice")) {
											theForm.fields.elementAt(i).obj = new PickOneChoice(mContext, (" " + theForm.fields.elementAt(i).getLabel()));
											panel.addView((View) theForm.fields.elementAt(i).obj);
										} else if (theForm.fields.elementAt(i).getType().equals("radiobutton")) {
											theForm.fields.elementAt(i).obj = new RadioBtn(mContext, (" " + theForm.fields.elementAt(i).getLabel()), theForm.fields.elementAt(i).getOptionsName(), theForm.fields.elementAt(i).getOptionsID());
											panel.addView((View) theForm.fields.elementAt(i).obj);
										} else if (theForm.fields.elementAt(i).getType().equals("date")) {
											theForm.fields.elementAt(i).obj = new PickDate(mContext, (" " + theForm.fields.elementAt(i).getLabel()));
											panel.addView((View) theForm.fields.elementAt(i).obj);
										} else if (theForm.fields.elementAt(i).getType().equals("pickonesupplier")) {
											theForm.fields.elementAt(i).obj = new PickOneSupplier(mContext, (" " + theForm.fields.elementAt(i).getLabel()), "supplier", "suppliername", "supplierid");
											panel.addView((View) theForm.fields.elementAt(i).obj);
										}
									}

									Button btn = new Button(mContext, null, android.R.attr.buttonStyleSmall);
									btn.setText("Submit");
									btn.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

									btn.setOnClickListener(new Button.OnClickListener() {
										public void onClick(View v) {
											boolean check;
											boolean cekgambar;

											locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

											if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
												buildLocationAlert();
												return;
											}

											if (isMapImageValid == false) {
												Toast.makeText(mContext, "Please check in first", Toast.LENGTH_SHORT).show();
												return;
											}




											if (isgambarValid == false){
												AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
												builder.setIcon(android.R.drawable.ic_dialog_info);
												builder.setTitle("Information");
												builder.setMessage(R.string.information2)
														.setCancelable(false)
														.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
															public void onClick(final DialogInterface dialog, final int id) {
																dialog.dismiss();
																return;
															}
														});

												final AlertDialog alert = builder.create();
												alert.show();
												return;
											}


											cekgambar = checkgambar2();

											if (!cekgambar) {
												Toast.makeText(mContext, "Take Photo FIRST !!! ", Toast.LENGTH_SHORT).show();
												return;
											}


											check = CheckForm();



											if (!check) {
												Toast.makeText(mContext, "Enter all required (!) fields", Toast.LENGTH_SHORT).show();
												return;


											} else {
												new PostData2().execute();
											}

										}
									});

									panel.addView(btn);
								}

							}
						});

						sv.addView(panel);
						return sv;
					}
				});

				tabHost.addTab(infoSpec);
				tabHost.addTab(mapSpec);
				tabHost.addTab(formSpec);

				//tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = 30;
				//tabHost.getTabWidget().getChildAt(1).getLayoutParams().height = 30;
				//tabHost.getTabWidget().getChildAt(2).getLayoutParams().height = 30;
				tabHost.getTabWidget().setCurrentTab(0);

				dialog.dismiss();
			} else {
				dialog.dismiss();

				Toast.makeText(mContext, "Connection time out", Toast.LENGTH_SHORT).show();
				finish();
				Intent intent = new Intent(mContext, ListCollectionActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("flagUpdate", "N");
				startActivity(intent);
			}

		}
	}


	///------BAGIAN UNTUK TAB ADD DATA ( TAKE PHOTO ) -----------///////
	private void loadPhoto() {

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
				!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission
		.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
			requestReadPhoneStatePermission();
		} else {
			Toast.makeText(mContext, "Take Photo First...!!!", Toast.LENGTH_SHORT).show();
			//startcamera();
		}
	}
	private void requestReadPhoneStatePermission() {
		if (ActivityCompat.shouldShowRequestPermissionRationale(this,
				android.Manifest.permission.CAMERA)) {
			new AlertDialog.Builder(CollectionActivity.this)
					.setTitle("Permission Request")
					.setMessage("Check permission")
					.setCancelable(false)
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							//re-request
							ActivityCompat.requestPermissions(CollectionActivity.this,
									new String[]{
											android.Manifest.permission.CAMERA,
											android.Manifest.permission.READ_EXTERNAL_STORAGE},
									MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
						}
					})
					.show();
		} else {
			ActivityCompat.requestPermissions(this, new String[]{
					android.Manifest.permission.CAMERA,
					android.Manifest.permission.READ_EXTERNAL_STORAGE},
					MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

		if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
			// Received permission result for READ_PHONE_STATE permission.est.");
			// Check if the only required permission has been granted
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(getApplicationContext(), "Permission IMEI HAVE BEEN GRANTED", Toast.LENGTH_SHORT).show();
				//startcamera();
//				if (userchoosen.equals("Take Photo")) {
//					cameraIntent();
//				} else if (userchoosen.equals("Choose from library")) {
//					galleryintent();
//				}
			}

		} else {
			Toast.makeText(getApplicationContext(), "permission gagal", Toast.LENGTH_SHORT).show();

		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);


//		if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
//			if (requestCode == SELECT_PICTURE) {
//
//				selectedImageUri = data.getData();
//				selectedImagePath = getPath(selectedImageUri);
//				gambarfoto.setTag(selectedImagePath);
//
//				if (!selectedImageUri.toString().toLowerCase()
//						.startsWith("content://media/")) {
//					Toast.makeText(CollectionActivity.this,
//							"Please Select Image From Local Storage",
//							Toast.LENGTH_SHORT).show();
//					Log.d("ambil", "onActivityResult: " + selectedImagePath);
//				} else {
//
//					File folder = new File(
//							Environment.getExternalStorageDirectory()
//									+ File.separator
//									+ CollectionActivity.this
//									.getString(R.string.app_name)
//									+ File.separator + "Collection_Photo");
//
//					Bitmap asli = BitmapFactory.decodeFile(gambarfoto
//							.getTag().toString());
//					Bitmap thumbnail = scaleDown(asli, 256, true);
//
//					ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//					thumbnail.compress(Bitmap.CompressFormat.JPEG, 80,
//							bytes);
//
//
//					Log.d("ambil_2", "onActivityResult: " + selectedImagePath);
//
//
//				}
//
//			}
//			Bitmap photo = (Bitmap) data.getExtras().get("data");
//			gambarfoto.setImageBitmap(photo);
//
//		}

		if (requestCode == CAMERA_REQUEST) {

			if (resultCode == RESULT_OK){
				datax = data;

				if(dialog != null && dialog.isShowing())
					dialog.dismiss();

				new PictureProcessing2().execute();
			}
			else if (resultCode == RESULT_CANCELED) {

				if (flag2 ==0){

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
				if (flag2 ==0){

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


	///--------IMAGE COLLECTION----------------\\\
	private void startCamera2(){
		if(dialog != null && dialog.isShowing())
			dialog.dismiss();

		String fileName = "photoCollection.jpg";
		ContentValues values = new ContentValues();

		values.put(MediaStore.Images.Media.TITLE, fileName);
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

		imageUri = getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

		startActivityForResult(intent, CAMERA_REQUEST);
	}
	void loadcustomer(){
		latinya2 = model_customer.getLat();
		longinya2 = model_customer.getLon();
		Log.d(TAG, "loadcustomer: latlongnya "+latinya2 +"dan"+longinya2);

	}
	private class PictureProcessing2 extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			if(dialog != null && dialog.isShowing())
				dialog.dismiss();

			dialog2 = ProgressDialog.show(mContext, "", "Attach Picture...", true);
		}

		@Override
		protected String doInBackground(Void... params) {
			String result = "";

			try{

				if(flag2 == 0 && imageUri != null) {
					loadcustomer();
					PictureAttach2();
				}
				else if(flag2 != 0) {
					loadcustomer();
					PictureAttach2();

				}
				else
					result = "imageUri = null";


				SystemClock.sleep(2000);



			}catch (Exception e) {
				CollectionActivity.this.e = e;
				result = e.getMessage();
			}

			return result;
		}

		@Override
		protected void onPostExecute(String sResponse) {
			if(dialog2 != null && dialog2.isShowing())
				dialog2.dismiss();

			if (e==null && sResponse.length()==0){

				if(gambarfoto!=null)
					gambarfoto.setImageBitmap(null);



				File cacheDir = getBaseContext().getCacheDir();
				File f = new File(cacheDir, "picCollection");
				FileInputStream fis = null;

				try {
					fis = new FileInputStream(f);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				Bitmap bitmap = BitmapFactory.decodeStream(fis);
				bitmap = Bitmap.createScaledBitmap(bitmap, 350, 350, false);



				if(bitmap!=null && gambarfoto!=null) {
					gambarfoto.setImageBitmap(bitmap);
				}
				isgambarValid = true;
				try {
					fis.close();
					cacheDir.delete();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.gc();
			}else
				Toast.makeText(mContext, sResponse, Toast.LENGTH_SHORT).show();
		}
	}
	private void PictureAttach2(){

		try{
			db = datasource.getWritableDatabase();
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

			if (flag2 ==0){
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



			System.gc();

			Bitmap bitmap2 = null;

			bitmap2 = Bitmap.createBitmap(BitmapFactory.decodeFile(picturePath,o) , 0, 0,
					o.outWidth, o.outHeight, matrix, true);

			bos = new ByteArrayOutputStream();
			bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, bos);

			File cacheDir = getBaseContext().getCacheDir();
			File f = new File(cacheDir, "picCollection");

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
			if (image_str_coll != null){
				image_str_coll = Base64.encodeToString(byt,Base64.DEFAULT);
			}else{
				image_str_coll = "";}


			latinya22 = latinya2;
			longinya22 = longinya2;


			bos.flush();
			bos.close();
			System.gc();

			Cursor cursor = db.rawQuery("SELECT id FROM " + tableImageCollection + " WHERE buttonId = '" + Integer.toString(Action_Code) +"'", null);

			if(cursor.moveToFirst())
			{
				ContentValues cv = new ContentValues();
				cv.put("imageStr", image_str_coll);
				datasource.updateData(db,cv,tableImageCollection," buttonId = '" + Action_Code + "'");
				cv.clear();
			}else{
				ContentValues cv = new ContentValues();

				if (Action_Code == buttonId5){
					deleteFlag5 = true;
				}

				cv.put("imageStr", image_str_coll);
				cv.put("buttonId", Action_Code);
				cv.put("lat", latinya22);
				cv.put("lng", longinya22);
				Log.d(TAG, "PictureAttach2: cek latitudenya customer " + latinya22+" dan longitudenya customer " + longinya22);
				Log.d(TAG, "PictureAttach2: "+image_str_coll);
				datasource.generateData(db,cv,tableImageCollection);

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
	private boolean checkgambar2() {

		try {
			boolean good = true;

			if (isgambarValid == false){
				Toast.makeText(mContext,"Take Picture Please.",Toast.LENGTH_SHORT).show();

			}else if (isgambarValid == true){
				Toast.makeText(mContext,"Picture Have Beed Added.",Toast.LENGTH_SHORT).show();
			}


			return good;
		} catch (Exception e) {
			Log.e("DashBoardActivity", "Error in checkgambar2() : " + e.getMessage());
			e.printStackTrace();
			return false;
		}

	}
	private void hapusfoto(){
		try{
			db = datasource.getWritableDatabase();
			datasource.deleteData(db, " buttonId = '" + Action_Code + "'", tableImageCollection);
			db.close();
		}catch (Exception e){
			e.printStackTrace();
		}

	}
	void cekdatagmabar(){
		db = datasource.getWritableDatabase();
		Cursor cr2 = db.rawQuery("Select * from imagecollection",null);
		int cnt = cr2.getCount();
		Log.d(TAG, "onClick: cek count row1 " + cnt);
		if (cnt==0){
			final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setIcon(android.R.drawable.ic_dialog_info);
			builder.setTitle("Information");
			builder.setMessage(R.string.information2).
					setCancelable(false).setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(final DialogInterface dialog, final int id) {
							dialog.dismiss();
						}
					});
			final AlertDialog alert = builder.create();
			alert.show();
			cr2.close();
		}
	}
///--------IMAGE COLLECTION----------------\\\



	///------BAGIAN UNTUK TAB MAP ( PETA ) -----------///////

	private void loadinfo(){
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("Information");
		builder.setMessage(R.string.information)
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, final int id) {
						dialog.dismiss();
						return;
					}
				});

		final AlertDialog alert = builder.create();
		alert.show();
	}

	private void initiliazeMap() {
		if (mapFragment == null) {
			//googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
			mapFragment.getMapAsync(this);
			if (mapFragment == null) {
				Toast.makeText(getApplicationContext(), "mapnya gak muncul", Toast.LENGTH_SHORT).show();
			}

		}
	}

	@Override
	public void onMapReady(final GoogleMap googleMap) {
		Nmap = googleMap;
		Nmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		CheckGpsStatus();
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
				android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

			return;
		}

		Nmap.setMyLocationEnabled(true);
	}

	public void CheckGpsStatus() {

		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
		locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		//GpsStatus = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (enabled) {
			if (status == ConnectionResult.SUCCESS) {
				//Toast.makeText(getApplicationContext(), "petanya muncul", Toast.LENGTH_SHORT).show();
				//posisicustomer();
				//currentlocation();
				//connectGmapApiClient();
			} else {
				GooglePlayServicesUtil.getErrorDialog(status, this, status);


			}
		} else {

			new SweetAlertDialog(CollectionActivity.this, SweetAlertDialog.WARNING_TYPE)
					.setTitleText("GPS anda tidak aktif")
					.setContentText("Aktipkan GPS Anda Terlebih Dahulu")
					.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
						@Override
						public void onClick(SweetAlertDialog sweetAlertDialog) {

							Intent pindah = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivity(pindah);
							finish();
						}
					}).show();

		}

	}

	private synchronized void connectGmapApiClient() {
		googleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
	}

	void currentlocation() {

		gps = new GPSTracker(mContext);
		isMapImageValid = false;
		handlelocation();

		if (gps.canGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			clatitude = latitude;
			clongitude =longitude;
//			Toast.makeText(mContext,"posisi latitudenya adalah :"+ clatitude + " dan longitudenya adalah "+
//					clongitude,Toast.LENGTH_SHORT).show();

		} else {
			gps.showSettingsAlert();
		}

		tampilketerangan();
		moveMap();

	}

	private void tampilketerangan(){

		btnGetDirection = (FloatingActionButton) findViewById(R.id.btn_direction);
		relative = (RelativeLayout) findViewById(R.id.lay_info);

		if (latiaja.equals("") || longiaja.equals("")){
			Log.d(TAG, "tampilketerangan: longinya kosong");
			Toast.makeText(mContext," Koordinat Customer Tidak Ada",Toast.LENGTH_LONG).show();
			btnGetDirection.setVisibility(View.GONE);
			relative.setVisibility(View.GONE);

		}else {
			btnGetDirection.setVisibility(View.VISIBLE);
			relative.setVisibility(View.VISIBLE);
			btnGetDirection.setEnabled(false);
		}

	}

	private void moveMap() {
		tampilketerangan();
		LatLng latLng = new LatLng(clatitude,clongitude);
		Log.d("cek", "moveMap: " + latLng);
		//Adding marker to map
		MarkerOptions mO = new MarkerOptions();

		mO.position(latLng) //setting position
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
				.title("Posisi Saya");
		cloc = Nmap.addMarker(mO);
		cloc.showInfoWindow();
		Nmap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		Nmap.animateCamera(CameraUpdateFactory.zoomTo(12));




		btnMyLocation.setVisibility(View.GONE);
		btnCheckIn.setVisibility(View.VISIBLE);




	}

	private void CheckInPosition(){

		if (latLongString.length() == 0) {
			Log.d("dawda", "onClick: "+latLongString);
			Toast.makeText(mContext, "Location not found, please wait..", Toast.LENGTH_SHORT).show();
			handlelocation();
		} else {

			isMapImageValid = true;
			btnCheckIn.setVisibility(View.GONE);
//			btnGetDirection.setVisibility(View.VISIBLE);
//			btnGetDirection.setEnabled(true);
//			RelativeLayout relativ = (RelativeLayout) findViewById(R.id.lay_info);
//			relativ.setVisibility(View.VISIBLE);
//			Toast.makeText(mContext,"Tombol Direction Sudah Aktif",Toast.LENGTH_SHORT).show();
//			btnGetDirection.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					//Toast.makeText(mContext,"wadw",Toast.LENGTH_SHORT).show();
//					sendRequest(originText);
//				}
//			});
			if (addressText.length() > 0) {
				Log.d("wadidaw", "onClick: "+addressText);
				originText = addressText;

				Toast.makeText(mContext, "You have check in at : \n" + addressText, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mContext, "You have check in at : \n" + latLongString, Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void posisicustomer() {

		Log.d(TAG, "posisicustomer: "+"Latitudenya adalah : "+ Lat_Customer +" DAN " + " longitudenya adalah : " + Lon_customer);
		Geocoder geocoder = new Geocoder(mContext);
		List<Address> addresses;

		try {

			//addresses = geocoder.getFromLocationName(alamat, 10);
			addresses = geocoder.getFromLocation(Lat_Customer,Lon_customer, 10);

			if (addresses == null) {

				return;
			}
			alamat_customer = addresses.get(0).getAddressLine(0);
			kota = addresses.get(0).getLocality();
			state = addresses.get(0).getAdminArea();
			negara = addresses.get(0).getCountryName();

			Log.d(TAG, "posisicustomer: " + " \n "
					+"alamatnya : "+ alamat_customer
					+"kotanya : "+ kota
					+" statenya : "+ state
					+"negaranya : " +negara);

//			Address location = addresses.get(0);
//			location.getLatitude();
//			location.getLongitude();
//
//
			try{
				model_customer.setAlamat(alamat_customer+kota);

			}catch (Exception e){
				e.printStackTrace();
			}
			LatLng latlong = new LatLng(Lat_Customer, Lon_customer);
//			Toast.makeText(mContext, "posisi customernya"
//							+ " lat dari alamat :" + Lat_Customer
//							+ " long dari alamat :" + Lon_customer
//					, Toast.LENGTH_SHORT).show();

			//MarkerOptions marker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()));
			MarkerOptions marker = new MarkerOptions().position(new LatLng(Lat_Customer,Lon_customer));
			marker.title(alamat_customer+" , "+kota);
			marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
			Nmap.addMarker(marker);
			Nmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong, 12));
			Nmap.getUiSettings().setMapToolbarEnabled(false);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void handlelocation() {

		if (locationManager == null) {
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		}

		latLongString = "";
		latString = "";
		lngString = "";

		//exceptions will be thrown if provider is not permitted.
		try {
			gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}

		try {
			network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		//don't start listeners if no provider is enabled
		if (!gps_enabled && !network_enabled)
			return;

		if (gps_enabled) {
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
					!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
					android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

				return;
			}
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
		} else {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
		}

		Toast.makeText(mContext, "Waiting for location..", Toast.LENGTH_LONG).show();
	}

	LocationListener locationListenerGps = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
			//sendRequest();
			if (ActivityCompat.checkSelfPermission(getApplicationContext(),
					android.Manifest.permission.ACCESS_FINE_LOCATION) !=
					PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
					(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
					PackageManager.PERMISSION_GRANTED) {
			}
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
			//sendRequest();
			if (ActivityCompat.checkSelfPermission(getApplicationContext(),
					android.Manifest.permission.ACCESS_FINE_LOCATION) !=
					PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
					android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				return;
			}
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
		double longtitude = newLocation.getLongitude();

		Geocoder gc = new Geocoder(this, Locale.getDefault());

		latLongString = "Latitude : " + latitude + ", Longtitude : " + longtitude;
		latString = Double.toString(latitude);
		lngString = Double.toString(longtitude);

		List<Address> addresses;

		try {
			addresses = gc.getFromLocation(latitude, longtitude, 1);

		StringBuilder sb = new StringBuilder();
		if (addresses.size() > 0) {

			Address address = addresses.get(0);

			for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
				if (i == 0) {
					sb.append(address.getAddressLine(i));
				} else {
					sb.append("," + address.getAddressLine(i));
				}
			}

			if (sb.toString().length() > 1) {
				addressText = sb.toString();
				originText = addressText;

				String longicuy = longiaja + latiaja;

				if (longicuy.equals("")){
					btnGetDirection.setVisibility(View.GONE);
					Toast.makeText(mContext,"Tombol Direction Tidak Aktif",Toast.LENGTH_SHORT).show();
				}else {
					btnGetDirection.setEnabled(true);
					Toast.makeText(mContext,"Tombol Direction Sudah Aktif",Toast.LENGTH_SHORT).show();
				}

				btnGetDirection.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						posisicustomer();
						sendRequest(originText);
						Log.d(TAG, "onClick: "+originText);
					}
				});


				//btnGetDirection.setEnabled(true);
				//Toast.makeText(mContext,"Tombol Direction Sudah Aktif",Toast.LENGTH_SHORT).show();
				/*String origin = originText;
				Log.d(TAG, "updateWithNewLocation: "+origin);
				String destination = alamat_customer;
				try {
					new DirectionFinder(this, origin, destination).execute();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}*/
			} else {
				addressText = "";
			}
		}
	} catch (IOException e) {
		e.printStackTrace();
	}

	Nmap.clear();

	//posisicustomer();
		//googleMap.clear();

		MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longtitude));
		marker.title(addressText);
		marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

		Nmap.addMarker(marker).showInfoWindow();
		//googleMap.addMarker(marker).showInfoWindow();
	}

	private void sendRequest(String origin2) {
		String origin = origin2;
		Log.d(TAG, "sendRequest: "+origin);
		String destination = alamat_customer+kota+state+negara;

		if (origin ==null && destination ==null){
			progressDialog.dismiss();
		} else {
			try {
				new DirectionFinder(this, origin, destination).execute();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}


	}

	@Override
	public void onDirectionFinderStart() {
		progressDialog = ProgressDialog.show(this, "Please wait.",
				"Finding direction..!", true);
//
//		if (originMarkers != null) {
//			for (Marker marker : originMarkers) {
//				marker.remove();
//			}
//		}
//
//		if (destinationMarkers != null) {
//			for (Marker marker : destinationMarkers) {
//				marker.remove();
//			}
//		}

		if (polylinePaths != null) {
			for (Polyline polyline:polylinePaths ) {
				polyline.remove();
			}
		}
	}

	@Override
	public void onDirectionFinderSearch() {



	}

	@Override
	public void onDirectionFinderSuccess(List<Route> routes) {
		progressDialog.dismiss();
		polylinePaths = new ArrayList<>();
		originMarkers = new ArrayList<>();
		destinationMarkers = new ArrayList<>();

		for (Route route : routes) {
			Nmap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));


				((TextView) findViewById(R.id.text_durasi)).setText("( " + route.duration.text +" )");
				((TextView) findViewById(R.id.text_jarak)).setText(route.distance.text);
				((TextView) findViewById(R.id.text_start)).setText("Melalui : "+route.startAddress);


			PolylineOptions polylineOptions = new PolylineOptions().
					geodesic(true).
					color(Color.BLUE).
					width(7);

			for (int i = 0; i < route.points.size(); i++)
				polylineOptions.add(route.points.get(i));

			polylinePaths.add(Nmap.addPolyline(polylineOptions));
		}
	}

	///------BAGIAN UNTUK TAB MAP ( PETA ) -----------///////

	private boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
			System.out.println(d);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	private void buildLocationAlert() {
		Intent myintent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		mContext.startActivity(myintent);
		Toast.makeText(mContext, "Please turn on location service", Toast.LENGTH_LONG).show();
	}

	private void buildAlertSave() {

		int i;
		NumberFormat formatter = new DecimalFormat("###,###,###");

		for (i = 0; i < theForm.fields.size(); i++) {
			fieldValue = (String) theForm.fields.elementAt(i).getData();
			Log.d(TAG, "buildAlertSave:  fieldValue "+fieldValue);


			if (fieldValue.trim().length() > 0) {
				//total = total + Double.valueOf(fieldValue); //yg lama
				total = total + Double.parseDouble(fieldValue); //editandwi
				Log.d(TAG, "buildAlertSave: total " +total);
			}
		}
		strTotal = formatter.format(total);
		Log.d(TAG, "buildAlertSave: strTotal "+ strTotal);

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("Confirmation");
		builder.setMessage("Total amount to be received is : \n" + "Rp " + strTotal.replace(",", ".") + " are you sure ?")
				.setCancelable(false).setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, final int id) {
						if (getValue().equals("EDC")) {
							new PrintReceiptEDC().execute();
						} else {
							new SubmitkeServer().execute();
						}

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

	private void ceklatlongcustomer(){
		if (latiaja.equals("") || longiaja.equals("")){
			Toast.makeText(mContext,"latitudenya dan longitudenya Customer tidak ada",Toast.LENGTH_LONG).show();
			Log.d(TAG, "onCreate: cek longi "+ longiaja + latiaja);
		}

//		if (longiaja.equals("")){
//			Toast.makeText(mContext,"longitudenya Customer tidak ada",Toast.LENGTH_LONG).show();
//			Log.d(TAG, "onCreate: cek longiaja "+ longiaja);
//		} else if (latiaja.equals("")){
//			Toast.makeText(mContext,"latitudenya Customer tidak ada",Toast.LENGTH_LONG).show();
//			Log.d(TAG, "onCreate: cek latiaja "+ longiaja);
//		}else if (latiaja.equals("") && longiaja.equals("")){
//			Toast.makeText(mContext,"latitudenya dan longitudenya Customer tidak ada",Toast.LENGTH_LONG).show();
//			Log.d(TAG, "onCreate: cek longi "+ longiaja);
//		}


	}


	///--------------------SUBMIT KE SERVER--------------------\\
	private class SubmitkeServer extends AsyncTask<String, Void, String> {
		String SOAP_ACTION = "http://layanan.mobilefin/simpanhasil2";
		String NAMESPACE = "http://layanan.mobilefin/";
		String METHOD_NAME = "simpanhasil2";
		String URLsumbitkeserver= getString(R.string.url);
		String outputresult;
		String custtype="";
		//ContentValues cv = new ContentValues();
		String[] data = theForm.getEncodedData();

		@Override
		protected void onPreExecute() {
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			try {
				db = datasource.getWritableDatabase();
				int cnt = 0;
				String image_str = null;
				String image_lat_str = null;
				String image_lng_str = null;


				Calendar cl = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
				SimpleDateFormat df22 = new SimpleDateFormat("dd-MM-yyyy");
				String myTime = df.format(cl.getTime());
				String tgl2 = df22.format(cl.getTime());


				prefsEditor.putString("lastransaction", String.valueOf(myTime)).commit();
				lasttransaction = myTime;
				tanggalbayar = tgl2;
				String theKey = datalocal.encrypt(myTime, getString(R.string.Key44));
				Log.d(TAG, "doInBackground: theKey1 "+theKey);

				Cursor c = db.rawQuery("SELECT imageStr, lat, lng FROM " + tableImageCollection, null);
				try {
					while (c.moveToNext()) {
						cnt++;
						if (image_str == null) {
							image_str = (c.getString(0));
							image_lat_str = c.getString(1);
							image_lng_str = c.getString(2);
							//Log.d(TAG, "doInBackground: "+image_lat_str+image_lng_str);
						} else {
							image_str = image_str + "split" + (c.getString(0));
							image_lat_str = image_lat_str + ";" + c.getString(1);
							image_lng_str = image_lng_str + ";" + c.getString(2);
							//Log.d(TAG, "doInBackground: "+image_lat_str+image_lng_str);
						}
					}
				} finally {
					c.close();
				}


				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
				Log.d(TAG, "doInBackground: request "+request.toString());

//				request.addProperty("a",datalocal.encrypt(data[0]
//						+ "duedate;taskdate;installmentno;paidby",myTime));
//				request.addProperty("b",datalocal.encrypt(data[1]
//						+ "'" + dueDate + "';'" + taskDate + "';'" + insNo + "';'" + getValue().trim() + "'",myTime));
//				request.addProperty("c",datalocal.encrypt(image_str,myTime));
//				request.addProperty("d",datalocal.encrypt(String.valueOf(cnt),myTime));
//				request.addProperty("e",datalocal.encrypt(moduleid,myTime));
//				request.addProperty("f",datalocal.encrypt(contractNo,myTime));
//				request.addProperty("g",datalocal.encrypt(latString,myTime));
//				request.addProperty("h",datalocal.encrypt(lngString,myTime));
//				request.addProperty("i",datalocal.encrypt(myTime,myTime));
//				request.addProperty("j",datalocal.encrypt(generalPrefs.getString("userID", null),myTime));
//				request.addProperty("k",datalocal.encrypt(image_lat_str,myTime));
//				request.addProperty("l",datalocal.encrypt(image_lng_str,myTime));
//				request.addProperty("m",datalocal.encrypt(theKey,myTime));
//				request.addProperty("n",datalocal.encrypt(custtype,myTime));

				///proses submit ke server
				request.addProperty("a",data[0]
						+ "duedate;taskdate;installmentno;paidby");
				request.addProperty("b",data[1]
						+ "'" + dueDate + "';'" + taskDate + "';'" + insNo + "';'" + getValue().trim() + "'");
				request.addProperty("c",image_str);
				request.addProperty("d",String.valueOf(cnt));
				request.addProperty("e",moduleid);
				request.addProperty("f",contractNo);
				request.addProperty("g",latString);
				request.addProperty("h",lngString);
				request.addProperty("i",myTime);
				request.addProperty("j",generalPrefs.getString("userID", null));
				request.addProperty("k",image_lat_str);
				request.addProperty("l",image_lng_str);
				request.addProperty("m",theKey);
				request.addProperty("n",custtype);
				//tambahan untuk tanggal bayar
				request.addProperty("o",tanggalbayar);



				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);


				HttpTransportSE androidHttpTransport = new HttpTransportSE(URLsumbitkeserver,120000);
				androidHttpTransport.debug = true;

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


				}
				System.out.println("outputresult saveresult :  SubmitkeServer " + outputresult);


			c.close();
			System.gc();


			} catch (Exception e) {
				e.printStackTrace();
				CollectionActivity.this.e = e;

			}



			return null;
		}

		@Override
		protected void onPostExecute(String aVoid) {
			super.onPostExecute(aVoid);
			Log.d("jstring dapat", "onPostExecute: SubmitkeServer " + aVoid);
			Log.d("jstring dapat", "onPostExecute: SubmitkeServer2 " + outputresult);


			if (outputresult.equals("Success")){
				Log.d(TAG, "onPostExecute: yangkedua" +outputresult);
				Toast.makeText(mContext,"Data Berhasil Tersimpan Di Server",Toast.LENGTH_SHORT).show();
				if (getValue().equals("EDC")) {
					Savekelocal();
					new PrintReceiptEDC().execute();
				} else {
					Savekelocal();
					new PrintReceiptCash().execute();
				}

			} else{
				//datasource.deleteData(db, "userid = '" +  userID + "'", "printcash");
				Toast.makeText(mContext,"Data Gagal Terkirim. Harap Diulang !!",Toast.LENGTH_LONG).show();
				dialog.dismiss();
			}

			//dialog.dismiss();
		}


	}

	public class PrintReceiptEDC extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			dialog.show();
		}

		@Override
		protected String doInBackground(Void... arg0) {
			boolean threadDone = false;
			StringBuilder receiptPrint = new StringBuilder();
			StringBuilder infoEDC = new StringBuilder();
			int serverPort = 48200;
			int timeout = 300000;
			String isTimeOut = "";
			String EDCKey = "A142AD52C2468087";

			String now;
			String sekarang;
			Calendar cl = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
			SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
			now = df.format(cl.getTime());
			sekarang = df2.format(cl.getTime());
			lasttransaction = now;
			tanggalbayar2 = sekarang;
			String strDueDate;

			strDueDate = dueDate.substring(6, 8) + "/" + dueDate.substring(4, 6) + "/" + dueDate.substring(0, 4);

			receiptPrint.append("2_|C|B|KWITANSI PEMBAYARAN TRANSFER_ _");

			receiptPrint.append("C|N|" + branchName + "_");
			receiptPrint.append("C|N|" + branchAddress + "_");
			receiptPrint.append("C|N|" + branchPhone + "_ _");

			receiptPrint.append("L|N|" + String.valueOf(now).trim() + " - " + collInitial + "_ _");

			receiptPrint.append("L|N|" + "No          : " + receiptNo.trim() + "_");
			receiptPrint.append("L|N|" + "Nama        : " + custNamePrint + "_");
			receiptPrint.append("L|N|" + "No Kontrak  : " + contractNo + "_");
			receiptPrint.append("L|N|" + "Angsuran ke : " + insNo + "_");
			receiptPrint.append("L|N|" + "Tgl Jt Tempo: " + strDueDate + "_");
			receiptPrint.append("L|N|" + "No Plat     : " + licensePlate.trim() + "_ _");

			int i;
			String space = "";
			String space2 = "";
			double total = 0.0;
			NumberFormat formatter = new DecimalFormat("###,###,###");

			for (i = 0; i < theForm.fields.size(); i++) {
				String fieldLabel = (String) theForm.fields.elementAt(i).getDataLabel();
				String fieldValue = (String) theForm.fields.elementAt(i).getData();

				fieldValue = fieldValue.replace(" ", "").replace(",", "").replace(".00", "");

				if (isNumeric(fieldValue) && fieldValue.length() > 2) {
					space = new String(new char[(15 - fieldLabel.trim().length())]).replace('\0', ' ');
					space2 = new String(new char[(11 - formatter.format(Double.valueOf(fieldValue)).length())]).replace('\0', ' ');

					receiptPrint.append("L|N|" + fieldLabel.trim() + space + "Rp" + space2 + formatter.format(Double.valueOf(fieldValue)).replace(",", ".") + "_");

					total = total + Double.valueOf(fieldValue);
				}
			}

			space = new String(new char[8]).replace('\0', ' ');
			space2 = new String(new char[(11 - formatter.format(total).length())]).replace('\0', ' ');

			receiptPrint.append("L|B|" + "Total" + space + "Rp" + space2 + formatter.format(total).replace(",", ".") + "_ _ _");

			receiptPrint.append("C|N|" + "Mohon Simpan Kwitansi ini" + "_");
			receiptPrint.append("C|N|" + "Sebagai Bukti Pembayaran anda" + "_");
			receiptPrint.append("C|N|" + "Terima Kasih" + "_");

			String kodeBank = generalPrefs.getString("kodeBank", null) + "FFFFFFFFFFFFFFFF";
			String noRek = generalPrefs.getString("noRek", null) + "FFFFFFFFFFFFFFFF";
			String noReff = contractNo.replace("AG", "") + insNo + "FFFFFFFFFFFFFFFF";
			String amountReceive = String.valueOf(total).replace(".0", "");
			StringBuilder amountReceiveClean = new StringBuilder();
			int length = amountReceive.length();
			int diff = 16 - length;

			for (int j = 0; j < diff; j++) {
				amountReceiveClean.append("0");
			}

			amountReceiveClean.append(amountReceive);

			infoEDC.append(encryptSingleDES(kodeBank.substring(0, 16), EDCKey) + "_");
			infoEDC.append(encryptSingleDES(noRek.substring(0, 16), EDCKey) + "_");
			infoEDC.append(encryptSingleDES(amountReceiveClean.toString(), EDCKey) + "_");
			infoEDC.append(encryptSingleDES(noReff.substring(0, 16), EDCKey) + "_");
			infoEDC.append(receiptPrint.toString());

			Log.d(TAG, "doInBackground: cek semua infoEDC = " + infoEDC.toString());
			try{
				String infoedc = infoEDC.toString();
				Log.d(TAG, "doInBackground: infoedc "+infoedc);
				prefsEditor.putString("infoedc", String.valueOf(infoedc)).commit();
			}catch (Exception e){
				e.printStackTrace();
			}

			try {
				serverSocket = new ServerSocket(serverPort);
				serverSocket.setSoTimeout(timeout);

				while (!threadDone) {
					new MultiThread(serverSocket.accept(), infoEDC.toString()).start();
					threadDone = true;
				}

				serverSocket.close();
				threadDone = false;

				serverSocket = new ServerSocket(serverPort);
				serverSocket.setSoTimeout(timeout);

				while (!threadDone) {
					new MultiThreadReceive(serverSocket.accept()).start();
					threadDone = true;
				}
				serverSocket.close();
			} catch (Exception e) {
				e.printStackTrace();

				try {
					serverSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				isTimeOut = "Transaction timeout, please try again..";
			}

			return isTimeOut;
		}

		@Override
		protected void onPostExecute(String sResponse) {
			Log.d(TAG, "onPostExecute: printEDC "+sResponse);
			boolean ada = true;

			if (sResponse.contentEquals("")) {
				if (GlobalVariable.getResult() != "")
					ada = false;

				while (ada) {
					if (GlobalVariable.getResult() != "")
						ada = false;
				}

				if (GlobalVariable.getResult().equals("1")) {
					GlobalVariable.setResult("");
					//tambahin savekelokal untuk ketika re-print ke table yg sama
					Savekelocal();
					new PostData().execute();
				} else {
					Toast.makeText(mContext, "Transaction failed, please try again..", Toast.LENGTH_LONG).show();
					dialog.dismiss();
					GlobalVariable.setResult("");
					return;
				}

			} else {
				Toast.makeText(mContext, sResponse, Toast.LENGTH_LONG).show();
				dialog.dismiss();
//				//--------------TAMBAHAN------------------------------------------------------\\
//				Toast.makeText(mContext, "Anda harus melakukan Re-Print !!", Toast.LENGTH_LONG).show();
//				try{
//					ContentValues cv = new ContentValues();
//					cv.put("status", sResponse);
//					prefsEditor.putString("status", sResponse).commit();
//					//datasource.updateData(db,cv,tableName," userid = '" + userID + "'");
//					datasource.updateData(db,cv,tableName," userid = '" + userID + "' and paidBy = '"+ getValue().equals("EDC")+"'");
//					cv.clear();
//				}catch (Exception e){
//					e.printStackTrace();
//				}
//
//				finish();
//				Intent intent = new Intent(mContext, ListCollectionActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				intent.putExtra("flagUpdate", "U");
//				startActivity(intent);
//				//-----------------------------------------------------------------------------\\

			}

		}

	}

	public class PrintReceiptCash extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			//dialog.show();
		}

		@Override
		protected String doInBackground(Void... arg0) {
			boolean threadDone = false;
			StringBuilder receiptPrint = new StringBuilder();
			int serverPort = 48200;
			//int timeout = 30000;
			int timeout = 60000; // 1 menit
			String isTimeOut = "";

			String now;
			Calendar cl = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
			now = df.format(cl.getTime());
			String strDueDate;

			strDueDate = dueDate.substring(6, 8) + "/" + dueDate.substring(4, 6) + "/" + dueDate.substring(0, 4);

			receiptPrint.append("2_|C|B|KWITANSI PEMBAYARAN CASH_ _");

			receiptPrint.append("C|N|" + branchName + "_");
			receiptPrint.append("C|N|" + branchAddress + "_");
			receiptPrint.append("C|N|" + branchPhone + "_ _");

			receiptPrint.append("L|N|" + String.valueOf(now).trim() + " - " + collInitial + "_ _");

			receiptPrint.append("L|N|" + "No          : " + receiptNo.trim() + "_");
			receiptPrint.append("L|N|" + "Nama        : " + custNamePrint + "_");
			receiptPrint.append("L|N|" + "No Kontrak  : " + contractNo + "_");
			receiptPrint.append("L|N|" + "Angsuran ke : " + insNo + "_");
			receiptPrint.append("L|N|" + "Tgl Jt Tempo: " + strDueDate + "_");
			receiptPrint.append("L|N|" + "No Plat     : " + licensePlate.trim() + "_ _");

			int i;
			String space = "";
			String space2 = "";
			double total = 0.0;
			NumberFormat formatter = new DecimalFormat("###,###,###");

			for (i = 0; i < theForm.fields.size(); i++) {
				String fieldLabel = (String) theForm.fields.elementAt(i).getDataLabel();
				String fieldValue = (String) theForm.fields.elementAt(i).getData();

				fieldValue = fieldValue.replace(" ", "").replace(",", "").replace(".00", "");

				if (isNumeric(fieldValue) && fieldValue.length() > 2) {
					space = new String(new char[(15 - fieldLabel.trim().length())]).replace('\0', ' ');
					space2 = new String(new char[(11 - formatter.format(Double.valueOf(fieldValue)).length())]).replace('\0', ' ');

					receiptPrint.append("L|N|" + fieldLabel.trim() + space + "Rp" + space2 + formatter.format(Double.valueOf(fieldValue)).replace(",", ".") + "_");

					total = total + Double.valueOf(fieldValue);
				}
			}

			space = new String(new char[8]).replace('\0', ' ');
			space2 = new String(new char[(11 - formatter.format(total).length())]).replace('\0', ' ');

			receiptPrint.append("L|B|" + "Total" + space + "Rp" + space2 + formatter.format(total).replace(",", ".") + "_ _ _");

			receiptPrint.append("C|N|" + "Mohon Simpan Kwitansi ini" + "_");
			receiptPrint.append("C|N|" + "Sebagai Bukti Pembayaran anda" + "_");
			receiptPrint.append("C|N|" + "Terima Kasih" + "_");

			Log.d(TAG, "doInBackground: cek semua receiptprintnya = " + receiptPrint.toString());
			try{
				String coba = receiptPrint.toString();
				Log.d(TAG, "doInBackground: PrintReceiptCash "+coba);
				prefsEditor.putString("prinan", String.valueOf(coba)).commit();
			}catch (Exception e){
				e.printStackTrace();
			}

			try {
				serverSocket = new ServerSocket(serverPort);
				serverSocket.setSoTimeout(timeout);

				while (!threadDone) {
					new MultiThread(serverSocket.accept(), receiptPrint.toString()).start();
					threadDone = true;
				}

				serverSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					serverSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				isTimeOut = "Print Receipt Timeout";
			}

			return isTimeOut;
		}

		@Override
		protected void onPostExecute(String sResponse) {
			Log.d(TAG, "onPostExecute: 123"+sResponse);

			if (sResponse.contentEquals("")) {
				Log.d(TAG, "onPostExecute: sResponse "+sResponse);
				hapusfoto();
				finish();
				Intent intent = new Intent(mContext, ListCollectionActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("flagUpdate", "U");
				startActivity(intent);
				Toast.makeText(mContext, "Data has been Print", Toast.LENGTH_LONG).show();
				//new PostData().execute();
			} else {
				Toast.makeText(mContext, sResponse, Toast.LENGTH_LONG).show();
				Toast.makeText(mContext, "Anda harus melakukan Re-Print !!", Toast.LENGTH_LONG).show();
				try{
					ContentValues cv = new ContentValues();
					cv.put("status", sResponse);
					prefsEditor.putString("status", sResponse).commit();
					//datasource.updateData(db,cv,tableName," userid = '" + userID + "'");
					datasource.updateData(db,cv,tableName," userid = '" + userID + "' and paidBy = '"+ paidBy + "'");
					cv.clear();
				}catch (Exception e){
					e.printStackTrace();
				}
				finish();
				Intent intent = new Intent(mContext, ListCollectionActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("flagUpdate", "U");
				startActivity(intent);
				dialog.dismiss();
			}

		}

	}

	private void Savekelocal(){
		db = datasource.getWritableDatabase();
		ContentValues cv = new ContentValues();
		String[] data = theForm.getEncodedData();

		datasource.deleteData(db, "userid = '" +  userID + "'", "printcash");
		try{
			cv.put("fieldname", data[0]);
			cv.put("fieldvalue", data[1]);
			cv.put("moduleid", moduleid);
			cv.put("key", contractNo);
			cv.put("userid", userID);
			cv.put("lasttransaction",lasttransaction);
			cv.put("receiptNo",receiptNo);
			cv.put("insNo",insNo);
			cv.put("dueDate",dueDate);
			cv.put("custNamePrint",custNamePrint);
			cv.put("collInitial",collInitial);
			cv.put("licensePlate",licensePlate);
			cv.put("status", "");
			cv.put("strTotal", strTotal);
			cv.put("paidBy", getValue().trim());
			cv.put("counter", 0);
			cv.put("paiddate",tanggalbayar);

			datasource.generateData(db, cv, "printcash");

			cv.clear();

			datasource.deleteData(db, "contractno = '" + contractNo + "'", "collectionlist");
			datasource.deleteData(db, "moduleid = '" + moduleid + "' and idx = '" + contractNo + "'", "infodetail");

			Log.d(TAG, "Savekelocal: "
					+"\n"+ "module idnya : " + moduleid
					+"\n"+ "user idnya : " + generalPrefs.getString("userID", null)
					+"\n"+ "no kontraknya : " + contractNo
					+"\n"+ " userid : " + userID
					+"\n"+ " moduleid : " + moduleid
					+"\n"+ " strTotal : " + strTotal
					+"\n"+ " receiptNo : " + receiptNo
					+"\n"+ " insNo : " + insNo
					+"\n"+ " dueDate : " + dueDate
					+"\n"+ " licensePlate : " + licensePlate
					+"\n"+ " custNamePrint : " + custNamePrint
					+"\n"+ " lasttransaction : " + lasttransaction
					+"\n"+ " collInitial : " + collInitial
					+"\n"+ "fieldname yg di edit dwi : " + data[0]
					+"\n"+ "fieldvalue yg di edit dwi : " + data[1]
					+"\n"+ "paidBy : " + getValue().trim()
					+"\n"+ "counter :"+0
					+"\n"+ "tanggalBayar :"+ tanggalbayar );

		}catch (Exception e){
			e.printStackTrace();
		}finally {
			db.close();
		}
	}

	private void hapusprintcash(){
		try{
			db = datasource.getWritableDatabase();
			datasource.deleteData(db, "userid = '" + userID + "'", "printcash");// bisa

			//datasource.deleteData(db, "userid = '" + userID + "' and key = '"+ contractNo +"'" , "printcash");//gak bisa
			//datasource.deleteData(db, "paidBy = '" + getValue().equals("Cash") +"'" , "printcash");//gak bisa
			//datasource.deleteData(db, "paidBy = '" + paidBy +"'" , "printcash");//gak bisa
			//datasource.deleteData(db, "key = '" + contractNo + "'", "printcash");//gak bisa
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			db.close();
		}

	}

	private class PostData extends AsyncTask<Void, Void, String> {
		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				db = datasource.getWritableDatabase();

				///tambahan buat save picture ke sqlite dan server
				int cnt = 0;
				String image_str = null;
				String image_lat_str = null;
				String image_lng_str = null;

				String[] data = theForm.getEncodedData();
				ContentValues cv = new ContentValues();


				//tambahan unutk save picture.
				Cursor c = db.rawQuery("SELECT imageStr, lat, lng FROM " + tableImageCollection, null);
				Log.d(TAG, "doInBackground: "+ db.rawQuery("SELECT imageStr, lat, lng FROM " + tableImageCollection, null));
				try {
					while (c.moveToNext()) {
						cnt++;
						if (image_str == null) {
							image_str = (c.getString(0));
							image_lat_str = c.getString(1);
							image_lng_str = c.getString(2);
							Log.d(TAG, "doInBackground: "+image_lat_str+image_lng_str);
						} else {
							image_str = image_str + "split" + (c.getString(0));
							image_lat_str = image_lat_str + ";" + c.getString(1);
							image_lng_str = image_lng_str + ";" + c.getString(2);
							Log.d(TAG, "doInBackground: "+image_lat_str+image_lng_str);
						}
					}
				} finally {
					c.close();
				}
				Calendar cl = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String myTime = df.format(cl.getTime());


				cv.put("fieldname", data[0]
						+ "duedate;taskdate;installmentno;paidby");
				cv.put("fieldvalue", data[1]
						+ "'" + dueDate + "';'" + taskDate + "';'" + insNo + "';'" + getValue().trim() + "'");
				//cv.put("imagestr", "");
				cv.put("imagestr", image_str);
				//cv.put("imagecount", "0");
				cv.put("imagecount", String.valueOf(cnt));//tambahan
				cv.put("moduleid", moduleid);
				cv.put("key", contractNo);
				cv.put("lat", latString);
				cv.put("lng", lngString);
				cv.put("dtmupd", myTime);
				cv.put("userid", generalPrefs.getString("userID", null));
				cv.put("imagelat", image_lat_str);
				cv.put("imagelng", image_lng_str);
				cv.put("custtype", "");
				cv.put("paiddate",tanggalbayar2);


				Log.d(TAG, "doInBackground: "+image_str + image_lat_str + image_lng_str);
				Log.d(TAG, "doInBackground: "
						+"module idnya : " + moduleid
						+"user idnya : " + generalPrefs.getString("userID", null)
						+"dtmupd idnya : " + myTime
						+"tanggalbayar2 : " + tanggalbayar2
						+"no kontraknya : " + contractNo
						+"imagecount : " + String.valueOf(cnt)
						+"fieldname : " + data[0]+ "duedate;taskdate;installmentno;paidby"
						+"fieldvalue : " + data[1] + "'" + dueDate + "';'" + taskDate + "';'" + insNo + "';'" + getValue().trim() + "'"

				);
				datasource.generateData(db, cv, "result");

				cv.clear();

				datasource.deleteData(db, "contractno = '" + contractNo + "'", "collectionlist");
				datasource.deleteData(db, "moduleid = '" + moduleid + "' and idx = '" + contractNo + "'", "infodetail");

				System.gc();

			} catch (Exception e) {
				e.printStackTrace();
				CollectionActivity.this.e = e;
			} finally {
				db.close();
			}
			return "";
		}

		@Override
		protected void onPostExecute(String sResponse) {
			if (e == null) {
				finish();
				Intent intent = new Intent(mContext, ListCollectionActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("flagUpdate", "N");
				startActivity(intent);

				dialog.dismiss();
				Toast.makeText(mContext, "Data has been saved", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(mContext, "Connection time out", Toast.LENGTH_SHORT).show();
				e = null;
				dialog.dismiss();
			}
		}
	}

	private class PostData2 extends AsyncTask<Void, Void, String> {
		@Override
		protected void onPreExecute() {
			dialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			try {

				db = datasource.getWritableDatabase();


				///tambahan buat save picture ke sqlite dan server
				int cnt = 0;
				String image_str = null;
				String image_lat_str = null;
				String image_lng_str = null;

				String[] data = theForm.getEncodedData();
				ContentValues cv = new ContentValues();

				Cursor c = db.rawQuery("SELECT imageStr, lat, lng FROM " + tableImageCollection, null);
				Log.d(TAG, "doInBackground: "+ db.rawQuery("SELECT imageStr, lat, lng FROM " + tableImageCollection, null));
				try {
					while (c.moveToNext()) {
						cnt++;
						if (image_str == null) {
							image_str = (c.getString(0));
							image_lat_str = c.getString(1);
							image_lng_str = c.getString(2);
							Log.d(TAG, "doInBackground: "+image_lat_str+image_lng_str);
						} else {
							image_str = image_str + "split" + (c.getString(0));
							image_lat_str = image_lat_str + ";" + c.getString(1);
							image_lng_str = image_lng_str + ";" + c.getString(2);
							Log.d(TAG, "doInBackground: "+image_lat_str+image_lng_str);
						}
					}
				} finally {
					c.close();
				}

				Calendar cl = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String myTime = df.format(cl.getTime());

				cv.put("fieldname", data[0]
						+ "duedate;taskdate;installmentno");
				cv.put("fieldvalue", data[1]
						+ "'" + dueDate + "';'" + taskDate + "';'" + insNo + "'");
				cv.put("imagestr", image_str);
				//cv.put("imagecount", 0);
				cv.put("imagecount", String.valueOf(cnt));//tambahan
				cv.put("moduleid", moduleid);
				cv.put("key", contractNo);
				cv.put("lat", latString);
				cv.put("lng", lngString);
				cv.put("dtmupd", myTime);
				cv.put("userid", generalPrefs.getString("userID", null));
				cv.put("imagelat", image_lat_str);
				cv.put("imagelng", image_lng_str);
				cv.put("custtype", "");

				Log.d(TAG, "doInBackground: post data2"+image_lat_str+image_lng_str);
				Log.d(TAG, "doInBackground: postdata2"+image_str);
				Log.d(TAG, "doInBackground: "
						+"module idnya : " + moduleid
						+"user idnya : " + generalPrefs.getString("userID", null)
						+"dtmupd idnya : " + myTime
						+"no kontraknya : " + contractNo
						+"imagecount : " + String.valueOf(cnt)
						+"fieldname : " + data[0]+ "duedate;taskdate;installmentno"
						+"fieldvalue : " + data[1] + "'" + dueDate + "';'" + taskDate + "';'" + insNo + "'"

				);

				datasource.generateData(db, cv, "result");

				cv.clear();
				datasource.deleteData(db, "contractno = '" + contractNo + "'", "collectionlist");
				datasource.deleteData(db, "moduleid = '" + moduleid + "' and idx = '" + contractNo + "'", "infodetail");

				System.gc();

			} catch (Exception e) {
				e.printStackTrace();
				CollectionActivity.this.e = e;

			} finally {
				db.close();
			}
			return "";
		}

		@Override
		protected void onPostExecute(String sResponse) {
			if (e == null) {
				finish();
				Intent intent = new Intent(mContext, ListCollectionActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("flagUpdate", "N");
				startActivity(intent);

				dialog.dismiss();
				Toast.makeText(mContext, "Data has been saved", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(mContext, "Connection time out", Toast.LENGTH_SHORT).show();
				e = null;
				dialog.dismiss();
			}
		}
	}

	///--------------------SUBMIT KE SERVER--------------------\\






	///--------------------GET VALUE--------------------\\

	private String getValue() {
		String text = "";
		for (int i = 0; i < radiogroup.getChildCount(); i++) {
			RadioButton rbt = (RadioButton) radiogroup.getChildAt(i);
			if (rbt.isChecked()) {
				text = (String) rbt.getText().toString().trim();
				break;
			}
		}
		return text.trim();
	}

	private void disableRadioButton() {
		for (int i = 0; i < radiogroup.getChildCount(); i++) {
			RadioButton rbt = (RadioButton) radiogroup.getChildAt(i);
			rbt.setEnabled(false);
		}
	}

	private boolean CheckForm() {
		try {
			int i;
			boolean good = true;

			for (i = 0; i < theForm.fields.size(); i++) {
				String fieldValue = (String) theForm.fields.elementAt(i).getData();

				if (theForm.fields.elementAt(i).isRequired()) {

					if (fieldValue == null || fieldValue.trim().length() == 0) {
						theForm.fields.elementAt(i).setData(
								theForm.fields.elementAt(i).getErrmsg());
						good = false;
					}
				}
			}
			return good;
		} catch (Exception e) {
			Log.e("Activity_Survey", "Error in CheckForm() : " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	private class PrintInfo extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			dialog.show();
		}

		@Override
		protected String doInBackground(Void... arg0) {
			boolean threadDone = false;
			StringBuilder infoPrint = new StringBuilder();
			int serverPort = 48200;
			int timeout = 30000;
			String isTimeOut = "";

			String now;
			Calendar cl = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
			now = df.format(cl.getTime());
			String strDueDate;

			strDueDate = dueDate.substring(6, 8) + "/" + dueDate.substring(4, 6) + "/" + dueDate.substring(0, 4);

			infoPrint.append("1_|C|B|INFORMASI TAGIHAN_ _");

			infoPrint.append("C|N|" + branchName + "_");
			infoPrint.append("C|N|" + branchAddress + "_");
			infoPrint.append("C|N|" + branchPhone + "_ _");

			infoPrint.append("L|N|" + String.valueOf(now).trim() + " - " + collInitial + "_ _");

			infoPrint.append("L|N|" + "Nama        : " + custNamePrint + "_");
			infoPrint.append("L|N|" + "No Kontrak  : " + contractNo + "_");
			infoPrint.append("L|N|" + "Angsuran ke : " + insNo + "_");
			infoPrint.append("L|N|" + "Tgl Jt Tempo: " + strDueDate + "_");
			infoPrint.append("L|N|" + "No Plat     : " + licensePlate.trim() + "_ _");

			int i;
			String space = "";
			String space2 = "";
			double total = 0.0;
			NumberFormat formatter = new DecimalFormat("###,###,###");

			for (i = 0; i < info.size(); i++) {
				String fieldLabel = (String) label.get(i);
				String fieldValue = (String) info.get(i);

				fieldValue = fieldValue.replace(" ", "").replace(",", "").replace(".00", "");

				if (isNumeric(fieldValue.replace("+", "#")) && fieldValue.length() > 2) {
					space = new String(new char[(15 - fieldLabel.trim().length())]).replace('\0', ' ');
					space2 = new String(new char[(11 - formatter.format(Double.valueOf(fieldValue)).length())]).replace('\0', ' ');

					infoPrint.append("L|N|" + fieldLabel.trim() + space + "Rp" + space2 + formatter.format(Double.valueOf(fieldValue)).replace(",", ".") + "_");

					total = total + Double.valueOf(fieldValue);
				}
			}

			space = new String(new char[8]).replace('\0', ' ');
			space2 = new String(new char[(11 - formatter.format(total).length())]).replace('\0', ' ');

			infoPrint.append("L|B|" + "Total" + space + "Rp" + space2 + formatter.format(total).replace(",", ".") + "_ _ _");

			infoPrint.append("C|N|" + "Informasi Tagihan ini bukan" + "_");
			infoPrint.append("C|N|" + "Merupakan Bukti Pembayaran" + "_");
			infoPrint.append("C|N|" + "Terima Kasih" + "_");

			try {
				serverSocket = new ServerSocket(serverPort);
				serverSocket.setSoTimeout(timeout);

				while (!threadDone) {
					new MultiThread(serverSocket.accept(), infoPrint.toString()).start();
					threadDone = true;
				}

				serverSocket.close();

			} catch (Exception e) {
				e.printStackTrace();

				try {
					serverSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				isTimeOut = "Print timeout, please try again..";
			}

			return isTimeOut;
		}

		@Override
		protected void onPostExecute(String sResponse) {

			if (sResponse.contentEquals("")) {
				dialog.dismiss();
			} else {
				Toast.makeText(mContext, sResponse, Toast.LENGTH_LONG).show();
				dialog.dismiss();
			}

		}

	}

	public String encryptSingleDES(String unencryptedString, String passKey) {

		try {

			byte[] myEncryptionKey = ISOUtil.hex2byte(passKey);

			DESKeySpec keySpec = new DESKeySpec(myEncryptionKey);

			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(keySpec);

			byte[] cleartext = ISOUtil.hex2byte(unencryptedString);

			Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			String encrypedPwd = ISOUtil.hexString(cipher.doFinal(cleartext));

			return encrypedPwd;

		} catch (InvalidKeyException e1) {
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return null;
	}

	///--------------------GET VALUE--------------------\\


	@Override
	public void onBackPressed() {

		final SweetAlertDialog close = new SweetAlertDialog(CollectionActivity.this, SweetAlertDialog.WARNING_TYPE);
		close.setCanceledOnTouchOutside(true);
		close.setTitleText("Perhatian!")
				.setContentText("Anda Yakin Untuk Kembali ?")
				.setConfirmText("Ya")
				.setCancelText("Tidak")

				.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(final SweetAlertDialog sDialog) {
						sDialog.dismiss();
						finish();
						hapusfoto();
						Intent intent = new Intent(mContext, ListCollectionActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("flagUpdate", "N");

						startActivity(intent);
					}
				})
				.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						sweetAlertDialog.cancel();

					}
				});
		close.show();


		return;
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (googleApiClient !=null){
			googleApiClient.connect();
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (wl.isHeld())
			wl.release();

		if (theForm != null) {
			theForm = null;
		}

		if (adapter != null) {
			adapter.notifyDataSetInvalidated();
			adapter = null;
		}

		if (datasource != null) {
			datasource.close();
		}

		if (locationManager != null) {
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

				return;
			}
			locationManager.removeUpdates(locationListenerGps);
			locationManager.removeUpdates(locationListenerNetwork);
			locationManager = null;
		}

		if (Nmap != null) {
			Nmap.clear();
		}

		if (tabHost != null) {
			tabHost = null;
		}

		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}

		if (db != null) {
			if (db.isOpen()) {
				db.close();
			}
		}

		//googleApiClient.disconnect();

	}

	@Override
	public void onPause() {
		super.onPause();
		
		if(wl.isHeld())
			wl.release();

		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}
		
		if(serverSocket != null)
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

//		if (googleApiClient.isConnected()){
//			googleApiClient.disconnect();
//		}

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
		
		if (flag == false) {
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

				//googleApiClient.connect();

				diffInMin = (dateEnd.getTime() / 60000) - (dateStart.getTime() / 60000);
				
				if ((int) diffInMin > Integer.valueOf(timeOut) || (int) diffInMin < 0) {
					Toast.makeText(this,"Your session has been expired, please re login for security purpose", Toast.LENGTH_LONG).show();
					prefsEditor.putBoolean("finish", true).commit();

					finish();
					Intent myintent = new Intent(mContext, HomeActivity.class);
					myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(myintent);
				}else{
					if(!wl.isHeld())
						wl.acquire();
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else{
			if(!wl.isHeld())
				wl.acquire();
		}
	}



	@Override
	public void onConnected(@Nullable Bundle bundle) {
//		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
//				!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
//				android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//			return;
//	}
//
//		Nmap.setMyLocationEnabled(true);
//
//		Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//		if (location != null) {
//			LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
//			Log.d("wadidaw", "onConnected: "+latLng);
//			MarkerOptions MO = new MarkerOptions();
//			MO.position(latLng);
//			MO.title("posisi saya");
//			MO.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//			cloc = Nmap.addMarker(MO);
//		}
//
//		locationRequest = new LocationRequest();
//		locationRequest.setInterval(5000);
//		locationRequest.setFastestInterval(2000);
//		locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//
//		LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);

	}

	@Override
	public void onConnectionSuspended(int i) {
		Toast.makeText(getApplicationContext(),"onConnectionSuspended",Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		Toast.makeText(getApplicationContext(),"onConnectionFailed",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onLocationChanged(Location location) {
//		if (cloc !=null){
//			cloc.remove();
//		}
//		LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
//		MarkerOptions mo = new MarkerOptions();
//		mo.position(latLng);
//		mo.title("poisis baru");
//		mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//		cloc = Nmap.addMarker(mo);
//
//		Nmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));

	}


	/*private OnClickListener myLocationOnClickListener = new OnClickListener() {
		public void onClick(View v) {
			Toast.makeText(getApplicationContext(),"inipeta",Toast.LENGTH_SHORT).show();
			handlelocation();
			isMapImageValid = false;
			if (ActivityCompat.checkSelfPermission(getApplicationContext(),
					android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
					&& ActivityCompat.checkSelfPermission(getApplicationContext(),
					android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				return;
			}

			Nmap.setMyLocationEnabled(true);
			LocationManager mylokasi = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			Location myLocation = mylokasi.getLastKnownLocation(mylokasi.getBestProvider(criteria,false));
			clatitude = myLocation.getLatitude();
			clongitude = myLocation.getLongitude();
			Log.d(TAG, "onClick: "+"latitudenya adalah "+clatitude +"longitudenya adalah "+clongitude);


			if (myLocation == null) {
				Toast.makeText(mContext, "Location not found, please wait..", Toast.LENGTH_SHORT).show();
			} else {


				LatLng myLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

				CameraPosition myPosition = new CameraPosition.Builder()
						.target(myLatLng).zoom(15).bearing(90).tilt(30).build();

				Nmap.animateCamera(
						CameraUpdateFactory.newCameraPosition(myPosition));
				Nmap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));

				Nmap.getUiSettings().setAllGesturesEnabled(false);

				btnMyLocation.setVisibility(View.GONE);
				btnCheckIn.setVisibility(View.VISIBLE);
				//Nmap.setMyLocationEnabled(false);
			}

		}
	};*/



	/*private OnClickListener checkInOnClickListener = new OnClickListener() {
		public void onClick(View v) {

			if (latLongString.length() == 0) {
				Log.d("dawda", "onClick: "+latLongString);
				Toast.makeText(mContext, "Location not found, please wait..", Toast.LENGTH_SHORT).show();
				handlelocation();
			} else {

				isMapImageValid = true;
				btnCheckIn.setVisibility(View.GONE);

				if (addressText.length() > 0) {
					Log.d("wadidaw", "onClick: "+addressText);
					Toast.makeText(mContext, "You have check in at : \n" + addressText, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(mContext, "You have check in at : \n" + latLongString, Toast.LENGTH_SHORT).show();
				}
			}
		}
	};*/

}