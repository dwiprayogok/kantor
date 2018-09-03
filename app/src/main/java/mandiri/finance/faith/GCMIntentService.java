package mandiri.finance.faith;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
//import com.google.android.gcm.GCMBaseIntentService;



//public class GCMIntentService extends GCMBaseIntentService {
public class GCMIntentService extends AppCompatActivity {

//    @Override
//    protected void onError(Context context, String data) {
//        Log.e("Registration", "Got an error!");
//        Log.e("Registration", context.toString() + data.toString());
//    }
//
//    @Override
//    protected void onMessage(Context context, Intent data) {
//        Log.i("Registration", "Got a message!");
//
//    	String message;
//    	// Message from server
//    	message = data.getStringExtra("message");
//
//    	PendingIntent pIntent = PendingIntent.getActivity(context, 0, new Intent(), 0);
//    	// Create the notification with a notification builder
//    	@SuppressWarnings("deprecation")
//		Notification notification = new Notification.Builder(context)
//	    	.setSmallIcon(R.drawable.ic_launcher)
//	    	.setWhen(System.currentTimeMillis())
//	    	.setContentTitle("You have new survey task")
//	    	.setContentText(message).setContentIntent(pIntent)
//	    	.getNotification();
//
//    	// Remove the notification on click
//    	notification.flags |= Notification.FLAG_AUTO_CANCEL;
//    	notification.defaults |= Notification.DEFAULT_SOUND;
//    	notification.defaults |= Notification.DEFAULT_VIBRATE;
//
//    	NotificationManager manager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
//
//		manager.notify(R.string.app_name, notification);
//    	{
//	    	// Wake Android Device when notification received
//	    	PowerManager pm = (PowerManager) context
//	    	.getSystemService(Context.POWER_SERVICE);
//	    	final PowerManager.WakeLock mWakelock = pm.newWakeLock(
//	    	PowerManager.FULL_WAKE_LOCK
//	    	| PowerManager.ACQUIRE_CAUSES_WAKEUP, "GCM_PUSH");
//	    	mWakelock.acquire();
//
//	    	// Timer before putting Android Device to sleep mode.
//	    	Timer timer = new Timer();
//	    	TimerTask task = new TimerTask() {
//	    	public void run() {
//	    	mWakelock.release();
//    	}
//    	};
//    		timer.schedule(task, 5000);
//    	}
//    }
//
//    @Override
//    protected void onRegistered(Context context, String data) {
//        Log.i("Registration", "Just registered!");
//        Log.i("Registration", context.toString() + data.toString());
//
//        SharedPreferences generalPrefs = PreferenceManager.getDefaultSharedPreferences(context);
//
//        SharedPreferences.Editor prefsEditor = generalPrefs.edit();
//        prefsEditor.putString("registrationID", data.toString()).commit();
//    }
//
//    @Override
//    protected void onUnregistered(Context arg0, String arg1) {
//    }
}
