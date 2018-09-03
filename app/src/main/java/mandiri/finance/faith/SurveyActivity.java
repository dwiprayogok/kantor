package mandiri.finance.faith;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost.TabSpec;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mandiri.finance.faith.Interface.CallbackID;
import mandiri.finance.faith.Interface.GPSTracker;
import mandiri.finance.faith.Model.ZipCode;

public class SurveyActivity extends DashBoardActivity3 implements OnMapReadyCallback {
	private String custID;
	private String custName;
	private String custAddress;
	private String custType;

	private Context mContext;

	private TabHost tabHost = null;
	private LazyAdapterImage adapter;
	private Exception e = null;

	//private ArrayList<String> dataResults = new ArrayList<String>();
	private ArrayList<String> imageURLResults = new ArrayList<String>();

	private ArrayList<String> label = new ArrayList<String>();
	private ArrayList<String> info = new ArrayList<String>();

	private DataSource datasource;
	private SQLiteDatabase db;
	private Form theForm;
	private String tableImage;

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

	private GoogleMap googleMap;
	private MapFragment mapFragment;

	private String moduleid;
	private RadioGroup radiogroup, radiogroup2;
	private GoogleMap Nmap;
	GPSTracker gps;
	Double clongitude;
	Double clatitude;
	Marker cloc;
	private String isLoadZipCode;
	String TAG = "SurveyActivity";
	private DataLocal datalocal;
	public String fieldKelurahan = "",
			fieldKecamatan = "",
			fieldOfficeKelurahan = "",
			fieldOfficeKecamatan = "";
	ZipCode kode = new ZipCode();
	public static String Kota = "";
	private GoogleApiClient googleApiClient;
	CallbackID call;

	private Spinner
			spinner_kota,
			spinner_kecamatan,
			spinner_kelurahan,
			spinner_zipcode;


	private Spinner
			spinner_kotaperusahaan,
			spinner_kecamatanperusahaan,
			spinner_kelurahanperusahaan,
			spinner_zipcodeperusahaan;


	private Spinner
			spinner_kotaW,
			spinner_kecamatanW,
			spinner_kelurahanW,
			spinner_zipcodeW;


	private Spinner
			spinner_kotaperusahaanW,
			spinner_kecamatanperusahaanW,
			spinner_kelurahanperusahaanW,
			spinner_zipcodeperusahaanW;


	private Spinner
			spinner_kotaB,
			spinner_kecamatanB,
			spinner_kelurahanB,
			spinner_zipcodeB;


	private Spinner
			spinner_kotaperusahaanB,
			spinner_kecamatanperusahaanB,
			spinner_kelurahanperusahaanB,
			spinner_zipcodeperusahaanB;

	public ArrayAdapter<String> adapter_Kota;
	public ArrayAdapter<String> adapter_zipcode;
	public ArrayAdapter<String> adapter_Kecamatan;
	public ArrayAdapter<String> adapter_Kelurahan;

	public ArrayAdapter<String> adapter_Kotaperusahaan;
	public ArrayAdapter<String> adapter_zipcodeperusahaan;
	public ArrayAdapter<String> adapter_Kecamatanperusahaan;
	public ArrayAdapter<String> adapter_Kelurahanperusahaan;
	private ArrayList<ZipCode> formitem = new ArrayList<>();

	private String nmkota = "";
	private String nmkecamatan = "";
	private String nmkelurahan = "";
	private String nmzipcode = "";

	private String nmkotaperusahaan = "";
	private String nmkecamatanperusahaan = "";
	private String nmkelurahanperusahaan = "";
	private String nmzipcodeperusahaan = "";
	private String kota = "";

	private EditText
			edt_namalengkap,
			edt_email,
			edt_alamattempattinggal,
			edt_rt,
			edt_rw,
			edt_zipcode,
			edt_kota,
			edt_kecamatan,
			edt_kelurahan,
			edt_alamattempattinggalkantor,
			edt_rtperusahaan,
			edt_rwperusahaan,
			edt_zipcodeperusahaan,
			edt_kotaperusahaan,
			edt_kecamatanperusahaan,
			edt_kelurahanperusahaan;


	private EditText
			edt_namalengkapW,
			edt_PhoneNoW,
			edt_emailW,
			edt_MotherMaidenNameW,
			edt_KetMotherNameW,
			edt_alamattempattinggalW,
			edt_rtW,
			edt_rwW,
			edt_zipcodeW,
			edt_kotaW,
			edt_kecamatanW,
			edt_kelurahanW,
			edt_namatempattinggalkantorW,
			edt_alamattempattinggalkantorW,
			edt_rtperusahaanW,
			edt_rwperusahaanW,
			edt_zipcodeperusahaanW,
			edt_kotaperusahaanW,
			edt_kecamatanperusahaanW,
			edt_kelurahanperusahaanW;


	private EditText
			edt_CompanyNameB,
			edt_LegalAddressB,
			edt_LegalrtB,
			edt_LegalrwB,
			edt_LegalzipcodeB,
			edt_LegalkotaB,
			edt_LegalkecamatanB,
			edt_LegalkelurahanB,
			edt_DomisiliAddressB,
			edt_DomisiliRTB,
			edt_DomisiliRWB,
			edt_DomisilizipcodeB,
			edt_DomisilikotaB,
			edt_DomisilikecamatanB,
			edt_DomisilikelurahanB;
	private String branchID;
	ZipCode Custom_model = new ZipCode();
	private String tglserver;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = SurveyActivity.this;
		datasource = new DataSource(mContext);
		db = datasource.getWritableDatabase();
		datalocal = new DataLocal();
		tableImage = "image";


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
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			now = df.format(c.getTime());

			timeOut = generalPrefs.getString("timeOut", null);
			sessionStart = generalPrefs.getString("sessionStart", null);
			isLoadZipCode = generalPrefs.getString("isLoadZipCode", null);

			Date dateStart = df.parse(sessionStart);
			Date dateEnd = df.parse(now);

			diffInMin = (dateEnd.getTime() / 60000) - (dateStart.getTime() / 60000);

			if ((int) diffInMin > Integer.valueOf(timeOut) || (int) diffInMin < 0) {
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
		formitem.add(Custom_model);
		flag = true;
		branchID = generalPrefs.getString("branchID", null);
		moduleid = generalPrefs.getString("moduleid", null);

		Log.d("wadaw", "onCreate: " + moduleid);
		Log.d(TAG, "onCreate: branchID " + branchID);

		tglserver = generalPrefs.getString("tglserver", null);
		Log.d(TAG, "onCreate: tglserver " + tglserver);


		Bundle b = getIntent().getExtras();
		custID = b.getString("custID");
		custName = b.getString("custName");
		custAddress = b.getString("custAddress");
		Log.d(TAG, "onCreate: custID " + custID + " custName " + custName + " custAddress" + custAddress);

		setContentView(R.layout.tabs);
		setHeader(getString(R.string.SurveyActivityTitle), true);

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
			return "";
		}

		protected void onPostExecute(String sResponse) {
			if (e == null) {

				try {
					db = datasource.getWritableDatabase();

					Cursor c = db.rawQuery("SELECT infolabel, infotext" +
							" FROM infodetail where moduleid = '" + moduleid + "' and idx = '" + custID + "'", null);
					try {
						while (c.moveToNext()) {
							label.add(c.getString(0));
							info.add(c.getString(1));
						}
					} finally {
						c.close();
					}

					Cursor cs = db.rawQuery("SELECT custtype FROM surveylist where customerid = '" + custID + "'", null);

					try {
						if (cs.moveToFirst()) {
							custType = cs.getString(0);
							Log.d(TAG, "onPostExecute: " + custType);
						}
					} finally {
						cs.close();
					}

					Cursor cr = db.rawQuery("SELECT imageurl " +
							"FROM imageurldetail where moduleid = '" + moduleid + "' and idx = '" + custID + "'", null);

					try {
						while (cr.moveToNext()) {
							imageURLResults.add(cr.getString(0));
						}
					} finally {
						cr.close();
						db.close();
					}


				} catch (Exception e) {
					SurveyActivity.this.e = e;
				}

				if (tabHost == null) {
					tabHost = (TabHost) findViewById(R.id.tabhost);
					tabHost.setup();
				}

				// Tab for Info
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
						tvHeader.setPadding(0, 10, 0, 0);
						tvHeader.setSingleLine(true);

						TextView tvDetail = new TextView(mContext);
						tvDetail.setText(custAddress);
						tvDetail.setLayoutParams(params);
						tvDetail.setTextColor(mContext.getResources().getColor(R.color.label_color));
						tvDetail.setTypeface(null, Typeface.ITALIC);
						tvDetail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
						tvDetail.setMaxLines(2);
						tvDetail.setPadding(0, 0, 0, 10);

						Gallery gl = new Gallery(mContext);
						gl.setLayoutParams(params);
						gl.setPadding(0, 0, 0, 10);
						gl.setSpacing(20);


						String[] imageURLArray = imageURLResults.toArray(new String[imageURLResults.size()]);
						//String [] dataArray = dataResults.toArray(new String[dataResults.size()]);

						adapter = new LazyAdapterImage(SurveyActivity.this, imageURLArray);
						gl.setAdapter(adapter);


						panel.addView(tvHeader);
						panel.addView(tvDetail);
						panel.addView(gl);
                		
                		/*
                		GridView gv = new GridView(context);                		
                		gv.setLayoutParams(params);
                		gv.setNumColumns(2);
                		gv.setGravity(Gravity.LEFT);
                		gv.setColumnWidth(70);
                		gv.setClickable(false);
                		gv.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
                		       
                		adapterInfo=new LazyAdapterSurveyInfo(Activity_Survey.this, dataArray);
                		gv.setAdapter(adapterInfo);
                		
                 		panel.addView(gv);
                 		
                 		*/

						TextView tvTitle = new TextView(mContext);
						tvTitle.setText("Lokasi Check In");
						tvTitle.setLayoutParams(params);
						tvTitle.setTextColor(mContext.getResources().getColor(R.color.label_color));
						tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
						tvTitle.setTypeface(null, Typeface.BOLD);
						panel.addView(tvTitle);

						radiogroup = new RadioGroup(mContext);
						radiogroup.setOrientation(RadioGroup.HORIZONTAL);

						RadioButton[] rbtResult = new RadioButton[2];
						rbtResult[0] = new RadioButton(mContext);
						rbtResult[0].setText("Indoor     ");
						rbtResult[0].setId(0);
						rbtResult[0].setTextColor(getResources().getColor(R.color.label_color));
						rbtResult[0].setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
						radiogroup.addView(rbtResult[0]);
						rbtResult[1] = new RadioButton(mContext);
						rbtResult[1].setText("Outdoor     ");
						rbtResult[1].setId(1);
						rbtResult[1].setTextColor(getResources().getColor(R.color.label_color));
						rbtResult[1].setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
						radiogroup.addView(rbtResult[1]);
						radiogroup.check(0);
						radiogroup.setPadding(0, 0, 0, 10);
						panel.addView(radiogroup);

						TableLayout tl = new TableLayout(mContext);
						tl.setLayoutParams(params);
						TableRow row;

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

							t1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
							t2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
							t3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

							row.addView(t1);
							row.addView(t2);
							row.addView(t3);

							tl.addView(row, params);
						}

						tl.setPadding(0, 0, 0, 10);
						panel.addView(tl);

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

						View view = View.inflate(mContext, R.layout.map, null);
						panel.addView(view);

						btnMyLocation = new Button(mContext, null, android.R.attr.buttonStyleSmall);
						btnMyLocation.setText("My Location");
						btnMyLocation.setLayoutParams(params);
						btnMyLocation.setPadding(0, 0, 0, 0);
						btnMyLocation.setTypeface(null, Typeface.BOLD);
						btnMyLocation.setBackgroundColor(ContextCompat.getColor(mContext, (R.color.md_white_12)));
						btnMyLocation.setTextColor(getResources().getColor(R.color.white));
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
						btnCheckIn.setBackgroundColor(ContextCompat.getColor(mContext, (R.color.md_white_12)));
						btnCheckIn.setTextColor(getResources().getColor(R.color.white));
						//btnCheckIn.setOnClickListener(checkInOnClickListener);
						btnCheckIn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								CheckInPosition();
							}
						});
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
					public View createTabContent(String tag) {
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
						Log.d(TAG, "createTabContent: cek tipe customer di add data" + custType);

						if (custType.equals("K")) {
							Log.d(TAG, "createTabContent: cek tipe customer di add data" + custType);
							TextView tv = new TextView(mContext);
							tv.setTextColor(getResources().getColor(R.color.label_color));
							tv.setText("Nama Lengkap (Tanpa Singkatan /  Jabatan)");
							panel.addView(tv);

							edt_namalengkap = new EditText(mContext);
							edt_namalengkap.setHint("Nama Lengkap (Tanpa Singkatan/Jabatan)");
							edt_namalengkap.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_namalengkap.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_namalengkap.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_namalengkap.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							edt_namalengkap.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							panel.addView(edt_namalengkap);


							TextView tv1 = new TextView(mContext);
							tv1.setTextColor(getResources().getColor(R.color.label_color));
							tv1.setText("Email");
							panel.addView(tv1);

							edt_email = new EditText(mContext);
							edt_email.setHint("Email");
							edt_email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
							edt_email.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_email.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							panel.addView(edt_email);


							TextView tv2 = new TextView(mContext);
							tv2.setTextColor(getResources().getColor(R.color.label_color));
							tv2.setText("Alamat Tempat Tinggal Sesuai Domisili");
							panel.addView(tv2);

							edt_alamattempattinggal = new EditText(mContext);
							edt_alamattempattinggal.setHint("Alamat Tempat Tinggal Sesuai Domisili");
							edt_alamattempattinggal.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_alamattempattinggal.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_alamattempattinggal.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							edt_alamattempattinggal.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_alamattempattinggal.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							edt_alamattempattinggal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
								@Override
								public void onFocusChange(View v, boolean hasFocus) {
									if (edt_email.equals("")) {

									} else {
										validateEmail();
									}

								}
							});
							panel.addView(edt_alamattempattinggal);


							TextView tv3 = new TextView(mContext);
							tv3.setTextColor(getResources().getColor(R.color.label_color));
							tv3.setText("RT");
							panel.addView(tv3);

							edt_rt = new EditText(mContext);
							edt_rt.setHint("RT");
							edt_rt.setInputType(InputType.TYPE_CLASS_NUMBER);
							edt_rt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
							edt_rt.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							panel.addView(edt_rt);

							TextView tv4 = new TextView(mContext);
							tv4.setTextColor(getResources().getColor(R.color.label_color));
							tv4.setText("RW");
							panel.addView(tv4);

							edt_rw = new EditText(mContext);
							edt_rw.setHint("RW");
							edt_rw.setInputType(InputType.TYPE_CLASS_NUMBER);
							edt_rw.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
							edt_rw.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							panel.addView(edt_rw);

							TextView tv24 = new TextView(mContext);
							tv24.setTextColor(getResources().getColor(R.color.label_color));
							tv24.setPadding(5, 5, 0, 0);
							tv24.setText("Kelurahan");
							panel.addView(tv24);

							edt_kelurahan = new EditText(mContext);
							edt_kelurahan.setHint("Kelurahan");

							edt_kelurahan.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_kelurahan.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_kelurahan.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							edt_kelurahan.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_kelurahan.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							panel.addView(edt_kelurahan);


							TextView tv23 = new TextView(mContext);
							tv23.setTextColor(getResources().getColor(R.color.label_color));
							tv23.setPadding(5, 5, 0, 0);
							tv23.setText("Kecamatan");
							panel.addView(tv23);

							edt_kecamatan = new EditText(mContext);
							edt_kecamatan.setHint("Kecamatan");
							edt_kecamatan.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_kecamatan.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_kecamatan.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							edt_kecamatan.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_kecamatan.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							panel.addView(edt_kecamatan);


							TextView tv22 = new TextView(mContext);
							tv22.setTextColor(getResources().getColor(R.color.label_color));
							tv22.setPadding(5, 5, 0, 0);
							tv22.setText("Kota");
							panel.addView(tv22);


							edt_kota = new EditText(mContext);
							edt_kota.setHint("Kota");
							edt_kota.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_kota.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_kota.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							edt_kota.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_kota.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							panel.addView(edt_kota);


//							TextView tv21 = new TextView(mContext);
//							tv21.setTextColor(getResources().getColor(R.color.label_color));
//							tv21.setPadding(5, 5, 0, 0);
//							tv21.setText("ZipCode");
//							panel.addView(tv21);
//
//
//							edt_zipcode = new EditText(mContext);
//							edt_zipcode.setHint("ZipCode");
//							edt_zipcode.setInputType(InputType.TYPE_CLASS_NUMBER);
//							edt_zipcode.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6)});
//							edt_zipcode.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
//							panel.addView(edt_zipcode);
//

//							TextView tv124 = new TextView(mContext);
//							tv124.setTextColor(getResources().getColor(R.color.label_color));
//							tv124.setText("Alamat Perusahaan Tempat Customer Bekerja");
//							panel.addView(tv124);
//
//							edt_alamattempattinggalkantor = new EditText(mContext);
//							edt_alamattempattinggalkantor.setHint("Alamat Perusahaan Tempat Customer Bekerja");
//							edt_alamattempattinggalkantor.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
//							edt_alamattempattinggalkantor.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT );
//							edt_alamattempattinggalkantor.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
//							panel.addView(edt_alamattempattinggalkantor);


//							TextView tv31 = new TextView(mContext);
//							tv31.setTextColor(getResources().getColor(R.color.label_color));
//							tv31.setText("Rt Perusahaan");
//							panel.addView(tv31);
//
//							edt_rtperusahaan = new EditText(mContext);
//							edt_rtperusahaan.setHint("Rt Perusahaan");
//							edt_rtperusahaan.setInputType(InputType.TYPE_CLASS_NUMBER);
//							edt_rtperusahaan.setFilters(new InputFilter[] { new InputFilter.LengthFilter(3)});
//							edt_rtperusahaan.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
//							panel.addView(edt_rtperusahaan);
//
//							TextView tv41 = new TextView(mContext);
//							tv41.setTextColor(getResources().getColor(R.color.label_color));
//							tv41.setText("Rw Perusahaan");
//							panel.addView(tv41);
//
//							edt_rwperusahaan = new EditText(mContext);
//							edt_rwperusahaan.setHint("Rw Perusahaan");
//							edt_rwperusahaan.setInputType(InputType.TYPE_CLASS_NUMBER);
//							edt_rwperusahaan.setFilters(new InputFilter[] { new InputFilter.LengthFilter(3)});
//							edt_rwperusahaan.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
//							panel.addView(edt_rwperusahaan);
//
//
//
//							TextView tv222 = new TextView(mContext);
//							tv222.setTextColor(getResources().getColor(R.color.label_color));
//							tv222.setPadding(5, 5, 0, 0);
//							tv222.setText("Kota ( Perusahaan )");
//							panel.addView(tv222);
//
//
//
//							edt_kotaperusahaan = new EditText(mContext);
//							edt_kotaperusahaan.setHint("Kota  ( Perusahaan )");
//							edt_kotaperusahaan.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT );
//							edt_kotaperusahaan.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
//							edt_kotaperusahaan.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
//							panel.addView(edt_kotaperusahaan);
//
//
//							TextView tv233 = new TextView(mContext);
//							tv233.setTextColor(getResources().getColor(R.color.label_color));
//							tv233.setPadding(5, 5, 0, 0);
//							tv233.setText("Kecamatan ( Perusahaan )");
//							panel.addView(tv233);
//
//
//							edt_kecamatanperusahaan = new EditText(mContext);
//							edt_kecamatanperusahaan.setHint("Kecamatan  ( Perusahaan )");
//							edt_kecamatanperusahaan.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT );
//							edt_kecamatanperusahaan.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
//							edt_kecamatanperusahaan.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
//							panel.addView(edt_kecamatanperusahaan);
//
//
//
//							TextView tv241 = new TextView(mContext);
//							tv241.setTextColor(getResources().getColor(R.color.label_color));
//							tv241.setPadding(5, 5, 0, 0);
//							tv241.setText("Kelurahan ( Perusahaan )");
//							panel.addView(tv241);
//
//							edt_kelurahanperusahaan = new EditText(mContext);
//							edt_kelurahanperusahaan.setHint("Kelurahan  ( Perusahaan )");
//							edt_kelurahanperusahaan.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT );
//							edt_kelurahanperusahaan.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
//							edt_kelurahanperusahaan.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
//							panel.addView(edt_kelurahanperusahaan);
//
//
//							TextView tv212 = new TextView(mContext);
//							tv212.setTextColor(getResources().getColor(R.color.label_color));
//							tv212.setPadding(5, 5, 0, 0);
//							tv212.setText("ZipCode Perusahaan");
//							panel.addView(tv212);
//
//
//							edt_zipcodeperusahaan = new EditText(mContext);
//							edt_zipcodeperusahaan.setHint("ZipCode Perusahaan");
//							edt_zipcodeperusahaan.setInputType(InputType.TYPE_CLASS_NUMBER);
//							edt_zipcodeperusahaan.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6)});
//							edt_zipcodeperusahaan.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
//							panel.addView(edt_zipcodeperusahaan);


						} else if (custType.equals("W")) {
							Log.d(TAG, "createTabContent: cek tipe customer di add data w " + custType);
							TextView tv = new TextView(mContext);
							tv.setTextColor(getResources().getColor(R.color.label_color));
							tv.setText("Nama Lengkap (Tanpa Singkatan /  Jabatan)");
							panel.addView(tv);

							edt_namalengkapW = new EditText(mContext);
							edt_namalengkapW.setHint("Nama Lengkap (Tanpa Singkatan/Jabatan)");
							edt_namalengkapW.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_namalengkapW.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_namalengkapW.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							edt_namalengkapW.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_namalengkapW.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							panel.addView(edt_namalengkapW);


							TextView tv12 = new TextView(mContext);
							tv12.setTextColor(getResources().getColor(R.color.label_color));
							tv12.setText("No handphone Pasangan");
							panel.addView(tv12);

							edt_PhoneNoW = new EditText(mContext);
							edt_PhoneNoW.setHint("No handphone Pasangan");
							edt_PhoneNoW.setInputType(InputType.TYPE_CLASS_PHONE);
							edt_PhoneNoW.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
							edt_PhoneNoW.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							panel.addView(edt_PhoneNoW);


							TextView tv1 = new TextView(mContext);
							tv1.setTextColor(getResources().getColor(R.color.label_color));
							tv1.setText("Email");
							panel.addView(tv1);

							edt_emailW = new EditText(mContext);
							edt_emailW.setHint("Email");
							edt_emailW.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
							edt_emailW.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_emailW.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							panel.addView(edt_emailW);

							TextView tv13 = new TextView(mContext);
							tv13.setTextColor(getResources().getColor(R.color.label_color));
							tv13.setText("Nama Gadis Ibu Kandung");
							panel.addView(tv13);

							edt_MotherMaidenNameW = new EditText(mContext);
							edt_MotherMaidenNameW.setHint("Nama Gadis Ibu Kandung");
							edt_MotherMaidenNameW.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_MotherMaidenNameW.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_MotherMaidenNameW.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_MotherMaidenNameW.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							edt_MotherMaidenNameW.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							edt_MotherMaidenNameW.setOnFocusChangeListener(new View.OnFocusChangeListener() {
								@Override
								public void onFocusChange(View v, boolean hasFocus) {
									if (edt_emailW.equals("")) {

									} else {
										validateEmail2();
									}


								}
							});
							panel.addView(edt_MotherMaidenNameW);

							TextView tv14 = new TextView(mContext);
							tv14.setTextColor(getResources().getColor(R.color.label_color));
							tv14.setText("Ket. Nama Ibu Kandung");
							panel.addView(tv14);

							edt_KetMotherNameW = new EditText(mContext);
							edt_KetMotherNameW.setHint("Ket. Nama Ibu Kandung");
							edt_KetMotherNameW.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_KetMotherNameW.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_KetMotherNameW.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							edt_KetMotherNameW.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_KetMotherNameW.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							panel.addView(edt_KetMotherNameW);


							TextView tv2 = new TextView(mContext);
							tv2.setTextColor(getResources().getColor(R.color.label_color));
							tv2.setText("Alamat Tempat Tinggal Sesuai Domisili");
							panel.addView(tv2);

							edt_alamattempattinggalW = new EditText(mContext);
							edt_alamattempattinggalW.setHint("Alamat Tempat Tinggal Sesuai Domisili");
							edt_alamattempattinggalW.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_alamattempattinggalW.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_alamattempattinggalW.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							edt_alamattempattinggalW.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_alamattempattinggalW.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							panel.addView(edt_alamattempattinggalW);


							TextView tv3 = new TextView(mContext);
							tv3.setTextColor(getResources().getColor(R.color.label_color));
							tv3.setText("RT");
							panel.addView(tv3);

							edt_rtW = new EditText(mContext);
							edt_rtW.setHint("RT");
							edt_rtW.setInputType(InputType.TYPE_CLASS_NUMBER);
							edt_rtW.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
							edt_rtW.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							panel.addView(edt_rtW);

							TextView tv4 = new TextView(mContext);
							tv4.setTextColor(getResources().getColor(R.color.label_color));
							tv4.setText("RW");
							panel.addView(tv4);

							edt_rwW = new EditText(mContext);
							edt_rwW.setHint("RW");
							edt_rwW.setInputType(InputType.TYPE_CLASS_NUMBER);
							edt_rwW.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
							edt_rwW.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							panel.addView(edt_rwW);

							TextView tv24 = new TextView(mContext);
							tv24.setTextColor(getResources().getColor(R.color.label_color));
							tv24.setPadding(5, 5, 0, 0);
							tv24.setText("Kelurahan");
							panel.addView(tv24);


							edt_kelurahanW = new EditText(mContext);
							edt_kelurahanW.setHint("Kelurahan");
							edt_kelurahanW.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_kelurahanW.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_kelurahanW.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							edt_kelurahanW.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_kelurahanW.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							panel.addView(edt_kelurahanW);


							TextView tv23 = new TextView(mContext);
							tv23.setTextColor(getResources().getColor(R.color.label_color));
							tv23.setPadding(5, 5, 0, 0);
							tv23.setText("Kecamatan");
							panel.addView(tv23);


							edt_kecamatanW = new EditText(mContext);
							edt_kecamatanW.setHint("Kecamatan");
							edt_kecamatanW.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_kecamatanW.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_kecamatanW.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							edt_kecamatanW.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_kecamatanW.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							panel.addView(edt_kecamatanW);


							TextView tv22 = new TextView(mContext);
							tv22.setTextColor(getResources().getColor(R.color.label_color));
							tv22.setPadding(5, 5, 0, 0);
							tv22.setText("Kota");
							panel.addView(tv22);


							edt_kotaW = new EditText(mContext);
							edt_kotaW.setHint("Kota");
							edt_kotaW.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_kotaW.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_kotaW.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							edt_kotaW.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_kotaW.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							panel.addView(edt_kotaW);


//							TextView tv21 = new TextView(mContext);
//							tv21.setTextColor(getResources().getColor(R.color.label_color));
//							tv21.setPadding(5, 5, 0, 0);
//							tv21.setText("ZipCode");
//							panel.addView(tv21);
//
//
//							edt_zipcodeW = new EditText(mContext);
//							edt_zipcodeW.setHint("ZipCode");
//							edt_zipcodeW.setInputType(InputType.TYPE_CLASS_NUMBER);
//							edt_zipcodeW.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6)});
//							edt_zipcodeW.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
//							panel.addView(edt_zipcodeW);


							TextView tv1243 = new TextView(mContext);
							tv1243.setTextColor(getResources().getColor(R.color.label_color));
							tv1243.setText("Nama Perusahaan Tempat Customer Bekerja");
							panel.addView(tv1243);

							edt_namatempattinggalkantorW = new EditText(mContext);
							edt_namatempattinggalkantorW.setHint("Nama Perusahaan Tempat Customer Bekerja");
							edt_namatempattinggalkantorW.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_namatempattinggalkantorW.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_namatempattinggalkantorW.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							edt_namatempattinggalkantorW.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_namatempattinggalkantorW.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});

							panel.addView(edt_namatempattinggalkantorW);


							TextView tv124 = new TextView(mContext);
							tv124.setTextColor(getResources().getColor(R.color.label_color));
							tv124.setText("Alamat Perusahaan Tempat Customer Bekerja");
							panel.addView(tv124);

							edt_alamattempattinggalkantorW = new EditText(mContext);
							edt_alamattempattinggalkantorW.setHint("Alamat Perusahaan Tempat Customer Bekerja");
							edt_alamattempattinggalkantorW.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_alamattempattinggalkantorW.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500)});
							edt_alamattempattinggalkantorW.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_alamattempattinggalkantorW.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));

							edt_alamattempattinggalkantorW.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_alamattempattinggalkantorW.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							panel.addView(edt_alamattempattinggalkantorW);


							TextView tv31 = new TextView(mContext);
							tv31.setTextColor(getResources().getColor(R.color.label_color));
							tv31.setText("RT Perusahaan");
							panel.addView(tv31);

							edt_rtperusahaanW = new EditText(mContext);
							edt_rtperusahaanW.setHint("RT Perusahaan");
							edt_rtperusahaanW.setInputType(InputType.TYPE_CLASS_NUMBER);
							edt_rtperusahaanW.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
							edt_rtperusahaanW.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							panel.addView(edt_rtperusahaanW);

							TextView tv41 = new TextView(mContext);
							tv41.setTextColor(getResources().getColor(R.color.label_color));
							tv41.setText("RW Perusahaan");
							panel.addView(tv41);

							edt_rwperusahaanW = new EditText(mContext);
							edt_rwperusahaanW.setHint("RW Perusahaan");
							edt_rwperusahaanW.setInputType(InputType.TYPE_CLASS_NUMBER);
							edt_rwperusahaanW.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
							edt_rwperusahaanW.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							panel.addView(edt_rwperusahaanW);


							TextView tv241 = new TextView(mContext);
							tv241.setTextColor(getResources().getColor(R.color.label_color));
							tv241.setPadding(5, 5, 0, 0);
							tv241.setText("Kelurahan ( Perusahaan )");
							panel.addView(tv241);

							edt_kelurahanperusahaanW = new EditText(mContext);
							edt_kelurahanperusahaanW.setHint("Kelurahan ( Perusahaan )");
							edt_kelurahanperusahaanW.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_kelurahanperusahaanW.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_kelurahanperusahaanW.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							edt_kelurahanperusahaanW.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_kelurahanperusahaanW.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							panel.addView(edt_kelurahanperusahaanW);


							TextView tv233 = new TextView(mContext);
							tv233.setTextColor(getResources().getColor(R.color.label_color));
							tv233.setPadding(5, 5, 0, 0);
							tv233.setText("Kecamatan ( Perusahaan )");
							panel.addView(tv233);

							edt_kecamatanperusahaanW = new EditText(mContext);
							edt_kecamatanperusahaanW.setHint("Kecamatan ( Perusahaan )");
							edt_kecamatanperusahaanW.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_kecamatanperusahaanW.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_kecamatanperusahaanW.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							edt_kecamatanperusahaanW.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_kecamatanperusahaanW.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							panel.addView(edt_kecamatanperusahaanW);


							TextView tv222 = new TextView(mContext);
							tv222.setTextColor(getResources().getColor(R.color.label_color));
							tv222.setPadding(5, 5, 0, 0);
							tv222.setText("Kota ( Perusahaan )");
							panel.addView(tv222);


							edt_kotaperusahaanW = new EditText(mContext);
							edt_kotaperusahaanW.setHint("Kota ( Perusahaan )");
							edt_kotaperusahaanW.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_kotaperusahaanW.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_kotaperusahaanW.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							edt_kotaperusahaanW.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_kotaperusahaanW.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							panel.addView(edt_kotaperusahaanW);


//							TextView tv212 = new TextView(mContext);
//							tv212.setTextColor(getResources().getColor(R.color.label_color));
//							tv212.setPadding(5, 5, 0, 0);
//							tv212.setText("ZipCode Perusahaan");
//							panel.addView(tv212);
//
//
//							edt_zipcodeperusahaanW = new EditText(mContext);
//							edt_zipcodeperusahaanW.setHint("ZipCode Perusahaan");
//							edt_zipcodeperusahaanW.setInputType(InputType.TYPE_CLASS_NUMBER);
//							edt_zipcodeperusahaanW.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6)});
//							edt_zipcodeperusahaanW.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
//							panel.addView(edt_zipcodeperusahaanW);


						} else if (custType.equals("B")) {
							Log.d(TAG, "createTabContent: cek tipe customer di add data w " + custType);
							TextView tv1B = new TextView(mContext);
							tv1B.setTextColor(getResources().getColor(R.color.label_color));
							tv1B.setText("Nama Badan Usaha Sesuai Akta");
							panel.addView(tv1B);

							edt_CompanyNameB = new EditText(mContext);
							edt_CompanyNameB.setHint("Nama Badan Usaha Sesuai Akta");
							edt_CompanyNameB.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_CompanyNameB.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_CompanyNameB.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							edt_CompanyNameB.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_CompanyNameB.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							panel.addView(edt_CompanyNameB);


							TextView tv2 = new TextView(mContext);
							tv2.setTextColor(getResources().getColor(R.color.label_color));
							tv2.setPadding(5, 5, 0, 0);
							tv2.setText("Jenis Perusahaan (Tbk & Non)");
							panel.addView(tv2);

							radiogroup2 = new RadioGroup(mContext);
							radiogroup2.setOrientation(RadioGroup.HORIZONTAL);

							final RadioButton[] rbtResult = new RadioButton[2];
							rbtResult[0] = new RadioButton(mContext);
							rbtResult[0].setText("TBK         ");
							rbtResult[0].setId(0);
							rbtResult[0].setTextColor(getResources().getColor(R.color.label_color));
							radiogroup2.addView(rbtResult[0]);

							rbtResult[1] = new RadioButton(mContext);
							rbtResult[1].setText("Non TBK         ");
							rbtResult[1].setId(1);
							rbtResult[1].setTextColor(getResources().getColor(R.color.label_color));
							radiogroup2.addView(rbtResult[1]);
							radiogroup2.check(0);
							panel.addView(radiogroup2);


							TextView tv12B = new TextView(mContext);
							tv12B.setTextColor(getResources().getColor(R.color.label_color));
							tv12B.setText("Alamat Legal");
							panel.addView(tv12B);

							edt_LegalAddressB = new EditText(mContext);
							edt_LegalAddressB.setHint("Alamat Legal");
							edt_LegalAddressB.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_LegalAddressB.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_LegalAddressB.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							edt_LegalAddressB.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_LegalAddressB.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							panel.addView(edt_LegalAddressB);


							TextView tv3B = new TextView(mContext);
							tv3B.setTextColor(getResources().getColor(R.color.label_color));
							tv3B.setText("RT");
							panel.addView(tv3B);

							edt_LegalrtB = new EditText(mContext);
							edt_LegalrtB.setHint("RT");
							edt_LegalrtB.setInputType(InputType.TYPE_CLASS_NUMBER);
							edt_LegalrtB.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
							edt_LegalrtB.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							panel.addView(edt_LegalrtB);

							TextView tv4 = new TextView(mContext);
							tv4.setTextColor(getResources().getColor(R.color.label_color));
							tv4.setText("RW");
							panel.addView(tv4);

							edt_LegalrwB = new EditText(mContext);
							edt_LegalrwB.setHint("RW");
							edt_LegalrwB.setInputType(InputType.TYPE_CLASS_NUMBER);
							edt_LegalrwB.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
							edt_LegalrwB.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							panel.addView(edt_LegalrwB);

							TextView tv24 = new TextView(mContext);
							tv24.setTextColor(getResources().getColor(R.color.label_color));
							tv24.setPadding(5, 5, 0, 0);
							tv24.setText("Kelurahan");
							panel.addView(tv24);

							edt_LegalkelurahanB = new EditText(mContext);
							edt_LegalkelurahanB.setHint("Kelurahan");
							edt_LegalkelurahanB.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

							edt_LegalkelurahanB.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_LegalkelurahanB.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});

							edt_LegalkelurahanB.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_LegalkelurahanB.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							panel.addView(edt_LegalkelurahanB);


							TextView tv23 = new TextView(mContext);
							tv23.setTextColor(getResources().getColor(R.color.label_color));
							tv23.setPadding(5, 5, 0, 0);
							tv23.setText("Kecamatan");
							panel.addView(tv23);


							edt_LegalkecamatanB = new EditText(mContext);
							edt_LegalkecamatanB.setHint("Kecamatan");
							edt_LegalkecamatanB.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_LegalkecamatanB.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_LegalkecamatanB.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							edt_LegalkecamatanB.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_LegalkecamatanB.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							panel.addView(edt_LegalkecamatanB);


							TextView tv22 = new TextView(mContext);
							tv22.setTextColor(getResources().getColor(R.color.label_color));
							tv22.setPadding(5, 5, 0, 0);
							tv22.setText("Kota");
							panel.addView(tv22);


							edt_LegalkotaB = new EditText(mContext);
							edt_LegalkotaB.setHint("Kota");
							edt_LegalkotaB.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_LegalkotaB.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_LegalkotaB.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							edt_LegalkotaB.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_LegalkotaB.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							panel.addView(edt_LegalkotaB);

//
//							TextView tv21 = new TextView(mContext);
//							tv21.setTextColor(getResources().getColor(R.color.label_color));
//							tv21.setPadding(5, 5, 0, 0);
//							tv21.setText("ZipCode");
//							panel.addView(tv21);
//
//
//							edt_LegalzipcodeB = new EditText(mContext);
//							edt_LegalzipcodeB.setHint("ZipCode");
//							edt_LegalzipcodeB.setInputType(InputType.TYPE_CLASS_NUMBER);
//							edt_LegalzipcodeB.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6)});
//							edt_LegalzipcodeB.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
//							panel.addView(edt_LegalzipcodeB);


							TextView tv1243b = new TextView(mContext);
							tv1243b.setTextColor(getResources().getColor(R.color.label_color));
							tv1243b.setText("Alamat Usaha");
							panel.addView(tv1243b);

							edt_DomisiliAddressB = new EditText(mContext);
							edt_DomisiliAddressB.setHint("Alamat Usaha");
							edt_DomisiliAddressB.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_DomisiliAddressB.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500)});
							edt_DomisiliAddressB.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_DomisiliAddressB.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							edt_DomisiliAddressB.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_DomisiliAddressB.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							panel.addView(edt_DomisiliAddressB);


							TextView tv31B = new TextView(mContext);
							tv31B.setTextColor(getResources().getColor(R.color.label_color));
							tv31B.setText("RT  ( Usaha )");
							panel.addView(tv31B);

							edt_DomisiliRTB = new EditText(mContext);
							edt_DomisiliRTB.setHint("RT ( Usaha )");
							edt_DomisiliRTB.setInputType(InputType.TYPE_CLASS_NUMBER);
							edt_DomisiliRTB.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
							edt_DomisiliRTB.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							panel.addView(edt_DomisiliRTB);

							TextView tv41 = new TextView(mContext);
							tv41.setTextColor(getResources().getColor(R.color.label_color));
							tv41.setText("RW ( Usaha )");
							panel.addView(tv41);

							edt_DomisiliRWB = new EditText(mContext);
							edt_DomisiliRWB.setHint("RW ( Usaha )");
							edt_DomisiliRWB.setInputType(InputType.TYPE_CLASS_NUMBER);
							edt_DomisiliRWB.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
							edt_DomisiliRWB.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							panel.addView(edt_DomisiliRWB);


							TextView tv241 = new TextView(mContext);
							tv241.setTextColor(getResources().getColor(R.color.label_color));
							tv241.setPadding(5, 5, 0, 0);
							tv241.setText("Kelurahan ( Usaha )");
							panel.addView(tv241);


							edt_DomisilikelurahanB = new EditText(mContext);
							edt_DomisilikelurahanB.setHint("Kelurahan  ( Usaha )");
							edt_DomisilikelurahanB.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_DomisilikelurahanB.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_DomisilikelurahanB.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							edt_DomisilikelurahanB.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_DomisilikelurahanB.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							panel.addView(edt_DomisilikelurahanB);


							TextView tv233 = new TextView(mContext);
							tv233.setTextColor(getResources().getColor(R.color.label_color));
							tv233.setPadding(5, 5, 0, 0);
							tv233.setText("Kecamatan ( Usaha )");
							panel.addView(tv233);


							edt_DomisilikecamatanB = new EditText(mContext);
							edt_DomisilikecamatanB.setHint("Kecamatan  ( Usaha )");
							edt_DomisilikecamatanB.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_DomisilikecamatanB.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_DomisilikecamatanB.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							edt_DomisilikecamatanB.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_DomisilikecamatanB.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							panel.addView(edt_DomisilikecamatanB);

							TextView tv222 = new TextView(mContext);
							tv222.setTextColor(getResources().getColor(R.color.label_color));
							tv222.setPadding(5, 5, 0, 0);
							tv222.setText("Kota ( Usaha )");
							panel.addView(tv222);


							edt_DomisilikotaB = new EditText(mContext);
							edt_DomisilikotaB.setHint("Kota  ( Usaha )");
							edt_DomisilikotaB.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
							edt_DomisilikotaB.addTextChangedListener(new TextWatcher() {
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									if (s.toString().contains("'")) {
										edt_DomisilikotaB.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
									}
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
							edt_DomisilikotaB.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_CLASS_TEXT);
							edt_DomisilikotaB.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
							panel.addView(edt_DomisilikotaB);

//							TextView tv212 = new TextView(mContext);
//							tv212.setTextColor(getResources().getColor(R.color.label_color));
//							tv212.setPadding(5, 5, 0, 0);
//							tv212.setText("ZipCode ( Usaha )");
//							panel.addView(tv212);
//
//
//							edt_DomisilizipcodeB = new EditText(mContext);
//							edt_DomisilizipcodeB.setHint("ZipCode ( Usaha )");
//							edt_DomisilizipcodeB.setInputType(InputType.TYPE_CLASS_NUMBER);
//							edt_DomisilizipcodeB.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6)});
//							edt_DomisilizipcodeB.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//									LinearLayout.LayoutParams.WRAP_CONTENT, 1));
//							panel.addView(edt_DomisilizipcodeB);

						}

						try {
							db = datasource.getWritableDatabase();

							theForm = new Form();

							Cursor c = db.rawQuery("SELECT name, label, type, required, optionsname, optionsid, hint, errmsg, maxlength" +
									", issearchable FROM form where moduleid = '" + moduleid + "' and custtype = '" + custType + "'", null);
							Log.d(TAG, "createTabContent: custType " + custType + " moduleid = " + moduleid);

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

						//panel.addView(View.inflate(mContext, R.layout.image_button2, null));

						int i;
						for (i = 0; i < theForm.fields.size(); i++) {
							if (theForm.fields.elementAt(i).getType().equals("text")) {
								theForm.fields.elementAt(i).obj = new EditBox(mContext, " "
										+ theForm.fields.elementAt(i).getLabel(), "",
										theForm.fields.elementAt(i).getHint(),
										theForm.fields.elementAt(i).getMaxLength());
								((EditBox) theForm.fields.elementAt(i).obj).makeChar();
								if (theForm.fields.elementAt(i).getLabel().equals("Email")) {
									theForm.fields.elementAt(i).obj = new EditBox(mContext, " "
											+ theForm.fields.elementAt(i).getLabel(), "",
											theForm.fields.elementAt(i).getHint(),
											theForm.fields.elementAt(i).getMaxLength());
									((EditBox) theForm.fields.elementAt(i).obj).makeEmail();
								}
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
							} else if (theForm.fields.elementAt(i).getType().equals("pickonesupplier")) {
								theForm.fields.elementAt(i).obj = new PickOneSupplier(mContext, (" " + theForm.fields.elementAt(i).getLabel()), "supplier", "suppliername", "supplierid");
								panel.addView((View) theForm.fields.elementAt(i).obj);
							} else if (theForm.fields.elementAt(i).getType().equals("Pick_Ekonomi")) {
								theForm.fields.elementAt(i).obj = new Pick_Ekonomi(mContext, (" " + theForm.fields.elementAt(i).getLabel()));
								panel.addView((View) theForm.fields.elementAt(i).obj);
							}


						}

						final Button btn_NEXT = new Button(mContext, null, android.R.attr.buttonStyleSmall);
						btn_NEXT.setText("NEXT");
						btn_NEXT.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
						panel.addView(btn_NEXT);
						btn_NEXT.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {


								btn_NEXT.setEnabled(false);
								if (custType.equals("K")) {
									panel.addView(View.inflate(mContext, R.layout.survey_photo_kardanwir, null));
								}
								if (custType.equals("W")) {
									panel.addView(View.inflate(mContext, R.layout.survey_photo_kardanwir, null));
								}

								if (custType.equals("B")) {
									panel.addView(View.inflate(mContext, R.layout.survey_photo, null));
								}


								Button btn = new Button(mContext, null, android.R.attr.buttonStyleSmall);
								btn.setText("Submit");
								btn.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

								btn.setOnClickListener(new Button.OnClickListener() {
									public void onClick(View v) {
										boolean check;
										int imageCount = 0;
										db = datasource.getWritableDatabase();
										locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

										if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
											buildLocationAlert();
											return;
										}

										Cursor cursor = db.rawQuery("SELECT id FROM " + tableImage, null);

										try {
											while (cursor.moveToNext()) {
												imageCount = imageCount + 1;
											}
										} finally {
											cursor.close();
											db.close();
										}

										if (imageCount < 4) {
											Toast.makeText(mContext, "Please pick at least 4 pictures", Toast.LENGTH_SHORT).show();
											return;
										}

										if (isMapImageValid == false) {
											Toast.makeText(mContext, "Please check in first", Toast.LENGTH_SHORT).show();
											return;
										}

//										if (custType.equals("K")){
//											if (edt_email.equals("")){
//
//											}else{
//												validateEmail();
//											}
//										}
//
//										if (custType.equals("W")){
//											if (edt_emailW.equals("")){
//
//											}else{
//												validateEmail2();
//											}
//										}
										try {
											checkpetiksatu();
										} catch (Exception e) {
											e.printStackTrace();
										}

//                                        check = CheckForm();
//
//                                        if (!check) {
//                                            Toast.makeText(mContext, "Enter all required (!) fields", Toast.LENGTH_SHORT).show();
//                                            return;
//                                        } else {
//											Log.d(TAG, "onClick: cek tipe customer "+custType);
//											if (custType.equals("K")){
//												Log.d(TAG, "edt_rwperusahaan: cek tipe customer "+ custType);
//												new PostData().execute();
//												}
//													else if (custType.equals("W")){
//													Log.d(TAG, "onClick: cek tipe customer "+ custType);
//													new PostDataW().execute();
//													}
//													else if (custType.equals("B")){
//													Log.d(TAG, "onClick: cek tipe customer "+ custType);
//													new PostDataB().execute();
//													}
//
//                                        }

									}
								});

								panel.addView(btn);
							}
						});
						sv.addView(panel);
						return sv;
					}
				});

				tabHost.addTab(infoSpec);
				tabHost.addTab(mapSpec);
				tabHost.addTab(formSpec);
				tabHost.getTabWidget().setCurrentTab(0);

				dialog.dismiss();
			} else {
				dialog.dismiss();
				Toast.makeText(mContext, "Connection time out", Toast.LENGTH_SHORT).show();
				finish();
				Intent intent = new Intent(mContext, ListSurveyActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("flagUpdate", "N");
				startActivity(intent);
			}

		}
	}

	//CEK PETIK SATU
	private void checkpetiksatu() {
		boolean check;
		db = datasource.getWritableDatabase();
		try {

			if (custType.equals("K")) {
				Log.d(TAG, "onClick: cek tipe customer " + custType);
				if (edt_alamattempattinggal.getText().toString().contains("'")
						|| edt_kecamatan.getText().toString().contains("'")
						|| edt_kelurahan.getText().toString().contains("'")
						|| edt_kota.getText().toString().contains("'")
						|| edt_namalengkap.getText().toString().contains("'")) {
					edt_alamattempattinggal.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
					edt_kecamatan.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
					edt_kelurahan.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
					edt_kota.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
					edt_namalengkap.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");

				} else {
					Log.d(TAG, "edt_rwperusahaan: cek tipe customer " + custType);
					try {

						if (edt_alamattempattinggal.getText().toString().contains("")
								|| edt_kecamatan.getText().toString().contains("")
								|| edt_kelurahan.getText().toString().contains("")
								|| edt_kota.getText().toString().contains("")
								|| edt_namalengkap.getText().toString().contains("")
								|| edt_rt.getText().toString().contains("")
								|| edt_rw.getText().toString().contains("")) {
							edt_alamattempattinggal.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_kecamatan.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_kelurahan.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_kota.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_namalengkap.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_rt.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_rw.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
						}
						check = CheckForm();
						if (!check) {
							Toast.makeText(mContext, "Enter all required (!) fields", Toast.LENGTH_SHORT).show();
							return;
						} else {
							new PostData().execute();
						}
					} finally {
						db.close();
					}

				}
			} else if (custType.equals("W")) {
				Log.d(TAG, "onClick: cek tipe customer " + custType);
				if (edt_MotherMaidenNameW.getText().toString().contains("'")
						|| edt_KetMotherNameW.getText().toString().contains("'")
						|| edt_kecamatanW.getText().toString().contains("'")
						|| edt_kelurahanW.getText().toString().contains("'")
						|| edt_kotaW.getText().toString().contains("'")
						|| edt_alamattempattinggalkantorW.getText().toString().contains("'")
						|| edt_kecamatanperusahaanW.getText().toString().contains("'")
						|| edt_kelurahanperusahaanW.getText().toString().contains("'")
						|| edt_kotaperusahaanW.getText().toString().contains("'")
						) {
					edt_MotherMaidenNameW.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
					edt_KetMotherNameW.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
					edt_kecamatanW.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
					edt_kelurahanW.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
					edt_kotaW.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
					edt_alamattempattinggalkantorW.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
					edt_kecamatanperusahaanW.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
					edt_kelurahanperusahaanW.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
					edt_kotaperusahaanW.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");

				} else {
					Log.d(TAG, "onClick: cek tipe customer " + custType);
					try {

						if (edt_MotherMaidenNameW.getText().toString().contains("'")
								|| edt_namalengkapW.getText().toString().contains("")
								|| edt_rtW.getText().toString().contains("")
								|| edt_rwW.getText().toString().contains("")
								|| edt_rtperusahaanW.getText().toString().contains("")
								|| edt_rwperusahaanW.getText().toString().contains("")
								|| edt_KetMotherNameW.getText().toString().contains("")
								|| edt_kecamatanW.getText().toString().contains("")
								|| edt_kelurahanW.getText().toString().contains("")
								|| edt_kotaW.getText().toString().contains("")
								|| edt_alamattempattinggalkantorW.getText().toString().contains("")
								|| edt_kecamatanperusahaanW.getText().toString().contains("")
								|| edt_kelurahanperusahaanW.getText().toString().contains("")
								|| edt_kotaperusahaanW.getText().toString().contains("")
								|| edt_PhoneNoW.getText().toString().contains("")

								) {
							edt_namalengkapW.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_rtW.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_rwW.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_rtperusahaanW.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_rwperusahaanW.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_MotherMaidenNameW.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_KetMotherNameW.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_kecamatanW.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_kelurahanW.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_kotaW.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_alamattempattinggalkantorW.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_kecamatanperusahaanW.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_kelurahanperusahaanW.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_kotaperusahaanW.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_PhoneNoW.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
						}
						check = CheckForm();
						if (!check) {
							Toast.makeText(mContext, "Enter all required (!) fields", Toast.LENGTH_SHORT).show();
							return;
						} else {
							new PostDataW().execute();
						}
					} finally {
						db.close();
					}

				}
			} else if (custType.equals("B")) {
				Log.d(TAG, "onClick: cek tipe customer " + custType);
				if (edt_LegalAddressB.getText().toString().contains("'")
						|| edt_LegalkecamatanB.getText().toString().contains("'")
						|| edt_LegalkelurahanB.getText().toString().contains("'")
						|| edt_LegalkotaB.getText().toString().contains("'")) {

					edt_LegalAddressB.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
					edt_LegalkecamatanB.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
					edt_LegalkelurahanB.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
					edt_LegalkotaB.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");

				} else {
					Log.d(TAG, "onClick: cek tipe customer " + custType);
					try {

						if (edt_LegalAddressB.getText().toString().contains("")
								|| edt_LegalkecamatanB.getText().toString().contains("")
								|| edt_LegalkelurahanB.getText().toString().contains("")
								|| edt_LegalkotaB.getText().toString().contains("")

								|| edt_CompanyNameB.getText().toString().contains("")
								|| edt_LegalrtB.getText().toString().contains("")
								|| edt_LegalrwB.getText().toString().contains("")
								|| edt_DomisiliAddressB.getText().toString().contains("")
								|| edt_DomisiliRTB.getText().toString().contains("")
								|| edt_DomisiliRWB.getText().toString().contains("")
								|| edt_DomisilikotaB.getText().toString().contains("")
								|| edt_DomisilikecamatanB.getText().toString().contains("")
								|| edt_DomisilikelurahanB.getText().toString().contains("")
								) {

							edt_LegalAddressB.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_LegalkecamatanB.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_LegalkelurahanB.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_LegalkotaB.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_CompanyNameB.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_LegalrtB.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_LegalrwB.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_DomisiliAddressB.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_DomisiliRTB.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_DomisiliRWB.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_DomisilikotaB.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_DomisilikecamatanB.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
							edt_DomisilikelurahanB.setError("DIHARAPKAN DIISI TERLEBIH DAHULU");
						}
						check = CheckForm();
						if (!check) {
							Toast.makeText(mContext, "Enter all required (!) fields", Toast.LENGTH_SHORT).show();
							return;
						} else {
							new PostDataB().execute();
						}
					} finally {
						db.close();
					}

				}
			}
//			  else{
//				check = CheckForm();
//
//				if (!check) {
//					Toast.makeText(mContext, "Enter all required (!) fields", Toast.LENGTH_SHORT).show();
//					return;
//				} else {
//					Log.d(TAG, "onClick: cek tipe customer "+custType);
//					if (custType.equals("K")){
//						Log.d(TAG, "edt_rwperusahaan: cek tipe customer "+ custType);
//						new PostData().execute();
//					}
//					else if (custType.equals("W")){
//						Log.d(TAG, "onClick: cek tipe customer "+ custType);
//						new PostDataW().execute();
//					}
//					else if (custType.equals("B")){
//						Log.d(TAG, "onClick: cek tipe customer "+ custType);
//						new PostDataB().execute();
//					}
//
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}


	//FUNGSI SPINNER UNTUK KARYAWAN
	private void spin2(String id) {
		ArrayList<String> myResArray2 = new ArrayList<>();
		if (myResArray2 != null) {
			myResArray2.clear();
		}

		myResArray2.add("-Kota-");
		myResArray2.addAll(datasource.getAllKota2(id));


		adapter_Kota = new ArrayAdapter<>(mContext, R.layout.spinner, myResArray2);
		adapter_Kota.notifyDataSetChanged();
		spinner_kota.setAdapter(adapter_Kota);
		spinner_kota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (!spinner_kota.getSelectedItem().toString().equals("-Kota-")) {
					nmkota = spinner_kota.getSelectedItem().toString();
					Log.d(TAG, "onItemSelected: Kota -- " + nmkota);

					spin3(nmzipcode, nmkota);
					Log.d(TAG, "onItemSelected: Kota " + nmkota);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void spin3(String zipcode, String kota) {
		ArrayList<String> myResArray3 = new ArrayList<>();
		if (myResArray3 != null) {
			myResArray3.clear();
		}
		Log.d(TAG, "Pick_Spinner1: nmsupplier" + nmkota);
		myResArray3.add("-Kecamatan-");
		myResArray3.addAll(datasource.getAllKecamatan2(zipcode, kota));
		Log.d(TAG, "Pick_Spinner2: nmsupplier" + nmkota);


		adapter_Kecamatan = new ArrayAdapter<>(mContext, R.layout.spinner, myResArray3);
		adapter_Kecamatan.notifyDataSetChanged();
		spinner_kecamatan.setAdapter(adapter_Kecamatan);
		spinner_kecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (!spinner_kecamatan.getSelectedItem().toString().equals("-Kecamatan-")) {
					nmkecamatan = spinner_kecamatan.getSelectedItem().toString();

					Log.d(TAG, "onItemSelected: Kecamatan -- " + nmkecamatan);

					spin4(nmzipcode, nmkota, nmkecamatan);
					Log.d(TAG, "onItemSelected: Kecamatan " + nmkecamatan);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void spin4(String zipcode, String kota, String kecamatan) {
		ArrayList<String> myResArray4 = new ArrayList<>();
		if (myResArray4 != null) {
			myResArray4.clear();
		}

		myResArray4.add("-Kelurahan-");
		myResArray4.addAll(datasource.getAllKelurahan2(zipcode, kota, kecamatan));

		adapter_Kelurahan = new ArrayAdapter<>(mContext, R.layout.spinner, myResArray4);
		adapter_Kelurahan.notifyDataSetChanged();
		spinner_kelurahan.setAdapter(adapter_Kelurahan);
		spinner_kelurahan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (!spinner_kelurahan.getSelectedItem().toString().equals("-Kelurahan-")) {
					nmkelurahan = spinner_kelurahan.getSelectedItem().toString();

					Log.d(TAG, "onItemSelected: ZipCode -- " + nmkelurahan);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}


	private void spin2perusahaan(String id) {
		ArrayList<String> myResArray22 = new ArrayList<>();
		if (myResArray22 != null) {
			myResArray22.clear();
		}

		myResArray22.add("-Kota-");
		myResArray22.addAll(datasource.getAllKota2(id));

		adapter_Kotaperusahaan = new ArrayAdapter<>(mContext, R.layout.spinner, myResArray22);
		adapter_Kotaperusahaan.notifyDataSetChanged();
		spinner_kotaperusahaan.setAdapter(adapter_Kotaperusahaan);
		spinner_kotaperusahaan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (!spinner_kotaperusahaan.getSelectedItem().toString().equals("-Kota-")) {
					nmkotaperusahaan = spinner_kotaperusahaan.getSelectedItem().toString();
					Log.d(TAG, "onItemSelected: Kotaperusahaan -- " + nmkotaperusahaan);

					spin3perusahaan(nmzipcodeperusahaan, nmkotaperusahaan);
					Log.d(TAG, "onItemSelected: Kotaperusahaan " + nmkotaperusahaan);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void spin3perusahaan(String zipcode, String kota) {
		ArrayList<String> myResArray33 = new ArrayList<>();
		if (myResArray33 != null) {
			myResArray33.clear();
		}

		myResArray33.add("-Kecamatan-");
		myResArray33.addAll(datasource.getAllKecamatan2(zipcode, kota));


		adapter_Kecamatanperusahaan = new ArrayAdapter<>(mContext, R.layout.spinner, myResArray33);
		adapter_Kecamatanperusahaan.notifyDataSetChanged();
		spinner_kecamatanperusahaan.setAdapter(adapter_Kecamatanperusahaan);
		spinner_kecamatanperusahaan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (!spinner_kecamatanperusahaan.getSelectedItem().toString().equals("-Kecamatan-")) {
					nmkecamatanperusahaan = spinner_kecamatanperusahaan.getSelectedItem().toString();

					Log.d(TAG, "onItemSelected: Kecamatan -- " + nmkecamatanperusahaan);

					spin4perusahaan(nmzipcodeperusahaan, nmkotaperusahaan, nmkecamatanperusahaan);
					Log.d(TAG, "onItemSelected: Kecamatan " + nmkecamatanperusahaan);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void spin4perusahaan(String zipcode, String kota, String kecamatan) {
		ArrayList<String> myResArray44 = new ArrayList<>();
		if (myResArray44 != null) {
			myResArray44.clear();
		}

		myResArray44.add("-Kelurahan-");
		myResArray44.addAll(datasource.getAllKelurahan2(zipcode, kota, kecamatan));

		adapter_Kelurahanperusahaan = new ArrayAdapter<>(mContext, R.layout.spinner, myResArray44);
		adapter_Kelurahanperusahaan.notifyDataSetChanged();
		spinner_kelurahanperusahaan.setAdapter(adapter_Kelurahanperusahaan);
		spinner_kelurahanperusahaan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (!spinner_kelurahanperusahaan.getSelectedItem().toString().equals("-Kelurahan-")) {
					nmkelurahanperusahaan = spinner_kelurahanperusahaan.getSelectedItem().toString();

					Log.d(TAG, "onItemSelected: nmkelurahanperusahaan -- " + nmkelurahanperusahaan);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}
	//FUNGSI SPINNER UNTUK KARYAWAN


	//FUNGSI SPINNER UNTUK WIRASWASTA
	private void spin2W(String id) {
		ArrayList<String> myResArray2 = new ArrayList<>();
		if (myResArray2 != null) {
			myResArray2.clear();
		}

		myResArray2.add("-Kota-");
		myResArray2.addAll(datasource.getAllKota2(id));


		adapter_Kota = new ArrayAdapter<>(mContext, R.layout.spinner, myResArray2);
		adapter_Kota.notifyDataSetChanged();
		spinner_kotaW.setAdapter(adapter_Kota);
		spinner_kotaW.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (!spinner_kotaW.getSelectedItem().toString().equals("-Kota-")) {
					nmkota = spinner_kotaW.getSelectedItem().toString();
					Log.d(TAG, "onItemSelected: Kota -- " + nmkota);

					spin3W(nmzipcode, nmkota);
					Log.d(TAG, "onItemSelected: Kota " + nmkota);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void spin3W(String zipcode, String kota) {
		ArrayList<String> myResArray3 = new ArrayList<>();
		if (myResArray3 != null) {
			myResArray3.clear();
		}
		Log.d(TAG, "Pick_Spinner1: nmsupplier" + nmkota);
		myResArray3.add("-Kecamatan-");
		myResArray3.addAll(datasource.getAllKecamatan2(zipcode, kota));
		Log.d(TAG, "Pick_Spinner2: nmsupplier" + nmkota);


		adapter_Kecamatan = new ArrayAdapter<>(mContext, R.layout.spinner, myResArray3);
		adapter_Kecamatan.notifyDataSetChanged();
		spinner_kecamatanW.setAdapter(adapter_Kecamatan);
		spinner_kecamatanW.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (!spinner_kecamatanW.getSelectedItem().toString().equals("-Kecamatan-")) {
					nmkecamatan = spinner_kecamatanW.getSelectedItem().toString();

					Log.d(TAG, "onItemSelected: Kecamatan -- " + nmkecamatan);

					spin4W(nmzipcode, nmkota, nmkecamatan);
					Log.d(TAG, "onItemSelected: Kecamatan " + nmkecamatan);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void spin4W(String zipcode, String kota, String kecamatan) {
		ArrayList<String> myResArray4 = new ArrayList<>();
		if (myResArray4 != null) {
			myResArray4.clear();
		}

		myResArray4.add("-Kelurahan-");
		myResArray4.addAll(datasource.getAllKelurahan2(zipcode, kota, kecamatan));

		adapter_Kelurahan = new ArrayAdapter<>(mContext, R.layout.spinner, myResArray4);
		adapter_Kelurahan.notifyDataSetChanged();
		spinner_kelurahanW.setAdapter(adapter_Kelurahan);
		spinner_kelurahanW.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (!spinner_kelurahanW.getSelectedItem().toString().equals("-Kelurahan-")) {
					nmkelurahan = spinner_kelurahanW.getSelectedItem().toString();

					Log.d(TAG, "onItemSelected: ZipCode -- " + nmkelurahan);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}


	private void spin2perusahaanW(String id) {
		ArrayList<String> myResArray22 = new ArrayList<>();
		if (myResArray22 != null) {
			myResArray22.clear();
		}

		myResArray22.add("-Kota-");
		myResArray22.addAll(datasource.getAllKota2(id));

		adapter_Kotaperusahaan = new ArrayAdapter<>(mContext, R.layout.spinner, myResArray22);
		adapter_Kotaperusahaan.notifyDataSetChanged();
		spinner_kotaperusahaanW.setAdapter(adapter_Kotaperusahaan);
		spinner_kotaperusahaanW.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (!spinner_kotaperusahaanW.getSelectedItem().toString().equals("-Kota-")) {
					nmkotaperusahaan = spinner_kotaperusahaanW.getSelectedItem().toString();
					Log.d(TAG, "onItemSelected: Kotaperusahaan -- " + nmkotaperusahaan);

					spin3perusahaanW(nmzipcodeperusahaan, nmkotaperusahaan);
					Log.d(TAG, "onItemSelected: Kotaperusahaan " + nmkotaperusahaan);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void spin3perusahaanW(String zipcode, String kota) {
		ArrayList<String> myResArray33 = new ArrayList<>();
		if (myResArray33 != null) {
			myResArray33.clear();
		}

		myResArray33.add("-Kecamatan-");
		myResArray33.addAll(datasource.getAllKecamatan2(zipcode, kota));


		adapter_Kecamatanperusahaan = new ArrayAdapter<>(mContext, R.layout.spinner, myResArray33);
		adapter_Kecamatanperusahaan.notifyDataSetChanged();
		spinner_kecamatanperusahaanW.setAdapter(adapter_Kecamatanperusahaan);
		spinner_kecamatanperusahaanW.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (!spinner_kecamatanperusahaanW.getSelectedItem().toString().equals("-Kecamatan-")) {
					nmkecamatanperusahaan = spinner_kecamatanperusahaanW.getSelectedItem().toString();

					Log.d(TAG, "onItemSelected: Kecamatan -- " + nmkecamatanperusahaan);

					spin4perusahaanW(nmzipcodeperusahaan, nmkotaperusahaan, nmkecamatanperusahaan);
					Log.d(TAG, "onItemSelected: Kecamatan " + nmkecamatanperusahaan);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void spin4perusahaanW(String zipcode, String kota, String kecamatan) {
		ArrayList<String> myResArray44 = new ArrayList<>();
		if (myResArray44 != null) {
			myResArray44.clear();
		}

		myResArray44.add("-Kelurahan-");
		myResArray44.addAll(datasource.getAllKelurahan2(zipcode, kota, kecamatan));

		adapter_Kelurahanperusahaan = new ArrayAdapter<>(mContext, R.layout.spinner, myResArray44);
		adapter_Kelurahanperusahaan.notifyDataSetChanged();
		spinner_kelurahanperusahaanW.setAdapter(adapter_Kelurahanperusahaan);
		spinner_kelurahanperusahaanW.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (!spinner_kelurahanperusahaanW.getSelectedItem().toString().equals("-Kelurahan-")) {
					nmkelurahanperusahaan = spinner_kelurahanperusahaanW.getSelectedItem().toString();

					Log.d(TAG, "onItemSelected: nmkelurahanperusahaan -- " + nmkelurahanperusahaan);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	//FUNGSI SPINNER UNTUK WIRASWASTA


	//FUNGSI SPINNER UNTUK BADAN USAHA
	private void spin2B(String id) {
		ArrayList<String> myResArray2 = new ArrayList<>();
		if (myResArray2 != null) {
			myResArray2.clear();
		}

		myResArray2.add("-Kota-");
		myResArray2.addAll(datasource.getAllKota2(id));


		adapter_Kota = new ArrayAdapter<>(mContext, R.layout.spinner, myResArray2);
		adapter_Kota.notifyDataSetChanged();
		spinner_kotaB.setAdapter(adapter_Kota);
		spinner_kotaB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (!spinner_kotaB.getSelectedItem().toString().equals("-Kota-")) {
					nmkota = spinner_kotaB.getSelectedItem().toString();
					Log.d(TAG, "onItemSelected: Kota -- " + nmkota);

					spin3B(nmzipcode, nmkota);
					Log.d(TAG, "onItemSelected: Kota " + nmkota);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void spin3B(String zipcode, String kota) {
		ArrayList<String> myResArray3 = new ArrayList<>();
		if (myResArray3 != null) {
			myResArray3.clear();
		}
		Log.d(TAG, "Pick_Spinner1: nmsupplier" + nmkota);
		myResArray3.add("-Kecamatan-");
		myResArray3.addAll(datasource.getAllKecamatan2(zipcode, kota));
		Log.d(TAG, "Pick_Spinner2: nmsupplier" + nmkota);


		adapter_Kecamatan = new ArrayAdapter<>(mContext, R.layout.spinner, myResArray3);
		adapter_Kecamatan.notifyDataSetChanged();
		spinner_kecamatanB.setAdapter(adapter_Kecamatan);
		spinner_kecamatanB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (!spinner_kecamatanB.getSelectedItem().toString().equals("-Kecamatan-")) {
					nmkecamatan = spinner_kecamatanB.getSelectedItem().toString();

					Log.d(TAG, "onItemSelected: Kecamatan -- " + nmkecamatan);

					spin4B(nmzipcode, nmkota, nmkecamatan);
					Log.d(TAG, "onItemSelected: Kecamatan " + nmkecamatan);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void spin4B(String zipcode, String kota, String kecamatan) {
		ArrayList<String> myResArray4 = new ArrayList<>();
		if (myResArray4 != null) {
			myResArray4.clear();
		}

		myResArray4.add("-Kelurahan-");
		myResArray4.addAll(datasource.getAllKelurahan2(zipcode, kota, kecamatan));

		adapter_Kelurahan = new ArrayAdapter<>(mContext, R.layout.spinner, myResArray4);
		adapter_Kelurahan.notifyDataSetChanged();
		spinner_kelurahanB.setAdapter(adapter_Kelurahan);
		spinner_kelurahanB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (!spinner_kelurahanB.getSelectedItem().toString().equals("-Kelurahan-")) {
					nmkelurahan = spinner_kelurahanB.getSelectedItem().toString();

					Log.d(TAG, "onItemSelected: ZipCode -- " + nmkelurahan);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}


	private void spin2perusahaanB(String id) {
		ArrayList<String> myResArray22 = new ArrayList<>();
		if (myResArray22 != null) {
			myResArray22.clear();
		}

		myResArray22.add("-Kota-");
		myResArray22.addAll(datasource.getAllKota2(id));

		adapter_Kotaperusahaan = new ArrayAdapter<>(mContext, R.layout.spinner, myResArray22);
		adapter_Kotaperusahaan.notifyDataSetChanged();
		spinner_kotaperusahaanB.setAdapter(adapter_Kotaperusahaan);
		spinner_kotaperusahaanB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (!spinner_kotaperusahaanB.getSelectedItem().toString().equals("-Kota-")) {
					nmkotaperusahaan = spinner_kotaperusahaanB.getSelectedItem().toString();
					Log.d(TAG, "onItemSelected: Kotaperusahaan -- " + nmkotaperusahaan);

					spin3perusahaanB(nmzipcodeperusahaan, nmkotaperusahaan);
					Log.d(TAG, "onItemSelected: Kotaperusahaan " + nmkotaperusahaan);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void spin3perusahaanB(String zipcode, String kota) {
		ArrayList<String> myResArray33 = new ArrayList<>();
		if (myResArray33 != null) {
			myResArray33.clear();
		}

		myResArray33.add("-Kecamatan-");
		myResArray33.addAll(datasource.getAllKecamatan2(zipcode, kota));


		adapter_Kecamatanperusahaan = new ArrayAdapter<>(mContext, R.layout.spinner, myResArray33);
		adapter_Kecamatanperusahaan.notifyDataSetChanged();
		spinner_kecamatanperusahaanB.setAdapter(adapter_Kecamatanperusahaan);
		spinner_kecamatanperusahaanB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (!spinner_kecamatanperusahaanB.getSelectedItem().toString().equals("-Kecamatan-")) {
					nmkecamatanperusahaan = spinner_kecamatanperusahaanB.getSelectedItem().toString();

					Log.d(TAG, "onItemSelected: Kecamatan -- " + nmkecamatanperusahaan);

					spin4perusahaanB(nmzipcodeperusahaan, nmkotaperusahaan, nmkecamatanperusahaan);
					Log.d(TAG, "onItemSelected: Kecamatan " + nmkecamatanperusahaan);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void spin4perusahaanB(String zipcode, String kota, String kecamatan) {
		ArrayList<String> myResArray44 = new ArrayList<>();
		if (myResArray44 != null) {
			myResArray44.clear();
		}

		myResArray44.add("-Kelurahan-");
		myResArray44.addAll(datasource.getAllKelurahan2(zipcode, kota, kecamatan));

		adapter_Kelurahanperusahaan = new ArrayAdapter<>(mContext, R.layout.spinner, myResArray44);
		adapter_Kelurahanperusahaan.notifyDataSetChanged();
		spinner_kelurahanperusahaanB.setAdapter(adapter_Kelurahanperusahaan);
		spinner_kelurahanperusahaanB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (!spinner_kelurahanperusahaanB.getSelectedItem().toString().equals("-Kelurahan-")) {
					nmkelurahanperusahaan = spinner_kelurahanperusahaanB.getSelectedItem().toString();

					Log.d(TAG, "onItemSelected: nmkelurahanperusahaan -- " + nmkelurahanperusahaan);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	//FUNGSI SPINNER UNTUK WIRASWASTA


	//-----MAPS----------------------\\
	private void buildLocationAlert() {
		Intent myintent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		mContext.startActivity(myintent);
		Toast.makeText(mContext, "Please turn on location service", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onMapReady(final GoogleMap map) {
		Nmap = map;
		Nmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		CheckGpsStatus();
		//map.getUiSettings().setMyLocationButtonEnabled(false);
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		Nmap.setMyLocationEnabled(true);

	}

	private void initiliazeMap() {
		if (mapFragment == null) {
			mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
			mapFragment.getMapAsync(this);

		}
	}

	public void CheckGpsStatus() {

		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
		locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (enabled) {
			if (status == ConnectionResult.SUCCESS) {
			} else {
				GooglePlayServicesUtil.getErrorDialog(status, this, status);


			}
		} else {

			new SweetAlertDialog(SurveyActivity.this, SweetAlertDialog.WARNING_TYPE)
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
//		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
//		locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//		boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//		if (enabled) {
//			if (status == ConnectionResult.SUCCESS) {
//
//			} else {
//				GooglePlayServicesUtil.getErrorDialog(status, this, status);
//
//
//			}
//		} else {
//
//			new SweetAlertDialog(SurveyActivity.this, SweetAlertDialog.WARNING_TYPE)
//					.setTitleText("GPS anda tidak aktif")
//					.setContentText("Aktipkan GPS Anda Terlebih Dahulu")
//					.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//						@Override
//						public void onClick(SweetAlertDialog sweetAlertDialog) {
//
//							Intent pindah = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//							startActivity(pindah);
//							finish();
//						}
//					}).show();
//
//		}

	}

	void currentlocation() {

		gps = new GPSTracker(mContext);
		isMapImageValid = false;
		handlelocation();

		if (gps.canGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			clatitude = latitude;
			clongitude = longitude;
//			Toast.makeText(mContext,"posisi latitudenya adalah :"+ clatitude + " dan longitudenya adalah "+
//					clongitude,Toast.LENGTH_SHORT).show();
		} else {
			gps.showSettingsAlert();
		}
		moveMap();

	}

	private void moveMap() {
		LatLng latLng = new LatLng(clatitude, clongitude);
		Log.d("cek", "moveMap: " + latLng);
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

	private void CheckInPosition() {

		if (latLongString.length() == 0) {
			Toast.makeText(mContext, "Location not found, please wait..", Toast.LENGTH_SHORT).show();
			handlelocation();
		} else {

			isMapImageValid = true;
			btnCheckIn.setVisibility(View.GONE);

			if (addressText.length() > 0) {
				Toast.makeText(mContext, "You have check in at : \n" + addressText, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mContext, "You have check in at : \n" + latLongString, Toast.LENGTH_SHORT).show();
			}

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
		else if (getValue().trim().equals("Indoor")) {
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

				return;
			}
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
		} else if (getValue().trim().equals("Outdoor")) {
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

				return;
			}
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
		} else {
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
					!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
					android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

				return;
			}
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
		}

		Toast.makeText(mContext, "Waiting for location..", Toast.LENGTH_LONG).show();
	}

	LocationListener locationListenerGps = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
			locationManager.removeUpdates(this);
			if (ActivityCompat.checkSelfPermission(getApplicationContext(),
					android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
					ActivityCompat.checkSelfPermission(getApplicationContext(),
							android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

				return;
			}
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
			if (ActivityCompat.checkSelfPermission(getApplicationContext(),
					android.Manifest.permission.ACCESS_FINE_LOCATION) !=
					PackageManager.PERMISSION_GRANTED &&
					ActivityCompat.checkSelfPermission(getApplicationContext(),
							android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

				return;
			}
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

		latLongString = "Latitude : " + latitude + ", Longtitude : " + longtitude;
		latString = Double.toString(latitude);
		lngString = Double.toString(longtitude);
		List<Address> addresses;

		try {
			Geocoder gc = new Geocoder(this, Locale.getDefault());
			StringBuilder sb = new StringBuilder();
			addresses = gc.getFromLocation(latitude, longtitude, 1);
			Log.d(TAG, "updateWithNewLocation: addresses "+ addresses);
			if (addresses.size() > 0) {
				Log.d(TAG, "updateWithNewLocation: "+ addresses.size());
				Address address = addresses.get(0);
				ArrayList<String> addressFragments = new ArrayList<String>();


				for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
					//addressFragments.add(address.getAddressLine(i));
					Log.d(TAG, "updateWithNewLocation: addressFragments "+ address.getMaxAddressLineIndex());
					if (i == 0){
						sb.append(address.getAddressLine(i));
					}else {
						sb.append("," +  address.getAddressLine(i));
					}
				}

				if (sb.toString().length() > 1) {
					addressText = sb.toString();
					Log.d(TAG, "updateWithNewLocation: addressText "+ addressText);

				} else {
					addressText = "";
					Log.d(TAG, "updateWithNewLocation: addressText yang else " + addressText);
				}

			}

//			if (addresses.size() > 0) {
//
//				Address address = addresses.get(0);
//				Log.d(TAG, "updateWithNewLocation: address "+ address);
//
//				for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
//					if (i == 0) {
//						sb.append(address.getAddressLine(i));
//					} else {
//						sb.append("," + address.getAddressLine(i));
//					}
//				}
//
//				if (sb.toString().length() > 1) {
//					addressText = sb.toString();
//					Log.d(TAG, "updateWithNewLocation: addressText1 "+ addressText);
//				} else {
//					addressText = "";
//				}
//			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		Nmap.clear();

		MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longtitude));
		marker.title(addressText);
		Log.d(TAG, "updateWithNewLocation: addressText "+ addressText);
		marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

		Nmap.addMarker(marker).showInfoWindow();
	}

    //-----MAPS----------------------\\




    //------CEK DATA ------------------\\
    private boolean CheckForm() {
        try {
            int i;
            boolean good = true;
            for (i = 0; i < theForm.fields.size(); i++) {
                String fieldValue = (String) theForm.fields.elementAt(i).getData();
				Log.d(TAG, "CheckForm: " + fieldValue);

                if (theForm.fields.elementAt(i).isRequired()) {
                    if (fieldValue == null || fieldValue.trim().length() == 0) {
                        theForm.fields.elementAt(i).setData(theForm.fields.elementAt(i).getErrmsg());
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


	private String getValue2() {
		String text = "";
		for (int i = 0; i < radiogroup2.getChildCount(); i++) {

			RadioButton rbt = (RadioButton) radiogroup.getChildAt(i);
			if(rbt.isChecked()) {
				text = (String) String.valueOf(rbt.getId());
				break;
			}

//			RadioButton rbt = (RadioButton) radiogroup2.getChildAt(i);
//			if (rbt.isChecked()) {
//				text = (String) rbt.getText().toString().trim();
//				break;
//			}
		}
		return text.trim();
	}


	public void validateEmail(){
		String email = edt_email.getText().toString();

		if (email.equals("")){
			edt_email.clearFocus();
		}else{
			if(isValidEmail(email)){
				Toast.makeText(mContext, "Email is valid", Toast.LENGTH_SHORT).show();
				edt_email.clearFocus();
			}else{
				//Toast.makeText(mContext, "Email is invalid", Toast.LENGTH_SHORT).show();
				edt_email.setError("Email is invalid");
				edt_email.requestFocus();
			}
		}




	}

	public void validateEmail2(){
		String email2 = edt_emailW.getText().toString();


		if (email2.equals("")){
			edt_emailW.clearFocus();
		}else{
			if(isValidEmail(email2)){
				Toast.makeText(mContext, "Email is valid", Toast.LENGTH_SHORT).show();
				edt_emailW.clearFocus();
			}else{
				//Toast.makeText(mContext, "Email is invalid", Toast.LENGTH_SHORT).show();
				edt_emailW.setError("Email is invalid");
				edt_emailW.requestFocus();
			}
		}


	}

	public static boolean isValidEmail(String email)
	{
		String expression = "^[\\w\\.]+@([\\w]+\\.)+[A-Z]{2,7}$";

		CharSequence inputString = email;
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputString);
		if (matcher.matches())
		{
			return true;
		}
		else{
			return false;
		}
	}

	//------CEK DATA ------------------\\







	private class PostData extends AsyncTask<Void, Void, String> {

		String edt_namalengkap1 = edt_namalengkap.getText().toString();
		String edt_email1 = edt_email.getText().toString();
		String edt_alamattempattinggal1 = edt_alamattempattinggal.getText().toString();
		String edt_rt1 = edt_rt.getText().toString();
		String edt_rw1 = edt_rw.getText().toString();
		String kota1 = edt_kota.getText().toString();
		String keca1 = edt_kecamatan.getText().toString();
		String kelu1 = edt_kelurahan.getText().toString();
		//String zip1 = edt_zipcode.getText().toString();


		//String edt_alamattempattinggalkantor2 = edt_alamattempattinggalkantor.getText().toString();
//		String edt_rtperusahaan2 = edt_rtperusahaan.getText().toString();
//		String edt_rwperusahaan2 = edt_rwperusahaan.getText().toString();
//		String kota2 = edt_kotaperusahaan.getText().toString();
//		String keca2 = edt_kecamatanperusahaan.getText().toString();
//		String kelu2 = edt_kelurahanperusahaan.getText().toString();
		//String zip2 = edt_zipcodeperusahaan.getText().toString();



		@Override
		protected void onPreExecute() {
			dialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				db = datasource.getWritableDatabase();
				int cnt = 0;
				String image_str = null;
				String image_lat_str = null;
				String image_lng_str = null;

				String[] data = theForm.getEncodedData();
				ContentValues cv = new ContentValues();

				Cursor c = db.rawQuery("SELECT imageStr, lat, lng FROM " + tableImage, null);

				try {
					while (c.moveToNext()) {
						cnt++;
						if (image_str == null) {
							image_str = (c.getString(0));
							image_lat_str = c.getString(1);
							image_lng_str = c.getString(2);
						} else {
							image_str = image_str + "split" + (c.getString(0));
							image_lat_str = image_lat_str + ";" + c.getString(1);
							image_lng_str = image_lng_str + ";" + c.getString(2);
						}
					}
				} finally {
					c.close();
				}

				Calendar cl = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String myTime = df.format(cl.getTime());

//				cv.put("fieldname", data[0].substring(0, data[0].length() - 1)
//						+ "ApplicantName"
//						+ ";Email"
//						+ ";ResidenceAddress"
//						+ ";ResidenceRT"
//						+ ";ResidenceRW"
//						+ ";ResidenceCity"
//						+ ";ResidenceKecamatan"
//						+ ";ResidenceKelurahan"
//						+ ";ResidenceZipcode"
//						+ ";CompanyAddress"
//						+ ";CompanyRT"
//						+ ";CompanyRW"
//						+ ";CompanyCity"
//						+ ";CompanyKecamatan"
//						+ ";CompanyKelurahan"
//						+ ";CompanyZipcode");
//				cv.put("fieldvalue", data[1].substring(0, data[1].length() - 1)
//						+ "'" + edt_namalengkap1 + "'"
//						+ ";'" + edt_email1+"'"
//						+ ";'" + edt_alamattempattinggal1 + "'"
//						+ ";'" + edt_rt1 + "'"
//						+ ";'" + edt_rw1 + "'"
//						+ ";'" + kota1 + "'"
//						+ ";'" + keca1 + "'"
//						+ ";'" + kelu1 + "'"
//						+ ";'" + zip1 + "'"
//						+ ";'" + edt_alamattempattinggalkantor2 + "'"
//						+ ";'" + edt_rtperusahaan2 + "'"
//						+ ";'" + edt_rwperusahaan2 + "'"
//						+ ";'" + kota2 + "'"
//						+ ";'" + keca2 + "'"
//						+ ";'" + kelu2 + "'"
//						+ ";'" + zip2 + "'");

				cv.put("fieldname", data[0]
						+ "ApplicantName"
						+ ";Email"
						+ ";ResidenceAddress"
						+ ";ResidenceRT"
						+ ";ResidenceRW"
						+ ";ResidenceCity"
						+ ";ResidenceKecamatan"
						+ ";ResidenceKelurahan");
				cv.put("fieldvalue", data[1]
						+ "'" + edt_namalengkap1 + "'"
						+ ";'" + edt_email1+"'"
						+ ";'" + edt_alamattempattinggal1 + "'"
						+ ";'" + edt_rt1 + "'"
						+ ";'" + edt_rw1 + "'"
						+ ";'" + kota1 + "'"
						+ ";'" + keca1 + "'"
						+ ";'" + kelu1 + "'");

				cv.put("imagestr", image_str);
				cv.put("imagecount", String.valueOf(cnt));
				cv.put("moduleid", moduleid);
				cv.put("key", custID);
				cv.put("lat", latString);
				cv.put("lng", lngString);
				//cv.put("dtmupd", myTime);
				cv.put("dtmupd", tglserver);
				cv.put("userid", generalPrefs.getString("userID", null));
				cv.put("imagelat", image_lat_str);
				cv.put("imagelng", image_lng_str);
				cv.put("custtype", custType);
				cv.put("grading",""); //tambahan untuk bagian grading
				//cv.put("paiddate", ""); //tambahan
				cv.put("branchID",branchID); //tambahan untuk di field paiddate di result


				Log.d(TAG, "doInBackground: "
						+"\n"+ "ApplicantName"
						+"\n"+ ";Email"
						+"\n"+ ";ResidenceAddress"
						+"\n"+ ";ResidenceRT"
						+"\n"+ ";ResidenceRW"
						+"\n"+ ";ResidenceCity"
						+"\n"+ ";ResidenceKecamatan"
						+"\n"+ ";ResidenceKelurahan"
						+"\n"+"fieldname : " + data[0].substring(0, data[0].length() - 1)
						+"\n"+"fieldvalue : " + data[1].substring(0, data[1].length() - 1)
						+"\n"+"imagestr : " + image_str
						+"\n"+"imagecount : " + String.valueOf(cnt)
						+"\n"+"moduleid : " + moduleid
						+"\n"+"key : " + custID
						+"\n"+"lat : " + latString
						+"\n"+"lng : " + lngString
						+"\n"+"dtmupd : " + tglserver
						+"\n"+"userid : " + generalPrefs.getString("userID", null)
						+"\n"+"imagelat : " + image_lat_str
						+"\n"+"imagelng : " + image_lng_str
						+"\n"+"custtype : " + custType
						+"\n"+"branchID : " + branchID

				);

				datasource.generateData(db, cv, "result");

				cv.clear();

				datasource.deleteData(db, " customerid = '" + custID + "'", "surveylist");
				datasource.deleteData(db, "moduleid = '" + moduleid + "' and idx = '" + custID + "'", "infodetail");
				datasource.deleteData(db, "moduleid = '" + moduleid + "' and idx = '" + custID + "'", "imageurldetail");

			} catch (Exception e) {
				e.printStackTrace();
				SurveyActivity.this.e = e;

			} finally {
				db.close();
			}

			return "";
		}

		@Override
		protected void onPostExecute(String sResponse) {
			if (e == null) {
				finish();
				Intent intent = new Intent(mContext, ListSurveyActivity.class);
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

	private class PostDataW extends AsyncTask<Void, Void, String> {

		String edt_namalengkap1 = edt_namalengkapW.getText().toString();
		String edt_phone1 = edt_PhoneNoW.getText().toString();
		String edt_email1 = edt_emailW.getText().toString();
		String edt_MotherMaiden1 = edt_MotherMaidenNameW.getText().toString();
		String edt_KetMotherNam1 = edt_KetMotherNameW.getText().toString();
		String edt_alamattempattinggal1 = edt_alamattempattinggalW.getText().toString();
		String edt_rt1 = edt_rtW.getText().toString();
		String edt_rw1 = edt_rwW.getText().toString();
		String kota1 = edt_kotaW.getText().toString();
		String keca1 = edt_kecamatanW.getText().toString();
		String kelu1 = edt_kelurahanW.getText().toString();
		//String zip1 = edt_zipcodeperusahaanW.getText().toString();

		String edt_namatempattinggalkantor2 = edt_namatempattinggalkantorW.getText().toString();
		String edt_alamattempattinggalkantor2 = edt_alamattempattinggalkantorW.getText().toString();
		String edt_rtperusahaan2 = edt_rtperusahaanW.getText().toString();
		String edt_rwperusahaan2 = edt_rwperusahaanW.getText().toString();
		String kota2 = edt_kotaperusahaanW.getText().toString();
		String keca2 = edt_kecamatanperusahaanW.getText().toString();
		String kelu2 = edt_kelurahanperusahaanW.getText().toString();
		//String zip2 = edt_zipcodeperusahaanW.getText().toString();



		@Override
		protected void onPreExecute() {
			dialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				db = datasource.getWritableDatabase();
				int cnt = 0;
				String image_str = null;
				String image_lat_str = null;
				String image_lng_str = null;

				String[] data = theForm.getEncodedData();
				ContentValues cv = new ContentValues();

				Cursor c = db.rawQuery("SELECT imageStr, lat, lng FROM " + tableImage, null);

				try {
					while (c.moveToNext()) {
						cnt++;
						if (image_str == null) {
							image_str = (c.getString(0));
							image_lat_str = c.getString(1);
							image_lng_str = c.getString(2);
						} else {
							image_str = image_str + "split" + (c.getString(0));
							image_lat_str = image_lat_str + ";" + c.getString(1);
							image_lng_str = image_lng_str + ";" + c.getString(2);
						}
					}
				} finally {
					c.close();
				}

				Calendar cl = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String myTime = df.format(cl.getTime());

//				cv.put("fieldname", data[0].substring(0, data[0].length() - 1)
//						+ "ApplicantName"
//						+ ";Email"
//						+ ";ResidenceAddress"
//						+ ";ResidenceRT"
//						+ ";ResidenceRW"
//						+ ";ResidenceCity"
//						+ ";ResidenceKecamatan"
//						+ ";ResidenceKelurahan"
//						+ ";ResidenceZipcode"
//						+ ";CompanyAddress"
//						+ ";CompanyRT"
//						+ ";CompanyRW"
//						+ ";CompanyCity"
//						+ ";CompanyKecamatan"
//						+ ";CompanyKelurahan");
//				cv.put("fieldvalue", data[1].substring(0, data[1].length() - 1)
//						+ "'" + edt_namalengkap1 + "'"
//						+ ";'" + edt_email1+"'"
//						+ ";'" + edt_alamattempattinggal1 + "'"
//						+ ";'" + edt_rt1 + "'"
//						+ ";'" + edt_rw1 + "'"
//						+ ";'" + kota1 + "'"
//						+ ";'" + keca1 + "'"
//						+ ";'" + kelu1 + "'"
//						+ ";'" + zip1 + "'"
//						+ ";'" + edt_alamattempattinggalkantor2 + "'"
//						+ ";'" + edt_rtperusahaan2 + "'"
//						+ ";'" + edt_rwperusahaan2 + "'"
//						+ ";'" + kota2 + "'"
//						+ ";'" + keca2 + "'"
//						+ ";'" + kelu2 + "'");

				cv.put("fieldname", data[0]
						+ "ApplicantName"
						+ ";PhoneNo"
						+ ";Email"
						+ ";MotherMaidenName"
						+ ";KetMotherName"
						+ ";ResidenceAddress"
						+ ";ResidenceRT"
						+ ";ResidenceRW"
						+ ";ResidenceCity"
						+ ";ResidenceKecamatan"
						+ ";ResidenceKelurahan"
						+ ";CompanyName"
						+ ";CompanyAddress"
						+ ";CompanyRT"
						+ ";CompanyRW"
						+ ";CompanyCity"
						+ ";CompanyKecamatan"
						+ ";CompanyKelurahan");
				cv.put("fieldvalue", data[1]
						+ "'" + edt_namalengkap1 + "'"
						+ ";'" + edt_phone1+"'"
						+ ";'" + edt_email1+"'"
						+ ";'" + edt_MotherMaiden1+"'"
						+ ";'" + edt_KetMotherNam1+"'"
						+ ";'" + edt_alamattempattinggal1 + "'"
						+ ";'" + edt_rt1 + "'"
						+ ";'" + edt_rw1 + "'"
						+ ";'" + kota1 + "'"
						+ ";'" + keca1 + "'"
						+ ";'" + kelu1 + "'"
						+ ";'" + edt_namatempattinggalkantor2 + "'"
						+ ";'" + edt_alamattempattinggalkantor2 + "'"
						+ ";'" + edt_rtperusahaan2 + "'"
						+ ";'" + edt_rwperusahaan2 + "'"
						+ ";'" + kota2 + "'"
						+ ";'" + keca2 + "'"
						+ ";'" + kelu2 + "'");

				cv.put("imagestr", image_str);
				cv.put("imagecount", String.valueOf(cnt));
				cv.put("moduleid", moduleid);
				cv.put("key", custID);
				cv.put("lat", latString);
				cv.put("lng", lngString);
				cv.put("dtmupd", tglserver);
				//cv.put("dtmupd", myTime);
				cv.put("userid", generalPrefs.getString("userID", null));
				cv.put("imagelat", image_lat_str);
				cv.put("imagelng", image_lng_str);
				cv.put("custtype", custType);
				cv.put("grading",""); //tambahan untuk bagian grading
				//cv.put("paiddate", ""); //tambahan
				cv.put("branchID",branchID); //tambahan untuk di field paiddate di result


				Log.d(TAG, "doInBackground: "
						+"\n"+ "ApplicantName"
						+"\n"+ ";Email"
						+"\n"+ ";ResidenceAddress"
						+"\n"+ ";ResidenceRT"
						+"\n"+ ";ResidenceRW"
						+"\n"+ ";ResidenceCity"
						+"\n"+ ";ResidenceKecamatan"
						+"\n"+ ";ResidenceKelurahan"
						+"\n"+ ";SalesCoor"
						+"\n"+ ";SuplierAdmin"
						+"\n"+ ";CompanyAddress"
						+"\n"+ ";CompanyRT"
						+"\n"+ ";CompanyRW"
						+"\n"+ ";CompanyCity"
						+"\n"+ ";CompanyKecamatan"
						+"\n"+ ";CompanyKelurahan"
						+"\n"+"fieldname : " + data[0].substring(0, data[0].length() - 1)
						+"\n"+"fieldvalue : " + data[1].substring(0, data[1].length() - 1)
						+"\n"+"imagestr : " + image_str
						+"\n"+"imagecount : " + String.valueOf(cnt)
						+"\n"+"moduleid : " + moduleid
						+"\n"+"key : " + custID
						+"\n"+"lat : " + latString
						+"\n"+"lng : " + lngString
						+"\n"+"dtmupd : " + tglserver
						+"\n"+"userid : " + generalPrefs.getString("userID", null)
						+"\n"+"imagelat : " + image_lat_str
						+"\n"+"imagelng : " + image_lng_str
						+"\n"+"custtype : " + custType

				);

				datasource.generateData(db, cv, "result");

				cv.clear();

				datasource.deleteData(db, " customerid = '" + custID + "'", "surveylist");
				datasource.deleteData(db, "moduleid = '" + moduleid + "' and idx = '" + custID + "'", "infodetail");
				datasource.deleteData(db, "moduleid = '" + moduleid + "' and idx = '" + custID + "'", "imageurldetail");

			} catch (Exception e) {
				e.printStackTrace();
				SurveyActivity.this.e = e;

			} finally {
				db.close();
			}

			return "";
		}

		@Override
		protected void onPostExecute(String sResponse) {
			if (e == null) {
				finish();
				Intent intent = new Intent(mContext, ListSurveyActivity.class);
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

	private class PostDataB extends AsyncTask<Void, Void, String> {

		String edt_CompanyNameB1 = edt_CompanyNameB.getText().toString();
		String edt_LegalAddressB1 = edt_LegalAddressB.getText().toString();
		String edt_LegalrtB1 = edt_LegalrtB.getText().toString();
		String edt_LegalrwB1 = edt_LegalrwB.getText().toString();
		String kota1B = edt_LegalkotaB.getText().toString();
		String keca1B = edt_LegalkecamatanB.getText().toString();
		String kelu1B = edt_LegalkelurahanB.getText().toString();
		//String zip1B = edt_LegalzipcodeB.getText().toString();


		String edt_DomisiliAddressB2 = edt_DomisiliAddressB.getText().toString();
		String edt_DomisiliRTB2 = edt_DomisiliRTB.getText().toString();
		String edt_DomisiliRWB2 = edt_DomisiliRWB.getText().toString();
		String kota2B = edt_DomisilikotaB.getText().toString();
		String keca2B = edt_DomisilikecamatanB.getText().toString();
		String kelu2B = edt_DomisilikelurahanB.getText().toString();
		//String zip2B = edt_DomisilizipcodeB.getText().toString();



		@Override
		protected void onPreExecute() {
			dialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				db = datasource.getWritableDatabase();
				int cnt = 0;
				String image_str = null;
				String image_lat_str = null;
				String image_lng_str = null;

				String[] data = theForm.getEncodedData();
				ContentValues cv = new ContentValues();

				Cursor c = db.rawQuery("SELECT imageStr, lat, lng FROM " + tableImage, null);

				try {
					while (c.moveToNext()) {
						cnt++;
						if (image_str == null) {
							image_str = (c.getString(0));
							image_lat_str = c.getString(1);
							image_lng_str = c.getString(2);
						} else {
							image_str = image_str + "split" + (c.getString(0));
							image_lat_str = image_lat_str + ";" + c.getString(1);
							image_lng_str = image_lng_str + ";" + c.getString(2);
						}
					}
				} finally {
					c.close();
				}

				Calendar cl = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String myTime = df.format(cl.getTime());

//				cv.put("fieldname", data[0].substring(0, data[0].length() - 1)
//						+ "ApplicantName"
//						+ ";Email"
//						+ ";ResidenceAddress"
//						+ ";ResidenceRT"
//						+ ";ResidenceRW"
//						+ ";ResidenceCity"
//						+ ";ResidenceKecamatan"
//						+ ";ResidenceKelurahan"
//						+ ";ResidenceZipcode"
//						+ ";CompanyAddress"
//						+ ";CompanyRT"
//						+ ";CompanyRW"
//						+ ";CompanyCity"
//						+ ";CompanyKecamatan"
//						+ ";CompanyKelurahan"
//						+ ";CompanyZipcode");
//				cv.put("fieldvalue", data[1].substring(0, data[1].length() - 1)
//						+ "'" + edt_namalengkap1 + "'"
//						+ ";'" + edt_email1+"'"
//						+ ";'" + edt_alamattempattinggal1 + "'"
//						+ ";'" + edt_rt1 + "'"
//						+ ";'" + edt_rw1 + "'"
//						+ ";'" + kota1 + "'"
//						+ ";'" + keca1 + "'"
//						+ ";'" + kelu1 + "'"
//						+ ";'" + zip1 + "'"
//						+ ";'" + edt_alamattempattinggalkantor2 + "'"
//						+ ";'" + edt_rtperusahaan2 + "'"
//						+ ";'" + edt_rwperusahaan2 + "'"
//						+ ";'" + kota2 + "'"
//						+ ";'" + keca2 + "'"
//						+ ";'" + kelu2 + "'"
//						+ ";'" + zip2 + "'");

				cv.put("fieldname", data[0]
						+ "CompanyName"
						+ ";CompanyType"
						+ ";LegalAddress"
						+ ";LegalRt"
						+ ";LegalRW"
						+ ";LegalCity"
						+ ";LegalKecamatan"
						+ ";LegalKelurahan"
						+ ";DomisiliAddress"
						+ ";DomisiliRT"
						+ ";DomisiliRW"
						+ ";DomisiliCity"
						+ ";DomisiliKecamatan"
						+ ";DomisiliKelurahan");

				cv.put("fieldvalue", data[1]
						+ "'" + edt_CompanyNameB1 + "'"
						+ ";'" + getValue2() +"'"
						//+ ";'" + getValue2().substring(0, 8) +"'"
						+ ";'" + edt_LegalAddressB1+"'"
						+ ";'" + edt_LegalrtB1+"'"
						+ ";'" + edt_LegalrwB1+"'"
						+ ";'" + kota1B + "'"
						+ ";'" + keca1B + "'"
						+ ";'" + kelu1B + "'"
						+ ";'" + edt_DomisiliAddressB2 + "'"
						+ ";'" + edt_DomisiliRTB2 + "'"
						+ ";'" + edt_DomisiliRWB2 + "'"
						+ ";'" + kota2B + "'"
						+ ";'" + keca2B + "'"
						+ ";'" + kelu2B + "'");
				cv.put("imagestr", image_str);
				cv.put("imagecount", String.valueOf(cnt));
				cv.put("moduleid", moduleid);
				cv.put("key", custID);
				cv.put("lat", latString);
				cv.put("lng", lngString);
				//cv.put("dtmupd", myTime);
				cv.put("dtmupd", tglserver);
				cv.put("userid", generalPrefs.getString("userID", null));
				cv.put("imagelat", image_lat_str);
				cv.put("imagelng", image_lng_str);
				cv.put("custtype", custType);
				cv.put("grading",""); //tambahan untuk bagian grading
				//cv.put("paiddate", ""); //tambahan
				cv.put("branchID",branchID); //tambahan untuk di field paiddate di result


				Log.d(TAG, "doInBackground: "
						+"\n"+ "CompanyName" + edt_CompanyNameB1
						+"\n"+ ";CompanyType" + getValue2()
						+"\n"+ ";LegalAddress" + edt_LegalAddressB1
						+"\n"+ ";LegalRt" + edt_LegalrtB1
						+"\n"+ ";LegalRW" + edt_LegalrwB1
						+"\n"+ ";LegalCity" + kota1B
						+"\n"+ ";LegalKecamatan" + keca1B
						+"\n"+ ";LegalKelurahan" + kelu1B
						+"\n"+ ";DomisiliAddress" + edt_DomisiliAddressB2
						+"\n"+ ";DomisiliRT" + edt_DomisiliRTB2
						+"\n"+ ";DomisiliRW" + edt_DomisiliRWB2
						+"\n"+ ";DomisiliCity" + kota2B
						+"\n"+ ";DomisiliKecamatan" + keca2B
						+"\n"+ ";DomisiliKelurahan" + kelu2B
						+"\n"+"fieldname : " + data[0].substring(0, data[0].length() - 1)
						+"\n"+"fieldvalue : " + data[1].substring(0, data[1].length() - 1)
						+"\n"+"imagestr : " + image_str
						+"\n"+"imagecount : " + String.valueOf(cnt)
						+"\n"+"moduleid : " + moduleid
						+"\n"+"key : " + custID
						+"\n"+"lat : " + latString
						+"\n"+"lng : " + lngString
						+"\n"+"dtmupd : " + tglserver
						+"\n"+"userid : " + generalPrefs.getString("userID", null)
						+"\n"+"imagelat : " + image_lat_str
						+"\n"+"imagelng : " + image_lng_str
						+"\n"+"custtype : " + custType

				);

				datasource.generateData(db, cv, "result");

				cv.clear();

				datasource.deleteData(db, " customerid = '" + custID + "'", "surveylist");
				datasource.deleteData(db, "moduleid = '" + moduleid + "' and idx = '" + custID + "'", "infodetail");
				datasource.deleteData(db, "moduleid = '" + moduleid + "' and idx = '" + custID + "'", "imageurldetail");

			} catch (Exception e) {
				e.printStackTrace();
				SurveyActivity.this.e = e;

			} finally {
				db.close();
			}

			return "";
		}

		@Override
		protected void onPostExecute(String sResponse) {
			if (e == null) {
				finish();
				Intent intent = new Intent(mContext, ListSurveyActivity.class);
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



	@Override
	public void onBackPressed() {
//		finish();
//		Intent intent = new Intent(mContext, ListSurveyActivity.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		intent.putExtra("flagUpdate", "N");
//		startActivity(intent);
//		return;

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("Confirmation");
		builder.setMessage("Do you really want to exit ?")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, final int id) {
						finish();
						Intent intent = new Intent(mContext, ListSurveyActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("flagUpdate", "N");
						startActivity(intent);
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

		if (theForm != null) {
			theForm = null;
		}

		if (db != null) {
			if (db.isOpen()) {
				db.close();
			}
		}


		if (adapter != null) {
			adapter.notifyDataSetInvalidated();
			adapter = null;
		}

		if (locationManager != null) {
			locationManager.removeUpdates(locationListenerGps);
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

				return;
			}
			locationManager.removeUpdates(locationListenerNetwork);
			locationManager = null;
    	}
    	
    	if (Nmap != null){
			Nmap.clear();
    	}    		
    	    	
    	if(tabHost != null){
    		tabHost = null;
    	}
    	    	
    	if (dialog != null){
    		if(dialog.isShowing()){
        		dialog.dismiss();
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
    	
    	System.gc();
    	
    }    
    
    @Override
    public void onStop() {
    	super.onStop();
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
        	   		Toast.makeText(this,"Your session has been expired, please re login for security purpose", Toast.LENGTH_LONG).show();
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


//    public class ZipcodeResidence {
//        private String kecamatan = "";
//        private String kelurahan = "";
//        private String kota = "";
//
//        public void setKota(String kota) {
//            this.kota = kota;
//        }
//
//        public void setKelurahan(String kelurahan) {
//            this.kelurahan = kelurahan;
//        }
//
//        public void setKecamatan(String kecamatan) {
//            this.kecamatan = kecamatan;
//        }
//
//        public String getKota() {
//            return kota;
//        }
//
//        public String getKelurahan() {
//            return kelurahan;
//        }
//
//        public String getKecamatan() {
//            return kecamatan;
//        }
//    }



//	private OnClickListener myLocationOnClickListener = new OnClickListener() {
//		public void onClick(View v) {
//			isMapImageValid = false;
//			Nmap.setMyLocationEnabled(true);
//			handlelocation();
//
//			Location myLocation = googleMap.getMyLocation();
//
//			if (myLocation == null) {
//				Toast.makeText(mContext, "Location not found, please wait..", Toast.LENGTH_SHORT).show();
//			} else {
//				LatLng myLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
//
//				CameraPosition myPosition = new CameraPosition.Builder()
//						.target(myLatLng).zoom(15).bearing(90).tilt(30).build();
//
//				googleMap.animateCamera(
//						CameraUpdateFactory.newCameraPosition(myPosition));
//
//				//googleMap.getUiSettings().setAllGesturesEnabled(false);
//
//				btnMyLocation.setVisibility(View.GONE);
//				btnCheckIn.setVisibility(View.VISIBLE);
//				if (ActivityCompat.checkSelfPermission(getApplicationContext(),
//						android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//						&& ActivityCompat.checkSelfPermission(getApplicationContext(),
//						android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//					return;
//				}
//				googleMap.setMyLocationEnabled(false);
//			}
//
//		}
//	};
//
//	private OnClickListener checkInOnClickListener = new OnClickListener() {
//		public void onClick(View v) {

//
//			if (latLongString.length() == 0) {
//				Toast.makeText(mContext, "Location not found, please wait..", Toast.LENGTH_SHORT).show();
//				handlelocation();
//			} else {
//
//				isMapImageValid = true;
//				btnCheckIn.setVisibility(View.GONE);
//
//				if (addressText.length() > 0) {
//					Toast.makeText(mContext, "You have check in at : \n" + addressText, Toast.LENGTH_SHORT).show();
//				} else {
//					Toast.makeText(mContext, "You have check in at : \n" + latLongString, Toast.LENGTH_SHORT).show();
//				}
//
//			}
//		}
//	};
    
}