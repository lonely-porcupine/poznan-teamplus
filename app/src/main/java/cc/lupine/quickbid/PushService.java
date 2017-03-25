package cc.lupine.quickbid;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by maciej on 25/03/2017.
 */

public class PushService extends FirebaseInstanceIdService {
    private final String TAG = AppConfig.TAG;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        SharedPreferences prefs = AppUtils.getMainPrefs(getApplicationContext());
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString("push_key", refreshedToken);
        ed.commit();
        //sendRegistrationToServer(refreshedToken);

    }

}
