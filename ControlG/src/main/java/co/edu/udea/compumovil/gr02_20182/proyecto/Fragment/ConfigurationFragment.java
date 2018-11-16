package co.edu.udea.compumovil.gr02_20182.proyecto.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.edu.udea.compumovil.gr02_20182.proyecto.R;

public class ConfigurationFragment extends Fragment {

    private FragmentManager fragManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragManager = getFragmentManager();

        return inflater.inflate(R.layout.fragment_configuration, container, false);
    }


    private void openFragmentUsuairoGestionar() {
        android.support.v4.app.FragmentManager fm = fragManager;
        fm.beginTransaction().replace(R.id.fragmentContainers, new UsuarioFragment()).commit();
    }



}
