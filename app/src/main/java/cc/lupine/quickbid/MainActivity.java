package cc.lupine.quickbid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;

public class MainActivity extends AppCompatActivity implements BidlistFragment.OnFragmentInteractionListener {
    private final String TAG = AppConfig.TAG;

    SharedPreferences prefs = null;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_bidlist:
                    ft.replace(R.id.fragment_container, BidlistFragment.newInstance());
                    break;
                case R.id.navigation_watchlist:
                    break;
                case R.id.navigation_wonlist:
                    break;
                case R.id.navigation_settings:
                    break;
                default:
                    Log.wtf(TAG, "This should never happen");
                    return false;
            }
            ft.commit();
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidNetworking.initialize(getApplicationContext());
        this.prefs = AppUtils.getMainPrefs(getApplicationContext());
        AppUtils.fetchAccessToken(new AppUtils.OnAccessTokenFetchInterface() {
            @Override
            public void onAccessTokenFetched() {
                AppUtils.isLoggedIn(getApplicationContext(), new AppUtils.OnLoggedInResultInterface() {
                    @Override
                    public void isLoggedIn() {
                        setContentView(R.layout.activity_main);
                        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
                        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
                    }

                    @Override
                    public void isNotLoggedIn() {
                        // user not logged in, switch to loginactivity
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                });

            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplication(), error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onFragmentInteraction() {
        Log.d(TAG, "onFragmentInteraction");
    }
}
