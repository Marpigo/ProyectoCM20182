package co.edu.udea.compumovil.gr02_20182.proyecto.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import co.edu.udea.compumovil.gr02_20182.proyecto.ActivityNavigationDrawer;
import co.edu.udea.compumovil.gr02_20182.proyecto.Firebase.UserFirebase;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.User;
import co.edu.udea.compumovil.gr02_20182.proyecto.R;



public class LoguinFirebaseFragment extends Fragment {


    static List<User> usuarioList;

    private Button button;
    private Activity activity;
    EditText campoEmail;/*Usaurio a buscar, perfil*/
    EditText campoPassword;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_loguin_firebase, container, false);
        init(view);

        activity = getActivity();

        iniciarFirebaseList();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = campoEmail.getText().toString();
                String password = campoPassword.getText().toString();


                if( validateCampo(email, password))
                {
                    UserFirebase.logueado  = 2;
                    openNavigationDrawer();
                }
            }
        });

        return  view;
    }



    void iniciarFirebaseList()
    {
        UserFirebase userFirebase = new  UserFirebase(activity);
        userFirebase.limpiarLista();
        userFirebase.cargarListUsuario();
        usuarioList = userFirebase.getListaUsuarios();
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        return super.shouldShowRequestPermissionRationale(permission);
    }

    private void openNavigationDrawer() {
        Intent miIntent = new Intent(getContext(), ActivityNavigationDrawer.class);
        startActivity(miIntent);
        //finish();
    }

    public void init(View view){

        button = (Button)view.findViewById(R.id.butLoguinF); //button de google
        //campo a buscar
        campoEmail = (EditText) view.findViewById(R.id.ediEmailLoguinF);
        campoPassword = (EditText) view.findViewById(R.id.ediPasswordLoguinF);

        //   mDatabase = FirebaseDatabase.getInstance().getReference(); //Inicia la referencia con la base de datos en el nodo principal 'mRootReference'
        //  mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    /*
     * Validar campos: Vacios o nulo
     * */
    boolean validateCampo (String email, String password){
        int  vericar = 0;

        campoEmail.setError(null);
        campoPassword.setError(null);

        if (TextUtils.isEmpty(email))
        {
            campoEmail.setError(getString(R.string.s_requerimiento).toString());vericar = 1;}

        if (TextUtils.isEmpty(password))
        {campoPassword.setError(getString(R.string.s_requerimiento).toString()); vericar +=  1;}


        vericar += autenticarUser(email, password)?0:1;

        return vericar>0?false:true;
    }


    boolean autenticarUser(String name, String pass)
    {
        boolean autenticado = false;
        int i = 0;
        for(i = 0; (i < usuarioList.size()); i++)
        {
            if(usuarioList.get(i).getEmail().equals(name) && usuarioList.get(i).getPassword().equals(pass))
            {
                autenticado = true;
                usuarioList.get(i).setAutenticado(1);

                    actualizarUserLogueado(usuarioList, i);

            }else{
                usuarioList.get(i).setAutenticado(0);
            }
        }


        if(!autenticado)
        {
            Toast.makeText(activity, "User no valido", Toast.LENGTH_SHORT).show();
        }
        return  autenticado;
    }


    public void actualizarUserLogueado(List<User> listau, int i){

        UserFirebase userFirebase = new  UserFirebase(activity);

        final String name = listau.get(i).getName();
        final String email = listau.get(i).getEmail();
        final String password = listau.get(i).getPassword();
        final String idU = listau.get(i).getId();
        final String tipo = listau.get(i).getTipo();
        final String imagenurl= listau.get(i).getImagen();
        final int autenticado = 1;
        userFirebase.updateUserAutenticado(idU, name, email, password, imagenurl, tipo, autenticado);
    }



}
