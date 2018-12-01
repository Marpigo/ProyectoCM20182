package co.edu.udea.compumovil.gr02_20182.proyecto.Fragment;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import co.edu.udea.compumovil.gr02_20182.proyecto.R;



public class ConfigInsumoFragment extends Fragment {


    Activity activity;
    EditText edi_grupo_linea;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_config_insumo, container, false);
        activity = getActivity();
        init(view);
        cargarPreferencias();

        return  view;
    }


    void init(View view){
        edi_grupo_linea = (EditText)view.findViewById(R.id.ediGrupoLinea);


    }


    @Override
    public void onStop() {
        super.onStop();
        guardarṔreferencias();
    }

    public void guardarṔreferencias(){
        SharedPreferences preferences = activity.getSharedPreferences("configuracionInsumo", Context.MODE_PRIVATE);
        String linea = edi_grupo_linea.getText().toString();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("grupolinea",linea);
        editor.commit();
    }


    public  void cargarPreferencias(){
        SharedPreferences preferences = activity.getSharedPreferences("configuracionInsumo", Context.MODE_PRIVATE);
        String linea = preferences.getString("grupolinea", "Agro insumos, Medicamentos, Sales, Ferreteria"); //segundo parametro es informacion por defecto dado el caso que el archivo preferecnias no exita

        edi_grupo_linea.setText(linea);

    }

}
