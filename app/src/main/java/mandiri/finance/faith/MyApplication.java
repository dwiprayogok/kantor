package mandiri.finance.faith;

import com.bugsense.trace.BugSenseHandler;
import com.onesignal.OneSignal;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import mandiri.finance.faith.Interface.MyNotificationOpenedHandler;
import mandiri.finance.faith.Interface.MyNotificationReceivedHandler;

public class MyApplication extends Application {
    private static Context context;

    public static Context getContext() {
        return context;
    }
      @Override
      public void onCreate() {
    	  super.onCreate();
    	  BugSenseHandler.initAndStartSession(this, "27841327");
          MultiDex.install(this);

          context = getApplicationContext();
          //MyNotificationOpenedHandler : This will be called when a notification is tapped on.
          //MyNotificationReceivedHandler : This will be called when a notification is received while your app is running.
          OneSignal.startInit(this)
                  .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                  .unsubscribeWhenNotificationsAreDisabled(true)
                  .setNotificationOpenedHandler(new MyNotificationOpenedHandler())
                  .setNotificationReceivedHandler( new MyNotificationReceivedHandler() )
                  .init();

//          OneSignal.startInit(this)
//                  .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                  .unsubscribeWhenNotificationsAreDisabled(true)
//                  .init();
      }
  }



