package mandiri.finance.faith;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.PublicKey;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ListCollectionActivity extends DashBoardActivity {

	private String userID;
	private Context mContext;
	private ListView list;
	private LazyAdapterCollection adapter = null;

	private ArrayList<SearchResults> results = new ArrayList<SearchResults>();

	private EditText filterText = null;
	private TextView lastRefresh;
	private DataSource datasource;
	private SQLiteDatabase db;
	private Exception e = null;
	private String flagUpdate;
	private SharedPreferences generalPrefs;
	private boolean flag = false;
	private Dialog dialog;
	private SharedPreferences.Editor prefsEditor;
	private String isLoadCollectionForm;
	private String isLoadCollectionPlanForm;
	private DataLocal datalocal;

	private long PERIOD = 0;
	private LocationManager locationManager;
	boolean gps_enabled = false;
	boolean network_enabled = false;
	double latCurrent = 0;
	double lngCurrent = 0;
	private Spinner tipe;
	String sort="";
	String tipe2;
	FloatingActionButton cekdata;
	private String fieldname="";
	private String fieldvalue="";
	private String moduleid;
	private String key="";
	private String userid="";
	private String lasttransaction= "";
	private String branchName = "";
	private String branchAddress = "";
	private String branchPhone = "";
	private String contractNo;
	private String custNamePrint;
	private String dueDate;
	private String insNo;
	private String collInitial = "";
	private String receiptNo = "";
	private String licensePlate = "";
	private String status = "";
	private String strTotal= "";
	private String paidBy= "";
	private	int counter ;
	ServerSocket serverSocket;
	String TAG = "ListCollectionActivity";
	String prinan="";
	String infoedc="";
	private String tableName;
	private Button btnRefresh;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = ListCollectionActivity.this;
		datalocal = new DataLocal();

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
			isLoadCollectionForm = generalPrefs.getString("isLoadCollectionForm", null);
			isLoadCollectionPlanForm = generalPrefs.getString("isLoadCollectionPlanForm", null);

			Log.d(TAG, "onCreate: "+timeOut+sessionStart+isLoadCollectionForm+isLoadCollectionPlanForm);


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


		datasource = new DataSource(mContext);
		db = datasource.getWritableDatabase();
		tableName = "printcash";

		moduleid = generalPrefs.getString("moduleid", null);
		userID = generalPrefs.getString("userID", null);
		branchName = generalPrefs.getString("branchName", null);
		branchAddress = generalPrefs.getString("branchAddress", null);
		branchPhone = generalPrefs.getString("branchPhone", null);
		prinan = generalPrefs.getString("prinan",null);
		status = generalPrefs.getString("status",null);
		infoedc = generalPrefs.getString("infoedc",null);



		Bundle bd = getIntent().getExtras();
		flagUpdate = bd.getString("flagUpdate");

		setContentView(R.layout.collection_list);
		setHeader(getString(R.string.CollectionActivityTitle), true);

		tipe = (Spinner) findViewById(R.id.typesort);
		list = (ListView) findViewById(R.id.list);
		adapter = new LazyAdapterCollection(ListCollectionActivity.this, results);
		list.setAdapter(adapter);
		cekdata = (FloatingActionButton) findViewById(R.id.cekdata);

		btnRefresh = (Button) findViewById(R.id.btnRefresh);
		btnRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				RefreshList();
			}
		});

		lastRefresh = (TextView) findViewById(R.id.textLastRefresh);
		PERIOD = generalPrefs.getLong("locationInterval", 0);
		prefsEditor.putString("sourceid", "collector").commit();

		Log.d(TAG, "onCreate: "+ moduleid + flagUpdate + PERIOD);
		Log.d(TAG, "onCreate: cek nama cabang = " + branchName
				+"\n"+" alamat cabang = " + branchAddress
				+"\n"+" telepon cabang = " + branchPhone
				+"\n"+" userID = " + userID
				+"\n"+" status = " + status
				+"\n"+" prinan = " + prinan
				+"\n"+" infoedc = " + infoedc);

		new GetList().execute();
		initSpinner();
		inifloating();



	}



	//----bagian RE-PRINT-----------///

	private void inifloating(){
		cekdata.setOnClickListener(new View.OnClickListener() {
			ContentValues cv = new ContentValues();

			@Override
			public void onClick(View v) {
				Cursor cr2 = db.rawQuery("Select * from printcash",null);
				int cnt = cr2.getCount();
				Log.d(TAG, "onClick: cek count row1 " + cnt);
				if (cnt==0){
					final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
					builder.setIcon(android.R.drawable.ic_dialog_info);
					builder.setTitle("Cek Data Re-Print");
					builder.setMessage("TIDAK ADA UNTUK DI RE-PRINT").
							setCancelable(false).setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(final DialogInterface dialog, final int id) {
								dialog.dismiss();
								}
							});
					final AlertDialog alert = builder.create();
					alert.show();
					cr2.close();
				}else {

					Cursor cr1 = db.rawQuery("SELECT fieldname, fieldvalue, " +
							"moduleid, key, userid, receiptNo, insNo, dueDate, custNamePrint, "+
							"collInitial, licensePlate, lasttransaction, status, strTotal, paidBy, counter "+
							"FROM printcash  where userid  = "+ userID + " order by lasttransaction ", null);
					try {
						if (cr1.moveToFirst()) {
							fieldname = cr1.getString(0);
							fieldvalue = cr1.getString(1);
							moduleid = cr1.getString(2);
							key = cr1.getString(3);
							userid = cr1.getString(4);
							receiptNo = cr1.getString(5);
							insNo = cr1.getString(6);
							dueDate = cr1.getString(7);
							custNamePrint = cr1.getString(8);
							collInitial = cr1.getString(9);
							licensePlate = cr1.getString(10);
							lasttransaction = cr1.getString(11);
							status = cr1.getString(12);
							strTotal = cr1.getString(13);
							paidBy = cr1.getString(14);
							counter = cr1.getInt(15);
							Log.d(TAG, "onClick: else"
									+"\n"+"cek fieldname " +fieldname
									+"\n"+"cek fieldvalue " +fieldvalue
									+"\n"+"cek moduleid " +moduleid
									+"\n"+"cek key " +key
									+"\n"+"cek userid" +userid
									+"\n"+"cek receiptNo" +receiptNo
									+"\n"+"cek insNo " +insNo
									+"\n"+"cek dueDate " +dueDate
									+"\n"+"cek custNamePrint " +custNamePrint
									+"\n"+"cek licensePlate " +licensePlate
									+"\n"+"cek lasttransaction " +lasttransaction
									+"\n"+"cek status " + status
									+"\n"+"cek strTotal "+ strTotal
									+"\n"+"cek paidBy "+ paidBy
									+"\n"+"cek counter "+ counter);
						}


						if (counter == 2){
							Toast.makeText(mContext,"Anda Tidak Bisa melakukan Re-Print",Toast.LENGTH_LONG).show();
						}
						else {
										if (paidBy.equals("EDC")){
										final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
										builder.setIcon(android.R.drawable.ic_dialog_info);
										builder.setTitle("Re - Print EDC" );
										builder.setMessage("Dengan nama " + custNamePrint +
												"\n Jumlah Total " + "Rp. " + strTotal)
												.setCancelable(false).setPositiveButton("Yes",
												new DialogInterface.OnClickListener() {
													public void onClick(final DialogInterface dialog, final int id) {
														tambahdata();
														new PrintReceiptCash().execute();
														dialog.dismiss();
													}
												})
												.setNegativeButton("No", new DialogInterface.OnClickListener() {
													public void onClick(final DialogInterface dialog, final int id) {
														dialog.dismiss();

													}
												});
										final AlertDialog alert = builder.create();
										alert.show();

									}else {
										final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
										builder.setIcon(android.R.drawable.ic_dialog_info);
										builder.setTitle("Re - Print CASH" );
										builder.setMessage("Dengan nama " + custNamePrint +
												"\n Jumlah Total " + "Rp. " + strTotal)
												.setCancelable(false).setPositiveButton("Yes",
												new DialogInterface.OnClickListener() {
													public void onClick(final DialogInterface dialog, final int id) {
														tambahdata();
														new PrintReceiptCash().execute();
														dialog.dismiss();
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
						}

					} finally {
						cr1.close();
					}

				}
			}
		});
	}
	private class PrintReceiptCash extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			dialog.show();
		}

		@Override
		protected String doInBackground(Void... arg0) {
			boolean threadDone = false;
			int serverPort = 48200;
			int timeout = 60000; // 1 menit
			String isTimeOut = "";

			try {
				serverSocket = new ServerSocket(serverPort);
				serverSocket.setSoTimeout(timeout);

				while (!threadDone) {
					if (paidBy.equals("EDC")){
						new MultiThread(serverSocket.accept(), infoedc.toString()).start();
					}else{
						new MultiThread(serverSocket.accept(), prinan.toString()).start();
					}
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
				hapusprintcash();
				finish();
				Intent intent = new Intent(mContext, ListCollectionActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("flagUpdate", "U");
				startActivity(intent);
				Toast.makeText(mContext, "Data has been Print", Toast.LENGTH_LONG).show();

			} else {
				Toast.makeText(mContext, sResponse, Toast.LENGTH_LONG).show();
				Toast.makeText(mContext, "Anda harus melakukan Re-Print !!", Toast.LENGTH_LONG).show();
				dialog.dismiss();

			}

		}

	}
	private void hapusprintcash(){
		try{
			db = datasource.getWritableDatabase();
			//datasource.deleteData(db, "userid = '" + userID + "' and key = '"+ contractNo +"'" , "printcash");// gak bisa
			//datasource.deleteData(db, "userid = '" + userID + "'" , "printcash");//tambahan untuk delete row bisa dengan mantap
			//datasource.deleteData(db, "paidBy = '"+paidBy.equals("Cash") +"'" , "printcash");//percobaan
			datasource.deleteData(db, "paidBy = '"+ paidBy +"'" , "printcash");//percobaan
			//datasource.deleteData(db, "key = '" + contractNo + "'", "printcash");// gak bisa
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			db.close();
		}

	}
	private void tambahdata() {
		ContentValues cv = new ContentValues();
		cv.put("counter", counter + 1 );
		Log.d(TAG, "onClick: bertambah atau gak ya22 " + counter);
		datasource.updateData(db,cv,tableName," userid = '" + userID + "'");
		cv.clear();
	}


	//----bagian RE-PRINT-----------///


	public void onRadioButtonClicked(View view) {
		boolean checked = ((RadioButton) view).isChecked();
		switch(view.getId()) {
			case R.id.alltask:
				if (checked)
					Toast.makeText(mContext,"ini all task",Toast.LENGTH_SHORT).show();
//				flagUpdate = "U";
//				new GetList().execute();
					break;
			case R.id.planvisit:
				if (checked)
					Toast.makeText(mContext,"ini plan visit",Toast.LENGTH_SHORT).show();
					// Ninjas rule
					break;
		}
	}

	private void RefreshList(){
		try {
			flagUpdate = "U";
			new GetList().execute();
		} catch (Exception e) {
			Toast.makeText(mContext, "Connection time out", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			return;
		}
	}

	private class GetList extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {

			if (dialog.isShowing())
				dialog.dismiss();

//			if (flagUpdate.equals("U") || flagUpdate.equals("N")) ;
			if (flagUpdate.equals("U") ) ;
			dialog.show();

			handlelocation();
		}

		@Override
		protected String doInBackground(Void... unsued) {
			String result = "";

			try {

//				if (flagUpdate.equals("U") || flagUpdate.equals("N")) {

					if (flagUpdate.equals("U")) {

						AlarmManager mgr = (AlarmManager) getSystemService(ALARM_SERVICE);

					Intent i = new Intent(mContext, LocationPoller.class);

					Bundle bundle = new Bundle();
					LocationPollerParameter parameter = new LocationPollerParameter(bundle);
					parameter.setIntentToBroadcastOnCompletion(new Intent(mContext, LocationReceiver.class));
					// try GPS and fall back to NETWORK_PROVIDER
					parameter.setProviders(new String[]{LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER});
					parameter.setTimeout(120000);
					i.putExtras(bundle);

					boolean alarmUp = (PendingIntent.getBroadcast(mContext, 0,
							new Intent(i), PendingIntent.FLAG_NO_CREATE) != null);

					if (!alarmUp) {
						System.out.println("Alarm is not active");
						PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i, 0);
						mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), PERIOD, pi);
					} else {
						System.out.println("Alarm is already active");
					}

					if (!GetCollectionData())
						result = "ErrorConnection";

					String now;
					Calendar c = Calendar.getInstance();
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					now = df.format(c.getTime());
					prefsEditor.putString("lastRefreshCollection", String.valueOf(now)).commit();
				}

				//hitung jarak, lambat...
				//else{
				//	if(!UpdateDistance())
				//		result = "ErrorConnection";
				//}

				if (results != null) {
					if (results.size() > 0) {
						results.clear();
					}
				}

				if (Boolean.valueOf(isLoadCollectionForm)) {
					if (datalocal.GetFormData(mContext, moduleid, getString(R.string.url)))
						prefsEditor.putString("isLoadCollectionForm", "false").commit();
					else
						result = "ErrorConnection";
				}
				Log.d(TAG, "doInBackground: "+isLoadCollectionForm);

				if (Boolean.valueOf(isLoadCollectionPlanForm)) {
					if (datalocal.GetFormData(mContext, "99", getString(R.string.url)))
						prefsEditor.putString("isLoadCollectionPlanForm", "false").commit();
					else
						result = "ErrorConnection";

				}

				Cursor c = db.rawQuery("SELECT headertext, contractno, " +
						"detailtext, duedate, taskdate, insno, insamount, lat, lng, OD, installamount FROM " +
						"collectionlist where status = 'n' ORDER BY duedate desc", null);

				Log.d(TAG, "doInBackground: "+ c);

				try {
					while (c.moveToNext()) {
						SearchResults sr1 = new SearchResults();
						sr1.setHeader(c.getString(0));
						sr1.setcontractNo(c.getString(1));
						sr1.setDetail(c.getString(2));
						sr1.setDueDate(c.getString(3));
						sr1.setTaskDate(c.getString(4));
						sr1.setInstallmentNo(c.getString(5));
						sr1.setInstallmentAmount(c.getString(6));
						sr1.setLatitude(c.getString(7));
						sr1.setLongitude(c.getString(8));
						sr1.setOverdue2(c.getString(9));
						sr1.setAmount(c.getString(10));
						results.add(sr1);
					}
				} finally {
					c.close();
					c = null;
				}

			} catch (Exception e) {
				ListCollectionActivity.this.e = e;
			} finally {
				//db.isOpen();
				//db.close();
			}

			return result;
		}

		protected void onPostExecute(String sResponse) {
			Log.d(TAG, "onPostExecute: "+sResponse);

			if (e == null && sResponse.length() == 0) {

				filterText = (EditText) findViewById(R.id.search_box);
				filterText.setText("");
				filterText.addTextChangedListener(filterTextWatcher);

				if (generalPrefs.getString("lastRefreshCollection", "").length() > 0) {
					lastRefresh.setText("Last synchronized at : " + generalPrefs.getString("lastRefreshCollection", ""));
				} else {
					lastRefresh.setText("Never synchronized before");
				}
				adapter.notifyDataSetChanged();
				GetList();

				if (dialog.isShowing())
					dialog.dismiss();
			} else {

				if (dialog.isShowing())
					dialog.dismiss();
				adapter.notifyDataSetChanged();
				Toast.makeText(mContext, "Connection time out", Toast.LENGTH_SHORT).show();
				lastRefresh.setText("Never synchronized before");
				e = null;
			}
		}
	}

	private boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
			System.out.println(d);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

		private void GetList() {
//		adapter = new LazyAdapterCollection(ListCollectionActivity.this, results);
//		list.setAdapter(adapter);
		//adapter.notifyDataSetChanged();
		list.setTextFilterEnabled(true);
		list.setLongClickable(true);
		list.setOnItemLongClickListener(listLongClickListener);
	}

	public void refreshClick(View v) {
		try {
			flagUpdate = "U";
			new GetList().execute();
		} catch (Exception e) {
			Toast.makeText(mContext, "Connection time out", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			return;
		}

	}

	private OnItemLongClickListener listLongClickListener = new OnItemLongClickListener() {
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			handlelocation();

			RelativeLayout rl = (RelativeLayout) view;
			TextView tvCustName = (TextView) rl.getChildAt(0);
			final TextView tvLocation = (TextView) rl.getChildAt(2);

			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			AlertDialog alertDialog;

			List<String> listItems = new ArrayList<String>();

			listItems.add("View in map");
			final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);

			builder.setTitle(tvCustName.getText().toString());

			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
							Uri.parse("http://maps.google.com/maps?daddr=" + tvLocation.getText().toString() + "&dirflg=d"));
					intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

					startActivity(intent);
					dialog.dismiss();
				}
			});

			alertDialog = builder.create();
			alertDialog.show();

			return false;

		}
	};

	private void initSpinner(){
		tipe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (tipe.getSelectedItemPosition())
				{
					case 0 :
						sort="asc";
						tipe2 = "ALL";
						new GetList().execute();
						break;
					case 1 :
						//sort="asc";
						tipe2 = "DATE";
						new GetList2().execute();
						break;
					case 2 :
						//sort="desc";
						tipe2 = "AMOUNT";
						new GetList2().execute();
						break;
					case 3 :
						//sort="desc";
						tipe2 = "OVERDUE";
						new GetList2().execute();
						break;

				}
				Log.d(TAG, "onItemClick: spinner sort : "+sort+" dan "+" tipenya : "+tipe2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}
	private class GetList2 extends AsyncTask<Void, Void, String> {
		String flagUpdate2="N";

		@Override
		protected void onPreExecute() {
//
//			if (dialog.isShowing())
//				dialog.dismiss();

			//if (flagUpdate.equals("N") || flagUpdate.equals("U")) ;
//			if (flagUpdate2.equals("N")) ;
//				handlelocation();
				//dialog.show();


		}

		@Override
		protected String doInBackground(Void... unsued) {
			String result = "";

			try {

//				if (flagUpdate.equals("U") || flagUpdate.equals("N")) {
					if (flagUpdate2.equals("N")) {


						AlarmManager mgr = (AlarmManager) getSystemService(ALARM_SERVICE);

						Intent i = new Intent(mContext, LocationPoller.class);

						Bundle bundle = new Bundle();
						LocationPollerParameter parameter = new LocationPollerParameter(bundle);
						parameter.setIntentToBroadcastOnCompletion(new Intent(mContext, LocationReceiver.class));
						// try GPS and fall back to NETWORK_PROVIDER
						parameter.setProviders(new String[]{LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER});
						parameter.setTimeout(120000);
						i.putExtras(bundle);

						boolean alarmUp = (PendingIntent.getBroadcast(mContext, 0,
								new Intent(i), PendingIntent.FLAG_NO_CREATE) != null);

						if (!alarmUp) {
							System.out.println("Alarm is not active");
							PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i, 0);
							mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), PERIOD, pi);
						} else {
							System.out.println("Alarm is already active");
						}

//						if (!GetCollectionData())
//							result = "ErrorConnection";

						String now;
						Calendar c = Calendar.getInstance();
						SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						now = df.format(c.getTime());
						prefsEditor.putString("lastRefreshCollection", String.valueOf(now)).commit();

				}



				if (results != null) {
					if (results.size() > 0) {
						results.clear();
					}
				}

				try{

							switch (tipe2){
								case "DATE":
									Cursor cr1 = db.rawQuery("SELECT headertext, contractno, " +
											"detailtext, duedate, taskdate, insno, insamount, lat, lng, OD, installamount FROM " +
											"collectionlist where status = 'n' ORDER BY duedate asc, headertext asc ", null);

									try {

										while (cr1.moveToNext()) {
											SearchResults sr1 = new SearchResults();
											sr1.setHeader(cr1.getString(0));
											sr1.setcontractNo(cr1.getString(1));
											sr1.setDetail(cr1.getString(2));
											sr1.setDueDate(cr1.getString(3));
											sr1.setTaskDate(cr1.getString(4));
											sr1.setInstallmentNo(cr1.getString(5));
											sr1.setInstallmentAmount(cr1.getString(6));
											sr1.setLatitude(cr1.getString(7));
											sr1.setLongitude(cr1.getString(8));
											sr1.setOverdue2(cr1.getString(9));
											sr1.setAmount(cr1.getString(10));

											results.add(sr1);
										}


									} finally {
										cr1.close();

									}
									break;
								case "AMOUNT":
									Cursor cr2 = db.rawQuery("SELECT headertext, contractno, " +
											"detailtext, duedate, taskdate, insno, insamount, "+
											"lat, lng, OD, installamount FROM " +
											" collectionlist where status = 'n' ORDER BY  cast(installamount as INTEGER) desc  ", null);

									try {

										while (cr2.moveToNext()) {
											SearchResults sr1 = new SearchResults();
											sr1.setHeader(cr2.getString(0));
											sr1.setcontractNo(cr2.getString(1));
											sr1.setDetail(cr2.getString(2));
											sr1.setDueDate(cr2.getString(3));
											sr1.setTaskDate(cr2.getString(4));
											sr1.setInstallmentNo(cr2.getString(5));
											sr1.setInstallmentAmount(cr2.getString(6));
											sr1.setLatitude(cr2.getString(7));
											sr1.setLongitude(cr2.getString(8));
											sr1.setOverdue2(cr2.getString(9));
											sr1.setAmount(cr2.getString(10));

											results.add(sr1);
										}

									} finally {
										cr2.close();

									}
									break;
								case "OVERDUE":
									Cursor cr3 = db.rawQuery("SELECT headertext, contractno, " +
											"detailtext, duedate, taskdate, insno, insamount, lat, lng, OD, installamount FROM " +
											"collectionlist where status = 'n' ORDER BY OD desc, headertext asc ", null);

									try {

										while (cr3.moveToNext()) {
											SearchResults sr1 = new SearchResults();
											sr1.setHeader(cr3.getString(0));
											sr1.setcontractNo(cr3.getString(1));
											sr1.setDetail(cr3.getString(2));
											sr1.setDueDate(cr3.getString(3));
											sr1.setTaskDate(cr3.getString(4));
											sr1.setInstallmentNo(cr3.getString(5));
											sr1.setInstallmentAmount(cr3.getString(6));
											sr1.setLatitude(cr3.getString(7));
											sr1.setLongitude(cr3.getString(8));
											sr1.setOverdue2(cr3.getString(9));
											sr1.setAmount(cr3.getString(10));
											results.add(sr1);
										}


									} finally {
										cr3.close();
									}
									break;
							}

				}catch (Exception e){
					e.printStackTrace();
				}

			} catch (Exception e) {
				ListCollectionActivity.this.e = e;
			}

			return result;
		}

		protected void onPostExecute(String sResponse) {
			Log.d(TAG, "onPostExecute: "+sResponse);

			if (e == null && sResponse.length() == 0) {

				filterText = (EditText) findViewById(R.id.search_box);
				filterText.setText("");
				filterText.addTextChangedListener(filterTextWatcher);

				if (generalPrefs.getString("lastRefreshCollection", "").length() > 0) {
					lastRefresh.setText("Last synchronized at : " + generalPrefs.getString("lastRefreshCollection", ""));
				} else {
					lastRefresh.setText("Never synchronized before");
				}


				adapter.notifyDataSetChanged();

				GetList();

				if (dialog.isShowing())
					dialog.dismiss();
			} else {

				if (dialog.isShowing())
					dialog.dismiss();

				adapter.notifyDataSetChanged();
				Toast.makeText(mContext, "Connection time out", Toast.LENGTH_SHORT).show();
				lastRefresh.setText("Never synchronized before");
				e = null;
			}
		}
	}


	private boolean GetCollectionData() {
		try {

			if (!db.isOpen()) {
				db = datasource.getWritableDatabase();
			}

			InputStream in = null;
			ContentValues cv = new ContentValues();

			ArrayList<String> contractNoDelete = new ArrayList<String>();
			ArrayList<String> contractNoExists = new ArrayList<String>();

			String[] contractNoExistsArray = null;
			String[] contractNoDeleteArray = null;

			datasource.deleteData(db, "", "collectionlist");
			datasource.deleteData(db, "moduleid = '" + moduleid + "'", "infodetail");
			Log.d(TAG, "GetCollectionData: moduleid" + moduleid);
			//db.execSQL("delete from sqlite_sequence where name = 'collectionlist'");
			//db.execSQL("delete from sqlite_sequence where name = 'infodetail'");

			Cursor c = db.rawQuery("SELECT key FROM result where moduleid = '" + moduleid + "'", null);

			try {
				while (c.moveToNext()) {
					contractNoExists.add(c.getString(0));
				}
			} finally {
				c.close();
			}

			StringBuilder sbl = new StringBuilder();
			sbl.append("");

			if (contractNoExists != null) {
				Log.d(TAG, "GetCollectionData: contractNoExists" + contractNoExists);
				contractNoExistsArray = contractNoExists.toArray(new String[contractNoExists.size()]);
				Log.d(TAG, "GetCollectionData: contractNoExistsArray" + contractNoExistsArray);
				Log.d(TAG, "GetCollectionData: contractNoExists2" +contractNoExists);

				for (int i = 0; i < contractNoExistsArray.length; i++) {
					if (i == 0) {
						sbl.append("'" + contractNoExistsArray[i] + "'");
					} else {
						sbl.append(",'" + contractNoExistsArray[i] + "'");
					}
				}
			}

			Calendar cl = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String myTime = df.format(cl.getTime());

			Log.d(TAG, "GetCollectionData: sbl "+sbl.toString());
			String contractNoExistsEncrypt = datalocal.encrypt(sbl.toString(), myTime);
			String theKey = datalocal.encrypt(myTime, getString(R.string.Key44));
			String userIDEncrypt = datalocal.encrypt(userID, myTime);
			Log.d(TAG, "GetCollectionData: contractNoExistsEncrypt" +contractNoExistsEncrypt);
			URL localUrl = new URL(getString(R.string.url) + "/GetCollectionList?a=" +
					URLEncoder.encode(userIDEncrypt, "UTF-8") +
					"&b=" + URLEncoder.encode(contractNoExistsEncrypt, "UTF-8") +
					"&c=" + URLEncoder.encode(theKey, "UTF-8"));

			URLConnection conn = localUrl.openConnection();
			Log.d(TAG, "GetCollectionData: "+conn);
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

			NodeList fields = root.getElementsByTagName("data");
			NodeList deleteFields = root.getElementsByTagName("delete");
			NodeList detailFields = root.getElementsByTagName("datadetail");

			for (int i = 0; i < deleteFields.getLength(); i++) {
				Node fieldNode = deleteFields.item(i);
				NamedNodeMap attr = fieldNode.getAttributes();

				contractNoDelete.add(attr.getNamedItem("ContractNo").getNodeValue().trim());
			}

			contractNoDeleteArray = contractNoDelete.toArray(new String[contractNoDelete.size()]);
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < contractNoDeleteArray.length; i++) {
				if (i == 0) {
					sb.append("'" + contractNoDeleteArray[i] + "'");
				} else {
					sb.append(",'" + contractNoDeleteArray[i] + "'");
				}
			}

			Cursor cr = db.rawQuery("SELECT id FROM collectionlist where contractno in(" + sb.toString() + ")", null);

			try {
				if (cr.moveToFirst()) {
					datasource.deleteData(db, "contractno in(" + sb.toString() + ")", "collectionlist");
					datasource.deleteData(db, "moduleid = '" + moduleid + "' and idx in(" + sb.toString() + ")", "infodetail");
				}
			} finally {
				cr.close();
			}

			for (int i = 0; i < detailFields.getLength(); i++) {
				Node fieldNode = detailFields.item(i);
				NamedNodeMap attr = fieldNode.getAttributes();

				cv.put("idx", attr.getNamedItem("ContractNo").getNodeValue());
				cv.put("moduleid", moduleid);
				cv.put("infolabel", attr.getNamedItem("InfoLabel").getNodeValue());
				cv.put("infotext", attr.getNamedItem("InfoText").getNodeValue());

				datasource.generateData(db, cv, "infodetail");
				cv.clear();
			}

			for (int i = 0; i < fields.getLength(); i++) {

				double distanceCalc = 0;

				Node fieldNode = fields.item(i);
				NamedNodeMap attr = fieldNode.getAttributes();

				//GeoPoint gpCurrentLocation = new GeoPoint((int)(latCurrent * 1E6),(int)(lngCurrent * 1E6));

				//hitung jarak, lambat...
				//if(gpCurrentLocation != null){
				//	distanceCalc = directionsDistance(gpCurrentLocation, attr.getNamedItem("DetailText").getNodeValue().replace("\n", " ").replace(" ", "%20"));
				//}

				cv.put("headertext", attr.getNamedItem("HeaderText").getNodeValue());//1
				cv.put("detailtext", attr.getNamedItem("DetailText").getNodeValue());//2
				cv.put("duedate", attr.getNamedItem("DueDate").getNodeValue());//3
				cv.put("contractno", attr.getNamedItem("ContractNo").getNodeValue());//4
				cv.put("insno", attr.getNamedItem("InsNo").getNodeValue());//5
				cv.put("collinitial", attr.getNamedItem("CollInitial").getNodeValue());//6
				cv.put("status", "n");//7
				cv.put("distance", distanceCalc);//8
				cv.put("taskdate", attr.getNamedItem("TaskDate").getNodeValue());//9
				cv.put("receiptno", attr.getNamedItem("ReceiptNo").getNodeValue());//10
				cv.put("insamount", attr.getNamedItem("InsAmount").getNodeValue());//1
				cv.put("licenseplate", attr.getNamedItem("LicensePlate").getNodeValue());//12
				cv.put("lat", attr.getNamedItem("Lat").getNodeValue());//13
				cv.put("lng", attr.getNamedItem("Lng").getNodeValue());//14
				cv.put("OD", attr.getNamedItem("OD").getNodeValue());//15
				cv.put("installamount",attr.getNamedItem("installamount").getNodeValue());
				datasource.generateData(db, cv, "collectionlist");
				cv.clear();
			}

			docb = null;
			doc = null;

			return true;

		} catch (Exception e) {
			//Log.e("List_Collection_Activity", "GetCollectionData : " + e.getMessage());
			Log.e("List_Collection", "GetCollectionData: "+e.getMessage() );
			e.printStackTrace();
			return false;
		}
	}




	//-------- PLAN VISIT ----------------------\\
	private boolean GetCollectionData_PlanVisit() {
		try {

			if (!db.isOpen()) {
				db = datasource.getWritableDatabase();
			}

			InputStream in = null;
			ContentValues cv = new ContentValues();

			ArrayList<String> contractNoDelete = new ArrayList<String>();
			ArrayList<String> contractNoExists = new ArrayList<String>();

			String[] contractNoExistsArray = null;
			String[] contractNoDeleteArray = null;

			datasource.deleteData(db, "", "collectionlist");
			datasource.deleteData(db, "moduleid = '" + moduleid + "'", "infodetail");
			Log.d(TAG, "GetCollectionData: moduleid" + moduleid);
			//db.execSQL("delete from sqlite_sequence where name = 'collectionlist'");
			//db.execSQL("delete from sqlite_sequence where name = 'infodetail'");

			Cursor c = db.rawQuery("SELECT key FROM result where moduleid = '" + moduleid + "'", null);

			try {
				while (c.moveToNext()) {
					contractNoExists.add(c.getString(0));
				}
			} finally {
				c.close();
			}

			StringBuilder sbl = new StringBuilder();
			sbl.append("");

			if (contractNoExists != null) {
				Log.d(TAG, "GetCollectionData: contractNoExists" + contractNoExists);
				contractNoExistsArray = contractNoExists.toArray(new String[contractNoExists.size()]);
				Log.d(TAG, "GetCollectionData: contractNoExistsArray" + contractNoExistsArray);
				Log.d(TAG, "GetCollectionData: contractNoExists2" +contractNoExists);

				for (int i = 0; i < contractNoExistsArray.length; i++) {
					if (i == 0) {
						sbl.append("'" + contractNoExistsArray[i] + "'");
					} else {
						sbl.append(",'" + contractNoExistsArray[i] + "'");
					}
				}
			}

			Calendar cl = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String myTime = df.format(cl.getTime());

			String contractNoExistsEncrypt = datalocal.encrypt(sbl.toString(), myTime);
			String theKey = datalocal.encrypt(myTime, getString(R.string.Key44));
			String userIDEncrypt = datalocal.encrypt(userID, myTime);
			Log.d(TAG, "GetCollectionData: contractNoExistsEncrypt" +contractNoExistsEncrypt);
			URL localUrl = new URL(getString(R.string.url) + "/GetCollectionList?a=" +
					URLEncoder.encode(userIDEncrypt, "UTF-8") +
					"&b=" + URLEncoder.encode(contractNoExistsEncrypt, "UTF-8") +
					"&c=" + URLEncoder.encode(theKey, "UTF-8"));

			URLConnection conn = localUrl.openConnection();
			Log.d(TAG, "GetCollectionData: "+conn);
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

			NodeList fields = root.getElementsByTagName("data");
			NodeList deleteFields = root.getElementsByTagName("delete");
			NodeList detailFields = root.getElementsByTagName("datadetail");

			for (int i = 0; i < deleteFields.getLength(); i++) {
				Node fieldNode = deleteFields.item(i);
				NamedNodeMap attr = fieldNode.getAttributes();

				contractNoDelete.add(attr.getNamedItem("ContractNo").getNodeValue().trim());
			}

			contractNoDeleteArray = contractNoDelete.toArray(new String[contractNoDelete.size()]);
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < contractNoDeleteArray.length; i++) {
				if (i == 0) {
					sb.append("'" + contractNoDeleteArray[i] + "'");
				} else {
					sb.append(",'" + contractNoDeleteArray[i] + "'");
				}
			}

			Cursor cr = db.rawQuery("SELECT id FROM collectionlist where contractno in(" + sb.toString() + ")", null);

			try {
				if (cr.moveToFirst()) {
					datasource.deleteData(db, "contractno in(" + sb.toString() + ")", "collectionlist");
					datasource.deleteData(db, "moduleid = '" + moduleid + "' and idx in(" + sb.toString() + ")", "infodetail");
				}
			} finally {
				cr.close();
			}

			for (int i = 0; i < detailFields.getLength(); i++) {
				Node fieldNode = detailFields.item(i);
				NamedNodeMap attr = fieldNode.getAttributes();

				cv.put("idx", attr.getNamedItem("ContractNo").getNodeValue());
				cv.put("moduleid", moduleid);
				cv.put("infolabel", attr.getNamedItem("InfoLabel").getNodeValue());
				cv.put("infotext", attr.getNamedItem("InfoText").getNodeValue());

				datasource.generateData(db, cv, "infodetail");
				cv.clear();
			}

			for (int i = 0; i < fields.getLength(); i++) {

				double distanceCalc = 0;

				Node fieldNode = fields.item(i);
				NamedNodeMap attr = fieldNode.getAttributes();

				//GeoPoint gpCurrentLocation = new GeoPoint((int)(latCurrent * 1E6),(int)(lngCurrent * 1E6));

				//hitung jarak, lambat...
				//if(gpCurrentLocation != null){
				//	distanceCalc = directionsDistance(gpCurrentLocation, attr.getNamedItem("DetailText").getNodeValue().replace("\n", " ").replace(" ", "%20"));
				//}

				cv.put("headertext", attr.getNamedItem("HeaderText").getNodeValue());//1
				cv.put("detailtext", attr.getNamedItem("DetailText").getNodeValue());//2
				cv.put("duedate", attr.getNamedItem("DueDate").getNodeValue());//3
				cv.put("contractno", attr.getNamedItem("ContractNo").getNodeValue());//4
				cv.put("insno", attr.getNamedItem("InsNo").getNodeValue());//5
				cv.put("collinitial", attr.getNamedItem("CollInitial").getNodeValue());//6
				cv.put("status", "n");//7
				cv.put("distance", distanceCalc);//8
				cv.put("taskdate", attr.getNamedItem("TaskDate").getNodeValue());//9
				cv.put("receiptno", attr.getNamedItem("ReceiptNo").getNodeValue());//10
				cv.put("insamount", attr.getNamedItem("InsAmount").getNodeValue());//1
				cv.put("licenseplate", attr.getNamedItem("LicensePlate").getNodeValue());//12
				cv.put("lat", attr.getNamedItem("Lat").getNodeValue());//13
				cv.put("lng", attr.getNamedItem("Lng").getNodeValue());//14
				cv.put("OD", attr.getNamedItem("OD").getNodeValue());//15
				cv.put("installamount",attr.getNamedItem("installamount").getNodeValue());
				datasource.generateData(db, cv, "collectionlist");
				cv.clear();
			}

			docb = null;
			doc = null;

			return true;

		} catch (Exception e) {
			//Log.e("List_Collection_Activity", "GetCollectionData : " + e.getMessage());
			Log.e("List_Collection", "GetCollectionData: "+e.getMessage() );
			e.printStackTrace();
			return false;
		}
	}

	private class GetListPlanVisit extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {

			if (dialog.isShowing())
				dialog.dismiss();

//			if (flagUpdate.equals("U") || flagUpdate.equals("N")) ;
			if (flagUpdate.equals("U") ) ;
			dialog.show();

			handlelocation();
		}

		@Override
		protected String doInBackground(Void... unsued) {
			String result = "";

			try {

//				if (flagUpdate.equals("U") || flagUpdate.equals("N")) {

				if (flagUpdate.equals("U")) {

					AlarmManager mgr = (AlarmManager) getSystemService(ALARM_SERVICE);

					Intent i = new Intent(mContext, LocationPoller.class);

					Bundle bundle = new Bundle();
					LocationPollerParameter parameter = new LocationPollerParameter(bundle);
					parameter.setIntentToBroadcastOnCompletion(new Intent(mContext, LocationReceiver.class));
					// try GPS and fall back to NETWORK_PROVIDER
					parameter.setProviders(new String[]{LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER});
					parameter.setTimeout(120000);
					i.putExtras(bundle);

					boolean alarmUp = (PendingIntent.getBroadcast(mContext, 0,
							new Intent(i), PendingIntent.FLAG_NO_CREATE) != null);

					if (!alarmUp) {
						System.out.println("Alarm is not active");
						PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i, 0);
						mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), PERIOD, pi);
					} else {
						System.out.println("Alarm is already active");
					}

					if (!GetCollectionData())
						result = "ErrorConnection";

					String now;
					Calendar c = Calendar.getInstance();
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					now = df.format(c.getTime());
					prefsEditor.putString("lastRefreshCollection", String.valueOf(now)).commit();
				}

				//hitung jarak, lambat...
				//else{
				//	if(!UpdateDistance())
				//		result = "ErrorConnection";
				//}

				if (results != null) {
					if (results.size() > 0) {
						results.clear();
					}
				}

				if (Boolean.valueOf(isLoadCollectionForm)) {
					if (datalocal.GetFormData(mContext, moduleid, getString(R.string.url)))
						prefsEditor.putString("isLoadCollectionForm", "false").commit();
					else
						result = "ErrorConnection";
				}
				Log.d(TAG, "doInBackground: "+isLoadCollectionForm);

				if (Boolean.valueOf(isLoadCollectionPlanForm)) {
					if (datalocal.GetFormData(mContext, "99", getString(R.string.url)))
						prefsEditor.putString("isLoadCollectionPlanForm", "false").commit();
					else
						result = "ErrorConnection";

				}

//				Cursor c = db.rawQuery("SELECT headertext, contractno, " +
//						"detailtext, duedate, taskdate, insno, insamount FROM " +
//						"collectionlist where status = 'n' ORDER BY duedate desc, headertext asc", null);
				Cursor c = db.rawQuery("SELECT headertext, contractno, " +
						"detailtext, duedate, taskdate, insno, insamount, lat, lng, OD, installamount FROM " +
						"collectionlist where status = 'n' ORDER BY duedate desc", null);

				Log.d(TAG, "doInBackground: "+ c);

				try {
					while (c.moveToNext()) {
						SearchResults sr1 = new SearchResults();
						sr1.setHeader(c.getString(0));
						sr1.setcontractNo(c.getString(1));
						sr1.setDetail(c.getString(2));
						sr1.setDueDate(c.getString(3));
						sr1.setTaskDate(c.getString(4));
						sr1.setInstallmentNo(c.getString(5));
						sr1.setInstallmentAmount(c.getString(6));
						sr1.setLatitude(c.getString(7));
						sr1.setLongitude(c.getString(8));
						sr1.setOverdue2(c.getString(9));
						sr1.setAmount(c.getString(10));
						results.add(sr1);
					}
				} finally {
					c.close();
					c = null;
				}

			} catch (Exception e) {
				ListCollectionActivity.this.e = e;
			} finally {
				//db.isOpen();
				//db.close();
			}

			return result;
		}

		protected void onPostExecute(String sResponse) {
			Log.d(TAG, "onPostExecute: "+sResponse);

			if (e == null && sResponse.length() == 0) {

				filterText = (EditText) findViewById(R.id.search_box);
				filterText.setText("");
				filterText.addTextChangedListener(filterTextWatcher);

				if (generalPrefs.getString("lastRefreshCollection", "").length() > 0) {
					lastRefresh.setText("Last synchronized at : " + generalPrefs.getString("lastRefreshCollection", ""));
				} else {
					lastRefresh.setText("Never synchronized before");
				}
				adapter.notifyDataSetChanged();
				GetList();

				if (dialog.isShowing())
					dialog.dismiss();
			} else {

				if (dialog.isShowing())
					dialog.dismiss();
				adapter.notifyDataSetChanged();
				Toast.makeText(mContext, "Connection time out", Toast.LENGTH_SHORT).show();
				lastRefresh.setText("Never synchronized before");
				e = null;
			}
		}
	}

	//-------- PLAN VISIT ----------------------\\

	private void handlelocation() {

		if (locationManager == null) {
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		}

		try {
			gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}

		try {
			network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		if (!gps_enabled && !network_enabled)
			return;

		if (gps_enabled) {
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

				return;
			}
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
			Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

			if (lastLocation != null && (latCurrent == 0.0 || lngCurrent == 0.0)) {
				latCurrent = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
				lngCurrent = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
			}

		} else {
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

				return;
			}
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
			Location lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			if (lastLocation != null && (latCurrent == 0.0 || lngCurrent == 0.0)) {
				latCurrent = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
				lngCurrent = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
			}

		}

	}

	LocationListener locationListenerGps = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
			if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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
		latCurrent = newLocation.getLatitude();
		lngCurrent = newLocation.getLongitude();
	}
    
    public class SearchResults {
    	 private String header = "";
    	 private String detail = "";
    	 private String duedate = "";
    	 private String contractNo = "";
    	 private String taskdate = "";
    	 private String installmentno = "";
    	 private String installmentamount = "";
		private String amount="" ;
		private String latitude = "";
		private String longitude = "";
		private String overdue2 = "";

    	 public void setHeader(String header) {
    		 this.header = header;
    	 }

    	 public String getHeader() {
    		 return header;
    	 }

    	 public void setDetail(String detail) {
    		 this.detail = detail;
    	 }

    	 public String getDetail() {
    		 return detail;
    	 }
    	 
    	 public void setDueDate(String duedate) {
    		 this.duedate = duedate;
    	 }
    	 
    	 public String getDueDate() {
    		 return duedate;
    	 }

    	 public void setcontractNo(String contractNo) {
    		 this.contractNo = contractNo;
       	 }

       	 public String getcontractNo() {
       		 return contractNo;
       	 }
       	 
       	 public void setTaskDate(String taskdate) {
       		 this.taskdate = taskdate;
	   	 }
	   	 
	   	 public String getTaskDate() {
	   		 return taskdate;
	   	 }
	   	 
	   	public void setInstallmentNo(String installmentno) {
	   		this.installmentno = installmentno;
	   	}
	   	 
	   	 public String getInstallmentNo() {
	   		 return installmentno;
	   	 }

	   	public void setInstallmentAmount(String installmentamount) {
	   		this.installmentamount = installmentamount;
	   	}

	   	 public String getInstallmentAmount() {
	   		 return installmentamount;
	   	 }

		public void setLatitude(String latitude) {
			this.latitude = latitude;
		}

		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}

		public String getLatitude() {
			return latitude;
		}

		public String getLongitude() {
			return longitude;
		}

		public void setOverdue2(String overdue2) {
			this.overdue2 = overdue2;
		}

		public String getOverdue2() {
			return overdue2;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

		public String getAmount() {
			return amount;
		}
	}
    
    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {        	
        	adapter.getFilter().filter(s.toString());
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                int count) {
        	
        }

    };

    
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
	            	   Intent myintent = new Intent(mContext,HomeActivity.class);
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
    
    @Override
    public void onDestroy() {
    	super.onDestroy();    	
    	
    	if (dialog != null){
    		if(dialog.isShowing()){
        		dialog.dismiss();
        	}
    	}
    	
    	if(datasource != null){
        	datasource.close();
        }
    	    	
    	if (db != null){
    		if (db.isOpen()){
        		db.close();	
        	}
    	}
    	
    	if (adapter != null){
			adapter.notifyDataSetInvalidated();
			adapter = null;
	   	}
		
    	if (filterText != null){
			filterText.removeTextChangedListener(filterTextWatcher);
			filterText = null;
 	   	}
    	
    	if(list!=null){
    		list.setAdapter(null);
   			list = null;
   		}
    	
    	System.gc();
    	
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
        	   		Toast.makeText(mContext,"Your session has been expired, please re login for security purpose", Toast.LENGTH_LONG).show();
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

 /*
    private boolean UpdateDistance(){
		try{

			Cursor c = db.rawQuery("SELECT contractno, detailtext FROM collectionlist where status = 'n' ORDER BY distance, duedate, headertext" ,null);

			try{
				while(c.moveToNext()) {

					int distanceCalc = 0;
    				GeoPoint gpCurrentLocation = null;

					gpCurrentLocation = new GeoPoint((int)(latCurrent * 1E6),(int)(lngCurrent * 1E6));

					if(gpCurrentLocation != null){
						distanceCalc = directionsDistance(gpCurrentLocation, c.getString(1).replace("\n", " ").replace(" ", "%20"));
					}

					if(distanceCalc>0){
						ContentValues cv = new ContentValues();
	    				cv.put("distance", distanceCalc);
	    				datasource.updateData(db, cv, "collectionlist", " contractno = '" + c.getString(0) + "'");
	    				cv.clear();
					}

    			}
			}
			finally{
				c.close();
				c = null;
			}

		return true;

		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private int directionsDistance(GeoPoint start, String dest) {

	    String jsonURL = "http://maps.googleapis.com/maps/api/directions/json?";
	    final StringBuffer sBuf = new StringBuffer(jsonURL);
	    sBuf.append("origin=");
	    sBuf.append(start.getLatitudeE6()/1E6);
	    sBuf.append(',');
	    sBuf.append(start.getLongitudeE6()/1E6);
	    sBuf.append("&destination=");
	    sBuf.append(dest);
	    sBuf.append("&avoid=tolls&sensor=true&mode=driving");

	    HttpGet httpGet = new HttpGet(sBuf.toString());
	    HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;

            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }

        } catch (ClientProtocolException e) {

        } catch (IOException e) {

        }

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject = new JSONObject(stringBuilder.toString());

            JSONArray array = jsonObject.getJSONArray("routes");
            JSONObject routes = array.getJSONObject(0);
            JSONArray legs = routes.getJSONArray("legs");
            JSONObject steps = legs.getJSONObject(0);
        	JSONObject distance = steps.getJSONObject("distance");

        	DecimalFormat dForm = new DecimalFormat("#");
        	return Integer.valueOf(dForm.format(distance.getDouble("value")/1000));

        } catch (JSONException e) {
        	e.printStackTrace();
        	return 0;
        }

	}

    */

}
