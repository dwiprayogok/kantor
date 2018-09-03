package mandiri.finance.faith;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mandiri.finance.faith.Adapter.News_Adapter;
import mandiri.finance.faith.Model.News;

public class News_Survey extends AppCompatActivity {
    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    News_Adapter news_adapter;
    private ArrayList<News> taskList = new ArrayList<News>();
    String TAG = "News_Survey";
    ImageView home, logout;
    private SharedPreferences generalPrefs;
    private SharedPreferences.Editor prefsEditor;
    private String isLoadProspectForm;
    private String isLoadAsset;
    private String isLoadSupplier;
    private String flagUpdate;
    private String flagUpdateberita;
    private boolean flag = false;
    private Context mContext;
    private SQLiteDatabase db;
    private DataSource datasource;
    String authorizedMenus;
    private DataLocal datalocal;
    private String userID;
    private Exception e = null;
    String kategori = "";
    LinearLayout lay_refresh;
    FloatingActionButton Refreshnews;
    AlertDialog alertDialog;
    AlertDialog.Builder builder;
    News model_news;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setTitle("");
        }

        swipeRefreshLayout =(SwipeRefreshLayout) findViewById(R.id.Refreshnews22);
        home = (ImageView) findViewById(R.id.gmbarhome);
        logout = (ImageView) findViewById(R.id.gambarlogout);
        progressBar = (ProgressBar) findViewById(R.id.progressbar2);


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
            isLoadProspectForm = generalPrefs.getString("isLoadProspectForm", null);
            isLoadAsset = generalPrefs.getString("isLoadAsset", null);
            isLoadSupplier = generalPrefs.getString("isLoadSupplier", null);
            userID = generalPrefs.getString("userID", null);
            Log.d(TAG, "onCreate: "
                    + "waktun akhir : " + timeOut
                    + "waktu awal :" + sessionStart
                    + "Load Asset : " + isLoadAsset
                    + "Load Prospect :  " + isLoadProspectForm
                    + "Load Supplier : " + isLoadSupplier
                    + "userID : " + userID);

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
        mContext = News_Survey.this;
        datasource = new DataSource(mContext);
        db = datasource.getWritableDatabase();
        datalocal = new DataLocal();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetNewsData();
                new DapetBerita_Credit().execute();
            }
        });

        lay_refresh = (LinearLayout) findViewById(R.id.lay_refresh2);
        Refreshnews = (FloatingActionButton) findViewById(R.id.Refreshnews);
        Refreshnews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshdata();
            }
        });

        Bundle bd = getIntent().getExtras();
        flagUpdate = bd.getString("flagUpdate");
        flagUpdateberita = bd.getString("flagUpdateberita");
        Log.d(TAG, "onCreate: cek flagnya " + flagUpdate + flagUpdateberita);
        flag = true;
        BagianListview();
        recycleviewitem();
        inibutton();
        authorizedMenus = generalPrefs.getString("authorizedMenu", null);
        Log.d(TAG, "onCreate: News_Survey " + authorizedMenus);

        if (authorizedMenus.contains("1")){
            kategori = "Credit";
            new DapetBerita_Credit().execute();
        }

//        if (authorizedMenus.contains("1")) {
//            kategori = "Collection";
//            new DapetBerita().execute();
//        } else {
//            kategori = "Credit";
//            new DapetBerita_Credit().execute();
//        }
    }


    private void refreshdata(){
        if (authorizedMenus.contains("3")){
            kategori = "Collection";
            new DapetBerita().execute();
        }else {
            kategori = "Credit";
            new DapetBerita_Credit().execute();
        }
    }


    private void inibutton(){
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SweetAlertDialog close = new SweetAlertDialog(News_Survey.this, SweetAlertDialog.WARNING_TYPE);
                close.setCanceledOnTouchOutside(true);
                close.setTitleText("Perhatian!")
                        .setContentText("Anda Yakin Untuk Kembali Ke Menu Utama ?")
                        .setConfirmText("Ya")
                        .setCancelText("Tidak")

                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                finish();

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
                final SweetAlertDialog close2 = new SweetAlertDialog(News_Survey.this, SweetAlertDialog.WARNING_TYPE);
                close2.setCanceledOnTouchOutside(true);
                close2.setTitleText("Perhatian!")
                        .setContentText("Anda Yakin Untuk Keluar ?")
                        .setConfirmText("Ya")
                        .setCancelText("Tidak")

                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                finish();

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

    }

    private void BagianListview() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) findViewById(R.id.list_news);
        news_adapter = new News_Adapter(News_Survey.this,taskList);
        recyclerView.setAdapter(news_adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(llm);
    }

    private void recycleviewitem(){
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                model_news = taskList.get(position);
                opendialog_rating();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


    private void opendialog_rating(){
        LayoutInflater inflater = getLayoutInflater();
        View subView = inflater.inflate(R.layout.dialog_news, null);


        builder= new android.support.v7.app.AlertDialog.Builder(this);


        final TextView content = (TextView) subView.findViewById(R.id.content_info);
        final TextView footer = (TextView) subView.findViewById(R.id.footer_info);
        final TextView header = (TextView) subView.findViewById(R.id.header_info);



        header.setText(model_news.getHeader());
        content.setText(model_news.getContent());
        footer.setText(model_news.getFooter());

        Linkify.addLinks(content, Linkify.WEB_URLS);

        builder.setView(subView);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        alertDialog.show();

    }


    private class DapetBerita extends AsyncTask<String, Void, String> {
        String theKey ="";
        String uri ="";
        String url="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);


        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {

                if (!GetNewsData())
                    result = "ErrorConnection";

                if (taskList != null) {
                    if (taskList.size() > 0) {
                        taskList.clear();
                    }
                }

                Cursor c1 = db.rawQuery("SELECT header, isi, footer FROM news", null);

                Log.d(TAG, "doInBackground: "+ c1);

                try {
                    while (c1.moveToNext()) {
                        News sr1 = new News();
                        sr1.setHeader(c1.getString(0));
                        sr1.setContent(c1.getString(1));
                        sr1.setFooter(c1.getString(2));

                        taskList.add(sr1);
                    }
                } finally {
                    c1.close();

                }

            } catch (Exception e) {
                e.printStackTrace();
                News_Survey.this.e = e;
            } finally {
                //db.isOpen();
                //db.close();
            }

            return result;
            //return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("ErrorConnection")){
                lay_refresh.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
                recyclerView.setAdapter(news_adapter);
                news_adapter.notifyDataSetChanged();
            }

            Log.d(TAG, "onPostExecute: "+result);






        }

    }

    private class DapetBerita_Credit extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String result = "";
            try {

                if (!GetNewsData())
                    result = "ErrorConnection";

                if (taskList != null) {
                    if (taskList.size() > 0) {
                        taskList.clear();
                    }
                }

                Cursor c1 = db.rawQuery("SELECT header, isi, footer FROM news ", null);

                Log.d(TAG, "doInBackground: "+ c1);

                try {
                    while (c1.moveToNext()) {
                        News sr1 = new News();
                        sr1.setHeader(c1.getString(0));
                        sr1.setContent(c1.getString(1));
                        sr1.setFooter(c1.getString(2));

                        taskList.add(sr1);
                    }
                } finally {
                    c1.close();
                    c1 = null;
                }


            } catch (Exception e) {
                e.printStackTrace();
                News_Survey.this.e = e;
            } finally {

            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (e == null && s.length() == 0) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setAdapter(news_adapter);
                news_adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }else {
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                lay_refresh.setVisibility(View.VISIBLE);
                Toast.makeText(mContext, "Connection time out", Toast.LENGTH_SHORT).show();
                e = null;
            }

        }
    }


    private boolean GetNewsData() {
        try {
            if (!db.isOpen()) {
                db = datasource.getWritableDatabase();

            }

            InputStream in = null;
            ContentValues cv = new ContentValues();


            datasource.deleteData(db, "", "news");


            StringBuilder sbl = new StringBuilder();
            sbl.append("");


            Calendar cl = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String myTime = df.format(cl.getTime());


            Log.d(TAG, "GetNewsData: "+ kategori);

            String theKey = datalocal.encrypt(myTime, getString(R.string.Key44));
            //String userIDEncrypt = datalocal.encrypt(userID, myTime);
            String kategori2 = datalocal.encrypt(kategori,myTime);



            java.net.URL localUrl = new URL(getString(R.string.url) + "/GetNewsData?" +
                    "&c=" + URLEncoder.encode(theKey, "UTF-8")+
                    "&b=" + URLEncoder.encode(kategori2, "UTF-8"));

            Log.d(TAG, "GetNewsData: "+localUrl);


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

            NodeList fields = root.getElementsByTagName("berita");

            for (int i = 0; i < fields.getLength(); i++) {

                Node fieldNode = fields.item(i);
                NamedNodeMap attr = fieldNode.getAttributes();


                cv.put("header", attr.getNamedItem("header").getNodeValue());
                cv.put("isi", attr.getNamedItem("isi").getNodeValue());
                cv.put("footer", attr.getNamedItem("footer").getNodeValue());

                datasource.generateData(db, cv, "news");
                cv.clear();
            }

            return true;

        } catch (Exception e) {
            //Log.e("List_Collection_Activity", "GetCollectionData : " + e.getMessage());
            Log.e("Report_Collection", "GetReportData: "+e.getMessage() );
            e.printStackTrace();
            return false;
        }
    }



    @Override
    protected void onPause() {
        super.onPause();

        flag = false;
        String now;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        now = df.format(c.getTime());
        generalPrefs.edit().putString("sessionStart", String.valueOf(now)).commit();
    }

    @Override
    protected void onResume() {
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
                    Toast.makeText(getApplicationContext(),"Your session has been expired, please re login for security purpose", Toast.LENGTH_LONG).show();
                    prefsEditor.putBoolean("finish", true).commit();

                    finish();
                    Intent myintent = new Intent(getApplicationContext(),HomeActivity.class);
                    myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(myintent);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
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
        final android.app.AlertDialog alert = builder.create();
        alert.show();


    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private News_Survey.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final News_Survey.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
