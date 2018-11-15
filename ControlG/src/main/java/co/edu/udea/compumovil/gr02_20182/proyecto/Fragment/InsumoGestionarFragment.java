package co.edu.udea.compumovil.gr02_20182.proyecto.Fragment;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.edu.udea.compumovil.gr02_20182.proyecto.Firebase.InsumoFirebase;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Insumo;
import co.edu.udea.compumovil.gr02_20182.proyecto.R;

public class InsumoGestionarFragment extends Fragment {

    static List<Insumo> recibirListInsumo;
    InsumoFirebase insumoFirebase = new InsumoFirebase();




    public static int modo = 0; /*0.Nuevo, 1.Modificar*/

    //Gestionar Levante
    ImageView img_guardar;
    ImageView img_nuevo;
    ImageView img_borrar;


    EditText insumo_edi;
    EditText presentacion_edi;
    EditText marca_edi;
    Spinner spinner_especies;
    Spinner spinner_linea;

    Activity activity;
    ArrayList<String> comboSpecie =new ArrayList<>();
    ArrayList<String> comboLinea =new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        activity = getActivity();

        View view;
        view = inflater.inflate(R.layout.fragment_insumo_gestionar, container, false);

        init(view);

        llenarEspecie();
        llenarLinea();

        img_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                java.util.Date fecha = new Date();

                final String insum = insumo_edi.getText().toString();
                final String presenta = presentacion_edi.getText().toString();
                final String marc = marca_edi.getText().toString();
                final String line = spinner_linea.getSelectedItem().toString();
                final String espec = spinner_especies.getSelectedItem().toString();
                final String cantidad = "0"; //cantidad del insumo solo se gestiona
                final String fecha_actual = fecha.toString();


                boolean requerimientos = false;
                requerimientos = validateCampo(insumo_edi.getText().toString(), presentacion_edi.getText().toString(), marca_edi.getText().toString());
                if (requerimientos) {
                    if (modo == 0 ){ //Nuevo
                       insumoFirebase.insertInsumo(insum, presenta, line, marc, espec, cantidad, fecha_actual);
                        Toast.makeText(activity, getString(R.string.s_Firebase_registro), Toast.LENGTH_SHORT).show();
                        limpiar();
                    }else if(modo == 1){ //Modificar
                        //levanteFirebase.updateLevante(insum, presenta, line, marc, espec, cantidad, fecha_actual);
                        Toast.makeText(activity, getString(R.string.s_Firebase_registro), Toast.LENGTH_SHORT).show();
                        limpiar();
                    }

                }
            }
        });

        img_nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        img_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiar();
            }
        });


        return  view;
    }


    void init(View view)
    {
        insumo_edi = (EditText) view.findViewById(R.id.ediInsumoI);
        presentacion_edi= (EditText) view.findViewById(R.id.ediPresentacionI);
        spinner_linea = (Spinner) view.findViewById(R.id.spinnerLineaI);
        marca_edi = (EditText) view.findViewById(R.id.ediMarcaI);
        spinner_especies = (Spinner) view.findViewById(R.id.spinnerEspecieI);

        img_guardar=(ImageView)view.findViewById(R.id.imaSaveI);
        img_borrar=(ImageView) view.findViewById(R.id.imaDeleteI);
        img_nuevo=(ImageView) view.findViewById(R.id.imaNewI);
    }

    void llenarEspecie(){
        /*LLenado del Spinner Genero*/
        comboSpecie.add("Bovino");
        comboSpecie.add("Equino");
        comboLLenado(spinner_especies, comboSpecie);
    }

    void llenarLinea(){
        /*LLenado del Spinner Genero*/
        comboLinea.add("Medicamentos");
        comboLinea.add("Sales");
        comboLinea.add("Ferreteria");
        comboLLenado(spinner_linea, comboLinea);
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


    void eliminarInsumo(String id)
    {
        insumoFirebase.deleteInsumo(id);
    }


    /*
      Validar campos: Vacios o nulo
     */
    boolean validateCampo (String insumo, String presentacion, String marca){
        int vericar = 0;


        insumo_edi.setError(null);
        presentacion_edi.setError(null);
        marca_edi.setError(null);

        ConnectivityManager con = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = con.getActiveNetworkInfo();


        if (TextUtils.isEmpty(insumo))
        {insumo_edi.setError(getString(R.string.s_requerimiento).toString());vericar = 1;}

        if (TextUtils.isEmpty(presentacion))
        {presentacion_edi.setError(getString(R.string.s_requerimiento).toString()); vericar += 1;}

        if (TextUtils.isEmpty(marca))
        {marca_edi.setError(getString(R.string.s_requerimiento).toString()); vericar += 1;}


        if(networkinfo == null && !networkinfo.isConnected()){
           Toast.makeText(activity, getString(R.string.s_web_not_conexion), Toast.LENGTH_SHORT).show();
            vericar += 1;
        }
        return vericar>0?false:true;
    }


    private void limpiar() {
        insumo_edi.setError(null);
        presentacion_edi.setError(null);
        marca_edi.setError(null);

        insumo_edi.setText("");
        presentacion_edi.setText("");
        marca_edi.setText("");
    }


}
