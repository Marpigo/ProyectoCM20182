package co.edu.udea.compumovil.gr02_20182.proyecto.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import co.edu.udea.compumovil.gr02_20182.proyecto.ActivityNavigationDrawer;
import co.edu.udea.compumovil.gr02_20182.proyecto.Firebase.UserFirebase;
import co.edu.udea.compumovil.gr02_20182.proyecto.R;


public class LoguinGoogleFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    public static final int SIGN_IN_CODE = 777; //codigo unico
    private FirebaseAuth firebaseAuth; //Para conectarnos con los datos de la base de datos
    private FirebaseAuth.AuthStateListener firebaseAuthListener; //Escucha si los datos ingresados, usuario y contraseña son correctos

    private ProgressBar progressBar;
    Activity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view;
        view = inflater.inflate(R.layout.fragment_loguin_google, container, false);

        init(view);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity = getActivity();

                //Intent que abre el selector o el inicio de sesion para una cuenta google
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE);

            }
        });
        return  view;
    }


    private void init(View view) {
        //segundo parametro de la autenticacion un objeto de opciones que dira como autenticarnos
        //Obetenemos tambien un Token con requestIdToken
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //inicializamos el GoogleApiCliente
        //gestionamos el ciclo de vida googlecliente con el activity utilizando enableAutoManage
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage((FragmentActivity) getContext(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton = (SignInButton)view.findViewById(R.id.signInButton); //button de google
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setColorScheme(SignInButton.COLOR_DARK);


    }


    //Este metodo llegan los resultado utilizando el requesCode, con el codigo a comprobar
    // y mediante Intent data obtenemos un resultado
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            iniciarSesionResultado(result);
        }
    }

    //aca ponemos lo que queremos hacer con el resultado, validamos que la operacion fue exitosa
    private void iniciarSesionResultado(GoogleSignInResult result) {
        if (result.isSuccess()) {
            openNavigationDrawer();
            UserFirebase.logueado  = 1;
        } else {
            // Toast.makeText(this, "NO LOGUEADO", Toast.LENGTH_SHORT).show();
        }
    }


    //este metodo se ejecuta cuando la conexion falla
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(activity, getString(R.string.s_Google_Autenticacion_fallo), Toast.LENGTH_SHORT).show();
    }

    private void openNavigationDrawer() {
        Intent miIntent = new Intent(getContext(), ActivityNavigationDrawer.class);
        startActivity(miIntent);
    }



}