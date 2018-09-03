package mandiri.finance.faith;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.*;
import android.Manifest;
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
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mandiri.finance.faith.Interface.CallbackID;
import mandiri.finance.faith.Model.Identitas;
import mandiri.finance.faith.Model.Model_Customer;
import mandiri.finance.faith.Model.Supplier;

public class ProspectActivity extends DashBoardActivity {
	private String tag = ProspectActivity.class.getName();
	private Form theForm;

	private DataSource datasource;
	private SQLiteDatabase db;
	private String tableImage;
	private Context mContext;

	private Exception e = null;
	private SharedPreferences generalPrefs;
	private boolean flag = false;
	private Dialog dialog;
	private SharedPreferences.Editor prefsEditor;
	private String isLoadProspectForm;
	private String isLoadAsset;
	private String isLoadSupplier;
	private String isLoadTempatLahir ;
	private String isLoadProduct;
	private String isLoadSales;
	private String tglserver;

	private DataLocal datalocal;

	private LocationManager locationManager;
	boolean gps_enabled = false;
	boolean network_enabled = false;

	private String latString = "";
	private String lngString = "";

	private String moduleid;
	private WakeLock wl;
	private RadioGroup radiogroup,radioGroup2;
	private Spinner spinner;

	private Spinner
			spinner_supplier,
			spinner_sales,
			spinner_salesSPV,
			spinner_salesCoor,
			spinner_admin;

    private Spinner
			spinner_produk,
			spinner_event;

	private    String nmevent="";
	private    String idevent="";
	private Button btn_product;
	private Dialog dialog2;

	private    String idproduk="";
	private    String nmproduk="";
	private    String idprodOffering = "";
	private    String nmprodOffering="";



    private    String idsupplier="";
    private    String nmsupplier="";
    private    String idsalesman = "";
    private    String nmsalesman="";
    private    String idsalesspv ="";
    private    String nmsalesspv="";
    private    String idsaleskoor ="";
    private    String nmsaleskoor="";
	private    String idsalesadm ="";
	private    String nmsalesadm="";


	private  EditText catatan;



	private Button Search;
	private TextView txt_search;


	private String Txt_Grading = "";
	noIdentitas identitas = new noIdentitas();
	String TAG = "ProspectActivity";
	public String fieldValue = "";
	private Button btn_search;
	private EditText searchText;
	private ListView lv;
	private ArrayList<SearchResults> results2 = new ArrayList<SearchResults>();
	private ArrayList<Identitas> results = new ArrayList<Identitas>();
	private String branchID;
	private String userID;
	private String ukuranproductoffering = "";
	private String ukuransizekota = "";
	String result2 = "";
	NodeList nodelist;
	ProgressDialog progress;
	String url_download = "http://123.231.241.53:48100/mobileapplication/AndroidFin.asmx";
	private int flag2;
	private Intent datax;
	public ArrayAdapter<String> adapter_produk;
	public ArrayAdapter<String> adapter_produkOffering;
	private String image_str_coll = "";
	private Uri imageUri;
	private String image_str = "";
	private String tableName;
	public ArrayAdapter<String> adapter_product;
	public ArrayAdapter<String> adapter_productOffering;
	public ArrayAdapter<String> adapter_supplier;
    public ArrayAdapter<String> adapter_salesman;
    public ArrayAdapter<String> adapter_salesspv;
	public ArrayAdapter<String> adapter_salekoordinasi;
	public ArrayAdapter<String> adapter_salesadmin;
    int position=0;
	private static final int CAMERA_REQUEST = 1888;
	int satu = 1;
	int dua = 2;
	private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
	PickOneAdapter aa;
	private String value_product;
	SearchableSpinner spinner_offering,spinner_product;
	ArrayList<String> items = new ArrayList<>();
	private String blocksimbol = "'*^`~|/<>";
	private  String cekbaruataulama ="";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = ProspectActivity.this;



		datasource = new DataSource(mContext);
		db = datasource.getWritableDatabase();
		datalocal = new DataLocal();
		tableImage = "image";

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		wl.acquire();

		generalPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		prefsEditor = generalPrefs.edit();

		dialog = new Dialog(mContext, R.style.lightbox_dialog);
		dialog.setContentView(R.layout.lightbox_dialog);
		((TextView) dialog.findViewById(R.id.dialogText)).setText("Loading...");
		dialog.setCancelable(false);
		progress  = new ProgressDialog(this);

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
			isLoadProspectForm = generalPrefs.getString("isLoadProspectForm", null);
			isLoadAsset = generalPrefs.getString("isLoadAsset", null);
			isLoadSupplier = generalPrefs.getString("isLoadSupplier", null);
			isLoadProduct = generalPrefs.getString("isLoadProduct", null);
			isLoadSales = generalPrefs.getString("isLoadSales", null);

			tglserver = generalPrefs.getString("tglserver", null);


			branchID = generalPrefs.getString("branchID", null);
			userID = generalPrefs.getString("userID", null);

			Log.d(TAG, "onCreate: "+timeOut+sessionStart+isLoadAsset+isLoadProspectForm+isLoadSupplier
					+"isLoadProduct"+isLoadProduct +"isLoadSales"+isLoadSales);
			Log.d(TAG, "onCreate: branchID " + branchID);
			Log.d(TAG, "onCreate: tglserver " + tglserver);

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
		Log.d(TAG, "onCreate: "+ moduleid);


		new GetFormData().execute();
	}

	@Override
	public void onBackPressed() {

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("Confirmation");
		builder.setMessage("Do you really want to exit ?")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, final int id) {
						finish();
						Intent myintent = new Intent(mContext, HomeActivity.class);
						myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(myintent);
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

	private void buildLocationAlert() {
		Intent myintent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		mContext.startActivity(myintent);
		Toast.makeText(mContext, "Please turn on location service", Toast.LENGTH_LONG).show();
	}

	private class GetFormData extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			dialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			String result = "";

			try {
				db = datasource.getWritableDatabase();

				if (Boolean.valueOf(isLoadProspectForm)) {
						if (datalocal.GetFormData(mContext, moduleid, getString(R.string.url)))
						prefsEditor.putString("isLoadProspectForm", "false").commit();
					else
						result = "ErrorConnection";
				}

				if (Boolean.valueOf(isLoadAsset)) {
					if (datalocal.GetAssetData(mContext, getString(R.string.url)))
						prefsEditor.putString("isLoadAsset", "false").commit();
					else
						result = "ErrorConnection";
				}

				if (Boolean.valueOf(isLoadSupplier)) {

					if (datalocal.GetSupplier(mContext, getString(R.string.url))) {
						prefsEditor.putString("isLoadSupplier", "false").commit();
					} else
						result = "ErrorConnection";
				}


				if (Boolean.valueOf(isLoadProduct)) {

					if (datalocal.GetProductData(mContext, getString(R.string.url))) {
						prefsEditor.putString("isLoadProduct", "false").commit();
					} else
						result = "ErrorConnection";
				}


				if (Boolean.valueOf(isLoadProduct)) {

					if (datalocal.GetProductOfferingData(mContext, getString(R.string.url))) {
						prefsEditor.putString("isLoadProduct", "false").commit();
					} else
						result = "ErrorConnection";
				}


				if (Boolean.valueOf(isLoadProduct)) {

					if (datalocal.GetSalesData(mContext, getString(R.string.url))) {
						prefsEditor.putString("isLoadProduct", "false").commit();
					} else
						result = "ErrorConnection";
				}


				if (Boolean.valueOf(isLoadSupplier)) {

					if (datalocal.GetEventData(mContext, getString(R.string.url))) {
						prefsEditor.putString("isLoadSupplier", "false").commit();
					} else
						result = "ErrorConnection";
				}

				//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						new ambilasset().execute();
//					}
//				});


//				if (Boolean.valueOf(isLoadProduct)) {
//
//					if (datalocal.GetTempatLahir(mContext, getString(R.string.url))) {
//						prefsEditor.putString("isLoadProduct", "false").commit();
//					} else
//						result = "ErrorConnection";
//				}



			} catch (Exception e) {
				ProspectActivity.this.e = e;
				e.printStackTrace();
			} finally {
				db.close();
			}

			Log.d(TAG, "doInBackground: "+result);
			return result;
		}

		@Override
		protected void onPostExecute(String sResponse) {
			Log.d(TAG, "onPostExecute: "+sResponse);

			if (e == null && sResponse.length() == 0) {
				Log.d(TAG, "onPostExecute: "+sResponse);
				try {
					ScrollView sv = new ScrollView(mContext);
					LinearLayout ll = new LinearLayout(mContext);
					final LinearLayout panel = new LinearLayout(mContext);

					sv.setBackgroundResource(R.color.all_background);

					ll.setOrientation(LinearLayout.VERTICAL);
					panel.setOrientation(LinearLayout.VERTICAL);

					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT);

					ll.setLayoutParams(params);
					panel.setLayoutParams(params);

					panel.setPadding(5, 0, 5, 0);
					sv.addView(ll);

					ViewStub stub = new ViewStub(mContext);
					stub.setId(R.id.vsHeader);
					stub.setInflatedId(R.id.header);
					stub.setLayoutParams(params);
					stub.setLayoutResource(R.layout.header);


					ll.addView(stub);
					ll.addView(panel);

					TextView tv = new TextView(mContext);
					tv.setTextColor(getResources().getColor(R.color.label_color));

					tv.setPadding(5, 5, 0, 0);
					tv.setText("Tipe Customer");
					panel.addView(tv);

					spinner = new Spinner(mContext);
					ArrayAdapter<CharSequence> adapter;

					adapter = ArrayAdapter.createFromResource(mContext, R.array.CustomerType,
							android.R.layout.simple_spinner_dropdown_item);

					spinner.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					panel.addView(spinner);
					spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
							String selectedItem = parent.getItemAtPosition(position).toString();
							if(selectedItem.equals("Badan Usaha"))
							{
								radioGroup2.setVisibility(View.GONE);
							}else{
								radioGroup2.setVisibility(View.VISIBLE);
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {

						}
					});

					//tambahan radiogroup baru WNI dan WNA
					radioGroup2 = new RadioGroup(mContext);
					radioGroup2.setOrientation(RadioGroup.HORIZONTAL);
					RadioButton[] rbtWarga = new RadioButton[2];
					rbtWarga [0] = new RadioButton(mContext);
					rbtWarga [0].setText("WNI   ");
					rbtWarga [0].setId(0);
					rbtWarga [0].setTextColor(getResources().getColor(R.color.label_color));
					radioGroup2.addView(rbtWarga [0]);

					rbtWarga [1] = new RadioButton(mContext);
					rbtWarga [1].setText("WNA   ");
					rbtWarga [1].setId(satu);
					rbtWarga [1].setTextColor(getResources().getColor(R.color.label_color));
					radioGroup2.addView(rbtWarga [1]);
					radioGroup2.check(0);
					panel.addView(radioGroup2);

					TextView tv2 = new TextView(mContext);
					tv2.setTextColor(getResources().getColor(R.color.label_color));
					tv2.setPadding(5, 5, 0, 0);
					tv2.setText("New / Used");
					panel.addView(tv2);

					radiogroup = new RadioGroup(mContext);
					radiogroup.setOrientation(RadioGroup.HORIZONTAL);

					final RadioButton[] rbtResult = new RadioButton[2];
					rbtResult[0] = new RadioButton(mContext);
					rbtResult[0].setText("New         ");
					rbtResult[0].setId(0);
					rbtResult[0].setTextColor(getResources().getColor(R.color.label_color));
					radiogroup.addView(rbtResult[0]);

					rbtResult[1] = new RadioButton(mContext);
					rbtResult[1].setText("Used         ");
					rbtResult[1].setId(satu);
					rbtResult[1].setTextColor(getResources().getColor(R.color.label_color));

//								radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//									@Override
//									public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//										if(checkedId == rbtResult[0].getId()) {
//										} else if(checkedId == rbtResult[1].getId()) {
//										}
//									}
//								});

					radiogroup.addView(rbtResult[1]);
					radiogroup.check(0);
					panel.addView(radiogroup);


					final Button btnNext = new Button(mContext, null, android.R.attr.buttonStyleSmall);
					btnNext.setText("Next");
					btnNext.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
							ViewGroup.LayoutParams.WRAP_CONTENT));
					panel.addView(btnNext);


					btnNext.setOnClickListener(new Button.OnClickListener() {
						public void onClick(View v) {
							checknewused();
							spinner.setEnabled(false);
							btnNext.setVisibility(View.GONE);
							handlelocation();
							for(int i = 0; i < radioGroup2.getChildCount(); i++){
								((RadioButton)radioGroup2.getChildAt(i)).setEnabled(false);
							}
							for(int i = 0; i < radiogroup.getChildCount(); i++){
								((RadioButton)radiogroup.getChildAt(i)).setEnabled(false);
							}
							//RADIO BUTTON WNI
							if (getValue2().equals("WNI") ) {
								db = datasource.getWritableDatabase();

								theForm = new Form();

								Cursor c = db.rawQuery("SELECT name, label, type, required, optionsname," +
										" optionsid, hint, errmsg, maxlength, issearchable FROM form " +
										"where moduleid = '" + moduleid + "' and custtype = '" +
										spinner.getSelectedItem().toString().substring(0, 1) + "'", null);

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
								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									c.close();
								}

								int i;
								for (i = 0; i < theForm.fields.size(); i++) {

									if (theForm.fields.elementAt(i).getType().equals("text")) {
										theForm.fields.elementAt(i).obj = new EditBox(mContext, " "
												+ theForm.fields.elementAt(i).getLabel(), "", theForm.fields.elementAt(i).getHint(),
												theForm.fields.elementAt(i).getMaxLength());
										((EditBox) theForm.fields.elementAt(i).obj).makeChar();
										if (theForm.fields.elementAt(i).getLabel().equals("Nama Pemohon")){
											theForm.fields.elementAt(i).obj = new EditBox(mContext, " "
													+ theForm.fields.elementAt(i).getLabel(), "", theForm.fields.elementAt(i).getHint(),
													theForm.fields.elementAt(i).getMaxLength());
											((EditBox) theForm.fields.elementAt(i).obj).makeChar2();

										}
										if (theForm.fields.elementAt(i).getLabel().equals("Nama Badan Usaha")){
											theForm.fields.elementAt(i).obj = new EditBox(mContext, " "
													+ theForm.fields.elementAt(i).getLabel(), "", theForm.fields.elementAt(i).getHint(),
													theForm.fields.elementAt(i).getMaxLength());
											((EditBox) theForm.fields.elementAt(i).obj).makeChar2();

										}
										panel.addView((View) theForm.fields.elementAt(i).obj);

									} else if (theForm.fields.elementAt(i).getType().equals("number")) {
										theForm.fields.elementAt(i).obj = new EditBox(mContext, " "
												+ theForm.fields.elementAt(i).getLabel(), "",
												theForm.fields.elementAt(i).getHint(),
												theForm.fields.elementAt(i).getMaxLength());
										((EditBox) theForm.fields.elementAt(i).obj).makeNumber();
										if (theForm.fields.elementAt(i).getLabel().equals("No Identitas")){
											theForm.fields.elementAt(i).obj = new EditBox(mContext, " "
													+ theForm.fields.elementAt(i).getLabel(), "", "No Identitas diinput 16 angka Sesuai KTP",
													"16");
											//theForm.fields.elementAt(i).getMaxLength());
											((EditBox) theForm.fields.elementAt(i).obj).makeNumber2();

										}
										panel.addView((View) theForm.fields.elementAt(i).obj);

									} else if (theForm.fields.elementAt(i).getType().equals("decimal")) {
										theForm.fields.elementAt(i).obj = new EditBox(mContext, " " + theForm.fields.elementAt(i).getLabel(),
												"", theForm.fields.elementAt(i).getHint(), theForm.fields.elementAt(i).getMaxLength());
										((EditBox) theForm.fields.elementAt(i).obj).makeDecimal();
										panel.addView((View) theForm.fields.elementAt(i).obj);

									} else if (theForm.fields.elementAt(i).getType().equals("multiline")) {
										theForm.fields.elementAt(i).obj = new EditBox(mContext, " " + theForm.fields.elementAt(i).getLabel(), "",
												theForm.fields.elementAt(i).getHint(), theForm.fields.elementAt(i).getMaxLength());
										((EditBox) theForm.fields.elementAt(i).obj).makeMultiLine();
										panel.addView((View) theForm.fields.elementAt(i).obj);

									} else if (theForm.fields.elementAt(i).getType().equals("pickone")) {
										theForm.fields.elementAt(i).obj = new PickOne(mContext, (" " +
												theForm.fields.elementAt(i).getLabel()), theForm.fields.elementAt(i).getOptionsName(),
												theForm.fields.elementAt(i).getOptionsID(), theForm.fields.elementAt(i).getIsSearchable());
										panel.addView((View) theForm.fields.elementAt(i).obj);

									} else if (theForm.fields.elementAt(i).getType().equals("pickonechoice")) {
										theForm.fields.elementAt(i).obj = new PickOneChoice(mContext, (" " + theForm.fields.elementAt(i).getLabel()));
										panel.addView((View) theForm.fields.elementAt(i).obj);

									} else if (theForm.fields.elementAt(i).getType().equals("radiobutton")) {
										theForm.fields.elementAt(i).obj = new RadioBtn(mContext, (" " + theForm.fields.elementAt(i).getLabel()), theForm.fields.elementAt(i).getOptionsName(), theForm.fields.elementAt(i).getOptionsID());
										panel.addView((View) theForm.fields.elementAt(i).obj);


									} else if (theForm.fields.  elementAt(i).getType().equals("date")) {
										theForm.fields.elementAt(i).obj = new PickDate(mContext, (" " + theForm.fields.elementAt(i).getLabel()));
										panel.addView((View) theForm.fields.elementAt(i).obj);

									}
									else if (theForm.fields.elementAt(i).getType().equals("pickonesupplier")) {
										theForm.fields.elementAt(i).obj = new PickOneSupplier(mContext, (" " + theForm.fields.elementAt(i).getLabel()), "supplier", "suppliername", "supplierid");
										panel.addView((View) theForm.fields.elementAt(i).obj);

									}
									else if (theForm.fields.elementAt(i).getType().equals("PickTempatLahir")) {
										theForm.fields.elementAt(i).obj = new PickTempatLahir(mContext, (" " + theForm.fields.elementAt(i).getLabel()), "TempatLahir", "namakota", "TempatID");
										panel.addView((View) theForm.fields.elementAt(i).obj);

									}
									else if (theForm.fields.elementAt(i).getType().equals("PickTahun")) {
										if (getValue().equals("New")){
											String baru = getValue().substring(0, 1);
											theForm.fields.elementAt(i).obj = new Pick_tahun(mContext, (" " + theForm.fields.elementAt(i).getLabel()), baru);
										}else{
											String baru = getValue().substring(0, 1);
											theForm.fields.elementAt(i).obj = new Pick_tahun(mContext, (" " + theForm.fields.elementAt(i).getLabel()), baru);

										}
										panel.addView((View) theForm.fields.elementAt(i).obj);

									}

								}

//								btn_search = new Button(mContext, null, android.R.attr.buttonStyleSmall);
//								btn_search.setText("Search");
//								btn_search.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//										ViewGroup.LayoutParams.WRAP_CONTENT));
//								btn_search.setOnClickListener(new View.OnClickListener() {
//									@Override
//									public void onClick(View v) {
//										CheckFormInput();
//										//new SearchData().execute();
//									}
//								});
//								panel.addView(btn_search);
//
//								searchText = new EditText(mContext);
//								searchText.setHint("Grading from IDNumber");
//								searchText.setEnabled(false);
//								searchText.setTextColor(Color.RED);
//								searchText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//										LinearLayout.LayoutParams.WRAP_CONTENT, 1));
//								panel.addView(searchText);
								//panel.addView(View.inflate(mContext, R.layout.layout_spinner_supplier, null));
								TextView tv11 = new TextView(mContext);
								tv11.setTextColor(getResources().getColor(R.color.label_color));
								tv11.setPadding(5, 5, 0, 0);
								tv11.setText("Produk");
								panel.addView(tv11);


								try {
									spinner_product = new SearchableSpinner(mContext);
									if (getValue().equals("New")){
										cekbaruataulama = getValue().substring(0, 1);
										Log.d(TAG, "onClick: cek baru atau lama 1 "+cekbaruataulama);
									}else{
										cekbaruataulama = getValue().substring(0, 1);
										Log.d(TAG, "onClick: cek baru atau lama 2 "+cekbaruataulama);
									}
									ArrayList<String> myResArray1 = new ArrayList<>();
									if(myResArray1!=null){
										myResArray1.clear();
									}
									myResArray1.add("-Select One-");
									myResArray1.addAll(datasource.getAllProduct(cekbaruataulama));

									adapter_product = new ArrayAdapter<>(mContext,R.layout.spinner,myResArray1);
									spinner_product.setAdapter(adapter_product);
									spinner_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
										@Override
										public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
											if(!spinner_product.getSelectedItem().toString().equals("-Select One-")) {
												nmproduk= spinner_product.getSelectedItem().toString();
												idproduk=datasource.getProductID(nmproduk);
												Log.d(TAG, "onItemSelected: idproduk -- "+idproduk);
												spin_prodOffering(idproduk);
												Log.d(TAG, "onItemSelected: "+idsupplier);

											}

										}
										@Override
										public void onNothingSelected(AdapterView<?> parent) {

										}
									});


								}catch (Exception e){
									e.printStackTrace();
								}
								panel.addView(spinner_product);

//
//									btn_product = new Button(mContext, null, android.R.attr.buttonStyleSmall);
//									btn_product.setText("Select Product");
//									btn_product.setOnClickListener(new View.OnClickListener() {
//										@Override
//										public void onClick(View v) {
//											btndialog();
//
//										}
//									});
//
//								panel.addView(btn_product)
								TextView tv223 = new TextView(mContext);
								tv223.setTextColor(getResources().getColor(R.color.label_color));
								tv223.setPadding(5, 5, 0, 0);
								tv223.setText("Product Offering");
								panel.addView(tv223);

								spinner_offering = new SearchableSpinner(mContext);
								panel.addView(spinner_offering);




								TextView tv21 = new TextView(mContext);
								tv21.setTextColor(getResources().getColor(R.color.label_color));
								tv21.setPadding(5, 5, 0, 0);
								tv21.setText("Supplier");
								panel.addView(tv21);

								try {
									spinner_supplier = new Spinner(mContext);
									ArrayList<String> myResArray1 = new ArrayList<>();
									if(myResArray1!=null){
										myResArray1.clear();
									}
									myResArray1.add("-Select One-");
									myResArray1.addAll(datasource.getAllSupplier());

									adapter_supplier = new ArrayAdapter<>(mContext,R.layout.spinner,myResArray1);
									spinner_supplier.setAdapter(adapter_supplier);
									spinner_supplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
										@Override
										public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
											if(!spinner_supplier.getSelectedItem().toString().equals("-Select One-")) {
												nmsupplier= spinner_supplier.getSelectedItem().toString();
												idsupplier=datasource.getSupplierID(nmsupplier);
												Log.d(TAG, "onItemSelected: foreignkey -- "+idsupplier);
												spin2(idsupplier);
												Log.d(TAG, "onItemSelected: "+idsupplier);

											}

										}
										@Override
										public void onNothingSelected(AdapterView<?> parent) {

										}
									});


								}catch (Exception e){
										e.printStackTrace();
								}
								panel.addView(spinner_supplier);


								TextView tv22 = new TextView(mContext);
								tv22.setTextColor(getResources().getColor(R.color.label_color));
								tv22.setPadding(5, 5, 0, 0);
								tv22.setText("Salesman");
								panel.addView(tv22);


								spinner_sales = new Spinner(mContext);
								panel.addView(spinner_sales);


								TextView tv23 = new TextView(mContext);
								tv23.setTextColor(getResources().getColor(R.color.label_color));
								tv23.setPadding(5, 5, 0, 0);
								tv23.setText("Sales SPV");
								panel.addView(tv23);


								spinner_salesSPV = new Spinner(mContext);
								panel.addView(spinner_salesSPV);



								TextView tv24 = new TextView(mContext);
								tv24.setTextColor(getResources().getColor(R.color.label_color));
								tv24.setPadding(5, 5, 0, 0);
								tv24.setText("Sales Koordinator");
								panel.addView(tv24);


								spinner_salesCoor = new Spinner(mContext);
								panel.addView(spinner_salesCoor);


								TextView tv25 = new TextView(mContext);
								tv25.setTextColor(getResources().getColor(R.color.label_color));
								tv25.setPadding(5, 5, 0, 0);
								tv25.setText("Sales Admin");
								panel.addView(tv25);


								spinner_admin = new Spinner(mContext);
								panel.addView(spinner_admin);



								TextView EEVENT = new TextView(mContext);
								EEVENT.setTextColor(getResources().getColor(R.color.label_color));
								EEVENT.setPadding(5, 5, 0, 0);
								EEVENT.setText("PromotionEvent");
								panel.addView(EEVENT);

								try {
									spinner_event = new Spinner(mContext);
									ArrayList<String> myResArray1 = new ArrayList<>();
									if(myResArray1!=null){
										myResArray1.clear();
									}
									myResArray1.add("-Select One-");
									myResArray1.addAll(datasource.getAllEvent());

									adapter_supplier = new ArrayAdapter<>(mContext,R.layout.spinner,myResArray1);
									spinner_event.setAdapter(adapter_supplier);
									spinner_event.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
										@Override
										public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
											if(!spinner_event.getSelectedItem().toString().equals("-Select One-")) {
												nmevent= spinner_event.getSelectedItem().toString();
												Log.d(TAG, "onItemSelected:nmevent "+ nmevent);
												idevent=datasource.getEventID(nmevent);
												Log.d(TAG, "onItemSelected: idevent -- "+idevent);

											}

										}
										@Override
										public void onNothingSelected(AdapterView<?> parent) {

										}
									});


								}catch (Exception e){
									e.printStackTrace();
								}
								panel.addView(spinner_event);



								TextView notes = new TextView(mContext);
								notes.setTextColor(getResources().getColor(R.color.label_color));
								notes.setPadding(5, 5, 0, 0);
								notes.setText("Notes");
								panel.addView(notes);

								catatan = new EditText(mContext);
								catatan.setHint("Catatan");
								catatan.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE| InputType.TYPE_TEXT_FLAG_CAP_WORDS
										| InputType.TYPE_CLASS_TEXT );
								catatan.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
								catatan.setFilters(new InputFilter[] {new InputFilter.LengthFilter(500)});
								catatan.addTextChangedListener(new TextWatcher() {
									@Override
									public void beforeTextChanged(CharSequence s, int start, int count, int after) {

									}

									@Override
									public void onTextChanged(CharSequence s, int start, int before, int count) {
										if (s.toString().contains("'")){
											catatan.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
										}
									}

									@Override
									public void afterTextChanged(Editable s) {

									}
								});
								InputFilter filter1 = new InputFilter() {
									@Override
									public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
										if (source != null && blocksimbol.contains(("" + source))){
											return "";
										}
										return null;
									}
								};
								catatan.setFilters(new InputFilter[]{filter1});

								panel.addView(catatan);


								final Button btnCheck1 = new Button(mContext, null, android.R.attr.buttonStyleSmall);
								btnCheck1.setText("Check");
								btnCheck1.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
										ViewGroup.LayoutParams.WRAP_CONTENT));
								panel.addView(btnCheck1);
								btnCheck1.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										//RADIO BUTTON WNI
										boolean check;
										try{

											check = CheckForm();
											if (!check) {
												Toast.makeText(mContext, "Enter all required (!) fields", Toast.LENGTH_SHORT).show();
												return;
											}

											if (catatan.getText().toString().contains("'")){
												catatan.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
												Toast.makeText(mContext, "Harap Tidak Menggunakan Special Karakter", Toast.LENGTH_SHORT).show();
											}

										}catch (Exception e){
											e.printStackTrace();
										}

//										try {
//											for (int i = 0; i < radiogroup.getChildCount(); i++) {
//												RadioButton rbt = (RadioButton) radiogroup.getChildAt(i);
//												if (rbt.isChecked()) {
//													for(int i2 = 0; i2 < radiogroup.getChildCount(); i2++){
//														((RadioButton)radiogroup.getChildAt(i2)).setEnabled(false);
//													}
//													btnNext1.setVisibility(View.GONE);
//													Toast.makeText(mContext,"udah Di Pilih",Toast.LENGTH_SHORT).show();
//												}
//												else {
//													for(int i2 = 0; i2 < radiogroup.getChildCount(); i2++){
//														((RadioButton)radiogroup.getChildAt(i2)).setEnabled(true);
//													}
//													btnNext1.setVisibility(View.GONE);
//													Toast.makeText(mContext,"Blom Dipilih",Toast.LENGTH_SHORT).show();
//
//												}
//											}
//										}catch (Exception e){
//											e.printStackTrace();
//										}
										btnCheck1.setVisibility(View.GONE);
										final Button btnNext2 = new Button(mContext, null, android.R.attr.buttonStyleSmall);
										btnNext2.setText("Next");
										btnNext2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
												ViewGroup.LayoutParams.WRAP_CONTENT));
										panel.addView(btnNext2);
										btnNext2.setOnClickListener(new View.OnClickListener() {
											@Override
											public void onClick(View v) {
											    // RADIO BUTTIN WNI NEW
                                                if (getValue().equals("New")){
                                                    for(int i = 0; i < radiogroup.getChildCount(); i++) {
                                                        ((RadioButton) radiogroup.getChildAt(i)).setEnabled(false);
                                                    }
                                                    if (catatan.getText().toString().contains("'")){
                                                        catatan.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
                                                        Toast.makeText(mContext, "Harap Tidak Menggunakan Special Karakter", Toast.LENGTH_SHORT).show();
                                                    }
                                                    btnNext2.setVisibility(View.GONE);
                                                    panel.addView(View.inflate(mContext, R.layout.take_photo, null));

                                                    final Button btn = new Button(mContext, null, android.R.attr.buttonStyleSmall);
                                                    btn.setText("Submit");
                                                    btn.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                            ViewGroup.LayoutParams.WRAP_CONTENT));
                                                    btn.setOnClickListener(new Button.OnClickListener() {
                                                        public void onClick(View v) {
                                                            int imageCount = 0;
                                                            boolean check;
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

                                                            if (imageCount == 0 ) {
                                                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                                                builder.setIcon(android.R.drawable.ic_dialog_info);
                                                                builder.setTitle("Information");
                                                                builder.setMessage(R.string.information3)
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

                                                            if (catatan.getText().toString().contains("'")){
                                                                catatan.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
                                                                Toast.makeText(mContext, "Harap Tidak Menggunakan Special Karakter", Toast.LENGTH_SHORT).show();
                                                            }else{
                                                                cekspinner2();
                                                            }

//															check = CheckForm();
//															if (!check) {
//																Toast.makeText(mContext, "Enter all required (!) fields", Toast.LENGTH_SHORT).show();
//
//																return;
//															} else {
//																//new SubmitkeServer().execute();
//																new PostData().execute();
//															}
                                                        }
                                                    });

                                                    panel.addView(btn);

                                                }
                                                // RADIO BUTTON WNI USED
                                                else  if (getValue().equals("Used")){
													for(int i = 0; i < radiogroup.getChildCount(); i++) {
														((RadioButton) radiogroup.getChildAt(i)).setEnabled(false);
													}
													if (catatan.getText().toString().contains("'")){
														catatan.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
														Toast.makeText(mContext, "Harap Tidak Menggunakan Special Karakter", Toast.LENGTH_SHORT).show();
													}
													btnNext2.setVisibility(View.GONE);
													panel.addView(View.inflate(mContext, R.layout.image_button, null));

													final Button btn = new Button(mContext, null, android.R.attr.buttonStyleSmall);
													btn.setText("Submit");
													btn.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
															ViewGroup.LayoutParams.WRAP_CONTENT));
													btn.setOnClickListener(new Button.OnClickListener() {
														public void onClick(View v) {
															int imageCount = 0;
															boolean check;
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

															if (imageCount == 0 ) {
																AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
																builder.setIcon(android.R.drawable.ic_dialog_info);
																builder.setTitle("Information");
																builder.setMessage(R.string.information3)
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

															if (catatan.getText().toString().contains("'")){
																catatan.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
																Toast.makeText(mContext, "Harap Tidak Menggunakan Special Karakter", Toast.LENGTH_SHORT).show();
															}else{
																cekspinner2();
															}

//															check = CheckForm();
//															if (!check) {
//																Toast.makeText(mContext, "Enter all required (!) fields", Toast.LENGTH_SHORT).show();
//
//																return;
//															} else {
//																//new SubmitkeServer().execute();
//																new PostData().execute();
//															}
														}
													});

													panel.addView(btn);
												}else {
													//RADIO BUTTON NEW WNI
//													try {
//														for (int i = 0; i < radiogroup.getChildCount(); i++) {
//															RadioButton rbt = (RadioButton) radiogroup.getChildAt(i);
//															if (rbt.isChecked()) {
//																Toast.makeText(mContext,"udah Di Pilih",Toast.LENGTH_SHORT).show();
//																for(int i2 = 0; i2 < radiogroup.getChildCount(); i2++){
//																	((RadioButton)radiogroup.getChildAt(i2)).setEnabled(false);
//																}
//																btnNext2.setVisibility(View.GONE);
//																break;
//															}
//															else {
//																Toast.makeText(mContext,"Blom Dipilih",Toast.LENGTH_SHORT).show();
//																for(int i2 = 0; i2 < radiogroup.getChildCount(); i2++){
//																	((RadioButton)radiogroup.getChildAt(i2)).setEnabled(true);
//																}
//																btnNext2.setVisibility(View.GONE);
//															}
//														}
//													}catch (Exception e){
//														Log.e(tag, "Error in checkradiobutton() : " + e.getMessage());
//														e.printStackTrace();
//													}
													btnNext2.setVisibility(View.GONE);
													final Button btnSubmit = new Button(mContext, null, android.R.attr.buttonStyleSmall);
													btnSubmit.setText("Submit");
													btnSubmit.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
															ViewGroup.LayoutParams.WRAP_CONTENT));

													btnSubmit.setOnClickListener(new Button.OnClickListener() {
														public void onClick(View v) {
															for(int i = 0; i < radiogroup.getChildCount(); i++){
																((RadioButton)radiogroup.getChildAt(i)).setEnabled(false);
															}

															btnNext2.setVisibility(View.GONE);
															boolean check;
															db = datasource.getWritableDatabase();

															locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

															if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
																buildLocationAlert();
																return;
															}
															if (catatan.getText().toString().contains("'")){
																catatan.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
																Toast.makeText(mContext, "Harap Tidak Menggunakan Special Karakter", Toast.LENGTH_SHORT).show();
															}else{
																cekspinner2();
															}

//															try {
//																check = CheckForm();
//
//																if (!check) {
//																	Toast.makeText(mContext, "Enter all required (!) fields", Toast.LENGTH_SHORT).show();
//
//																	return;
//																} else {
//																	//new SubmitkeServer().execute();
//																	new PostData().execute();
//																}
//
//															}
//															finally {
//																db.close();
//															}
//													Cursor cursor = db.rawQuery("SELECT id FROM " + tableImage, null);
//													try {
//														while (cursor.moveToNext()) {
//															imageCount = imageCount + 1;
//														}
//													} finally {
//														cursor.close();
//														db.close();
//													}
//
//													if (imageCount == 0 ) {
//														AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//														builder.setIcon(android.R.drawable.ic_dialog_info);
//														builder.setTitle("Information");
//														builder.setMessage(R.string.information3)
//																.setCancelable(false)
//																.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//																	public void onClick(final DialogInterface dialog, final int id) {
//																		dialog.dismiss();
//																		return;
//																	}
//																});
//
//														final AlertDialog alert = builder.create();
//														alert.show();
//														return;
//													}
														}
													});

													panel.addView(btnSubmit);
												}


											}
										});


									}
								});

							}
							else {
								//RADIO BUTTON WNA
								Toast.makeText(mContext,"ini bagian WNA",Toast.LENGTH_SHORT).show();
								db = datasource.getWritableDatabase();

								theForm = new Form();

								Cursor c = db.rawQuery("SELECT name, label, type, required, optionsname," +
										" optionsid, hint, errmsg, maxlength, issearchable FROM form " +
										"where moduleid = '" + moduleid + "' and custtype = '" +
										spinner.getSelectedItem().toString().substring(0, 1) + "'", null);

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
								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									c.close();
								}

								int i;
								for (i = 0; i < theForm.fields.size(); i++) {

									if (theForm.fields.elementAt(i).getType().equals("text")) {
											theForm.fields.elementAt(i).obj = new EditBox(mContext, " "
													+ theForm.fields.elementAt(i).getLabel(), "", theForm.fields.elementAt(i).getHint(),
													theForm.fields.elementAt(i).getMaxLength());
											((EditBox) theForm.fields.elementAt(i).obj).makeChar();
										if (theForm.fields.elementAt(i).getLabel().equals("Nama Pemohon")){
											theForm.fields.elementAt(i).obj = new EditBox(mContext, " "
													+ theForm.fields.elementAt(i).getLabel(), "", "Nama Pemohon Sesuai KITAS",
													theForm.fields.elementAt(i).getMaxLength());
											((EditBox) theForm.fields.elementAt(i).obj).makeChar2();

										}else if (theForm.fields.elementAt(i).getLabel().equals("Nama Customer")){
											theForm.fields.elementAt(i).obj = new EditBox(mContext, " "
													+ theForm.fields.elementAt(i).getLabel(), "", "Nama Pemohon Sesuai KITAS",
													theForm.fields.elementAt(i).getMaxLength());
											((EditBox) theForm.fields.elementAt(i).obj).makeChar2();

										}
										else if (theForm.fields.elementAt(i).getLabel().equals("Nama Badan Usaha")){
											theForm.fields.elementAt(i).obj = new EditBox(mContext, " "
													+ theForm.fields.elementAt(i).getLabel(), "", "Nama Badan Usaha Sesuai Akta",
													theForm.fields.elementAt(i).getMaxLength());
											((EditBox) theForm.fields.elementAt(i).obj).makeChar2();

										}
											panel.addView((View) theForm.fields.elementAt(i).obj);
									}

									 else if (theForm.fields.elementAt(i).getType().equals("number")) {
										theForm.fields.elementAt(i).obj = new EditBox(mContext, " " + theForm.fields.elementAt(i).getLabel(),
												"", theForm.fields.elementAt(i).getHint(), theForm.fields.elementAt(i).getMaxLength());
										((EditBox) theForm.fields.elementAt(i).obj).makeNumber();
										if (theForm.fields.elementAt(i).getLabel().equals("No Identitas")){
											theForm.fields.elementAt(i).obj = new EditBox(mContext, " "
													+ theForm.fields.elementAt(i).getLabel(), "", "No Identitas Sesuai KITAS",
													theForm.fields.elementAt(i).getMaxLength());
											((EditBox) theForm.fields.elementAt(i).obj).makeChar();

										}
										panel.addView((View) theForm.fields.elementAt(i).obj);

									} else if (theForm.fields.elementAt(i).getType().equals("decimal")) {
										theForm.fields.elementAt(i).obj = new EditBox(mContext, " " + theForm.fields.elementAt(i).getLabel(),
												"", theForm.fields.elementAt(i).getHint(), theForm.fields.elementAt(i).getMaxLength());
										((EditBox) theForm.fields.elementAt(i).obj).makeDecimal();
										panel.addView((View) theForm.fields.elementAt(i).obj);

									} else if (theForm.fields.elementAt(i).getType().equals("multiline")) {
										theForm.fields.elementAt(i).obj = new EditBox(mContext, " " + theForm.fields.elementAt(i).getLabel(), "",
												theForm.fields.elementAt(i).getHint(), theForm.fields.elementAt(i).getMaxLength());
										((EditBox) theForm.fields.elementAt(i).obj).makeMultiLine();
										panel.addView((View) theForm.fields.elementAt(i).obj);

									} else if (theForm.fields.elementAt(i).getType().equals("pickone")) {
										theForm.fields.elementAt(i).obj = new PickOne(mContext, (" " +
												theForm.fields.elementAt(i).getLabel()), theForm.fields.elementAt(i).getOptionsName(),
												theForm.fields.elementAt(i).getOptionsID(), theForm.fields.elementAt(i).getIsSearchable());
										panel.addView((View) theForm.fields.elementAt(i).obj);

									} else if (theForm.fields.elementAt(i).getType().equals("pickonechoice")) {
										theForm.fields.elementAt(i).obj = new PickOneChoice(mContext, (" " + theForm.fields.elementAt(i).getLabel()));
										panel.addView((View) theForm.fields.elementAt(i).obj);

									} else if (theForm.fields.elementAt(i).getType().equals("radiobutton")) {
										theForm.fields.elementAt(i).obj = new RadioBtn(mContext, (" " + theForm.fields.elementAt(i).getLabel()), theForm.fields.elementAt(i).getOptionsName(), theForm.fields.elementAt(i).getOptionsID());
										panel.addView((View) theForm.fields.elementAt(i).obj);

									} else if (theForm.fields.  elementAt(i).getType().equals("date")) {
										theForm.fields.elementAt(i).obj = new PickDate(mContext, (" " + theForm.fields.elementAt(i).getLabel()));
										panel.addView((View) theForm.fields.elementAt(i).obj);

									} else if (theForm.fields.elementAt(i).getType().equals("pickonesupplier")) {
										theForm.fields.elementAt(i).obj = new PickOneSupplier(mContext, (" " + theForm.fields.elementAt(i).getLabel()), "supplier", "suppliername", "supplierid");
										panel.addView((View) theForm.fields.elementAt(i).obj);
									}
									else if (theForm.fields.elementAt(i).getType().equals("PickTempatLahir")) {
										theForm.fields.elementAt(i).obj = new PickTempatLahir(mContext, (" " + theForm.fields.elementAt(i).getLabel()), "TempatLahir", "namakota", "TempatID");
										panel.addView((View) theForm.fields.elementAt(i).obj);
									}
									else if (theForm.fields.elementAt(i).getType().equals("PickTahun")) {
										if (getValue().equals("New")){
											String baru = getValue().substring(0, 1);
											theForm.fields.elementAt(i).obj = new Pick_tahun(mContext, (" " + theForm.fields.elementAt(i).getLabel()), baru);
										}else{
											String baru = getValue().substring(0, 1);
											theForm.fields.elementAt(i).obj = new Pick_tahun(mContext, (" " + theForm.fields.elementAt(i).getLabel()), baru);

										}
										//theForm.fields.elementAt(i).obj = new Pick_tahun(mContext, (" " + theForm.fields.elementAt(i).getLabel()));
										panel.addView((View) theForm.fields.elementAt(i).obj);

									}

								}


								TextView tv11 = new TextView(mContext);
								tv11.setTextColor(getResources().getColor(R.color.label_color));
								tv11.setPadding(5, 5, 0, 0);
								tv11.setText("Produk");
								panel.addView(tv11);


								try {
									spinner_product = new SearchableSpinner(mContext);
									if (getValue().equals("New")){
										cekbaruataulama = getValue().substring(0, 1);
										Log.d(TAG, "onClick: cek baru atau lama 1 "+cekbaruataulama);
									}else{
										cekbaruataulama = getValue().substring(0, 1);
										Log.d(TAG, "onClick: cek baru atau lama 2 "+cekbaruataulama);
									}
									ArrayList<String> myResArray1 = new ArrayList<>();
									if(myResArray1!=null){
										myResArray1.clear();
									}
									myResArray1.add("-Select One-");
									myResArray1.addAll(datasource.getAllProduct(cekbaruataulama));

									adapter_product = new ArrayAdapter<>(mContext,R.layout.spinner,myResArray1);
									spinner_product.setAdapter(adapter_product);
									spinner_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
										@Override
										public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
											if(!spinner_product.getSelectedItem().toString().equals("-Select One-")) {
												nmproduk= spinner_product.getSelectedItem().toString();
												idproduk=datasource.getProductID(nmproduk);
												Log.d(TAG, "onItemSelected: idproduk -- "+idproduk);
												spin_prodOffering(idproduk);
												Log.d(TAG, "onItemSelected: "+idsupplier);

											}

										}
										@Override
										public void onNothingSelected(AdapterView<?> parent) {

										}
									});


								}catch (Exception e){
									e.printStackTrace();
								}
								panel.addView(spinner_product);

//								btn_product = new Button(mContext, null, android.R.attr.buttonStyleSmall);
//								btn_product.setText("Select Product");
//								btn_product.setOnClickListener(new View.OnClickListener() {
//									@Override
//									public void onClick(View v) {
//										btndialog();
//
//									}
//								});
//
//								panel.addView(btn_product);
								TextView tv223 = new TextView(mContext);
								tv223.setTextColor(getResources().getColor(R.color.label_color));
								tv223.setPadding(5, 5, 0, 0);
								tv223.setText("Product Offering");
								panel.addView(tv223);

								spinner_offering = new SearchableSpinner(mContext);
								panel.addView(spinner_offering);


								TextView tv21 = new TextView(mContext);
								tv21.setTextColor(getResources().getColor(R.color.label_color));
								tv21.setPadding(5, 5, 0, 0);
								tv21.setText("Supplier");
								panel.addView(tv21);

								try {
									spinner_supplier = new Spinner(mContext);
									ArrayList<String> myResArray1 = new ArrayList<>();
									if(myResArray1!=null){
										myResArray1.clear();
									}
									myResArray1.add("-Select One-");
									myResArray1.addAll(datasource.getAllSupplier());

									adapter_supplier = new ArrayAdapter<>(mContext,R.layout.spinner,myResArray1);
									spinner_supplier.setAdapter(adapter_supplier);
									spinner_supplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
										@Override
										public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
											if(!spinner_supplier.getSelectedItem().toString().equals("-Select One-")) {
												nmsupplier= spinner_supplier.getSelectedItem().toString();
												idsupplier=datasource.getSupplierID(nmsupplier);
												Log.d(TAG, "onItemSelected: idsupplier -- "+idsupplier);
												spin2(idsupplier);
												Log.d(TAG, "onItemSelected: "+idsupplier);

											}

										}
										@Override
										public void onNothingSelected(AdapterView<?> parent) {

										}
									});


								}catch (Exception e){
									e.printStackTrace();
								}
								panel.addView(spinner_supplier);


								TextView tv22 = new TextView(mContext);
								tv22.setTextColor(getResources().getColor(R.color.label_color));
								tv22.setPadding(5, 5, 0, 0);
								tv22.setText("Salesman");
								panel.addView(tv22);


								spinner_sales = new Spinner(mContext);
								panel.addView(spinner_sales);


								TextView tv23 = new TextView(mContext);
								tv23.setTextColor(getResources().getColor(R.color.label_color));
								tv23.setPadding(5, 5, 0, 0);
								tv23.setText("Sales SPV");
								panel.addView(tv23);


								spinner_salesSPV = new Spinner(mContext);
								panel.addView(spinner_salesSPV);



								TextView tv24 = new TextView(mContext);
								tv24.setTextColor(getResources().getColor(R.color.label_color));
								tv24.setPadding(5, 5, 0, 0);
								tv24.setText("Sales Koordinator");
								panel.addView(tv24);


								spinner_salesCoor = new Spinner(mContext);
								panel.addView(spinner_salesCoor);


								TextView tv25 = new TextView(mContext);
								tv25.setTextColor(getResources().getColor(R.color.label_color));
								tv25.setPadding(5, 5, 0, 0);
								tv25.setText("Sales Admin");
								panel.addView(tv25);


								spinner_admin = new Spinner(mContext);
								panel.addView(spinner_admin);



								TextView EEVENT = new TextView(mContext);
								EEVENT.setTextColor(getResources().getColor(R.color.label_color));
								EEVENT.setPadding(5, 5, 0, 0);
								EEVENT.setText("PromotionEvent");
								panel.addView(EEVENT);

								try {
									spinner_event = new Spinner(mContext);
									ArrayList<String> myResArray1 = new ArrayList<>();
									if(myResArray1!=null){
										myResArray1.clear();
									}
									myResArray1.add("-Select One-");
									myResArray1.addAll(datasource.getAllEvent());

									adapter_supplier = new ArrayAdapter<>(mContext,R.layout.spinner,myResArray1);
									spinner_event.setAdapter(adapter_supplier);
									spinner_event.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
										@Override
										public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
											if(!spinner_event.getSelectedItem().toString().equals("-Select One-")) {
												nmevent= spinner_event.getSelectedItem().toString();
												Log.d(TAG, "onItemSelected:nmevent "+ nmevent);
												idevent=datasource.getEventID(nmevent);
												Log.d(TAG, "onItemSelected: idevent -- "+idevent);

											}

										}
										@Override
										public void onNothingSelected(AdapterView<?> parent) {

										}
									});


								}catch (Exception e){
									e.printStackTrace();
								}
								panel.addView(spinner_event);



								TextView notes = new TextView(mContext);
								notes.setTextColor(getResources().getColor(R.color.label_color));
								notes.setPadding(5, 5, 0, 0);
								notes.setText("Notes");
								panel.addView(notes);

								catatan = new EditText(mContext);
								catatan.setHint("Catatan");
								catatan.setFilters(new InputFilter[] {new InputFilter.LengthFilter(500)});

								catatan.addTextChangedListener(new TextWatcher() {
									@Override
									public void beforeTextChanged(CharSequence s, int start, int count, int after) {

									}

									@Override
									public void onTextChanged(CharSequence s, int start, int before, int count) {
										if (s.toString().contains("'")){
											catatan.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
										}
									}

									@Override
									public void afterTextChanged(Editable s) {

									}
								});

								InputFilter filter1 = new InputFilter() {
									@Override
									public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
										if (source != null && blocksimbol.contains((""+source))){
											return "";
										}
										return null;
									}
								};
								catatan.setFilters(new InputFilter[]{filter1});
								catatan.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE|InputType.TYPE_CLASS_TEXT
										| InputType.TYPE_TEXT_FLAG_CAP_WORDS);
								panel.addView(catatan);

								//panel.addView(View.inflate(mContext, R.layout.layout_spinner_supplier, null));

//								btn_search = new Button(mContext, null, android.R.attr.buttonStyleSmall);
//								btn_search.setText("Search");
//								btn_search.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//										ViewGroup.LayoutParams.WRAP_CONTENT));
//								btn_search.setOnClickListener(new View.OnClickListener() {
//									@Override
//									public void onClick(View v) {
//										CheckFormInput();
//										//new SearchData().execute();
//									}
//								});
//								panel.addView(btn_search);
//
//								searchText = new EditText(mContext);
//								searchText.setHint("Grading  from IDNumber");
//								searchText.setTextColor(Color.RED);
//								searchText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//										LinearLayout.LayoutParams.WRAP_CONTENT, 1));
//								panel.addView(searchText);
//
//								TextView tv2 = new TextView(mContext);
//								tv2.setTextColor(getResources().getColor(R.color.label_color));
//								tv2.setPadding(5, 5, 0, 0);
//								tv2.setText("New / Used");
//								panel.addView(tv2);
//
//								radiogroup = new RadioGroup(mContext);
//								radiogroup.setOrientation(RadioGroup.HORIZONTAL);
//
//								final RadioButton[] rbtResult = new RadioButton[2];
//								rbtResult[0] = new RadioButton(mContext);
//								rbtResult[0].setText("New         ");
//								rbtResult[0].setId(0);
//								rbtResult[0].setTextColor(getResources().getColor(R.color.label_color));
//								radiogroup.addView(rbtResult[0]);
//
//								rbtResult[1] = new RadioButton(mContext);
//								rbtResult[1].setText("Used         ");
//								rbtResult[1].setId(1);
//								rbtResult[1].setTextColor(getResources().getColor(R.color.label_color));
//								radiogroup.addView(rbtResult[1]);
//								radiogroup.clearCheck();
//								radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//									@Override
//									public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//										if(checkedId == rbtResult[0].getId()) {
//										} else if(checkedId == rbtResult[1].getId()) {
//										}
//
//									}
//								});
//								panel.addView(radiogroup);

								final Button btnNext = new Button(mContext, null, android.R.attr.buttonStyleSmall);
								btnNext.setText("Check");
								btnNext.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
										ViewGroup.LayoutParams.WRAP_CONTENT));
								panel.addView(btnNext);
								btnNext.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										boolean check;

										try{

											check = CheckForm();
											if (!check) {
												Toast.makeText(mContext, "Enter all required (!) fields", Toast.LENGTH_SHORT).show();
												return;
											}

											if (catatan.getText().toString().contains("'")){
												catatan.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
												Toast.makeText(mContext, "Harap Tidak Menggunakan Special Karakter", Toast.LENGTH_SHORT).show();
											}

										}catch (Exception e){
											e.printStackTrace();
										}
//										try {
//											for (int i = 0; i < radiogroup.getChildCount(); i++) {
//												RadioButton rbt = (RadioButton) radiogroup.getChildAt(i);
//												if (rbt.isChecked()) {
//													Toast.makeText(mContext,"udah Di Pilih",Toast.LENGTH_SHORT).show();
//													for(int i2 = 0; i2 < radiogroup.getChildCount(); i2++){
//														((RadioButton)radiogroup.getChildAt(i2)).setEnabled(false);
//													}
//													btnNext.setVisibility(View.GONE);
//												}
//												else {
//													Toast.makeText(mContext,"Blom Dipilih",Toast.LENGTH_SHORT).show();
//													for(int i2 = 0; i2 < radiogroup.getChildCount(); i2++){
//														((RadioButton)radiogroup.getChildAt(i2)).setEnabled(true);
//													}
//													btnNext.setVisibility(View.GONE);
//												}
//											}
//										}catch (Exception e){
//											Log.e(tag, "Error in checkradiobutton() : " + e.getMessage());
//											e.printStackTrace();
//										}
										btnNext.setVisibility(View.GONE);
										final Button btnNext2 = new Button(mContext, null, android.R.attr.buttonStyleSmall);
										btnNext2.setText("Next");
										btnNext2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
												ViewGroup.LayoutParams.WRAP_CONTENT));
										panel.addView(btnNext2);
										btnNext2.setOnClickListener(new View.OnClickListener() {
											@Override
											public void onClick(View v) {
														// RADIO BUTTON USED DI WNA
												if (getValue().equals("Used")) {
													for(int i = 0; i < radiogroup.getChildCount(); i++){
														((RadioButton)radiogroup.getChildAt(i)).setEnabled(false);
													}
													if (catatan.getText().toString().contains("'")){
														catatan.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
														Toast.makeText(mContext, "Harap Tidak Menggunakan Special Karakter", Toast.LENGTH_SHORT).show();
													}
													btnNext2.setVisibility(View.GONE);
													panel.addView(View.inflate(mContext, R.layout.image_button, null));

													final Button btn = new Button(mContext, null, android.R.attr.buttonStyleSmall);
													btn.setText("Submit");
													btn.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
															ViewGroup.LayoutParams.WRAP_CONTENT));

													btn.setOnClickListener(new Button.OnClickListener() {
														public void onClick(View v) {
															int imageCount = 0;
															boolean check,nod;
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

															if (imageCount == 0 ) {
																AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
																builder.setIcon(android.R.drawable.ic_dialog_info);
																builder.setTitle("Information");
																builder.setMessage(R.string.information3)
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
															try{
																if (catatan.getText().toString().contains("'")){
																	catatan.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
																	Toast.makeText(mContext, "Harap Tidak Menggunakan Special Karakter", Toast.LENGTH_SHORT).show();
																}else{
																	cekspinner2();
																}
															}catch (Exception e){
																e.printStackTrace();
															}

//															check = CheckForm();
//															if (!check) {
//																Toast.makeText(mContext, "Enter all required (!) fields", Toast.LENGTH_SHORT).show();
//																return;
//															} else {
//																//new SubmitkeServer().execute();
//																new PostData().execute();
//															}
														}
													});

													panel.addView(btn);

												}else{
													//RADIO BUTTON NEW WNA

													btnNext2.setVisibility(View.GONE);
													final Button btn = new Button(mContext, null, android.R.attr.buttonStyleSmall);
													btn.setText("Submit");
													btn.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
															ViewGroup.LayoutParams.WRAP_CONTENT));

													btn.setOnClickListener(new Button.OnClickListener() {
														public void onClick(View v) {

															for(int i = 0; i < radiogroup.getChildCount(); i++){
																((RadioButton)radiogroup.getChildAt(i)).setEnabled(false);
															}
															btnNext2.setVisibility(View.GONE);
															int imageCount = 0;
															boolean check;
															db = datasource.getWritableDatabase();

															locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

															if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
																buildLocationAlert();
																return;
															}
															try{
																if (catatan.getText().toString().contains("'")){
																	catatan.setError("DIHARAPKAN TIDAK MENGGUNAKAN SIMBOL PETIK (')");
																	Toast.makeText(mContext, "Harap Tidak Menggunakan Special Karakter", Toast.LENGTH_SHORT).show();
																}else{
																	cekspinner2();
																}
															}catch (Exception e){
																e.printStackTrace();
															}
//															try {
//																check = CheckForm();
//																if (!check) {
//																	Toast.makeText(mContext, "Enter all required (!) fields", Toast.LENGTH_SHORT).show();
//																	return;
//																} else {
//																	//new SubmitkeServer().execute();
//																	new PostData().execute();
//																}
//
//															}
//															finally {
//																db.close();
//															}

//													Cursor cursor = db.rawQuery("SELECT id FROM " + tableImage, null);
//													try {
//														while (cursor.moveToNext()) {
//															imageCount = imageCount + 1;
//														}
//													} finally {
//														cursor.close();
//														db.close();
//													}
//
//													if (imageCount == 0 ) {
//														AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//														builder.setIcon(android.R.drawable.ic_dialog_info);
//														builder.setTitle("Information");
//														builder.setMessage(R.string.information3)
//																.setCancelable(false)
//																.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//																	public void onClick(final DialogInterface dialog, final int id) {
//																		dialog.dismiss();
//																		return;
//																	}
//																});
//
//														final AlertDialog alert = builder.create();
//														alert.show();
//														return;
//													}
														}
													});

													panel.addView(btn);
												}


											}
										});

									}
								});

							}

						}

					});

					ProspectActivity.this.setContentView(sv);
					setHeader(getString(R.string.ProspectActivityTitle), true);
					dialog.dismiss();
					loadPhoto();

				} catch (Exception e) {
					dialog.dismiss();
					e.printStackTrace();
				}
			} else {
				dialog.dismiss();
				finish();
				Intent myintent = new Intent(mContext, HomeActivity.class);
				myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(myintent);
				Toast.makeText(mContext, "Connection time out", Toast.LENGTH_SHORT).show();
				e = null;
			}

		}

	}



	private void  btndialog(){
		db = datasource.getWritableDatabase();
		dialog2 = new Dialog(mContext);
		LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = li.inflate(R.layout.pick_one_list, null, false);

		dialog2.setContentView(v);
		dialog2.setTitle("Product");
		dialog2.setCancelable(true);


		searchText = (EditText) dialog2.findViewById(R.id.searchText);
		lv = (ListView) dialog2.findViewById(R.id.lvPickOne);

		if (getValue().equals("New") ){

			String baru = getValue().substring(0, 1);
			Log.d(TAG, "btndialog: "+ baru);
			String sqlqueryName = "SELECT distinct productname FROM product WHERE AssetUsedNew = '"+baru+"' order by productname" ;
			String sqlqueryID = "select  productid  from  product  WHERE AssetUsedNew = '"+baru+"'  order by  productname";
			List<String> lableName = datasource.getAllLabels(db, sqlqueryName);
			List<String> lableID = datasource.getAllLabels(db, sqlqueryID);
			String[]optsName = lableName.toArray(new String[lableName.size()]);
			String[]optsID = lableID.toArray(new String[lableID.size()]);
			for (int i = 0; i < optsName.length; i++) {
				SearchResults sr1 = new SearchResults();
				sr1.setName(optsName[i]);
				sr1.setID(optsID[i]);
				results2.add(sr1);
			}

			if (aa != null){
				aa.notifyDataSetInvalidated();
				aa = null;
			}

			aa = new PickOneAdapter(mContext, results2);
			lv.setAdapter(aa);
			lv.setTextFilterEnabled(true);
			lv.setCacheColorHint(0);
			lv.setFocusable(false);

			if (db != null){
				if (db.isOpen()){
					db.close();
				}
			}

			searchText.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
					aa.getFilter().filter(cs);
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
											  int arg3) {
				}

				@Override
				public void afterTextChanged(Editable arg0) {
				}
			});

			dialog2.show();
		}else{
			String baru = getValue().substring(0, 1);
			Log.d(TAG, "btndialog: else "+ baru);
			String sqlqueryName = "SELECT distinct productname FROM product WHERE AssetUsedNew = '"+baru+"' order by productname" ;
			String sqlqueryID = "select  productid  from  product  WHERE AssetUsedNew = '"+baru+"'  order by  productname";
			List<String> lableName = datasource.getAllLabels(db, sqlqueryName);
			String[]optsName = lableName.toArray(new String[lableName.size()]);
			List<String> lableID = datasource.getAllLabels(db, sqlqueryID);
			String[]optsID = lableID.toArray(new String[lableID.size()]);
			for (int i = 0; i < optsName.length; i++) {
				SearchResults sr1 = new SearchResults();
				sr1.setName(optsName[i]);
				sr1.setID(optsID[i]);
				results2.add(sr1);
			}

			if (aa != null){
				aa.notifyDataSetInvalidated();
				aa = null;
			}

			aa = new PickOneAdapter(mContext, results2);
			lv.setAdapter(aa);
			lv.setTextFilterEnabled(true);
			lv.setCacheColorHint(0);
			lv.setFocusable(false);

			if (db != null){
				if (db.isOpen()){
					db.close();
				}
			}

			searchText.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
					aa.getFilter().filter(cs);
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
											  int arg3) {
				}

				@Override
				public void afterTextChanged(Editable arg0) {
				}
			});

			dialog2.show();

		}

//		String sqlqueryName = "SELECT distinct productname FROM product order by productname" ;
//		List<String> lableName = datasource.getAllLabels(db, sqlqueryName);
//		String[]optsName = lableName.toArray(new String[lableName.size()]);
//		for (int i = 0; i < optsName.length; i++) {
//			SearchResults sr1 = new SearchResults();
//			sr1.setName(optsName[i]);
//			results2.add(sr1);
//		}
//
//		if (aa != null){
//			aa.notifyDataSetInvalidated();
//			aa = null;
//		}
//
//		aa = new PickOneAdapter(mContext, results2);
//		lv.setAdapter(aa);
//		lv.setTextFilterEnabled(true);
//		lv.setCacheColorHint(0);
//		lv.setFocusable(false);
//
//		if (db != null){
//			if (db.isOpen()){
//				db.close();
//			}
//		}
//
//		searchText.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
//				aa.getFilter().filter(cs);
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//										  int arg3) {
//			}
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//			}
//		});
//
//		dialog.show();
	}

	private void  checkrb(){
	try {
		theForm = new Form();
		int i;
		for (i = 0; i < theForm.fields.size(); i++) {

				if (theForm.fields.elementAt(i).getLabel().equals("Mandiri KPM")){
					String fieldValue = (String) theForm.fields.elementAt(i).getData();
					Log.d(TAG, "Checkradio button fieldValue : "+fieldValue);
				}else if (theForm.fields.elementAt(i).getLabel().equals("Angsuran Pertama")){
					String fieldValue2 = (String) theForm.fields.elementAt(i).getData();
					Log.d(TAG, "Checkradio button fieldValue2 : "+fieldValue2);
				}

		}
	} catch (Exception e) {
		Log.e(tag, "Error in CheckForm() : " + e.getMessage());
		e.printStackTrace();

	}
}

	private void spin_prodOffering(final String idproduk2){
		ArrayList<String> myResArray51 = new ArrayList<>();
		if(myResArray51!=null){
			myResArray51.clear();

		}
		Log.d(TAG, "spin_prodOffering: idproduk2 " + idproduk2);
		myResArray51.add("-Select One-");
		myResArray51.addAll(datasource.getAllproductoffering(idproduk2));
		Log.d(TAG, "spin_prodOffering: "+myResArray51);
		if (myResArray51.contains("-Select One-")){
			myResArray51.add("--");
		}


		adapter_productOffering = new ArrayAdapter<>(mContext,R.layout.spinner,myResArray51);
		spinner_offering.setAdapter(adapter_productOffering);
		spinner_offering.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				if(!spinner_offering.getSelectedItem().toString().equals("-Select One-")) {
					if (spinner_offering.getSelectedItem().toString().equals("--")){
						String suup2 = idproduk2;
						Log.d(TAG, "onItemSelected: idproduk2 "+ suup2);
						nmprodOffering= spinner_offering.getSelectedItem().toString();
						Log.d(TAG, "onItemSelected: nmprodOffering yg salah " + nmprodOffering);
						idprodOffering=datasource.getProductID(nmprodOffering);
						Log.d(TAG, "onItemSelected: cek lagi idprodOffering " + idprodOffering );


					}else{
						nmprodOffering= spinner_offering.getSelectedItem().toString();
						idprodOffering=datasource.getProductofferingID(nmprodOffering);
						Log.d(TAG, "onItemSelected: idprodOffering -- "+idprodOffering);
					}


				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void spin2(final String suppid1){
		ArrayList<String> myResArray2 = new ArrayList<>();
		if(myResArray2!=null){
			myResArray2.clear();
		}
		Log.d(TAG, "spin2: spinner_sales idsupplier"+suppid1);
		myResArray2.add("-Select One-");
		myResArray2.addAll(datasource.getAllSalesman(branchID,suppid1));
		Log.d(TAG, "spin2: "+myResArray2);
		if (myResArray2.contains("-Select One-")){
			myResArray2.add("--");
		}

		adapter_salesman = new ArrayAdapter<>(mContext,R.layout.spinner,myResArray2);
		spinner_sales.setAdapter(adapter_salesman);
		spinner_sales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



				if(!spinner_sales.getSelectedItem().toString().equals("-Select One-")) {
					if(spinner_sales.getSelectedItem().toString().equals("--")) {
						String suup21 = suppid1;
						Log.d(TAG, "onItemSelected: "+ suup21);
						nmsalesman= spinner_sales.getSelectedItem().toString();
						Log.d(TAG, "onItemSelected: nmsalesman yg salah " + nmsalesman);
						idsalesman = suppid1;
						Log.d(TAG, "onItemSelected: cek lagi " + idsalesman );
						spin3(idsalesman);
					}
					else {
						nmsalesman= spinner_sales.getSelectedItem().toString();
						Log.d(TAG, "onItemSelected: nmsalesman -- "  + nmsalesman);
						//idsalesman=datasource.getSalesmanID(nmsalesman);
						idsalesman=suppid1;
						Log.d(TAG, "onItemSelected: idsalesman -- "+idsalesman);
						spin3(idsalesman);
					}

				}



//				if(!spinner_sales.getSelectedItem().toString().equals("-Select One-")) {
//					nmsalesman= spinner_sales.getSelectedItem().toString();
//					Log.d(TAG, "onItemSelected: nmsalesman -- "  + nmsalesman);
//					idsalesman=datasource.getSalesmanID(nmsalesman);
//					Log.d(TAG, "onItemSelected: idsalesman -- "+idsalesman);
//					spin3(idsalesman);
//
//				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void spin3(final String idsupp3){
		ArrayList<String> myResArray3 = new ArrayList<>();
		if(myResArray3!=null){
			myResArray3.clear();

		}
		Log.d(TAG, "Pick_Spinner1: idsupplier"+idsupp3);
		myResArray3.add("-Select One-");
		myResArray3.addAll(datasource.getAllSalesSPV(branchID,idsupp3));
		Log.d(TAG, "spin3: "+myResArray3);
		if (myResArray3.contains("-Select One-")){
			myResArray3.add("--");
		}

		adapter_salesspv = new ArrayAdapter<>(mContext,R.layout.spinner,myResArray3);
		spinner_salesSPV.setAdapter(adapter_salesspv);
		spinner_salesSPV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(!spinner_salesSPV.getSelectedItem().toString().equals("-Select One-")) {
					if(spinner_salesSPV.getSelectedItem().toString().equals("--")) {
						String suup21 = idsupp3;
						Log.d(TAG, "onItemSelected: "+ suup21);
						nmsalesspv= spinner_salesSPV.getSelectedItem().toString();
						Log.d(TAG, "onItemSelected: nmsalesspv yg salah " + nmsalesspv);
						idsalesspv = suup21;
						Log.d(TAG, "onItemSelected: cek lagi " + idsalesspv );
						spin4(idsalesspv);
					}
					else {
						nmsalesspv= spinner_salesSPV.getSelectedItem().toString();
						Log.d(TAG, "onItemSelected: nmsalesspv " + nmsalesspv);
						//idsalesspv=datasource.getSalesSPVID(nmsalesspv);
						idsalesspv=idsupp3;
						Log.d(TAG, "onItemSelected: idsalesspv -- "+idsalesspv);
						spin4(idsalesspv);
					}

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void spin4(final String idsupp){
		ArrayList<String> myResArray4 = new ArrayList<>();
		if(myResArray4!=null){
			myResArray4.clear();

		}
		Log.d(TAG, "spin4: idsupplier " + idsupp);
		myResArray4.add("-Select One-");
		myResArray4.addAll(datasource.getAllSalesKoordinasi(branchID,idsupp));
		Log.d(TAG, "spin4: "+myResArray4);
		if (myResArray4.contains("-Select One-")){
			myResArray4.add("--");
		}

		adapter_salekoordinasi = new ArrayAdapter<>(mContext,R.layout.spinner,myResArray4);
		spinner_salesCoor.setAdapter(adapter_salekoordinasi);
		spinner_salesCoor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(!spinner_salesCoor.getSelectedItem().toString().equals("-Select One-")) {
					if (spinner_salesCoor.getSelectedItem().toString().equals("--")){
						String suup2 = idsupp;
						Log.d(TAG, "onItemSelected: "+ suup2);
						nmsaleskoor= spinner_salesCoor.getSelectedItem().toString();
						Log.d(TAG, "onItemSelected: nmsaleskoor yg salah " + nmsaleskoor);
						idsaleskoor = suup2;
						Log.d(TAG, "onItemSelected: cek lagi " + idsaleskoor );
						spin5(idsaleskoor);

					}else{
						nmsaleskoor= spinner_salesCoor.getSelectedItem().toString();
						//idsaleskoor=datasource.getSalesKoordinasiID(nmsaleskoor);
						idsaleskoor=idsupp;
						Log.d(TAG, "onItemSelected: idsaleskoor -- "+idsaleskoor);
						spin5(idsaleskoor);
					}


				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void spin5(final String supp5){
		ArrayList<String> myResArray5 = new ArrayList<>();
		if(myResArray5!=null){
			myResArray5.clear();

		}
		Log.d(TAG, "spin5: idsupplier " + supp5);
		myResArray5.add("-Select One-");
		myResArray5.addAll(datasource.getAllSalesAdmin(branchID,supp5));
		Log.d(TAG, "spin4: "+myResArray5);
		if (myResArray5.contains("-Select One-")){
			myResArray5.add("--");
		}


		adapter_salesadmin = new ArrayAdapter<>(mContext,R.layout.spinner,myResArray5);
		spinner_admin.setAdapter(adapter_salesadmin);
		spinner_admin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				if(!spinner_admin.getSelectedItem().toString().equals("-Select One-")) {
					if (spinner_admin.getSelectedItem().toString().equals("--")){
						String suup2 = supp5;
						Log.d(TAG, "onItemSelected: "+ supp5);
						nmsalesadm= spinner_salesCoor.getSelectedItem().toString();
						Log.d(TAG, "onItemSelected: nmsalesadm yg salah " + nmsalesadm);
						idsalesadm = supp5;
						Log.d(TAG, "onItemSelected: cek lagi idsalesadm " + idsalesadm );


					}else{
						nmsalesadm= spinner_admin.getSelectedItem().toString();
						//idsalesadm=datasource.getSalesAdminID(nmsalesadm);
						idsalesadm=supp5;
						Log.d(TAG, "onItemSelected: idsalesadm -- "+idsalesadm);
					}


				}



//				if(!spinner_admin.getSelectedItem().toString().equals("-Select One-")) {
//					nmsalesadm= spinner_admin.getSelectedItem().toString();
//					idsalesadm=datasource.getSalesAdminID(nmsalesadm);
//					Log.d(TAG, "onItemSelected: idsalesadm -- "+idsalesadm);
//				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}




	private void checknewused(){
		if (radiogroup.getCheckedRadioButtonId() == -1)
		{
			final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setIcon(android.R.drawable.ic_dialog_info);
			builder.setTitle("Information");
			builder.setMessage("ISI TERLEBIH DAHULU PILIHAN NEW ATAU USED").
					setCancelable(false).setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(final DialogInterface dialog, final int id) {
							dialog.dismiss();
						}
					});
			final AlertDialog alert = builder.create();
			alert.show();
		}
		else
		{
			Toast.makeText(mContext,"sudah diisi",Toast.LENGTH_SHORT).show();
		}
	}

	private void cekspinner2(){
		boolean check;
		db = datasource.getWritableDatabase();
		try{

			if(spinner_product.getSelectedItem().toString().equals("-Select One-")){
				TextView textView= (TextView)spinner_product.getSelectedView();
				textView.setError("");
				textView.setTextColor(Color.RED);
				textView.setText("Harap Diisi Terlebih dahulu");
			}
			else if(spinner_offering.getSelectedItem().toString().equals("-Select One-")){
				TextView textView= (TextView)spinner_offering.getSelectedView();
				textView.setError("");
				textView.setTextColor(Color.RED);
				textView.setText("Harap Diisi Terlebih dahulu");
			}

			else if(spinner_supplier.getSelectedItem().toString().equals("-Select One-")){
				TextView textView= (TextView)spinner_supplier.getSelectedView();
				textView.setError("");
				textView.setTextColor(Color.RED);
				textView.setText("Harap Diisi Terlebih dahulu");
			}

			else if(spinner_supplier.getSelectedItem().toString().equals("-Select One-")){
				TextView textView= (TextView)spinner_supplier.getSelectedView();
				textView.setError("");
				textView.setTextColor(Color.RED);
				textView.setText("Harap Diisi Terlebih dahulu");
			}
			  else if(spinner_sales.getSelectedItem().toString().equals("-Select One-")){
				TextView textView= (TextView)spinner_sales.getSelectedView();
				textView.setError("");
				textView.setTextColor(Color.RED);
				textView.setText("Harap Diisi Terlebih dahulu");
			}
			else if(spinner_salesSPV.getSelectedItem().toString().equals("-Select One-")){
				TextView textView= (TextView)spinner_salesSPV.getSelectedView();
				textView.setError("");
				textView.setTextColor(Color.RED);
				textView.setText("Harap Diisi Terlebih dahulu");
			}
			else if(spinner_salesCoor.getSelectedItem().toString().equals("-Select One-")){
				TextView textView= (TextView)spinner_salesCoor.getSelectedView();
				textView.setError("");
				textView.setTextColor(Color.RED);
				textView.setText("Harap Diisi Terlebih dahulu");
			}
			else if(spinner_admin.getSelectedItem().toString().equals("-Select One-")){

				TextView textView= (TextView)spinner_admin.getSelectedView();
				textView.setError("");
				textView.setTextColor(Color.RED);
				textView.setText("Harap Diisi Terlebih dahulu");
			}
			else if(spinner_event.getSelectedItem().toString().equals("-Select One-")){

				TextView textView= (TextView)spinner_event.getSelectedView();
				textView.setError("");
				textView.setTextColor(Color.RED);
				textView.setText("Harap Diisi Terlebih dahulu");
			}
			else if(catatan.getText().toString().equals("")){
					catatan.setError("Harap Diisi Terlebih dahulu");
			}
			else {
				try {
				check = CheckForm();
				if (!check) {
					Toast.makeText(mContext, "Enter all required (!) fields", Toast.LENGTH_SHORT).show();
					return;
				} else {
					new PostData().execute();
					}
				}
					finally {
						db.close();
				}

			}

		}catch (Exception e){
			e.printStackTrace();
		}


	}

	private boolean isValid(String str){
		boolean isValid = false;
		String charr = "[a-z_A-Z0-9 ]*$";
		CharSequence input = str;
		Pattern pattern = Pattern.compile(charr);
		Matcher matcher = pattern.matcher(input);
		if (matcher.matches()){
			isValid  = true;
		}
		return  isValid;
	}



	LocationListener locationListenerGps = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
			if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
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
			if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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
		double longitude = newLocation.getLongitude();

		latString = Double.toString(latitude);
		lngString = Double.toString(longitude);
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
		for (int i = 0; i < radioGroup2.getChildCount(); i++) {
			RadioButton rbt = (RadioButton) radioGroup2.getChildAt(i);
			if (rbt.isChecked()) {
				text = (String) rbt.getText().toString().trim();
				break;
			}
		}
		return text.trim();
	}

	private String ambilnilai()
	{
		//return (String) value_product;
		return (String)idproduk;
	}

	private boolean CheckForm() {
		try {
			int i;
			boolean good = true;

			for (i = 0; i < theForm.fields.size(); i++) {
				String fieldValue = (String) theForm.fields.elementAt(i).getData();
				Log.d(TAG, "CheckForm: "+fieldValue);

				if (theForm.fields.elementAt(i).getLabel().equals("Nama Pemohon")) {

					if (fieldValue.trim().length() < 3) {
						theForm.fields.elementAt(i).setData(theForm.fields.elementAt(i).getErrmsg());
						good = false;
					}

				}

				if (theForm.fields.elementAt(i).getLabel().equals("No Identitas")) {

					if (fieldValue.trim().length() < 16) {
						theForm.fields.elementAt(i).setData(theForm.fields.elementAt(i).getErrmsg());
						good = false;
					}

				}

				if (theForm.fields.elementAt(i).isRequired()) {
					if (fieldValue == null || fieldValue.trim().length() == 0) {
						theForm.fields.elementAt(i).setData(theForm.fields.elementAt(i).getErrmsg());
						good = false;
					}

				}

				if (theForm.fields.elementAt(i).getLabel().equals("Tahun Perakitan")){
					String fieldValue2 = (String) theForm.fields.elementAt(i).getData();
					if (fieldValue2.contains("Select One")){
						theForm.fields.elementAt(i).setData(theForm.fields.elementAt(i).getErrmsg());
						good = false;
					}

				}

				if (theForm.fields.elementAt(i).getLabel().equals("Tahun Pembelian")){
					String fieldValue2 = (String) theForm.fields.elementAt(i).getData();
					if (fieldValue2.contains("Select One")){
						theForm.fields.elementAt(i).setData(theForm.fields.elementAt(i).getErrmsg());
						good = false;
					}

				}


			}

			return good;
		} catch (Exception e) {
			Log.e(tag, "Error in CheckForm() : " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}


		//--CEK GRADING DATA--//
	private boolean CheckFormInput() {
		try {
			int i;
			boolean good = true;
			String fieldValue2 = "";
			for (i = 0; i < theForm.fields.size(); i++) {
				if (theForm.fields.elementAt(i).getLabel().equals("No Identitas")) {
					fieldValue2 = (String) theForm.fields.elementAt(i).getData();
					if (theForm.fields.elementAt(i).isRequired()) {
						if (fieldValue2 == null || fieldValue2.trim().length() == 0) {
							theForm.fields.elementAt(i).setData(theForm.fields.elementAt(i).getErrmsg());
							AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
							builder.setIcon(android.R.drawable.ic_dialog_info);
							builder.setTitle("Information");
							builder.setMessage("PLEASE INPUT NO IDENTITAS")
									.setCancelable(false)
									.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
										public void onClick(final DialogInterface dialog, final int id) {
											dialog.dismiss();
											return;
										}
									});
							final AlertDialog alert = builder.create();
							alert.show();
							good = false;
						}
						else {
							new SearchData().execute();

						}

					}
				}
				else if (theForm.fields.elementAt(i).getLabel().equals("No NPWP Badan Usaha")) {
						fieldValue2 = (String) theForm.fields.elementAt(i).getData();
						if (theForm.fields.elementAt(i).isRequired()) {
							if (fieldValue2 == null || fieldValue2.trim().length() == 0) {
								theForm.fields.elementAt(i).setData(theForm.fields.elementAt(i).getErrmsg());
								AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
								builder.setIcon(android.R.drawable.ic_dialog_info);
								builder.setTitle("Information");
								builder.setMessage("PLEASE INPUT No NPWP Badan Usaha")
										.setCancelable(false)
										.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
											public void onClick(final DialogInterface dialog, final int id) {
												dialog.dismiss();
												return;
											}
										});
								final AlertDialog alert = builder.create();
								alert.show();
								good = false;
							}else {

								new SearchData().execute();
							}
						}
				}

			}
			return good;
		} catch (Exception e) {
			Log.e(tag, "Error in CheckForm() : " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	private class SearchData extends AsyncTask<String, Void, String> {

		SweetAlertDialog loading = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//dialog.show();
			loading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
			loading.setTitleText("Searching ......");
			loading.setCancelable(false);
			loading.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			db = datasource.getWritableDatabase();
			try{
				int i;
				for (i = 0; i < theForm.fields.size(); i++) {
					if (theForm.fields.elementAt(i).getLabel().equals("No Identitas")){
						fieldValue = theForm.fields.elementAt(i).getData().toString();
						Log.d(TAG, "doInBackground:No Identitas " + fieldValue);
						identitas.setNoId(fieldValue);
					} else if (theForm.fields.elementAt(i).getLabel().equals("No NPWP Badan Usaha")){
						fieldValue = theForm.fields.elementAt(i).getData().toString();
						Log.d(TAG, "doInBackground: No NPWP Badan Usaha " + fieldValue);
						identitas.setNoId(fieldValue);
					}

				}

				if (!GetGradingData())
					result = "ErrorConnection";

				if (results != null) {
					if (results.size() > 0) {
						results.clear();
					}
				}


				Cursor c = db.rawQuery("SELECT IDNumber, CustomerID, CustomerGrade FROM grading ", null);
				try {
					while (c.moveToNext()) {
						Identitas sr1 =new Identitas();
						sr1.setIdNumber(c.getString(0));
						sr1.setCustomerID(c.getString(1));
						sr1.setCustomerGrade(c.getString(2));
						results.add(sr1);
						Log.d(TAG, "doInBackground: "+ results);
					}
				} finally {
					c.close();

				}
			}catch (Exception e){
				e.printStackTrace();
			}

			return result;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			Log.d(TAG, "onPostExecute:s "+ s);

			for (int i = 0; i < results.size(); i++) {
				searchText.setText("Grading  Customer : " + results.get(i).getCustomerGrade());
				Log.d(TAG, "onPostExecute: Gradingnya " +results.get(i).getCustomerGrade());
			}
			Txt_Grading = searchText.getText().toString().substring(20,27);
			Log.d(TAG, "onPostExecute: Txt_Grading "+ Txt_Grading);
			loading.dismissWithAnimation();
//			if (results !=null){
//				for (int i = 0; i < results.size(); i++) {
//					Log.d(TAG, "onPostExecute: results.size() "+results.size());
//					Toast.makeText(mContext,"dapet Gradingnya "+results.get(i).getCustomerGrade(),Toast.LENGTH_SHORT).show();
//					searchText.setText(results.get(i).getCustomerGrade());
//					Log.d(TAG, "onPostExecute: nmcabang" +results.get(i).getCustomerGrade());
//				}
//				loading.dismissWithAnimation();
//
//			}
//			else
//			{
//
//				Toast.makeText(mContext,"Connection Time Out",Toast.LENGTH_SHORT).show();
//				loading.dismissWithAnimation();
//			}

		}
	}
	private boolean GetGradingData() {
		try {
			if (!db.isOpen()) {
				db = datasource.getWritableDatabase();
				Log.d(TAG, "GetGradingData: database"+db);
			}

			InputStream in = null;
			ContentValues cv = new ContentValues();


			datasource.deleteData(db, "", "grading");

			StringBuilder sbl = new StringBuilder();
			sbl.append("");


			Calendar cl = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String myTime = df.format(cl.getTime());


			String noaja = identitas.getNoId();
			Log.d(TAG, "GetGradingData: noaja " + noaja);

			String theKey = datalocal.encrypt(myTime, getString(R.string.Key44));
			String idnumber2 = datalocal.encrypt(noaja,myTime);
			String nocabang = datalocal.encrypt(branchID,myTime);

			Log.d(TAG, "GetGradingData: nocabang " + branchID + nocabang);

			URL localUrl = new URL(getString(R.string.url) + "/GetGradingData?" +
					"&c=" + URLEncoder.encode(theKey, "UTF-8")+
					"&a=" + URLEncoder.encode(nocabang, "UTF-8")+
					"&b=" + URLEncoder.encode(idnumber2, "UTF-8"));

			Log.d(TAG, "GetGradingData: "+localUrl);

			URLConnection conn = localUrl.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
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

			NodeList fields = root.getElementsByTagName("grade");
			Log.d(TAG, "GetGradingData: cekpertama fields"+fields);
			for (int i = 0; i < fields.getLength(); i++) {

				Node fieldNode = fields.item(i);
				NamedNodeMap attr = fieldNode.getAttributes();


				cv.put("IDNumber", attr.getNamedItem("IDNumber").getNodeValue());
				cv.put("CustomerID", attr.getNamedItem("CustomerID").getNodeValue());
				cv.put("CustomerGrade", attr.getNamedItem("CustomerGrade").getNodeValue());

				datasource.generateData(db, cv, "grading");
				cv.clear();
			}


			docb = null;
			doc = null;

			return true;

		} catch (Exception e) {
			Log.e("GetGradingData", "GetGradingData: "+e.getMessage() );
			e.printStackTrace();
			return false;
		}
	}
		//--CEK GRADING DATA--//




	private class PostData extends AsyncTask<Void, Void, String> {
		String buatspinner = spinner.getSelectedItem().toString().substring(0, 1);


		//String prod = spinner_produk.getSelectedItem().toString();

		//String prod = ambilnilai();

		String prodid = spinner_product.getSelectedItem().toString();
		String idprod1 = datasource.getProductID(prodid);

		String Offerid = spinner_offering.getSelectedItem().toString();
		String idprodOffer = datasource.getProductofferingID(Offerid);

		String supp = spinner_supplier.getSelectedItem().toString();
		String idsupplier2=datasource.getSupplierID2(supp);

		String salesm = spinner_sales.getSelectedItem().toString();
		String salesSpv = spinner_salesSPV.getSelectedItem().toString();
		String saleskor = spinner_salesCoor.getSelectedItem().toString();
		String salesamd = spinner_admin.getSelectedItem().toString();
		String event = spinner_event.getSelectedItem().toString();
		String event1=datasource.getEventID(event);
		String notes1 = catatan.getText().toString();



		@Override
		protected void onPreExecute() {
			dialog.show();
		}

		@Override

		protected String doInBackground(Void... params) {
			try {
				db = datasource.getWritableDatabase();
				String image_str = null;
				String image_lat_str = null;
				String image_lng_str = null;



				int cnt = 0;

				String[] data = theForm.getEncodedData();
				ContentValues cv = new ContentValues();

				Cursor c = db.rawQuery("SELECT imageStr, lat, lng FROM " + tableImage, null);

				try {
					while (c.moveToNext()) {
						cnt++;
						if (image_str == null) {
							image_str = c.getString(0);
							image_lat_str = c.getString(1);
							image_lng_str = c.getString(2);
						} else {
							image_str = image_str + "split" + c.getString(0);
							image_lat_str = image_lat_str + ";" + c.getString(1);
							image_lng_str = image_lng_str + ";" + c.getString(2);
						}
					}
				} finally {
					c.close();
				}

				if (image_str == null) {
					image_str = "x";
					image_lat_str = "x";
					image_lng_str = "x";
					cnt = 0;
				}

				Calendar cl = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String myTime = df.format(cl.getTime());

				cv.clear();
//				cv.put("fieldname", data[0] + "branchid" + ";usednew");
//				cv.put("fieldvalue", data[1] + "'" + generalPrefs.getString("branchID", null).trim() + "'"
//						+ ";'" + getValue().substring(0, 1) + "'");

				if (buatspinner.contains("B")){
					cv.put("fieldname", data[0] + "branchid" + ";usednew" + ";CustGrading"+ ";Nationality"
							+ ";ProductID" +  ";ProductOfferingID"+ ";SupplierID"+ ";Sales"
							//+ ";ProductID" + ";SupplierID"+ ";Sales"
							+ ";SalesSPV" + ";SalesCoor" + ";SuplierAdmin"+ ";PromotionEvent" + ";Notes");
					cv.put("fieldvalue", data[1] + "'" + generalPrefs.getString("branchID", null).trim() + "'"
							+ ";'" + getValue().substring(0, 1) + "'" + ";'" + Txt_Grading+ "'"
							+ ";'" + "" + "'"
							+ ";'" + idprod1 + "'"
							+ ";'" + idprodOffer + "'"
							//+ ";'" + prod + "'"
							+ ";'" + idsupplier2 + "'"
							+ ";'" + salesm + "'"
							+ ";'" + salesSpv + "'"
							+ ";'" + saleskor + "'"
							+ ";'" + salesamd + "'"
							+ ";'" + event1 + "'"
							+ ";'" + notes1 + "'");
					cv.put("imagestr", image_str);
					cv.put("imagecount", String.valueOf(cnt));
					cv.put("moduleid", moduleid);
					cv.put("key", "");
					cv.put("lat", latString);
					cv.put("lng", lngString);
					cv.put("dtmupd", tglserver);
					//cv.put("dtmupd", myTime);
					cv.put("userid", generalPrefs.getString("userID", null));
					cv.put("imagelat", image_lat_str);
					cv.put("imagelng", image_lng_str);
					//cv.put("custtype", spinner.getSelectedItem().toString().substring(0, 1));
					cv.put("custtype",buatspinner);
					cv.put("grading",Txt_Grading); //tambahan untuk bagian grading
					//cv.put("paiddate",""); //tambahan untuk di field paiddate di result
					cv.put("branchID",branchID); //tambahan untuk di field paiddate di result
				}else {
					cv.put("fieldname", data[0] + "branchid" + ";usednew" + ";CustGrading"+ ";Nationality"
							+ ";ProductID" +  ";ProductOfferingID"+ ";SupplierID"+ ";Sales"
							+ ";SalesSPV" + ";SalesCoor" + ";SuplierAdmin"+ ";PromotionEvent" + ";Notes");
					cv.put("fieldvalue", data[1] + "'" + generalPrefs.getString("branchID", null).trim() + "'"
							+ ";'" + getValue().substring(0, 1) + "'" + ";'" + Txt_Grading+ "'"
							+ ";'" + getValue2().substring(0, 3) + "'"
							+ ";'" + idprod1 + "'"
							+ ";'" + idprodOffer + "'"
							+ ";'" + idsupplier2 + "'"
							+ ";'" + salesm + "'"
							+ ";'" + salesSpv + "'"
							+ ";'" + saleskor + "'"
							+ ";'" + salesamd + "'"
							+ ";'" + event1 + "'"
							+ ";'" + notes1 + "'");
					cv.put("imagestr", image_str);
					cv.put("imagecount", String.valueOf(cnt));
					cv.put("moduleid", moduleid);
					cv.put("key", "");
					cv.put("lat", latString);
					cv.put("lng", lngString);
					//cv.put("dtmupd", myTime);
					cv.put("dtmupd", tglserver);
					cv.put("userid", generalPrefs.getString("userID", null));
					cv.put("imagelat", image_lat_str);
					cv.put("imagelng", image_lng_str);
					//cv.put("custtype", spinner.getSelectedItem().toString().substring(0, 1));
					cv.put("custtype",buatspinner);
					cv.put("grading",Txt_Grading); //tambahan untuk bagian grading
					//cv.put("paiddate",""); //tambahan untuk di field paiddate di result
					cv.put("branchID",branchID); //tambahan untuk di field paiddate di result
				}

				Log.d(TAG, "doInBackground: prospek"
						+"\n" + moduleid
						+"\n" + data[0]
						+"\n" + data[1]
						+"\n" + latString
						+"\n" + lngString
						+"\n" + image_lat_str
						+"\n" + image_lng_str
						+"\n" + tglserver
						+"\n" + buatspinner
						+"\n" + branchID
						+"\n" + idprod1
						+"\n" + idprodOffer);

				datasource.generateData(db, cv, "result");
				cv.clear();

			} catch (Exception e) {
				e.printStackTrace();
				ProspectActivity.this.e = e;
			} finally {
				db.close();
			}

			return "";
		}

		@Override
		protected void onPostExecute(String sResponse) {
			Log.d(TAG, "onPostExecute: "+sResponse);
			if (e == null) {
				Log.d(TAG, "onPostExecute2: "+sResponse);

				//if (sResponse.length()>0){
				finish();
				Intent myintent = new Intent(mContext, HomeActivity.class);
				myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(myintent);

				dialog.dismiss();
				//dialog2.dismiss();
				Toast.makeText(mContext, "Data has been saved", Toast.LENGTH_SHORT).show();
				//}else{
				//Toast.makeText(mContext, "Location not found, please try again.." , Toast.LENGTH_SHORT).show();
				//dialog.dismiss();
				//}
			} else {
				Toast.makeText(mContext, "Connection time out", Toast.LENGTH_SHORT).show();
				e = null;
				dialog.dismiss();
				//dialog2.dismiss();
			}
		}
	}


	private void handlelocation() {

		if (locationManager == null) {
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		}

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
			if (ActivityCompat.checkSelfPermission(this,
					android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
					ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
							!= PackageManager.PERMISSION_GRANTED) {

				return;
			}
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
		} else {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
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

		if (db != null) {
			if (db.isOpen()) {
				db.close();
			}
		}

		if (datasource != null) {
			datasource.close();
		}

		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}

		if (locationManager != null) {
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

				return;
			}
			locationManager.removeUpdates(locationListenerGps);
			locationManager.removeUpdates(locationListenerNetwork);
    		locationManager = null;
    	}

    	System.gc();
    }

    @Override
    public void onPause() {
    	super.onPause();

    	if(wl.isHeld())
			wl.release();

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
        	   		Toast.makeText(mContext,"Your session has been expired, please re login for security purpose", Toast.LENGTH_LONG).show();
        	   		prefsEditor.putBoolean("finish", true).commit();

        	   		finish();
        	   		Intent myintent = new Intent(mContext,HomeActivity.class);
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

    public class noIdentitas {
		private String noId = "";

		public void setNoId(String noId) {
			this.noId = noId;
		}

		public String getNoId() {
			return noId;
		}
	}



	///------BAGIAN UNTUK TAB ADD DATA ( TAKE PHOTO ) -----------///////

    private void loadPhoto() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission
                .READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
            requestReadPhoneStatePermission();
        } else {
			//Toast.makeText(mContext, "Camera Already Active...!!!", Toast.LENGTH_SHORT).show();
        }
    }

	private void requestReadPhoneStatePermission() {
		if (ActivityCompat.shouldShowRequestPermissionRationale(this,
				android.Manifest.permission.CAMERA)) {
			new AlertDialog.Builder(ProspectActivity.this)
					.setTitle("Permission Request")
					.setMessage("Check permission")
					.setCancelable(false)
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							//re-request
							ActivityCompat.requestPermissions(ProspectActivity.this,
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


				//new PictureProcessing3().execute();
				//new PictureProcessing4().execute();
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

	///--------IMAGE PROSPECT----------------\\\








///--------IMAGE COLLECTION----------------\\\

	private class SearchResults {
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


	private class PickOneAdapter extends BaseAdapter implements Filterable {

		private Context context;
		private LayoutInflater inflater = null;
		private PickOneAdapter.ItemFilter mFilter = new PickOneAdapter.ItemFilter();
		private ArrayList<SearchResults> items = null;
		private ArrayList<SearchResults> filteredData = null;

		private PickOneAdapter(Context c, ArrayList<SearchResults> results) {
			context = c;
			this.items = results;
			this.filteredData = results;
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return filteredData.size();
		}

		public Object getItem(int position) {
			return filteredData.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		private class ViewHolder{
			TextView nameHolder;
			TextView idHolder;
		}



		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PickOneAdapter.ViewHolder holder;
			View vi=convertView;

			if (convertView == null) {
				vi = inflater.inflate(R.layout.pick_one, null);

				holder = new PickOneAdapter.ViewHolder();
				holder.nameHolder = (TextView)vi.findViewById(R.id.tvName);
				holder.idHolder = (TextView)vi.findViewById(R.id.tvID);

				vi.setTag(holder);
			} else {
				holder = (PickOneAdapter.ViewHolder) vi.getTag();
			}

			holder.nameHolder.setText(filteredData.get(position).getName());
			holder.idHolder.setText(filteredData.get(position).getID());

			vi.setClickable(true);

			vi.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					LinearLayout ll = (LinearLayout) v;

					TextView tvName = (TextView) ll.getChildAt(0);
					TextView tvID = (TextView) ll.getChildAt(1);

					String name = tvName.getText().toString();
					String id = tvID.getText().toString();

					Log.d(TAG, "onClick:  name " + name);
					Log.d(TAG, "onClick:  id " + id);


					btn_product.setText(name);
					value_product = name;
					idproduk=datasource.getProductID(value_product);
					Log.d(TAG, "onClick: idproduk "+idproduk);
					Log.d(TAG, "onClick:  value_product " + value_product);

					dialog2.dismiss();
				}

			});

			return vi;
		}

		@Override
		public Filter getFilter() {
			return mFilter;
		}

		private class ItemFilter extends Filter {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				constraint = constraint.toString().toLowerCase();
				FilterResults result = new FilterResults();
				if(constraint != null && constraint.toString().length() > 0)
				{
					ArrayList<SearchResults> filt = new ArrayList<SearchResults>();
					ArrayList<SearchResults> lItems = new ArrayList<SearchResults>();
					synchronized (this)
					{
						lItems.addAll(items);
					}
					for(int i = 0, l = lItems.size(); i < l; i++)
					{
						SearchResults m = lItems.get(i);
						if(m.getName().toLowerCase().contains(constraint))
							filt.add(m);
					}
					result.count = filt.size();
					result.values = filt;

				}
				else
				{
					synchronized(this)
					{
						result.values = items;
						result.count = items.size();
					}
				}

				return result;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				filteredData = (ArrayList<SearchResults>)results.values;
				notifyDataSetChanged();
			}

		}

	}


}