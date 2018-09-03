package mandiri.finance.faith;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

//public class WelcomeActivity extends LoginActivity {
public class WelcomeActivity extends AppCompatActivity {
    public static boolean startedApp;
    
	/** Called when the activity is first created. */
	protected boolean _active = true;
	//protected int _splashTime = 500;
    protected int _splashTime = 1000;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startedApp= true;
        
        setContentView(R.layout.splash);
        
        this.setRequestedOrientation(
        		ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        Thread WelcomeScreen = new Thread() {
            public void run() {
	            try { 
	            	sleep(_splashTime);
	            	finish();
	            	Intent myintent = new Intent(getApplicationContext(),LoginActivity.class);
	            	startActivity(myintent);           
	            } catch (InterruptedException e) {
	            	e.printStackTrace();
	            }
           }
       };
       
       WelcomeScreen.start();
    }    
    
    @Override
    public void onBackPressed() {
    	WelcomeActivity.startedApp = false;
        finish();
    }
    
    
}

