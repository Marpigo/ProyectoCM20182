package co.edu.udea.compumovil.gr02_20182.proyecto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
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


/*
* Activity para registrar los usuarios:
*  Se captura los datos de la activity, para insertar en la bdrestaurant
*
* */
public class UsuarioAtivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    static List<User> recibirListUsuario;
    UserFirebase userFirebase = new  UserFirebase();

    private Uri filePath;


    ImageView campoPhoto;
    EditText campoName, campoEmail, campoPassword;
    TextView campoId;

    Button butregistrarFirebase;
    public static int modo = 1; /*0.Nuevo, 1.Modificar*/


    Bitmap bitmaphoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_ativity);


        init();
        setupActionBar();


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

    private void setupActionBar() {
        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    public  void init()
    {
        campoName = (EditText) findViewById(R.id.ediNameUserR);
        campoEmail = (EditText) findViewById(R.id.ediEmailUserR);
        campoPassword = (EditText) findViewById(R.id.ediPasswordUserR);
        campoPhoto = (ImageView) findViewById(R.id.imgPhotoUserR);
        campoId = (TextView) findViewById(R.id.texId);
    }


    public void onClick(View view) {
        final String name = campoName.getText().toString();
        final String email = campoEmail.getText().toString();
        final String password = campoPassword.getText().toString();
        final String idU = campoId.getText().toString();

        boolean requerimientos = false;

        switch (view.getId()) {
            case R.id.imaSave:
                requerimientos = validateCampo(campoName.getText().toString(), campoEmail.getText().toString(), campoPassword.getText().toString());
                if (requerimientos) {
                    if (modo == 0 ){ //Nuevo
                        userFirebase.insertUser(name, email, password, filePath);
                        Toast.makeText(getApplicationContext(), getString(R.string.s_Firebase_registro), Toast.LENGTH_SHORT).show();
                        limpiar();
                    }else if(modo == 1){ //Modificar
                        userFirebase.updateUser(idU, name, email, password, filePath);
                        Toast.makeText(getApplicationContext(), getString(R.string.s_Firebase_registro), Toast.LENGTH_SHORT).show();
                        limpiar();
                    }
                }
                break;
            case R.id.imaNew:
                limpiar();
                modo=0;
                break;

            case R.id.imaDelete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.s_Firebase_eliminar) + ": " + campoName.getText().toString())
                        .setTitle(getString(R.string.s_Firebase_eliminar_continua));

                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        eliminarUser(idU);
                        limpiar();
                        modo =0;
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }

                });
                builder.show();
                break;

            case R.id.imgPhotoUserR:
                imagenGallery();
                break;
        }
    }

    void eliminarUser(String id)
    {
        userFirebase.deleteUsers(id);
    }

    /*
      Validar campos: Vacios o nulo
     */
    boolean validateCampo (String name, String email, String password){
        int vericar = 0;

        campoName.setError(null);
        campoEmail.setError(null);
        campoPassword.setError(null);
        ConnectivityManager con = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = con.getActiveNetworkInfo();

        if (TextUtils.isEmpty(name))
        {campoName.setError(getString(R.string.s_requerimiento).toString());vericar = 1;}

        if (TextUtils.isEmpty(email))
        {campoEmail.setError(getString(R.string.s_requerimiento).toString()); vericar += 1;}

        if (TextUtils.isEmpty(password))
        {campoPassword.setError(getString(R.string.s_requerimiento).toString()); vericar += 1;}

        if(filePath ==null)
            { Toast.makeText(this, "Imagen", Toast.LENGTH_SHORT).show();vericar += 1;}

        if(networkinfo == null && !networkinfo.isConnected()){
            //Toast.makeText(getApplicationContext(), getString(R.string.s_web_not_conexion), Toast.LENGTH_SHORT).show();
            vericar += 1;
        }
        return vericar>0?false:true;
    }


    private void imagenGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccionar la aplicación"),10);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            filePath = data.getData();

            try {
                bitmaphoto=MediaStore.Images.Media.getBitmap(this.getContentResolver(),filePath);
                campoPhoto.setImageBitmap(bitmaphoto);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void limpiar() {
        campoName.setText("");
        campoEmail.setText("");
        campoPassword.setText("");
        campoId.setText("");
        campoPhoto.setImageResource(R.drawable.ic_person_red_24dp);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



}
