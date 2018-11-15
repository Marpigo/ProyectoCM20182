package co.edu.udea.compumovil.gr02_20182.proyecto.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import java.util.List;
import co.edu.udea.compumovil.gr02_20182.proyecto.Firebase.UserFirebase;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.User;
import co.edu.udea.compumovil.gr02_20182.proyecto.R;

public class PerfilFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    TextView name_profil, email_profil;
    ImageView photo_profil;
    Activity activity;
    private GoogleApiClient googleApiClient;
    static List<User> recibirListUsuario;
    public static int logueado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;
        view = inflater.inflate(R.layout.fragment_perfil, container, false);
        init(view);


        activity = getActivity();


        logueado = UserFirebase.logueado; //1.Gollge, 2.Usuario

        if(logueado == 1)
        {
            iniciarGoogle(); //Logueado con Google
            loguinData();

        }else if(logueado == 2){
            iniciarFirebaseList(); // Logueado con Firebase
            userLogueadoFirebase();
        }


        return view;
    }

    public  void init(View view)
    {
        name_profil = (TextView)view.findViewById(R.id.txtNameProfil);
        email_profil = (TextView)view.findViewById(R.id.txtEmailProfil);
        photo_profil = (ImageView)view.findViewById(R.id.imgPhotoProfile);
    }

    void iniciarGoogle()
    {
        //segundo parametro de la autenticacion un objeto de opciones que dira como autenticarnos
        //Obetenemos tambien un Token con requestIdToken
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        //inicializamos el GoogleApiCliente
        //gestionamos el ciclo de vida googlecliente con el activity utilizando enableAutoManage
        googleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage((FragmentActivity) activity, 1, this) //el 1 es para enurar el fragmente
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    void iniciarFirebaseList()
    {
        recibirListUsuario = UserFirebase.usuarioList;

    }


    void loguinData()
    {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result = opr.get();
            userLogueadoGoogle(result);
        }else{
            //Toast.makeText(getContext(), "NO SE HA INICIADO SESION", Toast.LENGTH_SHORT).show();
        }
    }

    //aca ponemos lo que queremos hacer con el resultado, validamos que la operacion fue exitosa
    private void userLogueadoGoogle(GoogleSignInResult result) {
        if (result.isSuccess()) {

            GoogleSignInAccount user = result.getSignInAccount();
            name_profil.setText(user.getDisplayName());
            email_profil.setText(user.getEmail());
            Glide.with(activity).load(user.getPhotoUrl()).into(photo_profil);

        } else {
            //Toast.makeText(this, "NO LOGUEADO", Toast.LENGTH_SHORT).show();
        }
    }

    //aca ponemos lo que queremos hacer con el resultado, validamos que la operacion fue exitosa
    @SuppressLint("LongLogTag")
    private void userLogueadoFirebase() {

        boolean autenticado = false;
        int i = 0;
        for(i = 0; (i < recibirListUsuario.size() && !autenticado); i++)
        {
            if(recibirListUsuario.get(i).getAutenticado() == 1)
            {
                autenticado = true;
                name_profil.setText(recibirListUsuario.get(i).getName() );
                email_profil.setText(recibirListUsuario.get(i).getEmail());
                Glide.with(activity).load(recibirListUsuario.get(i).getImagen()).into(photo_profil);
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {



    }

    public static class ControlMenuMain extends Fragment {

        public ControlMenuMain() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_control_menu, container, false);
        }

    }
}
