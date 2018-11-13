package co.edu.udea.compumovil.gr02_20182.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "Notificación";
    private GoogleApiClient googleApiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        userAutenticadoEstado();

        notificaciones();


    }

    void notificaciones ()
    {
        String infoTextView ="";
        //infoTextView = (TextView) findViewById(R.id.infoTextView);
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                //infoTextView.append("\n" + key + ": " + value);
                infoTextView +=  "\n" + key + ": " + value;
            }
        }
      //  Toast.makeText(getApplicationContext(), infoTextView, Toast.LENGTH_SHORT).show();
       // String token = FirebaseInstanceId.getInstance().getToken();
      //  Log.d(TAG, "Token: " + token);
    }


    void userAutenticadoEstado()
    {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result = opr.get();

            if (result.isSuccess()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        openNavigationDrawer();
                    }
                }, 3000);


            }
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    openLoguin();
                }
            }, 3000);
        }
    }

    private void openLoguin() {
        Intent miIntent = new Intent(MainActivity.this, LoguinTabbed.class);
        startActivity(miIntent);
        finish();

    }

    private void openNavigationDrawer() {
        Intent miIntent = new Intent(MainActivity.this, ActivityNavigationDrawer.class);
        startActivity(miIntent);
        finish();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}