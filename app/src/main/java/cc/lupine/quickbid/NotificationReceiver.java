package cc.lupine.quickbid;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by maciej on 25/03/2017.
 */

public class NotificationReceiver extends BroadcastReceiver {
    private final String TAG = AppConfig.TAG;

    public NotificationReceiver() {}

    @Override
    public void onReceive(final Context c, Intent intent) {
        Log.i(TAG, "We're in onReceive");
        String action = intent.getAction();
        NotificationManager mNotifyMgr = (NotificationManager) c.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(0);
        AuctionModel.newInstanceFromId(action, new AuctionModel.FromIdInterface() {
            @Override
            public void onFetched(final AuctionModel am) {
                am.bid(am.getBidPrice() + 1.0, new AuctionModel.OnBidInterface() {
                    @Override
                    public void onBidResult(String resp) {
                        String notification_title = am.getName();
                        String notification_body = resp;
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(c)
                                        .setSmallIcon(R.drawable.ic_home_black_24dp)
                                        .setContentTitle(notification_title)
                                        .setContentText(notification_body)
                                        .setStyle(new NotificationCompat.BigTextStyle().bigText(notification_body))
                                        .setDefaults(Notification.DEFAULT_ALL);

                        Intent iAction1 = new Intent(c, NotificationReceiver.class);
                        iAction1.setAction(am.getId());
                        PendingIntent piAction1 = PendingIntent.getBroadcast(c, 0, iAction1, 0);

                        mBuilder.addAction(0, c.getString(R.string.bid), piAction1);

                        NotificationManager mNotifyMgr = (NotificationManager) c.getSystemService(NOTIFICATION_SERVICE);
                        mNotifyMgr.notify(0, mBuilder.build());

                    }

                    @Override
                    public void onBidFailure() {
                        Toast.makeText(c, c.getString(R.string.bid_error), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}