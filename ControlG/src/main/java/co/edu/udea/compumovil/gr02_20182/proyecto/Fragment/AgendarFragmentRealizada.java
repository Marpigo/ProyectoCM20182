package co.edu.udea.compumovil.gr02_20182.proyecto.Fragment;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import co.edu.udea.compumovil.gr02_20182.proyecto.Firebase.AgendarFirebase;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Agendar;
import co.edu.udea.compumovil.gr02_20182.proyecto.R;


public class AgendarFragmentRealizada extends Fragment implements View.OnClickListener{


    List<Agendar> recibirListAgenda;


    public static int modo = 0; /*0.Nuevo, 1.Modificar*/

    Activity activity;

    ImageView img_guardar;
    ImageView img_cancelar;


    static String id_actividad="";

    CheckBox che_realizada;
    TextView txt_activiad;
    TextView txt_lote;
    TextView txt_fecha;
    EditText edi_observacion;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agendar_realizada, container, false);
        activity = getActivity();

        id_actividad = getArguments() != null ? getArguments().getString("id_agenda") : "000000";

        iniciarFirebaseAgenda();


        init(view);

        modificarActividad(id_actividad);

        img_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarActividadRealizada();
            }
        });

        img_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiar();
                modo = 0;
                openFragmentRecyclerAgendar();
            }
        });

     return  view;
    }


    void init(View view){
        che_realizada = (CheckBox) view.findViewById(R.id.cheActividadRealizada);
        txt_activiad = (TextView)view.findViewById(R.id.txtActividadreali);
        txt_lote = (TextView)view.findViewById(R.id.txtLoterealizado);
        txt_fecha = (TextView)view.findViewById(R.id.txtFechaRealizada);
        edi_observacion = (EditText) view.findViewById(R.id.ediObservacionRealizada);

        img_guardar= (ImageView) view.findViewById(R.id.imaGuardarAR);
        img_cancelar= (ImageView) view.findViewById(R.id.imaCancelarAR);

    }

    void iniciarFirebaseAgenda()
    {
        recibirListAgenda = AgendarFirebase.agendarList;
    }

    public void guardarActividadRealizada() {
        final String estado_realizada = che_realizada.isChecked()?"1":"0";
        final String observa = edi_observacion.getText().toString();

        final String fecha = txt_fecha.getText().toString();
        final String actividad = txt_activiad.getText().toString();
        final String lote = txt_lote.getText().toString();


        boolean requerimientos = false;
        requerimientos = validateCampo(che_realizada.isChecked(), edi_observacion.getText().toString());
        if (requerimientos) {

             //Modificar
                AgendarFirebase agendarFirebase = new AgendarFirebase(activity);
                agendarFirebase.updateAgendar(id_actividad, fecha, actividad, lote, observa, estado_realizada);
                Toast.makeText(activity, getString(R.string.s_Firebase_registro), Toast.LENGTH_SHORT).show();
                limpiar();
        }
    }

    //(final String id, final String date, final String activity, final String lote, final String observacion, final String estado)

    public void modificarActividad(String id_actividad){

       // Toast.makeText(activity, "id actividad: " + id_actividad + "Tamaño lista: " + recibirListAgenda.size(), Toast.LENGTH_SHORT).show();

        String idac= id_actividad;
        int i =0;
        for(i = 0; i < recibirListAgenda.size(); i++)
        {
           if(recibirListAgenda.get(i).getId().equals(idac))
            {
                txt_activiad.setText(recibirListAgenda.get(i).getActivity());
                txt_fecha.setText(recibirListAgenda.get(i).getDate());
                txt_lote.setText(recibirListAgenda.get(i).getLote());
                edi_observacion.setText(recibirListAgenda.get(i).getObservacion());
            }

        }
    }





    /*
      Validar campos: Vacios o nulo
     */
    boolean validateCampo(Boolean realizada, String observacion) {
        int vericar = 0;

        edi_observacion.setError(null);
        che_realizada.setError(null);
        ConnectivityManager con = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = con.getActiveNetworkInfo();

        if (!che_realizada.isChecked()) {
            che_realizada.setError(getString(R.string.s_requerimiento).toString());
            vericar = 1;
        }

        if (TextUtils.isEmpty(observacion)) {
            edi_observacion.setError(getString(R.string.s_requerimiento).toString());
            vericar += 1;
        }

        if (networkinfo == null && !networkinfo.isConnected()) {
            Toast.makeText(activity, getString(R.string.s_web_not_conexion), Toast.LENGTH_SHORT).show();
            vericar += 1;
        }
        return vericar > 0 ? false : true;
    }

    void limpiar(){

        che_realizada.setError(null);
        edi_observacion.setError(null);

        txt_activiad.setText("");
        txt_lote.setText("");
        txt_fecha.setText("");
        edi_observacion.setText("");
    }

    private void openFragmentRecyclerAgendar() {
        FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new FragmentListAgendarRecycler()).commit();
    }


    @Override
    public void onClick(View view) {

    }
}
