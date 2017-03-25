package cc.lupine.quickbid;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maciej on 24/03/2017.
 */

public class AppUtils {
    private static final String TAG = AppConfig.TAG;
    private static SharedPreferences mainPrefs = null;
    private static String accessToken = null;

    public static SharedPreferences getMainPrefs(Context c) {
        if(AppUtils.mainPrefs == null)
            AppUtils.mainPrefs = c.getSharedPreferences("main", Context.MODE_PRIVATE);

        return AppUtils.mainPrefs;
    }

    public static String getAccessToken() {
        return AppUtils.accessToken;
    }

    public static void fetchAccessToken(final OnAccessTokenFetchInterface intf) {
        if (AppUtils.accessToken == null && !mainPrefs.contains("access_token")) {
            String creds = String.format("%s:%s", AppConfig.CLIENT_ID, AppConfig.CLIENT_SECRET);
            String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
            AndroidNetworking.post("https://ssl.allegro.pl/auth/oauth/token")
                    .addHeaders("Authorization", auth)
                    .addBodyParameter("grant_type", "client_credentials")
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                AppUtils.accessToken = response.getString("access_token");
                                SharedPreferences.Editor ed = mainPrefs.edit();
                                ed.putString("access_token", AppUtils.getAccessToken());
                                ed.commit();
                                Log.d(TAG, "Access token saved");
                                Log.d(TAG, AppUtils.accessToken);
                                intf.onAccessTokenFetched();
                            } catch (JSONException e) {
                                Log.e(TAG, "Access token not present in the response");
                                System.out.println(response);
                                intf.onError("Access token not present in the response");
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.e(TAG, "Error while obtaining an auth token");
                            System.out.println(anError.getResponse());
                            intf.onError("Error while obtaining an auth token");
                        }
                    });
        } else if(mainPrefs.contains("access_token")) {
            AppUtils.accessToken = mainPrefs.getString("access_token", "");
            intf.onAccessTokenFetched();
        }
    }


    public static void isLoggedIn(Context c, final OnLoggedInResultInterface intf) {
        SharedPreferences prefs = AppUtils.getMainPrefs(c);
        if(!prefs.contains("access_token") || !prefs.contains("user_id")) {
            Log.d(TAG, "Preferences do not contain required parameters");
            intf.isNotLoggedIn();
            return;
        }
        AndroidNetworking.get("https://api.natelefon.pl/v1/allegro/login/{userId}")
                .addPathParameter("userId", prefs.getString("user_id", ""))
                .addQueryParameter("access_token", AppUtils.getAccessToken())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(response);
                            Boolean logged_in = response.getBoolean("authenticated");
                            Log.d(TAG, "User logged in: " + logged_in);
                            if(logged_in) {
                                Log.d(TAG, "User IS logged in");
                                intf.isLoggedIn();
                            } else {
                                Log.d(TAG, "User NOT logged in");
                                intf.isNotLoggedIn();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON Error occurred");
                            System.out.println(response);
                            intf.isNotLoggedIn();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Error while obtaining logged in status");
                        System.out.println(anError.getResponse());
                        intf.isNotLoggedIn();
                    }
                });
    }

    public static void authenticate(String login, String password, final OnAuthenticationInterface intf) {
        AndroidNetworking.post("https://api.natelefon.pl/v1/allegro/login")
                .addBodyParameter("access_token", AppUtils.getAccessToken())
                .addBodyParameter("userLogin", login)
                .addBodyParameter("hashPass", AppUtils.hashPassword(password))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.has("userId")) {
                                // logged in
                                SharedPreferences.Editor ed = mainPrefs.edit();
                                ed.putString("user_id", response.getString("userId"));
                                ed.commit();
                                Log.d(TAG, "User authenticated");
                                intf.authenticated(response.getString("userId"));

                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON error 1");
                            System.out.println(response);
                            intf.notAuthenticated();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Error while authenticating the user");
                        System.out.println(anError.getResponse());
                        intf.notAuthenticated();
                    }
                });
    }

    public static HashMap<String, Integer> parseStringSubs(String subs) {
        HashMap<String, Integer> map = new HashMap<>();
        String[] l = subs.split(";");
        for(String line : l) {
            Log.w(TAG, line);
            String[] data = line.split(",");
            if(data.length == 2)
                map.put(data[0], Integer.valueOf(data[1]));
        }
        Log.i(TAG, "pss");
        System.out.println(map);
        return map;
    }

    public static String subsToString(HashMap<String, Integer> subs) {
        int i = 0;
        String gen = "";
        for(String key : subs.keySet()) {
            Integer val = (Integer) subs.values().toArray()[i];
            gen += key;
            gen += ",";
            gen += val;
            gen += ";";
            i++;
        }
        Log.i(TAG, "sts");
        System.out.println(gen);
        return gen;
    }

    private static String hashPassword(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            return Base64.encodeToString(hash, Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public interface OnAccessTokenFetchInterface {
        void onAccessTokenFetched();
        void onError(String error);
    }

    public interface OnLoggedInResultInterface {
        void isLoggedIn();
        void isNotLoggedIn();
    }

    public interface OnAuthenticationInterface {
        void authenticated(String userid);
        void notAuthenticated();
    }
}
