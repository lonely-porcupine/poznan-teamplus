package cc.lupine.quickbid;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class PushHandlerService extends FirebaseMessagingService {
    private final String TAG = AppConfig.TAG;
    public static final String ACTION1 = "BID";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            try {
                String notification_title = "";
                String notification_body = "";
                JSONObject data = new JSONObject(remoteMessage.getNotification().getBody());
                switch(data.getInt("type")) {
                    case 1:
                        notification_title = getString(R.string.outbid);
                        notification_body = getString(R.string.outbid_body);
                        break;
                }
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                                .setContentTitle(notification_title)
                                .setContentText(notification_body)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(notification_body));

                Intent iAction1 = new Intent(getBaseContext(), PushHandlerService.class);
                iAction1.setAction(PushHandlerService.ACTION1);
                PendingIntent piAction1 = PendingIntent.getService(this, 0, iAction1, 0);

                mBuilder.addAction(0, getString(R.string.bid), piAction1);
;

                NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.notify(0, mBuilder.build());
            } catch (JSONException e) {
                Log.e(TAG, "JSON error from notification");
                e.printStackTrace();
            }

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
}
