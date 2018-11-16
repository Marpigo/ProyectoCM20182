package co.edu.udea.compumovil.gr02_20182.proyecto.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.List;

import co.edu.udea.compumovil.gr02_20182.proyecto.Firebase.UserFirebase;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.User;
import co.edu.udea.compumovil.gr02_20182.proyecto.R;


public class UsuarioFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener{


    static List<User> recibirListUsuario;
    UserFirebase userFirebase = new  UserFirebase();

    private Uri filePath;


    ImageView campoPhoto; //imagen gris logo
    ImageView campoPhoto2; //imagen gris camara
    EditText campoName, campoEmail, campoPassword;
    TextView campoId;
    Bitmap bitmaphoto;

    Button butregistrarFirebase;
    public static int modo = 1; /*0.Nuevo, 1.Modificar*/


    Activity activity;


    public UsuarioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view;
        view = inflater.inflate(R.layout.fragment_usuario, container, false);
        init(view);


        int logueado;
        logueado = UserFirebase.logueado; //1.Gollge, 2.Usuario

        if(logueado == 1)
        {
            campoName.setText("Usuario Google");
            modo = 0; //nuevo
        }else if(logueado == 2){
            usuarioLogueado(); //logueo firebase
            modo = 1; //modificar
        }

        campoPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity = getActivity();
                imagenGallery();
            }
        });

        campoPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity = getActivity();
                imagenGallery();
            }
        });

        setHasOptionsMenu(true);//nos permite ejecutar icono del menu toobar onOptionsItemSelected

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_gestionar, menu);

        MenuItem menuItem;

        menuItem = menu.findItem(R.id.action_gestionar_guardar);
        menuItem.setVisible(true);

        menuItem = menu.findItem(R.id.action_gestionar_nuevo);
        menuItem.setVisible(true);

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

        }
        if (id == R.id.action_gestionar_guardar) {
            usarioGuardar();

        }else if (id == R.id.action_gestionar_nuevo) {
            limpiar();
            modo = 1;
        }

        return super.onOptionsItemSelected(item);
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


    public  void init(View view)
    {
        campoName = (EditText) view.findViewById(R.id.ediNameUserR);
        campoEmail = (EditText) view.findViewById(R.id.ediEmailUserR);
        campoPassword = (EditText) view.findViewById(R.id.ediPasswordUserR);
        campoPhoto = (ImageView) view.findViewById(R.id.imgUsuarioG);
        campoPhoto2 = (ImageView) view.findViewById(R.id.imgUsuarioG2);
        campoId = (TextView) view.findViewById(R.id.texId);
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

       // if(filePath ==null)
       // { Toast.makeText(getContext(), "Imagen", Toast.LENGTH_SHORT).show();vericar += 1;}

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

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
