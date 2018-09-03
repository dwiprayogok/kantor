package mandiri.finance.faith;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.Actions;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Actions;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapFragment;

import android.*;
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
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Gallery;
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

public class SurveyLocationActivity extends DashBoardActivity implements OnMapReadyCallback {
	private String custID;
	private String custName;
	private String custAddress;
	private String custType;

	private Context mContext;

	private TabHost tabHost = null;
	private LazyAdapterImage adapter;
	private Exception e = null;

	private ArrayList<String> imageURLResults = new ArrayList<String>();

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

	private GoogleMap googleMap;
	private MapFragment mapFragment2;
	private String moduleid;
	private RadioGroup radiogroup;
	private ImageView gambarfoto;
	private Uri imageUri;
	private boolean isgambarValid = false;
	private static final int CAMERA_REQUEST = 1888;
	private String image_str_coll = "";
	private Dialog dialog2;
	private int flag2;
	private Intent datax;
	private static int Action_Code = 0;
	private String tableImageSurveyLocatinActivity;
	private int buttonId5;
	private boolean deleteFlag5 = false;
	private GoogleMap Nmap;
	private MapFragment mapFragment;
	private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
	Double clongitude;
	Double clatitude;
	Marker cloc;
	private String branchID;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	//private GoogleApiClient client;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = SurveyLocationActivity.this;
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

		flag = true;
		moduleid = generalPrefs.getString("moduleid", null);
		branchID = generalPrefs.getString("branchID", null);
		Log.d(TAG, "onCreate: branchID " + branchID);
		datasource = new DataSource(mContext);
		db = datasource.getWritableDatabase();

		tableImageSurveyLocatinActivity = "imagesurveylocation";

		Bundle b = getIntent().getExtras();
		custID = b.getString("custID");
		custName = b.getString("custName");
		custAddress = b.getString("custAddress");

		setContentView(R.layout.tabs);
		setHeader(getString(R.string.SurveyActivityTitle2), true);

		tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();

		new createTabs().execute();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		//client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	public com.google.firebase.appindexing.Action getAction() {
		return Actions.newView("SurveyLocation Page", "http://[ENTER-YOUR-URL-HERE]");
	}

//	public Action getIndexApiAction() {
//		Thing object = new Thing.Builder()
//				.setName("SurveyLocation Page") // TODO: Define a title for the content shown.
//				// TODO: Make sure this auto-generated URL is correct.
//				.setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
//				.build();
//		return new Action.Builder(Action.TYPE_VIEW)
//				.setObject(object)
//				.setActionStatus(Action.STATUS_TYPE_COMPLETED)
//				.build();
//	}

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		//client.connect();
		//AppIndex.AppIndexApi.start(client, getIndexApiAction());
		//FirebaseAppIndex.getInstance().update(getIndexable());
		//FirebaseUserActions.getInstance().start(getAction());
	}

	@Override
	public void onStop() {
		//FirebaseUserActions.getInstance().end(getAction());
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		//AppIndex.AppIndexApi.end(client, getIndexApiAction());
		//client.disconnect();
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

					Cursor cs = db.rawQuery("SELECT custtype FROM surveyloclist where customerid = '" + custID + "'", null);

					try {
						if (cs.moveToFirst()) {
							custType = cs.getString(0);
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
					}

				} catch (Exception e) {
					SurveyLocationActivity.this.e = e;
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
						LayoutParams params = new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT);

						panel.setLayoutParams(params);
						panel.setOrientation(LinearLayout.VERTICAL);
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

						adapter = new LazyAdapterImage(SurveyLocationActivity.this, imageURLArray);
						gl.setAdapter(adapter);

						panel.addView(tvHeader);
						panel.addView(tvDetail);
						panel.addView(gl);

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
							//t3.setText(info.get(i).trim());
							t3.setText(info.get(i));

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
						//btnMyLocation.setOnClickListener(myLocationOnClickListener);
						btnMyLocation.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {

								currentlocation();
								//moveMap();
							}
						});
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
						LinearLayout panel = new LinearLayout(mContext);
						LayoutParams params = new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT);

						panel.setLayoutParams(params);
						panel.setOrientation(LinearLayout.VERTICAL);
						panel.setPadding(valuePixels, 0, valuePixels, 0);


						TextView tv2 = new TextView(mContext);
						tv2.setTextColor(getResources().getColor(R.color.label_color));

						tv2.setPadding(10, 10, 0, 0);
						tv2.setText("Foto Sumber Informasi");
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

						try {

							theForm = new Form();

							Cursor c = db.rawQuery("SELECT name, label, type, required, optionsname, optionsid, hint, errmsg, maxlength" +
									", issearchable FROM form where moduleid = '" + moduleid + "' and custtype = '" + custType + "'", null);

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

						btn.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								boolean check;

								locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

								if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
									buildLocationAlert();
									return;
								}

								if (isMapImageValid == false) {
									Toast.makeText(mContext, "Please check in first", Toast.LENGTH_SHORT).show();
									return;
								}

								check = CheckForm();

								if (!check) {
									Toast.makeText(mContext, "Enter all required (!) fields", Toast.LENGTH_SHORT).show();
									return;
								} else {
									new PostData().execute();
								}

							}
						});

						panel.addView(btn);
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

	private void buildLocationAlert() {
		Intent myintent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
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


//		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//		CheckGpsStatus();
//		map.getUiSettings().setMyLocationButtonEnabled(false);
//		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//			return;
//		}
//		map.setMyLocationEnabled(true);
//		map.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
//			@Override
//			public void onMyLocationChange(Location location) {
//				CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
//						location.getLongitude()), 8);
//				map.animateCamera(cameraUpdate);
//				if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//					return;
//				}
//				map.setMyLocationEnabled(false);
//				map.setOnMyLocationChangeListener(null);
//			}
//		});
	}

	private void initiliazeMap() {
		if (mapFragment == null) {
			mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
			mapFragment.getMapAsync(this);
			if (mapFragment == null) {
				Toast.makeText(getApplicationContext(), "mapnya gak muncul", Toast.LENGTH_SHORT).show();
			}
		}
//		if (googleMap == null) {
//			//googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
//			mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
//			mapFragment.getMapAsync(this);
//
//
////			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
////			googleMap.getUiSettings().setMyLocationButtonEnabled(false);
////			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////
////				return;
////			}
////			googleMap.setMyLocationEnabled(true);
////
////			googleMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
////				@Override
////				public void onMyLocationChange(Location point) {
////					CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(point.getLatitude(), point.getLongitude()), 8);
////					googleMap.animateCamera(cameraUpdate);
////					if (ActivityCompat.checkSelfPermission(getApplicationContext(),
/// 					android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
/// 					&& ActivityCompat.checkSelfPermission(getApplicationContext(),
/// 					android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

////						return;
////					}
////					googleMap.setMyLocationEnabled(false);
////					googleMap.setOnMyLocationChangeListener(null);
////				}
////			});
//		}
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
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
		} else if (getValue().trim().equals("Outdoor")) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
		} else {
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
		}

		Toast.makeText(mContext, "Waiting for location..", Toast.LENGTH_LONG).show();
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

			new SweetAlertDialog(SurveyLocationActivity.this, SweetAlertDialog.WARNING_TYPE)
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

	private void moveMap() {
		LatLng latLng = new LatLng(clatitude,clongitude);
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

	private void CheckInPosition(){

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
		moveMap();

	}

	LocationListener locationListenerGps = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
			locationManager.removeUpdates(this);
			if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
					!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
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
			if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
					&& ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
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

//		double latitude = newLocation.getLatitude();
//		double longtitude = newLocation.getLongitude();
//
//		Geocoder gc = new Geocoder(this, Locale.getDefault());
//
//		latLongString = "Latitude : " + latitude + ", Longtitude : " + longtitude;
//		latString = Double.toString(latitude);
//		lngString = Double.toString(longtitude);
//
//		List<Address> addresses;
//
//		try {
//			addresses = gc.getFromLocation(latitude, longtitude, 1);
//
//			StringBuilder sb = new StringBuilder();
//			if (addresses.size() > 0) {
//
//				Address address = addresses.get(0);
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
//				} else {
//					addressText = "";
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		googleMap.clear();
//
//
//		MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longtitude));
//		marker.title(addressText);
//		marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//
//		googleMap.addMarker(marker).showInfoWindow();

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



	///------BAGIAN UNTUK TAB ADD DATA ( TAKE PHOTO ) -----------///////
	private void loadPhoto() {

		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
				!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission
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
			new AlertDialog.Builder(SurveyLocationActivity.this)
					.setTitle("Permission Request")
					.setMessage("Check permission")
					.setCancelable(false)
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							//re-request
							ActivityCompat.requestPermissions(SurveyLocationActivity.this,
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
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(getApplicationContext(), "Permission IMEI HAVE BEEN GRANTED", Toast.LENGTH_SHORT).show();

			}

		} else {
			Toast.makeText(getApplicationContext(), "permission gagal", Toast.LENGTH_SHORT).show();

		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
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
							sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
									+ Environment.getExternalStorageDirectory())));

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


	///--------IMAGE SURVEYLOCATION----------------\\\
	private void startCamera2(){
		if(dialog != null && dialog.isShowing())
			dialog.dismiss();

		String fileName = "picSurveyLocation.jpg";
		ContentValues values = new ContentValues();

		values.put(MediaStore.Images.Media.TITLE, fileName);
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

		imageUri = getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

		startActivityForResult(intent, CAMERA_REQUEST);
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
					//loadcustomer();
					PictureAttach2();
				}
				else if(flag2 != 0) {
					//loadcustomer();
					PictureAttach2();

				}
				else
					result = "imageUri = null";


				SystemClock.sleep(2000);



			}catch (Exception e) {
				SurveyLocationActivity.this.e = e;
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
				File f = new File(cacheDir, "picSurveyLocation");
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
			File f = new File(cacheDir, "picSurveyLocation");

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


			//latinya22 = latinya2;
			//longinya22 = longinya2;


			bos.flush();
			bos.close();
			System.gc();

			Cursor cursor = db.rawQuery("SELECT id FROM " + tableImageSurveyLocatinActivity + " WHERE buttonId = '" + Integer.toString(Action_Code) +"'", null);

			if(cursor.moveToFirst())
			{
				ContentValues cv = new ContentValues();
				cv.put("imageStr", image_str_coll);
				datasource.updateData(db,cv,tableImageSurveyLocatinActivity," buttonId = '" + Action_Code + "'");
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
				datasource.generateData(db,cv,tableImageSurveyLocatinActivity);

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
			datasource.deleteData(db, " buttonId = '" + Action_Code + "'", tableImageSurveyLocatinActivity);
			//datasource.deleteData(db, " buttonId = '" + Action_Code + "'", tableImageCollection);
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
///--------IMAGE SURVEYLOCATION----------------\\\


	private class PostData extends AsyncTask<Void, Void, String> {
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

				Cursor c = db.rawQuery("SELECT imageStr, lat, lng FROM " + tableImageSurveyLocatinActivity, null);
				Log.d(TAG, "doInBackground: "+ db.rawQuery("SELECT imageStr, lat, lng FROM " + tableImageSurveyLocatinActivity, null));
				try {
					while (c.moveToNext()) {
						cnt++;
						if (image_str == null) {
							image_str = (c.getString(0));
							image_lat_str = c.getString(1);
							image_lng_str = c.getString(2);
							Log.d(TAG, "doInBackground: " + image_lat_str+ image_lng_str);
						} else {
							image_str = image_str + "split" + (c.getString(0));
							image_lat_str = image_lat_str + ";" + c.getString(1);
							image_lng_str = image_lng_str + ";" + c.getString(2);
							Log.d(TAG, "doInBackground: image_lat_str + image_lng_str " + image_lat_str + image_lng_str);
						}
					}
				} finally {
					c.close();
				}

				Calendar cl = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String myTime = df.format(cl.getTime());

				cv.put("fieldname", data[0].substring(0, data[0].length() - 1));
				cv.put("fieldvalue", data[1].substring(0, data[1].length() - 1));
				cv.put("imagestr",image_str);
				cv.put("imagecount", String.valueOf(cnt));
				cv.put("moduleid", moduleid);
				cv.put("key", custID);
				cv.put("lat", latString);
				cv.put("lng", lngString);
				cv.put("dtmupd", myTime);
				cv.put("userid", generalPrefs.getString("userID", null));
				cv.put("imagelat", "");
				cv.put("imagelng", "");
				cv.put("custtype", custType);
				cv.put("grading",""); //tambahan untuk bagian grading
				cv.put("branchID",branchID); //tambahan untuk bagian grading

				datasource.generateData(db, cv, "result");

				cv.clear();

				datasource.deleteData(db, " customerid = '" + custID + "'", "surveyloclist");
				datasource.deleteData(db, "moduleid = '" + moduleid + "' and idx = '" + custID + "'", "infodetail");
				datasource.deleteData(db, "moduleid = '" + moduleid + "' and idx = '" + custID + "'", "imageurldetail");

			} catch (Exception e) {
				e.printStackTrace();
				SurveyLocationActivity.this.e = e;

			} finally {
				db.close();
			}

			return "";
		}

		@Override
		protected void onPostExecute(String sResponse) {
			if (e == null) {
				finish();
				Intent intent = new Intent(mContext, ListSurveyLocationActivity.class);
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

	private boolean CheckForm() {
		try {
			int i;
			boolean good = true;

			for (i = 0; i < theForm.fields.size(); i++) {
				String fieldValue = (String) theForm.fields.elementAt(i).getData();

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

	@Override
	public void onBackPressed() {
		finish();
		Intent intent = new Intent(mContext, ListSurveyLocationActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("flagUpdate", "N");
		startActivity(intent);
		return;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

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
			locationManager.removeUpdates(locationListenerGps);
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

				return;
			}
			locationManager.removeUpdates(locationListenerNetwork);
			locationManager = null;
		}

		if (googleMap != null) {
			googleMap.clear();
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

		System.gc();

	}

	@Override
	public void onPause() {
		super.onPause();

		if (dialog != null) {
			if (dialog.isShowing()) {
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

				diffInMin = (dateEnd.getTime() / 60000) - (dateStart.getTime() / 60000);

				if ((int) diffInMin > Integer.valueOf(timeOut) || (int) diffInMin < 0) {
					Toast.makeText(this, "Your session has been expired, please re login for security purpose", Toast.LENGTH_LONG).show();
					prefsEditor.putBoolean("finish", true).commit();

					finish();
					Intent myintent = new Intent(mContext, HomeActivity.class);
					myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(myintent);
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

}