package co.edu.udea.compumovil.gr02_20182.proyecto.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import co.edu.udea.compumovil.gr02_20182.proyecto.R;

public class ConfigurationFragment extends Fragment {

    Activity activity;

    CheckBox che_sesion;
    CheckBox che_notificacion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = getActivity();
        final View view = inflater.inflate(R.layout.fragment_configuration, container, false);

        init(view);

        cargarPreferencias();

        return view;
    }

    void init(View view){
        che_sesion = (CheckBox)view.findViewById(R.id.cheSesion);
        che_notificacion = (CheckBox)view.findViewById(R.id.cheAuthorization_notification);

    }


    //fragmento esta invisible,  y pasa estar parado
    @Override
    public void onStop() {
        super.onStop();
        guardarṔreferencias();
        //Toast.makeText(activity, "Guardar preferencias ", Toast.LENGTH_SHORT).show();
    }

    public void guardarṔreferencias(){
        SharedPreferences preferences = activity.getSharedPreferences("configuracionapp", Context.MODE_PRIVATE);
        int mantener_sesion = che_sesion.isChecked()?1:0;
        int notificacion = che_notificacion.isChecked()?1:0;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("mantener_sesion", mantener_sesion);
        editor.putInt("notificacion", notificacion);
        editor.commit();

    }

    public  void cargarPreferencias(){
        SharedPreferences preferences = activity.getSharedPreferences("configuracionapp", Context.MODE_PRIVATE);
        int mantener_sesion = preferences.getInt("mantener_sesion", 1); //segundo parametro es informacion por defecto dado el caso que el archivo preferecnias no exita
        int notificacion = preferences.getInt("notificacion", 1);
        che_sesion.setChecked(mantener_sesion==1?true:false);
        che_notificacion.setChecked(notificacion==1?true:false);

    }

}

