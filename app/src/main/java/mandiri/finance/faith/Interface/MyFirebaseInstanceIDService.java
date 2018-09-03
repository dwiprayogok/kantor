package mandiri.finance.faith.Interface;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Gue-PC on 3/24/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseIIDService";
    public String refreshToken = "";
    private SharedPreferences generalPrefs;
//    @Override
//    public void onTokenRefresh() {
//
//        refreshToken = FirebaseInstanceId.getInstance().getToken();
//
//        Log.d(TAG, "Refresh_Token: "+ refreshToken);
//        try {
//            sendRegistrationToServer(refreshToken);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public void onNewToken(String s) {

        //refreshToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                refreshToken = instanceIdResult.getToken();
            }
        });

        Log.d(TAG, "Refresh_Token: "+ refreshToken);
        try {
            sendRegistrationToServer(refreshToken);
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onNewToken(s);
    }



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    private void sendRegistrationToServer(String token){
        generalPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor prefsEditor = generalPrefs.edit();
        prefsEditor.putString("registrationID", token).commit();
    }
}
