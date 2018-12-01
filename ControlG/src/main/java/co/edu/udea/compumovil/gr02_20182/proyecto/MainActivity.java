package co.edu.udea.compumovil.gr02_20182.proyecto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import co.edu.udea.compumovil.gr02_20182.proyecto.Firebase.UserFirebase;
import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.PerfilFragment;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.User;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "Notificación";
    private GoogleApiClient googleApiClient;


    static List<User> recibirListUsuario;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        iniciarFirebaseListUsuario();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        firebaseAuth = FirebaseAuth.getInstance();




        //Valida si la sesio permanece activiad, de lo contrario cierra las sesiones
        if (!cargarPreferenciasSesion()) {
            //actualizarUserLogueadoCerrar();
            firebaseAuth.signOut();
            openLoguin();
        }else
        {
            userAutenticadoEstado();

        }



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

        //Buscar si se encuentra autenticado con GOOGLE
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result = opr.get();

            if (result.isSuccess()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UserFirebase.logueado = 1;
                        openNavigationDrawer();
                    }
                }, 3000);


            }
        }else{
            new Handler().postDelayed(new Runnable() { //Busca si est autentiado con FIREBASE
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



    //Valida si mantiene activa la sesion
    public  boolean cargarPreferenciasSesion(){
        Boolean sesion = false;
        SharedPreferences preferences = getSharedPreferences("configuracionapp", Context.MODE_PRIVATE);
        int mantener_sesion = preferences.getInt("mantener_sesion", 1); //segundo parametro es informacion por defecto dado el caso que el archivo preferecnias no exita
        //int notificacion = preferences.getInt("notificacion", 1);
        sesion = (mantener_sesion==1?true:false);

        return  sesion;
    }

/*
    void iniciarFirebaseListUsuario() {

        UserFirebase userFirebase = new UserFirebase(this);
        userFirebase.limpiarLista();
        userFirebase.cargarListUsuario();
        recibirListUsuario = UserFirebase.usuarioList;

       // Toast.makeText(this, "tamaño lista: "+ recibirListUsuario.size(), Toast.LENGTH_SHORT).show();

    }
*/

    /*

    public void actualizarUserLogueadoCerrar(){

        int i=0;
        for(i=0; i<recibirListUsuario.size(); i++){
            final String name = recibirListUsuario.get(i).getName();
            final String email = recibirListUsuario.get(i).getEmail();
            final String password = recibirListUsuario.get(i).getPassword();
            final String idU = recibirListUsuario.get(i).getId();
            final String tipo = recibirListUsuario.get(i).getTipo();
            final String imagenurl= recibirListUsuario.get(i).getImagen();
            final int autenticado = 0;
            userFirebase.updateUserAutenticado(idU, name, email, password, imagenurl, tipo, autenticado);
        }
    }
    */

}