package co.edu.udea.compumovil.gr02_20182.proyecto.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;

import java.io.IOException;
import java.util.List;

import co.edu.udea.compumovil.gr02_20182.proyecto.ActivityNavigationDrawer;
import co.edu.udea.compumovil.gr02_20182.proyecto.Firebase.UserFirebase;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.User;
import co.edu.udea.compumovil.gr02_20182.proyecto.R;



public class ConfigUsuarioFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{


    static List<User> recibirListUsuario;
    UserFirebase userFirebase = new  UserFirebase();

    private Uri filePath;

    static boolean menu_visible = true;

    Activity activity;
    private GoogleApiClient googleApiClient;


    ImageView campoPhoto; //imagen gris logo
    EditText campoName, campoEmail, campoPassword;
    TextView campoId;
    Bitmap bitmaphoto;

    ImageView img_cancelar;
    ImageView img_nuevo;
    ImageView img_guardar;

    public static int modo = 1; /*0.Nuevo, 1.Modificar*/



    public ConfigUsuarioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view;
        view = inflater.inflate(R.layout.fragment_config_usuario, container, false);
        init(view);


        final FloatingActionButton fabfoto = (FloatingActionButton) view.findViewById(R.id.fabFotoUsuairo);
        fabfoto.setOnClickListener(this);

        activity = getActivity();


        int logueado;
        logueado = UserFirebase.logueado; //1.Gollge, 2.Usuario

        if(logueado == 1)
        {
            iniciarGoogle(); //Logueado con Google
                loguinData();
                modo = 0; //nuevo

            campoName.setEnabled(false);
            campoEmail.setEnabled(false);
            campoPassword.setEnabled(false);
        }else if(logueado == 2){

            usuarioLogueado(); //logueo firebase
            modo = 1; //modificar
        }


        img_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usarioGuardar();

            }
        });

        img_nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiar();
                modo = 0;
            }
        });

        img_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuRegresar();
                botonFlotanteMenu(true);
                modo = 0;
            }
        });
        return view;
    }


    public void botonFlotanteMenu(boolean mostrar) {
        if (mostrar) {
            ActivityNavigationDrawer.fabgrupo.setVisibility(View.VISIBLE); //Mostrar o Ocultar el boton flotante
            ActivityNavigationDrawer.fabgrupo.expand();

        } else if (!mostrar) {
            ActivityNavigationDrawer.fabgrupo.setVisibility(View.INVISIBLE); //Ocultar o Ocultar el boton flotante
        }
    }

    //implements View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fabFotoUsuairo:
                imagenGallery();
                break;
        }
    }


    public  void init(View view)
    {
        campoName = (EditText) view.findViewById(R.id.ediNameUserR);
        campoEmail = (EditText) view.findViewById(R.id.ediEmailUserR);
        campoPassword = (EditText) view.findViewById(R.id.ediPasswordUserR);
        campoPhoto = (ImageView) view.findViewById(R.id.imgUsuarioG);
        campoId = (TextView) view.findViewById(R.id.texId);

        img_cancelar = (ImageView) view.findViewById(R.id.imaCancelarU);
        img_nuevo = (ImageView) view.findViewById(R.id.imaNuevoU);
        img_guardar = (ImageView) view.findViewById(R.id.imaGuardarU);

    }


    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.stopAutoManage(getActivity());
            googleApiClient.disconnect();
        }
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
            campoName.setText(user.getDisplayName());
            campoEmail.setText(user.getEmail());
            Glide.with(activity).load(user.getPhotoUrl()).into(campoPhoto);

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
                campoName.setText(recibirListUsuario.get(i).getName() );
                campoEmail.setText(recibirListUsuario.get(i).getEmail());
                Glide.with(activity).load(recibirListUsuario.get(i).getImagen()).into(campoPhoto);

            }
        }
    }



    void usuarioLogueado(){
        recibirListUsuario = UserFirebase.usuarioList;
        boolean autenticado = false;
        int i = 0;
        for(i = 0; (i < recibirListUsuario.size() && !autenticado); i++) {
            if (recibirListUsuario.get(i).getAutenticado() == 1) {
                autenticado = true;
                campoName.setText(recibirListUsuario.get(i).getName());
                campoEmail.setText(recibirListUsuario.get(i).getEmail());
                campoPassword.setText(recibirListUsuario.get(i).getPassword());
                campoId.setText(recibirListUsuario.get(i).getId());
                String imag = recibirListUsuario.get(i).getImagen();
                Glide.with(this).load(imag).into(campoPhoto);
            }
        }
    }



    public void usarioGuardar(){
        final String name = campoName.getText().toString();
        final String email = campoEmail.getText().toString();
        final String password = campoPassword.getText().toString();
        final String idU = campoId.getText().toString();

        boolean requerimientos = false;
        requerimientos = validateCampo(campoName.getText().toString(), campoEmail.getText().toString(), campoPassword.getText().toString());
        if (requerimientos) {
            if (modo == 0 ){ //Nuevo
                userFirebase.insertUser(name, email, password, filePath);
                Toast.makeText(activity, getString(R.string.s_Firebase_registro), Toast.LENGTH_SHORT).show();
                limpiar();
            }else if(modo == 1){ //Modificar
                userFirebase.updateUser(idU, name, email, password, filePath);
                Toast.makeText(activity, getString(R.string.s_Firebase_registro), Toast.LENGTH_SHORT).show();
                limpiar();
            }
        }
    }


    void eliminarUser(String id)
    {
        userFirebase.deleteUsers(id);
    }

    /*
      Validar campos: Vacios o nulo
     */
    public boolean validateCampo (String name, String email, String password){
        int vericar = 0;

        campoName.setError(null);
        campoEmail.setError(null);
        campoPassword.setError(null);
        //ConnectivityManager con = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //NetworkInfo networkinfo = con.getActiveNetworkInfo();

        if (TextUtils.isEmpty(name))
        {campoName.setError(getString(R.string.s_requerimiento).toString());vericar = 1;}

        if (TextUtils.isEmpty(email))
        {campoEmail.setError(getString(R.string.s_requerimiento).toString()); vericar += 1;}

        if (TextUtils.isEmpty(password))
        {campoPassword.setError(getString(R.string.s_requerimiento).toString()); vericar += 1;}

        if(filePath ==null)
        {
        /*  Uri uriImage = Uri.parse("android.resource://" + getPackageName() +"/"+R.drawable.user);
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/"+ R.raw.color_whele);
            File file = "archivo";
            Uri uriImagex = Uri.parse("archivo");
            filePath = Uri.parse(String.valueOf(R.drawable.user));//advertencia sin foto
            */
            Toast.makeText(getContext(), "Imagen", Toast.LENGTH_SHORT).show();
            vericar += 1;
        }

     //   if(networkinfo == null && !networkinfo.isConnected()){
            //Toast.makeText(getApplicationContext(), getString(R.string.s_web_not_conexion), Toast.LENGTH_SHORT).show();
        //    vericar += 1;
      //  }
        return vericar>0?false:true;
    }


    private void imagenGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccionar la aplicación"),10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int RESULT_OK = -1;

        if(resultCode == RESULT_OK)
        {
            filePath = data.getData();

            try {
                bitmaphoto=MediaStore.Images.Media.getBitmap(activity.getContentResolver(),filePath);
                campoPhoto.setImageBitmap(bitmaphoto);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




    }

    private void limpiar() {
        campoName.setError(null);
        campoEmail.setError(null);
        campoPassword.setError(null);
        campoName.setText("");
        campoEmail.setText("");
        campoPassword.setText("");
        campoId.setText("");
        campoPhoto.setImageResource(R.drawable.ic_user_loguin);

        campoName.setEnabled(true);
        campoEmail.setEnabled(true);
        campoPassword.setEnabled(true);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void menuRegresar(){
        FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new ControlMenuFragment()).commit();
    }


}
