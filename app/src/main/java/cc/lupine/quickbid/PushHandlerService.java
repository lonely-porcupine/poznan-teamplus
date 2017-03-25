package cc.lupine.quickbid;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PushHandlerService extends FirebaseMessagingService {
    private final String TAG = AppConfig.TAG;



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
                final JSONObject data = new JSONObject(remoteMessage.getNotification().getBody());
                AuctionModel.newInstanceFromId(data.getString("auction_id"), new AuctionModel.FromIdInterface() {
                    @Override
                    public void onFetched(AuctionModel am) {
                        try {
                            String notification_title = am.getName();
                            String notification_body = "";
                            switch (data.getInt("type")) {
                                case 1:
                                    notification_body = getString(R.string.outbid_body, String.format("%.2f", am.getBidPrice()));
                                    break;
                                case 2:
                                    long unixTime = System.currentTimeMillis() / 1000L;
                                    long endTime = unixTime + am.getSecondsLeft();
                                    java.util.Date date = new java.util.Date((long)endTime*1000);
                                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                                    notification_body = getString(R.string.ending_body, df.format(date), String.format("%.2f", am.getBidPrice()));
                                    break;
                            }
                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(getBaseContext())
                                            .setSmallIcon(R.drawable.ic_home_black_24dp)
                                            .setContentTitle(notification_title)
                                            .setContentText(notification_body)
                                            .setStyle(new NotificationCompat.BigTextStyle().bigText(notification_body))
                                            .setDefaults(Notification.DEFAULT_ALL);

                            Intent iAction1 = new Intent(getBaseContext(), NotificationReceiver.class);
                            iAction1.setAction(am.getId());
                            PendingIntent piAction1 = PendingIntent.getBroadcast(getBaseContext(), 0, iAction1, PendingIntent.FLAG_CANCEL_CURRENT);


                            mBuilder.addAction(0, getString(R.string.bid), piAction1);

                            NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            mNotifyMgr.notify(0, mBuilder.build());
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON error from notification");
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JSONException e) {
                Log.e(TAG, "JSON error from notification");
                e.printStackTrace();
            }

        }


    }

}
