package cc.lupine.quickbid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = AppConfig.TAG;

    EditText edtLogin;
    EditText edtPassword;
    Button btnSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtLogin = (EditText) findViewById(R.id.input_login);
        edtPassword = (EditText) findViewById(R.id.input_password);
        btnSignin = (Button) findViewById(R.id.btn_signin);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.authenticate();
            }
        });
    }

    private void authenticate() {
        final String login = edtLogin.getText().toString();
        String password = edtPassword.getText().toString();
        if(login.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.login_pass_empty), Toast.LENGTH_LONG).show();
            return;
        }
        btnSignin.setEnabled(false);
        final ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setMessage(getString(R.string.loading));
        progressdialog.setIndeterminate(true);
        progressdialog.setCancelable(false);
        progressdialog.show();
        AppUtils.authenticate(login, password, new AppUtils.OnAuthenticationInterface() {
            @Override
            public void authenticated(String userid) {
                progressdialog.hide();
                SharedPreferences.Editor ed = AppUtils.getMainPrefs(getApplicationContext()).edit();
                ed.putString("user_uname", login.charAt(0) + "..." + login.charAt(login.length()-1));
                ed.commit();
                Log.d(TAG, "User authenticated, opening MainActivity");
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void notAuthenticated() {
                progressdialog.hide();
                Toast.makeText(getApplication(), R.string.invalid_login_pass, Toast.LENGTH_LONG).show();
                btnSignin.setEnabled(true);
            }
        });
    }
}
