package co.edu.udea.compumovil.gr02_20182.proyecto.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20182.proyecto.Firebase.LevanteFirebase;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Levante;
import co.edu.udea.compumovil.gr02_20182.proyecto.R;


public class AgendarFragment extends Fragment {


    static List<Levante> recibirListLevante;

    Spinner spinner_actividad;
    Spinner spinner_lote;

    Activity activity;
    ArrayList<String> comboActividad =new ArrayList<>();
    ArrayList<String> comboLote =new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity = getActivity();

        View view;
        view = inflater.inflate(R.layout.fragment_agendar, container, false);

        init(view);

        iniciarFirebaseListLevante();
        llenarActividad();
        llenarLote();

        return  view;
    }

    void init(View view)
    {

        spinner_actividad = (Spinner) view.findViewById(R.id.spinnerActividadA);
        spinner_lote = (Spinner) view.findViewById(R.id.spinnerLoteA);
    }

    void iniciarFirebaseListLevante()
    {
        LevanteFirebase levanteFirebase = new LevanteFirebase();
        levanteFirebase.limpiarLista();
        levanteFirebase.cargarListLevante();
        recibirListLevante = LevanteFirebase.levanteList;
    }


    void llenarActividad(){
        /*LLenado del Spinner Genero*/
        comboActividad.add("Vacunación");
        comboActividad.add("Curación");
        comboActividad.add("Pesaje");
        comboActividad.add("Cambio de pasto");
        comboLLenado(spinner_actividad, comboActividad);
    }

    void llenarLote(){
        /*LLenado del Spinner Genero*/
        String lot = "";
        String auxi ="";
        int i=0;
        int size = recibirListLevante.size();

        for (i=0; i<size; i++){

            lot = recibirListLevante.get(i).getLote();

            comboLote.add(lot);

            if (!lot.equals(auxi))
            {
                comboLote.add(lot);
            }
            auxi= recibirListLevante.get(i).getLote();
        }

        comboLLenado(spinner_lote, comboLote);
    }


    void comboLLenado(Spinner spinner, ArrayList<String> combobox)
    {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, combobox);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //String xx = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }



}
