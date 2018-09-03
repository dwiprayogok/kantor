package mandiri.finance.faith;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mandiri.finance.faith.Model.Guide;

/**
 * Created by dwi.prayogo on 7/4/2017.
 */

public class Guide_Activity extends AppCompatActivity {
    RelativeLayout pertanyaan2,lay_ask;
    ValueAnimator mAnimator;
    ImageView up,down;

    private Toolbar toolbar;
    String result = "";
    private ProgressBar progressBar;
    private ArrayList<Guide> results = new ArrayList<Guide>();
    String TAG = "News_Collection";
    ImageView home,logout;
    private SharedPreferences generalPrefs;
    private SharedPreferences.Editor prefsEditor;
    private String isLoadProspectForm;
    private String isLoadAsset;
    private String isLoadSupplier;
    private String flagUpdate;
    private boolean flag = false;
    private Context mContext;
    private SQLiteDatabase db;
    private DataSource datasource;
    String  authorizedMenus;
    private DataLocal datalocal;
    private String userID;
    private Exception e = null;
    String kategori = "";
    LinearLayout lay_refresh ;
    TextView open,isi,closing;
    private String branchName = "";
    private String branchAddress = "";
    private String branchPhone = "";
    private String moduleid;
    private Animation animationUp, animationDown;
    private final int COUNTDOWN_RUNNING_TIME = 450;
    //private final int COUNTDOWN_RUNNING_TIME = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setTitle("");
        }
        mContext = Guide_Activity.this;
        generalPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


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
            userID = generalPrefs.getString("userID", null);
            Log.d(TAG, "onCreate: "
                    +"waktun akhir : "+timeOut
                    +"waktu awal :"+sessionStart
                    +"Load Asset : "+isLoadAsset
                    +"Load Prospect :  "+isLoadProspectForm
                    +"Load Supplier : "+isLoadSupplier
                    +"userID : "    + userID);

            Date dateStart = df.parse(sessionStart);
            Date dateEnd = df.parse(now);

            diffInMin = (dateEnd.getTime() / 60000) - (dateStart.getTime() / 60000);

            if ((int) diffInMin > Integer.valueOf(timeOut) || (int) diffInMin < 0) {
                prefsEditor.putBoolean("finish", true).commit();
                finish();

                Intent myintent = new Intent(getApplicationContext(), HomeActivity.class);
                myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myintent);
                return;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        datasource = new DataSource(mContext);
        db = datasource.getWritableDatabase();
        datalocal = new DataLocal();

        authorizedMenus = generalPrefs.getString("authorizedMenu", null);
        Log.d(TAG, "onCreate: News_Collection " + authorizedMenus);

        Bundle bd = getIntent().getExtras();
        flagUpdate = bd.getString("flagUpdate");

        Log.d(TAG, "onCreate: cek flagnya "+flagUpdate);
        flag = true;
        animationUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        home = (ImageView) findViewById(R.id.gmbarhome);
        logout = (ImageView) findViewById(R.id.gambarlogout);
        //progressBar = (ProgressBar) findViewById(R.id.progressbar_report);
        lay_ask = (RelativeLayout) findViewById(R.id.lay_ask);
        pertanyaan2 = (RelativeLayout) findViewById(R.id.pertanyaan2);
        up = (ImageView) findViewById(R.id.up);
        down = (ImageView) findViewById(R.id.down);
        open = (TextView) findViewById(R.id.text_guide_opening);
        isi = (TextView) findViewById(R.id.text_guide_pertanyaan);
        closing = (TextView) findViewById(R.id.text_guide_closing);

        initbutton();

        if (authorizedMenus.contains("1")){
            kategori = "guide";
            new GetdataGuide().execute();
        }


    }

    private void initbutton(){

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SweetAlertDialog close = new SweetAlertDialog(Guide_Activity.this, SweetAlertDialog.WARNING_TYPE);
                close.setCanceledOnTouchOutside(true);
                close.setTitleText("Perhatian!")
                        .setContentText("Anda Yakin Untuk Kembali Ke Menu Utama ?")
                        .setConfirmText("Ya")
                        .setCancelText("Tidak")

                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {
                                //sDialog.dismiss();
                                //finish();
                                finish();
                                Intent myintent = new Intent(mContext,HomeActivity.class);
                                myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(myintent);

                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();

                            }
                        });
                close.show();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SweetAlertDialog close2 = new SweetAlertDialog(Guide_Activity.this, SweetAlertDialog.WARNING_TYPE);
                close2.setCanceledOnTouchOutside(true);
                close2.setTitleText("Perhatian!")
                        .setContentText("Anda Yakin Untuk Keluar ?")
                        .setConfirmText("Ya")
                        .setCancelText("Tidak")

                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {
                                //sDialog.dismiss();
                                //finish();
                                SharedPreferences.Editor prefsEditor = generalPrefs.edit();
                                prefsEditor.putBoolean("finish", true).commit();
                                finish();
                                Intent myintent = new Intent(mContext,HomeActivity.class);
                                myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(myintent);;

                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();

                            }
                        });
                close2.show();
            }
        });


        lay_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pertanyaan2.isShown()){
                    pertanyaan2.startAnimation(animationUp);
                    CountDownTimer countDownTimerStatic = new CountDownTimer(COUNTDOWN_RUNNING_TIME, 30) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            up.setVisibility(View.GONE);
                            down.setVisibility(View.VISIBLE);
                            pertanyaan2.setVisibility(View.GONE);
                        }
                    };
                    countDownTimerStatic.start();


                } else {
                    down.setVisibility(View.GONE);
                    up.setVisibility(View.VISIBLE);
                    pertanyaan2.setVisibility(View.VISIBLE);
                    pertanyaan2.startAnimation(animationDown);



                }

            }
        });


//        down.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                up.setVisibility(View.VISIBLE);
//                down.setVisibility(View.GONE);
//
//
//            }
//        });
//
//        up.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                up.setVisibility(View.GONE);
//                down.setVisibility(View.VISIBLE);
//            }
//        });
    }


    private class GetdataGuide extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {

            if (flagUpdate.equals("U") ) {
                //progressBar.setVisibility(View.VISIBLE);

            }
        }

        @Override
        protected String doInBackground(Void... unsued) {
            String result = "";

            try {

                if (!GetGuideData())
                    result = "ErrorConnection";



                if (results != null) {
                    if (results.size() > 0) {
                        results.clear();
                    }
                }

                Cursor c1 = db.rawQuery("SELECT pertanyaan, isi_pertanyaan, closing_pertanyaan  FROM guide ", null);

                Log.d(TAG, "doInBackground: "+ c1);

                try {
                    while (c1.moveToNext()) {
                        Guide sr1 = new Guide();
                        sr1.setPertanyaan(c1.getString(0));
                        sr1.setIsi_pertanyaan(c1.getString(1));
                        sr1.setClosing_pertanyaan(c1.getString(2));
                        results.add(sr1);
                    }
                } finally {
                    c1.close();
                    c1 = null;
                }

            } catch (Exception e) {
                e.printStackTrace();
                Guide_Activity.this.e = e;
            } finally {
                //db.isOpen();
                //db.close();
            }

            return result;
        }

        protected void onPostExecute(String sResponse) {
            Log.d(TAG, "onPostExecute: sResponse " +result);

            if (results !=null){

                //progressBar.setVisibility(View.GONE);

                for (int i = 0; i < results.size(); i++) {

                    open.setText( results.get(i).getPertanyaan());
                    isi.setText(results.get(i).getIsi_pertanyaan());
                    closing.setText(results.get(i).getClosing_pertanyaan());



                    Log.d(TAG, "onPostExecute: nmcabang" +results.get(i).getPertanyaan() );
                    Log.d(TAG, "onPostExecute: totalvisit"+results.get(i).getIsi_pertanyaan());
                    Log.d(TAG, "onPostExecute: total_task "+results.get(i).getClosing_pertanyaan());


                }
            }else {
                //progressBar.setVisibility(View.GONE);
                Toast.makeText(mContext,"Connection Time Out",Toast.LENGTH_SHORT).show();
            }



        }
    }


    private boolean GetGuideData() {
        try {
            if (!db.isOpen()) {
                db = datasource.getWritableDatabase();

            }

            InputStream in = null;
            ContentValues cv = new ContentValues();


            datasource.deleteData(db, "", "guide");


            StringBuilder sbl = new StringBuilder();
            sbl.append("");


            Calendar cl = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String myTime = df.format(cl.getTime());


            Log.d(TAG, "GetGuideData: "+ kategori);

            String theKey = datalocal.encrypt(myTime, getString(R.string.Key44));
            //String userIDEncrypt = datalocal.encrypt(userID, myTime);
            String kategori2 = datalocal.encrypt(kategori,myTime);

            URL localUrl = new URL(getString(R.string.url) + "/GetGuideData?a=" +
                    "&c=" + URLEncoder.encode(theKey, "UTF-8")+
                    "&b=" + URLEncoder.encode(kategori2, "UTF-8"));

            Log.d(TAG, "GetGuideData: "+localUrl);


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

            NodeList fields = root.getElementsByTagName("guide");

            for (int i = 0; i < fields.getLength(); i++) {

                Node fieldNode = fields.item(i);
                NamedNodeMap attr = fieldNode.getAttributes();


                cv.put("pertanyaan", attr.getNamedItem("pertanyaan").getNodeValue());
                cv.put("isi_pertanyaan", attr.getNamedItem("isi_pertanyaan").getNodeValue());
                cv.put("closing_pertanyaan", attr.getNamedItem("closing_pertanyaan").getNodeValue());

                datasource.generateData(db, cv, "guide");
                cv.clear();
            }

            return true;

        } catch (Exception e) {
            Log.e("Guide_Activity", "GetGuideData: "+e.getMessage() );
            e.printStackTrace();
            return false;
        }
    }


    private void expand() {

        pertanyaan2.setVisibility(View.VISIBLE);
        mAnimator.start();
    }

    private void collapse() {
        int finalHeight = pertanyaan2.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 10);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                pertanyaan2.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }


    private ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = pertanyaan2.getLayoutParams();
                layoutParams.height = value;
                pertanyaan2.setLayoutParams(layoutParams);
            }
        });
        return animator;
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


    public static void expand(final View v) {
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? WindowManager.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
