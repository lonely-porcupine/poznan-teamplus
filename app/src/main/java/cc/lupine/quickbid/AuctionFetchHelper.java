package cc.lupine.quickbid;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by maciej on 25/03/2017.
 */

public class AuctionFetchHelper {
    private static final String TAG = AppConfig.TAG;

    public static void fetchBidlist(OnListFetchInterface intf) {
        AuctionFetchHelper.fetchList("bids/active", intf);
    }

    public static void fetchWatchlist(OnListFetchInterface intf) {
        //// TODO: 25/03/2017
    }

    public static void fetchWonlist(OnListFetchInterface intf) {
        AuctionFetchHelper.fetchList("bought", intf);
    }

    private static void fetchList(String name, final OnListFetchInterface intf) {
        AndroidNetworking.get("https://api.natelefon.pl/v1/allegro/my/{name}")
                .addPathParameter("name", name)
                .addQueryParameter("access_token", AppUtils.getAccessToken())
                .addQueryParameter("limit", "200")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<AuctionModel> list = new ArrayList<>();
                        try {
                            if(!response.has("count")) {
                                Log.e(TAG, "invalid response");
                                System.out.println(response);
                                intf.onListFetched(list);
                                return;
                            }
                            final int auctionCount = response.getInt("count");
                            Log.d(TAG, "Fetched " + auctionCount + " auctions");
                            JSONArray offers = response.getJSONArray("offers");
                            for(int i = 0; i < offers.length(); i++) {
                                JSONObject offer = (JSONObject) offers.get(i);
                                AuctionModel auction = new AuctionModel(
                                        offer.getString("id"),
                                        offer.getString("name"),
                                        offer.getBoolean("buyNow"),
                                        offer.getBoolean("auction"),
                                        offer.getJSONObject("prices").optDouble("bid", 0.0),
                                        offer.getJSONObject("prices").optDouble("buyNow", 0.0),
                                        offer.getLong("secondsLeft"),
                                        offer.getLong("endingTime"),
                                        offer.getBoolean("tillOnStock"),
                                        offer.getBoolean("allegroStandard"),
                                        offer.getJSONObject("bids").getInt("count"),
                                        offer.getJSONObject("mainImage").optString("small", ""),
                                        offer.getJSONObject("mainImage").optString("medium", ""),
                                        offer.getJSONObject("mainImage").optString("large", ""),
                                        offer.getJSONObject("seller").optString("name", ""));
                                list.add(auction);
                            }
                            intf.onListFetched(list);
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON Error occurred 3");
                            e.printStackTrace();
                            System.out.println(response);
                            intf.onListFetched(list);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Error while obtaining auction list");
                        System.out.println(anError.getResponse());
                        ArrayList<AuctionModel> list = new ArrayList<>();
                        intf.onListFetched(list);
                    }
                });
    }

    public interface OnListFetchInterface {
        void onListFetched(ArrayList<AuctionModel> list);
    }
}
