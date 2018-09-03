package mandiri.finance.faith.Interface;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import mandiri.finance.faith.HomeActivity;
import mandiri.finance.faith.News_Survey;
import mandiri.finance.faith.R;

/**
 * Created by Gue-PC on 3/24/2017.
 */

public class MyFirebaseMesagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    public void onMessageReceived(RemoteMessage remoteMessage) {


        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "onMessageReceived: getData "+ remoteMessage.getData());
        Log.d(TAG, "onMessageReceived: getData "+ remoteMessage.getMessageId());
/*
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            sendNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("body"));
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            //sendNotification(remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("body"));
        }*/


        boolean sudah_kirim_notif = false;

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            sendNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("body"));
            sudah_kirim_notif = true;
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null && !sudah_kirim_notif) {
            Log.d(TAG, "Message Notification Body: getBody " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "Message Notification Body: getTitle " + remoteMessage.getNotification().getTitle());
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            //Intent resultIntent = new Intent(getApplicationContext(), News_Survey.class);
            //resultIntent.putExtra("flagUpdate", "U");
            //startActivity(resultIntent);
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String title,String messageBody) {
        Intent intent = new Intent(this, News_Survey.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("flagUpdate", "U");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0  , intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.logomtf)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());


    }

//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//
//        Log.d(TAG, "onMessageReceived: from "+remoteMessage.getFrom());
//        Log.d(TAG, "onMessageReceived: from "+remoteMessage.getNotification().getBody());
//
//        sendNotification(remoteMessage.getNotification().getBody());
//    }
//
//    private void sendNotification(String messageBody){
//        Intent intent = new Intent(this, HomeActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pen = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notif = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.logomtf)
//                .setContentTitle("coba")
//                .setContentText(messageBody)
//                .setAutoCancel(true)
//                .setSound(defaultUri)
//                .setContentIntent(pen);
//
//
//        NotificationManager notif2 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notif2.notify(0,notif.build());
//    }
}
