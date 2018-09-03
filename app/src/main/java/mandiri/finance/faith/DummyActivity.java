package mandiri.finance.faith;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class DummyActivity extends Activity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (!WelcomeActivity.startedApp) {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
        }
        
        Log.i("startedApp",Boolean.toString(WelcomeActivity.startedApp));
        
        finish();
    }
}


