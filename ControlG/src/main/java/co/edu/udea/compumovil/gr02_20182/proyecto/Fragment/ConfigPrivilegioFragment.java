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
import android.widget.Switch;

import co.edu.udea.compumovil.gr02_20182.proyecto.R;


public class ConfigPrivilegioFragment extends Fragment {




    Switch swi_levante_usuario;
    Switch swi_insumo_usuario;
    Switch swi_configuracion_usuario;

    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_config_privilegio, container, false);
        activity = getActivity();

        init(view);
        cargarPreferencias();

        return  view;
    }


    void init(View view){
        swi_levante_usuario= (Switch) view.findViewById(R.id.swiLevanteUsuario);
        swi_insumo_usuario= (Switch) view.findViewById(R.id.swiInsumoUsuario);
        swi_configuracion_usuario= (Switch) view.findViewById(R.id.swiConfiguracionUsuario);
    }


    @Override
    public void onStop() {
        super.onStop();

        guardarṔreferencias();
    }

    public void guardarṔreferencias(){
        SharedPreferences preferences = activity.getSharedPreferences("configuracionPrivilegio", Context.MODE_PRIVATE);
        Boolean levanteUs = swi_levante_usuario.isChecked()?true:false;
        Boolean insmoUs = swi_insumo_usuario.isChecked()?true:false;
        Boolean configuUs = swi_configuracion_usuario.isChecked()?true:false;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("levanteUs", levanteUs);

        editor.putBoolean("insumoUs", insmoUs);

        editor.putBoolean("configuracionUs", configuUs);

        editor.commit();
    }


    public  void cargarPreferencias(){
        SharedPreferences preferences = activity.getSharedPreferences("configuracionPrivilegio", Context.MODE_PRIVATE);

        Boolean levanteUs =  preferences.getBoolean("levanteUs", true); //segundo parametro es informacion por defecto dado el caso que el archivo preferecnias no exita

        Boolean insmoUs  =  preferences.getBoolean("insumoUs", true);

        Boolean configuUs  =  preferences.getBoolean("configuracionUs", true);


        swi_levante_usuario.setChecked(levanteUs);
        swi_insumo_usuario.setChecked(insmoUs);
        swi_configuracion_usuario.setChecked(configuUs);

    }

}
