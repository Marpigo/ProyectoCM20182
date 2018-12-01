package co.edu.udea.compumovil.gr02_20182.proyecto.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import co.edu.udea.compumovil.gr02_20182.proyecto.R;


public class ConfigLevanteFragment extends Fragment {


    EditText edi_grupo_raza;
    EditText edi_grupo_ingreso;
    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_config_levante, container, false);

        init(view);
        activity = getActivity();

        cargarPreferencias();

        return  view;
    }

    void init(View view){
        edi_grupo_raza = (EditText)view.findViewById(R.id.ediGrupoRaza);
        edi_grupo_ingreso = (EditText)view.findViewById(R.id.ediGrupoIngreso);

    }


    @Override
    public void onStop() {
        super.onStop();
        guardarṔreferencias();
    }

    public void guardarṔreferencias(){
        SharedPreferences preferences = activity.getSharedPreferences("configuracionLevante", Context.MODE_PRIVATE);
        String raza = edi_grupo_raza.getText().toString();
        String ingreso = edi_grupo_ingreso.getText().toString();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("gruporaza",raza);
        editor.putString("grupoingreso", ingreso);
        editor.commit();
    }


    public  void cargarPreferencias(){
        SharedPreferences preferences = activity.getSharedPreferences("configuracionLevante", Context.MODE_PRIVATE);
        String raza = preferences.getString("gruporaza", "Brahama, Caqueteño,  Cebu, Casanareño, Chino, Hartón, Lucerna, Red pull, Romosinuano"); //segundo parametro es informacion por defecto dado el caso que el archivo preferecnias no exita
        String ingreso = preferences.getString("grupoingreso", "Consignación, Compra, Nacimiento, Trueque");
        edi_grupo_raza.setText(raza);
        edi_grupo_ingreso.setText(ingreso);


    }

}
