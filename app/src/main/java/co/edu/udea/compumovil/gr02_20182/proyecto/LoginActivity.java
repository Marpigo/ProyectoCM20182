package co.edu.udea.compumovil.gr02_20182.proyecto;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20182.proyecto.Fragments.PerfilFragment;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.User;
import co.edu.udea.compumovil.gr02_20182.proyecto.SQLiteconexion.DatabaseSQLite;
import co.edu.udea.compumovil.gr02_20182.proyecto.SQLiteconexion.DatabaseSQLiteUser;

public class LoginActivity extends AppCompatActivity {


    EditText campoName;/*Usaurio a buscar, perfil*/
    EditText campoPassword;
    Button butenter_services;
    ImageView campoPhoto;

    public static byte[] photo;

    ArrayList<User> userList;
    ProgressDialog progreso;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        butenter_services = (Button) findViewById(R.id.butLoguin);

        //campo a buscar
        campoName = (EditText) findViewById(R.id.ediName_loguin);
        campoPassword = (EditText) findViewById(R.id.ediPass_loguin);
        campoPhoto = (ImageView) findViewById(R.id.imageView);

        //Validar si hay conexion de internet
        ConnectivityManager con = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = con.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) {

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.s_web_not_conexion), Toast.LENGTH_SHORT).show();
        }
    }



        public void onClick(View view) {
        switch (view.getId()){
            case R.id.butLoguin:
                String campos="";
                campos = validateCampo(campoName.getText().toString(), campoPassword.getText().toString());
                if(campos.length() == 0)
                {
                    PerfilFragment.user_login= campoName.getText().toString(); //User logueado
                    PerfilFragment.user_pass= campoPassword.getText().toString();
                    openNavigationDrawer();
            }else{
                    Toast.makeText(this, campos, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onCreateClick(View view) {
        openCreateUser();
    }

    private void openNavigationDrawer() {
        Intent miIntent = new Intent(LoginActivity.this, ActivityNavigationDrawer.class);
        startActivity(miIntent);
        finish();
    }

    public void openCreateUser() {
        Intent miIntent = new Intent(LoginActivity.this, UsuarioAtivity.class);
        startActivity(miIntent);
    }


    /*
     * Validar campos: Vacios o nulo
     * */
    String validateCampo (String name, String password){
        String campos;
        campos = name !=null && name.trim().length()>0? "" : "\n" + campoName.getHint() + "\n";
        campos += password !=null && password.trim().length()>0?"" : campoPassword.getHint() + "\n";
        campos += userValidate()?"" : "Usuario o contraseña no valido"+ "\n";
        return campos;
    }


    Boolean userValidate()
    {
        Boolean uservalido = false;
        List<User> userList;
        DatabaseSQLiteUser databasesqliteduser = new DatabaseSQLiteUser();
        final DatabaseSQLite databasesqlit = DatabaseSQLite.getInstance(this);
        databasesqlit.open();

        String name;
        String password;
        int consulta = 0;

        name = campoName.getText().toString();
        password = campoPassword.getText().toString();

        userList = databasesqliteduser.getUser(name, password);

        uservalido = userList.size()>0?true:false;

        databasesqlit.close();
        return  uservalido;
    }



}
