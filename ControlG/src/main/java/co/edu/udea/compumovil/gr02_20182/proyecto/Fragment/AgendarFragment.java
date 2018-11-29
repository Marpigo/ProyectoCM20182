package co.edu.udea.compumovil.gr02_20182.proyecto.Fragment;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.edu.udea.compumovil.gr02_20182.proyecto.Firebase.AgendarFirebase;
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

    CalendarView calendarView;
    TextView txt_fecha;
    EditText edi_observacion;

    ImageView img_cancelar;
    ImageView img_guardar;

    String id_actividad;
    AgendarFirebase agendarFirebase = new AgendarFirebase();

    public static int modo = 0; /*0.Nuevo, 1.Modificar*/

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


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                String date = (i1+1) +"/" +  i2 + "/" + i;
                txt_fecha.setText(date);
            }
        });


        img_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agendaGuardar();
            }
        });


        img_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    menuRegresar();
          //      botonFlotanteMenu(true);
                limpiar();
                modo = 0;
            }
        });



        return  view;
    }

    void init(View view)
    {
        calendarView = (CalendarView) view.findViewById(R.id.calendarViewAgend);
        txt_fecha = (TextView) view.findViewById(R.id.texFechaA);
        edi_observacion= (EditText) view.findViewById(R.id.editObservacionAg);

        spinner_actividad = (Spinner) view.findViewById(R.id.spinnerActividadA);
        spinner_lote = (Spinner) view.findViewById(R.id.spinnerLoteA);

        img_guardar= (ImageView) view.findViewById(R.id.imaGuardarA);
        img_cancelar= (ImageView) view.findViewById(R.id.imaCancelarA);
    }


    void iniciarFirebaseListLevante()
    {
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
        //Collections.sort(recibirListLevante,Collections.<Levante>reverseOrder() );/// INDICAMOS QUE ORDENE DE MAYOR A MENOR, DESCENDETE


        ArrayList<String> listLote=new ArrayList<>();
        for (i=0; i<size; i++){
            listLote.add(recibirListLevante.get(i).getLote());
        }

        Set<String> hs = new HashSet<>(); //Quitar lotes repetido con HashSet no admite repetido
        hs.addAll(listLote);
        listLote.clear();
        comboLote.addAll(hs);
        Collections.sort(comboLote);/*Ordenar lista*/
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


    public void agendaGuardar(){
        final String fecha = txt_fecha.getText().toString();
        final String actividad = spinner_actividad.getSelectedItem().toString();
        final String lote = spinner_lote.getSelectedItem().toString();
        final String observacion = edi_observacion.getText().toString();
        java.util.Date fechaActual = new Date();
        final String estado = "0"; //0. sin realizar la actividad, 1.Actividad realizada

        boolean requerimientos = false;
        requerimientos = validateCampo(txt_fecha.getText().toString(), edi_observacion.getText().toString());
        if (requerimientos) {
            if (modo == 0 ){ //Nuevo
                agendarFirebase.inserAgendar(fecha, actividad, lote, observacion, estado);
                Toast.makeText(activity, getString(R.string.s_Firebase_registro), Toast.LENGTH_SHORT).show();
                limpiar();
            }else if(modo == 1){ //Modificar
                agendarFirebase.updateAgendar(id_actividad, fecha, actividad, lote, observacion, estado);
                Toast.makeText(activity, getString(R.string.s_Firebase_registro), Toast.LENGTH_SHORT).show();
                limpiar();
            }
        }
    }


    /*
     Validar campos: Vacios o nulo
    */
    boolean validateCampo (String fecha, String observacion){
        int vericar = 0;

        edi_observacion.setError(null);
        txt_fecha.setError(null);

        ConnectivityManager con = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = con.getActiveNetworkInfo();

        if (TextUtils.isEmpty(observacion))
        {edi_observacion.setError(getString(R.string.s_requerimiento).toString());vericar = 1;}

        if (fecha.equals("Fecha"))
        {txt_fecha.setError("Fecha"); vericar += 1;}

        if(networkinfo == null && !networkinfo.isConnected()){
            Toast.makeText(activity, getString(R.string.s_web_not_conexion), Toast.LENGTH_SHORT).show();
            vericar += 1;
        }
        return vericar>0?false:true;
    }



    private void limpiar() {
        txt_fecha.setText("Fecha");
        edi_observacion.setError(null);
        edi_observacion.setText("");
    }




}
